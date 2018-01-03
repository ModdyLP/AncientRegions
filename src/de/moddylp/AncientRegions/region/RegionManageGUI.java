package de.moddylp.AncientRegions.region;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Language;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagUtil;
import de.moddylp.AncientRegions.loader.FileDriver;
import de.moddylp.AncientRegions.region.RegionManageNavigation;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RegionManageGUI {
    private Main plugin;
    private Player p;
    private Inventory menu;
    private WorldGuardPlugin worldguard;

    public RegionManageGUI(Player p, Main plugin, WorldGuardPlugin worldguard) {
        this.p = p;
        this.plugin = plugin;
        this.menu = Bukkit.createInventory((InventoryHolder)null, (int)18, (String)(ChatColor.GOLD + plugin.lang.getText("RegionManager")));
        this.worldguard = worldguard;
    }

    private void loadMenuItems() {
        try {
            RegionManageNavigation navigation = new RegionManageNavigation();
            navigation.loadguiitems(this.menu, this.plugin);
            ItemStack regionbuy = new ItemStack(Material.EMPTY_MAP);
            ItemMeta meta = regionbuy.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + this.plugin.lang.getText("RegionBuy"));
            regionbuy.setItemMeta(meta);
            this.menu.setItem(0, regionbuy);
            ItemStack addmember = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            ItemMeta meta2 = addmember.getItemMeta();
            meta2.setDisplayName(ChatColor.GREEN + this.plugin.lang.getText("AddMember"));
            ArrayList<String> values = new ArrayList<String>();
            values.add(ChatColor.YELLOW + Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_addmember") + " " + FlagUtil.loadCurrencyfromConfig());
            meta2.setLore(values);
            addmember.setItemMeta(meta2);
            this.menu.setItem(3, addmember);
            ItemStack removemember = new ItemStack(Material.SKULL_ITEM, 1, (short) 0);
            ItemMeta meta3 = removemember.getItemMeta();
            meta3.setDisplayName(ChatColor.RED + this.plugin.lang.getText("RemoveMember"));
            ArrayList<String> values2 = new ArrayList<String>();
            values2.add(ChatColor.YELLOW + Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_removemember") + " " + FlagUtil.loadCurrencyfromConfig());
            meta3.setLore(values2);
            removemember.setItemMeta(meta3);
            this.menu.setItem(5, removemember);
            ItemStack setowner = new ItemStack(Material.MAP);
            ItemMeta meta4 = setowner.getItemMeta();
            meta4.setDisplayName(ChatColor.GOLD + this.plugin.lang.getText("SetOwner"));
            ArrayList<String> values3 = new ArrayList<String>();
            values3.add(ChatColor.YELLOW + Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_changeowner") + " " + FlagUtil.loadCurrencyfromConfig());
            meta4.setLore(values3);
            setowner.setItemMeta(meta4);
            this.menu.setItem(4, setowner);
            ItemStack removeregion = new ItemStack(Material.BARRIER);
            ItemMeta meta5 = removeregion.getItemMeta();
            meta5.setDisplayName(ChatColor.RED + this.plugin.lang.getText("RemoveRegion"));
            ArrayList<String> desc = new ArrayList<String>();
            desc.add(ChatColor.AQUA + this.plugin.lang.getText("RemoveRegionLore1"));
            if (this.getregionnumber() == 0) {
                desc.add(ChatColor.AQUA + this.plugin.lang.getText("RemoveRegionLore2").replace("[PH]", "XX"));
            } else {
                desc.add(ChatColor.AQUA + this.plugin.lang.getText("RemoveRegionLore2").replace("[PH]", new StringBuilder().append(Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, new StringBuilder().append("_region").append(this.getregionnumber()).append("price").toString())) * (Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_payback")) / 100.0)).append(" ").append(FlagUtil.loadCurrencyfromConfig()).toString()));
            }
            meta5.setLore(desc);
            removeregion.setItemMeta(meta5);
            this.menu.setItem(8, removeregion);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void open() {
        this.p.closeInventory();
        this.loadMenuItems();
        this.p.openInventory(this.menu);
    }

    public String getName() {
        return this.menu.getName();
    }

    public Inventory getMenu() {
        return this.menu;
    }

    private int getregionnumber() {
        RegionContainer container = this.worldguard.getRegionContainer();
        RegionManager regions = container.get(this.p.getWorld());
        Vector pt = new Vector(this.p.getLocation().getX(), this.p.getLocation().getY(), this.p.getLocation().getZ());
        LocalPlayer ply = this.worldguard.wrapPlayer(this.p);
        List region = regions.getApplicableRegionsIDs(pt);
        if (!region.isEmpty()) {
            ProtectedRegion rg = regions.getRegion((String)region.get(0));
            if (rg.isOwner(ply) || this.p.hasPermission("ancient.regions.admin.bypass")) {
                String numbername = rg.getId().replace("-", "").replace("_", "").replace(this.p.getName().toLowerCase(), "");
                for (int count = regions.getRegionCountOfPlayer((LocalPlayer)ply); count > 0; --count) {
                    numbername = numbername.replace(String.valueOf(count), "");
                }
                String option = Main.DRIVER.getPropertyByValue(Main.DRIVER.CONFIG, "_" + numbername + "name");
                if (option != null) {
                    int number = Integer.valueOf(option.replace("region", "").replace("name", ""));
                    return number;
                }
                return 0;
            }
            return 0;
        }
        return 0;
    }
}

