package fr.uiytt.eventuhc;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.config.Language;
import org.bukkit.plugin.java.JavaPlugin;

import fr.minuskube.inv.InventoryManager;
import fr.uiytt.eventuhc.game.GameManager;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

	private static JavaPlugin instance;
	public static final ConfigManager CONFIG = new ConfigManager();
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
	
}
