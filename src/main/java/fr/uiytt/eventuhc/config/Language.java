package fr.uiytt.eventuhc.config;

import de.leonhard.storage.Yaml;
import fr.uiytt.eventuhc.Main;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.nio.file.Files;

public enum Language {

    HEADER("header"),
    ERROR_HEADER("error_header"),
    EVENT_HEADER("event_header"),

    GAME_VICTORY("game.victory"),
    GAME_PVP_SOON("game.pvp_soon"),
    GAME_PVP_ACTIVATED("game.pvp_activated"),
    GAME_BORDER_SOON("game.border_soon"),
    GAME_BORDER_ACTIVATED("game.border_activated"),
    GAME_TELEPORTING("game.teleporting"),
    GAME_TELEPORTING_PLAYER("game.teleporting_player"),
    GAME_XPLAYERS_CONNECTED("game.xplayers_connected"),
    GAME_ENOUGH_PLAYERS("game.enough_players"),
    GAME_SART_IN("game.start_in"),

    ERROR_WINNER_NOT_ONLINE("warning.error.winner_not_online"),
    ERROR_ADD_TEAM("warning.error.add_team"),
    WARNING_FFA("warning.ffa"),
    WARNING_RECONNECTION("warning.reconnection"),
    WARNING_PLAYER_REMOVED("warning.player_removed"),
    WARNING_DIAMOND_LIMIT("warning.diamond_limit"),
    WARNING_PERMISSION("warning.permission"),
    WARNING_GAME_ON("warning.game_is_on"),
    WARNING_GAME_OFF("warning.game_is_off"),
    WARNING_NOT_ENOUGH_PLAYERS("warning.not_enough_player"),
    WARNING_CONSOL("consol"),

    COMMAND_STOP("command.stop"),
    COMMAND_FORCE_BORDER("command.force_border"),
    COMMAND_FORCE_PVP("command.force_pvp"),
    COMMAND_RELOAD("command.reload"),
    COMMAND_FINISH_ITEMS("command.finish_items"),
    COMMAND_PLAYER_ALIVE("command.player_alive"),
    COMMAND_PLAYER_NOT_EXIST("command.player_exist"),
    COMMAND_SYNTAX("command.syntax"),
    COMMAND_HELP_TITLE("command.help.title"),
    COMMAND_HELP_ALIASES("command.help.aliases"),
    COMMAND_HELP_START("command.help.start"),
    COMMAND_HELP_STOP("command.help.stop"),
    COMMAND_HELP_CONFIG("command.help.config"),
    COMMAND_HELP_FORCE("command.help.force"),
    COMMAND_HELP_RELOAD("command.help.reload"),
    COMMAND_HELP_RESPAWN("command.help.respawn"),
    COMMAND_HELP_FINISH("command.help.finish"),
    COMMAND_HELP_TEAM("command.help.team"),
    COMMAND_HELP_HELP("command.help.help"),

