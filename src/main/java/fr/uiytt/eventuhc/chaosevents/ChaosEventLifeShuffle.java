package fr.uiytt.eventuhc.chaosevents;

import fr.uiytt.eventuhc.config.ConfigManager;
import fr.uiytt.eventuhc.game.GameManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ChaosEventLifeShuffle extends ChaosEvent {

	public ChaosEventLifeShuffle() {
		super("LifeShuffle", Material.PINK_BANNER, 23, Type.NORMAL, new String[]{"Les joueurs auront leurs vies mélangées"},10);
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		Bukkit.broadcastMessage(ConfigManager.EVENT_HEADER + "Vous avez échangé votre vie avec un autre joueur");
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
			p1.sendMessage(ConfigManager.EVENT_HEADER + "Tu as reçus la vie de " + ChatColor.YELLOW + p2.getName());
			p2.sendMessage(ConfigManager.EVENT_HEADER + "Tu as reçus la vie de " + ChatColor.YELLOW + p1.getName());
		}
		
	}


	
}
