package de.moddylp.AncientRegions.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.AllowShop;
import de.moddylp.AncientRegions.flags.BlockBreak;
import de.moddylp.AncientRegions.flags.BlockPlace;
import de.moddylp.AncientRegions.flags.Buyable;
import de.moddylp.AncientRegions.flags.ChestAccess;
import de.moddylp.AncientRegions.flags.DamageAnimals;
import de.moddylp.AncientRegions.flags.EditFlagsNavigation2;
import de.moddylp.AncientRegions.flags.EndermanGrief;
import de.moddylp.AncientRegions.flags.EntityItemframeDestroy;
import de.moddylp.AncientRegions.flags.EntityPaintingDestroy;
import de.moddylp.AncientRegions.flags.FireSpread;
import de.moddylp.AncientRegions.flags.Gamemode;
import de.moddylp.AncientRegions.flags.GhastFireball;
import de.moddylp.AncientRegions.flags.Interact;
import de.moddylp.AncientRegions.flags.Lighter;
import de.moddylp.AncientRegions.flags.MyceliumSpread;
import de.moddylp.AncientRegions.flags.NotifyEnter;
import de.moddylp.AncientRegions.flags.NotifyLeave;
import de.moddylp.AncientRegions.flags.OtherExplosion;
import de.moddylp.AncientRegions.flags.Pathtrought;
import de.moddylp.AncientRegions.flags.Price;
import de.moddylp.AncientRegions.flags.Ride;
import de.moddylp.AncientRegions.flags.Sleep;
import de.moddylp.AncientRegions.flags.SoilDry;
import de.moddylp.AncientRegions.flags.Use;
import de.moddylp.AncientRegions.flags.VehicleDestroy;
import de.moddylp.AncientRegions.flags.VehiclePlace;
import de.moddylp.AncientRegions.flags.VineGrowth;

public class EditflagsPage2 {
	private Inventory menu;
	private Player p;
	private Main plugin;
	private WorldGuardPlugin worldguard;

	public EditflagsPage2(Player p, Main plugin, WorldGuardPlugin worldguard)
    {
        this.plugin = plugin;
        this.p = p;
        this.menu = Bukkit.createInventory(null, 54, ChatColor.RED+plugin.lang.getText("EditFlags")+" 2");
        this.worldguard = worldguard;
               
    }
    //Load the menu items/icons
	private void loadMenuItems()
    {
		EditFlagsNavigation2 navigation = new EditFlagsNavigation2();
		navigation.loadguiitems(menu, plugin);
		Pathtrought pathtrought = new Pathtrought(plugin);
		pathtrought.loadgui(menu, p, worldguard);
		Interact interact = new Interact(plugin);
		interact.loadgui(menu, p, worldguard);
		BlockBreak blockbreak = new BlockBreak(plugin);
		blockbreak.loadgui(menu, p, worldguard);
		BlockPlace blockplace = new BlockPlace(plugin);
		blockplace.loadgui(menu, p, worldguard);
		Use use = new Use(plugin);
		use.loadgui(menu, p, worldguard);
		DamageAnimals damageanimals = new DamageAnimals(plugin);
		damageanimals.loadgui(menu, p, worldguard);
		ChestAccess chestaccess = new ChestAccess(plugin);
		chestaccess.loadgui(menu, p, worldguard);
		Ride ride = new Ride(plugin);
		ride.loadgui(menu, p, worldguard);
		Sleep sleep = new Sleep(plugin);
		sleep.loadgui(menu, p, worldguard);
		VehiclePlace vehicleplace = new VehiclePlace(plugin);
		vehicleplace.loadgui(menu, p, worldguard);
		VehicleDestroy vehicledestroy = new VehicleDestroy(plugin);
		vehicledestroy.loadgui(menu, p, worldguard);
		Lighter lighter = new Lighter(plugin);
		lighter.loadgui(menu, p, worldguard);
		GhastFireball ghastfireball = new GhastFireball(plugin);
		ghastfireball.loadgui(menu, p, worldguard);
		OtherExplosion otherexplosion = new OtherExplosion(plugin);
		otherexplosion.loadgui(menu, p, worldguard);
		FireSpread firespread = new FireSpread(plugin);
		firespread.loadgui(menu, p, worldguard);
        EndermanGrief endermangrief = new EndermanGrief(plugin);
        endermangrief.loadgui(menu, p, worldguard);
        EntityPaintingDestroy entitypaintingdetroy = new EntityPaintingDestroy(plugin);
        entitypaintingdetroy.loadgui(menu, p, worldguard);
        EntityItemframeDestroy entityitemframedestroy = new EntityItemframeDestroy(plugin);
        entityitemframedestroy.loadgui(menu, p, worldguard);
        MyceliumSpread myceliumspread = new MyceliumSpread(plugin);
        myceliumspread.loadgui(menu, p, worldguard);
        VineGrowth vinegrowth = new VineGrowth(plugin);
        vinegrowth.loadgui(menu, p, worldguard);
        SoilDry soildry = new SoilDry(plugin);
        soildry.loadgui(menu, p, worldguard);
        Gamemode gamemode = new Gamemode(plugin);
        gamemode.loadgui(menu, p, worldguard);
        NotifyEnter notifyenter = new NotifyEnter(plugin);
        notifyenter.loadgui(menu, p, worldguard);
        NotifyLeave notfyleave = new NotifyLeave(plugin);
		notfyleave.loadgui(menu, p, worldguard);
		AllowShop allowshop = new AllowShop(plugin);
		allowshop.loadgui(menu, p, worldguard);
		Buyable buyable = new Buyable(plugin);
		buyable.loadgui(menu, p, worldguard);
		Price price = new Price(plugin, worldguard, p);
		price.loadgui(menu, p, worldguard);
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
