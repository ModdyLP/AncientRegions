package de.moddylp.AncientRegions.gui;

import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import de.moddylp.AncientRegions.Language;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.EditFlagsNavigation2;
import de.moddylp.AncientRegions.flags.FlagOBJ;
import de.moddylp.AncientRegions.flags.GamemodeFlag;
import de.moddylp.AncientRegions.flags.StateFlag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

public class EditflagsPage2 {
    private Inventory menu;
    private Player p;
    private Main plugin;

    public EditflagsPage2(Player p, Main plugin) {
        this.plugin = plugin;
        this.p = p;
        this.menu = Bukkit.createInventory((InventoryHolder)null, (int)54, (String)(ChatColor.RED + plugin.lang.getText("EditFlags") + " 2"));
    }

    private void loadMenuItems() {
        EditFlagsNavigation2 navigation = new EditFlagsNavigation2();
        navigation.loadguiitems(this.menu, this.plugin);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.PASSTHROUGH), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.INTERACT), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.BLOCK_BREAK), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.BLOCK_PLACE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.USE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.DAMAGE_ANIMALS), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.CHEST_ACCESS), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.RIDE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.SLEEP), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.PLACE_VEHICLE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.DESTROY_VEHICLE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.LIGHTER), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.GHAST_FIREBALL), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.OTHER_EXPLOSION), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.FIRE_SPREAD), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ENDER_BUILD), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ENTITY_PAINTING_DESTROY), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ENTITY_ITEM_FRAME_DESTROY), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.MYCELIUM_SPREAD), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.VINE_GROWTH), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.SOIL_DRY), this.p, this.menu);
        GamemodeFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.GAME_MODE), this.p, this.menu);
        de.moddylp.AncientRegions.flags.BooleanFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.NOTIFY_ENTER), this.p, this.menu);
        de.moddylp.AncientRegions.flags.BooleanFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.NOTIFY_LEAVE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.FIREWORK_DAMAGE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.WITHER_DAMAGE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.CHORUS_TELEPORT), this.p, this.menu);
        de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.EXIT_DENY_MESSAGE), this.p, this.menu);
        de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.ENTRY_DENY_MESSAGE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.EXIT_VIA_TELEPORT), this.p, this.menu);
        de.moddylp.AncientRegions.flags.BooleanFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.EXIT_OVERRIDE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj((Flag)DefaultFlag.FALL_DAMAGE), this.p, this.menu);
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

