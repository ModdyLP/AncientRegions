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

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    public void loadregionskulls(WorldGuardPlugin worldguard) {
        try {
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
                    Set<UUID> players = rg.getMembers().getUniqueIds();
                    for (UUID p : players) {
                        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                        SkullMeta meta = (SkullMeta) skull.getItemMeta();
                        meta.setOwningPlayer(playername(p));
                        meta.setDisplayName(ChatColor.GREEN + playername(p).getName());
                        skull.setItemMeta(meta);
                        menu.addItem(skull);
                    }
                } else {
                    ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                    ItemMeta imeta = ITEM.getItemMeta();
                    imeta.setDisplayName(ChatColor.RED + plugin.lang.getText("Owner"));
                    imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    ITEM.setItemMeta(imeta);
                    menu.addItem(ITEM);
                }
            }
            Navigation2 navi = new Navigation2();
            navi.loadguiitems(menu, plugin);
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("Minecraft Version incompatible: " + ex.getMessage());
        }
    }

    public void loadserverskulls() {
        try {
            Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
            for (Player p : players) {
                ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwningPlayer(playername(p.getUniqueId()));
                meta.setDisplayName(ChatColor.GREEN + p.getName());
                skull.setItemMeta(meta);
                menu.addItem(skull);
            }
            Navigation2 navi = new Navigation2();
            navi.loadguiitems(menu, plugin);
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("Minecraft Version incompatible: " + ex.getMessage());
        }
    }

    public void addMember(InventoryClickEvent e) {
        p.closeInventory();
        p.sendMessage(ChatColor.GOLD + plugin.lang.getText("Playername"));
        plugin.getServer().getPluginManager().registerEvents(new SetValueFromChatEvent(p, plugin.lang.getText("AddMember"), plugin, worldguard), plugin);
    }

    public void removeMember(UUID uuid, InventoryClickEvent e, String name) {
        try {
            if (p.hasPermission("ancient.regions.region.removemember")) {
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
                        if (FlagUtil.payment(p, e, "_removemember") || p.hasPermission("ancient.regions.admin.bypass")) {
                            DefaultDomain member;
                            member = rg.getMembers();
                            member.removePlayer(uuid);
                            rg.setMembers(member);
                            p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + plugin.lang.getText("PlayerRemoved").replace("[PH]", name));
                            container.reload();
                            p.closeInventory();
                            RegionManageGUI gui = new RegionManageGUI(p, plugin, worldguard);
                            gui.open();
                        } else {
                            p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("NoMoney"));
                            e.setCancelled(true);
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Owner"));
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                }
            } else {
                p.sendMessage(ChatColor.RED + "[AR][ERROR] " + plugin.lang.getText("Permission"));
                e.setCancelled(true);
            }
            loadregionskulls(worldguard);
        } catch (Exception ex) {
            Main.getInstance().getLogger().warning("Minecraft Version incompatible: " + ex.getMessage());
        }
    }

    public OfflinePlayer playername(UUID p) {
        OfflinePlayer[] allplayers = plugin.getServer().getOfflinePlayers();
        for (int i = 0; allplayers.length >= i; i++) {
            UUID uuidname = allplayers[i].getUniqueId();
            if (p.equals(uuidname)) {
                return allplayers[i];
            }

        }
        return null;
    }

    public void changeowner(InventoryClickEvent e) {
        p.closeInventory();
        p.sendMessage(ChatColor.GOLD + plugin.lang.getText("Playername"));
        plugin.getServer().getPluginManager().registerEvents(new SetValueFromChatEvent(p, plugin.lang.getText("ChangeOwner"), plugin, worldguard), plugin);
    }

    public void openregion() {
        this.menu = Bukkit.createInventory(null, 54, ChatColor.GOLD + plugin.lang.getText("RemoveMember"));
        loadregionskulls(worldguard);
        p.openInventory(menu);

    }

    public void openserver() {
        this.menu = Bukkit.createInventory(null, 54, ChatColor.GOLD + plugin.lang.getText("AddMember"));
        loadserverskulls();
        p.openInventory(menu);

    }

    public void openserver2() {
        this.menu = Bukkit.createInventory(null, 54, ChatColor.GOLD + plugin.lang.getText("SetOwner"));
        loadserverskulls();
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
