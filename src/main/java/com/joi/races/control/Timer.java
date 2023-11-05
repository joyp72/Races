package com.joi.races.control;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Timer {

    private static Timer instance;
    private static List<AbsorptionCountdown> cd;

    static {
        instance = new Timer();
    }

    public static Timer get() {
        return instance;
    }

    private Timer() {
        cd = new ArrayList<AbsorptionCountdown>();
    }

    public Timer createTimer(UUID id, int time) {
        cd.add(new AbsorptionCountdown(id, time));
        return this;
    }

    public void startTimer(UUID id) {
        if (cd.isEmpty()) {
            return;
        }
        for (AbsorptionCountdown c : cd) {
            if (c.getUUID().equals(id)) {
                c.run();
            }
        }
    }

    public void stopTimer(UUID id) {
        if (cd.isEmpty()) {
            return;
        }
        List<AbsorptionCountdown> clone = new ArrayList<AbsorptionCountdown>();
        for (AbsorptionCountdown c : cd) {
            clone.add(c);
        }
        for (AbsorptionCountdown c : clone) {
            if (c.getUUID().equals(id)) {
                cd.remove(c);
            }
        }
    }

    public boolean hasTimer(UUID id) {
        if (cd.isEmpty()) {
            return false;
        }
        for (AbsorptionCountdown c : cd) {
            if (c.getUUID().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public int getTime(UUID id) {
        if (cd.isEmpty()) {
            return -1;
        }
        for (AbsorptionCountdown c : cd) {
            if (c.getUUID().equals(id)) {
                return c.getTimer();
            }
        }
        return -1;
    }

    public void onTimerEnd(AbsorptionCountdown c) {
        if (cd.contains(c)) {
            cd.remove(c);
        }
    }

    public void updateCd(AbsorptionCountdown o, AbsorptionCountdown n) {
		if (cd.contains(o)) {
			cd.remove(o);
			cd.add(n);
		}
	}
    
}
