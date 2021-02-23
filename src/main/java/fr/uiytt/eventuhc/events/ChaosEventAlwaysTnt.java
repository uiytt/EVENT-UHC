package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

public class ChaosEventAlwaysTnt extends ChaosEvent {

	public ChaosEventAlwaysTnt() {
		super("Tnt timer", Material.TNT, 5, Type.NORMAL, Language.splitLore(Language.EVENT_TNT_TIMER_LORE.getMessage()));
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_TNT_TIMER_ENABLE.getMessage());
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
					Location loc = player.getLocation();
					Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, EntityType.PRIMED_TNT);
				}

			}
		}.runTaskTimer(Main.getInstance(), 30 * 20, 20 * 20);
	}
}
