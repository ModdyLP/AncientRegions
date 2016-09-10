package de.moddylp.AncientRegions.gui.Events;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import de.moddylp.AncientRegions.Main;

public class GUIEvents {
	protected Main plugin;
	private WorldGuardPlugin worldguard;
	private WorldEditPlugin worldedit;
    
    public GUIEvents(Main plugin, WorldGuardPlugin worldguard, WorldEditPlugin worldedit)
    {
        this.worldguard = worldguard;
        this.plugin = plugin;
        this.worldedit = worldedit;
        
        try{
            registerGuiEvents();
            
        } catch (Exception ex) {
          
            ex.printStackTrace();
            
        }
        
    }
    
    //Register all GUI-Events
    private void registerGuiEvents()
    { 
        plugin.getServer().getPluginManager().registerEvents(new Menu_Click(this, worldguard, plugin, worldedit), plugin);
        
    }
}
