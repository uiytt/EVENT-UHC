package fr.uiytt.eventuhc.listeners;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameData;
import fr.uiytt.eventuhc.game.GameManager;
import fr.uiytt.eventuhc.game.GameScoreboard;
import fr.uiytt.eventuhc.game.GameTeam;
import fr.uiytt.eventuhc.game.PlayerDeathInfos;
import fr.uiytt.eventuhc.gui.DeconnectionRule;
import fr.uiytt.eventuhc.gui.MainMenu;
import fr.uiytt.eventuhc.gui.SpectatorInventoryMenu;
import fr.uiytt.eventuhc.gui.TeamsMenu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class GameListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if(!(GameManager.getGameInstance().getGameData().isGameRunning()) || GameManager.getGameInstance().getGameData().isPvp()) {
			return;
		}
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		if(event.getDamager().getType() == EntityType.ARROW) {
			Arrow arrow = (Arrow) event.getDamager();
			ProjectileSource damager = arrow.getShooter();
			if(!(damager instanceof Player)) return;
		} else if(!(event.getDamager() instanceof Player)) return;

		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		GameData gamedata = GameManager.getGameInstance().getGameData();
		if(!gamedata.isGameRunning()) {
			return;
		}
		Player deadPlayer = event.getEntity();
		if(!gamedata.getAlivePlayers().contains(deadPlayer.getUniqueId())) {
			deadPlayer.setGameMode(GameMode.SPECTATOR);
			return;
		}
		
		GameTeam playerTeam = gamedata.getPlayersTeam().get(deadPlayer.getUniqueId());
		
		
		//save infos in case of respawn
		gamedata.getPlayersDeathInfos().put(deadPlayer.getUniqueId(),
				new PlayerDeathInfos(
						deadPlayer,
						deadPlayer.getLocation(),
						deadPlayer.getInventory().getContents(),
						playerTeam
				)
		);
		
		if(playerTeam != null) {
			playerTeam.removePlayer(deadPlayer.getUniqueId());
		}
		
		List<UUID> playersUUID = gamedata.getAlivePlayers();
		playersUUID.remove(deadPlayer.getUniqueId());
		
		GameScoreboard scoreboard = GameManager.getGameInstance().getScoreboard();
		scoreboard.updateNbrPlayer(playersUUID.size());
		
		List<ItemStack> death_items = Arrays.asList(Main.getConfigManager().getDeathItems());
		if(!death_items.isEmpty()) {
			event.getDrops().addAll(death_items);
		}

		deadPlayer.setGameMode(GameMode.SPECTATOR);
		
		if(!GameManager.getGameInstance().isEnd()) {
			return;
		}


		Player winner = Bukkit.getPlayer(playersUUID.get(0));
		GameManager.getGameInstance().stopGame();
		if(winner == null) {
			Bukkit.broadcastMessage(Language.ERROR_WINNER_NOT_ONLINE.getMessage());
			return;
		}

		GameManager.celebrateWinner(winner);
		
	}
	/*
	 * This manage when a player get disconnected during the game, 
	 * what happen will change in function of the game's DeconnectionRule
	 */
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		GameData gameData = GameManager.getGameInstance().getGameData();
		Player player = event.getPlayer();
		if(!gameData.isGameRunning() || !gameData.getAlivePlayers().contains(player.getUniqueId())) {
			GameTeam.removePlayerFromAllTeams(player.getUniqueId());
			if(!gameData.isGameRunning() && Main.getConfigManager().isAutoStart()){
				Bukkit.broadcastMessage(Language.GAME_XPLAYERS_CONNECTED.getMessage()
						.replace("%s%",String.valueOf(Bukkit.getOnlinePlayers().size() - 1))
						.replace("%s2%",String.valueOf(Main.getConfigManager().getAutoStartNumber()))
				);
			}
			return;
		}
		if(Main.getConfigManager().getDeconnectionRule() == DeconnectionRule.NO_KICK) {
			return;
		}
		int timeBeforeRemoving = Main.getConfigManager().getDeconnectionRule().getWaitingTime();
		if(timeBeforeRemoving != 0) {
			Bukkit.broadcastMessage(Language.WARNING_RECONNECTION.getMessage().replace("%s%",player.getDisplayName()).replace("%s2%",String.valueOf(timeBeforeRemoving)));
		}
		Location location = player.getLocation().clone();
		ItemStack[] inventory = player.getInventory().getContents().clone();
		World world = player.getWorld();

		new BukkitRunnable() {	
			@Override
			public void run() {
				if(Bukkit.getPlayer(player.getUniqueId()) != null || !gameData.isGameRunning()) {return;}
				List<UUID> playersUUID = gameData.getAlivePlayers();
				if(!playersUUID.contains(player.getUniqueId())) {return;}
				playersUUID.remove(player.getUniqueId());
				GameScoreboard scoreboard = GameManager.getGameInstance().getScoreboard();

				Bukkit.broadcastMessage(Language.WARNING_PLAYER_REMOVED.getMessage().replace("%s%",player.getDisplayName()));
				//save stuff and location in case of respawn
				GameTeam playerTeam = gameData.getPlayersTeam().get(player.getUniqueId());

				gameData.getPlayersDeathInfos().put(player.getUniqueId(),
						new PlayerDeathInfos(
								player, 
								location,
								inventory,
								playerTeam
						)
				);
				if(playerTeam != null) {
					playerTeam.removePlayer(player.getUniqueId());
				}
				scoreboard.updateNbrPlayer(playersUUID.size());
				for (ItemStack item : inventory) {
					if(item == null) {continue;}
					world.dropItemNaturally(player.getLocation(), item);

				}
				for (ItemStack item : Main.getConfigManager().getDeathItems()) {
					if(item == null) {continue;}
					world.dropItemNaturally(player.getLocation(), item);
				}
				
				if(!GameManager.getGameInstance().isEnd()) {
					return;
				}

				GameManager.getGameInstance().stopGame();
				Player winner = Bukkit.getPlayer(playersUUID.get(0));
				if(winner == null) {
					Bukkit.broadcastMessage(Language.ERROR_WINNER_NOT_ONLINE.getMessage());
					return;
				}

				GameManager.celebrateWinner(winner);


			}
		}.runTaskLater(Main.getInstance(), timeBeforeRemoving * 20L);
		
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		GameData gameData = GameManager.getGameInstance().getGameData();
		if(!gameData.isGameRunning()) return;

		event.setCancelled(true);
		Player player = event.getPlayer();
		if(gameData.getAlivePlayers().contains(event.getPlayer().getUniqueId())) {
			if(Main.getConfigManager().getTeamSize() == 1) {
				Bukkit.getOnlinePlayers().forEach(loop_player -> loop_player.sendMessage(ChatColor.YELLOW + player.getDisplayName() + ": " + ChatColor.GRAY + event.getMessage()));
			} else {
				GameTeam playerTeam = gameData.getPlayersTeam().get(player.getUniqueId());
				if(event.getMessage().charAt(0) == '*') {
					Bukkit.broadcastMessage(playerTeam.getColor().getChat() + playerTeam.getName() + " " + player.getDisplayName() + ": " + ChatColor.GRAY + event.getMessage().substring(1));
				} else {
					for(UUID mateUUID : playerTeam.getPlayersUUIDs()) {
						Player matePlayer = Bukkit.getPlayer(mateUUID);
						matePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8&l[" + playerTeam.getColor().getChat() + "EQUIPE&8&l] &e" + player.getDisplayName() + "&7: " + event.getMessage()));
					}
				}

			}
		} else {
			for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if(Main.getConfigManager().getSpectatorMessageToPlayers() || !gameData.getAlivePlayers().contains(onlinePlayer.getUniqueId())) {
					onlinePlayer.sendMessage(ChatColor.GRAY + "[SPEC] " + player.getDisplayName() + ": " +  event.getMessage());
				}
			}

		}
	}
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event) {
		if(!GameManager.getGameInstance().getGameData().isGameRunning()) {
			return;
		}
		event.setCancelled(true);
		event.getBlock().setType(Material.AIR);
		if(ThreadLocalRandom.current().nextInt(1,100) <= Main.getConfigManager().getApplesDrop()) {
			event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		GameData gameData = GameManager.getGameInstance().getGameData();
		if(!gameData.isGameRunning()) {return;}

		Player player = event.getPlayer();
		Block block = event.getBlock();
		Material material = block.getType();

		if(material == Material.GRAVEL) {
			event.setCancelled(true);
			block.setType(Material.AIR);
			if(ThreadLocalRandom.current().nextInt(1,100) <= Main.getConfigManager().getFlintsDrop()) {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.FLINT));
			} else {
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GRAVEL));

			}
			return;
		} else if(material == Material.DIAMOND_ORE) {
			if(!Main.getConfigManager().isDiamondLimit()) {return;}
			if(event.isDropItems()) {
				if(gameData.getDiamondLimit().containsKey(player.getUniqueId()) && gameData.getDiamondLimit().get(player.getUniqueId()) >= Main.getConfigManager().getDiamondlimitAmmount()) {
					event.setCancelled(true);
					player.sendMessage(Language.WARNING_DIAMOND_LIMIT.getMessage().replace("%s%",String.valueOf(Main.getConfigManager().getDiamondlimitAmmount())));
				} else {
					if(!gameData.getDiamondLimit().containsKey(player.getUniqueId())) {
						gameData.getDiamondLimit().put(player.getUniqueId(), 1);
					} else {
						gameData.getDiamondLimit().replace(player.getUniqueId(), gameData.getDiamondLimit().get(player.getUniqueId()) + 1);
					}
					
				}
			}
			return;
		} else if(Tag.LEAVES.isTagged(material)) {
			event.setCancelled(true);
			event.getBlock().setType(Material.AIR);
			if(ThreadLocalRandom.current().nextFloat() * 100 + 1 <= Main.getConfigManager().getApplesDrop()) {
				event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
			}
		}
		if(!Main.getConfigManager().isCutClean() || !event.isDropItems()) {
			return;
		}

		if(material == Material.GOLD_ORE) {
			event.setCancelled(true);
			block.setType(Material.AIR);
			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
			assert EntityType.EXPERIENCE_ORB.getEntityClass() != null;
			ExperienceOrb orb = (ExperienceOrb) block.getWorld().spawn(block.getLocation(),EntityType.EXPERIENCE_ORB.getEntityClass());
			orb.setExperience(1);
		} else if(material == Material.IRON_ORE){
			event.setCancelled(true);
			block.setType(Material.AIR);
			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_INGOT));
			if(ThreadLocalRandom.current().nextInt(10) >= 2) {
				assert EntityType.EXPERIENCE_ORB.getEntityClass() != null;
				ExperienceOrb orb = (ExperienceOrb) block.getWorld().spawn(block.getLocation(),EntityType.EXPERIENCE_ORB.getEntityClass());
				orb.setExperience(1);
			}

		}
		
	}

	@EventHandler
	public void onBrew(BrewEvent event ) {
		if(!GameManager.getGameInstance().getGameData().isGameRunning() || Main.getConfigManager().isPotionLv2()) {
			return;
		}
		ItemStack[] items = event.getContents().getContents();
		for (ItemStack item : items ) {
			if(item == null) {continue;}
			if(item.getType() == Material.GLOWSTONE_DUST) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent event ) {
		GameData gamedata = GameManager.getGameInstance().getGameData();
		if(gamedata.isGameRunning()) {
			return;
		}
		if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPARATOR && Main.getConfigManager().isComparatorOpenTeams()) {
			event.setCancelled(true);
			if(event.getPlayer().hasPermission("event-uhc.config")) {
				if(event.getPlayer().getOpenInventory().getType() == InventoryType.CREATIVE || event.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING ) {
					new MainMenu().INVENTORY.open(event.getPlayer());
				}
			} else {
				event.getPlayer().sendMessage(Language.WARNING_PERMISSION.getMessage());
			}
		} else if (event.getPlayer().getInventory().getItemInMainHand().getType().toString() .contains("BANNER") && Main.getConfigManager().isBannerOpenConfig()) {
			event.setCancelled(true);
			if(Main.getConfigManager().getTeamSize() != 1) {
				new TeamsMenu().inventory.open(event.getPlayer());
			} else {
				event.getPlayer().sendMessage(Language.WARNING_FFA.getMessage());
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		GameData gamedata = GameManager.getGameInstance().getGameData();
		if(!gamedata.isGameRunning() || !Main.getConfigManager().isCutClean()) {
			return;
		}
		List<ItemStack> drops = new ArrayList<>();
		if(event.getEntityType() == EntityType.COW) {
			for(int i=0;i<ThreadLocalRandom.current().nextInt(3);i++) {
				drops.add(new ItemStack(Material.LEATHER));
			}
			for(int i=0;i<ThreadLocalRandom.current().nextInt(3) + 1;i++) {
				drops.add(new ItemStack(Material.COOKED_BEEF));
			}
		} else if(event.getEntityType() == EntityType.SHEEP) {
			for(int i=0;i<ThreadLocalRandom.current().nextInt(2) + 1;i++) {
				drops.add(new ItemStack(Material.COOKED_MUTTON));
			}
			drops.add(new ItemStack(Material.WHITE_WOOL));

		} else if(event.getEntityType() == EntityType.CHICKEN) {
			drops.add(new ItemStack(Material.COOKED_CHICKEN));
			for(int i=0;i<ThreadLocalRandom.current().nextInt(3) ;i++) {
				drops.add(new ItemStack(Material.FEATHER));
			}
		} else if(event.getEntityType() == EntityType.RABBIT) {
			drops.add(new ItemStack(Material.COOKED_RABBIT));
		} else if(event.getEntityType() == EntityType.PIG) {
			drops.add(new ItemStack(Material.COOKED_PORKCHOP));
		}else {
			return;
		}
		event.getDrops().clear();
		event.getDrops().addAll(drops);
	}


	@EventHandler
	public void onRightClickOnPlayer(PlayerInteractEntityEvent event) {
		GameData gamedata = GameManager.getGameInstance().getGameData();
		if(!gamedata.isGameRunning()) {
			return;
		}
		if(event.getPlayer().getGameMode() != GameMode.SPECTATOR || !(event.getRightClicked() instanceof Player)) {
			return;
		}
		new SpectatorInventoryMenu((Player) event.getRightClicked()).inventory.open(event.getPlayer());
	}


}
