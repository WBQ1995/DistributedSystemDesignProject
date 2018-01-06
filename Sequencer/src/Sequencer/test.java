package Sequencer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class test {		
		public static void main(String[] args) {
			
			
		boolean b = Boolean.parseBoolean("true");
		System.out.println(b);
			// TODO Auto-generated method stub
			Scanner input = new Scanner(System.in);
			DatagramSocket aSocket = null;
			try {
				aSocket = new DatagramSocket();
					
				while(true){
					
					String s = input.nextLine();
					byte []m = s.getBytes();
					InetAddress ahost = InetAddress.getByName("172.20.10.3");
					int serverport = 6788;
					DatagramPacket request = new DatagramPacket(m, m.length, ahost, 13370);
					
				aSocket.send(request);
				}
				}
				
			catch (SocketException e) {
				System.out.println("Socket: " + e.getMessage());
			}
			
			catch (IOException e) {
				System.out.println("IO: " + e.getMessage());
			}
				
			
			finally {
				if(aSocket != null)
					aSocket.close();
			}
			

		}

}
