package de.moddylp.AncientRegions.region;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagUtil;
import de.moddylp.AncientRegions.gui.Events.ActivateMode;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SetValueFromChatEvent
        implements Listener {
    private Player p;
    private Main plugin;
    private WorldGuardPlugin worldguard;
    private String mode;

    SetValueFromChatEvent(Player p, String mode, Main plugin, WorldGuardPlugin worldguard) {
        this.p = p;
        this.mode = mode;
        this.plugin = plugin;
        this.worldguard = worldguard;
    }

    @EventHandler
    public String getChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().equals(this.p)) {
            String msg = e.getMessage();
            if (msg != null && checkIfPlayerExists(msg)) {
                RegionContainer container;
                if (this.mode.equalsIgnoreCase(this.plugin.lang.getText("AddMember"))) {
                    if (this.p.hasPermission("ancient.regions.region.addmember")) {
                        container = this.worldguard.getRegionContainer();
                        RegionManager regions = container.get(this.p.getWorld());
                        Vector pt = new Vector(this.p.getLocation().getX(), this.p.getLocation().getY(), this.p.getLocation().getZ());
                        LocalPlayer ply = this.worldguard.wrapPlayer(this.p);
                        List region;
                        if (regions != null) {
                            region = regions.getApplicableRegionsIDs(pt);
                            if (region.isEmpty()) {
                                this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("GobalError"));
                            } else {
                                ProtectedRegion rg = regions.getRegion((String) region.get(0));
                                if (rg != null && (rg.isOwner(ply) || this.p.hasPermission("ancient.regions.admin.bypass"))) {
                                    if (FlagUtil.payment(this.p, e, "_addmember", ActivateMode.ACTIVATE) || this.p.hasPermission("ancient.regions.admin.bypass")) {
                                        DefaultDomain member = rg.getMembers();
                                        member.addPlayer(msg);
                                        rg.setMembers(member);
                                        this.p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.plugin.lang.getText("PlayerAdded").replace("[PH]", msg));
                                        container.reload();
                                        e.setCancelled(true);
                                    } else {
                                        this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("NoMoney"));
                                        e.setCancelled(true);
                                    }
                                } else {
                                    this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Owner"));
                                    e.setCancelled(true);
                                }
                                e.setCancelled(true);
                            }
                        }
                    } else {
                        this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Permission"));
                        e.setCancelled(true);
                    }
                } else if (this.mode.equalsIgnoreCase(this.plugin.lang.getText("ChangeOwner"))) {
                    if (this.p.hasPermission("ancient.regions.region.changeowner")) {
                        container = this.worldguard.getRegionContainer();
                        RegionManager regions = container.get(this.p.getWorld());
                        Vector pt = new Vector(this.p.getLocation().getX(), this.p.getLocation().getY(), this.p.getLocation().getZ());
                        LocalPlayer ply = this.worldguard.wrapPlayer(this.p);
                        List<String> region = Objects.requireNonNull(regions).getApplicableRegionsIDs(pt);
                        if (region.isEmpty()) {
                            this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("GobalError"));
                        } else {
                            ProtectedRegion rg = regions.getRegion(region.get(0));
                            if (Objects.requireNonNull(rg).isOwner(ply) || this.p.hasPermission("ancient.regions.admin.bypass")) {
                                if (FlagUtil.payment(this.p, e, "_changeowner", ActivateMode.ACTIVATE) || this.p.hasPermission("ancient.regions.admin.bypass")) {
                                    DefaultDomain owner = rg.getOwners();
                                    owner.removePlayer(this.p.getUniqueId());
                                    owner.addPlayer(msg);
                                    rg.setOwners(owner);
                                    this.p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.plugin.lang.getText("ChangeOwner").replace("[PH]", msg));
                                    container.reload();
                                    e.setCancelled(true);
                                } else {
                                    this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("NoMoney"));
                                    e.setCancelled(true);
                                }
                            } else {
                                this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Owner"));
                                e.setCancelled(true);
                            }
                            e.setCancelled(true);
                        }
                    } else {
                        this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Permission"));
                        e.setCancelled(true);
                    }
                }
                RegionManageGUI gui = new RegionManageGUI(this.p, this.plugin, this.worldguard);
                gui.open();
                HandlerList.unregisterAll(this);
                e.setCancelled(true);
            } else {
                e.setCancelled(true);
                this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Player").replace("[PH]", msg != null ? msg : "NO PLAYER"));
                RegionManageGUI gui = new RegionManageGUI(this.p, this.plugin, this.worldguard);
                gui.open();
                HandlerList.unregisterAll(this);
            }
        }
        return this.mode;
    }

    private boolean checkIfPlayerExists(String playername) {
        playername = playername.trim();
        OfflinePlayer[] allplayers = this.plugin.getServer().getOfflinePlayers();
        if (allplayers.length > 0) {
            if (allplayers[0].getName().equalsIgnoreCase(playername) || allplayers[0].getPlayer().getDisplayName().equalsIgnoreCase(playername)) {
                return true;
            }
        }
        for (Player player: this.plugin.getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(playername) || player.getDisplayName().equalsIgnoreCase(playername)) {
                return true;
            }
        }
        return false;
    }
}

