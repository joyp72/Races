package com.joi.races.commands;

import org.bukkit.entity.Player;

import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;


public class Test extends Commands {

    private Settings settings = Settings.get();

    public Test() {
        super("races.default", "Test command", "/races test", new String[] { "" });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 0:
                MessageManager.get().message(sender, settings.getSkinURL(settings.getRace(sender).toString()) + "");
                break;
            default:
                MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
        }
    }
    
}
