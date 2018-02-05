package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Events.SpezialFormatIntegar;
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

public class IntegerFlag {
    private FlagOBJ flagobj;

    private IntegerFlag(FlagOBJ flagOBJ) {
        this.flagobj = flagOBJ;
    }

    public static void createandload(FlagOBJ flagOBJ, Player p, Inventory menu) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        IntegerFlag flag;
        if (FlagUtil.doubleflag.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.integerFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new IntegerFlag(flagOBJ);
            FlagUtil.integerFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.loadgui(menu, p);
    }

    public static void createandtoggle(FlagOBJ flagOBJ, Player p, Inventory menu, InventoryClickEvent event) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        IntegerFlag flag;
        event.setCancelled(true);
        if (FlagUtil.integerFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.integerFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new IntegerFlag(flagOBJ);
            FlagUtil.integerFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.toggle(event, menu, p);
    }

    public boolean toggle(InventoryClickEvent e, Inventory menu, Player p) {
        if (p.hasPermission(this.flagobj.getPermission())) {
            RegionContainer container = Main.worldguard.getRegionContainer();
            RegionManager regions = container.get(p.getWorld());
            Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
            LocalPlayer ply = Main.worldguard.wrapPlayer(p);
            if (regions != null) {
                List region = regions.getApplicableRegionsIDs(pt);
                if (region.isEmpty()) {
                    p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion((String)region.get(0));
                    if (rg != null && rg.isOwner(ply) || rg != null && p.hasPermission("ancient.regions.admin.bypass")) {
                        if (rg.getFlag(this.flagobj.getFlag()) != null) {
                            rg.setFlag(this.flagobj.getFlag(), null);
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + ChatColor.GOLD + " " + this.flagobj.getName() + Main.getInstance().lang.getText("FlagRemoved"));
                        } else {
                            p.closeInventory();
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + Main.getInstance().lang.getText("Message3").replace("[PH]", this.flagobj.getName()));
                            Main.getInstance().getServer().getPluginManager().registerEvents(new SpezialFormatIntegar(p, this.flagobj), Main.getInstance());
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
        if (p.hasPermission(this.flagobj.getPermission())) {
            ItemStack ITEM = new ItemStack(this.flagobj.getItem());
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Set").replace("[PH]", this.flagobj.getName()));
            lore.add(ChatColor.YELLOW + Objects.requireNonNull(FlagUtil.loadPricefromConfig(this.flagobj.getName())).toString() + " " + FlagUtil.loadCurrencyfromConfig());
            if (!FlagUtil.isSet(p, this.flagobj.getFlag()).equals("null")) {
                lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Current") + ": " + ChatColor.AQUA + FlagUtil.isSet(p, this.flagobj.getFlag()));
            }
            ItemMeta imeta = ITEM.getItemMeta();
            if (!FlagUtil.isSet(p, this.flagobj.getFlag()).equals("null")) {
                imeta.setDisplayName(ChatColor.GREEN + "[ON] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + this.flagobj.getName());
            } else {
                imeta.setDisplayName(ChatColor.BLUE + "[/] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + this.flagobj.getName());
            }
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
            menu.setItem(this.flagobj.getMenuposition(), ITEM);
        } else {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            if (ITEM.getItemMeta().getLore() == null) {
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.RED + Main.getInstance().lang.getText("Permission"));
                ItemMeta imeta = ITEM.getItemMeta();
                imeta.setDisplayName(ChatColor.RED + "[OFF] " + Main.getInstance().lang.getText("s") + this.flagobj.getName());
                imeta.setLore(lore);
                imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                ITEM.setItemMeta(imeta);
            }
            menu.setItem(this.flagobj.getMenuposition(), ITEM);
        }
        return true;
    }
}
