package fr.toinetoine1.practice.events;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Created by Toinetoine1 on 02/06/2019.
 */

public class PlayerTeleportListener extends BadListener {

    @EventHandler
    public void onTelepor(PlayerTeleportEvent event){
        BadblockPlayer badblockPlayer = (BadblockPlayer) event.getPlayer();

        if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL){
            event.setCancelled(true);
            Location location = event.getTo();
            location.setX(location.getBlockX() + 0.5);
            location.setZ(location.getBlockZ() + 0.5);
            badblockPlayer.teleport(location);
        }

    }

}
