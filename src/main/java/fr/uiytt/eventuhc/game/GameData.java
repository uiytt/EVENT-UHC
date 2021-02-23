package fr.uiytt.eventuhc.game;

import fr.uiytt.eventuhc.events.ChaosEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class GameData {

	private boolean gameRunning = false;
	private List<UUID> alivePlayers = new ArrayList<>();
	private List<ChaosEvent> currentChaosEvents = new ArrayList<>();
	private List<ChaosEvent> availableChaosEvents = new ArrayList<>();
	private List<ChaosEvent> remainingChaosEvents = new ArrayList<>();
	private final HashMap<UUID, Integer> diamondLimit = new HashMap<>();
	private boolean pvp = false;
	//this one is activated 5s before the pvp;
	private boolean wasPvpEnabled;
	private boolean borderMoving = false;
	private final List<GameTeam> teams = new ArrayList<>();
	private final HashMap<UUID, GameTeam> playersTeam = new HashMap<>();
	private final HashMap<UUID, PlayerDeathInfos> playersDeathInfos = new HashMap<>();

	
	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(boolean gameRunning) {
		this.gameRunning = gameRunning;
	}

	public List<UUID> getAlivePlayers() {
		return alivePlayers;
	}

	public void setAlivePlayers(List<UUID> alivePlayers) {
		this.alivePlayers = alivePlayers;
	}

	public boolean isPvp() {
		return pvp;
	}

	public void setPvp(boolean pvp) {
		this.pvp = pvp;
	}


	public boolean isBorderAlreadyMoving() {
		return borderMoving;
	}

	public void setBorderAlreadyMoving(boolean borderstartedmoving) {
		this.borderMoving = borderstartedmoving;
	}

	public List<ChaosEvent> getCurrentChaosEvents() {
		return currentChaosEvents;
	}

	public void setCurrentChaosEvents(List<ChaosEvent> currentChaosEvents) {
		this.currentChaosEvents = currentChaosEvents;
	}

	public List<ChaosEvent> getAvailableChaosEvents() {
		return availableChaosEvents;
	}

	public void setAvailableChaosEvents(List<ChaosEvent> availableChaosEvents) {
		this.availableChaosEvents = availableChaosEvents;
	}

	public List<ChaosEvent> getRemainingChaosEvents() {
		return remainingChaosEvents;
	}

	public void setRemainingChaosEvents(List<ChaosEvent> remainingChaosEvents) {
		this.remainingChaosEvents = remainingChaosEvents;
	}

	public HashMap<UUID, Integer> getDiamondLimit() {
		return diamondLimit;
	}

	public List<GameTeam> getTeams() {
		return teams;
	}

	/**
	 * @return HashMap of team of each player
	 */
	public HashMap<UUID, GameTeam> getPlayersTeam() {
		return playersTeam;
	}

	public HashMap<UUID, PlayerDeathInfos> getPlayersDeathInfos() {
		return playersDeathInfos;
	}


	public boolean wasPvpEnabled() {
		return wasPvpEnabled;
	}

	public void setWasPvpEnabled(boolean wasPvpEnabled) {
		this.wasPvpEnabled = wasPvpEnabled;
	}
}
