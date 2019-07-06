package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Transmitter {

    private PrintWriter out;
    private BufferedReader in;

    public Transmitter(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void send(String s) {

        out.println(s);

    }

    public String receive() {

        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }

    }

}
