package server;

import java.net.ServerSocket;
import java.util.HashSet;

public class Lobby implements Runnable {

    HashSet<Player> players;
    ServerSocket server;

    public Lobby(HashSet<Player> players, ServerSocket server) {
        this.players = players;
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("run");
            for (Player p : players) {
                System.out.print(p.getName());
            }
            System.out.println();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }

    }

}
