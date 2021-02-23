package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ChaosEventExplosionSpawnMobs extends ChaosEvent {

	public static final List<EntityType> ENTITY_TYPES = Arrays.asList(EntityType.ZOMBIE,EntityType.CREEPER,EntityType.CAVE_SPIDER,EntityType.SPIDER,EntityType.SKELETON);
	
	public ChaosEventExplosionSpawnMobs() {
		super("ExplosionSpawnMobs", Material.TNT, 31, Type.NORMAL, Language.splitLore(Language.EVENT_EXPLOSION_SPAWN_MOBS_LORE.getMessage()));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_EXPLOSION_SPAWN_MOBS_ENABLE.getMessage());
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			player.getInventory().addItem(new ItemStack(Material.TNT,3),new ItemStack(Material.FLINT_AND_STEEL));
		}
		
	}

	
}
