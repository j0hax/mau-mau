package server;

import util.cards.Card;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class Player implements Runnable {

    private String username = "no-username";
    private Socket playerSocket;
    private LinkedList<Card> hand;

    private BufferedReader in;
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
        this.username = username;
        try {
            out = new PrintWriter(playerSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println("Error: Client " + username + " " + e.toString());
        }
    }

    @Override
    public void run() {
        System.out.println("starting player thread");

        /*String t = in.nextLine();
        ioHandler.send(t);*/

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

        ioHandler.send("End");


        //ioHandler.send(in.next());
        /*
        for (String packet : packets) {

            ioHandler.send(packet);

            // Thread.sleep() to mimic heavy server-side processing
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }*/

        // TODO: read player name from socket
        System.out.println("ending player thread");
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

    public BufferedReader getInput() {
        return this.in;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
