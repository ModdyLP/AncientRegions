package de.moddylp.AncientRegions.gui;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.EditFlagsNavigation2;
import de.moddylp.AncientRegions.flags.FlagOBJ;
import de.moddylp.AncientRegions.flags.GamemodeFlag;
import de.moddylp.AncientRegions.flags.StateFlag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EditflagsPage2 {
    private Inventory menu;
    private Player p;
    private Main plugin;

    public EditflagsPage2(Player p, Main plugin) {
        this.plugin = plugin;
        this.p = p;
        this.menu = Bukkit.createInventory(null, 54, ChatColor.RED + plugin.lang.getText("EditFlags") + " 2");
    }

    private void loadMenuItems() {
        EditFlagsNavigation2 navigation = new EditFlagsNavigation2();
        navigation.loadguiitems(this.menu, this.plugin);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.PASSTHROUGH), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.INTERACT), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.BLOCK_BREAK), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.BLOCK_PLACE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.USE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.DAMAGE_ANIMALS), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.CHEST_ACCESS), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.RIDE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SLEEP), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.PLACE_VEHICLE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.DESTROY_VEHICLE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.LIGHTER), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.GHAST_FIREBALL), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.OTHER_EXPLOSION), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FIRE_SPREAD), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENDER_BUILD), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENTITY_PAINTING_DESTROY), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.MYCELIUM_SPREAD), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.VINE_GROWTH), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.SOIL_DRY), this.p, this.menu);
        GamemodeFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.GAME_MODE), this.p, this.menu);
        de.moddylp.AncientRegions.flags.BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.NOTIFY_ENTER), this.p, this.menu);
        de.moddylp.AncientRegions.flags.BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.NOTIFY_LEAVE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FIREWORK_DAMAGE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.WITHER_DAMAGE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.CHORUS_TELEPORT), this.p, this.menu);
        de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.EXIT_DENY_MESSAGE), this.p, this.menu);
        de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.ENTRY_DENY_MESSAGE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.EXIT_VIA_TELEPORT), this.p, this.menu);
        de.moddylp.AncientRegions.flags.BooleanFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.EXIT_OVERRIDE), this.p, this.menu);
        StateFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.FALL_DAMAGE), this.p, this.menu);
        de.moddylp.AncientRegions.flags.StringFlag.createandload(FlagOBJ.getFlagObj(DefaultFlag.DENY_MESSAGE), this.p, this.menu);
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

