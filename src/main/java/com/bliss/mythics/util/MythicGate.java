package com.bliss.mythics.util;

import com.bliss.BlissPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class MythicGate {
    private MythicGate() {
    }

    public static boolean disabledInSpawn(Player var0, int var1) {
        if (var0 == null) {
            return false;
        } else {
            if (Bukkit.getPluginManager().getPlugin("BlissPlugin") instanceof BlissPlugin var13 && var13.gemsDisabledInSpawn(var0, 1308710710)) {
                return true;
            }

            return false;
        }
    }
}
