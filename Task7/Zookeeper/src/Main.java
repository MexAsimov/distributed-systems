import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static ZooKeeper zooKeeper;

    public static void main(String[] command) throws IOException, InterruptedException, KeeperException {

        if (command.length < 1) {
            System.err.println("You have to specify application to open");
            System.exit(2);
        }

        String znode = "/z";
        String servers = "localhost:2181,localhost:2182,localhost:2183";

        System.out.println("Opening ZooKeeper connection..");
        zooKeeper = new ZooKeeper(servers, 5000, null);
        System.out.println("DONE!\n");
        NodeMonitor monitor = new NodeMonitor(zooKeeper, znode, command);
        zooKeeper.addWatch(znode, monitor, AddWatchMode.PERSISTENT_RECURSIVE);

        Scanner sc = new Scanner(System.in);
        boolean working = true;
        while (working) {
            System.out.println("Type /tree to show tree, /stop to stop");
            String line = sc.nextLine();
            switch (line) {
                case "/tree" -> showTree(znode, "");
                case "/stop" -> {
                    working = false;
                    monitor.closeApp();
                    System.out.println("Client stopped..");
                }
            }
            System.out.println("");
        }
    }

    private static void showTree(String node, String tab) {
        try {
            if (zooKeeper.exists(node, false) != null) {
                System.out.println(tab + node);
                for (String child : zooKeeper.getChildren(node, false)) {
                    showTree(node + "/" + child, tab + "\t");
                }
            } else {
                throw new KeeperException(KeeperException.Code.NONODE) {
                };
            }
        } catch (KeeperException e) {
            if (e.code() == KeeperException.Code.NONODE) {
                System.out.println("Node " + node + " doesn't exist");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
