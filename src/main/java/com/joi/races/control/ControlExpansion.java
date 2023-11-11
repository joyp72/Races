package com.joi.races.control;

import org.bukkit.OfflinePlayer;

import com.joi.races.Settings;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;

public class ControlExpansion extends PlaceholderExpansion {

    public static ControlExpansion instance;

    static {
        instance = new ControlExpansion();
    }

    public static ControlExpansion get() {
        return instance;
    }

    private Settings settings = Settings.get();

    @Override
    public String getAuthor() {
        return "joi_";
    }

    @Override
    public String getIdentifier() {
        return "races";
    }

    @Override
    public String getVersion() {
        return "1.4";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player == null){
            return "";
        }
        if (params.equalsIgnoreCase("race")) {
            if (!settings.hasRace(player.getUniqueId())) {
                return "";
            }
            String race = settings.getRace(player.getUniqueId());
            race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
            switch (race) {
                case "Human":
                    race = ChatColor.DARK_AQUA + race;
                    break;
                case "Angel":
                    race = ChatColor.YELLOW + race;
                    break;
                case "Merrow":
                    race = ChatColor.BLUE + race;
                    break;
                case "Dragonborne":
                    race = ChatColor.DARK_PURPLE + race;
                    break;
                case "Dwarf":
                    race = ChatColor.GRAY + race;
                    break;
                case "Oni":
                    race = ChatColor.DARK_RED + race;
                    break;
                default:
                    break;
            }
            return race;
        }
        if (params.equalsIgnoreCase("tokens")) {
            int amount = settings.getChangeTokens(player.getUniqueId());
            return "" + amount;
        }
        return "";
    }
    
}
