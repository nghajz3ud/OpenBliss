package com.bliss.gems;

import com.bliss.core.Particles;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class GemBurst {
    private static final int RINGS = 8;
    private static final double START_R = 0.4;
    private static final double DEFAULT_MAX_R = 4.0;
    private static final double SPACING = 0.18;
    private static final int SPARKLE_RINGS = 3;
    private static final int SPARKLE_EVERY = 10;

    private GemBurst() {
    }

    public static void burst(Plugin var0, Location var1, Color var2) {
        burst(var0, var1, var2, 4.0, 1155716754);
    }

    public static void burst(Plugin var0, Location var1, Color var2, double var3, int var5) {
        burst(var0, var1, var2, var3, true, 1.0F, 662549968);
    }

    public static void gentleBurst(Plugin var0, Location var1, Color var2, double var3) {
        burst(var0, var1, var2, var3, false, 1.0F, 662549968);
    }

    public static void gentleBurst(Plugin var0, Location var1, Color var2, double var3, float var5, int var6) {
        burst(var0, var1, var2, var3, false, var5, 662549968);
    }

    private static void burst(Plugin var0, Location var1, final Color var2, double var3, final boolean var5, final float var6, int var7) {
        if (var1.getWorld() != null) {
            final Location var17 = var1.clone();
            double var32 = (var3 - 0.4) / 7.0;
            final double var18 = Math.max(0.05, var32);
            BukkitRunnable var27 = new BukkitRunnable() {
                int i = 0;
                double s = 0.4;

                public void run() {
                    if (this.i >= 8) {
                        this.cancel();
                    } else {
                        double var94 = this.s;
                        int var63 = (int)((Math.PI * 2) * var94 / 0.18);
                        int var17x = Math.max(1, var63);
                        Color var81 = var2;
                        float var99 = var6;
                        DustOptions var26 = new DustOptions(var81, var99);
                        DustOptions var18x = var26;

                        for (int var19 = 0; var19 < var17x; var19++) {
                            double var83 = (double)var19;
                            double var31 = (Math.PI * 2) * var83;
                            double var86 = (double)var17x;
                            double var20 = var31 / var86;
                            Location var37 = var17.clone();
                            double var66 = this.s;
                            double var102 = Math.cos(var20);
                            double var68 = var66 * var102;
                            double var118 = this.s;
                            double var128 = Math.sin(var20);
                            double var120 = var118 * var128;
                            Location var22 = var37.add(var68, 0.0, var120);
                            World var41 = var17.getWorld();
                            Particle var70 = Particle.DUST;
                            Particles.spawn(var41, var70, var22, 1, 0.0, 0.0, 0.0, 0.0, var18x, 1141219473);
                            if (var5) {
                                World var53 = var17.getWorld();
                                Particle var74 = Particle.SMOKE;
                                Particles.spawn(var53, var74, var22, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                            } else if (this.i < 3 && var19 % 10 == 0) {
                                World var50 = var17.getWorld();
                                Particle var73 = Particle.HAPPY_VILLAGER;
                                Location var91 = var22.clone().add(0.0, 0.25, 0.0);
                                Particles.spawn(var50, var73, var91, 1, 0.08, 0.08, 0.08, 0.0, 1986132876);
                            }
                        }

                        double var75 = this.s;
                        double var112 = var18;
                        double var77 = var75 + var112;
                        this.s = var77;
                        int var80 = this.i + 1;
                        this.i = var80;
                    }
                }
            };
            var27.runTaskTimer(var0, 0L, 1L);
        }
    }
}
