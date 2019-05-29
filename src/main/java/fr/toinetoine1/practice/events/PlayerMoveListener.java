package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 27/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.core.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event){
        BadblockPlayer player = (BadblockPlayer) event.getPlayer();

        if(event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ() || event.getFrom().getBlockY() != event.getTo().getBlockY()){
            if(Game.isInGame(player)){
                Game game = Game.get(player);
                if(!game.isHasStarted()){
                    player.teleport(event.getTo());
                }
            }
        }

    }


}
