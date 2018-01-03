package de.moddylp.AncientRegions.particle;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Language;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.FileDriver;
import de.moddylp.AncientRegions.particle.LogFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.inventivetalent.particle.ParticleEffect;

public class ParticleShower {
    private Main plugin;
    private Inventory menu;
    private int particle;
    private BukkitTask particles;
    private int timernum;
    private LogFile data;
    private static List<Player> players;
    private BukkitTask locations;
    private BukkitTask timertask;
    private List<Vector> vectoren = new ArrayList<Vector>();

    public ParticleShower(Main plugin, Inventory menu) {
        this.plugin = plugin;
        this.menu = menu;
        this.data = new LogFile();
    }

    public void toggle(Player p, WorldGuardPlugin worldguard) {
        try {
            RegionContainer container = worldguard.getRegionContainer();
            RegionManager regions = container.get(p.getWorld());
            Vector pt = new Vector(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
            List<String> region = regions.getApplicableRegionsIDs(pt);
            if (region.isEmpty()) {
                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("GobalError"));
            } else {
                for (String regionid : region) {
                    ProtectedRegion rg = regions.getRegion(regionid);
                    if (this.data.getString(regionid) == null || this.data.getString(regionid).equals("false")) {
                        World world = p.getWorld();
                        players = new ArrayList<Player>();
                        players.addAll(this.plugin.getServer().getOnlinePlayers());
                        this.timer(rg, p);
                        this.show(world, p, rg);
                        this.data.setString(regionid, "true");
                        p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.plugin.lang.getText("Particles").replace("[PH]", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "showtimeofparticle", 20)));
                        continue;
                    }
                    this.data.setString(regionid, null);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void timer(final ProtectedRegion rg, final Player p) {
        this.timernum = 0;
        this.timertask = new BukkitRunnable(){

            public void run() {
                if (ParticleShower.this.data.getString(rg.getId()) == null) {
                    p.sendMessage(ChatColor.RED + "[AR][INFO] " + plugin.lang.getText("ParticlesOff"));
                    ParticleShower.this.particles.cancel();
                    ParticleShower.this.timertask.cancel();
                }
                if (Integer.valueOf(Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showtimeofparticle", 20)) != -1) {
                    ParticleShower.this.timernum++;
                    if (ParticleShower.this.timernum >= Integer.valueOf(Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showtimeofparticle", 20)) * 2) {
                        ParticleShower.this.data.setString(rg.getId(), null);
                    }
                }
                if (p == null || !p.isOnline()) {
                    ParticleShower.this.data.setString(rg.getId(), null);
                    Main.getInstance().getLogger().info("Partciles cancled by Console");
                    ParticleShower.this.particles.cancel();
                    ParticleShower.this.timertask.cancel();
                }
            }
        }.runTaskTimer((Plugin)this.plugin, 0, 10);
    }

    private List<Vector> calcallwallvectors(ProtectedRegion rg, Player p) {
        ArrayList<Vector> vectoren = new ArrayList<Vector>();
        try {
            double j;
            double j2;
            double i;
            double i2;
            BlockVector maxXYZpt = rg.getMaximumPoint();
            BlockVector minXYZpt = rg.getMinimumPoint();
            Vector maxXZpt = new Vector(maxXYZpt.getBlockX(), minXYZpt.getBlockY(), maxXYZpt.getBlockZ());
            Vector maxXpt = new Vector(maxXYZpt.getBlockX(), minXYZpt.getBlockY(), minXYZpt.getBlockZ());
            Vector maxXYpt = new Vector(maxXYZpt.getBlockX(), maxXYZpt.getBlockY(), minXYZpt.getBlockZ());
            Vector maxYZpt = new Vector(minXYZpt.getBlockX(), maxXYZpt.getBlockY(), maxXYZpt.getBlockZ());
            Vector maxYpt = new Vector(minXYZpt.getBlockX(), maxXYZpt.getBlockY(), minXYZpt.getBlockZ());
            Vector maxZpt = new Vector(minXYZpt.getBlockX(), minXYZpt.getBlockY(), maxXYZpt.getBlockZ());
            Vector copyMinXYZpt = new Vector(minXYZpt.getBlockX(), minXYZpt.getBlockY(), minXYZpt.getBlockZ());
            for (i = (double)copyMinXYZpt.getBlockZ(); i < (double)maxYZpt.getBlockZ(); i += 2.0) {
                for (j = (double)copyMinXYZpt.getBlockY(); j < (double)maxYZpt.getBlockY(); j += 2.0) {
                    vectoren.add(new Vector(copyMinXYZpt.getBlockX(), copyMinXYZpt.getBlockY(), copyMinXYZpt.getBlockZ()));
                }
                copyMinXYZpt = new Vector(copyMinXYZpt.getBlockX(), minXYZpt.getBlockY(), copyMinXYZpt.getBlockZ());
            }
            copyMinXYZpt = new Vector(minXYZpt.getBlockX(), minXYZpt.getBlockY(), minXYZpt.getBlockZ());
            for (i = (double)copyMinXYZpt.getBlockX(); i < (double)maxXYpt.getBlockX(); i += 2.0) {
                for (j = (double)copyMinXYZpt.getBlockY(); j < (double)maxXYpt.getBlockY(); j += 2.0) {
                    vectoren.add(new Vector(copyMinXYZpt.getBlockX(), copyMinXYZpt.getBlockY(), copyMinXYZpt.getBlockZ()));
                }
                copyMinXYZpt = new Vector(copyMinXYZpt.getBlockX(), minXYZpt.getBlockY(), copyMinXYZpt.getBlockZ());
            }
            Vector copymaxXpt = new Vector(maxXpt.getBlockX(), maxXpt.getBlockY(), maxXpt.getBlockZ());
            for (double i3 = (double)copymaxXpt.getBlockZ(); i3 < (double)maxXYZpt.getBlockZ(); i3 += 2.0) {
                for (double j3 = (double)copymaxXpt.getBlockY(); j3 < (double)maxXYZpt.getBlockY(); j3 += 2.0) {
                    vectoren.add(new Vector(copymaxXpt.getBlockX(), copymaxXpt.getBlockY(), copymaxXpt.getBlockZ()));
                }
                copymaxXpt = new Vector(copymaxXpt.getBlockX(), maxXpt.getBlockY(), copymaxXpt.getBlockZ());
            }
            Vector copymaxZpt = new Vector(maxZpt.getBlockX(), maxZpt.getBlockY(), maxZpt.getBlockZ());
            for (i2 = (double)copymaxZpt.getBlockZ(); i2 < (double)maxXYZpt.getBlockZ(); i2 += 2.0) {
                for (j2 = (double)copymaxZpt.getBlockY(); j2 < (double)maxXYZpt.getBlockY(); j2 += 2.0) {
                    vectoren.add(new Vector(copymaxZpt.getBlockX(), copymaxZpt.getBlockY(), copymaxZpt.getBlockZ()));
                }
                copymaxZpt = new Vector(copymaxZpt.getBlockX(), maxZpt.getBlockY(), copymaxZpt.getBlockZ());
            }
            copyMinXYZpt = new Vector(minXYZpt.getBlockX(), minXYZpt.getBlockY(), minXYZpt.getBlockZ());
            for (i2 = (double)copyMinXYZpt.getBlockZ(); i2 < (double)maxXZpt.getBlockZ(); i2 += 2.0) {
                for (j2 = (double)copyMinXYZpt.getBlockX(); j2 < (double)maxXZpt.getBlockX(); j2 += 2.0) {
                    vectoren.add(new Vector(copyMinXYZpt.getBlockX(), copyMinXYZpt.getBlockY(), copyMinXYZpt.getBlockZ()));
                }
                copyMinXYZpt = new Vector(minXYZpt.getBlockX(), copyMinXYZpt.getBlockY(), copyMinXYZpt.getBlockZ());
            }
            Vector copymaxYpt = new Vector(maxYpt.getBlockX(), maxYpt.getBlockY(), maxYpt.getBlockZ());
            for (double i4 = (double)copymaxYpt.getBlockZ(); i4 < (double)maxXYZpt.getBlockZ(); i4 += 2.0) {
                for (double j4 = (double)copymaxYpt.getBlockX(); j4 < (double)maxXYZpt.getBlockX(); j4 += 2.0) {
                    vectoren.add(new Vector(copymaxYpt.getBlockX(), copymaxYpt.getBlockY(), copymaxYpt.getBlockZ()));
                }
                copymaxYpt = new Vector(maxYpt.getBlockX(), copymaxYpt.getBlockY(), copymaxYpt.getBlockZ());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return vectoren;
    }

    private List<Vector> calcVisibleVectors(List<Vector> vectoren, Vector playerlocation) {
        ArrayList<Vector> visbleVectors = new ArrayList<Vector>();
        double range = Double.valueOf(Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_particleshowrange", 16));
        for (Vector pt : vectoren) {
            if ((double)playerlocation.getBlockX() + range < (double)pt.getBlockX() && (double)playerlocation.getBlockX() - range > (double)pt.getBlockX() && (double)playerlocation.getBlockY() + range < (double)pt.getBlockY() && (double)playerlocation.getBlockY() - range > (double)pt.getBlockY() && (double)playerlocation.getBlockZ() + range < (double)pt.getBlockZ() && (double)playerlocation.getBlockZ() - range > (double)pt.getBlockZ()) continue;
            visbleVectors.add(pt);
        }
        return visbleVectors;
    }

    private void show(final World world, final Player p, final ProtectedRegion rg) {
        new BukkitRunnable(){

            public void run() {
                ParticleShower.this.vectoren = ParticleShower.this.calcallwallvectors(rg, p);
                ParticleShower.this.particles = new BukkitRunnable(){

                    public void run() {
                        try {
                            for (Vector vector : ParticleShower.this.calcVisibleVectors(ParticleShower.this.vectoren, new Vector(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()))) {
                                Location loc = new Location(world, (double)vector.getBlockX(), (double)vector.getBlockY(), (double)vector.getBlockZ());
                                if (Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showfor", 20).equals("player")) {
                                    ArrayList<Player> player = new ArrayList<Player>();
                                    player.add(p);
                                    ParticleEffect.FIREWORKS_SPARK.send(player, loc, 0.0, 0.0, 0.0, 0.0, 1);
                                    continue;
                                }
                                if (Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showfor", 20).equals("all")) {
                                    if (players.size() <= 0) continue;
                                    ParticleEffect.FIREWORKS_SPARK.send(players, loc, 0.0, 0.0, 0.0, 0.0, 1);
                                    continue;
                                }
                                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + ChatColor.BOLD + plugin.lang.getText("ConfigError").replace("[PH]", "showfor"));
                                ParticleShower.this.particles.cancel();
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }.runTaskTimerAsynchronously((Plugin)ParticleShower.this.plugin, 0, 25);
            }

        }.runTaskAsynchronously((Plugin)this.plugin);
    }

    public void loadgui(Player p) {
        String flagname = "Particle";
        if (p.hasPermission("ancient.regions.region.particle")) {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GOLD + this.plugin.lang.getText("Set").replace("[PH]", flagname));
            ItemMeta imeta = ITEM.getItemMeta();
            imeta.setDisplayName(this.plugin.lang.getText("Toggle") + flagname);
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
            this.menu.setItem(5, ITEM);
        } else {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            if (ITEM.getItemMeta().getLore() == null) {
                ArrayList<String> lore = new ArrayList<String>();
                lore.add(ChatColor.RED + this.plugin.lang.getText("Permission"));
                ItemMeta imeta = ITEM.getItemMeta();
                imeta.setDisplayName(ChatColor.RED + "[OFF] " + this.plugin.lang.getText("Toggle") + flagname);
                imeta.setLore(lore);
                imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                ITEM.setItemMeta(imeta);
            }
            this.menu.setItem(5, ITEM);
        }
    }

    public int getParticle() {
        return this.particle;
    }

    public void setParticle(int particle) {
        this.particle = particle;
    }

}

