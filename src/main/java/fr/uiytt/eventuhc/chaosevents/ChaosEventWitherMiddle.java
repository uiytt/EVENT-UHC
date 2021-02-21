package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventWitherMiddle extends ChaosEvent {

	public ChaosEventWitherMiddle() {
		super("WitherMiddle", Material.NETHER_STAR, 35, Type.AFTER_BORDER, new String[]{"Spawn un ou des Withers en 0 0"},30);

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Un/des wither vous attend(ent) en 0 0");
		for(int i =0;i<ThreadLocalRandom.current().nextInt(2) + 1;i++) {
			Main.CONFIG.getWorld().spawnEntity(new Location(Main.CONFIG.getWorld(), 0, 100, 0), EntityType.WITHER);
		}
	}

	
}
