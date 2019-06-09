package fr.toinetoine1.practice.events;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.core.Game;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.stream.Collectors;

/**
 * Created by Toinetoine1 on 30/05/2019.
 */

public class BlockPlaceListener extends BadListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlaceBlock(BlockPlaceEvent event){
        BadblockPlayer player = (BadblockPlayer) event.getPlayer();

        if(player.hasAdminMode()){
            event.setBuild(true);
            event.setCancelled(false);
            return;
        }

        if(Game.isInGame(player)){
            Game game = Game.get(player);
            if(!game.isHasStarted()){
                event.setCancelled(true);
                event.getBlockPlaced().setType(Material.AIR);
            } else {
                game.getPlacedBlocks().add(event.getBlockPlaced());
                event.setBuild(true);
                event.setCancelled(false);
            }
        }
    }

}
