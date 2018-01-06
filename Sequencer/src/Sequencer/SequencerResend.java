package Sequencer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

import tools.FErequest;
import tools.Message;
import tools.ReplicaReply;
import tools.ResendRequest;
import tools.SeqRequest;
import tools.UDPConnection;

public class SequencerResend extends Thread{
	
	private int SeqNumber;
	private ArrayList<String> HostAddr = new ArrayList<>();
	private RequestBuffer requestBuffer;

	
	private  FErequest fe;
	private  ResendRequest rp;
	
	static int resendport;
	static String resendaddress;
	
	private static int DVL_Port = 13320;
	private static int KKL_Port = 13321;
	private static int WST_Port = 13322;
	
	public SequencerResend(int SequenceNumber,RequestBuffer requestBuffer,FErequest fe,ResendRequest rp) {
		
		this.fe = fe;
		this.rp = rp;
		
		this.requestBuffer = requestBuffer;
		
		SeqNumber = SequenceNumber;
		String host1Addr = "172.20.10.14";
		String host2Addr = "172.20.10.13";
		String host3Addr = "172.20.10.3";
		HostAddr.add(host1Addr);
		HostAddr.add(host2Addr);
		HostAddr.add(host3Addr);
		
	}
	
	public void SendtoWorker(FErequest ferequest){
		
	
		String ID = ferequest.getrequestID();
		String Campus = " ";
		int port = -1;
		
		Campus = ferequest.GetCampus();
		if(Campus.equals("DVL"))
			port = DVL_Port;
		else if(Campus.equals("KKL"))
			port = KKL_Port;
		else if(Campus.equals("WST"))
			port = WST_Port;
		else
			return;
		
		//ferequest.setSeqNum(SeqNum);
		String message = ferequest.pack();
		String sendmessage = Integer.toString(SeqNumber) + " " + message.substring(4);
		
		Message s = new SeqRequest(sendmessage);
		
		for(int i = 0; i < HostAddr.size();i ++){
			if(i== 0 && SeqNumber%2==0)
				continue;
			UDPConnection udpsender = new UDPConnection(HostAddr.get(i),port);
			//System.out.println(port);
			udpsender.Send(s);
		}
		
		System.out.println("Send to replica:" + " " + s.pack());
		System.out.println();
	
	
	}
	
	public void ResendtoWorker(ResendRequest resendRequest){
	
		if(requestBuffer.Exist(SeqNumber)){
			String message = requestBuffer.GetRequest(SeqNumber);

			Message s = new SeqRequest(message);
			
			
			UDPConnection udpsender = new UDPConnection(resendaddress,resendport);
			udpsender.Send(s);
			
			System.out.println("Rensend to replica:" + " " + s.pack());
			System.out.println();
		}
		else{
			System.out.println("No such Sequence Number!");
			System.out.println();
		}
		
	
	}
	
	@Override
	public void run(){
		
		if(fe!=null){
			//System.out.println("got it!");
			SendtoWorker(fe);
		}
		if(rp!= null){
			//System.out.println("got it!");
			ResendtoWorker(rp);
		}
		
				
	}
	
	public static void main(String[] args) throws SocketException, InterruptedException {
		
		Lock lock = new ReentrantLock();	
		int seqDVL = 1;
		int seqKKL = 1;
		int seqWST = 1;
		RequestBuffer requestBufferDVL = new RequestBuffer();
		RequestBuffer requestBufferKKL = new RequestBuffer();
		RequestBuffer requestBufferWST = new RequestBuffer();
		SendingBuffer sendingBuffer = new SendingBuffer();
		
		FErequest fe = null;
		ResendRequest rp = null;
		
		int outerPort = 13370;
		
		System.out.println("Sequencer is running!");
		System.out.println();
		
		DatagramSocket socket = new DatagramSocket(outerPort);
		
		while(true){
			
			UDPConnection udp = new UDPConnection();
			DatagramPacket packet = udp.ReceivePacket(socket);
			
			if(packet == null)
				continue;
			
			String receivedata = new String(packet.getData(), 0, packet.getLength());
			
			if(receivedata.startsWith("DVL") || receivedata.startsWith("KKL") || receivedata.startsWith("WST")){
				
				System.out.println("Receiveing from FE:" + " " + receivedata);
				System.out.println();
		
				String requestID = receivedata.split(" ")[1];
				
				if(!sendingBuffer.Exist(requestID)){
					try {
						lock.lock();
						if(receivedata.startsWith("DVL")){
							String sendmessage = Integer.toString(seqDVL) + " " + receivedata.substring(4);
							sendingBuffer.PutSending(requestID);
							requestBufferDVL.PutRequest(seqDVL, sendmessage);
							FErequest message = new FErequest(packet);	
							SequencerResend sequencer = new SequencerResend(seqDVL, requestBufferDVL, message, rp);
							sequencer.start();
							seqDVL ++;
						}
						else if(receivedata.startsWith("KKL")){
							String sendmessage = Integer.toString(seqKKL) + " " + receivedata.substring(4);
							sendingBuffer.PutSending(requestID);
							requestBufferKKL.PutRequest(seqKKL, sendmessage);
							FErequest message = new FErequest(packet);
							SequencerResend sequencer = new SequencerResend(seqKKL, requestBufferKKL, message, rp);
							sequencer.start();
							seqKKL ++;
						}
						else if(receivedata.startsWith("WST")){
							String sendmessage = Integer.toString(seqWST) + " " + receivedata.substring(4);
							sendingBuffer.PutSending(requestID);
							requestBufferWST.PutRequest(seqWST, sendmessage);
							FErequest message = new FErequest(packet);
							SequencerResend sequencer = new SequencerResend(seqWST, requestBufferWST, message, rp);
							sequencer.start();
							seqWST ++;
						}						
					} finally {
						lock.unlock();
					}		
				}
				else{
					System.out.println("Already sent to replica!");
					System.out.println();
				}
					
			}
			
			else{			
				
				System.out.println("Receiveing from replica:" + " " + receivedata);
				System.out.println();
				
				int resendnum = Integer.parseInt(receivedata.substring(0, 1));
				
				ResendRequest request = new ResendRequest(packet);
				String resnedCampus = request.getCampus();
				String resnedReplica = request.getReplicaID();
				if(resnedCampus.equals("DVL"))
					resendport = 13320;
				else if(resnedCampus.equals("KKL"))
					resendport = 13321;
				else if(resnedCampus.equals("WST"))
					resendport = 13322;
				
				
				resendaddress  = packet.getAddress().toString().substring(1);
				
				System.out.println("src IP: " + resendaddress);
				System.out.println("");
				
//				System.out.println(resendaddress);
//				System.out.println(resendport);
				
				ResendRequest message = new ResendRequest(packet);
				
				if(receivedata.split(" ")[2].equals("DVL")){
					SequencerResend sequencer = new SequencerResend(resendnum, requestBufferDVL, fe, message);
					sequencer.start();
				}
				else if(receivedata.split(" ")[2].equals("KKL")){
					SequencerResend sequencer = new SequencerResend(resendnum, requestBufferKKL, fe, message);
					sequencer.start();	
				}
				else if(receivedata.split(" ")[2].equals("WST")){
					SequencerResend sequencer = new SequencerResend(resendnum, requestBufferWST, fe, message);
					sequencer.start();	
				}

			}
		}
	}

}
