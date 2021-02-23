package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventPopcorn extends ChaosEvent {

	public ChaosEventPopcorn() {
		super("Popcorn", Material.WHEAT, 40, Type.NORMAL, Language.splitLore(Language.EVENT_POPCORN_LORE.getMessage()));

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_POPCORN_ENABLE.getMessage());
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
					Vector velocity = new Vector(ThreadLocalRandom.current().nextDouble(-1, 1), ThreadLocalRandom.current().nextDouble(0.5), ThreadLocalRandom.current().nextDouble(-1, 1));
					player.setVelocity(velocity);
				}
			}
		}.runTaskTimer(Main.getInstance(), 20*5, 20*3);
	}

	

}
