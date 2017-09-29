package de.moddylp.AncientRegions.gui;

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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.particle.ParticleShower;

public class Startgui {
    private Inventory menu;
    private Player p;
    private Main plugin;

    public Startgui(Player p, Main plugin) {
        this.plugin = plugin;
        this.p = p;
        this.menu = Bukkit.createInventory(null, 9, ChatColor.GOLD + "AncientRegions");

    }

    // Load the menu items/icons
    private void loadMenuItems() {
        ItemStack flagedit = new ItemStack(Material.BANNER, 1, (short) 0);
        BannerMeta bannerMeta = (BannerMeta) flagedit.getItemMeta();
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.BORDER));
        ItemStack regionmanage = new ItemStack(Material.BANNER, 1, (short) 0);
        BannerMeta bannerMeta1 = (BannerMeta) flagedit.getItemMeta();
        bannerMeta1.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_RIGHT));
        bannerMeta1.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        bannerMeta1.addPattern(new Pattern(DyeColor.BLACK, PatternType.HALF_HORIZONTAL_MIRROR));
        bannerMeta1.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        bannerMeta1.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_LEFT));
        bannerMeta1.addPattern(new Pattern(DyeColor.BLACK, PatternType.BORDER));
        ItemStack rginfo = new ItemStack(Material.PAPER);
        ItemMeta meta3 = rginfo.getItemMeta();

        List<String> desc = new ArrayList<String>();

        desc.add(ChatColor.GREEN + plugin.lang.getText("DescriptionFlags1"));
        desc.add(ChatColor.GREEN + plugin.lang.getText("DescriptionFlags2"));
        List<String> desc2 = new ArrayList<String>();

        desc2.add(ChatColor.GREEN + plugin.lang.getText("RegionInfo"));

        List<String> desc3 = new ArrayList<String>();
        desc3.add(ChatColor.GREEN + plugin.lang.getText("RegionManage"));
        bannerMeta.setDisplayName(ChatColor.GOLD + plugin.lang.getText("EditFlagsItem"));
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
        bannerMeta1.setDisplayName(ChatColor.AQUA + plugin.lang.getText("RegionManager"));
        bannerMeta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_DESTROYS);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        bannerMeta1.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        rginfo.setItemMeta(meta3);
        flagedit.setItemMeta(bannerMeta);
        regionmanage.setItemMeta(bannerMeta1);

        menu.setItem(0, flagedit);
        menu.setItem(8, rginfo);
        menu.setItem(1, regionmanage);
        ParticleShower particle = new ParticleShower(plugin, menu);
        particle.loadgui(p);

    }

    // Open the inventory inGame
    public void open() {
        loadMenuItems();
        p.openInventory(menu);

    }

    // Return menu-name
    public String getName() {
        return menu.getName();
    }

    // Return this
    public Inventory getMenu() {
        return menu;
    }
}
