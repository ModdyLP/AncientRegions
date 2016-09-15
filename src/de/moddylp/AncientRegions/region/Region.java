package de.moddylp.AncientRegions.region;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.LoadConfig;
import net.milkbowl.vault.economy.Economy;

public class Region {
	// Flag Description
	private Main plugin;
	String region;
	String permission;
	private LoadConfig config;
	private String regionname;
	private String regionprice;
	private String regionsize;
	private int number;

	public Region(Main plugin, int number) {
		this.plugin = plugin;
		this.region = "region" + number;
		this.number = number;
		config = new LoadConfig(plugin);
		this.permission = "buy" + region.toLowerCase();
		this.regionname = config.getOption(region + "name");
		this.regionprice = config.getOption(region + "price");
		this.regionsize = config.getOption(region + "size");

	}

	public boolean buy(WorldGuardPlugin worldguard, Player p, InventoryClickEvent e, Inventory menu, WorldEditPlugin worldedit) {

		if (p.hasPermission("ancient.regions.region." + permission)) {
				RegionContainer container = worldguard.getRegionContainer();
				RegionManager regions = container.get(p.getWorld());
				p.sendMessage(ChatColor.GREEN + "[AR][INFO] "+ plugin.lang.getText("RegionCreation"));
				LocalPlayer ply = worldguard.wrapPlayer(p);
				if ((regions.getRegionCountOfPlayer(ply) < Integer.valueOf(config.getOption("limit")) || p.hasPermission("ancient.regions.admin.bypassregion"))) {
					new BukkitRunnable() {

						@Override
						public void run() {
							List<BlockVector> edges = edges(p);
							String seperator = "-";
							if (proofregion(edges, worldguard, p) || p.hasPermission("ancient.regions.admin.bypassregion")) {
								int id = 1;
								while (regions.hasRegion(p.getName() + seperator + regionname + seperator + id)) {
									id = id + 1;
								}
								ProtectedCuboidRegion region = new ProtectedCuboidRegion(
										p.getName() + seperator + regionname + seperator + id, edges.get(0),
										edges.get(2));
								ProtectedRegion grg = regions.getRegion(ProtectedRegion.GLOBAL_REGION);
								region.setPriority(Integer.parseInt(config.getOption("regionpriority")));
								try {
									region.setParent(grg);
								} catch (CircularInheritanceException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
									DefaultDomain owner = region.getOwners();
									owner.addPlayer(p.getUniqueId());
										if (payment(p, e) || p.hasPermission("ancient.regions.admin.bypass")) {
											File schematic = new File(plugin.getDataFolder(), "/schematics/" + region.getId()+".schematic");
								            File dir = new File(plugin.getDataFolder(), "/schematics/");
								            if (!dir.exists())
								                dir.mkdirs();
								            EditSession es = new EditSession(new BukkitWorld(p.getWorld()), 999999999);
								            Vector min = region.getMinimumPoint();
								            Vector max = region.getMaximumPoint();
								            es.enableQueue();
								            CuboidClipboard clipboard = new CuboidClipboard(max.subtract(min).add(new Vector(1, 1, 1)), min);
								            clipboard.copy(es);
								            try {
												SchematicFormat.MCEDIT.save(clipboard, schematic);
											} catch (IOException | DataException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
								            es.flushQueue();
								           
											if (!config.getOption("standartdenyflags").contains("[]")) {
												String[] flags = config.getOption("standartdenyflags").split(",");
												for (String flag : flags) {
													if (config.getOption(flag) != null) {
														region.setFlag(new StateFlag(flag, false), State.DENY);
													}
												}
	
											}
										} else {
											p.sendMessage(ChatColor.RED + "[AR][ERROR] "+ plugin.lang.getText("NoMoney"));
										}
									regions.addRegion(region);
									p.sendMessage(ChatColor.GREEN + "[AR][INFO] "+ plugin.lang.getText("Created") + "[ " + edges.get(0)
											+ " | " + edges.get(2) + " ]");
							} else {
								p.sendMessage(ChatColor.RED + "[AR][ERROR] "+ plugin.lang.getText("RegionExists"));
							}

						}
					}.runTaskAsynchronously(plugin);
				} else {
					p.sendMessage(ChatColor.RED+ "[AR][ERROR] "+plugin.lang.getText("ToMany"));
				}
			} else {
				p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Permission"));
				e.setCancelled(true);
			}

		loadgui(menu, p, worldguard);
		return false;
	}
	public void removeRegion(WorldGuardPlugin worldguard, Player p, InventoryClickEvent e, Inventory menu) {
		if (p.hasPermission("ancient.regions.region.removeregion")) {
			RegionContainer container = worldguard.getRegionContainer();
			RegionManager regions = container.get(p.getWorld());
			Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
			LocalPlayer ply = worldguard.wrapPlayer(p);
			List<String> region = regions.getApplicableRegionsIDs(pt);
			if (region.isEmpty()) {
				p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("GobalError"));
			} else {
				ProtectedRegion rg = regions.getRegion(region.get(0));
				if (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass")) {
					if (give(p, e)) {
						 File dir = new File(plugin.getDataFolder(), "/schematics/" + rg.getId()+".schematic");
						 
				            EditSession editSession = new EditSession(new BukkitWorld(p.getWorld()), 999999999);
				 
				            Vector min = rg.getMinimumPoint();
				            editSession.enableQueue();
				            CuboidClipboard clipboard;
							try {
								clipboard = SchematicFormat.MCEDIT.load(dir);
								clipboard.place(editSession, min, false);
								
							} catch (MaxChangedBlocksException | IOException | DataException e2) {
								e2.printStackTrace();
							}
				            editSession.flushQueue();
				            dir.delete();
						regions.removeRegion(rg.getId());
						container.reload();
						p.sendMessage(ChatColor.GREEN+"[AR][INFO] "+plugin.lang.getText("Removed").replace("[PH]", rg.getId()));
					}
				} else {
					p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Owner"));
					e.setCancelled(true);
				}
			}
		} else {
			p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Permission"));
			e.setCancelled(true);
		}
			}

	public String loadPricefromConfig() {
		try {
			String price = regionprice;
			return price;
		} catch (Exception ex) {
			plugin.getLogger().info(ex.toString());
		}
		return null;
	}

	public String loadCurrencyfromConfig() {
		try {

			String currency = config.getOption("currency");
			return currency;
		} catch (Exception ex) {
			plugin.getLogger().info(ex.toString());
		}
		return null;
	}

	public List<BlockVector> edges(Player p) {
		List<BlockVector> edges = new ArrayList<BlockVector>();
		Double halfregionsize = (Double.valueOf(regionsize)) / 2;
		if (config.getOption("regionheight").equals("max")) {
			edges.add(new BlockVector(p.getLocation().getBlockX() - halfregionsize, p.getWorld().getMaxHeight(),
					p.getLocation().getBlockZ() - halfregionsize));
			edges.add(new BlockVector(p.getLocation().getBlockX() + halfregionsize, p.getWorld().getMaxHeight(),
					p.getLocation().getBlockZ() - halfregionsize));
		} else {
			Double regionheight = Double.valueOf(config.getOption("regionheight"));
			edges.add(new BlockVector(p.getLocation().getBlockX() - halfregionsize,
					regionheight, p.getLocation().getBlockZ() - halfregionsize));
			edges.add(new BlockVector(p.getLocation().getBlockX() + halfregionsize,
					regionheight, p.getLocation().getBlockZ() - halfregionsize));
		}
		if (config.getOption("regiondepth").equals("max")) {
			edges.add(new BlockVector(p.getLocation().getBlockX() + halfregionsize, 1,
					p.getLocation().getBlockZ() + halfregionsize));
			edges.add(new BlockVector(p.getLocation().getBlockX() + halfregionsize, 1,
					p.getLocation().getBlockZ() - halfregionsize));
		} else {
			Double regiondepth = Double.valueOf(config.getOption("regiondepth"));
			edges.add(new BlockVector(p.getLocation().getBlockX() + halfregionsize,
					regiondepth, p.getLocation().getBlockZ() + halfregionsize));

			edges.add(new BlockVector(p.getLocation().getBlockX() + halfregionsize,
					regiondepth, p.getLocation().getBlockZ() - halfregionsize));
		}
		plugin.getLogger().info("Edges Initiated");

		return edges;
	}

	public boolean proofregion(List<BlockVector> edges, WorldGuardPlugin worldguard, Player p) {
		RegionContainer container = worldguard.getRegionContainer();
		RegionManager regions = container.get(p.getWorld());
		Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
		List<String> region = regions.getApplicableRegionsIDs(pt);
		if (!region.isEmpty()) {
			return false;
		}
		BlockVector pt1 = edges.get(0);
		BlockVector pt2 = edges.get(1);
		BlockVector pt3 = edges.get(2);
		BlockVector pt4 = edges.get(3);
		Double regionwidth = pt2.getX()-pt1.getX();
		Double regionheight = pt1.getY() -pt4.getY();
		BlockVector pt5 = new BlockVector(pt1.getX(), pt1.getY(), pt1.getZ()+ regionwidth);
		BlockVector pt6 = new BlockVector(pt3.getX(), pt3.getY(), pt3.getZ() - regionwidth);
		BlockVector pt7 = new BlockVector(pt3.getX(), pt3.getY()+regionheight, pt3.getZ());
		BlockVector pt8 = new BlockVector(pt1.getX(), pt2.getY()-regionheight, pt2.getZ());
		if (!compare(pt2, pt1, "X", worldguard, p)) {
			return false;
		} else if (!compare(pt1, pt8, "Y", worldguard, p)){
			return false;
		} else if (!compare(pt4, pt8, "X", worldguard, p)){
			return false;
		} else if (!compare(pt2, pt4, "Y", worldguard, p)){
			return false;
		} else if (!compare(pt3, pt4, "Z", worldguard, p)){
			return false;
		} else if (!compare(pt3, pt6, "X", worldguard, p)){
			return false;
		} else if (!compare(pt6, pt8, "Z", worldguard, p)){
			return false;
		} else if (!compare(pt5, pt6, "Y", worldguard, p)){
			return false;
		} else if (!compare(pt7, pt3, "Y", worldguard, p)){
			return false;
		} else if (!compare(pt7, pt5, "X", worldguard, p)){
			return false;
		} else if (!compare(pt5, pt1, "Z", worldguard, p)){
			return false;
		} else if (!compare(pt7, pt2, "Z", worldguard, p)){
			return false;
		} else if (!compare(pt2, pt8, "XY", worldguard, p)){
			return false;
		} else if (!compare(pt7, pt6, "XY", worldguard, p)){
			return false;
		} else if (!compare(pt7, pt4, "ZY", worldguard, p)){
			return false;
		} else if (!compare(pt5, pt8, "ZY", worldguard, p)){
			return false;
		} else if (!compare(pt7, pt1, "ZX", worldguard, p)){
			return false;
		} else if (!compare(pt3, pt8, "ZX", worldguard, p)){
			return false;
		} else {
			return true;
		}

	}

	public boolean compare(BlockVector big, BlockVector small, String Direction, WorldGuardPlugin worldguard,
			Player p) {
		if (Direction.equals("X")) {
			while (big.getX() >= small.getX()) {
				RegionContainer container = worldguard.getRegionContainer();
				RegionManager regions = container.get(p.getWorld());
				List<String> region = regions.getApplicableRegionsIDs(small);
				if (!region.isEmpty()) {
					return false;
				}
				small = new BlockVector(small.getX() + 1, small.getY(), small.getZ());
			}
			return true;
		} else if (Direction.equals("Y")) {
			while (big.getY() >= small.getY()) {
				RegionContainer container = worldguard.getRegionContainer();
				RegionManager regions = container.get(p.getWorld());
				List<String> region = regions.getApplicableRegionsIDs(small);
				if (!region.isEmpty()) {
					return false;
				}
				small = new BlockVector(small.getX(), small.getY() + 1, small.getZ());
			}
			return true;
		} else if (Direction.equals("XY")) {
			while (big.getY() >= small.getY() && big.getX() >= small.getX()) {
				RegionContainer container = worldguard.getRegionContainer();
				RegionManager regions = container.get(p.getWorld());
				List<String> region = regions.getApplicableRegionsIDs(small);
				if (!region.isEmpty()) {
					return false;
				}
				small = new BlockVector(small.getX() + 1, small.getY() + 1, small.getZ());
			}
			return true;
		} else if (Direction.equals("Z")) {
			while (big.getZ() >= small.getZ()) {
				RegionContainer container = worldguard.getRegionContainer();
				RegionManager regions = container.get(p.getWorld());
				List<String> region = regions.getApplicableRegionsIDs(small);
				if (!region.isEmpty()) {
					return false;
				}
				small = new BlockVector(small.getX(), small.getY(), small.getZ() + 1);
			}
			return true;
		} else if (Direction.equals("ZX")) {
			while (big.getZ() >= small.getZ()) {
				RegionContainer container = worldguard.getRegionContainer();
				RegionManager regions = container.get(p.getWorld());
				List<String> region = regions.getApplicableRegionsIDs(small);
				if (!region.isEmpty()) {
					return false;
				}
				if (small.getX() == big.getX()) {
					small = new BlockVector(small.getX(), small.getY(), small.getZ() + 1);
				} else {
					small = new BlockVector(small.getX() + 1, small.getY(), small.getZ() + 1);
				}
			}
			return true;
		} else if (Direction.equals("ZY")) {
			while (big.getZ() >= small.getZ()) {
				RegionContainer container = worldguard.getRegionContainer();
				RegionManager regions = container.get(p.getWorld());
				List<String> region = regions.getApplicableRegionsIDs(small);
				if (!region.isEmpty()) {
					return false;
				}
				if (small.getY() == big.getY()) {
					small = new BlockVector(small.getX(), small.getY(), small.getZ() + 1);
				} else {
					small = new BlockVector(small.getX(), small.getY() + 1, small.getZ() + 1);
				}
			}
			return true;
		} else if (Direction.equals("P")) {
			RegionContainer container = worldguard.getRegionContainer();
			RegionManager regions = container.get(p.getWorld());
			Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
			List<String> region = regions.getApplicableRegionsIDs(pt);
			if (!region.isEmpty()) {
				return false;
			}
			return true;
		} else {
			plugin.getLogger().warning("Wrong Direction");
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean payment(Player p, InventoryClickEvent e) {
		RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy vaultEcon = economyProvider.getProvider();
		if (p.hasPermission("ancient.regions.admin.bypass")) {
			e.setCancelled(true);
			return true;
		}
		if (vaultEcon != null) {
			String price = loadPricefromConfig();
			if (vaultEcon.getBalance(p.getName()) != 0 && vaultEcon.getBalance(p.getName()) >= Double.valueOf(price)) {
				vaultEcon.withdrawPlayer(p.getName(), Double.valueOf(price));
				p.sendMessage(ChatColor.BLUE + "[AR][INFO]" + plugin.lang.getText("PayNote2").replace("[PH]",
						loadPricefromConfig() + " " + loadCurrencyfromConfig()));
				e.setCancelled(true);
				return true;
			} else {
				p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("NoMoney"));
				e.setCancelled(true);
				return false;
			}
		} else {
			p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("VaultError"));
			e.setCancelled(true);
		}
		return false;
	}
	@SuppressWarnings("deprecation")
	public boolean give(Player p, InventoryClickEvent e) {
		RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy vaultEcon = economyProvider.getProvider();
		if (p.hasPermission("ancient.regions.admin.bypass")) {
			e.setCancelled(true);
			return true;
		}
		if (vaultEcon != null) {
			String price = loadPricefromConfig();
				vaultEcon.depositPlayer(p.getName(), Double.valueOf(price)*(Double.valueOf(config.getOption("payback"))/100));
				p.sendMessage(ChatColor.BLUE + "[AR][INFO]" + plugin.lang.getText("Payback").replace("[PH]",
						Double.valueOf(price)*(Double.valueOf(config.getOption("payback"))/100) + " " + loadCurrencyfromConfig()));
				e.setCancelled(true);
				return true;
		} else {
			p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("VaultError"));
			e.setCancelled(true);
		}
		return false;
	}

