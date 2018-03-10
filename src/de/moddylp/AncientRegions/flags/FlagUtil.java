package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Events.ActivateMode;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FlagUtil {
    public static HashMap<String, FlagOBJ> flagOBJHashMap = new HashMap<>();
    static HashMap<String, StateFlag> stateFlagHashMap = new HashMap<>();
    static HashMap<String, BooleanFlag> booleanFlagHashMap = new HashMap<>();
    static HashMap<String, StringFlag> stringFlagHashMap = new HashMap<>();
    static HashMap<String, EntitySetFlag> setFlagEntityHashmap = new HashMap<>();
    static HashMap<String, DoubleFlag> doubleflag = new HashMap<>();
    static HashMap<String, IntegerFlag> integerFlagHashMap = new HashMap<>();
    static HashMap<String, GamemodeFlag> gamemodeFlagHashMap = new HashMap<>();
    static HashMap<String, LocationFlagimpl> locationFlagHashMap = new HashMap<>();
    static HashMap<String, WeatherFlag> weatherFlagHashMap = new HashMap<>();

    public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static void checkPermInv(ItemStack ITEM, String regionname) {
        if (ITEM.getItemMeta().getLore() == null) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.RED + Main.getInstance().lang.getText("Permission"));
            ItemMeta imeta = ITEM.getItemMeta();
            imeta.setDisplayName(ChatColor.RED + "[OFF] " + Main.getInstance().lang.getText("Toggle") + regionname);
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
        }
    }

    public static Double loadPricefromConfig(String flagname, ActivateMode mode) {
        try {
            double price = 0.0;
            if (Main.DRIVER.hasKey(Main.DRIVER.CONFIG, flagname.toLowerCase())) {
                price = Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, flagname.toLowerCase()));
            }
            if (mode.equals(ActivateMode.ACTIVATE)) {
                price = price * (Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_activatecostpercent")) / 100.0);
            }
            if (mode.equals(ActivateMode.REMOVE)) {
                price = price * (Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_removecostpercent")) / 100.0);
            }
            if (mode.equals(ActivateMode.DEACTIVATE)) {
                price = price * (Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_deactivatecostpercent")) / 100.0);
            }
            return price;
        } catch (Exception ex) {
            Main.getInstance().getLogger().info(ex.toString());
            return null;
        }
    }

    public static String loadCurrencyfromConfig() {
        try {
            return Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_currency");
        } catch (Exception ex) {
            Main.getInstance().getLogger().info(ex.toString());
            return null;
        }
    }

    public static boolean payment(Player p, Cancellable e, String flagname, ActivateMode mode) {
        RegisteredServiceProvider service = Bukkit.getServicesManager().getRegistration(Economy.class);
        Economy vaultEcon = (Economy) service.getProvider();
        if (p.hasPermission("ancient.regions.admin.bypass")) {
            e.setCancelled(true);
            return true;
        }
        if (vaultEcon != null) {
            Double price = FlagUtil.loadPricefromConfig(flagname, mode);
            if (price != null && vaultEcon.getBalance(p) != 0.0 && vaultEcon.getBalance(p) >= price) {
                vaultEcon.withdrawPlayer(p, price);
                p.sendMessage(ChatColor.BLUE + "[AR][INFO]" + Main.getInstance().lang.getText("PayNote").replace("[PH]", String.valueOf(FlagUtil.loadPricefromConfig(flagname, mode)) + " " + FlagUtil.loadCurrencyfromConfig()));
                e.setCancelled(true);
                return true;
            }
            p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("NoMoney"));
            e.setCancelled(true);
            return false;
        }
        p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("VaultError"));
        e.setCancelled(true);
        return false;
    }

    public static String isSet(Player p, Flag<?> flag) {
        List region;
        RegionContainer container = Main.worldguard.getRegionContainer();
        RegionManager regions = container.get(p.getWorld());
        Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
        if (regions != null && !(region = regions.getApplicableRegionsIDs(pt)).isEmpty()) {
            ProtectedRegion rg = regions.getRegion((String) region.get(0));
            if (rg == null || rg.getFlag(flag) == null) {
                return "null";
            }
            Object flagobj = rg.getFlag(flag);
            if (flagobj != null) {
                return flagobj.toString();
            }
            return "null";
        }
        return "null";
    }

    public static void checkPerm(ItemStack ITEM, String flagname) {
        if (ITEM.getItemMeta().getLore() == null) {
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.RED + Main.getInstance().lang.getText("Permission"));
            ItemMeta imeta = ITEM.getItemMeta();
            imeta.setDisplayName(ChatColor.RED + "[OFF] " + Main.getInstance().lang.getText("s") + flagname);
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
        }
    }

    public static void loadStringGUI(Inventory menu, Player p, FlagOBJ flagOBJ) {
        if (p.hasPermission(flagOBJ.getPermission())) {
            ItemStack ITEM = new ItemStack(flagOBJ.getItem());
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Set").replace("[PH]", flagOBJ.getName()));
            lore.add(ChatColor.GREEN +
                    Objects.requireNonNull(FlagUtil.loadPricefromConfig(flagOBJ.getName(), ActivateMode.ACTIVATE)).toString() + " " + FlagUtil.loadCurrencyfromConfig() + "    " +
                    ChatColor.LIGHT_PURPLE +
                    Objects.requireNonNull(FlagUtil.loadPricefromConfig(flagOBJ.getName(), ActivateMode.REMOVE)).toString() + " " + FlagUtil.loadCurrencyfromConfig()
            );
            if (!FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("null")) {
                lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Current") + ": " + ChatColor.AQUA + FlagUtil.isSet(p, flagOBJ.getFlag()));
            }
            ItemMeta imeta = ITEM.getItemMeta();
            if (!FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("null")) {
                imeta.setDisplayName(ChatColor.GREEN + "[ON] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + flagOBJ.getName());
                lore.add(ChatColor.GREEN + "- "+Main.getInstance().lang.getText("changemode"));
                lore.add(ChatColor.LIGHT_PURPLE + "- "+Main.getInstance().lang.getText("removemode"));
            } else {
                imeta.setDisplayName(ChatColor.BLUE + "[/] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + flagOBJ.getName());
                lore.add(ChatColor.GREEN + "- "+Main.getInstance().lang.getText("activatemode"));
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
    }

    public static void loadBooleanGUI(Inventory menu, Player p, FlagOBJ flagOBJ) {
        if (p.hasPermission(flagOBJ.getPermission())) {
            ItemStack ITEM = new ItemStack(flagOBJ.getItem());
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Set").replace("[PH]", flagOBJ.getName()));
            lore.add(ChatColor.GREEN +
                    Objects.requireNonNull(FlagUtil.loadPricefromConfig(flagOBJ.getName(), ActivateMode.ACTIVATE)).toString() + " " + FlagUtil.loadCurrencyfromConfig() + "    " +
                    ChatColor.RED +
                    Objects.requireNonNull(FlagUtil.loadPricefromConfig(flagOBJ.getName(), ActivateMode.DEACTIVATE)).toString() + " " + FlagUtil.loadCurrencyfromConfig() + "    " +
                    ChatColor.LIGHT_PURPLE +
                    Objects.requireNonNull(FlagUtil.loadPricefromConfig(flagOBJ.getName(), ActivateMode.REMOVE)).toString() + " " + FlagUtil.loadCurrencyfromConfig()
            );
            if (!FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("null")) {
                lore.add(ChatColor.GOLD + Main.getInstance().lang.getText("Current") + ": " + ChatColor.AQUA + FlagUtil.isSet(p, flagOBJ.getFlag()));
            }
            ItemMeta imeta = ITEM.getItemMeta();
            if (FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("true")
                    || FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("allow")) {
                imeta.setDisplayName(ChatColor.GREEN + "[ON] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + flagOBJ.getName());
                lore.add(ChatColor.RED + "- "+Main.getInstance().lang.getText("deactivatemode"));
                lore.add(ChatColor.LIGHT_PURPLE + "- "+Main.getInstance().lang.getText("removemode"));
            } else if (FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("false")
                    || FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("deny")) {
                imeta.setDisplayName(ChatColor.RED + "[OFF] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + flagOBJ.getName());
                lore.add(ChatColor.GREEN + "- "+Main.getInstance().lang.getText("activatemode"));
                lore.add(ChatColor.LIGHT_PURPLE + "- "+Main.getInstance().lang.getText("removemode"));
            } else {
                imeta.setDisplayName(ChatColor.BLUE + "[/] " + ChatColor.GOLD + Main.getInstance().lang.getText("s") + flagOBJ.getName());
                lore.add(ChatColor.GREEN + "- "+Main.getInstance().lang.getText("activatemode"));
                lore.add(ChatColor.RED + "- "+Main.getInstance().lang.getText("deactivatemode"));
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
    }
}

