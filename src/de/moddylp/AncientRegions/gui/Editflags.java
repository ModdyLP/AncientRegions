package de.moddylp.AncientRegions.gui;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import de.moddylp.AncientRegions.Language;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.DoubleFlag;
import de.moddylp.AncientRegions.flags.EditFlagsNavigation;
import de.moddylp.AncientRegions.flags.EntitySetFlag;
import de.moddylp.AncientRegions.flags.FlagOBJ;
import de.moddylp.AncientRegions.flags.IntegerFlag;
import de.moddylp.AncientRegions.flags.LocationFlagimpl;
import de.moddylp.AncientRegions.flags.StateFlag;
import de.moddylp.AncientRegions.flags.WeatherFlag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public class Editflags {
    private Inventory menu;
    private Player p;
    private Main plugin;

    public Editflags(Player p, Main plugin) {
        this.plugin = plugin;
        this.p = p;
        this.menu = Bukkit.createInventory((InventoryHolder)null, (int)54, (String)(ChatColor.RED + plugin.lang.getText("EditFlags") + " 1"));
    }

    private void loadMenuItems() {
        try {
            EditFlagsNavigation navigation = new EditFlagsNavigation();
            navigation.loadguiitems(this.menu, this.plugin);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.BUILD), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.GREET_MESSAGE), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.FAREWELL_MESSAGE), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.PVP), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.MOB_DAMAGE), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ENTRY), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.EXIT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ENDERPEARL), this.p, this.menu);
            LocationFlagimpl.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.TELE_LOC), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ITEM_PICKUP), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ITEM_DROP), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.EXP_DROPS), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.MOB_SPAWNING), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.CREEPER_EXPLOSION), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.TNT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.INVINCIBILITY), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE), this.p, this.menu);
            EntitySetFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.DENY_SPAWN), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.SNOW_FALL), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.SNOW_MELT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ICE_FORM), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ICE_MELT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.GRASS_SPREAD), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.HEAL_AMOUNT), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.HEAL_DELAY), this.p, this.menu);
            DoubleFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.MIN_HEAL), this.p, this.menu);
            DoubleFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.MAX_HEAL), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ALLOWED_CMDS), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.BLOCKED_CMDS), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.TIME_LOCK), this.p, this.menu);
            WeatherFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.WEATHER_LOCK), this.p, this.menu);
            LocationFlagimpl.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.SPAWN_LOC), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.FEED_AMOUNT), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.FEED_DELAY), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.MIN_FOOD), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.MAX_FOOD), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.LAVA_FLOW), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.WATER_FLOW), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.LIGHTNING), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.LAVA_FIRE), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.MUSHROOMS), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.PISTONS), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.SEND_CHAT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.RECEIVE_CHAT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.POTION_SPLASH), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.LEAF_DECAY), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.DENY_MESSAGE), this.p, this.menu);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
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