	public boolean loadgui(Inventory menu, Player p, WorldGuardPlugin worldguard) {
		if (p.hasPermission("ancient.regions.region." + permission)) {
			ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
			if (number == 1) {
				ITEM = new ItemStack(Material.IRON_BLOCK);
			} else if (number == 2) {
				ITEM = new ItemStack(Material.GOLD_BLOCK);
			} else if (number == 3) {
				ITEM = new ItemStack(Material.DIAMOND_BLOCK);
			} else if (number == 4) {
				ITEM = new ItemStack(Material.EMERALD_BLOCK);
			}
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GOLD + plugin.lang.getText("RegionName") + ": " + ChatColor.YELLOW + regionname);
			lore.add(ChatColor.GOLD + plugin.lang.getText("RegionSize") + ": " + ChatColor.YELLOW + regionsize + " x "
					+ regionsize + " x " + config.getOption("regionheight") + " x " + config.getOption("regiondepth"));
			lore.add(ChatColor.GOLD + plugin.lang.getText("RegionPrice") + ": " + ChatColor.YELLOW
					+ loadPricefromConfig() + " " + loadCurrencyfromConfig());
			ItemMeta imeta = ITEM.getItemMeta();
			imeta.setDisplayName(ChatColor.GOLD + plugin.lang.getText("Buy") + "'" + regionname + "' Region");
			imeta.setLore(lore);
			imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			ITEM.setItemMeta(imeta);
			if (number == 1) {
				menu.setItem(2, ITEM);
			} else if (number == 2) {
				menu.setItem(3, ITEM);
			} else if (number == 3) {
				menu.setItem(5, ITEM);
			} else if (number == 4) {
				menu.setItem(6, ITEM);
			}
			
		} else {
			ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
			if (ITEM.getItemMeta().getLore() == null) {
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.RED + plugin.lang.getText("Permission"));
				ItemMeta imeta = ITEM.getItemMeta();
				imeta.setDisplayName(ChatColor.RED+"[OFF] " + plugin.lang.getText("Toggle") + regionname);
				imeta.setLore(lore);
				imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				ITEM.setItemMeta(imeta);
			}
			menu.setItem(number - 1, ITEM);
		}
		return true;
	}
}
