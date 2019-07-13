package server;

import util.cards.Card;
import util.cards.Deck;
import util.game.GameState;
import util.protocol.DataPacket;
import util.protocol.DataType;
import util.protocol.Packer;
import util.protocol.messages.NewGame;

public class GameThread implements Runnable {

    private int ID;
    private Player[] players;
    private String[] playerNames;
    private Deck deck = new Deck();
    private IOHandler gameIOHandler;
    private int closed = 0;

    /**
     * Index of the player who has the turn
     */
    private int activePlayer;

    /**
     * The last card that was placed in the middle of the "stack"
     */
    private Card lastPlaced;

    /**
     * Creates a game
     *
     * @param players       all players in the game
     * @param gameIOHandler IOHandler for player inputs
     */
    GameThread(int ID, Player[] players, IOHandler gameIOHandler) {
        this.ID = ID;
        this.players = players;
        this.playerNames = new String[players.length];
        this.gameIOHandler = gameIOHandler;
    }

    private boolean isOver() {
        // TODO: implement game logic
        return false;
    }

    private void nextPlayer() {
        if (activePlayer == players.length - 1) {
            activePlayer = 0;
        } else {
            ++activePlayer;
        }
    }

    @Override
    public void run() {
        activePlayer = 0;
        // initializing
        Thread thisThread = Thread.currentThread();
        thisThread.setName("Game-" + this.ID + "\t\t\t>> ");
        System.out.println(thisThread.getName() + "Starting new game");

        // prints out all players in the current game
        StringBuilder pString = new StringBuilder();
        for (int i = 0; i < playerNames.length; i++) {
            pString.append("'").append(players[i].getName()).append("'");
            playerNames[i] = players[i].getName();
        }
        System.out.println(thisThread.getName() + "[" + pString + "]");

        // send each player the NewGame message
        for (Player p : players) {
            p.addToHand(deck.deal(5));
            // share player names and their hand
            System.out.println("Sending new game to id: " + p.getID());
            String s = Packer.packData(DataType.NEWGAME, new NewGame(playerNames, p.getHand(), p.getID()));
            p.send(s);
            //System.out.println(s);
        }

        //String receivedMessage;
        while (!isOver() && players.length != closed) {

            System.out.println("Active player = " + activePlayer); //debugging

            Player current = players[activePlayer];

            String receivedMessage = gameIOHandler.receive();

            DataType type = Packer.getDataPacket(receivedMessage).getDataType();

            DataPacket packet = Packer.getDataPacket(receivedMessage);

            switch (type) {
                case CARDSUBMISSION:
                    Card c = (Card) Packer.unpackData(receivedMessage);
                    //TODO: process if card is legal
                    switch (c.getRank()) {
                        case SEVEN:
                            players[activePlayer + 1].addToHand(deck.deal(2));
                            break;
                        case EIGHT:
                            activePlayer++;
                            break;
                        case JACK:
                            //TODO: allow current player to choose the next card
                            break;
                        case ACE:
                            //TODO: force current player to play another card
                    }
                    nextPlayer();

                    current.removeFromHand(c);
                    lastPlaced = c;
                    // TODO add the last lastPlaced card back to deck

                    break;
                case CARDWISH:
                    //TODO: should only work if card was jack
                    nextPlayer();
                    break;
                case CHATMESSAGE:
                    break;
                case DISCONNECT:
                    Player rmPlayer = players[packet.getPlayerID()];
                    //System.out.println(thisThread + " disconnecting ID: " + players[packet.getPlayerID()].getID());
                    rmPlayer.send(Packer.packData(DataType.CONFIRM, true));
                    rmPlayer.disconnect();
                    ++closed;
                    break;
            }

            for (Player allP : players) {
                // share player names and their hand
                String s = Packer.packData(DataType.GAMESTATE, new GameState(activePlayer, allP.getHand()));
                allP.send(s);
                System.out.println(s);
            }

            /*
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/


        }
        System.out.println(thisThread.getName() + "\t\t\t>> Stopping game");
    }
}
