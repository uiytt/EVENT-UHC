package fr.uiytt.eventuhc;

import fr.uiytt.eventuhc.events.ChaosEvent;
import fr.uiytt.eventuhc.events.ChaosEventAlwaysTnt;
import fr.uiytt.eventuhc.events.ChaosEventBFF;
import fr.uiytt.eventuhc.events.ChaosEventBlaze;
import fr.uiytt.eventuhc.events.ChaosEventBlocksAttack;
import fr.uiytt.eventuhc.events.ChaosEventBowSwap;
import fr.uiytt.eventuhc.events.ChaosEventCurseOfBinding;
import fr.uiytt.eventuhc.events.ChaosEventDamageDropItems;
import fr.uiytt.eventuhc.events.ChaosEventDoubleHealth;
import fr.uiytt.eventuhc.events.ChaosEventExplosionSpawnMobs;
import fr.uiytt.eventuhc.events.ChaosEventGoldenApple;
import fr.uiytt.eventuhc.events.ChaosEventHaste;
import fr.uiytt.eventuhc.events.ChaosEventHeal;
import fr.uiytt.eventuhc.events.ChaosEventJump;
import fr.uiytt.eventuhc.events.ChaosEventLevitation;
import fr.uiytt.eventuhc.events.ChaosEventLifeShare;
import fr.uiytt.eventuhc.events.ChaosEventLifeShuffle;
import fr.uiytt.eventuhc.events.ChaosEventNoDamageByMobs;
import fr.uiytt.eventuhc.events.ChaosEventNoGravityForEntities;
import fr.uiytt.eventuhc.events.ChaosEventNoJump;
import fr.uiytt.eventuhc.events.ChaosEventOtherWorld;
import fr.uiytt.eventuhc.events.ChaosEventPopcorn;
import fr.uiytt.eventuhc.events.ChaosEventRainingMobs;
import fr.uiytt.eventuhc.events.ChaosEventRandomInventory;
import fr.uiytt.eventuhc.events.ChaosEventRunIsGlow;
import fr.uiytt.eventuhc.events.ChaosEventSkyHigh;
import fr.uiytt.eventuhc.events.ChaosEventSlowness;
import fr.uiytt.eventuhc.events.ChaosEventSmallMeteors;
import fr.uiytt.eventuhc.events.ChaosEventSnowBallZombie;
import fr.uiytt.eventuhc.events.ChaosEventSpeed;
import fr.uiytt.eventuhc.events.ChaosEventSpeedPig;
import fr.uiytt.eventuhc.events.ChaosEventStoneIsLava;
import fr.uiytt.eventuhc.events.ChaosEventSunlightKills;
import fr.uiytt.eventuhc.events.ChaosEventSwap;
import fr.uiytt.eventuhc.events.ChaosEventTeleportationOnTop;
import fr.uiytt.eventuhc.events.ChaosEventTeleportationToOthers;
import fr.uiytt.eventuhc.events.ChaosEventUnbreakableBlocks;
import fr.uiytt.eventuhc.events.ChaosEventWaterIsPoison;
import fr.uiytt.eventuhc.events.ChaosEventWeirdBiomes;
import fr.uiytt.eventuhc.events.ChaosEventWeirdFight;
import fr.uiytt.eventuhc.events.ChaosEventWitherMiddle;
import fr.uiytt.eventuhc.listeners.ChaosEventsListerner;
import fr.uiytt.eventuhc.listeners.GameListener;
import fr.uiytt.eventuhc.listeners.PlayerLoginListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Register {

	private static final List<ChaosEvent> chaosEvents = new ArrayList<>();
	
	public static void register(JavaPlugin javaplugin) {
		javaplugin.getCommand("event-uhc").setExecutor(new Command());
		javaplugin.getCommand("event-uhc").setTabCompleter(new Command());
		javaplugin.getServer().getPluginManager().registerEvents(new GameListener(), javaplugin);
		javaplugin.getServer().getPluginManager().registerEvents(new PlayerLoginListener(), javaplugin);
		javaplugin.getServer().getPluginManager().registerEvents(new ChaosEventsListerner(), javaplugin);
	}

	public static void registerEvents() {
		chaosEvents.clear();
		chaosEvents.add(new ChaosEventSwap());
		chaosEvents.add(new ChaosEventStoneIsLava());
		chaosEvents.add(new ChaosEventCurseOfBinding());
		chaosEvents.add(new ChaosEventTeleportationOnTop());
		chaosEvents.add(new ChaosEventSlowness());
		chaosEvents.add(new ChaosEventAlwaysTnt());
		chaosEvents.add(new ChaosEventSunlightKills());
		chaosEvents.add(new ChaosEventWaterIsPoison());
		chaosEvents.add(new ChaosEventSkyHigh());
		chaosEvents.add(new ChaosEventTeleportationToOthers());
		chaosEvents.add(new ChaosEventWeirdBiomes());
		chaosEvents.add(new ChaosEventSpeedPig());
		chaosEvents.add(new ChaosEventRandomInventory());
		chaosEvents.add(new ChaosEventOtherWorld());
		chaosEvents.add(new ChaosEventUnbreakableBlocks());
		chaosEvents.add(new ChaosEventSmallMeteors());
		chaosEvents.add(new ChaosEventNoGravityForEntities());
		chaosEvents.add(new ChaosEventBlocksAttack());
		chaosEvents.add(new ChaosEventBowSwap());
		chaosEvents.add(new ChaosEventSpeed());
		chaosEvents.add(new ChaosEventRunIsGlow());
		chaosEvents.add(new ChaosEventNoJump());
		chaosEvents.add(new ChaosEventLifeShare());
		chaosEvents.add(new ChaosEventLifeShuffle());
		chaosEvents.add(new ChaosEventLevitation());
		chaosEvents.add(new ChaosEventNoDamageByMobs());
		chaosEvents.add(new ChaosEventHaste());
		chaosEvents.add(new ChaosEventJump());
		
		/*
		 * I have currently 0 idea of why this doesn't work : 
		 * See : https://www.spigotmc.org/threads/casting-state-doesnt-seems-to-work.440145/
		schaos_events.add(new ChaosEventSpawnChest());
		chaos_events.add(new ChaosEventBlocksGravity()); Removed because of problem with 1.14
		**/
		
		chaosEvents.add(new ChaosEventBlaze());
		chaosEvents.add(new ChaosEventExplosionSpawnMobs());
		chaosEvents.add(new ChaosEventRainingMobs());
		chaosEvents.add(new ChaosEventSnowBallZombie());
		chaosEvents.add(new ChaosEventBFF());
		chaosEvents.add(new ChaosEventWitherMiddle());
		chaosEvents.add(new ChaosEventHeal());
		chaosEvents.add(new ChaosEventGoldenApple());
		chaosEvents.add(new ChaosEventDoubleHealth());
		chaosEvents.add(new ChaosEventDamageDropItems());
		chaosEvents.add(new ChaosEventWeirdFight());
		chaosEvents.add(new ChaosEventPopcorn());
		
		
	}
	
	
	public static List<ChaosEvent> getChaos_Events() {
		return chaosEvents;
		
	}

}
