package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
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

public class StateFlag {
    private final FlagOBJ flagOBJ;

    private StateFlag(FlagOBJ flagOBJ) {
        this.flagOBJ = flagOBJ;
    }

    public static void createandload(FlagOBJ flagOBJ, Player p, Inventory menu) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        StateFlag flag;
        if (FlagUtil.stateFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.stateFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new StateFlag(flagOBJ);
            FlagUtil.stateFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.loadgui(menu, p);
    }

    public static void createandtoggle(FlagOBJ flagOBJ, Player p, Inventory menu, InventoryClickEvent event) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        StateFlag flag;
        event.setCancelled(true);
        if (FlagUtil.stateFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.stateFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new StateFlag(flagOBJ);
            FlagUtil.stateFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.toggle(event, menu, p);
    }

    public boolean toggle(InventoryClickEvent e, Inventory menu, Player p) {
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
                    ProtectedRegion rg = regions.getRegion((String)region.get(0));
                    if (rg != null && rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass")) {
                        if (rg != null && rg.getFlag(this.flagOBJ.getFlag()) != null) {
                            if (Objects.equals(rg.getFlag(this.flagOBJ.getFlag()),  com.sk89q.worldguard.protection.flags.StateFlag.State.DENY)) {
                                rg.setFlag(this.flagOBJ.getFlag(), null);
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + ChatColor.GOLD + " " + this.flagOBJ.getName() + Main.getInstance().lang.getText("FlagRemoved"));
                            } else if (Objects.equals(rg.getFlag(this.flagOBJ.getFlag()),  com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW)) {
                                if (FlagUtil.payment(p, e, this.flagOBJ.getName())) {
                                    rg.setFlag((com.sk89q.worldguard.protection.flags.StateFlag) flagOBJ.getFlag(), com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
                                    p.sendMessage(ChatColor.GREEN + "[AR][INFO]" + ChatColor.RED + " " + this.flagOBJ.getName() + Main.getInstance().lang.getText("fDisabled"));
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("ToggleError").replace("[PH]", this.flagOBJ.getName()));
                            }
                        } else if (rg != null && FlagUtil.payment(p, e, this.flagOBJ.getName())) {
                            rg.setFlag((com.sk89q.worldguard.protection.flags.StateFlag) flagOBJ.getFlag(), com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.flagOBJ.getName() + Main.getInstance().lang.getText("fEnabled"));
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
            if (FlagUtil.isSet(p, this.flagOBJ.getFlag()).equalsIgnoreCase("allow")) {
                imeta.setDisplayName(ChatColor.GREEN + "[ON] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + this.flagOBJ.getName());
            } else if (FlagUtil.isSet(p, this.flagOBJ.getFlag()).equalsIgnoreCase("deny")) {
                imeta.setDisplayName(ChatColor.RED + "[OFF] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + this.flagOBJ.getName());
            } else {
                imeta.setDisplayName(ChatColor.BLUE + "[/] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + this.flagOBJ.getName());
            }
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
            menu.setItem(this.flagOBJ.getMenuposition(), ITEM);
        } else {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            FlagUtil.checkPerm(ITEM, this.flagOBJ.getName());
            menu.setItem(this.flagOBJ.getMenuposition(), ITEM);
        }
        return true;
    }
}

