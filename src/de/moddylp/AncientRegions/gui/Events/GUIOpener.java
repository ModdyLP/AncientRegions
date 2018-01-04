package de.moddylp.AncientRegions.gui.Events;

import de.moddylp.AncientRegions.gui.Editflags;
import de.moddylp.AncientRegions.gui.Events.GUIEvents;
import de.moddylp.AncientRegions.gui.Startgui;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GUIOpener {
    private GUIEvents loader;

    public GUIOpener(GUIEvents loader) {
        this.loader = loader;
    }

    public void openstartgui(CommandSender sender) {
        Startgui gui1 = new Startgui(Bukkit.getPlayer(sender.getName()), this.loader.plugin);
        gui1.open();
    }

    public void openeditflagsgui(Player sender) {
        sender.closeInventory();
        Editflags gui2 = new Editflags(Bukkit.getPlayer(sender.getName()), this.loader.plugin);
        gui2.open();
    }
}

