package server;

import util.cards.Deck;
import util.game.GameState;
import util.protocol.DataPacket;
import util.protocol.DataType;
import util.protocol.Packer;
import util.protocol.messages.NewGame;

import java.util.LinkedList;

public class GameThread implements Runnable {

    private int ID;
    private LinkedList<Player> players;
    private String[] playerNames;
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
        this.playerNames = new String[players.size()];
        this.gameIOHandler = gameIOHandler;
    }

    public boolean isOver() {
        // TODO: implement game logic
        return false;
    }

    @Override
    public void run() {
        // initializing
        Thread thisThread = Thread.currentThread();
        thisThread.setName("Game-" + this.ID);
        System.out.println(thisThread.getName() + "\t\t\t>> Starting new game");

        // prints out all players in the current game
        StringBuilder pString = new StringBuilder();
        for (int i = 0; i < playerNames.length; i++) {
            pString.append("'").append(players.get(i).getName()).append("'");
            playerNames[i] = players.get(i).getName();
        }
        System.out.println(thisThread.getName() + "\t\t\t>> [" + pString + "]");

        // send each player the NewGame message
        for (Player p : players) {
            // share player names and their hand
            String s = Packer.packData(DataType.NEWGAME, new NewGame(playerNames, deck.deal(5), p.getID()));
            p.send(s);
            //System.out.println(s);
        }

        // tests
        /*for (String receivedMessage = gameIOHandler.receive();
                !"End".equals(receivedMessage);
                receivedMessage = gameIOHandler.receive()) {

            System.out.println(thisThread.getName() + "\t\t\t>> " + receivedMessage);

        }*/

        String receivedMessage;
        while (!isOver() && players.size() != 0) {
            receivedMessage = gameIOHandler.receive();
            DataPacket packet = Packer.getDataPacket(receivedMessage);
            if(packet.getDataType() == DataType.DISCONNECT){
                System.out.println("Received disconnect");
                Player p = players.remove(packet.getPlayerID());
                p.disconnect();
            }
            //System.out.println(thisThread.getName() + "\t\t\t>> " + receivedMessage);

            for (Player p : players) {
                // share player names and their hand
                String s = Packer.packData(DataType.GAMESTATE, new GameState(0, deck.deal(5)));
                p.send(s);
                System.out.println(s);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO: Each player will have their turn here

        }
        System.out.println(thisThread.getName() + "\t\t\t>> Stopping game");
    }
}
