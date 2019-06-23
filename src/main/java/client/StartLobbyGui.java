package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartLobbyGui extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {


        // Game
        Stage game = primaryStage;

        Parent gameRoot = FXMLLoader.load(getClass().getResource("/client/gui/game.fxml"));
        game.setTitle("Mau Mau");

        Scene gameScene = new Scene(gameRoot, 1030,730);
        gameScene.getStylesheets().add(getClass().getResource("/client/style/gameStyle.css").toExternalForm());

        game.setScene(gameScene);
        game.setResizable(false);
        game.show();


        // Lobby
        Stage lobby = new Stage();

        Parent lobbyRoot = FXMLLoader.load(getClass().getResource("/client/gui/lobby.fxml"));
        lobby.setTitle("Mau Mau Lobby");

        Scene lobbyScene = new Scene(lobbyRoot, 450,580);
        lobbyScene.getStylesheets().add(getClass().getResource("/client/style/lobbyStyle.css").toExternalForm());

        lobby.setScene(lobbyScene);
        lobby.setResizable(false);
        lobby.show();


        // Login
        Stage login = new Stage();
        Parent loginRoot = FXMLLoader.load(getClass().getResource("/client/gui/login.fxml"));
        login.setTitle("Mau Mau Login");

        Scene loginScene = new Scene(loginRoot, 300,250);
        loginScene.getStylesheets().add(getClass().getResource("/client/style/loginStyle.css").toExternalForm());

        login.setScene(loginScene);
        login.setResizable(false);
        login.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
