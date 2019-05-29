package fr.toinetoine1.practice.scoreboard;

/*
    Created by Toinetoine1 on 22/04/2019
*/

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.scoreboard.BadblockScoreboardGenerator;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import org.bukkit.ChatColor;

public class PracticeScoreboard extends BadblockScoreboardGenerator {

    private CustomObjective objective;
    private BadblockPlayer player;

    public PracticeScoreboard(BadblockPlayer player) {
        this.objective = GameAPI.getAPI().buildCustomObjective("practice");
        this.player = player;

        objective.showObjective(player);
        objective.setDisplayName("§6§lPractice");
        objective.setGenerator(this);

        objective.generate();
    }

    @Override
    public void generate() {
        PPlayer pPlayer = PPlayer.get(player);
        int i = 16;

        i--;
        objective.changeLine(i, "§6┌ §c&n" + player.getName());
        i--;
        objective.changeLine(i, "§6│");
        i--;
        objective.changeLine(i, "§6├§a§l Rang: §6" + pPlayer.getCustomRank().getFormattedName());
        i--;
        objective.changeLine(i, "§6├§a§l ELO: §6" + ((RankedPlayerModeInfo)pPlayer.getInfos().get(Mode.RANKEDONE)).getPoints());
        i--;
        objective.changeLine(i, "§6│");
        i--;
        objective.changeLine(i, "§6├§b§l Prochain rang: §6");
        i--;
        objective.changeLine(i, "§6├§b§l "+(pPlayer.getNextRank() == pPlayer.getCustomRank() ? "§6Aucun" : pPlayer.getNextRank().getFormattedName()));
        i--;
        objective.changeLine(i, "§6├§b§l Points nécessaires: ");
        i--;
        objective.changeLine(i, "§6├§5 "+(pPlayer.getNextRank() == pPlayer.getCustomRank() ? "0" : (pPlayer.getNextRank().getNeededPoints() - ((RankedPlayerModeInfo)pPlayer.getInfos().get(Mode.RANKEDONE)).getPoints())));
        i--;
        objective.changeLine(i, "§6│ ");
        i--;
        objective.changeLine(i, "§6└ " + ChatColor.LIGHT_PURPLE + "§lIP: §dplay.badblock.fr");
    }

}
