package fr.toinetoine1.practice.database.request;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.Info;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.data.kit.KitLoader;
import fr.toinetoine1.practice.data.kit.RankedInfo;
import fr.toinetoine1.practice.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InfoRequest {

    public static Info loadInfoForMode(PPlayer pPlayer, Mode mode) {
        return mode.isRanked() ? loadRankedFromBDD(pPlayer, mode) : loadNoRankedFromBDD(pPlayer, mode);
    }

    private static Info loadNoRankedFromBDD(PPlayer pPlayer, Mode mode) {
        if (mode.isRanked())
            return null;

        BadblockPlayer badblockPlayer = pPlayer.getPlayer();

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills,deaths,kits FROM " + mode.getDatabaseName() + " where name = ?");
            preparedStatement.setString(1, badblockPlayer.getName());
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                final int kills = resultSet.getInt("kills");
                final int deaths = resultSet.getInt("deaths");
                final List<Kit> kits = KitLoader.deserialize(resultSet.getString("kits"));

                final Info info = new Info(pPlayer, badblockPlayer.getName(), kills, deaths, kits);
                return info;
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        badblockPlayer.sendMessage("§cError when loading your infos (" + mode.getDatabaseName() + "..");

        return null;
    }

    private static Info loadRankedFromBDD(PPlayer pPlayer, Mode mode) {
        if (!mode.isRanked())
            return null;

        BadblockPlayer badblockPlayer = pPlayer.getPlayer();

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills,deaths,kits FROM " + mode.getDatabaseName() + " where name = ?");
            preparedStatement.setString(1, badblockPlayer.getName());
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                final int kills = resultSet.getInt("kills");
                final int deaths = resultSet.getInt("deaths");
                final List<Kit> kits = KitLoader.deserialize(resultSet.getString("kits"));
                final int points = resultSet.getInt("points");

                final Info info = new RankedInfo(pPlayer, badblockPlayer.getName(), kills, deaths, kits, points);
                return info;
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        badblockPlayer.sendMessage("§cError when loading your infos (" + mode.getDatabaseName() + "..");

        return null;
    }

}
