package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Events.ActivateMode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class BooleanFlag {
    private FlagOBJ flagOBJ;

    private BooleanFlag(FlagOBJ flagOBJ) {
        this.flagOBJ = flagOBJ;
    }

    public static void createandload(FlagOBJ flagOBJ, Player p, Inventory menu) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        BooleanFlag flag;
        if (FlagUtil.booleanFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.booleanFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new BooleanFlag(flagOBJ);
            FlagUtil.booleanFlagHashMap.put(flagOBJ.getName(), flag);
        }
        FlagUtil.loadBooleanGUI(menu, p, flagOBJ);
    }

    public static void createandtoggle(FlagOBJ flagOBJ, Player p, Inventory menu, InventoryClickEvent event, ActivateMode mode) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        BooleanFlag flag;
        event.setCancelled(true);
        if (FlagUtil.booleanFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.booleanFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new BooleanFlag(flagOBJ);
            FlagUtil.booleanFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.toggle(event, menu, p, mode);
    }

    public boolean toggle(InventoryClickEvent e, Inventory menu, Player p, ActivateMode mode) {
        if (p.hasPermission(this.flagOBJ.getPermission())) {
            RegionContainer container = Main.worldguard.getRegionContainer();
            RegionManager regions = container.get(p.getWorld());
            Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
            LocalPlayer ply = Main.worldguard.wrapPlayer(p);
            if (regions != null) {
                List region = regions.getApplicableRegionsIDs(pt);
                if (region.isEmpty()) {
                    p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion((String) region.get(0));
                    if (rg != null && (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass"))) {
                        if (!FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("null") && mode.equals(ActivateMode.REMOVE)) {
                            if (FlagUtil.payment(p, e, this.flagOBJ.getName(), mode)) {
                                rg.setFlag(this.flagOBJ.getFlag(), null);
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + ChatColor.GOLD + " " + this.flagOBJ.getName() + " "+Main.getInstance().lang.getText("FlagRemoved"));
                            }
                        } else if (!FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("false") && mode.equals(ActivateMode.DEACTIVATE)) {
                            if (FlagUtil.payment(p, e, this.flagOBJ.getName(), mode)) {
                                rg.setFlag((com.sk89q.worldguard.protection.flags.BooleanFlag) flagOBJ.getFlag(), false);
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + ChatColor.RED + " " + this.flagOBJ.getName() + Main.getInstance().lang.getText("fDisabled"));
                            }
                        } else if (!FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("true") && mode.equals(ActivateMode.ACTIVATE)) {
                            if (FlagUtil.payment(p, e, this.flagOBJ.getName(), mode)) {
                                rg.setFlag((com.sk89q.worldguard.protection.flags.BooleanFlag) flagOBJ.getFlag(), true);
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.flagOBJ.getName() + Main.getInstance().lang.getText("fEnabled"));
                            }
                        }

                    } else {
                        p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("Owner"));
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                }
            } else {
                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("Permission"));
                e.setCancelled(true);
            }
        }
        FlagUtil.loadBooleanGUI(menu, p, flagOBJ);
        return false;
    }
}

