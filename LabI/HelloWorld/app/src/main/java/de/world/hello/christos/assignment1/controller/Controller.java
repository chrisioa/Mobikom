package de.world.hello.christos.assignment1.controller;

import java.util.logging.Logger;

import communication.ControllerInterface;
import communication.Model;
import communication.Protocol;
import de.world.hello.christos.assignment1.main.ServerActivity;
import de.world.hello.christos.assignment1.networking.NetworkConfig;
import de.world.hello.christos.assignment1.networking.SendMessageTask;

public class Controller implements ControllerInterface {


    private Logger logger = Logger.getLogger("Controller");
    private ServerActivity serverActivity;

    public Controller() {
        registerWithModel();
    }

    @Override
    public void shutdownServers() {

    }

    @Override
    public void updateTextArea(String s) {
        serverActivity.updateTextArea(s);
    }

    @Override
    public void registerWithModel() {
        Model.getInstance().setController(this);
    }

    public void sendMessage(Protocol protocol, String ipAddress, int port, String message) {
        SendMessageTask nt = new SendMessageTask();
        nt.execute(new NetworkConfig(port, ipAddress, protocol, this, message));
    }


    public void registerDeps(ServerActivity serverActivity) {
        this.serverActivity=serverActivity;
    }
}
