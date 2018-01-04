package de.moddylp.AncientRegions.region;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.region.Navigation;
import de.moddylp.AncientRegions.region.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BuyRegionGUI {
    private Main plugin;
    private Player p;
    private Inventory menu;
    private WorldGuardPlugin worldguard;

    public BuyRegionGUI(Player p, Main plugin, WorldGuardPlugin worldguard) {
        this.p = p;
        this.plugin = plugin;
        this.worldguard = worldguard;
        this.menu = Bukkit.createInventory(null, 27, ChatColor.GOLD + plugin.lang.getText("RegionBuy"));
    }

    private void loadMenuItems() {
        Region region1 = new Region(this.plugin, 1);
        region1.loadgui(this.menu, this.p, this.worldguard);
        Region region2 = new Region(this.plugin, 2);
        region2.loadgui(this.menu, this.p, this.worldguard);
        Region region3 = new Region(this.plugin, 3);
        region3.loadgui(this.menu, this.p, this.worldguard);
        Region region4 = new Region(this.plugin, 4);
        region4.loadgui(this.menu, this.p, this.worldguard);
        Navigation navi = new Navigation();
        navi.loadguiitems(this.menu, this.plugin);
    }

    public void open() {
        this.loadMenuItems();
        this.p.openInventory(this.menu);
    }

    public String getName() {
        return this.menu.getName();
    }

    public Inventory getMenu() {
        return this.menu;
    }
}

