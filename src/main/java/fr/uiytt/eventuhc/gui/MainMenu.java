package fr.uiytt.eventuhc.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameTeam;
import fr.uiytt.eventuhc.utils.Divers;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainMenu implements InventoryProvider {

	public final SmartInventory INVENTORY = SmartInventory.builder()
			.id("EUHC_MainMenu")
			.title(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Config")
			.size(4, 9)
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
				Divers.ItemStackBuilder(Material.DIAMOND_SWORD, ChatColor.YELLOW  + "Début du pvp",
						new String[] {ChatColor.GRAY + "Le pvp commencera ", ChatColor.GRAY + "au bout de " + ChatColor.YELLOW + (Main.CONFIG.getPvpTimer() / 60) + ChatColor.GRAY +" minutes" }
				), event -> new PvpMenu().inventory.open(player)));
		contents.set(1, 2, ClickableItem.of(
				Divers.ItemStackBuilder(Material.BARRIER, ChatColor.YELLOW + "Temps avant la bordure",
						new String[] {ChatColor.GRAY + "La bordure commencera ", ChatColor.GRAY + "à bouger au bout de " + ChatColor.YELLOW + (Main.CONFIG.getBorderTimer()/60) + ChatColor.GRAY +" minutes" }
				), event -> new BorderMenu().inventory.open(player)));
		contents.set(1, 3, ClickableItem.of(
				Divers.ItemStackBuilder(Material.LIME_STAINED_GLASS, ChatColor.YELLOW + "Bordure de départ",
						new String[] {ChatColor.GRAY + "La bordure de " + ChatColor.GREEN + "départ " + ChatColor.GRAY + "est ", 
								ChatColor.GRAY + "définit à " + ChatColor.YELLOW + "+" + Main.CONFIG.getBorderStart() + "/-" + Main.CONFIG.getBorderStart()
						}
				), event -> new BorderStartMenu().inventory.open(player)));
		contents.set(1, 4, ClickableItem.of(
				Divers.ItemStackBuilder(Material.RED_STAINED_GLASS, ChatColor.YELLOW + "Bordure finale",
						new String[] {ChatColor.GRAY + "La bordure " + ChatColor.RED + "finale " + ChatColor.GRAY + "est ", 
								ChatColor.GRAY + "définit à " + ChatColor.YELLOW + "+" + Main.CONFIG.getBorderEnd() + "/-" + Main.CONFIG.getBorderEnd()
						}
				), event -> new BorderEndMenu().inventory.open(player)));
		contents.set(1, 5, ClickableItem.of(
				Divers.ItemStackBuilder(Material.CHEST, ChatColor.YELLOW + "Items de départ",
						new String[] {ChatColor.GRAY + "Les items qui sont donnés au ", 
								ChatColor.GRAY + "début de la partie aux joueurs",
								"",
								ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
						}
				), event -> new StartItemsMenu().inventory.open(player)));
		contents.set(1, 6, ClickableItem.of(
				Divers.ItemStackBuilder(Material.ENDER_CHEST, ChatColor.YELLOW + "Items de mort",
						new String[] {ChatColor.GRAY + "Les items qui sont droppés à ", 
								ChatColor.GRAY + "la mort d'un joueur ",
								"",
								ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
						}
				), event -> new DeathItemsMenu().inventory.open(player)));
		contents.set(1, 7, ClickableItem.of(
				Divers.ItemStackBuilder(Material.BOOK, ChatColor.YELLOW + "Temps entre deux events",
						new String[] {ChatColor.GRAY + "Il y a " + ChatColor.LIGHT_PURPLE + ((double) Main.CONFIG.getTimeBetweenChaosEvents() / 60.0) + " minutes",
								ChatColor.GRAY + "entre deux events. ",
								ChatColor.GRAY + "La majorité des events ",
								ChatColor.GRAY + "dureront le même temps.",
								"",
								ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
						}
				), event -> new TimeEventsMenu().inventory.open(player)));
		contents.set(2, 1, ClickableItem.of(
				Divers.ItemStackBuilder(Material.ENDER_PEARL, ChatColor.YELLOW + "Events",
						new String[] {
								ChatColor.GRAY + "activer/désactiver les events",
								"",
								ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
						}
				), event -> new EventsMenu().inventory.open(player)));
		contents.set(2, 2, ClickableItem.of(
				Divers.ItemStackBuilder(Material.FEATHER, ChatColor.YELLOW + "Vitesse de la bordure",
						new String[] {ChatColor.GRAY + "La bordure fait du", 
								ChatColor.YELLOW + "" + Main.CONFIG.getBorderBlockPerSecond() + " bloc(s)/seconde",
								"",
								ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
						}
				), event -> new BorderSpeedMenu().inventory.open(player)));
		contents.set(2, 3, ClickableItem.of(
				Divers.ItemStackBuilder(Material.COMPARATOR, ChatColor.YELLOW + "Paramètres avancés",
						new String[] {
								ChatColor.GRAY + "Quelques paramètres avancés", 
								"",
								ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
						}
				), event -> new AdvancedMenu().inventory.open(player)));
		if (Main.CONFIG.getTeamSize() <= 1) {
			contents.set(2, 4, ClickableItem.of(
					Divers.ItemStackBuilder(Material.WHITE_BANNER, ChatColor.YELLOW + "Taille des teams",
							new String[] {
									ChatColor.GRAY + "FFA", 
									"",
									ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
							}
					), event -> {
						Main.CONFIG.setTeamSize(2);
						GameTeam.reorganize_team();
			}));
		} else {
			contents.set(2, 4, ClickableItem.of(
					Divers.ItemStackBuilder(Material.WHITE_BANNER, ChatColor.YELLOW + "Taille des teams",
							new String[] {
									ChatColor.GRAY + "" + Main.CONFIG.getTeamSize() + "vs" + Main.CONFIG.getTeamSize(),
									"",
									ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
							}
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
