package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;

public class ClientInputMulticast implements Runnable {
    private final DatagramSocket multiSocket;
    private final int port;

    boolean interrupted = false;

    public ClientInputMulticast(MulticastSocket multiSocket, int port){
        this.multiSocket = multiSocket;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            while (!interrupted){
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multiSocket.receive(packet);

                // Ignore packets from this client
                if (packet.getPort() == port){
                    continue;
                }

                String msg = new String(buffer);
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        interrupted = true;
        multiSocket.close();
    }
}
