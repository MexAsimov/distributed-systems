package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientInputTCP implements Runnable {
    private final Socket socketTCP;
    private final ClientOutput clientOutput;

    public ClientInputTCP(Socket socketTCP, ClientOutput clientOutput) {
        this.socketTCP = socketTCP;
        this.clientOutput = clientOutput;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socketTCP.getInputStream()));
            while (true) {
                String msg = in.readLine();

                if (msg == null) {
                    clientOutput.terminate();
                    socketTCP.close();
                    break;
                }

                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
