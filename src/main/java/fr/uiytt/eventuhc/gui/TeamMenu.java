package fr.uiytt.eventuhc.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameManager;
import fr.uiytt.eventuhc.game.GameTeam;
import fr.uiytt.eventuhc.utils.Divers;
import fr.uiytt.eventuhc.utils.PlayerFromUUIDNotFoundException;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamMenu implements InventoryProvider {

	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_TeamMenu")
			.title(ChatColor.DARK_GRAY + "Team")
			.size(6, 9)
			.provider(this)
			.manager(Main.getInvManager())
			.build();
	
	private int page = 0;

	public TeamMenu(int page) {
		this.page = page;
	}
	public TeamMenu() {
		this(0);
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		contents.fillBorders(ClickableItem.empty( Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + "")));
		
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		List<GameTeam> teams = GameManager.getGameInstance().getGameData().getTeams();
		SlotPos pos = new SlotPos(1, 1);
		for(int i = page * 28;i<page * 28 + 28;i++) {
			if(i >= teams.size()) {
				break;
			}
			GameTeam team = teams.get(i);
			List<String> itemLore = new ArrayList<>();
			for(UUID playerUUIFromTeam : team.getPlayersUUIDs()) {
				Player playerFromTeam = Bukkit.getPlayer(playerUUIFromTeam);
				if(playerFromTeam == null) {
					team.removePlayer(playerUUIFromTeam);
					this.inventory.open(player);
					return;
				}
				itemLore.add(ChatColor.GRAY + "-> " + playerFromTeam.getName());
			}
			int currentTeamSize = team.getPlayersUUIDs().size();
			if(currentTeamSize < Main.CONFIG.getTeamSize()) {
				while(currentTeamSize < Main.CONFIG.getTeamSize()) {
					itemLore.add(ChatColor.GRAY + "-> " + "______");
					currentTeamSize++;
				}
			}
			contents.set(pos.getRow(),
				pos.getColumn(),
				ClickableItem.of(
					Divers.ItemStackBuilder(
						team.getColor().getBanner(),
						team.getColor().getChat() + "" + ChatColor.BOLD  + team.getName(),
						itemLore.toArray(new String[]{})
					),
					event -> {
						if(team.getPlayersUUIDs().size() >= Main.CONFIG.getTeamSize()) {return;}
						try {
							GameTeam.removePlayerFromAllTeams(player.getUniqueId());
							team.addPlayer(player.getUniqueId());
						} catch (PlayerFromUUIDNotFoundException e) {
							player.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Une erreur a eu lieu lors de votre ajout dans cette team.");
							e.printStackTrace();
						}
						if(player.getInventory().getItemInMainHand().getType().toString().contains("BANNER")) {
							player.getInventory().setItemInMainHand(new ItemStack(team.getColor().getBanner()));
						}
						inventory.open(player,page);
					}
			));
			
			pos.setColumn(pos.getColumn() + 1);
			if(pos.getColumn() == 8) {
				pos.setColumn(1);
				pos.setRow(pos.getRow() +1);
			}
			
		}

		ClickableItem empty_gray_pane = ClickableItem.empty( Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + ""));
		if(page != 0) {
			contents.set(5,2, ClickableItem.of(Divers.ItemStackBuilder(Material.ARROW, ChatColor.YELLOW + "Page précédante"), event -> new TeamMenu(page-1).inventory.open(player)));
		} else {
			contents.set(5, 2,empty_gray_pane);
		}
		if(page + 1 < teams.size() / 28) {
			contents.set(5,6, ClickableItem.of(Divers.ItemStackBuilder(Material.ARROW, ChatColor.YELLOW + "Page suivante"), event -> new TeamMenu(page+1).inventory.open(player)));
		}
	}
	
	private static class SlotPos {
		private int row;
		private int column;
		public SlotPos(int row, int column) {
			this.row = row;
			this.column = column;
		}
		public int getRow() {
			return row;
		}
		public void setRow(int row) {
			this.row = row;
		}
		public int getColumn() {
			return column;
		}
		public void setColumn(int column) {
			this.column = column;
		}
		
	}
}
