package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChaosEventHeal extends ChaosEvent {

	public ChaosEventHeal() {
		super("Heal", Material.SPLASH_POTION, 36, Type.AFTER_PVP, Language.splitLore(Language.EVENT_HEAL_LORE.getMessage()),20);

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_HEAL_ENABLE.getMessage());
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
		}
	}

	
}
