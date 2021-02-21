package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventRandomInventory extends ChaosEvent {

	public ChaosEventRandomInventory() {
		super("RandomInventory", Material.COMPARATOR, 12, Type.NORMAL, new String[]{"L'inventaire se mélange aléatoirement "},240);
	}
	

	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Random Inventory !!! L'ordre de vos items est aléatoire.");
		new BukkitRunnable() {
			
			@Override
			public void run() {
				while(activated) {
					
					try {
						Thread.sleep(1000 * (ThreadLocalRandom.current().nextInt(20) + 20));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					new BukkitRunnable() {
						
						@Override
						public void run() {
							for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
								Player player = Bukkit.getPlayer(playerUUID);
								if(player == null) {continue;}
								List<ItemStack> items = Arrays.asList(player.getInventory().getStorageContents());
								player.getInventory().setStorageContents(new ItemStack[36]);
								Collections.shuffle(items);
								ItemStack[] itemsArray = Arrays.copyOf(items.toArray(), 36, ItemStack[].class);
								player.getInventory().setStorageContents(itemsArray);
							}
							
						}
					}.runTask(Main.getInstance());
				}
				
			}
		}.runTaskAsynchronously(Main.getInstance());
		
	}
	
}
