package com.bliss.core;

import com.bliss.gems.ConsistentDamage;
import com.bliss.honored.JuggernautGemGuardListener;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;

public final class BrokenStatePenaltyListener implements Listener {
    private final PlayerDataStore players;
    private final GlobalState globalState;

    public BrokenStatePenaltyListener(PlayerDataStore var1, GlobalState var2, int var3) {
        this.players = var1;
        this.globalState = var2;
    }

    public static boolean brokenVictim(GlobalState var0, PlayerDataStore var1, Entity var2, int var3) {
        if (!GlobalState.forEntity(var2, 601947313).brokenDamage(1031219045)) {
            return false;
        } else if (var2 instanceof Player var6) {
            if (var6.hasMetadata("NPC")) {
                return false;
            } else if (JuggernautGemGuardListener.isJuggernaut(var6, 859003337)) {
                return false;
            } else {
                UUID var26 = var6.getUniqueId();
                boolean var24;
                if (var1.get(var26, 1264010596).energy(1160853318) <= 1) {
                    var24 = true;
                } else {
                    var24 = false;
                }

                return var24;
            }
        } else {
            return false;
        }
    }

    @EventHandler(
        priority = EventPriority.HIGH,
        ignoreCancelled = true
    )
    public void onBrokenDamage(EntityDamageEvent var1) {
        GlobalState var15 = this.globalState;
        PlayerDataStore var60 = this.players;
        Entity var68 = var1.getEntity();
        if (brokenVictim(var15, var60, var68, 1142985265)) {
            Player var7 = (Player)var1.getEntity();
            DamageCause var21 = var1.getCause();
            DamageCause var61 = DamageCause.SONIC_BOOM;
            boolean var25;
            if (var21 != var61 && !ConsistentDamage.deliveringAbilityTo(var1.getEntity(), 450947555)) {
                var25 = false;
            } else {
                var25 = true;
            }

            if (var25) {
                if (var1 instanceof EntityDamageByEntityEvent var9) {
                    Player var10 = null;
                    Entity var14 = var9.getDamager();
                    if (var14 instanceof Player) {
                        var10 = (Player)var14;
                    } else {
                        Entity var69 = var9.getDamager();
                        if (var69 instanceof Projectile) {
                            ProjectileSource var70 = ((Projectile)var69).getShooter();
                            if (var70 instanceof Player) {
                                var10 = (Player)var70;
                            }
                        }
                    }

                    if (var10 == null && var9.getDamager() instanceof LivingEntity) {
                        return;
                    }

                    if (var10 != null && var10.equals(var7)) {
                        return;
                    }
                }

                double var66 = var1.getDamage() * 2.0;
                var1.setDamage(var66);
            }
        }
    }
}
