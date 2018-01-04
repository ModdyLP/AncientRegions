package de.moddylp.AncientRegions.gui.Events;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.*;
import de.moddylp.AncientRegions.gui.Editflags;
import de.moddylp.AncientRegions.gui.EditflagsPage2;
import de.moddylp.AncientRegions.gui.Startgui;
import de.moddylp.AncientRegions.particle.ParticleShower;
import de.moddylp.AncientRegions.region.BuyRegionGUI;
import de.moddylp.AncientRegions.region.MembersGUI;
import de.moddylp.AncientRegions.region.Region;
import de.moddylp.AncientRegions.region.RegionManageGUI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Menu_Click
implements Listener {
    protected GUIEvents loader;
    private WorldGuardPlugin worldguard;
    private Main plugin;
    private WorldEditPlugin worldedit;

    Menu_Click(GUIEvents loaderClass, WorldGuardPlugin worldguard, Main plugin, WorldEditPlugin worldedit) {
        this.worldguard = worldguard;
        this.loader = loaderClass;
        this.plugin = plugin;
        this.worldedit = worldedit;
        Main.getInstance().getLogger().info("Events registered");
    }

    @EventHandler
    public void item_clicked(InventoryClickEvent e) {
        block140 : {
            Player p = (Player)e.getWhoClicked();
            if (e.getInventory().getName().equals(ChatColor.GOLD + "AncientRegions")) {
                if (e.getCurrentItem() != null) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100.0f, 100.0f);
                }
                Inventory menu = e.getInventory();
                try {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("EditFlagsItem"))) {
                        GUIOpener opener = new GUIOpener(this.loader);
                        opener.openeditflagsgui(p);
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("RegionManager"))) {
                        RegionManageGUI gui = new RegionManageGUI(p, this.plugin, this.worldguard);
                        gui.open();
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Particle")) {
                        ParticleShower show = new ParticleShower(this.plugin, menu);
                        show.toggle(p, this.worldguard);
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Info")) {
                        p.closeInventory();
                        RegionContainer container = this.worldguard.getRegionContainer();
                        RegionManager regions = container.get(p.getWorld());
                        Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
                        LocalPlayer ply = this.worldguard.wrapPlayer(p);
                        List<String> region = Objects.requireNonNull(regions).getApplicableRegionsIDs(pt);
                        if (region.isEmpty()) {
                            p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("GobalError"));
                        } else {
                            for (String regionid : region) {
                                ProtectedRegion rg = regions.getRegion(regionid);
                                if (rg != null && rg.getOwners().contains(ply) || p.hasPermission("ancient.flags.admin.bypass")) {
                                    p.sendMessage(ChatColor.BLUE + "============Region Info============");
                                    p.sendMessage(ChatColor.GOLD + "Regionname:       " + ChatColor.GREEN + Objects.requireNonNull(rg).getId().trim());
                                    p.sendMessage(ChatColor.GOLD + "Owner:            \n" + ChatColor.GREEN + this.playername(rg, "owner"));
                                    p.sendMessage(ChatColor.GOLD + "Member:           \n" + ChatColor.GREEN + this.playername(rg, "member"));
                                    p.sendMessage(ChatColor.GOLD + "Dimensions:       \n" + ChatColor.GREEN + rg.getPoints().toString().trim().replace("[", "").replace("]", ""));
                                    p.sendMessage(ChatColor.GOLD + "Flags:            \n" + ChatColor.GREEN + rg.getFlags().toString().replace("{", " ").replace("}", " ").replace("StateFlag", "").replace("EnumFlag", "").replace("DoubleFlag", "").replace("StateFlag", "").replace("IntegerFlag", "").replace("SetFlag", "").replace("LocationFlag", "").replace("BooleanFlag", "").replace("BuildFlag", "").replace("StringFlag", "").replace(",", "\n").replace("name=", ""));
                                    p.sendMessage(ChatColor.BLUE + "========================================");
                                    continue;
                                }
                                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Owner"));
                            }
                        }
                        e.setCancelled(true);
                        break block140;
                    }
                    e.setCancelled(true);
                }
                catch (Exception ex) {
                    e.setCancelled(true);
                }
            } else if (e.getInventory().getName().equals(ChatColor.RED + this.plugin.lang.getText("EditFlags") + " 1")) {
                Inventory menu = e.getInventory();
                if (e.getCurrentItem() != null) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100.0f, 100.0f);
                }
                try {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj( DefaultFlag.BUILD)).getName())) {
                        StateFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.BUILD)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj( DefaultFlag.GREET_MESSAGE)).getName())) {
                        de.moddylp.AncientRegions.flags.StringFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj( DefaultFlag.GREET_MESSAGE)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj( DefaultFlag.FAREWELL_MESSAGE)).getName())) {
                        de.moddylp.AncientRegions.flags.StringFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.FAREWELL_MESSAGE)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.PVP)).getName())) {
                        StateFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.PVP)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.MOB_DAMAGE)).getName())) {
                        StateFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.MOB_DAMAGE)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.ENTRY)).getName())) {
                        StateFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.ENTRY)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.EXIT)).getName())) {
                        StateFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.EXIT)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.ENDERPEARL)).getName())) {
                        StateFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.ENDERPEARL)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.TELE_LOC)).getName())) {
                        LocationFlagimpl.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.TELE_LOC)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.ITEM_PICKUP)).getName())) {
                        StateFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.ITEM_PICKUP)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.ITEM_DROP)).getName())) {
                        StateFlag.createandtoggle(Objects.requireNonNull(FlagOBJ.getFlagObj(DefaultFlag.ITEM_DROP)), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.EXP_DROPS).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.EXP_DROPS), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.MOB_SPAWNING).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.MOB_SPAWNING), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.CREEPER_EXPLOSION).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.CREEPER_EXPLOSION), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.TNT).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.TNT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.INVINCIBILITY).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.INVINCIBILITY), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.DENY_SPAWN).getName())) {
                        EntitySetFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.DENY_SPAWN), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.SNOW_MELT).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.SNOW_MELT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.SNOW_FALL).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.SNOW_FALL), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.ICE_FORM).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.ICE_FORM), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.ICE_MELT).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.ICE_MELT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.GRASS_SPREAD).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.GRASS_SPREAD), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.HEAL_DELAY).getName())) {
                        de.moddylp.AncientRegions.flags.IntegerFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.HEAL_DELAY), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.HEAL_AMOUNT).getName())) {
                        de.moddylp.AncientRegions.flags.IntegerFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.HEAL_AMOUNT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.MIN_HEAL).getName())) {
                        de.moddylp.AncientRegions.flags.DoubleFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.MIN_HEAL), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.MAX_HEAL).getName())) {
                        de.moddylp.AncientRegions.flags.DoubleFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.MAX_HEAL), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.BLOCKED_CMDS).getName())) {
                        de.moddylp.AncientRegions.flags.StringFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.BLOCKED_CMDS), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.ALLOWED_CMDS).getName())) {
                        de.moddylp.AncientRegions.flags.StringFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.ALLOWED_CMDS), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.TIME_LOCK).getName())) {
                        de.moddylp.AncientRegions.flags.StringFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.TIME_LOCK), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.WEATHER_LOCK).getName())) {
                        WeatherFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.WEATHER_LOCK), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.SPAWN_LOC).getName())) {
                        LocationFlagimpl.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.SPAWN_LOC), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.FEED_DELAY).getName())) {
                        de.moddylp.AncientRegions.flags.IntegerFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.FEED_DELAY), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.FEED_AMOUNT).getName())) {
                        de.moddylp.AncientRegions.flags.IntegerFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.FEED_AMOUNT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.MIN_FOOD).getName())) {
                        de.moddylp.AncientRegions.flags.IntegerFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.MIN_FOOD), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.MAX_FOOD).getName())) {
                        de.moddylp.AncientRegions.flags.IntegerFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.MAX_FOOD), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.LAVA_FLOW).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.LAVA_FLOW), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.WATER_FLOW).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.WATER_FLOW), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.LIGHTNING).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.LIGHTNING), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.LAVA_FIRE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.LAVA_FIRE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.MUSHROOMS).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.MUSHROOMS), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.PISTONS).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.PISTONS), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.SEND_CHAT).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.SEND_CHAT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.RECEIVE_CHAT).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.RECEIVE_CHAT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.POTION_SPLASH).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.POTION_SPLASH), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.LEAF_DECAY).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.LEAF_DECAY), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("Mainmenu"))) {
                        e.setCancelled(true);
                        p.closeInventory();
                        Startgui gui = new Startgui(p, this.plugin);
                        gui.open();
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("Next"))) {
                        p.closeInventory();
                        EditflagsPage2 page2 = new EditflagsPage2(p, this.plugin);
                        page2.open();
                        e.setCancelled(true);
                        break block140;
                    }
                    e.setCancelled(true);
                }
                catch (Exception ex) {
                    e.setCancelled(true);
                }
            } else if (e.getInventory().getName().equals(ChatColor.RED + this.plugin.lang.getText("EditFlags") + " 2")) {
                Inventory menu = e.getInventory();
                if (e.getCurrentItem() != null) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100.0f, 100.0f);
                }
                try {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("Back"))) {
                        p.closeInventory();
                        Editflags page1 = new Editflags(p, this.plugin);
                        page1.open();
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("Mainmenu"))) {
                        p.closeInventory();
                        Startgui start = new Startgui(p, this.plugin);
                        start.open();
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.PASSTHROUGH).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.PASSTHROUGH), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.BLOCK_BREAK).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.BLOCK_BREAK), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.BLOCK_PLACE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.BLOCK_PLACE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.INTERACT).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.INTERACT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.USE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.USE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.DAMAGE_ANIMALS).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.DAMAGE_ANIMALS), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.CHEST_ACCESS).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.CHEST_ACCESS), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.RIDE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.RIDE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.SLEEP).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.SLEEP), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.PLACE_VEHICLE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.PLACE_VEHICLE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.DESTROY_VEHICLE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.DESTROY_VEHICLE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.LIGHTER).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.LIGHTER), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.GHAST_FIREBALL).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.GHAST_FIREBALL), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.OTHER_EXPLOSION).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.OTHER_EXPLOSION), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.FIRE_SPREAD).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.FIRE_SPREAD), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.ENDER_BUILD).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.ENDER_BUILD), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.ENTITY_PAINTING_DESTROY).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.ENTITY_PAINTING_DESTROY), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.ENTITY_ITEM_FRAME_DESTROY), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.MYCELIUM_SPREAD).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.MYCELIUM_SPREAD), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.VINE_GROWTH).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.VINE_GROWTH), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.SOIL_DRY).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.SOIL_DRY), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.GAME_MODE).getName())) {
                        GamemodeFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.GAME_MODE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.NOTIFY_ENTER).getName())) {
                        de.moddylp.AncientRegions.flags.BooleanFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.NOTIFY_ENTER), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.NOTIFY_LEAVE).getName())) {
                        de.moddylp.AncientRegions.flags.BooleanFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.NOTIFY_LEAVE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.FIREWORK_DAMAGE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.FIREWORK_DAMAGE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.WITHER_DAMAGE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.WITHER_DAMAGE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.CHORUS_TELEPORT).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.CHORUS_TELEPORT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.EXIT_DENY_MESSAGE).getName())) {
                        de.moddylp.AncientRegions.flags.StringFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.EXIT_DENY_MESSAGE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.ENTRY_DENY_MESSAGE).getName())) {
                        de.moddylp.AncientRegions.flags.StringFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.ENTRY_DENY_MESSAGE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.EXIT_VIA_TELEPORT).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.EXIT_VIA_TELEPORT), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.EXIT_OVERRIDE).getName())) {
                        de.moddylp.AncientRegions.flags.BooleanFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.EXIT_OVERRIDE), p, menu, e);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(FlagOBJ.getFlagObj(DefaultFlag.FALL_DAMAGE).getName())) {
                        StateFlag.createandtoggle(FlagOBJ.getFlagObj(DefaultFlag.FALL_DAMAGE), p, menu, e);
                        break block140;
                    }
                    e.setCancelled(true);
                }
                catch (Exception ex) {
                    e.setCancelled(true);
                }
            } else if (e.getInventory().getName().equals(ChatColor.GOLD + this.plugin.lang.getText("RegionManager"))) {
                if (e.getCurrentItem() != null) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100.0f, 100.0f);
                }
                Inventory menu = e.getInventory();
                try {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("RegionBuy"))) {
                        p.closeInventory();
                        BuyRegionGUI gui = new BuyRegionGUI(p, this.plugin, this.worldguard);
                        gui.open();
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("Mainmenu"))) {
                        p.closeInventory();
                        Startgui gui = new Startgui(p, this.plugin);
                        gui.open();
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("AddMember"))) {
                        p.closeInventory();
                        MembersGUI gui = new MembersGUI(p, this.plugin, this.worldguard);
                        gui.addMember(e);
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("RemoveMember"))) {
                        p.closeInventory();
                        MembersGUI gui = new MembersGUI(p, this.plugin, this.worldguard);
                        gui.openregion();
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("SetOwner"))) {
                        p.closeInventory();
                        MembersGUI gui = new MembersGUI(p, this.plugin, this.worldguard);
                        gui.changeowner(e);
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("RemoveRegion"))) {
                        e.setCancelled(true);
                        Region region = new Region(this.plugin, 1);
                        region.removeRegion(this.worldguard, p, e, menu);
                        break block140;
                    }
                    e.setCancelled(true);
                }
                catch (Exception ex) {
                    e.setCancelled(true);
                }
            } else if (e.getInventory().getName().equals(ChatColor.GOLD + this.plugin.lang.getText("RemoveMember"))) {
                if (e.getCurrentItem() != null) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100.0f, 100.0f);
                }
                try {
                    String name = e.getCurrentItem().getItemMeta().getDisplayName();
                    if (!name.contains(this.plugin.lang.getText("Back")) && !name.contains(this.plugin.lang.getText("Mainmenu"))) {
                        MembersGUI gui = new MembersGUI(p, this.plugin, this.worldguard);
                        gui.removeMember(this.uuid(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())), e, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("Back"))) {
                        p.closeInventory();
                        RegionManageGUI gui = new RegionManageGUI(p, this.plugin, this.worldguard);
                        gui.open();
                        e.setCancelled(true);
                        break block140;
                    }
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("Mainmenu"))) {
                        p.closeInventory();
                        Startgui gui = new Startgui(p, this.plugin);
                        gui.open();
                        e.setCancelled(true);
                        break block140;
                    }
                    e.setCancelled(true);
                }
                catch (Exception ex) {
                    e.setCancelled(true);
                }
            } else if (e.getInventory().getName().equals(ChatColor.GOLD + this.plugin.lang.getText("RegionBuy"))) {
                if (e.getCurrentItem() != null) {
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100.0f, 100.0f);
                }
                try {
                    Inventory menu = e.getInventory();
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_region1name"))) {
                        Region region1 = new Region(this.plugin, 1);
                        region1.buy(this.worldguard, p, e, menu, this.worldedit);
                        e.setCancelled(true);
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_region2name"))) {
                        Region region2 = new Region(this.plugin, 2);
                        region2.buy(this.worldguard, p, e, menu, this.worldedit);
                        e.setCancelled(true);
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_region3name"))) {
                        Region region3 = new Region(this.plugin, 3);
                        region3.buy(this.worldguard, p, e, menu, this.worldedit);
                        e.setCancelled(true);
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_region4name"))) {
                        Region region4 = new Region(this.plugin, 4);
                        region4.buy(this.worldguard, p, e, menu, this.worldedit);
                        e.setCancelled(true);
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("Back"))) {
                        p.closeInventory();
                        RegionManageGUI gui = new RegionManageGUI(p, this.plugin, this.worldguard);
                        gui.open();
                        e.setCancelled(true);
                    } else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(this.plugin.lang.getText("Mainmenu"))) {
                        p.closeInventory();
                        Startgui gui = new Startgui(p, this.plugin);
                        gui.open();
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(true);
                    }
                }
                catch (Exception ex) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public String playername(ProtectedRegion rg, String mode) {
        Set<UUID> uuids = null;
        if (mode.equals("member")) {
            uuids = rg.getMembers().getUniqueIds();
        } else if (mode.equals("owner")) {
            uuids = rg.getOwners().getUniqueIds();
        }
        for (UUID uuid : Objects.requireNonNull(uuids)) {
            OfflinePlayer[] allplayers = this.plugin.getServer().getOfflinePlayers();
            for (int i = 0; allplayers.length >= i; ++i) {
                UUID uuidname = allplayers[i].getUniqueId();
                if (!uuid.equals(uuidname)) continue;
                return allplayers[i].getName() + "\n";
            }
        }
        return uuids.toString() + "\n";
    }

    public UUID uuid(String playername) {
        OfflinePlayer[] allplayers = this.plugin.getServer().getOfflinePlayers();
        for (int i = 0; allplayers.length >= i; ++i) {
            String uuidname = allplayers[i].getName();
            if (!playername.trim().equals(uuidname)) continue;
            return allplayers[i].getUniqueId();
        }
        return null;
    }
}

