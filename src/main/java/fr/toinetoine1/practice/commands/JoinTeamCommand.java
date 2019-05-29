package fr.toinetoine1.practice.commands;

/*
    Created by Toinetoine1 on 26/04/2019
*/

import fr.badblock.api.common.utils.flags.GlobalFlags;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

public class JoinTeamCommand extends AbstractCommand {

    public JoinTeamCommand() {
        super("jointeam", null, BadblockPlayer.GamePermission.PLAYER);
        this.allowConsole(false);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {

        if(args.length == 1){
            BadblockPlayer invited = (BadblockPlayer) sender;
            BadblockPlayer teamOwner = (BadblockPlayer) Bukkit.getPlayer(args[0]);
            String key = "team_invite_"+teamOwner.getName()+"_"+invited.getName();

            if(!GlobalFlags.has(key)){
                invited.sendMessage(Team.PREFIX+"L'invitation a expiré !");
                return false;
            }

            if(!Team.hasOwnTeam(teamOwner)){
                invited.sendMessage(Team.PREFIX+"Ce joueur ne possède plus de Team !");
                return false;
            }

            if(Team.isInTeam(invited)){
                invited.sendMessage(Team.PREFIX+"Vous êtes déja dans une équipe");
                return false;
            }

            Team team = Team.getTeam(teamOwner);
            if(team == null){
                invited.sendMessage(Team.PREFIX+"§cErreur..");
                return false;
            } else {
                team.sendGroupMessage(Team.PREFIX+"Le joueur §9"+invited.getName()+" §6a rejoint votre Team");
                team.getSlave().add(invited);
                invited.sendMessage(Team.PREFIX+"Vous avez rejoint la Team de §9"+teamOwner.getName());
                team.getAllPlayers().forEach(badblockplayer -> badblockplayer.playSound(Sound.LEVEL_UP));
                return true;
            }

        }

        return false;
    }
}
