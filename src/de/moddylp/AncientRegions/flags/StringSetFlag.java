package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Events.SpezialFormatString;
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

import static de.moddylp.AncientRegions.flags.FlagUtil.*;
import static de.moddylp.AncientRegions.flags.FlagUtil.isSet;

public class StringSetFlag {
    private final FlagOBJ flagOBJ;
    private final Player p;

    public StringSetFlag(FlagOBJ flagOBJ, Player p) {
        this.flagOBJ = flagOBJ;
        this.p = p;
    }
    public static void createandload(FlagOBJ flagOBJ, Player p, Inventory menu) {
        StringSetFlag flag;
        if (FlagUtil.stringsetFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.stringsetFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new StringSetFlag(flagOBJ, p);
            FlagUtil.stringsetFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.loadgui(menu);
    }
    public static void createandtoggle(FlagOBJ flagOBJ, Player p, Inventory menu, InventoryClickEvent event) {
        event.setCancelled(true);
        StringSetFlag flag;
        if (FlagUtil.stringsetFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.stringsetFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new StringSetFlag(flagOBJ, p);
            FlagUtil.stringsetFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.toggle(event, menu);
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
                            rg.setFlag(flagOBJ.getFlag(), null);
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + ChatColor.GOLD + " " + flagOBJ.getName()
                                    + Main.getInstance().lang.getText("FlagRemoved"));
                        } else {
                            if (payment(p, e, flagOBJ.getName())) {
                                if (flagOBJ.getFlag() instanceof SetFlag) {
                                    p.closeInventory();
                                    p.sendMessage(ChatColor.GREEN + "[AR][INFO] "
                                            + Main.getInstance().lang.getText("Message4").replace("[PH]", flagOBJ.getName()));
                                    Main.getInstance().getServer().getPluginManager().registerEvents(
                                            new SpezialFormatString(p, flagOBJ), Main.getInstance());
                                }
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
