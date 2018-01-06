package Client;

import java.util.ArrayList;
import java.util.Arrays;


import FEAPP.FE;

public class Testcase {
	
	private static String[] timeslot = {"7:30-9:30", "10:00-12:30", "13:30-16:00", "17:00-18:00", "19:00-20:00"};
	private static String room = "201";
	private static String date1 = "2017-10-01";
	private static String date2 = "2017-10-02";
	

	public static void main(String[] args) throws Exception {

		System.out.println("\nTest case：Create room in three campus");
		TestCreatRoom(args, date1, date2);
		
		System.out.println("\nTest case： Book local room and show booking limitation");
		TestBookRoomLocal("DVL", args);
		
		System.out.println("\nTest case： Delete record and show delete chain");
		TestDeleteRoom(args);
		
		System.out.println("\nTest case: Book remote rooms which are in difference campus");
		ArrayList<String> bookingID = TestBookRoomRemote(args);
		
		System.out.println("\nTest case： Book the same room by another student");
		TestBookSameRoom(args);
		
		System.out.println("Test case: Cancel a booking that wasn't booked by the user");
		TestCancelNotBooking(args, bookingID);
		
		System.out.println("\nTest case： Cancel booking");
		TestCancelBooking(args, bookingID);
		
		System.out.println("\nTest case： Change room reservation");
		TestChangeReservation(args);
		
		System.out.println("\nTest case finish");
		
	
	}

	private static FE TestgetConnection(String[] args, ClientAlternative admin) throws Exception {
		//System.out.println("Connection builded.....");
		return admin.getConnection(args);
	}

	
	private static void TestCreatRoom(String[] args, String d1, String d2) throws Exception {
		
		ClientAlternative adminDVL = new ClientAlternative("DVLA1000");
		FE admin_DVL = TestgetConnection(args, adminDVL);
		
		ClientAlternative adminKKL = new ClientAlternative("KKLA1000");
		FE admin_KKL = TestgetConnection(args, adminKKL);
		
		ClientAlternative adminWST = new ClientAlternative("WSTA1000");
		FE admin_WST = TestgetConnection(args, adminWST);
		
		boolean[] result1 = admin_DVL.createRoom("DVLA1000", room, d1, timeslot);
		System.out.println("DVLA1000 creat room: " + Arrays.toString(result1));
		result1 = admin_DVL.createRoom("DVLA1000", room, d2, timeslot);
		System.out.println("DVLA1000 creat room: " + Arrays.toString(result1));
		
		boolean[] result2 = admin_KKL.createRoom("KKLA1000", room, d1, timeslot);
		System.out.println("KKLA1000 creat room: " + Arrays.toString(result2));
		result2 = admin_KKL.createRoom("KKLA1000", room, d2, timeslot);
		System.out.println("KKLA1000 creat room: " + Arrays.toString(result2));
		
		boolean[] result3 = admin_WST.createRoom("WSTA1000", room, d1, timeslot);
		System.out.println("WSTA1000 creat room: " + Arrays.toString(result3));
		result3 = admin_WST.createRoom("WSTA1000", room, d2, timeslot);
		System.out.println("WSTA1000 creat room: " + Arrays.toString(result3));
		
		
		ClientAlternative studentDVL = new ClientAlternative("DVLS1000");
		FE student = TestgetConnection(args, studentDVL);
		String avail = student.getAvailableTimeSlot("DVLS1000", d1);
		System.out.println(date1 + " available time slot now: " + avail);
		avail = student.getAvailableTimeSlot("DVLS1000", d2);
		System.out.println(date2 + " available time slot now: " + avail);
		
	}
	
	private static void TestDeleteRoom(String[] args) throws Exception {
		
		ClientAlternative adminDVL = new ClientAlternative("DVLA1000");
		FE admin_DVL = TestgetConnection(args, adminDVL);
		
		boolean[] result = admin_DVL.deleteRoom("DVLA1000", room, date1, timeslot);
		System.out.println("DVLA1000 delete room in " + date1 + ":  "+ Arrays.toString(result));
		
		ClientAlternative studentDVL = new ClientAlternative("DVLS1000");
		FE student = TestgetConnection(args, studentDVL);
		String avail = student.getAvailableTimeSlot("DVLS1000", date1);
		System.out.println(date1 + " available time slot now: " + avail);
		avail = student.getAvailableTimeSlot("DVLS1000", date2);
		System.out.println(date2 + " available time slot now: " + avail);
			
	}
	
	
	private static void TestBookRoomLocal(String campus, String[] args) throws Exception {
		
		ClientAlternative studentclient = new ClientAlternative(campus + "S1000");
		FE student = TestgetConnection(args, studentclient);
		
		for (int i = 0; i < 5; i++) {
			String result = student.bookRoom("DVLS1000", campus, room, date1, timeslot[i]);
			if(result != null && !result.equals("")) {
				System.out.println("Succeed to book room in " + campus);
				System.out.println("bookingID: " + result);
			}
			else {
				System.out.println("Failed to book.");
			}
			
			String avail = student.getAvailableTimeSlot("DVLS1000", date1);
			System.out.println(date1 + " available time slot now: " + avail);
			System.out.print("\n");
		}
	}
	
