package com.joi.races.commands;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

import net.md_5.bungee.api.ChatColor;

public class Get extends Commands {

    private Settings settings = Settings.get();

    public Get() {
        super("races.admin", "Get info for a race or player", "/races get [race/tokens/effects]", new String[] { "" });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "race":
                        MessageManager.get().message(sender, "Proper usage: " + "/races get race <player>", MessageType.BAD);
                        break;
                    case "tokens":
                        MessageManager.get().message(sender, "Proper usage: " + "/races get tokens <player>", MessageType.BAD);
                        break;
                    case "effects":
                        MessageManager.get().message(sender, "Proper usage: " + "/races get effects <race>", MessageType.BAD);
                        break;
                    default:
                        MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                        break;
                }
                break;
            case 2:
                switch (args[0]) {
                    case "tokens":
                    {
                        Player p = Main.get().getServer().getPlayerExact(args[1]);
                        if (p == null) {
                            MessageManager.get().message(sender, "Invalid player.", MessageType.BAD);
                            break;
                        }
                        int amount = settings.getChangeTokens(p);
                        MessageManager.get().message(sender, p.getDisplayName() + " has " + amount + " race change tokens.");
                        break;
                    }
                    case "race":
                    {
                        Player p = Main.get().getServer().getPlayerExact(args[1]);
                        if (p == null) {
                            MessageManager.get().message(sender, "Invalid player.", MessageType.BAD);
                            break;
                        }
                        if (!settings.hasRace(p)) {
                            MessageManager.get().message(sender, p.getDisplayName() + " is not part of a race.");
                            break;
                        } else {
                            String race = settings.getRace(p);
                            race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
                            ChatColor color = ChatColor.RESET;
                            if (settings.getRaceColor(race.toLowerCase()) != null) {
                                color = settings.getRaceColor(race.toLowerCase());
                            }
                            MessageManager.get().message(sender, p.getDisplayName() + " is part of the " + color + race + ChatColor.YELLOW + " race.");
                            break;
                        }
                    }
                    case "effects":
                    {
                        String race = args[1];
                        race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
                        if (!settings.isRace(race.toLowerCase())) {
                            MessageManager.get().message(sender, "Invalid race.", MessageType.BAD);
                            break;
                        }
                        ChatColor color = ChatColor.RESET;
                        if (settings.getRaceColor(race.toLowerCase()) != null) {
                            color = settings.getRaceColor(race.toLowerCase());
                        }
                        MessageManager.get().message(sender, ChatColor.RESET + "The " + color + race + ChatColor.RESET + " race has the following effects:");
                        for (PotionEffectType effectType : settings.getEffectTypes(race.toLowerCase())) {
                            String effectName = effectType.getName();
                            String firstChar = effectName.substring(0, 1);
                            effectName = effectName.substring(1);
                            effectName = effectName.toLowerCase();
                            effectName = firstChar + effectName;
                            effectName = effectName.replace("_", " ");
                            MessageManager.get().message(sender, ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + effectName);
                        }
                        break;
                    }
                    default:
                        MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                        break;
                }
                break;
            default:
                MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
        }
    }
    
}
