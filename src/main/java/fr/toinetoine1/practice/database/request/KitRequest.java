package fr.toinetoine1.practice.database.request;

/*
    Created by Toinetoine1 on 25/04/2019
*/

import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.data.kit.KitLoader;
import fr.toinetoine1.practice.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KitRequest {

    public static void loadKits(){

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM kits");
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                Mode mode = Mode.valueOf(resultSet.getString("mode"));
                List<Kit> kits = new ArrayList<>();
                if(resultSet.getString("kits").equalsIgnoreCase("null")){
                    kits = new ArrayList<>();
                } else {
                    kits = KitLoader.deserialize(resultSet.getString("kits"));
                }

                Kit.getDefaultsKit().put(mode, kits);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void saveKits(){

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            connection.prepareStatement("TRUNCATE TABLE kits").execute();

            for(Mode mode : Mode.values()){
                final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO kits (mode, kits) VALUES (?, ?)");
                preparedStatement.setString(1, mode.name());
                if(Kit.getDefaultsKit().get(mode) == null || (Kit.getDefaultsKit().get(mode) != null && Kit.getDefaultsKit().get(mode).isEmpty())){
                    preparedStatement.setString(2, "null");
                } else {
                    preparedStatement.setString(2, KitLoader.serialize(Kit.getDefaultsKit().get(mode)));
                }
                preparedStatement.executeUpdate();

                preparedStatement.close();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
