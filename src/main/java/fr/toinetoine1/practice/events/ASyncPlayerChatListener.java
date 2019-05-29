package fr.toinetoine1.practice.events;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Toinetoine1 on 25/05/2019.
 */

public class ASyncPlayerChatListener extends BadListener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        BadblockPlayer player = (BadblockPlayer) event.getPlayer();
        PPlayer pPlayer = PPlayer.get(player);

        event.setFormat("§9[§3"+((RankedPlayerModeInfo) pPlayer.getInfos().get(Mode.RANKEDONE)).getPoints()+"§9] " + pPlayer.getCustomRank().getFormattedName()+" "+player.getName()+"»§7 %2$s");
    }

}
