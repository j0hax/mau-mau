package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartServer {

    private static final int PORT = 55555;
    private static final int maxNumberOfPlayers = 20;

    public static void main(String[] args) {

        System.out.println("MAIN THREAD\t\t>> Starting Mau-Mau Server...");

        //contains all players
        LinkedList<Player> players = new LinkedList<>();

        try (ServerSocket server = new ServerSocket(PORT)) {
            //IOHandler ioHandler = new IOHandler();
            ExecutorService threadPool = Executors.newFixedThreadPool(maxNumberOfPlayers);

            //creates and starts lobby
            Thread lobbyThread = new Thread(new Lobby(players));
            lobbyThread.start();

            int i = 0;
            while (true) {
                try {
                    Socket playerSocket = server.accept();
                    System.out.println("new connection: " + playerSocket.getInetAddress());
                    Player p = new Player(playerSocket, null);
                    threadPool.execute(p);
                    players.add(p);
                    i = i + 1;


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
