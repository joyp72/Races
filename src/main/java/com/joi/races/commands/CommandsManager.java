package com.joi.races.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;
import com.joi.races.menus.Menus;

import net.md_5.bungee.api.ChatColor;

public class CommandsManager implements CommandExecutor {

    private List<Commands> cmds;
    private static CommandsManager instance;
    private Settings settings = Settings.get();
    private MessageManager msgManager = MessageManager.get();

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
        Main.get().getCommand("wings").setExecutor(this);
        cmds.add(new Set());
        cmds.add(new Get());
        cmds.add(new com.joi.races.commands.List());
        cmds.add(new Add());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("wings")) {
            if (!settings.hasRace(p)) {
                msgManager.message(p, "You are not part of the Angel race.", MessageType.BAD);
                return true;
            }
            if (!settings.getRace(p).equalsIgnoreCase("angel")) {
                msgManager.message(p, "You are not part of the Angel race.", MessageType.BAD);
                return true;
            }
            boolean wings = settings.getWings(p);
            if (!wings) {
                settings.setWings(p, true);
                msgManager.message(p, "Wings toggled on.", MessageType.GOOD);
                if (p.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                    return true;
                }
                PotionEffect e = new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0 , false, false);
                p.addPotionEffect(e);
                return true;
            } else {
                settings.setWings(p, false);
                msgManager.message(p, "Wings toggled off.", MessageType.GOOD);
                if (!p.hasPotionEffect(PotionEffectType.SLOW_FALLING)) {
                    return true;
                }
                p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                return true;
            }
        }
        if (!cmd.getName().equalsIgnoreCase("races")) {
            return true;
        }
        if (args.length == 0) {
            if (!p.hasPermission("races.default")) {
                p.sendMessage(ChatColor.RED + "Not enough permissions.");
                return true;
            }
            new Menus(p);
            p.openInventory(Menus.getMenus().get(p.getUniqueId()));
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
