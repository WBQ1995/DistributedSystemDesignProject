
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;

import FEAPP.*;


public class FrontEnd {
	
	//build CORBA connection
	public static void main(String[] args) {
		
		FEImpl frontEnd = new FEImpl();
		
		try{
		// create and initialize the ORB
	    ORB orb = ORB.init(args, null);

	    // get reference to rootpoa & activate the POAManager
	    POA rootpoa = (POA)orb.resolve_initial_references("RootPOA");
	    rootpoa.the_POAManager().activate();

	    // create servant and register it with the ORB
	    frontEnd.setORB(orb); 

	    // get object reference from the servant
	    org.omg.CORBA.Object ref = rootpoa.servant_to_reference(frontEnd);
	    // and cast the reference to a CORBA reference
	    FE href = FEHelper.narrow(ref);
		  
	    // get the root naming context
	    // NameService invokes the transient name service
	    org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
	      
	    // Use NamingContextExt, which is part of the
	    // Interoperable Naming Service (INS) specification.
	    NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

	    // bind the Object Reference in Naming
	    String name = "FrontEnd";
	    NameComponent path[] = ncRef.to_name(name);
	    ncRef.rebind(path, href);

	    System.out.println(" FE ready and waiting ...");
	    
	    // wait for invocations from clients
	    orb.run();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
