package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import fr.toinetoine1.practice.data.PPlayer;

import java.util.List;

public class RankedInfo extends Info{

    private int points;

    public RankedInfo(PPlayer pPlayer, String name, int kills, int deaths, List<Kit> kits, int points) {
        super(pPlayer, name, kills, deaths, kits);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