    EVENT_TNT_TIMER_LORE("events.tnt_timer.lore"),
    EVENT_TNT_TIMER_ENABLE("events.tnt_timer.enable"),
    EVENT_BFF_LORE("events.bff.lore"),
    EVENT_BFF_ENABLE("events.bff.enable"),
    EVENT_BLAZE_LORE("events.blaze.lore"),
    EVENT_BLAZE_ENABLE("events.blaze.enable"),
    EVENT_BLOCKS_ATTACK_LORE("events.blocks_attack.lore"),
    EVENT_BLOCKS_ATTACK_ENABLE("events.blocks_attack.enable"),
    EVENT_BLOCKS_GRAVITY_LORE("events.blocks_gravity.lore"),
    EVENT_BLOCKS_GRAVITY_ENABLE("events.blocks_gravity.enable"),
    EVENT_BOW_SWAP_LORE("events.bow_swap.lore"),
    EVENT_BOW_SWAP_ENABLE("events.bow_swap.enable"),
    EVENT_CURSE_OF_BINDING_LORE("events.curse_of_binding.lore"),
    EVENT_CURSE_OF_BINDING_ENABLE("events.curse_of_binding.enable"),
    EVENT_DAMAGE_DROP_ITEMS_LORE("events.damage_drop_items.lore"),
    EVENT_DAMAGE_DROP_ITEMS_ENABLE("events.damage_drop_items.enable"),
    EVENT_DOUBLE_HEALTH_LORE("events.double_health.lore"),
    EVENT_DOUBLE_HEALTH_ENABLE("events.double_health.enable"),
    EVENT_EXPLOSION_SPAWN_MOBS_LORE("events.explosion_spawn_mobs.lore"),
    EVENT_EXPLOSION_SPAWN_MOBS_ENABLE("events.explosion_spawn_mobs.enable"),
    EVENT_GOLDEN_APPLE_LORE("events.golden_apple.lore"),
    EVENT_GOLDEN_APPLE_ENABLE("events.golden_apple.enable"),
    EVENT_HASTE_APPLE_LORE("events.haste.lore"),
    EVENT_HASTE_APPLE_ENABLE("events.haste.enable"),
    EVENT_HEAL_LORE("events.heal.lore"),
    EVENT_HEAL_ENABLE("events.heal.enable"),
    EVENT_JUMP_BOOST_LORE("events.jump_boost.lore"),
    EVENT_JUMP_BOOST_ENABLE("events.jump_boost.enable"),
    EVENT_LEVITATION_LORE("events.levitation.lore"),
    EVENT_LEVITATION_ENABLE("events.levitation.enable"),
    EVENT_LIFE_SHARE_LORE("events.life_share.lore"),
    EVENT_LIFE_SHARE_ENABLE("events.life_share.enable"),
    EVENT_LIFE_SHARE_LINK("events.life_share.link"),
    EVENT_LIFE_SHUFFLE_LORE("events.life_shuffle.lore"),
    EVENT_LIFE_SHUFFLE_ENABLE("events.life_shuffle.enable"),
    EVENT_LIFE_SHUFFLE_RECEIVE("events.life_shuffle.receive"),
    EVENT_NO_DAMAGE_MOBS_LORE("events.no_damage_mobs.lore"),
    EVENT_NO_DAMAGE_MOBS_ENABLE("events.no_damage_mobs.enable"),
    EVENT_NO_ENTITY_GRAVITY_LORE("events.no_entity_gravity.lore"),
    EVENT_NO_ENTITY_GRAVITY_ENABLE("events.no_entity_gravity.enable"),
    EVENT_NO_JUMP_LORE("events.no_jump.lore"),
    EVENT_NO_JUMP_ENABLE("events.no_jump.enable"),
    EVENT_OTHER_WORLD_LORE("events.other_world.lore"),
    EVENT_OTHER_WORLD_ENABLE("events.other_world.enable"),
    EVENT_OTHER_WORLD_CANCEL("events.other_world.cancel"),
    EVENT_POPCORN_LORE("events.popcorn.lore"),
    EVENT_POPCORN_ENABLE("events.popcorn.enable"),
    EVENT_RAINING_MOBS_LORE("events.raining_mobs.lore"),
    EVENT_RAINING_MOBS_ENABLE("events.raining_mobs.enable"),
    EVENT_RANDOM_INVENTORY_LORE("events.random_inventory.lore"),
    EVENT_RANDOM_INVENTORY_ENABLE("events.random_inventory.enable"),
    EVENT_RUN_GLOW_LORE("events.run_glow.lore"),
    EVENT_RUN_GLOW_ENABLE("events.run_glow.enable"),
    EVENT_SKYHIGH_LORE("events.skyhigh.lore"),
    EVENT_SKYHIGH_ENABLE("events.skyhigh.enable"),
    EVENT_SLOWNESS_LORE("events.slowness.lore"),
    EVENT_SLOWNESS_ENABLE("events.slowness.enable"),
    EVENT_SMALL_METEORS_LORE("events.small_meteors.lore"),
    EVENT_SMALL_METEORS_ENABLE("events.small_meteors.enable"),
    EVENT_SNOWBALL_ZOMBIE_LORE("events.snowball_zombie.lore"),
    EVENT_SNOWBALL_ZOMBIE_ENABLE("events.snowball_zombie.enable"),
    EVENT_SPEED_LORE("events.speed.lore"),
    EVENT_SPEED_ENABLE("events.speed.enable"),
    EVENT_SPEED_PIG_LORE("events.speed_pig.lore"),
    EVENT_SPEED_PIG_ENABLE("events.speed_pig.enable"),
    EVENT_STONE_IS_LAVA_LORE("events.stone_is_lava.lore"),
    EVENT_STONE_IS_LAVA_ENABLE("events.stone_is_lava.enable"),
    EVENT_SUNLIGHT_KILLS_LORE("events.sunlight_kills.lore"),
    EVENT_SUNLIGHT_KILLS_ENABLE("events.sunlight_kills.enable"),
    EVENT_SWAP_LORE("events.swap.lore"),
    EVENT_SWAP_ENABLE("events.swap.enable"),
    EVENT_SWAP_TIMER("events.swap.timer"),
    EVENT_SWAP_CANCEL("events.swap.cancel"),
    EVENT_TELEPORTATION_TOP_LORE("events.teleportation_top.lore"),
    EVENT_TELEPORTATION_TOP_ENABLE("events.teleportation_top.enable"),
    EVENT_TELEPORTATION_OTHERS_LORE("events.teleportation_others.lore"),
    EVENT_TELEPORTATION_OTHERS_ENABLE("events.teleportation_others.enable"),
    EVENT_TELEPORTATION_OTHERS_ENABLE2("events.teleportation_others.enable2"),
    EVENT_UNBREAKABLE_BLOCKS_LORE("events.unbreakable_blocks.lore"),
    EVENT_UNBREAKABLE_BLOCKS_ENABLE("events.unbreakable_blocks.enable"),
    EVENT_WATER_POISON_LORE("events.water_poison.lore"),
    EVENT_WATER_POISON_ENABLE("events.water_poison.enable"),
    EVENT_WEIRD_BIOMES_LORE("events.weird_biomes.lore"),
    EVENT_WEIRD_BIOMES_ENABLE("events.weird_biomes.enable"),
    EVENT_WEIRD_BIOMES_COORDS("events.weird_biomes.coords"),
    EVENT_WEIRD_FIGHT_LORE("events.weird_fight.lore"),
    EVENT_WEIRD_FIGHT_ENABLE("events.weird_fight.enable"),
    EVENT_WITHER_MIDDLE_LORE("events.wither_middle.lore"),
    EVENT_WITHER_MIDDLE_ENABLE("events.wither_middle.enable"),

