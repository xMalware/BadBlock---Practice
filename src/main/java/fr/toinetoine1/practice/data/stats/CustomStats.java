package fr.toinetoine1.practice.data.stats;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Toinetoine1 on 01/06/2019.
 */

@AllArgsConstructor
@Data
public class CustomStats {

    private int gamePlayed;
    private long timePlayed;

    public void addGame(){
        gamePlayed += 1;
    }

}
