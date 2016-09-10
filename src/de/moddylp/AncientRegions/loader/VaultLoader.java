package de.moddylp.AncientRegions.loader;

import org.bukkit.plugin.RegisteredServiceProvider;

import de.moddylp.AncientRegions.Main;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultLoader {
	private Main plugin;
	private Economy econ;
	private Permission perms;

	public VaultLoader(Main plugin) {
		this.plugin = plugin;
	}
	public void load() {
		if (!setupEconomy() ) {
			plugin.getLogger().info(String.format("[AncientFlags] - Disabled due to no Vault dependency found!", plugin.getDescription().getName()));
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        setupPermissions();
	}
	private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        plugin.getLogger().info("Economy loaded");
        return econ != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        plugin.getLogger().info("Permissions loaded");
        return perms != null;
    }

}
