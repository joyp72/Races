package com.joi.races.control;

import com.joi.races.Main;
import com.joi.races.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ControlListener implements Listener {

    private static ControlListener instance;
    private  Settings settings = Settings.get();
    private List<UUID> scheduledPlayers = new ArrayList<UUID>();

    static {
        instance = new ControlListener();
    }

    public static ControlListener get() {
        return instance;
    }

    public void setup() {
        Bukkit.getPluginManager().registerEvents(this, Main.get());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (settings.getDB(p.getName()) == null) {
            return;
        }
        String race = (String) settings.getDB(p.getName().toString());
        for (PotionEffect effect : settings.getEffects(race)) {
            if (!p.hasPotionEffect(effect.getType())) {
                if (effect.getType().equals(PotionEffectType.ABSORPTION)
                    && scheduledPlayers.contains(p.getUniqueId())) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!p.isOnline()) {
                                    return;
                                }
                                PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 1 , false, false);
                                p.addPotionEffect(effect);
                                scheduledPlayers.remove(p.getUniqueId());
                            }
                        }.runTaskLater(Main.get(), 20L * 5L);
                        continue;
                }
                p.addPotionEffect(effect);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        if (settings.getDB(p.getName()) == null) {
            return;
        }
        String race = (String) settings.getDB(p.getName().toString());

        if (!race.equalsIgnoreCase("oni") || !p.hasPotionEffect(PotionEffectType.ABSORPTION) || p.getAbsorptionAmount() == 0) {
            return;
        }
        if (p.getAbsorptionAmount() - e.getDamage() <= 0) {
            p.removePotionEffect(PotionEffectType.ABSORPTION);
            scheduledPlayers.add(p.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!p.isOnline()) {
                        return;
                    }
                    PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 1 , false, false);
                    p.addPotionEffect(effect);
                    scheduledPlayers.remove(p.getUniqueId());
                }
            }.runTaskLater(Main.get(), 20L * 5L * 60L);
        }
    }

    @EventHandler
    public void onPotionEffect(EntityPotionEffectEvent e) {
        if (e.getAction() == null) {
            return;
        }
        if (!e.getAction().equals(EntityPotionEffectEvent.Action.REMOVED)
            && !e.getAction().equals(EntityPotionEffectEvent.Action.CLEARED)) {
                return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        if (settings.getDB(p.getName()) == null) {
            return;
        }
        String race = (String) settings.getDB(p.getName().toString());
        for (PotionEffect effect : settings.getEffects(race)) {
            if (e.getOldEffect().getType().equals(effect.getType())) {
                e.getOldEffect().apply(p);
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.MILK_BUCKET) {
            return;
        }
        Player p = (Player) e.getPlayer();
        if (settings.getDB(p.getName()) == null) {
            return;
        }
        String race = (String) settings.getDB(p.getName().toString());
        for (PotionEffect effect : settings.getEffects(race)) {
            if (p.hasPotionEffect(effect.getType())) {
                PotionEffect ef = p.getPotionEffect(effect.getType());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ef.apply(p);
                    }
                }.runTaskLater(Main.get(), 5L);
            }
        }
    }
}

