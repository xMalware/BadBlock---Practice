package fr.toinetoine1.practice.data.kit;

/*
    Created by Toinetoine1 on 02/04/2019
*/

import fr.toinetoine1.practice.data.Mode;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kit {

    @Getter
    private static Map<Mode, List<Kit>> defaultsKit = new HashMap<>();

    private String name;
    private String formattedName;
    private ItemStack[] contents;
    private ItemStack[] armor;
    private ItemStack representedMats;

    public Kit(String name, String formattedName, ItemStack[] contents, ItemStack[] armor, ItemStack representedMats) {
        this.name = name;
        this.formattedName = formattedName;
        this.contents = contents;
        this.armor = armor;
        this.representedMats = representedMats;
    }

    public ItemStack getRepresentedMats() {
        return representedMats;
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

    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public void setRepresentedMats(ItemStack representedMats) {
        this.representedMats = representedMats;
    }
}
