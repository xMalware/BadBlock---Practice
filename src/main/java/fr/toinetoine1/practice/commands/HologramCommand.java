package fr.toinetoine1.practice.commands;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.utils.HolographicScore;
import org.bukkit.command.CommandSender;

/**
 * Created by Toinetoine1 on 13/06/2019.
 */

public class HologramCommand extends AbstractCommand {

    public HologramCommand() {
        super("h", null, BadblockPlayer.GamePermission.ADMIN);
        this.allowConsole(false);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        BadblockPlayer badblockPlayer = (BadblockPlayer) sender;

        if(args.length == 0){
            badblockPlayer.sendMessage("§c§m                                          ");
            badblockPlayer.sendMessage("§e/h kills: §7- §aChanger la localisation de l'Hologram §b(Kills)");
            badblockPlayer.sendMessage("§e/h deaths: §7- §aChanger la localisation de l'Hologram §b(Morts)");
            badblockPlayer.sendMessage("§e/h score: §7- §aChanger la localisation de l'Hologram §b(Score)");
            badblockPlayer.sendMessage("§c§m                                          ");
        } else {
            switch (args[0]){
                case "kills":
                    HolographicScore.getInstance().setKillsLoc(badblockPlayer.getLocation());
                    break;
                case "deaths":
                    HolographicScore.getInstance().setDeathsLoc(badblockPlayer.getLocation());
                    break;
                case "score":
                    HolographicScore.getInstance().setScoreLoc(badblockPlayer.getLocation());
                    break;
                default:
                    badblockPlayer.sendMessage("§cNom introuvable");
                    break;
            }
            HolographicScore.getInstance().updateHologram(true);
        }

        return false;
    }
}
