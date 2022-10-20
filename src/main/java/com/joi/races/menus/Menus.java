package com.joi.races.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Menus {

    private static Inventory selector;

    public  static Inventory getSelector() {
        return selector;
    }

    public Menus(Player p) {

        selector = Bukkit.createInventory(p, 18, "Pick a race:");

        ItemStack human = new ItemStack(Material.PLAYER_HEAD, 1);
        {
            ItemMeta meta = human.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Human");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Join race");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Attributes: ");
            lore.add(ChatColor.GREEN + "Luck");
            lore.add(ChatColor.GREEN + "Hero of the Village");
            meta.setLore(lore);
            human.setItemMeta(meta);
            selector.setItem(10, human);
        }

        ItemStack angel = new ItemStack(Material.ELYTRA, 1);
        {
            ItemMeta meta = angel.getItemMeta();
            meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Angel");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Join race");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Attributes: ");
            lore.add(ChatColor.WHITE + "Slow fall");
            lore.add(ChatColor.WHITE + "Health boost");
            meta.setLore(lore);
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            angel.setItemMeta(meta);
            selector.setItem(12, angel);
        }

        ItemStack merrow = new ItemStack(Material.COD, 1);
        {
            ItemMeta meta = merrow.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Merrow");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Join race");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Attributes: ");
            lore.add(ChatColor.AQUA + "Conduit power");
            lore.add(ChatColor.AQUA + "Dolphin's grace");
            meta.setLore(lore);
            merrow.setItemMeta(meta);
            selector.setItem(14, merrow);
        }

        ItemStack dragonborne = new ItemStack(Material.BLAZE_POWDER, 1);
        {
            ItemMeta meta = dragonborne.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Dragonborne");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Join race");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Attributes: ");
            lore.add(ChatColor.RED + "Resistance");
            lore.add(ChatColor.RED + "Fire resistance");
            meta.setLore(lore);
            dragonborne.setItemMeta(meta);
            selector.setItem(16, dragonborne);
        }

        ItemStack dwarf = new ItemStack(Material.TOTEM_OF_UNDYING, 1);
        {
            ItemMeta meta = dwarf.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Dwarf");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Join race");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Attributes: ");
            lore.add(ChatColor.YELLOW + "Haste");
            lore.add(ChatColor.YELLOW + "Night Vision");
            meta.setLore(lore);
            dwarf.setItemMeta(meta);
            selector.setItem(2, dwarf);
        }

        ItemStack oni = new ItemStack(Material.ENDER_EYE, 1);
        {
            ItemMeta meta = oni.getItemMeta();
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Oni");
            ArrayList<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Join race");
            lore.add(" ");
            lore.add(ChatColor.GRAY + "Attributes: ");
            lore.add(ChatColor.LIGHT_PURPLE + "Strength");
            lore.add(ChatColor.LIGHT_PURPLE + "Absorption");
            meta.setLore(lore);
            oni.setItemMeta(meta);
            selector.setItem(6, oni);
        }
    }

    public static String[] formatLore(String text, int size, org.bukkit.ChatColor color) {
        List<String> ret = new ArrayList<String>();

        if (text == null || text.length() == 0)
            return new String[ret.size()];

        String[] words = text.split(" ");
        String rebuild = "";

        for (int i = 0; i < words.length; i++) {
            int wordLen = words[i].length();
            if (rebuild.length() + wordLen > 40 || words[i].contains("\n")
                    || words[i].equals(Character.LINE_SEPARATOR)) {

                ret.add(color + rebuild);
                rebuild = "";
                if (words[i].equalsIgnoreCase("\n")) {
                    words[i] = "";
                    continue;
                }

            }
            rebuild = rebuild + " " + words[i];

        }
        if (!rebuild.equalsIgnoreCase(""))
            ret.add(color + rebuild);

        String[] val = new String[ret.size()];
        for (int i = 0; i < ret.size(); i++)
            val[i] = ret.get(i);

        return val;
    }
}
