package fr.uiytt.eventuhc.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.utils.Divers;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DeathItemsMenu implements InventoryProvider {

	private static final List<UUID> playersModifyingItems = new ArrayList<>();
	private static final HashMap<UUID,GameMode> playersGamemode = new HashMap<>();
	private static final HashMap<UUID, ItemStack[]> playersInventory = new HashMap<>();
	
	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_DeathItemsMenu")
			.size(6, 9)
			.title(Language.GUI_TITLE_DEATH_ITEMS.getMessage())
			.provider(this)
			.manager(Main.getInvManager())
			.parent(new MainMenu().INVENTORY)
			.build();


	@Override
	public void init(Player player, InventoryContents contents) {
		ItemStack empty_gray_pane = Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "");
		List<ItemStack> items = new ArrayList<>(Arrays.asList(Main.getConfigManager().getDeathItems()));
		for(int i=0;i<items.size();i++) {
			ItemStack item = items.get(i);
			if(item == null || item.getType() == Material.AIR) {
				items.set(i,Divers.ItemStackBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + ""));
			}
		}
		contents.fillRow(4, ClickableItem.empty(empty_gray_pane));
		for(int i=0;i<36;i++) {
			ItemStack item = items.get(i);
			contents.add(ClickableItem.empty(item));
		}
		//Armor 
		contents.set(5, 0, ClickableItem.empty(items.get(36)));
		contents.set(5, 1, ClickableItem.empty(items.get(37)));
		contents.set(5, 2, ClickableItem.empty(items.get(38)));
		contents.set(5, 3, ClickableItem.empty(items.get(39)));

		contents.set(5,4,ClickableItem.empty(empty_gray_pane));
		//offhand item
		contents.set(5, 5, ClickableItem.empty(items.get(40)));

		contents.set(5,6,ClickableItem.empty(empty_gray_pane));
		
		contents.set(5,7,ClickableItem.of(Divers.ItemStackBuilder(Material.PAPER, ChatColor.GRAY + "<---"), event -> inventory.getParent().ifPresent(inventory -> inventory.open(player))));

		contents.set(5,8, ClickableItem.of(Divers.ItemStackBuilder(Material.EMERALD_BLOCK, Language.GUI_OTHER_ADD_ITEMS.getMessage()), event -> {
			player.closeInventory();
			playersModifyingItems.add(player.getUniqueId());
			playersGamemode.put(player.getUniqueId(), player.getGameMode());
			playersInventory.put(player.getUniqueId(), player.getInventory().getContents());
			player.getInventory().clear();
			player.getInventory().setContents(Main.getConfigManager().getDeathItems());
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage(Language.GUI_OTHER_FINISH.getMessage());
		}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}

	public static List<UUID> getPlayersModifyingItems() {
		return playersModifyingItems;
	}

	public static HashMap<UUID, GameMode> getPlayersGamemode() {
		return playersGamemode;
	}

	public static HashMap<UUID, ItemStack[]> getPlayersInventory() {
		return playersInventory;
	}

}
