package Server;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        System.out.println("Server v. 1.0");
        System.out.println("Waiting for clients to connect..");

        int port = 12345;
        ServerSocket serverSocketTCP = null;
        DatagramSocket serverSocketUDP = null;
        MulticastSocket serverSocketMULTI = null;

        String multiAddress = "228.5.6.7";
        int multiPort = 54321;

        try {
            // create socket TCP
            serverSocketTCP = new ServerSocket(port);

            // create socket UDP
            serverSocketUDP = new DatagramSocket(port);

            List<Socket> clients = new LinkedList<>();

            // create channel UDP
            ServerChannelUDP channelUDP = new ServerChannelUDP(clients, serverSocketUDP);

            // create channel Multicast
            InetAddress group = InetAddress.getByName(multiAddress);
            serverSocketMULTI = new MulticastSocket(multiPort);
            serverSocketMULTI.joinGroup(group);
            ServerChannelMulticast channelMULTI = new ServerChannelMulticast(serverSocketMULTI);

            new Thread(channelUDP).start();
            new Thread(channelMULTI).start();

            // accept all TCP clients
            while (true) {
                Socket client = serverSocketTCP.accept();
                clients.add(client);
                System.out.println("New Client! - " + client.getPort());
                ServerChannelTCP channelTCP = new ServerChannelTCP(client, clients);
                new Thread(channelTCP).start();
            }


        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (serverSocketTCP != null){
                serverSocketTCP.close();
            }
            if (serverSocketUDP != null){
                serverSocketUDP.close();
            }
            if (serverSocketMULTI != null){
                serverSocketMULTI.close();
            }
        }
    }
}
