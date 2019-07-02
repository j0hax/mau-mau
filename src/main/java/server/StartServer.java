package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartServer {

    private static final int PORT = 55555;
    private static final int numberOfPlayers = 20;

    public static void main(String[] args) {

        System.out.println("Starting Mau-Mau Server...");

        //contains all players
        Map<Integer, Player> players = new LinkedHashMap<>();

        try (ServerSocket server = new ServerSocket(PORT)) {
            IOHandler ioHandler = new IOHandler();
            ExecutorService threadPool = Executors.newFixedThreadPool(numberOfPlayers);

            //creates and starts lobby
            Thread lobbyThread = new Thread(new Lobby(players, server));
            lobbyThread.start();

            int i = 0;
            while (true) try (Socket playerSocket = server.accept()) {
                System.out.println("new connection: " + playerSocket.getInetAddress());
                Player p = new Player(playerSocket, null);
                threadPool.execute(p);
                players.put(i, p);
                i = i + 1;
                System.out.println(players.size());
                System.out.println(players.toString());


            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Server closed.");
            e.printStackTrace();
        }

    }
}
