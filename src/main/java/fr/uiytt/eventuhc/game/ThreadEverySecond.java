package fr.uiytt.eventuhc.game;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.events.ChaosEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadEverySecond {

	private final GameManager game;

	
	public ThreadEverySecond(GameManager game) {
		this.game = game;

	}
	public void init(Plugin plugin) {
		new SecondRunnable(game).runTaskTimer(plugin, 1, 20);
	}
	
	private static class SecondRunnable extends BukkitRunnable {
		private final int pvpTimer;
		private final int borderTimer;
		private int secondFromStart = 0;
		private int eventTimer = 0;
		private final GameManager game;
		
		private SecondRunnable(GameManager game) {
			pvpTimer = Main.getConfigManager().getPvpTimer();
			borderTimer = Main.getConfigManager().getBorderTimer();
			this.game = game;
		} 			
		
		@Override
		public void run() {
			//Get gamedata and stop game if the game is not running 
			GameData gamedata = game.getGameData();
			GameScoreboard scoreboard = game.getScoreboard();
			if(!gamedata.isGameRunning()) {
				this.cancel();
				return;
			}
			//Increment timer
			secondFromStart += 1;
			eventTimer += 1;
			
			//Update pvp timer and events
			if(!gamedata.wasPvpEnabled()) {
				scoreboard.updatePvpTimer(pvpTimer - secondFromStart);
				if(secondFromStart >= pvpTimer - 5) {
					game.enablePVP();
				}
			}
			
			//Activate the border and add events
			if(!gamedata.isBorderAlreadyMoving()) {
				scoreboard.updateBorderTimer(borderTimer - secondFromStart);
				if(secondFromStart >= borderTimer - 5) {
					game.enableBorder();
				}
			}
			
			//Update Timer for events
			if(eventTimer >= Main.getConfigManager().getTimeBetweenChaosEvents()) {
				eventTimer = 0;}
			scoreboard.updateNextEventTime(Main.getConfigManager().getTimeBetweenChaosEvents() - eventTimer);
			
			//Select a new event
			if(secondFromStart % Main.getConfigManager().getTimeBetweenChaosEvents() == 0) {
				List<ChaosEvent> chaosList = gamedata.getAvailableChaosEvents();
				for(int i = 0;i<ThreadLocalRandom.current().nextInt(3) + 1;i++) {
					if(chaosList.size() > 0) {
						ChaosEvent chaosevent = chaosList.get(ThreadLocalRandom.current().nextInt(chaosList.size()));
						chaosList.remove(chaosevent);
						chaosevent.enable();
						scoreboard.addEvent(chaosevent);
						gamedata.getCurrentChaosEvents().add(chaosevent);
					}
				}
				
					
			}
			//Disable Event after duration of the event
			List<ChaosEvent> currentChaosEvents = gamedata.getCurrentChaosEvents();
			List<ChaosEvent> toremove = new ArrayList<>();
			if(currentChaosEvents.size() > 0) {
				currentChaosEvents.forEach(chaosevent -> {
					if(chaosevent.getRemaining() > 0) {
						chaosevent.incrementTimer();
					} else {
						chaosevent.disable();
						toremove.add(chaosevent);
					}
				});
				//I added this because removing an object inside a foreach cause errors
				currentChaosEvents.removeAll(toremove);
			}
			//Update border size
			scoreboard.updateBorderSize((int) (game.getWorld().getWorldBorder().getSize() / 2.0));
			
			scoreboard.updateEvents(currentChaosEvents);
			scoreboard.updateGlobalTimer(secondFromStart);

		}
		
	}
}
