package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 02/04/2019
*/

public class RankedPlayerModeInfo extends PlayerModeInfo {

    private int points;

    public RankedPlayerModeInfo(int kills, int death, int points) {
        super(kills, death);
        this.points = points;
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
}
