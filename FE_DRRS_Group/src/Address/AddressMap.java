package Address;

public class AddressMap {
	
	private Address frontEnd;
	private Address sequencer;
	private Address replica_1;
	private Address replica_2;
	private Address replica_3;
	
	public AddressMap() {
		frontEnd = new Address("frontEnd", "172.20.10.13", 13360);
		sequencer = new Address("sequencer", "172.20.10.13", 13370);
		replica_1 = new Address("Replica_1", "172.20.10.14", 13350);
		replica_2 = new Address("Replica_2", "172.20.10.4", 13350);
		replica_3 = new Address("Replica_3", "172.20.10.3", 13350);
		
	}
	
	public Address get(String name){
		if(name.equals("frontEnd")) return frontEnd;
		if(name.equals("sequencer")) return sequencer;
		if(name.equals("Replica_1")) return replica_1;
		if(name.equals("Replica_2")) return replica_2;
		if(name.equals("Replica_3")) return replica_3;
		
		return null;
	}

}
