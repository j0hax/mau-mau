package server;

import java.net.ServerSocket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby implements Runnable {

    private final int GAMESIZE = 1;
    private Map<Integer, Player> players;
    private ServerSocket server;

    Lobby(Map<Integer, Player> players, ServerSocket server) {
        this.players = players;
        this.server = server;
    }

    @Override
    public void run() {
        ExecutorService gamePool = Executors.newSingleThreadExecutor(); // TODO change to newFixedThreadPool(numberOfGames)
        while (true) {
            System.out.println("LOBBY >> Waiting for players to start new game: " + (GAMESIZE - players.size()));

            while (players.size() != GAMESIZE) {
                Thread.onSpinWait();
            }


            // this block will be changed later
            if (players.size() == GAMESIZE) {
                IOHandler gameIOHandler = new IOHandler();
                Map<Integer, Player> playersInGame = new LinkedHashMap<>(players);
                for (int i = 0; i < GAMESIZE; i++) {
                    playersInGame.get(i).changeIOHandler(gameIOHandler);

                }
                gamePool.execute(new GameThread(playersInGame, gameIOHandler));
                break; //really important for now
            }

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }


    }

}
