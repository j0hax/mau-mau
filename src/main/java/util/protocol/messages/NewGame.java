package util.protocol.messages;

import util.cards.Card;

public class NewGame {

    private String[] allPlayers;

    private Card[] initialHand;

    public NewGame(String[] allPlayers, Card[] initialHand) {
        this.allPlayers = allPlayers;
        this.initialHand = initialHand;
    }

    public String[] getAllPlayers() {
        return allPlayers;
    }

    public Card[] getInitialHand(){
        return initialHand;
    }

}
