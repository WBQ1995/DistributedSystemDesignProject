package RoomResrvSys;
import java.io.File;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

public class Admintest {

	public static void main(String[] args) throws SocketException {
		
		
		HashMap<String, String> try1 = new HashMap<>();
		try1.put("123", "456");
		System.out.println(try1.get("345"));
		
		ArrayList<String> timeslots = new ArrayList<>();
		ArrayList<String> deleteslots = new ArrayList<>();
		//timeslots.add("11");
		timeslots.add("9:00-10:00");
		timeslots.add("10:00-11:00");
		deleteslots.add("9:00-10:00");
		
		
		ServerRemoteImpl test1 = new ServerRemoteImpl("DVL", 25560);
		ServerRemoteImpl test2 = new ServerRemoteImpl("KKL", 25561);
		ServerRemoteImpl test3 = new ServerRemoteImpl("WST", 25562);
		
		test1.createRoom("DVLA1111", "214","2017-01-01", timeslots);
		test1.login("DVLS");
		test2.login("KKLS");
		test3.login("WSTS1111");
		System.out.println(test2.getAvailableTimeslot("KKLS","2017-01-01"));
		
		String bookingID = test2.bookRoom("KKLS", "DVL", "214", "2017-01-01", "9:00-10:00");
		System.out.println(bookingID);
		System.out.println(test2.getAvailableTimeslot("KKLS","2017-01-01"));
		
		System.out.println(test2.cancelBook("KKLS", bookingID));
		System.out.println(test2.getAvailableTimeslot("KKLS","2017-01-01"));
		
		test1.deleteRoom("DVLA1111", "214", "2017-01-01", deleteslots);
		
		System.out.println(test2.changeReservation("KKLS", bookingID, "DVL", "214", "10:00-11:00"));
		System.out.println(test2.getAvailableTimeslot("KKLS","2017-01-01"));
		
		test2.bookRoom("KKLS", "DVL", "214", "2017-01-01", "10:00-11:00");
		System.out.println(test2.getAvailableTimeslot("KKLS","2017-01-01"));
		
		String f_name = "store.txt";
		
		test1.storeData(f_name);
		
		ServerRemoteImpl test4 = new ServerRemoteImpl("DVL", 25568);
		test4.loadData(f_name);
		
		System.out.println(test4.hashMap.get("2017-01-01").get("214").get(0).Booked_by);

	
	}

}
