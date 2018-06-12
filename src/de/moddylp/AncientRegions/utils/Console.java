package de.moddylp.AncientRegions.utils;

import de.moddylp.AncientRegions.Main;
import org.bukkit.ChatColor;

public class Console {

    public static void send(String string) {
        Main.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.GOLD+"[ANCIENTREGIONS] "+string);
    }
    public static void error(String string) {
        Main.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED+"[ANCIENTREGIONS] "+string);
    }



}
