package com.joi.races.menus;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.MessageManager;
import com.joi.races.control.MessageManager.MessageType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class MenusListener implements Listener {

    private static MenusListener instance;
    private Settings settings = Settings.get();
    private MessageManager msgManager = MessageManager.get();
    private String[] displayNames = {"Pick your race below!", "Human", "Angel", "Merrow",
                                     "Dragonborne", "Dwarf", "Oni", "Exit", "DISCLAIMER!", " "};

    static {
        instance = new MenusListener();
    }

    public static MenusListener get() {
        return instance;
    }

    public void setup() {
        Bukkit.getPluginManager().registerEvents(this, Main.get());
    }

    public boolean containsDisplayName(String displayName) {
        for (String s : displayNames) {
            if (s.equals(displayName)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (Menus.getMenus() == null) {
            return;
        }
        if (Menus.getMenus().isEmpty()) {
            return;
        }
        if (!e.getView().getTitle().equalsIgnoreCase("Pick a race:")) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        ItemStack item = e.getCurrentItem();
        if (containsDisplayName(ChatColor.stripColor(item.getItemMeta().getDisplayName()))) {
            e.setCancelled(true);
        } else {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        if (item.getType() == Material.PLAYER_HEAD) {
            String race = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            if (!settings.isRace(race.toLowerCase())) {
                return;
            }
            if (settings.hasRace(p)) {
                if (settings.getRace(p).equalsIgnoreCase(race)) {
                    p.closeInventory();
                    msgManager.message(p, "You are already part of this race!", MessageType.BAD);
                    return;
                }
                if (settings.getChangeTokens(p) > 0) {
                    String prevRace = settings.getRace(p);
                    for (PotionEffect effect : settings.getEffects(prevRace)) {
                        if (p.hasPotionEffect(effect.getType())) {
                            p.removePotionEffect(effect.getType());
                        }
                    }
                    p.closeInventory();
                    settings.setRace(p, race.toLowerCase());
                    settings.setChangeTokens(p, settings.getChangeTokens(p) - 1);
                    msgManager.message(p, "You changed to the " + race + " race!", MessageManager.MessageType.GOOD);
                    p.addPotionEffects(settings.getEffects(race.toLowerCase()));
                    return;

                }
                p.closeInventory();
                msgManager.message(p, "You don't have enough tokens to change races!", MessageManager.MessageType.BAD);
                return;
            }
            p.closeInventory();
            settings.setRace(p, race.toLowerCase());
            msgManager.message(p, "You joined the " + race + " race!", MessageManager.MessageType.GOOD);
            p.addPotionEffects(settings.getEffects(race.toLowerCase()));
            return;
        }
        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("exit")) {
                p.closeInventory();
                return;
        }
    }
}
