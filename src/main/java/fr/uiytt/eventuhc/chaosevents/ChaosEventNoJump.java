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

public class ChaosEventNoJump extends ChaosEvent {

	public ChaosEventNoJump() {
		super("NoJump", Material.COBBLESTONE_SLAB, 21, Type.NORMAL,new String[]{},180);
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Au moins vous gardez les pieds au sol... (NoJump)");
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
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20 * 2, 130, true));
				}
			}
		}.runTaskTimer(Main.getInstance(), 20, 20);
		
	}

	
}
