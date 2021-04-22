package fr.uiytt.eventuhc.game;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.utils.ColorLink;
import fr.uiytt.eventuhc.utils.PlayerFromUUIDNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GameTeam {

	private final List<UUID> playersUUIDs = new ArrayList<>();
	private final org.bukkit.scoreboard.Team scoreboardTeam;
	private final ColorLink color;
	private final String name;
	public GameTeam(ColorLink color,String name,int number) {
		this.color = color;
		scoreboardTeam = GameManager.getGameInstance().getScoreboard().getBukkitScoreboard().registerNewTeam(name);
		if(number != 1) {
			this.name = name + " [" + number + "]";
			scoreboardTeam.setSuffix(" [" + number + "]");
		} else {
			this.name = name;
		}

		scoreboardTeam.setAllowFriendlyFire(false);
		scoreboardTeam.setColor(color.getBukkitchatcolor());

	}
	/**
	 * This return only a COPY of the list of players, you cannot modify the players here,
	 * see addPlayer(), removePlayer()
	 */
	public List<UUID> getPlayersUUIDs() {
		return new ArrayList<>(playersUUIDs);
	}
	
	/**
	 * This add a player to the team
	 * @param playerUUID if of the player, produce an error if the player is not online
	 */
	public void addPlayer(UUID playerUUID) throws PlayerFromUUIDNotFoundException {
		Player player = Bukkit.getPlayer(playerUUID);
		if(player == null) {
			throw new PlayerFromUUIDNotFoundException(playerUUID);
		}
		player.setScoreboard(GameManager.getGameInstance().getScoreboard().getBukkitScoreboard());
		scoreboardTeam.addEntry(player.getName());
		playersUUIDs.add(playerUUID);
		GameManager.getGameInstance().getGameData().getPlayersTeam().put(playerUUID, this);

	}

	public void removePlayer(UUID playerUUID) {
		Player player = Bukkit.getPlayer(playerUUID);
		if(player != null) {
			scoreboardTeam.removeEntry(player.getName());
		}
		playersUUIDs.remove(playerUUID);
		GameManager.getGameInstance().getGameData().getPlayersTeam().remove(playerUUID);
	}

	public static void removePlayerFromAllTeams(UUID playerUUID) {
		GameTeam team = GameManager.getGameInstance().getGameData().getPlayersTeam().get(playerUUID);
		if(team != null) {
			team.removePlayer(playerUUID);
		}
	}

	public void removeAllPlayers() {
		GameManager.getGameInstance().getGameData().getPlayersTeam().clear();
		playersUUIDs.clear();
		scoreboardTeam.getEntries().forEach(scoreboardTeam::removeEntry);
		scoreboardTeam.unregister();
	}
	
	/**
	 * This register new teams in function of the number of player, the size of the team etc..
	 */
	public static void reorganizeTeam() {
		GameData gameData = GameManager.getGameInstance().getGameData();
		gameData.getTeams().forEach(GameTeam::removeAllPlayers);
		gameData.getTeams().clear();
		if(Main.getConfigManager().getTeamSize() == 1) {
			return;
		}
		List<ColorLink> colors = Arrays.asList(ColorLink.values());

		//Create teams with color, if more than one team for a color, add number 
		// ex : RED,BlUE,YELLOW, etc.. RED[1],BLUE[1]
		int numberTeam = Math.max((int) Math.ceil((double) Bukkit.getOnlinePlayers().size() / (double) Main.getConfigManager().getTeamSize()),2);
		int colorindex = 0;
		int numberOfSameColor = 1;
		for(int i=0;i<numberTeam;i++ ) {
			ColorLink color = colors.get(colorindex);
			gameData.getTeams().add(new GameTeam(color,color.name(),numberOfSameColor));
			
			colorindex++;
			if(colorindex == ColorLink.values().length) {
				colorindex = 0;
				numberOfSameColor++;
			}
		}
		
	}

	public ColorLink getColor() {
		return color;
	}

	public String getName() {
		return name;
	}
}
