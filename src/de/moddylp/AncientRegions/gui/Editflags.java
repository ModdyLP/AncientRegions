package de.moddylp.AncientRegions.gui;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import de.moddylp.AncientRegions.flags.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.moddylp.AncientRegions.Main;

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
		FlagOBJ buildflag = new FlagOBJ("Build", "", DefaultFlag.BUILD, Material.STONE, 0);
		BooleanFlag buildbolflag = new BooleanFlag(buildflag, p);
		buildbolflag.loadgui(menu);
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
		FlagOBJ blockedcmdsflag = new FlagOBJ("BlockedCmds", "", DefaultFlag.BLOCKED_CMDS, Material.COMMAND_REPEATING, 28);
		StringFlag blockedcmds = new StringFlag(blockedcmdsflag, p);
		blockedcmds.loadgui(menu);
		FlagOBJ alloedcmdsflag = new FlagOBJ("AllowedCmds", "", DefaultFlag.ALLOWED_CMDS,Material.COMMAND_CHAIN, 27);
		StringFlag alloedcmds = new StringFlag(alloedcmdsflag, p);
		alloedcmds.loadgui(menu);
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
