package de.moddylp.AncientRegions.region;

import de.moddylp.AncientRegions.Language;
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

public class RegionManageNavigation {
    public boolean loadguiitems(Inventory menu, Main plugin) {
        ItemStack Mainmenu = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta pmeta2 = (PotionMeta)Mainmenu.getItemMeta();
        pmeta2.setBasePotionData(new PotionData(PotionType.NIGHT_VISION));
        Mainmenu.setItemMeta((ItemMeta)pmeta2);
        ItemMeta imeta2 = Mainmenu.getItemMeta();
        imeta2.setDisplayName(ChatColor.DARK_BLUE + plugin.lang.getText("Mainmenu"));
        imeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        imeta2.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_POTION_EFFECTS});
        Mainmenu.setItemMeta(imeta2);
        menu.setItem(9, Mainmenu);
        return true;
    }
}

