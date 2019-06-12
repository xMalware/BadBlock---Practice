package fr.toinetoine1.practice.map;

/*
    Created by Toinetoine1 on 22/04/2019
*/

import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.utils.Cuboid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@AllArgsConstructor
@Data
public class Map {

    private transient boolean isUsed;
    private int key;
    private String mapName;
    private Location[] locations;
    private boolean isEnable;
    private Mode mode;
    private Cuboid cuboid;

}
