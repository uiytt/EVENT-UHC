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

public class ChaosEventSpeed extends ChaosEvent {

	public ChaosEventSpeed() {
		super("Speed", Material.GOLDEN_CARROT, 19, Type.NORMAL,new String[]{});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "+ Speed XII");
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
					if(player.hasPotionEffect(PotionEffectType.SPEED)) {player.removePotionEffect(PotionEffectType.SPEED);}
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 2, 11, true));
				}
			}
		}.runTaskTimer(Main.getInstance(), 20, 20);
		
	}

	
}
