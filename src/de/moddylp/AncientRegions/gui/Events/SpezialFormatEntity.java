package de.moddylp.AncientRegions.gui.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Editflags;

public class SpezialFormatEntity implements Listener {
	private Player p;
	private Main plugin;
	private SetFlag<EntityType> flag;
	private ProtectedRegion rg;
	private String flagname;
	private WorldGuardPlugin worldguard;

	public SpezialFormatEntity(Player p, ProtectedRegion rg, SetFlag<EntityType> flag, String Flagname, Main plugin, WorldGuardPlugin worldguard) {
		this.p = p;
		this.flag = flag;
		this.rg = rg;
		this.plugin = plugin;
		this.flagname = Flagname;
		this.worldguard = worldguard;
	}
	@EventHandler
	public String getChat(AsyncPlayerChatEvent e) {
		if (e.getPlayer().equals(p)) {
			String msg = e.getMessage().toString();
	    		try {
					rg.setFlag(flag, flag.parseInput(worldguard, p, msg));
					p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
					Editflags gui = new Editflags(p, plugin, worldguard);
					gui.open();
					HandlerList.unregisterAll(this);
					e.setCancelled(true);
				} catch (InvalidFlagFormat e1) {
					p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidEnitity"));
					e.setCancelled(true);
				}
    		}
		return null;
		}
	
}
