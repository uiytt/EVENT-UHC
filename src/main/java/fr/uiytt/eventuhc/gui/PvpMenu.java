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

public class PvpMenu implements InventoryProvider {

	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_PvpMenu")
			.title(ChatColor.DARK_GRAY + "Pvp")
			.size(3, 9)
			.title(Language.GUI_TITLE_PVP_TIME.getMessage())
			.provider(this)
			.manager(Main.getInvManager())
			.parent(new MainMenu().INVENTORY)
			.build();

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty(Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "", new String[] {} )));
		contents.set(1,1, ClickableItem.of(Divers.ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "-10"), event -> {
			Main.getConfigManager().setPvpTimer(Math.max(0, Main.getConfigManager().getPvpTimer() - (10 * 60)));
			updateItemValue(contents);
		}));
		contents.set(1,2, ClickableItem.of(Divers.ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "-5"), event -> {
			Main.getConfigManager().setPvpTimer(Math.max(0, Main.getConfigManager().getPvpTimer() - (5 * 60)));
			updateItemValue(contents);
		}));
		contents.set(1,3, ClickableItem.of(Divers.ItemStackBuilder(Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "-1"), event -> {
			Main.getConfigManager().setPvpTimer(Math.max(0, Main.getConfigManager().getPvpTimer() - (60)));
			updateItemValue(contents);
		}));

		contents.set(1,5, ClickableItem.of(Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "+1"), event -> {
			Main.getConfigManager().setPvpTimer(Main.getConfigManager().getPvpTimer() + 60);
			updateItemValue(contents);
		}));
		contents.set(1,6, ClickableItem.of(Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "+5"), event -> {
			Main.getConfigManager().setPvpTimer(Main.getConfigManager().getPvpTimer() + 5 * 60);
			updateItemValue(contents);
		}));
		contents.set(1,7, ClickableItem.of(Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS_PANE, ChatColor.GREEN + "+10"), event -> {
			Main.getConfigManager().setPvpTimer(Main.getConfigManager().getPvpTimer() + 10 * 60);
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
			Divers.ItemStackBuilder(Material.DIAMOND_SWORD, Language.GUI_MAIN_PVP_NAME.getMessage(),
				Language.splitLore(Language.GUI_MAIN_PVP_LORE.getMessage().replace("%s%",String.valueOf(Main.getConfigManager().getPvpTimer()/60))))
		));
	}

}
