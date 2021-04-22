package fr.uiytt.eventuhc;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.events.ChaosEvent;
import fr.uiytt.eventuhc.game.GameData;
import fr.uiytt.eventuhc.game.GameManager;
import fr.uiytt.eventuhc.gui.DeathItemsMenu;
import fr.uiytt.eventuhc.gui.MainMenu;
import fr.uiytt.eventuhc.gui.RespawnMenu;
import fr.uiytt.eventuhc.gui.StartItemsMenu;
import fr.uiytt.eventuhc.gui.TeamsMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Command implements CommandExecutor,TabCompleter{

	public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command cmd, @NotNull String arg, String[] args) {
		if(args.length < 1) {
			sendHelp(sender);
			return true;
		}
		GameManager game = GameManager.getGameInstance();
		GameData gameData = game.getGameData();
		if(args[0].equals("start")) {
			if(!sender.hasPermission("event-uhc.start")) {
				sender.sendMessage(Language.WARNING_PERMISSION.getMessage());
				return true;
			}
			if(gameData.isGameRunning()) {
				sender.sendMessage(Language.WARNING_GAME_ON.getMessage());
				return true;
			}

			if(Bukkit.getOnlinePlayers().size() >= 2) {
				game.init(new ArrayList<>(Bukkit.getServer().getOnlinePlayers()));
			} else {
				sender.sendMessage(Language.WARNING_NOT_ENOUGH_PLAYERS.getMessage());
			}

			return true;
			
			
		} else if(args[0].equals("stop")) {
			if(!sender.hasPermission("event-uhc.stop")) {
				sender.sendMessage(Language.WARNING_PERMISSION.getMessage());
				return true;
			}
			if(!gameData.isGameRunning()) {
				sender.sendMessage(Language.WARNING_GAME_OFF.getMessage());
				return true;
			}
			game.stopGame();
			Bukkit.broadcastMessage(Language.COMMAND_STOP.getMessage());
			return true;
			
		} else if(args[0].equalsIgnoreCase("force")) {
			if(!sender.hasPermission("event-uhc.force")) {
				sender.sendMessage(Language.WARNING_PERMISSION.getMessage());
				return true;
			}
			if(!gameData.isGameRunning()) {
				sender.sendMessage(Language.WARNING_GAME_OFF.getMessage());
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(Language.COMMAND_SYNTAX.getMessage() +"/event-uhc force (pvp|border)");
				return true;
			}
			if(args[1].equalsIgnoreCase("pvp")) {
				if(!gameData.wasPvpEnabled()) {
					game.enablePVP();
					return true;
				}
				sender.sendMessage(Language.COMMAND_FORCE_PVP.getMessage());
			} else if (args[1].equalsIgnoreCase("border")) {
				if(!gameData.isBorderAlreadyMoving()) {
					game.enableBorder();
					return true;
				}
				sender.sendMessage(Language.COMMAND_FORCE_BORDER.getMessage());
			} else {
				sender.sendMessage(Language.COMMAND_SYNTAX.getMessage() + "/event-uhc force (pvp|border)");
			}
			return true;
			
		} else if(args[0].equalsIgnoreCase("reload")) {
			if(!sender.hasPermission("event-uhc.reload")) {
				sender.sendMessage(Language.WARNING_PERMISSION.getMessage());
				return true;
			}
			if(gameData.isGameRunning()) {
				sender.sendMessage(Language.WARNING_GAME_ON.getMessage());
				return true;
			}
			Main.getConfigManager().forceReload();
			Main.getConfigManager().load();
			ChaosEvent.changeBaseDuration(Main.getConfigManager().getTimeBetweenChaosEvents());
			sender.sendMessage(Language.COMMAND_RELOAD.getMessage());
			return true;

		} else if(args[0].equalsIgnoreCase("config")) {
			if(!sender.hasPermission("event-uhc.config")) {
				sender.sendMessage(Language.WARNING_PERMISSION.getMessage());
				return true;
			}
			if(gameData.isGameRunning()) {
				sender.sendMessage(Language.WARNING_GAME_ON.getMessage());
				return true;
			}
			if(sender instanceof Player) {
				new MainMenu().INVENTORY.open((Player) sender);
			} else {
				sender.sendMessage(Language.WARNING_CONSOL.getMessage());
			}
			return true;
		} else if(args[0].equalsIgnoreCase("finish")) {
			if(!sender.hasPermission("event-uhc.finish")) {
				sender.sendMessage(Language.WARNING_PERMISSION.getMessage());
				return true;
			}
			if(gameData.isGameRunning()) {
				sender.sendMessage(Language.WARNING_GAME_ON.getMessage());
				return true;
			}
			if(!(sender instanceof Player)) {
				sender.sendMessage(Language.WARNING_CONSOL.getMessage());
				return true;
			}
			Player player = (Player) sender;
			if(StartItemsMenu.getPlayersModifyingItems().contains(player.getUniqueId())) {
				Main.getConfigManager().setSpawnItems(player.getInventory().getContents());
				StartItemsMenu.getPlayersModifyingItems().remove(player.getUniqueId());
				
				//Set previous gamemode
				player.setGameMode(StartItemsMenu.getPlayersGamemode().get(player.getUniqueId()));
				StartItemsMenu.getPlayersGamemode().remove(player.getUniqueId());
				
				//Set previous inventory
				player.getInventory().setContents(StartItemsMenu.getPlayersInventory().get(player.getUniqueId()));
				StartItemsMenu.getPlayersInventory().remove(player.getUniqueId());
				
				new StartItemsMenu().inventory.open(player);
				return true;
				
			} else if(DeathItemsMenu.getPlayersModifyingItems().contains(player.getUniqueId())) {
				Main.getConfigManager().setDeathItems(player.getInventory().getContents());
				DeathItemsMenu.getPlayersModifyingItems().remove(player.getUniqueId());
				
				//Set previous gamemode
				player.setGameMode(DeathItemsMenu.getPlayersGamemode().get(player.getUniqueId()));
				DeathItemsMenu.getPlayersGamemode().remove(player.getUniqueId());
				
				//Set previous inventory
				player.getInventory().setContents(DeathItemsMenu.getPlayersInventory().get(player.getUniqueId()));
				DeathItemsMenu.getPlayersInventory().remove(player.getUniqueId());
				
				//Reopen config item GUI
				new DeathItemsMenu().inventory.open(player);
				return true;
			}
			player.sendMessage(Language.COMMAND_FINISH_ITEMS.getMessage());
			return true;
		} else if(args[0].equalsIgnoreCase("team")) {
			if(gameData.isGameRunning()) {
				sender.sendMessage(Language.WARNING_GAME_ON.getMessage());
				return true;
			}
			if(!(sender instanceof Player)) {
				sender.sendMessage(Language.WARNING_CONSOL.getMessage());
				return true;
			}
			if(Main.getConfigManager().getTeamSize() == 1) {
				sender.sendMessage(Language.WARNING_FFA.getMessage());
				return true;
			}
			Player player = (Player) sender;
			new TeamsMenu().inventory.open(player);
			return true;
		} else if(args[0].equalsIgnoreCase("respawn")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(Language.WARNING_CONSOL.getMessage());
				return true;
			}
			if(!sender.hasPermission("event-uhc.respawn")) {
				sender.sendMessage(Language.WARNING_PERMISSION.getMessage());
				return true;
			}
			if(!gameData.isGameRunning()) {
				sender.sendMessage(Language.WARNING_GAME_OFF.getMessage());
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(Language.COMMAND_SYNTAX.getMessage() + "/event-uhc respawn <pseudo>");
				return true;
			}

			Player target = Bukkit.getPlayer(args[1]);
			if(target == null) {
				sender.sendMessage(Language.COMMAND_PLAYER_NOT_EXIST.getMessage());
				return true;
			}
			if(gameData.getAlivePlayers().contains(target.getUniqueId())) {
				sender.sendMessage(Language.COMMAND_PLAYER_ALIVE.getMessage());
				return true;
			}
			new RespawnMenu(target).inventory.open((Player) sender);
			
		}
		sendHelp(sender);
		return true;
	}

	
	private static void sendHelp(CommandSender sender) {
		sender.sendMessage(Language.COMMAND_HELP_TITLE.getMessage());
		sender.sendMessage(Language.COMMAND_HELP_ALIASES.getMessage());
		if(sender.hasPermission("event-uhc.start")) {
			sender.sendMessage(Language.COMMAND_HELP_START.getMessage());
		}
		if(sender.hasPermission("event-uhc.stop")) {
			sender.sendMessage(Language.COMMAND_HELP_STOP.getMessage());
		}
		if(sender.hasPermission("event-uhc.config")) {
			sender.sendMessage(Language.COMMAND_HELP_CONFIG.getMessage());
		}
		if(sender.hasPermission("event-uhc.force")) {
			sender.sendMessage(Language.COMMAND_HELP_FORCE.getMessage());
		}
		if(sender.hasPermission("event-uhc.reload")) {
			sender.sendMessage(Language.COMMAND_HELP_RELOAD.getMessage());
		}
		if(sender.hasPermission("event-uhc.respawn")) {
			sender.sendMessage(Language.COMMAND_HELP_RESPAWN.getMessage());
		}
		if(sender.hasPermission("event-uhc.finish")) {
			sender.sendMessage(Language.COMMAND_HELP_FINISH.getMessage());
		}	
		sender.sendMessage(Language.COMMAND_HELP_TEAM.getMessage());
		sender.sendMessage(Language.COMMAND_HELP_HELP.getMessage());
	}



	private static final String[] ARGS0_COMMANDS = { "config", "start", "stop", "force","reload","respawn","finish","team","help"};
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, String[] args) {
		List<String> possibilities = new ArrayList<>();
		if(args.length == 0) {
			possibilities.addAll(Arrays.asList(ARGS0_COMMANDS));
			return possibilities;
		}
		if(args.length == 1 ) {
			Iterable<String> iterable_args0_commands = Arrays.asList(ARGS0_COMMANDS);			
			StringUtil.copyPartialMatches(args[0], iterable_args0_commands, possibilities);
		}
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("force")) {
				Iterable<String> iterable_force_commands = Arrays.asList("border","pvp");
				StringUtil.copyPartialMatches(args[1], iterable_force_commands, possibilities);
			} else if(args[0].equalsIgnoreCase("respawn")) {
				return null;

			} 
		}
		Collections.sort(possibilities);
		return possibilities;
	}
}
