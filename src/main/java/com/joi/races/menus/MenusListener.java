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
import org.bukkit.potion.PotionEffectType;

public class MenusListener implements Listener {

    private static MenusListener instance;
    private Settings settings = Settings.get();
    private MessageManager msgManager = MessageManager.get();
    private String[] displayNames = {"Pick your race!", "Pick your race below!", "Human", "Angel", "Merrow",
                                     "Dragonborne", "Dwarf", "Oni", "Exit", "DISCLAIMER!", " ", "Wings", "Absorption",
                                     "Night Vision"};

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
        if (settings.isRace(displayName.toLowerCase())) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        // If inventory doesn't belong to Races
        if (!e.getView().getTitle().equalsIgnoreCase("Pick a race:") &&
            !e.getView().getTitle().equalsIgnoreCase("Races")) {
            return;
        }
        // If item doesn't exist
        if (e.getCurrentItem() == null) {
            return;
        }
        ItemStack item = e.getCurrentItem();
        // If item is part of menu
        if (containsDisplayName(ChatColor.stripColor(item.getItemMeta().getDisplayName()))) {
            e.setCancelled(true);
        } else {
            return;
        }
        // If player exists
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        // If item is a player head
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
        // If item is named "Pick your race here!"
        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Pick your race!")) {
                p.closeInventory();
                new Menus(p);
                p.openInventory(Menus.getMenus().get(p.getUniqueId()));
                return;
        }
        // If item is named "exit"
        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("exit")) {
                p.closeInventory();
                return;
        }
        // If item is named "wings"
        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("wings")) {
            if (!settings.hasRace(p)) {
                msgManager.message(p, "You are not part of the Angel race.", MessageType.BAD);
                p.closeInventory();
                return;
            }
            if (!settings.getRace(p).equalsIgnoreCase("angel")) {
                msgManager.message(p, "You are not part of the Angel race.", MessageType.BAD);
                p.closeInventory();
                return;
            }
            boolean wings = settings.getWings(p);
            if (!wings) {
                settings.setWings(p, true);
                msgManager.message(p, "Fall damage toggled on.", MessageType.GOOD);
                p.closeInventory();
                return;
            } else {
                settings.setWings(p, false);
                msgManager.message(p, "Fall damage toggled off.", MessageType.GOOD);
                p.closeInventory();
                return;
            }
        }
        // If item is named "night vision"
        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Night Vision")) {
            if (!settings.hasRace(p)) {
                msgManager.message(p, "You are not part of the Dwarf race.", MessageType.BAD);
                p.closeInventory();
                return;
            }
            if (!settings.getRace(p).equalsIgnoreCase("dwarf")) {
                msgManager.message(p, "You are not part of the Dwarf race.", MessageType.BAD);
                p.closeInventory();
                return;
            }
            boolean vision = settings.getNight_Vision(p);
            if (!vision) {
                settings.setNight_Vision(p, true);
                msgManager.message(p, "Night Vision toggled on.", MessageType.GOOD);
                if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    p.closeInventory();
                    return;
                }
                PotionEffect effect = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0 , false, false);
                p.addPotionEffect(effect);
                p.closeInventory();
                return;
            } else {
                settings.setNight_Vision(p, false);
                msgManager.message(p, "Night Vision toggled off.", MessageType.GOOD);
                if (!p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    p.closeInventory();
                    return;
                }
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                p.closeInventory();
                return;
            }
        }
    }
}
