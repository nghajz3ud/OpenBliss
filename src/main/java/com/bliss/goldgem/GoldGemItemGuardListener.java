package com.bliss.goldgem;

import org.bukkit.block.Container;
import org.bukkit.block.Crafter;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public final class GoldGemItemGuardListener implements Listener {
    @EventHandler(
        priority = EventPriority.LOWEST
    )
    public void onItemDamaged(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Item var4) {
            DamageCause var12 = var1.getCause();
            DamageCause var3 = DamageCause.VOID;
            if (var12 != var3) {
                if (isProtected(var4.getItemStack(), 1601196813)) {
                    var1.setCancelled(true);
                    var4.setInvulnerable(true);
                    var4.setFireTicks(0);
                }
            }
        }
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent var1) {
        for (ItemStack var7 : var1.getInventory().getMatrix()) {
            if (var7 != null && isProtected(var7, 1601196813)) {
                var1.getInventory().setResult(null);
                return;
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onCraft(CraftItemEvent var1) {
        for (ItemStack var7 : var1.getInventory().getMatrix()) {
            if (var7 != null && isProtected(var7, 1601196813)) {
                var1.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPrepareSmithing(PrepareSmithingEvent var1) {
        for (ItemStack var7 : var1.getInventory().getContents()) {
            if (var7 != null && isProtected(var7, 1601196813)) {
                var1.setResult(null);
                return;
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onSmith(SmithItemEvent var1) {
        for (ItemStack var7 : var1.getInventory().getContents()) {
            if (var7 != null && isProtected(var7, 1601196813)) {
                var1.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onCrafterCraft(CrafterCraftEvent var1) {
        if (var1.getBlock().getState() instanceof Crafter var4) {
            for (ItemStack var8 : var4.getInventory().getContents()) {
                if (var8 != null && isProtected(var8, 1601196813)) {
                    var1.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onBundleClick(InventoryClickEvent var1) {
        ItemStack var4 = var1.getCursor();
        ItemStack var5 = var1.getCurrentItem();
        if (isBundle(var4, 236934840) && var5 != null && isProtected(var5, 1601196813)
            || isBundle(var5, 236934840) && var4 != null && isProtected(var4, 1601196813)) {
            var1.setCancelled(true);
        }
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

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent var1) {
        if (isProtected(var1.getEntity().getItemStack(), 1601196813)) {
            var1.getEntity().setWillAge(false);
            var1.getEntity().setInvulnerable(true);
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDespawn(ItemDespawnEvent var1) {
        if (isProtected(var1.getEntity().getItemStack(), 1601196813)) {
            var1.setCancelled(true);
            var1.getEntity().setWillAge(false);
        }
    }

    static boolean isProtected(ItemStack var0, int var1) {
        if (var0 == null) {
            return false;
        } else if (!GoldGem.isGoldGem(var0) && !RitualItems.isWireFragment(var0, 130584315) && !RitualItems.isFragmentCore(var0, 1590975450)) {
            if (var0.getItemMeta() instanceof BlockStateMeta var4) {
                if (!var4.hasBlockState()) {
                    return false;
                } else if (var4.getBlockState() instanceof Container var49) {
                    ItemStack[] var50 = var49.getInventory().getContents();
                    int var7 = var50.length;

                    for (int var8 = 0; var8 < var7; var8++) {
                        if (isProtected(var50[var8], 1601196813)) {
                            return true;
                        }
                    }

                    return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}
