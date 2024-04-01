package com.joi.races.control;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.joi.races.Main;

public class FocusPoints {

    private int fp;
    private int fpCap;
    private UUID id;
    OfflinePlayer player;

    public FocusPoints(UUID uuid, int fpCap) {
        id = uuid;
        player = Main.get().getServer().getOfflinePlayer(uuid);
        this.fpCap = fpCap;
        fp = fpCap;
    }

    public int getFp() {
        return fp;
    }

    public void setFp(int amount) {
        if (amount > fpCap) {
            fp = fpCap;
            return;
        }
        fp = amount;
    }

    public int getFpCap() {
        return fpCap;
    }

    public void setFpCap(int amount) {
        fpCap = amount;
        if (fp > fpCap) {
            fp = fpCap;
        }
    }

    public UUID getUUID() {
        return id;
    }
    
}
