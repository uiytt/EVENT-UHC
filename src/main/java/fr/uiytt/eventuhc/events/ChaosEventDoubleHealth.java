package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChaosEventDoubleHealth extends ChaosEvent {

	public ChaosEventDoubleHealth() {
		super("DoubleHealth", Material.TOTEM_OF_UNDYING, 37, Type.AFTER_PVP, Language.splitLore(Language.EVENT_DOUBLE_HEALTH_LORE.getMessage()));

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_DOUBLE_HEALTH_ENABLE.getMessage());
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40.0);
		}
	}

	
	@Override
	protected void onDisable() {
		super.onDisable();
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			double added_health = player.getHealth() - 20.0;
			if(added_health > 0) {
				player.setAbsorptionAmount(added_health);
			}
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
		}
	}
}
