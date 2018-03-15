package de.moddylp.AncientRegions.loader;

import de.moddylp.AncientRegions.Main;
import de.moddylp.simplecommentconfig.Config;

public class ConfigLoader {
    public static void saveDefaultconfig() {
        Config config = Main.getInstance().getMainConfig();
        Main.getInstance().getLogger().info("Loading Config Default Options");

        config.get("main.language", "en", "Define Language code");
        config.get("main.metrics", true, "Define if the Plugin sends Usage statistics.");
        config.get("main.worlds", new String[]{"world"}, "Define in which worlds the plugin is active.");
        config.get("main.backuprg", false, "Define if the plugin should backup the region before each purchase.");

        config.get("eco.currency", "Euro");
        config.get("eco.removecostpercent", 50);
        config.get("eco.deactivatecostpercent", 100);
        config.get("eco.activatecostpercent", 100);
        config.get("eco.paybackpercent", 10);

        config.get("particle.particleshowrange", 16);
        config.get("particle.showtimeofparticle", 20);
        config.get("particle.showfor", "player");

        config.get("region.regionpriority", 10);
        config.get("region.limit", 4);
        config.get("region.standartdenyflags", new String[]{});
        config.get("region.standartallowflags", new String[]{});

        config.get("region.region1name", "Small");
        config.get("region.region1size", 9);
        config.get("region.region1price", 2500);
        config.get("region.region2name", "Medium");
        config.get("region.region2size", 21);
        config.get("region.region2price", 5000);
        config.get("region.region3name", "Big");
        config.get("region.region3size", 60);
        config.get("region.region3price", 7500);
        config.get("region.region4name", "Extrem");
        config.get("region.region4size", 99);
        config.get("region.region4price", 10000);
        config.get("region.regionheight", 125,"Set the value to 9999 to use the maximum. The height and depth are absolute cordinates like 64 for floor");
        config.get("region.regiondepth", 54,"Set the value to 9999 to use the maximum. The height and depth are absolute cordinates like 64 for floor");

        config.get("manage.addmember", 500);
        config.get("manage.changeowner", 1200);
        config.get("manage.removemember", 100);

        if (Main.DRIVER.checkIfFileExists(Main.DRIVER.CONFIG)) {
            config.set("main.metrics", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_metrics", true));
            config.set("eco.currency", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_currency", "EURO"));
            config.set("region.limit",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_limit", 4));
            config.set("main.worlds",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_worlds", new String[]{"world"}));
            config.set("main.backuprg",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_backuprg", false));
            config.set("particle.showtimeofparticle", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showtimeofparticle", 20));
            config.set("particle.showfor",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_showfor", "player"));
            config.set("region.region1name",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region1name", "Small"));
            config.set("region.region1size",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region1size", 9));
            config.set("region.region1price",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region1price", 2500));
            config.set("region.region2name",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region2name", "Medium"));
            config.set("region.region2size",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region2size", 21));
            config.set("region.region2price",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region2price", 5000));
            config.set("region.region3name",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region3name", "Big"));
            config.set("region.region3size",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region3size", 60));
            config.set("region.region3price",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region3price", 7500));
            config.set("region.region4name",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region4name", "Extrem"));
            config.set("region.region4size",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region4size", 99));
            config.set("region.region4price",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_region4price", 10000));
            config.set("manage.addmember",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_addmember", 500));
            config.set("manage.changeowner",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_changeowner", 1200));
            config.set("manage.removemember",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_removemember", 100));
            config.set("region.regionheight",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regionheight", 125));
            config.set("region.regiondepth",Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regiondepth", 54));
            config.set("eco.removecostpercent", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_removecostpercent", 50));
            config.set("eco.deactivatecostpercent", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_deactivatecostpercent", 100));
            config.set("eco.activatecostpercent", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_activatecostpercent", 100));
            config.set("eco.paybackpercent", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_payback", 10));
            config.set("region.regionpriority", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_regionpriority", 10));
            config.set("region.standartdenyflags", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_standartdenyflags", new String[0]));
            config.set("region.standartallowflags", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_standartallowflags", new String[0]));
            config.set("particle.particleshowrange", Main.DRIVER.getProperty(Main.DRIVER.CONFIG, "_particleshowrange", 16));

            Main.DRIVER.deletefile(Main.DRIVER.CONFIG);
        }
        config.saveToFile();

    }
}

