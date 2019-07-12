package server;

import util.cards.Card;
import util.protocol.Packer;

import java.io.BufferedReader;
import java.io.IOException;
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
    private boolean disconnected = false;
    private int id;

    /**
     * Creates game object
     *
     * @param playerSocket own socket
     * @param ioHandler    IOHandler that connects to the lobby thread
     */
    public Player(Socket playerSocket, BufferedReader in, PrintWriter out, IOHandler ioHandler) {
        this.playerSocket = playerSocket;
        this.ioHandler = ioHandler;
        this.username = username;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        Thread thisThread = Thread.currentThread();
        thisThread.setName("\tPlayer - " + this.username);

        System.out.println(thisThread.getName() + "\t>> starting player thread");

        while (ioHandler == null) {
            Thread.onSpinWait();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String incomingMsg = "";
        do {
            try {
                incomingMsg = in.readLine();
                System.out.println(thisThread.getName() + "\t\t>> received: " + incomingMsg);
                ioHandler.send(incomingMsg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!disconnected);

        // TODO: read player name from socket
        System.out.println(thisThread.getName() + "\t\t>> " + "ending player thread");
    }

    synchronized void disconnect(){
        disconnected = true;
        try {
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public synchronized void send(String msg) {
        out.println(msg);
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public void setID(int i) {
        System.out.println("PlayerThread: ID : " + id);
        id = i;
    }

    public int getID(){
        return id;
    }
}
