package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class ChaosEventUnbreakableBlocks extends ChaosEvent {

	public ChaosEventUnbreakableBlocks() {
		super("UnbreakableBlocks", Material.WOODEN_PICKAXE, 14, Type.AFTER_BORDER, new String[]{});
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + " Les blocs sont incassables. Let's play " + ChatColor.STRIKETHROUGH + "Mine" + ChatColor.RESET + "craft");
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
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 3, 3), true);
				}
			}
		}.runTaskTimer(Main.getInstance(), 0, 20);
	}
}
	