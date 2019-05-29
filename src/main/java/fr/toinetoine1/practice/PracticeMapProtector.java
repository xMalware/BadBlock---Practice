package fr.toinetoine1.practice;

import fr.toinetoine1.practice.core.Game;
import fr.toinetoine1.practice.team.Team;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.servers.MapProtector;

public class PracticeMapProtector implements MapProtector {

    @Override
    public boolean blockPlace(BadblockPlayer player, Block block) {
        return player.hasAdminMode();
    }

    @Override
    public boolean blockBreak(BadblockPlayer player, Block block) {
        return player.hasAdminMode();
    }

    @Override
    public boolean modifyItemFrame(BadblockPlayer player, Entity itemFrame) {
        return player.hasAdminMode();
    }

    @Override
    public boolean canLostFood(BadblockPlayer player) {
        return false;
    }

    @Override
    public boolean canUseBed(BadblockPlayer player, Block bed) {
        return false;
    }

    @Override
    public boolean canUsePortal(BadblockPlayer player) {
        return false;
    }

    @Override
    public boolean canDrop(BadblockPlayer player) {
        return player.hasAdminMode();
    }

    @Override
    public boolean canPickup(BadblockPlayer player) {
        return player.hasAdminMode();
    }

    @Override
    public boolean canFillBucket(BadblockPlayer player) {
        return true;
    }

    @Override
    public boolean canEmptyBucket(BadblockPlayer player) {
        return true;
    }

    @Override
    public boolean canInteract(BadblockPlayer player, Action action, Block block) {
        return true;
    }

    @Override
    public boolean canInteractArmorStand(BadblockPlayer player, ArmorStand entity) {
        return true;
    }

    @Override
    public boolean canInteractEntity(BadblockPlayer player, Entity entity) {
        return true;
    }

    @Override
    public boolean canEnchant(BadblockPlayer player, Block table) {
        return true;
    }

    @Override
    public boolean canBeingDamaged(BadblockPlayer player) {
        System.out.println("can Being Demaged333333333333");
        return true;
    }

    @Override
    public boolean healOnJoin(BadblockPlayer player) {
        return true;
    }

    @Override
    public boolean canBlockDamage(Block block) {
        return false;
    }

    @Override
    public boolean allowFire(Block block) {
        return false;
    }

    @Override
    public boolean allowMelting(Block block) {
        return false;
    }

    @Override
    public boolean allowBlockFormChange(Block block) {
        return false;
    }

    @Override
    public boolean allowPistonMove(Block block) {
        return false;
    }

    @Override
    public boolean allowBlockPhysics(Block block) {
        return true;
    }

    @Override
    public boolean allowLeavesDecay(Block block) {
        return false;
    }

    @Override
    public boolean allowRaining() {
        return false;
    }

    @Override
    public boolean modifyItemFrame(Entity itemframe) {
        return false;
    }

    @Override
    public boolean canSoilChange(Block soil) {
        return false;
    }

    @Override
    public boolean canSpawn(Entity entity) {
        return entity.getType().equals(EntityType.PLAYER) || entity.getType().equals(EntityType.ARROW);
    }

    @Override
    public boolean canCreatureSpawn(Entity creature, boolean isPlugin) {
        return isPlugin;
    }

    @Override
    public boolean canItemSpawn(Item item) {
        return true;
    }

    @Override
    public boolean canItemDespawn(Item item) {
        return true;
    }

    @Override
    public boolean allowExplosion(Location location) {
        return false;
    }

    @Override
    public boolean allowInteract(Entity entity) {
        return entity.getType().equals(EntityType.PLAYER);
    }

    @Override
    public boolean canCombust(Entity entity) {
        return true;
    }

    @Override
    public boolean canEntityBeingDamaged(Entity entity) {
        System.out.println("canEntityBeingDamaged2");
        return true;
    }

    @Override
    public boolean canEntityBeingDamaged(Entity entity, BadblockPlayer damagerPlayer) {
        System.out.println("canEntityBeingDamaged");
        if (!entity.getType().equals(EntityType.PLAYER))
        {
            System.out.println("return false");
            return false;
        }
        System.out.println(entity.getName());
        System.out.println(damagerPlayer.getName());

        return damageCheck(damagerPlayer, (BadblockPlayer) entity);
    }

    @Override
    public boolean destroyArrow() {
        return false;
    }

    private boolean damageCheck(BadblockPlayer killer, BadblockPlayer killed){
        if (Game.isInGame(killed) && Game.isInGame(killer)) {
            Team team = Team.getTeam(killed);
            if (team != null && team.getSlave().contains(killer)) {
                return false;
            }

            return true;
        }

        return false;
    }



}
