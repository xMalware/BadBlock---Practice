package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 27/04/2019
*/

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.events.fakedeaths.FakeDeathEvent;
import fr.badblock.gameapi.events.fakedeaths.FightingDeathEvent;
import fr.badblock.gameapi.events.fakedeaths.NormalDeathEvent;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.core.Game;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.map.Map;
import fr.toinetoine1.practice.utils.FakeInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerDeathListener extends BadListener {

    @EventHandler
    public void onDeath(FightingDeathEvent event) {
        BadblockPlayer player = event.getPlayer();
        event.setCancelled(true);
        event.setDeathMessage(null);
        death(event, player, event.getKiller(), event.getLastDamageCause());
    }

    @EventHandler
    public void onDeath2(NormalDeathEvent event) {
        event.setDeathMessage(null);
        event.setCancelled(true);
        death(event, event.getPlayer(), null, event.getLastDamageCause());
    }

    @EventHandler
    public void onDeath3(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    private void death(FakeDeathEvent e, BadblockPlayer player, Entity killer, EntityDamageEvent.DamageCause last) {
        if (Game.isInGame(player)) {
            Game game = Game.get(player);
            Game.GamePlayer gamePlayer;
            if (game.getTeam1().stream().anyMatch(player1 -> player1.getPlayer().getName().equals(player.getName()))) {
                gamePlayer = game.getTeam1().stream().filter(player1 -> player1.getPlayer().getName().equals(player.getName())).findAny().get();
            } else if (game.getTeam2().stream().anyMatch(player1 -> player1.getPlayer().getName().equals(player.getName()))) {
                gamePlayer = game.getTeam2().stream().filter(player1 -> player1.getPlayer().getName().equals(player.getName())).findAny().get();
            } else {
                return;
            }

            gamePlayer.setDead(true);
            if (killer != null) {
                if (killer instanceof BadblockPlayer) {
                    BadblockPlayer badKiller = (BadblockPlayer) killer;
                    if (player.getName().equals(killer.getName())) {
                        game.sendMessageToAllTeam(Practice.PREFIX + "Le joueur " + player.getName() + " s'est suicidé !");
                    } else {
                        game.sendMessageToAllTeam(Practice.PREFIX + "Le joueur " + player.getName() + " a été éliminé par " + killer.getName());
                        PPlayer pKiller = PPlayer.get(badKiller);
                        pKiller.getInfos().get(game.getMode()).addKill();
                        if (game.getMode().isRanked())
                            ((RankedPlayerModeInfo) pKiller.getInfos().get(game.getMode())).getStats().get(game.getKit().getName()).addKill();

                    }

                }
            } else {
                game.sendMessageToAllTeam(Practice.PREFIX + "Le joueur " + player.getName() + " est mort !");
            }

            PPlayer pPlayer = PPlayer.get(player);
            pPlayer.getInfos().get(game.getMode()).addDeath();
            if (game.getMode().isRanked())
                ((RankedPlayerModeInfo) pPlayer.getInfos().get(game.getMode())).getStats().get(game.getKit().getName()).addDeath();

            e.setLightning(false);
            e.getDrops().clear();
            if (!game.checkWin(player)) {
                e.setRespawnPlace(game.getRespawnPlace());
            } else {

            }
        }
    }

}
