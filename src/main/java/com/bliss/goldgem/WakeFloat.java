package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import com.bliss.nick.MemoryRoster;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public final class WakeFloat {
    private static final double RISE = 0.07;
    private static final double HEIGHT = 20.0;
    private static final String MESSAGE = "Wake Up.";
    private static final Map<UUID, Location> from;
    private static BukkitTask task;

    private WakeFloat() {
    }

    public static int start(BlissPlugin var0, int var1) {
        final GoldenDreamWorld var46;
        if (var0.goldenDream(2108340810) != null) {
            var46 = var0.goldenDream(2108340810).dream(2117018474);
        } else {
            var46 = null;
        }

        int var7 = 0;

        for (Player var9 : Bukkit.getOnlinePlayers()) {
            Map var28 = from;
            UUID var49 = var9.getUniqueId();
            if (!var28.containsKey(var49) && GoldenDreamWorld.isDreamWorld(var9.getWorld()) && !var9.hasMetadata("NPC")) {
                if (var0.memoryRoster(133420166) != null) {
                    MemoryRoster var38 = var0.memoryRoster(133420166);
                    UUID var52 = var9.getUniqueId();
                    if (var38.isMemory(var52, 1860168534)) {
                        continue;
                    }
                }

                if (!var9.isDead()) {
                    Map var42 = from;
                    UUID var54 = var9.getUniqueId();
                    Location var61 = var9.getLocation().clone();
                    var42.put(var54, var61);
                    var9.setGravity(false);
                    var9.setAllowFlight(true);
                    var7++;
                }
            }
        }

        if (from.isEmpty()) {
            return 0;
        } else {
            if (task == null) {
                BukkitRunnable var22 = new BukkitRunnable() {
                    public void run() {
                        Iterator var12 = WakeFloat.from.entrySet().iterator();

                        while (var12.hasNext()) {
                            Entry var13 = (Entry)var12.next();
                            Player var14 = Bukkit.getPlayer((UUID)var13.getKey());
                            if (var14 != null && var14.isOnline()) {
                                Location var15 = (Location)var13.getValue();
                                if (!var14.isDead() && GoldenDreamWorld.isDreamWorld(var14.getWorld())) {
                                    double var41 = var14.getLocation().getY();
                                    double var77 = var15.getY() + 20.0;
                                    if (var41 >= var77) {
                                        label37: {
                                            WakeFloat.ground(var14, 1224420547);
                                            if (var46 != null) {
                                                GoldenDreamWorld var48 = var46;
                                                UUID var68 = var14.getUniqueId();
                                                if (var48.inDream(var68, 2004945976)) {
                                                    GoldenDreamWorld var51 = var46;
                                                    var51.wake(var14, 1928909343);
                                                    break label37;
                                                }
                                            }

                                            if (var15.isWorldLoaded()) {
                                                Vector var71 = new Vector();
                                                var14.setVelocity(var71);
                                                var14.teleport(var15);
                                                var14.setFallDistance(0.0F);
                                            }
                                        }

                                        var14.kickPlayer("Wake Up.");
                                        var12.remove();
                                    } else {
                                        Vector var74 = new Vector(0.0, 0.07, 0.0);
                                        var14.setVelocity(var74);
                                    }
                                } else {
                                    WakeFloat.ground(var14, 1224420547);
                                    var12.remove();
                                }
                            } else {
                                var12.remove();
                            }
                        }

                        if (WakeFloat.from.isEmpty()) {
                            this.cancel();
                            WakeFloat.task = null;
                        }
                    }
                };
                task = var22.runTaskTimer(var0, 0L, 1L);
            }

            return var7;
        }
    }

    private static void ground(Player var0, int var1) {
        var0.setGravity(true);
        GameMode var4 = var0.getGameMode();
        GameMode var10 = GameMode.CREATIVE;
        if (var4 != var10) {
            GameMode var11 = GameMode.SPECTATOR;
            if (var4 != var11) {
                var0.setAllowFlight(false);
            }
        }
    }

    public static void shutdown() {
        Iterator var1 = from.keySet().iterator();

        while (var1.hasNext()) {
            Player var3 = Bukkit.getPlayer((UUID)var1.next());
            if (var3 != null && var3.isOnline()) {
                ground(var3, 1224420547);
            }
        }

        from.clear();
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    static {
        HashMap var0 = new HashMap();
        from = var0;
    }
}
