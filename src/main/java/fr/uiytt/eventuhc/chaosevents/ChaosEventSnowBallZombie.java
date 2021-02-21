package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ChaosEventSnowBallZombie extends ChaosEvent {

	public ChaosEventSnowBallZombie() {
		super("SnowBallZombie", Material.SNOWBALL, 33, Type.AFTER_PVP, new String[]{"Lancer une boule de neige fait","spawn un zombie"});

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Essayez de lancer une boule de neige");
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if (player == null) {
				continue;
			}
			player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 32));
		}
	}

	
}
