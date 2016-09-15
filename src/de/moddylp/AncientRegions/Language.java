package de.moddylp.AncientRegions;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Language {
	//Made by th3shadowbroker
    @SuppressWarnings("unused")
	private final Main plugin;
    private final File langFile;
    private final FileConfiguration lang;
    
    //Construction
    public Language(Main plugin, File config )
    {
        
        this.plugin = plugin;
        this.langFile = config;
        this.lang = YamlConfiguration.loadConfiguration(langFile);
        
    }
    public void reload() {
    	try {
			lang.load(langFile);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //Save lang file
    private void save()
    {
        try {
        
            lang.save(langFile);
            
        } catch ( Exception ex ) {
            
            ex.printStackTrace();
            
        }
    }
    
    //Set default message
    public void setTextOnce(String path , String text)
    {
        if (lang.getString(path) == null )
        {
            
            lang.set( path , text );
            
            this.save();
            
        }
    }
    
    //Get message from message-file
    public String getText(String path)
    {
        if (lang.getString(path) != null)
        {
            
            return lang.getString(path).replaceAll("&" , "§");
            
        } else {
            
            return "&cThis sentence does not exist. Please check lang-file message ".replaceAll( "&" , "§" ) + path ;
            
        }
    }
    
}
