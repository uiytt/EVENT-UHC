package fr.uiytt.eventuhc.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Divers {
	/**
	 * Always return an ItemStack with a size of 41
	 * @param defaultItems an invenory, might be null or incorrect.
	 * @return a working ItemStack[]
	 */
	public static ItemStack[] securizeItemStackInventory(ItemStack[] defaultItems) {
		if(defaultItems != null && defaultItems.length == 41) {return defaultItems;}
		return new ItemStack[41];
	}

	public static ItemStack ItemStackBuilder(Material material, String name, List<String> lore) {
		return ItemStackBuilder(material,name,lore.toArray(new String[0]),1);
	}

	public static ItemStack ItemStackBuilder(Material material,String name, String[] lore,int ammount) {
		ItemStack item = new ItemStack(material,ammount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		if(lore.length > 0) {
			meta.setLore(Arrays.asList(lore));
		}
		item.setItemMeta(meta);
		return item;
		
	}
	public static ItemStack ItemStackBuilder(Material material,String name, String[] lore) {
		return ItemStackBuilder(material, name, lore, 1);
		
	}
	public static ItemStack ItemStackBuilder(Material material,String name) {
		return ItemStackBuilder(material, name, new String[] {}, 1);
	}

	/**
	 * Return a random {@link Location} in the border
	 * @param world The {@link World} where the location is
	 * @return {@link Location} Location
	 */
	public static Location randomLocation(World world) {
		Location loc = null;
		for (int i =0;i<25;i++) {
			int random_x = ThreadLocalRandom.current().nextInt((int)(0 - world.getWorldBorder().getSize() / 2),(int) world.getWorldBorder().getSize() / 2);
			int random_z = ThreadLocalRandom.current().nextInt((int)(0 - world.getWorldBorder().getSize() / 2),(int) world.getWorldBorder().getSize() / 2);
			loc = new Location(world, random_x, 300, random_z);
			Location highestBlock = highestBlock(loc,true);
			if(highestBlock.getBlock().getType() != Material.WATER && highestBlock.getBlock().getType() != Material.LAVA) {
				break;
			}
		}
		return loc;
	}

	/**
	 * Return a {@link Location} where the highest block of this location is.
	 * @param originalLoc The location where it must search.
	 * @param acceptTransparentBlock Check if the block is transparent.
	 * @return Highest location where there is a block.
	 */
	public static Location highestBlock(Location originalLoc, boolean acceptTransparentBlock) {
		Location loc = originalLoc.clone();
		loc.setY(originalLoc.getWorld().getMaxHeight());
		while(true) {
			loc.setY(loc.getBlockY() - 1);
			if(loc.getBlockY() == -1) {
				loc.setY(300d);
				break;
			}
			if(loc.getBlockY() >= originalLoc.getWorld().getMaxHeight()) {continue;}

			//break if block is occluding or if this accept transparent block
			//and of course if the material is not air
			if(loc.getBlock().getType().isOccluding() || acceptTransparentBlock) {
				if(loc.getBlock().getType() != Material.AIR) {
					break;
				}
			}
		}
		return loc;

	}
	/**
	 * Return a {@link Location} where the highest block of this location is.
	 * It include all transparent blocks.
	 * @param loc The location where it must search.
	 * @return Highest location where there is a block.
	 */
	public static Location highestBlock(Location loc) {
		return highestBlock(loc, true);
	}


}
