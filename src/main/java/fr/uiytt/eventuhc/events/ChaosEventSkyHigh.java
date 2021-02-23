package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ChaosEventSkyHigh extends ChaosEvent {

	public ChaosEventSkyHigh() {
		super("SkyHigh", Material.DIRT, 8, Type.AFTER_BORDER, Language.splitLore(Language.EVENT_SKYHIGH_LORE.getMessage()),300);
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_SKYHIGH_ENABLE.getMessage());
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
					if((int) player.getLocation().getY() < 150) {
						player.damage(1.0);
					}
					
				}
			}
		}.runTaskTimer(Main.getInstance(), 60 * 20, 20 * 5);
		
	}
}
