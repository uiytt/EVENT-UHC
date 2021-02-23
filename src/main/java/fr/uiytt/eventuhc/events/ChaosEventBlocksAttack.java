package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ChaosEventBlocksAttack extends ChaosEvent {
	public ChaosEventBlocksAttack() {
		super("BlocksAttack", Material.BRICK, 17, Type.BEFORE_PVP, Language.splitLore(Language.EVENT_BLOCKS_ATTACK_LORE.getMessage()),180);
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_BLOCKS_ATTACK_ENABLE.getMessage());
	}
}
	