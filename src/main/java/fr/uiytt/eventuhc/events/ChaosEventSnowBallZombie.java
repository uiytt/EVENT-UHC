package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ChaosEventSnowBallZombie extends ChaosEvent {

	public ChaosEventSnowBallZombie() {
		super("SnowBallZombie", Material.SNOWBALL, 33, Type.AFTER_PVP, Language.splitLore(Language.EVENT_SNOWBALL_ZOMBIE_LORE.getMessage()));
	}

	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_SNOWBALL_ZOMBIE_ENABLE.getMessage());
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if (player == null) {
				continue;
			}
			player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 32));
		}
	}

	
}
