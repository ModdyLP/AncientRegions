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
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.Objects;

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
        this.p.sendMessage(ChatColor.GOLD + "[AR][INFO] " + Main.getInstance().lang.getText("exitinfo"));
    }

    public static boolean cancelEvent(String msg, Player p, AsyncPlayerChatEvent e, Listener listener) {
        if (msg.contains("exit") || msg.contains("cancel")) {
            p.sendMessage(ChatColor.RED + Main.getInstance().lang.getText("inputcancel"));
            RegionManageGUI gui = new RegionManageGUI(p, Main.getInstance(), Main.worldguard);
            gui.open();
            e.setCancelled(true);
            HandlerList.unregisterAll(listener);
            return true;
        }
        return false;
    }

    @EventHandler
    public String getChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().equals(this.p)) {
            String msg = e.getMessage();
            if (cancelEvent(msg, this.p, e, this)) {
                e.setCancelled(true);
                return null;
            } else {
                Player player = checkIfPlayerExists(msg);
                if (player != null) {
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
                                        if (FlagUtil.payment(this.p, e, "manage.addmember", null)) {
                                            DefaultDomain member = rg.getMembers();
                                            member.addPlayer(this.worldguard.wrapPlayer(player));
                                            rg.setMembers(member);
                                            this.p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.plugin.lang.getText("PlayerAdded").replace("[PH]", msg));
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
                                if (rg != null && (rg.isOwner(ply) || this.p.hasPermission("ancient.regions.admin.bypass"))) {
                                    if (FlagUtil.payment(this.p, e, "manage.changeowner", null)) {
                                        DefaultDomain owner = rg.getOwners();
                                        owner.removePlayer(ply);
                                        owner.addPlayer(this.worldguard.wrapPlayer(player));
                                        rg.setOwners(owner);
                                        this.p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.plugin.lang.getText("ChangeOwner").replace("[PH]", msg));
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
                    this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Player").replace("[PH]", msg));
                }
            }
        }
        return this.mode;
    }

    private Player checkIfPlayerExists(String playername) {
        playername = playername.trim();
        OfflinePlayer[] allplayers = this.plugin.getServer().getOfflinePlayers();
        for (OfflinePlayer offlinePlayer : allplayers) {
            if (offlinePlayer.getName().equalsIgnoreCase(playername) || (offlinePlayer.getPlayer() != null && offlinePlayer.getPlayer().getDisplayName().equalsIgnoreCase(playername))) {
                return offlinePlayer.getPlayer();
            }
        }
        for (Player player : this.plugin.getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(playername) || player.getDisplayName().equalsIgnoreCase(playername)) {
                return player;
            }
        }
        return null;
    }
}

