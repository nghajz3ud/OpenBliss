package com.bliss.gems;

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
import org.bukkit.event.Listener;

public final class SpeedGemListener implements Listener {
    public final Map<UUID, Long> cd1;
    public final Map<UUID, Long> thunderStepCd;
    public final Set<UUID> thunderStepPrimed;
    public final Map<UUID, Long> blurCd;
    public final Map<UUID, Long> blurPrimedUntil;
    public final Map<UUID, Integer> blurUses;
    public final Set<UUID> blurActive;
    public final Map<UUID, UUID> blurVictimToAttacker;
    public final Map<UUID, Long> blurLinkExpiry;
    public static final int STORM_MAX_CHARGES = 5;
    public static final long STORM_RECHARGE_MS = 20000L;
    private final Map<UUID, Deque<Long>> stormRecharges;

    public SpeedGemListener() {
        ConcurrentHashMap var2 = new ConcurrentHashMap();
        this.cd1 = var2;
        ConcurrentHashMap var13 = new ConcurrentHashMap();
        this.thunderStepCd = var13;
        KeySetView var14 = ConcurrentHashMap.newKeySet();
        this.thunderStepPrimed = var14;
        ConcurrentHashMap var15 = new ConcurrentHashMap();
        this.blurCd = var15;
        ConcurrentHashMap var16 = new ConcurrentHashMap();
        this.blurPrimedUntil = var16;
        ConcurrentHashMap var17 = new ConcurrentHashMap();
        this.blurUses = var17;
        KeySetView var18 = ConcurrentHashMap.newKeySet();
        this.blurActive = var18;
        ConcurrentHashMap var19 = new ConcurrentHashMap();
        this.blurVictimToAttacker = var19;
        ConcurrentHashMap var20 = new ConcurrentHashMap();
        this.blurLinkExpiry = var20;
        ConcurrentHashMap var21 = new ConcurrentHashMap();
        this.stormRecharges = var21;
    }

    public int stormCharges(UUID var1, int var2) {
        String var6 = "speed";
        String var7 = "speed-storm-charges";
        double var28 = GemTuning.power(var6, var7, 5.0, 1503850832);
        int var10 = (int)Math.max(1.0, var28);
        Map var16 = this.stormRecharges;
        Deque var11 = (Deque)var16.get(var1);
        if (var11 == null) {
            return var10;
        } else {
            Predicate<Long> var25 = var0 -> {
                long var4 = var0;
                long var2x = System.currentTimeMillis();
                boolean var7x;
                if (var4 <= var2x) {
                    var7x = true;
                } else {
                    var7x = false;
                }

                return var7x;
            };
            var11.removeIf(var25);
            int var31 = var11.size();
            int var27 = var10 - var31;
            return Math.max(0, var27);
        }
    }

    public void spendStormCharge(UUID var1) {
        this.spendStormCharge(var1, 20000L, 56212168);
    }

    public void spendStormCharge(UUID var1, long var2, int var4) {
        Map var15 = this.stormRecharges;
        Function<UUID, Deque<Long>> var7 = var0 -> new ArrayDeque<>();
        Deque<Long> var10 = (Deque<Long>)var15.computeIfAbsent(var1, var7);
        long var11 = System.currentTimeMillis();
        Predicate<Long> var31 = var2x -> {
            long var6 = var2x;
            boolean var9;
            if (var6 <= var11) {
                var9 = true;
            } else {
                var9 = false;
            }

            return var9;
        };
        var10.removeIf(var31);
        long var24;
        if (var10.isEmpty()) {
            var24 = var11;
        } else {
            long var40 = (Long)var10.peekLast();
            var24 = Math.max(var11, var40);
        }

        Long var36 = var24 + var2;
        var10.addLast(var36);
    }

    public long stormNextRechargeMs(UUID var1, int var2) {
        Map var14 = this.stormRecharges;
        Deque var7 = (Deque)var14.get(var1);
        if (var7 == null) {
            return 0L;
        } else {
            long var8 = System.currentTimeMillis();
            long var10 = Long.MAX_VALUE;

            for (Long var13 : (Deque<Long>)var7) {
                long var39 = var13;
                if (var39 > var8) {
                    long var51 = var13;
                    var10 = Math.min(var10, var51);
                }
            }

            long var33;
            if (var10 == Long.MAX_VALUE) {
                var33 = 0L;
            } else {
                var33 = var10 - var8;
            }

            return var33;
        }
    }

    public void clear(UUID var1) {
        Map var4 = this.cd1;
        var4.remove(var1);
        Map var7 = this.thunderStepCd;
        var7.remove(var1);
        Set var10 = this.thunderStepPrimed;
        var10.remove(var1);
        Map var13 = this.blurCd;
        var13.remove(var1);
        Map var16 = this.blurPrimedUntil;
        var16.remove(var1);
        Set var19 = this.blurActive;
        var19.remove(var1);
        Map var22 = this.blurUses;
        var22.remove(var1);
        Map var25 = this.blurVictimToAttacker;
        var25.remove(var1);
        Map var28 = this.blurLinkExpiry;
        var28.remove(var1);
        Map var31 = this.stormRecharges;
        var31.remove(var1);
    }
}
