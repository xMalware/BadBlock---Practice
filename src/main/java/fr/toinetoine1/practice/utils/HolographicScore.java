package fr.toinetoine1.practice.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.kit.Stats;
import fr.toinetoine1.practice.inventory.list.LeaderBoardInv;
import lombok.Data;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Toinetoine1 on 12/06/2019.
 */

@Data
public class HolographicScore {

    private static HolographicScore ourInstance = new HolographicScore();

    private Location killsLoc;
    private Location deathsLoc;
    private Location scoreLoc;
    private Hologram score;
    private Hologram death;
    private Hologram kills;

    public void updateHologram(boolean needLocationUpdate){
        if(needLocationUpdate){
            score.teleport(getScoreLoc());
            kills.teleport(getKillsLoc());
            death.teleport(getDeathsLoc());
        }
        this.updateHologram();
    }

    public void updateHologram() {
        if (score == null)
            score = HologramsAPI.createHologram(Practice.getInstance(), getScoreLoc());
        score.clearLines();
        score.appendTextLine("§b§bTop 10 Score:");
        addValueToHologram(score, ScoreType.SCORE);

        if (death == null)
            death = HologramsAPI.createHologram(Practice.getInstance(), getDeathsLoc());
        death.clearLines();
        death.appendTextLine("§b§bTop 10 Morts:");
        addValueToHologram(death, ScoreType.DEATH);

        if (kills == null)
            kills = HologramsAPI.createHologram(Practice.getInstance(), getKillsLoc());
        kills.clearLines();
        kills.appendTextLine("§b§bTop 10 Kills:");
        addValueToHologram(kills, ScoreType.KILLS);
    }

    private void addValueToHologram(Hologram hologram, ScoreType scoreType) {
        LeaderBoardInv.getStatsMap().entrySet()
                .stream()
                .sorted(Comparator.comparingInt(value -> {
                    switch (scoreType) {
                        case KILLS:
                            return value.getValue().getStats().values().stream().mapToInt(Stats::getKills).sum();
                        case DEATH:
                            return value.getValue().getStats().values().stream().mapToInt(Stats::getDeaths).sum();
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
                    int position = 1;
                    int value = -1;
                    switch (scoreType) {
                        case SCORE:
                            value = e.getValue().getPoints();
                            break;
                        case KILLS:
                            value = e.getValue().getStats().values().stream().mapToInt(Stats::getKills).sum();
                            break;
                        case DEATH:
                            value = e.getValue().getStats().values().stream().mapToInt(Stats::getDeaths).sum();
                            break;
                    }
                    hologram.appendTextLine("§d" + (position++) + "e: §a" + e.getKey() + "- §b" + value);
                });

    }

    public static HolographicScore getInstance() {
        return ourInstance;
    }

    private enum ScoreType {
        SCORE,
        KILLS,
        DEATH;
    }
}
