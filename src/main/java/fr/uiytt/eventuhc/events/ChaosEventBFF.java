package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ChaosEventBFF extends ChaosEvent {

	public ChaosEventBFF() {
		super("BFF (free dog)", Material.BONE, 34, Type.NORMAL, Language.splitLore(Language.EVENT_BFF_LORE.getMessage()),30);

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_BFF_ENABLE.getMessage());
		for(UUID playerUUID :  GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
			wolf.setOwner(player);
			player.giveExpLevels(1);
			player.getInventory().addItem(new ItemStack(Material.NAME_TAG));
			player.updateInventory();
		}
	}

	
}
