package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ChaosEventSunlightkill extends ChaosEvent {

	public ChaosEventSunlightkill() {
		super("Sunlight Kill", Material.GLOWSTONE, 6, Type.NORMAL, new String[]{"Au soleil, les joueurs meurent"});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Restez chez vous si vous ne nouvez pas bruler... Vous brulerez au soleil dans 20 secondes (oui vous brulez aussi la nuit TGCM).");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!activated) {
					this.cancel();
					return;
				}
				if(Main.CONFIG.getWorld().hasStorm()) {
					Main.CONFIG.getWorld().setStorm(false);
				}
				for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
					Player player = Bukkit.getPlayer(playerUUID);
					if(player == null) {continue;}
					Block block = player.getLocation().getBlock();
					if((int) block.getLightFromSky() == 15) {
						player.setFireTicks(3 * 20);
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 20 * 20, 20);
	}
}
