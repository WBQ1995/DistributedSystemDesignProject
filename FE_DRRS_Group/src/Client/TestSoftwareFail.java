package Client;

import java.util.Arrays;

import FEAPP.FE;

public class TestSoftwareFail {
	
	private static String[] timeslot = {"7:30-9:30", "10:00-12:30", "13:30-16:00", "17:00-18:00", "19:00-20:00"};
	private static String room = "201";
	private static String date1 = "2017-10-01";
	

	public static void main(String[] args) throws Exception {

		ClientAlternative adminDVL = new ClientAlternative("DVLA1000");
		FE admin_DVL = TestgetConnection(args, adminDVL);
		
		boolean[] result1 = admin_DVL.createRoom("DVLA1000", room, date1, timeslot);
		System.out.println("DVLA1000 creat room: " + Arrays.toString(result1));
		
		ClientAlternative studentKKL = new ClientAlternative("KKLS1000");
		FE student = TestgetConnection(args, studentKKL);
		
		String avail = student.getAvailableTimeSlot("KKLS1000", date1);
		System.out.println(date1 + " available time slot now: " + avail);
		
		
		String campus = "DVL";
		String result = student.bookRoom("KKLS1000", campus, room, date1, timeslot[0]);
		if(result != null && !result.equals("")) {
			System.out.println("Succeed to book room in " + campus);
			System.out.println("bookingID: " + result);
		}
		else {
			System.out.println("Failed to book.");
		}
		
		
		avail = student.getAvailableTimeSlot("KKLS1000", date1);
		System.out.println(date1 + " available time slot now: " + avail);
		
		avail = student.getAvailableTimeSlot("KKLS1000", date1);
		System.out.println(date1 + " available time slot now: " + avail);
		
		avail = student.getAvailableTimeSlot("KKLS1000", date1);
		System.out.println(date1 + " available time slot now: " + avail);
		
		
		
		
	}
	
	private static FE TestgetConnection(String[] args, ClientAlternative admin) throws Exception {
		//System.out.println("Connection builded.....");
		return admin.getConnection(args);
	}
}

	