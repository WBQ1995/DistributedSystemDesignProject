package RoomResrvSys;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ServerRemoteImpl implements RemoteServerInterface,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String campus_name = " ";
	int listenPort = -1;
	private Lock lock = new ReentrantLock();
	
	
	HashMap<String, HashMap<String,ArrayList<String>>> map2 = new HashMap<String, HashMap<String,ArrayList<String>>>();
	HashMap<String, HashMap<String, ArrayList<Bookrecord>>> hashMap =  new HashMap<>();
	HashMap<String, Bookrecord> BookIDmap =  new HashMap<>();
	HashMap<String, String> Bookdatemap =  new HashMap<>();
	HashMap<String, String> Booktimeslot =  new HashMap<>();
	
	ArrayList<AdminClient> adminClients = new ArrayList<>();
	ArrayList<StudentClient> studentClients = new ArrayList<>();
	ArrayList<Bookrecord> Bookrecords = new ArrayList<>();
	
	Random r = new Random(120152679);
	
	
	class RoomRecord{
		public RoomRecord(String campus_name) {
			// TODO Auto-generated constructor stub
		}
		public String RecordID;
		public String Date;
		public String RoomNumber;
		public String Booked_by = " ";
		public ArrayList<String > ListOfAvailable = new ArrayList<>();
	}
	
	class Bookrecord implements Serializable{
		String timeslot;
		String Booked_by = " ";
		String bookingID = " ";
		String RecordID = " ";
		Boolean booked = false;
	}
	
	class StudentClient{
		String studentID = " ";
		int bookingcount = 0;
		int BookingCount[] = new int[49];
	}
	
	class AdminClient{
		public String adminID = " ";
	}
	
	class Forward{
		String s = " ";
		int i = 0;
	}
	
	public boolean storeData(String f_name){
		
		FileOutputStream fs;
		try {
			lock.lock();
			
			fs = new FileOutputStream(f_name);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			
			os.writeObject(r);
			os.writeObject(campus_name);
			os.writeObject(hashMap);
			os.writeObject(BookIDmap);
			os.writeObject(Bookdatemap);
			os.writeObject(Booktimeslot);
			os.writeObject(studentClients);
		
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			lock.unlock();
		}
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadData(String f_name){
		
		try {
			lock.lock();
			
			FileInputStream fileStream = new FileInputStream(f_name);
			ObjectInputStream os = new ObjectInputStream(fileStream);

			r = (Random)os.readObject();
			campus_name = (String)os.readObject();
			hashMap = (HashMap<String, HashMap<String, ArrayList<Bookrecord>>>)os.readObject();
			BookIDmap = (HashMap<String, Bookrecord>)os.readObject();
			Bookdatemap = (HashMap<String, String>)os.readObject();
			Booktimeslot = (HashMap<String, String>)os.readObject();
			studentClients = (ArrayList<StudentClient>)os.readObject();
			
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
	
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		
		return true;
	}
	
	
	
	class RequestExecution extends Thread{
		public Thread t;
		public DatagramSocket serversocket;
		public InetAddress address;
		public int clientport;
		String message;
		
		public RequestExecution(DatagramSocket serversocket,InetAddress address,int clientport,String message) {
			super();
			this.address = address;
			this.message = message;
			this.clientport = clientport;
			this.serversocket = serversocket;
		}
		
		public void start(){
			if(t == null){
				t = new Thread(this);
				t.start();
			}
		}
		
		public void run(){
			String result = " ";
			if(message.startsWith("getAvailableTimeSlot")){
				result = UDPgetAvailableTimeSlot(message);
			}
			else if(message.startsWith("bookRoom")){
				result = UDPbookRoom(message);
			}
			else if(message.startsWith("cancelBooking")){
				result = UDPcancelBooking(message);
			}
			else if(message.startsWith("altercount")){
				result = UDPaltercount(message);
			}
			byte []buffer = result.getBytes();
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length,address,clientport);
			try {
				serversocket.send(reply);
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		}
		public String UDPgetAvailableTimeSlot(String message){
			String data = message.substring(message.indexOf("(") + 1,message.length() - 1);
			String result = getavailableLocal(data);
			return result;	
		}
		
		public String UDPbookRoom(String message){
			String arguments = message.substring(message.indexOf("(") + 1,message.length() - 1);
			String arg[] = arguments.split(",");
			String StudentID = arg[0];
			String roomNumber = arg[1];
			String date = arg[2];
			String timeslot = arg[3];
			String result = bookLocol(roomNumber, date, timeslot, StudentID);
			return result;
		}
		
		public String UDPcancelBooking(String message){
			String arguments = message.substring(message.indexOf("(") + 1,message.length() - 1);
			String arg[] = arguments.split(",");
			String BookingID = arg[0];
			String StudentID = arg[1];
			if(cancelLocol(BookingID, StudentID))
				return "successful";
			else
				return "failed";
		}
		
		public String UDPaltercount(String message){
			String arguments = message.substring(message.indexOf("(") + 1,message.length() - 1);
			String arg[] = arguments.split(",");
			String studentID = arg[0];
			String date = arg[1];
			AltercountLocal(studentID, date);
			return "successful";
		}
		
	}
	
	class Listening extends Thread{
		public Thread t;
		public DatagramSocket serversocket;
		
		public Listening(DatagramSocket serversocket){
			this.serversocket = serversocket;
		}
		
		public void run(){
			byte []buffer = new byte[1000];
			try {
				while(true){
					
					DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				
					serversocket.receive(request);
					byte []newbuffer = new byte[request.getLength()];
					System.arraycopy(buffer, 0, newbuffer, 0, request.getLength());
					String message = new String(newbuffer).trim();
					InetAddress address = request.getAddress();
					int clientport = request.getPort();
					RequestExecution exe = new RequestExecution(serversocket, address, clientport, message);
					exe.start();
				}
			} 
			catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				if(serversocket != null)
						serversocket.close();
			}
			
		}
		public void start () {
			if (t == null) {
		    		t = new Thread (this);
		    		t.start ();
		      }
		 }

	}
	

	ServerRemoteImpl(String campus_name, int innerport) throws SocketException{
		this.campus_name = campus_name;
		this.listenPort = innerport;
		
		
		DatagramSocket serversocket = new DatagramSocket(innerport);
		Listening listen = new Listening(serversocket);
		listen.start();
		
	}

	@Override
	public ArrayList<Boolean> createRoom (String ID, String room_Number, String date, ArrayList<String> Time_Slots){
		ArrayList<Boolean> returnvalues = new ArrayList<>();
		for(int i = 0; i < Time_Slots.size(); i ++){
			returnvalues.add(createRoomLocal(ID, room_Number, date, Time_Slots.get(i)));
		}	
		return returnvalues;	
	}
	
	public boolean createRoomLocal(String ID, String room_Number, String date, String Time_Slot) {
		Boolean create = false;
		if(incertRecord(room_Number, date, Time_Slot)){
			create = true;
			try {
				Log(campus_name, getFormatDate() + " AdminClient " + ID + " create a room successfully. " + "Room information: "
			+ date + " " + room_Number + " " + Time_Slot);
			} catch (Exception e) {
				e.printStackTrace();  
			}
		}
		
	if(!create){
		try {
			Log(campus_name, getFormatDate() + " AdminClient " + ID + " create a room failed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return create;
	}

	@Override
	public ArrayList<Boolean> deleteRoom (String ID, String room_Number, String date, ArrayList<String> Time_Slots){
		ArrayList<Boolean> returnvalues = new ArrayList<>();
		for(int i = 0; i < Time_Slots.size(); i ++){
			returnvalues.add(deleteRoomLocal(ID, room_Number, date, Time_Slots.get(i)));
		}	
		return returnvalues;	
	}
	
	
	public boolean deleteRoomLocal(String ID, String room_Number, String date, String Time_Slot) {
		Boolean delete = false;
		String result = " ";
		try {
			lock.lock();
			if(hashMap.containsKey(date)){
				if(hashMap.get(date).containsKey(room_Number)){
					for(int j = 0 ; j < hashMap.get(date).get(room_Number).size(); j ++){
						if(hashMap.get(date).get(room_Number).get(j).timeslot.equals(Time_Slot)){	
							if(hashMap.get(date).get(room_Number).get(j).booked){
								String studentID = hashMap.get(date).get(room_Number).get(j).Booked_by;
								String bookingID = hashMap.get(date).get(room_Number).get(j).bookingID;
								
								String command = "altercount(" + studentID + "," + date + ")";
								try{
								if(campus_name.equals("DVL")){
									if(studentID.startsWith("DVLS"))
										Altercount(studentID,date);
									else if(studentID.startsWith("KKLS"))
										result = UDPRequest.Altercount(command, 25561);
									else if(studentID.startsWith("WSTS"))
										result = UDPRequest.Altercount(command, 25562);
								}
								else if(campus_name.equals("KKL")){
									if(studentID.startsWith("KKLS"))
										Altercount(studentID,date);
									else if(studentID.startsWith("DVLS"))
										result = UDPRequest.Altercount(command, 25560);
									else if(studentID.startsWith("WSTS"))
										result = UDPRequest.Altercount(command, 25562);
								}
								else if(campus_name.equals("WST")){
									if(studentID.startsWith("WSTS"))
										Altercount(studentID,date);
									else if(studentID.startsWith("DVLS"))
										result = UDPRequest.Altercount(command, 25560);
									else if(studentID.startsWith("KKLS"))
										result = UDPRequest.Altercount(command, 25561);
								}
								}
								catch (Exception e) {
								}
								
								Bookdatemap.remove(bookingID);
								BookIDmap.remove(bookingID);
							}
							hashMap.get(date).get(room_Number).remove(j);
							delete = true;
							try {
								Log(campus_name, getFormatDate() + " AdminClient " + ID + " delete room successfully");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} finally {
			lock.unlock();
		}
		if(!delete){
			try {
				Log(campus_name, getFormatDate() + " AdminClient " + ID + " delete room failed");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return delete;
	}

	@Override
	public String bookRoom(String StudentID,String campusName, String roomNumber, String date, String timeslot) {
		
		loginlocal(StudentID);
		
		String result = " "; 
		int sequence;
		String command = "bookRoom(" + StudentID + "," + roomNumber + "," + date + "," + timeslot + ")";
		int week = getweek(date);
		for(int i = 0; i < studentClients.size();i ++){
			if(studentClients.get(i).studentID.equals(StudentID) && studentClients.get(i).BookingCount[week] <=2){
				try {
					if(campusName.equals(campus_name)){
						result = bookLocol(roomNumber, date, timeslot, StudentID);
					}
				
					else if(campusName.equals("DVL")){
						int serverport = 25560;
						result = UDPRequest.UDPbookroom(command, serverport);
					}
					else if(campusName.equals("KKL")){
						int serverport = 25561;
						result = UDPRequest.UDPbookroom(command, serverport);
					}
					else if(campusName.equals("WST")){
						int serverport = 25562;
						result = UDPRequest.UDPbookroom(command, serverport);
					}
					
				} catch (Exception e) {
					
				}
				if(result.equals(" ")){
					System.out.println("studentClient " + StudentID + " booked a room Unsuccessfully" );
					try {
						Log(campus_name, getFormatDate() + "StudentClient " + StudentID + " book a room failed");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return " ";
				}
				else{
					studentClients.get(i).BookingCount[week] ++;
					System.out.println("studentClient " + StudentID + " booked a room successfully" );
					Bookdatemap.put(result, date);
					Booktimeslot.put(result, timeslot);
					try {
						Log(campus_name, getFormatDate() + " StudentClient " + StudentID + " book a room successfully." 
					+ "Booking information: " + campusName + " " + date + " " + roomNumber + " " + timeslot + " " + result);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return result;
				}			
			}			
		}
		 return " ";
	}

	@Override
	public String getAvailableTimeslot(String ID, String date) {
		loginlocal(ID);
		
		String DVLret = " ";
		String KKLret = " ";
		String WSTret = " ";
		String Localresult = " ";
		
		
		String result = " ";
		Localresult = getavailableLocal(date);
		String command = "getAvailableTimeSlot(" + date + ")";	
		try {
			if(campus_name.equals("DVL")){
				int serverport1 = 25561;
				int serverport2 = 25562;
				result = Localresult + " " + UDPRequest.UDPgetAvailableTimeSlot(command, serverport1);
				result = result + " " + UDPRequest.UDPgetAvailableTimeSlot(command, serverport2);
						
			}
			else if(campus_name.equals("KKL")){
				int serverport1 = 25560;
				int serverport2 = 25562;
				result = UDPRequest.UDPgetAvailableTimeSlot(command, serverport1);
				result = result + " " + Localresult;
				result = result + " " + UDPRequest.UDPgetAvailableTimeSlot(command, serverport2);
			}
			else{
				int serverport1 = 25560;
				int serverport2 = 25561;
				result = UDPRequest.UDPgetAvailableTimeSlot(command, serverport1);
				result = result + " " + UDPRequest.UDPgetAvailableTimeSlot(command, serverport2);
				result = result + Localresult;
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Log(campus_name, getFormatDate() + " StudentClient " + ID + " check on number of available time slots");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public boolean cancelBook(String studentID, String bookingID) {
		login(studentID);
		String date = Bookdatemap.get(bookingID);
		String timeslot = Booktimeslot.get(bookingID);
		String command = "cancelBooking(" + bookingID + "," + studentID + ")";
		Boolean cancelled = false;
		int serverport;
		if(cancelLocol(bookingID,studentID)){
			cancelled = true;
		}
		else{
			try {
				if(campus_name.equals("DVL")){
					serverport = 25561;
					if(UDPRequest.UDPcancelBooking(command,serverport)){
						cancelled = true;
					}
					else{
						serverport = 25562;
						cancelled = UDPRequest.UDPcancelBooking(command, serverport);
					}
				}
				else if(campus_name.equals("KKL")){
					serverport = 25560;
					if(UDPRequest.UDPcancelBooking(command,serverport)){
						cancelled = true;
					}
					else{
						serverport = 25562;
						cancelled = UDPRequest.UDPcancelBooking(command, serverport);
					}
				}
				else if(campus_name.equals("WST")){
					serverport = 25560;
					if(UDPRequest.UDPcancelBooking(command,serverport)){
						cancelled = true;
					}
					else{
						serverport = 25561;
						cancelled = UDPRequest.UDPcancelBooking(command, serverport);
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}	
		if(cancelled){
			Altercount(studentID,date);
			Bookdatemap.remove(bookingID);
			try {
				Log(campus_name, getFormatDate() + " StudentClient " + studentID + " cancelBooking successfully." + " Booking information: "
			+ bookingID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		else{
			try {
				Log(campus_name, getFormatDate() + " StudentClient " + studentID + " cancelBooking failed");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}

//	@Override
//	public void StartServer(String campus) {
//		// TODO Auto-generated method stub
//		
//	}
	
	@Override
	public boolean login(String ID){
		return true;
	}
	
	public boolean loginlocal(String ID){
		Boolean exist = false;

		for(int i = 0; i < studentClients.size(); i ++){
			if(studentClients.get(i).studentID.equals(ID)){
				exist = true;
				break;
			}		
		}
		if(!exist){
			StudentClient newStudent = new StudentClient();
			newStudent.studentID = ID;
			studentClients.add(newStudent);
		}
		System.out.println("StudentClient " + ID + " log in");
		try {
			Log(campus_name, getFormatDate() + " StudentClient " + ID + " log in" );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
			
	}
	

//	@Override
//	public boolean adminLogin(String adminID) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean studentLogin(String studentID) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	@Override
	public String changeReservation(String studentID, String booking_id, String new_campus_name, String new_room_no, String new_time_slot) {
		loginlocal(studentID);
		if(!Bookdatemap.containsKey(booking_id))
			return " ";
		String date = Bookdatemap.get(booking_id);
		
		String result = " ";
		try {
			lock.lock();
			Altercount(studentID, date);			
			result = bookRoom(studentID, new_campus_name, new_room_no, date, new_time_slot);
			if(result.equals(" ")){
				Addcount(studentID, date);
				return " ";
			}
			else{
				if(cancelBook(studentID, booking_id)){
					Addcount(studentID, date);
					return result;
				}
				else{
					cancelBook(studentID, result);
					Addcount(studentID, date);
					return " ";
				}
			}
		} finally {
			lock.unlock();
		}
	}
	
public Boolean cancelLocol(String bookingID,String studentID){
		
		try {
			lock.lock();
			if(BookIDmap.containsKey(bookingID)){
				if(BookIDmap.get(bookingID).Booked_by.equals(studentID)){	
					BookIDmap.get(bookingID).Booked_by = " ";
					BookIDmap.get(bookingID).bookingID = " ";
					BookIDmap.get(bookingID).booked = false;
					BookIDmap.remove(bookingID);	
					return true;
				}
			}
		} finally {
			lock.unlock();
		}
		
		return false;
	}
	
	public Forward incertbook(String roomNumber, String date, String timeslot, String StudentID){
		int random;
		String bookingID;
		Forward forward = new Forward();
		try {
			lock.lock();
			if(hashMap.containsKey(date)){
				if(hashMap.get(date).containsKey(roomNumber)){
					for(int i = 0; i < hashMap.get(date).get(roomNumber).size(); i ++){
						if(hashMap.get(date).get(roomNumber).get(i).timeslot.equals(timeslot) && 
								(!hashMap.get(date).get(roomNumber).get(i).booked)){
							
							random = r.nextInt(100000);
							
							bookingID = campus_name + Integer.toString(random) + StudentID;
							hashMap.get(date).get(roomNumber).get(i).Booked_by = StudentID;
							hashMap.get(date).get(roomNumber).get(i).bookingID = bookingID;
							hashMap.get(date).get(roomNumber).get(i).booked = true;
							forward.i = i;
							forward.s = bookingID;
							return forward;
						}
					}
				}
			}
		} finally {
			lock.unlock();
		}
		return forward;
	}
	
	public String bookLocol(String roomNumber, String date, String timeslot, String StudentID){
		String result = " ";
		int sequence;
		Forward temp = incertbook(roomNumber, date, timeslot, StudentID);
		sequence = temp.i;
		result = temp.s;
		if(result.equals(" ")){
	
			return " ";
		}
		else{
			try {
				lock.lock();
				BookIDmap.put(result, hashMap.get(date).get(roomNumber).get(sequence));
			} finally {
				lock.unlock();
			}
			return result;
		}
	}
	
	
	public String getavailableLocal(String date){
		String result = " ";
		int availablenum = 0;
		
		try {
			lock.lock();
			if(hashMap.containsKey(date)){
				HashMap<String, ArrayList<Bookrecord>> teMap = new HashMap<>();
				teMap = hashMap.get(date);
				Iterator iterator = teMap.keySet().iterator();
				while(iterator.hasNext()){
					ArrayList<Bookrecord> templist = new ArrayList<>();
					templist = teMap.get(iterator.next());
					for(int i = 0 ; i < templist.size();i ++){
						if(!templist.get(i).booked)
							availablenum ++;
					}
				}	
				}
		} finally {
			lock.unlock();
		}
		result = campus_name + " " + Integer.toString(availablenum);	
		return result;
	}
	
	public void Altercount(String studentID,String date){
		int week = getweek(date);
		for(int i = 0; i < studentClients.size();i ++){
			if(studentClients.get(i).studentID.equals(studentID) && studentClients.get(i).BookingCount[week] >= 1){
				studentClients.get(i).BookingCount[week] --;		
			}
		}
	}
	
	public void AltercountLocal(String studentID,String date){
		int week = getweek(date);
		for(int i = 0; i < studentClients.size();i ++){
			if(studentClients.get(i).studentID.equals(studentID) && studentClients.get(i).BookingCount[week] >= 1){
				studentClients.get(i).BookingCount[week] --;		
			}
		}
	}
	
	public void Addcount(String studentID,String date){
		int week = getweek(date);
		for(int i = 0; i < studentClients.size();i ++){
			if(studentClients.get(i).studentID.equals(studentID) && studentClients.get(i).BookingCount[week] >= 1){
				studentClients.get(i).BookingCount[week] ++;		
			}
		}
	}
	
public Boolean incertRecord(String room_Number, String date, String timeslot){
		
		try {
			lock.lock();
			if(hashMap.containsKey(date)){
				if(hashMap.get(date).containsKey(room_Number)){
					
					Boolean exist = false;
					
					for(int i = 0; i< hashMap.get(date).get(room_Number).size(); i++){
						if(hashMap.get(date).get(room_Number).get(i).timeslot.equals(timeslot)){
							exist = true;
						}					
					}
					if(!exist){
						Bookrecord temp = new Bookrecord();
						temp = CreateRecord(timeslot);
						hashMap.get(date).get(room_Number).add(temp);
						System.out.println("Adminclient create a room Successfully");
						return true;
					}
					else{
						System.out.println("Adminclient create a room Unsuccessfully");
						return false;
					}
				}
				else{
					ArrayList<Bookrecord> temprecord = new ArrayList<>();
					temprecord.add(CreateRecord(timeslot));
					hashMap.get(date).put(room_Number, temprecord);
					System.out.println("Adminclient create a room Successfully");
					return true;
				}
			}
			else {	
				ArrayList<Bookrecord> temprecord = new ArrayList<>();
				temprecord.add(CreateRecord(timeslot));
				HashMap<String, ArrayList<Bookrecord>> tempmap = new HashMap<>();
				tempmap.put(room_Number, temprecord);
				hashMap.put(date, tempmap);	
				System.out.println("Adminclient create a room Successfully");
				return true;
			}
		} finally {
			lock.unlock();
		}
	
	}

public Bookrecord CreateRecord(String timeslot){
	int random =(int)(Math.random()*90000 + 10000);
	Bookrecord record = new Bookrecord();
	record.timeslot = timeslot;
	record.Booked_by = " ";
	record.bookingID = " ";
	record.RecordID = "RR" + Integer.toString(random);
	return record;
}	

public static int getweek(String date){
	int week = 0;
	String arg[] = date.split("-");
	Double month =  (double) Integer.parseInt(arg[1]);
	Double day = (double) Integer.parseInt(arg[2]);
	week = (int) ((month - 1) * 4 + Math.ceil(day/7));	
	return week;
}

public String getFormatDate(){
    Date date = new Date();
    long times = date.getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateString = formatter.format(date);
    return dateString;
}	
	
public static void Log(String serverID,String Message) throws Exception{
		
		String path = "/Users/WBQ/Documents/workspace/RoomResrvSys/ServerLog/" + serverID + ".txt";
		FileWriter fileWriter = new FileWriter(path,true);
		BufferedWriter bf = new BufferedWriter(fileWriter);
		bf.write(Message + "\n");
		bf.close();
	}

}
