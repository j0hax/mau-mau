package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartServer {
    public static void main(String[] args) {

        System.out.println("Starting Mau-Mau Server...");

        //contains all players
        HashSet<Player> players = new HashSet<>();

        try (ServerSocket server = new ServerSocket(55555)) {
            IOHandler lobbyIOHandler = new IOHandler();
            ExecutorService threadPool = Executors.newFixedThreadPool(11);
            threadPool.execute(new Lobby(players, server));
            while (true) {
                try (Socket playerSocket = server.accept()) {
                    System.out.println("new connection: " + playerSocket.getInetAddress());
                    Player p = new Player(playerSocket, lobbyIOHandler, null);
                    players.add(p);
                    threadPool.execute(p);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            System.err.println("Server closed.");
            e.printStackTrace();
        }

    }
}
