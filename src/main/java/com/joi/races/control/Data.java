package com.joi.races.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.joi.races.Settings;

import net.md_5.bungee.api.chat.TextComponent;

public class Data {

    private static Data instance;
    private List<FocusPoints> fpList;
    private List<AbsorptionCountdown> AbCooldownList;
    private Settings settings = Settings.get();

    static {
        instance = new Data();
    }

    public static Data get() {
        return instance;
    }

    private Data() {
        fpList = Collections.synchronizedList(new ArrayList<FocusPoints>());
        AbCooldownList = Collections.synchronizedList(new ArrayList<AbsorptionCountdown>());
    }

    public int getFp(UUID id) {
        for (FocusPoints fp : fpList) {
            if (fp.getUUID().equals(id)) {
                return fp.getFp();
            }
        }
        return settings.getfpCap(id);
    }

    public int getFp(Player player) {
        for (FocusPoints fp : fpList) {
            if (fp.getUUID().equals(player.getUniqueId())) {
                return fp.getFp();
            }
        }
        return settings.getfpCap(player);
    }

    public void setFpCap(UUID id, int amount) {
        for (FocusPoints fp : fpList) {
            if (fp.getUUID().equals(id)) {
                fp.setFpCap(amount);
                return;
            }
        }
    }

    public void setFp(Player player, int amount) {
        for (FocusPoints fp : fpList) {
            if (fp.getUUID().equals(player.getUniqueId())) {
                fp.setFp(amount);
                return;
            }
        }
        FocusPoints fp = new FocusPoints(player.getUniqueId(), settings.getfpCap(player));
        fp.setFp(amount);
        fpList.add(fp);
    }

    // remove
    public void showFp(Player player) {
        String msg = "§3" + "█".repeat(getFp(player)) + "§7" + "-".repeat(settings.getfpCap(player) - getFp(player));
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }

    public void createTimer(UUID id, int time) {
        AbCooldownList.add(new AbsorptionCountdown(id, time));
    }

    public void startTimer(UUID id) {
        for (AbsorptionCountdown cd : AbCooldownList) {
            if (cd.getUUID().equals(id)) {
                cd.run();
            }
        }
    }

    public void stopTimer(UUID id) {
        List<AbsorptionCountdown> clone = new ArrayList<AbsorptionCountdown>();
        for (AbsorptionCountdown c : AbCooldownList) {
            clone.add(c);
        }
        for (AbsorptionCountdown c : clone) {
            if (c.getUUID().equals(id)) {
                AbCooldownList.remove(c);
            }
        }
    }

    public boolean hasTimer(UUID id) {
        for (AbsorptionCountdown c : AbCooldownList) {
            if (c.getUUID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public int getTime(UUID id) {
        for (AbsorptionCountdown c : AbCooldownList) {
            if (c.getUUID().equals(id)) {
                return c.getTimer();
            }
        }
        return -1;
    }

    public void onTimerEnd(AbsorptionCountdown c) {
        if (AbCooldownList.contains(c)) {
            AbCooldownList.remove(c);
        }
    }

    public void updateCd(AbsorptionCountdown o, AbsorptionCountdown n) {
		if (AbCooldownList.contains(o)) {
			AbCooldownList.remove(o);
			AbCooldownList.add(n);
		}
	}
    
}
