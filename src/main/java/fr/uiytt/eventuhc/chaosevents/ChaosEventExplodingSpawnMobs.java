package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ChaosEventExplodingSpawnMobs extends ChaosEvent {

	public static final List<EntityType> ENTITY_TYPES = Arrays.asList(EntityType.ZOMBIE,EntityType.CREEPER,EntityType.CAVE_SPIDER,EntityType.SPIDER,EntityType.SKELETON);
	
	public ChaosEventExplodingSpawnMobs() {
		super("ExplodingSpawnMobs", Material.TNT, 31, Type.NORMAL,new String[]{"Lorsqu'un creeper ou une tnt explose,","10 mobs apparaissent"});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Les explosions font apparaîtrent des monstres... Conseil : Ne faites pas exploser les creepers");
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			player.getInventory().addItem(new ItemStack(Material.TNT,3),new ItemStack(Material.FLINT_AND_STEEL));
		}
		
	}

	
}
