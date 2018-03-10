package de.moddylp.AncientRegions.region;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagUtil;
import de.moddylp.AncientRegions.gui.Events.ActivateMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class MembersGUI {
    private Main plugin;
    private Player p;
    private Inventory menu;
    private WorldGuardPlugin worldguard;

    public MembersGUI(Player p, Main plugin, WorldGuardPlugin worldguard) {
        this.p = p;
        this.plugin = plugin;
        this.worldguard = worldguard;
    }

    private void loadregionskulls(WorldGuardPlugin worldguard) {
        try {
            RegionContainer container = worldguard.getRegionContainer();
            RegionManager regions = container.get(this.p.getWorld());
            Vector pt = new Vector(this.p.getLocation().getX(), this.p.getLocation().getY(), this.p.getLocation().getZ());
            LocalPlayer ply = worldguard.wrapPlayer(this.p);
            List region = Objects.requireNonNull(regions).getApplicableRegionsIDs(pt);
            if (region.isEmpty()) {
                this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("GobalError"));
            } else {
                ProtectedRegion rg = regions.getRegion((String) region.get(0));
                if (Objects.requireNonNull(rg).isOwner(ply) || this.p.hasPermission("ancient.regions.admin.bypass")) {
                    Set<UUID> players = rg.getMembers().getUniqueIds();
                    for (UUID p : players) {
                        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                        ItemMeta meta = skull.getItemMeta();
                        try {
                            if (meta instanceof SkullMeta && (((SkullMeta) meta).hasOwner() || !((SkullMeta) meta).hasOwner())) {
                                ((SkullMeta) meta).setOwningPlayer(this.playername(p));
                            }
                        } catch (Throwable ex) {
                            if ((((SkullMeta) meta).hasOwner() || !((SkullMeta) meta).hasOwner())) {
                                if (this.playername(p) != null) {
                                    ((SkullMeta) meta).setOwner(this.playername(p).getName());
                                }
                            }
                        }
                        meta.setDisplayName(ChatColor.GREEN + Objects.requireNonNull(this.playername(p)).getName());
                        skull.setItemMeta(meta);
                        this.menu.addItem(skull);
                    }
                } else {
                    ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                    ItemMeta imeta = ITEM.getItemMeta();
                    imeta.setDisplayName(ChatColor.RED + this.plugin.lang.getText("Owner"));
                    imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    ITEM.setItemMeta(imeta);
                    this.menu.addItem(ITEM);
                }
            }
            Navigation2 navi = new Navigation2();
            navi.loadguiitems(this.menu, this.plugin);
        } catch (Throwable ex) {
            Main.getInstance().getLogger().warning("Minecraft Version incompatible: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadserverskulls() {
        try {
            Collection<? extends Player> players = this.plugin.getServer().getOnlinePlayers();
            for (Player p : players) {
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                ItemMeta meta = skull.getItemMeta();
                try {
                    if (meta instanceof SkullMeta && (((SkullMeta) meta).hasOwner() || !((SkullMeta) meta).hasOwner())) {
                        ((SkullMeta) meta).setOwningPlayer(this.playername(p.getUniqueId()));
                    }
                } catch (Throwable ex) {
                    if ((((SkullMeta) meta).hasOwner() || !((SkullMeta) meta).hasOwner())) {
                        if (this.playername(p.getUniqueId()) != null) {
                            ((SkullMeta) meta).setOwner(this.playername(p.getUniqueId()).getName());
                        }
                    }
                }
                meta.setDisplayName(ChatColor.GREEN + p.getName());
                skull.setItemMeta(meta);
                this.menu.addItem(skull);
            }
            Navigation2 navi = new Navigation2();
            navi.loadguiitems(this.menu, this.plugin);
        } catch (Throwable ex) {
            Main.getInstance().getLogger().warning("Minecraft Version incompatible: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void addMember(InventoryClickEvent e) {
        this.p.closeInventory();
        this.p.sendMessage(ChatColor.GOLD + this.plugin.lang.getText("Playername"));
        this.plugin.getServer().getPluginManager().registerEvents(new SetValueFromChatEvent(this.p, this.plugin.lang.getText("AddMember"), this.plugin, this.worldguard), this.plugin);
    }

    public void removeMember(UUID uuid, InventoryClickEvent e, String name) {
        try {
            if (this.p.hasPermission("ancient.regions.region.removemember")) {
                RegionContainer container = this.worldguard.getRegionContainer();
                RegionManager regions = container.get(this.p.getWorld());
                Vector pt = new Vector(this.p.getLocation().getX(), this.p.getLocation().getY(), this.p.getLocation().getZ());
                LocalPlayer ply = this.worldguard.wrapPlayer(this.p);
                List<String> region = Objects.requireNonNull(regions).getApplicableRegionsIDs(pt);
                if (region.isEmpty()) {
                    this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion(region.get(0));
                    if (Objects.requireNonNull(rg).isOwner(ply) || this.p.hasPermission("ancient.regions.admin.bypass")) {
                        if (FlagUtil.payment(this.p, e, "_removemember", ActivateMode.ACTIVATE) || this.p.hasPermission("ancient.regions.admin.bypass")) {
                            DefaultDomain member = rg.getMembers();
                            member.removePlayer(uuid);
                            rg.setMembers(member);
                            this.p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + this.plugin.lang.getText("PlayerRemoved").replace("[PH]", name));
                            container.reload();
                            this.p.closeInventory();
                            RegionManageGUI gui = new RegionManageGUI(this.p, this.plugin, this.worldguard);
                            gui.open();
                        } else {
                            this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("NoMoney"));
                            e.setCancelled(true);
                        }
                    } else {
                        this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Owner"));
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                }
            } else {
                this.p.sendMessage(ChatColor.RED + "[AR][ERROR] " + this.plugin.lang.getText("Permission"));
                e.setCancelled(true);
            }
            this.loadregionskulls(this.worldguard);
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("Minecraft Version incompatible: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private OfflinePlayer playername(UUID p) {
        OfflinePlayer[] allplayers = this.plugin.getServer().getOfflinePlayers();
        for (int i = 0; allplayers.length >= i; ++i) {
            UUID uuidname = allplayers[i].getUniqueId();
            if (!p.equals(uuidname)) continue;
            return allplayers[i];
        }
        return null;
    }

    public void changeowner(InventoryClickEvent e) {
        this.p.closeInventory();
        this.p.sendMessage(ChatColor.GOLD + this.plugin.lang.getText("Playername"));
        this.plugin.getServer().getPluginManager().registerEvents(new SetValueFromChatEvent(this.p, this.plugin.lang.getText("ChangeOwner"), this.plugin, this.worldguard), this.plugin);
    }

    public void openregion() {
        this.menu = Bukkit.createInventory(null, 54, ChatColor.GOLD + this.plugin.lang.getText("RemoveMember"));
        this.loadregionskulls(this.worldguard);
        this.p.openInventory(this.menu);
    }

    public void openserver() {
        this.menu = Bukkit.createInventory(null, 54, ChatColor.GOLD + this.plugin.lang.getText("AddMember"));
        this.loadserverskulls();
        this.p.openInventory(this.menu);
    }

    public void openserver2() {
        this.menu = Bukkit.createInventory(null, 54, ChatColor.GOLD + this.plugin.lang.getText("SetOwner"));
        this.loadserverskulls();
        this.p.openInventory(this.menu);
    }

    public String getName() {
        return this.menu.getName();
    }

    public Inventory getMenu() {
        return this.menu;
    }
}

