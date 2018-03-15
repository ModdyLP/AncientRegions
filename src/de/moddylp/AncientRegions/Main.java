package de.moddylp.AncientRegions;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.moddylp.AncientRegions.flags.FlagOBJ;
import de.moddylp.AncientRegions.gui.Events.GUIEvents;
import de.moddylp.AncientRegions.gui.Events.GUIOpener;
import de.moddylp.AncientRegions.loader.*;
import de.moddylp.AncientRegions.particle.LogFile;
import de.moddylp.simplecommentconfig.Config;
import de.moddylp.simplecommentconfig.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Main
        extends JavaPlugin {
    public static WorldGuardPlugin worldguard;
    public static WorldEditPlugin worldedit;
    public static FileDriver DRIVER;
    private ConfigManager manager;
    private Config config;
    private static Main instance;

    static {
        DRIVER = FileDriver.getInstance();
    }

    public Language lang;
    protected GUIEvents loader;

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getManager() {
        return manager;
    }
    public Config getMainConfig() {
        return config;
    }

    public void onEnable() {
        instance = this;
        worldguard = this.getWorldGuard();
        worldedit = this.setupWorldEdit();
        this.manager = new ConfigManager();
        String[] header = {"###AncientRegions "+this.getDescription().getVersion()+"###", "Written by "+this.getDescription().getAuthors(), "This is the Configuration File"};
        this.config = manager.getConfig(getDataFolder()+"/"+"config.conf");
        this.config.setHeader(header);
        this.getLogger().info("Worldguard hooked");
        VaultLoader vaultloader = new VaultLoader(this);
        if (!vaultloader.load()) {
            this.onDisable();
            return;
        }
        ConfigLoader.saveDefaultconfig();
        this.lang = new Language();
        this.lang.setLangCode(Main.getInstance().getMainConfig().get("main.language", "en").toString());
        this.loadMessages();
        LogFile file = new LogFile();
        file.setup();
        this.loader = new GUIEvents(this, worldguard, worldedit);
        FlagLoader.load();
        if (Boolean.valueOf(Main.getInstance().getMainConfig().get("main.metrics", true).toString())) {
            try {
                de.moddylp.AncientRegions.bukkit.Metrics metrics = new de.moddylp.AncientRegions.bukkit.Metrics(this);
                Metrics metrics1 = new Metrics(this);
                metrics1.start();
            } catch (Exception ex) {
                this.getLogger().warning("Metrics cant be enabled because there was an error: " + ex.getLocalizedMessage());
            }
        }
        checkConfiguration();
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
                            Player p = ((Player) sender).getPlayer();
                            if (Main.getInstance().getMainConfig().containsKey("main.worlds")) {
                                List<?> worldconfig = (ArrayList)Main.getInstance().getMainConfig().get("main.worlds");
                                ArrayList<String> worlds = new ArrayList<>();
                                for (Object object : worldconfig) {
                                    worlds.add(object.toString());
                                }
                                if (worlds.contains(p.getWorld().getName())) {
                                    if (p.hasPermission("ancient.regions.flag.command")) {
                                        GUIOpener opener = new GUIOpener(this.loader);
                                        opener.openstartgui(p);
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
                            Main.getInstance().getMainConfig().reload();
                            this.lang.reload();
                            checkConfiguration();
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
                                this.getServer().getWorlds().stream().map(container::get)
                                        .map(regions -> Objects.requireNonNull(regions).getRegions())
                                        .forEach(rgids -> rgids.values().stream()
                                                .filter(rg -> file.getString(rg.getId()) != null)
                                                .forEach(rg -> file.setString(rg.getId(), null))
                                );
                                sender.sendMessage(ChatColor.GREEN + "[AR][ERROR] " + this.lang.getText("Canceled"));
                                return true;
                            }
                            sender.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("Permission"));
                        } catch (Exception ignored) {
                        }
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
                Player p = ((Player) sender).getPlayer();
                p.performCommand("ancr gui");
                return true;
            }
            this.getLogger().log(Level.INFO, "{0}[AR][ERROR] {1} ", new Object[]{this.lang.getText("Console")});
            return true;
        }
        return false;
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            System.out.println(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("Worldguard"));
        }
        return (WorldGuardPlugin) plugin;
    }

    public WorldEditPlugin setupWorldEdit() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
            return null;
        }
        return (WorldEditPlugin) plugin;
    }

    private void loadMessages() {
        Messages messages = new Messages(this);
    }

    private void checkConfiguration()  {
        if (((ArrayList)Main.getInstance().getMainConfig().get("region.standartdenyflags")).size() > 0) {
            List<?> standartdenyflags = (ArrayList)Main.getInstance().getMainConfig().get("region.standartdenyflags");
            ArrayList<String> flags = new ArrayList<>();
            for (Object object : standartdenyflags) {
                flags.add(object.toString());
            }
            for (String flag : flags) {
                boolean valid;
                valid = isValid(flag);
                if (!valid) {
                    this.getLogger().log(Level.WARNING, "[AR][ERROR] No valid Deny Flag: "+flag);
                }

            }
        }
        if (((ArrayList)Main.getInstance().getMainConfig().get("region.standartallowflags")).size() > 0) {
            List<?> standartallowflags = (ArrayList)Main.getInstance().getMainConfig().get("region.standartallowflags");
            ArrayList<String> flags = new ArrayList<>();
            for (Object object : standartallowflags) {
                flags.add(object.toString());
            }
            for (String flag : flags) {
                boolean valid;
                valid = isValid(flag);
                if (!valid) {
                    this.getLogger().log(Level.WARNING, "[AR][ERROR] No valid Allow Flag: "+flag);
                }

            }
        }
    }

    private boolean isValid(String flag) {
        boolean valid = false;
        FlagOBJ flagOBJ = FlagOBJ.getFlagObj(flag);
        if (Main.getInstance().getMainConfig().containsKey("flags."+flag) && !flagOBJ.getName().equals("NOT FOUND"))
        {
            if (flagOBJ.getFlag() instanceof StateFlag) {
                valid = true;
            }
            if (flagOBJ.getFlag() instanceof BooleanFlag) {
                valid = true;
            }
        }
        return valid;
    }
}

