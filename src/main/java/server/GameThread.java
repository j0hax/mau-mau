package server;

import util.cards.Deck;

import java.util.LinkedList;

public class GameThread implements Runnable {

    private int ID;
    private LinkedList<Player> players;
    private Deck deck = new Deck();
    private IOHandler gameIOHandler;

    /**
     * Creates a game
     *
     * @param players       all players in the game
     * @param gameIOHandler IOHandler for player inputs
     */
    GameThread(int ID, LinkedList<Player> players, IOHandler gameIOHandler) {
        this.ID = ID;
        this.players = players;
        this.gameIOHandler = gameIOHandler;
    }

    public boolean isOver() {
        // TODO: implement game logic
        return false;
    }

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        thisThread.setName("Game-" + this.ID);

        System.out.println(thisThread.getName() + "\t\t\t>> Starting new game");

        // some testing:

        String pString = "";
        for (Player p : players) {
            pString += "'" + p.getName() + "'";
        }
        System.out.println(thisThread.getName() + "\t\t\t>> [" + pString + "]");


        //Connection newConnection = (Connection) Packer.unpackData(gameIOHandler.receive());
        //System.out.println(thisThread.getName() + "\t\t\t>> " + "New client\t>> " + newConnection.getClientName());

        for (String receivedMessage = gameIOHandler.receive();
                !"End".equals(receivedMessage);
                receivedMessage = gameIOHandler.receive()) {

            System.out.println(thisThread.getName() + "\t\t\t>> " + receivedMessage);

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
        System.out.println(thisThread.getName() + "\t\t\t>> Stopping game");
    }
}
