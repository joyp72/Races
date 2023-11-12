package com.joi.races.commands;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

public class Remove extends Commands {

    private Settings settings = Settings.get();

    public Remove() {
        super("races.admin", "Remove a race or effect", "/races remove [race/effect]", new String[] { " " });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 0:
                MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
            case 1:
                switch (args[0]) {
                    case "race":
                        MessageManager.get().message(sender, "Proper usage: " + "/races remove race <race>", MessageType.BAD);
                        break;
                    default:
                        break;
                }
                if (args[0].equalsIgnoreCase("race")) {
                    break;
                }
            case 2:
                switch (args[0]) {
                    case "race":
                        String race = args[1];
                        race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
                        if (!settings.isRace(race.toLowerCase())) {
                            MessageManager.get().message(sender, "Invalid race.", MessageType.BAD);
                            break;
                        }
                        settings.removeRace(race.toLowerCase());
                        MessageManager.get().message(sender, "Removed " + race + " race.", MessageType.GOOD);
                        break;
                    case "effect":
                        MessageManager.get().message(sender, "Proper usage: " + "/races remove effect <race> <effect>", MessageType.BAD);
                        break;
                    default:
                        MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                        break;
                }
                break;
            case 3:
                switch (args[0]) {
                    case "effect":
                        String race = args[1];
                        race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
                        if (!settings.isRace(race.toLowerCase())) {
                            MessageManager.get().message(sender, "Invalid race.", MessageType.BAD);
                            break;
                        }
                        if (PotionEffectType.getByName(args[2]) == null) {
                            MessageManager.get().message(sender, "Invalid potion effect.", MessageType.BAD);
                            break;
                        }
                        PotionEffectType effectType = PotionEffectType.getByName(args[2]);
                        if (!settings.getEffectTypes(race.toLowerCase()).contains(effectType)) {
                            MessageManager.get().message(sender, args[2] + " is already not included for " + race + " race.", MessageType.BAD);
                            break;
                        }
                        settings.removeEffectType(race.toLowerCase(), effectType);
                        MessageManager.get().message(sender, args[2] + " removed from " + race + " race.", MessageType.GOOD);
                        break;
                    default:
                        MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                        break;
                }
                break;
            default:
                break;
        }
    }
    
}
