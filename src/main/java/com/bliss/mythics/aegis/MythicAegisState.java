package com.bliss.mythics.aegis;

import com.bliss.core.GemTuning;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.Function;
import java.util.function.Predicate;

public final class MythicAegisState {
    public final Map<UUID, Deque<Long>> chainCd;
    public final Set<UUID> grappling;
    public final Map<UUID, Long> chainLockUntil;
    public final Map<UUID, Long> dashReadyAt;
    public final Map<UUID, Long> parryUntil;
    public final Set<UUID> parrySucceeded;
    public final Map<UUID, Long> lastParryCastMs;
    public final Set<UUID> parryCastFull;
    public final Map<UUID, Long> lastChainThrowMs;
    public final Set<UUID> chainThrowFull;
    public final Map<UUID, Long> angelsReadyAt;
    public final Map<UUID, Long> lastSwingAt;
    public final Set<UUID> reachBoosted;
    public final Map<UUID, Long> angelsWindowUntil;
    public final Map<UUID, Double> angelsCeilY;
    public final Set<UUID> angelsOnCeiling;

    public MythicAegisState() {
        ConcurrentHashMap var2 = new ConcurrentHashMap();
        this.chainCd = var2;
        KeySetView var19 = ConcurrentHashMap.newKeySet();
        this.grappling = var19;
        ConcurrentHashMap var20 = new ConcurrentHashMap();
        this.chainLockUntil = var20;
        ConcurrentHashMap var21 = new ConcurrentHashMap();
        this.dashReadyAt = var21;
        ConcurrentHashMap var22 = new ConcurrentHashMap();
        this.parryUntil = var22;
        KeySetView var23 = ConcurrentHashMap.newKeySet();
        this.parrySucceeded = var23;
        ConcurrentHashMap var24 = new ConcurrentHashMap();
        this.lastParryCastMs = var24;
        KeySetView var25 = ConcurrentHashMap.newKeySet();
        this.parryCastFull = var25;
        ConcurrentHashMap var26 = new ConcurrentHashMap();
        this.lastChainThrowMs = var26;
        KeySetView var27 = ConcurrentHashMap.newKeySet();
        this.chainThrowFull = var27;
        ConcurrentHashMap var28 = new ConcurrentHashMap();
        this.angelsReadyAt = var28;
        ConcurrentHashMap var29 = new ConcurrentHashMap();
        this.lastSwingAt = var29;
        KeySetView var30 = ConcurrentHashMap.newKeySet();
        this.reachBoosted = var30;
        ConcurrentHashMap var31 = new ConcurrentHashMap();
        this.angelsWindowUntil = var31;
        ConcurrentHashMap var32 = new ConcurrentHashMap();
        this.angelsCeilY = var32;
        KeySetView var33 = ConcurrentHashMap.newKeySet();
        this.angelsOnCeiling = var33;
    }

    public void useChain(UUID var1, long var2, long var4, int var6) {
        Map<UUID, Deque<Long>> var17 = this.chainCd;
        Function<UUID, Deque<Long>> var9 = var0 -> new ArrayDeque<>();
        Deque<Long> var12 = var17.computeIfAbsent(var1, var9);
        Predicate<Long> var41 = var2x -> {
            long var6x = var2x;
            boolean var9x;
            if (var6x <= var2) {
                var9x = true;
            } else {
                var9x = false;
            }

            return var9x;
        };
        var12.removeIf(var41);
        long var13 = var2;

        for (Long var16 : var12) {
            long var32 = var16;
            if (var32 > var13) {
                var13 = var16;
            }
        }

        Long var46 = var13 + var4;
        var12.addLast(var46);
    }

