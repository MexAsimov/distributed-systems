import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Crew {
    private final String crewName;

    public Crew() {
        this.crewName = getCrewName();
    }

    public static void main(String[] argv) {
        Crew crew = new Crew();
        crew.run();
    }

    private void run() {
        System.out.println("CREW " + crewName);
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(Provider.exchangeName, BuiltinExchangeType.TOPIC);
            Admin.queueDeclareWithConsumer(channel, crewName);

            // Added Admin control communication

            channel.exchangeDeclare(Admin.exchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(Admin.adminInputQueue, false, false, false, null);

            String adminQueueName = channel.queueDeclare().getQueue();
            channel.queueBind(adminQueueName, Admin.exchangeName, Admin.adminBroadcastKey);
            channel.queueBind(adminQueueName, Admin.exchangeName, Admin.adminCrewsKey);

            Admin.adminConsumer(channel, adminQueueName);

            // end of added block


            Scanner in = new Scanner(System.in);
            while (true) {
                System.out.println("Specify what you need: ");
                System.out.println("(type e to end)");
                String item = in.nextLine().toLowerCase();
                if (item.equals("e")) break;

                byte[] message = crewName.getBytes(StandardCharsets.UTF_8);
                channel.queueDeclare(item, false, false, false, null);
                channel.basicPublish("", item, null, message);

                System.out.println("Sent request for " + item);
            }

            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private String getCrewName() {
        Scanner in = new Scanner(System.in);
        System.out.print("Crew name: ");
        return in.nextLine().toUpperCase();
    }
}
