package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.goldgem.GoldGem;
import com.bliss.goldgem.GoldenDreamWorld;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class PovOverlay {
    public static final long DAY = 80039L;
    static final int MAX_LEVEL = 15;
    private static final int FADE_OUT_TICKS = 10;
    private static final long HOUR_CAP = 23808L;
    private static final Map<UUID, BukkitTask> running;

    private PovOverlay() {
    }

    public static boolean plainClock(Player var0, int var1) {
        boolean var10;
        if (var0.isPlayerTimeRelative() && var0.getPlayerTimeOffset() == 0L) {
            var10 = true;
        } else {
            var10 = false;
        }

        return var10;
    }

    public static boolean isRunning(Player var0) {
        Map var1 = running;
        UUID var4 = var0.getUniqueId();
        return var1.containsKey(var4);
    }

    public static boolean show(BlissPlugin var0, final Player var1, GemType var2, int var3, int var4) {
        final int var11 = GoldGem.bit(var2, 880748148);
        if (var11 < 0) {
            return false;
        } else {
            Map var18 = running;
            UUID var37 = var1.getUniqueId();
            BukkitTask var12 = (BukkitTask)var18.remove(var37);
            if (var12 != null) {
                var12.cancel();
            } else if (!plainClock(var1, 1911943038)) {
                return false;
            }

            final int var13 = Math.max(1, var3) + 10;
            send(var1, var11, 15, 1857096040);
            BukkitScheduler var29 = var0.getServer().getScheduler();
            Runnable var44 = new Runnable() {
                int tick = 0;

                @Override
                public void run() {
                    int var22 = this.tick + 1;
                    this.tick = var22;
                    if (!var1.isOnline()) {
                        PovOverlay.stop(var1, false, 709807423);
                    } else {
                        int var10 = this.tick;
                        int var24 = var13;
                        if (var10 >= var24) {
                            PovOverlay.stop(var1, true, 709807423);
                        } else {
                            int var14 = var13;
                            int var27 = this.tick;
                            int var5 = var14 - var27;
                            if (var5 <= 10) {
                                Player var18 = var1;
                                int var30 = var11;
                                int var35 = var5 * 15 / 10;
                                int var33 = Math.max(1, var35);
                                PovOverlay.send(var18, var30, var33, 1857096040);
                            }
                        }
                    }
                }
            };
            BukkitTask var14 = var29.runTaskTimer(var0, var44, 1L, 1L);
            Map var31 = running;
            UUID var43 = var1.getUniqueId();
            var31.put(var43, var14);
            return true;
        }
    }

    public static boolean hide(Player var0, int var1) {
        Map var2 = running;
        UUID var8 = var0.getUniqueId();
        if (!var2.containsKey(var8)) {
            return false;
        } else {
            stop(var0, true, 709807423);
            return true;
        }
    }

    public static void shutdown() {
        Set<UUID> var24 = running.keySet();

        for (UUID var3 : new ArrayList<>(var24)) {
            Map var12 = running;
            BukkitTask var4 = (BukkitTask)var12.remove(var3);
            if (var4 != null) {
                var4.cancel();
            }

            Player var5 = Bukkit.getPlayer(var3);
            if (var5 != null && var5.isOnline()) {
                var5.resetPlayerTime();
            }
        }
    }

    private static void stop(Player var0, boolean var1, int var2) {
        Map var3 = running;
        UUID var23 = var0.getUniqueId();
        BukkitTask var5 = (BukkitTask)var3.remove(var23);
        if (var5 != null) {
            var5.cancel();
        }

        if (var1 && var0.isOnline()) {
            var0.resetPlayerTime();
            BlissPlugin var6 = (BlissPlugin)JavaPlugin.getPlugin(BlissPlugin.class);
            if (var6.goldenDream(2108340810) != null) {
                GoldenDreamWorld var22 = var6.goldenDream(2108340810).dream(2117018474);
                UUID var25 = var0.getUniqueId();
                var22.forgetGrade(var25, 1777841881);
            }
        }
    }

    private static void send(Player var0, int var1, int var2, int var3) {
        long var47 = Math.min(var0.getWorld().getFullTime() % 24000L / 128L * 128L, 23808L);
        long var39 = (long)var1;
        long var35 = var47 + var39;
        long var45 = (long)var2;
        long var43 = 8L * var45;
        long var37 = var35 + var43;
        SignatureClock.send(var0, 80039L, var37, 399305443);
    }

    static {
        HashMap var0 = new HashMap();
        running = var0;
    }
}
