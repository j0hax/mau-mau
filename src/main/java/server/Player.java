package server;

import util.Card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class Player {

    private String username = "Player";
    private Socket sock;
    private LinkedList<Card> hand;

    public BufferedReader in;
    public PrintWriter out;

    public Player(Socket sock) throws IOException {
        this.sock = sock;

        out = new PrintWriter(sock.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

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
