package de.moddylp.AncientRegions.region;

import java.util.List;
import java.util.UUID;

import de.moddylp.AncientRegions.loader.LoadConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import org.bukkit.plugin.RegisteredServiceProvider;

public class SetValueFromChatEvent implements Listener {
	private Player p;
	private Main plugin;
	private WorldGuardPlugin worldguard;
	private String mode;
	private UUID uuid;
	private String msg;

	public SetValueFromChatEvent(Player p, String mode, Main plugin, WorldGuardPlugin worldguard) {
		this.p = p;
		this.mode = mode;
		this.plugin = plugin;
		this.worldguard = worldguard;
	}
	@EventHandler
	public String getChat(AsyncPlayerChatEvent e) {
		if (e.getPlayer().equals(p)) {
			msg = e.getMessage().toString();
			this.uuid = uuid(msg);
			if (uuid != null) {
	    		if (mode.equals(plugin.lang.getText("AddMember"))) {
	    			if (p.hasPermission("ancient.regions.region.addmember")) {
	    				RegionContainer container = worldguard.getRegionContainer();
	    				RegionManager regions = container.get(p.getWorld());
	    				Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
	    				LocalPlayer ply = worldguard.wrapPlayer(p);
	    				List<String> region = regions.getApplicableRegionsIDs(pt);
	    				if (region.isEmpty()) {
	    					p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("GobalError"));
	    				} else {
	    					ProtectedRegion rg = regions.getRegion(region.get(0));
	    					if (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass")) {
								if (payment(p, e, "addmember") || p.hasPermission("ancient.regions.admin.bypass")) {
									DefaultDomain member = new DefaultDomain();
									member.addPlayer(uuid);
									rg.setMembers(member);
									p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + plugin.lang.getText("PlayerAdded").replace("[PH]", msg));
									container.reload();
									e.setCancelled(true);
								} else {
									p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("NoMoney"));
									e.setCancelled(true);
								}
	    					} else {
	    						p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Owner"));
	    						e.setCancelled(true);
	    					}
	    					e.setCancelled(true);
	    				}
	    			} else {
	    				p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Permission"));
	    				e.setCancelled(true);
	    			}
	    		} else if (mode.equals(plugin.lang.getText("ChangeOwner"))) {
	    			if (p.hasPermission("ancient.regions.region.changeowner")) {
	    				RegionContainer container = worldguard.getRegionContainer();
	    				RegionManager regions = container.get(p.getWorld());
	    				Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
	    				LocalPlayer ply = worldguard.wrapPlayer(p);
	    				List<String> region = regions.getApplicableRegionsIDs(pt);
	    				if (region.isEmpty()) {
	    					p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("GobalError"));
	    				} else {
	    					ProtectedRegion rg = regions.getRegion(region.get(0));
	    					if (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass")) {
								if (payment(p, e, "changeowner") || p.hasPermission("ancient.regions.admin.bypass")) {
									DefaultDomain owner;
									owner = rg.getOwners();
									owner.removePlayer(p.getUniqueId());
									owner.addPlayer(uuid);
									rg.setOwners(owner);
									p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + plugin.lang.getText("ChangeOwner").replace("[PH]", msg));
									container.reload();
									e.setCancelled(true);
								} else {
									p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("NoMoney"));
									e.setCancelled(true);
								}
	    					} else {
	    						p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Owner"));
	    						e.setCancelled(true);
	    					}
	    					e.setCancelled(true);
	    				}
	    			} else {
	    				p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Permission"));
	    				e.setCancelled(true);
	    			}
	    		}
				RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
				gui.open();
				HandlerList.unregisterAll(this);
				e.setCancelled(true);
		} else {
			e.setCancelled(true);
			p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("Player").replace("[PH]", msg));
			RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
			gui.open();
			HandlerList.unregisterAll(this);
		}
		}
		return mode;
	}
	public UUID uuid(String playername) {
		OfflinePlayer[] allplayers = plugin.getServer().getOfflinePlayers();

		for (int i = 0; allplayers.length >= i;) {
			String uuidname = allplayers[i].getName();
			if (playername.trim().equals(uuidname)) {
				return allplayers[i].getUniqueId();
			} else {
				return null;
			}

		}
		return null;
	}
	public String loadPricefromConfig(String configname) {
		try {
			LoadConfig config = new LoadConfig(plugin);
			String price = config.getOption(configname);
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
	public boolean payment(Player p, AsyncPlayerChatEvent e, String configname) {
		RegisteredServiceProvider<Economy> service = Bukkit.getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy vaultEcon = service.getProvider();
		if (p.hasPermission("ancient.regions.admin.bypass")) {
			e.setCancelled(true);
			return true;
		}
		if (vaultEcon != null) {
			String price = loadPricefromConfig(configname);
			if (vaultEcon.getBalance(p.getName()) != 0 && vaultEcon.getBalance(p.getName()) >= Double.valueOf(price)) {
				vaultEcon.withdrawPlayer(p.getName(), Double.valueOf(price));
				p.sendMessage(ChatColor.BLUE + "[AR][INFO]" + plugin.lang.getText("PayNote3").replace("[PH]",
						loadPricefromConfig(configname) + " " + loadCurrencyfromConfig()));
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
