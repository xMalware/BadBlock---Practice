package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.core.Game;
import fr.toinetoine1.practice.core.Queue;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.database.request.DataRequest;
import fr.toinetoine1.practice.team.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends BadListener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BadblockPlayer badblockPlayer = (BadblockPlayer) event.getPlayer();
        PPlayer pPlayer = PPlayer.get(badblockPlayer);
        pPlayer.getCustomStats().setTimePlayed((System.currentTimeMillis() - pPlayer.getCurrentTime()) + pPlayer.getCustomStats().getTimePlayed());
        event.setQuitMessage(null);

        if (Game.isInGame(badblockPlayer)) {
            Game game = Game.get(badblockPlayer);
            Game.GamePlayer gamePlayer = game.getUnions().stream().filter(gamePlayer1 -> gamePlayer1.getPlayer() == badblockPlayer).findFirst().get();
            gamePlayer.setDead(true);
            game.checkWin(badblockPlayer);
        }

        if(Queue.isAlreadyInQueue(badblockPlayer)){
            Queue.removePlayer(badblockPlayer);
        }

        if(Team.isInTeam(badblockPlayer)){
            Team team = Team.getTeam(badblockPlayer);

            if(team.getOwner().getName().equals(badblockPlayer.getName())){
                if(!team.getSlave().isEmpty()){
                    team.sendGroupMessage(Team.PREFIX+"§cLe chef a quitté le serveur !");
                    team.setOwner(team.getSlave().get(0), true);
                    team.getSlave().remove(0);
                } else {
                    Team.getTeams().remove(team);
                }
            } else {
                team.sendGroupMessage(Team.PREFIX+"Le joueur "+badblockPlayer.getName()+" a quitté le serveur");
                team.getSlave().remove(badblockPlayer);
                if(Queue.isAlreadyInQueue(team.getOwner())){
                    Queue.removePlayer(team.getOwner());
                    team.sendGroupMessage(Team.PREFIX+"Vous avez quitté la file d'attente");
                }
            }
        }

        if (pPlayer != null)
            DataRequest.updatePlayer(pPlayer);
    }

}
