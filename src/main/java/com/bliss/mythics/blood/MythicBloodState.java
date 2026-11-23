package com.bliss.mythics.blood;

import com.bliss.core.GemTuning;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.Function;
import java.util.function.Predicate;
import org.bukkit.Location;
import org.bukkit.World;

public final class MythicBloodState {
    public final Map<UUID, Deque<Long>> bloodsawCd;
    public final Map<UUID, Long> lastCastAt;
    public final List<MythicBloodState.Fountain> fountains;
    public final Map<UUID, Long> fountainReadyAt;
    public final Map<UUID, MythicBloodState.LinkGroup> linkOf;
    public final Set<UUID> reflecting;
    public final Set<UUID> ritualActive;
    public final Map<UUID, Long> ritualImmuneUntil;
    public final Map<UUID, Long> ritualReadyAt;
    public final Set<UUID> strengthII;

    public MythicBloodState() {
        ConcurrentHashMap var2 = new ConcurrentHashMap();
        this.bloodsawCd = var2;
        ConcurrentHashMap var13 = new ConcurrentHashMap();
        this.lastCastAt = var13;
        CopyOnWriteArrayList var14 = new CopyOnWriteArrayList();
        this.fountains = var14;
        ConcurrentHashMap var15 = new ConcurrentHashMap();
        this.fountainReadyAt = var15;
        ConcurrentHashMap var16 = new ConcurrentHashMap();
        this.linkOf = var16;
        KeySetView var17 = ConcurrentHashMap.newKeySet();
        this.reflecting = var17;
        KeySetView var18 = ConcurrentHashMap.newKeySet();
        this.ritualActive = var18;
        ConcurrentHashMap var19 = new ConcurrentHashMap();
        this.ritualImmuneUntil = var19;
        ConcurrentHashMap var20 = new ConcurrentHashMap();
        this.ritualReadyAt = var20;
        KeySetView var21 = ConcurrentHashMap.newKeySet();
        this.strengthII = var21;
    }

    public static boolean ricochetHalfDamage(int var0) {
        String var1 = "heretic";
        String var2 = "ricochet-half";
        return GemTuning.mythicFlag(var1, var2, true, 200661716);
    }

    public void useSaw(UUID var1, long var2, long var4, int var6) {
        Map<UUID, Deque<Long>> var17 = this.bloodsawCd;
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

    public int sawsAvailable(UUID var1, int var2, long var3, int var5) {
        Map var10 = this.bloodsawCd;
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
                Map var20 = this.bloodsawCd;
                var20.remove(var1);
            }

            int var30 = var9.size();
            int var29 = var2 - var30;
            return Math.max(0, var29);
        }
    }

    public long nextSawReady(UUID var1, int var2) {
        Map var13 = this.bloodsawCd;
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

    public MythicBloodState.Fountain fountainAt(Location var1, long var2, int var4) {
        MythicBloodState.Fountain var9 = null;

        for (MythicBloodState.Fountain var11 : this.fountains) {
            long var22 = var11.untilMs;
            if (var22 <= var2) {
                List var29 = this.fountains;
                var29.remove(var11);
            } else if (var11.contains(var1, 1663940311)) {
                var9 = var11;
            }
        }

        return var9;
    }

    public int activeFountainSecondsBy(UUID var1, long var2) {
        long var9 = 0L;

        for (MythicBloodState.Fountain var12 : this.fountains) {
            long var34 = var12.untilMs;
            if (var34 > var2) {
                UUID var38 = var12.casterId;
                if (var38.equals(var1)) {
                    long var41 = var12.untilMs;
                    if (var41 > var9) {
                        var9 = var12.untilMs;
                    }
                }
            }
        }

        int var21;
        if (var9 == 0L) {
            var21 = 0;
        } else {
            var21 = (int)((var9 - var2 + 999L) / 1000L);
        }

        return var21;
    }

    public MythicBloodState.LinkGroup activeLink(UUID var1, long var2, int var4) {
        Map var10 = this.linkOf;
        MythicBloodState.LinkGroup var9 = (MythicBloodState.LinkGroup)var10.get(var1);
        if (var9 == null) {
            return null;
        } else {
            long var16 = var9.untilMs;
            return var16 <= var2 ? null : var9;
        }
    }

    public static Set<UUID> newMemberSet(int var0) {
        return ConcurrentHashMap.newKeySet();
    }

    public boolean ritualImmune(UUID var1, int var2) {
        Map var8 = this.ritualImmuneUntil;
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
        Map var5 = this.bloodsawCd;
        var5.remove(var1);
        Map var8 = this.ritualReadyAt;
        var8.remove(var1);
        Map var11 = this.lastCastAt;
        var11.remove(var1);
        Map var14 = this.fountainReadyAt;
        var14.remove(var1);
    }

    public void clearTransient(UUID var1, int var2) {
        Set var5 = this.ritualActive;
        var5.remove(var1);
        Map var8 = this.ritualImmuneUntil;
        var8.remove(var1);
        Set var11 = this.strengthII;
        var11.remove(var1);
        Set var14 = this.reflecting;
        var14.remove(var1);
    }

    public static final class Fountain {
        public final UUID casterId;
        public final Location center;
        public final double radius;
        public final long untilMs;

        public Fountain(UUID var1, Location var2, double var3, long var5, int var7) {
            this.casterId = var1;
            Location var15 = var2.clone();
            this.center = var15;
            this.radius = var3;
            this.untilMs = var5;
        }

        public boolean contains(Location var1, int var2) {
            if (var1.getWorld() != null && this.center.getWorld() != null) {
                World var20 = this.center.getWorld();
                World var55 = var1.getWorld();
                if (!var20.equals(var55)) {
                    return false;
                } else {
                    double var24 = this.center.getX();
                    double var56 = var1.getX();
                    double var8 = var24 - var56;
                    double var30 = this.center.getZ();
                    double var59 = var1.getZ();
                    double var10 = var30 - var59;
                    double var35 = var1.getY();
                    double var63 = this.center.getY();
                    double var12 = var35 - var63;
                    double var41 = var8 * var8;
                    double var69 = var10 * var10;
                    double var43 = var41 + var69;
                    double var72 = this.radius;
                    double var81 = this.radius;
                    double var74 = var72 * var81;
                    boolean var54;
                    if (var43 <= var74 && var12 >= -3.0 && var12 <= 18.0) {
                        var54 = true;
                    } else {
                        var54 = false;
                    }

                    return var54;
                }
            } else {
                return false;
            }
        }
    }

    public static final class LinkGroup {
        public final Set<UUID> members;
        public final long untilMs;

        public LinkGroup(Set<UUID> var1, long var2, int var4) {
            this.members = var1;
            this.untilMs = var2;
        }
    }
}
