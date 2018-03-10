package de.moddylp.AncientRegions.gui;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Editflags {
    private Inventory menu;
    private Player p;
    private Main plugin;

    public Editflags(Player p, Main plugin) {
        this.plugin = plugin;
        this.p = p;
        this.menu = Bukkit.createInventory(null, 54, ChatColor.RED + plugin.lang.getText("EditFlags") + " 1");
    }

    private void loadMenuItems() {
        try {
            EditFlagsNavigation navigation = new EditFlagsNavigation();
            navigation.loadguiitems(this.menu, this.plugin);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.BUILD), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.GREET_MESSAGE), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FAREWELL_MESSAGE), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.PVP), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MOB_DAMAGE), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENTRY), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.EXIT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENDERPEARL), this.p, this.menu);
            LocationFlagimpl.createandload(FlagOBJ.getFlagObj(DefaultFlag.TELE_LOC), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ITEM_PICKUP), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ITEM_DROP), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.EXP_DROPS), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MOB_SPAWNING), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.CREEPER_EXPLOSION), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.TNT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.INVINCIBILITY), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE), this.p, this.menu);
            EntitySetFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.DENY_SPAWN), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SNOW_FALL), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SNOW_MELT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ICE_FORM), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ICE_MELT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.GRASS_SPREAD), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.HEAL_AMOUNT), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.HEAL_DELAY), this.p, this.menu);
            DoubleFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MIN_HEAL), this.p, this.menu);
            DoubleFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MAX_HEAL), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ALLOWED_CMDS), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.BLOCKED_CMDS), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.TIME_LOCK), this.p, this.menu);
            WeatherFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.WEATHER_LOCK), this.p, this.menu);
            LocationFlagimpl.createandload(FlagOBJ.getFlagObj(DefaultFlag.SPAWN_LOC), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FEED_AMOUNT), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FEED_DELAY), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MIN_FOOD), this.p, this.menu);
            IntegerFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MAX_FOOD), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LAVA_FLOW), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.WATER_FLOW), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LIGHTNING), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LAVA_FIRE), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MUSHROOMS), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.PISTONS), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SEND_CHAT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.RECEIVE_CHAT), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.POTION_SPLASH), this.p, this.menu);
            StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LEAF_DECAY), this.p, this.menu);
            de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.DENY_MESSAGE), this.p, this.menu);
        } catch (Exception ex) {
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

