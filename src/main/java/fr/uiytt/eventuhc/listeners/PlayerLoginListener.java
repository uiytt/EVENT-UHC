package fr.uiytt.eventuhc.listeners;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.config.Language;
import fr.uiytt.eventuhc.game.GameData;
import fr.uiytt.eventuhc.game.GameManager;
import fr.uiytt.eventuhc.game.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class PlayerLoginListener implements Listener {

    public static BukkitTask autoStartRunnable;
    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        //Spigot want a bit of delay before setting scoreboard, adding the player, etc..
        new BukkitRunnable() {
            @Override
            public void run() {
                GameData gamedata = GameManager.getGameInstance().getGameData();
                if (!gamedata.isGameRunning()) {
                    if (Main.getConfigManager().getTeamSize() != 1) {
                        int number_of_team = Math.max((int) Math.ceil((double) Bukkit.getOnlinePlayers().size() / (double) Main.getConfigManager().getTeamSize()), 2);
                        if (number_of_team != gamedata.getTeams().size()) {
                            GameTeam.reorganizeTeam();
                        }
                    }
                    if (Main.getConfigManager().isAutoStart()) {
                        Bukkit.broadcastMessage(Language.GAME_XPLAYERS_CONNECTED.getMessage()
                                .replace("%s%",String.valueOf(Bukkit.getOnlinePlayers().size()))
                                .replace("%s2%",String.valueOf(Main.getConfigManager().getAutoStartNumber()))
                        );
                        if(Bukkit.getOnlinePlayers().size() >= Main.getConfigManager().getAutoStartNumber() &&(autoStartRunnable == null || autoStartRunnable.isCancelled())) {
                            Bukkit.broadcastMessage(Language.GAME_ENOUGH_PLAYERS.getMessage());
                            autoStartRunnable = new AutoStartRunnable(gamedata).runTaskTimer(Main.getInstance(), 20, 20);
                        }
                    }
                    return;
                }

                //Set scoreboard etc...
                GameManager.getGameInstance().getScoreboard().addPlayer(event.getPlayer());
                if (gamedata.getAlivePlayers().contains(event.getPlayer().getUniqueId())) {
                    return;
                }
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
                event.getPlayer().getInventory().clear();
            }
        }.runTaskLater(Main.getInstance(), 5);
    }


    private static class AutoStartRunnable extends BukkitRunnable {

        private final GameData gameData;
        private int timer = 60;

        public AutoStartRunnable(GameData gameData) {
            this.gameData = gameData;
            Bukkit.broadcastMessage(Language.GAME_SART_IN.getMessage().replace("%s%",String.valueOf(timer)));
        }

        @Override
        public void run() {
            if (!Main.getConfigManager().isAutoStart() || gameData.isGameRunning() || Bukkit.getOnlinePlayers().size() < Main.getConfigManager().getAutoStartNumber()) {
                this.cancel();
                return;
            }
            if(timer==0) {
                GameManager.getGameInstance().init(new ArrayList<>(Bukkit.getServer().getOnlinePlayers()));
                this.cancel();
                return;
            }
            if(timer==30 || timer==10 || timer < 6) Bukkit.broadcastMessage(Language.GAME_SART_IN.getMessage().replace("%s%",String.valueOf(timer)));
            timer--;
        }
    }
}
