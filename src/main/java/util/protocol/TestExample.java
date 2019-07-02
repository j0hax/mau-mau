package util.protocol;

/**
 * Testing the Packer
 */
public class TestExample {

    public static void main(String[] args) {
        /*
        client side
         */
        String connectStatus = "new client"; // could be special class that contains data about client etc.
        String packedData = Packer.packData(DataType.CONNECT, connectStatus); //for now CONNECT only works for Strings

        // sending data to server ...

        /*
        server side
         */
        System.out.println("raw message:\t\t" + packedData); // only for testing

        System.out.println("decoded message:\t" + Packer.unpackData(packedData));


    }

}
