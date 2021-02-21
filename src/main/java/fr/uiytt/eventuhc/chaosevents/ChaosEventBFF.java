package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
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
		super("BFF (chien gratuit)", Material.BONE, 34, Type.NORMAL,new String[]{"Les joueurs recoivent un chien."},30);

	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Voici votre nouveau Best Friend Forever : un chien.");
		for(UUID playerUUID :  GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			for(int i=0;i<2;i++) {
				Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);
				wolf.setOwner(player);
			}
			player.giveExpLevels(2);
			player.getInventory().addItem(new ItemStack(Material.NAME_TAG,2));
			player.updateInventory();
		}
	}

	
}
