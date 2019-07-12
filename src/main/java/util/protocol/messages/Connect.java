package util.protocol.messages;

public class Connect {

    private String clientName;

    public Connect(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return this.clientName;
    }

}
