package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Events.SpezialFormatDouble;
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

/**
 * Created by N.Hartmann on 28.09.2017.
 * Copyright 2017
 */
public class DoubleFlag {
    private FlagOBJ flagobj;
    //FlagOBJ Description
    private Player p;

    public DoubleFlag(FlagOBJ flagOBJ, Player p) {
        this.flagobj = flagOBJ;
        this.p = p;
    }

    public static void createandload(FlagOBJ flagOBJ, Player p, Inventory menu) {
        DoubleFlag flag;
        if (FlagUtil.doubleflag.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.doubleflag.get(flagOBJ.getName());
        } else {
            flag = new DoubleFlag(flagOBJ, p);
            FlagUtil.doubleflag.put(flagOBJ.getName(), flag);
        }
        flag.loadgui(menu);
    }
    public static void createandtoggle(FlagOBJ flagOBJ, Player p, Inventory menu, InventoryClickEvent event) {
        event.setCancelled(true);
        DoubleFlag flag;
        if (FlagUtil.doubleflag.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.doubleflag.get(flagOBJ.getName());
        } else {
            flag = new DoubleFlag(flagOBJ, p);
            FlagUtil.doubleflag.put(flagOBJ.getName(), flag);
        }
        flag.toggle(event, menu);
    }
    public boolean toggle(InventoryClickEvent e, Inventory menu) {
        if (p.hasPermission(flagobj.getPermission())) {
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
                        if (rg.getFlag(flagobj.getFlag()) != null) {
                            rg.setFlag(flagobj.getFlag(), null);
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + ChatColor.GOLD + " " + flagobj.getName() + Main.getInstance().lang.getText("FlagRemoved"));
                        } else {
                            if (FlagUtil.payment(p, e, flagobj.getName())) {
                                p.closeInventory();
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + Main.getInstance().lang.getText("Message3").replace("[PH]", flagobj.getName()));
                                Main.getInstance().getServer().getPluginManager().registerEvents(new SpezialFormatDouble(p, flagobj), Main.getInstance());
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
            if (!FlagUtil.isSet(p, flagobj.getFlag()).equals("null")) {
                lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Current") + ": " + ChatColor.AQUA + FlagUtil.isSet(p, flagobj.getFlag()));
            }
            ItemMeta imeta = ITEM.getItemMeta();
            if (!FlagUtil.isSet(p, flagobj.getFlag()).equals("null")) {
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
                imeta.setDisplayName(ChatColor.RED + "[OFF] " + Main.getInstance().lang.getText("s") + flagobj.getName());
                imeta.setLore(lore);
                imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                ITEM.setItemMeta(imeta);
            }
            menu.setItem(flagobj.getMenuposition(), ITEM);
        }
        return true;
    }
}
