package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
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
		super("RandomTp", Material.END_STONE, 9, Type.AFTER_PVP, new String[]{"Téléporte des joueurs aléatoirement","à d'autres joueurs"},15);
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Dans 15s certains joueurs seront tp à d'autres. LET'S FIGHTTTTT");

		new BukkitRunnable() {
			
			@Override
			public void run() {
				List<UUID> alivePlayers = GameManager.getGameInstance().getGameData().getAlivePlayers();
				Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Vous avez 20% de chance d'être téléporté sur un autre joueur, ne mourrez pas !");
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
		}.runTaskLater(Main.getInstance(),15*20);
		
	}
}
