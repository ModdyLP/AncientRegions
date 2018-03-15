package de.moddylp.AncientRegions;

import de.moddylp.Config;

public class Language {
    private static Config messages;

    private static String langcode = "en";

    public void setLangCode(String langCode) {
        Language.langcode = langCode;
        Language.messages = Main.getInstance().getManager().getConfig(langcode+"_messages.yml");
    }

    public void reload() {
        Language.messages.reload();
    }

    private void save() {
        Language.messages.saveToFile();
    }

    public void setTextOnce(String path, String text) {
        if (!Language.messages.containsKey(path)) {
            Language.messages.set(path, text);
            this.save();
        }
    }

    public String getText(String path) {
        if (!Language.messages.containsKey(path)) {
            return Language.messages.get(path).toString().replaceAll("&", "\u00a7");
        }
        return "&cThis sentence does not exist. Please check lang-file message ".replaceAll("&", "\u00a7") + path;
    }
}

