package de.moddylp.AncientRegions.flags;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import de.moddylp.AncientRegions.Main;

public class EditFlagsNavigation2 {
	public EditFlagsNavigation2(){
		
	}
	public boolean loadguiitems(Inventory menu, Main plugin) {
		ItemStack Backarrow = new ItemStack(Material.TIPPED_ARROW);
		PotionMeta pmeta = (PotionMeta) Backarrow.getItemMeta();
		pmeta.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
		Backarrow.setItemMeta(pmeta);
	    ItemMeta imeta = Backarrow.getItemMeta();
	    imeta.setDisplayName(ChatColor.RED + plugin.lang.getText("Back"));
	    imeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    imeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
	    Backarrow.setItemMeta(imeta);
	    menu.setItem(45, Backarrow);
	     
	    ItemStack Mainmenu = new ItemStack(Material.TIPPED_ARROW);
	    PotionMeta pmeta2 = (PotionMeta) Mainmenu.getItemMeta();
		pmeta2.setBasePotionData(new PotionData(PotionType.NIGHT_VISION));
		Mainmenu.setItemMeta(pmeta2);
	    ItemMeta imeta2 = Mainmenu.getItemMeta();
	    imeta2.setDisplayName(ChatColor.DARK_BLUE + plugin.lang.getText("Mainmenu"));
	    imeta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    imeta2.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
	    Mainmenu.setItemMeta(imeta2);
	    menu.setItem(53, Mainmenu);
	     
	    ItemStack Panel1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)0);
	    ItemMeta PanelMeta1 = Panel1.getItemMeta();
	    PanelMeta1.setDisplayName(" ");
	    PanelMeta1.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    Panel1.setItemMeta(PanelMeta1);
	    menu.setItem(36, Panel1);
	    menu.setItem(37, Panel1);
	    menu.setItem(43, Panel1);
	    menu.setItem(44, Panel1);
	    menu.setItem(46, Panel1);
	    menu.setItem(52, Panel1);
	     
	    return true;
	}
}
