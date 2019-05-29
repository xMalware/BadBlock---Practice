package fr.toinetoine1.practice.team;

/*
    Created by Toinetoine1 on 25/04/2019
*/

import fr.badblock.api.common.utils.flags.GlobalFlags;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Team {

    @Getter
    private static List<Team> teams = new ArrayList<>();
    public static final String PREFIX = "§7[§aTeam§7] §6";

    private BadblockPlayer owner;
    private List<BadblockPlayer> slaves;
    private List<BadblockPlayer> invited;
    @Getter
    private List<Team> pendingDuel;

    public Team(BadblockPlayer owner) {
        this.owner = owner;
        this.slaves = new ArrayList<>();
        this.invited = new ArrayList<>();
        this.pendingDuel = new ArrayList<>();
    }

    public BadblockPlayer getOwner() {
        return owner;
    }

    public void setOwner(BadblockPlayer owner) {
        this.owner.sendMessage(Team.PREFIX+"Vous n'êtes plus le chef de cette Team");
        slaves.add(owner);
        this.owner = owner;
        slaves.remove(owner);
        sendGroupMessage(Team.PREFIX+"Le joueur "+owner.getName()+" est maintenant le chef de cette Team");
        getAllPlayers().forEach(badblockPlayer -> badblockPlayer.playSound(Sound.LEVEL_UP));
    }

    public List<BadblockPlayer> getSlave() {
        return slaves;
    }

    public int getSize(){
        return 1 + slaves.size();
    }

    public void disband(){
        owner.sendMessage(PREFIX+"Vous avez dissous la team");
        for(BadblockPlayer slave : slaves){
            slave.sendMessage(PREFIX+"Votre team a été dissoute !");
        }
        Team.getTeams().remove(this);
    }

    public void invitePlayer(BadblockPlayer target){
        if(!hasEnoughPlace(this)){
            owner.sendMessage(PREFIX+"Vous avez trop de membre dans votre groupe");
            return;
        }

        String key = "team_invite_"+owner.getName()+"_"+target.getName();
        GlobalFlags.set(key, 60000);

        TextComponent message = new TextComponent(owner.getName()+" vous invite pour rejoindre sa Team. Clique pour rejoindre");
        message.setColor(ChatColor.GOLD);
        TextComponent prefix = new TextComponent(Team.PREFIX);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/jointeam "+owner.getName()));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cClique ici !").create()));
        prefix.addExtra(message);
        target.spigot().sendMessage(prefix);
        target.sendMessage(PREFIX+"Vous avez 60 secondes pour accepter l'invitation");
        target.playSound(Sound.LEVEL_UP);

        invited.add(target);
    }

    public void kickPlayer(BadblockPlayer target){
        if(!slaves.contains(target))
            return;

        slaves.remove(target);
        target.sendMessage(PREFIX+"Vous avez été exclu de la Team par "+owner.getName());
        sendGroupMessage(PREFIX+"Le joueur "+target.getName()+" a été exclu par "+owner.getName());
    }


    public void sendGroupMessage(String message){
        owner.sendMessage(message);

        for(BadblockPlayer player : slaves){
            player.sendMessage(message);
        }
    }

    public static boolean isInTeam(BadblockPlayer player){
        boolean is = false;

        for(Team team : getTeams()){
            if(team.getOwner().getName().equals(player.getName()) || team.getSlave().contains(player)){
                is = true;
                break;
            }
        }

        return is;
    }

    public static boolean hasEnoughPlace(Team team){
        int maxSize = Arrays.stream(Mode.values()).filter(Mode::isRanked).mapToInt(Mode::getSize).max().getAsInt();

        if(team.getSize() == maxSize){
            return false;
        }
        return true;
    }

    public static boolean hasOwnTeam(BadblockPlayer player){
        boolean has = false;

        for(Team team : getTeams()){
            if(team.getOwner().getName().equals(player.getName())){
                has = true;
                break;
            }
        }
        return has;
    }

    public static Team getTeam(BadblockPlayer player){
        if(!isInTeam(player))
            return null;

        if(hasOwnTeam(player)){
            return getTeams().stream().filter(team -> team.getOwner().getName().equals(player.getName())).findFirst().get();
        } else {
            return getTeams().stream().filter(team -> team.getSlave().contains(player)).findFirst().get();
        }
    }

    public static boolean isSlave(BadblockPlayer player){
        boolean is = false;

        for(Team team : getTeams()){
            if(team.getSlave().contains(player)){
                is = true;
                break;
            }
        }
        return is;
    }

    public List<BadblockPlayer> getAllPlayers(){
        ArrayList<BadblockPlayer> players = new ArrayList<>();
        players.add(owner);
        players.addAll(slaves);
        return players;
    }

    public void sendDuel(BadblockPlayer badblockPlayer, Mode mode, Kit kit) {
        Team team = Team.getTeam(badblockPlayer);
        if(team == null)
            return;
        team.getPendingDuel().add(this);

        getOwner().sendMessage(Practice.PREFIX+"Le joueur §9"+badblockPlayer.getName()+" §evous défie en Duel");
        TextComponent message = new TextComponent("Clique pour accepter le duel");
        message.setColor(ChatColor.YELLOW);
        TextComponent prefix = new TextComponent(Practice.PREFIX);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel "+ badblockPlayer.getName()+ " " + mode.name()+" "+ kit.getName()));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cClique ici !").create()));
        prefix.addExtra(message);
        getOwner().spigot().sendMessage(prefix);
        getOwner().playSound(Sound.ARROW_HIT);
    }
}
