package fr.uiytt.eventuhc;

import fr.uiytt.eventuhc.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.minuskube.inv.InventoryManager;
import fr.uiytt.eventuhc.game.GameManager;

public class Main extends JavaPlugin {

	public static final ConfigManager CONFIG = new ConfigManager();
	private static JavaPlugin instance;
	public static final boolean devMode = false;
	private static InventoryManager InvManager;
	
	@Override
	public void onEnable() {
		instance = this;
		//Hook in the SmartInv API for GUI
		InvManager = new InventoryManager(this);
		InvManager.init();
		//Register a bunch of things (events)
		Register.register(this);
		//Load the config
		CONFIG.load();
		//Create a new empty instance of the game to avoid null error 
		GameManager.setGameInstance(new GameManager());
	}

	public static JavaPlugin getInstance() {
		return instance;
	}


	public static InventoryManager getInvManager() {
		return InvManager;
	}
	
}
