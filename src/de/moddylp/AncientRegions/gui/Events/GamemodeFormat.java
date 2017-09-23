package de.moddylp.AncientRegions.gui.Events;

import com.sk89q.worldguard.session.handler.GameModeFlag;
import de.moddylp.AncientRegions.loader.LoadConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.EditflagsPage2;
import org.bukkit.plugin.RegisteredServiceProvider;

public class GamemodeFormat implements Listener {
	private Player p;
	private Main plugin;
	private EnumFlag<GameMode> flag;
	private ProtectedRegion rg;
	private WorldGuardPlugin worldguard;
	private String flagname;

	public GamemodeFormat(Player p, ProtectedRegion rg, EnumFlag<GameMode> flag2, String flagname, Main plugin, WorldGuardPlugin worldguard) {
		this.p = p;
		this.flag = flag2;
		this.rg = rg;
		this.plugin = plugin;
		this.flagname = flagname;
		this.worldguard = worldguard;
	}
	@EventHandler
	public String getChat(AsyncPlayerChatEvent e) {
		if (e.getPlayer().equals(p)) {
			String msg = e.getMessage().toString();
			if (msg.contains("creative") || msg.contains("c") || msg.contains("1")) {
				rg.setFlag(flag, GameMode.valueOf("creative"));
				p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
				EditflagsPage2 gui = new EditflagsPage2(p, plugin, worldguard);
				gui.open();
				e.setCancelled(true);
				HandlerList.unregisterAll(this);
			} else if(msg.contains("survival") || msg.contains("sv") || msg.contains("0")) {
				rg.setFlag(flag, GameMode.valueOf("survival"));
				p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
				EditflagsPage2 gui = new EditflagsPage2(p, plugin, worldguard);
				gui.open();
				e.setCancelled(true);
				HandlerList.unregisterAll(this);
			} else if(msg.contains("spectator") || msg.contains("sp") || msg.contains("3")) {
				rg.setFlag(flag, GameMode.valueOf("spectator"));
				p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
				EditflagsPage2 gui = new EditflagsPage2(p, plugin, worldguard);
				gui.open();
				e.setCancelled(true);
				HandlerList.unregisterAll(this);
			} else if(msg.contains("adventure") || msg.contains("a") || msg.contains("2")) {
				rg.setFlag(flag, GameMode.valueOf("adventure"));
				p.sendMessage(ChatColor.GREEN+"[AR][INFO]"+plugin.lang.getText("ValueChat").replace("[PH]", flagname));
				EditflagsPage2 gui = new EditflagsPage2(p, plugin, worldguard);
				gui.open();
				e.setCancelled(true);
				HandlerList.unregisterAll(this);
			} else {
				p.sendMessage(ChatColor.RED+"[AR][ERROR] "+plugin.lang.getText("InvalidGamemode"));
				e.setCancelled(true);
			}
		}
		return null;
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
	
}
