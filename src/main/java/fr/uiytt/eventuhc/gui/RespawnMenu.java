package fr.uiytt.eventuhc.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.uiytt.eventuhc.Main;
import fr.uiytt.eventuhc.game.GameManager;
import fr.uiytt.eventuhc.game.PlayerDeathInfos;
import fr.uiytt.eventuhc.utils.Divers;
import fr.uiytt.eventuhc.utils.PlayerFromUUIDNotFoundException;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class RespawnMenu implements InventoryProvider {

	
	public final SmartInventory inventory = SmartInventory.builder()
			.id("EUHC_RespawnMenu")
			.title(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Paramètres avancés")
			.size(3, 9)
			.provider(this)
			.manager(Main.getInvManager())
			.build();
	private boolean randomTP = false;
	private boolean regivestuff = false;
	private final Player target;
	public RespawnMenu(Player target) {
		this.target = target;
	}
	
	@Override
	public void init(Player player, InventoryContents contents) {
		ClickableItem empty_panel = ClickableItem.empty( Divers.ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE, ChatColor.GRAY + ""));
		contents.fillBorders(empty_panel);
		contents.set(1, 1, empty_panel);
		contents.set(1,7,empty_panel);
		contents.set(1,3,empty_panel);
		contents.set(1,5,empty_panel);
		ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerheadmeta = (SkullMeta) playerhead.getItemMeta();
        playerheadmeta.setOwningPlayer(Bukkit.getOfflinePlayer(target.getUniqueId()));
        playerheadmeta.setDisplayName(target.getName());
        playerheadmeta.setLore(Arrays.asList(ChatColor.GRAY +"Le joueur réapparaîtra avec 30s ",
				ChatColor.GRAY +"de resistance au feu,",
				ChatColor.GRAY +"5s de Résistance IV et",
				ChatColor.GRAY +"1m de water breathing."));
        playerhead.setItemMeta(playerheadmeta);
        contents.set(1, 4, ClickableItem.empty(playerhead));
        contents.set(2, 4,ClickableItem.of(Divers.ItemStackBuilder(Material.EMERALD_BLOCK, ChatColor.GREEN + "Respawn " + target.getName()), event -> {
        	target.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*30, 0, false), true);
        	target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*5, 3, false), true);
        	target.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20*60, 1, false), true);
        	PlayerDeathInfos infos = GameManager.getGameInstance().getGameData().getPlayersDeathInfos().get(target.getUniqueId());
        	if(regivestuff) {
        		target.getInventory().setContents(infos.getInventory());
        	} else {
        		target.getInventory().clear();
        	}
        	target.setGameMode(GameMode.SURVIVAL);
        	Location tpLocation;
        	if(randomTP) {
        		tpLocation = Divers.randomLocation(target.getWorld());
        		tpLocation = Divers.highestBlock(tpLocation);
        	} else {
        		tpLocation = infos.getLocation();
        	}
        	target.teleport(tpLocation);

			GameManager.getGameInstance().getGameData().getAlivePlayers().add(target.getUniqueId());
        	if(infos.getTeam() != null) {
				try {
					infos.getTeam().addPlayer(target.getUniqueId());
				} catch (PlayerFromUUIDNotFoundException e) {
					e.printStackTrace();
				}
			}
        	
        	target.closeInventory();
        	player.closeInventory();
        	
        }));
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		if(randomTP) {
			contents.set(1, 2, ClickableItem.of(Divers.ItemStackBuilder(Material.ENDER_PEARL, ChatColor.YELLOW + "RandomTP", new String[] {
				ChatColor.GRAY + "Le randomTP est " + ChatColor.GREEN + " activé",
				ChatColor.GRAY +"le joueur réapparaîtra à des cordonées ",
				ChatColor.GRAY +"aléatoires."
			}), event -> randomTP = false));
		} else {
			contents.set(1, 2, ClickableItem.of(Divers.ItemStackBuilder(Material.ENDER_PEARL, ChatColor.YELLOW + "RandomTP", new String[] {
					ChatColor.GRAY + "Le randomTP est " + ChatColor.RED + " désactivé",
					ChatColor.GRAY +"le joueur réapparaîtra à l'endroit ",
					ChatColor.GRAY +"de sa mort."
			}), event -> randomTP = true));
		}
		if(regivestuff) {
			contents.set(1, 6, ClickableItem.of(Divers.ItemStackBuilder(Material.CHEST, ChatColor.YELLOW + "Stuff", new String[] {
				ChatColor.GRAY + "Le stuff est " + ChatColor.GREEN + " activé",
				ChatColor.GRAY +"le joueur réapparaîtra avec son ",
				ChatColor.GRAY +"stuff de mort."
			}), event -> regivestuff = false));
		} else {
			contents.set(1, 6, ClickableItem.of(Divers.ItemStackBuilder(Material.CHEST, ChatColor.YELLOW + "Stuff", new String[] {
					ChatColor.GRAY + "Le stuff est " + ChatColor.RED + " désactivé",
					ChatColor.GRAY +"le joueur réapparaîtra avec un inventaire ",
					ChatColor.GRAY +"vide."
			}), event -> regivestuff = true));
		}
		

	}

}
