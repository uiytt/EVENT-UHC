package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class ChaosEventNoGravityForEntities extends ChaosEvent {

	public ChaosEventNoGravityForEntities() {
		super("NoGravityForEntites", Material.ARROW, 16, Type.NORMAL, Language.splitLore(Language.EVENT_NO_ENTITY_GRAVITY_LORE.getMessage()));
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_NO_ENTITY_GRAVITY_ENABLE.getMessage());
		Main.getConfigManager().getWorld().getEntities().forEach(entity -> {
			if(entity.getType() != EntityType.PLAYER) {
				entity.setGravity(false);
			}
		});
	}
}
	