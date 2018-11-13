package communication;

public interface Connector {

    /**
     * Sends message to specified ip-address:port.
     *
     * @param ip      receiver address
     * @param port    and receiver port
     * @param message message to be transported
     */
    void sendMessage(String ip, int port, String message);


    /**
     * This method starts a server with the set protocol on specified port. The controller will be used to update the interface, especially with received messages.
     *
     * @param port       Port the server listens to
     * @param controller a controller that can be used to for instance update the GUI, display received messages
     */
    void startServer(int port, ControllerInterface controller);


    /**
     * Stops all running instances of any server and prepares program shutdown.
     */
    void stopServer();


    /**
     * Stops all server runnables, but further servers can still be started.
     */
    void stopServerTasks();
}
