package com.bliss.pedestal;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public final class PedestalRitualRunner {
    private static final double NEAR_RADIUS = 8.0;
    private static final AtomicInteger GEN;

    static void invalidateSteps(int var0) {
        GEN.incrementAndGet();
    }

    private PedestalRitualRunner() {
    }

    public static void complete(BlissPlugin var0, PedestalState var1, PlayerDataStore var2, BlissCoreItems var3, int var4) {
        Location var48;
        if (var1.subLoc(135424274) != null) {
            var48 = var1.subLoc(135424274);
        } else {
            var48 = var1.mainLoc(15453813);
        }

        Location var16 = var48;
        if (var16 != null) {
            PedestalState.Ritual var17 = var1.ritual(1751702735);
            UUID var18 = var1.revivingPlayer(203133736);
            int var19 = GEN.incrementAndGet();
            World var34 = var16.getWorld();
            Sound var7 = Sound.ITEM_AXE_SCRAPE;
            var34.playSound(var16, var7, 10.0F, 0.5F);
            World var36 = var16.getWorld();
            Sound var52 = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
            var36.playSound(var16, var52, 20.0F, 1.2F);

            for (int var20 = 1; var20 <= 5; var20++) {
                long var21 = (long)var20 * 200L;
                int var23 = var20;
                BukkitScheduler var45 = Bukkit.getScheduler();
                Runnable var56 = () -> {
                    int var68 = GEN.get();
                    if (var19 == var68) {
                        PedestalState.Ritual var18x = var1.ritual(1751702735);
                        PedestalState.Ritual var69 = PedestalState.Ritual.REPAIR;
                        if (var18x == var69) {
                            World var20x = var16.getWorld();
                            Sound var11 = Sound.BLOCK_BEACON_AMBIENT;
                            var20x.playSound(var16, var11, 40.0F, 1.6F);
                            drawHappyRings(var16, 878618867);
                            PedestalState.Ritual var71 = PedestalState.Ritual.REPAIR;
                            if (var17 == var71) {
                                for (Player var97 : var16.getWorld().getPlayers()) {
                                    World var47 = var97.getLocation().getWorld();
                                    World var80 = var16.getWorld();
                                    if (var47.equals(var80)) {
                                        Location var50 = var97.getLocation();
                                        if (!(var50.distanceSquared(var16) > 64.0) && !JuggernautGemGuardListener.isJuggernaut(var97, 859003337)) {
                                            UUID var83 = var97.getUniqueId();
                                            PlayerData var16x = var2.get(var83, 1264010596);
                                            if (var16x.energy(1160853318) > 1 && var16x.energy(1160853318) < 11) {
                                                var16x.addEnergy(1, 514979476);
                                                var3.refreshHeldGemItem(var97, var16x, 835235475);
                                                if (var16x.energy(1160853318) >= 11) {
                                                    AchievementStore var67 = var0.advancements(1975441698);
                                                    AchievementStore.Advancement var95 = AchievementStore.Advancement.OVERFLOWING;
                                                    var67.unlock(var97, var95, 1132982324);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                PedestalState.Ritual var72 = PedestalState.Ritual.REVIVE;
                                if (var17 == var72 && var18 != null) {
                                    PlayerData var14 = var2.get(var18, 1264010596);
                                    var14.setEnergy(11, 319922676);
                                    Player var15 = Bukkit.getPlayer(var18);
                                    if (var15 != null) {
                                        var3.refreshHeldGemItem(var15, var14, 835235475);
                                        AchievementStore var33 = var0.advancements(1975441698);
                                        AchievementStore.Advancement var91 = AchievementStore.Advancement.OVERFLOWING;
                                        var33.unlock(var15, var91, 1132982324);
                                    }
                                }
                            }

                            if (var23 == 5) {
                                finish(var0, var1, 6141580);
                            }
                        }
                    }
                };
                var45.runTaskLater(var0, var56, var21);
            }
        }
    }

    private static void finish(BlissPlugin var0, PedestalState var1, int var2) {
        Location var8 = var1.mainLoc(15453813);
        if (var8 != null) {
            World var14 = var8.getWorld();
            Sound var5 = Sound.BLOCK_BEACON_DEACTIVATE;
            var14.playSound(var8, var5, 20.0F, 2.0F);

            for (Player var10 : var8.getWorld().getPlayers()) {
                Location var25 = var10.getLocation();
                if (var25.distanceSquared(var8) <= 64.0) {
                    Component var37 = Text.legacy("&aThe pedestal ritual is complete.", 373192400);
                    var10.sendMessage(var37);
                }
            }
        }

        var1.removeBeam(1137327363);
        var1.setActive(false, 1028669057);
        var1.resetRitual(1772872156);
        var1.unbuildStructure(280713799);
        var1.save(973788971);
    }

    private static void drawHappyRings(Location var0, int var1) {
        double[] var32 = new double[]{0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.5, 3.0, 3.5, 4.0};

        for (double var22 : var32) {
            double var83 = var22 * 12.0;
            int var24 = (int)Math.max(12.0, var83);

            for (int var25 = 0; var25 < var24; var25++) {
                double var48 = (double)var25;
                double var86 = (double)var24;
                double var26 = Math.toRadians(var48 / var86 * 360.0);
                double var57 = var0.getX();
                double var120 = Math.cos(var26);
                double var92 = var22 * var120;
                double var28 = var57 + var92;
                double var62 = var0.getZ();
                double var124 = Math.sin(var26);
                double var96 = var22 * var124;
                double var30 = var62 + var96;
                World var67 = var0.getWorld();
                Particle var71 = Particle.HAPPY_VILLAGER;
                double var129 = var0.getY() + 0.1;
                Particles.spawn(var67, var71, var28, var129, var30, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
            }
        }
    }

    static {
        AtomicInteger var0 = new AtomicInteger();
        GEN = var0;
    }
}
