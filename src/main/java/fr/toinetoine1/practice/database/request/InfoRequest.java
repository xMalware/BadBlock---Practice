package fr.toinetoine1.practice.database.request;

/*
    Created by Toinetoine1 on 02/04/2019
*/

public class InfoRequest {

    /*public static PlayerModeInfo loadInfoForMode(PPlayer pPlayer, Mode mode) {
        return mode.isRanked() ? loadRankedFromBDD(pPlayer, mode) : loadNoRankedFromBDD(pPlayer, mode);
    }

    private static PlayerModeInfo loadNoRankedFromBDD(PPlayer pPlayer, Mode mode) {
        if (mode.isRanked())
            return null;

        BadblockPlayer badblockPlayer = pPlayer.getPlayer();

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + mode.getDatabaseName() + " where name = ?");
            preparedStatement.setString(1, badblockPlayer.getName());
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();
            PlayerModeInfo info = null;

            while (resultSet.next()) {
                final int kills = resultSet.getInt("kills");
                final int deaths = resultSet.getInt("deaths");
                final List<Kit> kits = KitLoader.deserialize(resultSet.getString("kits"));

                info = new PlayerModeInfo(pPlayer, badblockPlayer.getName(), kills, deaths, kits);
            }

            preparedStatement.close();
            connection.close();
            return info;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        badblockPlayer.sendMessage("§cError when loading your infos (" + mode.getDatabaseName() + "..");

        return null;
    }

    private static PlayerModeInfo loadRankedFromBDD(PPlayer pPlayer, Mode mode) {
        if (!mode.isRanked())
            return null;

        BadblockPlayer badblockPlayer = pPlayer.getPlayer();

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + mode.getDatabaseName() + " where name = ?");
            preparedStatement.setString(1, badblockPlayer.getName());
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();
            PlayerModeInfo info = null;

            while (resultSet.next()) {
                final int kills = resultSet.getInt("kills");
                final int deaths = resultSet.getInt("deaths");
                final List<Kit> kits = KitLoader.deserialize(resultSet.getString("kits"));
                final int points = resultSet.getInt("points");

                info = new RankedPlayerModeInfo(pPlayer, badblockPlayer.getName(), kills, deaths, kits, points);
            }

            preparedStatement.close();
            connection.close();
            return info;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        badblockPlayer.sendMessage("§cError when loading your infos (" + mode.getDatabaseName() + "..");
        return null;
    }*/

}
