package de.moddylp.AncientRegions.gui.Events;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.AllowShop;
import de.moddylp.AncientRegions.flags.AllowedCmds;
import de.moddylp.AncientRegions.flags.BlockBreak;
import de.moddylp.AncientRegions.flags.BlockPlace;
import de.moddylp.AncientRegions.flags.BlockedCmds;
import de.moddylp.AncientRegions.flags.BuildFlag;
import de.moddylp.AncientRegions.flags.Buyable;
import de.moddylp.AncientRegions.flags.ChestAccess;
import de.moddylp.AncientRegions.flags.CreeperExplosion;
import de.moddylp.AncientRegions.flags.DamageAnimals;
import de.moddylp.AncientRegions.flags.DenySpawn;
import de.moddylp.AncientRegions.flags.EnderDragonDamage;
import de.moddylp.AncientRegions.flags.EndermanGrief;
import de.moddylp.AncientRegions.flags.Enderpearl;
import de.moddylp.AncientRegions.flags.EntityItemframeDestroy;
import de.moddylp.AncientRegions.flags.EntityPaintingDestroy;
import de.moddylp.AncientRegions.flags.Entry;
import de.moddylp.AncientRegions.flags.Exit;
import de.moddylp.AncientRegions.flags.ExpDrop;
import de.moddylp.AncientRegions.flags.Farewell;
import de.moddylp.AncientRegions.flags.FeedAmount;
import de.moddylp.AncientRegions.flags.FeedDelay;
import de.moddylp.AncientRegions.flags.FeedMaxHunger;
import de.moddylp.AncientRegions.flags.FeedMinHunger;
import de.moddylp.AncientRegions.flags.FireSpread;
import de.moddylp.AncientRegions.flags.Gamemode;
import de.moddylp.AncientRegions.flags.GhastFireball;
import de.moddylp.AncientRegions.flags.GrassGrowth;
import de.moddylp.AncientRegions.flags.Greeting;
import de.moddylp.AncientRegions.flags.HealAmount;
import de.moddylp.AncientRegions.flags.HealDelay;
import de.moddylp.AncientRegions.flags.HealMaxHealth;
import de.moddylp.AncientRegions.flags.HealMinHealth;
import de.moddylp.AncientRegions.flags.IceForm;
import de.moddylp.AncientRegions.flags.IceMelt;
import de.moddylp.AncientRegions.flags.Interact;
import de.moddylp.AncientRegions.flags.Invisible;
import de.moddylp.AncientRegions.flags.ItemDrop;
import de.moddylp.AncientRegions.flags.ItemPickup;
import de.moddylp.AncientRegions.flags.LavaFire;
import de.moddylp.AncientRegions.flags.LavaFlow;
import de.moddylp.AncientRegions.flags.LeafDecay;
import de.moddylp.AncientRegions.flags.Lighter;
import de.moddylp.AncientRegions.flags.Lightning;
import de.moddylp.AncientRegions.flags.MobDamage;
import de.moddylp.AncientRegions.flags.MobSpawn;
import de.moddylp.AncientRegions.flags.MushroomGrowth;
import de.moddylp.AncientRegions.flags.MyceliumSpread;
import de.moddylp.AncientRegions.flags.NotifyEnter;
import de.moddylp.AncientRegions.flags.NotifyLeave;
import de.moddylp.AncientRegions.flags.OtherExplosion;
import de.moddylp.AncientRegions.flags.Pathtrought;
import de.moddylp.AncientRegions.flags.Piston;
import de.moddylp.AncientRegions.flags.PotionSplash;
import de.moddylp.AncientRegions.flags.Price;
import de.moddylp.AncientRegions.flags.PvP;
import de.moddylp.AncientRegions.flags.RecieveChat;
import de.moddylp.AncientRegions.flags.Ride;
import de.moddylp.AncientRegions.flags.SendChat;
import de.moddylp.AncientRegions.flags.Sleep;
import de.moddylp.AncientRegions.flags.Snowfall;
import de.moddylp.AncientRegions.flags.Snowmelt;
import de.moddylp.AncientRegions.flags.SoilDry;
import de.moddylp.AncientRegions.flags.SpawnLocation;
import de.moddylp.AncientRegions.flags.TNTExplosion;
import de.moddylp.AncientRegions.flags.Teleportlocation;
import de.moddylp.AncientRegions.flags.TimeLock;
import de.moddylp.AncientRegions.flags.Use;
import de.moddylp.AncientRegions.flags.VehicleDestroy;
import de.moddylp.AncientRegions.flags.VehiclePlace;
import de.moddylp.AncientRegions.flags.VineGrowth;
import de.moddylp.AncientRegions.flags.WaterFlow;
import de.moddylp.AncientRegions.flags.WeatherLock;
import de.moddylp.AncientRegions.gui.Editflags;
import de.moddylp.AncientRegions.gui.EditflagsPage2;
import de.moddylp.AncientRegions.gui.Startgui;
import de.moddylp.AncientRegions.loader.LoadConfig;
import de.moddylp.AncientRegions.particle.ParticleShower;
import de.moddylp.AncientRegions.region.BuyRegionGUI;
import de.moddylp.AncientRegions.region.MembersGUI;
import de.moddylp.AncientRegions.region.Region;
import de.moddylp.AncientRegions.region.RegionManageGUI;

