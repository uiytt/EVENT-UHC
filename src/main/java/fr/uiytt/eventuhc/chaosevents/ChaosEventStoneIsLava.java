package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChaosEventStoneIsLava extends ChaosEvent {

	public ChaosEventStoneIsLava() {
		super("Stone is LAVA", Material.LAVA_BUCKET, 1, Type.NORMAL, new String[]{}, 10);
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + ChatColor.RED + ChatColor.BOLD + "La stone autour des joueurs va se transformer en lave dans 60 secondes (protégez vous) ");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!activated) {
					this.cancel();
					return;
				}
				final int radius = 2;
				List<Block> blocklist = new ArrayList<>();
				for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
					Player player = Bukkit.getPlayer(playerUUID);
					if(player == null) {continue;}
					Block block = player.getLocation().getBlock();
					for (int x = radius; x >= -radius; x--) {
				        for (int y = radius; y >= -radius; y--) {
				            for (int z = radius; z >= -radius; z--) {
				            	if(block.getRelative(x, y, z).getType() == Material.STONE) {blocklist.add(block.getRelative(x, y, z));}
				            }
				        }
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				new BukkitRunnable() {
					
					@Override
					public void run() {
						blocklist.forEach(block -> block.setType(Material.LAVA));
					}
				}.runTask(Main.getInstance());
				
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 20 * 60, 20);
	}
}
