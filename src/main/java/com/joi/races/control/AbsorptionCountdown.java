package com.joi.races.control;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.joi.races.Main;
import com.joi.races.Settings;

public class AbsorptionCountdown extends BukkitRunnable {

    private int time;
    private UUID id;
    OfflinePlayer player;
    Timer timer = Timer.get();

    public AbsorptionCountdown(UUID uuid, int t) {
        time = t;
        id = uuid;
        player = Main.get().getServer().getOfflinePlayer(uuid);
    }

    public void onEnd() {
        if (!player.isOnline()) {
            return;
        }
        Player p = player.getPlayer();
        if (!Settings.get().getRace(p).equalsIgnoreCase("oni")) {
            return;
        }
        if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) {
            return;
        }
        PotionEffect effect = new PotionEffect(PotionEffectType.ABSORPTION, -1, 1 , false, false);
        p.addPotionEffect(effect);
     }

    @Override
    public void run() {
        if (!player.isOnline()) {
            timer.onTimerEnd(this);
            this.cancel();
            return;
        }
        if (!timer.hasTimer(id)) {
            this.cancel();
            return;
        }
        if (time <= 0) {
            timer.onTimerEnd(this);
            onEnd();
            this.cancel();
            return;
        }
        --time;
        AbsorptionCountdown c = new AbsorptionCountdown(id, time);
        timer.updateCd(this, c);
        c.runTaskLater(Main.get(), 20L);
    }

    public UUID getUUID() {
        return id;
    }

    public int getTimer() {
        return time;
    }
}