    GUI_ENABLE("gui.enable"),
    GUI_DISABLE("gui.disable"),
    GUI_NEXT("gui.next"),
    GUI_PREVIOUS("gui.previous"),
    GUI_ADVANCED_APPLES_DROP_NAME("gui.advanced.apples_drop_name"),
    GUI_ADVANCED_APPLES_DROP_LORE("gui.advanced.apples_drop_lore"),
    GUI_ADVANCED_FLINTS_DROP_NAME("gui.advanced.flints_drop_name"),
    GUI_ADVANCED_FLINTS_DROP_LORE("gui.advanced.flints_drop_lore"),
    GUI_ADVANCED_POTIONLV2_NAME("gui.advanced.potionslv2_name"),
    GUI_ADVANCED_POTIONLV2_LORE("gui.advanced.potionslv2_lore"),
    GUI_ADVANCED_DIAMOND_LIMIT_NAME("gui.advanced.diamond_limit_name"),
    GUI_ADVANCED_DIAMOND_LIMIT("gui.advanced.diamond_limit"),
    GUI_ADVANCED_DIAMOND_LIMIT_ENABLE("gui.advanced.diamond_limite_enable"),
    GUI_ADVANCED_DIAMOND_LIMIT_DISABLE("gui.advanced.diamond_limite_disable"),
    GUI_ADVANCED_CUTCLEAN_NAME("gui.advanced.cutclean_name"),
    GUI_ADVANCED_CUTCLEAN_LORE("gui.advanced.cutclean_lore"),
    GUI_ADVANCED_FINAL_HEAL_NAME("gui.advanced.final_heal_name"),
    GUI_ADVANCED_FINAL_HEAL_LORE("gui.advanced.final_heal_lore"),
    GUI_ADVANCED_VISIBLE_HEALTH_NAME("gui.advanced.visible_health_name"),
    GUI_ADVANCED_VISIBLE_HEALTH_LORE("gui.advanced.visible_health_lore"),
    GUI_BORDER_END_NAME("gui.border.end_name"),
    GUI_BORDER_END_LORE("gui.border.end_lore"),
    GUI_BORDER_TIME_NAME("gui.border.time_name"),
    GUI_BORDER_TIME_LORE("gui.border.time_lore"),
    GUI_BORDER_SPEED_NAME("gui.border.speed_name"),
    GUI_BORDER_SPEED_LORE("gui.border.speed_lore"),
    GUI_BORDER_START_NAME("gui.border.start_name"),
    GUI_BORDER_START_LORE("gui.border.start_lore"),
    GUI_RESPAWN_EFFECTS("gui.respawn.effects"),
    GUI_RESPAWN_NAME("gui.respawn.name"),
    GUI_RESPAWN_RANDOM_TP_NAME("gui.respawn.random_tp_name"),
    GUI_RESPAWN_RANDOM_TP_LORE("gui.respawn.random_tp_lore"),
    GUI_RESPAWN_GIVE_STUFF_NAME("gui.respawn.give_stuff_name"),
    GUI_RESPAWN_GIVE_STUFF_LORE("gui.respawn.give_stuff_lore"),
    GUI_MAIN_PVP_NAME("gui.main.pvp_name"),
    GUI_MAIN_PVP_LORE("gui.main.pvp_lore"),
    GUI_MAIN_TIME_EVENTS_NAME("gui.main.time_events_name"),
    GUI_MAIN_TIME_EVENTS_LORE("gui.main.time_events_lore"),
    GUI_MAIN_START_ITEMS_NAME("gui.main.start_items_name"),
    GUI_MAIN_START_ITEMS_LORE("gui.main.start_items_lore"),
    GUI_MAIN_DEATH_ITEMS_NAME("gui.main.death_items_name"),
    GUI_MAIN_DEATH_ITEMS_LORE("gui.main.death_items_lore"),
    GUI_MAIN_EVENTS_NAME("gui.main.events_name"),
    GUI_MAIN_EVENTS_LORE("gui.main.events_lore"),
    GUI_MAIN_ADVANCED_NAME("gui.main.advanced_name"),
    GUI_MAIN_ADVANCED_LORE("gui.main.advanced_lore"),
    GUI_MAIN_TEAMS_SIZE_NAME("gui.main.teams_size_name"),
    GUI_MAIN_TEAMS_FFA_LORE("gui.main.teams_ffa_lore"),
    GUI_MAIN_TEAMS_SIZE_LORE("gui.main.teams_size_lore"),
    GUI_OTHER_ADD_ITEMS("gui.other.add_items"),
    GUI_OTHER_FINISH("gui.other.finish"),

