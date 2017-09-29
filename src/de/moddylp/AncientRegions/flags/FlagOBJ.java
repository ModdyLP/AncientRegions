package de.moddylp.AncientRegions.flags;

import com.google.common.base.CaseFormat;
import com.sk89q.worldguard.protection.flags.*;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.loader.FileDriver;
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
        this.configname = name.toLowerCase();
        if (flag instanceof com.sk89q.worldguard.protection.flags.BooleanFlag) {
            this.permission = "ancient.regions.flag.toggle" + configname;
        } else {
            this.permission = "ancient.regions.flag." + configname;
        }
        this.item = item;
        this.menuposition = menuposition;
        FlagUtil.flagOBJHashMap.put(name, this);
        FileDriver.getInstance().getProperty(FileDriver.getInstance().CONFIG, configname, 100);
        Main.getInstance().getLogger().info("Created: "+name);
    }
    public static FlagOBJ getFlagObj(Flag flag) {
        String search = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, flag.getName().replaceAll("-", "_").toUpperCase());
        if (FlagUtil.flagOBJHashMap.containsKey(search)) {
            return FlagUtil.flagOBJHashMap.get(search);
        } else {
            Main.getInstance().getLogger().warning("No Flag found with name: "+search);
        }
        return null;
    }
    public String getName() {
        if (name != null) {
            return name;
        } else {
            return "NoName";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigname() {
        return configname;
    }

    public void setConfigname(String configname) {
        this.configname = configname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Flag<?> getFlag() {
        return flag;
    }

    public void setFlag(Flag<?> flag) {
        this.flag = flag;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Material getItem() {
        return item;
    }

    int getMenuposition() {
        return menuposition;
    }
}
