package fr.toinetoine1.practice.database.request;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.Rank;
import fr.toinetoine1.practice.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataRequest {

    public static void selectUser(BadblockPlayer badblockPlayer){
        if(!hasUser(badblockPlayer) && !PPlayer.contains(badblockPlayer.getName())){
            PPlayer.getPlayers().put(badblockPlayer.getName(), new PPlayer(badblockPlayer, Rank.getLessRank()));
            return;
        }

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players where name = ?");
            preparedStatement.setString(1, badblockPlayer.getName());
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Rank rank = Rank.getRankFromPower(resultSet.getInt("rankPower"));
                final Rank rankName = Rank.getRankByName(resultSet.getString("rankName"));

                if(rank != rankName)
                    rank = rankName;

                if (PPlayer.contains(badblockPlayer.getName()))
                    PPlayer.getPlayers().remove(badblockPlayer.getName());

                PPlayer.init(badblockPlayer, rank);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        badblockPlayer.sendMessage("§cSuccess !");
    }

    public static boolean hasUser(BadblockPlayer badblockPlayer){
        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM players WHERE name = ?");
            preparedStatement.setString(1, badblockPlayer.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean hasAccount = resultSet.next();
            preparedStatement.close();
            connection.close();

            return hasAccount;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void updatePlayer(PPlayer pPlayer){
        if(!hasUser(pPlayer.getPlayer())){
            createUserInBDD(pPlayer);
            return;
        }

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET rankPower = ?, rankName = ? WHERE name = ?");
            preparedStatement.setInt(1, pPlayer.getCustomRank().getPower());
            preparedStatement.setString(2, pPlayer.getCustomRank().getName());
            preparedStatement.setString(3, pPlayer.getName());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pPlayer.unload();
    }

    private static void createUserInBDD(PPlayer pPlayer){
        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (name, rankPower, rankName) VALUES (?, ?, ?)");
            preparedStatement.setString(1, pPlayer.getName());
            preparedStatement.setInt(2, pPlayer.getCustomRank().getPower());
            preparedStatement.setString(3, pPlayer.getCustomRank().getName());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pPlayer.unload();
    }

}
