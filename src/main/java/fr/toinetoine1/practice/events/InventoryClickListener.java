package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 25/04/2019
*/

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.Practice;
import fr.toinetoine1.practice.data.Mode;
import fr.toinetoine1.practice.data.PPlayer;
import fr.toinetoine1.practice.data.kit.Kit;
import fr.toinetoine1.practice.data.kit.ModifiedKit;
import fr.toinetoine1.practice.inventory.ClickAction;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.inventory.list.kitmodifier.Modifier;
import fr.toinetoine1.practice.utils.ItemInventory;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryClickListener extends BadListener {

    @Getter
    private static Map<BadblockPlayer, ItemInventory> lastMoves = new HashMap<>();
    @Getter
    private static List<BadblockPlayer> canCloseInv = new ArrayList<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            BadblockPlayer player = (BadblockPlayer) event.getWhoClicked();

            if (player.getInventory().getName().equals(event.getInventory().getName())) {
                if (!player.hasAdminMode())
                    event.setCancelled(true);
            }

            //Check if the inventory is custom

            if (event.getView().getTopInventory().getHolder() instanceof CustomHolder) {
                System.out.println("cancel event");
                event.setCancelled(true);

                //Check if the item the player clicked on is valid
                ItemStack current = event.getCurrentItem();
                ItemStack cursor = event.getCursor();
                if (event.getRawSlot() == -999) return;

                //Get our CustomHolder
                CustomHolder customHolder = (CustomHolder) event.getView().getTopInventory().getHolder();
                System.out.println(event.getRawSlot());

                if (customHolder instanceof Modifier) {
                    if (current != null && current.getType() == Material.STAINED_GLASS_PANE || event.getRawSlot() >= 54)
                        return;
                    InventoryAction inventoryAction = event.getAction();
                    switch (inventoryAction) {
                        case MOVE_TO_OTHER_INVENTORY:
                        case HOTBAR_MOVE_AND_READD:
                        case HOTBAR_SWAP:
                            event.setCancelled(true);
                            return;
                    }
                    System.out.println("test");

                    if (current == null)
                        return;

                    if (current.getType() == Material.EMERALD && event.getRawSlot() == event.getInventory().getSize() - 1) {
                        System.out.println(current);
                        System.out.println(cursor);
                        if (cursor == null || cursor.getType() == Material.AIR) {
                            Inventory inventory = event.getView().getTopInventory();
                            lastMoves.remove(player);
                            canCloseInv.add(player);
                            List<ItemStack> newInv = new ArrayList<>();
                            for (int i = 36; i <= 44; i++) {
                                addItem(inventory, newInv, i);
                            }
                            for (int i = 0; i <= 26; i++) {
                                addItem(inventory, newInv, i);
                            }
                            PPlayer pPlayer = PPlayer.get(player);
                            String[] splitted = inventory.getName().split(" ");
                            System.out.println(splitted[0]);
                            System.out.println(splitted[2]);
                            Mode mode = Arrays.stream(Mode.values()).filter(mode1 -> ChatColor.stripColor(mode1.getFormattedName()).equals(ChatColor.stripColor(splitted[0]))).findFirst().get();
                            ItemStack[] contents = newInv.stream().toArray(value -> new ItemStack[value]);

                            System.out.println(pPlayer.getInfos().get(mode).getModified());
                            pPlayer.getInfos().get(mode).getModified().put(splitted[2], new ModifiedKit(splitted[2], contents));
                            System.out.println("new kit");
                            System.out.println(pPlayer.getInfos().get(mode).getModified());
                            player.closeInventory();
                            player.sendMessage(Practice.PREFIX + "Votre inventaire a été sauvegardé");
                        } else {
                            player.sendMessage(Practice.PREFIX + "Vous devez posez votre item avant !");
                        }
                        return;
                    } else if (current.getType() == Material.REDSTONE && event.getRawSlot() == event.getInventory().getSize() - 2) {
                        System.out.println(current);
                        System.out.println(cursor);
                        if (cursor == null || cursor.getType() == Material.AIR) {
                            lastMoves.remove(player);
                            canCloseInv.add(player);
                            player.closeInventory();
                            player.sendMessage(Practice.PREFIX + "Votre modification d'inventaire a été annulé");
                        } else {
                            player.sendMessage(Practice.PREFIX + "Vous devez posez votre item avant !");
                        }
                        return;
                    }

                    if (current.getType().equals(Material.AIR)) {
                        lastMoves.remove(player);
                    } else if (!current.getType().equals(Material.AIR) && cursor.getType().equals(Material.AIR)) {
                        lastMoves.put(player, new ItemInventory(current, event.getRawSlot()));
                    } else if (!current.getType().equals(Material.AIR) && !cursor.getType().equals(Material.AIR)) {
                        lastMoves.put(player, new ItemInventory(current, event.getRawSlot()));
                    }

                    GameAPI.logColor("§cUNCANCELED");
                    event.setCancelled(false);
                    return;
                }

                //Check if the clicked slot is any icon
                Icon icon = customHolder.getIcon(event.getRawSlot());
                if (icon == null) return;

                //Execute all the actions
                for (ClickAction clickAction : icon.getClickActions()) {
                    clickAction.execute(player, current);
                }
            }
        }


    }

    private void addItem(Inventory inventory, List<ItemStack> newInv, int i) {
        if (inventory.getItem(i) == null)
            newInv.add(new ItemStack(Material.AIR));
        newInv.add(inventory.getItem(i));
    }

}
