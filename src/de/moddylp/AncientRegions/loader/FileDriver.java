package de.moddylp.AncientRegions.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.moddylp.AncientRegions.Main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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

    public boolean checkIfFileisEmpty(String filename) {
        return jsons.get(filename).keySet().size() == 0;
    }

    public void createNewFile() {
        try {
            File file = new File(Main.getInstance().getDataFolder(), "config.json");
            this.CONFIG = Main.getInstance().getDataFolder() + "/config.json";
            files.put(Main.getInstance().getDataFolder() + "/config.json", file);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Main.getInstance().getLogger().info(Main.getInstance().getDataFolder() + "config.json created at " + file.getAbsolutePath());
                }
            } else {
                Main.getInstance().getLogger().info(Main.getInstance().getDataFolder() + "config.json loaded at " + file.getAbsolutePath());
            }
            this.loadJson();
        }
        catch (Exception ex) {
            Main.getInstance().getLogger().warning("File can not be accessed: " + Main.getInstance().getDataFolder() + "config.json " + ex.getMessage());
        }
    }

    private JSONObject parseJson(String string) {
        JSONObject json = new JSONObject();
        try {
            if (!string.equals("")) {
                JsonParser jsonParser = new JsonParser();
                json = new JSONObject(jsonParser.parse(string).getAsJsonObject().toString());
            }
        }
        catch (Exception ex) {
            Main.getInstance().getLogger().warning("Parsing error");
        }
        return json;
    }

    public void loadJson() {
        try {
            Main.getInstance().getLogger().info("===LOADFILES===");
            for (String filename : files.keySet()) {
                String line;
                BufferedReader reader = new BufferedReader(new FileReader(files.get(filename)));
                StringBuilder content = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                jsons.put(filename, this.parseJson(content.toString()));
            }
        }
        catch (Exception ex) {
            Main.getInstance().getLogger().warning("File can not be loaded");
        }
    }

    public void saveJson() {
        try {
            Main.getInstance().getLogger().info("===SAVEFILES===");
            for (String filename : files.keySet()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(files.get(filename)));
                String json = jsons.get(filename).toString();
                json = this.crunchifyPrettyJSONUtility(json);
                writer.write(json);
                writer.close();
            }
        }
        catch (Exception ex) {
            Main.getInstance().getLogger().warning("File can not be saved" + ex.getMessage());
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            Main.getInstance().getLogger().warning("Can not set Property: ");
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

    public String getPropertyByValue(String filename, String search) {
        Set<String> keys = this.getAllKeysWithValues(filename).keySet();
        for (String key : keys) {
            String value = this.getAllKeysWithValues(filename).get(key).toString();
            if (!value.toLowerCase().contains(search)) continue;
            return key;
        }
        return null;
    }

    public boolean hasKey(String filename, String option) {
        try {
            return jsons.get(filename).has(option);
        }
        catch (Exception ex) {
            ex.printStackTrace();
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
            Main.getInstance().getLogger().warning("Can not remove Property: ");
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
            Main.getInstance().getLogger().warning("Can not list Property: ");
        }
        return objects;
    }
}

