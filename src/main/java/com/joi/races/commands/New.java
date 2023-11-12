package com.joi.races.commands;

import org.bukkit.entity.Player;

import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

public class New extends Commands{

    private Settings settings = Settings.get();

    public New() {
        super("races.admin", "Add a new race.", "/races new <name>", new String[] { " " });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 1:
                String race = args[0];
                race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
                if (settings.isRace(race.toLowerCase())) {
                    MessageManager.get().message(sender, "'" + race + "' is already an existing race.", MessageType.BAD);
                    break;
                }
                settings.newRace(race.toLowerCase());
                MessageManager.get().message(sender, "Added " + race + " as a new race.", MessageType.GOOD);
                MessageManager.get().message(sender, "Be sure to edit the new race using /races [set/add].");
                break;
            default:
                 MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
        }
    }
    
}
