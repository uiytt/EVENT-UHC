package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventTeleportationToOthers extends ChaosEvent {
	public ChaosEventTeleportationToOthers() {
		super("RandomTp", Material.END_STONE, 9, Type.AFTER_PVP, Language.splitLore(Language.EVENT_TELEPORTATION_OTHERS_LORE.getMessage()),15);
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_TELEPORTATION_OTHERS_ENABLE.getMessage());

		new BukkitRunnable() {
			
			@Override
			public void run() {
				List<UUID> alivePlayers = GameManager.getGameInstance().getGameData().getAlivePlayers();
				Bukkit.broadcastMessage(Language.EVENT_TELEPORTATION_OTHERS_ENABLE2.getMessage());
				for(UUID playerUUID : alivePlayers ) {
					Player player = Bukkit.getPlayer(playerUUID);
					if(player == null) {continue;}
					if(ThreadLocalRandom.current().nextInt(5) == 0) {
						UUID p2UUID = alivePlayers.get(ThreadLocalRandom.current().nextInt(alivePlayers.size()));
						Player p2 = Bukkit.getPlayer(p2UUID);
						if(p2 == null) {continue;}
						if(playerUUID != p2UUID) {
							player.teleport(p2);
						}
					}
					
				}
			}
		}.runTaskLater(Main.getInstance(),30*20);
		
	}
}
