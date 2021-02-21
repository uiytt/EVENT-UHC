package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ChaosEventBlocksAttack extends ChaosEvent {
	public ChaosEventBlocksAttack() {
		super("BlocksAttack", Material.BRICK, 17, Type.BEFORE_PVP, new String[]{"Certains blocs minés peuvent attaquer en retour"},180);
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Faites attention, certains blocs peuvent maintenant rispoter.");
	}
}
	