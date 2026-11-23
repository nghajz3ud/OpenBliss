package com.bliss.mythics;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public final class MythicItemGuardListener implements Listener {
    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onItemDamaged(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Item var4) {
            DamageCause var12 = var1.getCause();
            DamageCause var3 = DamageCause.VOID;
            if (var12 != var3) {
                if (isMythicGem(var4.getItemStack(), 942056978)) {
                    var1.setCancelled(true);
                    var4.setInvulnerable(true);
                    var4.setFireTicks(0);
                }
            }
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent var1) {
        if (isMythicGem(var1.getEntity().getItemStack(), 942056978)) {
            var1.getEntity().setWillAge(false);
            var1.getEntity().setInvulnerable(true);
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDespawn(ItemDespawnEvent var1) {
        if (isMythicGem(var1.getEntity().getItemStack(), 942056978)) {
            var1.setCancelled(true);
            var1.getEntity().setWillAge(false);
        }
    }

    @EventHandler
    public void onBundleClick(InventoryClickEvent var1) {
        ItemStack var4 = var1.getCursor();
        ItemStack var5 = var1.getCurrentItem();
        if (isBundle(var4, 1738743869) && isMythicGem(var5, 942056978) || isBundle(var5, 1738743869) && isMythicGem(var4, 942056978)) {
            var1.setCancelled(true);
        }
    }

    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onGemPlaced(BlockPlaceEvent var1) {
        if (isMythicGem(var1.getItemInHand(), 942056978)) {
            var1.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoinRestamp(PlayerJoinEvent var1) {
        PlayerInventory var5 = var1.getPlayer().getInventory();
        int var6 = 0;

        while (true) {
            int var18 = var5.getSize();
            if (var6 >= var18) {
                return;
            }

            ItemStack var7 = var5.getItem(var6);
            if (isMythicGem(var7, 942056978)) {
                restamp(var7, 480419486);
                var5.setItem(var6, var7);
            }

            var6++;
        }
    }

    private static void restamp(ItemStack var0, int var1) {
        ItemStack var17;
        if (MythicItems.isMythicBloodGem(var0, 824514243)) {
            var17 = MythicItems.bloodGem(1434502583);
        } else {
            var17 = MythicItems.aegisGem(1323939072);
        }

        ItemMeta var5 = var0.getItemMeta();
        ItemMeta var6 = var17.getItemMeta();
        Component var18 = var6.displayName();
        var5.displayName(var18);
        List var20 = var6.lore();
        var5.lore(var20);
        Integer var23 = var6.getCustomModelData();
        var5.setCustomModelData(var23);
        var0.setItemMeta(var5);
    }

    private static boolean isBundle(ItemStack var0, int var1) {
        boolean var8;
        if (var0 != null && var0.getType().name().endsWith("BUNDLE")) {
            var8 = true;
        } else {
            var8 = false;
        }

        return var8;
    }

    private static boolean isMythicGem(ItemStack var0, int var1) {
        boolean var6;
        if (!MythicItems.isMythicBloodGem(var0, 824514243) && !MythicItems.isMythicAegisGem(var0, 73386651)) {
            var6 = false;
        } else {
            var6 = true;
        }

        return var6;
    }
}
