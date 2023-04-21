package Client;

import java.io.IOException;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to my chat!");
        int ID;
        String hostName = "localhost";
        int portNumber = 12345;
        Socket socketTCP = null;
        DatagramSocket socketUDP = null;
        MulticastSocket socketMULTI = null;

        String multiAddress = "228.5.6.7";
        int multiPort = 54321;


        try {
            // create socket TCP
            socketTCP = new Socket(hostName, portNumber);
            ID = socketTCP.getLocalPort();
            // create socket UDP
            InetAddress address = InetAddress.getByName("localhost");
            socketUDP = new DatagramSocket(ID + 1);
            // create multicast socket UDP
            InetAddress group = InetAddress.getByName(multiAddress);
            socketMULTI = new MulticastSocket(multiPort);
            socketMULTI.joinGroup(group);
            // start client threads


            ClientInputUDP clientInputUDP = new ClientInputUDP(socketUDP);
            ClientInputMulticast clientInputMulticast = new ClientInputMulticast(socketMULTI, ID + 1);
            ClientOutput clientOutput = new ClientOutput(socketTCP, clientInputUDP, clientInputMulticast,
                                                        socketUDP, address, portNumber, group, multiPort);
            ClientInputTCP clientInputTCP = new ClientInputTCP(socketTCP, clientOutput);

            new Thread(clientOutput).start();
            new Thread(clientInputTCP).start();
            new Thread(clientInputMulticast).start();
            new Thread(clientInputUDP).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
