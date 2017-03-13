package de.moddylp.AncientRegions.region;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.sk89q.worldedit.*;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import de.moddylp.AncientRegions.loader.WorldEditHandler6;
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

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
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
    private String permission;
	private LoadConfig config;
	private String regionname;
	private String regionprice;
	private String regionsize;
	private int number;
	private int halfregionsize;
    private int realregionsize;

    public Region(Main plugin, int number) {
		this.plugin = plugin;
        String region = "region" + number;
		this.number = number;
		config = new LoadConfig(plugin);
		this.permission = "buy" + region.toLowerCase();
		this.regionname = config.getOption(region + "name");
		this.regionprice = config.getOption(region + "price");
		this.regionsize = config.getOption(region + "size");

	}

	public boolean buy(WorldGuardPlugin worldguard, Player p, InventoryClickEvent e, Inventory menu, WorldEditPlugin worldedit) {
        e.setCancelled(true);
		if (p.hasPermission("ancient.regions.region." + permission)) {
				RegionContainer container = worldguard.getRegionContainer();
				RegionManager regions = container.get(p.getWorld());
				p.sendMessage(ChatColor.GREEN + "[AR][INFO] "+ plugin.lang.getText("RegionCreation"));
				LocalPlayer ply = worldguard.wrapPlayer(p);
            if (regions != null) {
                if ((regions.getRegionCountOfPlayer(ply) < Integer.valueOf(config.getOption("limit")) || p.hasPermission("ancient.regions.admin.bypassregion"))) {
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                List<BlockVector> edges = edges(p);
                                if (checkRegionsExists(edges, worldguard, p) || p.hasPermission("ancient.regions.admin.bypassregion")) {
                                    String seperator = "-";
                                    int id = 1;
                                    while (regions.hasRegion(p.getName() + seperator + regionname + seperator + id)) {
                                        id = id + 1;
                                    }
                                    ProtectedRegion region = new ProtectedCuboidRegion(
                                            p.getName() + seperator + regionname + seperator + id, edges.get(0),
                                            edges.get(2));
                                    ProtectedRegion grg = regions.getRegion(ProtectedRegion.GLOBAL_REGION);
                                    region.setPriority(Integer.parseInt(config.getOption("regionpriority")));
                                        try {
                                            region.setParent(grg);
                                        } catch (CircularInheritanceException e2) {
                                            e2.printStackTrace();
                                        }
                                        DefaultDomain owner = region.getOwners();
                                        owner.addPlayer(p.getUniqueId());
                                            if (payment(p, e) || p.hasPermission("ancient.regions.admin.bypass")) {
                                                if (!config.getOption("standartdenyflags").contains("[]")) {
                                                    String[] flags = config.getOption("standartdenyflags").split(",");
                                                    for (String flag : flags) {
                                                        if (config.getOption(flag) != null) {
                                                            region.setFlag(new StateFlag(flag, false), State.DENY);
                                                        }
                                                    }

                                                }
                                                if (config.getOption("backuprg").equals("true")) {
                                                        File schematic = new File(plugin.getDataFolder(), "/schematics/" + region.getId() + ".schematic");
                                                        File dir = new File(plugin.getDataFolder(), "/schematics/");
                                                        if (!dir.exists())
                                                            dir.mkdirs();
                                                        WorldEditHandler6 handler = new WorldEditHandler6(plugin);
                                                        if (handler.saveRegionBlocks(schematic, p.getName() + seperator + regionname + seperator + id, p , region)) {
                                                            plugin.getLogger().info("BACKUP SUCCESS");
                                                            regions.addRegion(region);
                                                            p.sendMessage(ChatColor.GREEN + "[AR][INFO] "+ plugin.lang.getText("Created") + "[ " + edges.get(0)
                                                                    + " | " + edges.get(2) + " ]");
                                                            p.closeInventory();
                                                            RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                                                            gui.open();
                                                        } else {
                                                            plugin.getLogger().info("BACKUP ERROR");
                                                            p.sendMessage(ChatColor.RED + "[AR][ERROR] "+ plugin.lang.getText("ErrorCreate"));
                                                        }
                                                } else {
                                                    regions.addRegion(region);
                                                    p.sendMessage(ChatColor.GREEN + "[AR][INFO] "+ plugin.lang.getText("Created") + "[ " + edges.get(0)
                                                            + " | " + edges.get(2) + " ]");
                                                    p.closeInventory();
                                                    RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                                                    gui.open();
                                                }
                                            }
                                } else {
                                    p.sendMessage(ChatColor.RED + "[AR][ERROR] "+ plugin.lang.getText("RegionExists"));
                                }

                            }
                        }.runTaskAsynchronously(plugin);
                    } else {
                        p.sendMessage(ChatColor.RED+ "[AR][ERROR] "+plugin.lang.getText("ToMany"));
                    }
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
            e.setCancelled(true);
			RegionContainer container = worldguard.getRegionContainer();
			RegionManager regions = container.get(p.getWorld());
			Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
			LocalPlayer ply = worldguard.wrapPlayer(p);
            List<String> region = null;
            if (regions != null) {
                region = regions.getApplicableRegionsIDs(pt);
            }
            if (region != null) {
                if (region.isEmpty()) {
                    p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion(region.get(0));
                    if (rg != null) {
                        if (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass")) {
                                if (config.getOption("backuprg").equals("true")) {
                                    File file = new File(plugin.getDataFolder(), "/schematics/" + rg.getId() + ".schematic");
                                    Vector max = rg.getMaximumPoint();
                                    Vector min = rg.getMinimumPoint();
                                    Vector dimension = new Vector(max.getBlockX()-min.getBlockX()+1, max.getBlockY()-min.getBlockY()+1, max.getBlockZ()-min.getBlockZ()+1);
                                    WorldEditHandler6 handler = new WorldEditHandler6(plugin);
                                    if (handler.restoreRegionBlocks(file, rg.getId(), p , rg, dimension)) {
                                        plugin.getLogger().info("RESTORE SUCCESS");
                                        String regionname = rg.getId();
                                        regions.removeRegion(regionname);
                                        p.sendMessage(ChatColor.GREEN+"[AR][INFO] "+plugin.lang.getText("Removed").replace("[PH]", rg.getId()));
                                        give(p, e, regionname);
                                        p.closeInventory();
                                        RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                                        gui.open();
                                    } else {
                                        plugin.getLogger().info("RESTORE ERROR");
                                        p.sendMessage(ChatColor.RED + "[AR][ERROR] "+ plugin.lang.getText("ErrorDelete"));
                                    }
                                } else {
                                    String regionname = rg.getId();
                                    regions.removeRegion(regionname);
                                    p.sendMessage(ChatColor.GREEN+"[AR][INFO] "+plugin.lang.getText("Removed").replace("[PH]", rg.getId()));
                                    give(p, e, regionname);
                                    p.closeInventory();
                                    RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                                    gui.open();
                                }
                        } else {
                            p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Owner"));
                            e.setCancelled(true);
                        }
                    }
                }
            }
        } else {
			p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Permission"));
			e.setCancelled(true);
		}
			}

	public String loadPricefromConfig() {
		try {
            return regionprice;
		} catch (Exception ex) {
			plugin.getLogger().info(ex.toString());
		}
		return null;
	}

	public String loadCurrencyfromConfig() {
		try {

            return config.getOption("currency");
		} catch (Exception ex) {
			plugin.getLogger().info(ex.toString());
		}
		return null;
	}

	private List<BlockVector> edges(Player p) {
		List<BlockVector> edges = new ArrayList<>();
		if (Integer.parseInt(regionsize) % 2 == 1) {
			halfregionsize = (Integer.parseInt(regionsize)+1) / 2;
		} else {
			halfregionsize = (Integer.parseInt(regionsize)) / 2;
		}
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
	private boolean checkRegionsExists(List<BlockVector> edges, WorldGuardPlugin worldGuard, Player p) {
        plugin.getLogger().info("checking region");
		boolean collision = false;
        try {
            Vector KGK = edges.get(0);
            Vector GGK = edges.get(1);
            Vector GKG = edges.get(2);
            Vector GKK = edges.get(3);
            Vector KKK = new Vector(KGK.getX(), GKK.getY(), KGK.getZ());
            Vector GGG = new Vector(GGK.getX()+1, GGK.getY(), GKG.getZ()+1);
            plugin.getLogger().info(GGG+" "+KKK);
            RegionContainer container = worldGuard.getRegionContainer();
            RegionManager regions = container.get(p.getWorld());
            List<String> region;
            plugin.getLogger().info("parameter set");
            while(KKK.getY() < GGG.getY()) {
                while(KKK.getX() < GGG.getX()) {
                    region = regions.getApplicableRegionsIDs(KKK);
                    if (!region.isEmpty()) {
                        plugin.getLogger().info("!!! Check complete collision by"+ KKK);
						collision = true;
                        return false;
                    }
                    while(KKK.getZ() < GGG.getZ()) {
                        region = regions.getApplicableRegionsIDs(KKK);
                        if (!region.isEmpty()) {
                            plugin.getLogger().info("!!! Check complete collision by"+ KKK);
							collision = true;
                            return false;
                        }
                        KKK = new Vector(KKK.getX(), KKK.getY(), KKK.getZ()+1);
                    }
                    KKK = new Vector(KKK.getX()+1, KKK.getY(), KGK.getZ());
                }
                KKK = new Vector(KGK.getX(), KKK.getY()+1, KKK.getZ());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        if (collision) {
			return false;
		} else {
			plugin.getLogger().info("Check complete no collision");
			return true;
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
	public boolean give(Player p, InventoryClickEvent e, String regionname) {
		RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy vaultEcon = economyProvider.getProvider();
		if (p.hasPermission("ancient.regions.admin.bypass")) {
			e.setCancelled(true);
			return true;
		}
		if (vaultEcon != null) {
				vaultEcon.depositPlayer(p.getName(), (Double.valueOf(config.getOption("region"+getregionnumber(regionname, p)+"price"))*(Double.valueOf(config.getOption("payback"))/100)));
				p.sendMessage(ChatColor.BLUE + "[AR][INFO]" + plugin.lang.getText("Payback").replace("[PH]",
                        (Double.valueOf(config.getOption("region"+getregionnumber(regionname, p)+"price"))*(Double.valueOf(config.getOption("payback"))/100)) + " " + loadCurrencyfromConfig()));
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
            if (Integer.parseInt(regionsize) % 2 == 1) {
                realregionsize = (Integer.parseInt(regionsize)+2);
            } else {
                realregionsize = (Integer.parseInt(regionsize)+1);
            }
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GOLD + plugin.lang.getText("RegionName") + ": " + ChatColor.YELLOW + regionname);
			lore.add(ChatColor.GOLD + plugin.lang.getText("RegionSize") + ": " + ChatColor.YELLOW + realregionsize + " x "
					+ realregionsize + " x H:" + config.getOption("regionheight") + " x D:" + config.getOption("regiondepth"));
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
    public int getregionnumber(String regioname, Player p) {
                String numbername = regioname.replace("-", "").replace(p.getName().toLowerCase(), "");
                numbername = numbername.substring(0, numbername.length()-1);
                String option = config.searchoption(numbername);
                if (option != null) {
                    int number = Integer.valueOf(option.replace("region", "").replace("name", ""));
                    return number;
                } else {
                    return 0;
                }
    }
}
