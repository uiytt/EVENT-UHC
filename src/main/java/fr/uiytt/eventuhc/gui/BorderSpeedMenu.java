package fr.uiytt.eventuhc.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.utils.Divers;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BorderSpeedMenu implements InventoryProvider {

	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_BorderSpeedMenu")
			.title(Language.GUI_TITLE_BORDER_SPEED.getMessage())
			.size(3, 9)
			.provider(this)
			.manager(Main.getInvManager())
			.parent(new MainMenu().INVENTORY)
			.build();

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty(Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "", new String[] {} )));
		contents.set(1,1, ClickableItem.of(Divers.ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "-3"), event -> {
			Main.getConfigManager().setBorderBlockPerSecond(Math.max(0.5, Main.getConfigManager().getBorderBlockPerSecond() - 3.0d));
			updateItemValue(contents);
		}));
		contents.set(1,2, ClickableItem.of(Divers.ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "-1"), event -> {
			Main.getConfigManager().setBorderBlockPerSecond(Math.max(0.5, Main.getConfigManager().getBorderBlockPerSecond() - 1.0d));
			updateItemValue(contents);
		}));
		contents.set(1,3, ClickableItem.of(Divers.ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "-0.5"), event -> {
			Main.getConfigManager().setBorderBlockPerSecond(Math.max(0.5, Main.getConfigManager().getBorderBlockPerSecond() - 0.5d));
			updateItemValue(contents);
		}));

		contents.set(1,5, ClickableItem.of(Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "+0.5"), event -> {
			Main.getConfigManager().setBorderBlockPerSecond(Main.getConfigManager().getBorderBlockPerSecond() + 0.5d);
			updateItemValue(contents);
		}));
		contents.set(1,6, ClickableItem.of(Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "+1"), event -> {
			Main.getConfigManager().setBorderBlockPerSecond(Main.getConfigManager().getBorderBlockPerSecond() + 1.0d);
			updateItemValue(contents);
		}));
		contents.set(1,7, ClickableItem.of(Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "+3"), event -> {
			Main.getConfigManager().setBorderBlockPerSecond(Main.getConfigManager().getBorderBlockPerSecond() + 3.0d);
			updateItemValue(contents);
		}));
		contents.set(0, 0, ClickableItem.of(Divers.ItemStackBuilder(Material.PAPER, ChatColor.GRAY + "<---"), event -> inventory.getParent().ifPresent(inventory -> inventory.open(player))));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		updateItemValue(contents);
	}
	private void updateItemValue(InventoryContents contents) {
		contents.set(1, 4,ClickableItem.empty(
				Divers.ItemStackBuilder(Material.FEATHER, Language.GUI_BORDER_SPEED_NAME.getMessage(),
					Language.splitLore(Language.GUI_BORDER_SPEED_LORE.getMessage().replace("%s%",String.valueOf(Main.getConfigManager().getBorderBlockPerSecond())))
				)
		));
	}
}
