package fr.toinetoine1.practice.events;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.database.request.DataRequest;
import fr.toinetoine1.practice.scoreboard.PracticeScoreboard;
import fr.toinetoine1.practice.utils.HotBarSelector;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerJoinListener extends BadListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        BadblockPlayer badblockPlayer = (BadblockPlayer) event.getPlayer();
        PPlayer pPlayer = DataRequest.selectUser(badblockPlayer);

        badblockPlayer.setGameMode(GameMode.ADVENTURE);

        for (PotionEffect effect : badblockPlayer.getActivePotionEffects())
            badblockPlayer.removePotionEffect(effect.getType());
        event.setJoinMessage("");

        badblockPlayer.teleport(Practice.getSpawnLoc());

        PracticeScoreboard practiceScoreboard = new PracticeScoreboard(badblockPlayer);
        pPlayer.setScoreboard(practiceScoreboard);

        HotBarSelector.giveSelector(badblockPlayer);

        badblockPlayer.sendMessage(Practice.PREFIX+"§6Bienvenue sur la Bêta-test du §bPractice");
        badblockPlayer.sendMessage(Practice.PREFIX+"§6Elle est disponible dans le but de le perfectionner et de trouver tous les potentiels bugs");
        badblockPlayer.sendMessage(Practice.PREFIX+"§6Si vous trouvez un bug §cmineur§6, merci de le report sur le discord staff dans le salon #bugs");
        badblockPlayer.sendMessage(Practice.PREFIX+"§6Si le bug est §cmajeur§6, envoyez-un MP à Toinetoine1 ;)");
    }

}
