package de.moddylp.AncientRegions.flags;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Events.WeatherFormat;
import de.moddylp.AncientRegions.loader.LoadConfig;
import net.milkbowl.vault.economy.Economy;

public class WeatherLock {
	//Flag Description
	EnumFlag<WeatherType> flag = DefaultFlag.WEATHER_LOCK;
	String flagname = "WeatherLock";
	String permission = flagname.toLowerCase();
	private Main plugin;
	public WeatherLock(Main plugin){
		this.plugin = plugin;
	}
	public boolean toggle(WorldGuardPlugin worldguard, Player p, InventoryClickEvent e, Inventory menu) {
		if(p.hasPermission("ancient.regions.flag."+permission)){
			RegionContainer container = worldguard.getRegionContainer();
			RegionManager regions = container.get(p.getWorld());
			Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(),p.getLocation().getZ());
			LocalPlayer ply = worldguard.wrapPlayer(p);
			List<String> region = regions.getApplicableRegionsIDs(pt);
			if (region.isEmpty()) {
				p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("GobalError"));
			} else {
    			ProtectedRegion rg = regions.getRegion(region.get(0));
    			if (rg.isOwner(ply)|| p.hasPermission("ancient.regions.admin.bypass")) {
    				if (rg.getFlag(flag) != null) {
    					rg.setFlag(flag, null);
    					p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+ChatColor.GOLD+" "+flagname+plugin.lang.getText("FlagRemoved"));
    				} else {
    					p.closeInventory();
    					p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("WeatherInfo"));
    					plugin.getServer().getPluginManager().registerEvents(new WeatherFormat(p, rg, flag, flagname, plugin, worldguard),plugin);
    				}
    			} else {
    				p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("Owner"));
    				e.setCancelled(true);
    			}
    			e.setCancelled(true);
			}
		} else {
			p.sendMessage(ChatColor.RED+ "[AR][ERROR] "+plugin.lang.getText("Permission"));
			e.setCancelled(true);
		}
		loadgui(menu, p, worldguard);
		return false;
	}
	public String loadPricefromConfig() {
		try {
			LoadConfig config = new LoadConfig(plugin);
			String price = config.getOption(flagname.toLowerCase());
			return price;
		} catch( Exception ex) {
			plugin.getLogger().info(ex.toString());
		}
		return null;
	}
	public String loadCurrencyfromConfig() {
		try {
			LoadConfig config = new LoadConfig(plugin);
			String currency = config.getOption("currency");
			return currency;
		} catch( Exception ex) {
			plugin.getLogger().info(ex.toString());
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	public boolean payment(Player p, InventoryClickEvent e) {
		RegisteredServiceProvider<Economy> service = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		Economy vaultEcon = service.getProvider();
		if(p.hasPermission("ancient.regions.admin.bypass")) {
			e.setCancelled(true);
			return true;
		}
		if (vaultEcon != null) {
			String price = loadPricefromConfig();
			if(vaultEcon.getBalance(p.getName()) != 0 && vaultEcon.getBalance(p.getName()) >= Double.valueOf(price)) {
				vaultEcon.withdrawPlayer(p.getName(), Double.valueOf(price));
				p.sendMessage(ChatColor.BLUE+"[AR][INFO]"+plugin.lang.getText("PayNote").replace("[PH]", loadPricefromConfig()+" "+loadCurrencyfromConfig()));
				e.setCancelled(true);
				return true;
			} else {
				p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("NoMoney"));
				e.setCancelled(true);
				return false;
			}
		} else {
			p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("VaultError"));
			e.setCancelled(true);
		}
		return false;
	}
	public boolean loadgui(Inventory menu, Player p, WorldGuardPlugin worldguard) {
		if (p.hasPermission("ancient.regions.flag."+permission)){
			ItemStack ITEM = new ItemStack(Material.FIREBALL);
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.GOLD+plugin.lang.getText("Set").replace("[PH]", flagname));
            lore.add(ChatColor.YELLOW+loadPricefromConfig()+" "+loadCurrencyfromConfig());
            if (!isSet(worldguard, p).equals("null")) {
            	lore.add(ChatColor.GOLD+plugin.lang.getText("Current")+": "+ChatColor.AQUA+isSet(worldguard, p));
            }
            ItemMeta imeta = ITEM.getItemMeta();
            if (!isSet(worldguard, p).equals("null")) {
            	imeta.setDisplayName(ChatColor.GREEN+"[ON] "+ChatColor.GOLD+plugin.lang.getText("s")+flagname);
			} else {
				imeta.setDisplayName(ChatColor.BLUE+"[/] "+ChatColor.GOLD+plugin.lang.getText("s")+flagname);
			}
            imeta.setLore(lore);
            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ITEM.setItemMeta(imeta);
	       menu.setItem(30, ITEM);
		} else {
			ItemStack ITEM = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14);
			if (ITEM.getItemMeta().getLore() == null) {
	            List<String> lore = new ArrayList<String>();
	            lore.add(ChatColor.RED+plugin.lang.getText("Permission"));
	            ItemMeta imeta = ITEM.getItemMeta();
	            imeta.setDisplayName("§4[OFF] "+plugin.lang.getText("s")+flagname);
	            imeta.setLore(lore);
	            imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	            ITEM.setItemMeta(imeta);
	        }
       menu.setItem(30, ITEM);
		}
	return true;
	}
	public String isSet(WorldGuardPlugin worldguard, Player p) {
		RegionContainer container = worldguard.getRegionContainer();
		RegionManager regions = container.get(p.getWorld());
		Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(),p.getLocation().getZ());
		List<String> region = regions.getApplicableRegionsIDs(pt);
		if (!region.isEmpty()) {
			ProtectedRegion rg = regions.getRegion(region.get(0));
			if(rg.getFlag(flag) == null) {
				return "null";
			} else {
				return rg.getFlag(flag).toString();
			}
		}
		return "null";
	}
}
