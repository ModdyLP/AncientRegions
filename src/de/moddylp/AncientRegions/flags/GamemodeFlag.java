package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Events.GamemodeFormat;
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

public class GamemodeFlag {
    private FlagOBJ flagOBJ;

    private GamemodeFlag(FlagOBJ flagOBJ) {
        this.flagOBJ = flagOBJ;
    }

    public static void createandload(FlagOBJ flagOBJ, Player p, Inventory menu) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        GamemodeFlag flag;
        if (FlagUtil.gamemodeFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.gamemodeFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new GamemodeFlag(flagOBJ);
            FlagUtil.gamemodeFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.loadgui(menu, p);
    }

    public static void createandtoggle(FlagOBJ flagOBJ, Player p, Inventory menu, InventoryClickEvent event) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        GamemodeFlag flag;
        event.setCancelled(true);
        if (FlagUtil.gamemodeFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.gamemodeFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new GamemodeFlag(flagOBJ);
            FlagUtil.gamemodeFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.toggle(event, menu, p);
    }

    public boolean toggle(InventoryClickEvent e, Inventory menu, Player p) {
        if (p.hasPermission(this.flagOBJ.getPermission())) {
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
                    ProtectedRegion rg = regions.getRegion((String)region.get(0));
                    if (rg != null && rg.isOwner(ply) || rg != null && p.hasPermission("ancient.regions.admin.bypass")) {
                        if (rg.getFlag(this.flagOBJ.getFlag()) != null) {
                            rg.setFlag(this.flagOBJ.getFlag(), null);
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + ChatColor.GOLD + " " + this.flagOBJ.getName() + Main.getInstance().lang.getText("FlagRemoved"));
                        } else {
                            p.closeInventory();
                            p.sendMessage(ChatColor.GOLD + Main.getInstance().lang.getText("Gamemode"));
                            Main.getInstance().getServer().getPluginManager().registerEvents(new GamemodeFormat(p, this.flagOBJ, rg), Main.getInstance());
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

    public boolean loadgui(Inventory menu, Player p) {
        if (p.hasPermission(this.flagOBJ.getPermission())) {
            ItemStack ITEM = new ItemStack(this.flagOBJ.getItem());
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Set").replace("[PH]", this.flagOBJ.getName()));
            lore.add(ChatColor.YELLOW + Objects.requireNonNull(FlagUtil.loadPricefromConfig(this.flagOBJ.getName())).toString() + " " + FlagUtil.loadCurrencyfromConfig());
            if (!FlagUtil.isSet(p, this.flagOBJ.getFlag()).equals("null")) {
                lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Current") + ": " + ChatColor.AQUA + FlagUtil.isSet(p, this.flagOBJ.getFlag()));
            }
            ItemMeta imeta = ITEM.getItemMeta();
            if (!FlagUtil.isSet(p, this.flagOBJ.getFlag()).equals("null")) {
                imeta.setDisplayName(ChatColor.GREEN + "[ON] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + this.flagOBJ.getName());
            } else {
                imeta.setDisplayName(ChatColor.BLUE + "[/] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + this.flagOBJ.getName());
            }
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
            menu.setItem(this.flagOBJ.getMenuposition(), ITEM);
        } else {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            if (ITEM.getItemMeta().getLore() == null) {
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.RED + Main.getInstance().lang.getText("Permission"));
                ItemMeta imeta = ITEM.getItemMeta();
                imeta.setDisplayName(ChatColor.RED + "[OFF] " + Main.getInstance().lang.getText("s") + this.flagOBJ.getName());
                imeta.setLore(lore);
                imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                ITEM.setItemMeta(imeta);
            }
            menu.setItem(this.flagOBJ.getMenuposition(), ITEM);
        }
        return true;
    }
}

