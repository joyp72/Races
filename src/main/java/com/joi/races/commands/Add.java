package com.joi.races.commands;

import org.bukkit.entity.Player;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

public class Add extends Commands {

    private Settings settings = Settings.get();

    public Add() {
        super("races.admin", "Add to a player's tokens", "/races add tokens <player> <value>", new String[] { " " });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 3:
                if (!args[0].equalsIgnoreCase("tokens")) {
                    MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                    break;
                }
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
            default:
                MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
        }
    }
    
}
