package com.joi.races.commands;

import org.bukkit.entity.Player;

import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

public class Wings extends Commands {

    private Settings settings = Settings.get();
    private MessageManager msgManager = MessageManager.get();

    public Wings() {
        super("races.default", "Toggle fall damage on/off", "/races wings", new String[] { "" });
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
                msgManager.message(p, "Fall damage toggled off.", MessageType.GOOD);
                break;
            } else {
                settings.setWings(p, false);
                msgManager.message(p, "Fall damage toggled on.", MessageType.GOOD);
                break;
            }
            default:
                MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
        }
    }
    
}
