package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemTuning;
import com.bliss.core.Particles;
import com.bliss.core.TrustStore;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class FireCrispBurnTask {
    private static final long PERIOD_TICKS = 10L;
    private static final int FIRE_TICKS_ON_BURN = 30;
    private static final double DAMAGE_HALF_HEART = 1.0;
    private static final int RING_POINTS = 25;
    private static final double RING_RADIUS = 5.0;
    private static final double RING_Y = 0.6;
    private final BlissPlugin plugin;
    private final FireCrispListener crisp;
    private final TrustStore trust;
    private BukkitTask task;

    public FireCrispBurnTask(BlissPlugin var1, FireCrispListener var2, TrustStore var3, int var4) {
        this.plugin = var1;
        this.crisp = var2;
        this.trust = var3;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 10L, 10L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void drawRings(int var1) {
        String var2 = "fire";
        String var3 = "crisp-ring";
        double var22 = GemTuning.radius(var2, var3, 5.0, 1058601812);

        for (Location var25 : this.crisp.activeCrisps.values()) {
            if (var25.getWorld() != null) {
                for (int var26 = 0; var26 < 25; var26++) {
                    double var56 = (double)var26;
                    double var27 = (Math.PI * 2) * var56 / 25.0;
                    World var52 = var25.getWorld();
                    Particle var54 = Particle.FLAME;
                    double var61 = var25.getX();
                    double var72 = Math.cos(var27);
                    double var65 = var22 * var72;
                    double var63 = var61 + var65;
                    double var70 = var25.getY() + 0.6;
                    double var77 = var25.getZ();
                    double var84 = Math.sin(var27);
                    double var81 = var22 * var84;
                    double var79 = var77 + var81;
                    Particles.spawn(var52, var54, var63, var70, var79, 1, 0.03, 0.03, 0.03, 0.005, 1717927553);
                }
            }
        }
    }

    private void tick() {
        if (!this.crisp.activeCrisps.isEmpty()) {
            this.drawRings(359803597);

            for (Player var9 : Bukkit.getOnlinePlayers()) {
                for (Entry var11 : this.crisp.activeCrisps.entrySet()) {
                    Location var12 = (Location)var11.getValue();
                    World var42 = var9.getWorld();
                    World var71 = var12.getWorld();
                    if (var42.equals(var71)) {
                        String var44 = "fire";
                        String var72 = "crisp";
                        double var13 = GemTuning.radius(var44, var72, 4.0, 1058601812);
                        Location var48 = var9.getLocation();
                        double var49 = var48.distanceSquared(var12);
                        double var85 = var13 * var13;
                        if (!(var49 > var85)) {
                            UUID var15 = (UUID)var11.getKey();
                            UUID var56 = var9.getUniqueId();
                            if (!var56.equals(var15)) {
                                TrustStore var59 = this.trust;
                                UUID var88 = var9.getUniqueId();
                                if (!var59.isTrusted(var15, var88, 1736364606)) {
                                    int var78 = Math.max(var9.getFireTicks(), 30);
                                    var9.setFireTicks(var78);
                                    this.crisp.burning = true;

                                    try {
                                        String var90 = "fire";
                                        String var5 = "crisp";
                                        double var91 = GemTuning.damage(var90, var5, 1.0, 1172948035);
                                        ConsistentDamage.dealVanillaOrTrue(null, var9, var91, 0.0, 1236326608);
                                    } finally {
                                        this.crisp.burning = false;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
