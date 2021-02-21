package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class ChaosEventNoGravityForEntities extends ChaosEvent {

	public ChaosEventNoGravityForEntities() {
		super("NoGravityForEntites", Material.ARROW, 16, Type.NORMAL,new String[]{"Toutes entités qui spawn n'ont pas de gravité"});
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + " GravityInc. annonce sa fermeture.");
		Main.CONFIG.getWorld().getEntities().forEach(entity -> {
			if(entity.getType() != EntityType.PLAYER) {
				entity.setGravity(false);
			}
		});
	}
}
	