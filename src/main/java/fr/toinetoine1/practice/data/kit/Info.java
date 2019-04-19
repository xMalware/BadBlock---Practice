package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import fr.toinetoine1.practice.data.PPlayer;

import java.util.List;

public class Info {

    private PPlayer pPlayer;
    private String name;
    private int kills;
    private int deaths;
    private List<Kit> kits;

    public Info(PPlayer pPlayer, String name, int kills, int deaths, List<Kit> kits) {
        this.pPlayer = pPlayer;
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
        this.kits = kits;
    }

    public PPlayer getPlayer() {
        return pPlayer;
    }

    public String getName() {
        return name;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public List<Kit> getKits() {
        return kits;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setKits(List<Kit> kits) {
        this.kits = kits;
    }
}
