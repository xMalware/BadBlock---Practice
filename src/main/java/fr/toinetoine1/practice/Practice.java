package fr.toinetoine1.practice;

import fr.badblock.gameapi.BadblockPlugin;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.scoreboard.BadblockScoreboardGenerator;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.GameRules;
import fr.toinetoine1.practice.config.BoxConfig;
import fr.toinetoine1.practice.data.kit.KitLoader;
import fr.toinetoine1.practice.database.DatabaseManager;
import fr.toinetoine1.practice.map.MapManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Practice extends BadblockPlugin {

    @Getter
    private static Practice instance;

    @Override
    public void onEnable(RunType runType) {
        instance = this;

        saveDefaultConfig();

        GameRules.doDaylightCycle.setGameRule(false);
        GameRules.spectatorsGenerateChunks.setGameRule(false);
        GameRules.doFireTick.setGameRule(false);

        getAPI().getBadblockScoreboard().doBelowNameHealth();
        getAPI().formatChat(true, true);
        getAPI().getBadblockScoreboard().doGroupsPrefix();
        getAPI().getBadblockScoreboard().doOnDamageHologram();

        getAPI().setAntiAfk(true);
        getAPI().setMapProtector(new PracticeMapProtector());

        DatabaseManager.initAllDatabaseConnections();

        BoxConfig.reload(this);
        MapManager.load(this);
        initCommandsAndEvents();
        new KitLoader();

        GameAPI.logColor("§c[Practice] Loaded !");
    }

    @Override
    public void onDisable() {
        DatabaseManager.closeAllDatabaseConnections();
    }

    private void initCommandsAndEvents(){
        String[] commandsAndEvents = {"fr.toinetoine1.practice.commands", "fr.toinetoine1.practice.events"};

        try {
            BukkitUtils.instanciateListenersAndCommandsFrom(this, commandsAndEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
