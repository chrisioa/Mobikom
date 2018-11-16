package de.world.hello.christos.assignment1.networking;

import communication.ControllerInterface;
import communication.Protocol;

public class NetworkConfig {

    private final ControllerInterface controller;
    private int port;
    private String ipAddress;
    private Protocol protocol;
    private String message;

    public NetworkConfig(int port, String ipAddress, Protocol protocol, ControllerInterface controller, String message){
        this.port=port;
        this.ipAddress=ipAddress;
        this.protocol=protocol;
        this.controller = controller;
        this.message=message;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public ControllerInterface getController() {
        return controller;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
