package server;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class IceServer {
    public static void main(String[] args) {
        IceServer app = new IceServer();
        app.t1(args);
    }

    public void t1(String[] args){
        int status = 0;
        Communicator communicator = null;

        try {
            // 1. Inicjalizacja ICE - utworzenie communicatora
            communicator = Util.initialize(args);

            // 2. Konfiguracja adaptera
            ObjectAdapter adapter = communicator.createObjectAdapter("Adapter1");

            // 3. Stworzenie serwantów
            CameraI camera1 = new CameraI("Camera1");
            HeaterI heater1 = new HeaterI("Heater1");
            PTZCameraI ptzCamera1 = new PTZCameraI("PTZCamera1");
            RadioClockI radioClock1 = new RadioClockI("RadioClockI");

            // 4. Dodanie wpisów do tablicy ASM, skojarzenie nazwy obiektu (Identity) z serwantem
            adapter.add(camera1, new Identity("camera1", "Camera"));
            adapter.add(heater1, new Identity("heater1", "Heater"));
            adapter.add(ptzCamera1, new Identity("ptzCamera1", "PTZCamera"));
            adapter.add(radioClock1, new Identity("radioClock1", "RadioClock"));

            // 5. Aktywacja adaptera i wejście w pętlę przetwarzania żądań
            adapter.activate();

            System.out.println("Entering event processing loop...");

            communicator.waitForShutdown();
        }
        catch (Exception e) {
            System.err.println(e);
            status = 1;
        }
        if (communicator != null){
            try {
                communicator.destroy();
            }
            catch (Exception e) {
                System.err.println(e);
                status = 1;
            }
        }
        System.exit(status);
    }
}
