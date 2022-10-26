package com.joi.races.commands;

import org.bukkit.entity.Player;

import com.joi.races.Main;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class List extends Commands {

    public List() {
        super("races.default", "Display a list of online players and their race", "/races list", new String[] { "" });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        switch (args.length) {
            case 0:
                MessageManager.get().message(sender, ChatColor.WHITE + "Players online:");
                for (Player p : Main.get().getServer().getOnlinePlayers()) {
                    String text = ChatColor.GREEN + p.getDisplayName() + ChatColor.GRAY + " - " + "%races_race%";
                    text = PlaceholderAPI.setPlaceholders(p, text);
                    p.sendMessage(text);
                }
                break;
            default:
                MessageManager.get().message(sender, "Proper usage: " + getUsage(), MessageType.BAD);
                break;
        }
    }
}
