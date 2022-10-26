package com.joi.races.commands;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

public class Set extends Commands {

   private Settings settings = Settings.get();

    public Set() {
        super("races.admin", "Set data for a player", "/races set [race | tokens] <player> <value>", new String[] { "" });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 3:
                Player p = Main.get().getServer().getPlayerExact(args[1]);
                if (p == null) {
                    MessageManager.get().message(sender, "Invalid player.", MessageType.BAD);
                    break;
                }
                switch(args[0]) {
                    case "race":
                        String race = args[2];
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
                    case "tokens":
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
