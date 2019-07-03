package util.protocol.messages;

public class Connection {

    private String clientName;

    public Connection(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return this.clientName;
    }

}
