package Message;


import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Comparator;

public class ReplicaReply extends Message  implements Comparable<ReplicaReply>{
	private int seq_num;
	private String replicaID;
	private String requestID;
	private String returnVal;
	
	public ReplicaReply(DatagramPacket packet) {
		super(packet.getAddress(), packet.getPort());
		String message = new String(packet.getData(), 0, packet.getLength());
		getValueFromString(message);
	}
	
	public ReplicaReply(String message) {
		getValueFromString(message);
	}
	
	private void getValueFromString(String message) {
		String parts[] = message.split("\\s+");
		if(parts.length < 3)
			return;
		
		seq_num = Integer.valueOf(parts[0]);
		replicaID = parts[1];
		requestID = parts[2];
		returnVal = "";
		for(int i=3; i<parts.length; i++) {
			returnVal = returnVal + parts[i];
			if(i != parts.length-1)
				returnVal += " ";
		}
	}
	
	public ReplicaReply(int seq, String replica_id, String request_id, ArrayList<Boolean> strs){
		seq_num = seq;
		replicaID = replica_id;
		requestID = request_id;
		setReply(strs);
	}
	
	public ReplicaReply(int seq, String replica_id, String request_id, String str){
		seq_num = seq;
		replicaID = replica_id;
		requestID = request_id;
		setReply(str);
	}
	
	public void setReply(String str) {
		returnVal = str;
	}
	public void setReply(ArrayList<Boolean> strs) {
		returnVal = "";
		for(Boolean item:strs) 
			returnVal = returnVal + item.toString() + " ";
	}
	
	@Override
	public String pack() {
		String ret = seq_num + " " + replicaID + " " + requestID + " " + returnVal;

		return ret;
	}
	
	public String getReturnVal() {
		return returnVal;
	}
	
	public String getReplicaID() {
		return replicaID;
	}
	
	public String getRequestID() {
		return requestID;
	}
	
	public int getSeq_num() {
		return seq_num;
	}



	@Override
	public int compareTo(ReplicaReply AnotherReply) {
		
		int result = this.getSeq_num() > AnotherReply.getSeq_num() ? 1 :(this.getSeq_num() == AnotherReply.getSeq_num() ? 0 : -1);  
	    return (result);

	}

}