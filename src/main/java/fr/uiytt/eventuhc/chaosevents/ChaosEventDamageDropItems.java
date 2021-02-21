package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ChaosEventDamageDropItems extends ChaosEvent {

	public ChaosEventDamageDropItems() {
		super("DamageDropItems", Material.ROTTEN_FLESH, 38, Type.AFTER_PVP,new String[]{"Lorsqu'un joueur se fait frapper,"," il drop un item"});

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Lorsque vous prenez des dégats, vous droppez un item");
	}

	

}
