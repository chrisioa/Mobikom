package de.world.hello.christos.assignment1.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import communication.Protocol;
import de.world.hello.christos.assignment1.controller.Controller;
import de.world.hello.christos.assignment1.R;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private Controller controller = new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the Send button
     */
    public void switchToServer(View view) {
        Intent intent = new Intent(this, ServerActivity.class);
        startActivity(intent);

    }

    public void sendMessage(View view) {
        Protocol protocol = Protocol.TCP;
        RadioButton tcpButton = findViewById(R.id.tcpButtonClient);
        RadioButton udpButton = findViewById(R.id.udpButtonClient);

        if (tcpButton.isChecked()) {
            protocol = Protocol.TCP;
        } else if (udpButton.isChecked()) {
            protocol = Protocol.UDP;
        } else {
            //TODO:ALERT Selection is necessary
            showAlert("Protocol must be selected!");
        }

        EditText portField = findViewById(R.id.port);
        String portString = portField.getText().toString();
        int port = 1337;
        if (portString.matches("-?(0|[1-9]\\d*)")) {
            port = Integer.parseInt(portField.getText().toString());
        } else {
            showAlert("Port must be a number, 1337 used instead of " + portString);
        }


        EditText addressField = findViewById(R.id.ipAddress);
        String ipAddress = addressField.getText().toString();


        EditText messageField = findViewById(R.id.message);
        String message = messageField.getText().toString();

        controller.sendMessage(protocol, ipAddress, port, message);

    }

    public void showAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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