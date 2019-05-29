package fr.toinetoine1.practice.utils;

/**
 * Created by Toinetoine1 on 25/05/2019.
 */

import java.util.*;
import java.util.stream.Collectors;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.Rank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/*public class TabSort implements Listener {

    public TabSort(JavaPlugin plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        new BukkitManager(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        BadblockPlayer player = (BadblockPlayer) event.getPlayer();
        PPlayer pPlayer = PPlayer.get(player);

        player.setScoreboard(BukkitManager.getBoard());
        removePlayer(player);
        BukkitManager.teams.get(pPlayer.getCustomRank().getName()).addEntry(player.getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        BadblockPlayer player = (BadblockPlayer) event.getPlayer();

        removePlayer(player);
    }

    public void removePlayer(BadblockPlayer player){
        BukkitManager.getBoard().getTeams().forEach(team -> {
            if(team.hasEntry(player.getName()))
                team.removeEntry(player.getName());
        });
    }

    public static class BukkitManager {

        static Scoreboard sb;
        static HashMap<String, Team> teams = new HashMap<>();

        BukkitManager(Object bukkitPlugin) {
            Plugin plugin = (Plugin) bukkitPlugin;
            try {
                sb = plugin.getServer().getScoreboardManager().getMainScoreboard();
            } catch (Exception e) {
                sb = plugin.getServer().getScoreboardManager().getMainScoreboard();
            }

            List<Rank> sortedRanks = Rank.getRanks().stream().sorted(Comparator.comparingInt(Rank::getNeededPoints)).collect(Collectors.toList());
            Collections.reverse(sortedRanks);

            for (int i = 0; i < sortedRanks.size(); i++) {
                String[] alphabet = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
                Team team = sb.getTeam(alphabet[i]);
                if(team == null){
                    team = sb.registerNewTeam(alphabet[i]);
                    team.setPrefix(sortedRanks.get(i).getFormattedName() + " ");
                }

                teams.put(sortedRanks.get(i).getName(), team);
            }

        }

        public static Scoreboard getBoard() {
            return sb;
        }

        public static HashMap<String, Team> getTeams() {
            return teams;
        }
    }

}
*/