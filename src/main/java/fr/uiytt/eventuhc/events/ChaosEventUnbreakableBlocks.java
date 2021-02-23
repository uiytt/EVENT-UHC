package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ChaosEventUnbreakableBlocks extends ChaosEvent {

	public ChaosEventUnbreakableBlocks() {
		super("UnbreakableBlocks", Material.WOODEN_PICKAXE, 14, Type.AFTER_BORDER, Language.splitLore(Language.EVENT_UNBREAKABLE_BLOCKS_LORE.getMessage()));
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_UNBREAKABLE_BLOCKS_ENABLE.getMessage());
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!activated) {
					this.cancel();
					return;
				}
				for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
					Player player = Bukkit.getPlayer(playerUUID);
					if (player == null) {
						continue;
					}
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 3, 3));
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
}
	