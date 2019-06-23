package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LobbyThread extends Thread{

    HashSet<Player> players;
    ServerSocket server;

    public LobbyThread(HashSet<Player> players, ServerSocket server) {
        this.players = players;
        this.server = server;
    }

    @Override
    public void run() {
        while(true){
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
