package com.bliss.core;

import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public final class Particles {
    private static volatile Particles.Level level = Particles.Level.ON;

    private Particles() {
    }

    public static Particles.Level level() {
        return level;
    }

    public static void setLevel(Particles.Level var0, int var1) {
        Particles.Level var3;
        if (var0 == null) {
            var3 = Particles.Level.ON;
        } else {
            var3 = var0;
        }

        level = var3;
    }

    public static Particles.Level parse(String var0, int var1) {
        if (var0 == null) {
            return null;
        } else {
            for (Particles.Level var7 : Particles.Level.values()) {
                String var16 = var7.label();
                String var23 = var0.trim();
                if (var16.equalsIgnoreCase(var23)) {
                    return var7;
                }
            }

            return null;
        }
    }

    private static int scale(int var0, int var1) {
        switch (level) {
            case OFF:
                return -1;
            case DECREASED:
                if (var0 <= 1) {
                    int var16;
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        var16 = var0;
                    } else {
                        var16 = -1;
                    }

                    return var16;
                }

                int var18 = var0 / 2;
                return Math.max(1, var18);
            case EXTRA:
                int var13;
                if (var0 <= 0) {
                    var13 = var0;
                } else {
                    var13 = var0 * 2;
                }

                return var13;
            default:
                return var0;
        }
    }

    public static void spawn(World var0, Particle var1, Location var2, int var3) {
        int var8 = scale(var3, 1807339893);
        if (var8 >= 0) {
            var0.spawnParticle(var1, var2, var8);
        }
    }

    public static <T> void spawn(World var0, Particle var1, Location var2, int var3, T var4) {
        int var10 = scale(var3, 1807339893);
        if (var10 >= 0) {
            var0.spawnParticle(var1, var2, var10, var4);
        }
    }

    public static void spawn(World var0, Particle var1, Location var2, int var3, double var4, double var6, double var8) {
        int var20 = scale(var3, 1807339893);
        if (var20 >= 0) {
            var0.spawnParticle(var1, var2, var20, var4, var6, var8);
        }
    }

    public static <T> void spawn(World var0, Particle var1, Location var2, int var3, double var4, double var6, double var8, T var10) {
        int var22 = scale(var3, 1807339893);
        if (var22 >= 0) {
            var0.spawnParticle(var1, var2, var22, var4, var6, var8, var10);
        }
    }

    public static void spawn(World var0, Particle var1, Location var2, int var3, double var4, double var6, double var8, double var10, int var12) {
        int var25 = scale(var3, 1807339893);
        if (var25 >= 0) {
            var0.spawnParticle(var1, var2, var25, var4, var6, var8, var10);
        }
    }

    public static <T> void spawn(World var0, Particle var1, Location var2, int var3, double var4, double var6, double var8, double var10, T var12, int var13) {
        int var27 = scale(var3, 1807339893);
        if (var27 >= 0) {
            var0.spawnParticle(var1, var2, var27, var4, var6, var8, var10, var12);
        }
    }

    public static <T> void spawn(
        World var0, Particle var1, Location var2, int var3, double var4, double var6, double var8, double var10, T var12, boolean var13, int var14
    ) {
        int var29 = scale(var3, 1807339893);
        if (var29 >= 0) {
            var0.spawnParticle(var1, var2, var29, var4, var6, var8, var10, var12, var13);
        }
    }

    public static void spawn(World var0, Particle var1, double var2, double var4, double var6, int var8, int var9) {
        int var19 = scale(var8, 1807339893);
        if (var19 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var19);
        }
    }

    public static <T> void spawn(World var0, Particle var1, double var2, double var4, double var6, int var8, T var9) {
        int var20 = scale(var8, 1807339893);
        if (var20 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var20, var9);
        }
    }

    public static void spawn(World var0, Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13) {
        int var30 = scale(var8, 1807339893);
        if (var30 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var30, var9, var11, var13);
        }
    }

    public static <T> void spawn(World var0, Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, T var15) {
        int var32 = scale(var8, 1807339893);
        if (var32 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var32, var9, var11, var13, var15);
        }
    }

    public static void spawn(
        World var0, Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15, int var17
    ) {
        int var35 = scale(var8, 1807339893);
        if (var35 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var35, var9, var11, var13, var15);
        }
    }

    public static <T> void spawn(
        World var0, Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15, T var17, int var18
    ) {
        int var37 = scale(var8, 1807339893);
        if (var37 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var37, var9, var11, var13, var15, var17);
        }
    }

    public static <T> void spawn(
        World var0,
        Particle var1,
        double var2,
        double var4,
        double var6,
        int var8,
        double var9,
        double var11,
        double var13,
        double var15,
        T var17,
        boolean var18,
        int var19
    ) {
        int var39 = scale(var8, 1807339893);
        if (var39 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var39, var9, var11, var13, var15, var17, var18);
        }
    }

    public static <T> void always(
        World var0,
        Particle var1,
        double var2,
        double var4,
        double var6,
        int var8,
        double var9,
        double var11,
        double var13,
        double var15,
        T var17,
        boolean var18,
        int var19
    ) {
        var0.spawnParticle(var1, var2, var4, var6, var8, var9, var11, var13, var15, var17, var18);
    }

    public static <T> void always(
        Player var0,
        Particle var1,
        double var2,
        double var4,
        double var6,
        int var8,
        double var9,
        double var11,
        double var13,
        double var15,
        T var17,
        boolean var18,
        int var19
    ) {
        var0.spawnParticle(var1, var2, var4, var6, var8, var9, var11, var13, var15, var17, var18);
    }

    public static void spawn(Player var0, Particle var1, Location var2, int var3) {
        int var8 = scale(var3, 1807339893);
        if (var8 >= 0) {
            var0.spawnParticle(var1, var2, var8);
        }
    }

    public static void spawn(Player var0, Particle var1, Location var2, int var3, double var4, double var6, double var8) {
        int var20 = scale(var3, 1807339893);
        if (var20 >= 0) {
            var0.spawnParticle(var1, var2, var20, var4, var6, var8);
        }
    }

    public static void spawn(Player var0, Particle var1, Location var2, int var3, double var4, double var6, double var8, double var10) {
        int var24 = scale(var3, 1807339893);
        if (var24 >= 0) {
            var0.spawnParticle(var1, var2, var24, var4, var6, var8, var10);
        }
    }

    public static <T> void spawn(Player var0, Particle var1, Location var2, int var3, double var4, double var6, double var8, double var10, T var12, int var13) {
        int var27 = scale(var3, 1807339893);
        if (var27 >= 0) {
            var0.spawnParticle(var1, var2, var27, var4, var6, var8, var10, var12);
        }
    }

    public static void spawn(Player var0, Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15) {
        int var34 = scale(var8, 1807339893);
        if (var34 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var34, var9, var11, var13, var15);
        }
    }

    public static <T> void spawn(
        Player var0, Particle var1, double var2, double var4, double var6, int var8, double var9, double var11, double var13, double var15, T var17
    ) {
        int var36 = scale(var8, 1807339893);
        if (var36 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var36, var9, var11, var13, var15, var17);
        }
    }

    public static <T> void spawn(
        Player var0,
        Particle var1,
        double var2,
        double var4,
        double var6,
        int var8,
        double var9,
        double var11,
        double var13,
        double var15,
        T var17,
        boolean var18
    ) {
        int var38 = scale(var8, 1807339893);
        if (var38 >= 0) {
            var0.spawnParticle(var1, var2, var4, var6, var38, var9, var11, var13, var15, var17, var18);
        }
    }

    public static enum Level {
        OFF,
        DECREASED,
        ON,
        EXTRA;

        public String label() {
            String var3 = this.name();
            Locale var2 = Locale.ROOT;
            return var3.toLowerCase(var2);
        }
    }
}
