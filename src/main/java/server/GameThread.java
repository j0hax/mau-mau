package server;

import util.Deck;

import java.util.Map;

public class GameThread implements Runnable {

    private Map<Integer, Player> players;
    private Deck deck = new Deck();

    public GameThread(Map<Integer, Player> players) {
        this.players = players;
    }

    public boolean isOver() {
        // TODO: implement game logic
        return true;
    }

    @Override
    public void run() {
        System.out.println("start game 1");
        // TODO: deal the cards

        while (!isOver()) {
            //for (Player p : players) {
                // TODO: Each player will have their turn here
            //}
        }
        System.out.println("stop game 1");
    }
}
