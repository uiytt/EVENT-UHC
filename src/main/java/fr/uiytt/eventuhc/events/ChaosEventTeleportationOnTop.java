package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import fr.uiytt.eventuhc.utils.Divers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChaosEventTeleportationOnTop extends ChaosEvent {

	public ChaosEventTeleportationOnTop() {
		super("TP Surface", Material.GRASS_BLOCK, 3, Type.BEFORE_PVP, Language.splitLore(Language.EVENT_TELEPORTATION_TOP_LORE.getMessage()),  10);
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_TELEPORTATION_TOP_ENABLE.getMessage());
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			Location loc = player.getLocation().clone();
			loc.setY(280);
			loc = Divers.highestBlock(loc);
			loc.setY(loc.getBlockY() + 1);
			player.teleport(loc);
		}
	}
}
