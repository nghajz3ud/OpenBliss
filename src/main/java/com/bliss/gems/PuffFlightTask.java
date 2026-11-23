package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemType;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class PuffFlightTask {
    private static final long PERIOD_TICKS = 2L;
    private final BlissPlugin plugin;
    private final PuffGemListener puff;
    private BukkitTask task;

    public PuffFlightTask(BlissPlugin var1, PuffGemListener var2, int var3) {
        this.plugin = var1;
        this.puff = var2;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 2L, 2L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        long var6 = System.currentTimeMillis();

        for (Player var9 : Bukkit.getOnlinePlayers()) {
            GameMode var10 = var9.getGameMode();
            GameMode var3 = GameMode.CREATIVE;
            if (var10 != var3) {
                GameMode var68 = GameMode.SPECTATOR;
                if (var10 != var68) {
                    boolean var67;
                    label64: {
                        ItemStack var11 = var9.getInventory().getItemInMainHand();
                        ItemStack var12 = var9.getInventory().getItemInOffHand();
                        int var35 = BlissCoreItems.gemIdOf(var11, 382441079);
                        int var70 = GemType.PUFF.id();
                        if (var35 != var70) {
                            int var37 = BlissCoreItems.gemIdOf(var12, 382441079);
                            int var72 = GemType.PUFF.id();
                            if (var37 != var72) {
                                GemType var73 = GemType.PUFF;
                                if (!GoldGem.isWieldingSoul(var9, var73, 100010596)) {
                                    var67 = false;
                                    break label64;
                                }
                            }
                        }

                        var67 = true;
                    }

                    boolean var66;
                    label58: {
                        Map var42 = this.puff.cd4;
                        UUID var75 = var9.getUniqueId();
                        Long var14 = (Long)var42.get(var75);
                        if (var14 != null) {
                            long var47 = var14;
                            if (var47 > var6) {
                                var66 = true;
                                break label58;
                            }
                        }

                        var66 = false;
                    }

                    boolean var56;
                    label53: {
                        if (var67) {
                            PlayerDataStore var53 = this.plugin.players(1969902410);
                            UUID var77 = var9.getUniqueId();
                            if (var53.get(var77, 1264010596).canUseAbilities(325243089)) {
                                var56 = true;
                                break label53;
                            }
                        }

                        var56 = false;
                    }

                    if (var56 && !var66) {
                        if (!var9.getAllowFlight()) {
                            var9.setAllowFlight(true);
                        }
                    } else if (var9.getAllowFlight()) {
                        var9.setAllowFlight(false);
                        var9.setFlying(false);
                    }
                }
            }
        }
    }
}
