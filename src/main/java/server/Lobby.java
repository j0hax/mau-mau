package server;

import java.net.ServerSocket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby implements Runnable {

    Map<Integer, Player> players;
    ServerSocket server;

    public Lobby(Map<Integer, Player> players, ServerSocket server) {
        this.players = players;
        this.server = server;
    }

    @Override
    public void run() {
        ExecutorService gamePool = Executors.newSingleThreadExecutor(); // TODO change to newFixedThreadPool(numberOfGames)
        while (true) {
            System.out.println("run");
            System.out.println(players.size());
            System.out.println();

            // this block will be changed later
            if (players.size() == 2) {
                IOHandler gameIOHandler = new IOHandler();
                Map<Integer, Player> game1 = new LinkedHashMap<>();
                game1.putAll(players);
                for (int i = 0; i < 2; i++) {
                    Player p = game1.remove(i);
                    p.setGameIO(gameIOHandler);
                    game1.put(i, p);
                }
                gamePool.execute(new GameThread(game1));
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }

    }

}
