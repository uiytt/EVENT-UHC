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

public class ChaosEventSlowness extends ChaosEvent {

	public ChaosEventSlowness() {
		super("Slowness", Material.IRON_BOOTS, 4, Type.NORMAL,new String[]{});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Rien ne sert de courir ; il faut partir à point. (slowness)");
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
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999*20, 1, true));
				}
			}
		}.runTaskTimer(Main.getInstance(), 20, 20);
		
	}
	@Override
	protected void onDisable() {
		super.onDisable();
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			if(player.hasPotionEffect(PotionEffectType.SLOW)) {
				player.removePotionEffect(PotionEffectType.SLOW);
			}
		}
	}
	
}
