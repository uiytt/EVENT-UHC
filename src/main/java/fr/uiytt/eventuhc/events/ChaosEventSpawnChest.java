package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.utils.Divers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.loot.LootTables;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventSpawnChest extends ChaosEvent {
	@Deprecated
	public ChaosEventSpawnChest() {
		super("SpawnChest", Material.CHEST, 29, Type.BEFORE_BORDER, new String[]{"Des coffres avec des items puissants spawn aléatoirement"});
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		List<Location> locs = new ArrayList<>();
		for(int i = 0;i<ThreadLocalRandom.current().nextInt(5) + 2;i++) {
			locs.add(Divers.highestBlock(Divers.randomLocation(Main.getConfigManager().getWorld())));
		}
		locs.forEach(loc -> {
			Bukkit.broadcastMessage("Un coffre d'items très puissants apparait aux cords X:" + loc.getX() + " Y:" + loc.getBlockY() + " Z:" + loc.getZ());
			loc.getBlock().setType(Material.CHEST);
			loc.getBlock().getState().update(true);
			Chest block = (Chest) loc.getBlock().getState();
			block.setLootTable(LootTables.STRONGHOLD_LIBRARY.getLootTable());
			block.update(true);
		});
	}

	
}
