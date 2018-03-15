package de.moddylp.AncientRegions.particle;

import de.moddylp.AncientRegions.Main;
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
                Main.getInstance().getLogger().info("Creating data file");
                this.data.createSection("regions");
                this.data.saveToFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setString(String option, String value) {
        try {
            this.data.set("regions." + option, value);
            this.data.saveToFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getString(String option) {
        return this.data.get("regions." + option).toString();
    }
}

