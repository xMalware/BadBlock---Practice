package fr.toinetoine1.practice.inventory;

/*
    Created by Toinetoine1 on 25/04/2019
*/

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public void addAmount(int amount){
        int totalAmount = itemStack.getAmount() + amount;
        itemStack.setAmount(totalAmount > 64 ? 64 : totalAmount);
    }

    public void removeAmount(int amount){
        int totalAmount = itemStack.getAmount() - amount;
        itemStack.setAmount(totalAmount < 1 ? 1 : totalAmount);
    }

    public int getAmount(){
        return itemStack.getAmount();
    }

    public void setLore(List<String> lore){
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }
}
