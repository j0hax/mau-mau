package server;

import util.cards.Card;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Player implements Runnable {

    private String username = "Player";
    private Socket playerSocket;
    private LinkedList<Card> hand;

    private Scanner in;
    private PrintWriter out;
    private volatile IOHandler ioHandler;

    /**
     * Creates game object
     *
     * @param playerSocket own socket
     * @param ioHandler    IOHandler that connects to the lobby thread
     */
    public Player(Socket playerSocket, IOHandler ioHandler) {
        this.playerSocket = playerSocket;
        this.ioHandler = ioHandler;
        try {
            out = new PrintWriter(playerSocket.getOutputStream(), true);
            in = new Scanner(playerSocket.getInputStream());

        } catch (Exception e) {
            System.out.println("Error: Client " + username + " " + e.toString());
        }
    }

    @Override
    public void run() {

        String[] packets = {
                "First packet",
                "Second packet",
                "Third packet",
                "Fourth packet",
                "End"
        };

        while (ioHandler == null) {
            Thread.onSpinWait();
        }

        for (String packet : packets) {

            ioHandler.send(packet);

            // Thread.sleep() to mimic heavy server-side processing
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // TODO: read player name from socket
    }

    synchronized void changeIOHandler(IOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    Card[] getHand() {
        return (Card[]) hand.toArray();
    }

    void addToHand(Card c) {
        hand.add(c);
    }

    Card removeFromHand(int index) {
        return hand.remove(index);
    }

    String getName() {
        return username;
    }
}
