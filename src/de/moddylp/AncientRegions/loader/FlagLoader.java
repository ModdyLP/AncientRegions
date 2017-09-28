package de.moddylp.AncientRegions.loader;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
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
        //PAGE 1 Reserved Slots(36, 37, 43, 44, 45, 46, 52, 53)
        new FlagOBJ("", 0, Material.STONE, DefaultFlag.BUILD);
        new FlagOBJ("", 1, Material.SIGN, DefaultFlag.GREET_MESSAGE);
        new FlagOBJ("", 2, Material.SIGN, DefaultFlag.FAREWELL_MESSAGE);
        new FlagOBJ("", 3, Material.DIAMOND_SWORD, DefaultFlag.PVP);
        new FlagOBJ("", 4, Material.SKULL_ITEM, DefaultFlag.MOB_DAMAGE);
        new FlagOBJ("", 5, Material.IRON_DOOR, DefaultFlag.ENTRY);
        new FlagOBJ("", 6, Material.WOOD_DOOR, DefaultFlag.EXIT);
        new FlagOBJ("", 7, Material.ENDER_PEARL, DefaultFlag.ENDERPEARL);
        new FlagOBJ("", 8, Material.COMPASS, DefaultFlag.TELE_LOC);
        new FlagOBJ("", 9, Material.HOPPER, DefaultFlag.ITEM_PICKUP);
        new FlagOBJ("", 10, Material.DROPPER, DefaultFlag.ITEM_DROP);
        new FlagOBJ("", 11, Material.EXP_BOTTLE, DefaultFlag.EXP_DROPS);
        new FlagOBJ("", 12, Material.MONSTER_EGG, DefaultFlag.MOB_SPAWNING);
        new FlagOBJ("", 13, Material.SKULL_ITEM, DefaultFlag.CREEPER_EXPLOSION);
        new FlagOBJ("", 14, Material.TNT, DefaultFlag.TNT);
        new FlagOBJ("", 15, Material.POTION, DefaultFlag.INVINCIBILITY);
        new FlagOBJ("", 16, Material.SKULL_ITEM, DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE);
        new FlagOBJ("", 17, Material.MONSTER_EGG, DefaultFlag.DENY_SPAWN);
        new FlagOBJ("", 18, Material.MYCEL, DefaultFlag.MYCELIUM_SPREAD);
        new FlagOBJ("", 19, Material.VINE, DefaultFlag.VINE_GROWTH);
        new FlagOBJ("", 20, Material.SOIL, DefaultFlag.SOIL_DRY);
        new FlagOBJ("", 21, Material.SKULL_ITEM, DefaultFlag.GAME_MODE);
        new FlagOBJ("", 22, Material.GRASS, DefaultFlag.GRASS_SPREAD);
        new FlagOBJ("", 23, Material.GOLDEN_APPLE, DefaultFlag.HEAL_AMOUNT);
        new FlagOBJ("", 24, Material.GOLDEN_APPLE, DefaultFlag.HEAL_DELAY);
        new FlagOBJ("", 25, Material.GOLDEN_APPLE, DefaultFlag.MIN_HEAL);
        new FlagOBJ("", 26, Material.GOLDEN_APPLE, DefaultFlag.MAX_HEAL);
        new FlagOBJ("", 27, Material.COMMAND_CHAIN, DefaultFlag.ALLOWED_CMDS);
        new FlagOBJ("", 28, Material.COMMAND_REPEATING, DefaultFlag.BLOCKED_CMDS);
        new FlagOBJ("", 29, Material.EMERALD, DefaultFlag.TIME_LOCK);
        new FlagOBJ("", 30, Material.FIREBALL, DefaultFlag.WEATHER_LOCK);
        new FlagOBJ("", 31, Material.BED, DefaultFlag.SPAWN_LOC);
        new FlagOBJ("", 32, Material.COOKED_BEEF, DefaultFlag.FEED_AMOUNT);
        new FlagOBJ("", 33, Material.COOKED_BEEF, DefaultFlag.FEED_DELAY);
        new FlagOBJ("", 34, Material.COOKED_BEEF, DefaultFlag.MIN_FOOD);
        new FlagOBJ("", 35, Material.COOKED_BEEF, DefaultFlag.MAX_FOOD);
        new FlagOBJ("", 38, Material.LAVA_BUCKET, DefaultFlag.LAVA_FLOW);
        new FlagOBJ("", 39, Material.WATER_BUCKET, DefaultFlag.WATER_FLOW);
        new FlagOBJ("", 40, Material.BLAZE_ROD, DefaultFlag.LIGHTNING);
        new FlagOBJ("", 41, Material.FLINT_AND_STEEL, DefaultFlag.LAVA_FIRE);
        new FlagOBJ("", 42, Material.RED_MUSHROOM, DefaultFlag.MUSHROOMS);
        new FlagOBJ("", 47, Material.PISTON_BASE, DefaultFlag.PISTONS);
        new FlagOBJ("", 48, Material.PAPER, DefaultFlag.SEND_CHAT);
        new FlagOBJ("", 49, Material.PAPER, DefaultFlag.RECEIVE_CHAT);
        new FlagOBJ("", 50, Material.POTION, DefaultFlag.POTION_SPLASH);
        new FlagOBJ("", 51, Material.LEAVES, DefaultFlag.LEAF_DECAY);

        //PAGE 2 Reserved Slots(36, 37, 43, 44, 45, 46, 52, 53)
        new FlagOBJ("", 0, Material.COBBLESTONE, DefaultFlag.PASSTHROUGH);
        new FlagOBJ("", 1, Material.BARRIER, DefaultFlag.INTERACT);
        new FlagOBJ("", 2, Material.DIAMOND_PICKAXE, DefaultFlag.BLOCK_BREAK);
        new FlagOBJ("", 3, Material.WOOD, DefaultFlag.BLOCK_PLACE);
        new FlagOBJ("", 4, Material.WOOD_BUTTON, DefaultFlag.USE);
        new FlagOBJ("", 5, Material.WHEAT, DefaultFlag.DAMAGE_ANIMALS);
        new FlagOBJ("", 6, Material.CHEST, DefaultFlag.CHEST_ACCESS);
        new FlagOBJ("", 7, Material.SADDLE, DefaultFlag.RIDE);
        new FlagOBJ("", 8, Material.BED, DefaultFlag.SLEEP);
        new FlagOBJ("", 9, Material.BOAT, DefaultFlag.PLACE_VEHICLE);
        new FlagOBJ("", 10, Material.MINECART, DefaultFlag.DESTROY_VEHICLE);
        new FlagOBJ("", 11, Material.FLINT_AND_STEEL, DefaultFlag.LIGHTER);
        new FlagOBJ("", 12, Material.FIREBALL, DefaultFlag.GHAST_FIREBALL);
        new FlagOBJ("", 13, Material.TNT, DefaultFlag.OTHER_EXPLOSION);
        new FlagOBJ("", 14, Material.FLINT_AND_STEEL, DefaultFlag.FIRE_SPREAD);
        new FlagOBJ("", 15, Material.EYE_OF_ENDER, DefaultFlag.ENDER_BUILD);
        new FlagOBJ("", 16, Material.PAINTING, DefaultFlag.ENTITY_PAINTING_DESTROY);
        new FlagOBJ("", 17, Material.ITEM_FRAME, DefaultFlag.ENTITY_ITEM_FRAME_DESTROY);
        new FlagOBJ("", 18, Material.SNOW_BLOCK, DefaultFlag.SNOW_MELT);
        new FlagOBJ("", 19, Material.SNOW_BALL, DefaultFlag.SNOW_FALL);
        new FlagOBJ("", 20, Material.ICE, DefaultFlag.ICE_MELT);
        new FlagOBJ("", 21, Material.PACKED_ICE, DefaultFlag.ICE_FORM);
        new FlagOBJ("", 22, Material.SIGN, DefaultFlag.NOTIFY_ENTER);
        new FlagOBJ("", 23, Material.SIGN, DefaultFlag.NOTIFY_LEAVE);
        new FlagOBJ("", 27, Material.CHORUS_FRUIT, DefaultFlag.CHORUS_TELEPORT);

        //SAVE
        FileDriver.getInstance().saveJson();
        Main.getInstance().getLogger().info("Finished loading flags: "+ FlagUtil.flagOBJHashMap.size()+"  of Worldguard Flags (diff is normal) "+DefaultFlag.getDefaultFlags().size());
    }
}
