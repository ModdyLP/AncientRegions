package de.moddylp.AncientRegions.gui;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import de.moddylp.AncientRegions.flags.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.moddylp.AncientRegions.Main;

public class EditflagsPage2 {
	private Inventory menu;
	private Player p;
	private Main plugin;

	public EditflagsPage2(Player p, Main plugin)
    {
        this.plugin = plugin;
        this.p = p;
        this.menu = Bukkit.createInventory(null, 54, ChatColor.RED+plugin.lang.getText("EditFlags")+" 2");
               
    }
    //Load the menu items/icons
	private void loadMenuItems()
    {
		EditFlagsNavigation2 navigation = new EditFlagsNavigation2();
		navigation.loadguiitems(menu, plugin);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.PASSTHROUGH), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.INTERACT), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.BLOCK_BREAK), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.BLOCK_PLACE), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.USE), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.DAMAGE_ANIMALS), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.CHEST_ACCESS), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.RIDE), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SLEEP), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.PLACE_VEHICLE), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.DESTROY_VEHICLE), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LIGHTER), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.GHAST_FIREBALL), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.OTHER_EXPLOSION), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FIRE_SPREAD), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENDER_BUILD), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENTITY_PAINTING_DESTROY), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MYCELIUM_SPREAD), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.VINE_GROWTH), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SOIL_DRY), p, menu);
        GamemodeFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.GAME_MODE), p, menu);
		BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.NOTIFY_ENTER), p, menu);
        BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.NOTIFY_LEAVE), p, menu);
        BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.CHORUS_TELEPORT), p, menu);
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
