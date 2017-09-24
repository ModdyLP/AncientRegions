package de.moddylp.AncientRegions.flags;

import com.sk89q.worldguard.protection.flags.Flag;
import org.bukkit.Material;

public class FlagOBJ {

    private final Material item;
    private final int menuposition;
    private String name;
    private String configname;
    private String description;
    private Flag<?> flag;
    private String permission;


    public FlagOBJ(String name, String description, Flag<?> flag, Material item, int menuposition) {
        this.name = name;
        this.description = description;
        this.flag = flag;
        this.configname = name.toLowerCase();
        this.permission = "ancient.regions.flag." + configname;
        this.item = item;
        this.menuposition = menuposition;
    }

    public String getName() {
        return name;
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

    public int getMenuposition() {
        return menuposition;
    }
}
