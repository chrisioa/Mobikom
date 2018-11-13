package communication;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * UDP Implementation of the {@link communication.Connector} interface, which realizes the sendMessage, startServer messages with UDP.
 */
public class UDPConnector implements Connector {

    private final ControllerInterface controller;

    private ExecutorService exs = Executors.newCachedThreadPool();
    private List<UDPServerTask> taskList = new ArrayList<UDPServerTask>();

    private Logger logger = Logger.getLogger("UDPConnector");

    public UDPConnector(ControllerInterface controller) {
        this.controller = controller;
    }


    public void sendMessage(String ip, int port, String message) {
        logger.log(Level.INFO, "Sending message : " + message + " to: " + ip + ":" + port);
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            try (ByteArrayOutputStream bStream = new ByteArrayOutputStream(); ObjectOutput oo = new ObjectOutputStream(bStream)) {
                Message messageClass = new Message(message);
                oo.writeObject(messageClass);
                byte[] serializedMessage = bStream.toByteArray();
                InetAddress IPAddress = InetAddress.getByName(ip);
                DatagramPacket sendPacket = new DatagramPacket(serializedMessage, serializedMessage.length, IPAddress, port);
                clientSocket.send(sendPacket);
            }


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startServer(int port, ControllerInterface controller) {
        logger.log(Level.INFO, "Starting UDP server on port: " + port);
        UDPServerTask task = new UDPServerTask(port, controller);
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
