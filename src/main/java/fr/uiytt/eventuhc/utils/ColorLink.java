package fr.uiytt.eventuhc.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public enum ColorLink {

	PINK(DyeColor.PINK,ChatColor.LIGHT_PURPLE,Material.PINK_BANNER,org.bukkit.ChatColor.LIGHT_PURPLE),
	BLUE(DyeColor.BLUE,ChatColor.DARK_BLUE,Material.BLUE_BANNER,org.bukkit.ChatColor.DARK_BLUE),
	YELLOW(DyeColor.YELLOW,ChatColor.YELLOW,Material.YELLOW_BANNER,org.bukkit.ChatColor.YELLOW),
	ORANGE(DyeColor.ORANGE,ChatColor.GOLD,Material.ORANGE_BANNER,org.bukkit.ChatColor.GOLD),
	RED(DyeColor.RED,ChatColor.DARK_RED,Material.RED_BANNER,org.bukkit.ChatColor.DARK_RED),
	PURPLE(DyeColor.PURPLE,ChatColor.DARK_PURPLE,Material.PURPLE_BANNER,org.bukkit.ChatColor.DARK_PURPLE),
	GRAY(DyeColor.GRAY,ChatColor.GRAY,Material.GRAY_BANNER,org.bukkit.ChatColor.GRAY),
	CYAN(DyeColor.CYAN,ChatColor.BLUE,Material.CYAN_BANNER,org.bukkit.ChatColor.BLUE),
	GREEN(DyeColor.GREEN,ChatColor.GREEN,Material.GREEN_BANNER,org.bukkit.ChatColor.GREEN);

	
	private final DyeColor dye;
	private final ChatColor chat;
	private final Material banner;
	private final org.bukkit.ChatColor bukkitchatcolor;
	ColorLink(DyeColor dye,ChatColor chat,Material banner,org.bukkit.ChatColor bukkitchatcolor) {
		this.dye = dye;
		this.chat = chat;
		this.banner = banner;
		this.bukkitchatcolor = bukkitchatcolor;
	}
	public DyeColor getDye() {
		return dye;
	}
	public ChatColor getChat() {
		return chat;
	}
	public Material getBanner() {
		return banner;
	}
	public org.bukkit.ChatColor getBukkitchatcolor() {
		return bukkitchatcolor;
	}
	
}
