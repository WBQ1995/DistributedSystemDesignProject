package Client;

import java.util.Arrays;

import FEAPP.FE;

public class TestBookSameTime {
	
	private static String[] timeslot = {"7:30-9:30", "10:00-12:30", "13:30-16:00", "17:00-18:00", "19:00-20:00"};
	private static String room = "201";
	private static String date1 = "2017-11-01";

	public static void main(String[] args) throws Exception {
		
		System.out.println("\nTest case： Book room at the same time");
		
		
		ClientAlternative adminDVL = new ClientAlternative("DVLA1000");
		FE admin_DVL = TestgetConnection(args, adminDVL);
		
		boolean[] result1 = admin_DVL.createRoom("DVLA1000", room, date1, timeslot);
		System.out.println("DVLA1000 creat room: " + Arrays.toString(result1));
		
		System.out.println("\n Book rooms in 2017-11-01 & 2017-11-02");
		
		
		for (int i = 1000; i < 1010; i++) {
			
			ClientAlternative studentKKL = new ClientAlternative("KKLS" + i);
			FE student_KKL = TestgetConnection(args, studentKKL);
			
			ClientAlternative studentDVL = new ClientAlternative("DVLS" + i);
			FE student_DVL = TestgetConnection(args, studentDVL);

			
			TestThread t1 = new TestThread(student_KKL, "KKLS" + i);
			TestThread t2 = new TestThread(student_DVL, "DVLS" + i);
			
			t1.start();
			t2.start();

		}
		
	}
		
		private static FE TestgetConnection(String[] args, ClientAlternative admin) throws Exception {
			//System.out.println("Connection builded.....");
			return admin.getConnection(args);
		}

}
