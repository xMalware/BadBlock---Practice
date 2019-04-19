package fr.toinetoine1.practice.events;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.database.request.DataRequest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener extends BadListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        BadblockPlayer badblockPlayer = (BadblockPlayer) event.getPlayer();
        DataRequest.selectUser(badblockPlayer);

        event.setJoinMessage("");
    }

}
