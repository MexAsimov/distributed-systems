package server;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class IceServer2 {
    public static void main(String[] args) {
        IceServer2 app = new IceServer2();
        app.t2(args);
    }

    public void t2(String[] args){
        int status = 0;
        Communicator communicator = null;

        try {
            // 1. Inicjalizacja ICE - utworzenie communicatora
            communicator = Util.initialize(args);

            // 2. Konfiguracja adaptera
            ObjectAdapter adapter = communicator.createObjectAdapter("Adapter2");

            // 3. Stworzenie serwantów
            CameraI camera1 = new CameraI("OutsideCamera1");
            CameraI camera2 = new CameraI("OutsideCamera2");

            PTZCameraI ptzCamera1 = new PTZCameraI("OutsidePTZCamera1");
            PTZCameraI ptzCamera2 = new PTZCameraI("OutsidePTZCamera2");

            // 4. Dodanie wpisów do tablicy ASM, skojarzenie nazwy obiektu (Identity) z serwantem
            adapter.add(camera1, new Identity("cameraOut1", "CameraOutside"));
            adapter.add(camera2, new Identity("cameraOut2", "CameraOutside"));
            adapter.add(ptzCamera1, new Identity("ptzCameraOut1", "CameraOutside"));
            adapter.add(ptzCamera2, new Identity("ptzCameraOut2", "CameraOutside"));

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
