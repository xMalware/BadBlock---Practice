package fr.toinetoine1.practice.config;

import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.utils.HolographicScore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Toinetoine1 on 16/01/2019.
 */
public class ConfigManager {

    private static List<Config> configs = new ArrayList<>();

    public ConfigManager() {
        if (!Practice.getInstance().getDataFolder().exists()) {
            Practice.getInstance().getDataFolder().mkdir();
        }

        configs.addAll(Arrays.asList(
                new Config("spawn.yml")
        ));

        loadSpawnLoc();
        loadHoloLoc();
    }

    public static Config getConfigByName(String name) {
        for (Config config : configs) {
            if (name.equals(config.getFileName())) {
                return config;
            }
        }
        return null;
    }

    public static void refreshAllConfig() {
        configs.forEach(config -> config.setConfig(YamlConfiguration.loadConfiguration(config.getFile())));
    }

    public static List<Config> getConfigs() {
        return configs;
    }

    private void loadSpawnLoc(){
        YamlConfiguration config = getConfigByName("spawn.yml").getConfig();
        Location location = new Location(Bukkit.getWorld(config.getString("Spawn.world")),
                config.getDouble("Spawn.x"),
                config.getDouble("Spawn.y"),
                config.getDouble("Spawn.z"),
                config.getLong("Spawn.yaw"),
                config.getLong("Spawn.pitch"));

                Practice.setSpawnLoc(location);
    }

    private void loadHoloLoc() {
        YamlConfiguration config = getConfigByName("spawn.yml").getConfig();

        Location scoreLoc = new Location(Bukkit.getWorld(config.getString("ScoreLocation.world")),
                config.getDouble("ScoreLocation.x"),
                config.getDouble("ScoreLocation.y"),
                config.getDouble("ScoreLocation.z"));
        HolographicScore.getInstance().setScore(scoreLoc);

        Location killsLoc = new Location(Bukkit.getWorld(config.getString("KillsLocation.world")),
                config.getDouble("KillsLocation.x"),
                config.getDouble("KillsLocation.y"),
                config.getDouble("KillsLocation.z"));
        HolographicScore.getInstance().setKills(killsLoc);

        Location deathLoc = new Location(Bukkit.getWorld(config.getString("DeathLocation.world")),
                config.getDouble("DeathLocation.x"),
                config.getDouble("DeathLocation.y"),
                config.getDouble("DeathLocation.z"));
        HolographicScore.getInstance().setDeaths(deathLoc);
    }

}
