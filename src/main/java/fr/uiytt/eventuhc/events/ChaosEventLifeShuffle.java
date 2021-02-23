package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventLifeShuffle extends ChaosEvent {

	public ChaosEventLifeShuffle() {
		super("LifeShuffle", Material.PINK_BANNER, 23, Type.NORMAL, Language.splitLore(Language.EVENT_LIFE_SHUFFLE_LORE.getMessage()),10);
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(Language.EVENT_LIFE_SHUFFLE_ENABLE.getMessage());
		List<UUID> players = new ArrayList<>(GameManager.getGameInstance().getGameData().getAlivePlayers());
		
		if(players.size() < 2 ) {return;}
		while(players.size() > 1) {
			UUID UUID1 = players.get(ThreadLocalRandom.current().nextInt(players.size()));
			players.remove(UUID1);
			UUID UUID2 = players.get(ThreadLocalRandom.current().nextInt(players.size()));
			players.remove(UUID2);

			Player p1 = Bukkit.getPlayer(UUID1);
			Player p2 = Bukkit.getPlayer(UUID2);
			if(p1 == null || p2 == null) {continue;}

			double p1Health = p1.getHealth();
			System.out.println(p1Health);
			p1.setHealth(p2.getHealth());
			System.out.println(p1Health);
			p2.setHealth(p1Health);
			p1.sendMessage(Language.EVENT_LIFE_SHUFFLE_RECEIVE.getMessage().replace("%s%",p2.getDisplayName()));
			p2.sendMessage(Language.EVENT_LIFE_SHUFFLE_RECEIVE.getMessage().replace("%s%",p1.getDisplayName()));
		}
		
	}


	
}
