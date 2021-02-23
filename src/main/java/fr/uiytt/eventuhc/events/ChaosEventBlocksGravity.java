package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChaosEventBlocksGravity extends ChaosEvent {

	private static final int RADIUS = 10;

	public ChaosEventBlocksGravity() {
		super("BlocksGravity", Material.GRAVEL, 24, Type.NORMAL, Language.splitLore(Language.EVENT_BLOCKS_GRAVITY_LORE.getMessage()));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_BLOCKS_GRAVITY_ENABLE.getMessage());
	
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
					Player player = Bukkit.getPlayer(playerUUID);
					if(player == null) {continue;}
					List<Block> blocks = new ArrayList<>();
					Block block = player.getLocation().getBlock();
					//Loop in a radius
					for(int x = -RADIUS; x <= RADIUS; x++) {
			            for(int y = -RADIUS; y <= RADIUS; y++) {
			                for(int z = -RADIUS; z <= RADIUS; z++) {
			                    Block block_loop = block.getRelative(x, y, z);
			                    if(block_loop.getRelative(0, -1, 0).getType() == Material.AIR && block_loop.getType() != Material.AIR) {
			                    	blocks.add(block_loop);
			                    	block_loop.setType(Material.LAPIS_BLOCK);
			                    }
			                    
			                }
			            }
			        }
					blocks.forEach(defaultBlock -> {
						Block loopBlock = defaultBlock;
						while(loopBlock.getType() != Material.AIR && loopBlock.getY() <= 256) {
							BlockData loop_blockdata = loopBlock.getBlockData();
							loopBlock.setType(Material.AIR);
							loopBlock.getWorld().spawnFallingBlock(loopBlock.getLocation(), loop_blockdata);
							loopBlock = loopBlock.getRelative(0, 1, 0);
						}
					});
				}
				
				
			}
		}.runTaskTimer(Main.getInstance(), 20 * 20, 20 * 3);
		
	}


	
}
