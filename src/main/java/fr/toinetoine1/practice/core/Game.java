package fr.toinetoine1.practice.core;

/*
    Created by Toinetoine1 on 26/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.map.Map;
import fr.toinetoine1.practice.team.Team;
import fr.toinetoine1.practice.utils.EloCalculator;
import fr.toinetoine1.practice.utils.HotBarSelector;
import lombok.Data;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class Game implements Listener {

    private Mode mode;
    private Kit kit;
    private Map map;
    private List<GamePlayer> team1;
    private List<GamePlayer> team2;
    private List<GamePlayer> unions;
    private boolean hasStarted;
    private List<Block> placedBlocks;

    public Game(Mode mode, Kit kit, Map map, List<BadblockPlayer> team1, List<BadblockPlayer> team2) {
        this.mode = mode;
        this.kit = kit;
        this.map = map;
        this.team1 = new ArrayList<>();
        this.team2 = new ArrayList<>();
        this.placedBlocks = new ArrayList<>();
        this.hasStarted = false;

        team1.forEach(badblockPlayer -> this.team1.add(new GamePlayer(badblockPlayer)));
        team2.forEach(badblockPlayer -> this.team2.add(new GamePlayer(badblockPlayer)));
        this.unions = Stream.concat(this.team1.stream(), this.team2.stream()).collect(Collectors.toList());

        this.startGame();
        this.addWorkable();
        Practice.getInstance().getServer().getPluginManager().registerEvents(this, Practice.getInstance());
    }

    private void startGame() {
        sendMessageToAllTeam(Practice.PREFIX + "Votre partie va bientôt commencer..");
        unions.forEach(gamePlayer -> {
            PPlayer pPlayer = gamePlayer.getPPlayer();
            if (pPlayer.getInfos() != null && pPlayer.getInfos().containsKey(mode) && pPlayer.getInfos().get(mode).getModified() != null && pPlayer.getInfos().get(mode).getModified().containsKey(kit.getFormattedName())) {
                gamePlayer.getPlayer().getInventory().setContents(pPlayer.getInfos().get(mode).getModified().get(kit.getFormattedName()).getContents());
            } else {
                gamePlayer.getPlayer().getInventory().setContents(kit.getContents().clone());
            }
            gamePlayer.getPlayer().getInventory().setArmorContents(kit.getArmor().clone());
            gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
        });
        AtomicInteger i = new AtomicInteger();
        team1.forEach(gamePlayer -> {
            StringBuilder sb = new StringBuilder();
            for (int i1 = 0; i1 < team2.size(); i1++) {
                GamePlayer bb = team2.get(i1);
                if(i1 != 0){
                    sb.append("§8,");
                }

                sb.append(bb.getPPlayer().getCustomRank().getFormattedName()).append(" §9").append(bb.getPlayer().getName());
                if(getMode().isRanked()){
                    int elo = ((RankedPlayerModeInfo) bb.getPPlayer().getInfos().get(mode)).getPoints();
                    sb.append("§7(§b").append(elo).append("§7)");
                }

            }
            gamePlayer.getPlayer().sendMessage(Practice.PREFIX+"Vous vous battez contre: "+sb.toString());
            gamePlayer.getPlayer().teleport(map.getLocations()[i.getAndIncrement()]);

        });
        team2.forEach(gamePlayer -> {
            StringBuilder sb = new StringBuilder();
            for (int i1 = 0; i1 < team1.size(); i1++) {
                GamePlayer bb = team1.get(i1);
                if(i1 != 0){
                    sb.append("§8,");
                }

                sb.append(bb.getPPlayer().getCustomRank().getFormattedName()).append(" §9").append(bb.getPlayer().getName());
                if(getMode().isRanked()){
                    int elo = ((RankedPlayerModeInfo) bb.getPPlayer().getInfos().get(mode)).getPoints();
                    sb.append("§7(§b").append(elo).append("§7)");
                }

            }
            gamePlayer.getPlayer().sendMessage(Practice.PREFIX+"Vous vous battez contre: "+sb.toString());
            gamePlayer.getPlayer().teleport(map.getLocations()[i.getAndIncrement()]);
        });

        new BukkitRunnable() {
            int timer = 3;

            @Override
            public void run() {
                if (timer <= 0) {
                    setHasStarted(true);
                    sendMessageToAllTeam(Practice.PREFIX + "§c§nBattez vous !!");
                    sendSoundToAllTeam(Sound.LEVEL_UP);
                    cancel();
                    return;
                }

                sendMessageToAllTeam(Practice.PREFIX + "§c" + timer-- + "..");
                sendSoundToAllTeam(Sound.SUCCESSFUL_HIT);
            }
        }.runTaskTimer(Practice.getInstance(), 0, 20);
    }

    public void checkWin(BadblockPlayer player) {
        if (countDeadPlayer(getTeam1()) == mode.getSize()) {
            Team loserTeam = Team.getTeam(getTeam1().get(0).getPlayer());
            Team winnerTeam = Team.getTeam(getTeam2().get(0).getPlayer());
            List<PPlayer> winner = getTeam2().stream().map(gamePlayer -> PPlayer.get(gamePlayer.getPlayer())).collect(Collectors.toList());
            List<PPlayer> loser = getTeam1().stream().map(gamePlayer -> PPlayer.get(gamePlayer.getPlayer())).collect(Collectors.toList());

            if (loserTeam == null) {
                sendMessageToAllTeam(Practice.PREFIX + getTeam2().get(0).getPlayer().getName() + " gagne la partie !");
            } else {
                sendMessageToAllTeam(Practice.PREFIX + "La team de " + winnerTeam.getOwner().getName() + " gagne la partie !");
            }

            finish(winner, loser);
            return;
        } else if (countDeadPlayer(getTeam2()) == mode.getSize()) {
            Team loserTeam = Team.getTeam(getTeam2().get(0).getPlayer());
            Team winnerTeam = Team.getTeam(getTeam1().get(0).getPlayer());
            List<PPlayer> winner = getTeam1().stream().map(gamePlayer -> PPlayer.get(gamePlayer.getPlayer())).collect(Collectors.toList());
            List<PPlayer> loser = getTeam2().stream().map(gamePlayer -> PPlayer.get(gamePlayer.getPlayer())).collect(Collectors.toList());

            if (loserTeam == null) {
                sendMessageToAllTeam(Practice.PREFIX + getTeam1().get(0).getPlayer().getName() + " gagne la partie !");
            } else {
                sendMessageToAllTeam(Practice.PREFIX + "La team de " + winnerTeam.getOwner().getName() + " gagne la partie !");
            }

            finish(winner, loser);
            return;
        }

        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(Practice.PREFIX + "Vous êtes en spectateur le temps que la partie se termine");
    }

    private void finish(List<PPlayer> winner, List<PPlayer> loser) {
        unions.forEach(gamePlayer -> {
            BadblockPlayer player = gamePlayer.getPlayer();

            player.setHealth(player.getMaxHealth());
            player.setGameMode(GameMode.ADVENTURE);
            player.teleport(Practice.getSpawnLoc());
            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
            HotBarSelector.giveSelector(player);

            if (player.getFireTicks() > 0)
                player.setFireTicks(0);
        });

        if (mode.isRanked()) {
            int winnerPoint = winner.stream().mapToInt(pPlayer -> ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).getPoints()).sum();
            int loserPoint = loser.stream().mapToInt(pPlayer -> ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).getPoints()).sum();

            for (BadblockPlayer player : winner.stream().map(PPlayer::getPlayer).collect(Collectors.toList())) {
                PPlayer pPlayer = PPlayer.get(player);
                int points = ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).getPoints();
                ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).setPoints((int) Math.ceil((points + (EloCalculator.calculateRatingChange(winnerPoint, loserPoint, 1) * 1.50)) / mode.getSize()));

                pPlayer.tryUpdateRank();
                int newPoints = ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).getPoints();
                System.out.println("winner : actuel point:" + points + " new points: " + newPoints);

                pPlayer.getScoreboard().generate();
                //Practice.getTabSort().removePlayer(player);
                //TabSort.BukkitManager.getTeams().get(pPlayer.getCustomRank().getName()).addEntry(player.getName());
                player.sendMessage(Practice.PREFIX + "§aBravo ! §eVous avez maintenant §b" + newPoints + " §ed'ELO ! (Gain de §3" + (newPoints - points) + " §e)");
            }

            for (BadblockPlayer player : loser.stream().map(PPlayer::getPlayer).collect(Collectors.toList())) {
                PPlayer pPlayer = PPlayer.get(player);
                int points = ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).getPoints();
                ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).setPoints((int) Math.ceil(points + (EloCalculator.calculateRatingChange(loserPoint, winnerPoint, 0)) / mode.getSize()));

                pPlayer.tryUpdateRank();
                int newPoints = ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).getPoints();
                System.out.println("loser : actuel point:" + points + " new points: " + newPoints);

                pPlayer.getScoreboard().generate();
                //Practice.getTabSort().removePlayer(player);
                //TabSort.BukkitManager.getTeams().get(pPlayer.getCustomRank().getName()).addEntry(player.getName());
                player.sendMessage(Practice.PREFIX + "§cPerdu ! §eVous avez maintenant §b" + newPoints + " §eELO ! (Perte de §3" + (newPoints - points) + " §e)");
            }
        }

        this.removeWorkable();
        this.removeUnsafe();
        map.setUsed(false);
        placedBlocks.forEach(block -> block.setType(Material.AIR));
        HandlerList.unregisterAll(this);

        Practice.getGames().remove(this);
    }

    private int countDeadPlayer(List<GamePlayer> gamePlayers) {
        return (int) gamePlayers.stream().filter(GamePlayer::isDead).count();
    }

    public void sendMessageToAllTeam(String message) {
        List<GamePlayer> players = Stream.concat(team1.stream(), team2.stream()).collect(Collectors.toList());
        players.forEach(gamePlayer -> gamePlayer.getPlayer().sendMessage(message));
    }

    public void sendSoundToAllTeam(Sound sound) {
        List<GamePlayer> players = Stream.concat(team1.stream(), team2.stream()).collect(Collectors.toList());
        players.forEach(gamePlayer -> gamePlayer.getPlayer().playSound(sound));
    }

    private void addWorkable() {
        if (kit.getName().toLowerCase().contains("Combo".toLowerCase())) {
            this.unions.forEach(gamePlayer -> gamePlayer.getPlayer().setMaximumNoDamageTicks(7));
        }
    }

    private void removeWorkable() {
        if (kit.getName().toLowerCase().contains("Combo".toLowerCase())) {
            this.unions.forEach(gamePlayer -> gamePlayer.getPlayer().setMaximumNoDamageTicks(20));
        }
    }

    private void removeUnsafe() {
        List<Material> unsafeBlocks = new ArrayList<>(Arrays.asList(Material.COBBLESTONE, Material.STONE, Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER, Material.OBSIDIAN));
        List<Block> blocks = map.getCuboid().getBlocks();
        blocks.forEach(block -> {
            if (unsafeBlocks.contains(block.getType()))
                block.setType(Material.AIR);
        });

    }

    @Data
    public class GamePlayer {


        private BadblockPlayer player;
        private PPlayer pPlayer;
        private boolean isDead;

        public GamePlayer(BadblockPlayer player) {
            this.player = player;
            this.pPlayer = PPlayer.get(getPlayer());
            this.isDead = false;
        }
    }

    public static boolean isInGame(BadblockPlayer player) {
        boolean is = false;

        for (Game game : Practice.getGames()) {
            if (game.getUnions().stream().map(GamePlayer::getPlayer).collect(Collectors.toList()).contains(player)) {
                is = true;
                break;
            }
        }

        return is;
    }

    public static Game get(BadblockPlayer player) {
        return Practice.getGames().stream().filter(game -> game.getUnions().stream().map(GamePlayer::getPlayer).collect(Collectors.toList()).contains(player)).findFirst().get();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlaceBlock(BlockPlaceEvent event) {
        BadblockPlayer badblockPlayer = (BadblockPlayer) event.getPlayer();

        if (Game.isInGame(badblockPlayer) && this.unions.stream().map(GamePlayer::getPlayer).collect(Collectors.toList()).contains(badblockPlayer)) {
            placedBlocks.add(event.getBlockPlaced());
            event.setBuild(true);
            event.setCancelled(false);
        }


    }

}
