package fr.toinetoine1.practice.commands;

/*
    Created by Toinetoine1 on 19/05/2019
*/

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.core.Game;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.map.Map;
import fr.toinetoine1.practice.map.MapManager;
import fr.toinetoine1.practice.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class DuelCommand extends AbstractCommand {

    public DuelCommand() {
        super("duel", null, BadblockPlayer.GamePermission.PLAYER);
        this.allowConsole(false);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        if (args.length == 3) {
            BadblockPlayer invited = (BadblockPlayer) sender;
            BadblockPlayer inviter = (BadblockPlayer) Bukkit.getPlayer(args[0]);
            Mode mode = Mode.valueOf(args[1]);
            Kit kit = Kit.getDefaultsKit().get(mode).stream().filter(kit1 -> kit1.getName().equals(args[2])).findFirst().get();

            if (inviter == null) {
                invited.sendMessage(Practice.PREFIX + "Le joueur a déconnecté !");
                return false;
            }

            Team inviterTeam = Team.getTeam(inviter);
            Team invitedTeam = Team.getTeam(invited);

            if (invitedTeam == null) {
                invited.sendMessage(Practice.PREFIX + "Vous n'avez pas de Team ! Vous pouvez en créer une avec §9/team create");
                return false;
            }

            if (inviterTeam == null) {
                invited.sendMessage(Practice.PREFIX + "Le joueur adverse n'a pas d'équipe !");
                return false;
            }

            if(!inviterTeam.getPendingDuel().contains(invitedTeam)){
                invited.sendMessage(Practice.PREFIX+"Vous devez avant demander ce joueur en duel");
                return false;
            }

            if(Game.isInGame(invited)){
                invited.sendMessage(Practice.PREFIX+"Vous devez finir votre partie !");
                return false;
            }

            if(Game.isInGame(inviter)){
                invited.sendMessage(Practice.PREFIX+"L'adversaire est déja en combat !");
                return false;
            }

            if (invitedTeam.getSize() <= 1){
                invited.sendMessage(Practice.PREFIX+"Vous n'avez pas assez de joueurs ! (§c"+inviterTeam.getSize()+"§e)");
                return false;
            }

            if(!(invitedTeam.getSize() == inviterTeam.getSize())){
                invited.sendMessage(Practice.PREFIX+"Des joueurs dans les équipes sont manquants !");
                return false;
            }

            Map map = MapManager.getNewMap(mode);
            if(map == null){
                invitedTeam.getAllPlayers().forEach(player -> player.sendMessage(Practice.PREFIX+"Aucune map n'est disponible. Veuillez attendre.."));
                inviterTeam.getAllPlayers().forEach(player -> player.sendMessage(Practice.PREFIX+"Aucune map n'est disponible. Veuillez attendre.."));
                return false;
            }
            Game game = new Game(mode, kit, map, invitedTeam.getAllPlayers(), inviterTeam.getAllPlayers());
            Practice.getGames().add(game);

        }

        return false;
    }
}
