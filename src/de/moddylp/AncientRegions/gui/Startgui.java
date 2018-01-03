package de.moddylp.AncientRegions.gui;

import de.moddylp.AncientRegions.Language;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.particle.ParticleShower;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class Startgui {
    private Inventory menu;
    private Player p;
    private Main plugin;

    public Startgui(Player p, Main plugin) {
        this.plugin = plugin;
        this.p = p;
        this.menu = Bukkit.createInventory((InventoryHolder)null, (int)9, (String)(ChatColor.GOLD + "AncientRegions"));
    }

    private void loadMenuItems() {
        ItemStack flagedit = new ItemStack(Material.BANNER, 1, (short) 0);
        BannerMeta bannerMeta = (BannerMeta)flagedit.getItemMeta();
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.BORDER));
        ItemStack regionmanage = new ItemStack(Material.BANNER, 1, (short) 0);
        BannerMeta bannerMeta1 = (BannerMeta)flagedit.getItemMeta();
        bannerMeta1.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bannerMeta1.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bannerMeta1.addPattern(new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR));
        bannerMeta1.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        bannerMeta1.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bannerMeta1.addPattern(new Pattern(DyeColor.BLACK, PatternType.BORDER));
        ItemStack rginfo = new ItemStack(Material.PAPER);
        ItemMeta meta3 = rginfo.getItemMeta();
        ArrayList<String> desc = new ArrayList<String>();
        desc.add(ChatColor.GREEN + this.plugin.lang.getText("DescriptionFlags1"));
        desc.add(ChatColor.GREEN + this.plugin.lang.getText("DescriptionFlags2"));
        ArrayList<String> desc2 = new ArrayList<String>();
        desc2.add(ChatColor.GREEN + this.plugin.lang.getText("RegionInfo"));
        ArrayList<String> desc3 = new ArrayList<String>();
        desc3.add(ChatColor.GREEN + this.plugin.lang.getText("RegionManage"));
        bannerMeta.setDisplayName(ChatColor.GOLD + this.plugin.lang.getText("EditFlagsItem"));
        bannerMeta.setLore(desc);
        bannerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        bannerMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        bannerMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bannerMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        bannerMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta3.setDisplayName(ChatColor.AQUA + "Region Info");
        meta3.setLore(desc2);
        bannerMeta1.setLore(desc3);
        bannerMeta1.setDisplayName(ChatColor.AQUA + this.plugin.lang.getText("RegionManager"));
        bannerMeta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_DESTROYS);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        rginfo.setItemMeta(meta3);
        flagedit.setItemMeta((ItemMeta)bannerMeta);
        regionmanage.setItemMeta((ItemMeta)bannerMeta1);
        this.menu.setItem(0, flagedit);
        this.menu.setItem(8, rginfo);
        this.menu.setItem(1, regionmanage);
        ParticleShower particle = new ParticleShower(this.plugin, this.menu);
        particle.loadgui(this.p);
    }

    public void open() {
        this.loadMenuItems();
        this.p.openInventory(this.menu);
    }

    public String getName() {
        return this.menu.getName();
    }

    public Inventory getMenu() {
        return this.menu;
    }
}

