package de.moddylp.AncientRegions.gui.Events;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import de.moddylp.AncientRegions.loader.LoadConfig;
import de.moddylp.AncientRegions.region.RegionManageGUI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Editflags;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.List;

public class WeatherFormat implements Listener {
	private Player p;
	private Main plugin;
	private EnumFlag<WeatherType> flag;
	private ProtectedRegion rg;
	private WorldGuardPlugin worldguard;
	private String flagname;

	public WeatherFormat(Player p, ProtectedRegion rg, EnumFlag<WeatherType> flag2, String flagname, Main plugin, WorldGuardPlugin worldguard) {
		this.p = p;
		this.flag = flag2;
		this.rg = rg;
		this.plugin = plugin;
		this.flagname = flagname;
		this.worldguard = worldguard;
	}
	@EventHandler
	public String getChat(AsyncPlayerChatEvent e) {
		if (e.getPlayer().equals(p)) {
			String msg = e.getMessage().toString();
			RegionContainer container = worldguard.getRegionContainer();
			RegionManager regions = container.get(p.getWorld());
			Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(),p.getLocation().getZ());
			LocalPlayer ply = worldguard.wrapPlayer(p);
			List<String> region = regions.getApplicableRegionsIDs(pt);
			if (region.isEmpty()) {
				p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("GobalError"));
			} else {
				ProtectedRegion rg = regions.getRegion(region.get(0));
				if (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass")) {
					if (msg.contains("sun") || msg.contains("clear")) {
						try {
                            if (payment(p, e)) {
                                rg.setFlag(flag, flag.parseInput(worldguard, p, "clear"));
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + plugin.lang.getText("ValueChat").replace("[PH]", flagname));
                                Editflags gui = new Editflags(p, plugin, worldguard);
                                gui.open();
                                e.setCancelled(true);
                                HandlerList.unregisterAll(this);
                            }
						} catch (InvalidFlagFormat e1) {
							p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("InvalidWeather"));
							e.setCancelled(true);
						}
					} else if (msg.contains("storm") || msg.contains("thunder") || msg.contains("rain") || msg.contains("downfall")) {
						try {
                            if (payment(p, e)) {
                                rg.setFlag(flag, flag.parseInput(worldguard, p, "downfall"));
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + plugin.lang.getText("ValueChat").replace("[PH]", flagname));
                                Editflags gui = new Editflags(p, plugin, worldguard);
                                gui.open();
                                e.setCancelled(true);
                                HandlerList.unregisterAll(this);
                            }
						} catch (InvalidFlagFormat e1) {
							p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("InvalidWeather"));
							e.setCancelled(true);
						}

					} else {
						p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("InvalidWeather"));
						Editflags gui = new Editflags(p, plugin, worldguard);
						gui.open();
						e.setCancelled(true);
						HandlerList.unregisterAll(this);
					}
				} else {
					p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("Owner"));
					e.setCancelled(true);
				}
				e.setCancelled(true);
			}
		}
		return null;
		}
	public String loadPricefromConfig() {
		try {
			LoadConfig config = new LoadConfig(plugin);
			String price = config.getOption(flagname.toLowerCase());
			return price;
		} catch (Exception ex) {
			plugin.getLogger().info(ex.toString());
		}
		return null;
	}

	public String loadCurrencyfromConfig() {
		try {
			LoadConfig config = new LoadConfig(plugin);
			String currency = config.getOption("currency");
			return currency;
		} catch (Exception ex) {
			plugin.getLogger().info(ex.toString());
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public boolean payment(Player p, AsyncPlayerChatEvent e) {
		RegisteredServiceProvider<Economy> service = Bukkit.getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy vaultEcon = service.getProvider();
		if (p.hasPermission("ancient.regions.admin.bypass")) {
			e.setCancelled(true);
			return true;
		}
		if (vaultEcon != null) {
			String price = loadPricefromConfig();
			if (vaultEcon.getBalance(p.getName()) != 0 && vaultEcon.getBalance(p.getName()) >= Double.valueOf(price)) {
				vaultEcon.withdrawPlayer(p.getName(), Double.valueOf(price));
				p.sendMessage(ChatColor.BLUE + "[AR][INFO]" + plugin.lang.getText("PayNote").replace("[PH]",
						loadPricefromConfig() + " " + loadCurrencyfromConfig()));
				e.setCancelled(true);
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("NoMoney"));
				e.setCancelled(true);
				return false;
			}
		} else {
			p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("VaultError"));
			e.setCancelled(true);
		}
		return false;
	}
	
}
