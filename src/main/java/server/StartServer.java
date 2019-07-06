package server;

import util.protocol.DataType;
import util.protocol.Packer;
import util.protocol.messages.Connection;

import java.io.IOException;
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
                    System.out.println("SERVER\t\t\t>> New connection: " + playerSocket.getInetAddress());
                    Player p = new Player(playerSocket, null);

                    input = p.getInput().readLine();
                    String newName = ((Connection) Packer.unpackData(input)).getClientName();
                    PrintWriter out = new PrintWriter(playerSocket.getOutputStream());
                    if(names.contains(newName)){
                        out.println(Packer.packData(DataType.CONFIRM, Boolean.FALSE));
                        out.close();
                    }else {
                        out.println(Packer.packData(DataType.CONFIRM, Boolean.TRUE));
                        out.close();
                        p.setUsername(newName);
                        names.add(newName);
                        players.add(p);
                        threadPool.execute(p);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Server closed.");
            e.printStackTrace();
        }

    }
}
