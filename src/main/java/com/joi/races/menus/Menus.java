package com.joi.races.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerProfile;

import com.joi.races.Settings;
import com.joi.races.control.Timer;

import net.md_5.bungee.api.ChatColor;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Menus {

    private static HashMap<UUID, Inventory> selector_menus = new HashMap<UUID, Inventory>();
    private static HashMap<UUID, Inventory> init_menus = new HashMap<UUID, Inventory>();
    private Settings settings = Settings.get();

    public static HashMap<UUID, Inventory> getMenus() {
        return selector_menus;
    }

    public static HashMap<UUID, Inventory> getInit_Menus() {
        return init_menus;
    }

    public Menus(Player p) {

        Inventory selector;
        Inventory menu;

        if (selector_menus.get(p.getUniqueId()) != null) {
            selector = selector_menus.get(p.getUniqueId());
        } else {
            selector = Bukkit.createInventory(p, 45, "Pick a race:");
        }

        if (init_menus.get(p.getUniqueId()) != null) {
            menu = init_menus.get(p.getUniqueId());
        } else {
            menu = Bukkit.createInventory(p, 9, "Races");
        }
        
        selector_menus.put(p.getUniqueId(), selector);
        init_menus.put(p.getUniqueId(), menu);

        ItemStack menu_info = new ItemStack(Material.ENDER_EYE, 1);
            {
                ItemMeta meta = menu_info.getItemMeta();
                meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Pick your race!");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(" ");
                lore.add(ChatColor.GRAY + "Click to choose");
                lore.add(ChatColor.GRAY + "your race.");
                meta.setLore(lore);
                menu_info.setItemMeta(meta);
                menu.setItem(3, menu_info);
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
                menu.setItem(5, info2);
                selector.setItem(6, info2);
            }

        ItemStack info = new ItemStack(Material.ENDER_EYE, 1);
        {
            ItemMeta meta = info.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Pick your race below!");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Hover over the races");
            lore.add(ChatColor.GRAY + "to see more details.");
            meta.setLore(lore);
            info.setItemMeta(meta);
            selector.setItem(2, info);
        }

        for (String race : settings.getRaces()) {
            race = race.substring(0,1).toUpperCase() + race.substring(1).toLowerCase();
            ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);
            {
                SkullMeta meta = (SkullMeta) stack.getItemMeta();
                if (settings.getSkinURL(race.toLowerCase()) != null) {
                    UUID uuid = UUID.randomUUID();
                    URL url = settings.getSkinURL(race.toLowerCase());
                    PlayerProfile profile = p.getServer().createPlayerProfile(uuid);
                    profile.getTextures().setSkin(url);
                    meta.setOwnerProfile(profile);
                }
                ChatColor color = ChatColor.RESET;
                if (settings.getRaceColor(race.toLowerCase()) != null) {
                    color = settings.getRaceColor(race.toLowerCase());
                }
                meta.setDisplayName(color + "" + ChatColor.BOLD + race);
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Click to join race!");
                lore.add(" ");
                lore.add(ChatColor.GRAY + "Bonuses: ");
                for (PotionEffect effect : settings.getEffects(race.toLowerCase())) {
                    String effectName = effect.getType().getName();
                    String firstChar = effectName.substring(0, 1);
                    effectName = effectName.substring(1);
                    effectName = effectName.toLowerCase();
                    effectName = firstChar + effectName;
                    effectName = effectName.replace("_", " ");
                    lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + effectName);
                }
                meta.setLore(lore);
                meta.setUnbreakable(true);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                stack.setItemMeta(meta);
                selector.setItem(settings.getGuiIndex(race.toLowerCase()), stack);
            }
        }

        if (settings.hasRace(p)) {
            if (settings.getRace(p).equalsIgnoreCase("angel")) {
                ItemStack wings = new ItemStack(Material.ELYTRA, 1);
                {
                    ItemMeta meta = wings.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Wings");
                    ArrayList<String> lore = new ArrayList<>();
                    if (settings.getWings(p)) {
                        lore.add(ChatColor.GRAY + "(Click to deactivate)");
                    } else {
                        lore.add(ChatColor.GRAY + "(Click to activate)");
                    }
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Toggle slow falling");
                    meta.addItemFlags(ItemFlag.values());
                    meta.setLore(lore);
                    wings.setItemMeta(meta);
                    if (settings.getWings(p)) {
                        wings.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
                    }
                    menu.setItem(7, wings);
                }
            } else if (settings.getRace(p).equalsIgnoreCase("oni")) {
                ItemStack timer = new ItemStack(Material.GOLDEN_APPLE, 1);
                {
                    ItemMeta meta = timer.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Absorption");
                    ArrayList<String> lore = new ArrayList<>();
                    if (Timer.get().hasTimer(p.getUniqueId())) {
                        lore.add(ChatColor.GRAY + "(On cooldown)");
                    } else {
                        lore.add(ChatColor.GRAY + "(Not on cooldown)");
                    }
                    lore.add(" ");
                    lore.add(ChatColor.WHITE + "Time remaining:");
                    if (Timer.get().hasTimer(p.getUniqueId()) && Timer.get().getTime(p.getUniqueId()) != -1) {
                        Duration dur = Duration.ofSeconds(Timer.get().getTime(p.getUniqueId()));
                        String m = "" + (int)dur.toMinutes();
                        String durS = "" + dur;
                        String s1 = "0", s0 = "";
                        if (durS.length() == 7 || durS.length() == 5) {
                            s1 = durS.substring(durS.indexOf('S') - 2, durS.indexOf('S') - 1); 
                        }
                        s0 = durS.substring(durS.indexOf('S') - 1, durS.indexOf('S'));
                        lore.add(ChatColor.GRAY + m + "m " + s1 + s0 + "s");
                    } else {
                        lore.add(ChatColor.GRAY + "--:--");
                    }
                    meta.addItemFlags(ItemFlag.values());
                    meta.setLore(lore);
                    timer.setItemMeta(meta);
                    if (!Timer.get().hasTimer(p.getUniqueId())) {
                        timer.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
                    }
                    menu.setItem(7, timer);
                }
            } else if (settings.getRace(p).equalsIgnoreCase("dwarf")) {
                ItemStack potion = new ItemStack(Material.POTION, 1);
                {
                    PotionMeta meta = (PotionMeta) potion.getItemMeta();
                    meta.setBasePotionData(new PotionData(PotionType.NIGHT_VISION));
                    meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Night Vision");
                    ArrayList<String> lore = new ArrayList<>();
                    if (settings.getNight_Vision(p)) {
                        lore.add(ChatColor.GRAY + "(Click to deactivate)");
                    } else {
                        lore.add(ChatColor.GRAY + "(Click to activate)");
                    }
                    lore.add(" ");
                    lore.add(ChatColor.GRAY + "Toggle night vision");
                    meta.addItemFlags(ItemFlag.values());
                    meta.setLore(lore);
                    potion.setItemMeta(meta);
                    if (settings.getNight_Vision(p)) {
                        potion.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
                    }
                    menu.setItem(7, potion);
                }
            } else {
                menu.clear(7);
            }
        }

        ItemStack exit = new ItemStack(Material.BOOK, 1);
        {
            ItemMeta meta = exit.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Exit");
            exit.setItemMeta(meta);
            selector.setItem(40, exit);
        }

        while (menu.firstEmpty() >= 0) {
            ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
            {
                ItemMeta meta = pane.getItemMeta();
                meta.setDisplayName(" ");
                pane.setItemMeta(meta);
                menu.setItem(menu.firstEmpty(), pane);
            }
        }

        while (selector.firstEmpty() >= 0) {
            ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
            {
                ItemMeta meta = pane.getItemMeta();
                meta.setDisplayName(" ");
                pane.setItemMeta(meta);
                selector.setItem(selector.firstEmpty(), pane);
            }
        }
    }
}
