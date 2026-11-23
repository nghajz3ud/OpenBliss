package com.bliss.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public final class GemTuning {
    private static FileConfiguration cfg;

    private GemTuning() {
    }

    public static void init(FileConfiguration var0, int var1) {
        cfg = var0;
    }

    public static long cd(String var0, String var1, double var2, long var4, int var6) {
        if (var2 <= 0.0) {
            return var4;
        } else {
            String var20 = "gems." + var0 + ".cooldowns." + var1;
            double var14 = read(var20, var2, 1025946559);
            double var25 = (double)var4;
            double var35 = var14 / var2;
            return Math.round(var25 * var35);
        }
    }

    public static double damage(String var0, String var1, double var2, int var4) {
        String var7 = "gems." + var0 + ".damage." + var1;
        return read(var7, var2, 1025946559);
    }

    public static double speed(String var0, String var1, double var2, int var4) {
        String var7 = "gems." + var0 + ".speed." + var1;
        return read(var7, var2, 1025946559);
    }

    public static double radius(String var0, String var1, double var2, int var4) {
        String var7 = "gems." + var0 + ".radius." + var1;
        return read(var7, var2, 1025946559);
    }

    public static double duration(String var0, String var1, double var2, int var4) {
        String var7 = "gems." + var0 + ".duration." + var1;
        return read(var7, var2, 1025946559);
    }

    public static double power(String var0, String var1, double var2, int var4) {
        String var7 = "gems." + var0 + ".power." + var1;
        return read(var7, var2, 1025946559);
    }

    public static double chance(String var0, String var1, double var2, int var4) {
        String var7 = "gems." + var0 + ".chance." + var1;
        return read(var7, var2, 1025946559);
    }

    public static double health(String var0, String var1, double var2, int var4) {
        String var7 = "gems." + var0 + ".health." + var1;
        return read(var7, var2, 1025946559);
    }

    public static long mythicCd(String var0, String var1, double var2, long var4) {
        return mythicCd(var0, var1, var2, var4, null, 1380510852);
    }

    public static long mythicCd(String var0, String var1, double var2, long var4, Player var6, int var7) {
        long var14 = mythicCdRaw(var0, var1, var2, var4, 1613227941);
        if (var6 != null && GlobalState.mythicDragonEggEnabled(346533648) && GlobalState.isHoldingDragonEgg(var6, 2044938455)) {
            var14 /= 2L;
        }

        return var14;
    }

    private static long mythicCdRaw(String var0, String var1, double var2, long var4, int var6) {
        if (var2 <= 0.0) {
            return var4;
        } else {
            String var20 = "mythic-gems." + var0 + ".cooldowns." + var1;
            double var14 = read(var20, var2, 1025946559);
            double var25 = (double)var4;
            double var35 = var14 / var2;
            return Math.round(var25 * var35);
        }
    }

    public static boolean gemsFlag(String var0, boolean var1) {
        FileConfiguration var5 = cfg;
        boolean var7;
        if (var5 == null) {
            var7 = var1;
        } else {
            String var9 = "gems." + var0;
            var7 = var5.getBoolean(var9, var1);
        }

        return var7;
    }

    public static boolean flag(String var0, String var1, boolean var2, int var3) {
        FileConfiguration var7 = cfg;
        boolean var10;
        if (var7 == null) {
            var10 = var2;
        } else {
            String var11 = "gems." + var0 + "." + var1;
            var10 = var7.getBoolean(var11, var2);
        }

        return var10;
    }

    public static boolean passive(String var0, String var1, boolean var2, int var3) {
        FileConfiguration var7 = cfg;
        boolean var10;
        if (var7 == null) {
            var10 = var2;
        } else {
            String var11 = "gems." + var0 + ".passives." + var1;
            var10 = var7.getBoolean(var11, var2);
        }

        return var10;
    }

    public static double mythicDamage(String var0, String var1, double var2, int var4) {
        String var7 = "mythic-gems." + var0 + ".damage." + var1;
        return read(var7, var2, 1025946559);
    }

    public static boolean mythicFlag(String var0, String var1, boolean var2, int var3) {
        FileConfiguration var7 = cfg;
        boolean var10;
        if (var7 == null) {
            var10 = var2;
        } else {
            String var11 = "mythic-gems." + var0 + "." + var1;
            var10 = var7.getBoolean(var11, var2);
        }

        return var10;
    }

    public static void setMythicFlag(String var0, String var1, boolean var2, int var3) {
        FileConfiguration var7 = cfg;
        if (var7 != null) {
            String var10 = "mythic-gems." + var0 + "." + var1;
            Boolean var12 = var2;
            var7.set(var10, var12);
        }
    }

    private static double read(String var0, double var1, int var3) {
        FileConfiguration var8 = cfg;
        if (var8 == null) {
            return var1;
        } else {
            double var9 = var8.getDouble(var0, var1);
            return !Double.isNaN(var9) && !Double.isInfinite(var9) && !(var9 < 0.0) ? var9 : var1;
        }
    }
}
