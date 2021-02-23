package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventGoldenApple extends ChaosEvent {

	public ChaosEventGoldenApple() {
		super("GoldenApple", Material.GOLDEN_APPLE, 37, Type.NORMAL, Language.splitLore(Language.EVENT_GOLDEN_APPLE_LORE.getMessage()),20);

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_GOLDEN_APPLE_ENABLE.getMessage());
		int ammount = ThreadLocalRandom.current().nextInt(1) +1;
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, ammount));
			
		}
	}

	
}
