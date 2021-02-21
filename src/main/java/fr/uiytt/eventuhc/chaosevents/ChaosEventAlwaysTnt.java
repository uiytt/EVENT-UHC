package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
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
		super("Tnt timer", Material.TNT, 5, Type.NORMAL, new String[]{"Une tnt spawn sur les joueurs","toutes les 20s"});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "BOOM BOOM BOOM (une tnt toutes les 20 secondes apparaitera sur vous :D ) ");
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
		}.runTaskTimer(Main.getInstance(), 20 * 20, 20 * 20);
	}
}
