package server;

import util.Deck;

import java.util.Map;

public class GameThread implements Runnable {

    private Map<Integer, Player> players;
    private Deck deck = new Deck();
    private IOHandler gameIOHandler;

    /**
     * Creates a game
     *
     * @param players       all players in the game
     * @param gameIOHandler IOHandler for player inputs
     */
    GameThread(Map<Integer, Player> players, IOHandler gameIOHandler) {

        this.players = players;
        this.gameIOHandler = gameIOHandler;
    }

    public boolean isOver() {
        // TODO: implement game logic
        return false;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("game");
        System.out.println("start game 1");

        /*try {
            Thread.sleep(2000);
        } catch (InterruptedException e)  {
            Thread.currentThread().interrupt();
        }*/

        // some testing:

        for (String receivedMessage = gameIOHandler.receive();
             !"End".equals(receivedMessage);
             receivedMessage = gameIOHandler.receive()) {

            System.out.println(receivedMessage);

        }

        // TODO: deal the cards
        /*while (!isOver()) {
            if(gameIOHandler != null){
                String packet = gameIOHandler.receive();

                System.out.println("Packet: " + packet);
                System.out.println("loop");
            }


            //for (Player p : players) {
                // TODO: Each player will have their turn here
            //}
        }*/
        System.out.println("stop game 1");
    }
}
