package de.world.hello.christos.assignment1.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Communicator;
import communication.Model;
import communication.Protocol;
import de.world.hello.christos.assignment1.networking.ServerConfig;
import de.world.hello.christos.assignment1.R;
import de.world.hello.christos.assignment1.controller.Controller;

public class ServerActivity extends AppCompatActivity {

    Controller controller = (Controller) Model.getInstance().getController();
    private Logger logger = Logger.getLogger("ServerActivity");
    private List<StartServerTask> tasks = new ArrayList<StartServerTask>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);


    }

    public void updateTextArea(String s) {
        logger.log(Level.INFO, "RECEIVED : " + s);

        runOnUiThread(new Runnable() {
            public void run() {
                EditText textArea = findViewById(R.id.textArea);
                textArea.setText(s);
            }
        });

    }

    public void startServer(View view) {
        Protocol protocol = Protocol.TCP;
        RadioButton tcpButton = findViewById(R.id.tcpButtonServer);
        RadioButton udpButton = findViewById(R.id.udpButtonServer);

        if (tcpButton.isChecked()) {
            protocol = Protocol.TCP;
        } else if (udpButton.isChecked()) {
            protocol = Protocol.UDP;
        } else {
            showAlert("Protocol must be selected!");
        }


        EditText portField = findViewById(R.id.portServer);

        controller.registerDeps(this);
        String portString = portField.getText().toString();
        int port = 1337;
        if (portString.matches("-?(0|[1-9]\\d*)")) {
            port = Integer.parseInt(portField.getText().toString());
        } else {
            showAlert("Port must be a number, 1337 used instead of " + portString);
        }

        Button startButton = findViewById(R.id.startServerButton);
        StartServerTask sst = new StartServerTask();
        if ("Start Server".equals(startButton.getText())) {
            tasks.add(sst);
            sst.execute(new ServerConfig(port, protocol));
            startButton.setText("Stop");

        } else {
            logger.log(Level.INFO,"STOP triggered...");
            for(StartServerTask task : tasks){
                task.stopServer();
                task.cancel(true);
                tasks.remove(task);
            }
            startButton.setText("Start Server");
        }

    }


    public class StartServerTask extends AsyncTask<ServerConfig, String, String> {

        private Logger logger = Logger.getLogger("StartServerTask");
        private int port;
        private Protocol protocol;
        private Communicator communicator = new Communicator(Protocol.TCP, Model.getInstance().getController());


        @Override
        protected String doInBackground(ServerConfig... serverConfigs) {
            ServerConfig config = serverConfigs[0];
            communicator.setProtocol(config.getProtocol());
            communicator.startServer(config.getPort(), Model.getInstance().getController());
            publishProgress(findOutIp());

            return "";
        }



        @Override
        protected void onProgressUpdate(String... progress) {
            TextView serverIP = findViewById(R.id.serverIPServer);
            serverIP.setText(progress[0]);
        }

        private String findOutIp() {
            String ip = "Not assigned/found";
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return ip;
        }

        @Override
        protected void onPostExecute(String string) {
            logger.log(Level.INFO,"POST EXECUTE");
        }

        public void stopServer() {
            communicator.stopServerTasks();
            communicator.stopServer();
        }


    }


    public void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(ServerActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
