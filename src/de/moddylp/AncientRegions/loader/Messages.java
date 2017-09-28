package de.moddylp.AncientRegions.loader;

import de.moddylp.AncientRegions.Language;
import de.moddylp.AncientRegions.Main;

public class Messages {
	private Main plugin;

	public Messages(Main plugin) {
		this.plugin = plugin;
		this.setup();
	}

	private void setup() {

		Language lang = plugin.lang;

		// Main
		lang.setTextOnce("1", "ATTENTION: Please don't touch the placeholder ( [PH] ) tag!!");
		lang.setTextOnce("2", "------------------------------------------------------------");
		lang.setTextOnce("Enabled", "AncientRegions has been enabled!");
		lang.setTextOnce("Disabled", "AncientRegions has been disabled!");

		// Config Reload
		lang.setTextOnce("ConfigReload", "Config and LanguageFile have been reloaded!");
		lang.setTextOnce("Cancel", "Canceled all Particle Activities!");

		// User Information
		lang.setTextOnce("ValueChat", " Value for FlagOBJ([PH]) was set!");
		lang.setTextOnce("PayNote", "You have payed [PH] for changing this flag!");
		lang.setTextOnce("PayNote2", "You have payed [PH] for this region!");
		lang.setTextOnce("PayNote3", "You have payed [PH] for this region change!");
		lang.setTextOnce("FlagRemoved", "FlagOBJ removed!");
		lang.setTextOnce("Message", "Please type in the Chat your [PH] Message!");
		lang.setTextOnce("Message2",
				"Please type in the Chat your EntityTypes. Seperate it with a comma! e.g. Creeper, Zombie");
		lang.setTextOnce("Message3", "Please type in the Chat only numeric Values.");
		lang.setTextOnce("Message4",
				"Please type in the Chat commands without slash. Seperate each command with a Comma!");
		lang.setTextOnce("Message5", "Please type in the Chat only numeric Values.");
		lang.setTextOnce("fEnabled", " enabled");
		lang.setTextOnce("fDisabled", " disabled");
		lang.setTextOnce("Toggle", "Toggle ");
		lang.setTextOnce("Set", "Set [PH] in your Region!");
		lang.setTextOnce("s", "Set ");
		lang.setTextOnce("WeatherInfo",
				"Please type a form of Weather! Valid Values are 'sun, clear, storm, thunder, rain and downfall'!");
		lang.setTextOnce("Gamemode",
				"Please type a form of Gamemode! Valid Values are 'creative, survival, adventure, spectator, 0,1,2,3'!");
		lang.setTextOnce("Particles", "Particle Border toggled! Showing effect for [PH] seconds.");
		lang.setTextOnce("ParticlesOff", "Particle Border toggled!");
		lang.setTextOnce("Canceled", "All Particleborders are canceled");

		// Regionmanager
		lang.setTextOnce("Buy", "Buy ");
		lang.setTextOnce("RegionManager", "RegionManager");
		lang.setTextOnce("RegionManage", "Manage your complete Region or buy some new!");
		lang.setTextOnce("RegionInfo", "Inspect Region Information");
		lang.setTextOnce("RegionBuy", "Buy a Region");
		lang.setTextOnce("RegionName", "Name");
		lang.setTextOnce("RegionPrice", "Price");
		lang.setTextOnce("RegionSize", "Size");
		lang.setTextOnce("RegionCreation", "The creation of the region has started.... Please wait...");
		lang.setTextOnce("RegionExists", "There is already a Region. Please choose another place.");
		lang.setTextOnce("Created", "Your Region was created.");
		lang.setTextOnce("AddMember", "Add Member");
		lang.setTextOnce("RemoveMember", "Remove Member");
		lang.setTextOnce("PlayerAdded", "Player [PH] added to region.");
		lang.setTextOnce("PlayerRemoved", "Player [PH] removed from region.");
		lang.setTextOnce("SetOwner", "Change Owner");
		lang.setTextOnce("ChangeOwner", "Changed Owner to [PH]!");
		lang.setTextOnce("RemoveRegion", "Sell Region");
		lang.setTextOnce("RemoveRegionLore1", "Sell your Region to the Server.");
		lang.setTextOnce("RemoveRegionLore2", "You get [PH] back by selling the Region.");
		lang.setTextOnce("Removed", "Your Region ([PH]) was selled.");
		lang.setTextOnce("Payback", " You get [PH] for selling this region.");
		lang.setTextOnce("Playername", "Please type in the Chat a Playername!");

		// Menu
		lang.setTextOnce("DescriptionFlags1", "Here you can edit all Flags from Worldguard");
		lang.setTextOnce("DescriptionFlags2", "from a region that you are Owner from!");
		lang.setTextOnce("EditFlags", "Edit Flags - Page");
		lang.setTextOnce("EditFlagsItem", "Edit Flags");
		lang.setTextOnce("Update", "Sorry, maybe in the next update availble!");
		lang.setTextOnce("Next", "Next");
		lang.setTextOnce("Back", "Back");
		lang.setTextOnce("Mainmenu", "Main Menu");
		lang.setTextOnce("Current", "Current Value");

		// Errors
		lang.setTextOnce("ErrorPasting", "There was an error. The Plugin can't rollback the Region. Please contact the Adminstrator.");
		lang.setTextOnce("ErrorSaving", "There was an error. The Plugin can't save the Region into file. Please contact the Adminstrator.");
		lang.setTextOnce("Worldguard", "Worldguard isn't installed!");
		lang.setTextOnce("NoMoney", "You dont have enought money!");
		lang.setTextOnce("VaultError", "There was an Error with Vault!");
		lang.setTextOnce("GobalError", "You are standing in global region. That is not allowed!");
		lang.setTextOnce("Owner", "You are not the Owner of this Region or there is no region!");
		lang.setTextOnce("ToggleError", "There was an error, while toggling [PH]!");
		lang.setTextOnce("Permission", "You don't have the permissions for this!");
		lang.setTextOnce("InvalidEnitity", "Invalid Format. You had a tipping error in your EntityType. Try again");
		lang.setTextOnce("Console", "This Command can only be executed by Players!");
		lang.setTextOnce("InvalidWeather",
				"Invalid Format! Use 'sun', 'clear', 'thunder', 'storm' or 'rain'! Try again!");
		lang.setTextOnce("InvalidGamemode",
				"Invalid Format! Use 'survival', 'creative', 'spectator', 'adventure'! Try again!");
		lang.setTextOnce("CommandNotFound",
				"This parameter doesn't exist, please choose another. Type /ancr help for help!");
		lang.setTextOnce("ConfigError",
				"There is an error in the Config. Please contact your Adminstrator. Error by: [PH]");
		lang.setTextOnce("World", "In this world is AncientRegions disabled.");
		lang.setTextOnce("ToMany", "You have reached the limit of regions!");
		lang.setTextOnce("ErrorDelete", "Failed to delete Region");
		lang.setTextOnce("ErrorCreate", "Failed to create Region");

		// Flags -- TEST
		lang.setTextOnce("build", "Build");
		lang.setTextOnce("Player", "That Player ([PH]) doesn't exist, or is not online. Please choose another one!");

	}
}
