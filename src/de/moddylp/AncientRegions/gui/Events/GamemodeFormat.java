package de.moddylp.AncientRegions.gui.Events;

import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagOBJ;
import de.moddylp.AncientRegions.gui.EditflagsPage2;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GamemodeFormat
implements Listener {
    private ProtectedRegion rg;
    private Player p;
    private FlagOBJ flag;

    public GamemodeFormat(Player p, FlagOBJ flag2, ProtectedRegion rg) {
        this.p = p;
        this.flag = flag2;
        this.rg = rg;
    }

    @EventHandler
    public String getChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().equals(this.p)) {
            String msg = e.getMessage();
            if (msg.contains("creative") || msg.contains("c") || msg.contains("1")) {
                this.rg.setFlag((EnumFlag)this.flag.getFlag(), GameMode.CREATIVE);
                this.p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + Main.getInstance().lang.getText("ValueChat").replace("[PH]", this.flag.getName()));
                EditflagsPage2 gui = new EditflagsPage2(this.p, Main.getInstance());
                gui.open();
                e.setCancelled(true);
                HandlerList.unregisterAll(this);
            } else if (msg.contains("survival") || msg.contains("sv") || msg.contains("0")) {
                this.rg.setFlag((EnumFlag)this.flag.getFlag(), GameMode.SURVIVAL);
                this.p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + Main.getInstance().lang.getText("ValueChat").replace("[PH]", this.flag.getName()));
                EditflagsPage2 gui = new EditflagsPage2(this.p, Main.getInstance());
                gui.open();
                e.setCancelled(true);
                HandlerList.unregisterAll(this);
            } else if (msg.contains("spectator") || msg.contains("sp") || msg.contains("3")) {
                this.rg.setFlag((EnumFlag)this.flag.getFlag(), GameMode.SPECTATOR);
                this.p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + Main.getInstance().lang.getText("ValueChat").replace("[PH]", this.flag.getName()));
                EditflagsPage2 gui = new EditflagsPage2(this.p, Main.getInstance());
                gui.open();
                e.setCancelled(true);
                HandlerList.unregisterAll(this);
            } else if (msg.contains("adventure") || msg.contains("a") || msg.contains("2")) {
                this.rg.setFlag((EnumFlag)this.flag.getFlag(), GameMode.ADVENTURE);
                this.p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + Main.getInstance().lang.getText("ValueChat").replace("[PH]", this.flag.getName()));
                EditflagsPage2 gui = new EditflagsPage2(this.p, Main.getInstance());
                gui.open();
                e.setCancelled(true);
                HandlerList.unregisterAll(this);
            } else {
                this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("InvalidGamemode"));
                e.setCancelled(true);
            }
        }
        return null;
    }
}

