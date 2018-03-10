package de.moddylp.AncientRegions.loader;

import de.moddylp.AncientRegions.Main;

public class ConfigLoader {
    public static void saveDefaultconfig() {
        Main.getInstance().getLogger().info("Loading Config Default Options");
        Main.getInstance().getLogger().info("Config: " + Main.getInstance().getDataFolder() + "/config.json");
        Main.DRIVER.createNewFile();
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_111", "If you need help, than please look on the wiki site");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_111wikisite", "https://git.moddylp.de/ModdyLP/AncientRegions/wiki");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_metrics", true);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_currency", "EURO");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_limit", 4);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_worlds", new String[]{"world"});
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_backuprg", false);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showtimeofparticle", 20);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showfor", "player");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region1name", "Small");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region1size", 9);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region1price", 2500);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region2name", "Medium");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region2size", 21);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region2price", 5000);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region3name", "Big");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region3size", 60);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region3price", 7500);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region4name", "Extrem");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region4size", 99);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region4price", 10000);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_addmember", 500);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_changeowner", 1200);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_removemember", 100);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regiondesc", "Set the value to 9999 to use the maximum. The height and depth are absolute cordinates like 64 for floor");
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regionheight", 125);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regiondepth", 54);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_removecostpercent", 50);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_deactivatecostpercent", 100);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_activatecostpercent", 100);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_payback", 10);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regionpriority", 10);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_standartdenyflags", new String[0]);
        Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_particleshowrange", 16);
        Main.DRIVER.saveJson();
    }
}

