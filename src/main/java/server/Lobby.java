package server;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby implements Runnable {

    private final int GAMESIZE = 1;
    private final int MAXGAMES = 10;
    private int gameID = 0;
    private LinkedList<Player> players;
    private IOHandler lobbyIOHandler;

    Lobby(LinkedList<Player> players, IOHandler lobbyIOHandler) {
        this.players = players;
        this.lobbyIOHandler = lobbyIOHandler;
    }

    @Override
    public void run() {
        ExecutorService gamePool = Executors.newFixedThreadPool(MAXGAMES);

        while (true) {

            System.out.println("LOBBY\t\t\t>> Waiting for players to start new game: " + (GAMESIZE - players.size()));

            while (players.size() != GAMESIZE) {
                Thread.onSpinWait();
            }

            String pString = "";
            for (Player p : players) {
                pString += "'" + p.getName() + "'";
            }
            System.out.println("LOBBY\t\t\t>> Players in Lobby [" + pString + "]");

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
