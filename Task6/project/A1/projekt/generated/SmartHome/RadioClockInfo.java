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

public class RadioClockInfo implements java.lang.Cloneable,
                                       java.io.Serializable
{
    public State radioState;

    public int volume;

    public float frequency;

    public State alarmState;

    public TimeOfDay alarmTime;

    public RadioClockInfo()
    {
        this.radioState = State.ON;
        this.alarmState = State.ON;
        this.alarmTime = new TimeOfDay();
    }

    public RadioClockInfo(State radioState, int volume, float frequency, State alarmState, TimeOfDay alarmTime)
    {
        this.radioState = radioState;
        this.volume = volume;
        this.frequency = frequency;
        this.alarmState = alarmState;
        this.alarmTime = alarmTime;
    }

    public boolean equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        RadioClockInfo r = null;
        if(rhs instanceof RadioClockInfo)
        {
            r = (RadioClockInfo)rhs;
        }

        if(r != null)
        {
            if(this.radioState != r.radioState)
            {
                if(this.radioState == null || r.radioState == null || !this.radioState.equals(r.radioState))
                {
                    return false;
                }
            }
            if(this.volume != r.volume)
            {
                return false;
            }
            if(this.frequency != r.frequency)
            {
                return false;
            }
            if(this.alarmState != r.alarmState)
            {
                if(this.alarmState == null || r.alarmState == null || !this.alarmState.equals(r.alarmState))
                {
                    return false;
                }
            }
            if(this.alarmTime != r.alarmTime)
            {
                if(this.alarmTime == null || r.alarmTime == null || !this.alarmTime.equals(r.alarmTime))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int hashCode()
    {
        int h_ = 5381;
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, "::SmartHome::RadioClockInfo");
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, radioState);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, volume);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, frequency);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, alarmState);
        h_ = com.zeroc.IceInternal.HashUtil.hashAdd(h_, alarmTime);
        return h_;
    }

    public RadioClockInfo clone()
    {
        RadioClockInfo c = null;
        try
        {
            c = (RadioClockInfo)super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return c;
    }

    public void ice_writeMembers(com.zeroc.Ice.OutputStream ostr)
    {
        State.ice_write(ostr, this.radioState);
        ostr.writeInt(this.volume);
        ostr.writeFloat(this.frequency);
        State.ice_write(ostr, this.alarmState);
        TimeOfDay.ice_write(ostr, this.alarmTime);
    }

    public void ice_readMembers(com.zeroc.Ice.InputStream istr)
    {
        this.radioState = State.ice_read(istr);
        this.volume = istr.readInt();
        this.frequency = istr.readFloat();
        this.alarmState = State.ice_read(istr);
        this.alarmTime = TimeOfDay.ice_read(istr);
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, RadioClockInfo v)
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

    static public RadioClockInfo ice_read(com.zeroc.Ice.InputStream istr)
    {
        RadioClockInfo v = new RadioClockInfo();
        v.ice_readMembers(istr);
        return v;
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, java.util.Optional<RadioClockInfo> v)
    {
        if(v != null && v.isPresent())
        {
            ice_write(ostr, tag, v.get());
        }
    }

    static public void ice_write(com.zeroc.Ice.OutputStream ostr, int tag, RadioClockInfo v)
    {
        if(ostr.writeOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            int pos = ostr.startSize();
            ice_write(ostr, v);
            ostr.endSize(pos);
        }
    }

    static public java.util.Optional<RadioClockInfo> ice_read(com.zeroc.Ice.InputStream istr, int tag)
    {
        if(istr.readOptional(tag, com.zeroc.Ice.OptionalFormat.FSize))
        {
            istr.skip(4);
            return java.util.Optional.of(RadioClockInfo.ice_read(istr));
        }
        else
        {
            return java.util.Optional.empty();
        }
    }

    private static final RadioClockInfo _nullMarshalValue = new RadioClockInfo();

    /** @hidden */
    public static final long serialVersionUID = -1824842331L;
}
