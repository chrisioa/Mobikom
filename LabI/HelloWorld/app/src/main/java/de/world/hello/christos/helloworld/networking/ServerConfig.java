package de.world.hello.christos.helloworld.networking;

import communication.Protocol;

public class ServerConfig {

    private int port;
    private Protocol protocol;


    public ServerConfig(int port, Protocol protocol) {
        this.port = port;
        this.protocol = protocol;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
