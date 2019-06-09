package fr.toinetoine1.practice.data.kit;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Toinetoine1 on 30/05/2019.
 */

@AllArgsConstructor
@Data
public class Stats {

    private int kills;
    private int deaths;

    public int addKill(){
        return this.kills += 1;
    }

    public int addDeath(){
        return this.deaths += 1;
    }

}
