package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ChaosEventNoDamageByMobs extends ChaosEvent {

	public ChaosEventNoDamageByMobs() {
		super("NoDamageByMobs", Material.ROTTEN_FLESH, 26, Type.NORMAL, Language.splitLore(Language.EVENT_NO_DAMAGE_MOBS_LORE.getMessage()));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_NO_DAMAGE_MOBS_ENABLE.getMessage());
	}

	
}
