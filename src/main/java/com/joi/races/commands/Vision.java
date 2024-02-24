package com.joi.races.commands;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

public class Vision extends Commands {

    private Settings settings = Settings.get();
    private MessageManager msgManager = MessageManager.get();

    public Vision() {
        super("races.default", "Toggle night vision on/off", "/races vision", new String[] { "" });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        Player p = sender;
        switch (args.length) {
            case 0:
            if (!settings.hasRace(p)) {
                msgManager.message(p, "You are not part of the Dwarf race.", MessageType.BAD);
                break;
            }
            if (!settings.getRace(p).equalsIgnoreCase("dwarf")) {
                msgManager.message(p, "You are not part of the Dwarf race.", MessageType.BAD);
                break;
            }
            boolean vision = settings.getNight_Vision(p);
            if (!vision) {
                settings.setNight_Vision(p, true);
                msgManager.message(p, "Night Vision toggled on.", MessageType.GOOD);
                if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    break;
                }
                PotionEffect effect = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0 , false, false);
                p.addPotionEffect(effect);
                break;
            } else {
                settings.setNight_Vision(p, false);
                msgManager.message(p, "Night Vision toggled off.", MessageType.GOOD);
                if (!p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    break;
                }
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                break;
            }
            default:
                MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
        }
    }
    
}
