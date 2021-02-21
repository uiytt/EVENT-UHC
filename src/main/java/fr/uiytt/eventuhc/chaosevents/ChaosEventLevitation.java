package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ChaosEventLevitation extends ChaosEvent {

	public ChaosEventLevitation() {
		super("Levitation", Material.FEATHER, 25, Type.NORMAL, new String[]{});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Vers l'infini et l'au-delà. (Vous avez lévitation de manière régulière).");
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
					if(player.hasPotionEffect(PotionEffectType.LEVITATION)) {player.removePotionEffect(PotionEffectType.LEVITATION);}
					player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 5, 1, true));
				}
			}
		}.runTaskTimer(Main.getInstance(), 20 * 10, 20 * 20);
		
	}

	
}
