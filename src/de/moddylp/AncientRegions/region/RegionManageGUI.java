package de.moddylp.AncientRegions.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.LoadConfig;

public class RegionManageGUI {
	private Main plugin;
	private Player p;
	private Inventory menu;
	private LoadConfig config;
	private WorldGuardPlugin worldguard;

	public RegionManageGUI(Player p, Main plugin, WorldGuardPlugin worldguard) {
		this.p = p;
		this.plugin = plugin;
		this.menu = Bukkit.createInventory(null, 18, ChatColor.GOLD + plugin.lang.getText("RegionManager"));
		this.config = new LoadConfig(plugin);
		this.worldguard = worldguard;
	}
    //Load the menu items/icons
	private void loadMenuItems()
    {
		RegionManageNavigation navigation = new RegionManageNavigation();
		navigation.loadguiitems(menu, plugin);
		ItemStack regionbuy = new ItemStack(Material.EMPTY_MAP);
		ItemMeta meta = regionbuy.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD+plugin.lang.getText("RegionBuy"));
		regionbuy.setItemMeta(meta);
		menu.setItem(0, regionbuy);
		ItemStack addmember = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		ItemMeta meta2 = addmember.getItemMeta();
		meta2.setDisplayName(ChatColor.GREEN+plugin.lang.getText("AddMember"));
		List<String> values = new ArrayList<>();
		values.add(ChatColor.YELLOW + config.getOption("addmember") + " " + config.getOption("currency"));
		meta2.setLore(values);
		addmember.setItemMeta(meta2);
		menu.setItem(3, addmember);
		ItemStack removemember = new ItemStack(Material.SKULL_ITEM, 1, (byte) 0);
		ItemMeta meta3 = removemember.getItemMeta();
		meta3.setDisplayName(ChatColor.RED+plugin.lang.getText("RemoveMember"));
		List<String> values2 = new ArrayList<>();
		values2.add(ChatColor.YELLOW + config.getOption("removemember") + " " + config.getOption("currency"));
		meta3.setLore(values2);
		removemember.setItemMeta(meta3);
		menu.setItem(5, removemember);
		
		ItemStack setowner = new ItemStack(Material.MAP);
		ItemMeta meta4 = setowner.getItemMeta();
		meta4.setDisplayName(ChatColor.GOLD+plugin.lang.getText("SetOwner"));
		List<String> values3 = new ArrayList<>();
		values3.add(ChatColor.YELLOW + config.getOption("changeowner") + " " + config.getOption("currency"));
		meta4.setLore(values3);
		setowner.setItemMeta(meta4);
		menu.setItem(4, setowner);
		
		ItemStack removeregion = new ItemStack(Material.BARRIER);
		ItemMeta meta5 = removeregion.getItemMeta();
		meta5.setDisplayName(ChatColor.RED+plugin.lang.getText("RemoveRegion"));
		List<String> desc = new ArrayList<>();
		desc.add(ChatColor.AQUA+plugin.lang.getText("RemoveRegionLore1"));
		if (getregionnumber() == 0) {
			desc.add(ChatColor.AQUA+plugin.lang.getText("RemoveRegionLore2").replace("[PH]", "XX"));
		} else {
				desc.add(ChatColor.AQUA+plugin.lang.getText("RemoveRegionLore2").replace("[PH]",
				Double.valueOf(config.getOption("region"+getregionnumber()+"price"))*(Double.valueOf(config.getOption("payback"))/100) + " " + config.getOption("currency")));
		}
		meta5.setLore(desc);
		removeregion.setItemMeta(meta5);
		menu.setItem(8, removeregion);
    }
	//Open the inventory inGame
    public void open()
    {
       loadMenuItems(); 
       p.openInventory(menu);
        
    }
    
    //Return menu-name
    public String getName()
    {
        return menu.getName();
    }
    
    //Return this
    public Inventory getMenu()
    {
        return menu;
    }
    public int getregionnumber() {
    	RegionContainer container = worldguard.getRegionContainer();
		RegionManager regions = container.get(p.getWorld());
		Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
		LocalPlayer ply = worldguard.wrapPlayer(p);
		List<String> region = regions.getApplicableRegionsIDs(pt);
		if (!region.isEmpty()) {
			ProtectedRegion rg = regions.getRegion(region.get(0));
			if (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass")) {
				String numbername = rg.getId().replace("-", "").replace(p.getName().toLowerCase(), "");
				int count = regions.getRegionCountOfPlayer(ply);
				while (count > 0) {
					numbername = numbername.replace(String.valueOf(count), "");
					count = count-1;
				}
				String option = config.searchoption(numbername);
				if (option != null) {
					int number = Integer.valueOf(option.replace("region", "").replace("name", ""));
					return number;	
				} else {
					return 0;
				}
			} else {
				return 0;
			}
		} else {
		return 0;
		}
    }
}
