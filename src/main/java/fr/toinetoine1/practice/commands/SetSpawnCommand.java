package fr.toinetoine1.practice.commands;

/*
    Created by Toinetoine1 on 27/04/2019
*/

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import org.bukkit.command.CommandSender;

public class SetSpawnCommand extends AbstractCommand {

    public SetSpawnCommand() {
        super("setspawn", null, BadblockPlayer.GamePermission.ADMIN);
        this.allowConsole(false);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        BadblockPlayer player = (BadblockPlayer) sender;

        if(args.length == 0){
            Practice.setSpawnLoc(player.getLocation());
            player.sendMessage(Practice.PREFIX+"Spawn modifié");
        }

        return false;
    }
}
