package de.moddylp.AncientRegions.region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.moddylp.AncientRegions.Main;

public class BuyRegionGUI {
	private Main plugin;
	private Player p;
	private Inventory menu;
	private WorldGuardPlugin worldguard;

	public BuyRegionGUI(Player p, Main plugin, WorldGuardPlugin worldguard) {
		this.p = p;
		this.plugin = plugin;
		this.worldguard = worldguard;
		this.menu = Bukkit.createInventory(null, 27, ChatColor.GOLD + plugin.lang.getText("RegionBuy"));
	}
    //Load the menu items/icons
	private void loadMenuItems()
    {
		Region region1 = new Region(plugin, 1);
		region1.loadgui(menu, p, worldguard);
		Region region2 = new Region(plugin, 2);
		region2.loadgui(menu, p, worldguard);
		Region region3 = new Region(plugin, 3);
		region3.loadgui(menu, p, worldguard);
		Region region4 = new Region(plugin, 4);
		region4.loadgui(menu, p, worldguard);
		Navigation navi = new Navigation();
		navi.loadguiitems(menu, plugin);
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
}
