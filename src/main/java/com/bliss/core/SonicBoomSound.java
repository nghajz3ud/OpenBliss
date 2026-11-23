package com.bliss.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;

public final class SonicBoomSound {
    public static final Sound SOUND = Sound.ENTITY_WARDEN_SONIC_BOOM;
    public static final float FAST = 2.0F;
    public static final float FULL = 1.0F;
    private static final float VOLUME = 1.5F;
    private static final int GAP = 50;
    private static final Map<String, Integer> LAST;
    public static final Sound HIT = Sound.ENTITY_WARDEN_SONIC_CHARGE;
    private static final int PULSE_GAP = 50;
    private static final Map<String, Integer> LAST_PULSE;

    private SonicBoomSound() {
    }

    public static void burst(String var0, World var1, Location var2, float var3, int var4) {
        if (var1 != null && var2 != null) {
            int var11 = Bukkit.getCurrentTick();
            String var28 = var1.getName();
            String var12 = var0 + "|" + var28;
            Map var18 = LAST;
            Integer var13 = (Integer)var18.get(var12);
            if (var13 != null) {
                int var31 = var13;
                if (var11 - var31 < 50) {
                    int var34 = var13;
                    if (var11 >= var34) {
                        return;
                    }
                }
            }

            Map var25 = LAST;
            Integer var37 = var11;
            var25.put(var12, var37);
            Sound var38 = SOUND;
            SoundCategory var8 = SoundCategory.MASTER;
            var1.playSound(var2, var38, var8, 1.5F, var3);
        }
    }

    public static void burst(String var0, World var1, Location var2, int var3) {
        burst(var0, var1, var2, 2.0F, 2117960030);
    }

    public static void pulse(String var0, World var1, Location var2, int var3) {
        if (var1 != null && var2 != null) {
            int var10 = Bukkit.getCurrentTick();
            String var27 = var1.getName();
            String var11 = var0 + "|" + var27;
            Map var17 = LAST_PULSE;
            Integer var12 = (Integer)var17.get(var11);
            if (var12 != null) {
                int var30 = var12;
                if (var10 - var30 < 50) {
                    int var33 = var12;
                    if (var10 >= var33) {
                        return;
                    }
                }
            }

            Map var24 = LAST_PULSE;
            Integer var36 = var10;
            var24.put(var11, var36);
            Sound var37 = HIT;
            SoundCategory var7 = SoundCategory.MASTER;
            var1.playSound(var2, var37, var7, 1.5F, 1.0F);
        }
    }

    public static void burst(String var0, World var1, double var2, double var4, double var6) {
        Location var10 = new Location(var1, var2, var4, var6);
        burst(var0, var1, var10, 2.0F, 2117960030);
    }

    static {
        ConcurrentHashMap var1 = new ConcurrentHashMap();
        LAST = var1;
        ConcurrentHashMap var3 = new ConcurrentHashMap();
        LAST_PULSE = var3;
    }
}
