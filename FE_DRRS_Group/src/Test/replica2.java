package Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class replica2 {

	public static void main(String[] args) throws SocketException {
		
		DatagramSocket Socket = new DatagramSocket(13351);
		try {
	    	  	// build a listening thread;
	    	  	byte[] buffer = new byte[500];
	    	  		
			 	while(true){
			 		
			 		System.out.println("Replica_2 listening");
			 		DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			 		Socket.receive(request);
			 		
			 		//System.out.println("Request getlength: " + request.getLength());
			 		
			 		byte[] newBuffer = new byte[request.getLength()];
			 		System.arraycopy(buffer, 0, newBuffer, 0, request.getLength());
			 		
			 		//analyze the request, and get the appropriate methods;
			 		String message = new String(newBuffer).trim();
			 		System.out.println("received the message  " + message);

			 	}

		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
	    }catch (IOException e) {System.out.println("IO: " + e.getMessage());
//	    }catch (Exception e) {System.out.println("Exception: " + e.getMessage());
	    }finally {if(Socket != null) Socket.close();
	    }
	}

}
