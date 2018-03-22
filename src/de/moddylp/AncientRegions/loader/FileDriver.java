package de.moddylp.AncientRegions.loader;

import com.google.gson.*;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.utils.Console;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;

public class FileDriver {
    public String CONFIG = "";
    private static FileDriver instance;
    private static HashMap<String, File> files;
    private static HashMap<String, JSONObject> jsons;

    public static FileDriver getInstance() {
        if (instance == null) {
            instance = new FileDriver();
            files = new HashMap<>();
            jsons = new HashMap<>();
        }
        return instance;
    }

    public boolean checkIfFileExists(String filename) {
        return files.get(filename) != null && files.get(filename).exists();
    }
    public boolean deletefile(String filename) {
        if (files.get(filename) != null && files.get(filename).exists()){
            if (!files.get(filename).delete()) {
                Console.error("Cant delete old File");
                return false;
            }
        }
        return true;
    }

    public boolean checkIfFileisEmpty(String filename) {
        return jsons.get(filename).keySet().size() == 0;
    }

    public void createNewFile() {
        try {
            File file = new File(Main.getInstance().getDataFolder(), "config.json");
            this.CONFIG = Main.getInstance().getDataFolder() + "/config.json";
            files.put(Main.getInstance().getDataFolder() + "/config.json", file);
            /*if (!file.exists()) {
                if (file.createNewFile()) {
                    Main.getInstance().getLogger().info(Main.getInstance().getDataFolder() + "config.json created at " + file.getAbsolutePath());
                }
            } else {
                Main.getInstance().getLogger().info(Main.getInstance().getDataFolder() + "config.json loaded at " + file.getAbsolutePath());
            }*/
            if (file.exists()) {
                this.loadJson();
            }
        }
        catch (Exception ex) {
            Main.getInstance().getLogger().warning("File can not be accessed: " + Main.getInstance().getDataFolder() + "config.json " + ex.getMessage());
        }
    }

    private JSONObject parseJson(String string) {
        JSONObject json = new JSONObject();
        try {
            if (!string.equalsIgnoreCase("")) {
                JsonParser jsonParser = new JsonParser();
                json = new JSONObject(jsonParser.parse(string).getAsJsonObject().toString());
            }
        }
        catch (Exception ex) {
            Console.error("Parsing error");
        }
        return json;
    }

    public void loadJson() {
        try {
            Console.send("===LOADFILES===");
            for (String filename : files.keySet()) {
                String line;
                BufferedReader reader = new BufferedReader(new FileReader(files.get(filename)));
                StringBuilder content = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                jsons.put(filename, this.parseJson(content.toString()));
                reader.close();
            }
        }
        catch (Exception ex) {
            Console.error("File can not be loaded");
        }
    }

    public void saveJson() {
        try {
            Console.send("===SAVEFILES===");
            for (String filename : files.keySet()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(files.get(filename)));
                String json = jsons.get(filename).toString();
                json = this.crunchifyPrettyJSONUtility(json);
                writer.write(json);
                writer.close();
            }
        }
        catch (Exception ex) {
            Console.error("File can not be saved" + ex.getMessage());
        }
    }

    public String crunchifyPrettyJSONUtility(String simpleJSON) {
        JsonParser crunhifyParser = new JsonParser();
        JsonObject json = crunhifyParser.parse(simpleJSON).getAsJsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        ArrayList<String> keys = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            keys.add(entry.getKey());
        }
        keys.sort(Comparator.naturalOrder());
        JsonObject sortedJson = new JsonObject();
        for (String key : keys) {
            sortedJson.add(key, json.get(key));
        }
        return gson.toJson(sortedJson);
    }

    public static JSONArray objectToJSONArray(Object object) {
        Object json = null;
        JSONArray jsonArray = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        }
        catch (JSONException e) {
            Console.error(e.getMessage());
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray)json;
        }
        return jsonArray;
    }

    public static JSONObject objectToJSONObject(Object object) {
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        }
        catch (JSONException e) {
            Console.error(e.getMessage());
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject)json;
        }
        return jsonObject;
    }

    public void setProperty(String filename, String option, Object value) {
        try {
            if (jsons.get(filename) != null) {
                if (jsons.get(filename).has(option)) {
                    this.removeProperty(filename, option);
                }
                jsons.get(filename).put(option, value);
            } else {
                JSONObject json = new JSONObject();
                json.put(option, value);
                jsons.put(filename, json);
            }
        }
        catch (Exception ex) {
            Console.error("Can not set Property: ");
        }
    }

    public String getProperty(String filename, String option, Object defaultvalue) {
        try {
            if (jsons.get(filename) == null || !jsons.get(filename).has(option)) {
                this.setProperty(filename, option, defaultvalue);
            }
        }
        catch (Exception ex) {
            this.setProperty(filename, option, defaultvalue);
        }
        return jsons.get(filename).get(option).toString();
    }
    public Object getPropertyAsObj(String filename, String option, Object defaultvalue) {
        try {
            if (jsons.get(filename) == null || !jsons.get(filename).has(option)) {
                this.setProperty(filename, option, defaultvalue);
            }
        }
        catch (Exception ex) {
            this.setProperty(filename, option, defaultvalue);
        }
        return jsons.get(filename).get(option);
    }

    public String getPropertyByValue(String filename, String search) {
        HashMap<String, Object> map = this.getAllKeysWithValues(filename);
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if (map.get(key).toString().toLowerCase().equalsIgnoreCase(search)) {
                return key;
            }
        }
        Console.error("Nothing found for search: "+search);
        return "Nothing";
    }

    public boolean hasKey(String filename, String option) {
        try {
            return jsons.get(filename).has(option);
        }
        catch (Exception ex) {
            Console.error(ex.getMessage());
            return false;
        }
    }

    public String getPropertyOnly(String filename, String option) {
        if (jsons.get(filename).has(option)) {
            return jsons.get(filename).get(option).toString();
        }
        return "No Value";
    }

    public void removeProperty(String filename, String option) {
        try {
            if (jsons.get(filename).has(option)) {
                jsons.get(filename).remove(option);
            }
        }
        catch (Exception ex) {
            Console.error("Can not remove Property: ");
        }
    }

    public HashMap<String, Object> getAllKeysWithValues(String filename) {
        HashMap<String, Object> objects = new HashMap<>();
        try {
            for (String key : jsons.get(filename).keySet()) {
                objects.put(key, jsons.get(filename).get(key));
            }
        }
        catch (Exception ex) {
            Console.error("Can not list Property: ");
        }
        return objects;
    }
}

