package fr.uiytt.eventuhc;

import fr.minuskube.inv.InventoryManager;
import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

	private static JavaPlugin instance;
	private static ConfigManager CONFIG;
	private static Logger logger;
	private static InventoryManager InvManager;
	
	@Override
	public void onEnable() {
		instance = this;
		logger = getLogger();
		//Hook in the SmartInv API for GUI
		InvManager = new InventoryManager(this);
		InvManager.init();

		//Register commands, listener, etc..;
		Register.register(this);

		//Load the config
		CONFIG = new ConfigManager();
		CONFIG.load();

		//Create a new empty instance of the game to avoid null error 
		GameManager.setGameInstance(new GameManager());
	}

	public static JavaPlugin getInstance() {
		return instance;
	}
	public static Logger getLog() {return logger;}
	public static InventoryManager getInvManager() {
		return InvManager;
	}

	public static ConfigManager getConfigManager() {
		return CONFIG;
	}
}
