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

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }



}
