import sys, Ice
Ice.loadSlice("../../slice/SmartHome.ice")
import SmartHome as shi
from termcolor import colored

def print_green(text):
    print(colored(text, 'green'))

def print_red(text):
    print(colored(text, 'red'))

def print_yellow(text):
    print(colored(text, 'yellow'))
 
with Ice.initialize(sys.argv) as communicator:

    servers = 2

    # ===== UNTYPED PROXIES =====
    camera_obj = communicator.propertyToProxy("camera")
    heater_obj = communicator.propertyToProxy("heater")
    ptz_camera_obj = communicator.propertyToProxy("ptzCamera")
    radio_clock_obj = communicator.propertyToProxy("radio")

    camera_out1_obj = communicator.propertyToProxy("outCam1")
    camera_out2_obj = communicator.propertyToProxy("outCam2")
    ptz_camera_out1_obj = communicator.propertyToProxy("outPtz1")
    ptz_camera_out2_obj = communicator.propertyToProxy("outPtz2")
    # ==========================


    print()
    print_green('Established tcp connection with server.')

    print_green('Getting device proxies...')


    # ===== TYPE-SPECIFIED PROXIES =====
    # Checking if server is on (only on first proxy) -> Raise RuntimeError if not
    try:
        camera = shi.CameraPrx.checkedCast(camera_obj)
    except Ice.ConnectionRefusedException:
        raise RuntimeError("Server1 down")

    if not camera_obj:
        raise RuntimeError("Invalid proxy 1")

    heater = shi.HeaterPrx.checkedCast(heater_obj)
    if not heater_obj:
        raise RuntimeError("Invalid proxy 2")

    ptz_camera = shi.PTZCameraPrx.checkedCast(ptz_camera_obj)
    if not ptz_camera_obj:
        raise RuntimeError("Invalid proxy 3")

    radio_clock = shi.RadioClockPrx.checkedCast(radio_clock_obj)
    if not radio_clock_obj:
        raise RuntimeError("Invalid proxy 4")


    if servers == 2:
        try:
            camera_out1 = shi.CameraPrx.checkedCast(camera_out1_obj)
        except Ice.ConnectionRefusedException:
             raise RuntimeError("Server2 down")
        if not camera_out1:
            raise RuntimeError("Invalid proxy 5")


        camera_out2 = shi.CameraPrx.checkedCast(camera_out2_obj)
        if not camera_out2:
            raise RuntimeError("Invalid proxy 6")

        ptz_camera_out1 = shi.PTZCameraPrx.checkedCast(ptz_camera_out1_obj)
        if not ptz_camera_out1:
            raise RuntimeError("Invalid proxy 7")

        ptz_camera_out2 = shi.PTZCameraPrx.checkedCast(ptz_camera_out1_obj)
        if not ptz_camera_out2:
            raise RuntimeError("Invalid proxy 8")

    # ================================


    print()
    print_green('All proxies are valid')

    print("Starting infinite interpreter loop...")


    while True:
        print()
        print()
        print("Type command, Type /x to exit")
        line = input()

        if line == "/x":
            break

        elif line == "heater on":
            heater.setState(shi.State.ON)
            print_green("Heater turned on")

        elif line == "heater off":
            heater.setState(shi.State.OFF)
            print_green("Heater turned off")

        elif line == "heater info":
            info = heater.getHeaterInfo()
            print()
            print_yellow("= Information about heater =")
            print_yellow("State: " + str(info.state))
            print_yellow("Temperature: " + str(round(info.actualHeaterTemp, 2)))

        elif line == "heater set_temp":
            print("Set temp value:", end=" ")
            temp = input()

            try:
                heater.setTemp(float(temp))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be a number")
                continue

            text = "Succesfully set to: " + str(temp)
            print_green(text)

        elif line == "camera on":
            camera.setState(shi.State.ON)
            print_green("Camera turned on")

        elif line == "camera off":
            camera.setState(shi.State.OFF)
            print_green("Camera turned off")

        elif line == "camera info":
            info = camera.getCameraInfo()
            print()
            print_yellow("= Information about camera =")
            print_yellow("State: " + str(info.state))
            print_yellow("NightVision: " + str(info.nightVisionState))

        elif line == "camera nightvision on":
            try:
                camera.setNightVisionState(shi.State.ON)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned on")

        elif line == "camera nightvision off":
            try:
                camera.setNightVisionState(shi.State.OFF)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned off")

        # =======================================================
        # ================================= SERVER 2 CAMERA BLOCK

        elif line == "camera out1 on" and servers == 2:
            camera_out1.setState(shi.State.ON)
            print_green("Camera turned on")

        elif line == "camera out1 off" and servers == 2:
            camera_out1.setState(shi.State.OFF)
            print_green("Camera turned off")

        elif line == "camera out1 info" and servers == 2:
            info = camera_out1.getCameraInfo()
            print()
            print_yellow("= Information about camera outside 1 =")
            print_yellow("State: " + str(info.state))
            print_yellow("NightVision: " + str(info.nightVisionState))

        elif line == "camera out1 nightvision on" and servers == 2:
            try:
                camera_out1.setNightVisionState(shi.State.ON)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned on")

        elif line == "camera out1 nightvision off" and servers == 2:
            try:
                camera_out1.setNightVisionState(shi.State.OFF)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned off")

        elif line == "camera out2 on" and servers == 2:
            camera_out2.setState(shi.State.ON)
            print_green("Camera turned on")

        elif line == "camera out2 off" and servers == 2:
            camera_out2.setState(shi.State.OFF)
            print_green("Camera turned off")

        elif line == "camera out2 info" and servers == 2:
            info = camera_out2.getCameraInfo()
            print()
            print_yellow("= Information about camera outside 2 =")
            print_yellow("State: " + str(info.state))
            print_yellow("NightVision: " + str(info.nightVisionState))

        elif line == "camera out2 nightvision on" and servers == 2:
            try:
                camera_out2.setNightVisionState(shi.State.ON)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned on")

        elif line == "camera out2 nightvision off" and servers == 2:
            try:
                camera_out2.setNightVisionState(shi.State.OFF)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned off")

        # ================================= END OF SERVER 2 CAMERA BLOCK
        # ==============================================================

        elif line == "ptz on":
            ptz_camera.setState(shi.State.ON)
            print_green("Camera PTZ turned on")

        elif line == "ptz off":
            ptz_camera.setState(shi.State.OFF)
            print_green("Camera PTZ turned off")

        elif line == "ptz info":
            info = ptz_camera.getPTZCameraInfo()
            print()
            print_yellow("= Information about PTZ camera =")
            print_yellow("State: " + str(info.state))
            print_yellow("NightVision: " + str(info.nightVisionState))
            print_yellow("XAngle: " + str(round(info.position.xAngle, 2)))
            print_yellow("YAngle: " + str(round(info.position.yAngle, 2)))

        elif line == "ptz nightvision on":
            try:
                ptz_camera.setNightVisionState(shi.State.ON)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned on")

        elif line == "ptz nightvision off":
            try:
                ptz_camera.setNightVisionState(shi.State.OFF)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned off")

        elif line == "ptz change_pos":
            try:
                print("Change X position:", end=" ")
                xChange = float(input())
                print("Change Y position:", end=" ")
                yChange = float(input())
                ptz_camera.changeCameraPosition(shi.CameraPosition(xChange, yChange))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be a number")
                continue

            print_green("Successfully changed position by: (" + str(xChange) + ", " + str(yChange) + ")")

        elif line == "ptz set_pos":
            try:
                print("Set X position:", end=" ")
                xPos = float(input())
                print("Set Y position:", end=" ")
                yPos = float(input())
                ptz_camera.setCameraPosition(shi.CameraPosition(xPos, yPos))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be a number")
                continue

            print_green("Successfully set position to: (" + str(xPos) + ", " + str(yPos) + ")")

        # ====================================================
        # ================================= SERVER 2 PTZ BLOCK

        elif line == "ptz out1 on" and servers == 2:
            ptz_camera_out1.setState(shi.State.ON)
            print_green("Camera PTZ turned on")

        elif line == "ptz out1 off" and servers == 2:
            ptz_camera_out1.setState(shi.State.OFF)
            print_green("Camera PTZ turned off")

        elif line == "ptz out1 info" and servers == 2:
            info = ptz_camera_out1.getPTZCameraInfo()
            print()
            print_yellow("= Information about PTZ camera outside 1 =")
            print_yellow("State: " + str(info.state))
            print_yellow("NightVision: " + str(info.nightVisionState))
            print_yellow("XAngle: " + str(round(info.position.xAngle, 2)))
            print_yellow("YAngle: " + str(round(info.position.yAngle, 2)))

        elif line == "ptz out1 nightvision on" and servers == 2:
            try:
                ptz_camera_out1.setNightVisionState(shi.State.ON)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned on")

        elif line == "ptz out1 nightvision off" and servers == 2:
            try:
                ptz_camera_out1.setNightVisionState(shi.State.OFF)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned off")

        elif line == "ptz out1 change_pos" and servers == 2:
            try:
                print("Change X position:", end=" ")
                xChange = float(input())
                print("Change Y position:", end=" ")
                yChange = float(input())
                ptz_camera_out1.changeCameraPosition(shi.CameraPosition(xChange, yChange))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be a number")
                continue

            print_green("Successfully changed position by: (" + str(xChange) + ", " + str(yChange) + ")")

        elif line == "ptz out1 set_pos" and servers == 2:
            try:
                print("Set X position:", end=" ")
                xPos = float(input())
                print("Set Y position:", end=" ")
                yPos = float(input())
                ptz_camera_out1.setCameraPosition(shi.CameraPosition(xPos, yPos))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be a number")
                continue

            print_green("Successfully set position to: (" + str(xPos) + ", " + str(yPos) + ")")

        elif line == "ptz out2 on" and servers == 2:
            ptz_camera_out2.setState(shi.State.ON)
            print_green("Camera PTZ turned on")

        elif line == "ptz out2 off" and servers == 2:
            ptz_camera_out2.setState(shi.State.OFF)
            print_green("Camera PTZ turned off")

        elif line == "ptz out2 info" and servers == 2:
            info = ptz_camera_out2.getPTZCameraInfo()
            print()
            print_yellow("= Information about PTZ camera outside 2 =")
            print_yellow("State: " + str(info.state))
            print_yellow("NightVision: " + str(info.nightVisionState))
            print_yellow("XAngle: " + str(round(info.position.xAngle, 2)))
            print_yellow("YAngle: " + str(round(info.position.yAngle, 2)))

        elif line == "ptz out2 nightvision on" and servers == 2:
            try:
                ptz_camera_out2.setNightVisionState(shi.State.ON)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned on")

        elif line == "ptz out2 nightvision off" and servers == 2:
            try:
                ptz_camera_out2.setNightVisionState(shi.State.OFF)
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue

            print_green("Nightvision turned off")

        elif line == "ptz out2 change_pos" and servers == 2:
            try:
                print("Change X position:", end=" ")
                xChange = float(input())
                print("Change Y position:", end=" ")
                yChange = float(input())
                ptz_camera_out2.changeCameraPosition(shi.CameraPosition(xChange, yChange))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be a number")
                continue

            print_green("Successfully changed position by: (" + str(xChange) + ", " + str(yChange) + ")")

        elif line == "ptz out2 set_pos" and servers == 2:
            try:
                print("Set X position:", end=" ")
                xPos = float(input())
                print("Set Y position:", end=" ")
                yPos = float(input())
                ptz_camera_out2.setCameraPosition(shi.CameraPosition(xPos, yPos))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be a number")
                continue

            print_green("Successfully set position to: (" + str(xPos) + ", " + str(yPos) + ")")

        # ================================= END OF SERVER 2 PTZ BLOCK
        # ===========================================================

        elif line == "radio on":
            radio_clock.setRadioState(shi.State.ON)
            print_green("Radio turned on")

        elif line == "radio off":
            radio_clock.setRadioState(shi.State.OFF)
            print_green("Radio turned off")

        elif line == "radio info":
            info = radio_clock.getRadioClockInfo()
            print()
            print_yellow("= Information about radio clock =")
            print_yellow("RadioState: " + str(info.radioState))
            print_yellow("Volume: " + str(info.volume))
            print_yellow("Frequency: " + str(round(info.frequency,2)))
            print_yellow("AlarmState: " + str(info.alarmState))
            print_yellow("AlarmTime: " + str(info.alarmTime.hour) + ":" + "{0:0>2}".format(str(info.alarmTime.minute)))

        elif line == "radio set_vol":
            try:
                print("Set volume value:", end=" ")
                volume = input()
                radio_clock.setVolume(int(volume))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be an integer number")
                continue

            print_green("Successfully set volume to: " + volume)

        elif line == "radio set_freq":
            try:
                print("Set frequency value:", end=" ")
                freq = input()
                radio_clock.setFrequency(float(volume))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be a number")
                continue

            print_green("Successfully set frequency to: " + freq)

        elif line == "alarm on":
            radio_clock.setAlarmState(shi.State.ON)
            print_green("Alarm turned on")

        elif line == "alarm off":
            radio_clock.setAlarmState(shi.State.OFF)
            print_green("Alarm turned off")

        elif line == "alarm set_time":
            try:
                print("Set hour:", end=" ")
                hour = int(input())
                print("Set minute:", end=" ")
                minute = int(input())
                radio_clock.setAlarmTime(shi.TimeOfDay(hour, minute))
            except shi.DeviceOff as e:
                print_red(e.reason)
                continue
            except shi.OutOfRangeValue as e:
                print_red(e.reason)
                continue
            except ValueError as e:
                print_red("Invalid value: should be a number")
                continue

            print_green("Successfully set alarm time to: " + str(hour) + ":" + str(minute))

        else:
            print_red("Invalid command")