package de.moddylp.AncientRegions.gui.Events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.EditflagsPage2;

public class GamemodeFormat implements Listener {
	private Player p;
	private Main plugin;
	private EnumFlag<GameMode> flag;
	private ProtectedRegion rg;
	private WorldGuardPlugin worldguard;
	private String flagname;

	public GamemodeFormat(Player p, ProtectedRegion rg, EnumFlag<GameMode> flag2, String flagname, Main plugin, WorldGuardPlugin worldguard) {
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
			if (msg.contains("creative") || msg.contains("c") || msg.contains("1")) {
				try {
					rg.setFlag(flag, flag.parseInput(worldguard, p, "creative"));
					p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
					EditflagsPage2 gui = new EditflagsPage2(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
					HandlerList.unregisterAll(this);
				} catch (InvalidFlagFormat e1) {
					p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidGamemode"));
					e.setCancelled(true);
				}
			} else if(msg.contains("survival") || msg.contains("sv") || msg.contains("0")) {
				try {
					rg.setFlag(flag, flag.parseInput(worldguard, p, "survival"));
					p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
					EditflagsPage2 gui = new EditflagsPage2(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
					HandlerList.unregisterAll(this);
				} catch (InvalidFlagFormat e1) {
					p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidGamemode"));
					e.setCancelled(true);
				}
			} else if(msg.contains("spectator") || msg.contains("sp") || msg.contains("3")) {
				try {
					rg.setFlag(flag, flag.parseInput(worldguard, p, "spectator"));
					p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
					EditflagsPage2 gui = new EditflagsPage2(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
					HandlerList.unregisterAll(this);
				} catch (InvalidFlagFormat e1) {
					p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidGamemode"));
					e.setCancelled(true);
				}
			} else if(msg.contains("adventure") || msg.contains("a") || msg.contains("2")) {
				try {
					rg.setFlag(flag, flag.parseInput(worldguard, p, "adventure"));
					p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
					EditflagsPage2 gui = new EditflagsPage2(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
					HandlerList.unregisterAll(this);
				} catch (InvalidFlagFormat e1) {
					p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidGamemode"));
					e.setCancelled(true);
				}
			} else {
				p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidGamemode"));
				e.setCancelled(true);
			}
		}
		return null;
		}
	
}
