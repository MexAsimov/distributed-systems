package server;

import SmartHome.Camera;
import SmartHome.CameraInfo;
import SmartHome.DeviceOff;
import SmartHome.State;
import com.zeroc.Ice.Current;

public class CameraI implements Camera {
    private String identifier;

    private State state;
    private State nightVisionState;

    public CameraI(String identifier){
        this.identifier = identifier;

        this.state = State.OFF;
        this.nightVisionState = State.OFF;
    }

    public String getIdentifier(){
        return this.identifier;
    }

    @Override
    public CameraInfo getCameraInfo(Current current) {
        System.out.println("Client call *" + this.identifier + "* info");

        CameraInfo info = new CameraInfo(this.state, this.nightVisionState);

        return info;
    }

    @Override
    public void setState(State state, Current current) {
        if(this.state != state) {
            this.state = state;
            System.out.println("Client changed *" + this.identifier + "* state to: " + state);
        }
    }

    @Override
    public void setNightVisionState(State state, Current current) throws DeviceOff {
        if(this.state == State.OFF){
            throw new DeviceOff();
        }
        if(this.nightVisionState != state) {
            this.nightVisionState = state;
            System.out.println("Client changed *" + this.identifier + "* state to: " + state);
        }
    }
}
