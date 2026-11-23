package com.bliss.core;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingTrimRecipe;

public final class NetheriteSmithingListener implements Listener {
    private final GlobalState state;

    public NetheriteSmithingListener(GlobalState var1, int var2) {
        this.state = var1;
    }

    @EventHandler
    public void onPrepareSmithing(PrepareSmithingEvent var1) {
        GlobalState var4 = GlobalState.forWorld(var1.getView().getPlayer().getWorld(), 728926677);
        if (!var4.netheriteAllowed(613865620) && var4.progArmorLimit(1353714115)) {
            if (!(var1.getInventory().getRecipe() instanceof SmithingTrimRecipe)) {
                ItemStack var5 = var1.getResult();
                if (var5 != null) {
                    if (isNetheriteGear(var5.getType(), 1857626685)) {
                        var1.setResult(null);
                    }
                }
            }
        }
    }

    private static boolean isNetheriteGear(Material var0, int var1) {
        return switch (var0) {
            case NETHERITE_SWORD, NETHERITE_PICKAXE, NETHERITE_AXE, NETHERITE_SHOVEL, NETHERITE_HOE, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS-> true;
            default -> false;
        };
    }
}
