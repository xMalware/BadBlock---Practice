package fr.toinetoine1.practice.commands;

import fr.badblock.api.common.utils.time.Time;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.TimeUnit;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.core.Game;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.RankedPlayerModeInfo;
import fr.toinetoine1.practice.database.request.DataRequest;
import fr.toinetoine1.practice.database.request.StatsRequest;
import fr.toinetoine1.practice.inventory.list.LeaderBoardInv;
import fr.toinetoine1.practice.utils.HolographicScore;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;


/**
 * Created by Toinetoine1 on 25/05/2019.
 */

public class ManageCommand extends AbstractCommand {

    private static final String PREFIX = "§c[Manage]§6 ";

    public ManageCommand() {
        super("manage", null, BadblockPlayer.GamePermission.BMODERATOR);
        this.allowConsole(false);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        BadblockPlayer player = (BadblockPlayer) sender;

        if (args.length == 0) {
            sendHelpMessage(player);
        } else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("setpoints")) {
                if (args.length == 1) {
                    player.sendMessage("§e/manage setpoints <Mode> <Joueur> <ELO>  §7- §aChanger le nombre de BadPoints d'un Joueur");
                    return false;
                }

                if (Arrays.stream(Mode.values()).anyMatch(mode -> mode.name().toUpperCase().equalsIgnoreCase(args[1].toUpperCase()))) {
                    Mode mode = Mode.valueOf(args[1].toUpperCase());
                    if (!mode.isRanked()) {
                        player.sendMessage(PREFIX + "Ce mode n'est pas classé");
                        return false;
                    }

                    if (args.length == 2) {
                        player.sendMessage("§e/manage setpoints <Mode> <Joueur> <ELO>  §7- §aChanger le nombre de BadPoints d'un Joueur");
                        return false;
                    }

                    BadblockPlayer target = (BadblockPlayer) Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        player.sendMessage(PREFIX + "Ce joueur n'est pas en ligne");
                        return false;
                    }

                    if(args.length == 3){
                        player.sendMessage("§e/manage setpoints <Mode> <Joueur> <ELO>  §7- §aChanger le nombre de BadPoints d'un Joueur");
                        return false;
                    }

                    if (!isInteger(args[3])) {
                        player.sendMessage(PREFIX + "Vous devez saisir un chiffre");
                        return false;
                    }
                    int newPoints = Integer.parseInt(args[3]);
                    PPlayer pTarget = PPlayer.get(target);
                    ((RankedPlayerModeInfo) pTarget.getInfos().get(mode)).setPoints(newPoints);

                    pTarget.tryUpdateRank();
                    pTarget.getScoreboard().generate();
                    //Practice.getTabSort().removePlayer(target);
                    //TabSort.BukkitManager.getTeams().get(pTarget.getCustomRank().getName()).addEntry(target.getName());
                    player.sendMessage(PREFIX + "Vous avez changé le nombre de ELO de §9" + target.getName() + " §6dans le mode " + mode.getFormattedName() + " §6à §c" + newPoints);
                    target.sendMessage(Practice.PREFIX + "Votre nombre de ELO dans le mode " + mode.getFormattedName() + " §ea été modifié à §c" + newPoints);
                    LeaderBoardInv.getStatsMap().put(target.getName(), (RankedPlayerModeInfo) pTarget.getInfos().get(Mode.RANKEDONE));
                    LeaderBoardInv.sortMap();
                    HolographicScore.getInstance().updateHologram();
                } else {
                    KitCommand.sendModeMessage(player);
                }
            } else if(args[0].equalsIgnoreCase("addpoints")){
                if (args.length == 1) {
                    player.sendMessage("§e/manage addpoints <Mode> <Joueur> <ELO> §7- §aAjouter le nombre de BadPoints d'un Joueur");
                    return false;
                }

                if (Arrays.stream(Mode.values()).anyMatch(mode -> mode.name().toUpperCase().equalsIgnoreCase(args[1].toUpperCase()))) {
                    Mode mode = Mode.valueOf(args[1].toUpperCase());
                    if (!mode.isRanked()) {
                        player.sendMessage(PREFIX + "Ce mode n'est pas classé");
                        return false;
                    }

                    if (args.length == 2) {
                        player.sendMessage("§e/manage addpoints <Mode> <Joueur> <ELO> §7- §aAjouter le nombre de BadPoints d'un Joueur");
                        return false;
                    }

                    BadblockPlayer target = (BadblockPlayer) Bukkit.getPlayer(args[2]);
                    if (target == null) {
                        player.sendMessage(PREFIX + "Ce joueur n'est pas en ligne");
                        return false;
                    }

                    if(args.length == 3){
                        player.sendMessage("§e/manage addpoints <Mode> <Joueur> <ELO> §7- §aAjouter le nombre de BadPoints d'un Joueur");
                        return false;
                    }

                    if (!isInteger(args[3])) {
                        player.sendMessage(PREFIX + "Vous devez saisir un chiffre");
                        return false;
                    }
                    int newPoints = Integer.parseInt(args[3]);
                    PPlayer pTarget = PPlayer.get(target);
                    int totalPoints = ((RankedPlayerModeInfo) pTarget.getInfos().get(mode)).addPoints(newPoints);

                    pTarget.tryUpdateRank();
                    pTarget.getScoreboard().generate();
                    //Practice.getTabSort().removePlayer(target);
                    //TabSort.BukkitManager.getTeams().get(pTarget.getCustomRank().getName()).addEntry(target.getName());
                    player.sendMessage(PREFIX + "Le joueur §9" +target.getName()+" §6possède maintenant §c"+totalPoints+" §6dans le mode "+mode.getFormattedName());
                    target.sendMessage(Practice.PREFIX + "§6Votre nombre de points a soudeinement augmenté de §c"+newPoints+" §6dans le mode "+mode.getFormattedName());
                    target.sendMessage(Practice.PREFIX + "§6Vous avez maintenant §c"+totalPoints+" §ed'ELO");
                    LeaderBoardInv.getStatsMap().put(target.getName(), (RankedPlayerModeInfo) pTarget.getInfos().get(Mode.RANKEDONE));
                    LeaderBoardInv.sortMap();
                    HolographicScore.getInstance().updateHologram();
                } else {
                    KitCommand.sendModeMessage(player);
                }
            } else if(args[0].equalsIgnoreCase("info")){
                if(args.length == 1){
                    player.sendMessage("§e/manage info <Joueur> §7- §aInformations d'un joueur");
                    return false;
                }

                BadblockPlayer target = (BadblockPlayer) Bukkit.getPlayer(args[1]);
                PPlayer pTarget = PPlayer.get(target);
                player.sendMessage("§6Informations de §9"+target.getName()+"§6:");
                player.sendMessage("§9ELO: §3"+ ((RankedPlayerModeInfo) pTarget.getInfos().get(Mode.RANKEDONE)).getPoints());
                player.sendMessage("§9Rang: "+pTarget.getCustomRank().getFormattedName());
                player.sendMessage(" ");
                player.sendMessage("§9Next rank: "+ (pTarget.getNextRank() == pTarget.getCustomRank() ? "§6Aucun" : pTarget.getNextRank().getFormattedName()));
                player.sendMessage("§9Needed Points: §c"+ (pTarget.getNextRank() == pTarget.getCustomRank() ? "0" : (pTarget.getNextRank().getNeededPoints() - ((RankedPlayerModeInfo)pTarget.getInfos().get(Mode.RANKEDONE)).getPoints())));
                player.sendMessage(" ");
                player.sendMessage("§9Partie jouées: §c"+pTarget.getCustomStats().getGamePlayed());
                player.sendMessage("§9En partie: §c"+(Game.isInGame(target) ? "Oui" : "Non"));
                player.sendMessage("§9Temps total de connexion: §c"+ TimeUnit.MILLIS_SECOND.toFrench((System.currentTimeMillis() - pTarget.getCurrentTime()) + pTarget.getCustomStats().getTimePlayed(), TimeUnit.SECOND, TimeUnit.MONTH));

            } else if(args[0].equalsIgnoreCase("reloadMode")){
                if(args.length == 1){
                    player.sendMessage((player.hasPermission("practice.admin") ? "§a" : "§c")+"§e/manage reloadMode <Joueur> - Update de toutes les infos d'un Joueur");
                    return false;
                }

                BadblockPlayer target = (BadblockPlayer) Bukkit.getPlayer(args[1]);
                if(target == null){
                    player.sendMessage(PREFIX+"Joueur hors ligne");
                    return false;
                }
                PPlayer pTarget = PPlayer.get(target);
                pTarget.setInfos(DataRequest.reloadModeInfo(target));
                pTarget.setCustomStats(DataRequest.reloadCustomStats(target));
                StatsRequest.updateStats(player);
                player.sendMessage(PREFIX+"Toutes les modes du joueur ont bien été reload");

            } else {
                sendHelpMessage(player);
            }

        } else {
            sendHelpMessage(player);
        }

        return false;
    }

    private void sendHelpMessage(BadblockPlayer player) {
        player.sendMessage("§7§m                                                         ");
        player.sendMessage("§e/manage setpoints <Mode> <Joueur> <ELO>  §7- §aChanger le nombre de BadPoints d'un Joueur");
        player.sendMessage("§e/manage addpoints <Mode> <Joueur> <ELO> §7- §aAjouter le nombre de BadPoints d'un Joueur");
        player.sendMessage("§e/manage info <Joueur> §7- §aInformations d'un joueur");
        player.sendMessage((player.hasPermission("practice.admin") ? "§a" : "§c")+"§e/manage reloadMode <Joueur> - Update de toutes les infos d'un Joueur");
        player.sendMessage("§7§m                                                         ");
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }


}
