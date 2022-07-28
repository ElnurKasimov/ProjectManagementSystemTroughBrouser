package model.prefs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Prefs {
    public static final String DB_JDBC_CONNECTION_URL = "dbUrl";
    public static final String DB_JDBC_USERNAME = "dbUserName";
    public static final String DB_JDBC_PASSWORD = "dbPassword";
    public static final String FRONTCONTROLLER_TEMPLATES = "frontcontroller_templates"
            + "/.SmartTomcat/ProjectManagementSystem/ProjectManagementSystem.main/frontcontroller_templates/tempates/";

    public static final String DEFAULT_PREFS_FILENAME = "prefs.json";

    private Map<String, Object> prefs = new HashMap<>();

    public Prefs() {
        this(DEFAULT_PREFS_FILENAME);
    }

    public Prefs(String filename) {
        try {
              String json = String.join(
                    "\n",
                    Files.readAllLines(Paths.get(filename).toAbsolutePath())
            );

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
