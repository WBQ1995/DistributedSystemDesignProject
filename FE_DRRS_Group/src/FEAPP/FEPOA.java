package FEAPP;


/**
* FEAPP/FEPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/Ling/Desktop/eclipse-workspace/FE_DRRS_Group/src/FE.idl
* Tuesday, November 28, 2017 5:21:42 PM EST
*/

public abstract class FEPOA extends org.omg.PortableServer.Servant
 implements FEAPP.FEOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("Login", new java.lang.Integer (0));
    _methods.put ("createRoom", new java.lang.Integer (1));
    _methods.put ("deleteRoom", new java.lang.Integer (2));
    _methods.put ("bookRoom", new java.lang.Integer (3));
    _methods.put ("getAvailableTimeSlot", new java.lang.Integer (4));
    _methods.put ("cancelBooking", new java.lang.Integer (5));
    _methods.put ("changeReservation", new java.lang.Integer (6));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // FEAPP/FE/Login
       {
         String id = in.read_string ();
         boolean $result = false;
         $result = this.Login (id);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 1:  // FEAPP/FE/createRoom
       {
         String id = in.read_string ();
         String room_Number = in.read_string ();
         String date = in.read_string ();
         String Time_Slots[] = FEAPP.ArrayStringHelper.read (in);
         boolean $result[] = null;
         $result = this.createRoom (id, room_Number, date, Time_Slots);
         out = $rh.createReply();
         FEAPP.ArrayBooleanHelper.write (out, $result);
         break;
       }

       case 2:  // FEAPP/FE/deleteRoom
       {
         String id = in.read_string ();
         String room_Number = in.read_string ();
         String date = in.read_string ();
         String Time_Slots[] = FEAPP.ArrayStringHelper.read (in);
         boolean $result[] = null;
         $result = this.deleteRoom (id, room_Number, date, Time_Slots);
         out = $rh.createReply();
         FEAPP.ArrayBooleanHelper.write (out, $result);
         break;
       }

       case 3:  // FEAPP/FE/bookRoom
       {
         String id = in.read_string ();
         String campusName = in.read_string ();
         String room_Number = in.read_string ();
         String date = in.read_string ();
         String Time_Slots = in.read_string ();
         String $result = null;
         $result = this.bookRoom (id, campusName, room_Number, date, Time_Slots);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // FEAPP/FE/getAvailableTimeSlot
       {
         String id = in.read_string ();
         String date = in.read_string ();
         String $result = null;
         $result = this.getAvailableTimeSlot (id, date);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 5:  // FEAPP/FE/cancelBooking
       {
         String id = in.read_string ();
         String bookingID = in.read_string ();
         boolean $result = false;
         $result = this.cancelBooking (id, bookingID);
         out = $rh.createReply();
         out.write_boolean ($result);
         break;
       }

       case 6:  // FEAPP/FE/changeReservation
       {
         String id = in.read_string ();
         String bookingID = in.read_string ();
         String campusName = in.read_string ();
         String room_Number = in.read_string ();
         String Time_Slots = in.read_string ();
         String $result = null;
         $result = this.changeReservation (id, bookingID, campusName, room_Number, Time_Slots);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:FEAPP/FE:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public FE _this() 
  {
    return FEHelper.narrow(
    super._this_object());
  }

  public FE _this(org.omg.CORBA.ORB orb) 
  {
    return FEHelper.narrow(
    super._this_object(orb));
  }


} // class FEPOA
