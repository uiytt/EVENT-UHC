package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ChaosEventCurseOfBinding extends ChaosEvent {

	public ChaosEventCurseOfBinding() {
		super("Curse Of Binding", Material.ENCHANTED_BOOK, 2, Type.NORMAL,new String[]{});
	}
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "AHA, Vous ne pouvez plus enlever vos armures !!");

		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			List<ItemStack> armors = Arrays.asList(player.getInventory().getArmorContents());
			for (ItemStack item : armors) {
				if (item == null) {continue;}
				item.addEnchantment(Enchantment.BINDING_CURSE, 1);
			}
			player.getInventory().setArmorContents(Arrays.copyOf(armors.toArray(), 4, ItemStack[].class));
		}
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		for(UUID playerUUID : GameManager.getGameInstance().getGameData().getAlivePlayers()) {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player == null) {continue;}
			List<ItemStack> armors = Arrays.asList(player.getInventory().getArmorContents());
			for (ItemStack item : armors) {
				if (item == null) {continue;}
				if (item.containsEnchantment(Enchantment.BINDING_CURSE)) { item.removeEnchantment(Enchantment.BINDING_CURSE); }
			}
			player.getInventory().setArmorContents(Arrays.copyOf(armors.toArray(), 4, ItemStack[].class));
		}
	}
}
