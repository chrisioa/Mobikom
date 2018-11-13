package communication;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServerTask implements Runnable {


    private final int port;
    private final ControllerInterface controller;
    private volatile boolean running = true;
    private Logger logger = Logger.getLogger("TCPConnector");


    public TCPServerTask(int port, ControllerInterface controller) {
        this.port = port;
        this.controller = controller;
    }

    @Override
    public void run() {
        logger.log(Level.INFO, "Waiting for accept");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setSoTimeout(100);

            while (running) {
                try (Socket clientSocket = serverSocket.accept()) {
                    try (ObjectInputStream inFromClient =
                                 new ObjectInputStream(clientSocket.getInputStream())) {
                        Object inObject = inFromClient.readObject();
                        if (inObject instanceof Message) {
                            String message = ((Message) inObject).getMessage();
                            logger.log(Level.INFO, "RECEIVED: " + message);
                            controller.updateTextArea(message);
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (SocketTimeoutException e) {
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This boolean value is checked in the while loop that keeps the server running.
     *
     * @param running boolean whether server should be running.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}
