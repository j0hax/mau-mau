package server;

import util.protocol.DataType;
import util.protocol.Packer;
import util.protocol.messages.Connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartServer {

    private static final int PORT = 55555;
    private static final int maxNumberOfPlayers = 20;

    public static void main(String[] args) {

        System.out.println("MAIN THREAD\t\t>> Starting Mau-Mau Server...");

        //contains all players
        LinkedList<Player> players = new LinkedList<>();
        LinkedList<String> names = new LinkedList<>();

        try (ServerSocket server = new ServerSocket(PORT)) {
            //IOHandler ioHandler = new IOHandler();
            ExecutorService threadPool = Executors.newFixedThreadPool(maxNumberOfPlayers);

            //creates and starts lobby
            IOHandler lobbyIOHandler = new IOHandler();
            Thread lobbyThread = new Thread(new Lobby(players, lobbyIOHandler));
            lobbyThread.start();

            String input = "";

            while (true) {
                try {
                    Socket playerSocket = server.accept();
                    PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

                    System.out.println("SERVER\t\t\t>> New connection: " + playerSocket.getInetAddress());
                    Player p = new Player(playerSocket, in, out, null);

                    input = p.getInput().readLine();
                    String newName = ((Connect) Packer.unpackData(input)).getClientName();
                    if(names.contains(newName)) {
                        p.send(Packer.packData(DataType.CONFIRM, Boolean.FALSE));
                        out.close();
                    } else {
                        p.send(Packer.packData(DataType.CONFIRM, Boolean.TRUE));

                        p.setUsername(newName);
                        names.add(newName);
                        players.add(p);
                        threadPool.execute(p);
                    }

                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Server closed.");
            e.printStackTrace();
        }

    }
}
