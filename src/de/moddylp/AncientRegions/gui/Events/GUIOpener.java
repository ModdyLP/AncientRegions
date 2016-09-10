package de.moddylp.AncientRegions.gui.Events;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.moddylp.AncientRegions.gui.Editflags;
import de.moddylp.AncientRegions.gui.Startgui;

public class GUIOpener {
	
	private GUIEvents loader;
	private Startgui gui1;
	private Editflags gui2;
	private WorldGuardPlugin worldguard;

	public GUIOpener(GUIEvents loader, WorldGuardPlugin worldguard) {
		this.loader = loader;
		this.worldguard = worldguard;
	}
	
	public void openstartgui(CommandSender sender) {
            gui1 = new Startgui(Bukkit.getPlayer(sender.getName()),loader.plugin);
            gui1.open();
	}
	public void openeditflagsgui(Player sender) {
        sender.closeInventory();
        gui2 = new Editflags(Bukkit.getPlayer(sender.getName()), loader.plugin, worldguard);
        gui2.open();
        
        }
}
