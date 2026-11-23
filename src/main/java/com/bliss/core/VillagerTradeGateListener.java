package com.bliss.core;

import com.bliss.villager.EventState;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

public final class VillagerTradeGateListener implements Listener {
    private static final int RESULT_SLOT = 2;
    private final GlobalState state;
    private final EventState eventState;

    public VillagerTradeGateListener(GlobalState var1, EventState var2, int var3) {
        this.state = var1;
        this.eventState = var2;
    }

    @EventHandler
    public void onMerchantResult(InventoryClickEvent var1) {
        if (!this.state.villagers(570366672)) {
            if (var1.getWhoClicked() instanceof Player var7) {
                EventState var20 = this.eventState;
                UUID var43 = var7.getUniqueId();
                if (var20.tradingWith(var43, 1785384040) != null) {
                    return;
                }
            }

            if (var1.getView().getTopInventory() instanceof MerchantInventory) {
                if (var1.getRawSlot() == 2) {
                    ItemStack var49 = var1.getCurrentItem();
                    if (var49 != null && !var49.getType().isAir()) {
                        var1.setCancelled(true);
                        if (var1.getWhoClicked() instanceof Player var9) {
                            Location var47 = var9.getLocation();
                            Sound var4 = Sound.ENTITY_VILLAGER_NO;
                            var9.playSound(var47, var4, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }
}
