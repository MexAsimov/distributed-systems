package Server;

import Utilities.Utilities;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerChannelMulticast implements Runnable{
    private final DatagramSocket socketMULTI;

    public ServerChannelMulticast(DatagramSocket socketMULTI){
        this.socketMULTI = socketMULTI;
    }


    @Override
    public void run() {
        try {
            while (true){
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socketMULTI.receive(packet);

                String msg = new String(Utilities.cutBlanks(buffer));
                System.out.println("M at " + Utilities.chatTime() + " " + msg);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
