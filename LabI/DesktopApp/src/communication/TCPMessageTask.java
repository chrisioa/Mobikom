package communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPMessageTask implements Runnable {
	private final String ip;
	private final int port;
	private final Message message;

	public TCPMessageTask(String ip, int port, Message message) {
		this.ip = ip;
		this.port = port;
		this.message = message;
	}

	@Override
	public void run() {
		// try-with-resources block auto closes the socket
		try (Socket clientSocket = new Socket()) {
			clientSocket.connect(new InetSocketAddress(InetAddress.getByName(ip), port), 2000);
			// Initialize an object output stream to send the message object
			try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
				out.writeObject(message);
				out.flush();
			}

		} catch (IOException e ) {
			e.printStackTrace();
		}
	}
}
