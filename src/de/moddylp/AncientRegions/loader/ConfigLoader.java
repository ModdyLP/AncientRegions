package de.moddylp.AncientRegions.loader;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.config.SimpleConfig;

import java.util.ArrayList;

public class ConfigLoader {
    public static void saveDefaultconfig() {
        SimpleConfig config = Main.getInstance().getMainConfig();
        Main.getInstance().getLogger().info("Loading Config Default Options");
        Main.getInstance().getLogger().info("Config: " + config.getPath());

        Main.getInstance().getMainConfig().get("main.language", "en", "Define Language code");
        Main.getInstance().getMainConfig().get("main.metrics", true, "Define if the Plugin sends Usage statistics.");
        Main.getInstance().getMainConfig().get("main.worlds", new String[]{"world"}, "Define in which worlds the plugin is active.");
        Main.getInstance().getMainConfig().get("main.backuprg", false, "Define if the plugin should backup the region before each purchase.");

        Main.getInstance().getMainConfig().get("eco.currency", "Euro");
        Main.getInstance().getMainConfig().get("eco.removecostpercent", 50);
        Main.getInstance().getMainConfig().get("eco.deactivatecostpercent", 100);
        Main.getInstance().getMainConfig().get("eco.activatecostpercent", 100);
        Main.getInstance().getMainConfig().get("eco.paybackpercent", 10);

        Main.getInstance().getMainConfig().get("particle.particleshowrange", 16);
        Main.getInstance().getMainConfig().get("particle.showtimeofparticle", 20);
        Main.getInstance().getMainConfig().get("particle.showfor", "player");

        Main.getInstance().getMainConfig().get("region.regionpriority", 10);
        Main.getInstance().getMainConfig().get("region.limit", 4);
        Main.getInstance().getMainConfig().get("region.standartdenyflags", new String[]{});
        Main.getInstance().getMainConfig().get("region.standartallowflags", new String[]{});

        Main.getInstance().getMainConfig().get("region.region1name", "Small");
        Main.getInstance().getMainConfig().get("region.region1size", 9);
        Main.getInstance().getMainConfig().get("region.region1price", 2500);
        Main.getInstance().getMainConfig().get("region.region2name", "Medium");
        Main.getInstance().getMainConfig().get("region.region2size", 21);
        Main.getInstance().getMainConfig().get("region.region2price", 5000);
        Main.getInstance().getMainConfig().get("region.region3name", "Big");
        Main.getInstance().getMainConfig().get("region.region3size", 60);
        Main.getInstance().getMainConfig().get("region.region3price", 7500);
        Main.getInstance().getMainConfig().get("region.region4name", "Extrem");
        Main.getInstance().getMainConfig().get("region.region4size", 99);
        Main.getInstance().getMainConfig().get("region.region4price", 10000);
        Main.getInstance().getMainConfig().get("region.regionheight", 125
                , new String[]{"Set the value to 9999 to use the maximum. "
                        , "The height and depth are absolute cordinates like 64 for floor"});
        Main.getInstance().getMainConfig().get("region.regiondepth", 54
                , new String[]{"Set the value to 9999 to use the maximum. "
                        , "The height and depth are absolute cordinates like 64 for floor"});

        Main.getInstance().getMainConfig().get("manage.addmember", 500);
        Main.getInstance().getMainConfig().get("manage.changeowner", 1200);
        Main.getInstance().getMainConfig().get("manage.removemember", 100);

        if (Main.DRIVER.checkIfFileExists(Main.DRIVER.CONFIG)) {
            Main.getInstance().getMainConfig().set("main.metrics", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_metrics", true));
            Main.getInstance().getMainConfig().set("eco.currency", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_currency", "EURO"));
            Main.getInstance().getMainConfig().set("region.limit",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_limit", 4));
            Main.getInstance().getMainConfig().set("main.worlds",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_worlds", new String[]{"world"}));
            Main.getInstance().getMainConfig().set("main.backuprg",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_backuprg", false));
            Main.getInstance().getMainConfig().set("particle.showtimeofparticle", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showtimeofparticle", 20));
            Main.getInstance().getMainConfig().set("particle.showfor",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showfor", "player"));
            Main.getInstance().getMainConfig().set("region.region1name",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region1name", "Small"));
            Main.getInstance().getMainConfig().set("region.region1size",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region1size", 9));
            Main.getInstance().getMainConfig().set("region.region1price",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region1price", 2500));
            Main.getInstance().getMainConfig().set("region.region2name",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region2name", "Medium"));
            Main.getInstance().getMainConfig().set("region.region2size",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region2size", 21));
            Main.getInstance().getMainConfig().set("region.region2price",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region2price", 5000));
            Main.getInstance().getMainConfig().set("region.region3name",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region3name", "Big"));
            Main.getInstance().getMainConfig().set("region.region3size",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region3size", 60));
            Main.getInstance().getMainConfig().set("region.region3price",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region3price", 7500));
            Main.getInstance().getMainConfig().set("region.region4name",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region4name", "Extrem"));
            Main.getInstance().getMainConfig().set("region.region4size",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region4size", 99));
            Main.getInstance().getMainConfig().set("region.region4price",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region4price", 10000));
            Main.getInstance().getMainConfig().set("manage.addmember",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_addmember", 500));
            Main.getInstance().getMainConfig().set("manage.changeowner",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_changeowner", 1200));
            Main.getInstance().getMainConfig().set("manage.removemember",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_removemember", 100));
            Main.getInstance().getMainConfig().set("region.regionheight",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regionheight", 125));
            Main.getInstance().getMainConfig().set("region.regiondepth",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regiondepth", 54));
            Main.getInstance().getMainConfig().set("eco.removecostpercent", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_removecostpercent", 50));
            Main.getInstance().getMainConfig().set("eco.deactivatecostpercent", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_deactivatecostpercent", 100));
            Main.getInstance().getMainConfig().set("eco.activatecostpercent", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_activatecostpercent", 100));
            Main.getInstance().getMainConfig().set("eco.paybackpercent", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_payback", 10));
            Main.getInstance().getMainConfig().set("region.regionpriority", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regionpriority", 10));
            Main.getInstance().getMainConfig().set("region.standartdenyflags", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_standartdenyflags", new String[0]));
            Main.getInstance().getMainConfig().set("region.standartallowflags", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_standartallowflags", new String[0]));
            Main.getInstance().getMainConfig().set("particle.particleshowrange", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_particleshowrange", 16));

            Main.DRIVER.deletefile(Main.DRIVER.CONFIG);
        }
        Main.getInstance().getMainConfig().saveConfig();

    }
}

