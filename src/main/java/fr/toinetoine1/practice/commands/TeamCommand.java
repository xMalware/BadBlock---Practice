package fr.toinetoine1.practice.commands;

/*
    Created by Toinetoine1 on 26/04/2019
*/

import com.google.common.base.Joiner;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.stream.Collectors;

public class TeamCommand extends AbstractCommand {

    public TeamCommand() {
        super("team", null, BadblockPlayer.GamePermission.PLAYER);
        this.allowConsole(false);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        BadblockPlayer player = (BadblockPlayer) sender;

        if(args.length == 0){
            player.sendMessage(Team.PREFIX+"§e/team §7- §6Voir les commandes de Team");
            player.sendMessage(Team.PREFIX+"§e/team toggle §7- §6Activer/Désactiver les demandes de Team");
            player.sendMessage(Team.PREFIX+"§e/team info §7- §6Voir les informations de votre Team");
            player.sendMessage(Team.PREFIX+"§e/team create §7- §6Créer sa Team");
            player.sendMessage(Team.PREFIX+"§e/team leader <Pseuso> §7- §6Mettre un joueur chef");
            player.sendMessage(Team.PREFIX+"§e/team invite <Pseudo> §7- §6Ajouter un joueur");
            player.sendMessage(Team.PREFIX+"§e/team kick <Pseudo> §7- §6Supprimer un joueur");
            player.sendMessage(Team.PREFIX+"§e/team leave §7- §6Quitter son groupe");
            player.sendMessage(Team.PREFIX+"§e/team disband §7- §6Dissoudre sa Team");
        } else if(args.length == 1){
            if(args[0].equalsIgnoreCase("create")){
                if(Team.isInTeam(player)){
                    player.sendMessage(Team.PREFIX+"Vous avez déja une Team");
                    return false;
                } else {
                    Team.getTeams().add(new Team(player));
                    player.sendMessage(Team.PREFIX+"Vous avez créer votre propre Team");
                    player.sendMessage(Team.PREFIX+"Invitez des personnes avec /team invite <Pseudo>");
                    return true;
                }
            } else if(args[0].equalsIgnoreCase("leave")){
                if(!Team.isInTeam(player)){
                    player.sendMessage(Team.PREFIX+"Vous n'avez pas de Team");
                    return false;
                } else {
                    if(!Team.isSlave(player)){
                        player.sendMessage(Team.PREFIX+"Vous devez éxécuter la commande /team disband pour dissoudre votre Team");
                        return false;
                    }

                    Team team = Team.getTeam(player);
                    team.getSlave().remove(player);
                    team.sendGroupMessage(Team.PREFIX+"Le joueur §9"+player.getName()+" §6a quitté la Team");
                    player.sendMessage(Team.PREFIX+"Vous avez quitté la Team");
                    return true;
                }
            } else if(args[0].equalsIgnoreCase("disband")){
                if(!Team.isInTeam(player)){
                    player.sendMessage(Team.PREFIX+"Vous n'avez pas de Team");
                    return false;
                } else {
                    if(!Team.hasOwnTeam(player)){
                        player.sendMessage(Team.PREFIX+"Vous ne pouvez pas dissoudre la Team !");
                        return false;
                    }

                    Team team = Team.getTeam(player);
                    team.disband();
                    return true;
                }
            }else if(args[0].equalsIgnoreCase("info")){
                if(!Team.isInTeam(player)){
                    player.sendMessage(Team.PREFIX+"Vous n'avez pas de Team");
                    return false;
                } else {
                    Team team = Team.getTeam(player);

                    player.sendMessage(Team.PREFIX+"§cChef de team: §6" +team.getOwner().getName());
                    player.sendMessage(Team.PREFIX+"§cJoueurs dans la team: §3" + Joiner.on(", ").join(team.getSlave().stream().map(HumanEntity::getName).collect(Collectors.toList())));
                    return true;
                }
            }
        } else if(args.length == 2){
            if(args[0].equalsIgnoreCase("invite")){
                BadblockPlayer target = (BadblockPlayer) Bukkit.getPlayer(args[1]);

                if(target == null){
                    player.sendMessage(Team.PREFIX+"Joueur introuvable");
                    return false;
                }

                if(player.getName().equals(target.getName())){
                    player.sendMessage(Team.PREFIX+"Vous ne pouvez pas vous invitez vous même !");
                    return false;
                }

                if(Team.isSlave(player)){
                    player.sendMessage(Team.PREFIX+"Vous n'êtes pas le chef de cette Team");
                    return false;
                }

                Team team = Team.getTeam(player);
                if(team == null){
                    team = new Team(player);
                    Team.getTeams().add(team);
                    player.sendMessage(Team.PREFIX+"Vous avez maintenant votre propre Team");
                }

                team.invitePlayer(target);
                player.sendMessage(Team.PREFIX+"Vous avez invité "+target.getName()+" a rejoindre votre Team");

            } else if(args[0].equalsIgnoreCase("kick")){
                BadblockPlayer target = (BadblockPlayer) Bukkit.getPlayer(args[1]);

                if(target == null){
                    player.sendMessage(Team.PREFIX+"Joueur introuvable");
                    return false;
                }

                if(!Team.hasOwnTeam(player)){
                    player.sendMessage(Team.PREFIX+"Vous ne possédez pas de Team ou vous n'êtes pas le chef !");
                    return false;
                }
                Team team = Team.getTeam(player);

                if(!team.getSlave().contains(target)){
                    player.sendMessage(Team.PREFIX+"Ce joueur n'est pas dans votre Team");
                    return false;
                }

                team.kickPlayer(target);

            } else if(args[0].equalsIgnoreCase("leader")){
                if(!Team.hasOwnTeam(player)){
                    player.sendMessage(Team.PREFIX+"Vous ne possédez pas de Team ou vous n'êtes pas le chef !");
                    return false;
                }

                Team team = Team.getTeam(player);
                BadblockPlayer target = (BadblockPlayer) Bukkit.getPlayer(args[1]);

                if(target == null){
                    player.sendMessage(Team.PREFIX+"Joueur introuvable");
                    return false;
                }

                team.setOwner(target, false);
            }
        }

        return false;
    }
}
