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

public class ChaosEventJump extends ChaosEvent {

	public ChaosEventJump() {
		super("JumpBoost", Material.POTION, 28, Type.NORMAL, Language.splitLore(Language.EVENT_JUMP_BOOST_LORE.getMessage()));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_JUMP_BOOST_ENABLE.getMessage());
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
					if(player.hasPotionEffect(PotionEffectType.JUMP)) {player.removePotionEffect(PotionEffectType.JUMP);}
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 2, 4, true));
				}
			}
		}.runTaskTimer(Main.getInstance(), 20, 20);
		
	}

	
}
