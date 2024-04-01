package com.joi.races.commands;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

public class Add extends Commands {

    private Settings settings = Settings.get();

    public Add() {
        super("races.admin", "Add to effects or a player's data", "/races add [effect/tokens/fpcap]", new String[] { " " });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 0:
                MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
            case 1:
            case 2:
                switch (args[0]) {
                    case "effect":
                        MessageManager.get().message(sender, "Proper usage: " + "/races add effect <race> <potion effect>", MessageType.BAD);
                        break;
                    case "tokens":
                        MessageManager.get().message(sender, "Proper usage: " + "/races add tokens <player> <amount>", MessageType.BAD);
                        break;
                    case "fpcap":
                        MessageManager.get().message(sender, "Proper usage: " + "/races add fpcap <player> <amount>", MessageType.BAD);
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
                        if (settings.getEffectTypes(race.toLowerCase()).contains(effectType)) {
                            MessageManager.get().message(sender, args[2] + " is already included for " + race + " race.", MessageType.BAD);
                            break;
                        }
                        settings.addEffectType(race.toLowerCase(), effectType);
                        MessageManager.get().message(sender, args[2] + " added to " + race + " race.", MessageType.GOOD);
                        break;
                    case "tokens":
                        Player p = Main.get().getServer().getPlayerExact(args[1]);
                        if (p == null) {
                            MessageManager.get().message(sender, "Invalid player.", MessageType.BAD);
                            break;
                        }
                        int value;
                        try {
                            value = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            MessageManager.get().message(sender, "Invalid amount.", MessageType.BAD);
                            break;
                        }
                        settings.setChangeTokens(p, settings.getChangeTokens(p) + value);
                        MessageManager.get().message(sender, p.getDisplayName() + " now has " + settings.getChangeTokens(p) + " tokens.", MessageType.GOOD);
                        break;
                    case "fpcap":
                        Player player = Main.get().getServer().getPlayerExact(args[1]);
                        if (player == null) {
                            MessageManager.get().message(sender, "Invalid player.", MessageType.BAD);
                            break;
                        }
                        int val;
                        try {
                            val = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            MessageManager.get().message(sender, "Invalid amount.", MessageType.BAD);
                            break;
                        }
                        settings.setfpCap(player, settings.getfpCap(player) + val);
                        MessageManager.get().message(sender, player.getDisplayName() + " now has " + settings.getfpCap(player) + " FP cap.", MessageType.GOOD);
                        break;
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
