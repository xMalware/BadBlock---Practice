package fr.toinetoine1.practice.data;

import fr.badblock.gameapi.GameAPI;
import fr.toinetoine1.practice.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Rank {

    private static ArrayList<Rank> ranks = new ArrayList<>();

    private String name;
    private int neededPoints;
    private String formattedName;

    public Rank(String name, int neededPoints, String formattedName) {
        this.name = name;
        this.neededPoints = neededPoints;
        this.formattedName = formattedName;
    }

    public String getName() {
        return name;
    }

    public int getNeededPoints() {
        return neededPoints;
    }

    public String getFormattedName() {
        return ChatColor.translateAlternateColorCodes('&', formattedName);
    }

    public static ArrayList<Rank> getRanks() {
        return ranks;
    }

    public static Rank getRankFromBadPoints(int badPoints){
        Rank rank = Rank.getRanks().get(0);
        for(Rank current : ranks){
            if(badPoints >= current.getNeededPoints()){
                if(rank != null && rank.getNeededPoints() < current.getNeededPoints()){
                    rank = current;
                }
            }
        }

        if(rank == null){
            return null;
        }

        return rank;
    }

    public static Rank getRankByName(String rankName){
        for(Rank rank : ranks){
            if(rankName.equalsIgnoreCase(rank.getName())){
                return rank;
            }
        }

        return null;
    }

    public static void initRanks() {
        try {
            final Connection connection = DatabaseManager.MAIN_DATABASE.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ranks");

            final ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                final String name = resultSet.getString("name");
                final int neededPoints = resultSet.getInt("neededPoints");
                final String formattedName = resultSet.getString("formattedName");
                final Rank rank = new Rank(name, neededPoints, formattedName);

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

        Rank.getRanks().sort(Comparator.comparingInt(Rank::getNeededPoints));
    }

}
