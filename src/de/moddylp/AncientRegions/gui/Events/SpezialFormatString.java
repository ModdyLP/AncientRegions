package de.moddylp.AncientRegions.gui.Events;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.util.command.composition.FlagParser;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import de.moddylp.AncientRegions.flags.FlagUtil;
import de.moddylp.AncientRegions.flags.StringFlag;
import de.moddylp.AncientRegions.gui.EditflagsPage2;
import de.moddylp.AncientRegions.loader.LoadConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Editflags;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpezialFormatString implements Listener {
	private Player p;
	private Main plugin;
	private Flag flag;
	private String flagname;
	private WorldGuardPlugin worldguard;

	public SpezialFormatString(Player p, Flag flag, String Flagname, Main plugin, WorldGuardPlugin worldguard) {
		this.p = p;
		this.flag = flag;
		this.plugin = plugin;
		this.flagname = Flagname;
		this.worldguard = worldguard;
	}
	@EventHandler
	public String getChat(AsyncPlayerChatEvent e) {
		if (e.getPlayer().equals(p)) {
			String msg = e.getMessage();
			RegionContainer container = worldguard.getRegionContainer();
			RegionManager regions = container.get(p.getWorld());
			Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(),p.getLocation().getZ());
			LocalPlayer ply = worldguard.wrapPlayer(p);
			List<String> region = null;
			if (regions != null) {
				region = regions.getApplicableRegionsIDs(pt);

				if (region.isEmpty()) {
					p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("GobalError"));
				} else {
					ProtectedRegion rg = regions.getRegion(region.get(0));
					if ((rg != null && rg.isOwner(ply)) || (rg != null && p.hasPermission("ancient.regions.admin.bypass"))) {
						if (FlagUtil.payment(p, e, flagname)) {
							Set<String> set = new HashSet<>();
							set.add(msg);
							rg.setFlag((SetFlag<String>)flag, set);

							p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + plugin.lang.getText("ValueChat").replace("[PH]", flagname));
							Editflags gui = new Editflags(p, plugin, worldguard);
							gui.open();
							HandlerList.unregisterAll(this);
							e.setCancelled(true);
						}
					} else {
						p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Owner"));
						e.setCancelled(true);
					}
					e.setCancelled(true);
				}
			}
		}
		return null;
		}
	
}
