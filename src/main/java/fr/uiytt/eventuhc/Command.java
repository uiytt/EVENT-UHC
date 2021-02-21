package fr.uiytt.eventuhc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.game.GameData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import fr.uiytt.eventuhc.chaosevents.ChaosEvent;
import fr.uiytt.eventuhc.game.GameManager;
import fr.uiytt.eventuhc.gui.DeathItemsMenu;
import fr.uiytt.eventuhc.gui.MainMenu;
import fr.uiytt.eventuhc.gui.RespawnMenu;
import fr.uiytt.eventuhc.gui.StartItemsMenu;
import fr.uiytt.eventuhc.gui.TeamMenu;
import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

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
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous n'avez pas la permission.");
				return true;
			}
			if(gameData.isGameRunning()) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "La partie est déjà en cours.");
				return true;
			}
			if(Main.devMode) {
				game.init(new ArrayList<>(Bukkit.getServer().getOnlinePlayers()));
			} else {
				if(Bukkit.getOnlinePlayers().size() >= 2) {
					game.init(new ArrayList<>(Bukkit.getServer().getOnlinePlayers()));
				} else {
					sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous ne pouvez pas commencer la partie avec moins de 2 joueurs.");
				} 
			}
						
			return true;
			
			
		} else if(args[0].equals("stop")) {
			if(!sender.hasPermission("event-uhc.stop")) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous n'avez pas la permission.");
				return true;
			}
			if(!gameData.isGameRunning()) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "La partie n'est pas lancée.");
				return true;
			}
			game.stopGame();
			Bukkit.broadcastMessage(ConfigManager.HEADER + ChatColor.GRAY + "Un joueur a arrêté la partie.");
			return true;
			
		} else if(args[0].equalsIgnoreCase("force")) {
			if(!sender.hasPermission("event-uhc.force")) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous n'avez pas la permission.");
				return true;
			}
			if(!gameData.isGameRunning()) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous ne pouvez pas faire ça alors que la partie n'est pas lancée.");
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "La syntaxe n'est pas correcte : /event-uhc force (pvp|border)");
				return true;
			}
			if(args[1].equalsIgnoreCase("pvp")) {
				if(!gameData.wasPvpEnabled()) {
					game.enablePVP();
					return true;
				}
				sender.sendMessage(ConfigManager.HEADER + "Vous avez déjà forcé le pvp.");
			} else if (args[1].equalsIgnoreCase("border")) {
				if(!gameData.isBorderAlreadyMoving()) {
					game.enableBorder();
					return true;
				}
				sender.sendMessage(ConfigManager.HEADER + "Vous avez déjà forcé la bordure.");
			} else {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "La syntaxe n'est pas correcte : /event-uhc force (pvp|border)");
			}
			return true;
			
		} else if(args[0].equalsIgnoreCase("reload")) {
			if(!sender.hasPermission("event-uhc.reload")) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous n'avez pas la permission.");
				return true;
			}
			if(gameData.isGameRunning()) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous ne pouvez pas faire ça alors que la partie est lancée.");
				return true;
			}
			Main.CONFIG.forceReload();
			Main.CONFIG.load();
			ChaosEvent.changeBaseDuration(Main.CONFIG.getTimeBetweenChaosEvents());
			sender.sendMessage(ConfigManager.HEADER + "La config a été rechargée.");
			return true;

		} else if(args[0].equalsIgnoreCase("config")) {
			if(!sender.hasPermission("event-uhc.config")) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous n'avez pas la permission.");
				return true;
			}
			if(gameData.isGameRunning()) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous ne pouvez pas faire ça alors que la partie est lancée.");
				return true;
			}
			if(sender instanceof Player) {
				new MainMenu().INVENTORY.open((Player) sender);
			} else {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Cette commande ne peut être exécutée depuis la console.");
			}
			return true;
		} else if(args[0].equalsIgnoreCase("finish")) {
			if(!sender.hasPermission("event-uhc.finish")) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous n'avez pas la permission.");
				return true;
			}
			if(gameData.isGameRunning()) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous ne pouvez pas faire ça alors que la partie est lancée.");
				return true;
			}
			if(!(sender instanceof Player)) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Cette commande ne peut être exécutée depuis la console.");
				return true;
			}
			Player player = (Player) sender;
			if(StartItemsMenu.getPlayersModifyingItems().contains(player.getUniqueId())) {
				Main.CONFIG.setSpawnItems(player.getInventory().getContents());
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
				Main.CONFIG.setDeathItems(player.getInventory().getContents());
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
			player.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous n'êtes pas entrain de modifier les items.");
			return true;
		} else if(args[0].equalsIgnoreCase("team")) {
			if(gameData.isGameRunning()) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous ne pouvez pas faire ça alors que la partie est lancée.");
				return true;
			}
			if(!(sender instanceof Player)) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Cette commande ne peut être exécutée depuis la console.");
				return true;
			}
			if(Main.CONFIG.getTeamSize() == 1) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous ne pouvez pas faire ça en mode FFA.");
				return true;
			}
			Player player = (Player) sender;
			new TeamMenu().inventory.open(player);
			return true;
		} else if(args[0].equalsIgnoreCase("respawn")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Cette commande ne peut être exécutée depuis la console.");
				return true;
			}
			if(!sender.hasPermission("event-uhc.respawn")) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous n'avez pas la permission.");
				return true;
			}
			if(!gameData.isGameRunning()) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Vous ne pouvez pas faire ça alors que la partie n'est pas lancée.");
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "La syntaxe n'est pas correcte : /event-uhc respawn <pseudo>");
				return true;
			}

			Player target = Bukkit.getPlayer(args[1]);
			if(target == null) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Ce joueur n'existe pas.");
				return true;
			}
			if(gameData.getAlivePlayers().contains(target)) {
				sender.sendMessage(ConfigManager.HEADER + ChatColor.RED + "Ce joueur est toujours vivant.");
				return true;
			}
			new RespawnMenu(target).inventory.open((Player) sender);
			
		}
		sendHelp(sender);
		return true;
	}

	
	private static void sendHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GRAY + ">> " +  ChatColor.YELLOW + ChatColor.BOLD + "/event-uhc" + ChatColor.AQUA + " ... ");
		sender.sendMessage(ChatColor.YELLOW + "Aliases: /eu,/euhc");
		if(sender.hasPermission("event-uhc.start")) {
			sender.sendMessage(ChatColor.AQUA + "... "+ ChatColor.BOLD +"start" + ChatColor.GRAY + " : Démarre la partie");
		}
		if(sender.hasPermission("event-uhc.stop")) {
			sender.sendMessage(ChatColor.AQUA + "... "+ ChatColor.BOLD +"stop" + ChatColor.GRAY + " : Arête la partie");
		}
		if(sender.hasPermission("event-uhc.config")) {
			sender.sendMessage(ChatColor.AQUA + "... "+ ChatColor.BOLD +"config" + ChatColor.GRAY + " : Ouvre le panneau de configuration");
		}
		if(sender.hasPermission("event-uhc.force")) {
			sender.sendMessage(ChatColor.AQUA + "... "+ ChatColor.BOLD +"force (border|pvp)" + ChatColor.GRAY + " : Force le pvp ou la bordure");
		}
		if(sender.hasPermission("event-uhc.reload")) {
			sender.sendMessage(ChatColor.AQUA + "... "+ ChatColor.BOLD +"reload" + ChatColor.GRAY + " : Recharge la config");
		}
		if(sender.hasPermission("event-uhc.respawn")) {
			sender.sendMessage(ChatColor.AQUA + "... "+ ChatColor.BOLD +"respawn <pseudo>" + ChatColor.GRAY + " : Permet de faire respawn le joueur");
		}
		if(sender.hasPermission("event-uhc.finish")) {
			sender.sendMessage(ChatColor.AQUA + "... "+ ChatColor.BOLD +"finish" + ChatColor.GRAY + " : Mets fin à l'éditeur d'item de départ et de mort");
		}	
		sender.sendMessage(ChatColor.AQUA + "... "+ ChatColor.BOLD +"team" + ChatColor.GRAY + " : Ouvre le menu de sélection des teams");
		sender.sendMessage(ChatColor.AQUA + "... "+ ChatColor.BOLD +"help" + ChatColor.GRAY + " : Envoie ce message");
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
