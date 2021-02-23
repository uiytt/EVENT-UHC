package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventBlaze extends ChaosEvent {


	public ChaosEventBlaze() {
		super("4 Blaze", Material.BLAZE_ROD, 30, Type.NORMAL, Language.splitLore(Language.EVENT_BLAZE_LORE.getMessage()));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_BLAZE_ENABLE.getMessage());
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			for(int i =0;i<4;i++) {
				Location loc = player.getLocation();
				//Add a random offset to the blaze
				loc.add(ThreadLocalRandom.current().nextDouble(-7.0,7.0), 0, ThreadLocalRandom.current().nextDouble(-7.0,7.0));
				player.getWorld().spawnEntity(loc, EntityType.BLAZE);
			}
		}
		
	}

	
}
