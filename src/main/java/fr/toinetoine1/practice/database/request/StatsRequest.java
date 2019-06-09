package fr.toinetoine1.practice.database.request;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.database.DatabaseManager;
import fr.toinetoine1.practice.inventory.list.LeaderBoardInv;
import fr.toinetoine1.practice.utils.GsonFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Toinetoine1 on 30/05/2019.
 */

public class StatsRequest {

    public static void updateStats(BadblockPlayer player){
        //String rankedColonn = Joiner.on(", ").join(Arrays.stream(Mode.values()).map(Mode::isRanked).collect(Collectors.toList()));

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT name, rankedone FROM players");
            /*if(player == null){
                preparedStatement =
            } else {
                preparedStatement = connection.prepareStatement("SELECT name, rankedone FROM players where name = ?");
                preparedStatement.setString(1, player.getName());
            }*/
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                String playerName = resultSet.getString("name");
                RankedPlayerModeInfo rankedOne = GsonFactory.getCompactGson().fromJson(resultSet.getString("rankedone"), RankedPlayerModeInfo.class);

                LeaderBoardInv.getStatsMap().put(playerName, rankedOne);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LeaderBoardInv.sortMap();
    }

}
