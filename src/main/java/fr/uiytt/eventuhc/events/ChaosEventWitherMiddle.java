package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventWitherMiddle extends ChaosEvent {

	public ChaosEventWitherMiddle() {
		super("WitherMiddle", Material.NETHER_STAR, 35, Type.AFTER_BORDER, Language.splitLore(Language.EVENT_WITHER_MIDDLE_LORE.getMessage()),30);

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_WITHER_MIDDLE_ENABLE.getMessage());
		for(int i =0;i<ThreadLocalRandom.current().nextInt(2) + 1;i++) {
			Main.getConfigManager().getWorld().spawnEntity(new Location(Main.getConfigManager().getWorld(), 0, 100, 0), EntityType.WITHER);
		}
	}

	
}
