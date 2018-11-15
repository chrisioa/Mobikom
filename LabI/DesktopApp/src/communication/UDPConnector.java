package communication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UDP Implementation of the {@link communication.Connector} interface, which
 * realizes the sendMessage, startServer messages with UDP.
 */
public class UDPConnector implements Connector {

	private ExecutorService exs = Executors.newCachedThreadPool();
	private Logger logger = Logger.getLogger("UDPConnector");

	private List<UDPServerTask> taskList = new ArrayList<UDPServerTask>();

	public UDPConnector() {
	}

	public void sendMessage(String ip, int port, String message) {
		logger.log(Level.INFO, "Sending message : " + message + " to: " + ip + ":" + port);
		UDPMessageTask task = new UDPMessageTask(ip, port, message);
		exs.submit(task);
	}

	public void startServer(int port, ControllerInterface controller) {
		logger.log(Level.INFO, "Starting UDP server on port: " + port);
		UDPServerTask task = new UDPServerTask(port, controller);
		taskList.add(task);
		exs.submit(task);
	}

	public void stopServerTasks() {
		for (UDPServerTask task : taskList) {
			task.setRunning(false);
		}
	}

	public void stopServer() {
		// taskList.stream().forEach(p -> p.setRunning(false));
		stopServerTasks();
		exs.shutdownNow();
		try {
			while (!exs.isShutdown()) {
				exs.awaitTermination(1, TimeUnit.SECONDS);
				break;
			}
		} catch (InterruptedException e) {
			// ignored
		}
		logger.log(Level.INFO, "Server shutdown.");
	}

}
