package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Lobby {

    HashSet<Player> players;
    ServerSocket server;

    public Lobby(HashSet<Player> players, ServerSocket server) {
        this.players = players;
        this.server = server;
    }

    public void accept() {
        while (true) {
            try (Socket playerSocket = server.accept()) {
                players.add(new Player(playerSocket));
                System.out.println("new connection.");

                // TODO create ExecutorService for clients

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
