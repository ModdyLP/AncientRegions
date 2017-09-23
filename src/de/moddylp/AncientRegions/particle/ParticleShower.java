package de.moddylp.AncientRegions.particle;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.LoadConfig;
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
import org.inventivetalent.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class ParticleShower {
	private Main plugin;
	private Inventory menu;
	private String flagname = "Particle";
	private int particle;
	private BukkitTask particles;
	private int timernum;
	private LoadConfig config;
	private LogFile data;
    private static List<Player> players;
    private BukkitTask locations;
    private BukkitTask timertask;

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
			Vector pt = new Vector(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
			List<String> region = regions.getApplicableRegionsIDs(pt);
			if (region.isEmpty()) {
				p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("GobalError"));
			} else {
				ProtectedRegion rg = regions.getRegion(region.get(0));
				if (data.getString(rg.getId()) == null || data.getString(rg.getId()).equals("false")) {
					World world = p.getWorld();
					players = new ArrayList<>();
					for (Player ps : plugin.getServer().getOnlinePlayers()) {
						players.add(ps);
					}
					timer(rg, p);
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
    public void timer (ProtectedRegion rg, Player p) {
        timernum = 0;
        timertask = new BukkitRunnable() {
            public void run() {
                if (data.getString(rg.getId()) == null) {
                    p.sendMessage(ChatColor.RED + "[AR][INFO] " + plugin.lang.getText("ParticlesOff"));
                    particles.cancel();
                    timertask.cancel();
                }
                if (!config.getOption("showtimeofparticle").equals("-1")) {
                    timernum++;
                    if (timernum >= (Integer.valueOf(config.getOption("showtimeofparticle")) * 2)) {
                        data.setString(rg.getId(), null);
                    }
                }
                if (p == null || !p.isOnline()) {
                    data.setString(rg.getId(), null);
                    plugin.getLogger().info("Partciles cancled by Console");
                    particles.cancel();
                    timertask.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 10);
    }
	public List<Vector> pt(ProtectedRegion rg, Player p) {
		List<Vector> vectoren = new ArrayList<>();
                try {
                    double range1;
                    Vector pt1 = rg.getMaximumPoint();
                    if (rg.getMaximumPoint().getBlockY() <= p.getLocation().getBlockY() + 16) {
                        range1 = rg.getMaximumPoint().getBlockY() - p.getLocation().getBlockY();
                    } else {
                        range1 = 16;
                    }
                    Vector pt2 = rg.getMinimumPoint();
                    if (rg.getMinimumPoint().getBlockY() >= p.getLocation().getBlockY() - 16) {
                        pt2 = new Vector(pt2.getBlockX(), rg.getMinimumPoint().getBlockY(), pt2.getBlockZ());
                    } else {
                        pt2 = new Vector(pt2.getBlockX(), p.getLocation().getBlockY() - 16, pt2.getBlockZ());
                    }
                    pt1 = new Vector(pt1.getBlockX(), p.getLocation().getBlockY() + range1, pt1.getBlockZ());

                    int widthX = pt1.getBlockX() - pt2.getBlockX() + 1;
                    int widthZ = pt1.getBlockZ() - pt2.getBlockZ() + 2;
                    Vector pt3 = new Vector(pt2.getBlockX() + widthX, pt2.getBlockY(), pt2.getBlockZ());
                    Vector pt4 = new Vector(pt2.getBlockX(), pt2.getBlockY(), pt2.getBlockZ() + widthZ);


                    while (pt1.getBlockY() >= pt2.getBlockY()) {
                        vectoren.add(new Vector(pt1.getBlockX(), pt2.getBlockY(), pt1.getBlockZ()));
                        while (pt4.getBlockZ() >= pt2.getBlockZ()) {
                            vectoren.add(new Vector(pt4.getBlockX(), pt2.getBlockY(), pt4.getBlockZ()));
                            pt4 = new Vector(pt4.getBlockX(), pt4.getBlockY(), pt4.getBlockZ() - 1);
                        }
                        pt4 = new Vector(pt4.getBlockX(), pt4.getBlockY(), pt4.getBlockZ() + widthZ);
                        while (pt4.getBlockX() <= pt1.getBlockX()) {
                            vectoren.add(new Vector(pt4.getBlockX(), pt2.getBlockY(), pt4.getBlockZ()));
                            pt4 = new Vector(pt4.getBlockX() + 1, pt4.getBlockY(), pt4.getBlockZ());
                        }
                        pt4 = new Vector(pt4.getBlockX() - widthX, pt4.getBlockY(), pt4.getBlockZ());
                        while (pt3.getBlockZ() <= pt1.getBlockZ()) {
                            vectoren.add(new Vector(pt3.getBlockX(), pt2.getBlockY(), pt3.getBlockZ()));
                            pt3 = new Vector(pt3.getBlockX(), pt3.getBlockY(), pt3.getBlockZ() + 1);
                        }
                        pt3 = new Vector(pt3.getBlockX(), pt3.getBlockY(), pt3.getBlockZ() - widthZ);
                        while (pt2.getBlockX() <= pt3.getBlockX()) {
                            vectoren.add(new Vector(pt2.getBlockX(), pt2.getBlockY(), pt2.getBlockZ()));
                            pt2 = new Vector(pt2.getBlockX() + 1, pt2.getBlockY(), pt2.getBlockZ());
                        }
                        pt2 = new Vector(pt2.getBlockX() - widthX, pt2.getBlockY() + 1, pt2.getBlockZ());
                    }
                    if (p.getLocation().getBlockY() - 16 <= rg.getMinimumPoint().getBlockY()) {
                        while (pt4.getBlockX() <= pt3.getBlockX()) {
                            vectoren.add(new Vector(pt4.getBlockX(), pt4.getBlockY(), pt4.getBlockZ()));
                            while (pt3.getBlockZ() <= pt4.getBlockZ()) {
                                vectoren.add(new Vector(pt4.getBlockX(), pt4.getBlockY(), pt3.getBlockZ()));
                                pt3 = new Vector(pt3.getBlockX(), pt3.getBlockY(), pt3.getBlockZ() + 1);
                            }
                            pt3 = new Vector(pt3.getBlockX(), pt3.getBlockY(), pt3.getBlockZ() - widthZ);
                            pt4 = new Vector(pt4.getBlockX() + 1, pt4.getBlockY(), pt4.getBlockZ());
                        }
                        pt4 = new Vector(pt4.getBlockX() - widthX, pt4.getBlockY(), pt4.getBlockZ());
                    }
                    if (p.getLocation().getBlockY() + 16 >= rg.getMaximumPoint().getBlockY()) {
                        while (pt4.getBlockX() <= pt3.getBlockX()) {
                            vectoren.add(new Vector(pt4.getBlockX(), pt1.getBlockY(), pt4.getBlockZ()));
                            while (pt3.getBlockZ() <= pt4.getBlockZ()) {
                                vectoren.add(new Vector(pt4.getBlockX(), pt1.getBlockY(), pt3.getBlockZ()));
                                pt3 = new Vector(pt3.getBlockX(), pt3.getBlockY(), pt3.getBlockZ() + 1);
                            }
                            pt3 = new Vector(pt3.getBlockX(), pt3.getBlockY(), pt3.getBlockZ() - widthZ);
                            pt4 = new Vector(pt4.getBlockX() + 1, pt4.getBlockY(), pt4.getBlockZ());
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

		return vectoren;
	}

	private void show(World world, Player p, ProtectedRegion rg) {
        particles = new BukkitRunnable() {
			public void run() {
				try {
                    for (Vector vector : pt(rg, p)) {
                        Location loc = new Location(world, vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
                        if (config.getOption("showfor").equals("player")) {
                            List<Player> player = new ArrayList<>();
                            player.add(p);
                            ParticleEffect.FIREWORKS_SPARK.send(player,loc,0, 0, 0, 0, 1 );
                        } else if (config.getOption("showfor").equals("all")) {
                            if (players.size() > 0) {
                                ParticleEffect.FIREWORKS_SPARK.send(players,loc,0, 0, 0, 0, 1 );
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "[AR][ERROR] " + "" + ChatColor.BOLD
                                    + plugin.lang.getText("ConfigError").replace("[PH]", "showfor"));
                            particles.cancel();

                        }
                    }
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}.runTaskTimerAsynchronously(plugin, 0, 25);
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
