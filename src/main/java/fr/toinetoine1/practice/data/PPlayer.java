package fr.toinetoine1.practice.data;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.data.kit.PlayerModeInfo;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.scoreboard.PracticeScoreboard;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PPlayer {

    private transient static Map<String, PPlayer> players = new HashMap<>();
    private BadblockPlayer player;
    private transient PracticeScoreboard scoreboard;
    private Rank customRank;
    private String name;
    private Map<Mode, PlayerModeInfo> infos;

    public PPlayer(BadblockPlayer player, Map<Mode, PlayerModeInfo> infos, Rank customRank) {
        this.player = player;
        this.name = player.getName();
        this.infos = infos;
        this.customRank = customRank;
    }

    public void unload(){
        players.remove(getName());
    }

    public int getELO(){
        return infos.entrySet().stream().filter(modeInfoEntry -> modeInfoEntry.getKey().isRanked()).map(Map.Entry::getValue).mapToInt(value -> ((RankedPlayerModeInfo) value).getPoints()).sum();
    }

    public Rank getNextRank(){
        int index = Rank.getRanks().indexOf(customRank);
        if(Rank.getRanks().size() - 1 == index){
            return customRank;
        }

        return Rank.getRanks().get(index + 1);
    }

    public void tryUpdateRank() {
        this.customRank = Rank.getRankFromBadPoints(((RankedPlayerModeInfo) infos.get(Mode.RANKEDONE)).getPoints());
    }

    public static PPlayer get(String name){
        return players.get(name);
    }

    public static PPlayer get(BadblockPlayer player) {
        return players.get(player.getName());
    }

    public static PPlayer init(BadblockPlayer player, Map<Mode, PlayerModeInfo> infos, Rank customRank){
        return players.put(player.getName(), new PPlayer(player, infos, customRank));
    }

    public static boolean contains(String name) {
        return players.containsKey(name);
    }

    public static Map<String, PPlayer> getPlayers() {
        return players;
    }
}
