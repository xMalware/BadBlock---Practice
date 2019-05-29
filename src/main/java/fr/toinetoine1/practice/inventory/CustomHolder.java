package fr.toinetoine1.practice.inventory;

/*
    Created by Toinetoine1 on 25/04/2019
*/

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class CustomHolder implements InventoryHolder {

    protected final Map<Integer, Icon> icons = new HashMap<>();

    protected final int size;
    private final String title;

    public CustomHolder(int size, String title) {
        this.size = size;
        this.title = title;
    }

    public void setIcon(int position, Icon icon) {
        this.icons.put(position, icon);
    }

    public Icon getIcon(int position) {
        return this.icons.get(position);
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, this.size, this.title);

        for (Map.Entry<Integer, Icon> entry : this.icons.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().itemStack);
        }

        return inventory;
    }
}
