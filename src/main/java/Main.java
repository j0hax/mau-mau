import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        String hello = "Hello World";
        GsonBuilder b = new GsonBuilder();
        Gson gson = b.create();
        String convert = gson.toJson(hello);

        System.out.println(gson.fromJson(convert, String.class));
    }
}
