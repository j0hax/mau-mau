package client;

import java.io.PrintWriter;

public class Transmitter {

    private Client c;
    private PrintWriter out;

    public Transmitter(PrintWriter out) {
        this.out = out;
    }

    public void sendMessage() {
        out.println("Hello from client!");
    }

}
