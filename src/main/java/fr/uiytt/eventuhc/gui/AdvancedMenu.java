package fr.uiytt.eventuhc.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.utils.Divers;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class AdvancedMenu implements InventoryProvider {

	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_AdvancedMenu")
			.title(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Paramètres avancés")
			.size(3, 9)
			.provider(this)
			.manager(Main.getInvManager())
			.parent(new MainMenu().INVENTORY)
			.build();


	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty( Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "")));
		contents.set(0, 0, ClickableItem.of(Divers.ItemStackBuilder(Material.PAPER, ChatColor.GRAY + "<---"), event -> inventory.getParent().ifPresent(inventory -> inventory.open(player))));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		contents.set(1, 1, ClickableItem.of(Divers.ItemStackBuilder(Material.APPLE, ChatColor.YELLOW + "Drop des pommes", new String[] {
				ChatColor.GRAY + "Le drop de pommes est de " + ChatColor.AQUA + "" + Main.CONFIG.getApplesDrop() +"%",
				"",
				ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
		}), event -> new ApplesDropMenu().inventory.open(player)));
		contents.set(1, 2, ClickableItem.of(Divers.ItemStackBuilder(Material.FLINT, ChatColor.YELLOW + "Drop de silex", new String[] {
				ChatColor.GRAY + "Le drop de silex est de " + ChatColor.AQUA + "" + Main.CONFIG.getFlintsDrop() +"%",
				"",
				ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
		}), event -> new FlintsDropMenu().inventory.open(player)));

		ItemStack potionLv2Item;
		if(Main.CONFIG.isPotionLv2()) {
			potionLv2Item = Divers.ItemStackBuilder(Material.POTION, ChatColor.YELLOW + "Potions II", new String[] {
					ChatColor.GRAY + "Les potionwinners level II sont " + ChatColor.GREEN + "activé",
					"",
					ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
			});
		} else {
			potionLv2Item = Divers.ItemStackBuilder(Material.POTION, ChatColor.YELLOW + "Potions II", new String[] {
					ChatColor.GRAY + "Les potions level II sont " + ChatColor.RED + "désactivé",
					"",
					ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
			});
		}
		contents.set(1, 3, ClickableItem.of(potionLv2Item, event -> Main.CONFIG.setPotionLv2(!Main.CONFIG.isPotionLv2())));

		ItemStack diamondLimitItem;
		if(Main.CONFIG.isDiamondLimit()) {
			diamondLimitItem = Divers.ItemStackBuilder(Material.DIAMOND_ORE, ChatColor.YELLOW + "DiamondLimit", new String[] {
					ChatColor.GRAY + "La diamondLimit est " + ChatColor.GREEN + "activé",
					ChatColor.GRAY + "sa limite actuelle est de " + ChatColor.AQUA + "" + Main.CONFIG.getDiamondlimitAmmount() + " diamants",
					"",
					ChatColor.WHITE + "Clique gauche"+ ChatColor.BOLD+" >> " + ChatColor.RED + ""  + "Desactiver",
					ChatColor.WHITE + "Clique droit"+ ChatColor.BOLD+" >> " + ChatColor.AQUA + "" + "Changer la limite"
			});
		} else {
			diamondLimitItem = Divers.ItemStackBuilder(Material.DIAMOND_ORE, ChatColor.YELLOW + "DiamondLimit", new String[] {
					ChatColor.GRAY + "La diamondLimit est " + ChatColor.RED + "désactivé",
					ChatColor.GRAY + "sa limite actuelle est de " + ChatColor.AQUA + "" + Main.CONFIG.getDiamondlimitAmmount() + " diamants",
					"",
					ChatColor.WHITE + "Clique gauche"+ ChatColor.BOLD+" >> " + ChatColor.GREEN + ""  + "Activer",
					ChatColor.WHITE + "Clique droit"+ ChatColor.BOLD+ " >> " + ChatColor.AQUA + "" + "Changer la limite"
			});
		}
		contents.set(1, 4, ClickableItem.of(diamondLimitItem, event -> {
			if(event.getClick() == ClickType.LEFT) {
				Main.CONFIG.setDiamondLimit(!Main.CONFIG.isDiamondLimit());
			} else {
				new DiamondLimitMenu().inventory.open(player);
			}
		}));
		switch (Main.CONFIG.getDeconnectionRule()) {
			case INSTANT_KICK:
				contents.set(1,5, ClickableItem.of(
					Divers.ItemStackBuilder(Material.LEVER, ChatColor.YELLOW +"Règle de déconnection", new String[] {
						ChatColor.WHITE + ">>" + ChatColor.BOLD + " Instant" + ChatColor.GRAY + ": Les joueurs se",
						ChatColor.GRAY + "font instantanément kick",
						ChatColor.GRAY + ">> NORMAL: Les joueurs se",
						ChatColor.GRAY +"font kick après 30 secondes",
						ChatColor.GRAY + ">> PRIVE: Les joueurs se",
						ChatColor.GRAY +"font kick après 1 minute",
						ChatColor.GRAY + ">> AMIS: Les joueurs ne",
						ChatColor.GRAY +"se font pas kick. (dangereux)"
					}),
					event -> Main.CONFIG.setDeconnectionRule(DeconnectionRule.NORMAL_KICK)
				));
				break;

			case NORMAL_KICK:
				contents.set(1,5, ClickableItem.of(
					Divers.ItemStackBuilder(Material.LEVER, ChatColor.YELLOW +"Règle de déconnection", new String[] {
						ChatColor.GRAY + ">> Instant: Les joueurs se",
						ChatColor.GRAY + "font instantanément kick",
						ChatColor.WHITE + ">>" + ChatColor.BOLD + " NORMAL" + ChatColor.GRAY + ": Les joueurs se",
						ChatColor.GRAY +"font kick après 30 secondes",
						ChatColor.GRAY + ">> PRIVE: Les joueurs se",
						ChatColor.GRAY +"font kick après 1 minute",
						ChatColor.GRAY + ">> AMIS: Les joueurs ne",
						ChatColor.GRAY +"se font pas kick. (dangereux)"
					}),
					event -> Main.CONFIG.setDeconnectionRule(DeconnectionRule.PRIVATE_KICK)
				));
				break;

			case PRIVATE_KICK:
				contents.set(1,5, ClickableItem.of(
					Divers.ItemStackBuilder(Material.LEVER, ChatColor.YELLOW +"Règle de déconnection", new String[] {
						ChatColor.GRAY + ">> Instant: Les joueurs se",
						ChatColor.GRAY +"font instantanément kick",
						ChatColor.GRAY + ">> NORMAL: Les joueurs se",
						ChatColor.GRAY +"font kick après 30 secondes",
						ChatColor.WHITE + ">>" +ChatColor.BOLD +" PRIVE" + ChatColor.GRAY+ ": Les joueurs se",
						ChatColor.GRAY +"font kick après 1 minute",
						ChatColor.GRAY + ">> AMIS: Les joueurs ne",
						ChatColor.GRAY +"se font pas kick. (dangereux)"
					}),
					event -> Main.CONFIG.setDeconnectionRule(DeconnectionRule.NO_KICK)
				));
				break;
			case NO_KICK:
				contents.set(1,5, ClickableItem.of(
					Divers.ItemStackBuilder(Material.LEVER, ChatColor.YELLOW +"Règle de déconnection", new String[] {
						ChatColor.GRAY + ">> Instant: Les joueurs se",
						ChatColor.GRAY +"font instantanément kick",
						ChatColor.GRAY + ">> NORMAL: Les joueurs se",
						ChatColor.GRAY +"font kick après 30 secondes",
						ChatColor.GRAY + ">> PRIVE: Les joueurs se",
						ChatColor.GRAY +"font kick après 1 minute ",
						ChatColor.WHITE + ">>" + ChatColor.BOLD + " AMIS" + ChatColor.GRAY + ": Les joueurs ne",
						ChatColor.GRAY +"se font pas kick. (dangereux)"
					}),
					event -> Main.CONFIG.setDeconnectionRule(DeconnectionRule.INSTANT_KICK)
				));
				break;
		}

		ItemStack cutclean;
		if(Main.CONFIG.isCutClean()) {
			cutclean = Divers.ItemStackBuilder(Material.IRON_AXE, ChatColor.YELLOW + "Cutclean", new String[] {
					ChatColor.GRAY + "Le cutclean est " + ChatColor.GREEN + "activé"
			});
		} else {
			cutclean = Divers.ItemStackBuilder(Material.IRON_AXE, ChatColor.YELLOW + "Cutclean", new String[] {
					ChatColor.GRAY + "Le cutclean est " + ChatColor.RED + "désactivé"
			});
		}
		contents.set(1, 6, ClickableItem.of(cutclean, event -> Main.CONFIG.setCutClean(!Main.CONFIG.isCutClean())));
		ItemStack finalHealItem;
		if(Main.CONFIG.isFinalHeal()) {
			finalHealItem = Divers.ItemStackBuilder(Material.GOLDEN_APPLE, ChatColor.YELLOW + "FinalHeal", new String[] {
					ChatColor.GRAY + "Les joueurs sont heal avant le pvp: " + ChatColor.GREEN + "activé",
					"",
					ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
			});
		} else {
			finalHealItem = Divers.ItemStackBuilder(Material.GOLDEN_APPLE, ChatColor.YELLOW + "FinalHeal", new String[] {
					ChatColor.GRAY + "Les joueurs sont heal avant le pvp: " + ChatColor.RED + "désactivé",
					"",
					ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
			});
		}
		contents.set(1,7,ClickableItem.of(finalHealItem,event -> {
			Main.CONFIG.setFinalHeal(!Main.CONFIG.isFinalHeal());
			inventory.open(player);
		}));

		ItemStack displayLifeItem;
		if(Main.CONFIG.isDisplayLife()) {
			displayLifeItem = Divers.ItemStackBuilder(Material.OAK_BOAT, ChatColor.YELLOW + "Vie visible", new String[] {
					ChatColor.GRAY + "La vie est visible dans le Tab: " + ChatColor.GREEN + "activé",
					"",
					ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
			});
		} else {
			displayLifeItem = Divers.ItemStackBuilder(Material.OAK_BOAT, ChatColor.YELLOW + "Vie visible", new String[] {
					ChatColor.GRAY + "La vie est visible dans le Tab: " + ChatColor.RED + "désactivé",
					"",
					ChatColor.GREEN + ">> " + ChatColor.BOLD + "Cliquez pour changer"
			});
		}
		contents.set(2,1,ClickableItem.of(displayLifeItem,event -> {
			Main.CONFIG.setDisplayLife(!Main.CONFIG.isDisplayLife());
			inventory.open(player);
		}));
	}


}
