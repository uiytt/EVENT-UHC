package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class ChaosEventWeirdFight extends ChaosEvent {

	public static final List<Material> SWORDS = Arrays.asList(Material.DIAMOND_SWORD,Material.IRON_SWORD,Material.GOLDEN_SWORD,Material.STONE_SWORD,Material.WOODEN_SWORD);
	public ChaosEventWeirdFight() {
		super("WeirdFight", Material.IRON_AXE, 39, Type.AFTER_PVP, new String[]{"Les joueurs ne peuvent utiliser ni","des épées, ni des arcs."});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Vous ne pouvez plus utiliser des épées ou des arcs pour combattre.");
	}

}
