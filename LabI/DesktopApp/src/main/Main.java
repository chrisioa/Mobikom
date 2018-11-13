package main;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import communication.Model;

public class Main extends Application {
    private Controller controller;
    private Model model = Model.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/LabIGUI.fxml"));
        primaryStage.setTitle("Mobikom TCP/UDP Packet Masher 2000");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void stop() throws Exception {
        super.stop();
        model.stopServer();
    }
}
