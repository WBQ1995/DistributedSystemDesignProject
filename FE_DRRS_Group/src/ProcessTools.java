import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Address.AddressMap;
import Message.FErequest;
import Message.Message;
import Message.MistakeToRM;
import Message.ReplicaReply;


public class ProcessTools {
	
	private AddressMap ad;
	private MessageBuffer requestBuffer;
	public static HashMap<String, String> latedreplies;
	public static Lock latedreplieslock;
	
	
	static {
		latedreplies = new HashMap<String, String>();
		latedreplieslock = new ReentrantLock();
	}

	
	public ProcessTools() {
		ad = new AddressMap();
		requestBuffer = new MessageBuffer();
	}
	
	
	public String sendAndReceive(FErequest feRequst_message){
		
		String requestID = feRequst_message.getrequestID();
		String result = new String();
		
		try {
			//build the UDP connection and send the message;
			UDPConnection udpLink = new UDPConnection(ad.get("sequencer").getIp(), ad.get("sequencer").getPort());
			udpLink.Send(feRequst_message);
			requestBuffer.put(feRequst_message.pack());
			
			
			long startTime = System.currentTimeMillis(); 
			
			ArrayList<ReplicaReply> replies = new ArrayList<ReplicaReply>();
			
			while(true) {
				
				replies = getReplies(requestID, replies);
				
				if (replies != null && replies.size() == 3) {
					//System.out.println("replies size:" + replies.size());
					result = compareAndAction(replies);
					return result;
				}
				else {
					long interval = System.currentTimeMillis() - startTime; 
					if (interval > 5000) {
						result = insufficientCompareAndAction(replies, requestID);
						return result;
					}
					else {
						Thread.sleep(100);
					}
				}
	
			} 
		}catch (InterruptedException e) {
					e.printStackTrace();
		}
		return result;
		
	}


	private ArrayList<ReplicaReply> getReplies(String requestID, ArrayList<ReplicaReply> replies ) {
		
		try{
			//look the peek of three priory queue, get the correspond replies
			
			ListenThread.replieslock.lock();
			if(ListenThread.repliesOne.peek() != null) {
				String req = ListenThread.repliesOne.peek().getRequestID();
				if (req.equals(requestID)) {
					replies.add(ListenThread.repliesOne.peek());
					ListenThread.repliesOne.remove();
				}
			}
			
			if(ListenThread.repliesTwo.peek() != null) {
				String req = ListenThread.repliesTwo.peek().getRequestID();
				if (req.equals(requestID)) {
					replies.add(ListenThread.repliesTwo.peek());
					ListenThread.repliesTwo.remove();
				}
			}
			
			
			if(ListenThread.repliesThree.peek() != null) {
				String req = ListenThread.repliesThree.peek().getRequestID();
				if (req.equals(requestID)) {
					replies.add(ListenThread.repliesThree.peek());
					ListenThread.repliesThree.remove();
				}
			}
	
		}finally {
				ListenThread.replieslock.unlock();
		}			
		return replies;
	}


	private String compareAndAction(ArrayList<ReplicaReply> replies) {
		
		String result0 = replies.get(0).getReturnVal();
		String result1 = replies.get(1).getReturnVal();
		String result2 = replies.get(2).getReturnVal();
		
		
		//get needed parameters
		//System.out.println("key is this request ID:    " +replies.get(0).getRequestID());
		String req = replies.get(0).getRequestID();
		String campus_id = requestBuffer.get(req).substring(0, 3);
		
			if (result0.equals(result1) && result0.equals(result2)) { 
				System.out.println("the right result is :" + result0);
				return result0;
			}
			
			if (!result0.equals(result1) && !result0.equals(result2) && !result1.equals(result2)) {
				System.out.println("different result in 3 replica!");
				return "different result in 3 replica!";	
			}
		
		
			if (result0.equals(result1) && !result0.equals(result2)) {
				String replicaID = replies.get(2).getReplicaID();
				UDPConnection udpLink = new UDPConnection(ad.get(replicaID).getIp(), ad.get(replicaID).getPort());
				Message misInfo = new MistakeToRM(replies.get(2).getSeq_num(), campus_id, false);
				udpLink.Send(misInfo);
			
				System.out.println("the right result is :" + result0);
				return result0;	
			}
		
			if (result0.equals(result2) && !result1.equals(result2)) {
				String replicaID = replies.get(1).getReplicaID();
				UDPConnection udpLink = new UDPConnection(ad.get(replicaID).getIp(), ad.get(replicaID).getPort());
				Message misInfo = new MistakeToRM(replies.get(1).getSeq_num(), campus_id, false);
				udpLink.Send(misInfo);
			
				System.out.println("the right result is :" + result2);
				return result2;
			}
		
			if (result1.equals(result2) && !result1.equals(result0)) {
				String replicaID = replies.get(0).getReplicaID();
				UDPConnection udpLink = new UDPConnection(ad.get(replicaID).getIp(), ad.get(replicaID).getPort());
				Message misInfo = new MistakeToRM(replies.get(0).getSeq_num(), campus_id, false);
				udpLink.Send(misInfo);
			
				System.out.println("the right result is :" + result1);
				return result1;
			}
		
		return "";
	}
	
	private String insufficientCompareAndAction(ArrayList<ReplicaReply> replies, String requstID) {
		
		System.out.println("insufficient Compare And Action start: " + replies.size());
		
			switch (replies.size()) {
			case 0:
				return "Time out, get nothing!!";
			
			case 1:
				String result = replies.get(0).getReturnVal();
				
				try {
					latedreplieslock.lock();
					latedreplies.put(requstID, result);	
				}finally{
					latedreplieslock.unlock();
				}
				
				return result;
				
				
			case 2:
				result = replies.get(0).getReturnVal();
				String result2 = replies.get(1).getReturnVal();
				if (result.equals(result2)) {
					try {
						latedreplieslock.lock();
						latedreplies.put(requstID, result);	
					}finally{
						latedreplieslock.unlock();
					}
					return result;
				}
				break;
			}
		
		return "";
	}
	
}

