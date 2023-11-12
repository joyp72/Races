package com.joi.races.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.joi.races.Main;
import com.joi.races.Settings;

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
        cmds.add(new com.joi.races.commands.List());
        cmds.add(new Add());
        cmds.add(new com.joi.races.commands.New());
        cmds.add(new Remove());
        cmds.add(new Wings());
        cmds.add(new Test());
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
            Inventory ini = Bukkit.createInventory(p, 9, "Races");
            ItemStack info = new ItemStack(Material.ENDER_EYE, 1);
            {
                ItemMeta meta = info.getItemMeta();
                meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Pick your race!");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(" ");
                lore.add(ChatColor.GRAY + "Click to choose");
                lore.add(ChatColor.GRAY + "your race.");
                meta.setLore(lore);
                info.setItemMeta(meta);
                ini.setItem(3, info);
            }
            ItemStack info2 = new ItemStack(Material.NETHER_STAR, 1);
            {
                ItemMeta meta = info2.getItemMeta();
                meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "DISCLAIMER!");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Please pick your race wisely");
                lore.add(ChatColor.GRAY + "as you will only get a number of");
                lore.add(ChatColor.GRAY + "chances to change your race.");
                lore.add(" ");
                int amount = Settings.get().getChangeTokens(p);
                String s = "You have " + ChatColor.RED + amount + ChatColor.GRAY + " Race Change token";
                if (amount != 1 && amount != -1) {
                    s = s + "s";
                }
                s = s + ".";
                lore.add(ChatColor.GRAY + s);
                meta.setLore(lore);
                info2.setItemMeta(meta);
                ini.setItem(5, info2);
            }
            while (ini.firstEmpty() >= 0) {
                ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
                {
                    ItemMeta meta = pane.getItemMeta();
                    meta.setDisplayName(" ");
                    pane.setItemMeta(meta);
                    ini.setItem(ini.firstEmpty(), pane);
                }
            }
            p.openInventory(ini);
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
