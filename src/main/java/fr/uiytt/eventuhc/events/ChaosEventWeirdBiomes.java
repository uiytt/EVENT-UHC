package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.utils.Divers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventWeirdBiomes extends ChaosEvent {
	private static List<Material> generatedMaterials = new ArrayList<>();

	public ChaosEventWeirdBiomes() {
		super("WeirdBiomes", Material.RED_GLAZED_TERRACOTTA, 10, Type.NORMAL, Language.splitLore(Language.EVENT_WEIRD_BIOMES_LORE.getMessage()));
	}
	private final List<WeirdBiome> weirdBiomes = new ArrayList<>();
	private int radius = 1;
	private int Cx = -radius;
	private int Cz = -radius;


	@Override
	protected void onEnable() {
		super.onEnable();
		//Reset vars in case of game after another game.
		weirdBiomes.clear();
		radius = 1;
		Cx = -1;
		Cz = -1;

		for(int i = 0;i<ThreadLocalRandom.current().nextInt(4) + 1;i++) {
			//Add a random number of biomes
			//biomes are init with the highest block of a random location and random materials for blocs
			weirdBiomes.add(new WeirdBiome(Divers.highestBlock(Divers.randomLocation(Main.getConfigManager().getWorld())).getBlock(), getRandomMaterialsList()));
		}
		Bukkit.broadcastMessage(Language.EVENT_WEIRD_BIOMES_ENABLE.getMessage());
		weirdBiomes.forEach(biome -> Bukkit.broadcastMessage(
				Language.EVENT_WEIRD_BIOMES_COORDS.getMessage()
						.replace("%X%",String.valueOf(biome.center.getX()))
						.replace("%Z%",String.valueOf(biome.center.getZ()))
				)
		);

		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!activated) {
					this.cancel();
					return;
				}
				//grow the circle
				if(Cx > radius) {
					radius += 1;
					Cx = -radius;
					Cz = -radius;
				}
				weirdBiomes.forEach(weirdBiome -> {
					Block block = weirdBiome.getCenter();
					List<Block> changingBlocks = new ArrayList<>();
					//add the blocs to change
					changingBlocks.add(block.getRelative(Cx, 0, radius));
					changingBlocks.add(block.getRelative(Cx, 0, -radius));
					changingBlocks.add(block.getRelative(radius, 0, Cz));
					changingBlocks.add(block.getRelative(-radius, 0, -Cz));
					//for each, find the highest block and change it
					for (Block blocktochange : changingBlocks) {
						Divers.highestBlock(blocktochange.getLocation(), false).getBlock().setType(weirdBiome.getRandomMaterial());
					}
				});
				//grow radius
				Cx += 1;
				Cz += 1;
			}
		}.runTaskTimer(Main.getInstance(),15*20,20);
		
	}

	//return a random list of block materials
	private static List<Material> getRandomMaterialsList() {
		List<Material> resultMaterials = new ArrayList<>();
		for(int i = 0;i<ThreadLocalRandom.current().nextInt(2)+3;i++) {
			resultMaterials.add(generatedMaterials.get(ThreadLocalRandom.current().nextInt(generatedMaterials.size())));
		}
		return resultMaterials;
	}

	public static List<String> initDefaultGeneratedMaterials() {
		List<String> result = new ArrayList<>();
		for(Material material : Material.values()) {
			if(material.name().contains("LEGACY_")) {continue;}
			if(!material.isInteractable() && !material.hasGravity() && !material.name().toLowerCase().contains("banner") && !material.name().toLowerCase().contains("coral")){
				result.add(material.name());
			}
		}
		result.remove(Material.DIAMOND_ORE.name());
		result.remove(Material.DIAMOND_BLOCK.name());
		result.remove(Material.GOLD_ORE.name());
		result.remove(Material.GOLD_BLOCK.name());
		result.remove(Material.LAPIS_ORE.name());
		result.remove(Material.LAPIS_BLOCK.name());
		result.remove(Material.REDSTONE_ORE.name());
		result.remove(Material.REDSTONE_BLOCK.name());
		result.remove(Material.IRON_ORE.name());
		result.remove(Material.IRON_BLOCK.name());
		result.remove(Material.COAL_ORE.name());
		result.remove(Material.NETHER_QUARTZ_ORE.name());
		result.remove(Material.EMERALD_ORE.name());
		result.remove(Material.EMERALD_BLOCK.name());

		return result;
	}

	public static void setGeneratedMaterials(List<Material> generatedMaterials) {
		ChaosEventWeirdBiomes.generatedMaterials = generatedMaterials;
	}

	static class WeirdBiome {
		private final Block center;
		private final List<Material> materials;
		protected WeirdBiome(Block center,List<Material> materialList) {
			this.center = center;
			this.materials = materialList;
		}
		
		
		public Block getCenter() {
			return center;
		}
		public Material getRandomMaterial() {
			return materials.get(ThreadLocalRandom.current().nextInt(materials.size()));
		}
	}
}
