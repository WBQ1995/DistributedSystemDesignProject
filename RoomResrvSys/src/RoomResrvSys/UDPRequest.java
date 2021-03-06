package RoomResrvSys;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPRequest {

	public UDPRequest() {
		super();
	}
	
	public static String request(String command,int serverport) throws SocketException{
		
		String replymsg = " ";
		DatagramSocket requestsocket = new DatagramSocket();
		try {
			
			byte []m = command.getBytes();
			InetAddress ahost = InetAddress.getByName("localhost");
			DatagramPacket request = new DatagramPacket(m,m.length,ahost,serverport);
			requestsocket.send(request);
			
			byte []buffer = new byte[1000];		
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			requestsocket.receive(reply);
			
			byte []message = new byte[reply.getLength()];
			System.arraycopy(buffer, 0, message, 0, reply.getLength());
			replymsg = new String(message);
		} 
		catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
		
		finally {
			if(requestsocket != null)
				requestsocket.close();
		}
			
		return replymsg;
	}
	
	public static String UDPgetAvailableTimeSlot(String command,int serverport) throws SocketException{
		String result = ""; 
		result = request(command, serverport);
		return result;
		
	}
	public static String UDPbookroom(String command,int serverport) throws SocketException{
		String result = " ";
		result = request(command, serverport);
		return result;
	}
	
	public static Boolean UDPcancelBooking(String command,int serverport) throws SocketException{
		String result = " ";
		result = request(command, serverport);
		if(result.equals("successful"))
			return true;
		else
			return false;
	}
	
	public static String Altercount(String command, int serverport) throws SocketException{
		String result = " ";
		result = request(command, serverport);
		return result;
	}
	
}
