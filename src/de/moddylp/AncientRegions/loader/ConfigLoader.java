package de.moddylp.AncientRegions.loader;

import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.utils.Console;
import de.moddylp.simplecommentconfig.Config;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigLoader {

    public static void loadMySQLParamsandSet() {
        if (Main.getInstance().getMainConfig().get("main.db.installed") == null) {
            Console.send("Creating MySQL Table");
            Main.getInstance().getMysql().execute("CREATE TABLE `ancientregionsconfig` ( `ID` VARCHAR(255) NOT NULL , `value` VARCHAR(255) NOT NULL ) ENGINE = InnoDB;");
            Main.getInstance().getMysql().execute("ALTER TABLE `ancientregionsconfig` ADD PRIMARY KEY(`ID`);");
            Main.getInstance().getMysql().execute("INSERT INTO `ancientregionsconfig` (`ID`, `value`) VALUES ('main.db.installed', 'true');");
        }
        List<HashMap<String, Object>> config = Main.getInstance().getMysql().getResultFromQuery("SELECT * FROM ancientregionsconfig");
        Console.send("DBConfig: " + Arrays.toString(config.toArray()));
        for (HashMap<String, Object> map : config) {
            String key = map.get("ID").toString();
            String value = map.get("value").toString();

            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                Main.getInstance().getMainConfig().set(key, Boolean.parseBoolean(value));
            } else if (value.contains(".")) {
                Main.getInstance().getMainConfig().set(key, Double.parseDouble(value));
            } else if (Console.isInteger(value)) {
                Main.getInstance().getMainConfig().set(key, Integer.parseInt(value));
            } else {
                Main.getInstance().getMainConfig().set(key, value);
            }
            Console.send("Import("+key+"): "+value+" Proof: "+Main.getInstance().getMainConfig().get(key));
        }
        Main.getInstance().getMainConfig().saveToFile();
    }

    public static void saveDefaultconfig() {
        Config config = Main.getInstance().getMainConfig();
        Console.send("Loading Config Default Options");

        config.get("main.language", "en", "Define Language code");
        config.get("main.worlds", new String[]{"world"}, "Define in which worlds the plugin is active.");
        config.get("main.backuprg", false, "Define if the plugin should backup the region before each purchase.");
        config.get("main.config", "file", "Set where price config should come from should come from. (options are file and mysql)");

        config.get("main.db.host", "localhost");
        config.get("main.db.port", "3306");
        config.get("main.db.database", "default");
        config.get("main.db.user", "default");
        config.get("main.db.password", "default");

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
        config.get("region.regionheight", 125, "Set the value to 9999 to use the maximum. The height and depth are absolute cordinates like 64 for floor");
        config.get("region.regiondepth", 54, "Set the value to 9999 to use the maximum. The height and depth are absolute cordinates like 64 for floor");

        config.get("manage.addmember", 500);
        config.get("manage.changeowner", 1200);
        config.get("manage.removemember", 100);
        config.saveToFile();
    }

    private static ArrayList<String> convertJsonArray(String config) {
        JSONArray array = (JSONArray) Main.DRIVER.getPropertyAsObj(Main.DRIVER.CONFIG, config, new JSONArray());
        ArrayList<String> liste = new ArrayList<>();
        for (Object object : array) {
            liste.add(object.toString());
        }
        return liste;
    }
}

