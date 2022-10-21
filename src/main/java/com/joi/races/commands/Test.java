package com.joi.races.commands;

import com.joi.races.menus.Menus;
import org.bukkit.entity.Player;

public class Test extends Commands {

    public Test() {
        super("plugin.admin", "Test", "", new String[] { "t" });
    }

    @Override
    public void onCommand(Player sender, String[] args) {
        Player p = sender;
        new Menus(p);
        p.openInventory(Menus.getSelector());
    }
}
