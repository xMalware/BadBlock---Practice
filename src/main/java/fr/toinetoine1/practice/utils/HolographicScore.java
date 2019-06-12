package fr.toinetoine1.practice.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.inventory.list.LeaderBoardInv;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Toinetoine1 on 12/06/2019.
 */

@Data
public class HolographicScore {

    @Getter
    private Map<String, RankedPlayerModeInfo> statsMap = new HashMap<>();
    @Getter
    private Map<String, List<InfoStats>> sortedStats = new HashMap<>();

    private static HolographicScore ourInstance = new HolographicScore();

    public static HolographicScore getInstance() {
        return ourInstance;
    }

    private Location kills;
    private Location deaths;
    private Location score;

    public void updateHologram() {
        Hologram score = HologramsAPI.createHologram(Practice.getInstance(), getScore());
        score.appendTextLine("§b§bTop 10 Score:");
        for (int i = 1; i < 10; i++) {
            score.appendTextLine("§d"+i+"e: §a");
        }

    }

    public void sortMap() {
        this.sortMap(ScoreType.KILLS);
    }

    private void sortMap(ScoreType scoreType){
        sortedStats.clear();

        for (Kit kit : Kit.getDefaultsKit().get(Mode.RANKEDONE)) {
            statsMap.entrySet()
                    .stream()
                    .sorted(Comparator.comparingInt(value -> {
                        switch (scoreType){
                            case KILLS:
                                return value.getValue().getStats().get(kit.getName()).getKills();
                            case DEATH:
                                return value.getValue().getStats().get(kit.getName()).getDeaths();
                            case SCORE:
                                return value.getValue().getPoints();
                            default:
                                return 0;
                        }
                    }))
                    .collect(Collectors.collectingAndThen(Collectors.toList(), strings -> {
                        Collections.reverse(strings);
                        return strings;
                    }))
                    .stream()
                    .limit(10)
                    .forEach(e -> {
                        if (sortedStats.containsKey(kit.getName())) {
                            sortedStats.get(kit.getName()).add(new InfoStats(e.getKey(), e.getValue()));
                        } else {
                            sortedStats.put(kit.getName(), new ArrayList<>(Arrays.asList(new InfoStats(e.getKey(), e.getValue()))));
                        }
                    });
        }
    }

    private void addValueToHologram(Hologram hologram, ScoreType scoreType){
        

    }

    private enum ScoreType {
        SCORE,
        KILLS,
        DEATH
    }
}
