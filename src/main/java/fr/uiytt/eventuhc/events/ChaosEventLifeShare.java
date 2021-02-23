package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventLifeShare extends ChaosEvent {

	private final HashMap<UUID, UUID> linkPlayers = new HashMap<>();
	
	public ChaosEventLifeShare() {
		super("LifeShare", Material.GOLDEN_CHESTPLATE, 22, Type.NORMAL, Language.splitLore(Language.EVENT_LIFE_SHARE_LORE.getMessage()));
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		linkPlayers.clear();

		Bukkit.broadcastMessage(Language.EVENT_LIFE_SHARE_ENABLE.getMessage());
		List<UUID> players = new ArrayList<>(GameManager.getGameInstance().getGameData().getAlivePlayers());
		
		if(players.size() < 2 ) {return;}
		while(players.size() > 1) {
			UUID UUID1 = players.get(ThreadLocalRandom.current().nextInt(players.size()));
			players.remove(UUID1);
			UUID UUID2 = players.get(ThreadLocalRandom.current().nextInt(players.size()));
			players.remove(UUID2);
			linkPlayers.put(UUID1,UUID2);
			linkPlayers.put(UUID2, UUID1);

			Player p1 = Bukkit.getPlayer(UUID1);
			Player p2 = Bukkit.getPlayer(UUID2);
			if(p1 == null || p2 == null) {continue;}

			double life = Math.max(p1.getHealth(), p2.getHealth());
			p1.setHealth(life);
			p2.setHealth(life);
			p1.sendMessage(Language.EVENT_LIFE_SHARE_LINK.getMessage().replace("%s%",p2.getDisplayName()));
			p2.sendMessage(Language.EVENT_LIFE_SHARE_LINK.getMessage().replace("%s%",p1.getDisplayName()));
		}
		
	}

	public HashMap<UUID, UUID> getLinkPlayers() {
		return linkPlayers;
	}


	public void onDisable() {
		super.onDisable();
		linkPlayers.clear();
	}
	
}
