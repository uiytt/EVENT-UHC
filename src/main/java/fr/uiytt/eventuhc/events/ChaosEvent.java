package fr.uiytt.eventuhc.events;

import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.Register;
import org.bukkit.Material;

public abstract class ChaosEvent {

	protected final Material itemMaterial;
	protected final int id;
	protected final Type type;
	protected final String[] eventDescription;
	protected boolean loaded = true;

	protected boolean enabled = true;
	protected boolean activated;
	protected int duration;
	protected String name;
	protected boolean defaultDuration;

	private int timer = -1;

	protected ChaosEvent(String name, Material itemMaterial, int id, Type type, String[] eventDescription) {
		this(name,itemMaterial, id, type,eventDescription, Main.getConfigManager().getTimeBetweenChaosEvents());
	}
	protected ChaosEvent(String name, Material itemMaterial, int id, Type type, String[] eventDescription, int duration) {
		this.type = type;
		this.id = id;
		this.itemMaterial = itemMaterial;
		this.name = name;
		this.eventDescription = eventDescription;
		this.duration = duration;
		defaultDuration = (duration == Main.getConfigManager().getTimeBetweenChaosEvents());
	}
	public static void changeBaseDuration(int configDuration) {
		for(ChaosEvent chaosEvent : Register.getChaos_Events()) {
			if(chaosEvent.defaultDuration) {
				chaosEvent.duration = configDuration;
			}
		}
	}
	protected void onEnable() {
		activated = true;
	}

	protected void onDisable() {
		activated = false;
	}

	public Material getItemMaterial() {
		return itemMaterial;
	}
	
	public void incrementTimer() {
		this.timer += 1;
	}
	public int getRemaining() {
		return duration - timer;
	}
	public void resetTimer() {
		this.timer = -1;
	}
	public void enable() {
		this.onEnable();
	}
	public void disable() {
		this.onDisable();
	}
	public boolean getActivated() {
		return activated;
	}
	public int getId() {return id;}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public enum Type {
		NORMAL,
		BEFORE_PVP,
		AFTER_PVP,
		BEFORE_BORDER,
		AFTER_BORDER,		
	}

	public String getName() {
		return name;
	}
	public Type getType() {
		return type;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String[] getDescriptionLore() {
		return eventDescription;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public boolean isDefaultDuration() {
		return defaultDuration;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
}
