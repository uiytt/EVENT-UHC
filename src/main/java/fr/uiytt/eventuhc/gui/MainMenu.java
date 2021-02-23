package fr.uiytt.eventuhc.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameTeam;
import fr.uiytt.eventuhc.utils.Divers;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainMenu implements InventoryProvider {

	public final SmartInventory INVENTORY = SmartInventory.builder()
		.id("EUHC_MainMenu")
		.size(4, 9)
		.title(Language.GUI_TITLE_MAIN.getMessage())
		.provider(this)
		.manager(Main.getInvManager())
		.build();

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty( Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "") ));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		contents.set(1, 1, ClickableItem.of(
			Divers.ItemStackBuilder(Material.DIAMOND_SWORD, Language.GUI_MAIN_PVP_NAME.getMessage(),
				Language.splitLore(Language.GUI_MAIN_PVP_LORE.getMessage().replace("%s%",String.valueOf(Main.CONFIG.getPvpTimer()/60)))
			), event -> new PvpMenu().inventory.open(player)));

		contents.set(1, 2, ClickableItem.of(
			Divers.ItemStackBuilder(Material.BARRIER, Language.GUI_BORDER_TIME_NAME.getMessage(),
				Language.splitLore(Language.GUI_BORDER_TIME_LORE.getMessage().replace("%s%",String.valueOf(Main.CONFIG.getBorderTimer()/60)))
			), event -> new BorderTimeMenu().inventory.open(player)));

		contents.set(1, 3, ClickableItem.of(
			Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS, Language.GUI_BORDER_START_NAME.getMessage(),
				Language.splitLore(Language.GUI_BORDER_START_LORE.getMessage().replace("%s%",String.valueOf(Main.CONFIG.getBorderStart())))
			), event -> new BorderStartMenu().inventory.open(player)));

		contents.set(1, 4, ClickableItem.of(
			Divers.ItemStackBuilder(Material.RED_STAINED_GLASS, Language.GUI_BORDER_END_NAME.getMessage(),
				Language.splitLore(Language.GUI_BORDER_END_LORE.getMessage().replace("%s%",String.valueOf(Main.CONFIG.getBorderEnd())))
			), event -> new BorderEndMenu().inventory.open(player)));

		contents.set(1, 5, ClickableItem.of(
			Divers.ItemStackBuilder(Material.CHEST, Language.GUI_MAIN_START_ITEMS_NAME.getMessage(),
				Language.splitLore(Language.GUI_MAIN_START_ITEMS_LORE.getMessage())
			), event -> new StartItemsMenu().inventory.open(player)));
		contents.set(1, 6, ClickableItem.of(
			Divers.ItemStackBuilder(Material.ENDER_CHEST, Language.GUI_MAIN_DEATH_ITEMS_NAME.getMessage(),
				Language.splitLore(Language.GUI_MAIN_DEATH_ITEMS_LORE.getMessage())
			), event -> new DeathItemsMenu().inventory.open(player)));

		contents.set(1, 7, ClickableItem.of(
			Divers.ItemStackBuilder(Material.BOOK, Language.GUI_MAIN_TIME_EVENTS_NAME.getMessage(),
				Language.splitLore(Language.GUI_MAIN_TIME_EVENTS_LORE.getMessage().replace("%s%",String.valueOf((double) Main.CONFIG.getTimeBetweenChaosEvents() / 60.0)))
			), event -> new TimeEventsMenu().inventory.open(player)));

		contents.set(2, 1, ClickableItem.of(
			Divers.ItemStackBuilder(Material.ENDER_PEARL, Language.GUI_MAIN_EVENTS_NAME.getMessage(),
				Language.splitLore(Language.GUI_MAIN_EVENTS_LORE.getMessage())
			), event -> new EventsMenu().inventory.open(player)));

		contents.set(2, 2, ClickableItem.of(
			Divers.ItemStackBuilder(Material.FEATHER, Language.GUI_BORDER_SPEED_NAME.getMessage(),
				Language.splitLore(Language.GUI_BORDER_SPEED_LORE.getMessage().replace("%s%",String.valueOf(Main.CONFIG.getBorderBlockPerSecond())))
			), event -> new BorderSpeedMenu().inventory.open(player)));

		contents.set(2, 3, ClickableItem.of(
			Divers.ItemStackBuilder(Material.COMPARATOR, Language.GUI_MAIN_ADVANCED_NAME.getMessage(),
				Language.splitLore(Language.GUI_MAIN_ADVANCED_LORE.getMessage())
			), event -> new AdvancedMenu().inventory.open(player)));

		if (Main.CONFIG.getTeamSize() <= 1) {
			contents.set(2, 4, ClickableItem.of(
				Divers.ItemStackBuilder(Material.WHITE_BANNER, Language.GUI_MAIN_TEAMS_SIZE_NAME.getMessage(),
					Language.splitLore(Language.GUI_MAIN_TEAMS_FFA_LORE.getMessage())
				), event -> {
					Main.CONFIG.setTeamSize(2);
					GameTeam.reorganize_team();
			}));
		} else {
			contents.set(2, 4, ClickableItem.of(
				Divers.ItemStackBuilder(Material.WHITE_BANNER, Language.GUI_MAIN_TEAMS_SIZE_NAME.getMessage(),
					Language.splitLore(Language.GUI_MAIN_TEAMS_SIZE_LORE.getMessage().replace("%s%",String.valueOf(Main.CONFIG.getTeamSize())))
				), event -> {
					Main.CONFIG.setTeamSize(Main.CONFIG.getTeamSize() + 1);
					if(Main.CONFIG.getTeamSize() == 8) {
						Main.CONFIG.setTeamSize(1);
					}
					GameTeam.reorganize_team();
			}));
		}
	}

}
