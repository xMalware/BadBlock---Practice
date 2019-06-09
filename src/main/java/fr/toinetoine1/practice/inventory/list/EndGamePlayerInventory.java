package fr.toinetoine1.practice.inventory.list;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.toinetoine1.practice.inventory.CustomHolder;
import fr.toinetoine1.practice.inventory.Icon;
import fr.toinetoine1.practice.utils.FakeInventory;

/**
 * Created by Toinetoine1 on 29/05/2019.
 */

public class EndGamePlayerInventory extends CustomHolder {

    public EndGamePlayerInventory(BadblockPlayer target, FakeInventory fakeInventory) {
        super(9 * 4, "§c"+target.getName());

        if(fakeInventory == null)
            return;

        for (int i = 0; i < fakeInventory.getContents().length; i++) {
            setIcon(i, new Icon(fakeInventory.getContents()[i]));
        }

        int armorSlot = 27;
        for (int i = armorSlot; i < fakeInventory.getArmor().length + armorSlot; i++) {
            setIcon(i, new Icon(fakeInventory.getArmor()[i - armorSlot]));
        }
    }

}
