package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.gems.GrantedEffect;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class DepartureFlight implements Listener {
    private static final double AIR_LIFT = 15.0;
    private static final int SLOW_FALL_TICKS = 100;
    private static final int GRACE = 10;
    private static final int MAX_TICKS = 3000;
    private final BlissPlugin plugin;
    private final Map<UUID, DepartureFlight.Drifter> drifting;
    private BukkitTask task;

    public DepartureFlight(BlissPlugin var1, int var2) {
        HashMap var4 = new HashMap();
        this.drifting = var4;
        this.plugin = var1;
    }

    public void begin(UUID var1, int var2) {
        if (this.plugin.audienceGathered(242123729)) {
            Collection<? extends Player> var5 = Bukkit.getOnlinePlayers();

            for (Player var8 : new ArrayList<>(var5)) {
                this.launch(var8, 930755294);
            }
        } else {
            Player var27;
            if (var1 == null) {
                var27 = null;
            } else {
                var27 = Bukkit.getPlayer(var1);
            }

            if (var27 != null) {
                this.launch(var27, 930755294);
            }
        }

        this.plugin.spendAudienceLatch(1437717958);
        this.start(80136520);
    }

    private void launch(Player var1, int var2) {
        DepartureFlight.Drifter var3 = new DepartureFlight.Drifter();
        GameMode var12 = var1.getGameMode();
        GameMode var34 = GameMode.SPECTATOR;
        if (var12 == var34) {
            GameMode var51;
            label27: {
                BlissPlugin var14 = this.plugin;
                UUID var36 = var1.getUniqueId();
                GameMode var10 = var14.audiencePeekMode(var36, 2049808628);
                if (var10 != null) {
                    GameMode var4 = GameMode.SPECTATOR;
                    if (var10 != var4) {
                        var51 = var10;
                        break label27;
                    }
                }

                var51 = null;
            }

            var3.restoreMode = var51;
            GameMode var39 = GameMode.SURVIVAL;
            var1.setGameMode(var39);
        }

        Location var53 = var1.getLocation().add(0.0, 15.0, 0.0);
        if (!var1.teleport(var53)) {
            if (var3.restoreMode != null) {
                GameMode var47 = var3.restoreMode;
                var1.setGameMode(var47);
            }

            Logger var33 = this.plugin.getLogger();
            String var50 = "Departure drift: could not lift " + var1.getName() + " — left as they were.";
            var33.warning(var50);
        } else {
            var1.setFallDistance(0.0F);
            Map var25 = this.drifting;
            UUID var45 = var1.getUniqueId();
            var25.put(var45, var3);
            slowFall(var1, 1879953508);
        }
    }

    private void start(int var1) {
        if (this.task == null && !this.drifting.isEmpty()) {
            BukkitRunnable var3 = new BukkitRunnable() {
                public void run() {
                    DepartureFlight.this.tick(1839852147);
                }
            };
            BlissPlugin var14 = this.plugin;
            BukkitTask var12 = var3.runTaskTimer(var14, 1L, 1L);
            this.task = var12;
        }
    }

    public void shutdown(int var1) {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }

        Set<Entry<UUID, DepartureFlight.Drifter>> var31 = this.drifting.entrySet();
        ArrayList<Entry<UUID, DepartureFlight.Drifter>> var13 = new ArrayList<>(var31);
        this.drifting.clear();

        for (Entry<UUID, DepartureFlight.Drifter> var7 : var13) {
            Player var8 = Bukkit.getPlayer((UUID)var7.getKey());
            if (var8 != null) {
                DepartureFlight.Drifter var34 = (DepartureFlight.Drifter)var7.getValue();
                this.release(var8, var34, 815399165);
            }
        }
    }

    public void releaseIfFlying(Player var1, int var2) {
        Map var7 = this.drifting;
        UUID var12 = var1.getUniqueId();
        DepartureFlight.Drifter var6 = (DepartureFlight.Drifter)var7.remove(var12);
        if (var6 != null) {
            this.release(var1, var6, 815399165);
        }
    }

    private void tick(int var1) {
        if (this.drifting.isEmpty()) {
            if (this.task != null) {
                this.task.cancel();
                this.task = null;
            }
        } else {
            Iterator var5 = this.drifting.entrySet().iterator();

            while (var5.hasNext()) {
                Entry var6 = (Entry)var5.next();
                DepartureFlight.Drifter var7 = (DepartureFlight.Drifter)var6.getValue();
                Player var8 = Bukkit.getPlayer((UUID)var6.getKey());
                if (var8 == null || !var8.isOnline()) {
                    var5.remove();
                } else if (var8.isDead()) {
                    var5.remove();
                    this.release(var8, var7, 815399165);
                } else {
                    int var52 = var7.age + 1;
                    var7.age = var52;
                    if (var7.grace > 0) {
                        int var54 = var7.grace - 1;
                        var7.grace = var54;
                    } else if (var8.isOnGround() || var7.age >= 3000) {
                        var5.remove();
                        this.release(var8, var7, 815399165);
                        continue;
                    }

                    slowFall(var8, 1879953508);
                }
            }
        }
    }

    private void release(Player var1, DepartureFlight.Drifter var2, int var3) {
        var1.setFallDistance(0.0F);
        PotionEffectType var12 = PotionEffectType.SLOW_FALLING;
        GrantedEffect.strip(var1, var12, 553187889);
        if (var2.restoreMode != null) {
            GameMode var14 = var2.restoreMode;
            var1.setGameMode(var14);
        }

        BlissPlugin var11 = this.plugin;
        UUID var16 = var1.getUniqueId();
        var11.audienceModeRestored(var16, 597291661);
    }

    private static void slowFall(Player var0, int var1) {
        PotionEffectType var4 = PotionEffectType.SLOW_FALLING;
        PotionEffect var3 = new PotionEffect(var4, 100, 0, false, false);
        GrantedEffect.grant(var0, var3, 491118892);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Map var6 = this.drifting;
        UUID var19 = var1.getPlayer().getUniqueId();
        DepartureFlight.Drifter var4 = (DepartureFlight.Drifter)var6.remove(var19);
        if (var4 != null) {
            Player var5 = var1.getPlayer();
            PotionEffectType var20 = PotionEffectType.SLOW_FALLING;
            GrantedEffect.strip(var5, var20, 553187889);
            if (var4.restoreMode != null) {
                GameMode var22 = var4.restoreMode;
                var5.setGameMode(var22);
            }

            BlissPlugin var17 = this.plugin;
            UUID var24 = var5.getUniqueId();
            var17.audienceModeRestored(var24, 597291661);
        }
    }

    private static final class Drifter {
        int grace = 10;
        int age;
        GameMode restoreMode;
    }
}
