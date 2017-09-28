package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
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

public class LocationFlagimpl {
    private Player p;
    private FlagOBJ flagobj;

    public LocationFlagimpl(FlagOBJ flagobj, Player p) {
        this.p = p;
        this.flagobj = flagobj;
    }

    public static void createandload(FlagOBJ flagOBJ, Player p, Inventory menu) {
        LocationFlagimpl flag;
        if (FlagUtil.locationFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.locationFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new LocationFlagimpl(flagOBJ, p);
            FlagUtil.locationFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.loadgui(menu);
    }

    public static void createandtoggle(FlagOBJ flagOBJ, Player p, Inventory menu, InventoryClickEvent event) {
        event.setCancelled(true);
        LocationFlagimpl flag;
        if (FlagUtil.locationFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.locationFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new LocationFlagimpl(flagOBJ, p);
            FlagUtil.locationFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.toggle(event, menu);
    }

    public boolean toggle(InventoryClickEvent e, Inventory menu) {

        if (p.hasPermission(flagobj.getPermission())) {
            RegionContainer container = Main.worldguard.getRegionContainer();
            RegionManager regions = container.get(p.getWorld());
            Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
            LocalPlayer ply = Main.worldguard.wrapPlayer(p);
            List<String> region = null;
            if (regions != null) {
                region = regions.getApplicableRegionsIDs(pt);
                if (region.isEmpty()) {
                    p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion(region.get(0));
                    if (rg != null && rg.isOwner(ply) || rg != null && p.hasPermission("ancient.regions.admin.bypass")) {
                        if (rg.getFlag(flagobj.getFlag()) != null) {
                            rg.setFlag(flagobj.getFlag(), null);
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + ChatColor.GOLD + " " + flagobj.getName() + Main.getInstance().lang.getText("FlagRemoved"));
                        } else {
                            if (FlagUtil.payment(p, e, flagobj.getName())) {
                                rg.setFlag((LocationFlag) flagobj.getFlag(), BukkitUtil.toLocation(p.getLocation()));
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + flagobj.getName() + Main.getInstance().lang.getText("fEnabled"));
                            }
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
        loadgui(menu);
        return false;
    }

    public boolean loadgui(Inventory menu) {
        if (p.hasPermission(flagobj.getPermission())) {
            ItemStack ITEM = new ItemStack(flagobj.getItem());
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Set").replace("[PH]", flagobj.getName()));
            lore.add(ChatColor.YELLOW + FlagUtil.loadPricefromConfig(flagobj.getName()).toString() + " " + FlagUtil.loadCurrencyfromConfig());
            if (!FlagUtil.isSet(p, flagobj.getFlag()).equals("false")) {
                lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Current") + ":");
                String[] location = FlagUtil.isSet(p, flagobj.getFlag()).split(",");
                location[1] = location[1].replace(" :", "X: ");
                location[2] = "Y: " + location[2];
                location[3] = "Z: " + location[3];
                for (String loc : location) {
                    lore.add(ChatColor.AQUA + loc);
                }

            }
            ItemMeta imeta = ITEM.getItemMeta();
            if (!FlagUtil.isSet(p, flagobj.getFlag()).equals("false")) {
                imeta.setDisplayName(ChatColor.GREEN + "[ON] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + flagobj.getName());
            } else {
                imeta.setDisplayName(ChatColor.BLUE + "[/] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + flagobj.getName());
            }
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
            menu.setItem(flagobj.getMenuposition(), ITEM);
        } else {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            if (ITEM.getItemMeta().getLore() == null) {
                List<String> lore = new ArrayList<String>();
                lore.add(ChatColor.RED + Main.getInstance().lang.getText("Permission"));
                ItemMeta imeta = ITEM.getItemMeta();
                imeta.setDisplayName(ChatColor.RED + "[OFF] " + Main.getInstance().lang.getText("Toggle") + flagobj.getName());
                imeta.setLore(lore);
                imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                ITEM.setItemMeta(imeta);
            }
            menu.setItem(flagobj.getMenuposition(), ITEM);
        }
        return true;
    }
}
