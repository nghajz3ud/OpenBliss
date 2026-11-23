package com.bliss.gems;

import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

public final class FluxFreezeDeathListener implements Listener {
    private final FluxGemListener flux;

    public FluxFreezeDeathListener(FluxGemListener var1, int var2) {
        this.flux = var1;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onMoveFreeze(PlayerMoveEvent var1) {
        double var7 = var1.getFrom().getX();
        double var31 = var1.getTo().getX();
        if (var7 == var31) {
            double var12 = var1.getFrom().getY();
            double var35 = var1.getTo().getY();
            if (var12 == var35) {
                double var17 = var1.getFrom().getZ();
                double var39 = var1.getTo().getZ();
                if (var17 == var39) {
                    return;
                }
            }
        }

        UUID var5 = var1.getPlayer().getUniqueId();
        Boolean var23 = Boolean.TRUE;
        Map var27 = this.flux.groundFlag;
        Object var28 = var27.get(var5);
        if (var23.equals(var28)) {
            var1.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent var1) {
        Player var5 = var1.getEntity();
        UUID var6 = var5.getUniqueId();
        String var7 = this.flux.fluxKiller;
        if (var7 != null) {
            boolean var30;
            label29: {
                Player var8 = Bukkit.getPlayerExact(var7);
                if (var8 != null) {
                    PotionEffectType var3 = PotionEffectType.INVISIBILITY;
                    if (var8.hasPotionEffect(var3)) {
                        var30 = true;
                        break label29;
                    }
                }

                var30 = false;
            }

            String var24;
            if (var30) {
                String var29 = String.valueOf(ChatColor.MAGIC);
                String var39 = String.valueOf(ChatColor.RESET);
                var24 = var29 + "???" + var39;
            } else {
                var24 = var7;
            }

            Boolean var25 = Boolean.TRUE;
            Map var33 = this.flux.groundFlag;
            Object var34 = var33.get(var6);
            if (var25.equals(var34)) {
                String var36 = var5.getName();
                String var37 = var36 + " experienced high voltage from " + var24;
                var1.setDeathMessage(var37);
            }
        }
    }
}
