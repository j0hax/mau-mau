package server;

import util.cards.Card;
import util.cards.CardRank;
import util.cards.Deck;
import util.game.GameState;
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
    private int winner = -1;

    private int additionalCards = 0;

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
    /**
     * Check if one player has an empty hand
     * @return true/false if a player has an empty hand
     * */
    private boolean isOver() {
        for (Player player : players) {
            if (player.getHand().length == 0) {
                System.out.println("Player '" + player.getName() + "' won!");
                winner = player.getID();
                return true;
            }
        }

        return false;
    }

    private void nextPlayer() {
        if (activePlayer == players.length - 1) {
            activePlayer = 0;
        } else {
            ++activePlayer;
        }
    }

    /**
     * Determines if the submitted card is legal to place
     *
     * @param last    the last card that was placed
     * @param current the card that wants to be placed
     * @return the determination
     */
    public boolean legalMove(Card last, Card current) {
        // Seven may always be allowed
        System.out.println(additionalCards);
        if (additionalCards > 0){
            return current.getRank() == CardRank.SEVEN;
        }

        switch (current.getRank()) {
            case JACK:
                return additionalCards == 0;

            default:
                return last.compareTo(current) == 0;
        }
    }

    /**
     * Gives number of cards of each player
     *
     * @return Integer Array of number of cards
     */
    private Integer[] getNumberOfCards() {
        Integer[] numberOfCards = new Integer[players.length];

        for (int i = 0; i < players.length; i++) {
            numberOfCards[i] = players[i].getHand().length;
        }

        return numberOfCards;
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

        try {
            lastPlaced = deck.deal();
        } catch (Exception e) {
            System.out.println(e);
        }

        for (Player p : players) {
            try {
                p.addToHand(deck.deal(5));

            } catch (Exception e) {
                System.out.println(e.toString());
            }
            // share player names and their hand
            System.out.println(thisThread.getName() + "Sending new game to id: " +
                    p.getID());
            String s = Packer.packData(DataType.NEWGAME, new NewGame(playerNames,
                    p.getHand(), p.getID(), getNumberOfCards(), lastPlaced));
            p.send(s);
            //System.out.println(s);
        }

        //String receivedMessage;
        while (!isOver() && players.length != closed) {
            System.out.println(thisThread.getName() + "Active player = " +
                    activePlayer); //debugging
            Player current = players[activePlayer];
            String receivedMessage = gameIOHandler.receive();
            DataType type = Packer.getDataPacket(receivedMessage).getDataType();

            switch (type) {
                case CARDSUBMISSION:
                    Card c = (Card) Packer.unpackData(receivedMessage);

                    // if not legal, do nothing
                    if (!legalMove(lastPlaced, c)) {
                        break;
                    }

                    //add the last placed card back to the deck
                    deck.add(lastPlaced);

                    switch (c.getRank()) {
                        case SEVEN:
                            // increase the number of cards to be picked up by the next player
                            additionalCards += 2;
                            break;

                        case EIGHT:
                            nextPlayer();
                            break;

                        case JACK:
                            //TODO: allow current player to choose the next card
                            break;
                    }

                    nextPlayer();
                    current.removeFromHand(c);
                    lastPlaced = c;
                    break;

                case CARDWISH:
                    nextPlayer();
                    break;

                case CHATMESSAGE:
                    break;

                case DISCONNECT:
                    Player rmPlayer = players[(Integer) Packer.unpackData(receivedMessage)];
                    //System.out.println(thisThread + " disconnecting ID: " + players[packet.getPlayerID()].getID());
                    rmPlayer.send(Packer.packData(DataType.CONFIRM, true));
                    rmPlayer.disconnect();
                    ++closed;
                    nextPlayer();
                    break;

                case CARDREQUEST:
                    try {
                        players[(Integer) Packer.unpackData(receivedMessage)]
                                .addToHand(deck.deal(additionalCards > 0 ? additionalCards : 1));
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                    additionalCards = 0;
                    nextPlayer();
                    break;
            }

            for (Player allP : players) {
                // share player names and their hand
                String s = Packer.packData(DataType.GAMESTATE, new GameState(activePlayer,
                        allP.getHand(), getNumberOfCards(), lastPlaced));
                allP.send(s);

                //System.out.println(s);
            }

            System.out.println(thisThread.getName() + "Deck size: " + deck.getSize());
        }

        String msg;

        for (Player allP : players) {
            if (winner == -1) {
                System.out.println("No winner");
                break;
            }

            msg = Packer.packData(DataType.GAMEOVER, winner);
            allP.send(msg);
            allP.disconnect();
        }

        System.out.println(thisThread.getName() + "\t\t\t>> Stopping game");
    }


}
