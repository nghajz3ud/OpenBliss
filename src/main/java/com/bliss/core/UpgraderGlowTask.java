package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.items.BlissCoreItems;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class UpgraderGlowTask {
    private static final long PERIOD_TICKS = 20L;
    private static final int GLOW_DURATION_TICKS = 60;
    private final BlissPlugin plugin;
    private final BlissCoreItems items;
    private final PlayerDataStore players;
    private final GlobalState globalState;
    private final Set<UUID> glowed;
    private BukkitTask task;

    public UpgraderGlowTask(BlissPlugin var1, BlissCoreItems var2, PlayerDataStore var3, GlobalState var4, int var5) {
        HashSet var7 = new HashSet();
        this.glowed = var7;
        this.plugin = var1;
        this.items = var2;
        this.players = var3;
        this.globalState = var4;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.task = var10;
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
        }

        Iterator var2 = this.glowed.iterator();

        while (var2.hasNext()) {
            Player var4 = Bukkit.getPlayer((UUID)var2.next());
            if (var4 != null) {
                TeamManager.clearGemGlow(var4, 2024070305);
            }
        }

        this.glowed.clear();
    }

    private void tick() {
        for (Player var5 : Bukkit.getOnlinePlayers()) {
            UUID var6 = var5.getUniqueId();
            GlobalState var7 = GlobalState.forPlayer(var5, 1433087673);
            boolean var24;
            if (var7.stage(1483650410) < 3 && var7.tier2Glow(1825034216)) {
                var24 = true;
            } else {
                var24 = false;
            }

            GemType var26;
            if (var24) {
                var26 = this.upgradedGemOf(var5, 2001772409);
            } else {
                var26 = null;
            }

            if (var26 != null) {
                TeamManager.applyGemGlow(var5, var26, 60, 1294384923);
                Set var30 = this.glowed;
                var30.add(var6);
            } else {
                Set var33 = this.glowed;
                if (var33.remove(var6)) {
                    TeamManager.clearGemGlow(var5, 2024070305);
                }
            }
        }
    }

    private GemType upgradedGemOf(Player var1, int var2) {
        for (ItemStack var8 : var1.getInventory().getContents()) {
            if (var8 != null) {
                int var9 = BlissCoreItems.gemIdOf(var8, 382441079);
                if (var9 >= 0 && BlissCoreItems.tierOf(var8, 1633828214) == 2) {
                    GemType var10 = GemType.byId(var9);
                    GemType var32 = GemType.NONE;
                    if (var10 != var32) {
                        return var10;
                    }
                }
            }
        }

        return null;
    }
}
