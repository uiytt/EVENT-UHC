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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class AdvancedMenu implements InventoryProvider {

	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_AdvancedMenu")
			.size(3, 9)
			.title(Language.GUI_TITLE_ADVANCED.getMessage())
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
		contents.set(1, 1, ClickableItem.of(Divers.ItemStackBuilder(Material.APPLE, Language.GUI_ADVANCED_APPLES_DROP_NAME.getMessage(),
				Language.splitLore(Language.GUI_ADVANCED_APPLES_DROP_LORE.getMessage().replace("%s%",String.valueOf(Main.getConfigManager().getApplesDrop())))
				), event -> new ApplesDropMenu().inventory.open(player)));
		contents.set(1, 2, ClickableItem.of(Divers.ItemStackBuilder(Material.FLINT, Language.GUI_ADVANCED_FLINTS_DROP_NAME.getMessage(),
				Language.splitLore(Language.GUI_ADVANCED_FLINTS_DROP_LORE.getMessage().replace("%s%",String.valueOf(Main.getConfigManager().getFlintsDrop())))
		), event -> new FlintsDropMenu().inventory.open(player)));

		String potionLV2Enabled = Main.getConfigManager().isPotionLv2() ? Language.GUI_ENABLE.getMessage() : Language.GUI_DISABLE.getMessage();
		contents.set(1, 3, ClickableItem.of(Divers.ItemStackBuilder(
				Material.POTION,
				Language.GUI_ADVANCED_POTIONLV2_NAME.getMessage(),
				Language.splitLore(Language.GUI_ADVANCED_POTIONLV2_LORE.getMessage().replace("%enable%",potionLV2Enabled))
			), event -> Main.getConfigManager().setPotionLv2(!Main.getConfigManager().isPotionLv2())));

		ItemStack diamondLimitItem;
		if(Main.getConfigManager().isDiamondLimit()) {
			diamondLimitItem = Divers.ItemStackBuilder(Material.DIAMOND_ORE, Language.GUI_ADVANCED_DIAMOND_LIMIT_NAME.getMessage(),
				Language.splitLore(Language.GUI_ADVANCED_DIAMOND_LIMIT_ENABLE.getMessage().replace("%s%",String.valueOf(Main.getConfigManager().getDiamondlimitAmmount())))
			);
		} else {
			diamondLimitItem = Divers.ItemStackBuilder(Material.DIAMOND_ORE, Language.GUI_ADVANCED_DIAMOND_LIMIT_NAME.getMessage(),
				Language.splitLore(Language.GUI_ADVANCED_DIAMOND_LIMIT_DISABLE.getMessage().replace("%s%",String.valueOf(Main.getConfigManager().getDiamondlimitAmmount())))
			);
		}
		contents.set(1, 4, ClickableItem.of(diamondLimitItem, event -> {
			if(event.getClick() == ClickType.LEFT) {
				Main.getConfigManager().setDiamondLimit(!Main.getConfigManager().isDiamondLimit());
			} else {
				new DiamondLimitMenu().inventory.open(player);
			}
		}));

		String cutCleanEnabled = Main.getConfigManager().isCutClean() ? Language.GUI_ENABLE.getMessage() : Language.GUI_DISABLE.getMessage();
		contents.set(1, 5, ClickableItem.of(Divers.ItemStackBuilder(
				Material.IRON_AXE,
				Language.GUI_ADVANCED_CUTCLEAN_NAME.getMessage(),
				Language.splitLore(Language.GUI_ADVANCED_CUTCLEAN_LORE.getMessage().replace("%enable%",cutCleanEnabled))
			), event -> {
				Main.getConfigManager().setCutClean(!Main.getConfigManager().isCutClean());
				inventory.open(player);
		}));

		String finalHealEnabled = Main.getConfigManager().isFinalHeal() ? Language.GUI_ENABLE.getMessage() : Language.GUI_DISABLE.getMessage();
		contents.set(1,6,ClickableItem.of(Divers.ItemStackBuilder(
				Material.GOLDEN_APPLE,
				Language.GUI_ADVANCED_FINAL_HEAL_NAME.getMessage(),
				Language.splitLore(Language.GUI_ADVANCED_FINAL_HEAL_LORE.getMessage().replace("%enable%",finalHealEnabled))
			),event -> {
				Main.getConfigManager().setFinalHeal(!Main.getConfigManager().isFinalHeal());
				inventory.open(player);
		}));

		String displayLifeEnabled = Main.getConfigManager().isDisplayLife() ? Language.GUI_ENABLE.getMessage() : Language.GUI_DISABLE.getMessage();

		contents.set(1,7,ClickableItem.of(Divers.ItemStackBuilder(
				Material.OAK_BOAT,
				Language.GUI_ADVANCED_VISIBLE_HEALTH_NAME.getMessage(),
				Language.splitLore(Language.GUI_ADVANCED_VISIBLE_HEALTH_LORE.getMessage().replace("%enable%",displayLifeEnabled))
			),event -> {
				Main.getConfigManager().setDisplayLife(!Main.getConfigManager().isDisplayLife());
				inventory.open(player);
		}));
	}


}
