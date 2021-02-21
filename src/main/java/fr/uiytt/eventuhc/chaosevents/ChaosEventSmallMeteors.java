package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameManager;
import fr.uiytt.eventuhc.utils.Divers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventSmallMeteors extends ChaosEvent {

	public ChaosEventSmallMeteors() {
		super("SmallMeteors", Material.FIRE_CHARGE, 15, Type.AFTER_BORDER,new String[]{});
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + " La fin du monde est arrivée, seul les élues survivront (pluis de météorites).");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!activated) {
					this.cancel();
					return;
				}
				for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
					Player player = Bukkit.getPlayer(playerUUID);
					if(player == null) {continue;}
					Location loc = player.getLocation().add(ThreadLocalRandom.current().nextInt(-20,20), 0, ThreadLocalRandom.current().nextInt(-20, 20));
					loc.setY(Divers.highestBlock(player.getLocation(),true).getBlockY() + 20);
					Fireball fireball = (Fireball) player.getWorld().spawnEntity(loc, EntityType.FIREBALL);
					fireball.setVelocity(new Vector(0, -0.1, 0).normalize());
					fireball.setDirection(new Vector(0, -1, 0).normalize());
					fireball.setYield(2);					
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
}
	