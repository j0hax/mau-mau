package util.protocol.messages;

public class NewGame {

    private String[] allPlayers;

    public NewGame(String[] allPlayers) {
        this.allPlayers = allPlayers;
    }

    public String[] getAllPlayers() {
        return allPlayers;
    }

}
