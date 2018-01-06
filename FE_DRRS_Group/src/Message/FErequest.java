package Message;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;


public class FErequest extends Message {
	
	private static HashMap<RequestType, String> requestMap;
	private String campusID;
	private String requestID;
	private String functionName;
	private ArrayList<String> para;
	
	static {
		requestMap = new HashMap<RequestType, String>();
		requestMap.put(RequestType.Login, "login");
		requestMap.put(RequestType.Book, "bookRoom");
		requestMap.put(RequestType.CancelBook, "cancelBook");
		requestMap.put(RequestType.AddRecord, "createRoom");
		requestMap.put(RequestType.DeleteRecord, "deleteRoom");
		requestMap.put(RequestType.GetAvailTimeSlot, "getAvailableTimeslot");
		requestMap.put(RequestType.ChangeReservation, "changeReservation");
	}
	
	

	public FErequest(DatagramPacket packet) {
		
		super(packet.getAddress(), packet.getPort());
		String message = new String(packet.getData(), 0, packet.getLength());
		getValueFromString(message);
		
	}
	
	public FErequest(String message) {
		getValueFromString(message);
	}
	
	private void getValueFromString(String message) {
		
		String parts[] = message.split("\\s+");
		if(parts.length < 4)
			return;
		
		campusID = parts[0];
		requestID = parts[1];
		functionName = parts[2];
		para = new ArrayList<String>();
		for(int i=3; i<parts.length; i++) {
			para.add(parts[i]);
		}
	}
	
	public FErequest(String campus_id, int requstNum, RequestType funcType, ArrayList<String> parameters){
		
		campusID = campus_id;
		requestID = "REQ" + String.valueOf(requstNum);
		functionName = requestMap.get(funcType);
		para = parameters;
		
	}
	
	
	public String paraToString(ArrayList<String> parameters) {
		
		String returnVal = "";
		for(String each : parameters) 
			returnVal = returnVal + each + " ";
		return returnVal.trim();
		
	}
	
	@Override
	public String pack() {
		
		String ret = campusID + " " + requestID + " " + functionName + " " + paraToString(para);
		return ret;
	}
	
	public String getrequestID() {
		return requestID;
	}

}