    GUI_TITLE_ADVANCED("gui.title.advanced_menu"),
    GUI_TITLE_APPLES_RATE("gui.title.apples_rate_menu"),
    GUI_TITLE_BORDER_END("gui.title.border_end_menu"),
    GUI_TITLE_BORDER_START("gui.title.border_start_menu"),
    GUI_TITLE_BORDER_SPEED("gui.title.border_speed_menu"),
    GUI_TITLE_BORDER_TIME("gui.title.border_time_menu"),
    GUI_TITLE_DEATH_ITEMS("gui.title.death_items_menu"),
    GUI_TITLE_DIAMOND_LIMIT("gui.title.diamond_limit_menu"),
    GUI_TITLE_EVENTS("gui.title.events_menu"),
    GUI_TITLE_FLITNTS_RATE("gui.title.flints_rate_menu"),
    GUI_TITLE_MAIN("gui.title.main_menu"),
    GUI_TITLE_PVP_TIME("gui.title.pvp_menu"),
    GUI_TITLE_RESPAWN("gui.title.respawn_menu"),
    GUI_TITLE_SPECTATOR_INVENTORY("gui.title.spectator_inventory_menu"),
    GUI_TITLE_START_ITEMS("gui.title.start_items_menu"),
    GUI_TITLE_TEAMS_MENU("gui.title.teams_menu"),
    GUI_TITLE_EVENTS_TIME("gui.title.time_events"),



    GAME_STOP("game.stop");

    public static void init(String langName) {
        try {
            File languageFile = new File(Main.getInstance().getDataFolder().getAbsolutePath() + File.separator + "languages" + File.separator + langName + ".yml");
            if (!languageFile.exists()) {
                Main.getLog().fine(languageFile.getName() + " not found, extracting...");
                languageFile.getParentFile().mkdirs();
                Files.copy(Language.class.getResourceAsStream("/languages/"+ langName +".yml"), languageFile.toPath());
            }

            Yaml languageYaml = new Yaml(langName, "plugins/Event-UHC/languages");
            for (Language message : Language.values()) {
                String newMessage = languageYaml.getOrDefault(message.path, "&cThe text in file " + languageYaml.getName() + " with path " + message.path + " could not be found.");
                newMessage = newMessage.replace("%header%", HEADER.message);
                newMessage = newMessage.replace("%error_header%", ERROR_HEADER.message);
                newMessage = newMessage.replace("%event_header%", EVENT_HEADER.message);

                message.message = ChatColor.translateAlternateColorCodes('&', newMessage);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Main.getLog().severe("There was a probleme while loading the language file " + langName);
        }
    }


    private final String path;
    private String message;

    Language(String path) {
        this.path = path;
        this.message = "not loaded";
    }

    public String getMessage() {
        return message;
    }

    /**
     * Transform a string into a lore.
     * It splits the lore at ||
     *
     * @param lore A string which should be transformed into a lore
     * @return A String[] containing the lore.
     */
    public static String[] splitLore(String lore) {
        return lore.split("\n");
    }
}
