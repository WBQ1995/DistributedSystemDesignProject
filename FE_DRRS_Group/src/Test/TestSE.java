package Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import Address.AddressMap;

public class TestSE {

	public static void main(String[] args) throws SocketException, InterruptedException {
		
		int i = 1;
		AddressMap ad = new AddressMap();
		DatagramSocket Socket = new DatagramSocket(13370);
		 try {
	    	  	// build a listening thread;
	    	  	byte[] buffer = new byte[500];
	    	  		
			 	while(true){
			 		
			 		System.out.println("Sequencer listening");
			 		DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			 		Socket.receive(request);
			 		
			 		//System.out.println("Request getlength: " + request.getLength());
			 		
			 		byte[] newBuffer = new byte[request.getLength()];
			 		System.arraycopy(buffer, 0, newBuffer, 0, request.getLength());
			 		
			 		//analyze the request, and get the appropriate methods;
			 		String message = new String(newBuffer).trim();
			 		System.out.println("received the message  " + message);
			 		
			 		//FEImpl.replyBuffer.put(message);
			 		//System.out.println(FEImpl.replyBuffer);
			 		
			 		if (message.indexOf("login") >= 0) {
			 			Thread.sleep(100);
			 			
			 			String reply1 = i +" Replica_1 REQ" + i +" true";
			 			String reply2 = i +" Replica_2 REQ" + i +" false";
			 			String reply3 = i +" Replica_3 REQ" + i +" true";
			 			
			 			InetAddress feIP = InetAddress.getByName("localhost");
			 			int fePort = Integer.valueOf(ad.get("frontEnd").getPort());
			 			
			 			byte[] byteReply = reply1.getBytes();
				  		DatagramPacket reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
				  		byteReply = reply2.getBytes();
				  		reply = new DatagramPacket(byteReply, byteReply.length,feIP, fePort);
				  		Socket.send(reply);
				  		
				  		byteReply = reply3.getBytes();
				  		reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
				  		i++;
			 		}
			 			
			 		if (message.indexOf("createRoom") >= 0) {
			 			
			 			
			 			//DVL REQ5 createRoom DVLA1000 201, 2017-10-01, 7:30-9:30, 9:30-11:30, 11:30-13:30
			 			String reply1 = i +" Replica_1 REQ" + i +" true true true  true true";
			 			String reply2 = i +" Replica_2 REQ" + i +" true true true  true true";
			 			String reply3 = i +" Replica_3 REQ" + i +" false true true  true true";
			 			
			 			InetAddress feIP = InetAddress.getByName("localhost");
			 			int fePort = Integer.valueOf(ad.get("frontEnd").getPort());
			 			
			 			byte[] byteReply = reply1.getBytes();
				  		DatagramPacket reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
				  		byteReply = reply2.getBytes();
				  		reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
//				  		byteReply = reply3.getBytes();
//				  		reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
//				  		Socket.send(reply);
				  		
				  		i++;
			 		}
			 		
			 		if (message.indexOf("deleteRoom") >= 0) {
			 			//Thread.sleep(100);
			 			
			 			//DVL REQ5 createRoom DVLA1000 201, 2017-10-01, 7:30-9:30, 9:30-11:30, 11:30-13:30
			 			String reply1 = i +" Replica_1 REQ" + i +" true true true  true true";
			 			String reply2 = i +" Replica_2 REQ" + i +" true true true  true true";
			 			String reply3 = i +" Replica_3 REQ" + i +" true true true  true true";
			 			
			 			InetAddress feIP = InetAddress.getByName("localhost");
			 			int fePort = Integer.valueOf(ad.get("frontEnd").getPort());
			 			
			 			byte[] byteReply = reply1.getBytes();
				  		DatagramPacket reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
				  		byteReply = reply2.getBytes();
				  		reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
				  		byteReply = reply3.getBytes();
				  		reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
				  		i++;
			 		}
			 		
			 		if (message.indexOf("getAvailableTimeslot") >= 0) {
			 			//Thread.sleep(100);
			 			
			 			//DVL REQ5 createRoom DVLA1000 201, 2017-10-01, 7:30-9:30, 9:30-11:30, 11:30-13:30
			 			String reply1 = i +" Replica_1 REQ" + i +" DVL 2 KKL 3 WST 4";
			 			String reply2 = i +" Replica_2 REQ" + i +" DVL 2 KKL 3 WST 4";
			 			String reply3 = i +" Replica_3 REQ" + i +" DVL 2 KKL 3 WST 5";
			 			
			 			InetAddress feIP = InetAddress.getByName("localhost");
			 			int fePort = Integer.valueOf(ad.get("frontEnd").getPort());
			 			
			 			byte[] byteReply = reply1.getBytes();
				  		DatagramPacket reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
				  		byteReply = reply2.getBytes();
				  		reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
				  		byteReply = reply3.getBytes();
				  		reply = new DatagramPacket(byteReply, byteReply.length, feIP, fePort);
				  		Socket.send(reply);
				  		
				  		i++;
			 		}	
			 			
			 	
			 		
			 	}		
	        
	      }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
	      }catch (IOException e) {System.out.println("IO: " + e.getMessage());
//	      }catch (Exception e) {System.out.println("Exception: " + e.getMessage());
	      }finally {if(Socket != null) Socket.close();
	      }

	}

}


