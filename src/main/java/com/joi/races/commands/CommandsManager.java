package com.joi.races.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.joi.races.Main;
import com.joi.races.menus.Menus;

import net.md_5.bungee.api.ChatColor;

public class CommandsManager implements CommandExecutor {

    private List<Commands> cmds;
    private static CommandsManager instance;

    static {
        instance = new CommandsManager();
    }

    public static CommandsManager get() {
        return instance;
    }

    private  CommandsManager() {
        cmds = new ArrayList<Commands>();
    }

    public void setup() {
        Main.get().getCommand("races").setExecutor(this);
        cmds.add(new Set());
        cmds.add(new Get());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (!cmd.getName().equalsIgnoreCase("races")) {
            return true;
        }
        if (args.length == 0) {
            if (!p.hasPermission("races.default")) {
                p.sendMessage(ChatColor.RED + "Not enough permissions.");
                return true;
            }
            new Menus(p);
            p.openInventory(Menus.getSelector());
        }else {
            Commands c = getCommand(args[0]);
            if (c != null) {
                List<String> a = new ArrayList<String>(Arrays.asList(args));
                a.remove(0);
                args = a.toArray(new String[a.size()]);
                c.commandPreprocess(p, args);
            } else {
                p.sendMessage(ChatColor.RED + "Invalid command.");
            }
        }
        return true;
    }

    private Commands getCommand(String name) {
        for (Commands c : cmds) {
            if (c.getClass().getSimpleName().trim().equalsIgnoreCase(name.trim())) {
                return c;
            }
            String[] aliases;
            for (int length = (aliases = c.getAliases()).length, i = 0; i < length; ++i) {
                String s = aliases[i];
                if (s.trim().equalsIgnoreCase(name.trim())) {
                    return c;
                }
            }
        }
        return null;
    }
}
