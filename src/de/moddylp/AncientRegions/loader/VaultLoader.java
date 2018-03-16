package de.moddylp.AncientRegions.loader;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.utils.Console;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultLoader {
    private final Main plugin;

    public VaultLoader(Main plugin) {
        this.plugin = plugin;
    }

    public boolean load() {
        Plugin vault = this.plugin.getServer().getPluginManager().getPlugin("Vault");
        if (vault == null || !(vault instanceof Vault) || !this.setupEconomy()) {
            Console.error("Vault was not found or some Economy Plugin is missing! Disabling plugin.");
            Console.error("!!!!!!!!!!!!!!!!!!!! MAKE SURE YOU HAVE INSTALLED A ECONOMY PLUGIN AND VAULT !!!!!!!!!!!!!!!!!!!!!!!!");
            return false;
        }
        Console.send(String.format("Vault loaded Version %s", vault.getDescription().getVersion()));
        if (!this.setupPermissions()) {
            Console.error("Vault permission not loaded");
            return false;
        }
        return true;
    }

    private Boolean setupEconomy() {
        RegisteredServiceProvider economyProvider = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
        Economy economy = null;
        if (economyProvider != null) {
            economy = (Economy) economyProvider.getProvider();
        }
        return economy != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider permissionProvider = this.plugin.getServer().getServicesManager().getRegistration(Permission.class);
        Permission permissionHandler = null;
        if (permissionProvider != null) {
            permissionHandler = (Permission) permissionProvider.getProvider();
        }
        return permissionHandler != null;
    }
}

