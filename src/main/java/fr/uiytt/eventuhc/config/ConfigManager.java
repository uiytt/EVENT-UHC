package fr.uiytt.eventuhc.config;

import de.leonhard.storage.Config;
import de.leonhard.storage.Json;
import de.leonhard.storage.Yaml;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.Register;
import fr.uiytt.eventuhc.events.ChaosEvent;
import fr.uiytt.eventuhc.events.ChaosEventWeirdBiomes;
import fr.uiytt.eventuhc.gui.DeconnectionRule;
import fr.uiytt.eventuhc.utils.Divers;
import fr.uiytt.eventuhc.utils.InventorySerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigManager {
	
	private final Config configYaml;
	private final Yaml chaosEventsYaml;
	private final Json inventoriesJSON;

	private int pvpTimer;
	private int borderTimer;
	private int borderStart;
	private int borderEnd;
	private double borderBlockPerSecond;
	private World world;
	private World nether;
	private boolean spectatorMessageToPlayers;
	private int timeBetweenChaosEvents; //in seconds
	private final List<String> defaultGeneratedMaterials;
	@Nullable
	private ItemStack[] spawnItems;
	@Nullable
	private ItemStack[] deathItems;
	private float flintsDrop;
	private float applesDrop;
	private boolean potionLv2;
	private boolean diamondLimit;
	private int diamondlimitAmmount;
	private boolean cutClean;
	private int teamSize;
	private DeconnectionRule deconnectionRule;
	private boolean bannerOpenConfig;
	private boolean comparatorOpenTeams;
	private boolean finalHeal;
	private boolean displayLife;
	private String languageName;
	private boolean autoStart;
	private int autoStartNumber;

	public ConfigManager() {

		File configFile = new File(Main.getInstance().getDataFolder().getAbsolutePath() + File.separator + "config.yml");
		if (!configFile.exists()) {
			Main.getLog().fine(configFile.getName() + " not found, extracting...");
			configFile.getParentFile().mkdirs();
			try {
				Files.copy(ConfigManager.class.getResourceAsStream("/config.yml"), configFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.configYaml = new Config("config","plugins/Event-UHC");
		this.chaosEventsYaml = new Yaml("ChaosEvents","plugins/Event-UHC");
		this.inventoriesJSON = new Json("Inventories","plugins/Event-UHC");
		defaultGeneratedMaterials = ChaosEventWeirdBiomes.initDefaultGeneratedMaterials();
	}

	public void load() {
		//Timer
		pvpTimer = configYaml.getOrSetDefault("timer.Pvp", 20) * 60;
		borderTimer = configYaml.getOrSetDefault("timer.Border",60) * 60;
		//Border
		borderStart = configYaml.getOrSetDefault("border.StartSize",1000);
		borderEnd = configYaml.getOrSetDefault("border.EndSize",50);
		borderBlockPerSecond = configYaml.getOrSetDefault("border.BlockPerSecond",1.0);
		//Worlds
		world = Bukkit.getWorld(configYaml.getOrSetDefault("world.overworld","world"));
		nether = Bukkit.getWorld(configYaml.getOrSetDefault("world.nether","world_nether"));

		//Various
		flintsDrop = Math.min(configYaml.getOrSetDefault("various.Drops.FlintsRate", 50f),100);
		applesDrop = Math.min(configYaml.getOrSetDefault("various.Drops.ApplesRate", 4f),100);
		potionLv2 = configYaml.getOrSetDefault("various.LVL2Potions", false);
		diamondLimit = configYaml.getOrSetDefault("various.DiamondLimit.enable", false);
		diamondlimitAmmount = configYaml.getOrSetDefault("various.DiamondLimit.ammount", 17);
		cutClean = configYaml.getOrSetDefault("various.CutClean", false);
		finalHeal = configYaml.getOrSetDefault("various.FinalHeal",false);
		displayLife = configYaml.getOrSetDefault("various.DisplayLife",true);
		teamSize = configYaml.getOrSetDefault("various.TeamSize",1);

		//ServerOwner (can be modified only in the config file)
		languageName = configYaml.getOrSetDefault("serverOwner.language","fr");
		bannerOpenConfig = configYaml.getOrSetDefault("serverOwner.BannerOpenTeams", true);
		comparatorOpenTeams = configYaml.getOrSetDefault("serverOwner.ComparatorOpenConfig", true);
		deconnectionRule = DeconnectionRule.getFromString(configYaml.getOrSetDefault("serverOwner.DeconnectionRule", DeconnectionRule.NORMAL_KICK.name()));
		spectatorMessageToPlayers = configYaml.getOrSetDefault("serverOwner.SpectatorMessageToPlayers",true);
		autoStart = configYaml.getOrSetDefault("serverOwner.AutoStart.enabled",false);
		autoStartNumber = configYaml.getOrSetDefault("serverOwner.AutoStart.number",10);
		//Inventory
		String spawnItemsBase64 = (String) inventoriesJSON.get("spawnItems");
		if(spawnItemsBase64 != null && !spawnItemsBase64.isEmpty()) {
			ItemStack[] inventoryContent = InventorySerializer.base64ToContent(spawnItemsBase64);
			spawnItems = Divers.securizeItemStackInventory(inventoryContent);
		}

		String deathItemsBase64 = (String) inventoriesJSON.get("deathItems");
		if(deathItemsBase64 != null && !deathItemsBase64.isEmpty()) {
			ItemStack[] inventoryContent = InventorySerializer.base64ToContent(deathItemsBase64);
			deathItems = Divers.securizeItemStackInventory(inventoryContent);
		}

		//Events
		timeBetweenChaosEvents = configYaml.getOrSetDefault("timer.TimeBetweenEvents",7) * 60;
		ChaosEventWeirdBiomes.setGeneratedMaterials(parseStringListToMaterial(chaosEventsYaml.getOrSetDefault("events.WeirdBiomes.GeneratedMaterials", defaultGeneratedMaterials)));

		Language.init(languageName);
		loadChaosEvents();
	}


	private void loadChaosEvents() {
		Register.registerEvents();
		for(ChaosEvent chaosEvent : Register.getChaos_Events()) {
			boolean loaded = chaosEventsYaml.getOrSetDefault("events." + chaosEvent.getName() + ".load",true);
			chaosEvent.setLoaded(loaded);
			if(loaded) {
				chaosEvent.setEnabled(chaosEventsYaml.getOrSetDefault("events." + chaosEvent.getName() + ".enable",true));
			} else {
				chaosEvent.setEnabled(false);
			}
		}
	}
	public void setChaosEventsEnable(ChaosEvent chaosEvent) {
		chaosEventsYaml.set("events." + chaosEvent.getName() + ".enable",chaosEvent.isEnabled());
		chaosEventsYaml.set("events." + chaosEvent.getName() + ".load",chaosEvent.isLoaded());
	}

	/**
	 * For a given list of strings, return the a list of the matching materials.
	 * @param materialsName list of strings
	 * @return List of Materials
	 */
	@SuppressWarnings("unchecked")
	private List<Material> parseStringListToMaterial(List<String> materialsName) {
		Set<Material> result = new HashSet<>();
	    for(String materialName : materialsName) {
	    	Material material = Material.getMaterial(materialName);
			if(material != null) {
				result.add(material);
			}
	    }
		return (List<Material>) (List<?>) Arrays.asList(result.toArray());
		
	}

	/**
	 * Recreate the config files if they were deleted.
	 */
	public void forceReload() {
		File configFile = new File(Main.getInstance().getDataFolder().getAbsolutePath() + File.separator + "config.yml");
		if (!configFile.exists()) {
			Main.getLog().fine(configFile.getName() + " not found, extracting...");
			configFile.getParentFile().mkdirs();
			try {
				Files.copy(Language.class.getResourceAsStream("/config.yml"), configFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		configYaml.forceReload();
		chaosEventsYaml.forceReload();
		inventoriesJSON.forceReload();
	}
	public void setSpawnItems(@Nullable ItemStack[] items) {
		spawnItems = items;
		inventoriesJSON.set("spawnItems",InventorySerializer.contentToBase64(items));
	}
	public void setDeathItems(@Nullable ItemStack[] items) {
		deathItems = items;
		inventoriesJSON.set("deathItems",InventorySerializer.contentToBase64(items));
	}
	public ItemStack[] getSpawnItems() {
		return Divers.securizeItemStackInventory(spawnItems);
	}
	public ItemStack[] getDeathItems() { return Divers.securizeItemStackInventory(deathItems); }

	public int getPvpTimer() {
		return pvpTimer;
	}

	/**
	 * Set a new time for the pvp
	 * @param pvpTimer in seconds
	 */
	public void setPvpTimer(int pvpTimer) {
		this.pvpTimer = pvpTimer;
		configYaml.set("timer.Pvp",pvpTimer/60);
	}
	
	public int getBorderStart() {
		return borderStart;
	}

	public void setBorderStart(int borderStart) {
		this.borderStart = borderStart;
		configYaml.set("border.StartSize",borderStart);
	}

	public int getBorderEnd() {
		return borderEnd;
	}

	public void setBorderEnd(int borderEnd) {
		this.borderEnd = borderEnd;
		configYaml.set("border.EndSize",borderEnd);
	}

	public World getWorld() {
		return world;
	}

	public boolean getSpectatorMessageToPlayers() {
		return spectatorMessageToPlayers;
	}


	public int getTimeBetweenChaosEvents() {
		return timeBetweenChaosEvents;
	}

	/**
	 * Set a new time for the length between two ChaosEvent
	 * @param timeBetweenChaosEvents in seconds
	 */
	public void setTimeBetweenChaosEvents(int timeBetweenChaosEvents) {
		this.timeBetweenChaosEvents = timeBetweenChaosEvents;
		for(ChaosEvent chaosEvent : Register.getChaos_Events()) {
			if(chaosEvent.isDefaultDuration()) {
				chaosEvent.setDuration(timeBetweenChaosEvents);
			}
		}
		configYaml.set("events.TimeBetweenEvents",timeBetweenChaosEvents/60);
	}

	public int getBorderTimer() {
		return borderTimer;
	}
	/**
	 * Set a new time for the border
	 * @param borderTimer in seconds
	 */
	public void setBorderTimer(int borderTimer) {
		this.borderTimer = borderTimer;
		configYaml.set("timer.Border",borderTimer/60);
	}

	public double getBorderBlockPerSecond() {
		return borderBlockPerSecond;
	}

	public void setBorderBlockPerSecond(double borderBlockPerSecond) {
		this.borderBlockPerSecond = borderBlockPerSecond;
		configYaml.set("border.BlockPerSecond",borderBlockPerSecond);
	}

	public World getNether() {
		return nether;
	}

	public float getFlintsDrop() {
		return flintsDrop;
	}

	public void setFlintsDrop(float flintsDrop) {
		this.flintsDrop = flintsDrop;
		configYaml.set("various.Drops.FlintsRate", flintsDrop);
	}

	public float getApplesDrop() {
		return applesDrop;
	}

	public void setApplesDrop(float applesDrop) {
		this.applesDrop = applesDrop;
		configYaml.set("various.Drops.ApplesRate",applesDrop);
	}

	public int getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(int teamSize) {
		this.teamSize = teamSize;
		configYaml.set("various.TeamSize",teamSize);
	}

	public boolean isPotionLv2() {
		return potionLv2;
	}

	public void setPotionLv2(boolean potionLv2) {
		this.potionLv2 = potionLv2;
		configYaml.set("various.LVL2Potions",potionLv2);
	}

	public boolean isDiamondLimit() {
		return diamondLimit;
	}

	public void setDiamondLimit(boolean diamondLimit) {
		this.diamondLimit = diamondLimit;
		configYaml.set("various.DiamondLimit.enable",diamondLimit);
	}

	public int getDiamondlimitAmmount() {
		return diamondlimitAmmount;
	}

	public void setDiamondlimitAmmount(int diamondlimitAmmount) {
		this.diamondlimitAmmount = diamondlimitAmmount;
		configYaml.set("various.DiamondLimit.ammount",diamondlimitAmmount);
	}

	public DeconnectionRule getDeconnectionRule() {
		return deconnectionRule;
	}

	public void setDeconnectionRule(DeconnectionRule deconnectionRule) {
		this.deconnectionRule = deconnectionRule;
		configYaml.set("deconnection.rule",deconnectionRule.name());
	}

	public boolean isBannerOpenConfig() {
		return bannerOpenConfig;
	}

	public boolean isComparatorOpenTeams() {
		return comparatorOpenTeams;
	}

	public boolean isCutClean() {
		return cutClean;
	}

	public void setCutClean(boolean cutClean) {
		this.cutClean = cutClean;
		configYaml.set("various.CutClean",cutClean);
	}

	public boolean isFinalHeal() {
		return finalHeal;
	}

	public void setFinalHeal(boolean finalHeal) {
		this.finalHeal = finalHeal;
		configYaml.set("various.finalHeal",finalHeal);
	}

	public boolean isDisplayLife() {
		return displayLife;
	}

	public void setDisplayLife(boolean displayLife) {
		this.displayLife = displayLife;
		configYaml.set("various.DisplayLife",displayLife);
	}

	public boolean isAutoStart() {
		return autoStart;
	}

	public void setAutoStart(boolean autoStart) {
		configYaml.set("serverOwner.AutoStart.enabled",autoStart);
		this.autoStart = autoStart;
	}

	public int getAutoStartNumber() {
		return autoStartNumber;
	}

	public void setAutoStartNumber(int autoStartNumber) {
		configYaml.set("serverOwner.AutoStart.number",autoStartNumber);
		this.autoStartNumber = autoStartNumber;
	}
}
