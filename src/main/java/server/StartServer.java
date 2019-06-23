package server;

import util.Deck;

import java.net.ServerSocket;
import java.util.HashSet;

public class StartServer {
    public static void main(String[] args) {

        Deck d = new Deck();
        System.out.print(d);

        System.out.println("Starting Mau-Mau Server...");

        //contains all players
        HashSet<Player> players = new HashSet<Player>();

        try(ServerSocket server = new ServerSocket(55555)){
            //creating a lobby for all Players that will connect
            LobbyThread lobby = new LobbyThread(players, server);
            lobby.start();
            //waits for lobby to close until closes ServerSocket
            //otherwise server.accept in Lobby Class would crash immediately
            lobby.join();
        }catch (Exception e){
            System.err.println("Server closed.");
            e.printStackTrace();
        }

    }
}
