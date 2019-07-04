package client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import util.protocol.DataType;
import util.protocol.Packer;
import util.protocol.messages.Connection;

import java.io.IOException;


/**
* Controller Class to add functionality to the Gui
* */
public class FxController {
    @FXML
    private Button button;

    @FXML
    public TextField nameField;
    @FXML
    public TextField serverIPField;
    @FXML
    public TextField portField;


    private Client client;
    private Transmitter transmitter;


    /**
    * Adds functionality to the "connect" Button from the Login Gui
    */
    @FXML
    public void buttonClickHandler() {

        String port = portField.getText();
        // Checks if the input is valid
        if (port.matches("[0-9]+")  && nameField.getText().length()>0 && serverIPField.getText().length()>0) {
            int portValue = Integer.valueOf(port);
           // System.out.println("user with the name: "+nameField.getText()+" is trying to connect to Ip: " +serverIPField.getText()+" on Port:" + portField.getText());


            //Create and connect Client to Server
            client = new Client(nameField.getText(), portValue, serverIPField.getText());
            if (client.getConnectionStatus()) {
                transmitter = new Transmitter(client.getOutput());
                transmitter.send(Packer.packData(DataType.CONNECT, new Connection(client.getName())));
            }

            //Closes the Log in Window after the Client is successfully connected to the server

            if (client.getConnectionStatus()) {
                Stage game = (Stage) button.getScene().getWindow();

                Parent gameRoot = null;
                try {
                    gameRoot = FXMLLoader.load(getClass().getResource("/client/gui/game.fxml"));
                    game.setTitle("Mau Mau");

                    Scene gameScene = new Scene(gameRoot, 1000, 700);
                    gameScene.getStylesheets().add(getClass().getResource("/client/style/gameStyle.css").toExternalForm());

                    game.setScene(gameScene);

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    game.setX((screenBounds.getWidth() - game.getWidth()) / 2);
                    game.setY((screenBounds.getHeight() - game.getHeight()) / 2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } else {
            System.out.println("Error: Check your input");

        }
    }
}
