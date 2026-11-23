package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.SignatureClock;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class ControlRoomVeil implements Listener {
    private static final long DARK_DAY = 80017L;
    private static final double RADIUS = 150.0;
    private static final double RADIUS_SQ = 22500.0;
    private static final long PERIOD = 10L;
    private static final long NIGHT = 18000L;
    private final BlissPlugin plugin;
    private final Set<UUID> veiled;
    private BukkitTask task;
    private long pauseLeft;

    public void pause(long var1, int var3) {
        long var9 = this.pauseLeft;
        long var11 = Math.max(var9, var1);
        this.pauseLeft = var11;
        this.liftAll(167366833);
    }

    public ControlRoomVeil(BlissPlugin var1, int var2) {
        HashSet var4 = new HashSet();
        this.veiled = var4;
        this.plugin = var1;
    }

    public void start(int var1) {
        if (this.task == null) {
            BukkitRunnable var3 = new BukkitRunnable() {
                public void run() {
                    ControlRoomVeil.this.tick(1410594982);
                }
            };
            BlissPlugin var11 = this.plugin;
            BukkitTask var9 = var3.runTaskTimer(var11, 20L, 10L);
            this.task = var9;
        }
    }

    public void shutdown(int var1) {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }

        this.liftAll(167366833);
    }

    private void tick(int var1) {
        if (this.pauseLeft > 0L) {
            long var64 = this.pauseLeft - 10L;
            this.pauseLeft = var64;
        } else if (this.plugin.globalState(724248306) != null && !this.plugin.globalState(724248306).shaderEffects(1378906903)) {
            this.liftAll(167366833);
        } else {
            ControlRoomManager var7 = this.plugin.controlRoom(1275615420);
            Location var31;
            if (var7 != null && var7.inSession(1710238328)) {
                var31 = var7.location(1988022717);
            } else {
                var31 = null;
            }

            Location var8 = var31;
            if (var31 == null) {
                this.liftAll(167366833);
            } else {
                World var9 = var31.getWorld();

                for (Player var11 : Bukkit.getOnlinePlayers()) {
                    boolean var50;
                    label45: {
                        World var43 = var11.getWorld();
                        if (var43.equals(var9)) {
                            Location var46 = var11.getLocation();
                            if (var46.distanceSquared(var8) <= 22500.0) {
                                var50 = true;
                                break label45;
                            }
                        }

                        var50 = false;
                    }

                    if (var50) {
                        SignatureClock.send(var11, 80017L, 18000L, 399305443);
                        Set var54 = this.veiled;
                        UUID var71 = var11.getUniqueId();
                        var54.add(var71);
                    } else {
                        Set var57 = this.veiled;
                        UUID var73 = var11.getUniqueId();
                        if (var57.remove(var73)) {
                            var11.resetPlayerTime();
                        }
                    }
                }
            }
        }
    }

    public void liftAll(int var1) {
        if (!this.veiled.isEmpty()) {
            Iterator var3 = this.veiled.iterator();

            while (var3.hasNext()) {
                Player var5 = Bukkit.getPlayer((UUID)var3.next());
                if (var5 != null) {
                    var5.resetPlayerTime();
                }
            }

            this.veiled.clear();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Set var4 = this.veiled;
        UUID var7 = var1.getPlayer().getUniqueId();
        var4.remove(var7);
    }
}
