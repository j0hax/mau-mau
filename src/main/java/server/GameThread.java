package server;

import util.Deck;

public class GameThread implements Runnable {

    private Player[] players;
    private Deck deck = new Deck();

    public GameThread(Player[] players) {
        this.players = players;
    }

    public boolean isOver() {
        // TODO: implement game logic
        return true;
    }

    @Override
    public void run() {

        // TODO: deal the cards

        while (!isOver()) {
            for (Player p : players) {
                // TODO: Each player will have their turn here
            }
        }
    }
}
