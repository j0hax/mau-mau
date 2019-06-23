package server;

import util.Card;

import java.net.Socket;
import java.util.LinkedList;

public class Player {

    private String username = "Player";
    private Socket sock;
    private LinkedList<Card> hand;

    public Player(Socket sock) {
        this.sock = sock;

        // TODO: read player name from socket
    }

    public Card[] getHand() {
        return (Card[]) hand.toArray();
    }

    public void addToHand(Card c) {
        hand.add(c);
    }

    public Card removeFromHand(int index) {
        return hand.remove(index);
    }

    public String getName() {
        return username;
    }
}
