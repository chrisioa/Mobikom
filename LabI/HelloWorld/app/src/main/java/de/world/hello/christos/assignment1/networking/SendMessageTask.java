package de.world.hello.christos.assignment1.networking;

import android.os.AsyncTask;

import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Communicator;

public class SendMessageTask extends AsyncTask<NetworkConfig, Integer, String> {

    private Logger logger = Logger.getLogger("SendMessageTask");

    @Override
    protected String doInBackground(NetworkConfig... networkConfigs) {
        logger.log(Level.INFO, "Send request received.");
        NetworkConfig config = networkConfigs[0];
        Communicator communicator = new Communicator(config.getProtocol(), config.getController());
        communicator.sendMessage(config.getIpAddress(), config.getPort(), config.getMessage());
        //TODO:VALIDATION!
        logger.log(Level.INFO, "\tAddress: " + config.getIpAddress() + ":" + config.getIpAddress() + "\n\t\tMessage: " + config.getMessage() + "\n\t\tProtocol: " + config.getProtocol());

        return "Message sent.";
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
