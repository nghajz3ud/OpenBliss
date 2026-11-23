package com.bliss.villager;

import com.bliss.core.Particles;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class DrawnBolt {
    private static final int SEGS = 12;
    private static final int PTS = 5;
    private static final double JITTER = 1.5;
    private static final double FORK_STEP = 1.6;
    private static final double FORK_DROP = 1.3;

    private DrawnBolt() {
    }

    public static List<Location> path(World var0, Location var1, double var2, double var4, ThreadLocalRandom var6, int var7) {
        double var24 = var6.nextDouble() * Math.PI * 2.0;
        double var214 = Math.sqrt(var6.nextDouble());
        double var26 = var2 * var214;
        double var81 = var1.getX();
        double var218 = Math.cos(var24);
        double var220 = var218 * var26;
        double var28 = var81 + var220;
        double var86 = var1.getZ();
        double var224 = Math.sin(var24);
        double var226 = var224 * var26;
        double var30 = var86 + var226;
        int var188 = (int)Math.floor(var28);
        int var232 = (int)Math.floor(var30);
        double var32 = (double)(var0.getHighestBlockYAt(var188, var232) + 1);
        double var34 = var32 + var4;
        ArrayList var99 = new ArrayList();
        ArrayList var36 = var99;
        ArrayList var100 = new ArrayList();
        ArrayList var37 = var100;
        double var38 = var28;
        double var40 = var34;
        double var42 = var30;

        for (int var44 = 1; var44 <= 12; var44++) {
            double var237 = var34 - var32;
            double var300 = (double)var44;
            double var241 = var237 * var300 / 12.0;
            double var45 = var34 - var241;
            double var248 = (var6.nextDouble() - 0.5) * 1.5;
            double var48 = var38 + var248;
            double var255 = (var6.nextDouble() - 0.5) * 1.5;
            double var52 = var42 + var255;
            line(var36, var0, var38, var40, var42, var48, var45, var52, 607092366);
            double[] var193 = new double[]{var48, var45, var52};
            var37.add(var193);
            var38 = var48;
            var40 = var45;
            var42 = var52;
        }

        int var195 = var6.nextInt(3);
        int var342 = 2 + var195;
        int var198 = var37.size() - 3;
        int var343 = Math.max(1, var198);

        for (int var47 = 0; var47 < var342; var47++) {
            int var201 = var6.nextInt(var343);
            double[] var344 = (double[])var37.get(var201);
            double var50 = var344[0];
            double var54 = var344[1];
            double var56 = var344[2];
            double var58 = var6.nextDouble() - 0.5;
            double var60 = var6.nextDouble() - 0.5;
            int var206 = var6.nextInt(3);
            int var62 = 2 + var206;

            for (int var63 = 0; var63 < var62; var63++) {
                double var269 = var58 * 1.6;
                double var164 = var50 + var269;
                double var276 = (var6.nextDouble() - 0.5) * 1.5;
                double var64 = var164 + var276;
                double var280 = var60 * 1.6;
                double var170 = var56 + var280;
                double var287 = (var6.nextDouble() - 0.5) * 1.5;
                double var66 = var170 + var287;
                double var68 = var54 - 1.3;
                line(var36, var0, var50, var54, var56, var64, var68, var66, 607092366);
                var50 = var64;
                var54 = var68;
                var56 = var66;
            }
        }

        return var36;
    }

    public static void draw(Plugin var0, final World var1, final List<Location> var2, final int var3, int var4) {
        if (!var2.isEmpty()) {
            BukkitRunnable var10 = new BukkitRunnable() {
                int i = 0;

                public void run() {
                    int var15 = var2.size();
                    int var25 = this.i;
                    int var34 = var3;
                    int var26 = var25 + var34;
                    int var13 = Math.min(var15, var26);

                    while (true) {
                        int var18 = this.i;
                        if (var18 >= var13) {
                            int var23 = this.i;
                            int var33 = var2.size();
                            if (var23 >= var33) {
                                this.cancel();
                            }

                            return;
                        }

                        World var20 = var1;
                        Particle var28 = Particle.WAX_OFF;
                        List var36 = var2;
                        int var40 = this.i;
                        Location var38 = (Location)var36.get(var40);
                        Particles.spawn(var20, var28, var38, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                        int var30 = this.i + 1;
                        this.i = var30;
                    }
                }
            };
            var10.runTaskTimer(var0, 0L, 1L);
        }
    }

    private static void line(List<Location> var0, World var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
        for (int var30 = 0; var30 < 5; var30++) {
            double var31 = (double)var30 / 5.0;
            double var44 = var8 - var2;
            double var46 = var44 * var31;
            double var42 = var2 + var46;
            double var56 = var10 - var4;
            double var58 = var56 * var31;
            double var50 = var4 + var58;
            double var68 = var12 - var6;
            double var70 = var68 * var31;
            double var62 = var6 + var70;
            Location var41 = new Location(var1, var42, var50, var62);
            var0.add(var41);
        }
    }
}
