package FEAPP;


/**
* FEAPP/ArrayBooleanHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /Users/Ling/Desktop/eclipse-workspace/FE_DRRS_Group/src/FE.idl
* Tuesday, November 28, 2017 5:21:42 PM EST
*/

public final class ArrayBooleanHolder implements org.omg.CORBA.portable.Streamable
{
  public boolean value[] = null;

  public ArrayBooleanHolder ()
  {
  }

  public ArrayBooleanHolder (boolean[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = FEAPP.ArrayBooleanHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    FEAPP.ArrayBooleanHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return FEAPP.ArrayBooleanHelper.type ();
  }

}
