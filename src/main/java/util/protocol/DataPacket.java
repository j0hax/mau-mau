package util.protocol;

/**
 * Class represents the data that is send between clients and server
 * TODO create classes for datatypes and
 */
public class DataPacket {

    private DataType dataType;
    private String data;

    /**
     * Creates new DataPacket Object
     *
     * @param type    Type of data
     * @param message Message to store
     */
    public DataPacket(DataType type, String message) {
        this.dataType = type;
        this.data = message;
    }

    public String getData() {
        return data;
    }

    public DataType getDataType() {
        return this.dataType;
    }


}
