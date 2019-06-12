package fr.toinetoine1.practice;

import fr.badblock.gameapi.BadblockPlugin;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.GameRules;
import fr.toinetoine1.practice.config.Config;
import fr.toinetoine1.practice.config.ConfigManager;
import fr.toinetoine1.practice.core.Game;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.database.DatabaseManager;
import fr.toinetoine1.practice.database.request.MapRequest;
import fr.toinetoine1.practice.inventory.list.KitChoice;
import fr.toinetoine1.practice.utils.HolographicScore;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Practice extends BadblockPlugin {

    @Getter
    private static Practice instance;
    public static final String PREFIX = "§7[§2Practice§7] §e";
    @Getter
    private static List<Game> games = new ArrayList<>();
    //@Getter
    //private static TabSort tabSort;
    @Getter @Setter
    private static Location spawnLoc;

    @Override
    public void onEnable(RunType runType) {
        instance = this;

        saveDefaultConfig();
        GameRules.doDaylightCycle.setGameRule(false);
        GameRules.spectatorsGenerateChunks.setGameRule(false);
        GameRules.doFireTick.setGameRule(false);

        getAPI().getBadblockScoreboard().doBelowNameHealth();
        getAPI().setMapProtector(new PracticeMapProtector());
        getAPI().setAntiAfk(true);

        DatabaseManager.initAllDatabaseConnections();

        initCommandsAndEvents();
        new ConfigManager();
        loadInventory();
        HolographicScore.getInstance().loadHologram();
        //tabSort = new TabSort(this);

        GameAPI.logColor("§c[Practice] Loaded !");
    }

    @Override
    public void onDisable() {
        MapRequest.saveMaps();

        for(BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers()){
            player.kickPlayer("§cLe serveur est en train de s'éteindre..");
        }

        DatabaseManager.closeAllDatabaseConnections();

        Config config = ConfigManager.getConfigByName("spawn.yml");
        YamlConfiguration spawnConfig = config.getConfig();
        spawnConfig.set("Spawn.world", spawnLoc.getWorld().getName());
        spawnConfig.set("Spawn.x", spawnLoc.getX());
        spawnConfig.set("Spawn.y", spawnLoc.getY());
        spawnConfig.set("Spawn.z", spawnLoc.getZ());
        spawnConfig.set("Spawn.yaw", spawnLoc.getYaw());
        spawnConfig.set("Spawn.pitch", spawnLoc.getPitch());

        try {
            spawnConfig.save(config.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initCommandsAndEvents(){
        String[] commandsAndEvents = {"fr.toinetoine1.practice.commands", "fr.toinetoine1.practice.events"};

        try {
            BukkitUtils.instanciateListenersAndCommandsFrom(this, commandsAndEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadInventory(){
        for(Mode mode : Mode.values()){
            KitChoice.getKitChoiceMap().put(mode, new KitChoice(mode));
        }
    }
}
