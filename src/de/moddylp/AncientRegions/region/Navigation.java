package de.moddylp.AncientRegions.region;

import de.moddylp.AncientRegions.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class Navigation {
    public boolean loadguiitems(Inventory menu, Main plugin) {
        ItemStack Mainmenu = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta pmeta2 = (PotionMeta)Mainmenu.getItemMeta();
        pmeta2.setBasePotionData(new PotionData(PotionType.NIGHT_VISION));
        Mainmenu.setItemMeta(pmeta2);
        ItemMeta imeta2 = Mainmenu.getItemMeta();
        imeta2.setDisplayName(ChatColor.DARK_BLUE + plugin.lang.getText("Mainmenu"));
        imeta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        imeta2.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        Mainmenu.setItemMeta(imeta2);
        menu.setItem(26, Mainmenu);
        ItemStack Mainmenu2 = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta pmeta3 = (PotionMeta)Mainmenu2.getItemMeta();
        pmeta3.setBasePotionData(new PotionData(PotionType.INSTANT_HEAL));
        Mainmenu2.setItemMeta(pmeta3);
        ItemMeta imeta3 = Mainmenu2.getItemMeta();
        imeta3.setDisplayName(ChatColor.RED + plugin.lang.getText("Back"));
        imeta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        imeta3.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        Mainmenu2.setItemMeta(imeta3);
        menu.setItem(18, Mainmenu2);
        return true;
    }
}

