package com.joi.races.control;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager
{
    private static MessageManager instance;
    public final String PREFIX;

    static {
        MessageManager.instance = new MessageManager();
    }

    public MessageManager() {
        this.PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "Races" + ChatColor.GRAY + "] ";
    }

    public static MessageManager get() {
        return MessageManager.instance;
    }

    public void message(final Player p, final String message) {
        this.message(p, message, MessageType.INFO);
    }

    public void message(final Player p, final String message, final MessageType type) {
        if (p != null) {
            p.sendMessage(type.getColor() + message);
        }
    }

    public void broadcast(final String message) {
        Bukkit.broadcastMessage(MessageType.INFO.getColor() + message);
    }

    public enum MessageType
    {
        INFO("INFO", 0, ChatColor.YELLOW),
        GOOD("GOOD", 1, ChatColor.GREEN),
        BAD("BAD", 2, ChatColor.RED);

        private ChatColor color;

        private MessageType(final String s, final int n, final ChatColor color) {
            this.color = color;
        }

        public ChatColor getColor() {
            return this.color;
        }
    }
}
