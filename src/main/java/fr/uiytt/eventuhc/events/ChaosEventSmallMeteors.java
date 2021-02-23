package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
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
		super("SmallMeteors", Material.FIRE_CHARGE, 15, Type.AFTER_BORDER, Language.splitLore(Language.EVENT_SMALL_METEORS_LORE.getMessage()));
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_SMALL_METEORS_ENABLE.getMessage());
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
					fireball.setDirection(new Vector(0,-1,0).normalize());
					fireball.setYield(2);
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
}
	