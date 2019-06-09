package fr.toinetoine1.practice.events;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.core.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

/*
    Created by Toinetoine1 on 06/06/2019
*/

public class EntityRegainHealthListener extends BadListener {

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof BadblockPlayer) {
            BadblockPlayer badblockPlayer = (BadblockPlayer) event.getEntity();

            if (Game.isInGame(badblockPlayer)) {
                Game game = Game.get(badblockPlayer);
                if(game.getKit().getName().toLowerCase().contains("uhc".toLowerCase()))
                if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
