package RoomResrvSys;
import java.util.ArrayList;

public interface RemoteServerInterface {

	ArrayList<Boolean> createRoom (String ID, String room_Number, String date, ArrayList<String> Time_Slots);

	ArrayList<Boolean> deleteRoom (String ID, String room_Number, String date, ArrayList<String> Time_Slots);

	String bookRoom (String StudentID,String campusName, String roomNumber, String date, String timeslot);

	String getAvailableTimeslot (String ID, String date);

	boolean cancelBook (String studentID, String bookingID);

	//void StartServer (String campus);

	//boolean adminLogin (String adminID);

	//boolean studentLogin (String studentID);
	
	boolean login(String ID);
	
	String changeReservation (String studentID, String booking_id, String new_campus_name, String new_room_no, String new_time_slot);
	
	boolean storeData(String f_name);
	boolean loadData(String f_name);

}
