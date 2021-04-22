package fr.uiytt.eventuhc.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator.Type;
import fr.minuskube.inv.content.SlotPos;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.Register;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.events.ChaosEvent;
import fr.uiytt.eventuhc.utils.Divers;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class EventsMenu implements InventoryProvider {

	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_EventsMenu")
			.size(6, 9)
			.title(Language.GUI_TITLE_EVENTS.getMessage())
			.provider(this)
			.manager(Main.getInvManager())
			.parent(new MainMenu().INVENTORY)
			.build();
			/*.listener(new InventoryListener<>(InventoryCloseEvent.class, event -> Main.getConfigManager().setChaosEvents()))*/

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty( Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "") ));
		
		Pagination pagination = contents.pagination();
		pagination.setItemsPerPage(36);

		List<ClickableItem> clickableItems = new ArrayList<>();
		for(ChaosEvent chaosEvent : Register.getChaos_Events()) {
			if(!chaosEvent.isLoaded()) continue;
			ItemStack chaosEventItem;
			if(chaosEvent.isEnabled()) {
				chaosEventItem = Divers.ItemStackBuilder(chaosEvent.getItemMaterial(), ChatColor.GREEN + chaosEvent.getName() + " - " + Language.GUI_ENABLE.getMessage(),chaosEvent.getDescriptionLore());
			} else {
				chaosEventItem = Divers.ItemStackBuilder(chaosEvent.getItemMaterial(), ChatColor.RED + chaosEvent.getName() + " - " + Language.GUI_DISABLE.getMessage(),chaosEvent.getDescriptionLore());
			}
			clickableItems.add(ClickableItem.of(
				chaosEventItem,
				listener -> {
					chaosEvent.setEnabled(!chaosEvent.isEnabled());
					Main.getConfigManager().setChaosEventsEnable(chaosEvent);
					this.inventory.open(player, pagination.getPage());
				}
			));
		}
		pagination.setItems(clickableItems.toArray(new ClickableItem[0]));
		pagination.addToIterator(contents.newIterator(Type.HORIZONTAL, new SlotPos(1, 0)));

		contents.set(0, 0, ClickableItem.of(Divers.ItemStackBuilder(Material.PAPER, ChatColor.GRAY + "<---"), event -> inventory.getParent().ifPresent(inventory -> inventory.open(player))));
		
		if(!pagination.isLast()) {
			ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta playerheadmeta = (SkullMeta) playerhead.getItemMeta();
            playerheadmeta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_ArrowRight"));
            playerheadmeta.setDisplayName(Language.GUI_NEXT.getMessage());
            playerhead.setItemMeta(playerheadmeta);
			contents.set(5, 6, ClickableItem.of(playerhead, event -> this.inventory.open(player, pagination.next().getPage())));
		}
		if(!pagination.isFirst()) {
			ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta playerheadmeta = (SkullMeta) playerhead.getItemMeta();
            playerheadmeta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_ArrowLeft"));
            playerheadmeta.setDisplayName(Language.GUI_PREVIOUS.getMessage());
            playerhead.setItemMeta(playerheadmeta);
			contents.set(5, 2, ClickableItem.of(playerhead, event -> this.inventory.open(player, pagination.previous().getPage())));
		}
	}

	@Override
	public void update(Player player, InventoryContents contents) {


	}

}
