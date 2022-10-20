package com.joi.races;

import com.joi.races.commands.CommandsManager;
import com.joi.races.control.ControlListener;
import com.joi.races.menus.MenusListener;
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
        getLogger().info("Plugin Enabled");
        CommandsManager.get().setup();
        Settings.get().setup(this);
        ControlListener.get().setup();
        MenusListener.get().setup();
    }
    @Override
    public void onDisable() {
        super.onDisable();
        Settings.get().resetDB();
        getLogger().info("Disabled!");
    }

    public void print(String s) {
        getLogger().info(s);
        return;
    }
}
