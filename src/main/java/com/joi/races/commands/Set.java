package com.joi.races.commands;

import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

import net.md_5.bungee.api.ChatColor;

public class Set extends Commands {

   private Settings settings = Settings.get();

    public Set() {
        super("races.admin", "Set data for a race or player", "/races set [race/tokens/color/head]", new String[] { "" });
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
                    case "race":
                        MessageManager.get().message(sender, "Proper usage: " + "/races set race <player> <race>", MessageType.BAD);
                        break;
                    case "tokens":
                        MessageManager.get().message(sender, "Proper usage: " + "/races set tokens <player> <amount>", MessageType.BAD);
                        break;
                    case "color":
                        MessageManager.get().message(sender, "Proper usage: " + "/races set color <race> <color code>", MessageType.BAD);
                        break;
                    case "head":
                        MessageManager.get().message(sender, "Proper usage: " + "/races set head <race> <skin URL>", MessageType.BAD);
                        break;
                    default:
                        MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                        break;
                }
                break;
            case 3:
                switch(args[0]) {
                    case "race":
                    {
                        Player p = Main.get().getServer().getPlayerExact(args[1]);
                        if (p == null) {
                            MessageManager.get().message(sender, "Invalid player.", MessageType.BAD);
                            break;
                        }
                        String race = args[2];
                        race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
                        if (!settings.isRace(race.toLowerCase())) {
                            MessageManager.get().message(sender, "Invalid race.", MessageType.BAD);
                            break;
                        }
                        if (settings.hasRace(p)) {
                            String prevRace = settings.getRace(p);
                            if (prevRace.equalsIgnoreCase(race.toLowerCase())) {
                                MessageManager.get().message(sender, p.getDisplayName() + " is already of race " + race + ".", MessageType.BAD);
                                break;
                            }
                            for (PotionEffect effect : settings.getEffects(prevRace)) {
                                if (p.hasPotionEffect(effect.getType())) {
                                    p.removePotionEffect(effect.getType());
                                }
                            }
                        }
                        settings.setRace(p, race.toLowerCase());
                        p.addPotionEffects(settings.getEffects(race.toLowerCase()));
                        MessageManager.get().message(sender, "Race for " + p.getDisplayName() + " set to " + race + ".", MessageType.GOOD);
                        break;
                    }
                    case "tokens":
                    {
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
                        settings.setChangeTokens(p, value);
                        MessageManager.get().message(sender, "# of tokens for " + p.getDisplayName() + " set to " + value + ".", MessageType.GOOD);
                        break;
                    }
                    case "color":
                    {
                        String race = args[1];
                        race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
                        if (!settings.isRace(race.toLowerCase())) {
                            MessageManager.get().message(sender, "Invalid race.", MessageType.BAD);
                            break;
                        }
                        String colorcode = args[2];
                        if (ChatColor.getByChar(colorcode.charAt(1)) == null) {
                            MessageManager.get().message(sender, "Invalid color.", MessageType.BAD);
                            break;
                        }
                        ChatColor color = ChatColor.getByChar(colorcode.charAt(1));
                        settings.setRaceColor(race.toLowerCase(), color);
                        MessageManager.get().message(sender, "Color changed for the " + color + race + ChatColor.GREEN + " race.", MessageType.GOOD);
                        break;
                    }
                    case "head":
                    {
                        String race = args[1];
                        race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
                        if (!settings.isRace(race.toLowerCase())) {
                            MessageManager.get().message(sender, "Invalid race.", MessageType.BAD);
                            break;
                        }
                        URL url = null;
                        try {
                            url = new URL(args[2]);
                        } catch (MalformedURLException e) {
                            MessageManager.get().message(sender, "Invalid skin URL.", MessageType.BAD);
                            e.printStackTrace();
                            break;
                        }
                        settings.setSkinURL(race.toLowerCase(), url);
                        ChatColor color = ChatColor.RESET;
                        if (settings.getRaceColor(race.toLowerCase()) != null) {
                            color = settings.getRaceColor(race.toLowerCase());
                        }
                        MessageManager.get().message(sender, " Successfuly set Skin URL for " + color + race + ChatColor.GREEN + " race.", MessageType.GOOD);
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
