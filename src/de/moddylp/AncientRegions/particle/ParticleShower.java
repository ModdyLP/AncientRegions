package de.moddylp.AncientRegions.particle;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.darkblade12.particleeffect.ParticleEffect;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.LoadConfig;

public class ParticleShower {
	private Main plugin;
	private Inventory menu;
	private String flagname = "Particle";
	private int particle;
	private BukkitTask particles;
	private int timer;
	private LoadConfig config;
	private LogFile data;

	public ParticleShower(Main plugin, Inventory menu) {
		this.plugin = plugin;
		this.menu = menu;
		this.data = new LogFile(plugin);
		this.config = new LoadConfig(plugin);
	}

	public void toggle(Player p, WorldGuardPlugin worldguard) {
		try {
			RegionContainer container = worldguard.getRegionContainer();
			RegionManager regions = container.get(p.getWorld());
			Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
			List<String> region = regions.getApplicableRegionsIDs(pt);
			if (region.isEmpty()) {
				p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("GobalError"));
			} else {
				ProtectedRegion rg = regions.getRegion(region.get(0));
				if (data.getString(rg.getId()) == null || data.getString(rg.getId()).equals("false")) {
					World world = p.getWorld();
					show(world, p, rg);
					data.setString(rg.getId(), "true");
					p.sendMessage(ChatColor.GREEN
							+ "[AR][INFO] "+ plugin.lang.getText("Particles").replace("[PH]", config.getOption("showtimeofparticle")));
				} else {
					data.setString(rg.getId(), null);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public List<Vector> pt(ProtectedRegion rg, Player p) {
		List<Vector> vectoren = new ArrayList<>();
		try {
			double range1;
			Vector pt1 = rg.getMaximumPoint();
			if (rg.getMaximumPoint().getY() <= p.getLocation().getY()+16) {
				range1 = rg.getMaximumPoint().getY()-p.getLocation().getY();
			} else {
				range1 = 16;
			}
			Vector pt2 = rg.getMinimumPoint();
			if (rg.getMinimumPoint().getY() >= p.getLocation().getY()-16) {
				pt2 = new Vector(pt2.getX(), rg.getMinimumPoint().getY(), pt2.getZ());
			} else {
				pt2 = new Vector(pt2.getX(), p.getLocation().getY()-16, pt2.getZ());
			}
			pt1 = new Vector(pt1.getX(), p.getLocation().getY()+range1, pt1.getZ());
			
			Double widthX = pt1.getX()-pt2.getX()+1;
			Double widthZ = pt1.getZ()-pt2.getZ()+1;
			Vector pt3 = new Vector(pt2.getX()+widthX, pt2.getY(), pt2.getZ());
			Vector pt4 = new Vector(pt2.getX(), pt2.getY(), pt2.getZ()+widthZ);

			
			while (pt1.getY() >= pt2.getY()) {
				vectoren.add(new Vector(pt1.getX(), pt2.getY(), pt1.getZ()));
				while(pt4.getZ() >= pt2.getZ()) {
					vectoren.add(new Vector(pt4.getX(), pt2.getY(), pt4.getZ()));
					pt4 = new Vector(pt4.getX(), pt4.getY(), pt4.getZ()-1);
				}
				pt4 = new Vector(pt4.getX(), pt4.getY(), pt4.getZ()+widthZ);
				while(pt4.getX() <= pt1.getX()) {
					vectoren.add(new Vector(pt4.getX(), pt2.getY(), pt4.getZ()));
					pt4 = new Vector(pt4.getX()+1, pt4.getY(), pt4.getZ());
				}
				pt4 = new Vector(pt4.getX()-widthX, pt4.getY(), pt4.getZ());
				while (pt3.getZ() <= pt1.getZ()) {
					vectoren.add(new Vector(pt3.getX(), pt2.getY(), pt3.getZ()));
					pt3 = new Vector(pt3.getX(), pt3.getY(), pt3.getZ()+1);
				}
				pt3 = new Vector(pt3.getX(), pt3.getY(), pt3.getZ()-widthZ);
				while (pt2.getX() <= pt3.getX()) {
					vectoren.add(new Vector(pt2.getX(), pt2.getY(), pt2.getZ()));
					pt2 = new Vector(pt2.getX()+1, pt2.getY(), pt2.getZ());
				}
				pt2 = new Vector(pt2.getX()-widthX, pt2.getY()+1, pt2.getZ());	
			}
			if (p.getLocation().getY()-16 <= rg.getMinimumPoint().getY()) {
				while (pt4.getX() <= pt3.getX()) {
					vectoren.add(new Vector(pt4.getX(), pt4.getY(), pt4.getZ()));
					while (pt3.getZ() <= pt4.getZ()) {
						vectoren.add(new Vector(pt4.getX(), pt4.getY(), pt3.getZ()));
						pt3 = new Vector(pt3.getX(), pt3.getY(), pt3.getZ()+1);
					}
					pt3 = new Vector(pt3.getX(), pt3.getY(), pt3.getZ()-widthZ);
					pt4 = new Vector(pt4.getX()+1, pt4.getY(), pt4.getZ());
				}
				pt4 = new Vector(pt4.getX()-widthX, pt4.getY(), pt4.getZ());
			}
			if(p.getLocation().getY()+16 >= rg.getMaximumPoint().getY()) {
				while (pt4.getX() <= pt3.getX()) {
					vectoren.add(new Vector(pt4.getX(), pt1.getY(), pt4.getZ()));
					while (pt3.getZ() <= pt4.getZ()) {
						vectoren.add(new Vector(pt4.getX(), pt1.getY(), pt3.getZ()));
						pt3 = new Vector(pt3.getX(), pt3.getY(), pt3.getZ()+1);
					}
					pt3 = new Vector(pt3.getX(), pt3.getY(), pt3.getZ()-widthZ);
					pt4 = new Vector(pt4.getX()+1, pt4.getY(), pt4.getZ());
				}
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return vectoren;
	}

	public void show(World world, Player p, ProtectedRegion rg) {
		timer = 0;
		particles = new BukkitRunnable() {
			public void run() {
				try {
					if (p == null || !p.isOnline()) {
						data.setString(rg.getId(), null);
						plugin.getLogger().info("Partciles cancled by Console");
						particles.cancel();
					} else {
						List<Vector> vectoren = pt(rg, p);
						for (int i = 0; i < vectoren.size(); i++) {
							Vector vector = vectoren.get(i);
							Location loc = new Location(world, vector.getX(), vector.getY(), vector.getZ());
							if (config.getOption("showfor").equals("player")) {
								ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, loc, p);
							} else if (config.getOption("showfor").equals("all")) {
								List<Player> players = new ArrayList<>();
								for (Player p : plugin.getServer().getOnlinePlayers()) {
									players.add(p);
								}
								if (players.size() > 0) {
									ParticleEffect.REDSTONE.display(0, 0, 0, 0, 1, loc, players);
								}
							} else {
								p.sendMessage(ChatColor.RED + "[AR][ERROR] "+ "" + ChatColor.BOLD
										+ plugin.lang.getText("ConfigError").replace("[PH]", "showfor"));
								particles.cancel();
		
							}
		
						}
					}
					if (data.getString(rg.getId()) == null) {
						p.sendMessage(ChatColor.RED + "[AR][INFO] "+ plugin.lang.getText("ParticlesOff"));
						particles.cancel();
					}
					if (!config.getOption("showtimeofparticle").equals("-1")) {
						timer++;
						if (timer >= (Integer.valueOf(config.getOption("showtimeofparticle")) * 2)) {
							data.setString(rg.getId(), null);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}.runTaskTimerAsynchronously(plugin, 0, 10);
	}
	

	public void loadgui(Player p) {
		if (p.hasPermission("ancient.regions.region.particle")) {
			ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GOLD + plugin.lang.getText("Set").replace("[PH]", flagname));
			ItemMeta imeta = ITEM.getItemMeta();
			imeta.setDisplayName(plugin.lang.getText("Toggle") + flagname);
			imeta.setLore(lore);
			imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			ITEM.setItemMeta(imeta);
			menu.setItem(5, ITEM);
		} else {
			ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
			if (ITEM.getItemMeta().getLore() == null) {
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.RED + plugin.lang.getText("Permission"));
				ItemMeta imeta = ITEM.getItemMeta();
				imeta.setDisplayName(ChatColor.RED+"[OFF] " + plugin.lang.getText("Toggle") + flagname);
				imeta.setLore(lore);
				imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				ITEM.setItemMeta(imeta);
			}
			menu.setItem(5, ITEM);
		}
	}

	public int getParticle() {
		return particle;
	}

	public void setParticle(int particle) {
		this.particle = particle;
	}
}
