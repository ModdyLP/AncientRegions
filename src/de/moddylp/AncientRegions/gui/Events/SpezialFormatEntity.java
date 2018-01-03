package de.moddylp.AncientRegions.gui.Events;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Language;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagOBJ;
import de.moddylp.AncientRegions.flags.FlagUtil;
import de.moddylp.AncientRegions.gui.Editflags;
import java.util.HashSet;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SpezialFormatEntity
implements Listener {
    private FlagOBJ flagobj;
    private Player p;

    public SpezialFormatEntity(Player p, FlagOBJ flagOBJ) {
        this.p = p;
        this.flagobj = flagOBJ;
    }

    @EventHandler
    public String getChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().equals(this.p)) {
            String msg = e.getMessage();
            RegionContainer container = Main.worldguard.getRegionContainer();
            RegionManager regions = container.get(this.p.getWorld());
            Vector pt = new Vector(this.p.getLocation().getX(), this.p.getLocation().getY(), this.p.getLocation().getZ());
            LocalPlayer ply = Main.worldguard.wrapPlayer(this.p);
            List region = null;
            if (regions != null) {
                region = regions.getApplicableRegionsIDs(pt);
                if (region.isEmpty()) {
                    this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion((String)region.get(0));
                    if (rg != null && rg.isOwner(ply) || rg != null && this.p.hasPermission("ancient.regions.admin.bypass")) {
                        if (FlagUtil.payment(this.p, (Cancellable)e, this.flagobj.getName())) {
                            HashSet<EntityType> set = new HashSet<EntityType>();
                            set.add(EntityType.valueOf((String)msg));
                            rg.setFlag((SetFlag)this.flagobj.getFlag(), set);
                            this.p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + Main.getInstance().lang.getText("ValueChat").replace("[PH]", this.flagobj.getName()));
                            Editflags gui = new Editflags(this.p, Main.getInstance());
                            gui.open();
                            HandlerList.unregisterAll((Listener)this);
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

