package communication;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TCP Implementation of the {@link communication.Connector} interface, which realizes the sendMessage, startServer messages with TCP.
 */
public class TCPConnector implements Connector {

    private final ControllerInterface controller;
    private Logger logger = Logger.getLogger("TCPConnector");
    private ExecutorService exs = Executors.newCachedThreadPool();
    ;
    private List<TCPServerTask> taskList = new ArrayList<TCPServerTask>();

    public TCPConnector(ControllerInterface controller) {

        this.controller = controller;
    }


    public void sendMessage(String ip, int port, String message) {

        // Messages to send, should always be final
        final Message msg = new Message(message);
        logger.log(Level.INFO, "Sending message : " + message + " to: " + ip + ":" + port);

        // try-with-resources block auto closes the socket
        try (Socket clientSocket = new Socket(InetAddress.getByName(ip), port)) {

            // Initialize an object output stream to send the message object
            // #{ping}
            try (ObjectOutputStream out = new ObjectOutputStream(
                    clientSocket.getOutputStream())) {
                out.writeObject(msg);
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startServer(int port, ControllerInterface controller) {
        logger.log(Level.INFO, "Starting TCP server on port: " + port);
        TCPServerTask task = new TCPServerTask(port, controller);
        taskList.add(task);
        exs.execute(task);
    }

    public void stopServerTasks() {
        taskList.stream().forEach(p -> p.setRunning(false));
    }

    public void stopServer() {
        taskList.stream().forEach(p -> p.setRunning(false));
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
}
