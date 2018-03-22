package de.moddylp.AncientRegions.flags;

import com.google.common.base.CaseFormat;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.FileDriver;
import de.moddylp.AncientRegions.utils.Console;
import org.bukkit.Material;

public class FlagOBJ {
    private final Material item;
    private final int menuposition;
    private String name;
    private String configname;
    private String description;
    private Flag<?> flag;
    private String permission;

    public FlagOBJ(String description, int menuposition, Material item, Flag<?> flag) {
        this.name = flag.getName();
        this.description = description;
        this.flag = flag;
        this.configname = this.name.toLowerCase();
        this.permission = flag instanceof BooleanFlag || flag instanceof StateFlag ? "ancient.regions.flag.toggle" + this.configname.replaceAll("-", "") : "ancient.regions.flag." + this.configname.replaceAll("-", "");
        this.item = item;
        this.menuposition = menuposition;
        FlagUtil.flagOBJHashMap.put(this.configname, this);
        Main.getInstance().getMainConfig().get("flags."+this.configname, 100, "TYPE: "+flag.getClass().getSimpleName());
        if (Main.DRIVER.checkIfFileExists(Main.DRIVER.CONFIG)) {
            Console.send("OLD: "+this.configname.replaceAll("-", "")+"  "+Main.DRIVER.hasKey(Main.DRIVER.CONFIG, this.configname.replaceAll("-", "")));
            Main.getInstance().getMainConfig().set("flags."+this.configname, Main.DRIVER.getPropertyAsObj(Main.DRIVER.CONFIG, this.configname.replaceAll("-", ""), 100));
        }
    }

    public static FlagOBJ getFlagObj(Flag flag) {
        String search = flag.getName().toLowerCase();
        if (FlagUtil.flagOBJHashMap.containsKey(search)) {
            return FlagUtil.flagOBJHashMap.get(search);
        }
        Console.error("No Flag found with name: " + search);
        return new FlagOBJ("NOT FOUND", 999, Material.BARRIER, DefaultFlag.ALLOWED_CMDS);
    }
    public static FlagOBJ getFlagObj(String flag) {
        String search = flag.toLowerCase();
        if (FlagUtil.flagOBJHashMap.containsKey(search)) {
            return FlagUtil.flagOBJHashMap.get(search);
        }
        Console.error("No Flag found with name: " + search);
        return new FlagOBJ("NOT FOUND", 999, Material.BARRIER, DefaultFlag.ALLOWED_CMDS);
    }

    public String getName() {
        if (this.name != null) {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.name.replaceAll("-", "_"));
        }
        return "NoName";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigname() {
        return this.configname;
    }

    public void setConfigname(String configname) {
        this.configname = configname;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Flag<?> getFlag() {
        return this.flag;
    }

    public void setFlag(Flag<?> flag) {
        this.flag = flag;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Material getItem() {
        return this.item;
    }

    int getMenuposition() {
        return this.menuposition;
    }
}

