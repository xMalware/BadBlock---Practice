package fr.toinetoine1.practice.utils;

/*
    Created by Toinetoine1 on 12/05/2019
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Data
public class ItemInventory {

    private ItemStack itemStack;
    private int index;

}
