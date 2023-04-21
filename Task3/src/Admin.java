import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Admin {
    public static final String exchangeName = "admin_exchange";

    // admin queue
    public static final String adminInputQueue = "admin_queue_in";

    // keys to specific groups
    public static final String adminBroadcastKey = "admin_broadcast";

    public static final String adminProvidersKey = "admin_providers";

    public static final String adminCrewsKey = "admin_crews";

    public static void main(String[] args) {
        Admin admin = new Admin();
        admin.run();
    }

    private void run() {
        System.out.println("=== ADMIN PANEL ===");

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            queueDeclareWithConsumer(channel, adminInputQueue);

            Scanner in = new Scanner(System.in);
            while (true) {
                System.out.println("Type p to send to providers, c to send to crews, a to send to all, e to exit");
                String recipient = in.nextLine();

                if (recipient.equals("e")) break;

                // selected option refer to specific routingKey
                String routingKey;
                switch (recipient) {
                    case "p" -> routingKey = adminProvidersKey;
                    case "c" -> routingKey = adminCrewsKey;
                    case "a" -> routingKey = adminBroadcastKey;
                    default -> {
                        System.out.println("Invalid option");
                        continue;
                    }
                }

                System.out.println("Enter message");
                String message = in.nextLine();

                channel.basicPublish(exchangeName, routingKey, null, message.getBytes(StandardCharsets.UTF_8));

            }
            channel.close();
            connection.close();

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    static void queueDeclareWithConsumer(Channel channel, String adminInputQueue) throws IOException {
        channel.queueDeclare(adminInputQueue, false, false, false, null);

        Consumer adminConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelop, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                String confirm = "Received " + message;
                System.out.println(confirm);
            }
        };
        channel.basicConsume(adminInputQueue, true, adminConsumer);
    }

    static void adminConsumer(Channel channel, String adminQueueName) throws IOException {
        Consumer adminConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(">> Received from admin: " + message);
            }
        };
        channel.basicConsume(adminQueueName, true, adminConsumer);
    }
}
