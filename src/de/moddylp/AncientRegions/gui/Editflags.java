package de.moddylp.AncientRegions.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.AllowedCmds;
import de.moddylp.AncientRegions.flags.BlockedCmds;
import de.moddylp.AncientRegions.flags.BuildFlag;
import de.moddylp.AncientRegions.flags.CreeperExplosion;
import de.moddylp.AncientRegions.flags.DenySpawn;
import de.moddylp.AncientRegions.flags.EditFlagsNavigation;
import de.moddylp.AncientRegions.flags.EnderDragonDamage;
import de.moddylp.AncientRegions.flags.EnderPearlTeleport;
import de.moddylp.AncientRegions.flags.Entry;
import de.moddylp.AncientRegions.flags.Exit;
import de.moddylp.AncientRegions.flags.ExpDrop;
import de.moddylp.AncientRegions.flags.Farewell;
import de.moddylp.AncientRegions.flags.FeedAmount;
import de.moddylp.AncientRegions.flags.FeedDelay;
import de.moddylp.AncientRegions.flags.FeedMaxHunger;
import de.moddylp.AncientRegions.flags.FeedMinHunger;
import de.moddylp.AncientRegions.flags.GrassGrowth;
import de.moddylp.AncientRegions.flags.Greeting;
import de.moddylp.AncientRegions.flags.HealAmount;
import de.moddylp.AncientRegions.flags.HealDelay;
import de.moddylp.AncientRegions.flags.HealMaxHealth;
import de.moddylp.AncientRegions.flags.HealMinHealth;
import de.moddylp.AncientRegions.flags.IceForm;
import de.moddylp.AncientRegions.flags.IceMelt;
import de.moddylp.AncientRegions.flags.Invisible;
import de.moddylp.AncientRegions.flags.ItemDrop;
import de.moddylp.AncientRegions.flags.ItemPickup;
import de.moddylp.AncientRegions.flags.LavaFire;
import de.moddylp.AncientRegions.flags.LavaFlow;
import de.moddylp.AncientRegions.flags.LeafDecay;
import de.moddylp.AncientRegions.flags.Lightning;
import de.moddylp.AncientRegions.flags.MobDamage;
import de.moddylp.AncientRegions.flags.MobSpawn;
import de.moddylp.AncientRegions.flags.MushroomGrowth;
import de.moddylp.AncientRegions.flags.Piston;
import de.moddylp.AncientRegions.flags.PotionSplash;
import de.moddylp.AncientRegions.flags.PvP;
import de.moddylp.AncientRegions.flags.RecieveChat;
import de.moddylp.AncientRegions.flags.SendChat;
import de.moddylp.AncientRegions.flags.Snowfall;
import de.moddylp.AncientRegions.flags.Snowmelt;
import de.moddylp.AncientRegions.flags.SpawnLocation;
import de.moddylp.AncientRegions.flags.TNTExplosion;
import de.moddylp.AncientRegions.flags.Teleportlocation;
import de.moddylp.AncientRegions.flags.TimeLock;
import de.moddylp.AncientRegions.flags.WaterFlow;
import de.moddylp.AncientRegions.flags.WeatherLock;

public class Editflags {
	private Inventory menu;
	private Player p;
	private Main plugin;
	private WorldGuardPlugin worldguard;

	public Editflags(Player p, Main plugin, WorldGuardPlugin worldguard) {
		this.plugin = plugin;
		this.p = p;
		this.menu = Bukkit.createInventory(null, 54, ChatColor.RED + plugin.lang.getText("EditFlags") + " 1");
		this.worldguard = worldguard;

	}

