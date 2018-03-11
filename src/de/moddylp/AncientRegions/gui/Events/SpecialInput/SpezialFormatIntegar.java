package de.moddylp.AncientRegions.gui.Events.SpecialInput;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagOBJ;
import de.moddylp.AncientRegions.flags.FlagUtil;
import de.moddylp.AncientRegions.gui.Editflags;
import de.moddylp.AncientRegions.gui.Events.ActivateMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

public class SpezialFormatIntegar
        implements Listener {
    private final ActivateMode mode;
    private FlagOBJ flagobj;
    private Player p;

    public SpezialFormatIntegar(Player p, FlagOBJ flagOBJ, ActivateMode mode) {
        this.p = p;
        this.flagobj = flagOBJ;
        this.mode = mode;
    }

    @EventHandler
    public String getChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().equals(this.p)) {
            String msg = e.getMessage();
            RegionContainer container = Main.worldguard.getRegionContainer();
            RegionManager regions = container.get(this.p.getWorld());
            Vector pt = new Vector(this.p.getLocation().getX(), this.p.getLocation().getY(), this.p.getLocation().getZ());
            LocalPlayer ply = Main.worldguard.wrapPlayer(this.p);
            List region;
            if (regions != null) {
                region = regions.getApplicableRegionsIDs(pt);
                if (region.isEmpty()) {
                    this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion((String) region.get(0));
                    if (rg != null && (rg.isOwner(ply) || this.p.hasPermission("ancient.regions.admin.bypass"))) {
                        if (FlagUtil.payment(this.p, e, this.flagobj.getName(), mode)) {
                            rg.setFlag((IntegerFlag) this.flagobj.getFlag(), Integer.valueOf(msg));
                            this.p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + Main.getInstance().lang.getText("ValueChat").replace("[PH]", this.flagobj.getName()));
                            Editflags gui = new Editflags(this.p, Main.getInstance());
                            gui.open();
                            HandlerList.unregisterAll(this);
                            e.setCancelled(true);
                        }
                    } else {
                        this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("Owner"));
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                }
            }
        }
        return null;
    }
}

