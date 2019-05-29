package fr.toinetoine1.practice.database;

import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Rank;
import fr.toinetoine1.practice.database.request.KitRequest;
import fr.toinetoine1.practice.database.request.MapRequest;
import fr.toinetoine1.practice.map.MapManager;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Toinetoine1 on 31/03/2019.
 */

public enum DatabaseManager {

    MAIN_DATABASE(new DatabaseCredentials(Practice.getInstance().getConfig().getString("host"),
            Practice.getInstance().getConfig().getString("user"),
            Practice.getInstance().getConfig().getString("pass"),
            Practice.getInstance().getConfig().getString("databasename"),
            Practice.getInstance().getConfig().getInt("port")));

    private DatabaseAccess databaseAccess;

    DatabaseManager(DatabaseCredentials credentials){
        this.databaseAccess = new DatabaseAccess(credentials);
    }

    public DatabaseAccess getDatabaseAccess() {
        return databaseAccess;
    }

    public static void initAllDatabaseConnections(){
        for(DatabaseManager databaseManager : DatabaseManager.values()){
            databaseManager.getDatabaseAccess().initPool();
        }

        Rank.initRanks();
        KitRequest.loadKits();
        new BukkitRunnable(){
            @Override
            public void run() {
                MapManager.load();
            }
        }.runTaskLater(Practice.getInstance(), 15 * 20);
    }

    public static void closeAllDatabaseConnections(){
        MapRequest.saveMaps();
        KitRequest.saveKits();
        for(DatabaseManager databaseManager : DatabaseManager.values()){
            databaseManager.getDatabaseAccess().closePool();
        }
    }
}
