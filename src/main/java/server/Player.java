package server;

import util.Card;

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
    private IOHandler ioHandler;

    /**
     * Creates game object
     *
     * @param playerSocket own socket
     * @param ioHandler      IOHandler that connects to the lobby thread
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
        out.println("df");
        try {
            while (true) {
                System.out.println(ioHandler == null ? "null" : ioHandler.toString());
                Thread t = Thread.currentThread();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.out.println("sleep fail" + e.toString());
        }

        // TODO: read player name from socket
    }

    public synchronized void changeIOHandler(IOHandler ioHandler) {
        this.ioHandler = ioHandler;
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
