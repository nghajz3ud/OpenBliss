package com.bliss.gems;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;

public final class EntityClickGuard {
    private static final long WINDOW_MS = 100L;
    private static final Map<UUID, Long> lastEntityClick;
    private static final Map<UUID, Integer> lastMeleeTick;

    private EntityClickGuard() {
    }

    public static void markMelee(UUID var0, int var1) {
        Map var2 = lastMeleeTick;
        Integer var6 = Bukkit.getCurrentTick();
        var2.put(var0, var6);
    }

    public static boolean meleeSince(UUID var0, int var1, int var2) {
        Map var3 = lastMeleeTick;
        Integer var5 = (Integer)var3.get(var0);
        if (var5 != null) {
            int var10 = var5;
            if (var10 >= var1) {
                return true;
            }
        }

        return false;
    }

    public static void mark(UUID var0, int var1) {
        Map var2 = lastEntityClick;
        Long var7 = System.currentTimeMillis();
        var2.put(var0, var7);
    }

    public static boolean recentlyClickedEntity(UUID var0, int var1) {
        Map var2 = lastEntityClick;
        Long var5 = (Long)var2.get(var0);
        if (var5 != null) {
            long var9 = System.currentTimeMillis();
            long var15 = var5;
            if (var9 - var15 < 100L) {
                return true;
            }
        }

        return false;
    }

    public static void clear(UUID var0, int var1) {
        Map var2 = lastEntityClick;
        var2.remove(var0);
        Map var5 = lastMeleeTick;
        var5.remove(var0);
    }

    static {
        ConcurrentHashMap var0 = new ConcurrentHashMap();
        lastEntityClick = var0;
        ConcurrentHashMap var1 = new ConcurrentHashMap();
        lastMeleeTick = var1;
    }
}
