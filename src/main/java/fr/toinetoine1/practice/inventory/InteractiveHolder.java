package fr.toinetoine1.practice.inventory;

/*
    Created by Toinetoine1 on 18/05/2019
*/

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class InteractiveHolder extends CustomHolder{

    private Inventory inventory;

    public InteractiveHolder(int size, String title) {
        super(size, title);

        this.inventory = Bukkit.createInventory(this, size, title);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void setIcon(int position, Icon icon) {
        super.setIcon(position, icon);
        inventory.setItem(position, icon.itemStack);
    }
}
