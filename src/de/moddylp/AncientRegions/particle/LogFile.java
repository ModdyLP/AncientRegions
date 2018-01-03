package de.moddylp.AncientRegions.particle;

import de.moddylp.AncientRegions.Main;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LogFile {
    private File f = new File(Main.getInstance().getDataFolder(), "data.yml");
    private FileConfiguration Data;

    public FileConfiguration getfile() {
        this.Data = YamlConfiguration.loadConfiguration((File)this.f);
        return this.Data;
    }

    public void setup() {
        try {
            if (!this.f.exists()) {
                Main.getInstance().getLogger().info("Creating data file");
                this.f.createNewFile();
                this.Data = this.getfile();
                this.Data.createSection("regions");
                this.Data.save(this.f);
            } else {
                this.Data = this.getfile();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setString(String option, String value) {
        try {
            this.Data = this.getfile();
            this.Data.set("regions." + option, value);
            this.Data.save(this.f);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getString(String option) {
        this.Data = this.getfile();
        return this.Data.getString("regions." + option);
    }
}

