package fr.uiytt.eventuhc;

import fr.uiytt.eventuhc.chaosevents.*;
import fr.uiytt.eventuhc.listeners.ChaosEventsListerner;
import fr.uiytt.eventuhc.listeners.GameListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Register {

	private static final List<ChaosEvent> chaosEvents = new ArrayList<>();
	
	public static void register(JavaPlugin javaplugin) {
		javaplugin.getCommand("event-uhc").setExecutor(new Command());
		javaplugin.getCommand("event-uhc").setTabCompleter(new Command());
		javaplugin.getServer().getPluginManager().registerEvents(new GameListener(), javaplugin);
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
		chaosEvents.add(new ChaosEventSunlightkill());
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
		chaosEvents.add(new ChaosEventExplodingSpawnMobs());
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
