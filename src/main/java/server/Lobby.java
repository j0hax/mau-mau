package server;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby implements Runnable {

    private final int GAMESIZE = 1;
    private int gameID = 0;
    private LinkedList<Player> players;

    Lobby(LinkedList<Player> players) {
        this.players = players;
    }

    @Override
    public void run() {
        ExecutorService gamePool = Executors.newFixedThreadPool(10);

        while (true) {

            System.out.println("LOBBY\t\t\t>> Lobby is active");

            System.out.println("LOBBY\t\t\t>> Waiting for players to start new game: " + (GAMESIZE - players.size()));

            while (players.size() != GAMESIZE) {
                Thread.onSpinWait();
            }

            System.out.println(players.toString());

            // this block will be changed later
            LinkedList<Player> playersInGame = new LinkedList<>(players);
            players.clear();

            IOHandler gameIOHandler = new IOHandler();
            for (int i = 0; i < GAMESIZE; i++) {
                playersInGame.get(i).changeIOHandler(gameIOHandler);
            }

            gamePool.execute(new GameThread(gameID++, playersInGame, gameIOHandler));

        }


    }

}
