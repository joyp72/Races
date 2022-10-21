package com.joi.races.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import com.joi.races.Main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class Menus {

    private static Inventory selector;

    public  static Inventory getSelector() {
        return selector;
    }

    public Menus(Player p) {

        selector = Bukkit.createInventory(p, 45, "Pick a race:");

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
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Please pick your race wisely");
            lore.add(ChatColor.GRAY + "as you will only get one chance");
            lore.add(ChatColor.GRAY + "to change your race.");
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
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Luck");
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Hero of the Village");
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
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Slow fall");
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Health boost");
            meta.setLore(lore);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            angel.setItemMeta(meta);
            selector.setItem(20, angel);
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
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Conduit power");
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Dolphin's grace");
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
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Resistance");
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Fire resistance");
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
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Haste");
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Night Vision");
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
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Strength");
            lore.add(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + "Absorption");
            meta.setLore(lore);
            oni.setItemMeta(meta);
            selector.setItem(25, oni);
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
