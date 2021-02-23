package fr.uiytt.eventuhc.listeners;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.events.ChaosEvent;
import fr.uiytt.eventuhc.events.ChaosEventExplosionSpawnMobs;
import fr.uiytt.eventuhc.events.ChaosEventLifeShare;
import fr.uiytt.eventuhc.events.ChaosEventWeirdFight;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventsListerner implements Listener {

	// For event PigSpeed
	@EventHandler
	public void onVehicleExit(VehicleExitEvent event) {
		if (!GameManager.getGameInstance().getGameData().isGameRunning()) {
			return;
		}
		GameManager.getGameInstance().getGameData().getCurrentChaosEvents().forEach(chaosevent -> {
			if (chaosevent.getId() == 11) { //ChaosEventSpeedPig
				event.setCancelled(true);
			}
		});
	}

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (!GameManager.getGameInstance().getGameData().isGameRunning()) {
			return;
		}
		for(ChaosEvent chaosEvent : GameManager.getGameInstance().getGameData().getCurrentChaosEvents()) {
			if (chaosEvent.getId() == 16) { //NoGravityForEntites
				event.getEntity().setGravity(false);
				return;
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!GameManager.getGameInstance().getGameData().isGameRunning()) {
			return;
		}
		for(ChaosEvent chaosEvent : GameManager.getGameInstance().getGameData().getCurrentChaosEvents()) {
			if (chaosEvent.getId() == 17) { //ChaosEventBlocksAttack
				if (event.getBlock().getType() == Material.STONE
						|| event.getBlock().getType() == Material.DIRT
						|| event.getBlock().getType() == Material.GRASS) {
					return;
				}
				if (ThreadLocalRandom.current().nextInt(5) != 0) {
					return;
				}
				World world = event.getBlock().getWorld();
				Location loc = event.getBlock().getLocation();
				loc.add(0.5,0.5,0.5);
				Zombie mob = (Zombie) world.spawnEntity(loc, EntityType.ZOMBIE);
				mob.setBaby(true);
				mob.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999 * 20, 0, true));
				EntityEquipment equipment = mob.getEquipment();
				if(equipment != null) {
					equipment.setHelmet(new ItemStack(event.getBlock().getType(), 1));
				}

				mob.setHealth(3);
				mob.setMetadata("chaosevent_blockMob", new FixedMetadataValue(Main.getInstance(), "true"));
				return;
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (!GameManager.getGameInstance().getGameData().isGameRunning()) {
			return;
		}
		if (event.getEntityType() != EntityType.ZOMBIE) {
			return;
		}
		if (event.getEntity().hasMetadata("chaosevent_blockMob")) {
			event.getDrops().clear();
			event.getDrops().add(event.getEntity().getEquipment().getHelmet());
		}
	}

	@EventHandler
	public void onProjectilHit(ProjectileHitEvent event) {
		if (!GameManager.getGameInstance().getGameData().isGameRunning()) {
			return;
		}
		for (ChaosEvent chaosEvent : GameManager.getGameInstance().getGameData().getCurrentChaosEvents()) {

			if (chaosEvent.getId() == 33) { //ChaosEventSnowBallZombie
				if(event.getEntity().getType() != EntityType.SNOWBALL) {return;}
				Block hitBlock = event.getHitBlock();
				Location loc;
				if(hitBlock != null) {
					loc = hitBlock.getLocation().add(0, 1, 0);
				} else {
					if(event.getHitEntity() == null) {return;}
					loc = event.getHitEntity().getLocation();
				}

				Zombie entity = (Zombie) event.getEntity().getWorld().spawnEntity(loc, EntityType.ZOMBIE);
				entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 99999, 0));

			} else if (chaosEvent.getId() == 18) { //ChaosEventBowSwap
				if (!(event.getEntity() instanceof Arrow) || event.getHitEntity() == null) {return;}
				Entity shooter = (Entity) event.getEntity().getShooter();
				if(shooter == null) {continue;}
				Location loc1 = shooter.getLocation();
				shooter.teleport(event.getHitEntity().getLocation());
				event.getHitEntity().teleport(loc1);

			} else if (chaosEvent.getId() == 39) { // No fight with bow (ChaosEventWeirdFight)
				Entity shooter = (Entity) event.getEntity().getShooter();
				if (!(shooter instanceof Player) || !(event.getEntity() instanceof Arrow)) {return;}
				Player damager = (Player) shooter;
				Vector velocity = new Vector(ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
						1,
						ThreadLocalRandom.current().nextDouble(-0.5, 0.5)).normalize();

				//Force the player to drop all the bows
				HashMap<Integer,? extends ItemStack> bows = damager.getInventory().all(Material.BOW);
				for (Map.Entry<Integer,? extends ItemStack> bow : bows.entrySet() ) {
					damager.getWorld().dropItem(damager.getLocation(), bow.getValue()).setVelocity(velocity);
					damager.getInventory().setItem(bow.getKey(),new ItemStack(Material.AIR));
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamagedbyEntity(EntityDamageByEntityEvent event) {
		if (!GameManager.getGameInstance().getGameData().isGameRunning()) {
			return;
		}
		for (ChaosEvent chaosEvent : GameManager.getGameInstance().getGameData().getCurrentChaosEvents()) {
			if (chaosEvent.getId() == 39) { // No fight with sword (ChaosEventWeirdFight)
				if(event.getDamager().getType() != EntityType.PLAYER) {continue;}
				Player damager = (Player) event.getDamager();
				if (ChaosEventWeirdFight.SWORDS.contains(damager.getInventory().getItemInMainHand().getType()) || ChaosEventWeirdFight.SWORDS.contains(damager.getInventory().getItemInOffHand().getType())) {
					Vector velocity = new Vector(ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
							1,
							ThreadLocalRandom.current().nextDouble(-0.5, 0.5)).normalize();
					damager.getWorld().dropItem(damager.getLocation(), damager.getInventory().getItemInMainHand())
							.setVelocity(velocity);
					damager.getInventory().remove(damager.getInventory().getItemInMainHand());
				}

			} else if (chaosEvent.getId() == 26) { //ChaosEventNoDamageByMobs
				if(event.getDamager().getType() != EntityType.PLAYER) {
					event.setCancelled(true);
				}
			}
		}

	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (!GameManager.getGameInstance().getGameData().isGameRunning() || !(event.getEntity() instanceof Player)) {
			return;
		}
		List<ChaosEvent> currentChaosEvent = GameManager.getGameInstance().getGameData().getCurrentChaosEvents();
		for (ChaosEvent chaosEvent : currentChaosEvent) {
			if (chaosEvent.getId() == 22) { //ChaosEventLifeShare
				ChaosEventLifeShare chaosEventLifeShare = (ChaosEventLifeShare) chaosEvent;
				Player player = (Player) event.getEntity();
				UUID linkedPlayerUUID = chaosEventLifeShare.getLinkPlayers().get(player.getUniqueId());
				Player linkedPlayer = Bukkit.getPlayer(linkedPlayerUUID);
				double newHealth = (player.getHealth() - event.getFinalDamage()) / 2;
				if(linkedPlayer != null) {
					linkedPlayer.setHealth(newHealth);
				}

			} else if (chaosEvent.getId() == 38) { //ChaosEventDamageDropItems
				Player player = (Player) event.getEntity();
				PlayerInventory inv = player.getInventory();
				List<Integer> filledSlot = new ArrayList<>();
				for (int i = 0; i < 36; i++) {
					ItemStack item = inv.getItem(i);
					if (item != null && item.getType() != Material.AIR) {
						filledSlot.add(i);
					}
				}
				int slot = filledSlot.get(ThreadLocalRandom.current().nextInt(filledSlot.size()));
				Vector velocity = new Vector(ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
						1,
						ThreadLocalRandom.current().nextDouble(-0.5, 0.5)).normalize();
				ItemStack item = inv.getItem(slot);
				if(item ==null) {
					try {
						throw new Exception("Item with slot " + slot + " was not found in the inventory.");
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				player.getWorld().dropItem(player.getLocation(), item ).setVelocity(velocity);
				inv.clear(slot);

			}
		}
	}

	@EventHandler
	public void onHeal(EntityRegainHealthEvent event) {
		if (!GameManager.getGameInstance().getGameData().isGameRunning() || !(event.getEntity() instanceof Player)) {
			return;
		}
		for (ChaosEvent chaosEvent : GameManager.getGameInstance().getGameData().getCurrentChaosEvents()) {
			if (chaosEvent.getId() == 22) { //ChaosEventLifeShare
				ChaosEventLifeShare chaosEventLifeShare = (ChaosEventLifeShare) chaosEvent;
				Player player = (Player) event.getEntity();
				UUID linkedPlayerUUID = chaosEventLifeShare.getLinkPlayers().get(player.getUniqueId());
				Player linkedPlayer = Bukkit.getPlayer(linkedPlayerUUID);
				if(linkedPlayer != null) {
					linkedPlayer.setHealth(player.getHealth());
				}
			}
		}
	}

	@EventHandler
	public void EntityExplode(ExplosionPrimeEvent event) {
		if (!GameManager.getGameInstance().getGameData().isGameRunning() || event.getEntity().getType() != EntityType.CREEPER) {
			return;
		}
		for (ChaosEvent chaosEvent : GameManager.getGameInstance().getGameData().getCurrentChaosEvents()) {
			if (chaosEvent.getId() == 31) {
				new BukkitRunnable() {

					@Override
					public void run() {
						Location loc = event.getEntity().getLocation();
						for (int i = 0; i < 10; i++) {
							EntityType entity = ChaosEventExplosionSpawnMobs.ENTITY_TYPES.get(
									ThreadLocalRandom.current().nextInt(ChaosEventExplosionSpawnMobs.ENTITY_TYPES.size()));
							event.getEntity().getWorld().spawnEntity(loc, entity);
						}

					}
				}.runTaskLater(Main.getInstance(), 10);
			}
		}
	}
}
