package fr.toinetoine1.practice.core;

/*
    Created by Toinetoine1 on 26/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.inventory.list.KitChoice;
import fr.toinetoine1.practice.map.MapManager;
import fr.toinetoine1.practice.team.Team;
import fr.toinetoine1.practice.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Queue {

    @Getter
    private static Map<Mode, Queue> waitingQueue = new HashMap<>();
    @Getter
    private static Map<BadblockPlayer, QueueReporter> queueReporter = new HashMap<>();

    private Mode mode;
    private Map<Kit, List<BadblockPlayer>> inQueue;

    static {
        for (Mode modes : Mode.values())
            waitingQueue.put(modes, new Queue(modes));
    }

    public Queue(Mode mode) {
        this.mode = mode;
        this.inQueue = new HashMap<>();
        Kit.getDefaultsKit().get(mode).forEach(kit -> inQueue.put(kit, new ArrayList<>()));
    }

    public Mode getMode() {
        return mode;
    }

    private void check(Mode mode, Kit kit) {
        if (inQueue.get(kit).size() >= 2) {
            List<BadblockPlayer> team1 = new ArrayList<>(Arrays.asList(inQueue.get(kit).get(0)));
            List<BadblockPlayer> team2 = new ArrayList<>(Arrays.asList(inQueue.get(kit).get(1)));
            if (mode.getSize() > 1) {
                team1.addAll(Team.getTeam(inQueue.get(kit).get(0)).getSlave());
                team2.addAll(Team.getTeam(inQueue.get(kit).get(1)).getSlave());
            }
            fr.toinetoine1.practice.map.Map map = MapManager.getNewMap(mode);
            if (map == null) {
                team1.forEach(player -> player.sendMessage(Practice.PREFIX + "Aucune map n'est disponible. Veuillez attendre.."));
                team2.forEach(player -> player.sendMessage(Practice.PREFIX + "Aucune map n'est disponible. Veuillez attendre.."));
                return;
            }
            Stream.concat(team1.stream(), team2.stream()).collect(Collectors.toList()).forEach(Queue::removePlayer);
            Practice.getGames().add(new Game(mode, kit, map, team1, team2));
            KitChoice.getKitChoiceMap().get(mode).updateQueue(mode, kit);
        }

    }

    public Map<Kit, List<BadblockPlayer>> getInQueue() {
        return inQueue;
    }

    public static void addPlayerInQueue(BadblockPlayer badblockPlayer, Mode mode, Kit kit) {
        Queue queue = waitingQueue.get(mode);
        /*if (isAlreadyInQueue(badblockPlayer)) {
            removePlayer(badblockPlayer);
            KitChoice.getKitChoiceMap().get(mode).updateQueue(mode, kit);
            badblockPlayer.sendMessage(Practice.PREFIX + "Vous avez quitté une file d'attente");
        }*/
        queue.getInQueue().get(kit).add(badblockPlayer);
        queueReporter.put(badblockPlayer, new QueueReporter(mode, kit));
        KitChoice.getKitChoiceMap().get(mode).updateQueue(mode, kit);

        if(mode.getSize() > 1){
            Team team = Team.getTeam(badblockPlayer);
            team.getAllPlayers().forEach(badblockPlayer1 -> {
                badblockPlayer1.clearInventory();
                ItemStack item = new ItemBuilder(Material.MAGMA_CREAM).displayname("§cQuitter la file d'attente").build();
                for (int i = 0; i <= 8; i++) {
                    badblockPlayer1.getInventory().setItem(i, item);
                }
            });
        } else {
            badblockPlayer.clearInventory();
            ItemStack item = new ItemBuilder(Material.MAGMA_CREAM).displayname("§cQuitter la file d'attente").build();
            for (int i = 0; i <= 8; i++) {
                badblockPlayer.getInventory().setItem(i, item);
            }
        }

        badblockPlayer.sendMessage(Practice.PREFIX + "Vous avez rejoint la file d'attente pour " + kit.getFormattedName());
        badblockPlayer.sendMessage(Practice.PREFIX + "§3Mode: " + mode.getFormattedName());
        queue.check(mode, kit);
    }

    public static boolean isAlreadyInQueue(BadblockPlayer player) {
        boolean is = false;
        for (Collection<List<BadblockPlayer>> lists : getWaitingQueue().values().stream().map(Queue::getInQueue).map(Map::values).collect(Collectors.toList())) {
            for (List<BadblockPlayer> players : lists) {
                if (players.contains(player)) {
                    is = true;
                    break;
                }
            }
        }

        return is;
    }

    public static void removePlayer(BadblockPlayer player) {
        getWaitingQueue().values().forEach(queue1 -> queue1.getInQueue().values().forEach(badblockPlayers -> {
            badblockPlayers.remove(player);
        }));
        if (queueReporter.containsKey(player)) {
            QueueReporter queueReporter = getQueueReporter().get(player);
            KitChoice.getKitChoiceMap().get(queueReporter.getMode()).updateQueue(queueReporter.getMode(), queueReporter.getKit());
            getQueueReporter().remove(player);
        }
    }

    @Data
    @AllArgsConstructor
    private static class QueueReporter {

        private Mode mode;
        private Kit kit;

    }
}
