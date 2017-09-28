package de.moddylp.AncientRegions.loader;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagOBJ;
import de.moddylp.AncientRegions.flags.FlagUtil;
import org.bukkit.Material;

/**
 * Created by N.Hartmann on 28.09.2017.
 * Copyright 2017
 */
public class FlagLoader {
    public static void load() {
        Main.getInstance().getLogger().info("Start loading flags");
        new FlagOBJ("Build", "", DefaultFlag.BUILD, Material.STONE, 0);
        new FlagOBJ("PvP", "", DefaultFlag.PVP, Material.DIAMOND_SWORD, 3);
        new FlagOBJ("MobDamage", "", DefaultFlag.MOB_DAMAGE, Material.SKULL_ITEM, 4);
        new FlagOBJ("Entry", "", DefaultFlag.ENTRY, Material.IRON_DOOR, 5);
        new FlagOBJ("Exit", "", DefaultFlag.EXIT, Material.WOOD_DOOR, 6);
        new FlagOBJ("AllowedCmds", "", DefaultFlag.ALLOWED_CMDS,Material.COMMAND_CHAIN, 27);
        new FlagOBJ("BlockedCmds", "", DefaultFlag.BLOCKED_CMDS, Material.COMMAND_REPEATING, 28);
        Main.getInstance().getLogger().info("Finished loading flags: "+ FlagUtil.flagOBJHashMap.size());
        for (Flag flag: DefaultFlag.getDefaultFlags()) {
            Main.getInstance().getLogger().info(flag.getName());
        }
    }
}
