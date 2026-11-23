package com.bliss.gems;

import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import java.util.Random;
import java.util.UUID;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public final class FirePassivesListener implements Listener {
    private static final int FLAME_STRIKE_CHANCE_PERCENT = 15;
    private static final int FLAME_STRIKE_FIRE_TICKS = 80;
    private final PlayerDataStore players;
    private final TrustStore trust;
    private final Random random;

    public FirePassivesListener(PlayerDataStore var1, TrustStore var2, int var3) {
        Random var5 = new Random();
        this.random = var5;
        this.players = var1;
        this.trust = var2;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onFlameStrike(EntityDamageByEntityEvent var1) {
        DamageCause var11 = var1.getCause();
        DamageCause var3 = DamageCause.ENTITY_ATTACK;
        if (var11 == var3) {
            if (var1.getDamager() instanceof Player var8) {
                if (var1.getEntity() instanceof LivingEntity var63) {
                    if (!var63.equals(var8)) {
                        if (this.holdsFire(var8, 1527035983)) {
                            PlayerDataStore var29 = this.players;
                            UUID var50 = var8.getUniqueId();
                            if (var29.get(var50, 1264010596).canUsePassives(2051432579)) {
                                if (var63 instanceof Player var64) {
                                    TrustStore var37 = this.trust;
                                    UUID var52 = var8.getUniqueId();
                                    UUID var56 = var64.getUniqueId();
                                    if (var37.isTrusted(var52, var56, 1736364606)) {
                                        return;
                                    }
                                }

                                double var43 = this.random.nextDouble() * 100.0;
                                String var59 = "fire";
                                String var5 = "flame-strike";
                                double var60 = GemTuning.chance(var59, var5, 15.0, 1091538029);
                                if (var43 < var60) {
                                    int var55 = Math.max(var63.getFireTicks(), 80);
                                    var63.setFireTicks(var55);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean holdsFire(Player var1, int var2) {
        int var7 = BlissCoreItems.gemIdOf(var1.getInventory().getItemInMainHand(), 382441079);
        int var15 = GemType.FIRE.id();
        if (var7 != var15) {
            int var11 = BlissCoreItems.gemIdOf(var1.getInventory().getItemInOffHand(), 382441079);
            int var17 = GemType.FIRE.id();
            if (var11 != var17) {
                GemType var18 = GemType.FIRE;
                if (!GoldGem.hasAbsorbedSoul(var1, var18, 1603837876)) {
                    return false;
                }
            }
        }

        return true;
    }
}
