package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PlayerModeInfo {

    private int kills;
    private int deaths;
    private Map<String, ModifiedKit> modified;

    public PlayerModeInfo(int kills, int deaths) {
        this.kills = kills;
        this.deaths = deaths;
        this.modified = new HashMap<>();
    }

    public void addKill(){
        kills += 1;
    }

    public void addDeath(){
        deaths += 1;
    }

}
