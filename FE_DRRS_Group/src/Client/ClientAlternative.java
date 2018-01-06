package Client;

import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import Client.ClientConsole;
import FEAPP.FE;
import FEAPP.FEHelper;


public class ClientAlternative {
	
	private FE h;
	private ClientConsole console = new ClientConsole();
	private String id;
	private String userType;
	
	
	public ClientAlternative(String id) {
		this.id = id;
		userType = id.substring(3, 4);
	}

	public FE getConnection(String[] args) throws Exception {

	     	ORB orb = ORB.init(args, null);

	     	// get the root naming context
	     	org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	     	// Use NamingContextExt instead of NamingContext, 
	     	// part of the Interoperable naming Service.  
	     	NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

	     	// resolve the Object Reference in Naming
	     	String name = "FrontEnd";
	     	h = FEHelper.narrow(ncRef.resolve_str(name));
	     	
	     	return h;
	}
			
	//Login
	public void Login() throws Exception {
		console.login(id, h);	
	}		
			
	public void adminFunc(String command) throws Exception {
		
		if (!userType.equals("A")) {
			System.out.println("Command error, can not find such command.");
		}
		if (command.equals("quit")) {
			System.exit(0);
		}
		else {
			if (command.startsWith("createRoom") || command.startsWith("deleteRoom")) {
				console.createANDdelete(h, command);	
			}
			else {
			System.out.println("Command error, can not find such command.");
			}
		}
	}
	
	public void studentFunc(String command) throws Exception {
		if (!userType.equals("S")) {
			System.out.println("Command error, can not find such command.");
		}
		
		if (command.equals("quit")) {
			System.exit(0);
		}
					
		if (command.startsWith("bookRoom")) {
			console.bookRoom(h, command);			
		}
					
		if (command.startsWith("cancelBooking")) {
			console.cancelBooking(h, command, id);
		}
					
		if (command.startsWith("getAvailableTimeSlot")) {
			//System.out.println("getAvailableTimeSlot begin");
			console.getAvailableTimeSlot(h, command);
		}
					
		if (command.startsWith("changeReservation")) {
			//System.out.println("changeReservation begin");
			console.changeReservation(h, command);
		}
					
	}
}

