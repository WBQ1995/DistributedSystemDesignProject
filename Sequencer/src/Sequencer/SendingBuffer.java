package Sequencer;

import java.util.ArrayList;

public class SendingBuffer {
	
	 private ArrayList<String> SendingID = new ArrayList<>();
	 
	 public void PutSending(String ID){	 
		 SendingID.add(ID);
	 }
	 
	 public Boolean Exist(String ID){
		 if(SendingID.contains(ID))
			 return true;
		 else
			 return false;
	 }
	 

}
