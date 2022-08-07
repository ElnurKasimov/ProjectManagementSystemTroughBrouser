package model.default_settings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Prefs {
    public static final String DB_JDBC_CONNECTION_URL = "dbUrl";
    public static final String DB_JDBC_USERNAME = "dbUserName";
    public static final String DB_JDBC_PASSWORD = "dbPassword";

    public static final String DEFAULT_PREFS_FILENAME = "prefs.json";

    private Map<String, Object> prefs = new HashMap<>();

    public Prefs() {
        this(DEFAULT_PREFS_FILENAME);
    }

    public Prefs(String filename) {
        System.out.println("trying to read file");
         /*
        try {
           String json = String.join(
                    "\n",
                    Files.readAllLines(Paths.get(filename))
                    );
            */
            String json = "";
            try (FileReader fr = new FileReader(filename); BufferedReader reader = new BufferedReader(fr);){

                //создаем BufferedReader с существующего FileReader для построчного считывания

                // считаем сначала первую строку
                String line = reader.readLine();
                while (line != null) {
                    json = json + line + "\n";
                    // считываем остальные строки в цикле
                    line = reader.readLine();
                }
                System.out.println(json);
                
            TypeToken<?> typeToken = TypeToken.getParameterized(
                    Map.class,
                    String.class,
                    Object.class
            );
            prefs = new Gson().fromJson(json, typeToken.getType());

            System.out.println(prefs);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public String getString(String key) {
        return getPref(key).toString();
    }

    public Object getPref(String key) {
        return prefs.get(key);
    }

}
