package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import util.cards.Card;
import util.protocol.DataType;
import util.protocol.messages.Connection;
import util.protocol.messages.NewGame;

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


    /**
    * Adds functionality to the "connect" Button from the Login Gui
    */
    @FXML
    public void buttonClickHandler(ActionEvent event) {

        String port = portField.getText();
        // Checks if the input is valid
        if (port.matches("[0-9]+")  && nameField.getText().length()>0 && serverIPField.getText().length()>0) {
            int portValue = Integer.valueOf(port);
            // System.out.println("user with the name: "+nameField.getText()+" is trying to connect to Ip: " +serverIPField.getText()+" on Port:" + portField.getText());


            //Create and connect Client to Server
            client = new Client(nameField.getText(), portValue, serverIPField.getText());
            if (client.getConnectionStatus()) {
                client.getHandUpdatedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        System.out.println("New hand is ready to be printed...");
                        client.setHandUpdatedProperty(false);
                    }
                });
                /* OLD CODE EXAMPLE -- now managed by Client class
                transmitter = new Transmitter(client.getOutput(), client.getInput());
                transmitter.sendData(Packer.packData(DataType.CONNECT, new Connection(client.getName())));
                */

                client.sendData(DataType.CONNECT, new Connection(client.getName()));

                boolean nameConfirmed = (boolean) client.receiveData();
                if(!nameConfirmed) {
                    System.out.println("You cannot use this name.");
                    client.closeConnection();
                } else {
                    System.out.println("Server has confirmed your name.");
                }
            }

            //Closes the Log in Window after the Client is successfully connected to the server

            if (client.getConnectionStatus()) {

                NewGame ng = (NewGame) client.receiveData();
                for (String p : ng.getAllPlayers()) {
                    System.out.println(p);
                }

                for(Card c : ng.getInitialHand()){
                    System.out.println(c);
                }

                new Thread(client).start();

                for (int i = 0; i < 3; i++) {
                    //transmitter.send("Hello " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //transmitter.send("End");
                System.out.println("Sent end");

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/gui/game.fxml"));
                    Parent gameRoot = loader.load();

                    Scene gameScene = new Scene(gameRoot, 1000, 700);
                    gameScene.getStylesheets().add(getClass().getResource("/client/style/gameStyle.css").toExternalForm());

                    Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

                    window.setTitle("Mau Mau");
                    window.setScene(gameScene);
                    window.show();

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    window.setX((screenBounds.getWidth() - window.getWidth()) / 2);
                    window.setY((screenBounds.getHeight() - window.getHeight()) / 2);



                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } else {
            System.out.println("Error: Check your input");

        }
    }
}
