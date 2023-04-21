
#ifndef SMART_HOME_ICE
#define SMART_HOME_ICE

module SmartHome
{
  exception NoInput {};

  exception DeviceOff {
    string reason = "Device has to be on";
  };

  exception OutOfRangeValue {
    string reason;
  };

  enum State { ON, OFF }; // if applicable

  struct HeaterInfo
  {
    State state;
    float actualHeaterTemp;
  };

  interface Heater
  {
    idempotent HeaterInfo getHeaterInfo();

    idempotent void setState(State state);
    idempotent void setTemp(float temp) throws DeviceOff, OutOfRangeValue;
  };

  struct CameraInfo
  {
    State state;
    State nightVisionState;
  };

  interface Camera
  {
    idempotent CameraInfo getCameraInfo();

    idempotent void setState(State state);
    idempotent void setNightVisionState(State state) throws DeviceOff;
  };

  struct CameraPosition
    {
      float xAngle;
      float yAngle;
    };

  struct PTZCameraInfo {
    State state;
    State nightVisionState;
    CameraPosition position;
  };

  interface PTZCamera extends Camera
  {
    idempotent PTZCameraInfo getPTZCameraInfo();

    void changeCameraPosition(CameraPosition position) throws DeviceOff, OutOfRangeValue;
    idempotent void setCameraPosition(CameraPosition position) throws DeviceOff, OutOfRangeValue;
  };

  struct TimeOfDay // only hour and minutes for alarm clock
  {
    short hour;
    short minute;
  };

  struct RadioClockInfo
  {
    State radioState;
    int volume;
    float frequency;
    State alarmState;
    TimeOfDay alarmTime;
  };

  interface RadioClock
  {
    idempotent RadioClockInfo getRadioClockInfo();

    idempotent void setRadioState(State state);
    idempotent void setVolume(int volume) throws DeviceOff, OutOfRangeValue;
    idempotent void setFrequency(float hertz) throws DeviceOff, OutOfRangeValue;

    idempotent void setAlarmState(State state);
    idempotent void setAlarmTime(TimeOfDay time) throws OutOfRangeValue;
  };

};

#endif
