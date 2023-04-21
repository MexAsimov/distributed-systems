import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Provider {
    public static final String exchangeName = "exchange1";
    private long lastOrderNumber;
    private final String providerName;


    public Provider() {
        this.lastOrderNumber = 0;
        this.providerName = getProviderName();

    }

    public static void main(String[] argv) {
        Provider provider = new Provider();
        provider.run();
    }

    private void run() {
        System.out.println("PROVIDER " + providerName);
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.basicQos(1);

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);

            Set<String> equipmentSet = provideEquipment();

            // Added admin control communication

            channel.exchangeDeclare(Admin.exchangeName, BuiltinExchangeType.TOPIC);
            channel.queueDeclare(Admin.adminInputQueue, false, false, false, null);

            String adminQueueName = channel.queueDeclare().getQueue();
            channel.queueBind(adminQueueName, Admin.exchangeName, Admin.adminBroadcastKey);
            channel.queueBind(adminQueueName, Admin.exchangeName, Admin.adminProvidersKey);

            Admin.adminConsumer(channel, adminQueueName);


            // end of added block

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    try {
                        String crew = new String(body, StandardCharsets.UTF_8);
                        String item = envelope.getRoutingKey();
                        System.out.println("Received request from " + crew + " (" + item + " needed)");

                        long orderNumber = ++lastOrderNumber;
                        String confirm = item + " (order " + orderNumber + ")";

                        byte[] message = confirm.getBytes(StandardCharsets.UTF_8);
                        channel.queueDeclare(crew, false, false, false, null);
                        channel.basicPublish("", crew, null, message);

                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            for (String queue : equipmentSet) {
                channel.queueDeclare(queue, false, false, false, null);
                channel.basicConsume(queue, false, consumer);
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }


    private String getProviderName() {
        Scanner in = new Scanner(System.in);
        System.out.println("Creating new provider...");
        System.out.print("Provider name: ");
        return in.nextLine().toUpperCase();
    }

    private Set<String> provideEquipment() {
        Scanner in = new Scanner(System.in);
        Set<String> eqSet = new HashSet<>();
        System.out.println("Specify equipment available to provide");
        System.out.println("(e to end)");

        String item;
        // creating new equipment list assuming that there is at least one product on it
        while (true) {
            item = in.nextLine().toLowerCase();
            if (item.equals("e")) {
                break;
            }
            if (!item.isEmpty()) {
                eqSet.add(item);
            }
        }

        System.out.printf("Providing following items: %s\n", eqSet);
        return eqSet;
    }

}
