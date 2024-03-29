package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Events.ActivateMode;
import de.moddylp.AncientRegions.utils.Console;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocationFlagimpl {
    private FlagOBJ flagobj;

    private LocationFlagimpl(FlagOBJ flagobj) {
        this.flagobj = flagobj;
    }

    public static void createandload(FlagOBJ flagOBJ, Player p, Inventory menu) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        LocationFlagimpl flag;
        if (FlagUtil.locationFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.locationFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new LocationFlagimpl(flagOBJ);
            FlagUtil.locationFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.loadgui(menu, p);
    }

    public static void createandtoggle(FlagOBJ flagOBJ, Player p, Inventory menu, InventoryClickEvent event, ActivateMode mode) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        LocationFlagimpl flag;
        event.setCancelled(true);
        if (FlagUtil.locationFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.locationFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new LocationFlagimpl(flagOBJ);
            FlagUtil.locationFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.toggle(event, menu, p, mode);
    }

    public boolean toggle(InventoryClickEvent e, Inventory menu, Player p, ActivateMode mode) {
        if (p.hasPermission(this.flagobj.getPermission())) {
            RegionContainer container = Main.worldguard.getRegionContainer();
            RegionManager regions = container.get(p.getWorld());
            Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
            LocalPlayer ply = Main.worldguard.wrapPlayer(p);
            List region;
            if (regions != null) {
                region = regions.getApplicableRegionsIDs(pt);
                if (region.isEmpty()) {
                    p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion((String) region.get(0));
                    if (rg != null && (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass"))) {
                        if (!FlagUtil.isSet(p, flagobj.getFlag()).equalsIgnoreCase("null") && mode.equals(ActivateMode.REMOVE)) {
                            if (FlagUtil.payment(p, e, this.flagobj.getConfigname(), mode)) {
                                rg.setFlag(this.flagobj.getFlag(), null);
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + ChatColor.GOLD + " " + this.flagobj.getName() + " "+Main.getInstance().lang.getText("FlagRemoved"));
                            }
                        } else if (mode.equals(ActivateMode.ACTIVATE) && FlagUtil.payment(p, e, flagobj.getName(), mode)) {
                            rg.setFlag((LocationFlag) flagobj.getFlag(), BukkitUtil.toLocation(p.getLocation()));
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.flagobj.getName() + Main.getInstance().lang.getText("fEnabled"));
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("Owner"));
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                }
            }
        } else {
            p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("Permission"));
            e.setCancelled(true);
        }
        this.loadgui(menu, p);
        return false;
    }

    private void loadgui(Inventory menu, Player p) {
        if (p.hasPermission(this.flagobj.getPermission())) {
            ItemStack ITEM = new ItemStack(this.flagobj.getItem());
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Set").replace("[PH]", this.flagobj.getName()));
            lore.add(ChatColor.GREEN +
                    Objects.requireNonNull(FlagUtil.loadPricefromConfig(this.flagobj.getName(), ActivateMode.ACTIVATE)).toString() + " " + FlagUtil.loadCurrencyfromConfig() + "    " +
                    ChatColor.LIGHT_PURPLE +
                    Objects.requireNonNull(FlagUtil.loadPricefromConfig(this.flagobj.getName(), ActivateMode.REMOVE)).toString() + " " + FlagUtil.loadCurrencyfromConfig()
            );
            if (!FlagUtil.isSet(p, this.flagobj.getFlag()).equalsIgnoreCase("null")) {
                lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Current") + ":");
                String[] location = FlagUtil.isSet(p, this.flagobj.getFlag()).split(",");
                location[1] = location[1].replace(" :", "X: ");
                location[2] = "Y: " + location[2];
                location[3] = "Z: " + location[3];
                for (String loc : location) {
                    lore.add(ChatColor.AQUA + loc);
                }
            }
            ItemMeta imeta = ITEM.getItemMeta();
            if (!FlagUtil.isSet(p, this.flagobj.getFlag()).equalsIgnoreCase("null")) {
                imeta.setDisplayName(ChatColor.GREEN + "[ON] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + this.flagobj.getName());
                lore.add(ChatColor.LIGHT_PURPLE + Main.getInstance().lang.getText("removemode"));
            } else {
                imeta.setDisplayName(ChatColor.BLUE + "[/] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + this.flagobj.getName());
                lore.add(ChatColor.GREEN + Main.getInstance().lang.getText("activatemode"));
            }
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
            menu.setItem(this.flagobj.getMenuposition(), ITEM);
        } else {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            FlagUtil.checkPerm(ITEM, this.flagobj.getName());
            menu.setItem(this.flagobj.getMenuposition(), ITEM);
        }
    }
}

