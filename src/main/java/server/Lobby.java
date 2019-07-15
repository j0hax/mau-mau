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
            System.out.println("LOBBY\t\t\t>> Waiting for players to start new game: " +
                    (GAMESIZE - players.size()));

            while (players.size() < GAMESIZE) {
                try {
                    Thread.onSpinWait();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            StringBuilder pString = new StringBuilder();

            for (Player p : players) {
                pString.append("'").append(p.getName()).append("'");
            }

            System.out.println("LOBBY\t\t\t>> Players in Lobby [" + pString + "]");
            // this block will be changed later
            Player[] playersInGame = players.toArray(new Player[players.size()]);
            players.clear();
            IOHandler gameIOHandler = new IOHandler();

            for (int i = 0; i < GAMESIZE; i++) {
                playersInGame[i].changeIOHandler(gameIOHandler);
                playersInGame[i].setID(i);
                System.out.println(i);
                System.out.println(playersInGame[i].getID());
            }

            gamePool.execute(new GameThread(gameID++, playersInGame, gameIOHandler));
        }
    }

}
