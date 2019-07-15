package server;

import util.cards.Card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.LinkedList;

public class Player implements Runnable {

    private String username = "no-username";
    private Socket playerSocket;
    private LinkedList<Card> hand = new LinkedList<>();

    private BufferedReader in;
    private PrintWriter out;
    private volatile IOHandler ioHandler;
    private boolean disconnected = false;
    private int id;
    private Thread thisThread;

    /**
     * Creates game object
     *
     * @param playerSocket own socket
     * @param ioHandler    IOHandler that connects to the lobby thread
     */
    public Player(Socket playerSocket, BufferedReader in, PrintWriter out,
                  IOHandler ioHandler) {
        this.playerSocket = playerSocket;
        this.ioHandler = ioHandler;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        thisThread = Thread.currentThread();
        thisThread.setName("\tPlayer - " + this.username + "\t\t>> ");
        System.out.println(thisThread.getName() + "starting player thread");

        while (ioHandler == null) {
            Thread.onSpinWait();

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String incomingMsg = "";

        while (!disconnected && !playerSocket.isClosed()) {
            try {
                Thread.sleep(20);
                incomingMsg = in.readLine();
                System.out.println(thisThread.getName() + "received: " + incomingMsg);

                if (incomingMsg != null) {
                    ioHandler.send(incomingMsg);
                } else {
                    break;
                }
            } catch (SocketException se) {
                System.out.println(thisThread.getName() + " > socket is closed");
                break;
            } catch (IOException io) {
                System.out.println(thisThread.getName() + " > stream is closed");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        System.out.println(thisThread.getName() + "ending player thread");
        // TODO: read player name from socket
    }

    synchronized void disconnect() {
        disconnected = true;

        try {
            in.close();
            out.close();
            playerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized void changeIOHandler(IOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    Card[] getHand() {
        return hand.toArray(new Card[hand.size()]);
    }

    void addToHand(Card c) {
        hand.add(c);
    }

    void addToHand(Card[] c) {
        for (Card card :
                c) {
            System.out.println(card.toString());
        }

        hand.addAll(Arrays.asList(c));
    }

    void removeFromHand(Card c) {
        for (Card handCard : hand) {
            if (c.equals(handCard)) {
                hand.remove(handCard);
                break;
            }
        }
    }

    String getName() {
        return username;
    }

    public BufferedReader getInput() {
        return this.in;
    }

    synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized void send(String msg) {
        out.println(msg);
    }

    public synchronized void setOut(PrintWriter out) {
        this.out = out;
    }

    synchronized void setID(int i) {
        System.out.println("setID : " + i);
        id = i;
    }

    public int getID() {
        return id;
    }

    public Socket getPlayerSocket() {
        return playerSocket;
    }
}
