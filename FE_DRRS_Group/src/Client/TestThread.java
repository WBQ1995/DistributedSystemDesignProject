package Client;

import FEAPP.FE;

public class TestThread extends Thread{ 
	
	private static String[] timeslot = {"7:30-9:30", "10:00-12:30", "13:30-16:00", "17:00-18:00", "19:00-20:00"};
	private static String room = "201";
	private static String date1 = "2017-11-01";
	private static String date2 = "2017-11-02";
	
	private FE student;
	private Thread t;
	private String id;
	

	public TestThread(FE student, String id) throws Exception {
		super();
		this.student = student;
		this.id = id;
	}
	
	public void start () {
		if (t == null) {
	    		t = new Thread (this);
	    		t.start ();
	      }
	 }
	
//	public void output(String bookingID) {
//		if (bookingID != null && !bookingID.equals("")) 
//			System.out.println("Success!  the bookingID is " + bookingID);
//		else 
//			System.out.println("failure!");
//	}
	
	public void run() {
		
	  		
	  		String bookingID = "";
	  		
	  		try {
	  			//DVL
	  			bookingID = student.bookRoom(id, "DVL", room, date1, timeslot[0]);
	  			//System.out.println("client received bookingID" + bookingID);
	  			
	  			if (bookingID != null && !bookingID.equals("")) 
	  				System.out.println("Success!  the bookingID is: " + bookingID);
	  			else 
	  				System.out.println("failure!");
	  			
					
	  		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	  		//System.out.println("Thread ending " + id);

		
	}

}
