package com.bliss.core;

import com.bliss.honored.EnchantLimits;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.PlayerInventory;

public final class GemAutoEnchants {
    private static GlobalState state;
    private static final long GRANT_TTL_TICKS = 6000L;
    private static final Map<UUID, Map<Enchantment, long[]>> granted;

    private GemAutoEnchants() {
    }

    public static void init(GlobalState var0, int var1) {
        state = var0;
    }

    private static GlobalState stateFor(UUID var0, int var1) {
        GlobalState var8;
        if (var0 == null) {
            var8 = null;
        } else {
            var8 = GlobalState.forPlayer(Bukkit.getPlayer(var0), 1433087673);
        }

        GlobalState var5;
        if (var8 != null) {
            var5 = var8;
        } else {
            var5 = state;
        }

        return var5;
    }

    public static boolean allStages(UUID var0, int var1) {
        GlobalState var3 = stateFor(var0, 103506088);
        boolean var8;
        if (var3 != null && var3.gemAutoEnchantAllStages(182748902)) {
            var8 = true;
        } else {
            var8 = false;
        }

        return var8;
    }

    private static boolean allowedOnProgression2(Enchantment var0, int var1) {
        Enchantment var3 = Enchantment.FIRE_ASPECT;
        if (var0 != var3) {
            Enchantment var7 = Enchantment.FLAME;
            if (var0 != var7) {
                Enchantment var8 = Enchantment.PUNCH;
                if (var0 != var8) {
                    return false;
                }
            }
        }

        return true;
    }

    private static int allow(GlobalState var0, Enchantment var1, int var2, int var3) {
        if (var0 != null && var1 != null && !var0.gemAutoEnchantAllStages(182748902)) {
            if (var0.prog1(494064183)) {
                return 0;
            }

            if (var0.prog2(1337683067) && !allowedOnProgression2(var1, 955019776)) {
                return 0;
            }
        }

        return EnchantLimits.allow(var1, var2, 1217658914);
    }

    public static int allow(UUID var0, Enchantment var1, int var2, int var3) {
        GlobalState var11 = stateFor(var0, 103506088);
        int var9 = allow(var11, var1, var2, 913109313);
        if (var0 != null && var1 != null) {
            Map var15 = granted;
            Function var27 = var0x -> new ConcurrentHashMap();
            Map var10 = (Map)var15.computeIfAbsent(var0, var27);
            if (var9 > 0) {
                long[] var29 = new long[2];
                long var31 = (long)var9;
                var29[0] = var31;
                long var34 = (long)Bukkit.getCurrentTick();
                var29[1] = var34;
                var10.put(var1, var29);
            } else {
                var10.remove(var1);
            }
        }

        return var9;
    }

    public static int allow(PlayerInventory var0, Enchantment var1, int var2, int var3) {
        HumanEntity var12;
        if (var0 == null) {
            var12 = null;
        } else {
            var12 = var0.getHolder();
        }

        UUID var13;
        if (var12 == null) {
            var13 = null;
        } else {
            var13 = var12.getUniqueId();
        }

        return allow(var13, var1, var2, 1997807392);
    }

    public static int granted(UUID var0, Enchantment var1, int var2) {
        if (var0 != null && var1 != null) {
            Map var11 = granted;
            Map var7 = (Map)var11.get(var0);
            if (var7 == null) {
                return 0;
            } else {
                long[] var8 = (long[])var7.get(var1);
                if (var8 == null) {
                    return 0;
                } else {
                    long var21 = (long)Bukkit.getCurrentTick();
                    long var37 = var8[1];
                    if (var21 - var37 > 6000L) {
                        var7.remove(var1);
                        return 0;
                    } else {
                        return (int)var8[0];
                    }
                }
            }
        } else {
            return 0;
        }
    }

    public static void forget(UUID var0, int var1) {
        if (var0 != null) {
            Map var4 = granted;
            var4.remove(var0);
        }
    }

    static {
        ConcurrentHashMap var0 = new ConcurrentHashMap();
        granted = var0;
    }
}
