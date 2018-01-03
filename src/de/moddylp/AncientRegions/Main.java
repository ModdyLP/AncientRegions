package de.moddylp.AncientRegions;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Language;
import de.moddylp.AncientRegions.Metrics;
import de.moddylp.AncientRegions.gui.Events.GUIEvents;
import de.moddylp.AncientRegions.gui.Events.GUIOpener;
import de.moddylp.AncientRegions.loader.ConfigLoader;
import de.moddylp.AncientRegions.loader.FileDriver;
import de.moddylp.AncientRegions.loader.FlagLoader;
import de.moddylp.AncientRegions.loader.Messages;
import de.moddylp.AncientRegions.loader.VaultLoader;
import de.moddylp.AncientRegions.particle.LogFile;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;

public class Main
extends JavaPlugin {
    protected GUIEvents loader;
    public static WorldGuardPlugin worldguard;
    public Language lang;
    public static WorldEditPlugin worldedit;
    private static Main instance;
    public static FileDriver DRIVER;

    public static Main getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        worldguard = this.getWorldGuard();
        worldedit = this.setupWorldEdit();
        this.getLogger().info("Worldguard hooked");
        VaultLoader vaultloader = new VaultLoader(this);
        if (!vaultloader.load()) {
            this.onDisable();
            return;
        }
        ConfigLoader.saveDefaultconfig();
        this.lang = new Language(new File(this.getDataFolder(), "messages.yml"));
        this.loadMessages();
        LogFile file = new LogFile();
        file.setup();
        this.loader = new GUIEvents(this, worldguard, worldedit);
        FlagLoader.load();
        if (DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_metrics").equals("true")) {
            try {
                de.moddylp.AncientRegions.bukkit.Metrics metrics = new de.moddylp.AncientRegions.bukkit.Metrics(this);
                Metrics metrics1 = new Metrics((Plugin)this);
                metrics1.start();
            }
            catch (Exception ex) {
                this.getLogger().warning("Metrics cant be enabled because there was an error: " + ex.getLocalizedMessage());
            }
        }
        System.out.println(this.lang.getText("Enabled"));
    }

    public void onDisable() {
        System.out.println(this.lang.getText("Disabled"));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ancientregions")) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "gui": {
                        if (sender instanceof Player) {
                            Player p = ((Player)sender).getPlayer();
                            if (DRIVER.hasKey(Main.DRIVER.CONFIG, "_worlds")) {
                                JSONArray worldconfig = FileDriver.objectToJSONArray(DRIVER.getProperty(Main.DRIVER.CONFIG, "_worlds", "[]"));
                                ArrayList<String> worlds = new ArrayList<String>();
                                for (Object object : worldconfig) {
                                    worlds.add(object.toString());
                                }
                                if (worlds.contains(p.getWorld().getName())) {
                                    if (p.hasPermission("ancient.regions.flag.command")) {
                                        GUIOpener opener = new GUIOpener(this.loader);
                                        opener.openstartgui((CommandSender)p);
                                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_SNARE, 100.0f, 100.0f);
                                        return true;
                                    }
                                    sender.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("Permission"));
                                    return true;
                                }
                                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("World"));
                                return true;
                            }
                            p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("World"));
                            return true;
                        }
                        this.getLogger().log(Level.INFO, "{0}[AR][ERROR] {1}", new Object[]{ChatColor.RED, this.lang.getText("Console")});
                        return true;
                    }
                    case "reload": {
                        if (sender.hasPermission("ancient.regions.admin.reload")) {
                            DRIVER.loadJson();
                            this.lang.reload();
                            sender.sendMessage(ChatColor.GOLD + "[AR][INFO] " + this.lang.getText("ConfigReload"));
                            return true;
                        }
                        sender.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("Permission"));
                        break;
                    }
                    case "cancelall": {
                        try {
                            if (sender.hasPermission("ancient.regions.admin.cancelall")) {
                                LogFile file = new LogFile();
                                RegionContainer container = worldguard.getRegionContainer();
                                this.getServer().getWorlds().stream().map(world -> container.get(world)).map(regions -> regions.getRegions()).forEach(rgids -> {
                                    rgids.values().stream().filter(rg -> file.getString(rg.getId()) != null).forEach(rg -> {
                                        file.setString(rg.getId(), null);
                                    }
                                    );
                                }
                                );
                                sender.sendMessage(ChatColor.GREEN + "[AR][ERROR] " + this.lang.getText("Canceled"));
                                return true;
                            }
                            sender.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("Permission"));
                        }
                        catch (Exception file) {}
                        break;
                    }
                    case "info": 
                    case "help": {
                        sender.sendMessage(ChatColor.BLUE + "============AncientFlags About============");
                        sender.sendMessage(ChatColor.GOLD + "AncientRegions " + this.getDescription().getVersion());
                        sender.sendMessage(ChatColor.GOLD + "Written by " + this.getDescription().getAuthors());
                        sender.sendMessage(ChatColor.GOLD + "Developed for Mystic-World");
                        sender.sendMessage(ChatColor.BLUE + "=============Plugin Help===============");
                        sender.sendMessage(ChatColor.GOLD + "/ancientregions gui - Open the GUI");
                        sender.sendMessage(ChatColor.GOLD + "/ancientregions info - Show this Message");
                        sender.sendMessage(ChatColor.GOLD + "/ancientregions reload - Reload Config");
                        sender.sendMessage(ChatColor.GOLD + "/ancientregions cancelall - Cancel all Border Activities.");
                        break;
                    }
                    default: {
                        sender.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("CommandNotFound"));
                    }
                }
                return true;
            }
            if (sender instanceof Player) {
                Player p = ((Player)sender).getPlayer();
                p.performCommand("ancr gui");
                return true;
            }
            this.getLogger().log(Level.INFO, "{0}[AR][ERROR] {1}", new Object[]{ChatColor.RED, this.lang.getText("Console")});
            return true;
        }
        return false;
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            System.out.println(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("Worldguard"));
        }
        return (WorldGuardPlugin)plugin;
    }

    public WorldEditPlugin setupWorldEdit() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return null;
        }
        return (WorldEditPlugin)plugin;
    }

    private void loadMessages() {
        Messages messages = new Messages(this);
    }

    static {
        DRIVER = FileDriver.getInstance();
    }
}

