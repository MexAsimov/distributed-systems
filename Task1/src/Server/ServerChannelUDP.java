package Server;

import Utilities.Utilities;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class ServerChannelUDP implements Runnable{
    private final List<Socket> clients;
    private final DatagramSocket serverSocketUDP;

    public ServerChannelUDP(List<Socket> clients, DatagramSocket socket){
        this.clients = clients;
        this.serverSocketUDP = socket;
    }

    @Override
    public void run() {
        try{
            InetAddress address = InetAddress.getByName("localhost");
            while (true){
                byte[] buffer = new byte[1024];
                DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
                serverSocketUDP.receive(recvPacket);

                String msg = new String(Utilities.cutBlanks(buffer));
                int sender = recvPacket.getPort() - 1;
                System.out.println(sender + " at " + Utilities.chatTime() + ":\n" + msg);

                byte[] newBuffer = (sender + " at " + Utilities.chatTime() + ":\n" + msg).getBytes();

                for (Socket client : clients){
                    if (client.getPort() == sender) continue;
                    DatagramPacket sendPacket = new DatagramPacket(newBuffer, newBuffer.length, address, client.getPort() + 1);
                    serverSocketUDP.send(sendPacket);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
