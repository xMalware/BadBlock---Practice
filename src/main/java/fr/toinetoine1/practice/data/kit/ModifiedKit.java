package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 14/05/2019
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public class ModifiedKit {

    private String kitName;
    private ItemStack[] contents;

}
