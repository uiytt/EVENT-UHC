package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChaosEventSpeedPig extends ChaosEvent {
	private final List<Pig> pigs = new ArrayList<>();
	
	public ChaosEventSpeedPig() {
		super("SpeedPig", Material.CARROT_ON_A_STICK, 11, Type.AFTER_BORDER, Language.splitLore(Language.EVENT_SPEED_PIG_LORE.getMessage()),220);
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		pigs.clear();

		Bukkit.broadcastMessage(Language.EVENT_SPEED_PIG_ENABLE.getMessage());
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			World world = player.getWorld();
			Pig pig = (Pig) world.spawnEntity(player.getLocation(), EntityType.PIG);
			pig.setSaddle(true);
			pig.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999 * 20, 5, true));
			pig.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999 * 20, 1,false));
			pig.addPassenger(player);
			HashMap<Integer, ItemStack> itemsnotgiven = player.getInventory().addItem(new ItemStack(Material.CARROT_ON_A_STICK));
			if(!itemsnotgiven.isEmpty()) {
				itemsnotgiven.forEach((index,item) -> world.dropItem(player.getLocation(), item));
			}
			pigs.add(pig);
		}
	}
	
	@Override
	protected void onDisable() {
		super.onDisable();
		pigs.forEach(pig -> {
			pig.eject();
			World world = pig.getWorld();
			world.dropItem(pig.getLocation(), new ItemStack(Material.PORKCHOP,3));
			pig.remove();
		});
		pigs.clear();
	}
}
