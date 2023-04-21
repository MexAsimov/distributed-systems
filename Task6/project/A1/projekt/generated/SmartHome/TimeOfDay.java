//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.7
//
// <auto-generated>
//
// Generated from file `SmartHome.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package SmartHome;

public class TimeOfDay implements java.lang.Cloneable,
                                  java.io.Serializable
{
    public short hour;

    public short minute;

    public TimeOfDay()
    {
    }

    public TimeOfDay(short hour, short minute)
    {
        this.hour = hour;
        this.minute = minute;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        TimeOfDay r = null;
        if(rhs instanceof TimeOfDay)
        {
            r = (TimeOfDay)rhs;
        }

        if(r != null)
        {
            if(this.hour != r.hour)
            {
                return false;
            }
            if(this.minute != r.minute)
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::SmartHome::TimeOfDay");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, hour);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, minute);
        return h_;
    }

    public TimeOfDay clone()
    {
        TimeOfDay c = null;
        try
        {
            c = (TimeOfDay)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        ostr.writeShort(this.hour);
        ostr.writeShort(this.minute);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.hour = istr.readShort();
        this.minute = istr.readShort();
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, TimeOfDay v)
    {
        if(v == null)
        {
            _nullMarshalValue.ice_writeMembers(ostr);
        }
        else
        {
            v.ice_writeMembers(ostr);
        }
    }

    static public TimeOfDay ice_read(com.zeroc.Ice.InputStream istr)
    {
        TimeOfDay v = new TimeOfDay();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<TimeOfDay> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, TimeOfDay v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.VSize))
        {
            ostr.writeSize(4);
            ice_write(ostr, v);
        }
    }

    static public java.util.Optional<TimeOfDay> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.VSize))
        {
            istr.skipSize();
            return java.util.Optional.of(TimeOfDay.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final TimeOfDay _nullMarshalValue = new TimeOfDay();

    /** @hidden */
    public static final long serialVersionUID = -1115021005L;
}
