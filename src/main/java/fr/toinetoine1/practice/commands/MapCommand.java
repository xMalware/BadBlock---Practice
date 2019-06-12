package fr.toinetoine1.practice.commands;

/*
    Created by Toinetoine1 on 22/04/2019
*/

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.map.Map;
import fr.toinetoine1.practice.map.MapManager;
import fr.toinetoine1.practice.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

public class MapCommand extends AbstractCommand {

    private static java.util.Map<BadblockPlayer, Map> edit = new HashMap<>();
    private DecimalFormat decimalFormat;

    public MapCommand() {
        super("map", null, BadblockPlayer.GamePermission.ADMIN);
        allowConsole(false);
        this.decimalFormat = new DecimalFormat("0.##");
    }

    @Override
    public boolean executeCommand(CommandSender sender, String[] args) {
        BadblockPlayer player = (BadblockPlayer) sender;

        if (args.length == 0) {
            sendHelpMessage(player);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                player.sendMessage("§3Voici les Maps disponbles:");
                if (MapManager.getMaps().isEmpty())
                    player.sendMessage("§cAucune Map n'est enregistrée");
                else {
                    StringBuilder active = new StringBuilder();
                    StringBuilder inactive = new StringBuilder();

                    for (Map map : MapManager.getMaps()) {
                        if (map.isEnable()) {
                            active.append("§eID: §3").append(map.getKey()).append(" §eMode: §c").append(map.getMode().getFormattedName()).append(" §eUsed: §c").append(map.isUsed()).append(" §eName: §c").append(map.getMapName()).append("\n");
                        } else
                            inactive.append("§eID: §3").append(map.getKey()).append(" §eMode: §c").append(map.getMode().getFormattedName()).append(" §eUsed: §c").append(map.isUsed()).append(" §eName: §c").append(map.getMapName()).append("\n");
                    }

                    player.sendMessage("§cMap active: §e" + active.toString());
                    player.sendMessage("§cMap inactive: §e" + inactive.toString());

                }
            } else if (args[0].equalsIgnoreCase("view")) {
                if (!edit.containsKey(player)) {
                    player.sendMessage("§cVous devez déja créer une Map !");
                    return false;
                }
                Map map = edit.get(player);
                sendLocationMessage(player, map);
            } else if (args[0].equalsIgnoreCase("finish")) {
                if (!edit.containsKey(player)) {
                    player.sendMessage("§cVous devez déja créer une Map !");
                    return false;
                }
                Map map = edit.get(player);
                boolean canFinish = true;

                for (int i = 0; i <= ((map.getMode().getSize() * 2) - 1); i++) {
                    if (map.getLocations()[i] == null) {
                        canFinish = false;
                        break;
                    }
                }
                if (map.getCuboid().loc1 == null || map.getCuboid().loc2 == null) {
                    player.sendMessage("§cUne des positions du Cuboid est invalide !");
                    return false;
                } else {
                    map.getCuboid().initCuboid();
                }

                if (canFinish) {
                    MapManager.getMaps().add(map);
                    edit.remove(player);
                    player.sendMessage("§cVotre Map a bien été enregistrée !");
                } else {
                    player.sendMessage("§cImpossible, Une des positions est null !");
                }
            } else if (args[0].equalsIgnoreCase("cancel")) {
                if (!edit.containsKey(player)) {
                    player.sendMessage("§cVous devez déja créer une Map !");
                    return false;
                }

                edit.remove(player);
                player.sendMessage("§cVous avez annulé la création de Map !");
            }

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("help")) {
                if (args[1].equalsIgnoreCase("1")) {
                    sendHelpMessage(player);
                } else if (args[1].equalsIgnoreCase("2")) {
                    player.sendMessage("§m§7                                                         ");
                    player.sendMessage("§ePour créer une Map, vous devez éxécuter la commande §e/map create");
                    player.sendMessage("§eVoici les commandes disponible pendant la création d'une Map:");
                    sendMapEditMessage(player, "?");
                    player.sendMessage("§m§7                                                         ");

                } else {
                    player.sendMessage("§cErreur, Usage: §e/map help <1/2>");
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (MapManager.getMaps().stream().filter(q -> isInteger(args[1])).anyMatch(s -> Integer.parseInt(args[1]) == s.getKey())) {
                    Map map = MapManager.getMaps().stream().filter(s -> Integer.parseInt(args[1]) == s.getKey()).findFirst().get();
                    MapManager.getMaps().remove(map);

                    player.sendMessage("§cLa Map a été supprimée");
                } else {
                    player.sendMessage("§cNom introuvable");
                }
            } else if (args[0].equalsIgnoreCase("enable")) {
                if (MapManager.getMaps().stream().filter(q -> isInteger(args[1])).anyMatch(s -> Integer.parseInt(args[1]) == s.getKey())) {
                    Map map = MapManager.getMaps().stream().filter(s -> Integer.parseInt(args[1]) == s.getKey()).findFirst().get();
                    map.setEnable(true);

                    player.sendMessage("§cVous avez activé cette Map");
                } else {
                    player.sendMessage("§cValeur introuvable");
                }
            } else if (args[0].equalsIgnoreCase("disable")) {
                if (MapManager.getMaps().stream().filter(q -> isInteger(args[1])).anyMatch(s -> Integer.parseInt(args[1]) == s.getKey())) {
                    Map map = MapManager.getMaps().stream().filter(s -> Integer.parseInt(args[1]) == s.getKey()).findFirst().get();
                    map.setEnable(false);

                    player.sendMessage("§cVous avez désactivé cette Map");
                } else {
                    player.sendMessage("§cValeur introuvable");
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (!edit.containsKey(player)) {
                    player.sendMessage("§cVous devez déja créer une Map !");
                    return false;
                }

                if (!isInteger(args[1])) {
                    player.sendMessage("§cIl faut saisir un nombre !");
                    return false;
                }

                Mode mode = edit.get(player).getMode();
                int i = Integer.parseInt(args[1]);
                if (i < 1 || i > (mode.getSize() * 2)) {
                    player.sendMessage("§cVous devez saisir un nombre entre 1 et " + mode.getSize() * 2 + " !");
                    return false;
                }

                edit.get(player).getLocations()[i - 1] = player.getLocation();
                player.sendMessage("§cLa location a bien été enregistrée !");
            } else if (args[0].equalsIgnoreCase("setcuboid")) {
                if (!edit.containsKey(player)) {
                    player.sendMessage("§cVous devez déja créer une Map !");
                    return false;
                }

                if (!isInteger(args[1])) {
                    player.sendMessage("§cIl faut saisir un nombre !");
                    return false;
                }

                int i = Integer.parseInt(args[1]);
                if (i != 1 && i != 2) {
                    player.sendMessage("§cVous devez saisir soit 1 ou 2 !");
                    return false;
                }

                edit.get(player).getCuboid().setLocation(i, player.getLocation());
                player.sendMessage("§cLa position " + i + " du cuboid a bien enregistré");
            } else if (args[0].equalsIgnoreCase("tp")) {
                if (MapManager.getMaps().stream().map(Map::getMapName).anyMatch(s -> s.equals(args[1]))) {
                    Map map = MapManager.getMaps().stream().filter(map1 -> map1.getMapName().equals(args[1])).findFirst().get();

                    player.teleport(map.getLocations()[0]);
                    player.sendMessage("§cVous avez été TP à la map: §9" + map.getMapName());
                }

            }

        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                if (edit.containsKey(player)) {
                    player.sendMessage("§cVous êtes déja dans le mode d'édition, si vous souhaitez quitter ce mode, éxécuter: /map cancel");
                    return false;
                }

                if (Arrays.stream(Mode.values()).anyMatch(mode -> mode.name().toUpperCase().equalsIgnoreCase(args[1].toUpperCase()))) {
                    Mode mode = Mode.valueOf(args[1].toUpperCase());

                    player.sendMessage("§eBienvenue dans le créateur de Map, voici les commandes disponibles:");
                    sendMapEditMessage(player, mode.getSize() * 2 + "");
                    edit.put(player, new Map(false, MapManager.incrementAndGet(), args[2], new Location[6], false, mode, new Cuboid()));
                } else {
                    KitCommand.sendModeMessage(player);
                }

            }
        }