public class Menu_Click implements Listener {
	protected GUIEvents loader;
	private WorldGuardPlugin worldguard;
	private Main plugin;
	private WorldEditPlugin worldedit;

	public Menu_Click(GUIEvents loaderClass, WorldGuardPlugin worldguard, Main plugin, WorldEditPlugin worldedit) {
		this.worldguard = worldguard;
		this.loader = loaderClass;
		this.plugin = plugin;
		this.worldedit = worldedit;
		plugin.getLogger().info("Events registered");
	}

	@EventHandler
	public void item_clicked(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getName().equals(ChatColor.GOLD + "AncientRegions")) {
			if (e.getCurrentItem() != null) {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100, 100);
			}
			Inventory menu = e.getInventory();
			try {
				// Startgui Item Click Check
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains(plugin.lang.getText("EditFlagsItem"))) {
					// Open new GUI for editing Flags
					GUIOpener opener = new GUIOpener(loader, worldguard);
					opener.openeditflagsgui(p);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("RegionManager"))) {
					p.closeInventory();
					RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Particle")) {
					ParticleShower show = new ParticleShower(plugin, menu);
					show.toggle(p, worldguard);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Info")) {
					RegionContainer container = worldguard.getRegionContainer();
					RegionManager regions = container.get(p.getWorld());
					Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
					LocalPlayer ply = worldguard.wrapPlayer(p);
					List<String> region = regions.getApplicableRegionsIDs(pt);
					if (region.isEmpty()) {

						p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("GobalError"));
					} else {
						ProtectedRegion rg = regions.getRegion(region.get(0));
						if (rg.getOwners().contains(ply) || p.hasPermission("ancient.flags.admin.bypass")) {
							p.sendMessage(ChatColor.BLUE + "============Region Info============");
							p.sendMessage(ChatColor.GOLD + "Regionname:       " + ChatColor.GREEN + rg.getId().trim());
							p.sendMessage(ChatColor.GOLD + "Owner:            \n" + ChatColor.GREEN
									+ playername(rg, "owner"));
							p.sendMessage(ChatColor.GOLD + "Member:           \n" + ChatColor.GREEN
									+ playername(rg, "member"));
							p.sendMessage(ChatColor.GOLD + "Dimensions:       \n" + ChatColor.GREEN
									+ rg.getPoints().toString().trim().replace("[", "").replace("]", ""));
							p.sendMessage(ChatColor.GOLD + "Flags:            \n" + ChatColor.GREEN
									+ rg.getFlags().toString().replace("{", " ").replace("}", " ")
											.replace("StateFlag", "").replace("EnumFlag", "").replace("DoubleFlag", "")
											.replace("BooleanFlag", "").replace("IntegerFlag", "")
											.replace("SetFlag", "").replace("LocationFlag", "").replace("BuildFlag", "")
											.replace("StringFlag", "").replace(",", "\n").replace("name=", ""));
							p.sendMessage(ChatColor.BLUE + "========================================");
						} else {
							p.sendMessage(ChatColor.RED+ "[AR][ERROR] "+plugin.lang.getText("Owner"));
						}
					}
					e.setCancelled(true);
				} else {
					e.setCancelled(true);
				}

			} catch (Exception ex) {
				e.setCancelled(true);
			}
		} else if (e.getInventory().getName().equals(ChatColor.RED + plugin.lang.getText("EditFlags") + " 1")) {
			Inventory menu = e.getInventory();
			if (e.getCurrentItem() != null) {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100, 100);
			}
			try {
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Build")) {
					BuildFlag buildflag = new BuildFlag(plugin);
					buildflag.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Greeting")) {
					Greeting greeting = new Greeting(plugin);
					greeting.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Farewell")) {
					Farewell farewell = new Farewell(plugin);
					farewell.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("PvP")) {
					PvP pvp = new PvP(plugin);
					pvp.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("MobDamage")) {
					MobDamage mobdamage = new MobDamage(plugin);
					mobdamage.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Entry")) {
					Entry entry = new Entry(plugin);
					entry.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Exit")) {
					Exit exit = new Exit(plugin);
					exit.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Enderpearl")) {
					Enderpearl enderpearl = new Enderpearl(plugin);
					enderpearl.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Teleportlocation")) {
					Teleportlocation teleloc = new Teleportlocation(plugin);
					teleloc.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("ItemPickup")) {
					ItemPickup itempickup = new ItemPickup(plugin);
					itempickup.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("ItemDrop")) {
					ItemDrop itemdrop = new ItemDrop(plugin);
					itemdrop.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("ExpDrop")) {
					ExpDrop expdrop = new ExpDrop(plugin);
					expdrop.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("MobSpawn")) {
					MobSpawn mobspawn = new MobSpawn(plugin);
					mobspawn.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("CreeperExplosion")) {
					CreeperExplosion creeperexplosion = new CreeperExplosion(plugin);
					creeperexplosion.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("TNTExplosion")) {
					TNTExplosion tntexplosion = new TNTExplosion(plugin);
					tntexplosion.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Invisible")) {
					Invisible invisible = new Invisible(plugin);
					invisible.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("EnderDragonDamage")) {
					EnderDragonDamage enderdragondamage = new EnderDragonDamage(plugin);
					enderdragondamage.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("DenySpawn")) {
					DenySpawn denyspawn = new DenySpawn(plugin, worldguard, p);
					denyspawn.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Snowfall")) {
					Snowfall snowfall = new Snowfall(plugin);
					snowfall.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Snowmelt")) {
					Snowmelt snowmelt = new Snowmelt(plugin);
					snowmelt.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("IceForm")) {
					IceForm iceform = new IceForm(plugin);
					iceform.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("IceMelt")) {
					IceMelt icemelt = new IceMelt(plugin);
					icemelt.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("GrassGrowth")) {
					GrassGrowth grassgrowth = new GrassGrowth(plugin);
					grassgrowth.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("HealDelay")) {
					HealDelay healdelay = new HealDelay(plugin, worldguard, p);
					healdelay.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("HealAmount")) {
					HealAmount healamount = new HealAmount(plugin, worldguard, p);
					healamount.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("HealMinHealth")) {
					HealMinHealth healminhealth = new HealMinHealth(plugin, worldguard, p);
					healminhealth.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("HealMaxHealth")) {
					HealMaxHealth healMaxhealth = new HealMaxHealth(plugin, worldguard, p);
					healMaxhealth.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("BlockedCmds")) {
					BlockedCmds blockcmd = new BlockedCmds(plugin, worldguard, p);
					blockcmd.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("AllowedCmds")) {
					AllowedCmds allowcmd = new AllowedCmds(plugin, worldguard, p);
					allowcmd.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("TimeLock")) {
					TimeLock timelock = new TimeLock(plugin);
					timelock.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("WeatherLock")) {
					WeatherLock weather = new WeatherLock(plugin);
					weather.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("SpawnLocation")) {
					SpawnLocation spawnlocation = new SpawnLocation(plugin);
					spawnlocation.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("FeedDelay")) {
					FeedDelay Feeddelay = new FeedDelay(plugin, worldguard, p);
					Feeddelay.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("FeedAmount")) {
					FeedAmount Feedamount = new FeedAmount(plugin, worldguard, p);
					Feedamount.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("FeedMinHunger")) {
					FeedMinHunger FeedminFeedth = new FeedMinHunger(plugin, worldguard, p);
					FeedminFeedth.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("FeedMaxHunger")) {
					FeedMaxHunger FeedMaxFeedth = new FeedMaxHunger(plugin, worldguard, p);
					FeedMaxFeedth.toggle(e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Lavaflow")) {
					LavaFlow lavaflow = new LavaFlow(plugin);
					lavaflow.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Waterflow")) {
					WaterFlow waterflow = new WaterFlow(plugin);
					waterflow.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Lightning")) {
					Lightning lightning = new Lightning(plugin);
					lightning.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("LavaFire")) {
					LavaFire lavafire = new LavaFire(plugin);
					lavafire.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("MushroomGrowth")) {
					MushroomGrowth mushroom = new MushroomGrowth(plugin);
					mushroom.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Pistons")) {
					Piston piston = new Piston(plugin);
					piston.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("SendChat")) {
					SendChat senchat = new SendChat(plugin);
					senchat.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("RecieveChat")) {
					RecieveChat recieve = new RecieveChat(plugin);
					recieve.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("PotionSplash")) {
					PotionSplash potion = new PotionSplash(plugin);
					potion.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("LeafDecay")) {
					LeafDecay leafdecay = new LeafDecay(plugin);
					leafdecay.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("Mainmenu"))) {
					e.setCancelled(true);
					p.closeInventory();
					Startgui gui = new Startgui(p, plugin);
					gui.open();
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(plugin.lang.getText("Next"))) {
					p.closeInventory();
					EditflagsPage2 page2 = new EditflagsPage2(p, plugin, worldguard);
					page2.open();
					e.setCancelled(true);
				} else {
					e.setCancelled(true);
				}
			} catch (Exception ex) {
				e.setCancelled(true);
			}
		} else if (e.getInventory().getName().equals(ChatColor.RED + plugin.lang.getText("EditFlags") + " 2")) {
			Inventory menu = e.getInventory();
			if (e.getCurrentItem() != null) {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100, 100);
			}
			try {
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains(plugin.lang.getText("Back"))) {
					p.closeInventory();
					Editflags page1 = new Editflags(p, plugin, worldguard);
					page1.open();
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("Mainmenu"))) {
					p.closeInventory();
					Startgui start = new Startgui(p, plugin);
					start.open();
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Passthrough")) {
					Pathtrought paththrought = new Pathtrought(plugin);
					paththrought.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("BlockBreak")) {
					BlockBreak blockbreak = new BlockBreak(plugin);
					blockbreak.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("BlockPlace")) {
					BlockPlace blockplace = new BlockPlace(plugin);
					blockplace.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Interact")) {
					Interact interact = new Interact(plugin);
					interact.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Use")) {
					Use use = new Use(plugin);
					use.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("DamageAnimals")) {
					DamageAnimals damageanimals = new DamageAnimals(plugin);
					damageanimals.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("ChestAccess")) {
					ChestAccess chestaccess = new ChestAccess(plugin);
					chestaccess.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Ride")) {
					Ride ride = new Ride(plugin);
					ride.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Sleep")) {
					Sleep sleep = new Sleep(plugin);
					sleep.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("VehiclePlace")) {
					VehiclePlace vehicleplace = new VehiclePlace(plugin);
					vehicleplace.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("VehicleDestroy")) {
					VehicleDestroy vehicledestroy = new VehicleDestroy(plugin);
					vehicledestroy.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Lighter")) {
					Lighter lighter = new Lighter(plugin);
					lighter.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("GhastFireball")) {
					GhastFireball ghastfireball = new GhastFireball(plugin);
					ghastfireball.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("OtherExplosion")) {
					OtherExplosion otherexplosion = new OtherExplosion(plugin);
					otherexplosion.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("FireSpread")) {
					FireSpread firespread = new FireSpread(plugin);
					firespread.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("EndermanGrief")) {
					EndermanGrief endermangrief = new EndermanGrief(plugin);
					endermangrief.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("EntityPaintingDestroy")) {
					EntityPaintingDestroy entitypaintingdetroy = new EntityPaintingDestroy(plugin);
					entitypaintingdetroy.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("EntityItemframeDestroy")) {
					EntityItemframeDestroy entityitemframedestroy = new EntityItemframeDestroy(plugin);
					entityitemframedestroy.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("MyceliumSpread")) {
					MyceliumSpread myceliumspread = new MyceliumSpread(plugin);
					myceliumspread.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("VineGrowth")) {
					VineGrowth vinegrowth = new VineGrowth(plugin);
					vinegrowth.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("SoilDry")) {
					SoilDry soildry = new SoilDry(plugin);
					soildry.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Gamemode")) {
					Gamemode gamemode = new Gamemode(plugin);
					gamemode.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("NotifyEnter")) {
					NotifyEnter notifyenter = new NotifyEnter(plugin);
					notifyenter.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("NotifyLeave")) {
					NotifyLeave notfyleave = new NotifyLeave(plugin);
					notfyleave.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("AllowShop")) {
					AllowShop allowshop = new AllowShop(plugin);
					allowshop.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Buyable")) {
					Buyable buyable = new Buyable(plugin);
					buyable.toggle(worldguard, p, e, menu);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Price")) {
					Price price = new Price(plugin, worldguard, p);
					price.toggle(e, menu);
					e.setCancelled(true);
				} else {
					e.setCancelled(true);
				}
			} catch (Exception ex) {
				e.setCancelled(true);
			}
		} else if (e.getInventory().getName().equals(ChatColor.GOLD + plugin.lang.getText("RegionManager"))) {
			if (e.getCurrentItem() != null) {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100, 100);
			}
			Inventory menu = e.getInventory();
			try {
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains(plugin.lang.getText("RegionBuy"))) {
					p.closeInventory();
					BuyRegionGUI gui = new BuyRegionGUI(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("Mainmenu"))) {
					p.closeInventory();
					Startgui gui = new Startgui(p, plugin);
					gui.open();
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("AddMember"))) {
					p.closeInventory();
					MembersGUI gui = new MembersGUI(p, plugin, worldguard);
					gui.addMember(e);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("RemoveMember"))) {
					p.closeInventory();
					MembersGUI gui = new MembersGUI(p, plugin, worldguard);
					gui.openregion();
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("SetOwner"))) {
					p.closeInventory();
					MembersGUI gui = new MembersGUI(p, plugin, worldguard);
					gui.changeowner(e);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("RemoveRegion"))) {
					Region region = new Region(plugin, 1);
					region.removeRegion(worldguard, p, e, menu);;
					e.setCancelled(true);
				} else {
					e.setCancelled(true);
				}
			} catch (Exception ex) {
				e.setCancelled(true);
			}
		} else if (e.getInventory().getName().equals(ChatColor.GOLD + plugin.lang.getText("RemoveMember"))) {
			if (e.getCurrentItem() != null) {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100, 100);
			}
			try {
				String name = e.getCurrentItem().getItemMeta().getDisplayName();
				if (!name.contains(plugin.lang.getText("Back")) && !name.contains(plugin.lang.getText("Mainmenu"))) {
					MembersGUI gui = new MembersGUI(p, plugin, worldguard);
					gui.removeMember(uuid(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())), e,
							ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(plugin.lang.getText("Back"))) {
					p.closeInventory();
					RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("Mainmenu"))) {
					p.closeInventory();
					Startgui gui = new Startgui(p, plugin);
					gui.open();
					e.setCancelled(true);
				} else {
					e.setCancelled(true);
				}
			} catch (Exception ex) {
				e.setCancelled(true);
			}
		} else if (e.getInventory().getName().equals(ChatColor.GOLD + plugin.lang.getText("RegionBuy"))) {
			if (e.getCurrentItem() != null) {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 100, 100);
			}
			try {
				LoadConfig config = new LoadConfig(plugin);
				Inventory menu = e.getInventory();
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains(config.getOption("region1name"))) {
					Region region1 = new Region(plugin, 1);
					region1.buy(worldguard, p, e, menu, worldedit);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(config.getOption("region2name"))) {
					Region region2 = new Region(plugin, 2);
					region2.buy(worldguard, p, e, menu, worldedit);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(config.getOption("region3name"))) {
					Region region3 = new Region(plugin, 3);
					region3.buy(worldguard, p, e, menu, worldedit);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(config.getOption("region4name"))) {
					Region region4 = new Region(plugin, 4);
					region4.buy(worldguard, p, e, menu, worldedit);
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName().contains(plugin.lang.getText("Back"))) {
					p.closeInventory();
					RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
					gui.open();
					e.setCancelled(true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.contains(plugin.lang.getText("Mainmenu"))) {
					p.closeInventory();
					Startgui gui = new Startgui(p, plugin);
					gui.open();
					e.setCancelled(true);
				} else {
					e.setCancelled(true);
				}
			} catch (Exception ex) {
				e.setCancelled(true);
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
		for (UUID uuid : uuids) {
			OfflinePlayer[] allplayers = plugin.getServer().getOfflinePlayers();
			for (int i = 0; allplayers.length >= i; i++) {
				UUID uuidname = allplayers[i].getUniqueId();
				if (uuid.equals(uuidname)) {
					return allplayers[i].getName() + "\n";
				}

			}
		}
		return uuids.toString() + "\n";
	}

	public UUID uuid(String playername) {
		OfflinePlayer[] allplayers = plugin.getServer().getOfflinePlayers();

		for (int i = 0; allplayers.length >= i; i++) {
			String uuidname = allplayers[i].getName();
			if (playername.trim().equals(uuidname)) {
				return allplayers[i].getUniqueId();
			}

		}
		return null;
	}
}
