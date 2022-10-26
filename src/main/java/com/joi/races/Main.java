package com.joi.races;

import com.joi.races.commands.CommandsManager;
import com.joi.races.control.ControlExpansion;
import com.joi.races.control.ControlListener;
import com.joi.races.menus.MenusListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;

    public static Main get() {
        return instance;
    }
    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        CommandsManager.get().setup();
        Settings.get().setup(this);
        ControlListener.get().setup();
        MenusListener.get().setup();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            ControlExpansion.get().register();
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        Settings.get().resetDB();
        getLogger().info("Disabled!");
    }
}
