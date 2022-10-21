package com.joi.races;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Settings {

    private static Settings instance;
    private Plugin plugin;
    private File dbFile;
    private FileConfiguration dbConfig;
    private File effectsFile;
    private FileConfiguration effectsConfig;
    private String[] races = {"human", "angel", "merrow", "dragonborne", "dwarf", "oni"};

    static {
        instance = new Settings();
    }

    public static Settings get() {
        return instance;
    }

    public void setup(Plugin p) {
        plugin = p;
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdirs();
        }
        dbFile = new File(p.getDataFolder() + "/db.yml");
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (Exception e) {
                p.getLogger().info("Failed to generate db file!");
                e.printStackTrace();
            }
        }
        dbConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(dbFile);

        effectsFile = new File(p.getDataFolder() + "/effects.yml");
        if (!effectsFile.exists()) {
            try {
                effectsFile.createNewFile();
            } catch (Exception e) {
                p.getLogger().info("Failed to generate effects file!");
                e.printStackTrace();
            }
            effectsConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(effectsFile);
            for (String s : races) {
                effectsConfig.set(s, addDefaults(s));
            }
            try {
                effectsConfig.save(effectsFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        effectsConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(effectsFile);
    }

    public void resetDB() {
        dbFile.delete();
        dbFile = new File(Main.get().getDataFolder() + "/db.yml");
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (Exception e) {
                Main.get().getLogger().info("Failed to generate db file!");
                e.printStackTrace();
            }
        }
        dbConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(dbFile);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDB(String path, Object value) {
        dbConfig.set(path, value);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getDB(String path) {
        return (T) dbConfig.get(path);
    }

    public List<PotionEffect> getEffects(String race) {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (String s : effectsConfig.getStringList(race)) {
            PotionEffectType effectType = PotionEffectType.getByName(s);
            if (effectType.equals(PotionEffectType.HEALTH_BOOST) || effectType.equals(PotionEffectType.ABSORPTION)) {
                PotionEffect e = new PotionEffect(effectType, Integer.MAX_VALUE, 1 , false, false);
                effects.add(e);
                continue;
            }
            PotionEffect e = new PotionEffect(effectType, Integer.MAX_VALUE, 0 , false, false);
            effects.add(e);
        }
        return effects;
    }

    public ConfigurationSection getConfigSectionDB() {
        return dbConfig.getConfigurationSection("db");
    }

    public ConfigurationSection createConfigurationDB(String path) {
        final ConfigurationSection s = dbConfig.createSection(path);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public boolean isRace(String race) {
        for (String s : races) {
            if (s.equals(race)) {return true;}
        }
        return false;
    }

    public List<String> addDefaults(String race) {
        List<String> effects = new ArrayList<String>();
        switch (race) {
            case "human":
                effects.add(PotionEffectType.LUCK.getName());
                effects.add(PotionEffectType.HERO_OF_THE_VILLAGE.getName());
                break;
            case "angel":
                effects.add(PotionEffectType.SLOW_FALLING.getName());
                effects.add(PotionEffectType.HEALTH_BOOST.getName());
                break;
            case "merrow":
                effects.add(PotionEffectType.CONDUIT_POWER.getName());
                effects.add(PotionEffectType.DOLPHINS_GRACE.getName());
                break;
            case "dragonborne":
                effects.add(PotionEffectType.DAMAGE_RESISTANCE.getName());
                effects.add(PotionEffectType.FIRE_RESISTANCE.getName());
                break;
            case "dwarf":
                effects.add(PotionEffectType.FAST_DIGGING.getName());
                effects.add(PotionEffectType.NIGHT_VISION.getName());
                break;
            case "oni":
                effects.add(PotionEffectType.INCREASE_DAMAGE.getName());
                effects.add(PotionEffectType.ABSORPTION.getName());
                break;
            default:
                break;
        }
        return effects;
    }

}
