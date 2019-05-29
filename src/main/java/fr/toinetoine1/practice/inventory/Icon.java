package fr.toinetoine1.practice.inventory;

/*
    Created by Toinetoine1 on 25/04/2019
*/

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Icon {

    final ItemStack itemStack;
    private final List<ClickAction> clickActions = new CopyOnWriteArrayList<>();

    public Icon(Material material){
        this(new ItemStack(material));
    }

    public Icon(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Icon addClickAction(ClickAction clickAction) {
        this.clickActions.add(clickAction);
        return this;
    }

    public List<ClickAction> getClickActions() {
        return this.clickActions;
    }
}
