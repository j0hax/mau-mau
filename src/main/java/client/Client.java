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
    private int winner;

    private BooleanProperty handUpdatedProperty = new SimpleBooleanProperty(false);
    private BooleanProperty gameOver = new SimpleBooleanProperty(false);
    private boolean stopClientThreads = false;
    private String[] allPlayerNames;

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
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sendData(DataType.CONNECT, new Connect(this.getName()));
            boolean nameConfirmed = (boolean) receiveData();

            if (!nameConfirmed) {
                System.out.println("You cannot use this name.");
                closeConnection();
            } else {
                System.out.println("Server has confirmed your name.");
                connected = true;
                NewGame ng = (NewGame) receiveData();
                ID = ng.getPlayerID();
                setNewGame(ng);
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

    private void setNewGame(NewGame ng) {
        allPlayerNames = ng.getAllPlayers();
        System.out.println("First card: " + ng.getFirstCard().toString());
        setGameState(new GameState(0, ng.getInitialHand(), ng.getNumberOfCards(),
                ng.getFirstCard()));
        System.out.println(ng.getFirstCard().toString());
    }

    private void setGameState(GameState gameState) {
        currentGameState = gameState;
    }

    public String[] getAllPlayerNames() {
        return allPlayerNames;
    }

    /**
     * Disconnects the Client from the Server
     *
     * */
    public void closeConnection() {
        // TODO SEND DISCONNECT MESSAGE TO SERVER
        try {
            Thread.sleep(500);
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
        sendData(DataType.DISCONNECT, ID);
    }

    private boolean stopThreads() {
        return stopClientThreads;
    }

    public int getID() {
        return ID;
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
    private Object receiveData() {
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
     *
     * used by GameController to update GameState
     *
     * @return current GameState
     */
    synchronized GameState getCurrentGameState() {
        return this.currentGameState;
    }

    /**
     * Sends an object to the server
     *
     * @param tag         Type of the object to send
     * @param classToPack Data structure which will be sent to the server
     */
    void sendData(DataType tag, Object classToPack) {
        if (tag == DataType.CONNECT) {
            out.println(Packer.packData(tag, classToPack));
        } else if (getCurrentGameState().activePlayerIndex() == ID
                || (tag != DataType.CARDSUBMISSION
                && tag != DataType.CARDWISH && tag != DataType.CARDREQUEST)) {
            out.println(Packer.packData(tag, classToPack));
        } else {
            System.out.println("Not your turn");
        }
    }

    BooleanProperty getHandUpdatedProperty() {
        return handUpdatedProperty;
    }

    synchronized void setHandUpdatedProperty(boolean b) {
        handUpdatedProperty.set(b);
    }

    BooleanProperty getGameOverProperty() {
        return gameOver;
    }

    synchronized void setGameOverProperty(boolean b) {
        gameOver.set(b);
    }


    /**
     * Submit the players "wish" for a card to the server.
     *
     * @param s the suite of card the opponent should get next.
     */
    void wishCard(CardSuite s) {
        System.out.println("Client wished " + s);
        sendData(DataType.CARDWISH, s);
    }

    /**
     * Submit the chosen card from the hand to the server.
     *
     * @param c the card to be used
     */
    void layCard(Card c) {
        // faster than converting to an List, etc.
        for (Card h : getCurrentGameState().getHand()) {
            if (c.equals(h)) {
                sendData(DataType.CARDSUBMISSION, c);
            }
        }
    }

    /**
     * Submit the new card request to the server
     */
    void requestCard() {
        sendData(DataType.CARDREQUEST, ID);
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
            /*try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            //sendData(DataType.CHATMESSAGE, "hi");
            try {
                String input = in.readLine();
                DataPacket d = Packer.getDataPacket(input);

                if (d.getDataType() == DataType.CONFIRM) {
                    closeConnection();
                    break;
                } else if (d.getDataType() == DataType.GAMESTATE) {
                    setGameState(((GameState) Packer.unpackData(input)));
                } else if (d.getDataType() == DataType.GAMEOVER) {
                    setWinner((Integer) Packer.unpackData(input));
                    setGameOverProperty(true);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            setHandUpdatedProperty(true);
            //setHandUpdatedProperty(false);
        }

        System.out.println("Client listener thread closed.");
    }

    private void setWinner(int winner) {
        this.winner = winner;
    }

    public int getWinner() {
        return winner;
    }
}