    public boolean refundChain(UUID var1, int var2) {
        Map var6 = this.chainCd;
        Deque var5 = (Deque)var6.get(var1);
        if (var5 != null && !var5.isEmpty()) {
            var5.removeLast();
            if (var5.isEmpty()) {
                Map var17 = this.chainCd;
                var17.remove(var1);
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean legacyAuratus(int var0) {
        String var1 = "auratus";
        String var2 = "legacy";
        return GemTuning.mythicFlag(var1, var2, false, 200661716);
    }

    public static boolean chainRefundLock(int var0) {
        String var1 = "auratus";
        String var2 = "refund-lock";
        return GemTuning.mythicFlag(var1, var2, true, 200661716);
    }

    public int chainsAvailable(UUID var1, int var2, long var3, int var5) {
        Map var10 = this.chainCd;
        Deque var9 = (Deque)var10.get(var1);
        if (var9 == null) {
            return var2;
        } else {
            Predicate<Long> var26 = var2x -> {
                long var6 = var2x;
                boolean var9x;
                if (var6 <= var3) {
                    var9x = true;
                } else {
                    var9x = false;
                }

                return var9x;
            };
            var9.removeIf(var26);
            if (var9.isEmpty()) {
                Map var20 = this.chainCd;
                var20.remove(var1);
            }

            int var30 = var9.size();
            int var29 = var2 - var30;
            return Math.max(0, var29);
        }
    }

    public long nextChainReady(UUID var1, int var2) {
        Map var13 = this.chainCd;
        Deque<Long> var7 = (Deque<Long>)var13.get(var1);
        if (var7 != null && !var7.isEmpty()) {
            long var8 = Long.MAX_VALUE;

            for (long var11 : var7) {
                var8 = Math.min(var8, var11);
            }

            return var8;
        } else {
            return 0L;
        }
    }

    public boolean fullyCharged(UUID var1, long var2, int var4) {
        if (this.chainsAvailable(var1, 2, var2, 1870480623) < 2) {
            return false;
        } else {
            Map var15 = this.dashReadyAt;
            Long var10 = (Long)var15.get(var1);
            if (var10 != null) {
                long var20 = var10;
                if (var20 > var2) {
                    return false;
                }
            }

            Map var25 = this.angelsReadyAt;
            Long var11 = (Long)var25.get(var1);
            if (var11 != null) {
                long var30 = var11;
                if (var30 > var2) {
                    return false;
                }
            }

            return true;
        }
    }

    public void penalizeAll(UUID var1, long var2, long var4, long var6, long var8, int var10) {
        ArrayDeque var11 = new ArrayDeque();
        Long var33 = var2 + var4;
        var11.addLast(var33);
        Long var38 = var2 + var4;
        var11.addLast(var38);
        Map var21 = this.chainCd;
        var21.put(var1, var11);
        Map var24 = this.dashReadyAt;
        Long var46 = var2 + var6;
        var24.put(var1, var46);
        Map var27 = this.angelsReadyAt;
        Long var51 = var2 + var8;
        var27.put(var1, var51);
    }

    public void armAngels(UUID var1, double var2, long var4, int var6) {
        Map var11 = this.angelsWindowUntil;
        Long var21 = var4;
        var11.put(var1, var21);
        Map var14 = this.angelsCeilY;
        Double var24 = var2;
        var14.put(var1, var24);
        Set var17 = this.angelsOnCeiling;
        var17.remove(var1);
    }

    public boolean angelsActive(UUID var1, int var2) {
        Map var8 = this.angelsWindowUntil;
        Long var7 = (Long)var8.get(var1);
        if (var7 != null) {
            long var13 = var7;
            long var5 = System.currentTimeMillis();
            if (var13 > var5) {
                return true;
            }
        }

        return false;
    }

    public double angelsCeilY(UUID var1) {
        Map var5 = this.angelsCeilY;
        Double var4 = (Double)var5.get(var1);
        double var10;
        if (var4 == null) {
            var10 = 0.0;
        } else {
            var10 = var4;
        }

        return var10;
    }

    public boolean angelsOnCeiling(UUID var1, int var2) {
        Set var5 = this.angelsOnCeiling;
        return var5.contains(var1);
    }

    public void markAngelsCeiling(UUID var1, int var2) {
        Set var5 = this.angelsOnCeiling;
        var5.add(var1);
    }

    public void clearAngels(UUID var1, int var2) {
        Map var5 = this.angelsWindowUntil;
        var5.remove(var1);
        Map var8 = this.angelsCeilY;
        var8.remove(var1);
        Set var11 = this.angelsOnCeiling;
        var11.remove(var1);
    }

    public boolean parryActive(UUID var1, int var2) {
        Map var8 = this.parryUntil;
        Long var7 = (Long)var8.get(var1);
        if (var7 != null) {
            long var13 = var7;
            long var5 = System.currentTimeMillis();
            if (var13 > var5) {
                return true;
            }
        }

        return false;
    }

    public void resetCooldowns(UUID var1, int var2) {
        Map var5 = this.chainCd;
        var5.remove(var1);
        Map var8 = this.dashReadyAt;
        var8.remove(var1);
        Map var11 = this.angelsReadyAt;
        var11.remove(var1);
        Map var14 = this.lastSwingAt;
        var14.remove(var1);
    }

    public void clearTransient(UUID var1, int var2) {
        Set var5 = this.grappling;
        var5.remove(var1);
        Map var8 = this.parryUntil;
        var8.remove(var1);
        Set var11 = this.parrySucceeded;
        var11.remove(var1);
        Set var14 = this.reachBoosted;
        var14.remove(var1);
        this.clearAngels(var1, 950114969);
    }
}
