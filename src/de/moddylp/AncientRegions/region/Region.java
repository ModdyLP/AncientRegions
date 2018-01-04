package de.moddylp.AncientRegions.region;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagUtil;
import de.moddylp.AncientRegions.loader.FileDriver;
import de.moddylp.AncientRegions.loader.WorldEditHandler6;
import net.milkbowl.vault.economy.Economy;
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
import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Region {
    private Integer intregionheight;
    private Integer intregiondepth;
    private Main plugin;
    private String permission;
    private String regionname;
    private double regionprice;
    private int regionsize;
    private int number;

    public Region(Main plugin, int number) {
        this.plugin = plugin;
        String region = "region" + number;
        this.number = number;
        this.permission = "buy" + region.toLowerCase();
        this.regionname = Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_" + region + "name");
        this.regionprice = Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_" + region + "price"));
        this.regionsize = Integer.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_" + region + "size"));
        this.intregionheight = Integer.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_regionheight"));
        this.intregiondepth = Integer.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_regiondepth"));
    }

    public void buy(final WorldGuardPlugin worldguard, final Player p, final InventoryClickEvent e, Inventory menu, WorldEditPlugin worldedit) {
        e.setCancelled(true);
        p.closeInventory();
        try {
            if (p.hasPermission("ancient.regions.region." + this.permission)) {
                RegionContainer container = worldguard.getRegionContainer();
                final RegionManager regions = container.get(p.getWorld());
                p.sendMessage( ChatColor.GREEN + "[AR][INFO] " + this.plugin.lang.getText("RegionCreation"));
                LocalPlayer ply = worldguard.wrapPlayer(p);
                if (regions != null) {
                    if (regions.getRegionCountOfPlayer(ply) < Integer.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_limit")) || p.hasPermission("ancient.regions.admin.bypassregion")) {
                        new BukkitRunnable() {

                            public void run() {
                                List<BlockVector> edges = edges(p);
                                if (checkRegionsExists(edges, worldguard, p) || p.hasPermission("ancient.regions.admin.bypassregion")) {
                                    String seperator = "-";
                                    int id = 1;
                                    while (regions.hasRegion(p.getName() + seperator + regionname + seperator + id)) {
                                        ++id;
                                    }
                                    ProtectedCuboidRegion region = new ProtectedCuboidRegion(p.getName() + seperator + regionname + seperator + id, edges.get(0), edges.get(2));
                                    ProtectedRegion grg = regions.getRegion("__global__");
                                    region.setPriority(Integer.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_regionpriority")));
                                    try {
                                        region.setParent(grg);
                                    } catch (ProtectedRegion.CircularInheritanceException e2) {
                                        e2.printStackTrace();
                                    }
                                    DefaultDomain owner = region.getOwners();
                                    owner.addPlayer(p.getUniqueId());
                                    if (payment(p, e) || p.hasPermission("ancient.regions.admin.bypass")) {
                                        if (!Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_standartdenyflags").contains("[]")) {
                                            JSONArray standartdenyflags = FileDriver.objectToJSONArray(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_standartdenyflags"));
                                            ArrayList<String> flags = new ArrayList<>();
                                            for (Object object : standartdenyflags) {
                                                flags.add(object.toString());
                                            }
                                            for (String flag : flags) {
                                                if (!Main.DRIVER.hasKey(Main.DRIVER.CONFIG, flag)) continue;
                                                region.setFlag(new StateFlag(flag, false),  StateFlag.State.DENY);
                                            }
                                        }
                                        if (Boolean.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_backuprg"))) {
                                            File schematic = new File(plugin.getDataFolder(), "/schematics/" + region.getId() + ".schematic");
                                            File dir = new File(plugin.getDataFolder(), "/schematics/");
                                            if (!dir.exists()) {
                                                dir.mkdirs();
                                            }
                                            if (new WorldEditHandler6(plugin).saveRegionBlocks(schematic, p.getName() + seperator + regionname + seperator + id, p, region)) {
                                                Main.getInstance().getLogger().info("BACKUP SUCCESS");
                                                regions.addRegion(region);
                                                p.sendMessage( ChatColor.GREEN + "[AR][INFO] " + plugin.lang.getText("Created") + "[ " + edges.get(0) + " | " + edges.get(2) + " ]");
                                                RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                                                gui.open();
                                            } else {
                                                Main.getInstance().getLogger().info("BACKUP ERROR");
                                                p.sendMessage( ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("ErrorCreate"));
                                            }
                                        } else {
                                            regions.addRegion(region);
                                            p.sendMessage( ChatColor.GREEN + "[AR][INFO] " + plugin.lang.getText("Created") + "[ " + edges.get(0) + " | " + edges.get(2) + " ]");
                                            RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                                            gui.open();
                                        }
                                    }
                                } else {
                                    p.sendMessage( ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("RegionExists"));
                                }
                            }
                        }.runTaskAsynchronously(this.plugin);
                    } else {
                        p.sendMessage( ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("ToMany"));
                    }
                }
            } else {
                p.sendMessage( ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Permission"));
                e.setCancelled(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.loadgui(menu, p, worldguard);
    }

    public void removeRegion(final WorldGuardPlugin worldguard, final Player p, final InventoryClickEvent e, Inventory menu) {
        e.setCancelled(true);
        p.closeInventory();
        if (p.hasPermission("ancient.regions.region.removeregion")) {
            p.sendMessage( ChatColor.GREEN + "[AR][INFO] " + this.plugin.lang.getText("Selling"));
            new BukkitRunnable() {

                public void run() {
                    RegionContainer container = worldguard.getRegionContainer();
                    RegionManager regions = container.get(p.getWorld());
                    Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
                    LocalPlayer ply = worldguard.wrapPlayer(p);
                    List region = null;
                    if (regions != null) {
                        region = regions.getApplicableRegionsIDs(pt);
                    }
                    if (region != null) {
                        if (region.isEmpty()) {
                            p.sendMessage( ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("GobalError"));
                        } else {
                            ProtectedRegion rg = regions.getRegion((String) region.get(0));
                            if (rg != null) {
                                if (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass")) {
                                    if (Boolean.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_backuprg"))) {
                                        p.sendMessage( ChatColor.GREEN + "[AR][INFO] " + plugin.lang.getText("Restore"));
                                        File file = new File(plugin.getDataFolder(), "/schematics/" + rg.getId() + ".schematic");
                                        BlockVector max = rg.getMaximumPoint();
                                        BlockVector min = rg.getMinimumPoint();
                                        Vector dimension = new Vector(max.getBlockX() - min.getBlockX() + 1, max.getBlockY() - min.getBlockY() + 1, max.getBlockZ() - min.getBlockZ() + 1);
                                        WorldEditHandler6 handler = new WorldEditHandler6(plugin);
                                        if (handler.restoreRegionBlocks(file, rg.getId(), p, rg, dimension)) {
                                            Main.getInstance().getLogger().info("RESTORE SUCCESS");
                                            regions.removeRegion(rg.getId());
                                            p.sendMessage( ChatColor.GREEN + "[AR][INFO] " + plugin.lang.getText("Removed").replace("[PH]", rg.getId()));
                                            give(p, e, rg.getId());
                                            RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                                            gui.open();
                                        } else {
                                            regions.removeRegion(rg.getId());
                                            Main.getInstance().getLogger().warning("RESTORE ERROR on "+rg.getId()+" money was payed back to customer.");
                                            p.sendMessage( ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("RemovedError").replace("[PH]", rg.getId()));
                                            give(p, e, rg.getId());
                                            RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                                            gui.open();
                                        }
                                    }
                                } else {
                                    String regionname = rg.getId();
                                    regions.removeRegion(regionname);
                                    p.sendMessage( ChatColor.GREEN + "[AR][INFO] " + plugin.lang.getText("Removed").replace("[PH]", rg.getId()));
                                    give(p, e, regionname);
                                    RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                                    gui.open();
                                }
                            } else {
                                p.sendMessage( ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Owner"));
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }.runTaskAsynchronously(this.plugin);
        } else {
            p.sendMessage( ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Permission"));
            e.setCancelled(true);
        }
    }

    private List<BlockVector> edges(Player p) {
        ArrayList<BlockVector> edges = new ArrayList<>();
        int halfregionsize = this.regionsize % 2 == 1 ? (this.regionsize + 1) / 2 : this.regionsize / 2;
        if (this.intregionheight == 9999) {
            edges.add(new BlockVector(p.getLocation().getBlockX() - halfregionsize, p.getWorld().getMaxHeight(), p.getLocation().getBlockZ() - halfregionsize));
            edges.add(new BlockVector(p.getLocation().getBlockX() + halfregionsize, p.getWorld().getMaxHeight(), p.getLocation().getBlockZ() - halfregionsize));
        } else {
            Double regionheight = (double) this.intregionheight;
            edges.add(new BlockVector((double) (p.getLocation().getBlockX() - halfregionsize), regionheight, (double) (p.getLocation().getBlockZ() - halfregionsize)));
            edges.add(new BlockVector((double) (p.getLocation().getBlockX() + halfregionsize), regionheight, (double) (p.getLocation().getBlockZ() - halfregionsize)));
        }
        if (this.intregionheight == 9999) {
            edges.add(new BlockVector(p.getLocation().getBlockX() + halfregionsize, 1, p.getLocation().getBlockZ() + halfregionsize));
            edges.add(new BlockVector(p.getLocation().getBlockX() + halfregionsize, 1, p.getLocation().getBlockZ() - halfregionsize));
        } else {
            Double regiondepth = (double) this.intregiondepth;
            edges.add(new BlockVector((double) (p.getLocation().getBlockX() + halfregionsize), regiondepth, (double) (p.getLocation().getBlockZ() + halfregionsize)));
            edges.add(new BlockVector((double) (p.getLocation().getBlockX() + halfregionsize), regiondepth, (double) (p.getLocation().getBlockZ() - halfregionsize)));
        }
        Main.getInstance().getLogger().info("Edges Initiated");
        return edges;
    }

    private boolean checkRegionsExists(List<BlockVector> edges, WorldGuardPlugin worldGuard, Player p) {
        Main.getInstance().getLogger().info("checking region");
        try {
            Vector KGK = edges.get(0);
            Vector GGK = edges.get(1);
            Vector GKG = edges.get(2);
            Vector GKK = edges.get(3);
            Vector KKK = new Vector(KGK.getX(), GKK.getY(), KGK.getZ());
            Vector GGG = new Vector(GGK.getX() + 1.0, GGK.getY(), GKG.getZ() + 1.0);
            Main.getInstance().getLogger().info( GGG + " " +  KKK);
            RegionContainer container = worldGuard.getRegionContainer();
            RegionManager regions = container.get(p.getWorld());
            Main.getInstance().getLogger().info("parameter set");
            while (KKK.getY() < GGG.getY()) {
                while (KKK.getX() < GGG.getX()) {
                    List<String> region = Objects.requireNonNull(regions).getApplicableRegionsIDs(KKK);
                    if (!region.isEmpty()) {
                        Main.getInstance().getLogger().info("!!! Check complete collision by" +  KKK);
                        return false;
                    }
                    while (KKK.getZ() < GGG.getZ()) {
                        region = regions.getApplicableRegionsIDs(KKK);
                        if (!region.isEmpty()) {
                            Main.getInstance().getLogger().info("!!! Check complete collision by" +  KKK);
                            return false;
                        }
                        KKK = new Vector(KKK.getX(), KKK.getY(), KKK.getZ() + 1.0);
                    }
                    KKK = new Vector(KKK.getX() + 1.0, KKK.getY(), KGK.getZ());
                }
                KKK = new Vector(KGK.getX(), KKK.getY() + 1.0, KKK.getZ());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        Main.getInstance().getLogger().info("Check complete no collision");
        return true;
    }

    public boolean payment(Player p, InventoryClickEvent e) {
        RegisteredServiceProvider economyProvider = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
        Economy vaultEcon = (Economy) economyProvider.getProvider();
        if (p.hasPermission("ancient.regions.admin.bypass")) {
            e.setCancelled(true);
            return true;
        }
        if (vaultEcon != null) {
            if (vaultEcon.getBalance(p) != 0.0 && vaultEcon.getBalance(p) >= this.regionprice) {
                vaultEcon.withdrawPlayer(p, this.regionprice);
                p.sendMessage( ChatColor.BLUE + "[AR][INFO]" + this.plugin.lang.getText("PayNote2").replace("[PH]", String.valueOf(this.regionprice) + " " + FlagUtil.loadCurrencyfromConfig()));
                e.setCancelled(true);
                return true;
            }
            p.sendMessage( ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("NoMoney"));
            e.setCancelled(true);
            return false;
        }
        p.sendMessage( ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("VaultError"));
        e.setCancelled(true);
        return false;
    }

    private void give(Player p, InventoryClickEvent e, String regionname) {
        RegisteredServiceProvider economyProvider = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
        Economy vaultEcon = (Economy) economyProvider.getProvider();
        if (p.hasPermission("ancient.regions.admin.bypass")) {
            e.setCancelled(true);
        }
        if (vaultEcon != null) {
            vaultEcon.depositPlayer(p, Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "region" + this.getregionnumber(regionname, p) + "price")) * Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_payback")) / 100.0);
            p.sendMessage( ChatColor.BLUE + "[AR][INFO]" + this.plugin.lang.getText("Payback").replace("[PH]", String.valueOf(Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "region" + this.getregionnumber(regionname, p) + "price")) * (Double.valueOf(Main.DRIVER.getPropertyOnly(Main.DRIVER.CONFIG, "_payback")) / 100.0)) + " " + FlagUtil.loadCurrencyfromConfig()));
            e.setCancelled(true);
        } else {
            p.sendMessage( ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("VaultError"));
            e.setCancelled(true);
        }
    }

    public boolean loadgui(Inventory menu, Player p, WorldGuardPlugin worldguard) {
        if (p.hasPermission("ancient.regions.region." + this.permission)) {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            switch (this.number) {
                case 1:
                    ITEM = new ItemStack(Material.IRON_BLOCK);
                    break;
                case 2:
                    ITEM = new ItemStack(Material.GOLD_BLOCK);
                    break;
                case 3:
                    ITEM = new ItemStack(Material.DIAMOND_BLOCK);
                    break;
                case 4:
                    ITEM = new ItemStack(Material.EMERALD_BLOCK);
                    break;
            }
            int realregionsize = this.regionsize % 2 == 1 ? this.regionsize + 2 : this.regionsize + 1;
            ArrayList<String> lore = new ArrayList<>();
            lore.add( ChatColor.GOLD + this.plugin.lang.getText("RegionName") + ": " +  ChatColor.YELLOW + this.regionname);
            lore.add( ChatColor.GOLD + this.plugin.lang.getText("RegionSize") + ": " +  ChatColor.YELLOW + realregionsize + " x " + realregionsize + " x H:" + this.intregionheight + " x D:" + this.intregiondepth);
            lore.add( ChatColor.GOLD + this.plugin.lang.getText("RegionPrice") + ": " +  ChatColor.YELLOW + String.valueOf(this.regionprice) + " " + FlagUtil.loadCurrencyfromConfig());
            ItemMeta imeta = ITEM.getItemMeta();
            imeta.setDisplayName( ChatColor.GOLD + this.plugin.lang.getText("Buy") + "'" + this.regionname + "' Region");
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
            switch (this.number) {
                case 1:
                    menu.setItem(2, ITEM);
                    break;
                case 2:
                    menu.setItem(3, ITEM);
                    break;
                case 3:
                    menu.setItem(5, ITEM);
                    break;
                case 4:
                    menu.setItem(6, ITEM);
                    break;
            }
        } else {
            ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            FlagUtil.checkPermInv(ITEM, this.regionname);
            menu.setItem(this.number - 1, ITEM);
        }
        return true;
    }

    private int getregionnumber(String regioname, Player p) {
        String numbername = regioname.replaceAll("-", "").replaceAll("_", "").replaceAll(p.getName().toLowerCase(), "");
        String option = Main.DRIVER.getPropertyByValue(Main.DRIVER.CONFIG, numbername.substring(0, numbername.length() - 1));
        if (option != null) {
            return Integer.valueOf(option.replace("region", "").replace("name", ""));
        }
        return 0;
    }

}

