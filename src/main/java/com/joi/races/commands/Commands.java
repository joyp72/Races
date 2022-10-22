package com.joi.races.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class Commands {
    private String message;
    private String usage;
    private String permission;
    private String[] aliases;

    public Commands(String permission, String message, String usage, String... aliases) {
        this.permission = permission;
        this.message = message;
        this.usage = usage;
        this.aliases = aliases;
    }

    public abstract void onCommand(Player p0, String[] p1);

    public void commandPreprocess(Player sender, String[] args) {
        if (sender.hasPermission(permission)) {
            onCommand(sender, args);
        } else {
            sender.sendMessage(ChatColor.RED + "Not enough permissions.");
        }
    }

    public String getMessage() {
        return message;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    public boolean noIndex() {
        return false;
    }
}
