package de.world.hello.christos.helloworld.controller;

import android.app.Activity;
import android.widget.EditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import communication.ControllerInterface;
import communication.Model;
import communication.Protocol;
import de.world.hello.christos.helloworld.main.ServerActivity;
import de.world.hello.christos.helloworld.networking.NetworkConfig;
import de.world.hello.christos.helloworld.networking.SendMessageTask;
import de.world.hello.christos.helloworld.networking.ServerConfig;
import de.world.hello.christos.helloworld.networking.StartServerTask;

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
