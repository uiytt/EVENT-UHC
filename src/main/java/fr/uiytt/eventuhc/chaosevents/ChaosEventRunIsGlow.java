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

public class ChaosEventRunIsGlow extends ChaosEvent {

	public ChaosEventRunIsGlow() {
		super("Run = Glow", Material.GLOWSTONE, 20, Type.NORMAL,new String[]{});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Dés que vous courez, vous brillez ! ");
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
					if(player.isSprinting()) {
						if (player.hasPotionEffect(PotionEffectType.GLOWING)) {
							player.removePotionEffect(PotionEffectType.GLOWING);
						}
						player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 2, 4, true));
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 20, 10);
		
	}

	
}
