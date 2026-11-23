package com.bliss.pedestal;

import com.bliss.BlissPlugin;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TeamManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class PedestalGlowTask {
    private static final double RADIUS = 8.0;
    private static final long PERIOD_TICKS = 100L;
    private static final int GLOW_DURATION_TICKS = 140;
    private final BlissPlugin plugin;
    private final PedestalState state;
    private final PlayerDataStore players;
    private final Set<UUID> tracked;
    private BukkitTask task;

    public PedestalGlowTask(BlissPlugin var1, PedestalState var2, PlayerDataStore var3, int var4) {
        HashSet var6 = new HashSet();
        this.tracked = var6;
        this.plugin = var1;
        this.state = var2;
        this.players = var3;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 100L, 100L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }

        this.clearAllTracked(1226418562);
    }

    private void tick() {
        Location var5 = this.state.mainLoc(15453813);
        if (var5 != null) {
            PedestalState.Ritual var17 = this.state.ritual(1751702735);
            PedestalState.Ritual var2 = PedestalState.Ritual.REPAIR;
            if (var17 == var2) {
                HashSet var18 = new HashSet();
                HashSet var6 = var18;

                for (Player var10 : var5.getWorld().getPlayers()) {
                    Location var31 = var10.getLocation();
                    if (!(var31.distanceSquared(var5) > 64.0)) {
                        PlayerDataStore var36 = this.players;
                        UUID var52 = var10.getUniqueId();
                        PlayerData var11 = var36.get(var52, 1264010596);
                        GemType var39 = var11.gem(1413186337);
                        GemType var53 = GemType.NONE;
                        if (var39 != var53) {
                            GemType var55 = var11.gem(1413186337);
                            TeamManager.applyGemGlow(var10, var55, 140, 1294384923);
                            Set var42 = this.tracked;
                            UUID var57 = var10.getUniqueId();
                            var42.add(var57);
                            UUID var59 = var10.getUniqueId();
                            var6.add(var59);
                        }
                    }
                }

                Set<UUID> var47 = this.tracked;
                Predicate<UUID> var61 = var1 -> {
                    if (var6.contains(var1)) {
                        return false;
                    } else {
                        Player var4 = Bukkit.getPlayer(var1);
                        if (var4 != null) {
                            TeamManager.clearGemGlow(var4, 2024070305);
                        }

                        return true;
                    }
                };
                var47.removeIf(var61);
                return;
            }
        }

        this.clearAllTracked(1226418562);
    }

    private void clearAllTracked(int var1) {
        if (!this.tracked.isEmpty()) {
            Iterator var3 = this.tracked.iterator();

            while (var3.hasNext()) {
                Player var5 = Bukkit.getPlayer((UUID)var3.next());
                if (var5 != null) {
                    TeamManager.clearGemGlow(var5, 2024070305);
                }
            }

            this.tracked.clear();
        }
    }
}
