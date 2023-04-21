package Server;

import Utilities.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ServerChannelTCP implements Runnable{
    private final Socket client;
    private final List<Socket> clients;
    private final int ID; // one channel for each client

    public ServerChannelTCP(Socket client, List<Socket> clients){
        this.client = client;
        this.clients = clients;
        this.ID = client.getPort();
    }


    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while (true){
                String msg = in.readLine();
                if (msg == null || msg.equals("/end")){
                    System.out.println("client " + ID + " disconnected!");
                    in.close();
                    clients.remove(client);
                    break;
                }

                System.out.println(ID + " at " + Utilities.chatTime() + " said:\n" + msg);
                for (Socket client : clients){
                    if (client.getPort() == ID) continue;
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    out.println(ID + " at " + Utilities.chatTime() + " said:\n" + msg);
                }

            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

