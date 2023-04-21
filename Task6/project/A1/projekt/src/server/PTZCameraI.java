package server;

import SmartHome.*;
import com.zeroc.Ice.Current;

public class PTZCameraI implements PTZCamera {
    final private String identifier;
    final private int minXAngle = 0;
    final private int maxXAngle = 180;
    final private int minYAngle = 0;
    final private int maxYAngle = 90;

    private State state;
    private State nightVisionState;
    private CameraPosition position;

    public PTZCameraI(String identifier){
        this.identifier = identifier;

        this.state = State.OFF;
        this.nightVisionState = State.OFF;
        this.position = new CameraPosition(0,0);
    }

    public String getIdentifier(){
        return this.identifier;
    }

    @Override
    public CameraInfo getCameraInfo(Current current) {
        System.out.println("Client call *" + this.identifier + "* info");

        return new CameraInfo(this.state, this.nightVisionState);
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
            System.out.println("Client changed *" + this.identifier + "* nightVision state to: " + state);
        }
    }

    @Override
    public PTZCameraInfo getPTZCameraInfo(Current current) {
        System.out.println("Client call *" + this.identifier + "* PTZinfo");

        PTZCameraInfo info = new PTZCameraInfo(this.state, this.nightVisionState, this.position);

        return info;
    }

    @Override
    public void changeCameraPosition(CameraPosition position, Current current) throws DeviceOff, OutOfRangeValue {
        if(this.state == State.OFF){
            throw new DeviceOff();
        }
        if(position.xAngle<-maxXAngle || position.xAngle>maxXAngle || position.yAngle<-maxYAngle || position.yAngle>maxYAngle){
            throw new OutOfRangeValue("Invalid position change value: should be between -180 and 180 (X) and between -90 and 90 (Y)");
        }

        // Changing position out of the range (fix position to range)
        if(this.position.xAngle+position.xAngle>maxXAngle){
            position.xAngle -= 180;
        }
        else if(this.position.xAngle+position.xAngle<minXAngle){
            position.xAngle += 180;
        }
        if(this.position.yAngle+position.yAngle>maxYAngle){
            position.yAngle -= 180;
        }
        else if(this.position.yAngle+position.yAngle<minYAngle){
            position.yAngle += 180;
        }

        System.out.println("Client call *" + this.identifier + "* change position by: (" + position.xAngle + ", " + position.yAngle + ")");

        this.position = new CameraPosition(this.position.xAngle+position.xAngle, this.position.yAngle+position.yAngle);
    }

    @Override
    public void setCameraPosition(CameraPosition position, Current current) throws DeviceOff, OutOfRangeValue {
        if(this.state == State.OFF){
            throw new DeviceOff();
        }
        if(position.xAngle<minXAngle || position.xAngle>maxXAngle || position.yAngle<minYAngle || position.yAngle>maxYAngle){
            throw new OutOfRangeValue("Invalid position set value: should be between 0 and 180 (X) and between 0 and 90 (Y)");
        }

        System.out.println("Client call *" + this.identifier + "* change position to: (" + position.xAngle + ", " + position.yAngle + ")");

        this.position = new CameraPosition(position.xAngle, position.yAngle);
    }
}
