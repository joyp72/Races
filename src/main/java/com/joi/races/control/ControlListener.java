package com.joi.races.control;

import com.joi.races.Main;
import com.joi.races.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ControlListener implements Listener {

    private static ControlListener instance;
    private Settings settings = Settings.get();
    private Data data = Data.get();
    private List<UUID> left = new ArrayList<UUID>();

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
        if (data.hasTimer(p.getUniqueId())) {
            data.stopTimer(p.getUniqueId());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                p.addPotionEffects(settings.getEffects(race));
                if (race.equalsIgnoreCase("dwarf") && !settings.getNight_Vision(p)) {
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                }
            }
        }.runTaskLater(Main.get(), 5L);
    }

    // @EventHandler
    // public void onPlayerInteract(PlayerInteractEvent event) {
    //     Player player = event.getPlayer();
    //     if (!settings.hasRace(player)) {
    //         return;
    //     }
    //     String race = (String) settings.getRace(player);
    //     if (!race.equalsIgnoreCase("dragonborne")) {
    //         return;
    //     }
    //     if (event.getHand() != EquipmentSlot.HAND || !player.isSneaking()) {
    //         return;
    //     }
    //     if (data.getFp(player) < 3) { // todo: get fp cost from config
    //         return;
    //     }
    //     new FireBreath(player);
    // }

    // @EventHandler
    // public void onEntityDamagebyEntity(EntityDamageByEntityEvent event) {
    //     if (event.getDamager() instanceof Player) {
    //         Player player = (Player) event.getDamager();
    //         if (!settings.hasRace(player)) {
    //             return;
    //         }
    //         // todo: get fp cost from config
    //         if (settings.getRace(player).equalsIgnoreCase("dragonborne") && player.isSneaking() && data.getFp(player) >= 3) {
    //             new FireBreath(player);
    //             if (event.getEntity() instanceof Damageable) {
    //                 ((Damageable) event.getEntity()).setFireTicks(40); // todo: get from config
    //             }
    //             return;
    //         }
    //         if (data.getFp(player) >= settings.getfpCap(player)) {
    //             return;
    //         }
    //         data.setFp(player, data.getFp(player) + 1);
    //         data.showFp(player);
    //     }
    // }

    // @EventHandler
    // public void onProjectileHit(ProjectileHitEvent event) {
    //     if (event.getEntity() == null) {
    //         return;
    //     }
    //     if (event.getHitEntity() == null || !(event.getHitEntity() instanceof Damageable)) {
    //         return;
    //     }
    //     if (event.getEntity().getShooter() instanceof Player) {
    //         Player player = (Player) event.getEntity().getShooter();
    //         if (!settings.hasRace(player)) {
    //             return;
    //         }
    //         if (data.getFp(player) >= settings.getfpCap(player)) {
    //             return;
    //         }
    //         data.setFp(player, data.getFp(player) + 1);
    //         data.showFp(player);
    //     }
    // }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!settings.hasRace(p)) {
            return;
        }
        String race = (String) settings.getRace(p);
        if (p.getActivePotionEffects() != null) {
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if (!settings.getEffectTypes(race).contains(effect.getType())) {
                    if (effect.getDuration() > 1000000 || effect.getDuration() == -1) {
                        p.removePotionEffect(effect.getType());
                    }
                }
            }
        }
        for (PotionEffect effect : settings.getEffects(race)) {
            if (effect.getType().equals(PotionEffectType.NIGHT_VISION) && race.equalsIgnoreCase("dwarf") && !settings.getNight_Vision(p)) {
                continue;
            }

            if (effect.getType().equals(PotionEffectType.ABSORPTION) && p.getAbsorptionAmount() > 0) {
                continue;
            }
            if (!p.hasPotionEffect(effect.getType())) {
                if (effect.getType().equals(PotionEffectType.ABSORPTION)
                    && left.contains(p.getUniqueId())) {
                        if (data.hasTimer(p.getUniqueId())) {
                            data.stopTimer(p.getUniqueId());
                        }
                        data.createTimer(p.getUniqueId(), 5 * 60);
                        data.startTimer(p.getUniqueId());
                        left.remove(p.getUniqueId());
                        continue;
                }
                p.addPotionEffect(effect);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (!settings.hasRace(p)) {
            return;
        }
        String race = (String) settings.getRace(p);
        if (!race.equalsIgnoreCase("oni")) {
            return;
        }
        if (data.hasTimer(p.getUniqueId())) {
            left.add(p.getUniqueId());
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

        if (race.equalsIgnoreCase("angel") && e.getCause() == DamageCause.FALL) {
            if (settings.getWings(p)) {
                e.setCancelled(true);
            }
            return;
        } 

        if (!race.equalsIgnoreCase("oni") || p.getAbsorptionAmount() == 0) {
            return;
        }
        if (p.getAbsorptionAmount() - e.getDamage() <= 0) {
            p.removePotionEffect(PotionEffectType.ABSORPTION);
            data.createTimer(p.getUniqueId(), 5 * 60);
            data.startTimer(p.getUniqueId());
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
        data.createTimer(p.getUniqueId(), 5 * 60);
        data.startTimer(p.getUniqueId());
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
                    data.createTimer(p.getUniqueId(), 5 * 60);
                    data.startTimer(p.getUniqueId());
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

