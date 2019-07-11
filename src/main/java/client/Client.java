package client;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import util.protocol.DataType;
import util.protocol.Packer;

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
    private int port;
    private String serverIP;
    private Socket clientSocket;
    private boolean connected= false;
    private PrintWriter out;
    private BufferedReader in;
    private Transmitter transmitter;

    private BooleanProperty handUpdatedProperty = new SimpleBooleanProperty(false);


    /**
     * Constructor for Client
     * @param name Client Name
     * @param port Port on which the Client wants to connect
     * @param serverIP Ip of the Server the client wants to connect to
     * */
    Client(String name, int port, String serverIP) {
        this.name=name;
        this.port=port;
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
            System.out.println("Trying to Connect to: " +serverIP+" on port: " +port);
            clientSocket = new Socket(serverIP,port);
            System.out.println("Connected");
            connected = true;

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (UnknownHostException ue) {
            System.out.println("Unknown Host, Check your ip-address/Port input" +ue.getMessage());
        } catch (IOException io) {
            System.out.println("IOError: Check your ip-address/Port input " + io.getMessage());
        }

        transmitter = new Transmitter(out, in);
    }

    /**
     * Disconnects the Client from the Server
     *
     * */
    public void closeConnection() {
        connected = false;
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public boolean getConnectionStatus() {
        return connected;
    }

    /**
     * Wrapper for unpacking data from the Transmitter
     *
     * @return object from the Transmitter
     */
    public Object receiveData() {
        return Packer.unpackData(transmitter.receive());
    }

    /**
     * Sends an object to the server
     *
     * @param tag         Type of the object to send
     * @param classToPack Data structure which will be sent to the server
     */
    public void sendData(DataType tag, Object classToPack) {
        transmitter.send(Packer.packData(tag, classToPack));
    }

    public boolean gethandUpdated() {
        return handUpdatedProperty.get();
    }

    public BooleanProperty getHandUpdatedProperty() {
        return handUpdatedProperty;
    }

    public void setHandUpdatedProperty(boolean b) {
        handUpdatedProperty.set(b);
    }

    /**
     * Continuously reads from socket to update the GUI
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setHandUpdatedProperty(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setHandUpdatedProperty(false);
        }
    }
}
