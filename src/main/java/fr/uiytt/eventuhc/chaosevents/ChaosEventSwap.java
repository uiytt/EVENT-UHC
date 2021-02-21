package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventSwap extends ChaosEvent {

	public ChaosEventSwap() {
		super("Swap",Material.ENDER_CHEST, 0,Type.AFTER_PVP,new String[]{"Les joueurs sont swap régulièrement"});
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Les joueurs vont êtres swap toutes les 1 minutes et 30 secondes (attention les swaps peuvent être annulés) ");
		new BukkitRunnable() {
			@Override
			public void run() {
				if(!activated) {
					this.cancel();
					return;
				}
				int i = 10;
				while (i > 0) {
					int a = i;
					Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Swap dans " + a + " secondes.");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i -= 1;
					if(!activated) {
						this.cancel();
						return;
					}
				}
				if(ThreadLocalRandom.current().nextInt(4) == 0) {
					List<UUID> playersUUID = new ArrayList<>(GameManager.getGameInstance().getGameData().getAlivePlayers());
					if(playersUUID.size() > 1) {
						while(playersUUID.size() > 1) {
							UUID UUID1 = playersUUID.get(ThreadLocalRandom.current().nextInt(playersUUID.size()));
							playersUUID.remove(UUID1);
							UUID UUID2 = playersUUID.get(ThreadLocalRandom.current().nextInt(playersUUID.size()));
							playersUUID.remove(UUID2);

							Player p1 = Bukkit.getPlayer(UUID1);
							Player p2 = Bukkit.getPlayer(UUID2);
							if(p1 == null || p2 == null) {continue;}

							Location p2loc = p2.getLocation();
							new BukkitRunnable() {
								
								@Override
								public void run() {
									p2.teleport(p1.getLocation());
									p1.teleport(p2loc);
									
								}
							}.runTask(Main.getInstance());
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						//If there is a player left, teleport it too
						if(playersUUID.size() > 0) {
							List<UUID> playersAliveUUID = GameManager.getGameInstance().getGameData().getAlivePlayers();
							UUID p1UUID = playersUUID.get(0);
							UUID p2UUID = p1UUID;
							while(p1UUID == p2UUID) {
								p2UUID = playersAliveUUID.get(ThreadLocalRandom.current().nextInt(playersAliveUUID.size()));
							}

							Player p1 = Bukkit.getPlayer(p1UUID);
							Player p2 = Bukkit.getPlayer(p2UUID);
							if(p1 == null || p2 == null) {return;}

							Location p2loc = p2.getLocation();
							new BukkitRunnable() {
								
								@Override
								public void run() {
									p2.teleport(p1.getLocation());
									p1.teleport(p2loc);
									
								}
							}.runTask(Main.getInstance());
						}
						return;
					}
							
				}
				Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Le swap a été annulé.");
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 20, 90 * 20);
		
	}

}
