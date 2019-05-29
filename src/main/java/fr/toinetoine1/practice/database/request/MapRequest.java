package fr.toinetoine1.practice.database.request;

/*
    Created by Toinetoine1 on 22/04/2019
*/

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.badblock.gameapi.utils.selections.CuboidSelection;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.database.DatabaseManager;
import fr.toinetoine1.practice.map.Map;
import fr.toinetoine1.practice.map.MapManager;
import fr.toinetoine1.practice.utils.Cuboid;
import fr.toinetoine1.practice.utils.LocationAdapter;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MapRequest {

    private static Gson gson;

    static {
        gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Location.class, LocationAdapter.INSTANCE)
                .disableHtmlEscaping()
                .create();
    }

    public static List<Map> loadMaps(){
        List<Map> maps = new ArrayList<>();

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM maps");
            preparedStatement.executeQuery();
            final ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                int key = resultSet.getInt("keyy");
                Location[] positions = gson.fromJson(resultSet.getString("positions"), Location[].class);
                boolean enable = resultSet.getBoolean("enable");
                Mode mode = Mode.valueOf(resultSet.getString("mode"));
                Cuboid cuboidSelection = gson.fromJson(resultSet.getString("cuboid"), Cuboid.class);

                if(key > MapManager.getActualKey())
                    MapManager.setActualKey(key);

                maps.add(new Map(false, key, positions, enable, mode, cuboidSelection));
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maps;
    }

    public static void saveMaps(){

        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            connection.prepareStatement("TRUNCATE TABLE maps").execute();

            for(Map map : MapManager.getMaps()){
                final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO maps (keyy, positions, enable, mode, cuboid) VALUES (?, ?, ?, ?, ?)");
                preparedStatement.setInt(1, map.getKey());
                preparedStatement.setString(2, gson.toJson(map.getLocations()));
                preparedStatement.setBoolean(3, map.isEnable());
                preparedStatement.setString(4, map.getMode().name());
                preparedStatement.setString(5, gson.toJson(map.getCuboid()));
                preparedStatement.executeUpdate();

                preparedStatement.close();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
