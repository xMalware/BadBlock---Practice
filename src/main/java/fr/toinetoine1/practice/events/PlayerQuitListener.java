package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.database.request.DataRequest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends BadListener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BadblockPlayer badblockPlayer = (BadblockPlayer) event.getPlayer();
        PPlayer pPlayer = PPlayer.get(badblockPlayer);

        if (pPlayer != null)
            DataRequest.updatePlayer(pPlayer);

        System.out.println("tess22");

    }

}
