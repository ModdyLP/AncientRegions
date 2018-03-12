package de.moddylp.AncientRegions.particle;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.config.SimpleConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LogFile {
    private SimpleConfig data;

    private void getfile() {
        this.data = Main.getInstance().getManager().getNewConfig("data.yml", new String[]{
                "The plugin saves the data of the Particle Shower in this file",
        "AncientRegions v."+Main.getInstance().getDescription().getVersion()});
    }

    public void setup() {
        try {
            getfile();
            if (data != null) {
                Main.getInstance().getLogger().info("Creating data file");
                this.data.createSection("regions");
                this.data.saveConfig();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setString(String option, String value) {
        try {
            this.data.set("regions." + option, value);
            this.data.saveConfig();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getString(String option) {
        return this.data.getString("regions." + option);
    }
}

