package com.bliss.core;

import org.bukkit.entity.Player;

public final class SignatureClock {
    private SignatureClock() {
    }

    public static void send(Player var0, long var1, long var3, int var5) {
        if (var0 != null && var0.getWorld() != null) {
            long var13 = var0.getWorld().getFullTime() / 24000L;
            long var26 = (var1 - var13) * 24000L;
            long var28 = var26 + var3;
            var0.setPlayerTime(var28, false);
        }
    }

    public static void sendKeepingHour(Player var0, long var1, int var3) {
        if (var0 != null && var0.getWorld() != null) {
            long var16 = var0.getWorld().getFullTime() % 24000L;
            send(var0, var1, var16, 399305443);
        }
    }
}
