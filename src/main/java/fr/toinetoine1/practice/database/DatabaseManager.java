package fr.toinetoine1.practice.database;

import fr.badblock.gameapi.GameAPI;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Rank;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        initRanks();
    }

    public static void closeAllDatabaseConnections(){
        for(DatabaseManager databaseManager : DatabaseManager.values()){
            databaseManager.getDatabaseAccess().closePool();
        }
    }

    private static void initRanks() {
        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ranks");

            final ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                final String name = resultSet.getString("name");
                final int power = resultSet.getInt("power");
                final String formattedName = resultSet.getString("formattedName");
                final Rank rank = new Rank(name, power, formattedName);

                Rank.getRanks().add(rank);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(Rank.getRanks().isEmpty()){
            GameAPI.logColor("§cError, unable to find ranks..");
            Bukkit.shutdown();
        }
    }
}
