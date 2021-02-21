package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventGoldenApple extends ChaosEvent {

	public ChaosEventGoldenApple() {
		super("GoldenApple", Material.GOLDEN_APPLE, 37, Type.NORMAL, new String[]{"Les joueurs recoivent entre une","et deux pommes d'or"},20);

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Vous avez reçus un cadeau.");
		int ammount = ThreadLocalRandom.current().nextInt(1) +1;
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, ammount));
			
		}
	}

	
}