	private static ArrayList<String> TestBookRoomRemote(String[] args) throws Exception {
		
		ClientAlternative studentDVL = new ClientAlternative("DVLS1000");
		FE student = TestgetConnection(args, studentDVL);
		
		ArrayList<String> bookingID = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			String result = student.bookRoom("DVLS1000", "KKL", room, date2, timeslot[i]);
			if(result != null && !result.equals("")) {
				//System.out.println("Succeed to book room in " + "KKL");
				System.out.println("bookingID: " + result);
				bookingID.add(result);
			}
			else {
				System.out.println("Failed to book.");
			}
			String avail = student.getAvailableTimeSlot("DVLS1000", date2);
			System.out.println(date2 + " available time slot now: " + avail);
			System.out.print("\n");
		}
		
		return bookingID;
	}
	
	private static void TestBookSameRoom(String[] args) throws Exception {
		
		ClientAlternative studentKKL = new ClientAlternative("KKLS1000");
		FE student = TestgetConnection(args, studentKKL);
		
		for (int i = 0; i < 5; i++) {
			String result = student.bookRoom("KKLS1000", "KKL", room, date2, timeslot[i]);
			if(result != null && !result.equals("")) {
				//System.out.println("Succeed to book room in " + "KKL");
				System.out.println("bookingID: " + result);
			}
			else {
				System.out.println("Failed to book.");
			}
			String avail = student.getAvailableTimeSlot("DVLS1000", date2);
			System.out.println(date2 + " available time slot now: " + avail);
			System.out.print("\n");
		}
	}
	
	public static void TestCancelNotBooking(String[] args, ArrayList<String> bookingID) throws Exception {
		ClientAlternative studentWST = new ClientAlternative("WSTS1000");
		FE student = TestgetConnection(args, studentWST);	
		
		for (String each : bookingID) {
			boolean result = student.cancelBooking("WSTS1000", each);
			if(result == true)
				System.out.println("Cancel bookingID " + each + " Success");
			else
				System.out.println("Cancel bookingID " + each + " Failure");		
		}
		
		String avail = student.getAvailableTimeSlot("DVLS1000", date2);
		System.out.println(date2 + " available time slot now: " + avail);
		System.out.print("\n");
	}
	
	private static void TestCancelBooking(String[] args, ArrayList<String> bookingID) throws Exception {
		ClientAlternative studentDVL = new ClientAlternative("DVLS1000");
		FE student = TestgetConnection(args, studentDVL);
		
		for (String each : bookingID) {
			boolean result = student.cancelBooking("DVLS1000", each);
			if(result == true)
				System.out.println("Cancel bookingID " + each + " Success");
			else
				System.out.println("Cancel bookingID " + each + " Failure");		
		}
		
		String avail = student.getAvailableTimeSlot("DVLS1000", date2);
		System.out.println(date2 + " available time slot now: " + avail);
		System.out.print("\n");
	}
	

	
	private static void TestChangeReservation(String[] args) throws Exception {
		
		
		ClientAlternative studentDVL = new ClientAlternative("DVLS1000");
		FE student_DVL = TestgetConnection(args, studentDVL);
		String DVLid = "DVLS1000";
		
		System.out.println("\nfirst: Book a room first");
		String bookingID = student_DVL.bookRoom(DVLid, "WST", room, date2, timeslot[1]);
		
		String cnt = student_DVL.getAvailableTimeSlot(DVLid, date2);
		System.out.println("Available time slots on " + date2+ ": " + cnt);
		System.out.print("\n\n");
		
		System.out.println("\nThen: Change room reservation");
		String newbookingID = student_DVL.changeReservation(DVLid, bookingID, "KKL", room, timeslot[0]);
		if(newbookingID == null) {
			System.out.println("Failed to change.");
		}
		else {
			System.out.println("Change successfully! New bookingID: " + bookingID);
		}
		
		cnt = student_DVL.getAvailableTimeSlot(DVLid, date2);
		System.out.println("Available time slots on " + date2+ ": " + cnt);
		System.out.print("\n\n");


//		System.out.println("Test case: change reservation to a room that has been booked");
//		String ret = student_DVL.changeReservation(DVLid, newbookingID, "WST", room, timeslot[0]);
//		if(ret == null) {
//			System.out.println("Failed to change.");
//		}
//		else {
//			System.out.println("Change successfully! New bookingID: " + ret);
//		}
//		System.out.print("\n\n");
//		
		String ret;
		System.out.println("Test case: change reservation by another student who didn't book the room");
		ClientAlternative studentWST = new ClientAlternative("WSTS1000");
		FE student_WST = TestgetConnection(args, studentWST);
		String WSTid = "WSTS1000";
		
		ret = student_WST.changeReservation(WSTid, bookingID, "DVL", room, timeslot[1]);
		if(ret == null || ret.equals("")) {
			System.out.println("Failed to change.");
		}
		else {
			System.out.println("Change successfully! New bookingID: " + ret);
		}
		System.out.print("\n\n");
		
	}
	

}
