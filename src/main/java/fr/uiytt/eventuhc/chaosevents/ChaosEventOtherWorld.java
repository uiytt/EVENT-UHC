package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
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
		super("OtherWorld", Material.NETHERRACK, 13, Type.AFTER_PVP, new String[]{"Tp tous les joueurs aux mêmes cordoonées dans le nether"},300);
	}
	

	@Override
	protected void onEnable() {
		super.onEnable();
		previousLocation.clear();

		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Ça vous dit un voyage vers un autre monde ? Tant mieux vous n'avez pas le choix :D \n P.S : Vous reviendrez à la fin");
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}

			World destinationWorld = player.getWorld().getEnvironment() == Environment.NETHER ? Main.CONFIG.getWorld() : Main.CONFIG.getNether();
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
				player.sendMessage(ConfigManager.EVENT_HEADER + "Votre tp a été annulé car il y a de la lave à votre destination.");
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
