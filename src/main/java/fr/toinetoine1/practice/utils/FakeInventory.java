package fr.toinetoine1.practice.utils;

import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Data;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Toinetoine1 on 29/05/2019.
 */

@Data
public class FakeInventory {

    @Getter
    private static Map<BadblockPlayer, FakeInventory> fakeInvs = new HashMap<>();

    private ItemStack[] contents;
    private ItemStack[] armor;

    public FakeInventory(ItemStack[] contents, ItemStack[] armor) {
        this.contents = contents;
        this.armor = armor;
    }
}
