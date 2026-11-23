package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemTuning;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TrustStore;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class FireCozyHealTask {
    private static final long PERIOD_TICKS = 10L;
    private static final int COZY_RADIUS = 4;
    private static final int SATURATION_DURATION_TICKS = 15;
    private final BlissPlugin plugin;
    private final FireCozyListener cozy;
    private final TrustStore trust;
    private BukkitTask task;
    private final Map<UUID, Set<UUID>> notified;

    public FireCozyHealTask(BlissPlugin var1, FireCozyListener var2, TrustStore var3, int var4) {
        ConcurrentHashMap var6 = new ConcurrentHashMap();
        this.notified = var6;
        this.plugin = var1;
        this.cozy = var2;
        this.trust = var3;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 10L, 10L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }
    }

    private void tick() {
        if (this.cozy.activeCozies.isEmpty()) {
            this.notified.clear();
        } else {
            Set var24 = this.notified.keySet();
            Set var101 = this.cozy.activeCozies.keySet();
            var24.retainAll(var101);
            String var26 = "fire";
            String var102 = "cozy";
            double var5 = GemTuning.radius(var26, var102, 4.0, 1058601812);
            double var7 = var5 * var5;

            for (Entry var10 : this.cozy.activeCozies.entrySet()) {
                UUID var11 = (UUID)var10.getKey();
                Location var12 = (Location)var10.getValue();
                if (var12 != null && var12.getWorld() != null) {
                    Player var13 = Bukkit.getPlayer(var11);
                    if (var13 != null && var13.isOnline()) {
                        PlayerDataStore var59 = this.plugin.players(1969902410);
                        if (!var59.get(var11, 1264010596).isBroken(430675503)) {
                            World var63 = var13.getWorld();
                            World var105 = var12.getWorld();
                            if (var63.equals(var105)) {
                                Location var66 = var13.getLocation();
                                double var67 = var66.distanceSquared(var12);
                                if (var67 <= var7) {
                                    healOnce(var13, 1948982942);
                                }
                            }

                            Map var72 = this.notified;
                            Function var123 = var0 -> ConcurrentHashMap.newKeySet();
                            Set var14 = (Set)var72.computeIfAbsent(var11, var123);

                            for (Player var16 : Bukkit.getOnlinePlayers()) {
                                if (!var16.equals(var13)) {
                                    World var85 = var16.getWorld();
                                    World var110 = var12.getWorld();
                                    if (var85.equals(var110)) {
                                        Location var88 = var16.getLocation();
                                        double var89 = var88.distanceSquared(var12);
                                        if (!(var89 > var7)) {
                                            TrustStore var93 = this.trust;
                                            UUID var127 = var16.getUniqueId();
                                            if (var93.isTrusted(var11, var127, 1736364606)) {
                                                healOnce(var16, 1948982942);
                                                UUID var114 = var16.getUniqueId();
                                                if (var14.add(var114)) {
                                                    Component var118 = Text.legacy(
                                                        "&6\ud83d\udd2e &bYou are being healed by &6" + var13.getName() + "&b's &6\ud83e\udd7eCozy Campfire",
                                                        373192400
                                                    );
                                                    var16.sendMessage(var118);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void healOnce(Player var0, int var1) {
        if (!var0.isDead()) {
            Attribute var3 = Attribute.MAX_HEALTH;
            AttributeInstance var11 = var0.getAttribute(var3);
            double var18;
            if (var11 == null) {
                var18 = 20.0;
            } else {
                var18 = var11.getValue();
            }

            double var31 = var0.getHealth() + 2.0;
            double var26 = Math.min(var18, var31);
            var0.setHealth(var26);
            PotionEffectType var33 = PotionEffectType.SATURATION;
            PotionEffect var28 = new PotionEffect(var33, 15, 0, true, false, false);
            var0.addPotionEffect(var28);
        }
    }
}
