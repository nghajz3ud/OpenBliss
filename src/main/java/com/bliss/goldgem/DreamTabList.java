package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

public final class DreamTabList implements Listener {
    private final BlissPlugin plugin;

    public DreamTabList(BlissPlugin var1, int var2) {
        this.plugin = var1;
    }

    private static boolean dreamSide(Player var0, int var1) {
        return GoldenDreamWorld.isDreamWorld(var0.getWorld());
    }

    public void refresh(Player var1, boolean var2, int var3) {
        if (var1.isOnline()) {
            boolean var7 = dreamSide(var1, 1481898347);

            for (Player var9 : this.plugin.getServer().getOnlinePlayers()) {
                if (var9 != var1) {
                    boolean var26 = dreamSide(var9, 1481898347);
                    boolean var27;
                    if (var26 == var7) {
                        var27 = true;
                    } else {
                        var27 = false;
                    }

                    if (var2 && var9.canSee(var1)) {
                        var9.listPlayer(var1);
                    }

                    list(var9, var1, var27, 163466215);
                    list(var1, var9, var27, 163466215);
                }
            }
        }
    }

    public void refreshAll(int var1) {
        for (Player var6 : this.plugin.getServer().getOnlinePlayers()) {
            this.refresh(var6, false, 141712163);
        }
    }

    public void listAll(int var1) {
        for (Player var6 : this.plugin.getServer().getOnlinePlayers()) {
            for (Player var8 : this.plugin.getServer().getOnlinePlayers()) {
                if (var6 != var8) {
                    list(var6, var8, true, 163466215);
                }
            }
        }
    }

    private static void list(Player var0, Player var1, boolean var2, int var3) {
        if (var0.canSee(var1)) {
            if (var2) {
                if (!var0.isListed(var1)) {
                    var0.listPlayer(var1);
                }
            } else if (var0.isListed(var1)) {
                var0.unlistPlayer(var1);
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onJoin(PlayerJoinEvent var1) {
        Player var6 = var1.getPlayer();
        BukkitScheduler var11 = this.plugin.getServer().getScheduler();
        BlissPlugin var13 = this.plugin;
        Runnable var14 = () -> this.refresh(var6, true, 141712163);
        var11.runTask(var13, var14);
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onWorld(PlayerChangedWorldEvent var1) {
        boolean var6 = GoldenDreamWorld.isDreamWorld(var1.getFrom());
        boolean var9 = dreamSide(var1.getPlayer(), 1481898347);
        if (var6 != var9) {
            Player var11 = var1.getPlayer();
            this.refresh(var11, false, 141712163);
        }
    }
}
