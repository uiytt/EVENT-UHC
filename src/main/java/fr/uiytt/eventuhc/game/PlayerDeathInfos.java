package fr.uiytt.eventuhc.game;

import fr.uiytt.eventuhc.utils.Divers;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PlayerDeathInfos {

	private final ItemStack[] inventory;
	private final Location location;
	private final GameTeam team;
	public PlayerDeathInfos(Player player,@Nullable Location location,ItemStack[] inventory,@Nullable GameTeam team) {
		this.inventory = inventory;
		if(location != null) {
			this.location = location;
		} else {
			this.location = Divers.highestBlock(Divers.randomLocation(player.getWorld()));
		}
		this.team = team;
	}
	
	public ItemStack[] getInventory() {
		return inventory;
	}
	public Location getLocation() {
		return location;
	}
	/**
	 * @return can return something null if there is no team in game
	 */
	@Nullable
	public GameTeam getTeam() {
		return team;
	}

}
