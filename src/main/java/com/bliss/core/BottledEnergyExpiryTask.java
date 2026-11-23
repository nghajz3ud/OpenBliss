package com.bliss.core;

import com.bliss.BlissPlugin;
import com.bliss.items.BlissCoreItems;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class BottledEnergyExpiryTask {
    private static final long PERIOD_TICKS = 20L;
    private final BlissPlugin plugin;
    private final BlissCoreItems items;
    private BukkitTask task;

    public BottledEnergyExpiryTask(BlissPlugin var1, BlissCoreItems var2, int var3) {
        this.plugin = var1;
        this.items = var2;
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
    }

    private void tick() {
        if (this.plugin.globalState(724248306).bottledEnergyExpiry(261106370)) {
            long var5 = System.currentTimeMillis();

            for (Player var8 : Bukkit.getOnlinePlayers()) {
                ItemStack[] var9 = var8.getInventory().getContents();
                int var10 = 0;

                while (true) {
                    int var58 = var9.length;
                    if (var10 >= var58) {
                        break;
                    }

                    ItemStack var11 = var9[var10];
                    if (var11 != null) {
                        BlissCoreItems var36 = this.items;
                        if (var36.isBottledEnergy(var11, 1793320558)) {
                            Long var12 = BlissCoreItems.energyExpiryOf(var11, 374126995);
                            if (var12 != null) {
                                long var42 = var12;
                                long var13 = var42 - var5;
                                if (var13 <= 0L) {
                                    BlissCoreItems var50 = this.items;
                                    var50.makeUseless(var11, 607150677);
                                    PlayerInventory var52 = var8.getInventory();
                                    var52.setItem(var10, var11);
                                } else {
                                    BlissCoreItems var54 = this.items;
                                    if (var54.refreshEnergyLoreIfChanged(var11, var13, 1269672779)) {
                                        PlayerInventory var57 = var8.getInventory();
                                        var57.setItem(var10, var11);
                                    }
                                }
                            }
                        }
                    }

                    var10++;
                }
            }
        }
    }
}
