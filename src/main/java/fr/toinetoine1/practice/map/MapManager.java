package fr.toinetoine1.practice.map;

import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.database.request.MapRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MapManager {

    @Getter
    private static List<Map> maps;
    @Getter@Setter
    private static int actualKey = 0;

    public static void load() {
        maps = MapRequest.loadMaps();
    }

    public static int incrementAndGet(){
        return actualKey += 1;
    }

    public static Map getNewMap(Mode mode){
        List<Map> availableMaps = maps.stream().filter(Objects::nonNull).filter(map -> map.getMode() == mode).filter(Map::isEnable).filter(map -> !map.isUsed()).collect(Collectors.toList());

        if(availableMaps.isEmpty())
            return null;

        Map map = availableMaps.get(0);
        map.setUsed(true);
        return map;
    }

}
