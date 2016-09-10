package de.moddylp.AncientRegions.gui.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Editflags;

public class TimeFormat implements Listener {
	private Player p;
	private Main plugin;
	private StringFlag flag;
	private ProtectedRegion rg;
	private WorldGuardPlugin worldguard;
	private String flagname;

	public TimeFormat(Player p, ProtectedRegion rg, StringFlag flag, String flagname, Main plugin, WorldGuardPlugin worldguard) {
		this.p = p;
		this.flag = flag;
		this.rg = rg;
		this.plugin = plugin;
		this.flagname = flagname;
		this.worldguard = worldguard;
	}
	@EventHandler
	public String getChat(AsyncPlayerChatEvent e) {
		if (e.getPlayer().equals(p)) {
			String msg = e.getMessage().toString();
			msg = Integer.valueOf(msg).toString();
			if (msg != null) {
			    	rg.setFlag(flag, msg);
			    	p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
					Editflags gui = new Editflags(p, plugin, worldguard);
					gui.open();
					HandlerList.unregisterAll(this);
					e.setCancelled(true);
    		} else {
    			p.sendMessage(ChatColor.RED+"[AR][ERROR]"+plugin.lang.getText("Message3"));
    			e.setCancelled(true);
    		}
		}
		return null;
		}
	
}
