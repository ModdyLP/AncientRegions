package de.moddylp.AncientRegions.particle;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParticleShower {
    private Main plugin;
    private Inventory menu;
    private int particle;
    private BukkitTask particles;
    private int timernum;
    private LogFile data;
    private static List<Player> players;
    private BukkitTask timertask;
    private List<Vector> vectoren = new ArrayList<>();

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
            List<String> region = Objects.requireNonNull(regions).getApplicableRegionsIDs(pt);
            if (region.isEmpty()) {
                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("GobalError"));
            } else {
                for (String regionid : region) {
                    ProtectedRegion rg = regions.getRegion(regionid);
                    if (data.getString(regionid) == null || data.getString(regionid).equals("false")) {
                        World world = p.getWorld();
                        players = new ArrayList<>();
                        players.addAll(this.plugin.getServer().getOnlinePlayers());
                        timer(rg, p);
                        show(world, p, rg);
                        data.setString(regionid, "true");
                        p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.plugin.lang.getText("Particles").replace("[PH]", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "showtimeofparticle", 20)));
                        continue;
                    }
                    this.data.setString(regionid, null);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void timer(final ProtectedRegion rg, final Player p) {
        this.timernum = 0;
        this.timertask = new BukkitRunnable() {

            public void run() {
                if (data.getString(rg.getId()) == null) {
                    p.sendMessage(ChatColor.RED + "[AR][INFO] " + plugin.lang.getText("ParticlesOff"));
                    particles.cancel();
                    timertask.cancel();
                }
                if (Integer.valueOf(Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showtimeofparticle", 20)) != -1) {
                    timernum++;
                    if (timernum >= Integer.valueOf(Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showtimeofparticle", 20)) * 2) {
                        data.setString(rg.getId(), null);
                    }
                }
                if (p == null || !p.isOnline()) {
                    data.setString(rg.getId(), null);
                    Main.getInstance().getLogger().info("Partciles cancled by Console");
                    particles.cancel();
                    timertask.cancel();
                }
            }
        }.runTaskTimer(this.plugin, 0, 10);
    }

    private List<Vector> calcallwallvectors(ProtectedRegion rg) {
        ArrayList<Vector> vectoren = new ArrayList<>();
        try {
            BlockVector maxXYZpt = rg.getMaximumPoint();
            BlockVector minXYZpt = rg.getMinimumPoint();
            Vector maxXZpt = new Vector(maxXYZpt.getBlockX(), minXYZpt.getBlockY(), maxXYZpt.getBlockZ());
            Vector maxXpt = new Vector(maxXYZpt.getBlockX(), minXYZpt.getBlockY(), minXYZpt.getBlockZ());
            Vector maxXYpt = new Vector(maxXYZpt.getBlockX(), maxXYZpt.getBlockY(), minXYZpt.getBlockZ());
            Vector maxYZpt = new Vector(minXYZpt.getBlockX(), maxXYZpt.getBlockY(), maxXYZpt.getBlockZ());
            Vector maxYpt = new Vector(minXYZpt.getBlockX(), maxXYZpt.getBlockY(), minXYZpt.getBlockZ());
            Vector maxZpt = new Vector(minXYZpt.getBlockX(), minXYZpt.getBlockY(), maxXYZpt.getBlockZ());
            Vector copyMinXYZpt = new Vector(minXYZpt.getBlockX(), minXYZpt.getBlockY(), minXYZpt.getBlockZ());


            while (copyMinXYZpt.getBlockZ() <= maxYZpt.getBlockZ()) {
                while (copyMinXYZpt.getBlockY() <= maxYZpt.getBlockY()) {
                    vectoren.add(new Vector(copyMinXYZpt.getBlockX(), copyMinXYZpt.getBlockY(), copyMinXYZpt.getBlockZ()));
                    copyMinXYZpt = new Vector(copyMinXYZpt.getBlockX(), copyMinXYZpt.getBlockY()+2, copyMinXYZpt.getBlockZ());
                }
                copyMinXYZpt = new Vector(copyMinXYZpt.getBlockX(), minXYZpt.getBlockY(), copyMinXYZpt.getBlockZ()+2);
            }

            copyMinXYZpt = new Vector(minXYZpt.getBlockX(), minXYZpt.getBlockY(), minXYZpt.getBlockZ());


            while (copyMinXYZpt.getBlockX() <= maxXYpt.getBlockX()) {
                while (copyMinXYZpt.getBlockY() <= maxXYpt.getBlockY()) {
                    vectoren.add(new Vector(copyMinXYZpt.getBlockX(), copyMinXYZpt.getBlockY(), copyMinXYZpt.getBlockZ()));
                    copyMinXYZpt = new Vector(copyMinXYZpt.getBlockX(), copyMinXYZpt.getBlockY()+2, copyMinXYZpt.getBlockZ());
                }
                copyMinXYZpt = new Vector(copyMinXYZpt.getBlockX()+2, minXYZpt.getBlockY(), copyMinXYZpt.getBlockZ());
            }

            Vector copymaxXpt = new Vector(maxXpt.getBlockX(), maxXpt.getBlockY(), maxXpt.getBlockZ());


            while (copymaxXpt.getBlockZ() <= maxXYZpt.getBlockZ()) {
                while (copymaxXpt.getBlockY() <= maxXYZpt.getBlockY()) {
                    vectoren.add(new Vector(copymaxXpt.getBlockX(), copymaxXpt.getBlockY(), copymaxXpt.getBlockZ()));
                    copymaxXpt = new Vector(copymaxXpt.getBlockX(), copymaxXpt.getBlockY()+2, copymaxXpt.getBlockZ());
                }
                copymaxXpt = new Vector(copymaxXpt.getBlockX(), maxXpt.getBlockY(), copymaxXpt.getBlockZ()+2);
            }

            Vector copymaxZpt = new Vector(maxZpt.getBlockX(), maxZpt.getBlockY(), maxZpt.getBlockZ());


            while (copymaxZpt.getBlockX() <= maxXYZpt.getBlockX()) {
                while (copymaxZpt.getBlockY() <= maxXYZpt.getBlockY()) {
                    vectoren.add(new Vector(copymaxZpt.getBlockX(), copymaxZpt.getBlockY(), copymaxZpt.getBlockZ()));
                    copymaxZpt = new Vector(copymaxZpt.getBlockX(), copymaxZpt.getBlockY()+2, copymaxZpt.getBlockZ());
                }
                copymaxZpt = new Vector(copymaxZpt.getBlockX()+2, maxZpt.getBlockY(), copymaxZpt.getBlockZ());
            }

            copyMinXYZpt = new Vector(minXYZpt.getBlockX(), minXYZpt.getBlockY(), minXYZpt.getBlockZ());


            while (copyMinXYZpt.getBlockZ() <= maxXZpt.getBlockZ()) {
                while (copyMinXYZpt.getBlockX() <= maxXZpt.getBlockX())  {
                    vectoren.add(new Vector(copyMinXYZpt.getBlockX(), copyMinXYZpt.getBlockY(), copyMinXYZpt.getBlockZ()));
                    copyMinXYZpt = new Vector(copyMinXYZpt.getBlockX()+2, copyMinXYZpt.getBlockY(), copyMinXYZpt.getBlockZ());
                }
                copyMinXYZpt = new Vector(minXYZpt.getBlockX(), copyMinXYZpt.getBlockY(), copyMinXYZpt.getBlockZ()+2);
            }

            Vector copymaxYpt = new Vector(maxYpt.getBlockX(), maxYpt.getBlockY(), maxYpt.getBlockZ());


            while (copymaxYpt.getBlockZ() <= maxXYZpt.getBlockZ()) {
                while (copymaxYpt.getBlockX() <= maxXYZpt.getBlockX()) {
                    vectoren.add(new Vector(copymaxYpt.getBlockX(), copymaxYpt.getBlockY(), copymaxYpt.getBlockZ()));
                    copymaxYpt = new Vector(copymaxYpt.getBlockX()+2, copymaxYpt.getBlockY(), copymaxYpt.getBlockZ());
                }
                copymaxYpt = new Vector(maxYpt.getBlockX(), copymaxYpt.getBlockY(), copymaxYpt.getBlockZ()+2);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return vectoren;
    }

    private List<Vector> calcVisibleVectors(List<Vector> vectoren, Player p) {
        Vector playerlocation = new Vector(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
        ArrayList<Vector> visbleVectors = new ArrayList<>();
        double range = Double.valueOf(Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_particleshowrange", 16));
        for (Vector pt : vectoren) {
            double distance = pt.distance(playerlocation);
            if (distance < range) {
                visbleVectors.add(pt);
            }

        }
        return visbleVectors;
    }

    private void show(final World world, final Player p, final ProtectedRegion rg) {
        new BukkitRunnable() {

            public void run() {
                vectoren = calcallwallvectors(rg);
                particles = new BukkitRunnable() {

                    public void run() {
                        try {
                            for (Vector vector : calcVisibleVectors(vectoren, p)) {
                                Location loc = new Location(world, (double) vector.getBlockX(), (double) vector.getBlockY(), (double) vector.getBlockZ());
                                if (Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showfor", "player").equals("player")) {
                                    p.spawnParticle(Particle.FIREWORKS_SPARK, loc, 1,0,0,0, 0);
                                }
                                if (Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showfor", "player").equals("all")) {
                                    if (players.size() <= 0) {
                                        loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 1,0,0,0, 0);
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }.runTaskTimerAsynchronously(plugin, 0, 5);
            }

        }.runTaskAsynchronously(this.plugin);
    }

    public void loadgui(Player p) {
        String flagname = "Particle";
        if (p.hasPermission("ancient.regions.region.particle")) {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ArrayList<String> lore = new ArrayList<>();
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
                ArrayList<String> lore = new ArrayList<>();
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

