package Client;

import Utilities.Utilities;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientInputUDP implements Runnable{

    private final DatagramSocket socketUDP;

    boolean interrupted = false;

    public ClientInputUDP(DatagramSocket socketUDP){
        this.socketUDP = socketUDP;
    }

    @Override
    public void run() {
        try {
            while (!interrupted) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socketUDP.receive(packet);
                String msg = new String(Utilities.cutBlanks(buffer));
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate(){
        interrupted = true;
        socketUDP.close();
    }
}
