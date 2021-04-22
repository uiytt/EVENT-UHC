package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ChaosEventSunlightKills extends ChaosEvent {

	public ChaosEventSunlightKills() {
		super("Sunlight Kill", Material.GLOWSTONE, 6, Type.NORMAL, Language.splitLore(Language.EVENT_SUNLIGHT_KILLS_LORE.getMessage()));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_SUNLIGHT_KILLS_ENABLE.getMessage());
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!activated) {
					this.cancel();
					return;
				}
				if(Main.getConfigManager().getWorld().hasStorm()) {
					Main.getConfigManager().getWorld().setStorm(false);
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
