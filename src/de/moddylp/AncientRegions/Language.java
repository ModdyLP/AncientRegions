package de.moddylp.AncientRegions;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Language {
    private final File langFile;
    private final FileConfiguration lang;

    public Language(File config) {
        this.langFile = config;
        this.lang = YamlConfiguration.loadConfiguration((File)this.langFile);
    }

    public void reload() {
        try {
            this.lang.load(this.langFile);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            this.lang.save(this.langFile);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTextOnce(String path, String text) {
        if (this.lang.getString(path) == null) {
            this.lang.set(path, text);
            this.save();
        }
    }

    public String getText(String path) {
        if (this.lang.getString(path) != null) {
            return this.lang.getString(path).replaceAll("&", "\u00a7");
        }
        return "&cThis sentence does not exist. Please check lang-file message ".replaceAll("&", "\u00a7") + path;
    }
}

