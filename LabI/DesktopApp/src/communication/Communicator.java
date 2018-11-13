package communication;

/**
 * Allows the selection/setting of the underlying protocol, the start of a server, triggers the sending of messages over the defined protocol. Server and server tasks can also be stopped.
 */
public class Communicator {

    private Protocol protocol;
    private Connector connector;
    private TCPConnector tcpConnector;
    private UDPConnector udpConnector;

    public Communicator(Protocol protocol, ControllerInterface controller) {
        tcpConnector = new TCPConnector();
        udpConnector = new UDPConnector();
        setProtocol(protocol);
    }

    /**
     * Sends message to specified ip-address, port.
     *
     * @param Ip      receiver address
     * @param port    and receiver port
     * @param message message to be transported
     */
    public void sendMessage(String Ip, int port, String message) {
        connector.sendMessage(Ip, port, message);
    }

    /**
     * This method starts a server with the set protocol on specified port. The controller will be used to update the interface, especially with received messages.
     *
     * @param port       Port the server listens to
     * @param controller a controller that can be used to for instance update the GUI, display received messages
     */
    public void startServer(int port, ControllerInterface controller) {
        connector.startServer(port, controller);
    }

    /**
     * Gets the currently set protocol.
     *
     * @return currently set protocol
     */
    public Enum<Protocol> getProtocol() {
        return protocol;
    }

    /**
     * Sets underlying protocol, either TCP or UDP, that will be used to start server or send messages.
     *
     * @param protocol
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
        switch (protocol) {
            case TCP:
                connector = tcpConnector;
                break;
            case UDP:
                connector = udpConnector;
                break;
        }
    }


    /**
     * Stops all running instances of any server and prepares program shutdown.
     */
    public void stopServer() {
        tcpConnector.stopServer();
        udpConnector.stopServer();
    }

    /**
     * Stops all server runnables, but further servers can still be started.
     */
    public void stopServerTasks() {
        tcpConnector.stopServerTasks();
        udpConnector.stopServerTasks();
    }
}
