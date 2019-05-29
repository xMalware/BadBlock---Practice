package fr.toinetoine1.practice.commands;

/*
    Created by Toinetoine1 on 25/04/2019
*/

import com.google.common.base.Joiner;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.kit.Kit;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KitCommand extends AbstractCommand {

    private static Map<BadblockPlayer, KitEdit> edit = new HashMap<>();

    public KitCommand() {
        super("mkit", null, BadblockPlayer.GamePermission.ADMIN);
        this.allowConsole(false);
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        BadblockPlayer player = (BadblockPlayer) sender;

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("setitem")){
                if(!edit.containsKey(player)){
                    player.sendMessage("§cVous devez déja taper la commande: /mkit <Mode> add <Nom> <Nom formatté>");
                    return false;
                }

                if(player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR){
                    player.sendMessage("§cVous devez avoir un item en main !");
                    return false;
                }

                edit.get(player).getKit().setRepresentedMats(player.getInventory().getItemInHand());
                Kit.getDefaultsKit().get(edit.get(player).getMode()).add(edit.get(player).getKit());
                edit.remove(player);
                player.sendMessage("§cLe kit a été enregistrée !");
            }
        }

        if(args.length == 0){
            sendHelpMessage(player);
        } else if(args.length == 1){
            sendModeMessage(player);
        } else if(args.length == 2){
            if(Arrays.stream(Mode.values()).anyMatch(mode -> mode.name().toUpperCase().equalsIgnoreCase(args[0].toUpperCase()))){
                Mode mode = Mode.valueOf(args[0].toUpperCase());

                if(args[1].equalsIgnoreCase("view")){
                    player.sendMessage("§eVoici la liste des Kits disponibles:");
                    if(Kit.getDefaultsKit().get(mode).isEmpty()){
                        player.sendMessage("§cAucun");
                    } else {
                        for(Kit kit : Kit.getDefaultsKit().get(mode)){
                            player.sendMessage("§e§m                                              ");
                            player.sendMessage("§cNom: §e" +kit.getName());
                            player.sendMessage("§cNom formatté: " +kit.getFormattedName());
                            player.sendMessage("§cItem vitrine: "+kit.getRepresentedMats());
                        }
                        player.sendMessage("§e§m                                              ");
                    }
                }
            } else {
                sendModeMessage(player);
            }
        } else if(args.length == 3){
            if(Arrays.stream(Mode.values()).anyMatch(mode -> mode.name().toUpperCase().equalsIgnoreCase(args[0].toUpperCase()))){
                Mode mode = Mode.valueOf(args[0].toUpperCase());

                if(args[1].equalsIgnoreCase("delete")){
                    if(Kit.getDefaultsKit().get(mode).stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(args[2]))){
                        Kit kit = Kit.getDefaultsKit().get(mode).stream().filter(kit1 -> kit1.getName().equals(args[2])).findFirst().get();
                        Kit.getDefaultsKit().get(mode).remove(kit);
                        player.sendMessage("§cLe kit a bien été supprimé !");
                    }
                } else if(args[1].equalsIgnoreCase("givekit")){
                    if(Kit.getDefaultsKit().get(mode).stream().anyMatch(kit -> kit.getName().equals(args[2]))){
                        Kit kit = Kit.getDefaultsKit().get(mode).stream().filter(kit1 -> kit1.getName().equals(args[2])).findFirst().get();
                        player.clearInventory();
                        player.getInventory().setContents(kit.getContents());
                        player.getInventory().setArmorContents(kit.getArmor());
                        player.sendMessage("§cVous avez maintenant le kit "+kit.getFormattedName());
                        player.updateInventory();
                    } else {
                        player.sendMessage("Voici les kits disponibles: "+ Joiner.on(", ").join(Kit.getDefaultsKit().get(mode).stream().map(Kit::getName).collect(Collectors.toList())));
                    }
                }

            } else {
                sendModeMessage(player);
            }
        } else if(args.length >= 4){
            if(Arrays.stream(Mode.values()).anyMatch(mode -> mode.name().toUpperCase().equalsIgnoreCase(args[0].toUpperCase()))){
                Mode mode = Mode.valueOf(args[0].toUpperCase());

                if(args[1].equalsIgnoreCase("add")){
                    String name = args[2];
                    StringBuilder str = new StringBuilder();
                    for (int i = 3; i < args.length; i++) {
                        if(i != 3){
                            str.append(" ");
                        }

                        str.append(args[i]);
                    }

                    ItemStack[] contents = player.getInventory().getContents().clone();
                    ItemStack[] armorContents = player.getInventory().getArmorContents().clone();
                    for (int i = 0; i < contents.length; i++) {
                        if(contents[i] != null){
                            contents[i] = contents[i].clone();
                        }
                    }
                    for (int i = 0; i < armorContents.length; i++) {
                        if(armorContents[i] != null){
                            armorContents[i] = armorContents[i].clone();
                        }
                    }
                    Kit kit = new Kit(name, ChatColor.translateAlternateColorCodes('&', str.toString()), contents, armorContents, null);
                    edit.put(player, new KitEdit(mode, kit));
                    player.sendMessage("§cIl faut maintenant ajouter un item dans votre main et taper la commande /mkit setitem");
                }
            } else {
                sendModeMessage(player);
            }
        }

        return false;
    }

    private void sendHelpMessage(BadblockPlayer player) {
        player.sendMessage("§7§m                                                         ");
        player.sendMessage("§e/mkit <Mode> view §7- §aVoir les Kits disponibles pour un mode");
        player.sendMessage("§e/mkit <Mode> add <Nom> <Nom formatté> §7- §aAjouter un Kit");
        player.sendMessage("§e/mkit <Mode> delete <Nom> §7- §aSupprimer un kit");
        player.sendMessage("§e/mkit <Mode> givekit <Nom> §7- §aSe donner un kit");
        player.sendMessage("§7§m                                                         ");
    }

    public static void sendModeMessage(BadblockPlayer player){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < Mode.values().length; i++) {
            if(i != 0){
                str.append("§7,§9");
            }

            str.append(Mode.values()[i].name());
        }

        player.sendMessage(Practice.PREFIX+"Les modes disponibles sont: §9"+str.toString());
    }

    @Data
    @AllArgsConstructor
    public class KitEdit{
        private Mode mode;
        private Kit kit;
    }

}
