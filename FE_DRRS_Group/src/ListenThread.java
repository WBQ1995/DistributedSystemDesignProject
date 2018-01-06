

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.PriorityQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Message.ReplicaReply;

public class ListenThread extends Thread {
	private Thread t;
	private String threadName;
	private DatagramSocket FESocket;
	public static PriorityQueue<ReplicaReply> repliesOne;
	public static PriorityQueue<ReplicaReply> repliesTwo;
	public static PriorityQueue<ReplicaReply> repliesThree;
	public static Lock replieslock;
	   
	
	static {
		repliesOne = new PriorityQueue<ReplicaReply>(100);
		repliesTwo = new PriorityQueue<ReplicaReply>(100);
		repliesThree = new PriorityQueue<ReplicaReply>(100);
		replieslock = new ReentrantLock();
	}
	
	public ListenThread(String name, DatagramSocket FESocket) {
	      threadName = name;
	      this.FESocket = FESocket;

	   }
	   
	   public void run() {
	      System.out.println( threadName + " Running ");

	      try {
	    	  	// build a listening thread;
	    	  	byte[] buffer = new byte[1000];
	    	  		
			 	while(true){
			 		
			 		DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			 		FESocket.receive(request);
			 		
			 		//System.out.println("Request getlength: " + request.getLength());
			 		
			 		byte[] newBuffer = new byte[request.getLength()];
			 		System.arraycopy(buffer, 0, newBuffer, 0, request.getLength());
			 		
			 		//analyze the request, and get the appropriate methods;
			 		String message = new String(newBuffer).trim();
			 		System.out.println("received the message  " + message);
			 		
			 		//FEImpl.replyBuffer.put(message);
			 		//System.out.println(FEImpl.replyBuffer);
			 		
			 		ReplicaReply reply = new ReplicaReply(message);
			 		String replicaID = reply.getReplicaID();
			 		String req = reply.getRequestID();
			 		
			 		try{
			 			ProcessTools.latedreplieslock.lock();
			 			boolean contains = ProcessTools.latedreplies.containsKey(req);
			 			if (contains) {
			 				System.out.println("Get lated message!!");
			 				continue;
			 			}
			 		}finally {
			 			ProcessTools.latedreplieslock.unlock();
			 		}
			 		

			 		try{
		 				replieslock.lock();
		 				if (replicaID.equals("Replica_1")) {
		 					repliesOne.add(reply);
		 				}
			 		
		 				if (replicaID.equals("Replica_2")) {
		 					repliesTwo.add(reply);
		 				}
			 		
		 				if (replicaID.equals("Replica_3")) {
		 					repliesThree.add(reply);
		 				}
			 		}finally {
			 			replieslock.unlock();
			 		}
			 		
		 			
			 	}
	        
	      }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
	      }catch (IOException e) {System.out.println("IO: " + e.getMessage());
	      }finally {if(FESocket != null) FESocket.close();
	      }
	   }
	   

	public void start () {
		
		if (t == null) {
	    		t = new Thread (this, threadName);
	    		t.start ();
	      }
	 }

}
