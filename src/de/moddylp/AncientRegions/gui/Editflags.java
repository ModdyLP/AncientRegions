package de.moddylp.AncientRegions.gui;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import de.moddylp.AncientRegions.flags.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.moddylp.AncientRegions.Main;

public class Editflags {
	private Inventory menu;
	private Player p;
	private Main plugin;

	public Editflags(Player p, Main plugin) {
		this.plugin = plugin;
		this.p = p;
		this.menu = Bukkit.createInventory(null, 54, ChatColor.RED + plugin.lang.getText("EditFlags") + " 1");
	}

	// Load the menu items/icons
	private void loadMenuItems() {
	    try {
            EditFlagsNavigation navigation = new EditFlagsNavigation();
            navigation.loadguiitems(menu, plugin);

            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.BUILD), p, menu);
            StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.GREET_MESSAGE), p, menu);
            StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FAREWELL_MESSAGE), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.PVP), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MOB_DAMAGE), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENTRY), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.EXIT), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENDERPEARL), p, menu);
            LocationFlagimpl.createandload(FlagOBJ.getFlagObj(DefaultFlag.TELE_LOC), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ITEM_PICKUP), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ITEM_DROP), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.EXP_DROPS), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MOB_SPAWNING), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.CREEPER_EXPLOSION), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.TNT), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.INVINCIBILITY), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE), p, menu);
            EntitySetFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.DENY_SPAWN), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SNOW_FALL), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SNOW_MELT), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ICE_FORM), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ICE_MELT), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.GRASS_SPREAD), p, menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.HEAL_AMOUNT), p, menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.HEAL_DELAY), p, menu);
            DoubleFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MIN_HEAL), p, menu);
            DoubleFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MAX_HEAL), p, menu);
            StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ALLOWED_CMDS), p, menu);
            StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.BLOCKED_CMDS), p, menu);
            StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.TIME_LOCK), p, menu);
            WeatherFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.WEATHER_LOCK), p, menu);
            LocationFlagimpl.createandload(FlagOBJ.getFlagObj(DefaultFlag.SPAWN_LOC), p, menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FEED_AMOUNT), p, menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FEED_DELAY), p, menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MIN_FOOD), p, menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MAX_FOOD), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LAVA_FLOW), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.WATER_FLOW), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LIGHTNING), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LAVA_FIRE), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MUSHROOMS), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.PISTONS), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SEND_CHAT), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.RECEIVE_CHAT), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.POTION_SPLASH), p, menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LEAF_DECAY), p, menu);
            StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.DENY_MESSAGE), p, menu);


        } catch (Exception ex) {
	        ex.printStackTrace();
        }
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
