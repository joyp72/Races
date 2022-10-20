package com.joi.races.control;

import com.joi.races.Main;
import com.joi.races.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ControlListener implements Listener {

    private static ControlListener instance;
    private  Settings settings = Settings.get();

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
        if (settings.getDB(p.getName()) == null) {return;}
        String race = (String) settings.getDB(p.getName().toString());
        for (PotionEffect effect : settings.getEffects(race)) {
            if (!p.hasPotionEffect(effect.getType())) {
                p.addPotionEffect(effect);
            }
        }
    }

    @EventHandler
    public void onClick(EntityPotionEffectEvent e) {
        if (e.getAction() == null) {return;}
        if (!e.getAction().equals(EntityPotionEffectEvent.Action.REMOVED)
            && !e.getAction().equals(EntityPotionEffectEvent.Action.CLEARED)) {return;}
        if (!(e.getEntity() instanceof Player)) {return;}
        Player p = (Player) e.getEntity();
        if (settings.getDB(p.getName()) == null) {return;}
        String race = (String) settings.getDB(p.getName().toString());
        for (PotionEffect effect : settings.getEffects(race)) {
            if (e.getOldEffect().getType().equals(PotionEffectType.ABSORPTION) || e.getOldEffect().getType().equals(PotionEffectType.NIGHT_VISION)) {
                PotionEffect ef = new PotionEffect(e.getOldEffect().getType(), 20 * 5 * 60, 1 , false, false);
                p.addPotionEffect(ef);
            }
            if (e.getOldEffect().getType().equals(effect.getType())) {
                e.getOldEffect().apply(p);
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() != Material.MILK_BUCKET) {return;}
        Player p = (Player) e.getPlayer();
        if (settings.getDB(p.getName()) == null) {return;}
        String race = (String) settings.getDB(p.getName().toString());
        for (PotionEffect effect : settings.getEffects(race)) {
            if (p.hasPotionEffect(effect.getType())) {
                if (effect.getType().equals(PotionEffectType.ABSORPTION) || effect.getType().equals(PotionEffectType.NIGHT_VISION)) {
                    int dr = p.getPotionEffect(effect.getType()).getDuration();
                    PotionEffect ef = new PotionEffect(effect.getType(), dr, 1 , false, false);
                    new BukkitRunnable() {
                        @Override
                        public void run() {p.addPotionEffect(ef);
                        }
                    }.runTaskLater(Main.get(), 5L);
                    continue;
                }
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

