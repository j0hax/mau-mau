package util.protocol;

import com.google.gson.Gson;
import util.cards.Card;
import util.game.GameState;
import util.protocol.messages.Connect;
import util.protocol.messages.Disconnect;
import util.protocol.messages.NewGame;

/**
 * Packer should be used if you want to pack data in a DataPacket structure
 */
public class Packer {

    private static Gson gson = new Gson();

    /**
     * Packs an object in a string by using a DataPacket
     *
     * @param type        Type of object/message
     * @param classToPack Object to pack
     * @return DataPacket packed in a String
     */
    public static String packData(DataType type, Object classToPack) {
        DataPacket exportPacket = new DataPacket(type, gson.toJson(classToPack));
        return gson.toJson(exportPacket);
    }

    /**
     * Unpacks a DataPacket and its information which is packet into a String
     *
     * @param packetString String to unpack
     * @return Returns an object that has been stored in the DataPacket when it was created
     */

    public static DataPacket getDataPacket(String packetString) {
        return gson.fromJson(packetString, DataPacket.class);
    }


    public static Object unpackData(String packetString) {
        DataPacket packet = gson.fromJson(packetString, DataPacket.class);

        // TODO IDEA: each kind of message has its own class containing different kinds of data
        switch (packet.getDataType()) {
            case CONNECT:
                return gson.fromJson(packet.getData(), Connect.class);

            case CONFIRM:
                return gson.fromJson(packet.getData(), Boolean.class);

            case DISCONNECT:
                return gson.fromJson(packet.getData(), Disconnect.class);

            case CHATMESSAGE:
                return gson.fromJson(packet.getData(), String.class);

            case GAMESTATE:
                return gson.fromJson(packet.getData(), GameState.class);

            case NEWGAME:
                return gson.fromJson(packet.getData(), NewGame.class);
            case CARDSUBMISSION:
                return gson.fromJson(packet.getData(), Card.class);
        }

        return null;
    }

}
