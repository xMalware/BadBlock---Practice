package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import java.util.Map;

public class RankedPlayerModeInfo extends PlayerModeInfo {

    private int points;
    private Map<String, Stats> stats;

    public RankedPlayerModeInfo(int kills, int death, int points, Map<String, Stats> stats) {
        super(kills, death);
        this.points = points;
        this.stats = stats;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int addPoints(int points){
        return this.points += points;
    }

    public Map<String, Stats> getStats() {
        return stats;
    }

}
