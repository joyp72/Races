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
import org.bukkit.event.player.PlayerRespawnEvent;
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
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (!settings.hasRace(p)) {
            return;
        }
        String race = settings.getRace(p);
        if (scheduledPlayers.contains(p.getUniqueId())) {
            scheduledPlayers.remove(p.getUniqueId());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                p.addPotionEffects(settings.getEffects(race));
                if (race.equalsIgnoreCase("angel") && !settings.getWings(p)) {
                    p.removePotionEffect(PotionEffectType.SLOW_FALLING);
                }
            }
        }.runTaskLater(Main.get(), 5L);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!settings.hasRace(p)) {
            return;
        }
        String race = (String) settings.getRace(p);
        for (PotionEffect effect : settings.getEffects(race)) {
            if (effect.getType().equals(PotionEffectType.SLOW_FALLING) && race.equalsIgnoreCase("angel") && !settings.getWings(p)) {
                continue;
            }
            if (effect.getType().equals(PotionEffectType.ABSORPTION) && p.getAbsorptionAmount() > 0) {
                continue;
            }
            if (!p.hasPotionEffect(effect.getType())) {
                if (effect.getType().equals(PotionEffectType.ABSORPTION)
                    && scheduledPlayers.contains(p.getUniqueId())) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!p.isOnline() || !scheduledPlayers.contains(p.getUniqueId())) {
                                    return;
                                }
                                if (!settings.getRace(p).equalsIgnoreCase("oni")) {
                                    scheduledPlayers.remove(p.getUniqueId());
                                    return;
                                }
                                PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 1 , false, false);
                                p.addPotionEffect(effect);
                                scheduledPlayers.remove(p.getUniqueId());
                            }
                        }.runTaskLater(Main.get(), 20L * 5L * 60L);
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
        if (!settings.hasRace(p)) {
            return;
        }
        String race = (String) settings.getRace(p);

        if (!race.equalsIgnoreCase("oni") || p.getAbsorptionAmount() == 0) {
            return;
        }
        if (p.getAbsorptionAmount() - e.getDamage() <= 0) {
            p.removePotionEffect(PotionEffectType.ABSORPTION);
            scheduledPlayers.add(p.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!p.isOnline() || !scheduledPlayers.contains(p.getUniqueId())) {
                        return;
                    }
                    if (!settings.getRace(p).equalsIgnoreCase("oni")) {
                        scheduledPlayers.remove(p.getUniqueId());
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
            && !e.getAction().equals(EntityPotionEffectEvent.Action.CLEARED)
            && !e.getAction().equals(EntityPotionEffectEvent.Action.CHANGED)) {
                return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        if (!settings.hasRace(p)) {
            return;
        }
        if (!settings.getRace(p).equalsIgnoreCase("oni")
            || !e.getOldEffect().getType().equals(PotionEffectType.ABSORPTION)
            || e.getOldEffect().getAmplifier() <= 1) {
            return;
        }
        if (e.getCause().equals(EntityPotionEffectEvent.Cause.MILK)) {
            return;
        }
        scheduledPlayers.add(p.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!p.isOnline() || !scheduledPlayers.contains(p.getUniqueId())) {
                        return;
                    }
                    if (!settings.getRace(p).equalsIgnoreCase("oni")) {
                        scheduledPlayers.remove(p.getUniqueId());
                        return;
                    }
                    PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 1 , false, false);
                    p.addPotionEffect(effect);
                    scheduledPlayers.remove(p.getUniqueId());
                }
            }.runTaskLater(Main.get(), 20L * 5L * 60L);
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if (e.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) {
            if (!settings.hasRace(p)) {
                return;
            }
            if (!p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                return;
            }
            p.removePotionEffect(PotionEffectType.ABSORPTION);
        }
        if (e.getItem().getType() != Material.MILK_BUCKET) {
            return;
        }
        if (!settings.hasRace(p)) {
            return;
        }
        String race = (String) settings.getRace(p);
        for (PotionEffect effect : settings.getEffects(race)) {
            if (p.hasPotionEffect(effect.getType())) {
                PotionEffect ef = p.getPotionEffect(effect.getType());
                if (ef.getType().equals(PotionEffectType.ABSORPTION) && ef.getAmplifier() == 1) {
                    double hearts = p.getAbsorptionAmount();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.setAbsorptionAmount(hearts);
                        }
                    }.runTaskLater(Main.get(), 5L);
                    continue;
                }
                if (ef.getType().equals(PotionEffectType.ABSORPTION) && ef.getAmplifier() > 1) {
                    scheduledPlayers.add(p.getUniqueId());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!p.isOnline() || !scheduledPlayers.contains(p.getUniqueId())) {
                                return;
                            }
                            if (!settings.getRace(p).equalsIgnoreCase("oni")) {
                                scheduledPlayers.remove(p.getUniqueId());
                                return;
                            }
                            PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 1 , false, false);
                            p.addPotionEffect(effect);
                            scheduledPlayers.remove(p.getUniqueId());
                        }
                    }.runTaskLater(Main.get(), 20L * 5L * 60L);
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            p.addPotionEffect(effect);
                        }
                    }.runTaskLater(Main.get(), 5L);
                }
            }
        }
    }
}

