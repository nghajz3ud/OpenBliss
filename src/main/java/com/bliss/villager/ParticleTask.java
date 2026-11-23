package com.bliss.villager;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public final class ParticleTask {
    private static final double COMPASS_HEIGHT = 5.0;
    private static final double TRIAL_RADIUS = 3.0;
    private static final int TRIAL_POINTS = 36;
    private static final int TRIAL_TRAIL = 3;
    private static final double TRIAL_TOP = 0.7;
    private static final double TRIAL_STEP_Y = 0.12;
    private static final double TILT_DEG = 20.0;
    private static final double COPPER_TILT_DEG = -45.0;
    private static final double COPPER_RADIUS = 3.5;
    private static final double OUTER_TILT_DEG = 10.0;
    private static final double SELF_SPIN_DEG = 0.8;
    private static final double DISK_INNER = 0.0;
    private static final double DISK_OUTER = 1.0;
    private static final int DISK_ARMS = 4;
    private static final int DISK_PTS = 11;
    private static final double DISK_SPIN = 0.35;
    private static final double DISK_SWEEP = 1.6;
    private static final double DISK_TILT = 25.0;
    private static final double DISK_DRIFT = 0.12;
    private static final double DISK_PULL = 1.0;
    private static final double DISK_SWIRL = 0.45;
    private static final DustOptions AQUA;
    private static final DustOptions WHITE;
    private static final DustOptions BALL;
    private static final DustOptions CORE;
    private static final DustOptions GRAY;
    private static final double HAZE_INNER_R = 13.0;
    private static final double HAZE_OUTER_R = 17.0;
    private static final int HAZE_PER_TICK = 5;
    private static final double HAZE_DRIFT_SPEED = 0.03;
    private static final double HAZE_Y_SPREAD = 3.0;
    private static final double BLOSSOM_R = 20.0;
    private static final double BLOSSOM_TOP = 11.0;
    private static final int BLOSSOM_POINTS = 48;
    private static final int BLOSSOM_EVERY = 15;
    private static final double NETHER_SCALE = 8.0;
    private static final double FULL_BORDER_BLOCKS = 8000.0;
    private static final double MAX_BEAM_LEN = 30.0;
    private static final double MIN_BEAM_LEN = 4.0;
    private static final double LOCK_RADIUS_SQ = 4.0;
    private static final double BENEATH_RADIUS = 10.0;
    private static final long BENEATH_PERIOD = 300L;
    private static final String BENEATH_MSG = "&e(!)Beneath the earth";
    private final BlissPlugin plugin;
    private final EventState state;
    private BukkitTask compassTask;
    private BukkitTask beamTask;
    private BukkitTask auraTask;
    private BukkitTask villagerLockTask;
    private BukkitTask beneathTask;
    private double selfSpinDeg = 0.0;
    private int blossomTick = 0;
    private static final double HEAD_SPEED_RAD = 0.06;
    private static final double VISIBLE_ARC_RAD = 1.0995574287564276;
    private static final double CIRCLE_RADIUS = 1.3;
    private static final double CIRCLE_Y_OFFSET = 1.0;
    private static final double THICKNESS_OFFSET = 0.08;
    private static final int THICKNESS_LAYERS = 3;
    private static final int PARTICLES_PER_RAD = 5;
    private static final float CENTER_PARTICLE_SIZE = 2.0F;
    private static final float EDGE_PARTICLE_SIZE = 0.2F;
    private static final int INTER_CIRCLE_GAP_TICKS = 20;
    private static final double EGG_MIN_FACTOR = 0.75;
    private static final double EGG_MAX_FACTOR = 1.25;
    private final Map<Integer, ParticleTask.CircleProgress> circleProgress;
    private final Random random;
    private final Map<Integer, Location> auraLastPos;
    private static final double AURA_MOVE_EPS_SQ = 0.0025;
    private double diskAngle;
    private int trialStep;

    public ParticleTask(BlissPlugin var1, EventState var2, int var3) {
        HashMap var17 = new HashMap();
        this.circleProgress = var17;
        Random var18 = new Random();
        this.random = var18;
        HashMap var19 = new HashMap();
        this.auraLastPos = var19;
        this.diskAngle = 0.0;
        this.trialStep = 0;
        this.plugin = var1;
        this.state = var2;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var23 = this.plugin;
        Runnable var32 = this::drawCompass;
        BukkitTask var14 = var3.runTaskTimer(var23, var32, 1L, 1L);
        this.compassTask = var14;
        BukkitScheduler var15 = Bukkit.getScheduler();
        BlissPlugin var25 = this.plugin;
        Runnable var34 = this::drawTrackingBeams;
        BukkitTask var16 = var15.runTaskTimer(var25, var34, 4L, 4L);
        this.beamTask = var16;
        BukkitScheduler var17 = Bukkit.getScheduler();
        BlissPlugin var27 = this.plugin;
        Runnable var36 = this::drawAuras;
        BukkitTask var18 = var17.runTaskTimer(var27, var36, 1L, 1L);
        this.auraTask = var18;
        BukkitScheduler var19 = Bukkit.getScheduler();
        BlissPlugin var29 = this.plugin;
        Runnable var38 = this::lockVillagers;
        BukkitTask var20 = var19.runTaskTimer(var29, var38, 10L, 10L);
        this.villagerLockTask = var20;
        BukkitScheduler var21 = Bukkit.getScheduler();
        BlissPlugin var31 = this.plugin;
        Runnable var40 = this::warnBeneath;
        BukkitTask var22 = var21.runTaskTimer(var31, var40, 300L, 300L);
        this.beneathTask = var22;
    }

    public void stop(int var1) {
        if (this.compassTask != null) {
            this.compassTask.cancel();
        }

        if (this.beamTask != null) {
            this.beamTask.cancel();
        }

        if (this.auraTask != null) {
            this.auraTask.cancel();
        }

        if (this.villagerLockTask != null) {
            this.villagerLockTask.cancel();
        }

        if (this.beneathTask != null) {
            this.beneathTask.cancel();
        }
    }

    private void drawCompass() {
        if (this.state.isActive(2119994266)) {
            Location var24 = this.state.compassLoc(561536293);
            if (var24 != null) {
                this.state.addRingRot(2.0, 919066100);
                double var232 = this.selfSpinDeg + 0.8;
                this.selfSpinDeg = var232;
                World var25 = var24.getWorld();
                double var26 = var24.getX();
                double var28 = var24.getY() + 5.0;
                double var30 = var24.getZ();
                this.trialRing(var25, var24, 1079538860);
                double var32 = Math.toRadians(20.0);
                double var34 = Math.sin(var32);
                double var36 = Math.cos(var32);
                double var38 = Math.toRadians(-45.0);
                double var40 = Math.sin(var38);
                double var42 = Math.cos(var38);
                double var44 = Math.toRadians(10.0);
                double var46 = Math.sin(var44);
                double var48 = Math.cos(var44);
                double var50 = Math.toRadians(this.selfSpinDeg);
                double var52 = Math.sin(var50);
                double var54 = Math.cos(var50);

                for (int var56 = 1; var56 <= 60; var56++) {
                    double var207 = (double)var56 / 60.0 * 360.0;
                    double var317 = this.state.ringRot(421317427);
                    double var476 = Math.toRadians(var207 + var317);
                    double var321 = Math.sin(var476);
                    double var480 = 3.5 * var321;
                    double var325 = Math.cos(var476);
                    double var219 = -3.5 * var325;
                    double var484 = var219 * var40;
                    double var331 = Math.cos(var476);
                    double var225 = 3.5 * var331;
                    double var488 = var225 * var42;
                    Particle var243 = Particle.WAX_ON;
                    double var371 = var480 * var54;
                    double var337 = var26 + var371;
                    double var375 = var488 * var52;
                    double var339 = var337 + var375;
                    double var379 = var28 + var484;
                    double var439 = var480 * var52;
                    double var417 = var30 - var439;
                    double var443 = var488 * var54;
                    double var419 = var417 + var443;
                    Particles.spawn(var25, var243, var339, var379, var419, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                }

                for (byte var472 = 1; var472 <= 72; var472 += 1) {
                    double var179 = (double)var472 / 72.0 * 360.0;
                    double var287 = this.state.ringRot(421317427);
                    double var474 = Math.toRadians(var179 - var287);
                    double var291 = Math.sin(var474);
                    double var478 = 8.0 * var291;
                    double var295 = Math.cos(var474);
                    double var191 = -8.0 * var295;
                    double var482 = var191 * var34;
                    double var301 = Math.cos(var474);
                    double var197 = 8.0 * var301;
                    double var486 = var197 * var36;
                    Particle var242 = Particle.WAX_OFF;
                    double var359 = var478 * var54;
                    double var307 = var26 + var359;
                    double var363 = var486 * var52;
                    double var309 = var307 + var363;
                    double var367 = var28 + var482;
                    double var430 = var478 * var52;
                    double var405 = var30 - var430;
                    double var434 = var486 * var54;
                    double var407 = var405 + var434;
                    Particles.spawn(var25, var242, var309, var367, var407, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                }

                for (byte var473 = 1; var473 <= 150; var473 += 1) {
                    double var151 = (double)var473 / 150.0 * 360.0;
                    double var257 = this.state.ringRot(421317427);
                    double var57 = Math.toRadians(var151 - var257);
                    double var261 = Math.sin(var57);
                    double var59 = 12.0 * var261;
                    double var265 = Math.cos(var57);
                    double var163 = -12.0 * var265;
                    double var61 = var163 * var46;
                    double var271 = Math.cos(var57);
                    double var169 = 12.0 * var271;
                    double var63 = var169 * var48;
                    Particle var241 = Particle.WAX_OFF;
                    double var347 = var59 * var54;
                    double var277 = var26 + var347;
                    double var351 = var63 * var52;
                    double var279 = var277 + var351;
                    double var355 = var28 + var61;
                    double var421 = var59 * var52;
                    double var393 = var30 - var421;
                    double var425 = var63 * var54;
                    double var395 = var393 + var425;
                    Particles.spawn(var25, var241, var279, var355, var395, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                }

                this.spawnAccretionDisk(var25, var26, var28, var30, 2046264577);
                this.spawnGrayHaze(var25, var26, var28, var30, 184489685);
                this.spawnBlossom(var25, var26, var28, var30, 447593450);
            }
        }
    }

    private void spawnBlossom(World var1, double var2, double var4, double var6, int var8) {
        int var61 = this.blossomTick;
        int var65 = var61 + 1;
        this.blossomTick = var65;
        if (var61 % 15 == 0) {
            double var24 = var4 + 11.0;
            double var26 = Math.toRadians(this.state.ringRot(421317427));

            for (int var28 = 0; var28 < 48; var28++) {
                double var89 = (double)var28;
                double var72 = (Math.PI * 2) * var89 / 48.0;
                double var29 = var26 + var72;
                double var78 = Math.cos(var29) * 20.0;
                double var31 = var2 + var78;
                double var84 = Math.sin(var29) * 20.0;
                double var33 = var6 + var84;
                Particle var64 = Particle.CHERRY_LEAVES;
                Particles.spawn(var1, var64, var31, var24, var33, 1, 0.0, 0.0, 0.0, 0.0, null, false, 954482185);
            }
        }
    }

    private void spawnGrayHaze(World var1, double var2, double var4, double var6, int var8) {
        for (int var28 = 0; var28 < 5; var28++) {
            double var29 = this.random.nextDouble() * 2.0 * Math.PI;
            double var84 = this.random.nextDouble() * 4.0;
            double var31 = 13.0 + var84;
            double var33 = Math.cos(var29);
            double var35 = Math.sin(var29);
            double var88 = var31 * var33;
            double var37 = var2 + var88;
            double var92 = var31 * var35;
            double var39 = var6 + var92;
            double var100 = (this.random.nextDouble() - 0.5) * 3.0;
            double var41 = var4 + var100;
            Particle var77 = Particle.DUST;
            double var114 = var33 * 0.5;
            double var118 = var35 * 0.5;
            DustOptions var26 = GRAY;
            Particles.spawn(var1, var77, var37, var41, var39, 0, var114, 1.0, var118, 0.03, var26, false, 954482185);
        }
    }

    private static void spawnSphereShell(World var0, double var1, double var3, double var5, double var7, DustOptions var9, int var10, int var11) {
        for (int var31 = 0; var31 <= var10; var31++) {
            double var79 = (double)var31;
            double var48 = Math.PI * var79;
            double var82 = (double)var10;
            double var32 = var48 / var82;
            double var86 = Math.cos(var32);
            double var34 = var7 * var86;
            double var90 = Math.sin(var32);
            double var36 = var7 * var90;

            for (int var38 = 0; var38 < var11; var38++) {
                double var93 = (double)var38;
                double var64 = (Math.PI * 2) * var93;
                double var96 = (double)var11;
                double var39 = var64 / var96;
                double var100 = Math.cos(var39);
                double var41 = var36 * var100;
                double var104 = Math.sin(var39);
                double var43 = var36 * var104;
                Particle var78 = Particle.DUST;
                double var108 = var1 + var41;
                double var112 = var3 + var34;
                double var116 = var5 + var43;
                Particles.spawn(var0, var78, var108, var112, var116, 1, 0.0, 0.0, 0.0, 0.0, var9, false, 954482185);
            }
        }
    }

    private static void spawnSphereShellParticle(World var0, double var1, double var3, double var5, double var7, Particle var9, int var10, int var11) {
        for (int var29 = 0; var29 <= var10; var29++) {
            double var77 = (double)var29;
            double var46 = Math.PI * var77;
            double var80 = (double)var10;
            double var30 = var46 / var80;
            double var84 = Math.cos(var30);
            double var32 = var7 * var84;
            double var88 = Math.sin(var30);
            double var34 = var7 * var88;

            for (int var36 = 0; var36 < var11; var36++) {
                double var91 = (double)var36;
                double var62 = (Math.PI * 2) * var91;
                double var94 = (double)var11;
                double var37 = var62 / var94;
                double var98 = Math.cos(var37);
                double var39 = var34 * var98;
                double var102 = Math.sin(var37);
                double var41 = var34 * var102;
                double var106 = var1 + var39;
                double var110 = var3 + var32;
                double var114 = var5 + var41;
                Particles.spawn(var0, var9, var106, var110, var114, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
            }
        }
    }

    private void spawnAccretionDisk(World var1, double var2, double var4, double var6, int var8) {
        double var183 = this.diskAngle + 0.35;
        this.diskAngle = var183;
        double var27 = Math.toRadians(25.0);
        double var29 = Math.sin(var27);
        double var31 = Math.cos(var27);

        for (int var33 = 0; var33 < 4; var33++) {
            double var88 = this.diskAngle;
            double var244 = (double)var33;
            double var190 = (Math.PI * 2) * var244 / 4.0;
            double var34 = var88 + var190;

            for (int var36 = 0; var36 < 11; var36++) {
                double var37 = (double)var36 / 10.0;
                double var198 = (1.0 - var37) * 1.0;
                double var39 = Math.sqrt(0.0 + var198);
                double var202 = 1.6 * var37;
                double var41 = var34 + var202;
                double var43 = Math.cos(var41);
                double var45 = Math.sin(var41);
                double var47 = -var45;
                double var51 = var39 * var43;
                double var53 = var39 * var45;
                double var57 = var53 * var29;
                double var59 = var53 * var31;
                double var145 = -var43 * 1.0;
                double var216 = var47 * 0.45;
                double var61 = var145 + var216;
                double var153 = -var45 * 1.0;
                double var222 = var43 * 0.45;
                double var63 = var153 + var222;
                double var67 = var63 * var29;
                double var69 = var63 * var31;
                double var169 = var61 * var61;
                double var232 = var67 * var67;
                double var171 = var169 + var232;
                double var236 = var69 * var69;
                double var71 = Math.sqrt(var171 + var236);
                if (!(var71 < 1.0E-6)) {
                    Particle var187 = Particle.SCRAPE;
                    double var242 = var2 + var51;
                    double var266 = var4 + var57;
                    double var270 = var6 + var59;
                    double var273 = var61 / var71;
                    double var277 = var67 / var71;
                    double var281 = var69 / var71;
                    Particles.spawn(var1, var187, var242, var266, var270, 0, var273, var277, var281, 0.12, 1717927553);
                }
            }
        }
    }

    private void trialRing(World var1, Location var2, int var3) {
        int var58 = this.trialStep;
        int var61 = var58 + 1;
        this.trialStep = var61;
        int var20 = var58;

        for (int var21 = 0; var21 < 3; var21++) {
            double var65 = (double)Math.floorMod(var20 - var21, 36);
            double var22 = (Math.PI * 2) * var65 / 36.0;
            double var39 = var2.getX();
            double var73 = Math.cos(var22) * 3.0;
            double var24 = var39 + var73;
            double var44 = var2.getZ();
            double var79 = Math.sin(var22) * 3.0;
            double var26 = var44 + var79;

            for (double var28 = 0.1; var28 <= 0.7; var28 += 0.12) {
                Particle var60 = Particle.TRIAL_SPAWNER_DETECTION;
                double var95 = var2.getY();
                double var97 = var95 + var28;
                Location var83 = new Location(var1, var24, var97, var26);
                Particles.spawn(var1, var60, var83, 1, 0.02, 0.02, 0.02, 0.0, null, true, 1748031581);
            }
        }
    }

    private void drawTrackingBeams() {
        if (this.state.isActive(2119994266)) {
            Location var5 = this.state.compassLoc(561536293);
            if (var5 != null) {
                for (int var6 = 1; var6 <= 3; var6++) {
                    EventState var19 = this.state;
                    Location var7 = var19.villagerLoc(var6, 1908420537);
                    EventState var22 = this.state;
                    String var8 = var22.soulHolder(var6, 745178346);
                    if (var8 != null) {
                        Player var9 = this.findOnlinePlayerByName(var8, 164864529);
                        if (var9 != null) {
                            var7 = var9.getLocation();
                            EventState var45 = this.state;
                            var45.setSoulLastPos(var6, var7, 1364685242);
                        } else {
                            EventState var37 = this.state;
                            if (var37.soulLastPos(var6, 63022161) != null) {
                                EventState var40 = this.state;
                                var7 = var40.soulLastPos(var6, 63022161);
                            }
                        }
                    } else {
                        EventState var26 = this.state;
                        if (var26.soulLastPos(var6, 63022161) != null) {
                            EventState var29 = this.state;
                            var7 = var29.soulLastPos(var6, 63022161);
                        }
                    }

                    if (var7 != null) {
                        this.drawBeam(var6, var5, var7, 1544697295);
                    }
                }
            }
        }
    }

    private void drawBeam(int var1, Location var2, Location var3, int var4) {
        World var24 = var3.getWorld();
        World var25 = var2.getWorld();
        if (var24 != null && var25 != null) {
            ParticleTask.DimTip var26 = ParticleTask.DimTip.NORMAL;
            String var27 = var24.getName().toLowerCase();
            if (var27.contains("nether")) {
                var26 = ParticleTask.DimTip.NETHER;
            } else if (var27.contains("end")) {
                var26 = ParticleTask.DimTip.END;
            }

            boolean var76;
            if (!var24.equals(var25)) {
                var76 = true;
            } else {
                var76 = false;
            }

            if (var76) {
                ParticleTask.DimTip var192 = ParticleTask.DimTip.NORMAL;
                if (var26 == var192) {
                    return;
                }
            }

            double var29 = var3.getX();
            double var31 = var3.getZ();
            if (var76) {
                ParticleTask.DimTip var193 = ParticleTask.DimTip.NETHER;
                if (var26 == var193) {
                    var29 *= 8.0;
                    var31 *= 8.0;
                }
            }

            double var204 = var2.getX();
            double var33 = var29 - var204;
            double var207 = var2.getZ();
            double var35 = var31 - var207;
            double var105 = var33 * var33;
            double var213 = var35 * var35;
            double var37 = Math.sqrt(var105 + var213);
            if (!(var37 < 1.0)) {
                double var39 = var33 / var37;
                double var41 = var35 / var37;
                int var43 = (int)Math.floor(var37 * 30.0 / 8000.0);
                if ((double)var43 < 4.0) {
                    var43 = 4;
                }

                if ((double)var43 > 30.0) {
                    var43 = 30;
                }

                DustOptions var44 = Cosmetics.beamDust(var1, 1853779924);
                double var45 = var2.getY() + 5.0;
                double var47 = var2.getX();
                double var49 = var2.getZ();
                World var51 = var25;
                double var52 = var47;
                double var54 = var49;
                double var56 = 0.2;

                while (true) {
                    double var232 = (double)var43;
                    if (!(var56 <= var232)) {
                        switch (var26) {
                            case NETHER:
                                Particle var197 = Particle.FLAME;
                                Particles.spawn(var51, var197, var52, var45, var54, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                                Particle var198 = Particle.FLAME;
                                double var268 = var45 + 0.4;
                                Particles.spawn(var51, var198, var52, var268, var54, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                                break;
                            case END:
                                Particle var194 = Particle.SCULK_SOUL;
                                Particles.spawn(var51, var194, var52, var45, var54, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                                Particle var195 = Particle.SCULK_SOUL;
                                double var260 = var45 + 0.4;
                                Particles.spawn(var51, var195, var52, var260, var54, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                                break;
                            default:
                                Particle var196 = Particle.DUST;
                                Particles.spawn(var51, var196, var52, var45, var54, 1, 0.0, 0.0, 0.0, 0.0, var44, true, 954482185);
                        }

                        return;
                    }

                    double var246 = var39 * var56;
                    double var58 = var47 + var246;
                    double var250 = var41 * var56;
                    double var60 = var49 + var250;
                    Particle var199 = Particle.DUST;
                    Particles.spawn(var51, var199, var58, var45, var60, 1, 0.0, 0.0, 0.0, 0.0, var44, true, 954482185);
                    var52 = var58;
                    var54 = var60;
                    var56 += 0.2;
                }
            }
        }
    }

    private void drawAuras() {
        if (this.state.isActive(2119994266)) {
            this.state.addAuraRot(12.0, 449571373);

            for (int var5 = 1; var5 <= 3; var5++) {
                Location var6 = this.locateAuraTarget(var5, 1531124757);
                if (var6 == null) {
                    Map var19 = this.auraLastPos;
                    Integer var42 = var5;
                    var19.remove(var42);
                } else {
                    boolean var38;
                    label34: {
                        Map var22 = this.auraLastPos;
                        Integer var44 = var5;
                        Location var7 = (Location)var22.get(var44);
                        Map var26 = this.auraLastPos;
                        Integer var46 = var5;
                        Location var51 = var6.clone();
                        var26.put(var46, var51);
                        if (var7 != null) {
                            World var30 = var7.getWorld();
                            World var48 = var6.getWorld();
                            if (var30.equals(var48) && !(var7.distanceSquared(var6) > 0.0025)) {
                                var38 = false;
                                break label34;
                            }
                        }

                        var38 = true;
                    }

                    if (!var38) {
                        this.spawnAuraHelix(var5, var6, 141764346);
                    }
                }
            }
        }
    }

    private Location locateAuraTarget(int var1, int var2) {
        EventState var7 = this.state;
        if (var7.isVillagerAlive(var1, 2034788216)) {
            EventState var10 = this.state;
            Villager var5 = var10.getVillager(var1, 1571302102);
            if (var5 != null && var5.isValid() && !var5.isDead()) {
                return var5.getLocation();
            }
        }

        EventState var20 = this.state;
        String var35 = var20.soulHolder(var1, 745178346);
        if (var35 != null) {
            Player var6 = this.findOnlinePlayerByName(var35, 164864529);
            if (var6 != null) {
                return var6.getLocation();
            }
        }

        EventState var27 = this.state;
        return var27.soulLastPos(var1, 63022161);
    }

    private void spawnAuraHelix(int var1, Location var2, int var3) {
        World var17 = var2.getWorld();
        if (var17 != null) {
            Map var38 = this.circleProgress;
            Integer var99 = var1;
            Function var6 = var0 -> new ParticleTask.CircleProgress();
            ParticleTask.CircleProgress var18 = (ParticleTask.CircleProgress)var38.computeIfAbsent(var99, var6);
            if (var18.active.isEmpty()) {
                if (var18.restTicks > 0) {
                    int var111 = var18.restTicks - 1;
                    var18.restTicks = var111;
                    return;
                }

                List var47 = var18.active;
                ParticleTask.Circle var126 = this.makeRandomCircle(275022396);
                ParticleTask.ActiveCircle var100 = new ParticleTask.ActiveCircle(var126, 727546685);
                var47.add(var100);
            }

            DustOptions var19 = Cosmetics.auraDust(var1, 2.0F, 1468893591);
            DustOptions var20 = Cosmetics.auraDustEdge(var1, 0.2F, 1707342974);
            double var21 = var2.getX();
            double var23 = var2.getY() + 1.0;
            double var25 = var2.getZ();
            Iterator var27 = var18.active.iterator();

            while (var27.hasNext()) {
                ParticleTask.ActiveCircle var28 = (ParticleTask.ActiveCircle)var27.next();
                double var105 = var28.headAngle + 0.06;
                var28.headAngle = var105;
                double var29 = var28.headAngle - 1.0995574287564276;
                double var31 = Math.max(0.0, var29);
                double var119 = var28.headAngle;
                double var33 = Math.min(Math.PI * 2, var119);
                if (var31 < var33) {
                    ParticleTask.Circle var131 = var28.circle;
                    drawArcSegment(var17, var21, var23, var25, var131, var31, var33, var19, var20, 1812931888);
                }

                if (var29 >= Math.PI * 2) {
                    var27.remove();
                }
            }

            if (var18.active.isEmpty()) {
                var18.restTicks = 20;
            }
        }
    }

    private static void drawArcSegment(
        World var0, double var1, double var3, double var5, ParticleTask.Circle var7, double var8, double var10, DustOptions var12, DustOptions var13, int var14
    ) {
        double var38 = var10 - var8;
        int var123 = (int)Math.ceil(var38 * 5.0);
        int var40 = Math.max(2, var123);

        for (int var41 = 0; var41 < var40; var41++) {
            double var116;
            if (var40 == 1) {
                var116 = 0.0;
            } else {
                double var114 = (double)var41;
                double var182 = (double)(var40 - 1);
                var116 = var114 / var182;
            }

            double var130 = var116 * var38;
            double var44 = var8 + var130;
            double var46 = Math.cos(var44);
            double var48 = Math.sin(var44);

            for (int var50 = -1; var50 <= 1; var50++) {
                double var84 = var7.radiusU;
                double var135 = (double)var50 * 0.08;
                double var51 = var84 + var135;
                double var89 = var7.radiusV;
                double var140 = (double)var50 * 0.08;
                double var53 = var89 + var140;
                double var144 = var46 * var51;
                double var193 = var7.basisU.getX();
                double var146 = var144 * var193;
                double var95 = var1 + var146;
                double var150 = var48 * var53;
                double var199 = var7.basisV.getX();
                double var152 = var150 * var199;
                double var55 = var95 + var152;
                double var156 = var46 * var51;
                double var205 = var7.basisU.getY();
                double var158 = var156 * var205;
                double var101 = var3 + var158;
                double var162 = var48 * var53;
                double var211 = var7.basisV.getY();
                double var164 = var162 * var211;
                double var57 = var101 + var164;
                double var168 = var46 * var51;
                double var217 = var7.basisU.getZ();
                double var170 = var168 * var217;
                double var107 = var5 + var170;
                double var174 = var48 * var53;
                double var223 = var7.basisV.getZ();
                double var176 = var174 * var223;
                double var59 = var107 + var176;
                DustOptions var118;
                if (var50 == 0) {
                    var118 = var12;
                } else {
                    var118 = var13;
                }

                Particle var127 = Particle.DUST;
                Particles.spawn(var0, var127, var55, var57, var59, 1, 0.0, 0.0, 0.0, 0.0, var118, false, 954482185);
            }
        }
    }

    private ParticleTask.Circle makeRandomCircle(int var1) {
        double var10 = this.random.nextDouble() * 2.0 * Math.PI;
        double var89 = this.random.nextDouble();
        double var12 = Math.acos(2.0 * var89 - 1.0);
        double var45 = Math.sin(var12);
        double var95 = Math.cos(var10);
        double var14 = var45 * var95;
        double var51 = Math.sin(var12);
        double var99 = Math.sin(var10);
        double var16 = var51 * var99;
        double var18 = Math.cos(var12);
        Vector var59 = new Vector(var14, var16, var18);
        Vector var82;
        if (Math.abs(var14) < 0.9) {
            var82 = new Vector(1, 0, 0);
        } else {
            var82 = new Vector(0, 1, 0);
        }

        Vector var66 = var59.clone();
        Vector var22 = var66.crossProduct(var82).normalize();
        Vector var70 = var59.clone();
        Vector var23 = var70.crossProduct(var22).normalize();
        double var123 = this.random.nextDouble() * 0.5;
        double var108 = 0.75 + var123;
        double var24 = 1.3 * var108;
        double var129 = this.random.nextDouble() * 0.5;
        double var112 = 0.75 + var129;
        double var26 = 1.3 * var112;
        return new ParticleTask.Circle(var22, var23, var24, var26, 1076458955);
    }

    private void lockVillagers() {
        if (this.state.isActive(2119994266)) {
            for (int var6 = 1; var6 <= 3; var6++) {
                EventState var20 = this.state;
                if (var20.isVillagerAlive(var6, 2034788216)) {
                    EventState var23 = this.state;
                    if (var23.villagerUuid(var6, 1186869288) != null) {
                        EventState var27 = this.state;
                        Villager var8 = var27.getVillager(var6, 1571302102);
                        if (var8 != null) {
                            EventState var31 = this.state;
                            Location var9 = var31.villagerLoc(var6, 1908420537);
                            if (var9 != null) {
                                World var35 = var8.getWorld();
                                World var79 = var9.getWorld();
                                if (!var35.equals(var79)) {
                                    var8.teleport(var9);
                                } else if (var8.isOnGround()) {
                                    Location var10 = var8.getLocation();
                                    double var44 = var10.getX();
                                    double var83 = var9.getX();
                                    double var11 = var44 - var83;
                                    double var49 = var10.getZ();
                                    double var86 = var9.getZ();
                                    double var13 = var49 - var86;
                                    double var55 = var11 * var11;
                                    double var92 = var13 * var13;
                                    if (var55 + var92 > 4.0) {
                                        var8.teleport(var9);
                                    } else {
                                        double var63 = var10.getY();
                                        double var97 = var9.getY();
                                        if (Math.abs(var63 - var97) > 0.5) {
                                            EventState var71 = this.state;
                                            var71.setVillagerLoc(var6, var10, 1133457113);
                                            this.state.save(1112893273);
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

    private void warnBeneath() {
        if (this.state.isActive(2119994266)) {
            for (int var10 = 1; var10 <= 3; var10++) {
                EventState var27 = this.state;
                if (!var27.wasVillagerEverKilled(var10, 1612506533)) {
                    EventState var30 = this.state;
                    if (var30.isVillagerAlive(var10, 2034788216)) {
                        EventState var33 = this.state;
                        Location var11 = var33.villagerLoc(var10, 1908420537);
                        if (var11 != null && var11.getWorld() != null) {
                            World var12 = var11.getWorld();
                            boolean var15 = false;

                            for (Player var17 : var12.getPlayers()) {
                                double var53 = var17.getLocation().getX();
                                double var85 = var11.getX();
                                double var18 = var53 - var85;
                                double var59 = var17.getLocation().getZ();
                                double var88 = var11.getZ();
                                double var20 = var59 - var88;
                                double var65 = var18 * var18;
                                double var94 = var20 * var20;
                                if (var65 + var94 <= 100.0) {
                                    Component var81 = Text.legacy("&e(!)Beneath the earth", 373192400);
                                    var17.sendMessage(var81);
                                    var15 = true;
                                }
                            }

                            if (var15) {
                                int var83 = var11.getBlockX();
                                int var99 = var11.getBlockZ();
                                int var110 = var12.getHighestBlockYAt(var83, var99);
                                double var101 = var11.getX();
                                double var105 = (double)var110 + 1.0;
                                double var108 = var11.getZ();
                                Location var84 = new Location(var12, var101, var105, var108);
                                var12.strikeLightningEffect(var84);
                            }
                        }
                    }
                }
            }
        }
    }

    private Player findOnlinePlayerByName(String var1, int var2) {
        for (Player var6 : Bukkit.getOnlinePlayers()) {
            String var15 = var6.getName();
            if (var15.equals(var1)) {
                return var6;
            }
        }

        return null;
    }

    static {
        Color var8 = Color.fromRGB(0, 210, 220);
        DustOptions var0 = new DustOptions(var8, 0.9F);
        AQUA = var0;
        Color var10 = Color.fromRGB(235, 235, 255);
        DustOptions var4 = new DustOptions(var10, 0.7F);
        WHITE = var4;
        Color var12 = Color.fromRGB(255, 140, 0);
        DustOptions var5 = new DustOptions(var12, 1.2F);
        BALL = var5;
        Color var14 = Color.fromRGB(255, 200, 50);
        DustOptions var6 = new DustOptions(var14, 1.5F);
        CORE = var6;
        Color var16 = Color.fromRGB(130, 130, 130);
        DustOptions var7 = new DustOptions(var16, 0.2F);
        GRAY = var7;
    }

    private static final class ActiveCircle {
        final ParticleTask.Circle circle;
        double headAngle;

        ActiveCircle(ParticleTask.Circle var1, int var2) {
            this.circle = var1;
            this.headAngle = 0.0;
        }
    }

    private static final class Circle {
        final Vector basisU;
        final Vector basisV;
        final double radiusU;
        final double radiusV;

        Circle(Vector var1, Vector var2, double var3, double var5, int var7) {
            this.basisU = var1;
            this.basisV = var2;
            this.radiusU = var3;
            this.radiusV = var5;
        }
    }

    private static final class CircleProgress {
        final List<ParticleTask.ActiveCircle> active;
        int restTicks;

        private CircleProgress() {
            ArrayList var2 = new ArrayList();
            this.active = var2;
            this.restTicks = 0;
        }
    }

    private static enum DimTip {
        NORMAL,
        NETHER,
        END;
    }
}