	// Load the menu items/icons
	private void loadMenuItems() {
		EditFlagsNavigation navigation = new EditFlagsNavigation();
		navigation.loadguiitems(menu, plugin);
		BuildFlag buildflag = new BuildFlag(plugin);
		buildflag.loadgui(menu, p, worldguard);
		Greeting greeting = new Greeting(plugin);
		greeting.loadgui(menu, p, worldguard);
		Farewell farewell = new Farewell(plugin);
		farewell.loadgui(menu, p, worldguard);
		PvP pvp = new PvP(plugin);
		pvp.loadgui(menu, p, worldguard);
		MobDamage mobdamage = new MobDamage(plugin);
		mobdamage.loadgui(menu, p, worldguard);
		Entry entry = new Entry(plugin);
		entry.loadgui(menu, p, worldguard);
		Exit exit = new Exit(plugin);
		exit.loadgui(menu, p, worldguard);
		EnderPearlTeleport enderpearl = new EnderPearlTeleport(plugin);
		enderpearl.loadgui(menu, p, worldguard);
		Teleportlocation teleloc = new Teleportlocation(plugin);
		teleloc.loadgui(menu, p, worldguard);
		ItemPickup itempickup = new ItemPickup(plugin);
		itempickup.loadgui(menu, p, worldguard);
		ItemDrop itemdrop = new ItemDrop(plugin);
		itemdrop.loadgui(menu, p, worldguard);
		ExpDrop expdrop = new ExpDrop(plugin);
		expdrop.loadgui(menu, p, worldguard);
		MobSpawn mobspawn = new MobSpawn(plugin);
		mobspawn.loadgui(menu, p, worldguard);
		CreeperExplosion creeperexplosion = new CreeperExplosion(plugin);
		creeperexplosion.loadgui(menu, p, worldguard);
		TNTExplosion tntexplosion = new TNTExplosion(plugin);
		tntexplosion.loadgui(menu, p, worldguard);
		Invisible invisible = new Invisible(plugin);
		invisible.loadgui(menu, p, worldguard);
		EnderDragonDamage enderdragondamage = new EnderDragonDamage(plugin);
		enderdragondamage.loadgui(menu, p, worldguard);
		DenySpawn denyspawn = new DenySpawn(plugin, worldguard, p);
		denyspawn.loadgui(menu, p, worldguard);
		Snowfall snowfall = new Snowfall(plugin);
		snowfall.loadgui(menu, p, worldguard);
		Snowmelt snowmelt = new Snowmelt(plugin);
		snowmelt.loadgui(menu, p, worldguard);
		IceForm iceform = new IceForm(plugin);
		iceform.loadgui(menu, p, worldguard);
		IceMelt icemelt = new IceMelt(plugin);
		icemelt.loadgui(menu, p, worldguard);
		GrassGrowth grassgrowth = new GrassGrowth(plugin);
		grassgrowth.loadgui(menu, p, worldguard);
		HealDelay healdelay = new HealDelay(plugin, worldguard, p);
		healdelay.loadgui(menu, p, worldguard);
		HealAmount healamount = new HealAmount(plugin, worldguard, p);
		healamount.loadgui(menu, p, worldguard);
		HealMinHealth healminheal = new HealMinHealth(plugin, worldguard, p);
		healminheal.loadgui(menu, p, worldguard);
		HealMaxHealth healmaxheal = new HealMaxHealth(plugin, worldguard, p);
		healmaxheal.loadgui(menu, p, worldguard);
		BlockedCmds blockedcmds = new BlockedCmds(plugin, worldguard, p);
		blockedcmds.loadgui(menu, p, worldguard);
		AllowedCmds allowcmd = new AllowedCmds(plugin, worldguard, p);
		allowcmd.loadgui(menu, p, worldguard);
		TimeLock timelock = new TimeLock(plugin);
		timelock.loadgui(menu, p, worldguard);
		WeatherLock weather = new WeatherLock(plugin);
		weather.loadgui(menu, p, worldguard);
		SpawnLocation spawnlocation = new SpawnLocation(plugin);
		spawnlocation.loadgui(menu, p, worldguard);
		FeedDelay Feeddelay = new FeedDelay(plugin, worldguard, p);
		Feeddelay.loadgui(menu, p, worldguard);
		FeedAmount Feedamount = new FeedAmount(plugin, worldguard, p);
		Feedamount.loadgui(menu, p, worldguard);
		FeedMinHunger FeedminFeed = new FeedMinHunger(plugin, worldguard, p);
		FeedminFeed.loadgui(menu, p, worldguard);
		FeedMaxHunger FeedmaxFeed = new FeedMaxHunger(plugin, worldguard, p);
		FeedmaxFeed.loadgui(menu, p, worldguard);
		LavaFlow lavaflow = new LavaFlow(plugin);
		lavaflow.loadgui(menu, p, worldguard);
		WaterFlow waterflow = new WaterFlow(plugin);
		waterflow.loadgui(menu, p, worldguard);
		Lightning lightning = new Lightning(plugin);
		lightning.loadgui(menu, p, worldguard);
		LavaFire lavafire = new LavaFire(plugin);
		lavafire.loadgui(menu, p, worldguard);
		MushroomGrowth mushroom = new MushroomGrowth(plugin);
		mushroom.loadgui(menu, p, worldguard);
		Piston piston = new Piston(plugin);
		piston.loadgui(menu, p, worldguard);
		SendChat senchat = new SendChat(plugin);
		senchat.loadgui(menu, p, worldguard);
		RecieveChat recieve = new RecieveChat(plugin);
		recieve.loadgui(menu, p, worldguard);
		PotionSplash potion = new PotionSplash(plugin);
		potion.loadgui(menu, p, worldguard);
		LeafDecay leafdecay = new LeafDecay(plugin);
		leafdecay.loadgui(menu, p, worldguard); // 51
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
