package com.bliss.gems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public final class EntityClickGuardListener implements Listener {
    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onMelee(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var4) {
            DamageCause var12 = var1.getCause();
            DamageCause var3 = DamageCause.ENTITY_ATTACK;
            if (var12 != var3) {
                DamageCause var14 = var1.getCause();
                DamageCause var17 = DamageCause.ENTITY_SWEEP_ATTACK;
                if (var14 != var17) {
                    return;
                }
            }

            EntityClickGuard.markMelee(var4.getUniqueId(), 513078373);
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onEntityClick(PlayerInteractEntityEvent var1) {
        if (!(var1 instanceof PlayerInteractAtEntityEvent)) {
            EntityClickGuard.mark(var1.getPlayer().getUniqueId(), 1276880015);
        }
    }
}
