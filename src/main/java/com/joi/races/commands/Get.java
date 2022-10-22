package com.joi.races.commands;

import org.bukkit.entity.Player;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

public class Get extends Commands {

    private Settings settings = Settings.get();

    public Get() {
        super("races.admin", "Get data for a player", "get [race | tokens] <player>", new String[] { "" });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 2:
                Player p = Main.get().getServer().getPlayerExact(args[1]);
                if (p == null) {
                    MessageManager.get().message(sender, "Invalid player.", MessageType.BAD);
                    break;
                }
                switch (args[0]) {
                    case "tokens":
                        int amount = settings.getChangeTokens(p);
                        MessageManager.get().message(sender, p.getDisplayName() + " has " + amount + " race change tokens.");
                        break;
                    case "race":
                        if (!settings.hasRace(p)) {
                            MessageManager.get().message(sender, p.getDisplayName() + " is not part of a race.");
                            break;
                        } else {
                            String race = settings.getRace(p);
                            MessageManager.get().message(sender, p.getDisplayName() + " is part of the " + race + " race.");
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
