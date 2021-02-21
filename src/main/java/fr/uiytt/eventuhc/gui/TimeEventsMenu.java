package fr.uiytt.eventuhc.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.chaosevents.ChaosEvent;
import fr.uiytt.eventuhc.utils.Divers;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TimeEventsMenu implements InventoryProvider {

	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_TimeEventsMenu")
			.title(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Temps entre deux events")
			.size(3, 9)
			.provider(this)
			.manager(Main.getInvManager())
			.parent(new MainMenu().INVENTORY)
			.build();


	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty( Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "", new String[] {} )));
		contents.set(1,1, ClickableItem.of(Divers.ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "-5m"), event -> {
			Main.CONFIG.setTimeBetweenChaosEvents(Math.max(15, Main.CONFIG.getTimeBetweenChaosEvents() - 5 * 60));
			updateItemValue(contents);
		}));
		contents.set(1,2, ClickableItem.of(Divers.ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "-1m"), event -> {
			Main.CONFIG.setTimeBetweenChaosEvents(Math.max(15, Main.CONFIG.getTimeBetweenChaosEvents() - 60));
			updateItemValue(contents);
		}));
		contents.set(1,3, ClickableItem.of(Divers.ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "-15s"), event -> {
			Main.CONFIG.setTimeBetweenChaosEvents(Math.max(15, Main.CONFIG.getTimeBetweenChaosEvents() - 15));
			updateItemValue(contents);
		}));
		
		contents.set(1,5, ClickableItem.of(Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "+15s"), event -> {
			Main.CONFIG.setTimeBetweenChaosEvents(Main.CONFIG.getTimeBetweenChaosEvents() + 15);
			updateItemValue(contents);
		}));
		contents.set(1,6, ClickableItem.of(Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "+1m"), event -> {
			Main.CONFIG.setTimeBetweenChaosEvents(Main.CONFIG.getTimeBetweenChaosEvents() + 60);
			updateItemValue(contents);
		}));
		contents.set(1,7, ClickableItem.of(Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "+5m"), event -> {
			Main.CONFIG.setTimeBetweenChaosEvents(Main.CONFIG.getTimeBetweenChaosEvents() + 5 * 60);
			updateItemValue(contents);
		}));
		contents.set(0, 0, ClickableItem.of(Divers.ItemStackBuilder(Material.PAPER, ChatColor.GRAY + "<---"), event -> {
			inventory.getParent().ifPresent(inventory -> inventory.open(player));
			ChaosEvent.changeBaseDuration(Main.CONFIG.getTimeBetweenChaosEvents());
		}));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		updateItemValue(contents);
	}
	private void updateItemValue(InventoryContents contents) {
		contents.set(1, 4,ClickableItem.empty(
				Divers.ItemStackBuilder(Material.BOOK, ChatColor.YELLOW + "Temps entre deux events",
						new String[] {ChatColor.GRAY + "Il y a " + ChatColor.LIGHT_PURPLE + ((double) Main.CONFIG.getTimeBetweenChaosEvents() / 60.0) + " minutes",
								ChatColor.GRAY + "entre deux events. ",
								ChatColor.GRAY + "La majorité des events ",
								ChatColor.GRAY + "dureront le même temps."
						}
				)
		));
	}

}
