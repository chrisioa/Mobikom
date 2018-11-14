package controller;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import communication.Communicator;
import communication.ControllerInterface;
import communication.Model;
import communication.Protocol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Controller implements ControllerInterface {
    //DEFAULT VALUE TCP
    private Communicator communicator = new Communicator(Protocol.TCP, this);
    ;
    //CLIENT
    @FXML
    private ComboBox<Protocol> myComboBox;
    @FXML
    private TextField portNumberFX;
    @FXML
    private TextField messageFX;
    @FXML
    private Tab clientTab;
    @FXML
    private TextField ipAddressFX;
    private ObservableList<Protocol> myComboBoxData = FXCollections.observableArrayList();

    //SERVER
    @FXML
    private TextArea textArea;
    @FXML
    private TextField serverPort;
    @FXML
    private ComboBox<Protocol> serverComboBox;
    @FXML
    private Circle serverStatusCircle;
    @FXML
    private Button startButton;
    @FXML
    private Text serverIP;


    private Logger logger = Logger.getLogger("Controller");

    @FXML
    public void initialize() {
        myComboBoxData.add(Protocol.TCP);
        myComboBoxData.add(Protocol.UDP);
        myComboBox.getItems().addAll(myComboBoxData);
        serverComboBox.getItems().addAll(myComboBoxData);
        Model.getInstance().setController(this);
    }

    @FXML
    private void sendButtonListener(ActionEvent event) {
        logger.log(Level.INFO, "Sending message...");
        int port = Integer.valueOf(portNumberFX.getText());
        String ipAddress = ipAddressFX.getText();
        String message = messageFX.getText();
        Protocol protocol = myComboBox.getSelectionModel().getSelectedItem();
        communicator.setProtocol(protocol);
        communicator.sendMessage(ipAddress, port, message);
        //TODO:VALIDATION!
        logger.log(Level.INFO, "\tAddress: " + ipAddress + ":" + port + "\n\t\tMessage: " + message + "\n\t\tProtocol: " + protocol);

    }

    @FXML
    private void startButtonListener(ActionEvent event) {
        if ("Start".equals(startButton.getText())) {
            Protocol protocol = serverComboBox.getSelectionModel().getSelectedItem();
            logger.log(Level.INFO, "Protocol: " + protocol.toString());
            communicator.setProtocol(protocol);
            int port = Integer.valueOf(serverPort.getText());
            communicator.startServer(port, this);
            serverStatusCircle.setFill(Color.GREEN);
            startButton.setText("Stop");
            serverComboBox.setDisable(true);
            clientTab.setDisable(true);
            serverIP.setText(findOutIp());
        } else {
            startButton.setText("Start");
            serverStatusCircle.setFill(Color.RED);
            communicator.stopServerTasks();
            serverComboBox.setDisable(false);
            clientTab.setDisable(false);
        }
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

    public void updateTextArea(String text) {
        String old = textArea.getText();
        textArea.setText(old + "\n" + text);
    }

    @Override
    public void registerWithModel() {
        Model.getInstance().setController(this);
    }

    public void shutdownServers() {
        communicator.stopServerTasks();
        communicator.stopServer();
    }
}
