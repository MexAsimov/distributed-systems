package server;

import SmartHome.*;
import com.zeroc.Ice.Current;

public class HeaterI implements Heater {
    final private String identifier;
    final private int minTemp = 15;
    final private int maxTemp = 50; // assuming that it is large heater

    private State state;
    private float actualHeaterTemp;

    public HeaterI(String identifier){
        this.identifier = identifier;

        this.state = State.OFF;
        this.actualHeaterTemp = 0;
    }

    public String getIdentifier(){
        return this.identifier;
    }

    @Override
    public HeaterInfo getHeaterInfo(Current current) {
        System.out.println("Client call *" + this.identifier + "* info");

        HeaterInfo info = new HeaterInfo(this.state, this.actualHeaterTemp);

        return info;
    }

    @Override
    public void setState(State state, Current current) {
        if(this.state != state){
            this.state = state;
            if(state == State.OFF){
                this.actualHeaterTemp = 0;
            }
            else{
                this.actualHeaterTemp = 18;
            }
            System.out.println("Client changed *" + this.identifier + "* state to: " + state);
        }
    }

    @Override
    public void setTemp(float temp, Current current) throws DeviceOff, OutOfRangeValue {
        if(this.state == State.OFF){
            throw new DeviceOff();
        }
        if(temp<minTemp || temp>maxTemp){
            throw new OutOfRangeValue("Invalid temperature value: should be between " + minTemp + " and " + maxTemp);
        }
        if(this.actualHeaterTemp != temp){
            this.actualHeaterTemp = temp;
            System.out.println("Client changed *" + this.identifier + "* temperature to: " + temp);
        }
    }
}