        return false;
    }

    private void sendMapEditMessage(BadblockPlayer player, String nb) {
        player.sendMessage("   ");
        player.sendMessage("§e/map set <1 à " + nb + ">: §7- §aIndique les positions de la Map");
        player.sendMessage("§e/map setcuboid <1/2>: §7- §aCuboid de la Map");
        player.sendMessage("§e/map view: §7- §aVoir les positions actuelles");
        player.sendMessage("§e/map finish: §7- §aIndiquer que la configuration est terminée");
        player.sendMessage("§e/map cancel: §7- §aInterrompre la création d'une nouvelle Map");
    }

    private void sendHelpMessage(CommandSender player) {
        player.sendMessage("§m§7                                                         ");
        player.sendMessage("§e/map help <1/2> §7- §aVoir l'aide");
        player.sendMessage("§e/map create <Mode> <Nom> §7- §aCréer une nouvelle Map");
        player.sendMessage("§e/map delete <Key> §7- §aSupprime une nouvelle Map");
        player.sendMessage("§e/map tp <Nom> §7- §aSe TP à une Map");
        player.sendMessage("§e/map enable <Key> §7- §aActiver une Map");
        player.sendMessage("§e/map disable <Key> §7- §aDésactiver une Map");
        player.sendMessage("§e/map list §7- §aVoir les Maps activés/désactivés");
        player.sendMessage("                                                         ");
        player.sendMessage("§eVoir la page d'aide 2 pour créer une Map");
        player.sendMessage("§m§7                                                         ");
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    private void sendLocationMessage(BadblockPlayer player, Map map) {
        player.sendMessage("§8Key: §7" + map.getKey());
        StringBuilder loc1 = new StringBuilder();
        StringBuilder loc2 = new StringBuilder();
        for (int i = 0; i < map.getLocations().length; i++) {
            if (map.getLocations()[i] == null) {
                if (i <= 2) {
                    loc1.append("§cAucune");
                } else {
                    loc2.append("§cAucune");
                }
                continue;
            }
            Location location = map.getLocations()[i];

            if (i < 2) {
                loc1.append("§3Element: §6" + i + 1 + " §3Location: \n" +
                        "§eMonde: " + location.getWorld().getName() + "\n" +
                        "§eX: " + decimalFormat.format(location.getX()) + "\n" +
                        "§eY: " + decimalFormat.format(location.getX()) + "\n" +
                        "§eZ: " + decimalFormat.format(location.getX()) + "\n" +
                        "§eY: " + decimalFormat.format(location.getYaw()) + "\n" +
                        "§eY: " + decimalFormat.format(location.getPitch()) + "\n")
                        .append("\n");
            } else {
                loc2.append("§3Element: §6" + i + 1 + " §3Location: \n" +
                        "§eMonde: " + location.getWorld().getName() + "\n" +
                        "§eX: " + decimalFormat.format(location.getX()) + "\n" +
                        "§eY: " + decimalFormat.format(location.getX()) + "\n" +
                        "§eZ: " + decimalFormat.format(location.getX()) + "\n" +
                        "§eY: " + decimalFormat.format(location.getYaw()) + "\n" +
                        "§eY: " + decimalFormat.format(location.getPitch()) + "\n")
                        .append("\n");
            }

        }

        player.sendMessage("§8Equipe §c1: §7" + loc1.toString());
        player.sendMessage("§8Equipe §c2: §7" + loc2.toString());
        player.sendMessage("   ");
    }
}
