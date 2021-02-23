package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ChaosEventBowSwap extends ChaosEvent {
	public ChaosEventBowSwap() {
		super("BowSwap", Material.BOW, 18, Type.AFTER_PVP,Language.splitLore(Language.EVENT_BOW_SWAP_LORE.getMessage()));
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_BOW_SWAP_ENABLE.getMessage());
	}
}
	