package fr.toinetoine1.practice.database.request;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.Rank;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.data.kit.PlayerModeInfo;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.data.kit.Stats;
import fr.toinetoine1.practice.data.stats.CustomStats;
import fr.toinetoine1.practice.database.DatabaseManager;
import fr.toinetoine1.practice.utils.GsonFactory;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRequest {

    public static PPlayer selectUser(BadblockPlayer badblockPlayer) {
        if (!hasUser(badblockPlayer) && !PPlayer.contains(badblockPlayer.getName())) {
            Map<String, Stats> stats = new HashMap<>();
            for (Map.Entry<Mode, List<Kit>> entries : Kit.getDefaultsKit().entrySet()) {
                if (entries.getKey().isRanked()) {
                    for (Kit kit : entries.getValue()) {
                        stats.put(kit.getName(), new Stats(0, 0));
                    }
                }
            }

            Map<Mode, PlayerModeInfo> infos = new HashMap<>();
            for (Mode mode : Mode.values()) {
                if (mode.isRanked()) {
                    infos.put(mode, new RankedPlayerModeInfo(0, 0, 2000, stats));
                } else {
                    infos.put(mode, new PlayerModeInfo(0, 0));
                }
            }

            int badPoints = infos.entrySet().stream().filter(modeInfoEntry -> modeInfoEntry.getKey().isRanked()).filter(modePlayerModeInfoEntry -> modePlayerModeInfoEntry.getKey() == Mode.RANKEDONE).map(Map.Entry::getValue).mapToInt(value -> ((RankedPlayerModeInfo) value).getPoints()).sum();
            CustomStats customStats = new CustomStats(0,0);

            PPlayer.init(badblockPlayer, infos, Rank.getRankFromBadPoints(badPoints), customStats);
            return PPlayer.get(badblockPlayer);
        }

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players where name = ?");
            preparedStatement.setString(1, badblockPlayer.getName());
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Map<Mode, PlayerModeInfo> infos = new HashMap<>();
                for (Mode mode : Mode.values()) {
                    if (mode.isRanked()) {
                        infos.put(mode, GsonFactory.getCompactGson().fromJson(resultSet.getString(mode.getColomnName()), RankedPlayerModeInfo.class));
                    } else {
                        infos.put(mode, GsonFactory.getCompactGson().fromJson(resultSet.getString(mode.getColomnName()), PlayerModeInfo.class));
                    }
                }

                CustomStats customStats = GsonFactory.getCompactGson().fromJson(resultSet.getString("customstats"), CustomStats.class);
                if(customStats == null)
                    customStats = new CustomStats(0,0);

                int badPoints = infos.entrySet().stream().filter(modeInfoEntry -> modeInfoEntry.getKey().isRanked()).filter(modePlayerModeInfoEntry -> modePlayerModeInfoEntry.getKey() == Mode.RANKEDONE).map(Map.Entry::getValue).mapToInt(value -> ((RankedPlayerModeInfo) value).getPoints()).sum();

                if (PPlayer.contains(badblockPlayer.getName()))
                    PPlayer.getPlayers().remove(badblockPlayer.getName());

                PPlayer.init(badblockPlayer, infos, Rank.getRankFromBadPoints(badPoints), customStats);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return PPlayer.get(badblockPlayer);
    }

    public static boolean hasUser(BadblockPlayer badblockPlayer) {
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

    public static void updatePlayer(PPlayer pPlayer) {
        if (!hasUser(pPlayer.getPlayer())) {
            createUserInBDD(pPlayer);
            return;
        }

        StringBuilder str = new StringBuilder();
        for (Map.Entry<Mode, PlayerModeInfo> entryInfo : pPlayer.getInfos().entrySet()) {
            str.append(entryInfo.getKey().getColomnName()).append(" = ?,");
        }

        String request = "UPDATE players SET " + str.toString().substring(0, str.length() - 1) + ", customstats = ? WHERE name = ?";

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement(request);
            int i = 0;
            for (Map.Entry<Mode, PlayerModeInfo> entryInfo : pPlayer.getInfos().entrySet()) {
                i++;
                if (entryInfo.getKey().isRanked()) {
                    preparedStatement.setString(i, GsonFactory.getCompactGson().toJson(entryInfo.getValue(), RankedPlayerModeInfo.class));
                } else {
                    preparedStatement.setString(i, GsonFactory.getCompactGson().toJson(entryInfo.getValue(), PlayerModeInfo.class));
                }
            }
            preparedStatement.setString(++i, GsonFactory.getCompactGson().toJson(pPlayer.getCustomStats()));
            preparedStatement.setString(++i, pPlayer.getName());
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pPlayer.unload();
    }

    private static void createUserInBDD(PPlayer pPlayer) {
        String column = StringUtils.join(pPlayer.getInfos().keySet().stream().map(Mode::getColomnName).toArray(), ", ");
        StringBuilder interrogation = new StringBuilder();
        for (int i = 0; i < pPlayer.getInfos().size(); i++) {
            if (i != 0) {
                interrogation.append(",");
            }

            interrogation.append("? ");
        }

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (name, " + column + ") VALUES (?, " + interrogation.toString() + ", ?)");
            preparedStatement.setString(1, pPlayer.getName());
            int i = 1;
            for (Map.Entry<Mode, PlayerModeInfo> entryInfo : pPlayer.getInfos().entrySet()) {
                i++;
                if (entryInfo.getKey().isRanked()) {
                    preparedStatement.setString(i, GsonFactory.getCompactGson().toJson(entryInfo.getValue(), RankedPlayerModeInfo.class));
                } else {
                    preparedStatement.setString(i, GsonFactory.getCompactGson().toJson(entryInfo.getValue(), PlayerModeInfo.class));
                }
            }
            preparedStatement.setString(++i, GsonFactory.getCompactGson().toJson(pPlayer.getCustomStats()));
            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pPlayer.unload();
    }

    public static Map<Mode, PlayerModeInfo> reloadModeInfo(BadblockPlayer badblockPlayer){
        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players where name = ?");
            preparedStatement.setString(1, badblockPlayer.getName());
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Map<Mode, PlayerModeInfo> infos = new HashMap<>();
                for (Mode mode : Mode.values()) {
                    if (mode.isRanked()) {
                        infos.put(mode, GsonFactory.getCompactGson().fromJson(resultSet.getString(mode.getColomnName()), RankedPlayerModeInfo.class));
                    } else {
                        infos.put(mode, GsonFactory.getCompactGson().fromJson(resultSet.getString(mode.getColomnName()), PlayerModeInfo.class));
                    }
                }

                return infos;
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CustomStats reloadCustomStats(BadblockPlayer badblockPlayer){
        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT customstats FROM players where name = ?");
            preparedStatement.setString(1, badblockPlayer.getName());
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                CustomStats customStats = GsonFactory.getCompactGson().fromJson(resultSet.getString("customstats"), CustomStats.class);
                return customStats;
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
