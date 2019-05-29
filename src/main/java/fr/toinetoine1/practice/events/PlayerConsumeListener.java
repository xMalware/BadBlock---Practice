package fr.toinetoine1.practice.events;

/*
    Created by Toinetoine1 on 12/05/2019
*/

import fr.badblock.gameapi.BadListener;
import fr.toinetoine1.practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerConsumeListener extends BadListener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        final Player player = e.getPlayer();

        if (e.getItem().getTypeId() == 373) {
            Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Practice.getInstance(), () -> player.setItemInHand(new ItemStack(Material.AIR)), 1L);
        }
    }

}
