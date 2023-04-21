import org.apache.zookeeper.*;

import java.io.IOException;

public class NodeMonitor implements Watcher {

    private final ZooKeeper zooKeeper;
    private final String znode;
    private final String[] command;

    private final Runtime rt;
    private Process proc;

    public NodeMonitor(ZooKeeper zk, String znode, String[] command) throws InterruptedException, KeeperException {
        this.command = command;
        this.zooKeeper = zk;
        this.znode = znode;

        this.rt = Runtime.getRuntime();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeCreated
                && watchedEvent.getPath().endsWith("/z")) {
            try {
                proc = rt.exec(command);
                System.out.println("=== Application opened ===");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (watchedEvent.getType() == Event.EventType.NodeDeleted && watchedEvent.getPath().equals(znode)) {
            closeApp();
        } else {
            try {
                if (zooKeeper.exists(znode, false) != null) {
                    System.out.println("No. children in " + znode + ": " + zooKeeper.getAllChildrenNumber(znode));
                }
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeApp() {
        if (proc != null && proc.isAlive()) {
            proc.destroy();
            System.out.println("=== Application closed ===");
        } else {
            System.out.println("=== Application closed earlier ===");
        }

    }
}
