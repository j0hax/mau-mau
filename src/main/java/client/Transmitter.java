package client;

import java.io.PrintWriter;

public class Transmitter {

    private PrintWriter out;

    public Transmitter(PrintWriter out) {
        this.out = out;
    }

    public void send(String s) {

        out.println(s);

    }

}
