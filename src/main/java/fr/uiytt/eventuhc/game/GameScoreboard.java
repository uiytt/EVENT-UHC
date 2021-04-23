package fr.uiytt.eventuhc.game;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.events.ChaosEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameScoreboard {

	private final Scoreboard scoreboard;
	private final List<Score> scorelist = new ArrayList<>();
	
	public GameScoreboard() {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		assert scoreboardManager != null;
		scoreboard = scoreboardManager.getNewScoreboard();
	}

	//Based on https://www.spigotmc.org/wiki/making-scoreboard-with-teams-no-flicker/ 
	//If someone know a better way to do it, let me know
	public void createScoreboard() {

		if(Main.getConfigManager().isDisplayLife()) {
			final Objective objLife = scoreboard.registerNewObjective("health","health","health");
			objLife.setRenderType(RenderType.HEARTS);
			objLife.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		}

		//Title
		Objective obj = scoreboard.registerNewObjective("title","dummy",ChatColor.DARK_GRAY + "»»»" + ChatColor.YELLOW + "" + ChatColor.BOLD + "EVENT-UHC" + ChatColor.DARK_GRAY + "«««");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		//Timer
		scorelist.add(obj.getScore(ChatColor.RED + "" + ChatColor.BOLD + "Timer"));
		Team time_counter = scoreboard.registerNewTeam("EUHC_Timer");
		time_counter.addEntry(ChatColor.AQUA + "");
		time_counter.setPrefix(ChatColor.GRAY + "Timer: " + ChatColor.RED + "00:00");
		scorelist.add(obj.getScore(ChatColor.AQUA + ""));
		Team pvp_counter = scoreboard.registerNewTeam("EUHC_pvp");
		pvp_counter.addEntry(ChatColor.BLACK + "");
		pvp_counter.setPrefix(ChatColor.GRAY + "Pvp: " + ChatColor.RED + "00:00");
		scorelist.add(obj.getScore(ChatColor.BLACK + ""));
		Team border_counter = scoreboard.registerNewTeam("EUHC_border");
		border_counter.addEntry(ChatColor.BOLD + "");
		border_counter.setPrefix(ChatColor.GRAY + "Border: " + ChatColor.RED + "00:00");
		scorelist.add(obj.getScore(ChatColor.BOLD + ""));
		
		//Infos
		scorelist.add(obj.getScore(" "));
		scorelist.add(obj.getScore(ChatColor.BLUE + "" + ChatColor.BOLD + "Infos"));
		Team players_alive = scoreboard.registerNewTeam("EUHC_pa");
		players_alive.addEntry(ChatColor.BLUE + "");
		players_alive.setPrefix(ChatColor.GREEN + "Players: " + GameManager.getGameInstance().getGameData().getAlivePlayers().size());
		scorelist.add(obj.getScore(ChatColor.BLUE + ""));
		Team border_size = scoreboard.registerNewTeam("EUHC_bordersize");
		border_size.addEntry(ChatColor.DARK_BLUE + "");
		border_size.setPrefix(ChatColor.GREEN + "Border:" + ChatColor.GRAY + " +0/-0");
		scorelist.add(obj.getScore(ChatColor.DARK_BLUE + ""));
		
		//Events
		scorelist.add(obj.getScore(" "));
		scorelist.add(obj.getScore(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Events"));
		Team next_event = scoreboard.registerNewTeam("EUHC_nextevent");
		next_event.addEntry(ChatColor.DARK_AQUA + "");
		next_event.setPrefix(ChatColor.GRAY + "Next: " + ChatColor.DARK_PURPLE + "00:00");
		scorelist.add(obj.getScore(ChatColor.DARK_AQUA + ""));
		
		int n = 10;
		for (Score score : scorelist) {
			score.setScore(n);
			n--;
		}
		
	}
	
	public void updateBorderSize(int size) {
		Team border_size = scoreboard.getTeam("EUHC_bordersize");
		if (border_size != null) {
			border_size.setPrefix(ChatColor.GREEN + "Border:" + ChatColor.GRAY + " +" + size + "/" + "-" + size);
		}
	}
	public void addPlayer(Player player) {
		player.setScoreboard(scoreboard);
	}
	
	public void removePlayer(Player player) {
		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
		assert scoreboardManager != null;
		player.setScoreboard(scoreboardManager.getNewScoreboard());
	}
	public void addEvent(ChaosEvent event) {
		Team team = scoreboard.getTeam("EUHC_events");
		if(team == null) {
			team = scoreboard.registerNewTeam("EUHC_events");
		}
		String display = ChatColor.GRAY + event.getName() +  ": ";
		team.addEntry(display);
		Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
		assert obj != null;
		scorelist.add(obj.getScore(display));
	}
	public void updateEvents(List<ChaosEvent> chaoseventslist) {
		Team team = scoreboard.getTeam("EUHC_events");
		Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
		List<Score> temp_scorelist = new ArrayList<>(scorelist);
		if(obj == null || team == null) {return;}


		if(chaoseventslist.size() > 0) {
			for (Score score : temp_scorelist) {
				if (score.getEntry().contains(ChatColor.GRAY + "")) {

					List<String> name = Arrays.asList(score.getEntry().split(":"));
					name = Arrays.asList(name.get(0).split(ChatColor.GRAY + ""));
					for (ChaosEvent event : chaoseventslist) {
						//check if the matching event is found
						if (event.getName().equals(name.get(1))) {
							scorelist.remove(score);
							int scoreindex = score.getScore();
							if (team.hasEntry(score.getEntry())) {
								scoreboard.resetScores(score.getEntry());
								team.removeEntry(score.getEntry());
							}
							if (event.getRemaining() > 0) {
								String display = ChatColor.GRAY + event.getName() + ": " + ChatColor.DARK_PURPLE + intToTime(event.getRemaining());
								team.addEntry(display);
								obj.getScore(display).setScore(scoreindex);
								scorelist.add(obj.getScore(display));
							}

						}
					}
				}
			}
		}
		
	}
	private void updateScores() {
		int n = 10;
		for (Score score : scorelist) {
			Objective obj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
			assert obj != null;
			obj.getScore(score.getEntry()).setScore(n);
			n--;
		}
	}
	
	public void updateGlobalTimer(int time) {
		Team timeCounter = scoreboard.getTeam("EUHC_Timer");
		if(timeCounter == null) {
			return;
		}
		timeCounter.setPrefix(ChatColor.GRAY + "Timer: " + ChatColor.RED + intToTime(time) );
	}
	public void updatePvpTimer(int time) {
		Team pvpCounter = scoreboard.getTeam("EUHC_pvp");
		if(pvpCounter == null) {
			return;
		}
		if(time != -1) {
			pvpCounter.setPrefix(ChatColor.GRAY + "Pvp: " + ChatColor.RED + intToTime(time) );
			return;
		}
		scorelist.removeIf(l -> l.getEntry().equals(ChatColor.BLACK + ""));
		scoreboard.resetScores(ChatColor.BLACK + "");
		this.updateScores();
	}
	public void updateBorderTimer(int time) {
		Team borderCounter = scoreboard.getTeam("EUHC_border");
		if(borderCounter == null) {return;}
		if(time != -1) {
			borderCounter.setPrefix(ChatColor.GRAY + "Border: " + ChatColor.RED + intToTime(time) );
			return;
		}
		scorelist.removeIf(l -> l.getEntry().equals(ChatColor.BOLD + ""));
		scoreboard.resetScores(ChatColor.BOLD + "");
		this.updateScores();
	}
	public void updateNextEventTime(int time) {
		Team nextEvent = scoreboard.getTeam("EUHC_nextevent");
		if(nextEvent == null) {return;}
		if(time != -1) {
			nextEvent.setPrefix(ChatColor.GRAY + "Next: " + ChatColor.DARK_PURPLE + intToTime(time));
		}
		
	}
	
	public void updateNbrPlayer(int players) {
		Team playersCounter = scoreboard.getTeam("EUHC_pa");
		if(playersCounter == null) {return;}
		playersCounter.setPrefix(ChatColor.GREEN + "Players: " + players);
	}
	
	protected String intToTime(int time) {
		String hour = "";
		String min = "00";
		String sec = "";
		if(time > 59) {
			int temp;
			if(time > 3599) {
				temp = (int) Math.floor(time / (float)3600);
				hour = String.valueOf(temp);
				hour += ":";
				time -= temp * 3600;
			}
			temp = (int) Math.floor(time / (float)60);
			min = String.valueOf(temp);
			time -= temp * 60;
		}
		if(time < 10) sec = "0";
		sec += String.valueOf(time);
		return hour+min+":"+sec;
	}
	
	public Scoreboard getBukkitScoreboard() {
		return scoreboard;
	}
}
