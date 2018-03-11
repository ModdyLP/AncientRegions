package de.moddylp.AncientRegions.flags;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.moddylp.AncientRegions.Main;
import de.moddylp.AncientRegions.gui.Events.ActivateMode;
import de.moddylp.AncientRegions.gui.Events.SpecialInput.SetValueFromChatEvent;
import de.moddylp.AncientRegions.gui.Events.SpecialInput.SpezialFormatString;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class StringFlag {
    private final FlagOBJ flagOBJ;

    private StringFlag(FlagOBJ flagOBJ) {
        this.flagOBJ = flagOBJ;
    }

    public static void createandload(FlagOBJ flagOBJ, Player p, Inventory menu) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        StringFlag flag;
        if (FlagUtil.stringFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.stringFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new StringFlag(flagOBJ);
            FlagUtil.stringFlagHashMap.put(flagOBJ.getName(), flag);
        }
        FlagUtil.loadStringGUI(menu, p, flagOBJ);
    }

    public static void createandtoggle(FlagOBJ flagOBJ, Player p, Inventory menu, InventoryClickEvent event, ActivateMode mode) {
        if (flagOBJ.getMenuposition() == 999) {
            return;
        }
        StringFlag flag;
        event.setCancelled(true);
        if (FlagUtil.stringFlagHashMap.containsKey(flagOBJ.getName())) {
            flag = FlagUtil.stringFlagHashMap.get(flagOBJ.getName());
        } else {
            flag = new StringFlag(flagOBJ);
            FlagUtil.stringFlagHashMap.put(flagOBJ.getName(), flag);
        }
        flag.toggle(event, menu, p, mode);
    }

    public boolean toggle(InventoryClickEvent e, Inventory menu, Player p, ActivateMode mode) {
        if (p.hasPermission(this.flagOBJ.getPermission())) {
            RegionContainer container = Main.worldguard.getRegionContainer();
            RegionManager regions = container.get(p.getWorld());
            Vector pt = new Vector(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
            LocalPlayer ply = Main.worldguard.wrapPlayer(p);
            if (regions != null) {
                List region = regions.getApplicableRegionsIDs(pt);
                if (region.isEmpty()) {
                    p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("GobalError"));
                } else {
                    ProtectedRegion rg = regions.getRegion((String) region.get(0));
                    if (rg != null && (rg.isOwner(ply) || p.hasPermission("ancient.regions.admin.bypass"))) {
                        if (!FlagUtil.isSet(p, flagOBJ.getFlag()).equalsIgnoreCase("null") && mode.equals(ActivateMode.REMOVE)) {
                            if (FlagUtil.payment(p, e, this.flagOBJ.getName(), mode)) {
                                rg.setFlag(this.flagOBJ.getFlag(), null);
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + ChatColor.GOLD + " " + this.flagOBJ.getName() + " "+Main.getInstance().lang.getText("FlagRemoved"));
                            }
                        } else if (mode.equals(ActivateMode.ACTIVATE)) {
                            if (this.flagOBJ.getFlag() instanceof SetFlag) {
                                p.closeInventory();
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + Main.getInstance().lang.getText("Message4").replace("[PH]", this.flagOBJ.getName()));
                                Main.getInstance().getServer().getPluginManager().registerEvents(new SpezialFormatString(p, this.flagOBJ, mode), Main.getInstance());
                            } else if (this.flagOBJ.getFlag() instanceof com.sk89q.worldguard.protection.flags.StringFlag) {
                                p.closeInventory();
                                p.sendMessage(ChatColor.GREEN + "[AR][INFO] " + Main.getInstance().lang.getText("SimpleTypeChatMSG").replace("[PH]", this.flagOBJ.getName()));
                                Main.getInstance().getServer().getPluginManager().registerEvents(new SetValueFromChatEvent(p, this.flagOBJ, mode), Main.getInstance());
                            }
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("Owner"));
                        e.setCancelled(true);
                    }
                    e.setCancelled(true);
                }
            }
        } else {
            p.sendMessage(ChatColor.RED + "[AR][ERROR] " + Main.getInstance().lang.getText("Permission"));
            e.setCancelled(true);
        }
        FlagUtil.loadStringGUI(menu, p, flagOBJ);
        return false;
    }
}

