package de.moddylp.AncientRegions;

import de.moddylp.AncientRegions.loader.config.SimpleConfig;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Language {
    private static SimpleConfig messages;

    private static String langcode = "en";

    public void setLangCode(String langCode) {
        Language.langcode = langCode;
        Language.messages = Main.getInstance().getManager().getNewConfig(langcode+"_messages.yml");
    }

    public void reload() {
        Language.messages.reloadConfig();
    }

    private void save() {
        Language.messages.saveConfig();
    }

    public void setTextOnce(String path, String text) {
        if (!Language.messages.contains(path)) {
            Language.messages.set(path, text);
            this.save();
        }
    }

    public String getText(String path) {
        if (!Language.messages.contains(path)) {
            return Language.messages.getString(path).replaceAll("&", "\u00a7");
        }
        return "&cThis sentence does not exist. Please check lang-file message ".replaceAll("&", "\u00a7") + path;
    }
}

