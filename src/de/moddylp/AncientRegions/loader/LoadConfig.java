package de.moddylp.AncientRegions.loader;

import java.io.File;
import java.util.Set;

import de.moddylp.AncientRegions.Main;



public class LoadConfig {
	private Main plugin;
	public LoadConfig(Main plugin) {
		this.plugin = plugin;
	}
	public void setup() {
		try {
	        if (!plugin.getDataFolder().exists()) {
	            plugin.getDataFolder().mkdirs();
	        }
	        File file = new File(plugin.getDataFolder(), "config.yml");
	        if (!file.exists()) {
	            Main.getInstance().getLogger().info("Config.yml not found, creating!");
	            plugin.saveDefaultConfig();
	        } else {
	            Main.getInstance().getLogger().info("Config.yml found, loading!");
	            plugin.getConfig();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();

	    }
	}
	public void reload() {
		plugin.reloadConfig();
	}
	public String getOption(String option) {
		return plugin.getConfig().getString(option);
	}
	public String searchoption(String search) {
		Set<String> keys = plugin.getConfig().getKeys(true);
		for (String key : keys) {
			String value = plugin.getConfig().getString(key);
			if (value.toLowerCase().contains(search)) {
				return key;
			}
		}
		return null;
	}
	public void setOption(String option, String value) {
		plugin.getConfig().set(option, value);
		plugin.saveConfig();
	}
}
