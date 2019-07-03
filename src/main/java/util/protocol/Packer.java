package util.protocol;

import com.google.gson.Gson;
import util.protocol.messages.Connection;

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
    public static Object unpackData(String packetString) {
        DataPacket packet = gson.fromJson(packetString, DataPacket.class);
        // TODO IDEA: each kind of message has its own class containing different kinds of data
        switch (packet.getDataType()) {
            case CONNECT:
                // example that will be changed later
                // in this case the connect message is a single string; could be something more complex later
                return gson.fromJson(packet.getData(), Connection.class);
            case DECONNECT:
            case CHATMESSAGE:
            case GAMEMESSAGE:
                break;
        }
        return null;
    }

}
