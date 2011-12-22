package jp.go.aist.rtm.RTC.util;


abstract public class IiopAddressCompHelper
{
  private static String  _id = "IDL:jp.go.aist.rtm.RTC.util/IiopAddressComp:1.0";

  public static void insert (org.omg.CORBA.Any a, jp.go.aist.rtm.RTC.util.IiopAddressComp that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static jp.go.aist.rtm.RTC.util.IiopAddressComp extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [2];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "HostID",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_short);
          _members0[1] = new org.omg.CORBA.StructMember (
            "Port",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (jp.go.aist.rtm.RTC.util.IiopAddressCompHelper.id (), "IiopAddressComp", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static jp.go.aist.rtm.RTC.util.IiopAddressComp read (org.omg.CORBA.portable.InputStream istream)
  {
    jp.go.aist.rtm.RTC.util.IiopAddressComp value = new jp.go.aist.rtm.RTC.util.IiopAddressComp ();
    value.HostID = istream.read_string ();
    value.Port = istream.read_short ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, jp.go.aist.rtm.RTC.util.IiopAddressComp value)
  {
    ostream.write_string (value.HostID);
    ostream.write_short (value.Port);
  }

}
