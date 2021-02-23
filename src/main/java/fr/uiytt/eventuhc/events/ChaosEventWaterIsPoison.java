package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ChaosEventWaterIsPoison extends ChaosEvent {

	public ChaosEventWaterIsPoison() {
		super("Poisonous water", Material.FERMENTED_SPIDER_EYE, 7, Type.NORMAL, Language.splitLore(Language.EVENT_WATER_POISON_LORE.getMessage()));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_WATER_POISON_ENABLE.getMessage());
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
					Block block = player.getLocation().getBlock();
					if(block.getType() == Material.WATER) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3 * 20, 0));
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 20 * 20, 20);
		
	}
}
