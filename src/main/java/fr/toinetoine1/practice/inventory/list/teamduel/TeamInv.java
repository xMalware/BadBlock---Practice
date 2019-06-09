package fr.toinetoine1.practice.inventory.list.teamduel;

/*
    Created by Toinetoine1 on 15/05/2019
*/

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.inventory.InteractiveHolder;
import fr.toinetoine1.practice.team.Team;
import fr.toinetoine1.practice.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class TeamInv extends InteractiveHolder {

    private static List<Integer> deco = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53));
    private static List<Integer> teamSlot = new ArrayList<>(Arrays.asList(20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42));

    private BadblockPlayer player;
    private Team team;
    private int page;
    private int nextPageSlot;
    private int previousPageSlot;

    public TeamInv(BadblockPlayer player) {
        super(9 * 6, "§bListes des groupes");
        this.player = player;
        this.nextPageSlot = 43;
        this.previousPageSlot = 37;

        ItemStack itemDeco = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15)).displayname(" ").build();
        for (Integer integer : deco) {
            setIcon(integer, new Icon(itemDeco));
        }

        Team team = Team.getTeam(player);
        this.team = team;
        this.page = 1;

        List<String> ownLore = new ArrayList<>();
        if(team != null){
            for (BadblockPlayer loopPlayer : team.getAllPlayers()) {
                ownLore.add("§a- §9" + loopPlayer.getName() + (player == loopPlayer ? " §c(Vous)" : ""));
            }
        } else {
            ownLore.add("§9Créer en une avec /team create !");
        }
        setIcon(13, new Icon(createHead(player.getName(), (team == null ? "§cVous n'avez pas de Team" : "§6Votre team:"), ownLore)));
        openNewPage(0);
    }

    private void openNewPage(int teamIndex) {
        super.icons.values().forEach(icon -> icon.getClickActions().clear());
        List<Team> teams = new ArrayList<>(Team.getTeams());
        if (team != null)
            teams.remove(team);

        if (teams.size() == 0) {
            this.getInventory().setItem(teamSlot.get(0), new ItemBuilder(Material.BARRIER).displayname("§cAucune team disponible").build());
            return;
        }

        if (page > 1) {
            for (int slot : teamSlot) {
                this.getInventory().setItem(slot, new ItemStack(Material.AIR));
            }
            this.getInventory().setItem(nextPageSlot, new ItemStack(Material.AIR));
            setIcon(nextPageSlot, new Icon(Material.AIR));
            setIcon(previousPageSlot, new Icon(ItemBuilder.createHeadByData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==", 1, "§cPage précédente", ""))
                    .addClickAction(new ClickAction() {
                        @Override
                        public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                            getInventory().setItem(previousPageSlot, new ItemStack(Material.AIR));
                            page--;
                            openNewPage(teamSlot.size() * page - teamSlot.size() + (page > 1 ? 1 : 0));
                        }
                    }));
        } else {
            this.getInventory().setItem(previousPageSlot, new ItemStack(Material.AIR));
        }

        for (int i = teamIndex; i < teams.size(); i++) {
            Team otherTeam = teams.get(i);
            int realSlot = (i - ((page - 1) * teamSlot.size())) + (page == 1 ? 0 : (-1 * (page - 1)));

            if (teamSlot.size() == realSlot) {
                int finalI = i;
                setIcon(nextPageSlot, new Icon(ItemBuilder.createHeadByData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19", 1, "§cPage suivante", ""))
                        .addClickAction(new ClickAction() {
                            @Override
                            public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                                getInventory().setItem(nextPageSlot, new ItemStack(Material.AIR));
                                page++;
                                openNewPage(finalI + 1);
                            }

                        }));
                break;
            }
            // 16 - ((2 - 1) * 15)
            List<String> slaveLore = new ArrayList<>();
            for (BadblockPlayer slave : otherTeam.getSlave()) {
                slaveLore.add("§a- §e" + slave.getName());
            }
            slaveLore.add("§7Cliquez pour défier cette team");
            setIcon(teamSlot.get(this.page == 1 ? i : realSlot), new Icon(createHead(otherTeam.getOwner().getName(), "§3" + otherTeam.getOwner().getName(), slaveLore))
                    .addClickAction(new ClickAction() {
                        @Override
                        public void execute(BadblockPlayer badblockPlayer, ItemStack clickedItem) {
                            if (team == null) {
                                player.sendMessage(Practice.PREFIX + "Vous devez posséder une Team avec au minimum deux joueurs");
                                return;
                            } else if (team.getSlave().isEmpty()) {
                                player.sendMessage(Practice.PREFIX + "Il faut minimum deux joueurs !");
                                return;
                            }

                            if (!(team.getOwner() == badblockPlayer)) {
                                badblockPlayer.sendMessage(Team.PREFIX + "Vous devez être le chef de votre team pour accéder à cet inventaire");
                                badblockPlayer.closeInventory();
                                return;
                            }

                            SkullMeta skullMeta = (SkullMeta) clickedItem.getItemMeta();
                            Player pTarget = Bukkit.getPlayer(skullMeta.getOwner());
                            if (pTarget == null) {
                                badblockPlayer.sendMessage(Practice.PREFIX + "Ce joueur n'est plus en ligne !");
                            } else {
                                BadblockPlayer target = (BadblockPlayer) pTarget;
                                Team team = Team.getTeam(target);
                                if (team == null) {
                                    badblockPlayer.sendMessage(Practice.PREFIX + "Ce joueur n'a pas d'équipe !");
                                } else {
                                    badblockPlayer.openInventory(new TeamInvKit(Arrays.stream(Mode.values()).filter(mode -> !mode.isRanked()).filter(mode -> mode.getSize() == team.getSize()).findFirst().get(), team).getInventory());
                                }
                            }
                        }
                    }));
        }
    }

    private ItemStack createHead(String owner, String displayName, List<String> lore) {
        ItemStack ownHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta ownHeadMeta = (SkullMeta) ownHead.getItemMeta();

        ownHeadMeta.setDisplayName(displayName);
        ownHeadMeta.setLore(lore);
        ownHeadMeta.setOwner(owner);
        ownHead.setItemMeta(ownHeadMeta);

        return ownHead;
    }
}
