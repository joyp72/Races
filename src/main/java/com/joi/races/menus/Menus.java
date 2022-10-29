package com.joi.races.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.profile.PlayerProfile;

import com.joi.races.Main;
import com.joi.races.Settings;
import com.joi.races.control.Timer;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Menus {

    private static HashMap<UUID, Inventory> menus = new HashMap<UUID, Inventory>();
    private Settings settings = Settings.get();

    public static HashMap<UUID, Inventory> getMenus() {
        return menus;
    }

    public Menus(Player p) {

        Inventory selector;

        if (menus.get(p.getUniqueId()) != null) {
            selector = menus.get(p.getUniqueId());
        } else {
            selector = Bukkit.createInventory(p, 45, "Pick a race:");
        }

        menus.put(p.getUniqueId(), selector);

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

        ItemStack info2 = new ItemStack(Material.NETHER_STAR, 1);
        {
            ItemMeta meta = info2.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "DISCLAIMER!");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Please pick your race wisely");
            lore.add(ChatColor.GRAY + "as you will only get a number of");
            lore.add(ChatColor.GRAY + "chances to change your race.");
            lore.add(" ");
            int amount = settings.getChangeTokens(p);
            String s = "You have " + ChatColor.RED + amount + ChatColor.GRAY + " Race Change token";
            if (amount < 1 || amount > 1) {
                s = s + "s";
            }
            s = s + ".";
            lore.add(ChatColor.GRAY + s);
            meta.setLore(lore);
            info2.setItemMeta(meta);
            selector.setItem(6, info2);
        }

        ItemStack human = new ItemStack(Material.PLAYER_HEAD, 1);
        {
            // ItemMeta meta = human.getItemMeta();
            SkullMeta meta = (SkullMeta) human.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Human");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to join race!");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Bonuses: ");
            for (PotionEffect effect : settings.getEffects("human")) {
                String effectName = effect.getType().getName();
                String firstChar = effectName.substring(0, 1);
                effectName = effectName.substring(1);
                effectName = effectName.toLowerCase();
                effectName = firstChar + effectName;
                effectName = effectName.replace("_", " ");
                lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + effectName);
            }
            meta.setLore(lore);
            human.setItemMeta(meta);
            selector.setItem(19, human);
        }

        ItemStack angel = new ItemStack(Material.PLAYER_HEAD, 1);
        {
            SkullMeta meta = (SkullMeta) angel.getItemMeta();
            UUID uuid = UUID.randomUUID();
            URL url = null;
            try {
                url = new URL("https://textures.minecraft.net/texture/be6f6d3560a29a3ab0c28f8b72c0582c0a75790b93720a9c0586bf8a1e59595d");
            } catch (MalformedURLException e) {
                Main.get().getLogger().info("Failed to load skin for " + human.toString());
                e.printStackTrace();
            }
            PlayerProfile profile = p.getServer().createPlayerProfile(uuid);
            profile.getTextures().setSkin(url);
            meta.setOwnerProfile(profile);
            meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Angel");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to join race!");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Bonuses: ");
            for (PotionEffect effect : settings.getEffects("angel")) {
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
            angel.setItemMeta(meta);
            selector.setItem(20, angel);
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
                    lore.add(ChatColor.WHITE + "Desc:");
                    lore.add(ChatColor.GRAY + "Toggle slow falling");
                    meta.addItemFlags(ItemFlag.values());
                    meta.setLore(lore);
                    wings.setItemMeta(meta);
                    if (settings.getWings(p)) {
                        wings.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
                    }
                    selector.setItem(29, wings);
                }
            } else {
                selector.clear(29);
            }
        }

        ItemStack merrow = new ItemStack(Material.PLAYER_HEAD, 1);
        {
            SkullMeta meta = (SkullMeta) merrow.getItemMeta();
            UUID uuid = UUID.randomUUID();
            URL url = null;
            try {
                url = new URL("https://textures.minecraft.net/texture/76688cbe83fe65e2b3465725d54fbda280796d5fee54a71b8ffdaa04634e91b");
            } catch (MalformedURLException e) {
                Main.get().getLogger().info("Failed to load skin for " + human.toString());
                e.printStackTrace();
            }
            PlayerProfile profile = p.getServer().createPlayerProfile(uuid);
            profile.getTextures().setSkin(url);
            meta.setOwnerProfile(profile);
            meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Merrow");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to join race!");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Bonuses: ");
            for (PotionEffect effect : settings.getEffects("merrow")) {
                String effectName = effect.getType().getName();
                String firstChar = effectName.substring(0, 1);
                effectName = effectName.substring(1);
                effectName = effectName.toLowerCase();
                effectName = firstChar + effectName;
                effectName = effectName.replace("_", " ");
                lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + effectName);
            }
            meta.setLore(lore);
            merrow.setItemMeta(meta);
            selector.setItem(21, merrow);
        }

        ItemStack dragonborne = new ItemStack(Material.PLAYER_HEAD, 1);
        {
            SkullMeta meta = (SkullMeta) dragonborne.getItemMeta();
            UUID uuid = UUID.randomUUID();
            URL url = null;
            try {
                url = new URL("https://textures.minecraft.net/texture/c23b749e4f458448ea8f666483f9f917beab1d3caa9d411f909945c1af6ffd1d");
            } catch (MalformedURLException e) {
                Main.get().getLogger().info("Failed to load skin for " + human.toString());
                e.printStackTrace();
            }
            PlayerProfile profile = p.getServer().createPlayerProfile(uuid);
            profile.getTextures().setSkin(url);
            meta.setOwnerProfile(profile);
            meta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Dragonborne");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to join race!");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Bonuses: ");
            for (PotionEffect effect : settings.getEffects("dragonborne")) {
                String effectName = effect.getType().getName();
                String firstChar = effectName.substring(0, 1);
                effectName = effectName.substring(1);
                effectName = effectName.toLowerCase();
                effectName = firstChar + effectName;
                effectName = effectName.replace("_", " ");
                lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + effectName);
            }
            meta.setLore(lore);
            dragonborne.setItemMeta(meta);
            selector.setItem(23, dragonborne);
        }

        ItemStack dwarf = new ItemStack(Material.PLAYER_HEAD, 1);
        {
            SkullMeta meta = (SkullMeta) dwarf.getItemMeta();
            UUID uuid = UUID.randomUUID();
            URL url = null;
            try {
                url = new URL("https://textures.minecraft.net/texture/397c1732ca0ccd08c87705a84107927fbf59fab45f82a8840071189fb61cdb54");
            } catch (MalformedURLException e) {
                Main.get().getLogger().info("Failed to load skin for " + human.toString());
                e.printStackTrace();
            }
            PlayerProfile profile = p.getServer().createPlayerProfile(uuid);
            profile.getTextures().setSkin(url);
            meta.setOwnerProfile(profile);
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Dwarf");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to join race!");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Bonuses: ");
            for (PotionEffect effect : settings.getEffects("dwarf")) {
                String effectName = effect.getType().getName();
                String firstChar = effectName.substring(0, 1);
                effectName = effectName.substring(1);
                effectName = effectName.toLowerCase();
                effectName = firstChar + effectName;
                effectName = effectName.replace("_", " ");
                lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + effectName);
            }
            meta.setLore(lore);
            dwarf.setItemMeta(meta);
            selector.setItem(24, dwarf);
        }

        ItemStack oni = new ItemStack(Material.PLAYER_HEAD, 1);
        {
            SkullMeta meta = (SkullMeta) oni.getItemMeta();
            UUID uuid = UUID.randomUUID();
            URL url = null;
            try {
                url = new URL("https://textures.minecraft.net/texture/8aebecee607f0bb87b95185de589aa55c80955860b664bc52d4ee60f7de6d710");
            } catch (MalformedURLException e) {
                Main.get().getLogger().info("Failed to load skin for " + human.toString());
                e.printStackTrace();
            }
            PlayerProfile profile = p.getServer().createPlayerProfile(uuid);
            profile.getTextures().setSkin(url);
            meta.setOwnerProfile(profile);
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Oni");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to join race!");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Bonuses: ");
            for (PotionEffect effect : settings.getEffects("oni")) {
                String effectName = effect.getType().getName();
                String firstChar = effectName.substring(0, 1);
                effectName = effectName.substring(1);
                effectName = effectName.toLowerCase();
                effectName = firstChar + effectName;
                effectName = effectName.replace("_", " ");
                lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + effectName);
            }
            meta.setLore(lore);
            oni.setItemMeta(meta);
            selector.setItem(25, oni);
        }

        if (settings.hasRace(p)) {
            if (settings.getRace(p).equalsIgnoreCase("oni")) {
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
                    if (Timer.get().hasTimer(p.getUniqueId())) {
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
                    selector.setItem(34, timer);
                }
            } else {
                selector.clear(34);
            }
        }

        ItemStack exit = new ItemStack(Material.BOOK, 1);
        {
            ItemMeta meta = exit.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Exit");
            exit.setItemMeta(meta);
            selector.setItem(40, exit);
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
