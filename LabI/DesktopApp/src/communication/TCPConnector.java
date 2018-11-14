package communication;


import java.awt.peer.ListPeer;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TCP Implementation of the {@link communication.Connector} interface, which realizes the sendMessage, startServer messages with TCP.
 */
public class TCPConnector implements Connector {

    private Logger logger = Logger.getLogger("TCPConnector");
    private ExecutorService exs = Executors.newCachedThreadPool();
    ;
    private List<TCPServerTask> taskList = new ArrayList<TCPServerTask>();
    private List<Future<TCPMessageTask>> futureList = new ArrayList<Future<TCPMessageTask>>();

    public TCPConnector() {

    }


    public void sendMessage(String ip, int port, String message) {

        logger.log(Level.INFO, "Sending message : " + message + " to: " + ip + ":" + port);
        // Messages to send, should always be final
        final Message msg = new Message(message);
        TCPMessageTask messageTask = new TCPMessageTask(ip, port, msg);
        Future future = exs.submit(messageTask);
        futureList.add(future);
    }

    public void startServer(int port, ControllerInterface controller) {
        logger.log(Level.INFO, "Starting TCP server on port: " + port);
        TCPServerTask task = new TCPServerTask(port, controller);
        taskList.add(task);
        exs.submit(task);

    }

    public void stopServerTasks() {
        //Streams not supported in Android 7 and lower
        //taskList.stream().forEach(p -> p.setRunning(false));
        for (TCPServerTask task : taskList) {
            task.setRunning(false);
        }
    }

    public void stopServer() {
        //taskList.stream().forEach(p -> p.setRunning(false));
        stopServerTasks();
        stopMessageTasks();
        exs.shutdownNow();
        try {
            while (!exs.isShutdown()) {
                exs.awaitTermination(1, TimeUnit.SECONDS);
                break;
            }
        } catch (InterruptedException e) {
            //ignored
        }
        logger.log(Level.INFO, "Server shutdown.");
    }

    private void stopMessageTasks() {
        for (Future future : futureList) {
            future.cancel(true);
            System.out.println("Is done: " + future.isDone());
        }
        System.out.println("Futures: " + futureList.size());
    }
}
