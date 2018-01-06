package Client;

import java.util.regex.Pattern;

import FEAPP.FE;


public class ClientConsole {
	
	private String userID;
	
	public ClientConsole() {
		userID = "";
	}

	
	public void login(String id, FE h) throws Exception {
		
		//System.out.println("Login " );
		boolean judge = h.Login(id);
		//System.out.println(judge);
		if (judge == false ) {
			System.out.println("Wrong ID or Wrong Password!");
			//Logger.writeClientLog(id, "Login", false, "Login ID or password isn't correct");
			System.exit(0);
		}
		else {
			userID = id;
			System.out.println("Welcom, " + id);
		}
	}
	

	public void createANDdelete(FE h, String command) throws Exception {
	
		// get each parameter;
		String 	parameter = command.substring(command.indexOf("(")+1, command.length()-1);
		//System.out.println("the parameter:  " + parameter);
		String[] para = parameter.split("\\s+");
		
		String room_Number = para[0].trim();
		String date = para[1].trim();
		String[] list_Of_Time_Slots = new String[para.length - 2];
		int j = 0;
		for (int i = 2; i < para.length; i++ ) {
			list_Of_Time_Slots[j] = para[i].trim();
			j++;
		}
	
	//try {
		/* run
		 * createRoom (String room_Number, Date date, String TimeSlot)
		 */
		boolean[] ifSuccess = new boolean[list_Of_Time_Slots.length];
		if (command.startsWith("createRoom")) {
			ifSuccess = h.createRoom(userID, room_Number, date, list_Of_Time_Slots);
			//System.out.println("createRoom");
		}
		if (command.startsWith("deleteRoom")) {
			ifSuccess = h.deleteRoom(userID, room_Number, date, list_Of_Time_Slots);
			//System.out.println("deleteRoom");
		}
	//}	catch (Exception ex) {
	//	ifSuccess = 0;
	//	return ifSuccess;
	//}
		for (boolean each : ifSuccess) {
			System.out.println(" ");
			System.out.print(each + " ");
			
		}
	}
	
	
	public void bookRoom(FE h, String command) throws Exception{
		/* get each parameter;
		 *bookRoom(String campusName, String roomNumber, String date, String timeslot) 
		 */
		String 	parameter = command.substring(command.indexOf("(")+1, command.length()-1);	
		String[] para = parameter.split("\\s+");
		
		String campusName = para[0].trim();
		String room_Number = para[1].trim();
		String date = para[2].trim();
		String list_Of_Time_Slots = para[3].trim();
		
		String bookingID = "";
		//try{
			//System.out.println("connect to Sever");
			bookingID = h.bookRoom(userID, campusName, room_Number, date, list_Of_Time_Slots);	
			//System.out.println("booking ID::::::" + bookingID);
			//}	catch (Exception ex) {
			//	return ("");
			//}
		if (bookingID == null || bookingID.equals("")) {
			System.out.println("Command error, please check your command");
		}
		else {
			System.out.println("Success! the bookingID is :" + bookingID);
		}
	}
	
	
	public void cancelBooking(FE h, String command, String id) throws Exception{
		/* get each parameter;
		 * cancelBooking(String bookingID) 
		 */
		String 	bookingID = command.substring(command.indexOf("(")+1, command.length()-1);
		String  studentID = bookingID.substring(bookingID.length()-8);
		
		boolean ifSuccess = false;
		if (!studentID.equals(id)) {
			System.out.println("Failure! studntID in the BookingID is not matched the login ID");
			ifSuccess = false;
		}
		else{
			//System.out.println("connect to local sever to cancelBooking");
			ifSuccess = h.cancelBooking(userID, bookingID);	
		}
		
		if (ifSuccess == true)  {
			System.out.println("Successful cancel booking!");
		}
		else{
			System.out.println("Failure, can not find the bookingID");
		}
	}
	
	public void getAvailableTimeSlot(FE h, String command) throws Exception {
		/* get each parameter;
		 * String getAvailableTimeSlot(String date) 
		 */
		String 	date = command.substring(command.indexOf("(")+1, command.length()-1);	

		String avail = null;
		//try{
			avail = h.getAvailableTimeSlot(userID, date);	
		//} catch (Exception ex) {
		//	return null;
		//}
		if ( avail == null) {
			System.out.println("Command error, please check your command");
		}
		System.out.println(avail);
	}

	public void changeReservation(FE h, String command) throws Exception {
		
		String 	parameter = command.substring(command.indexOf("(")+1, command.length()-1);
		//System.out.println("the parameter:  " + parameter);
		String[] para = parameter.split(",");
		
		String bookingID = para[0].trim();
		String studentID = bookingID.substring(bookingID.length()-8);
		//System.out.println("student ID :" + studentID + "  UserID :" + userID);
		String new_campus_name = para[1].trim();
		String new_room_Number = para[2].trim();
		String new_Time_Slots = para[3].trim();
	
		String new_bookingID = "";
		if (!studentID.equals(userID)) {
			System.out.println("Failure! studntID in the BookingID is not matched the login ID");
		}
		else {
			new_bookingID = h.changeReservation(userID, bookingID, new_campus_name, new_room_Number, new_Time_Slots);	
		}
		
		if (new_bookingID == null || new_bookingID.equals("")) {
			System.out.println("Command error, please check your command");
		}
		else {
			System.out.println("Success! the new_bookingID is :" + new_bookingID);
		}
		
	}

	

	
}
