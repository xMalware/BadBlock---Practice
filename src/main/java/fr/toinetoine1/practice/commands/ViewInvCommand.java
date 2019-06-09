package fr.toinetoine1.practice.commands;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.inventory.list.EndGamePlayerInventory;
import fr.toinetoine1.practice.utils.FakeInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * Created by Toinetoine1 on 29/05/2019.
 */


public class ViewInvCommand extends AbstractCommand {

    public ViewInvCommand() {
        super("viewinv", null, BadblockPlayer.GamePermission.PLAYER);
        this.allowConsole(false);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {

        if(args.length == 1){
            BadblockPlayer badblockPlayer = (BadblockPlayer) sender;
            BadblockPlayer target = (BadblockPlayer) Bukkit.getPlayer(args[0]);

            if(!FakeInventory.getFakeInvs().containsKey(target)){
                badblockPlayer.sendMessage(Practice.PREFIX+"Impossible de voir l'inventaire de ce joueur !");
                return false;
            }

            FakeInventory fakeInventory = FakeInventory.getFakeInvs().get(target);
            badblockPlayer.openInventory(new EndGamePlayerInventory(target, fakeInventory).getInventory());
        }

        return false;
    }
}
