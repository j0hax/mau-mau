package server;

import java.net.ServerSocket;
import java.util.HashSet;

public class StartServer {
    public static void main(String[] args) {

        System.out.println("Starting Mau-Mau Server...");

        //contains all players
        HashSet<Player> players = new HashSet<>();

        try (ServerSocket server = new ServerSocket(55555)) {
            //creating a lobby for all Players that will connect
            Lobby lobby = new Lobby(players, server);
            lobby.accept();

            //waits for lobby to close until closes ServerSocket
            //otherwise server.accept in Lobby Class would crash immediately
        } catch (Exception e) {
            System.err.println("Server closed.");
            e.printStackTrace();
        }

    }
}
