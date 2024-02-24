package com.joi.races;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;

public class Settings {

    private static Settings instance;
    private Plugin plugin;
    private File dbFile;
    private FileConfiguration dbConfig;
    private File RacesFile;
    private FileConfiguration racesConfig;
    private String racePath = ".race";
    private String raceEffectsPath = ".effects";
    private String raceColorPath = ".color";
    private String raceSkinPath = ".skin";
    private String raceGuiIndexPath = ".GuiIndex";
    private String changeTokensPath = ".changeTokens";
    private String wingsPath = ".wings";
    private String visionPath = ".night_vision";
    private String[] defaultRaces = {"human", "angel", "merrow", "dragonborne", "dwarf", "oni"};

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

        RacesFile = new File(p.getDataFolder() + "/races.yml");
        racesConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(RacesFile);
        if (!RacesFile.exists()) {
            try {
                RacesFile.createNewFile();
            } catch (Exception e) {
                p.getLogger().info("Failed to generate effects file!");
                e.printStackTrace();
            }
        }
        if (racesConfig.getKeys(false) == null || racesConfig.getKeys(false).isEmpty()) {
            for (String s : defaultRaces) {
                racesConfig.set(s + raceColorPath, getDefaultColor(s));
            }
        }
        for (String s : racesConfig.getKeys(false)) {
            if (racesConfig.get(s + raceColorPath) == null) {
                racesConfig.set(s + raceColorPath, getDefaultColor(s));
            }
            if (racesConfig.get(s + raceSkinPath) == null) {
                racesConfig.set(s + raceSkinPath, getDefaultSkin(s));
            }
            if (racesConfig.get(s + raceGuiIndexPath) == null) {
                racesConfig.set(s + raceGuiIndexPath, getDefaultGuiIndex(s));
            }
            if (racesConfig.get(s + raceEffectsPath) == null) {
                racesConfig.set(s + raceEffectsPath, getDefaultEffects(s));
            }
        }
        try {
            racesConfig.save(RacesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        racesConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(RacesFile);
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

    public void setWings(Player p, boolean value) {
        dbConfig.set(p.getUniqueId().toString() + wingsPath, value);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWings(UUID id, boolean value) {
        dbConfig.set(id.toString() + wingsPath, value);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getWings(Player p) {
        if (!dbConfig.contains(p.getUniqueId() + wingsPath)) {
            setWings(p, true);
            return true;
        }
        return dbConfig.getBoolean(p.getUniqueId() + wingsPath);
    }

    public boolean getWings(UUID id) {
        if (!dbConfig.contains(id + wingsPath)) {
            setWings(id, true);
            return true;
        }
        return dbConfig.getBoolean(id + wingsPath);
    }

    public void setNight_Vision(Player p, boolean value) {
        dbConfig.set(p.getUniqueId().toString() + visionPath, value);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNight_Vision(UUID id, boolean value) {
        dbConfig.set(id.toString() + visionPath, value);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getNight_Vision(Player p) {
        if (!dbConfig.contains(p.getUniqueId() + visionPath)) {
            setWings(p, true);
            return true;
        }
        return dbConfig.getBoolean(p.getUniqueId() + visionPath);
    }

    public boolean getNight_Vision(UUID id) {
        if (!dbConfig.contains(id + visionPath)) {
            setWings(id, true);
            return true;
        }
        return dbConfig.getBoolean(id + visionPath);
    }

    public void setChangeTokens(Player p, int value) {
        dbConfig.set(p.getUniqueId().toString() + changeTokensPath, value);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setChangeTokens(UUID id, int value) {
        dbConfig.set(id.toString() + changeTokensPath, value);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getChangeTokens(Player p) {
        if (!dbConfig.contains(p.getUniqueId() + changeTokensPath)) {
            setChangeTokens(p, 1);
            return 1;
        }
        return dbConfig.getInt(p.getUniqueId() + changeTokensPath);
    }

    public int getChangeTokens(UUID id) {
        if (!dbConfig.contains(id + changeTokensPath)) {
            setChangeTokens(id, 1);
            return 1;
        }
        return dbConfig.getInt(id + changeTokensPath);
    }

    public void setRace(Player p, Object value) {
        dbConfig.set(p.getUniqueId().toString() + racePath, value);
        try {
            dbConfig.save(dbFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasRace(Player p) {
        return getRace(p) == null ? false : true;
    }

    public boolean hasRace(UUID id) {
        return getRace(id) == null ? false : true;
    }

    public String getRace(UUID id) {
        return dbConfig.getString(id.toString() + racePath);
    }

    public String getRace(Player p) {
        return dbConfig.getString(p.getUniqueId().toString() + racePath);
    }

    public List<PotionEffect> getEffects(String race) {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (String s : racesConfig.getStringList(race + raceEffectsPath)) {
            if (PotionEffectType.getByName(s) == null) {
                continue;
            }
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

    public List<PotionEffectType> getEffectTypes(String race) {
        List<PotionEffectType> effects = new ArrayList<PotionEffectType>();
        for (String s : racesConfig.getStringList(race + raceEffectsPath)) {
            if (PotionEffectType.getByName(s) == null) {
                continue;
            }
            PotionEffectType effectType = PotionEffectType.getByName(s);
            effects.add(effectType);
        }
        return effects;
    }

    public void removeEffectType(String race, PotionEffectType effectType) {
        if (!isRace(race)) {
            return;
        }
        if (!getEffectTypes(race).contains(effectType)) {
            return;
        }
        List<String> effectList = racesConfig.getStringList(race + raceEffectsPath);
        effectList.remove(effectType.getName());
        racesConfig.set(race + raceEffectsPath, effectList);
        try {
            racesConfig.save(RacesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Player p : Main.get().getServer().getOnlinePlayers()) {
            if (!hasRace(p)) {
                continue;
            }
            if (!getRace(p).equalsIgnoreCase(race)) {
                continue;
            }
            if (!p.hasPotionEffect(effectType)) {
                continue;
            }
            if (p.getPotionEffect(effectType).getDuration() < 1000000) {
                continue;
            }
            p.removePotionEffect(effectType);
        }
        return;
    }

    public void addEffectType(String race, PotionEffectType effectType) {
        if (!isRace(race)) {
            return;
        }
        if (getEffectTypes(race).contains(effectType)) {
            return;
        }
        List<String> effectList = racesConfig.getStringList(race + raceEffectsPath);
        effectList.add(effectType.getName());
        racesConfig.set(race + raceEffectsPath, effectList);
        try {
            racesConfig.save(RacesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Player p : Main.get().getServer().getOnlinePlayers()) {
            if (!hasRace(p)) {
                continue;
            }
            if (!getRace(p).equalsIgnoreCase(race)) {
                continue;
            }
            if (p.hasPotionEffect(effectType)) {
                p.removePotionEffect(effectType);
            }
            PotionEffect effect = new PotionEffect(effectType, Integer.MAX_VALUE, 0 , false, false);
            p.addPotionEffect(effect);
        }
        return;
    }

    public ArrayList<String> getRaces() {
        ArrayList<String> races = new ArrayList<String>();
        for (String s: racesConfig.getKeys(false)) {
            races.add(s);
        }
        return races;
    }

    public void newRace(String race) {
        if (isRace(race)) {
            return;
        }
        racesConfig.set(race + raceColorPath, getDefaultColor(race));
        racesConfig.set(race + raceSkinPath, getDefaultSkin(race));
        racesConfig.set(race + raceGuiIndexPath, getGuiNextIndex());
        racesConfig.set(race + raceEffectsPath, getDefaultEffects(race));
        try {
            racesConfig.save(RacesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeRace(String race) {
        race = race.toLowerCase();
        if (!isRace(race)) {
            return;
        }
        for (Player p : Main.get().getServer().getOnlinePlayers()) {
            if (!hasRace(p)) {
                continue;
            }
            if (!getRace(p).equalsIgnoreCase(race)) {
                continue;
            }
            for (PotionEffectType effectType : getEffectTypes(race)) {
                if (!p.hasPotionEffect(effectType)) {
                    continue;
                }
                if (p.getPotionEffect(effectType).getDuration() < 1000000) {
                    continue;
                }
                p.removePotionEffect(effectType);
            }
            setChangeTokens(p, getChangeTokens(p) + 1);
            dbConfig.set(p.getUniqueId().toString() + racePath, null);
            try {
                dbConfig.save(dbFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        racesConfig.set(race, null);
        try {
            racesConfig.save(RacesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRaceColor(String race, ChatColor color) {
        if (!isRace(race)) {
            return;
        }
        if (getRaceColor(race) != null) {
            if (getRaceColor(race).equals(color)) {
                return;
            }
        }
        racesConfig.set(race + raceColorPath, color.toString());
        try {
            racesConfig.save(RacesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChatColor getRaceColor(String race) {
        if (!isRace(race)) {
            return null;
        }
        if (racesConfig.getString(race.toLowerCase() + raceColorPath) == null) {
            return null;
        }
        return ChatColor.getByChar((racesConfig.getString(race.toLowerCase() + raceColorPath)).charAt(1));
    }

    public void setSkinURL(String race, URL url) {
        if (!isRace(race)) {
            return;
        }
        racesConfig.set(race + raceSkinPath, url.toString());
        try {
            racesConfig.save(RacesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public URL getSkinURL(String race) {
        URL url = null;
        if (!isRace(race)) {
            return null;
        }
        if (racesConfig.getString(race.toLowerCase() + raceSkinPath) == null) {
            return null;
        }
        try {
            url = new URL(racesConfig.getString(race.toLowerCase() + raceSkinPath));
        } catch (MalformedURLException e) {
            Main.get().getLogger().info("Failed to load skin for " + race + ".");
            e.printStackTrace();
        }
        return url;
    }

    public void setGuiIndex(String race, int index) {
        if (!isRace(race)) {
            return;
        }
        if (index < 0 || index > 44) {
            return;
        }
        racesConfig.set(race + raceGuiIndexPath, index);
        try {
            racesConfig.save(RacesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getGuiIndex(String race) {
        return racesConfig.getInt(race + raceGuiIndexPath);
    }

    public int getGuiNextIndex() {
        int index = 0;
        for (String race : racesConfig.getKeys(false)) {
            if (getGuiIndex(race) > index) {
                index = getGuiIndex(race) + 1;
                if (index > 44) {
                    return 0;
                }
            }
        }
        return index;
    }


    public boolean isRace(String race) {
        return getRaces().contains(race);
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

    public String getDefaultColor(String race) {
        String color = ChatColor.RESET.toString();
        switch (race) {
            case "human":
                color = ChatColor.DARK_AQUA.toString();
                break;
            case "angel":
                color = ChatColor.YELLOW.toString();
                break;
            case "merrow":
                color = ChatColor.BLUE.toString();
                break;
            case "dragonborne":
                color = ChatColor.DARK_PURPLE.toString();
                break;
            case "dwarf":
                color = ChatColor.GRAY.toString();
                break;
            case "oni":
                color = ChatColor.DARK_RED.toString();
                break;
            default:
                break;
        }
        return color;
    }

    public List<String> getDefaultEffects(String race) {
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
                effects.add(PotionEffectType.INCREASE_DAMAGE.getName());
                break;
            case "dwarf":
                effects.add(PotionEffectType.FAST_DIGGING.getName());
                effects.add(PotionEffectType.NIGHT_VISION.getName());
                break;
            case "oni":
                effects.add(PotionEffectType.FIRE_RESISTANCE.getName());
                effects.add(PotionEffectType.ABSORPTION.getName());
                break;
            default:
                break;
        }
        return effects;
    }

    public int getDefaultGuiIndex(String race) {
        int index = 0;
        switch (race) {
            case "human":
                index = 19;
                break;
            case "angel":
                index = 20;
                break;
            case "merrow":
                index = 21;
                break;
            case "dragonborne":
                index = 23;
                break;
            case "dwarf":
                index = 24;
                break;
            case "oni":
                index = 25;
                break;
            default:
                break;
        }
        return index;
    }

    public String getDefaultSkin(String race) {
        String skin = null;
        switch (race) {
            case "human":
                skin = null;
                break;
            case "angel":
                skin = "https://textures.minecraft.net/texture/be6f6d3560a29a3ab0c28f8b72c0582c0a75790b93720a9c0586bf8a1e59595d";
                break;
            case "merrow":
                skin = "https://textures.minecraft.net/texture/76688cbe83fe65e2b3465725d54fbda280796d5fee54a71b8ffdaa04634e91b";
                break;
            case "dragonborne":
                skin = "https://textures.minecraft.net/texture/c23b749e4f458448ea8f666483f9f917beab1d3caa9d411f909945c1af6ffd1d";
                break;
            case "dwarf":
                skin = "https://textures.minecraft.net/texture/397c1732ca0ccd08c87705a84107927fbf59fab45f82a8840071189fb61cdb54";
                break;
            case "oni":
                skin = "https://textures.minecraft.net/texture/8aebecee607f0bb87b95185de589aa55c80955860b664bc52d4ee60f7de6d710";
                break;
            default:
                break;
        }
        return skin;
    }

}
