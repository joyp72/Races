package com.joi.races.menus;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenusListener implements Listener {

    private static MenusListener instance;
    private Settings settings = Settings.get();
    private MessageManager msgManager = MessageManager.get();

    static {
        instance = new MenusListener();
    }

    public static MenusListener get() {
        return instance;
    }

    public void setup() {
        Bukkit.getPluginManager().registerEvents(this, Main.get());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (Menus.getSelector() == null) {
            return;
        }
        if (!e.getView().getTitle().equalsIgnoreCase("Pick a race:")) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        String race = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        if (!settings.isRace(race.toLowerCase())) {return;}
        e.setCancelled(true);
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (settings.getDB(p.getName()) != null) {
            p.closeInventory();
            msgManager.message(p, "You are already part of a race!", MessageManager.MessageType.BAD);
            return;
        }
        p.closeInventory();
        settings.setDB(p.getName(), race.toLowerCase());
        msgManager.message(p, "You joined the " + race + " race!", MessageManager.MessageType.GOOD);
        p.addPotionEffects(settings.getEffects(race.toLowerCase()));
        return;
    }
}
