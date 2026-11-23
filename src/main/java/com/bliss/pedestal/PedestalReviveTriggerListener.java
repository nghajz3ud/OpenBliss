package com.bliss.pedestal;

import com.bliss.BlissPlugin;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitScheduler;

public final class PedestalReviveTriggerListener implements Listener {
    private static final double TRIGGER_RADIUS_SQ = 0.5625;
    private static final double TRIGGER_Y_TOLERANCE = 1.0;
    private final PedestalState state;
    private final PedestalReviveRitual ritual;

    public PedestalReviveTriggerListener(PedestalState var1, PedestalReviveRitual var2, int var3) {
        this.state = var1;
        this.ritual = var2;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent var1) {
        Player var5 = var1.getPlayer();
        PedestalState var11 = this.state;
        UUID var36 = var5.getUniqueId();
        if (!var11.revivalLocked(var36, 1520632396)) {
            this.tryStart(var5, 998015102);
        } else {
            Location var6 = var1.getFrom();
            Location var7 = var1.getTo();
            if (var7 != null) {
                double var19 = var6.getX();
                double var43 = var7.getX();
                if (var19 == var43) {
                    double var23 = var6.getY();
                    double var46 = var7.getY();
                    if (var23 == var46) {
                        double var27 = var6.getZ();
                        double var49 = var7.getZ();
                        if (var27 == var49) {
                            return;
                        }
                    }
                }

                Location var8 = var6.clone();
                float var38 = var7.getYaw();
                var8.setYaw(var38);
                float var40 = var7.getPitch();
                var8.setPitch(var40);
                var1.setTo(var8);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onLockedTeleport(PlayerTeleportEvent var1) {
        PedestalState var4 = this.state;
        UUID var10 = var1.getPlayer().getUniqueId();
        if (var4.revivalLocked(var10, 1520632396)) {
            PlayerTeleportEvent.TeleportCause var2 = var1.getCause();
            if (var2 == PlayerTeleportEvent.TeleportCause.ENDER_PEARL || var2 == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
                var1.setCancelled(true);
            }
        }
    }

    private void tryStart(Player var1, int var2) {
        if (!this.state.pedestalBusy(1861024962)) {
            PedestalState.Ritual var23 = this.state.ritual(1751702735);
            PedestalState.Ritual var4 = PedestalState.Ritual.REVIVE;
            if (var23 == var4) {
                if (this.state.count(629418704) >= 5) {
                    if (this.state.revivingPlayer(203133736) != null) {
                        UUID var31 = var1.getUniqueId();
                        UUID var75 = this.state.revivingPlayer(203133736);
                        if (var31.equals(var75)) {
                            Location var12 = this.state.pedestalLoc1(1941968134);
                            if (var12 != null) {
                                World var38 = var1.getWorld();
                                World var77 = var12.getWorld();
                                if (var38.equals(var77)) {
                                    Location var13 = var12.clone().add(0.5, 0.0, 0.5);
                                    Location var14 = var1.getLocation();
                                    double var46 = var14.getX();
                                    double var81 = var13.getX();
                                    double var15 = var46 - var81;
                                    double var51 = var14.getZ();
                                    double var84 = var13.getZ();
                                    double var17 = var51 - var84;
                                    double var57 = var15 * var15;
                                    double var90 = var17 * var17;
                                    if (var57 + var90 > 0.5625) {
                                        return;
                                    }

                                    double var63 = var14.getY();
                                    double var95 = var13.getY();
                                    if (Math.abs(var63 - var95) > 1.0) {
                                        return;
                                    }

                                    PedestalReviveRitual var71 = this.ritual;
                                    var71.start(var1, 2071224045);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void startPolling(BlissPlugin var1, int var2) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        Runnable var11 = () -> {
            UUID var3x = this.state.revivingPlayer(203133736);
            if (var3x != null) {
                Player var4 = Bukkit.getPlayer(var3x);
                if (var4 != null) {
                    this.tryStart(var4, 998015102);
                }
            }
        };
        var3.runTaskTimer(var1, var11, 20L, 10L);
    }
}
