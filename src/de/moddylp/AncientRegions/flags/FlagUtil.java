package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.LoadConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.*;

public class FlagUtil {
    public static HashMap<String, FlagOBJ> flagOBJHashMap = new HashMap<>();
    public static HashMap<String, BooleanFlag> booleanFlagHashMap = new HashMap<>();
    public static HashMap<String, StringSetFlag> stringsetFlagHashMap = new HashMap<>();
    public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch(ClassCastException e) {
            return null;
        }
    }
    public static String loadPricefromConfig(String flagname) {
        try {
            LoadConfig config = new LoadConfig(Main.getInstance());
            return config.getOption(flagname.toLowerCase());
        } catch (Exception ex) {
            Main.getInstance().getLogger().info(ex.toString());
        }
        return null;
    }

    public static String loadCurrencyfromConfig() {
        try {
            LoadConfig config = new LoadConfig(Main.getInstance());
            return config.getOption("currency");
        } catch (Exception ex) {
            Main.getInstance().getLogger().info(ex.toString());
        }
        return null;
    }
    public static boolean payment(Player p, Cancellable e, String flagname) {
        RegisteredServiceProvider<Economy> service = Bukkit.getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);
        Economy vaultEcon = service.getProvider();
        if (p.hasPermission("ancient.regions.admin.bypass")) {
            e.setCancelled(true);
            return true;
        }
        if (vaultEcon != null) {
            String price = loadPricefromConfig(flagname);
            if (price != null && vaultEcon.getBalance(p) != 0 && vaultEcon.getBalance(p) >= Double.valueOf(price)) {
                vaultEcon.withdrawPlayer(p, Double.valueOf(price));
                p.sendMessage(ChatColor.BLUE + "[AR][INFO]" + Main.getInstance().lang.getText("PayNote").replace("[PH]",
                        loadPricefromConfig(flagname) + " " + loadCurrencyfromConfig()));
                e.setCancelled(true);
                return true;
            } else {
                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("NoMoney"));
                e.setCancelled(true);
                return false;
            }
        } else {
            p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("VaultError"));
            e.setCancelled(true);
        }
        return false;
    }
    public static String isSet(WorldGuardPlugin worldguard, Player p, Flag<?> flag) {
        RegionContainer container = worldguard.getRegionContainer();
        RegionManager regions = container.get(p.getWorld());
        Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
        if (regions != null) {
            List<String> region = regions.getApplicableRegionsIDs(pt);
            if (!region.isEmpty()) {
                ProtectedRegion rg = regions.getRegion(region.get(0));
                if (rg == null || rg.getFlag(flag) == null) {
                    return "null";
                } else {
                    Object locflag = rg.getFlag(flag);
                    if (locflag != null) {
                        return locflag.toString();
                    } else {
                        return "null";
                    }
                }
            }
        }
        return "null";
    }
    public static void checkPerm(ItemStack ITEM, String flagname) {
        if (ITEM.getItemMeta().getLore() == null) {
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.RED + Main.getInstance().lang.getText("Permission"));
            ItemMeta imeta = ITEM.getItemMeta();
            imeta.setDisplayName(ChatColor.RED + "[OFF] " + Main.getInstance().lang.getText("s") + flagname);
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
        }
    }
}
