package de.world.hello.christos.helloworld.networking;

import android.os.AsyncTask;
import android.widget.RadioButton;
import android.widget.TextView;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Communicator;
import communication.Model;
import communication.Protocol;
import de.world.hello.christos.helloworld.R;

public class StartServerTask extends AsyncTask<ServerConfig, Integer, String> {

    private Logger logger = Logger.getLogger("StartServerTask");
    private int port;
    private Protocol protocol;
    private Communicator communicator = new Communicator(Protocol.TCP, Model.getInstance().getController());

    public StartServerTask(int port, Protocol protocol) {

        this.port = port;
        this.protocol = protocol;

    }


    @Override
    protected String doInBackground(ServerConfig... serverConfigs) {
        ServerConfig config = serverConfigs[0];
        communicator.setProtocol(protocol);
        communicator.startServer(port, Model.getInstance().getController());


        return "";
    }

    @Override
    protected void onPostExecute(String string){

    }

    public void stopServer() {
    }


}
