package de.moddylp.AncientRegions.loader;

import de.moddylp.AncientRegions.Main;

/**
 * Created by N.Hartmann on 28.09.2017.
 * Copyright 2017
 */
public class ConfigLoader {
    public static void saveDefaultconfig() {
        Main.getInstance().getLogger().info("Loading Config Default Options");
        Main.getInstance().getLogger().info("Config: "+Main.getInstance().getDataFolder()+"/config.json");
        Main.DRIVER.createNewFile();
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"wikisite", "https://git.moddylp.de/ModdyLP/AncientRegions/wiki");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"metrics", true);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"currency", "EURO");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"limit", 4);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"worlds", new String[]{"world"});
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"backuprg", false);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"showtimeofparticle", 20);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"showfor", "player");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region1name", "Small");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region1size", 9);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region1price", 2500);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region2name", "Medium");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region2size", 21);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region2price", 5000);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region3name", "Big");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region3size", 60);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region3price", 7500);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region4name", "Extrem");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region4size", 99);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"region4price", 10000);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"addmember", 500);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"changeowner", 1200);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"removemember", 100);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"regiondesc", "Set the " +
                "value to 9999 to use the maximum. The height and depth are absolute cordinates like 64 for floor");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"regionheight", 125);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"regiondepth", 54);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"payback", 10);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"regionpriority", 10);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG,"standartdenyflags", new String[]{});
        Main.DRIVER.saveJson();
    }
}
