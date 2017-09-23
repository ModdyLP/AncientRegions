package de.moddylp.AncientRegions;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.moddylp.AncientRegions.gui.Events.GUIEvents;
import de.moddylp.AncientRegions.gui.Events.GUIOpener;
import de.moddylp.AncientRegions.loader.LoadConfig;
import de.moddylp.AncientRegions.loader.Messages;
import de.moddylp.AncientRegions.loader.VaultLoader;
import de.moddylp.AncientRegions.particle.LogFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	protected GUIEvents loader;
	private WorldGuardPlugin worldguard;
	public Language lang;
	private LoadConfig config;
	private WorldEditPlugin worldedit;

        @Override
	public void onEnable() {
		worldguard = getWorldGuard();
		worldedit = setupWorldEdit();
		this.getLogger().info("Worldguard hooked");
        VaultLoader vaultloader = new VaultLoader(this);
		if (!vaultloader.load()) {
            getServer().getPluginManager().disablePlugin(this);
        } else {
            this.getLogger().info("Vault hooked");
            config = new LoadConfig(this);
            config.setup();
            lang = new Language(this, new File(this.getDataFolder(), "messages.yml"));
            loadMessages();
            LogFile file = new LogFile(this);
            file.setup();
            loader = new GUIEvents(this, worldguard, worldedit);
        }

		
        
		this.getLogger().info(this.lang.getText("Enabled"));
	}

        @Override
	public void onDisable() {
		this.getLogger().info(this.lang.getText("Disabled"));
	}

        @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("ancientregions")) {
			if (args.length > 0) {
                            switch (args[0]) {
                                case "gui":
                                    if (sender instanceof Player) {
                                        Player p = ((Player) sender).getPlayer();
                                        if (!config.getOption("worlds").equals("[]") && config.getOption("worlds") != null) {
                                            String[] worldconfig = config.getOption("worlds").split(",");
                                            List<String> worlds = new ArrayList<>();
                                            worlds.addAll(Arrays.asList(worldconfig));
                                            if (worlds.contains(p.getWorld().getName())) {
                                                if (p.hasPermission("ancient.regions.flag.command")) {
                                                    
                                                    GUIOpener opener = new GUIOpener(loader, worldguard);
                                                    opener.openstartgui(p);
                                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_SNARE, 100, 100);
                                                    return true;
                                                    
                                                } else {
                                                    sender.sendMessage(ChatColor.RED+ "[AR][ERROR] "+this.lang.getText("Permission"));
                                                    return true;
                                                }
                                            } else {
                                                p.sendMessage(ChatColor.RED + "[AR][ERROR] "+ this.lang.getText("World"));
                                                return true;
                                            }
                                        } else {
                                            p.sendMessage(ChatColor.RED + "[AR][ERROR] "+ this.lang.getText("World"));
                                            return true;
                                        }
                                    } else {
                                        this.getLogger().log(Level.INFO, "{0}[AR][ERROR] {1}", new Object[]{ChatColor.RED, this.lang.getText("Console")});
                                        return true;
                                    }
                                case "reload":
                                    if (sender.hasPermission("ancient.regions.admin.reload")) {
                                        LoadConfig loadconfig;
                                        loadconfig = new LoadConfig(this);
                                        loadconfig.reload();
                                        lang.reload();
                                        sender.sendMessage(ChatColor.GOLD + "[AR][INFO] "+ this.lang.getText("ConfigReload"));
                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.RED+ "[AR][ERROR] "+this.lang.getText("Permission"));
                                    }
                                    break;
                                case "cancelall":
                                    try {
                                        if (sender.hasPermission("ancient.regions.admin.cancelall")) {
                                            LogFile file = new LogFile(this);
                                            RegionContainer container = worldguard.getRegionContainer();
                                            this.getServer().getWorlds().stream().map((world) -> container.get(world)).map((regions) -> regions.getRegions()).forEach((rgids) -> {
                                                rgids.values().stream().filter((rg) -> (file.getString(rg.getId()) != null)).forEach((rg) -> {
                                                    file.setString(rg.getId(), null);
                                                });
                                            });
                                            sender.sendMessage(ChatColor.GREEN + "[AR][ERROR] "+ this.lang.getText("Canceled"));
                                            return true;
                                        } else {
                                            sender.sendMessage(ChatColor.RED+ "[AR][ERROR] "+this.lang.getText("Permission"));
                                        }
                                    } catch (Exception ex) {
                                    }
                                    break;
                                case "info":
                                case "help":
                                    // Author Info and Plugin Info
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
                                default:
                                    sender.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("CommandNotFound"));
                                    break;
                            }
				return true;
			} else {
				if (sender instanceof Player) {
					Player p = ((Player) sender).getPlayer();
					p.performCommand("ancr gui");
					return true;
				} else {
					this.getLogger().log(Level.INFO, "{0}[AR][ERROR] {1}", new Object[]{ChatColor.RED, this.lang.getText("Console")});
					return true;
				}
			}
		}
		return false;
	}

	private WorldGuardPlugin getWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded*
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			System.out.println(ChatColor.RED + "[AR][ERROR] " + this.lang.getText("Worldguard"));
		}

		return (WorldGuardPlugin) plugin;
	}
	public WorldEditPlugin setupWorldEdit() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin == null || !(plugin instanceof WorldEditPlugin))
            return null;
        return (WorldEditPlugin) plugin;
    }

	// Setup messages
	private void loadMessages() {
		Messages messages = new Messages(this);
	}
}
