import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.omg.CORBA.ORB;
import FEAPP.FEPOA;
import Message.FErequest;
import Message.RequestType;


public class FEImpl extends FEPOA {
	
	private ORB orb;
	private AtomicInteger req_Num;
	private ProcessTools tools;

	
	public void setORB(ORB orb_val) {
		orb = orb_val;
	}
	
	public FEImpl() {
		req_Num = new AtomicInteger(0);
		tools = new ProcessTools();
		
		try {
			DatagramSocket FESocket = new DatagramSocket(13360);
			String threadName = "FE" + "udpListen";
			ListenThread listen = new ListenThread(threadName, FESocket);
			listen.start();
		
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean Login(String id){
	  
		// form the FeRequest Message;
		String campus_id = id.substring(0, 3);
		int requstNum = req_Num.incrementAndGet();
		RequestType funcType = RequestType.Login;
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(id);
	  
		FErequest feRequst_message = new FErequest(campus_id, requstNum, funcType, parameters);
		String result = tools.sendAndReceive(feRequst_message);
		
		return Boolean.parseBoolean(result);	
	}
	 

	@Override
	public boolean[] createRoom(String id, String room_Number, String date, String[] Time_Slots) {
		// form the FeRequest Message;
		System.out.println(id);
		String campus_id = id.substring(0, 3);
		int requstNum = req_Num.incrementAndGet();
		RequestType funcType = RequestType.AddRecord;
		
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(id);
		parameters.add(room_Number);
		parameters.add(date);
		int num = 0;
		for (String each : Time_Slots) {
			parameters.add(each);
			num++;
		}
		
		FErequest feRequst_message = new FErequest(campus_id, requstNum, funcType, parameters);
		String result = tools.sendAndReceive(feRequst_message);
		System.out.println("result is: " + result);
		
		
		boolean[] results = new boolean[num];
		if (result.equals("Time out, get nothing!!") || result.equals("different result in 3 replica!")) {
			
			for (int i = 0; i < num; i++) {
				results[i] = false;
			}
			
			return results;
		}
		
		String[] temp = result.trim().split(" ");
		System.out.println("size of temp:  " + temp.length);
		System.out.println("size of results:  " + results.length);
		
		for (int i = 0; i < num; i++) {
			results[i] = Boolean.parseBoolean(temp[i]);
		}
		
		return results;
	}


	@Override
	public boolean[] deleteRoom(String id, String room_Number, String date, String[] Time_Slots) {
		// form the FeRequest Message;
		String campus_id = id.substring(0, 3);
		int requstNum = req_Num.incrementAndGet();
		RequestType funcType = RequestType.DeleteRecord;
				
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(id);
		parameters.add(room_Number);
		parameters.add(date);
		int num = 0;
		for (String each : Time_Slots) {
			parameters.add(each);
			num++;
		}
				
		FErequest feRequst_message = new FErequest(campus_id, requstNum, funcType, parameters);
		String result = tools.sendAndReceive(feRequst_message);
		System.out.println("result is: " + result);
		
		boolean[] results = new boolean[num];
		if (result.equals("Time out, get nothing!!") || result.equals("different result in 3 replica!")) {
			
			for (int i = 0; i < num; i++) {
				results[i] = false;
			}
			
			return results;
		}
		
		String[] temp = result.trim().split(" ");
		
		
		//System.out.println("size of temp:  " + temp.length);
		//System.out.println("size of results:  " + results.length);
		
		for (int i = 0; i < num; i++) {
			results[i] = Boolean.parseBoolean(temp[i]);
		}
		
		return results;
	}


	@Override
	public String bookRoom(String id, String campusName, String room_Number, String date, String Time_Slot) {
		// form the FeRequest Message;
		String campus_id = id.substring(0, 3);
		int requstNum = req_Num.incrementAndGet();
		RequestType funcType = RequestType.Book;
						
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(id);
		parameters.add(campusName);
		parameters.add(room_Number);
		parameters.add(date);
		parameters.add(Time_Slot);
		
						
		FErequest feRequst_message = new FErequest(campus_id, requstNum, funcType, parameters);
		String result = tools.sendAndReceive(feRequst_message);
		System.out.println("FE Impl return result: " + result);
		
		return result;
	}


	@Override
	public String getAvailableTimeSlot(String id, String date) {
		// form the FeRequest Message;
		String campus_id = id.substring(0, 3);
		int requstNum = req_Num.incrementAndGet();
		RequestType funcType = RequestType.GetAvailTimeSlot;
								
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(id);
		parameters.add(date);

		FErequest feRequst_message = new FErequest(campus_id, requstNum, funcType, parameters);
		String result = tools.sendAndReceive(feRequst_message);
		
		return result;

	}


	@Override
	public boolean cancelBooking(String id, String bookingID) {
		// form the FeRequest Message;
		String campus_id = id.substring(0, 3);
		int requstNum = req_Num.incrementAndGet();
		RequestType funcType = RequestType.CancelBook;
										
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(id);
		parameters.add(bookingID);

		FErequest feRequst_message = new FErequest(campus_id, requstNum, funcType, parameters);
		String result = tools.sendAndReceive(feRequst_message);	
		
		if (result.equals("Time out, get nothing!!") || result.equals("different result in 3 replica!")) {
			return false;
		}
		
		return Boolean.parseBoolean(result);
	}


	@Override
	public String changeReservation(String id, String bookingID, String campusName, String room_Number, String Time_Slot) {
		// form the FeRequest Message;
		String campus_id = id.substring(0, 3);
		int requstNum = req_Num.incrementAndGet();
		RequestType funcType = RequestType.ChangeReservation;
								
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add(id);
		parameters.add(bookingID);
		parameters.add(campusName);
		parameters.add(room_Number);
		parameters.add(Time_Slot);
				
		FErequest feRequst_message = new FErequest(campus_id, requstNum, funcType, parameters);
		String result = tools.sendAndReceive(feRequst_message);									
		
		return result;
	}
	
	
}
	
