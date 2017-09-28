package de.moddylp.AncientRegions.particle;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.moddylp.AncientRegions.Main;

public class LogFile {


	private File f;
	private FileConfiguration Data;

	public LogFile() {
	    f = new File(Main.FOLDER, "data.yml");
	}
	public FileConfiguration getfile() {
		return Data = YamlConfiguration.loadConfiguration(f);
	}
    public void setup(){
    	try {
	    	if (!f.exists()) {
	    		Main.getInstance().getLogger().info("Creating data file");
	    		f.createNewFile();
	    		Data = getfile();
	    		Data.createSection("regions");
	    		Data.save(f);
	    	} else {
	    		Data = getfile();
	    	}
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	}
    }
    public void setString(String option, String value) {
    	try {
	    	Data = getfile();
	    	Data.set("regions."+option, value);
	    	Data.save(f);
    	} catch (Exception ex) {
	    	ex.printStackTrace();
	    }
    }
    public String getString(String option) {
    	Data = getfile();
    	return Data.getString("regions."+option);
    }
	
}
