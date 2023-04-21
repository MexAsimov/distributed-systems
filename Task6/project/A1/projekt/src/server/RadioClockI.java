package server;

import SmartHome.*;
import com.zeroc.Ice.Current;

public class RadioClockI implements RadioClock {
    final private String identifier;
    final private int minVol = 0;
    final private int maxVol = 100;
    final private float minFreq = 87.5F;
    final private float maxFreq = 108.0F;

    private State radioState;
    private int volume;
    private float freq;
    private State alarmState;
    TimeOfDay alarmTime;

    public RadioClockI(String identifier) {
        this.identifier = identifier;

        this.radioState = State.OFF;
        this.volume = 20;
        this.freq = 87.5F;
        this.alarmState = State.OFF;
        this.alarmTime = null;
    }

    @Override
    public RadioClockInfo getRadioClockInfo(Current current) {
        System.out.println("Client call *" + this.identifier + "* info");

        return new RadioClockInfo(this.radioState, this.volume, this.freq, this.alarmState, this.alarmTime);
    }

    @Override
    public void setRadioState(State state, Current current) {
        if(this.radioState != state) {
            this.radioState = state;
            System.out.println("Client changed *" + this.identifier + "* radio state to: " + this.radioState);
        }
    }

    @Override
    public void setVolume(int volume, Current current) throws DeviceOff, OutOfRangeValue {
        if(this.radioState == State.OFF){
            throw new DeviceOff();
        }
        if(volume < minVol || volume > maxVol){
            throw new OutOfRangeValue("Invalid volume value: should be between 0 and 100");
        }
        if(this.volume != volume) {
            this.volume = volume;
            System.out.println("Client changed *" + this.identifier + "* radio volume to: " + this.volume);
        }
    }

    @Override
    public void setFrequency(float hertz, Current current) throws DeviceOff, OutOfRangeValue {
        if(this.radioState == State.OFF){
            throw new DeviceOff();
        }
        if(hertz < minFreq || hertz > maxFreq){
            throw new OutOfRangeValue("Invalid frequency value: should be between 87.5 and 108");
        }
        if(this.freq != hertz){
            this.freq = hertz;
            System.out.println("Client changed *" + this.identifier + "* radio frequency to: " + this.freq);
        }
    }

    @Override
    public void setAlarmState(State state, Current current) {
        if(this.alarmState != state){
            this.alarmState = state;
            System.out.println("Client changed *" + this.identifier + "* alarm state to: " + this.alarmState);
        }
    }

    @Override
    public void setAlarmTime(TimeOfDay time, Current current) throws OutOfRangeValue {
        if(time.hour > 23 || time.hour < 0 || time.minute > 59 || time.minute < 0){
            throw new OutOfRangeValue("Invalid time value: should be between 0:00 and 23:59");
        }
        this.alarmTime = time;
        String minute = String.format("%02d", this.alarmTime.minute);
        System.out.println("Client set *" + this.identifier + "* alarm time as: " + this.alarmTime.hour + ":" + minute);
    }
}
