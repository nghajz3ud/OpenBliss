package com.bliss.gems.legacy;

import com.bliss.BlissPlugin;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerDataStore;
import com.bliss.items.BlissCoreItems;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class LegacySoulSpeedTask {
    private static final long PERIOD_TICKS = 20L;
    private static final int EFFECT_TICKS = 30;
    private static final int AMP_T1 = 6;
    private static final int AMP_T2 = 8;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final GlobalState globalState;
    private BukkitTask task;

    public LegacySoulSpeedTask(BlissPlugin var1, PlayerDataStore var2, GlobalState var3, int var4) {
        this.plugin = var1;
        this.players = var2;
        this.globalState = var3;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 20L, 20L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        for (Player var11 : Bukkit.getOnlinePlayers()) {
            GlobalState var23 = GlobalState.forPlayer(var11, 1433087673);
            if (var23.legacySpeedFor(var11, 1914133154)) {
                Material var28 = var11.getLocation().getBlock().getType();
                Material var54 = Material.SOUL_SAND;
                if (var28 == var54) {
                    ItemStack var12 = var11.getInventory().getItemInMainHand();
                    ItemStack var13 = var11.getInventory().getItemInOffHand();
                    int var14 = -1;
                    int var37 = BlissCoreItems.gemIdOf(var12, 382441079);
                    int var56 = GemType.SPEED.id();
                    if (var37 == var56) {
                        var14 = BlissCoreItems.tierOf(var12, 1633828214);
                    } else {
                        int var51 = BlissCoreItems.gemIdOf(var13, 382441079);
                        int var63 = GemType.SPEED.id();
                        if (var51 == var63) {
                            var14 = BlissCoreItems.tierOf(var13, 1633828214);
                        }
                    }

                    if (var14 >= 1) {
                        PlayerDataStore var42 = this.players;
                        UUID var59 = var11.getUniqueId();
                        if (var42.get(var59, 1264010596).canUsePassives(2051432579)) {
                            String var46 = "speed";
                            String var60 = "auto-soul-speed";
                            if (GemTuning.passive(var46, var60, true, 14309468)) {
                                PotionEffectType var4 = PotionEffectType.SPEED;
                                byte var64;
                                if (var14 == 2) {
                                    var64 = 8;
                                } else {
                                    var64 = 6;
                                }

                                PotionEffect var61 = new PotionEffect(var4, 30, var64, true, false, false);
                                var11.addPotionEffect(var61);
                            }
                        }
                    }
                }
            }
        }
    }
}
