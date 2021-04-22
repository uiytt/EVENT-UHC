package fr.uiytt.eventuhc.game;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.Register;
import fr.uiytt.eventuhc.events.ChaosEvent;
import fr.uiytt.eventuhc.events.ChaosEvent.Type;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.gui.StartItemsMenu;
import fr.uiytt.eventuhc.utils.Divers;
import fr.uiytt.eventuhc.utils.PlayerFromUUIDNotFoundException;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GameManager {
	private static GameManager gameInstance;

	private final GameData gameData;
	private final World world;
	private final GameScoreboard scoreboard;
	
	
	public GameManager() {
		gameData = new GameData();
		world = Main.getConfigManager().getWorld();
		scoreboard = new GameScoreboard();
	}
	
	public void init(List<Player> players) {
		List<UUID> playersUUID = new ArrayList<>();
		for(Player player : players) {
			playersUUID.add(player.getUniqueId());
		}
		gameData.setAlivePlayers(playersUUID);
		fillTeams();
		initWorld();
		removePlayersFromConfig();
		scoreboard.createScoreboard();
		startPlayerTP(players);
		initChaosEvents();
		gameData.setGameRunning(true);
		//Start action at every second
		new ThreadEverySecond(this).init(Main.getInstance());
	}


	private void fillTeams() {
		if(Main.getConfigManager().getTeamSize() == 1) {return;}
		List<UUID> playersUUID = new ArrayList<>(gameData.getAlivePlayers());
		Collections.shuffle(playersUUID);
		for(UUID playerUUID : playersUUID) {
			if(gameData.getPlayersTeam().get(playerUUID) != null) {continue;}
			GameTeam smallestTeam = gameData.getTeams().get(0);
			for(GameTeam team : gameData.getTeams()) {
				if(team.getPlayersUUIDs().size() < smallestTeam.getPlayersUUIDs().size()) {
					smallestTeam = team;
				}
			}
			try {
				smallestTeam.addPlayer(playerUUID);
			} catch (PlayerFromUUIDNotFoundException e) {
				gameData.getAlivePlayers().remove(playerUUID);
				e.printStackTrace();
			}
		}
	}

	
	private void removePlayersFromConfig() {
		Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
		StartItemsMenu.getPlayersModifyingItems().clear();
		StartItemsMenu.getPlayersGamemode().clear();
		StartItemsMenu.getPlayersInventory().clear();
	}
	public static void celebrateWinner(Player winner) {
		new BukkitRunnable() {
			private int i = 0;
			@Override
			public void run() {
				winner.getWorld().spawn(winner.getLocation(),Firework.class);
				i += 1;
				if(i == 10) {
					this.cancel();
				}
				
			}
		}.runTaskTimer(Main.getInstance(), 1, 5);
			
		Bukkit.getServer().broadcastMessage(Language.GAME_VICTORY.getMessage().replace("%s",winner.getDisplayName()));
	}
	public void enablePVP() {
		gameData.setWasPvpEnabled(true);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(int i=5;i>0;i--) {
					Bukkit.getServer().broadcastMessage(Language.GAME_PVP_SOON.getMessage().replace("%s",String.valueOf(i)));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				new BukkitRunnable() {
					
					@Override
					public void run() {
						gameData.setPvp(true);
						scoreboard.updatePvpTimer(-1);
						if(Main.getConfigManager().isFinalHeal()) {
							for(UUID playerUUID : gameData.getAlivePlayers()) {
								Player player = Bukkit.getPlayer(playerUUID);
								if(player != null) {
									player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
								}
							}
						}
						
						//Remove every events that can only be fired Before the PVP
						gameData.getAvailableChaosEvents().removeIf(event -> event.getType() == Type.BEFORE_PVP);
						
						//Add every events which can be fired only after the pvp
						Iterator<ChaosEvent> iterator = gameData.getRemainingChaosEvents().iterator();
						while (iterator.hasNext()) {
							ChaosEvent event = iterator.next();
							if(event.getType() == Type.AFTER_PVP) {
								gameData.getAvailableChaosEvents().add(event);
								iterator.remove();
							}
						}
						
						Bukkit.getServer().broadcastMessage(Language.GAME_PVP_ACTIVATED.getMessage());
						
					}
				}.runTask(Main.getInstance());
			}
		}.runTaskAsynchronously(Main.getInstance());
		
	}
	
	public void enableBorder() {
		gameData.setBorderAlreadyMoving(true);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(int i=5;i>0;i--) {
					//Async 5s timer
					Bukkit.getServer().broadcastMessage(Language.GAME_BORDER_SOON.getMessage().replace("%s",String.valueOf(i)));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//Sync event
				new BukkitRunnable() {
					
					@Override
					public void run() {
						//Remove every events which can only be fired before the border started moving
						gameData.getAvailableChaosEvents().removeIf(event -> event.getType() == Type.BEFORE_BORDER);
						
						//Add every events which can be fired only after the border started moving
						Iterator<ChaosEvent> iterator = gameData.getRemainingChaosEvents().iterator();
						while (iterator.hasNext()) {
							ChaosEvent event = iterator.next();
							if(event.getType() == Type.AFTER_BORDER) {
								gameData.getAvailableChaosEvents().add(event);
								iterator.remove();
							}
						}
						//Move border
						int border_end = Main.getConfigManager().getBorderEnd();
						Bukkit.getServer().broadcastMessage(Language.GAME_BORDER_ACTIVATED.getMessage());
						world.getWorldBorder().setSize(border_end, (long) (border_end / Main.getConfigManager().getBorderBlockPerSecond() * 20));
						scoreboard.updateBorderTimer(-1);
					}
				}.runTask(Main.getInstance());
			}
		}.runTaskAsynchronously(Main.getInstance());
		
		
		
	}
	//Define world border and time
	private void initWorld() {
		for(World loopWorld : Bukkit.getWorlds()) {
			loopWorld.getWorldBorder().setCenter(0, 0);
			loopWorld.getWorldBorder().setSize(Main.getConfigManager().getBorderStart() * 2);
			loopWorld.getWorldBorder().setWarningDistance(25);
			loopWorld.getWorldBorder().setDamageBuffer(10);
			loopWorld.getWorldBorder().setDamageAmount(2);
			loopWorld.setTime(0);

			loopWorld.setGameRule(GameRule.NATURAL_REGENERATION, false);
		}

	}
	private void startPlayerTP(List<Player> players) {
		players.forEach(player -> {
			for(PotionEffect potion : player.getActivePotionEffects()) {player.removePotionEffect(potion.getType());}
			Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20.0);
			player.setLevel(0);
			player.setExp(0);
			player.setFoodLevel(20);
			player.setHealth(20.0);
			player.setGameMode(GameMode.SURVIVAL);
			player.getInventory().clear();
			player.getInventory().setContents(Main.getConfigManager().getSpawnItems());
			player.setAbsorptionAmount(0);
			scoreboard.addPlayer(player);
		});
		Bukkit.broadcastMessage(Language.GAME_TELEPORTING.getMessage());
		
		if(Main.getConfigManager().getTeamSize() == 1) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					for(int i=0;i<players.size();i++) {
						Player player = players.get(i);
						Location loc = Divers.randomLocation(world);
						Bukkit.broadcastMessage( Language.GAME_TELEPORTING_PLAYER.getMessage().replace("%s%",String.valueOf(i+1)).replace("%s2%",String.valueOf(players.size())) );
						new BukkitRunnable() {
							
							@Override
							public void run() {
								player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30 * 20, 4, true));
								player.teleport(loc);
							}
						}.runTask(Main.getInstance());
						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
			}.runTaskAsynchronously(Main.getInstance());
			
			
		} else {
			List<GameTeam> teams = gameData.getTeams();
			new BukkitRunnable() {
				
				@Override
				public void run() {
					for(int i=0;i<teams.size();i++) {
						Location loc = Divers.randomLocation(world);
						GameTeam team = teams.get(i);
						Bukkit.broadcastMessage(Language.GAME_TELEPORTING_PLAYER.getMessage().replace("%s%",String.valueOf(i+1)).replace("%s2%",String.valueOf(teams.size())));
						new BukkitRunnable() {
							
							@Override
							public void run() {
								for(UUID playerUUID : team.getPlayersUUIDs()) {
									Player player = Bukkit.getPlayer(playerUUID);
									try {
										if(player == null) {
											throw new PlayerFromUUIDNotFoundException(playerUUID);
										}
									} catch (PlayerFromUUIDNotFoundException e) {
										e.printStackTrace();
										team.removePlayer(playerUUID);
										continue;
									}
									player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30 * 20, 4, true));
									player.teleport(loc);

								
							}
						}}.runTask(Main.getInstance());
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
			}.runTaskAsynchronously(Main.getInstance());
			
		}
	}
	
	private void initChaosEvents() {
		List<ChaosEvent> existingEvents = Register.getChaos_Events();
		List<ChaosEvent> availableEvents = new ArrayList<>();
		List<ChaosEvent> remainingEvents = new ArrayList<>();

		//All the default available events are added to "availableEvents", the rest will be added after the pvp or the border
		for(ChaosEvent chaosEvent : existingEvents) {
			Type type = chaosEvent.getType();
			if(!chaosEvent.isEnabled()) {continue;}
			chaosEvent.resetTimer();
			if( type == ChaosEvent.Type.BEFORE_BORDER  || type == ChaosEvent.Type.BEFORE_PVP || type == ChaosEvent.Type.NORMAL ) {
				availableEvents.add(chaosEvent);
			} else {
				remainingEvents.add(chaosEvent);
			}
		}
		gameData.setAvailableChaosEvents(availableEvents);
		gameData.setRemainingChaosEvents(remainingEvents);
	}

	/**
	 * End the game and reset everything
	 */
	public void stopGame() {
		world.getWorldBorder().reset();
		List<Player> players = world.getPlayers();
		Location spawn = world.getSpawnLocation();
		players.forEach(player -> {
			player.setGameMode(GameMode.ADVENTURE);
			player.teleport(spawn);
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,600,4,false,false));
			scoreboard.removePlayer(player);
		});
		gameData.getCurrentChaosEvents().forEach(ChaosEvent::disable);

		gameData.setGameRunning(false);
		setGameInstance(new GameManager());
		GameTeam.reorganizeTeam();
	}
	
	/**
	 * This function check if the game must end or not
	 * It does not end the game itself
	 * return true if the game must be ended
	 */
	public boolean isEnd() {
		if(Main.getConfigManager().getTeamSize() == 1) {
			return gameData.getAlivePlayers().size() <= 1;
		}
		GameTeam lastPlayer = gameData.getPlayersTeam().get(gameData.getAlivePlayers().get(0));
		boolean twoTeamsRemaining = false;
		for(int i = 0; i< gameData.getAlivePlayers().size(); i++) {
			UUID playerUUID = gameData.getAlivePlayers().get(i);
			GameTeam team_player = gameData.getPlayersTeam().get(playerUUID);
			if(!lastPlayer.getName().equals(team_player.getName())) {
				twoTeamsRemaining = true;
				break;
			}
		}
		//Is there is more than one team, return false
		return !twoTeamsRemaining;
	}

	public static void setGameInstance(GameManager gameManager) {
		gameInstance = gameManager;
	}
	public static GameManager getGameInstance() {
		return gameInstance;
	}

	public GameData getGameData() {
		return gameData;
	}

	public GameScoreboard getScoreboard() {
		return scoreboard;
	}

	public World getWorld() {
		return world;
	}
}
