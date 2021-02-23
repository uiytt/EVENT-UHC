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
import org.bukkit.inventory.ItemStack;

public class SpectatorInventoryMenu implements InventoryProvider {

	private final Player target;

	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_SpectatorInventory")
			.title(Language.GUI_TITLE_RESPAWN.getMessage())
			.size(6, 9)
			.provider(this)
			.manager(Main.getInvManager())
			.parent(new MainMenu().INVENTORY)
			.build();

	public SpectatorInventoryMenu(Player target) {
		this.target = target;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		ItemStack empty_gray_pane = Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "");
		ItemStack[] targetInventory = target.getInventory().getContents();
		
		contents.fillRow(4, ClickableItem.empty(empty_gray_pane));
		for(int i=0;i<36;i++) {
			ItemStack item = targetInventory[i];
			contents.add(ClickableItem.empty(item));

		}
		//Armor 
		contents.set(5, 0, ClickableItem.empty(targetInventory[36]));
		contents.set(5, 1, ClickableItem.empty(targetInventory[37]));
		contents.set(5, 2, ClickableItem.empty(targetInventory[38]));
		contents.set(5, 3, ClickableItem.empty(targetInventory[39]));

		contents.set(5,4,ClickableItem.empty(empty_gray_pane));
		//offhand item
		contents.set(5, 5, ClickableItem.empty(targetInventory[40]));

		contents.fillRect(5,6,5,8,ClickableItem.empty(empty_gray_pane));

	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}


}
