package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 26/04/2019
*/

import fr.badblock.api.common.utils.flags.GlobalFlags;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.TimeUnit;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.core.Game;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.inventory.list.KitChoice;
import fr.toinetoine1.practice.inventory.list.statsandparameter.StatsAndParameter;
import fr.toinetoine1.practice.inventory.list.teamduel.TeamInv;
import fr.toinetoine1.practice.inventory.list.kitmodifier.ModeModifier;
import fr.toinetoine1.practice.team.Team;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerInteractListener extends BadListener {

    private Map<BadblockPlayer, Long> cooldownEnderpearl = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        BadblockPlayer player = (BadblockPlayer) event.getPlayer();
        if (handleEnderPearl(player, event.getItem(), event.getAction())) {
            event.setCancelled(true);
            player.updateInventory();
            return;
        }

        if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.MUSHROOM_SOUP && player.getMaxHealth() != player.getHealth() && Game.isInGame(player) && Game.get(player).getKit().getName().contains("Soup")) {
            double healtToAdd = player.getHealth() + 7;
            if (healtToAdd > player.getMaxHealth()) healtToAdd = player.getMaxHealth();
            player.setHealth(healtToAdd);
            player.playSound(Sound.DRINK);
            player.setItemInHand(new ItemStack(Material.AIR));
            player.updateInventory();
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getItemInHand() != null) {
                switch (player.getItemInHand().getType()) {
                    case BOOK:
                        player.openInventory(new ModeModifier().getInventory());
                        return;
                    case BOOKSHELF:
                        player.openInventory(new TeamInv(player).getInventory());
                        return;
                    case PAPER:
                        player.openInventory(new StatsAndParameter().getInventory());
                        return;
                }

                if (!Game.isInGame(player)) {
                    Team team = Team.getTeam(player);

                    switch (player.getItemInHand().getType()) {
                        case IRON_SWORD:
                            if (team == null) {
                                player.openInventory(new KitChoice(Mode.ONE).getInventory());
                            } else if (team.getOwner().getName().equals(player.getName())) {
                                if (team.getSize() == 2) {
                                    player.openInventory(new KitChoice(Mode.TWO).getInventory());
                                } else if (team.getSize() == 3) {
                                    player.openInventory(new KitChoice(Mode.THREE).getInventory());
                                } else if (team.getSize() == 1) {
                                    player.openInventory(new KitChoice(Mode.ONE).getInventory());
                                }
                            } else {
                                player.sendMessage(Practice.PREFIX + "Vous n'êtes pas la chef de votre groupe !");
                            }
                            break;
                        case DIAMOND_SWORD:
                            if (team == null) {
                                player.openInventory(new KitChoice(Mode.RANKEDONE).getInventory());
                            } else if (team.getOwner().getName().equals(player.getName())) {
                                if (team.getSize() == 2) {
                                    player.openInventory(new KitChoice(Mode.RANKEDTWO).getInventory());
                                } else if (team.getSize() == 1) {
                                    player.openInventory(new KitChoice(Mode.RANKEDONE).getInventory());
                                }
                            } else {
                                player.sendMessage(Practice.PREFIX + "Vous n'êtes pas la chef de votre groupe !");
                            }
                            break;
                    }
                }
            }
        }


    }

    private static int longTime = 5000;

    private boolean handleEnderPearl(BadblockPlayer player, ItemStack item, Action action) {
        if (item != null && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            if (!(item.getType() == Material.ENDER_PEARL))
                return false;
            if(!Game.isInGame(player)){
                return false;
            }
            if(!Game.get(player).isHasStarted()){
                return true;
            }

            if (cooldownEnderpearl.containsKey(player)) {
                if ((cooldownEnderpearl.get(player) > System.currentTimeMillis())) {
                    player.sendMessage(Practice.PREFIX + "Vous devez encore attendre §c" + TimeUnit.MILLIS_SECOND.toFrench(cooldownEnderpearl.get(player) - System.currentTimeMillis()));
                    return true;
                }
            }

            cooldownEnderpearl.put(player, System.currentTimeMillis() + longTime);
        }

        return false;
    }

}
