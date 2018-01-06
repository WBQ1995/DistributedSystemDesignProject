package Sequencer;

import java.util.HashMap;

public class RequestBuffer {

	private HashMap<Integer, String> RequestMap = new HashMap<>();
	
	
	
	public void PutRequest(int SequenceNum, String Request){
		RequestMap.put(SequenceNum, Request);
	}
	
	public String GetRequest(int SequenceNum){
		String ret = " ";
		if(RequestMap.containsKey(SequenceNum))
			ret = RequestMap.get(SequenceNum);
		return ret;
	}
	
	public boolean Exist(int SequenceNum){
		
		if(RequestMap.containsKey(SequenceNum))
			return true;
		else
			return false;
	}
	
}
