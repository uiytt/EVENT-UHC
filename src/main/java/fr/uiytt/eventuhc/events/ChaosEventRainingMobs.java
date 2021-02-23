package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ChaosEventRainingMobs extends ChaosEvent {

	private final List<EntityType> entities;
	
	public ChaosEventRainingMobs() {
		super("RainingMobs", Material.WATER_BUCKET, 32, Type.NORMAL, Language.splitLore(Language.EVENT_RAINING_MOBS_LORE.getMessage()),180);
		List<EntityType> entityTypeList;
		entityTypeList = Arrays.asList(EntityType.values()).stream()
				.filter(EntityType::isAlive)
				.collect(Collectors.toList());
		entityTypeList.remove(EntityType.ENDER_DRAGON);
		entityTypeList.remove(EntityType.WITHER);
		entityTypeList.remove(EntityType.ELDER_GUARDIAN);
		entityTypeList.remove(EntityType.PLAYER);
		entities = entityTypeList;
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_RAINING_MOBS_ENABLE.getMessage());
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
					Location loc = player.getLocation().add(ThreadLocalRandom.current().nextInt(-20,20), 0, ThreadLocalRandom.current().nextInt(-20, 20));
					loc.setY(90);
					EntityType entitytype = entities.get(ThreadLocalRandom.current().nextInt(entities.size()));
					LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(loc, entitytype);
					entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 12*20, 0));

				}
			}
		}.runTaskTimer(Main.getInstance(), 20, 40);

		
	}

	
}
