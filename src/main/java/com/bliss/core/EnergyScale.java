package com.bliss.core;

public final class EnergyScale {
    public static final int BASELINE = 6;
    private static final double UP_PER_ENERGY = 0.02;
    private static final double DOWN_PER_ENERGY = 0.025;

    private EnergyScale() {
    }

    public static double power(PlayerData var0, int var1) {
        if (var0 == null) {
            return 1.0;
        } else {
            return !var0.restrictions(457321189) ? 1.0 : curve(var0.energy(1160853318), 376563720);
        }
    }

    public static double power(int var0, int var1) {
        return !PlayerData.energyRestrictionsEnabled(190425336) ? 1.0 : curve(var0, 376563720);
    }

    private static double curve(int var0, int var1) {
        int var19 = Math.min(11, var0);
        int var8 = Math.max(1, var19);
        if (var8 >= 6) {
            double var25 = (double)(var8 - 6) * 0.02;
            return 1.0 + var25;
        } else {
            double var31 = (double)(6 - var8) * 0.025;
            return 1.0 - var31;
        }
    }

    public static double radius(int var0, int var1) {
        double var7 = power(var0, 12277567);
        return Math.min(1.0, var7);
    }

    public static double cooldown(int var0, int var1) {
        double var7 = power(var0, 12277567);
        return 1.0 / var7;
    }

    public static long cd(long var0, int var2, int var3) {
        double var7 = (double)var0;
        double var13 = cooldown(var2, 996927983);
        return Math.round(var7 * var13);
    }
}
