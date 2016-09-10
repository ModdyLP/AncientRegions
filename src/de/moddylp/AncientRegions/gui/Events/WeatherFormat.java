package de.moddylp.AncientRegions.gui.Events;

import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
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
import de.moddylp.AncientRegions.gui.Editflags;

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
			if (msg.contains("sun") || msg.contains("clear")) {
				try {
					rg.setFlag(flag, flag.parseInput(worldguard, p, "clear"));
					p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
					Editflags gui = new Editflags(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
					HandlerList.unregisterAll(this);
				} catch (InvalidFlagFormat e1) {
					p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidWeather"));
					e.setCancelled(true);
				}
			} else if(msg.contains("storm") || msg.contains("thunder") || msg.contains("rain") || msg.contains("downfall")) {
				try {
					rg.setFlag(flag, flag.parseInput(worldguard, p, "downfall"));
					p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
					Editflags gui = new Editflags(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
					HandlerList.unregisterAll(this);
				} catch (InvalidFlagFormat e1) {
					p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidWeather"));
					e.setCancelled(true);
				}
			} else {
				p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidWeather"));
				e.setCancelled(true);
			}
		}
		return null;
		}
	
}
