package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class ChaosEventWeirdFight extends ChaosEvent {

	public static final List<Material> SWORDS = Arrays.asList(Material.DIAMOND_SWORD,Material.IRON_SWORD,Material.GOLDEN_SWORD,Material.STONE_SWORD,Material.WOODEN_SWORD);
	public ChaosEventWeirdFight() {
		super("WeirdFight", Material.IRON_AXE, 39, Type.AFTER_PVP, Language.splitLore(Language.EVENT_WEIRD_FIGHT_LORE.getMessage()));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_WEIRD_FIGHT_ENABLE.getMessage());
	}

}
