package client;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import util.cards.Card;
import util.cards.CardSuite;
import util.game.GameState;
import util.protocol.DataPacket;
import util.protocol.DataType;
import util.protocol.Packer;
import util.protocol.messages.Connect;
import util.protocol.messages.Disconnect;
import util.protocol.messages.NewGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
* Client Class, creates the Client and connects it to the Server when it's possible
* */
public class Client implements Runnable {
    private String name;
    private int ID;
    private int port;
    private String serverIP;
    private Socket clientSocket;
    private boolean connected = false;
    private PrintWriter out;
    private BufferedReader in;
    private GameState currentGameState;

    private BooleanProperty handUpdatedProperty = new SimpleBooleanProperty(false);
    private boolean stopClientThreads = false;
    private int activePlayer;


    /**
     * Constructor for Client
     * @param name Client Name
     * @param port Port on which the Client wants to connect
     * @param serverIP Ip of the Server the client wants to connect to
     * */
    Client(String name, int port, String serverIP) {
        this.name = name;
        this.port = port;
        this.serverIP = serverIP;
        connectClient();
    }
    /**
     * Connects the Client to the Server
     *
     * */
    private void connectClient() {
        try {
            //a lot of Print outs  to the console for debugging purpose
            System.out.println("Trying to Connect to: " + serverIP + " on port: " + port);
            clientSocket = new Socket(serverIP, port);
            System.out.println("Connected");
            connected = true;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sendData(DataType.CONNECT, new Connect(this.getName()));
            boolean nameConfirmed = (boolean) receiveData();

            if (!nameConfirmed) {
                System.out.println("You cannot use this name.");
                closeConnection();
            } else {
                System.out.println("Server has confirmed your name.");
                NewGame ng = (NewGame) receiveData();
                ID = ng.getPlayerID();
                currentGameState = new GameState(ID, ng.getInitialHand());
                System.out.println("ID: " + ID);

                for (String p : ng.getAllPlayers()) {
                    System.out.println(p);
                }

                for (Card c : ng.getInitialHand()) {
                    System.out.println(c);
                }
            }
        } catch (UnknownHostException ue) {
            System.out.println("Unknown Host, Check your ip-address/Port input" +
                    ue.getMessage());
        } catch (IOException io) {
            System.out.println("IOError: Check your ip-address/Port input " +
                    io.getMessage());
        }
    }

    /**
     * Disconnects the Client from the Server
     *
     * */
    public void closeConnection() {
        // TODO SEND DISCONNECT MESSAGE TO SERVER
        try {
            Thread.sleep(2000);
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        connected = false;

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void close() {
        stopClientThreads = true;
        sendData(DataType.DISCONNECT, new Disconnect(true));
    }

    private boolean stopThreads() {
        return stopClientThreads;
    }

    public int getID(){
        return ID;
    }
    public int getActivePlayer(){
        return activePlayer;
    }
    public PrintWriter getOutput() {
        return out;
    }

    public BufferedReader getInput() {
        return in;
    }

    /**
     * Getter for the name of the Client
     * @return Name of the Client
     * */
    public String getName() {
        return name;
    }
    /**
     * Getter for the port on which the Client wants to connect
     * @return port of the client
     * */
    public int getPort() {
        return port;
    }
    /**
     * Getter for the serverIp of the Client
     * @return ip of the server the client wants to connect to
     * */
    public String getServerIP() {
        return serverIP;
    }
    /**
     * Getter for the connectionStatus of the client
     * @return true/false whether or not the client is connected
     * */
    boolean getConnectionStatus() {
        return connected;
    }

    /**
     * unpacking data from the BufferedReader
     *
     * @return object from the Packer
     */
    Object receiveData() {
        String msg;

        try {
            msg = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return Packer.unpackData(msg);
    }

    /**
     * used by GameController to update hand
     *
     * @return current hand from GameState
     */
    public synchronized Card[] getCurrentHand() {
        return this.currentGameState.getHand();
    }

    /**
     * Sends an object to the server
     *
     * @param tag         Type of the object to send
     * @param classToPack Data structure which will be sent to the server
     */
    public void sendData(DataType tag, Object classToPack) {
        if (activePlayer == ID || (tag != DataType.CARDSUBMISSION
                && tag != DataType.CARDWISH)) {
            out.println(Packer.packData(tag, classToPack));
        } else {
            System.out.println("Not your turn");
        }
    }

    public boolean gethandUpdated() {
        return handUpdatedProperty.get();
    }

    public BooleanProperty getHandUpdatedProperty() {
        return handUpdatedProperty;
    }

    public synchronized void setHandUpdatedProperty(boolean b) {
        handUpdatedProperty.set(b);
    }

    /**
     * Submit the players "wish" for a card to the server.
     *
     * @param s the suite of card the opponent should get next.
     */
    public void wishCard(CardSuite s) {
        System.out.println("Client wished " + s);
        sendData(DataType.CARDWISH, s);
    }

    /**
     * Submit the chosen card from the hand to the server.
     *
     * @param c the card to be used
     */
    public void layCard(Card c) {
        // faster than converting to an List, etc.
        for (Card h : getCurrentHand()) {
            if (c.equals(h)) {
                sendData(DataType.CARDSUBMISSION, c);
            }
        }
    }

    /**
     * Continuously reads from socket to update the GUI
     */
    @Override
    public void run() {
        System.out.println("Client listener thread started.");
        Thread t = Thread.currentThread();
        t.setName("ClientT");
        setHandUpdatedProperty(true);

        while (!stopThreads()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //sendData(DataType.CHATMESSAGE, "hi");
            try {
                String input = in.readLine();
                DataPacket d = Packer.getDataPacket(input);

                if (d.getDataType() == DataType.CONFIRM) {
                    closeConnection();
                    break;
                } else if (d.getDataType() == DataType.GAMESTATE) {
                    currentGameState = ((GameState) Packer.unpackData(input));
                    activePlayer = currentGameState.activePlayerIndex();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setHandUpdatedProperty(true);
            //setHandUpdatedProperty(false);
        }

        System.out.println("Client listener thread closed.");
    }
}
