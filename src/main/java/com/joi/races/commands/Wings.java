package com.joi.races.commands;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

public class Wings extends Commands {

    private Settings settings = Settings.get();
    private MessageManager msgManager = MessageManager.get();

    public Wings() {
        super("races.default", "Toggle wings on/off", "/races wings", new String[] { "" });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        Player p = sender;
        switch (args.length) {
            case 0:
            if (!settings.hasRace(p)) {
                msgManager.message(p, "You are not part of the Angel race.", MessageType.BAD);
                break;
            }
            if (!settings.getRace(p).equalsIgnoreCase("angel")) {
                msgManager.message(p, "You are not part of the Angel race.", MessageType.BAD);
                break;
            }
            boolean wings = settings.getWings(p);
            if (!wings) {
                settings.setWings(p, true);
                msgManager.message(p, "Wings toggled on.", MessageType.GOOD);
                if (p.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                    break;
                }
                PotionEffect e = new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0 , false, false);
                p.addPotionEffect(e);
                break;
            } else {
                settings.setWings(p, false);
                msgManager.message(p, "Wings toggled off.", MessageType.GOOD);
                if (!p.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                    break;
                }
                p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                break;
            }
            default:
                MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
        }
    }
    
}
