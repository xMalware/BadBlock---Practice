package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 21/05/2019
*/

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.core.Game;
import fr.toinetoine1.practice.team.Team;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener extends BadListener {

   @EventHandler(priority = EventPriority.HIGHEST)
   public void onDamage(EntityDamageEvent event){
       if(event.getEntity() instanceof BadblockPlayer){
           BadblockPlayer badblockPlayer = (BadblockPlayer) event.getEntity();
           if(!Game.isInGame(badblockPlayer)){
               event.setCancelled(true);
               return;
           }
           event.setCancelled(false);
       }
   }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageListener(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof BadblockPlayer && event.getEntity() instanceof BadblockPlayer){
            BadblockPlayer damaged = (BadblockPlayer) event.getEntity();
            BadblockPlayer damager = (BadblockPlayer) event.getDamager();

            if (Game.isInGame(damaged) && Game.isInGame(damager)) {
                Team team = Team.getTeam(damaged);
                if (team != null && team.getAllPlayers().contains(damager)) {
                    event.setCancelled(true);
                    return;
                }
            } else {

                event.setCancelled(true);
                return;
            }
        }

        event.setCancelled(false);
    }

}
