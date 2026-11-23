package com.bliss.honored;

import com.bliss.items.BlissCoreItems;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class JuggernautGemGuardListener implements Listener {
    private static final NamespacedKey JUG_BOND = NamespacedKey.fromString("blissmythics:juggernaut_bond");
    private static volatile boolean roleActive = false;

    public static void setRoleActive(boolean var0, int var1) {
        roleActive = var0;
    }

    public static boolean isJuggernaut(Player var0, int var1) {
        if (roleActive && JUG_BOND != null) {
            PersistentDataContainer var7 = var0.getPersistentDataContainer();
            NamespacedKey var3 = JUG_BOND;
            PersistentDataType var4 = PersistentDataType.STRING;
            if (var7.has(var3, var4)) {
                return true;
            }
        }

        return false;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPickup(EntityPickupItemEvent var1) {
        LivingEntity var5 = var1.getEntity();
        if (var5 instanceof Player) {
            if (isJuggernaut((Player)var5, 859003337)) {
                if (BlissCoreItems.gemIdOf(var1.getItem().getItemStack(), 382441079) >= 0) {
                    var1.setCancelled(true);
                }
            }
        }
    }
}
