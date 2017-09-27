package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.flags.StateFlag;
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
import java.util.Objects;

import static de.moddylp.AncientRegions.flags.FlagUtil.*;

/**
 * Created by N.Hartmann on 27.09.2017.
 * Copyright 2017
 */
public class BooleanFlag {
    private final FlagOBJ flagOBJ;
    private final Player p;

    public BooleanFlag(FlagOBJ flagOBJ, Player p) {
        this.flagOBJ = flagOBJ;
        this.p = p;
    }

    public boolean toggle(InventoryClickEvent e, Inventory menu) {

        if (p.hasPermission(flagOBJ.getPermission())) {
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
                    if (rg != null && rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass")) {
                        if (rg != null && rg.getFlag(flagOBJ.getFlag()) != null) {
                            if (Objects.equals(rg.getFlag(flagOBJ.getFlag()), StateFlag.State.DENY)) {
                                rg.setFlag(flagOBJ.getFlag(), null);
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + ChatColor.GOLD + " " + flagOBJ.getName() + Main.getInstance().lang.getText("FlagRemoved"));
                            } else if (Objects.equals(rg.getFlag(flagOBJ.getFlag()), StateFlag.State.ALLOW)) {
                                if (FlagUtil.payment(p, e, flagOBJ.getName())) {
                                    rg.setFlag((StateFlag) flagOBJ.getFlag(), StateFlag.State.DENY);
                                    p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + ChatColor.RED + " " + flagOBJ.getName() + Main.getInstance().lang.getText("fDisabled"));
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("ToggleError").replace("[PH]", flagOBJ.getName()));
                            }
                        } else {
                            if (rg != null && FlagUtil.payment(p, e, flagOBJ.getName())) {
                                rg.setFlag((StateFlag) flagOBJ.getFlag(), StateFlag.State.ALLOW);
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + flagOBJ.getName() + Main.getInstance().lang.getText("fEnabled"));
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
        loadgui(menu);
        return false;
    }

    public boolean loadgui(Inventory menu) {
        if (p.hasPermission(flagOBJ.getPermission())) {
            ItemStack ITEM = new ItemStack(flagOBJ.getItem());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Set").replace("[PH]", flagOBJ.getName()));
            lore.add(ChatColor.YELLOW + loadPricefromConfig(flagOBJ.getName()) + " " + loadCurrencyfromConfig());
            if (!isSet(Main.worldguard, p, flagOBJ.getFlag()).equals("null")) {
                lore.add(
                        ChatColor.GOLD + Main.getInstance().lang.getText("Current") + ": " + ChatColor.AQUA + isSet(Main.worldguard, p, flagOBJ.getFlag()));
            }
            ItemMeta imeta = ITEM.getItemMeta();
            if (!isSet(Main.worldguard, p, flagOBJ.getFlag()).equals("null")) {
                imeta.setDisplayName(ChatColor.GREEN + "[ON] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + flagOBJ.getName());
            } else {
                imeta.setDisplayName(ChatColor.BLUE + "[/] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + flagOBJ.getName());
            }
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
            menu.setItem(flagOBJ.getMenuposition(), ITEM);
        } else {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            FlagUtil.checkPerm(ITEM, flagOBJ.getName());
            menu.setItem(flagOBJ.getMenuposition(), ITEM);
        }
        return true;
    }
}
