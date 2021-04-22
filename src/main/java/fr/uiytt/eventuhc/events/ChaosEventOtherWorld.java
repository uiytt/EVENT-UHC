package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class ChaosEventOtherWorld extends ChaosEvent {
	private final HashMap<UUID, Location> previousLocation = new HashMap<>();

	public ChaosEventOtherWorld() {
		super("OtherWorld", Material.NETHERRACK, 13, Type.AFTER_PVP, Language.splitLore(Language.EVENT_OTHER_WORLD_LORE.getMessage()),300);
	}
	

	@Override
	protected void onEnable() {
		super.onEnable();
		previousLocation.clear();

		Bukkit.broadcastMessage(Language.EVENT_OTHER_WORLD_ENABLE.getMessage());
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}

			World destinationWorld = player.getWorld().getEnvironment() == Environment.NETHER ? Main.getConfigManager().getWorld() : Main.getConfigManager().getNether();
			if(destinationWorld.getBlockAt(player.getLocation()).getType() != Material.LAVA) {
				previousLocation.put(player.getUniqueId(), player.getLocation());

				Block destination = destinationWorld.getBlockAt(player.getLocation());
				destination.setType(Material.OBSIDIAN);
				destination.getRelative(1, 0, 0).setType(Material.OBSIDIAN);
				destination.getRelative(1, 0, 1).setType(Material.OBSIDIAN);
				destination.getRelative(1, 0, -1).setType(Material.OBSIDIAN);
				destination.getRelative(0, 0, 1).setType(Material.OBSIDIAN);
				destination.getRelative(0, 0, -1).setType(Material.OBSIDIAN);
				destination.getRelative(-1, 0, 0).setType(Material.OBSIDIAN);
				destination.getRelative(-1, 0, 1).setType(Material.OBSIDIAN);
				destination.getRelative(-1, 0, -1).setType(Material.OBSIDIAN);
				destination.getRelative(0, 1, 0).setType(Material.AIR);
				destination.getRelative(0, 2, 0).setType(Material.AIR);

				player.teleport(destination.getLocation().add(0.5, 1, 0.5));
				player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 90 * 20, 5));

			} else {
				player.sendMessage(Language.EVENT_OTHER_WORLD_CANCEL.getMessage());
			}
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			Location prevloc = previousLocation.get(player.getUniqueId());
			player.teleport(prevloc);
		}
		previousLocation.clear();
	}
}
