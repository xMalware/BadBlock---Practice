package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import org.bukkit.inventory.ItemStack;

public class Kit {

    private String name;
    private String formattedName;
    private ItemStack[] contents;

    public Kit(String name, String formattedName, ItemStack[] contents) {
        this.name = name;
        this.formattedName = formattedName;
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public String getFormattedName() {
        return formattedName.replace("&", "§");
    }

    public ItemStack[] getContents() {
        return contents;
    }
}
