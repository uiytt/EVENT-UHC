package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ChaosEventDamageDropItems extends ChaosEvent {

	public ChaosEventDamageDropItems() {
		super("DamageDropItems", Material.ROTTEN_FLESH, 38, Type.AFTER_PVP, Language.splitLore(Language.EVENT_DAMAGE_DROP_ITEMS_LORE.getMessage()));

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_DAMAGE_DROP_ITEMS_ENABLE.getMessage());
	}

	

}
