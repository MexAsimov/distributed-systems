package Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ClientOutput implements Runnable {

    private final int ID;
    private final Socket socketTCP;
    private final ClientInputUDP clientInputUDP;
    private final ClientInputMulticast clientInputMulticast;
    private final DatagramSocket socketUDP;
    private final InetAddress address;
    private final int port;
    private final InetAddress multiAddress;
    private final int multiPort;


    private final Scanner input;

    public ClientOutput(Socket socketTCP, ClientInputUDP clientInputUDP,
                        ClientInputMulticast clientInputMulticast,
                        DatagramSocket socketUDP, InetAddress address, int port,
                        InetAddress multiAddress, int multiPort){
        this.ID = socketTCP.getLocalPort();
        this.socketTCP = socketTCP;

        this.clientInputUDP = clientInputUDP;
        this.clientInputMulticast = clientInputMulticast;

        this.socketUDP = socketUDP;
        this.address = address;
        this.port = port;

        this.multiAddress = multiAddress;
        this.multiPort = multiPort;

        this.input = new Scanner(System.in);
    }

    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socketTCP.getOutputStream(), true);
            while (true) {
                System.out.println("Enter message");
                System.out.println("type: /end to quit, U to UDP, M to multicast");
                String msg = input.nextLine();

                // End of work
                if (msg.equals("/end")) {
                    out.println("/end");
                    terminate();
                    break;
                }

                // UDP
                else if (msg.equals("U")) {
                    System.out.println("UDP chosen - C to cancel, empty line to finish msg");
                    String data = read();
                    if (data.equals("C") || data.isEmpty()) {
                        System.out.println("Done UDP..");
                    } else {
                        byte[] buffer = data.getBytes();
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                        socketUDP.send(packet);
                    }
                }

                // MULTICAST
                else if (msg.equals("M")){
                    System.out.println("Multicast chosen - C to cancel, empty line to finish msg");
                    String data = read();
                    if (data.equals("C") || data.isEmpty()){
                        System.out.println("Done Multicast..");
                    } else {
                        data = ID + ": \n" + data;
                        byte[] buffer = data.getBytes();
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, multiAddress, multiPort);
                        socketUDP.send(packet);
                    }
                }

                //TCP
                else if (!msg.isEmpty()){
                    out.println(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String read(){
        List<String> msgParts = new LinkedList<>();
        while (input.hasNextLine()){
            String line = input.nextLine();
            if (line.isEmpty()){
                break;
            }
            msgParts.add(line);
        }
        return String.join("\n", msgParts);
    }

    public void terminate(){
        clientInputUDP.terminate();
        clientInputMulticast.terminate();
        input.close();
    }
}
