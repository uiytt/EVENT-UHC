package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ChaosEventNoDamageByMobs extends ChaosEvent {

	public ChaosEventNoDamageByMobs() {
		super("NoDamageByMobs", Material.ROTTEN_FLESH, 26, Type.NORMAL,new String[]{});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Vous êtes maintenant temporairement immunisé contre les damages des mobs.");
	}

	
}
