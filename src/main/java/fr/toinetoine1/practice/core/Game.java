package fr.toinetoine1.practice.core;

/*
    Created by Toinetoine1 on 26/04/2019
*/

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.inventory.list.KitChoice;
import fr.toinetoine1.practice.inventory.list.LeaderBoardInv;
import fr.toinetoine1.practice.map.Map;
import fr.toinetoine1.practice.team.Team;
import fr.toinetoine1.practice.utils.EloCalculator;
import fr.toinetoine1.practice.utils.FakeInventory;
import fr.toinetoine1.practice.utils.HotBarSelector;
import fr.toinetoine1.practice.utils.ItemBuilder;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Firework;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
    private Location respawnPlace;
    private List<Block> placedBlocks;

    public Game(Mode mode, Kit kit, Map map, List<BadblockPlayer> team1, List<BadblockPlayer> team2) {
        this.mode = mode;
        this.kit = kit;
        this.map = map;
        this.team1 = new ArrayList<>();
        this.team2 = new ArrayList<>();
        this.placedBlocks = new ArrayList<>();
        this.hasStarted = false;
        this.respawnPlace = map.getLocations()[0];

        team1.forEach(badblockPlayer -> this.team1.add(new GamePlayer(badblockPlayer)));
        team2.forEach(badblockPlayer -> this.team2.add(new GamePlayer(badblockPlayer)));
        this.unions = Stream.concat(this.team1.stream(), this.team2.stream()).collect(Collectors.toList());

        KitChoice.getKitChoiceMap().get(mode).addIGPlayer(mode, kit, mode.getSize());
        this.startGame();
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
            gamePlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
        });
        AtomicInteger i = new AtomicInteger();
        team1.forEach(gamePlayer -> {
            StringBuilder sb = new StringBuilder();
            for (int i1 = 0; i1 < team2.size(); i1++) {
                GamePlayer bb = team2.get(i1);
                if (i1 != 0) {
                    sb.append("§8,");
                }

                sb.append(bb.getPPlayer().getCustomRank().getFormattedName()).append(" §9").append(bb.getPlayer().getName());
                if (getMode().isRanked()) {
                    int elo = ((RankedPlayerModeInfo) bb.getPPlayer().getInfos().get(mode)).getPoints();
                    sb.append("§7(§b").append(elo).append("§7)");
                }

            }
            gamePlayer.getPlayer().sendMessage(Practice.PREFIX + "Vous vous battez contre: " + sb.toString());
            gamePlayer.getPlayer().teleport(map.getLocations()[i.getAndIncrement()]);

        });
        team2.forEach(gamePlayer -> {
            StringBuilder sb = new StringBuilder();
            for (int i1 = 0; i1 < team1.size(); i1++) {
                GamePlayer bb = team1.get(i1);
                if (i1 != 0) {
                    sb.append("§8,");
                }

                sb.append(bb.getPPlayer().getCustomRank().getFormattedName()).append(" §9").append(bb.getPlayer().getName());
                if (getMode().isRanked()) {
                    int elo = ((RankedPlayerModeInfo) bb.getPPlayer().getInfos().get(mode)).getPoints();
                    sb.append("§7(§b").append(elo).append("§7)");
                }

            }
            gamePlayer.getPlayer().sendMessage(Practice.PREFIX + "Vous vous battez contre: " + sb.toString());
            gamePlayer.getPlayer().teleport(map.getLocations()[i.getAndIncrement()]);
        });
        this.addWorkable();
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

    public boolean checkWin(BadblockPlayer player) {
        if (countDeadPlayer(getTeam1()) == mode.getSize()) {
            Team loserTeam = Team.getTeam(getTeam1().get(0).getPlayer());
            Team winnerTeam = Team.getTeam(getTeam2().get(0).getPlayer());
            List<PPlayer> winner = getTeam2().stream().map(gamePlayer -> PPlayer.get(gamePlayer.getPlayer())).collect(Collectors.toList());
            List<PPlayer> loser = getTeam1().stream().map(gamePlayer -> PPlayer.get(gamePlayer.getPlayer())).collect(Collectors.toList());

            if (loserTeam == null || winnerTeam == null) {
                sendMessageToAllTeam(Practice.PREFIX + getTeam2().get(0).getPlayer().getName() + " gagne la partie !");
            } else {
                sendMessageToAllTeam(Practice.PREFIX + "La team de " + winnerTeam.getOwner().getName() + " gagne la partie !");
            }

            for (GamePlayer team2 : team2) {
                team2.setLife((int) team2.getPlayer().getHealth());
                sendInventoryMessage(team2.getPlayer(), team1, true);

                if (!team2.isDead()) {
                    launchFirework(team2.getPlayer());
                }
            }
            for (GamePlayer team1 : team1) {
                sendInventoryMessage(team1.getPlayer(), team2, false);
            }
            finish(winner, loser);
            return true;
        } else if (countDeadPlayer(getTeam2()) == mode.getSize()) {
            Team loserTeam = Team.getTeam(getTeam2().get(0).getPlayer());
            Team winnerTeam = Team.getTeam(getTeam1().get(0).getPlayer());
            List<PPlayer> winner = getTeam1().stream().map(gamePlayer -> PPlayer.get(gamePlayer.getPlayer())).collect(Collectors.toList());
            List<PPlayer> loser = getTeam2().stream().map(gamePlayer -> PPlayer.get(gamePlayer.getPlayer())).collect(Collectors.toList());

            if (loserTeam == null || winnerTeam == null) {
                sendMessageToAllTeam(Practice.PREFIX + getTeam1().get(0).getPlayer().getName() + " gagne la partie !");
            } else {
                sendMessageToAllTeam(Practice.PREFIX + "La team de " + winnerTeam.getOwner().getName() + " gagne la partie !");
            }

            for (GamePlayer team1 : team1) {
                team1.setLife((int) team1.getPlayer().getHealth());
                sendInventoryMessage(team1.getPlayer(), team2, true);

                if (!team1.isDead()) {
                    launchFirework(team1.getPlayer());
                }
            }
            for (GamePlayer team2 : team2) {
                sendInventoryMessage(team2.getPlayer(), team1, false);
            }

            finish(winner, loser);
            return true;
        }

        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage(Practice.PREFIX + "Vous êtes en spectateur le temps que la partie se termine");
        return false;
    }

    private void finish(List<PPlayer> winner, List<PPlayer> loser) {

        for (Game.GamePlayer gamePlayer1 : getUnions()) {
            ItemStack[] contents = gamePlayer1.getPlayer().getInventory().getContents().clone();
            ItemStack[] armorContents = gamePlayer1.getPlayer().getInventory().getArmorContents().clone();
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] != null) {
                    contents[i] = contents[i].clone();
                }
            }
            for (int i = 0; i < armorContents.length; i++) {
                if (armorContents[i] != null) {
                    armorContents[i] = armorContents[i].clone();
                }
            }
            FakeInventory.getFakeInvs().put(gamePlayer1.getPlayer(), new FakeInventory(contents, armorContents));

            gamePlayer1.getPlayer().clearInventory();
            gamePlayer1.getPlayer().getInventory().setArmorContents(null);
            if (gamePlayer1.getPlayer().getFireTicks() > 0)
                gamePlayer1.getPlayer().setFireTicks(0);
            ((CraftPlayer) gamePlayer1.getPlayer()).getHandle().setAbsorptionHearts(0.0f);
        }

        loser.forEach(pPlayer -> pPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1)));
        winner.forEach(pPlayer -> {
            pPlayer.getPlayer().setAllowFlight(true);
            pPlayer.getPlayer().setFlying(true);
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                winner.forEach(pPlayer -> {
                    pPlayer.getPlayer().setAllowFlight(false);
                    pPlayer.getPlayer().setFlying(false);
                });

                unions.forEach(gamePlayer -> {
                    BadblockPlayer player = gamePlayer.getPlayer();

                    player.setHealth(player.getMaxHealth());
                    player.setGameMode(GameMode.ADVENTURE);
                    player.teleport(Practice.getSpawnLoc());
                    gamePlayer.getPPlayer().getCustomStats().addGame();
                    for (PotionEffect effect : player.getActivePotionEffects())
                        player.removePotionEffect(effect.getType());
                    HotBarSelector.giveSelector(player);
                    LeaderBoardInv.getStatsMap().put(gamePlayer.getPlayer().getName(), (RankedPlayerModeInfo) gamePlayer.getPPlayer().getInfos().get(Mode.RANKEDONE));

                });
                LeaderBoardInv.sortMap();
                KitChoice.getKitChoiceMap().get(mode).removeIGPlayer(mode, kit, mode.getSize());

                if (mode.isRanked()) {
                    int winnerPoint = winner.stream().mapToInt(pPlayer -> ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).getPoints()).sum();
                    int loserPoint = loser.stream().mapToInt(pPlayer -> ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).getPoints()).sum();

                    for (BadblockPlayer player : winner.stream().map(PPlayer::getPlayer).collect(Collectors.toList())) {
                        PPlayer pPlayer = PPlayer.get(player);
                        int points = ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).getPoints();
                        ((RankedPlayerModeInfo) pPlayer.getInfos().get(mode)).setPoints((int) Math.ceil(points + (EloCalculator.calculateRatingChange(winnerPoint, loserPoint, 1) * 1.50) / mode.getSize()));

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

                placedBlocks.forEach(block -> block.setType(Material.AIR));
                map.setUsed(false);
            }
        }.runTaskLater(Practice.getInstance(), 60);

        removeWorkable();
        removeUnsafe();
        Practice.getGames().remove(this);
    }

    private int countDeadPlayer(List<GamePlayer> gamePlayers) {
        return (int) gamePlayers.stream().filter(GamePlayer::isDead).count();
    }

    public void sendMessageToAllTeam(String message) {
        List<GamePlayer> players = Stream.concat(team1.stream(), team2.stream()).collect(Collectors.toList());
        players.forEach(gamePlayer -> gamePlayer.getPlayer().sendMessage(message));
    }

    private void sendSoundToAllTeam(Sound sound) {
        List<GamePlayer> players = Stream.concat(team1.stream(), team2.stream()).collect(Collectors.toList());
        players.forEach(gamePlayer -> gamePlayer.getPlayer().playSound(sound));
    }

    private void addWorkable() {
        switch (kit.getName().toLowerCase()) {
            case "combo":
                this.unions.forEach(gamePlayer -> gamePlayer.getPlayer().setMaximumNoDamageTicks(7));
                break;
        }

        if (kit.getName().toLowerCase().contains("uhc")) {
            unions.forEach(gamePlayer -> {
                BadblockPlayer badblockPlayer = gamePlayer.getPlayer();
                badblockPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);

                ItemStack[] contents = badblockPlayer.getInventory().getContents();
                for (int i = 0; i < contents.length; i++) {
                    ItemStack itemStack = contents[i];
                    if (itemStack != null && itemStack.getType() == Material.GOLDEN_APPLE && itemStack.hasItemMeta()) {
                            ItemStack newItem = ItemBuilder.createHeadByData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTlhNzlkN2U1ZDFiYTczOWFiNDQ3MTY0M2U3NDRlZjc4MWY3YzFkNGVhNTJlZmM5OTE2OGQ2Y2I1NzMyMzI2In19fQ==", itemStack.getAmount(), "§bGolden Head", "");
                            badblockPlayer.getInventory().setItem(i, newItem);
                    }
                }
            });
        }

    }

    private void removeWorkable() {
        switch (kit.getName().toLowerCase()) {
            case "combo":
                this.unions.forEach(gamePlayer -> gamePlayer.getPlayer().setMaximumNoDamageTicks(20));
                break;
        }
        if (kit.getName().toLowerCase().contains("uhc")) {
            unions.forEach(gamePlayer -> gamePlayer.getPlayer().setGameMode(GameMode.ADVENTURE));
        }
    }

    private void removeUnsafe() {
        List<Material> unsafeBlocks = new ArrayList<>(Arrays.asList(Material.COBBLESTONE, Material.STONE, Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER, Material.OBSIDIAN));
        List<Block> blocks = map.getCuboid().getBlocks();
        blocks.forEach(block -> {
            if (unsafeBlocks.contains(block.getType()) || placedBlocks.contains(block))
                block.setType(Material.AIR);
        });

    }

    private void sendInventoryMessage(BadblockPlayer player, List<GamePlayer> gamePlayers, boolean hasWon) {
        TextComponent message = new TextComponent("Clique sur un joueur pour voir son inventaire: ");
        message.setColor(ChatColor.DARK_AQUA);

        for (GamePlayer gamePlayer : gamePlayers) {
            TextComponent txt = new TextComponent(gamePlayer.getPlayer().getName() + (hasWon ? "" : " (" + gamePlayer.getLife() + "§c❤§b)") + " ");
            txt.setColor(ChatColor.AQUA);
            txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewinv " + gamePlayer.getPlayer().getName()));
            txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cClique ici !").create()));
            message.addExtra(txt);
        }

        player.spigot().sendMessage(message);
    }

    @Data
    public class GamePlayer {

        private BadblockPlayer player;
        private PPlayer pPlayer;
        private boolean isDead;
        private int life;

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

    private static final Random RANDOM = new Random();

    private void launchFirework(BadblockPlayer player) {
        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(RANDOM.nextBoolean()).trail(RANDOM.nextBoolean()).with(FireworkEffect.Type.values()[RANDOM.nextInt(FireworkEffect.Type.values().length)])
                .withColor(Color.fromRGB(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255)))
                .withFade(Color.fromRGB(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255))).build());
        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }

}
