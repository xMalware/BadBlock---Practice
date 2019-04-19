package fr.toinetoine1.practice.data;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.kit.Info;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class PPlayer {

    private transient static Map<String, PPlayer> players = new HashMap<>();
    private BadblockPlayer player;
    private String name;
    private Rank customRank;
    private boolean isAttackable;
    private Map<Mode, Info> infos;

    public PPlayer(BadblockPlayer player, Rank customRank) {
        this.player = player;
        this.name = player.getName();
        this.customRank = customRank;
        this.isAttackable = false;
        infos = new HashMap<>();
    }

    public void unload(){
        players.remove(getName());
    }

    public static PPlayer get(String name){
        return players.get(name);
    }

    public static PPlayer get(BadblockPlayer player) {
        return players.get(player.getName());
    }

    public static void init(BadblockPlayer player, Rank rank){
        players.put(player.getName(), new PPlayer(player, rank));
    }

    public static boolean contains(String name) {
        return players.containsKey(name);
    }

    public static Map<String, PPlayer> getPlayers() {
        return players;
    }
}
