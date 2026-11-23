package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import com.bliss.core.SonicBoomSound;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class FluxBeamScript {
    private static final Color CYAN = Color.fromRGB(94, 215, 255);
    private static final Color WHITE = Color.fromRGB(255, 255, 255);
    private static final Color DEEP = Color.fromRGB(0, 190, 220);
    private static final Color PALE = Color.fromRGB(170, 255, 255);
    public static final int MIN_PULSES = 3;
    public static final int MAX_PULSES = 7;
    private static final long PULSE_GAP_TICKS = 4L;
    private static final double ULTRA_LEN = 40.0;
    private static final int ULTRA_TICKS = 100;
    private static final int ULTRA_BURST_TICKS = 70;
    private static final double ULTRA_BURST_RADIUS = 14.0;
    private static final Map<UUID, FluxBeamScript.Aim> FROZEN_AIM;
    private static final int OC_STEPS = 10;
    private static final double OC_HALF_ANGLE = 32.0;
    private static final double OC_LENGTH = 45.0;
    private static final int OC_BOOMS_PER_EDGE = 4;
    private static final double[][] CONE_EDGES;
    private static final boolean FLASH_NEEDS_COLOR;

    private FluxBeamScript() {
    }

    private static void freezeAim(Player var0, int var1) {
        Map var2 = FROZEN_AIM;
        UUID var8 = var0.getUniqueId();
        Location var10 = var0.getEyeLocation().clone();
        Location var12 = var0.getLocation().clone();
        FluxBeamScript.Aim var4 = new FluxBeamScript.Aim(var10, var12);
        var2.put(var8, var4);
    }

    public static void clearAim(UUID var0, int var1) {
        Map var2 = FROZEN_AIM;
        var2.remove(var0);
    }

    private static Location aim(Player var0, int var1) {
        Map var2 = FROZEN_AIM;
        UUID var12 = var0.getUniqueId();
        FluxBeamScript.Aim var4 = (FluxBeamScript.Aim)var2.get(var12);
        Location var11;
        if (var4 == null) {
            var11 = var0.getEyeLocation();
        } else {
            var11 = var4.eye().clone();
        }

        return var11;
    }

    private static Location aimFeet(Player var0, int var1) {
        Map var2 = FROZEN_AIM;
        UUID var12 = var0.getUniqueId();
        FluxBeamScript.Aim var4 = (FluxBeamScript.Aim)var2.get(var12);
        Location var11;
        if (var4 == null) {
            var11 = var0.getLocation();
        } else {
            var11 = var4.feet().clone();
        }

        return var11;
    }

    private static Location front(Player var0, double var1, int var3) {
        Location var8 = aim(var0, 1368675942);
        Location var11 = var8.clone();
        Vector var13 = var8.getDirection();
        Vector var14 = var13.multiply(var1);
        return var11.add(var14);
    }

    public static void playTier(BlissPlugin var0, Player var1, int var2, int var3) {
        freezeAim(var1, 1909633542);
        switch (var2) {
            case 0:
                playTier0(var0, var1, 679675105);
                break;
            case 1:
                playTier1(var0, var1, 2101220926);
                break;
            case 2:
                playTier2(var0, var1, 1412845014);
                break;
            case 3:
                playTier3(var0, var1, 889645080);
                break;
            case 4:
                playTier4(var0, var1, 960703031);
                break;
            case 5:
                playTier5(var0, var1, 1090736242);
                break;
            case 6:
                playTier6(var0, var1, 2042038943);
                break;
            case 7:
                playTier7(var0, var1, 419315282);
                break;
            case 8:
                playTier8(var0, var1, 1950081383);
                break;
            case 9:
                playTier9(var0, var1, 308368532);
                break;
            case 10:
                playTier10(var0, var1, 1649793794);
                break;
            case 11:
                playTier11Overcharge(var0, var1, 444140248);
                break;
            default:
                playTier12Ultra(var0, var1, 922282104);
        }

        if (var2 != 11) {
            spawnEndRingSonicBooms(var1, var2, 1725307334);
        }
    }

    private static void spawnEndRingSonicBooms(Player var0, int var1, int var2) {
        int var17 = beamRangeFor(var1, 1722681478);
        double var103 = (double)Math.max(0, var1);
        double var88 = 0.07 * var103;
        double var18 = 0.3 + var88;
        Location var20 = aim(var0, 1368675942);
        Vector var21 = var20.getDirection().normalize();
        Location var41 = var20.clone();
        Vector var71 = var21.clone();
        Vector var72 = var71.multiply(var17);
        Location var22 = var41.add(var72);
        Vector var23 = horizPerp(var21, 1755382018);
        Vector var46 = var21.clone();
        Vector var24 = var46.crossProduct(var23).normalize();

        for (int var25 = 0; var25 < 4; var25++) {
            double var26 = Math.toRadians((double)var25 * 90.0);
            Vector var59 = var23.clone();
            double var77 = Math.cos(var26);
            double var79 = var77 * var18;
            Vector var60 = var59.multiply(var79);
            Vector var82 = var24.clone();
            double var95 = Math.sin(var26);
            double var97 = var95 * var18;
            Vector var83 = var82.multiply(var97);
            Vector var28 = var60.add(var83);
            Location var63 = var22.clone();
            Location var29 = var63.add(var28);
            World var66 = var22.getWorld();
            Particle var85 = Particle.SONIC_BOOM;
            Particles.spawn(var66, var85, var29, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
            String var70 = "flux-endring|" + var0.getUniqueId();
            World var87 = var22.getWorld();
            SonicBoomSound.burst(var70, var87, var22, 1967648345);
        }
    }

    public static void playDiagonalConeBeams(Player var0, double var1, int var3) {
        if (!(var1 <= 0.0)) {
            Location var21 = aim(var0, 1368675942);
            Vector var22 = var21.getDirection().normalize();
            Vector var23 = horizPerp(var22, 1755382018);
            Vector var46 = var22.clone();
            Vector var24 = var46.crossProduct(var23).normalize();
            double var25 = Math.tan(Math.toRadians(18.0 * var1));
            double var108 = 40.0 * var1;
            int var27 = (int)Math.round(30.0 + var108);
            double[][] var65 = new double[4][];
            double[] var130 = new double[]{1.0, 1.0};
            var65[0] = var130;
            double[] var132 = new double[]{-1.0, 1.0};
            var65[1] = var132;
            double[] var134 = new double[]{1.0, -1.0};
            var65[2] = var134;
            double[] var136 = new double[]{-1.0, -1.0};
            var65[3] = var136;

            for (double[] var32 : var65) {
                Vector var74 = var22.clone();
                Vector var95 = var23.clone();
                double var139 = var32[0];
                double var116 = var25 * var139;
                Vector var96 = var95.multiply(var116);
                Vector var75 = var74.add(var96);
                Vector var98 = var24.clone();
                double var142 = var32[1];
                double var120 = var25 * var142;
                Vector var99 = var98.multiply(var120);
                Vector var33 = var75.add(var99).normalize();
                double var34 = 1.0;

                while (true) {
                    double var123 = (double)var27;
                    if (!(var34 <= var123)) {
                        break;
                    }

                    Location var84 = var21.clone();
                    Vector var101 = var33.clone();
                    Vector var102 = var101.multiply(var34);
                    Location var36 = var84.add(var102);
                    World var87 = var36.getWorld();
                    Particle var103 = Particle.END_ROD;
                    Particles.spawn(var87, var103, var36, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
                    var34 += 0.5;
                }
            }
        }
    }

    private static int beamRangeFor(int var0, int var1) {
        if (var0 <= 2) {
            return 12;
        } else if (var0 <= 5) {
            return 40;
        } else {
            return var0 <= 8 ? 70 : 120;
        }
    }

    private static Location pl2(Player var0, int var1) {
        return front(var0, 1.0, 2012661791);
    }

    private static void playTier0(BlissPlugin var0, Player var1, int var2) {
        Sound var4 = Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED;
        playOnce(var1, var4, 20.0F, 1.0F, 1734520375);
        Sound var24 = Sound.BLOCK_BEACON_POWER_SELECT;
        playOnce(var1, var24, 20.0F, 1.0F, 1734520375);
        Sound var25 = Sound.BLOCK_BEACON_ACTIVATE;
        playOnce(var1, var25, 20.0F, 2.0F, 1734520375);
        Sound var26 = Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE;
        playOnce(var1, var26, 20.0F, 1.0F, 1734520375);
        Sound var27 = Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN;
        playOnce(var1, var27, 20.0F, 1.0F, 1734520375);
        Sound var28 = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
        playOnce(var1, var28, 20.0F, 0.8F, 1734520375);
        energyboom651(pl2(var1, 948902320), 1987373392);

        for (int var9 = 0; var9 < 6; var9++) {
            BukkitScheduler var22 = Bukkit.getScheduler();
            Runnable var37 = () -> energyboom6544844(pl2(var1, 948902320), 1672847186);
            long var47 = (long)var9;
            long var45 = 3L * var47;
            var22.runTaskLater(var0, var37, var45);
        }
    }

    private static void playTier1(BlissPlugin var0, Player var1, int var2) {
        Consumer<Location> var5 = FluxBeamScript::energyboom659;
        Consumer<Location> var6 = FluxBeamScript::energyboom654444;
        lowTierIntro(var0, var1, var5, var6, 6, 3, 1104143122);
    }

    private static void playTier2(BlissPlugin var0, Player var1, int var2) {
        Consumer<Location> var5 = FluxBeamScript::energyboom65444;
        Consumer<Location> var6 = FluxBeamScript::energyboom654444;
        lowTierIntro(var0, var1, var5, var6, 6, 3, 1104143122);
    }

    private static void playTier3(BlissPlugin var0, Player var1, int var2) {
        Consumer<Location> var5 = FluxBeamScript::energyboom654;
        Consumer<Location> var6 = FluxBeamScript::energyboom6544;
        lowTierIntro(var0, var1, var5, var6, 6, 3, 1104143122);
    }

    private static void playTier4(BlissPlugin var0, Player var1, int var2) {
        Consumer<Location> var5 = FluxBeamScript::energyboom607;
        Consumer<Location> var6 = FluxBeamScript::energyboom6077;
        lowTierIntro(var0, var1, var5, var6, 5, 5, 1104143122);
    }

    private static void playTier5(BlissPlugin var0, Player var1, int var2) {
        Consumer<Location> var5 = FluxBeamScript::energyboom609999;
        Consumer<Location> var6 = FluxBeamScript::energyboom60999;
        lowTierIntro(var0, var1, var5, var6, 8, 3, 1104143122);
    }

    private static void lowTierIntro(BlissPlugin var0, Player var1, Consumer<Location> var2, Consumer<Location> var3, int var4, int var5, int var6) {
        Sound var8 = Sound.ITEM_AXE_SCRAPE;
        playOnce(var1, var8, 20.0F, 0.5F, 1734520375);
        BukkitScheduler var15 = Bukkit.getScheduler();
        Runnable var19 = () -> {
            Sound var7 = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var7, 50.0F, 1.6F, 1734520375);
            Sound var21 = Sound.ENTITY_WARDEN_SONIC_BOOM;
            playOnce(var1, var21, 20.0F, 1.0F, 1734520375);
            Sound var22 = Sound.ENTITY_GENERIC_EXPLODE;
            playOnce(var1, var22, 20.0F, 0.75F, 1734520375);
            Location var24 = pl2(var1, 948902320);
            var2.accept(var24);

            for (int var11 = 0; var11 < var4; var11++) {
                BukkitScheduler var19x = Bukkit.getScheduler();
                Runnable var30 = () -> {
                    Location var4xx = pl2(var1, 948902320);
                    var3.accept(var4xx);
                };
                long var35 = (long)var5;
                long var39 = (long)var11;
                long var37 = var35 * var39;
                var19x.runTaskLater(var0, var30, var37);
            }
        };
        var15.runTaskLater(var0, var19, 12L);
    }

    private static void playTier6(BlissPlugin var0, Player var1, int var2) {
        Sound var4 = Sound.ITEM_AXE_SCRAPE;
        playOnce(var1, var4, 20.0F, 0.5F, 1734520375);
        BukkitScheduler var7 = Bukkit.getScheduler();
        Runnable var14 = () -> {
            Sound var2x = Sound.ITEM_AXE_SCRAPE;
            playOnce(var1, var2x, 20.0F, 0.5F, 1734520375);
        };
        var7.runTaskLater(var0, var14, 5L);
        BukkitScheduler var9 = Bukkit.getScheduler();
        Runnable var16 = () -> {
            Sound var3 = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var3, 50.0F, 1.6F, 1734520375);
            Sound var18 = Sound.ENTITY_WARDEN_SONIC_BOOM;
            playOnce(var1, var18, 20.0F, 1.0F, 1734520375);
            Sound var19 = Sound.ENTITY_GENERIC_EXPLODE;
            playOnce(var1, var19, 20.0F, 0.75F, 1734520375);
            energyboom609(pl2(var1, 948902320), 2065244043);

            for (int var7x = 0; var7x < 8; var7x++) {
                BukkitScheduler var16x = Bukkit.getScheduler();
                Runnable var25 = () -> energyboom6099(pl2(var1, 948902320), 1157854521);
                long var32 = (long)var7x;
                long var30 = 3L * var32;
                var16x.runTaskLater(var0, var25, var30);
            }
        };
        var9.runTaskLater(var0, var16, 17L);
    }

    private static void playTier7(BlissPlugin var0, Player var1, int var2) {
        Sound var4 = Sound.ITEM_AXE_SCRAPE;
        playOnce(var1, var4, 20.0F, 0.5F, 1734520375);
        BukkitScheduler var7 = Bukkit.getScheduler();
        Runnable var23 = () -> {
            Sound var2x = Sound.ITEM_AXE_SCRAPE;
            playOnce(var1, var2x, 20.0F, 0.5F, 1734520375);
        };
        var7.runTaskLater(var0, var23, 5L);
        BukkitScheduler var9 = Bukkit.getScheduler();
        Runnable var25 = () -> {
            Sound var3 = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var3, 50.0F, 1.6F, 1734520375);
            Sound var16 = Sound.ENTITY_WARDEN_SONIC_BOOM;
            playOnce(var1, var16, 20.0F, 1.0F, 1734520375);
            Sound var17 = Sound.ENTITY_GENERIC_EXPLODE;
            playOnce(var1, var17, 20.0F, 0.75F, 1734520375);
            Sound var18 = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
            playOnce(var1, var18, 20.0F, 0.8F, 1734520375);
            Location var6 = pl2(var1, 948902320);
            energyboom314(var6, 739772548);
            energyboom315(var6, 920346367);
            energyboom316(var0, var1, var6, 774190454);
            energyboom1002(var0, var1, var6, 895528860);
        };
        var9.runTaskLater(var0, var25, 17L);
        BukkitScheduler var11 = Bukkit.getScheduler();
        Runnable var27 = () -> {
            Location var5 = pl2(var1, 948902320);
            energyboom314(var5, 739772548);
            energyboom315(var5, 920346367);
            energyboom317(var0, var1, var5, 440897030);
            energyboom1002(var0, var1, var5, 895528860);
        };
        var11.runTaskLater(var0, var27, 33L);
        BukkitScheduler var13 = Bukkit.getScheduler();
        Runnable var29 = () -> {
            Sound var2x = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var2x, 50.0F, 1.6F, 1734520375);
        };
        var13.runTaskLater(var0, var29, 37L);
        BukkitScheduler var15 = Bukkit.getScheduler();
        Runnable var31 = () -> {
            Location var5 = pl2(var1, 948902320);
            energyboom314(var5, 739772548);
            energyboom315(var5, 920346367);
            energyboom318(var0, var1, var5, 1621622158);
            energyboom1002(var0, var1, var5, 895528860);
        };
        var15.runTaskLater(var0, var31, 49L);
    }

    private static void playTier8(BlissPlugin var0, Player var1, int var2) {
        Sound var4 = Sound.ENTITY_WARDEN_SONIC_CHARGE;
        playOnce(var1, var4, 10.0F, 1.0F, 1734520375);
        Sound var24 = Sound.ITEM_AXE_SCRAPE;
        playOnce(var1, var24, 20.0F, 0.5F, 1734520375);
        BukkitScheduler var8 = Bukkit.getScheduler();
        Runnable var35 = () -> {
            Sound var2x = Sound.ITEM_AXE_SCRAPE;
            playOnce(var1, var2x, 20.0F, 0.5F, 1734520375);
        };
        var8.runTaskLater(var0, var35, 4L);
        BukkitScheduler var10 = Bukkit.getScheduler();
        Runnable var37 = () -> {
            Sound var2x = Sound.ITEM_AXE_SCRAPE;
            playOnce(var1, var2x, 20.0F, 0.5F, 1734520375);
        };
        var10.runTaskLater(var0, var37, 8L);
        BukkitScheduler var12 = Bukkit.getScheduler();
        Runnable var39 = () -> {
            Sound var2x = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
            playOnce(var1, var2x, 20.0F, 1.2F, 1734520375);
        };
        var12.runTaskLater(var0, var39, 34L);
        BukkitScheduler var14 = Bukkit.getScheduler();
        Runnable var41 = () -> {
            Location var5 = pl2(var1, 948902320);
            energyboom314(var5, 739772548);
            energyboom315(var5, 920346367);
            energyboom1002(var0, var1, var5, 895528860);
            energyboom500(var0, var1, var5, 1053969440);
        };
        var14.runTaskLater(var0, var41, 35L);
        BukkitScheduler var16 = Bukkit.getScheduler();
        Runnable var43 = () -> {
            Sound var3 = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var3, 50.0F, 1.6F, 1734520375);
            Sound var15 = Sound.ENTITY_WARDEN_SONIC_BOOM;
            playOnce(var1, var15, 20.0F, 1.0F, 1734520375);
            Sound var16x = Sound.ENTITY_GENERIC_EXPLODE;
            playOnce(var1, var16x, 20.0F, 0.75F, 1734520375);
            Sound var17 = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
            playOnce(var1, var17, 20.0F, 0.8F, 1734520375);
            Location var6 = pl2(var1, 948902320);
            energyboom314(var6, 739772548);
            energyboom315(var6, 920346367);
            energyboom1002(var0, var1, var6, 895528860);
        };
        var16.runTaskLater(var0, var43, 44L);
        BukkitScheduler var18 = Bukkit.getScheduler();
        Runnable var45 = () -> {
            Location var5 = pl2(var1, 948902320);
            energyboom314(var5, 739772548);
            energyboom315(var5, 920346367);
            energyboom1002(var0, var1, var5, 895528860);
        };
        var18.runTaskLater(var0, var45, 60L);
        BukkitScheduler var20 = Bukkit.getScheduler();
        Runnable var47 = () -> {
            Sound var2x = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var2x, 50.0F, 1.6F, 1734520375);
        };
        var20.runTaskLater(var0, var47, 64L);
        BukkitScheduler var22 = Bukkit.getScheduler();
        Runnable var49 = () -> {
            Location var5 = pl2(var1, 948902320);
            energyboom314(var5, 739772548);
            energyboom315(var5, 920346367);
            energyboom500(var0, var1, var5, 1053969440);
            energyboom1002(var0, var1, var5, 895528860);
        };
        var22.runTaskLater(var0, var49, 76L);
    }

    private static void playTier9(BlissPlugin var0, Player var1, int var2) {
        Sound var4 = Sound.ENTITY_WARDEN_SONIC_CHARGE;
        playOnce(var1, var4, 10.0F, 1.0F, 1734520375);
        Sound var28 = Sound.ITEM_AXE_SCRAPE;
        playOnce(var1, var28, 20.0F, 0.5F, 1734520375);
        BukkitScheduler var8 = Bukkit.getScheduler();
        Runnable var41 = () -> {
            Sound var2x = Sound.ITEM_AXE_SCRAPE;
            playOnce(var1, var2x, 20.0F, 0.5F, 1734520375);
        };
        var8.runTaskLater(var0, var41, 4L);
        BukkitScheduler var10 = Bukkit.getScheduler();
        Runnable var43 = () -> {
            Sound var2x = Sound.ITEM_AXE_SCRAPE;
            playOnce(var1, var2x, 20.0F, 0.5F, 1734520375);
        };
        var10.runTaskLater(var0, var43, 8L);
        BukkitScheduler var12 = Bukkit.getScheduler();
        Runnable var45 = () -> {
            Sound var2x = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
            playOnce(var1, var2x, 20.0F, 1.2F, 1734520375);
        };
        var12.runTaskLater(var0, var45, 34L);
        BukkitScheduler var14 = Bukkit.getScheduler();
        Runnable var47 = () -> {
            Location var5 = pl2(var1, 948902320);
            energyboom3144(var5, 857823121);
            energyboom3155(var5, 264400220);
            energyboom1002(var0, var1, var5, 895528860);
        };
        var14.runTaskLater(var0, var47, 35L);
        BukkitScheduler var16 = Bukkit.getScheduler();
        Runnable var49 = () -> {
            Sound var3 = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var3, 50.0F, 1.6F, 1734520375);
            Sound var18x = Sound.ENTITY_WARDEN_SONIC_BOOM;
            playOnce(var1, var18x, 20.0F, 1.0F, 1734520375);
            Sound var19 = Sound.ENTITY_GENERIC_EXPLODE;
            playOnce(var1, var19, 20.0F, 0.75F, 1734520375);
            Sound var20x = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
            playOnce(var1, var20x, 20.0F, 0.8F, 1734520375);
            Location var6 = pl2(var1, 948902320);
            energyboom3144(var6, 857823121);
            energyboom3155(var6, 264400220);
            energyboom500(var0, var1, var6, 1053969440);
            energyboom503(var0, var1, var6, 2104672860);
            energyboom504(var0, var1, var6, 1207186834);
            energyboom1002(var0, var1, var6, 895528860);
        };
        var16.runTaskLater(var0, var49, 44L);
        BukkitScheduler var18 = Bukkit.getScheduler();
        Runnable var51 = () -> {
            Location var5 = pl2(var1, 948902320);
            energyboom3144(var5, 857823121);
            energyboom3155(var5, 264400220);
            energyboom501(var0, var1, var5, 1279033734);
            energyboom502(var0, var1, var5, 604099853);
            energyboom1002(var0, var1, var5, 895528860);
        };
        var18.runTaskLater(var0, var51, 60L);
        BukkitScheduler var20 = Bukkit.getScheduler();
        Runnable var53 = () -> {
            Sound var2x = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var2x, 50.0F, 1.6F, 1734520375);
        };
        var20.runTaskLater(var0, var53, 64L);
        BukkitScheduler var22 = Bukkit.getScheduler();
        Runnable var55 = () -> {
            Location var5 = pl2(var1, 948902320);
            energyboom3144(var5, 857823121);
            energyboom3155(var5, 264400220);
            energyboom1002(var0, var1, var5, 895528860);
        };
        var22.runTaskLater(var0, var55, 76L);
        BukkitScheduler var24 = Bukkit.getScheduler();
        Runnable var57 = () -> {
            Sound var2x = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var2x, 50.0F, 1.6F, 1734520375);
        };
        var24.runTaskLater(var0, var57, 84L);
        BukkitScheduler var26 = Bukkit.getScheduler();
        Runnable var59 = () -> {
            Location var5 = pl2(var1, 948902320);
            energyboom3144(var5, 857823121);
            energyboom3155(var5, 264400220);
            energyboom1002(var0, var1, var5, 895528860);
        };
        var26.runTaskLater(var0, var59, 92L);
    }

    private static void playTier10(BlissPlugin var0, Player var1, int var2) {
        Sound var4 = Sound.ENTITY_WARDEN_SONIC_CHARGE;
        playOnce(var1, var4, 10.0F, 1.0F, 1734520375);
        Sound var28 = Sound.ITEM_AXE_SCRAPE;
        playOnce(var1, var28, 20.0F, 0.5F, 1734520375);
        BukkitScheduler var8 = Bukkit.getScheduler();
        Runnable var41 = () -> {
            Sound var2x = Sound.ITEM_AXE_SCRAPE;
            playOnce(var1, var2x, 20.0F, 0.5F, 1734520375);
        };
        var8.runTaskLater(var0, var41, 4L);
        BukkitScheduler var10 = Bukkit.getScheduler();
        Runnable var43 = () -> {
            Sound var2x = Sound.ITEM_AXE_SCRAPE;
            playOnce(var1, var2x, 20.0F, 0.5F, 1734520375);
        };
        var10.runTaskLater(var0, var43, 8L);
        BukkitScheduler var12 = Bukkit.getScheduler();
        Runnable var45 = () -> {
            Sound var2x = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
            playOnce(var1, var2x, 20.0F, 1.2F, 1734520375);
        };
        var12.runTaskLater(var0, var45, 34L);
        BukkitScheduler var14 = Bukkit.getScheduler();
        Runnable var47 = () -> {
            Location var6 = front(var1, 2.0, 2012661791);
            Location var7 = front(var1, 1.3, 2012661791);
            energyboom31444(var6, 1347904311);
            energyboom3155(var7, 264400220);
            energyboom1002(var0, var1, var6, 895528860);
            energyboom505(var0, var1, var6, 287803222);
        };
        var14.runTaskLater(var0, var47, 35L);
        BukkitScheduler var16 = Bukkit.getScheduler();
        Runnable var49 = () -> {
            Sound var3 = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var3, 50.0F, 1.6F, 1734520375);
            Sound var18x = Sound.ENTITY_WARDEN_SONIC_BOOM;
            playOnce(var1, var18x, 20.0F, 1.0F, 1734520375);
            Sound var19 = Sound.ENTITY_GENERIC_EXPLODE;
            playOnce(var1, var19, 20.0F, 0.75F, 1734520375);
            Sound var20x = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
            playOnce(var1, var20x, 20.0F, 0.8F, 1734520375);
            Location var6 = front(var1, 2.0, 2012661791);
            Location var7 = front(var1, 1.3, 2012661791);
            energyboom31444(var6, 1347904311);
            energyboom3155(var7, 264400220);
            energyboom1002(var0, var1, var6, 895528860);
        };
        var16.runTaskLater(var0, var49, 44L);
        BukkitScheduler var18 = Bukkit.getScheduler();
        Runnable var51 = () -> {
            Location var6 = front(var1, 2.0, 2012661791);
            Location var7 = front(var1, 1.3, 2012661791);
            energyboom31444(var6, 1347904311);
            energyboom3155(var7, 264400220);
            energyboom505(var0, var1, var6, 287803222);
            energyboom506(var0, var1, var6, 746568019);
            energyboom507(var0, var1, var6, 1545204755);
            energyboom508(var0, var1, var6, 925489385);
            energyboom509(var0, var1, var6, 2118137132);
            energyboom1002(var0, var1, var6, 895528860);
        };
        var18.runTaskLater(var0, var51, 60L);
        BukkitScheduler var20 = Bukkit.getScheduler();
        Runnable var53 = () -> {
            Sound var2x = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var2x, 50.0F, 1.6F, 1734520375);
        };
        var20.runTaskLater(var0, var53, 64L);
        BukkitScheduler var22 = Bukkit.getScheduler();
        Runnable var55 = () -> {
            Location var6 = front(var1, 2.0, 2012661791);
            Location var7 = front(var1, 1.3, 2012661791);
            energyboom31444(var6, 1347904311);
            energyboom3155(var7, 264400220);
            energyboom1002(var0, var1, var6, 895528860);
            energyboom505(var0, var1, var6, 287803222);
            energyboom506(var0, var1, var6, 746568019);
            energyboom507(var0, var1, var6, 1545204755);
        };
        var22.runTaskLater(var0, var55, 76L);
        BukkitScheduler var24 = Bukkit.getScheduler();
        Runnable var57 = () -> {
            Sound var2x = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var2x, 50.0F, 1.6F, 1734520375);
        };
        var24.runTaskLater(var0, var57, 84L);
        BukkitScheduler var26 = Bukkit.getScheduler();
        Runnable var59 = () -> {
            Location var6 = front(var1, 2.0, 2012661791);
            Location var7 = front(var1, 1.3, 2012661791);
            energyboom31444(var6, 1347904311);
            energyboom3155(var7, 264400220);
            energyboom1002(var0, var1, var6, 895528860);
            energyboom508(var0, var1, var6, 925489385);
            energyboom509(var0, var1, var6, 2118137132);
        };
        var26.runTaskLater(var0, var59, 92L);
    }

    private static void playTier11Overcharge(BlissPlugin var0, final Player var1, int var2) {
        BukkitRunnable var3 = new BukkitRunnable() {
            int i = 0;
            double s = 0.5;

            public void run() {
                if (this.i < 20 && var1.isOnline()) {
                    double var18 = this.s + 0.5;
                    this.s = var18;
                    Location var14 = FluxBeamScript.aimFeet(var1, 1322525274);
                    double var21 = this.s;
                    FluxBeamScript.drawFluxCircle(var14, var21, 1214255522);
                    int var24 = this.i + 1;
                    this.i = var24;
                } else {
                    this.cancel();
                }
            }
        };
        var3.runTaskTimer(var0, 0L, 1L);
        playOverchargeCone(var0, var1, 1197343222);
    }

    private static void playOverchargeCone(BlissPlugin var0, final Player var1, int var2) {
        final Location var10 = aim(var1, 1368675942);
        final Vector var11 = var10.getDirection().normalize();
        final Vector var12 = horizPerp(var11, 1755382018);
        Vector var21 = var11.clone();
        final Vector var13 = var21.crossProduct(var12).normalize();
        Sound var29 = Sound.ENTITY_WARDEN_SONIC_CHARGE;
        playOnce(var1, var29, 12.0F, 0.7F, 1734520375);
        Sound var30 = Sound.ENTITY_WARDEN_SONIC_BOOM;
        playOnce(var1, var30, 12.0F, 1.5F, 1734520375);
        Sound var31 = Sound.BLOCK_BEACON_POWER_SELECT;
        playOnce(var1, var31, 12.0F, 0.6F, 1734520375);
        BukkitRunnable var27 = new BukkitRunnable() {
            int step = 0;

            public void run() {
                if (this.step < 10 && var1.isOnline()) {
                    int var115 = this.step + 1;
                    this.step = var115;
                    double var18 = (double)this.step / 10.0;
                    double var20 = Math.tan(Math.toRadians(32.0 * var18));
                    double var22 = 45.0 * var18;
                    boolean var112;
                    if (this.step == 10) {
                        var112 = true;
                    } else {
                        var112 = false;
                    }

                    boolean var24 = var112;

                    for (double[] var28 : FluxBeamScript.CONE_EDGES) {
                        Vector var77 = var11.clone();
                        Vector var126 = var12.clone();
                        double var178 = var28[0];
                        double var153 = var20 * var178;
                        Vector var127 = var126.multiply(var153);
                        Vector var78 = var77.add(var127);
                        Vector var130 = var13.clone();
                        double var181 = var28[1];
                        double var157 = var20 * var181;
                        Vector var131 = var130.multiply(var157);
                        Vector var29 = var78.add(var131).normalize();

                        for (double var30 = 1.0; var30 <= var22; var30 += 0.6) {
                            Location var88 = var10.clone();
                            Vector var133 = var29.clone();
                            Vector var134 = var133.multiply(var30);
                            Location var33 = var88.add(var134);
                            World var91 = var33.getWorld();
                            Particle var135 = Particle.END_ROD;
                            Particles.spawn(var91, var135, var33, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
                            World var93 = var33.getWorld();
                            Particle var136 = Particle.DUST_COLOR_TRANSITION;
                            Color var15 = FluxBeamScript.CYAN;
                            Color var16 = FluxBeamScript.WHITE;
                            DustTransition var207 = new DustTransition(var15, var16, 1.6F);
                            Particles.spawn(var93, var136, var33, 2, 0.15, 0.15, 0.15, 0.0, var207, true, 1748031581);
                        }

                        if (var24) {
                            for (byte var211 = 1; var211 <= 4; var211 += 1) {
                                Location var103 = var10.clone();
                                Vector var139 = var29.clone();
                                double var188 = (double)var211;
                                double var171 = var22 * var188 / 4.0;
                                Vector var140 = var139.multiply(var171);
                                Location var32 = var103.add(var140);
                                World var106 = var32.getWorld();
                                Particle var141 = Particle.SONIC_BOOM;
                                Particles.spawn(var106, var141, var32, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
                                String var111 = "flux-overcharge|" + var1.getUniqueId();
                                World var143 = var32.getWorld();
                                SonicBoomSound.pulse(var111, var143, var32, 2077538397);
                            }
                        }
                    }

                    if (this.step % 3 == 1 || var24) {
                        Location var69 = var10;
                        byte var122;
                        if (var24) {
                            var122 = 3;
                        } else {
                            var122 = 1;
                        }

                        FluxBeamScript.flash(var69, var122, 1265218313);
                    }

                    if (var24) {
                        Player var72 = var1;
                        Sound var121 = Sound.ENTITY_WARDEN_SONIC_BOOM;
                        FluxBeamScript.playOnce(var72, var121, 12.0F, 0.8F, 1734520375);
                    }
                } else {
                    this.cancel();
                }
            }
        };
        var27.runTaskTimer(var0, 0L, 1L);
    }

    private static void flash(Location var0, int var1, int var2) {
        if (var0.getWorld() != null) {
            if (FLASH_NEEDS_COLOR) {
                World var20 = var0.getWorld();
                Particle var4 = Particle.FLASH;
                Color var15 = Color.WHITE;
                Particles.spawn(var20, var4, var0, var1, 0.4, 0.4, 0.4, 0.0, var15, true, 1748031581);
            } else {
                World var22 = var0.getWorld();
                Particle var23 = Particle.FLASH;
                Particles.spawn(var22, var23, var0, var1, 0.4, 0.4, 0.4, 0.0, null, true, 1748031581);
            }
        }
    }

    public static int ultraPulseCount(double var0, int var2) {
        double var30 = (var0 - 100.0) / 100.0;
        double var26 = Math.min(1.0, var30);
        double var9 = Math.max(0.0, var26);
        int var25 = (int)Math.round(4.0 * var9);
        return 3 + var25;
    }

    public static long ultraWindupTicks(int var0, int var1) {
        int var10 = var0 - 1;
        return (long)Math.max(0, var10) * 4L;
    }

    public static void ultraWindup(BlissPlugin var0, Player var1, int var2, int var3) {
        Sound var5 = Sound.ENTITY_WARDEN_SONIC_CHARGE;
        playOnce(var1, var5, 10.0F, 1.0F, 1734520375);
        int var9 = Math.max(1, var2);

        for (int var10 = 0; var10 < var9; var10++) {
            float var47;
            if (var9 == 1) {
                var47 = 0.0F;
            } else {
                float var39 = (float)var10;
                float var51 = (float)(var9 - 1);
                var47 = var39 / var51;
            }

            float var34 = 0.35F * var47;
            float var11 = 0.5F + var34;
            long var12 = (long)var10 * 4L;
            if (var12 == 0L) {
                Sound var35 = Sound.ITEM_AXE_SCRAPE;
                playOnce(var1, var35, 20.0F, var11, 1734520375);
            } else {
                BukkitScheduler var29 = Bukkit.getScheduler();
                Runnable var46 = () -> {
                    Sound var3x = Sound.ITEM_AXE_SCRAPE;
                    playOnce(var1, var3x, 20.0F, var11, 1734520375);
                };
                var29.runTaskLater(var0, var46, var12);
            }
        }
    }

    public static void ultraBeam(BlissPlugin var0, final Player var1, double var2, int var4) {
        final Location var26 = aim(var1, 1368675942);
        final Vector var27 = var26.getDirection().normalize();
        final Vector var28 = horizPerp(var27, 1755382018);
        Vector var51 = var27.clone();
        final Vector var29 = var51.crossProduct(var28).normalize();
        final World var30 = var26.getWorld();
        if (var30 != null) {
            double var108 = Math.min(1.0, var2);
            final double var31 = Math.max(0.05, var108);
            final double var33 = 40.0 * var31;
            int var97 = (int)Math.round(100.0 * var31);
            final int var35 = Math.max(20, var97);
            final double var36 = var33 / 2.0 * 0.7;
            final double[][] var38 = new double[27][2];
            byte var174 = 1;
            double[] var117 = new double[]{0.0, 0.0};
            var38[0] = var117;
            int[] var78 = new int[]{5, 9, 12};
            int[] var40 = var78;
            double[] var80 = new double[]{0.34, 0.67, 1.0};
            double[] var41 = var80;

            for (int var42 = 0; var42 < 3; var42++) {
                int var43 = 0;

                while (true) {
                    int var102 = var40[var42];
                    if (var43 >= var102) {
                        break;
                    }

                    double[] var86 = var38[var174];
                    double var126 = var41[var42];
                    var86[0] = var126;
                    double[] var88 = var38[var174];
                    double var132 = (double)(var43 * 2) * Math.PI;
                    double var159 = (double)var40[var42];
                    double var134 = var132 / var159;
                    double var164 = (double)var42 * 0.35;
                    double var136 = var134 + var164;
                    var88[1] = var136;
                    var174 += 1;
                    var43++;
                }
            }

            BukkitRunnable var89 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    int var37 = this.t;
                    int var174 = var35;
                    if (var37 >= var174) {
                        this.cancel();
                    } else {
                        double var222 = (double)(this.t + 1) / 10.0;
                        double var17 = Math.min(1.0, var222);
                        if (this.t % 3 == 0) {
                            double var19 = (double)(this.t / 3 % 3) * 2.0;

                            for (double[] var26x : var38) {
                                double var63 = var26x[0];
                                double var227 = var36;
                                double var27x = var63 * var227;
                                Vector var69 = var27.clone();
                                double var182 = var33;
                                Vector var70 = var69.multiply(var182);
                                Vector var186 = var28.clone();
                                double var232 = Math.cos(var26x[1]);
                                double var234 = var232 * var27x;
                                Vector var187 = var186.multiply(var234);
                                Vector var71 = var70.add(var187);
                                Vector var190 = var29.clone();
                                double var239 = Math.sin(var26x[1]);
                                double var241 = var239 * var27x;
                                Vector var191 = var190.multiply(var241);
                                Vector var31x = var71.add(var191);
                                double var74 = var31x.length();
                                double var32 = var74 * var17;
                                var31x.normalize();
                                String var84 = "flux-rays|" + var1.getUniqueId();
                                World var193 = var30;
                                Location var246 = var26;
                                SonicBoomSound.burst(var84, var193, var246, 1967648345);

                                for (double var34 = 2.0 + var19; var34 <= var32; var34 += 6.0) {
                                    World var93 = var30;
                                    Particle var194 = Particle.SONIC_BOOM;
                                    Location var253 = var26.clone();
                                    Vector var314 = var31x.clone();
                                    Vector var315 = var314.multiply(var34);
                                    Location var254 = var253.add(var315);
                                    Particles.spawn(var93, var194, var254, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
                                }
                            }
                        }

                        if (this.t % 5 == 0) {
                            double var389 = (double)this.t * 0.02;

                            for (byte var392 = 0; var392 < 4; var392 += 1) {
                                double var262 = (double)var392 * 90.0;
                                double var113 = Math.toRadians(45.0 + var262);
                                double var393 = var113 + var389;
                                double var397 = Math.cos(var393);
                                double var29x = Math.sin(var393);

                                for (double var400 = 0.04; var400 <= var17; var400 += 0.0125) {
                                    double var131 = var36;
                                    double var135 = var131 * var400 * 1.35;
                                    double var330 = var31;
                                    double var274 = 3.5 * var330;
                                    double var276 = var274 * var400;
                                    double var340 = Math.sin(var400 * Math.PI * 4.0);
                                    double var278 = var276 * var340;
                                    double var402 = var135 + var278;
                                    Location var141 = var26.clone();
                                    Vector var199 = var27.clone();
                                    double var281 = var33;
                                    double var283 = var281 * var400;
                                    Vector var200 = var199.multiply(var283);
                                    Location var142 = var141.add(var200);
                                    Vector var203 = var28.clone();
                                    double var287 = var397 * var402;
                                    Vector var204 = var203.multiply(var287);
                                    Location var143 = var142.add(var204);
                                    Vector var207 = var29.clone();
                                    double var291 = var29x * var402;
                                    Vector var208 = var207.multiply(var291);
                                    Location var36x = var143.add(var208);
                                    World var146 = var30;
                                    Particle var209 = Particle.END_ROD;
                                    Particles.spawn(var146, var209, var36x, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
                                }
                            }
                        }

                        for (byte var391 = 1; var391 <= 16; var391 += 1) {
                            double var156 = (double)var391 / 16.0;
                            double var21 = var156 * var17;
                            double var161 = var36;
                            double var395 = var161 * var21 * 0.9;
                            Location var169 = var26.clone();
                            Vector var213 = var27.clone();
                            double var305 = var33;
                            double var307 = var305 * var21;
                            Vector var214 = var213.multiply(var307);
                            Location var399 = var169.add(var214);
                            World var172 = var30;
                            Particle var215 = Particle.DUST;
                            double var354 = var395 * 0.7;
                            double var366 = var395 * 0.7;
                            double var374 = var395 * 0.7;
                            Color var387;
                            if (var391 % 2 == 0) {
                                var387 = FluxBeamScript.CYAN;
                            } else {
                                var387 = FluxBeamScript.PALE;
                            }

                            DustOptions var383 = new DustOptions(var387, 3.0F);
                            Particles.spawn(var172, var215, var399, 6, var354, var366, var374, 0.0, var383, true, 1748031581);
                        }

                        int var217 = this.t + 1;
                        this.t = var217;
                    }
                }
            };
            var89.runTaskTimer(var0, 0L, 1L);
        }
    }

    public static void ultraBurst(BlissPlugin var0, final Player var1, double var2, int var4) {
        Location var18 = aim(var1, 1368675942);
        Vector var19 = var18.getDirection().normalize();
        double var94 = Math.min(1.0, var2);
        double var20 = Math.max(0.05, var94);
        double var98 = (double)beamRangeFor(12, 1722681478);
        double var100 = var98 * var20;
        double var22 = Math.max(8.0, var100);
        int var73 = (int)Math.round(70.0 * var20);
        final int var24 = Math.max(20, var73);
        double var104 = 14.0 * var20;
        final double var25 = Math.max(3.5, var104);
        int var80 = (int)Math.round(25.0 * var20);
        final int var27 = Math.max(5, var80);
        World var52 = var18.getWorld();
        FluidCollisionMode var14 = FluidCollisionMode.NEVER;
        RayTraceResult var28 = var52.rayTraceBlocks(var18, var19, var22, var14, true);
        Location var68;
        if (var28 != null) {
            Vector var56 = var28.getHitPosition();
            World var83 = var18.getWorld();
            var68 = var56.toLocation(var83);
        } else {
            Location var67 = var18.clone();
            Vector var92 = var19.clone();
            Vector var93 = var92.multiply(var22);
            var68 = var67.add(var93);
        }

        Vector var86 = var19.clone().multiply(2.0);
        final Location var29 = var68.subtract(var86);
        final World var30 = var29.getWorld();
        if (var30 != null) {
            Sound var109 = Sound.ENTITY_GENERIC_EXPLODE;
            var30.playSound(var29, var109, 20.0F, 0.6F);
            Sound var110 = Sound.ENTITY_WARDEN_SONIC_BOOM;
            var30.playSound(var29, var110, 20.0F, 0.7F);
            flash(var29, 2, 1265218313);
            BukkitRunnable var64 = new BukkitRunnable() {
                int t = 0;
                double spin;
                final double spiralPhase;

                {
                    double var31 = Math.random() * 360.0;
                    this.spin = var31;
                    double var35 = Math.random() * 360.0;
                    this.spiralPhase = var35;
                }

                public void run() {
                    int var36 = this.t;
                    int var160 = var24;
                    if (var36 >= var160) {
                        this.cancel();
                    } else {
                        double var163 = this.spin + 9.0;
                        this.spin = var163;
                        double var195 = var25 - 3.0;
                        double var376 = (double)this.t;
                        double var420 = (double)var27;
                        double var378 = var376 / var420;
                        double var310 = Math.min(1.0, var378);
                        double var197 = var195 * var310;
                        double var21 = 3.0 + var197;

                        for (int var23 = 0; var23 < 4; var23++) {
                            double var381 = (double)var23;
                            double var314 = 0.22 * var381;
                            double var201 = 0.35 + var314;
                            double var24x = var21 * var201;
                            double var50 = this.spin;
                            byte var242;
                            if (var23 % 2 == 0) {
                                var242 = 1;
                            } else {
                                var242 = -1;
                            }

                            double var205 = (double)var242;
                            double var52 = var50 * var205;
                            double var210 = (double)var23 * 45.0;
                            double var26 = Math.toRadians(var52 + var210);
                            int var59 = this.t;
                            int var167 = var23 * 7;
                            double var65 = Math.sin((double)(var59 + var167) * 0.25);
                            double var28 = var65 * var21 * 0.25;

                            Color var30x = switch (var23 % 3) {
                                case 0 -> FluxBeamScript.DEEP;
                                case 1 -> FluxBeamScript.CYAN;
                                default -> FluxBeamScript.PALE;
                            };

                            for (int var31 = 0; var31 < 14; var31++) {
                                double var224 = Math.toRadians((double)var31 * 25.714285714285715);
                                double var32 = var26 + var224;
                                double var228 = var32 * 2.0;
                                double var326 = (double)this.t * 0.2;
                                double var232 = Math.cos(var228 + var326);
                                double var236 = var232 * var24x * 0.45;
                                double var34 = var28 + var236;
                                World var84 = var30;
                                Particle var170 = Particle.DUST;
                                Location var240 = var29.clone();
                                double var286 = Math.cos(var32);
                                double var288 = var286 * var24x;
                                double var399 = Math.sin(var32);
                                double var401 = var399 * var24x;
                                Location var241 = var240.add(var288, var34, var401);
                                DustOptions var18 = new DustOptions(var30x, 4.0F);
                                Particles.spawn(var84, var170, var241, 4, 0.9, 0.9, 0.9, 0.0, var18, true, 1748031581);
                            }
                        }

                        for (byte var463 = 0; var463 < 3; var463 += 1) {
                            double var115 = this.spiralPhase;
                            double var258 = (double)var463 * 120.0;
                            double var117 = var115 + var258;
                            double var263 = this.spin * 2.0;
                            double var466 = Math.toRadians(var117 + var263);

                            for (double var468 = 1.0; var468 <= var21; var468 += 0.7) {
                                double var269 = var468 * 0.55;
                                double var470 = var466 + var269;
                                double var136 = Math.sin(var470 * 1.7);
                                double var472 = var136 * var468 * 0.35;
                                Location var144 = var29.clone();
                                double var186 = Math.cos(var470);
                                double var188 = var186 * var468;
                                double var371 = Math.sin(var470);
                                double var373 = var371 * var468;
                                Location var474 = var144.add(var188, var472, var373);
                                World var147 = var30;
                                Particle var190 = Particle.END_ROD;
                                Particles.spawn(var147, var190, var474, 1, 0.05, 0.05, 0.05, 0.0, null, true, 1748031581);
                                if ((int)(var468 * 10.0) % 3 == 0) {
                                    World var155 = var30;
                                    Particle var192 = Particle.DUST;
                                    Color var460 = FluxBeamScript.WHITE;
                                    DustOptions var453 = new DustOptions(var460, 3.0F);
                                    Particles.spawn(var155, var192, var474, 1, 0.1, 0.1, 0.1, 0.0, var453, true, 1748031581);
                                }
                            }
                        }

                        World var89 = var30;
                        Particle var172 = Particle.DUST;
                        Location var244 = var29;
                        double var336 = var21 * 0.7;
                        double var391 = var21 * 0.55;
                        double var428 = var21 * 0.7;
                        Color var459 = FluxBeamScript.CYAN;
                        DustOptions var450 = new DustOptions(var459, 2.5F);
                        Particles.spawn(var89, var172, var244, 25, var336, var391, var428, 0.0, var450, true, 1748031581);
                        if (this.t % 5 == 0) {
                            double var464 = Math.toRadians(this.spin * 3.0);
                            World var101 = var30;
                            Particle var174 = Particle.SONIC_BOOM;
                            Location var249 = var29.clone();
                            double var294 = Math.cos(var464);
                            double var298 = var294 * var21 * 0.6;
                            double var363 = Math.sin((double)this.t * 0.4);
                            double var367 = var363 * var21 * 0.3;
                            double var411 = Math.sin(var464);
                            double var415 = var411 * var21 * 0.6;
                            Location var250 = var249.add(var298, var367, var415);
                            Particles.spawn(var101, var174, var250, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
                            String var106 = "flux-domain|" + var1.getUniqueId();
                            World var176 = var30;
                            Location var252 = var29;
                            SonicBoomSound.pulse(var106, var176, var252, 2077538397);
                        }

                        int var108 = this.t;
                        int var178 = var27;
                        if (var108 == var178) {
                            FluxBeamScript.flash(var29, 3, 1265218313);
                            World var112 = var30;
                            Location var181 = var29;
                            Sound var253 = Sound.ENTITY_WARDEN_SONIC_BOOM;
                            var112.playSound(var181, var253, 20.0F, 1.2F);
                        }

                        int var183 = this.t + 1;
                        this.t = var183;
                    }
                }
            };
            var64.runTaskTimer(var0, 0L, 1L);
        }
    }

    private static void playTier12Ultra(BlissPlugin var0, Player var1, int var2) {
        ultraWindup(var0, var1, 7, 1032809015);
        BukkitScheduler var12 = Bukkit.getScheduler();
        Runnable var51 = () -> {
            Sound var2x = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
            playOnce(var1, var2x, 20.0F, 1.2F, 1734520375);
            energyboom312(pl2(var1, 948902320), 840634181);
        };
        var12.runTaskLater(var0, var51, 36L);
        BukkitScheduler var14 = Bukkit.getScheduler();
        Runnable var53 = () -> ultraBeam(var0, var1, 1.0, 1396458786);
        var14.runTaskLater(var0, var53, 36L);
        BukkitScheduler var16 = Bukkit.getScheduler();
        Runnable var55 = () -> ultraBurst(var0, var1, 1.0, 453258327);
        var16.runTaskLater(var0, var55, 38L);
        BukkitScheduler var20 = Bukkit.getScheduler();
        Runnable var57 = () -> {
            energyboom312(pl2(var1, 948902320), 840634181);
            Location var5 = pl2(var1, 948902320);
            waveyBeam(var1, var5, 1463487883);
        };
        var20.runTaskLater(var0, var57, 46L);
        BukkitScheduler var22 = Bukkit.getScheduler();
        Runnable var59 = () -> {
            Sound var2x = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var2x, 50.0F, 1.6F, 1734520375);
            Sound var7 = Sound.ENTITY_WARDEN_SONIC_BOOM;
            playOnce(var1, var7, 20.0F, 1.0F, 1734520375);
            Sound var8 = Sound.ENTITY_GENERIC_EXPLODE;
            playOnce(var1, var8, 20.0F, 0.75F, 1734520375);
        };
        var22.runTaskLater(var0, var59, 47L);
        BukkitScheduler var24 = Bukkit.getScheduler();
        Runnable var61 = () -> {
            energyboom312(pl2(var1, 948902320), 840634181);
            Location var5 = pl2(var1, 948902320);
            waveyBeam(var1, var5, 1463487883);
        };
        var24.runTaskLater(var0, var61, 58L);
        BukkitScheduler var26 = Bukkit.getScheduler();
        Runnable var63 = () -> {
            Sound var2x = Sound.BLOCK_BEACON_POWER_SELECT;
            playOnce(var1, var2x, 20.0F, 1.6F, 1734520375);
            Sound var7 = Sound.BLOCK_BEACON_DEACTIVATE;
            playOnce(var1, var7, 20.0F, 2.0F, 1734520375);
            Sound var8 = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var8, 50.0F, 1.6F, 1734520375);
        };
        var26.runTaskLater(var0, var63, 62L);
        BukkitScheduler var28 = Bukkit.getScheduler();
        Runnable var65 = () -> {
            energyboom312(pl2(var1, 948902320), 840634181);
            Location var5 = pl2(var1, 948902320);
            waveyBeam(var1, var5, 1463487883);
        };
        var28.runTaskLater(var0, var65, 70L);
        BukkitScheduler var30 = Bukkit.getScheduler();
        Runnable var67 = () -> {
            energyboom312(pl2(var1, 948902320), 840634181);
            Location var5 = pl2(var1, 948902320);
            waveyBeam(var1, var5, 1463487883);
        };
        var30.runTaskLater(var0, var67, 82L);
        BukkitScheduler var32 = Bukkit.getScheduler();
        Runnable var69 = () -> {
            Sound var2x = Sound.BLOCK_BEACON_POWER_SELECT;
            playOnce(var1, var2x, 20.0F, 1.6F, 1734520375);
            Sound var7 = Sound.BLOCK_BEACON_DEACTIVATE;
            playOnce(var1, var7, 20.0F, 2.0F, 1734520375);
            Sound var8 = Sound.BLOCK_BEACON_AMBIENT;
            playOnce(var1, var8, 50.0F, 1.6F, 1734520375);
        };
        var32.runTaskLater(var0, var69, 86L);
        BukkitScheduler var34 = Bukkit.getScheduler();
        Runnable var71 = () -> {
            energyboom312(pl2(var1, 948902320), 840634181);
            Location var5 = pl2(var1, 948902320);
            waveyBeam(var1, var5, 1463487883);
        };
        var34.runTaskLater(var0, var71, 94L);
        BukkitScheduler var36 = Bukkit.getScheduler();
        Runnable var73 = () -> {
            energyboom312(pl2(var1, 948902320), 840634181);
            Location var5 = pl2(var1, 948902320);
            waveyBeam(var1, var5, 1463487883);
        };
        var36.runTaskLater(var0, var73, 106L);
    }

    static void energyboom651(Location var0, int var1) {
        double var3 = randomSeed(1911612574);
        FluxBeamScript.Phase[] var24 = new FluxBeamScript.Phase[1];
        Particle var20 = Particle.END_ROD;
        FluxBeamScript.Phase var7 = new FluxBeamScript.Phase(12, 8, 45.0, 0.3, 0.22, 0.4, 5.0, var20, 1, 0.1);
        var24[0] = var7;
        runSpiral(var0, var3, var24, 687067837);
    }

    static void energyboom6544844(Location var0, int var1) {
        FluxBeamScript.Phase[] var24 = new FluxBeamScript.Phase[3];
        Particle var20 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var7 = new FluxBeamScript.Phase(2, 24, 15.0, 0.5, 0.2, 0.8, 5.0, var20, 2, 0.3);
        var24[0] = var7;
        Particle var53 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var27 = new FluxBeamScript.Phase(3, 24, 15.0, 0.7, 0.4, 0.8, 5.0, var53, 2, 0.3);
        var24[1] = var27;
        Particle var54 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var28 = new FluxBeamScript.Phase(6, 24, 15.0, 1.9, 0.15, 0.8, 5.0, var54, 2, 0.3);
        var24[2] = var28;
        runSpiral(var0, 20.0, var24, 687067837);
    }

    static void energyboom654444(Location var0) {
        FluxBeamScript.Phase[] var23 = new FluxBeamScript.Phase[3];
        Particle var19 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var6 = new FluxBeamScript.Phase(2, 24, 15.0, 0.3, 0.1, 0.8, 5.0, var19, 2, 0.3);
        var23[0] = var6;
        Particle var52 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var26 = new FluxBeamScript.Phase(3, 24, 15.0, 0.5, 0.4, 0.8, 5.0, var52, 2, 0.3);
        var23[1] = var26;
        Particle var53 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var27 = new FluxBeamScript.Phase(6, 24, 15.0, 1.7, 0.15, 0.8, 5.0, var53, 2, 0.3);
        var23[2] = var27;
        runSpiral(var0, 20.0, var23, 687067837);
    }

    static void energyboom6544(Location var0) {
        FluxBeamScript.Phase[] var23 = new FluxBeamScript.Phase[3];
        Particle var19 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var6 = new FluxBeamScript.Phase(2, 24, 15.0, 0.3, 0.1, 0.8, 5.0, var19, 5, 0.3);
        var23[0] = var6;
        Particle var52 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var26 = new FluxBeamScript.Phase(5, 24, 15.0, 0.5, 0.5, 0.8, 5.0, var52, 5, 0.3);
        var23[1] = var26;
        Particle var53 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var27 = new FluxBeamScript.Phase(7, 24, 15.0, 3.0, 0.15, 0.8, 5.0, var53, 5, 0.3);
        var23[2] = var27;
        runSpiral(var0, 20.0, var23, 687067837);
    }

    static void energyboom65444(Location var0) {
        double var2 = randomSeed(1911612574);
        FluxBeamScript.Phase[] var23 = new FluxBeamScript.Phase[1];
        Particle var19 = Particle.END_ROD;
        FluxBeamScript.Phase var6 = new FluxBeamScript.Phase(20, 4, 90.0, 0.3, 0.07, 0.4, 5.0, var19, 1, 0.1);
        var23[0] = var6;
        runSpiral(var0, var2, var23, 687067837);
    }

    static void energyboom659(Location var0) {
        double var2 = randomSeed(1911612574);
        FluxBeamScript.Phase[] var23 = new FluxBeamScript.Phase[1];
        Particle var19 = Particle.END_ROD;
        FluxBeamScript.Phase var6 = new FluxBeamScript.Phase(20, 8, 45.0, 0.3, 0.07, 0.4, 5.0, var19, 1, 0.1);
        var23[0] = var6;
        runSpiral(var0, var2, var23, 687067837);
    }

    static void energyboom654(Location var0) {
        double var2 = randomSeed(1911612574);
        FluxBeamScript.Phase[] var23 = new FluxBeamScript.Phase[1];
        Particle var19 = Particle.END_ROD;
        FluxBeamScript.Phase var6 = new FluxBeamScript.Phase(25, 12, 30.0, 0.3, 0.07, 0.4, 5.0, var19, 1, 0.0);
        var23[0] = var6;
        runSpiral(var0, var2, var23, 687067837);
    }

    static void energyboom607(Location var0) {
        FluxBeamScript.Phase[] var23 = new FluxBeamScript.Phase[1];
        Particle var19 = Particle.END_ROD;
        FluxBeamScript.Phase var6 = new FluxBeamScript.Phase(80, 6, 60.0, 0.3, 0.03, 0.2, 1.0, var19, 1, 0.1);
        var23[0] = var6;
        runSpiral(var0, 20.0, var23, 687067837);
    }

    static void energyboom6077(Location var0) {
        Particle var15 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var1 = new FluxBeamScript.Phase(20, 60, 6.0, 0.3, 0.04, 0.2, 1.0, var15, 1, 0.15);
        FluxBeamScript.Phase[] var24 = new FluxBeamScript.Phase[]{var1, var1, var1, var1};
        runSpiral(var0, 20.0, var24, 687067837);
    }

    static void energyboom609999(Location var0) {
        FluxBeamScript.Phase[] var23 = new FluxBeamScript.Phase[4];
        Particle var19 = Particle.END_ROD;
        FluxBeamScript.Phase var6 = new FluxBeamScript.Phase(6, 8, 45.0, 0.3, 0.08, 0.8, 5.0, var19, 1, 0.0);
        var23[0] = var6;
        Particle var66 = Particle.END_ROD;
        FluxBeamScript.Phase var27 = new FluxBeamScript.Phase(6, 8, 45.0, 0.78, -0.05, 0.8, 5.0, var66, 1, 0.0);
        var23[1] = var27;
        Particle var67 = Particle.END_ROD;
        FluxBeamScript.Phase var28 = new FluxBeamScript.Phase(6, 8, 45.0, 0.48, 0.08, 0.8, 5.0, var67, 1, 0.0);
        var23[2] = var28;
        Particle var68 = Particle.END_ROD;
        FluxBeamScript.Phase var29 = new FluxBeamScript.Phase(6, 8, 45.0, 0.96, -0.05, 0.8, 5.0, var68, 1, 0.0);
        var23[3] = var29;
        runSpiral(var0, 20.0, var23, 687067837);
    }

    static void energyboom60999(Location var0) {
        FluxBeamScript.Phase[] var23 = new FluxBeamScript.Phase[3];
        Particle var19 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var6 = new FluxBeamScript.Phase(6, 8, 45.0, 0.3, 0.1, 0.8, 5.0, var19, 5, 0.3);
        var23[0] = var6;
        Particle var52 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var26 = new FluxBeamScript.Phase(6, 8, 45.0, 0.9, -0.03, 0.8, 5.0, var52, 5, 0.3);
        var23[1] = var26;
        Particle var53 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var27 = new FluxBeamScript.Phase(6, 8, 45.0, 0.72, 0.1, 0.8, 5.0, var53, 5, 0.3);
        var23[2] = var27;
        runSpiral(var0, 20.0, var23, 687067837);
    }

    static void energyboom609(Location var0, int var1) {
        FluxBeamScript.Phase[] var24 = new FluxBeamScript.Phase[1];
        Particle var20 = Particle.END_ROD;
        FluxBeamScript.Phase var7 = new FluxBeamScript.Phase(60, 8, 45.0, 0.3, 0.03, 0.5, 5.0, var20, 1, 0.1);
        var24[0] = var7;
        runSpiral(var0, 20.0, var24, 687067837);
    }

    static void energyboom6099(Location var0, int var1) {
        FluxBeamScript.Phase[] var24 = new FluxBeamScript.Phase[1];
        Particle var20 = Particle.DUST_COLOR_TRANSITION;
        FluxBeamScript.Phase var7 = new FluxBeamScript.Phase(49, 1, 60.0, 0.3, 0.01, 0.8, 5.0, var20, 40, 0.15);
        var24[0] = var7;
        runSpiral(var0, 20.0, var24, 687067837);
    }

    static void energyboom314(Location var0, int var1) {
        Vector var6 = var0.getDirection();

        for (int var7 = 0; var7 < 70; var7++) {
            Location var13 = var0.clone();
            Vector var17 = var6.clone();
            int var20 = var7 + 1;
            Vector var18 = var17.multiply(var20);
            spawnDust(var13.add(var18), 40, 0.5, 1253182854);
        }
    }

    static void energyboom315(Location var0, int var1) {
        Vector var14 = var0.getDirection();

        for (int var15 = 0; var15 < 230; var15++) {
            Location var21 = var0.clone();
            Vector var26 = var14.clone();
            double var34 = (double)var15 * 0.3;
            double var29 = 1.0 + var34;
            Vector var27 = var26.multiply(var29);
            Location var16 = var21.add(var27);
            World var24 = var16.getWorld();
            Particle var28 = Particle.END_ROD;
            Particles.spawn(var24, var28, var16, 3, 0.0, 0.0, 0.0, 0.03, 1986132876);
        }
    }

    static void energyboom3144(Location var0, int var1) {
        Vector var6 = var0.getDirection();

        for (int var7 = 0; var7 < 80; var7++) {
            Location var13 = var0.clone();
            Vector var17 = var6.clone();
            int var20 = var7 + 1;
            Vector var18 = var17.multiply(var20);
            spawnDust(var13.add(var18), 40, 0.6, 1253182854);
        }
    }

    static void energyboom3155(Location var0, int var1) {
        Vector var14 = var0.getDirection();

        for (int var15 = 0; var15 < 250; var15++) {
            Location var21 = var0.clone();
            Vector var26 = var14.clone();
            double var34 = (double)var15 * 0.3;
            double var29 = 1.0 + var34;
            Vector var27 = var26.multiply(var29);
            Location var16 = var21.add(var27);
            World var24 = var16.getWorld();
            Particle var28 = Particle.END_ROD;
            Particles.spawn(var24, var28, var16, 3, 0.0, 0.0, 0.0, 0.03, 1986132876);
        }
    }

    static void energyboom31444(Location var0, int var1) {
        Vector var7 = var0.getDirection();

        for (int var8 = 0; var8 < 80; var8++) {
            Location var14 = var0.clone();
            Vector var18 = var7.clone();
            int var21 = var8 + 1;
            Vector var19 = var18.multiply(var21);
            spawnDust(var14.add(var19), 40, 0.6, 3.5F, 226296995);
        }
    }

    static void energyboom316(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 40, 1.5, 220.0, 2, 120.0, 4, 2, false, 501431761);
    }

    static void energyboom317(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 40, 1.5, 220.0, 2, 120.0, 4, 2, true, 501431761);
    }

    static void energyboom318(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 35, 1.7, 200.0, 2, 100.0, 3, 2, false, 501431761);
    }

    static void energyboom500(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 20, 1.7, 220.0, 2, 60.0, 3, 1, true, 501431761);
    }

    static void energyboom501(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 20, 2.0, 200.0, 2, 90.0, 3, 1, false, 501431761);
    }

    static void energyboom502(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 20, 2.2, 180.0, 2, 60.0, 3, 1, true, 501431761);
    }

    static void energyboom503(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 22, 2.4, 200.0, 2, 70.0, 3, 1, false, 501431761);
    }

    static void energyboom504(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 22, 2.6, 220.0, 2, 60.0, 3, 1, true, 501431761);
    }

    static void energyboom505(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 24, 2.8, 200.0, 2, 100.0, 3, 1, false, 501431761);
    }

    static void energyboom506(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 24, 3.0, 220.0, 2, 60.0, 3, 1, true, 501431761);
    }

    static void energyboom507(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 26, 3.2, 200.0, 2, 80.0, 3, 1, false, 501431761);
    }

    static void energyboom508(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 26, 3.4, 220.0, 2, 60.0, 3, 1, true, 501431761);
    }

    static void energyboom509(BlissPlugin var0, Player var1, Location var2, int var3) {
        scheduleOuterRingOverlay(var0, var1, var2, 28, 3.6, 200.0, 2, 90.0, 3, 1, false, 501431761);
    }

    static void energyboom1002(BlissPlugin var0, final Player var1, final Location var2, int var3) {
        BukkitRunnable var4 = new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (this.tick < 35 && var1.isOnline()) {
                    Vector var13 = var2.getDirection();
                    double var58 = (double)(this.tick * 4) * 0.5;
                    double var14 = 1.0 + var58;
                    String var34 = "flux-advance|" + var1.getUniqueId();
                    World var46 = var2.getWorld();
                    Location var62 = var2.clone();
                    Vector var71 = var13.clone();
                    Vector var72 = var71.multiply(var14);
                    Location var63 = var62.add(var72);
                    SonicBoomSound.pulse(var34, var46, var63, 2077538397);

                    for (int var16 = 0; var16 < 4; var16++) {
                        Location var39 = var2.clone();
                        Vector var49 = var13.clone();
                        double var79 = (double)var16 * 0.5;
                        double var66 = var14 + var79;
                        Vector var50 = var49.multiply(var66);
                        Location var17 = var39.add(var50);
                        World var42 = var17.getWorld();
                        Particle var51 = Particle.SONIC_BOOM;
                        Particles.spawn(var42, var51, var17, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                    }

                    int var53 = this.tick + 1;
                    this.tick = var53;
                } else {
                    this.cancel();
                }
            }
        };
        var4.runTaskTimer(var0, 0L, 1L);
    }

    static void energyboom312(Location var0, int var1) {
        Vector var13 = var0.getDirection();
        String var17 = "flux-sweep";
        World var28 = var0.getWorld();
        SonicBoomSound.burst(var17, var28, var0, 1967648345);

        for (int var14 = 0; var14 < 200; var14++) {
            Location var21 = var0.clone();
            Vector var31 = var13.clone();
            double var45 = (double)var14 * 0.5;
            double var38 = 1.0 + var45;
            Vector var32 = var31.multiply(var38);
            Location var15 = var21.add(var32);
            spawnDust(var15, 30, 0.5, 3.0F, 226296995);
            if (var14 % 5 == 0) {
                World var27 = var15.getWorld();
                Particle var35 = Particle.SONIC_BOOM;
                Particles.spawn(var27, var35, var15, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
            }
        }
    }

    static void waveyBeam(Player var0, Location var1, int var2) {
        Vector var15 = var1.getDirection();
        Vector var16 = horizPerp(var15, 1755382018);
        Vector var26 = var15.clone();
        var26.crossProduct(var16).normalize();
        String var32 = "flux-wave|" + var0.getUniqueId();
        World var52 = var1.getWorld();
        SonicBoomSound.burst(var32, var52, var1, 1967648345);

        for (int var18 = 0; var18 < 60; var18++) {
            double var19 = Math.sin((double)var18 * 0.5) * 0.4;
            Location var45 = var1.clone();
            Vector var55 = var15.clone();
            int var67 = var18 + 1;
            Vector var56 = var55.multiply(var67);
            Location var46 = var45.add(var56);
            Vector var58 = var16.clone();
            Vector var59 = var58.multiply(var19);
            Location var21 = var46.add(var59);
            World var49 = var21.getWorld();
            Particle var60 = Particle.SONIC_BOOM;
            Particles.spawn(var49, var60, var21, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
            spawnDust(var21, 8, 0.3, 2.5F, 226296995);
        }
    }

    private static void scheduleOuterRingOverlay(
        BlissPlugin var0,
        final Player var1,
        final Location var2,
        final int var3,
        final double var4,
        final double var6,
        final int var8,
        final double var9,
        final int var11,
        final int var12,
        final boolean var13,
        int var14
    ) {
        BukkitRunnable var15 = new BukkitRunnable() {
            int tick = 0;
            double n = 1.0;

            public void run() {
                int var23 = this.tick;
                int var67 = var3;
                if (var23 < var67 && var1.isOnline()) {
                    Vector var14 = var2.getDirection();
                    Vector var15 = FluxBeamScript.horizPerp(var14, 1755382018);
                    Vector var33 = var14.clone();
                    Vector var16 = var33.crossProduct(var15).normalize();
                    int var17 = 0;

                    while (true) {
                        int var70 = var11;
                        if (var17 >= var70) {
                            int var72 = this.tick + 1;
                            this.tick = var72;
                            return;
                        }

                        int var18 = 0;

                        while (true) {
                            int var74 = var8;
                            if (var18 >= var74) {
                                double var91 = this.n;
                                double var120 = (double)var12 * 0.1;
                                double var93 = var91 + var120;
                                this.n = var93;
                                var17++;
                                break;
                            }

                            double var43 = (double)(var18 - 2);
                            double var96 = var6;
                            double var45 = var43 * var96;
                            double var99 = var9;
                            double var19 = Math.toRadians(var45 + var99);
                            Vector var52 = var15.clone();
                            double var78 = Math.cos(var19);
                            double var113 = var4;
                            double var80 = var78 * var113;
                            Vector var53 = var52.multiply(var80);
                            Vector var83 = var16.clone();
                            double var103 = Math.sin(var19);
                            double var122 = var4;
                            double var105 = var103 * var122;
                            Vector var84 = var83.multiply(var105);
                            Vector var21 = var53.add(var84);
                            Location var57 = var2.clone();
                            Vector var86 = var14.clone();
                            double var108 = this.n;
                            Vector var87 = var86.multiply(var108);
                            Location var58 = var57.add(var87);
                            Location var22 = var58.add(var21);
                            if (var13) {
                                FluxBeamScript.spawnDust(var22, 1, 0.0, 1253182854);
                            } else {
                                World var63 = var22.getWorld();
                                Particle var89 = Particle.END_ROD;
                                Particles.spawn(var63, var89, var22, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                            }

                            var18++;
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        };
        var15.runTaskTimer(var0, 0L, 1L);
    }

    private static double randomSeed(int var0) {
        double var9 = Math.random() * 59.0;
        return 1.0 + var9;
    }

    private static void playOnce(Player var0, Sound var1, float var2, float var3, int var4) {
        World var10 = var0.getWorld();
        Location var11 = var0.getLocation();
        var10.playSound(var11, var1, var2, var3);
    }

    private static void spawnDust(Location var0, int var1, double var2, int var4) {
        spawnDust(var0, var1, var2, 2.5F, 226296995);
    }

    private static void spawnDust(Location var0, int var1, double var2, float var4, int var5) {
        Color var7 = CYAN;
        Color var8 = WHITE;
        DustTransition var6 = new DustTransition(var7, var8, var4);
        World var19 = var0.getWorld();
        Particle var20 = Particle.DUST_COLOR_TRANSITION;
        Particles.spawn(var19, var20, var0, var1, var2, var2, var2, 0.0, var6, 1141219473);
    }

    private static void runSpiral(Location var0, double var1, FluxBeamScript.Phase[] var3, int var4) {
        Vector var17 = var0.getDirection().normalize();
        Vector var18 = horizPerp(var17, 1755382018);
        Vector var42 = var17.clone();
        Vector var19 = var42.crossProduct(var18).normalize();
        double var20 = var1;
        double var22 = 1.0;

        for (FluxBeamScript.Phase var27 : var3) {
            double var28 = var27.radiusStart;
            int var30 = 0;

            while (true) {
                int var103 = var27.rings;
                if (var30 >= var103) {
                    break;
                }

                Location var62 = var0.clone();
                Vector var105 = var17.clone();
                Vector var106 = var105.multiply(var22);
                Location var31 = var62.add(var106);
                double var126 = var27.seedStep;
                var20 += var126;
                int var32 = 0;

                while (true) {
                    int var108 = var27.slices;
                    if (var32 >= var108) {
                        double var144 = var27.forwardStep;
                        var22 += var144;
                        double var147 = var27.radiusDelta;
                        var28 += var147;
                        var30++;
                        break;
                    }

                    double var72 = (double)(var32 - 1);
                    double var129 = var27.stepDeg;
                    double var74 = var72 * var129;
                    double var33 = Math.toRadians(var74 + var20);
                    Vector var81 = var18.clone();
                    double var112 = Math.cos(var33);
                    double var114 = var112 * var28;
                    Vector var82 = var81.multiply(var114);
                    Vector var117 = var19.clone();
                    double var135 = Math.sin(var33);
                    double var137 = var135 * var28;
                    Vector var118 = var117.multiply(var137);
                    Vector var35 = var82.add(var118);
                    Location var85 = var31.clone();
                    Location var36 = var85.add(var35);
                    Particle var88 = var27.particle;
                    Particle var120 = Particle.DUST_COLOR_TRANSITION;
                    if (var88 == var120) {
                        int var122 = var27.particleCount;
                        double var140 = var27.offset;
                        spawnDust(var36, var122, var140, 1.5F, 226296995);
                    } else {
                        World var91 = var36.getWorld();
                        Particle var124 = var27.particle;
                        int var150 = var27.particleCount;
                        double var153 = var27.offset;
                        double var155 = var27.offset;
                        double var157 = var27.offset;
                        Particles.spawn(var91, var124, var36, var150, var153, var155, var157, 0.0, 1986132876);
                    }

                    var32++;
                }
            }
        }
    }

    private static Vector horizPerp(Vector var0, int var1) {
        double var18 = -var0.getZ();
        double var24 = var0.getX();
        Vector var2 = new Vector(var18, 0.0, var24);
        Vector var8 = var2;
        if (var2.lengthSquared() < 1.0E-6) {
            Vector var13 = new Vector(1, 0, 0);
            var8 = var13;
        }

        return var8.normalize();
    }

    private static void drawFluxCircle(Location var0, double var1, int var3) {
        int var72 = (int)Math.floor((Math.PI * 2) * var1 / 0.1);
        int var27 = Math.max(1, var72);
        Color var77 = CYAN;
        DustOptions var43 = new DustOptions(var77, 1.0F);
        DustOptions var28 = var43;

        for (int var29 = 0; var29 < var27; var29++) {
            double var49 = (double)var29 * 360.0;
            double var81 = (double)var27;
            double var30 = Math.toRadians(var49 / var81);
            double var85 = Math.cos(var30);
            double var32 = var1 * var85;
            double var89 = Math.sin(var30);
            double var34 = var1 * var89;
            Location var64 = var0.clone();
            Location var36 = var64.add(var32, 0.0, var34);
            World var67 = var0.getWorld();
            Particle var76 = Particle.DUST;
            Particles.spawn(var67, var76, var36, 1, 0.0, 0.0, 0.0, 0.0, var28, 1141219473);
        }
    }

    static {
        ConcurrentHashMap var14 = new ConcurrentHashMap();
        FROZEN_AIM = var14;
        double[][] var16 = new double[4][];
        double[] var31 = new double[]{1.0, 1.0};
        var16[0] = var31;
        double[] var33 = new double[]{-1.0, 1.0};
        var16[1] = var33;
        double[] var35 = new double[]{1.0, -1.0};
        var16[2] = var35;
        double[] var37 = new double[]{-1.0, -1.0};
        var16[3] = var37;
        CONE_EDGES = var16;
        boolean var19;
        if (Particle.FLASH.getDataType() == Color.class) {
            var19 = true;
        } else {
            var19 = false;
        }

        FLASH_NEEDS_COLOR = var19;
    }

    private static record Aim(Location eye, Location feet) {
    }

    private static record Phase(
        int rings,
        int slices,
        double stepDeg,
        double radiusStart,
        double radiusDelta,
        double forwardStep,
        double seedStep,
        Particle particle,
        int particleCount,
        double offset
    ) {
    }
}
