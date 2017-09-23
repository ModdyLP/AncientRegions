package de.moddylp.AncientRegions.loader;

import de.moddylp.AncientRegions.Main;
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
        Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault"); // speichert das Objekt des Plugins Vault in vault

        if (vault != null && vault instanceof Vault && setupEconomy()) { // wenn vault erfolgreich geladen wurde, d.h. auf dem Server vorhanden ist und aktiv ist und wirklich eine Instanz vom Vault plugin ist EDIT wenn dies nicht der Fall ist das plugin unloaden ??? warum war hier ein & geht nicht auch &&
            plugin.getLogger().info(String.format("Enabled Version %s", vault.getDescription().getVersion())); // Informiere, dass Plugin aktiviert wurde MOVE zum ende von enable
        } else {
            plugin.getLogger().warning("Vault was not found or some Economy Plugin is missing! Disabling plugin."); // warnen, dass Vault nicht gefunden wurde.
            plugin.getLogger().warning("!!!!!!!!!!!!!!!!!!!! MAKE SURE YOU HAVE INSTALLED A ECONOMY PLUGIN AND VAULT !!!!!!!!!!!!!!!!!!!!!!!!");
            return false;
        }
        if (!setupPermissions()) {
            this.plugin.getLogger().warning("Vault permission not loaded");
            return false;
        }
        return true;
    }
    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class); // eine liste aller mit vault verbundenen Economy systeme erstellen
        Economy economy = null;
        if (economyProvider != null) { // wenn es einen Economy provider gibt
            economy = economyProvider.getProvider(); // diesen Benutzen
        }
        return (economy != null); // --- überflüssig, wird sowieso nicht überprüft
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        Permission permissionHandler = null;
        if (permissionProvider != null) {
            permissionHandler = permissionProvider.getProvider();
        }
        return (permissionHandler != null);
    }

}
