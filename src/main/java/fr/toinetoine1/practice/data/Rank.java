package fr.toinetoine1.practice.data;

import java.util.ArrayList;

public class Rank {

    private static ArrayList<Rank> ranks = new ArrayList<>();

    private String name;
    private int power;
    private String formattedName;

    public Rank(String name, int power, String formattedName) {
        this.name = name;
        this.power = power;
        this.formattedName = formattedName;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }

    public String getFormattedName() {
        return formattedName.replace('&','§');
    }

    public static ArrayList<Rank> getRanks() {
        return ranks;
    }

    public static Rank getRankFromPower(int power){
        for(Rank rank : ranks){
            if(rank.getPower() == power)
                return rank;
        }

        return null;
    }

    public static Rank getRankByName(String rankName){
        for(Rank rank : ranks){
            if(rankName.equalsIgnoreCase(rank.getName())){
                return rank;
            }
        }

        return null;
    }

    public static Rank getLessRank(){
        int power = Integer.MAX_VALUE;
        for(Rank rank : ranks){
            if(rank.getPower() < power){
                power = rank.getPower();
            }
        }

        return getRankFromPower(power);
    }

}
