package fr.toinetoine1.practice.commands;

/*
    Created by Toinetoine1 on 27/04/2019
*/

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.core.Game;
import org.bukkit.command.CommandSender;

public class SpawnCommand extends AbstractCommand {

    public SpawnCommand() {
        super("spawn", null, BadblockPlayer.GamePermission.PLAYER);
        this.allowConsole(false);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        BadblockPlayer badblockPlayer = (BadblockPlayer) sender;

        if(Game.isInGame(badblockPlayer)){
            badblockPlayer.sendMessage(Practice.PREFIX+"Vous devez déja finir votre partie");
        } else {
            badblockPlayer.teleport(Practice.getSpawnLoc());
            badblockPlayer.sendMessage(Practice.PREFIX+"§7Téléportation..");
        }

        return false;
    }
}
