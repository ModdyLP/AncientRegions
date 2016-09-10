package de.moddylp.AncientRegions.region;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;

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
	    					if (rg.isOwner(ply) || p.hasPermission("ancient.regions.region.bypass")) {
	    						DefaultDomain member = new DefaultDomain();
	    						member.addPlayer(uuid);
	    						rg.setMembers(member);
	    						p.sendMessage(ChatColor.GREEN + "[AR][INFO] "+ plugin.lang.getText("PlayerAdded").replace("[PH]", msg));
	    						container.reload();
	    						e.setCancelled(true);
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
	    					if (rg.isOwner(ply) || p.hasPermission("ancient.regions.region.bypass")) {
	    						DefaultDomain owner = new DefaultDomain();
	    						owner = rg.getOwners();
	    						owner.removePlayer(p.getUniqueId());
	    						owner.addPlayer(uuid);
	    						rg.setOwners(owner);
	    						p.sendMessage(ChatColor.GREEN + "[AR][INFO] "+ plugin.lang.getText("ChangeOwner").replace("[PH]", msg));
	    						container.reload();
	    						e.setCancelled(true);
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
	
}
