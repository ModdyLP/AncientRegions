package de.moddylp.AncientRegions.gui.Events;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.flags.DoubleFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagOBJ;
import de.moddylp.AncientRegions.flags.FlagUtil;
import de.moddylp.AncientRegions.gui.EditflagsPage2;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class SpezialFormatDouble implements Listener {
    private final FlagOBJ flagObj;
    private Player p;

    public SpezialFormatDouble(Player p, FlagOBJ flagOBJ) {
        this.p = p;
        this.flagObj = flagOBJ;
    }

    @EventHandler
    public String getChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().equals(p)) {
            String msg = e.getMessage();
            RegionContainer container = Main.worldguard.getRegionContainer();
            RegionManager regions = container.get(p.getWorld());
            Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
            LocalPlayer ply = Main.worldguard.wrapPlayer(p);
            List<String> region;
            if (regions != null) {
                region = regions.getApplicableRegionsIDs(pt);

                if (region.isEmpty()) {
                    p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion(region.get(0));
                    if (rg != null && rg.isOwner(ply) || rg != null && p.hasPermission("ancient.regions.admin.bypass")) {
                        if (FlagUtil.payment(p, e, flagObj.getName())) {
                            rg.setFlag((DoubleFlag) flagObj.getFlag(), Double.valueOf(msg));
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + Main.getInstance().lang.getText("ValueChat").replace("[PH]", flagObj.getName()));
                            EditflagsPage2 gui = new EditflagsPage2(p, Main.getInstance());
                            gui.open();
                            HandlerList.unregisterAll(this);
                            e.setCancelled(true);
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("Owner"));
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                }
            }
        }
        return null;
    }

}
