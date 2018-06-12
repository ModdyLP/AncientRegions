package de.moddylp.AncientRegions.particle;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.utils.Console;
import de.moddylp.simplecommentconfig.Config;

public class LogFile {
    private Config data;

    private void getfile() {
        this.data = Main.getInstance().getManager().getConfig(Main.getInstance().getDataFolder()+"/"+"data.data");
        this.data.setHeader(new String[]{
                "The plugin saves the data of the Particle Shower in this file",
                "AncientRegions v." + Main.getInstance().getDescription().getVersion()});
    }

    public void setup() {
        try {
            getfile();
            if (data != null) {
                Console.send("Creating data file");
                this.data.createSection("regions");
                this.data.saveToFile();
            }
        } catch (Exception ex) {
            Console.error(ex.getMessage());
        }
    }

    public void setString(String option, String value) {
        try {
            this.data.set("regions." + option, value);
            this.data.saveToFile();
        } catch (Exception ex) {
            Console.error(ex.getMessage());
        }
    }
    public void remove(String option) {
        try {
            this.data.remove("regions." + option);
            this.data.saveToFile();
        } catch (Exception ex) {
            Console.error(ex.getMessage());
        }
    }

    public String getString(String option) {
        this.data.reload();
        if (this.data.get("regions." + option) == null) {
            return null;
        }
        return this.data.get("regions." + option).toString();
    }
}

