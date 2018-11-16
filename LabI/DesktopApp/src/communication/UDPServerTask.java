package communication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This runnable task provides a UDP server that listens for messages as long as
 * it is running. Can be stopped by setting running to false.
 */
public class UDPServerTask implements Runnable {

	private final ControllerInterface controller;
	private int port = 1337;
	private volatile boolean running = true;
	private final Logger logger = Logger.getLogger("UDPServer");

	public UDPServerTask(int port, ControllerInterface controller) {
		this.port = port;
		this.controller = controller;
	}

	@Override
	public void run() {
		while (running) {
			try (DatagramSocket serverSocket = new DatagramSocket(port)) {
				serverSocket.setSoTimeout(1000);
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				try {
					serverSocket.receive(receivePacket);
					try (ObjectInputStream iStream = new ObjectInputStream(
							new ByteArrayInputStream(receivePacket.getData()))) {
						Message messageClass = (Message) iStream.readObject();
						System.out.println("RECEIVED: " + messageClass.getMessage());
						controller.updateTextArea(messageClass.getMessage());
					} catch (ClassNotFoundException e) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					}
				} catch (SocketTimeoutException ignored) {
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This boolean value is checked in the while loop that keeps the server
	 * running.
	 *
	 * @param running
	 *                    boolean whether server should be running.
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
}
