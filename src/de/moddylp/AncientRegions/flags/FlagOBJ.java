package de.moddylp.AncientRegions.flags;

import com.google.common.base.CaseFormat;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.flags.FlagUtil;
import de.moddylp.AncientRegions.loader.FileDriver;
import java.util.HashMap;
import java.util.logging.Logger;
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
        this.name = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, flag.getName().replaceAll("-", "_").toUpperCase());
        this.description = description;
        this.flag = flag;
        this.configname = this.name.toLowerCase();
        this.permission = flag instanceof BooleanFlag || flag instanceof StateFlag ? "ancient.regions.flag.toggle" + this.configname : "ancient.regions.flag." + this.configname;
        this.item = item;
        this.menuposition = menuposition;
        FlagUtil.flagOBJHashMap.put(this.name, this);
        FileDriver.getInstance().getProperty(FileDriver.getInstance().CONFIG, this.configname, 100);
    }

    public static FlagOBJ getFlagObj(Flag flag) {
        String search = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, flag.getName().replaceAll("-", "_").toUpperCase());
        if (FlagUtil.flagOBJHashMap.containsKey(search)) {
            return FlagUtil.flagOBJHashMap.get(search);
        }
        Main.getInstance().getLogger().warning("No Flag found with name: " + search);
        return null;
    }

    public String getName() {
        if (this.name != null) {
            return this.name;
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

