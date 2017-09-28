package de.moddylp.AncientRegions.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sk89q.worldguard.protection.flags.StringFlag;
import de.moddylp.AncientRegions.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class FileDriver {

    //FILEDRIVE IS NOT TRANSLATED - CAUSES ERRORS BECAUSE LANGUAGE IS NOT LOADED
    public String FOLDER = "plugins/AncientRegions/";
    public String CONFIG = FOLDER+"config.json";

    private static FileDriver instance;
    private static HashMap<String, File> files = new HashMap<>();
    private static HashMap<String, JSONObject> jsons = new HashMap<>();

    /**
     * Get Instance
     * @return Class Instance
     */
    public static FileDriver getInstance() {
        if (instance == null) {
            instance = new FileDriver();
        }
        return instance;
    }
    public boolean checkIfFileExists(String filename) {
        return files.get(filename) != null && files.get(filename).exists();
    }
    public boolean checkIfFileisEmpty(String filename) {
        return jsons.get(filename).keySet().size() == 0;
    }

    /**
     * Create new File
     * @param filenamewithpath FileName
     */
    public void createNewFile(String filenamewithpath) {
        try {
            String[] parts = filenamewithpath.split("/");
            for (int i = 0; i < parts.length-1; i++) {
                File file = new File(parts[i]);
                if(!file.exists()) {
                    if (!file.mkdir()) {
                        Main.getInstance().getLogger().warning("Cant create Folder: "+file.getAbsolutePath());
                    }
                }
            }
            File file = new File(filenamewithpath);
            files.put(filenamewithpath, file);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Main.getInstance().getLogger().info(filenamewithpath + " created at " + file.getAbsolutePath());
                }
            } else {
                Main.getInstance().getLogger().info(filenamewithpath + " loaded at " + file.getAbsolutePath());
            }
            loadJson();
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("File can not be accessed: "+filenamewithpath);
        }
    }

    /**
     * Parse the String to a JSONObject
     * @param string Content
     * @return Jsonobject
     */
    private JSONObject parseJson(String string) {
        JSONObject json = new JSONObject();
        try {
            if (!string.equals("")) {
                JsonParser jsonParser = new JsonParser();
                json = new JSONObject(jsonParser.parse(string).getAsJsonObject().toString());
            }
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("Parsing error");
        }
        return json;
    }

    /**
     * Load Json from File
     */
    public void loadJson() {
        try {
            Main.getInstance().getLogger().info("===LOADFILES===");
            for (String filename: files.keySet()) {
                BufferedReader reader = new BufferedReader(new FileReader(files.get(filename)));
                StringBuilder content = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null) {
                    content.append(line);
                }
                jsons.put(filename, parseJson(content.toString()));

            }

        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("File can not be loaded");
        }
    }

    /**
     * Save Json to File
     */
    public void saveJson() {
        try {
            Main.getInstance().getLogger().info("===SAVEFILES===");
            for (String filename: files.keySet()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(files.get(filename)));
                String json = jsons.get(filename).toString();
                json = crunchifyPrettyJSONUtility(json);
                writer.write(json);
                writer.close();
            }
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("File can not be saved");
        }
    }
    public String crunchifyPrettyJSONUtility(String simpleJSON) {
        JsonParser crunhifyParser = new JsonParser();
        JsonObject json = crunhifyParser.parse(simpleJSON).getAsJsonObject();

        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

        return prettyGson.toJson(json);
    }
    public static JSONArray objectToJSONArray(Object object){
        Object json = null;
        JSONArray jsonArray = null;
        try {
            Main.getInstance().getLogger().info(object.toString());
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }
    public static JSONObject objectToJSONObject(Object object){
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }

    /**
     * Set A property in a specific file
     * @param filename filename
     * @param option option
     * @param value value
     */
    public void setProperty(String filename, String option, Object value) {
        try {
            if (jsons.get(filename) != null) {
                if (jsons.get(filename).has(option)) {
                    removeProperty(filename, option);
                }
                jsons.get(filename).put(option, value);
            } else {
                JSONObject json = new JSONObject();
                json.put(option, value);
                jsons.put(filename, json);
            }
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("Can not set Property: ");
        }
    }

    /**
     * Get Property of File with defaultvalue
     * @param filename filename
     * @param option option
     * @param defaultvalue defaultvalue
     * @return value
     */
    public String getProperty(String filename, String option, Object defaultvalue) {
        try {
            if (jsons.get(filename) == null || !jsons.get(filename).has(option)) {
                setProperty(filename, option, defaultvalue);
            }
        } catch (Exception ex) {
            setProperty(filename, option, defaultvalue);
        }
        return jsons.get(filename).get(option).toString();
    }
    public String getPropertyByValue(String filename, String search) {
        Set<String> keys = getAllKeysWithValues(filename).keySet();
        for (String key : keys) {
            String value = getAllKeysWithValues(filename).get(key).toString();
            if (value.toLowerCase().contains(search)) {
                return key;
            }
        }
        return null;
    }

    public boolean hasKey(String filename, String option) {
        try {
            return jsons.get(filename).has(option);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Get Property of File but without a Default Value
     * @param filename filename
     * @param option option
     * @return value
     */
    public String getPropertyOnly(String filename, String option) {
        if (jsons.get(filename).has(option)) {
            return jsons.get(filename).get(option).toString();
        } else {
            return "No Value";
        }

    }

    /**
     * Removes A property in a specific file
     * @param filename filename
     * @param option option
     */
    public void removeProperty(String filename, String option) {
        try {
            if (jsons.get(filename).has(option)) {
                jsons.get(filename).remove(option);
            }
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("Can not remove Property: ");
        }
    }

    public HashMap<String, Object> getAllKeysWithValues(String filename) {
        HashMap<String, Object> objects = new HashMap<>();
        try {
            for (Object key: jsons.get(filename).keySet()) {
                objects.put(key.toString(), jsons.get(filename).get(key.toString()));
            }
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("Can not list Property: ");
        }
        return objects;
    }



}
