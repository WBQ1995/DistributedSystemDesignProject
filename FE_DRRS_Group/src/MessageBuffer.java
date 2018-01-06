import java.util.ArrayList;
import java.util.Iterator;


public class MessageBuffer {
	
	/**
	  *  @fieldname: messageBuffer
	  *  @description: save the messages temporarily
	*/
	private ArrayList<String> messageBuffer;
	
	
	public MessageBuffer() {
		messageBuffer = new ArrayList<String>();
	}

	/**
	  *  @name: put
	  *  @description: put the received message in the messageBuffer
	  *  @param: message-String, the message that received by UDP listening
	  *  @return: void
	  *  @notice: None
	  */
	public void put(String message) {
		messageBuffer.add(message);
	}
	 
	/**
	  *  @name: get
	  *  @description: return all the messages that start with sendingNum
	  *  @param: aimRequestID-String, the no. that assigned when request was sending
	  *  @return: message-String[]
	  *  @notice: None
	  */
	
	public String get(String aimRequestID) {
		
		for(String each : messageBuffer) {
			
			int startpos = each.indexOf("REQ");
			int endpos = each.indexOf(" ", startpos);
			
			String requestID = each.substring(startpos, endpos);
			if ( requestID.equals(aimRequestID)) {
				return each;
			}
		};
		
		return null;
	}
	
	

	/**
	  *  @name: remove
	  *  @description: remove the messages that start with sendingNum
	  *  @param: sendingNum-String, the no. that assigned when request was sending
	  *  @return: message-String
	  *  @notice: None
	  */
	public String getAndRemove(int aimRequestNum) {
		
		Iterator<String> it = messageBuffer.iterator();
		
		while (it.hasNext()) {
		
			String each = it.next();
			System.out.println("each");
			
			int startpos = each.indexOf("REQ") + 3;
			int endpos = each.indexOf(" ", startpos);
			
			System.out.println(each.substring(startpos, endpos));
			
			int requestNum = Integer.valueOf(each.substring(startpos, endpos));
			if ( requestNum == aimRequestNum) {
				it.remove();
				return each;
			}
		
		}
		
		return null;
		
	}
	
	public String toString() { 
		return messageBuffer.toString();
	}
	

}
