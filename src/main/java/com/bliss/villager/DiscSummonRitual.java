package com.bliss.villager;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class DiscSummonRitual {
    private static final int ARM_START_TICK = 260;
    private static final int BURST_TICK = 460;
    private static final int SETTLE_END = 550;
    private static final double PLATFORM_RADIUS = 2.6;
    private static final double FLAME_RIM_RADIUS = 4.6;
    private static final double EGG_OFFSET = 3.8;
    private static final double AURA_RADIUS = 2.4;
    private static final double AURA_Y_OFFSET = 1.8;
    private static final double HEAD_SPEED_RAD = 0.06;
    private static final double VISIBLE_ARC_RAD = 1.7278759594743864;
    private static final double THICKNESS_OFFSET = 0.08;
    private static final int THICKNESS_LAYERS = 3;
    private static final int PARTICLES_PER_RAD = 18;
    private static final float CENTER_PARTICLE_SIZE = 1.9F;
    private static final float EDGE_PARTICLE_SIZE = 0.12F;
    private static final double EGG_MIN_FACTOR = 0.75;
    private static final double EGG_MAX_FACTOR = 1.25;
    private static final int LIGHTNING_PERIOD = 26;
    private static final double STAR_ORBIT_MAX = 0.9;
    private static final double STAR_ORBIT_MIN = 0.06;
    private static final double STAR_OMEGA_BASE = 0.063;
    private static final double STAR_OMEGA_MAX = 0.126;
    private static final double STAR_Y_OFFSET = 0.6;
    private static final double ARM_RISE = 0.35;
    private static final int ARM_COUNT = 4;
    private static final double ARM_REACH = 2.8;
    private static final double ROD_FLIGHT = 11.0;
    private static final Color ARM_COLOR = Color.fromRGB(245, 245, 255);
    private static final float ARM_SIZE_EDGE = 0.12F;
    private static final DustOptions PINK;
    private static final DustOptions MAGENTA;
    private static final DustOptions CORE;
    private static final DustOptions EMBER;
    private static final DustOptions STAR_CORE;
    private final BlissPlugin plugin;
    private final Random random;
    private final Map<Integer, DiscSummonRitual.CircleProgress> eggProgress;
    private double starAngle;
    private static long busyUntil = 0L;

    public DiscSummonRitual(BlissPlugin var1, int var2) {
        Random var4 = new Random();
        this.random = var4;
        HashMap var9 = new HashMap();
        this.eggProgress = var9;
        this.starAngle = 0.0;
        this.plugin = var1;
    }

    public static boolean isRunning(int var0) {
        long var4 = (long)Bukkit.getCurrentTick();
        long var2 = busyUntil;
        boolean var7;
        if (var4 < var2) {
            var7 = true;
        } else {
            var7 = false;
        }

        return var7;
    }

    public boolean summon(int var1) {
        final Location var11 = this.plugin.pedestalState(590646643).mainLoc(15453813);
        if (var11 != null && var11.getWorld() != null) {
            if (isRunning(1066722316)) {
                return false;
            } else {
                busyUntil = (long)(Bukkit.getCurrentTick() + 550 + 1);
                final World var12 = var11.getWorld();
                final Location var13 = var11.clone().add(0.5, 1.0, 0.5);
                FileConfiguration var37 = this.plugin.getConfig();
                String var49 = "villager-event.disc-song-key";
                String var4 = "bliss:village_clue";
                String var14 = var37.getString(var49, var4);
                BlissItems var41 = this.plugin.villagerItems(1841893765);
                final ItemStack var15 = var41.villageDisc(var14, 638606571);
                BukkitRunnable var43 = new BukkitRunnable() {
                    int t = 0;

                    public void run() {
                        if (this.t < 460) {
                            DiscSummonRitual var8 = DiscSummonRitual.this;
                            World var30 = var12;
                            Location var46 = var13;
                            int var56 = this.t;
                            var8.eggs(var30, var46, var56, 990977685);
                            if (this.t >= 260) {
                                DiscSummonRitual var12x = DiscSummonRitual.this;
                                World var33 = var12;
                                Location var48 = var13;
                                int var58 = this.t;
                                var12x.groundStars(var33, var48, var58, 1319722816);
                                DiscSummonRitual var14 = DiscSummonRitual.this;
                                World var35 = var12;
                                Location var50 = var13;
                                Location var60 = var11;
                                int var66 = this.t;
                                var14.charge(var35, var50, var60, var66, 493000336);
                            }
                        }

                        if (this.t == 460) {
                            DiscSummonRitual var28 = DiscSummonRitual.this;
                            World var45 = var12;
                            Location var55 = var13;
                            ItemStack var65 = var15;
                            var28.burst(var45, var55, var65, 1493063078);
                        } else if (this.t > 460 && this.t <= 550) {
                            DiscSummonRitual var22 = DiscSummonRitual.this;
                            World var40 = var12;
                            Location var52 = var13;
                            int var63 = this.t - 460;
                            var22.settle(var40, var52, var63, 621776341);
                        }

                        if (this.t >= 550) {
                            this.cancel();
                        } else {
                            int var43 = this.t + 1;
                            this.t = var43;
                        }
                    }
                };
                BlissPlugin var52 = this.plugin;
                var43.runTaskTimer(var52, 0L, 1L);
                return true;
            }
        } else {
            return false;
        }
    }

    private void eggs(World var1, Location var2, int var3, int var4) {
        for (int var20 = 0; var20 < 3; var20++) {
            int var21 = var20 + 1;
            double var22 = (double)var20 * (Math.PI * 2.0 / 3.0) + (Math.PI / 6);
            double var41 = var2.getX();
            double var85 = Math.cos(var22);
            double var72 = 3.8 * var85;
            double var24 = var41 + var72;
            double var26 = var2.getY() + 1.8;
            double var51 = var2.getZ();
            double var89 = Math.sin(var22);
            double var78 = 3.8 * var89;
            double var28 = var51 + var78;
            this.traceEggAura(var1, var21, var24, var26, var28, 1238448456);
            if (var3 % 14 == 0) {
                this.sideStreaks(var1, var24, var26, var28, var21, 756869629);
            }
        }

        if (var3 % 8 == 0) {
            Sound var83 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
            var1.playSound(var2, var83, 0.7F, 1.1F);
        }
    }

    private void sideStreaks(World var1, double var2, double var4, double var6, int var8, int var9) {
        ThreadLocalRandom var29 = ThreadLocalRandom.current();
        DustOptions var30 = Cosmetics.auraDust(var8, 1.8F, 1468893591);
        int var31 = var29.nextInt(3);

        for (int var32 = 0; var32 < var31; var32++) {
            double var33 = var29.nextDouble() * Math.PI * 2.0;
            double var90 = var29.nextDouble() * 2.0;
            double var35 = 0.5 + var90;
            double var63 = var4 - 0.2;
            double var97 = var29.nextDouble() * 2.6;
            double var37 = var63 + var97;
            double var142 = Math.cos(var33);
            double var144 = var142 * var35;
            double var121 = var2 + var144;
            double var175 = Math.sin(var33);
            double var177 = var175 * var35;
            double var169 = var6 + var177;
            Location var67 = new Location(var1, var121, var37, var169);
            double var103 = var29.nextDouble() - 0.5;
            double var151 = var29.nextDouble() * 0.55;
            double var130 = 0.25 + var151;
            double var156 = var29.nextDouble() - 0.5;
            Vector var68 = new Vector(var103, var130, var156);
            Vector var40 = var68.normalize().multiply(0.3);
            double var108 = var29.nextDouble() - 0.5;
            double var137 = var29.nextDouble() - 0.5;
            double var163 = var29.nextDouble() - 0.5;
            Vector var71 = new Vector(var108, var137, var163);
            Vector var41 = var71.normalize();
            double var113 = var29.nextDouble() * 0.045;
            double var75 = 0.025 + var113;
            byte var117;
            if (var29.nextBoolean()) {
                var117 = 1;
            } else {
                var117 = -1;
            }

            double var118 = (double)var117;
            double var42 = var75 * var118;
            this.animateStreak(var1, var67, var40, var30, var41, var42, 1303234238);
        }
    }

    private void animateStreak(final World var1, final Location var2, final Vector var3, final DustOptions var4, final Vector var5, final double var6, int var8) {
        BukkitRunnable var9 = new BukkitRunnable() {
            final Location pos;
            int age;

            {
                Location var28 = var2.clone();
                this.pos = var28;
                this.age = 0;
            }

            public void run() {
                Vector var17 = var3;
                Vector var31 = var5;
                double var47 = var6;
                var17.rotateAroundAxis(var31, var47);
                Vector var20 = var3;
                double var36 = var3.getY() + 0.005;
                var20.setY(var36);

                for (int var16 = 0; var16 < 2; var16++) {
                    Location var27 = this.pos;
                    Vector var45 = var3.clone().multiply(0.5);
                    var27.add(var45);
                    World var30 = var1;
                    Particle var46 = Particle.DUST;
                    Location var53 = this.pos;
                    DustOptions var55 = var4;
                    Particles.spawn(var30, var46, var53, 1, 0.0, 0.0, 0.0, 0.0, var55, true, 1748031581);
                }

                int var40 = this.age + 1;
                this.age = var40;
                if (var40 > 90) {
                    this.cancel();
                }
            }
        };
        BlissPlugin var20 = this.plugin;
        var9.runTaskTimer(var20, 0L, 1L);
    }

    private void traceEggAura(World var1, int var2, double var3, double var5, double var7, int var9) {
        Map var35 = this.eggProgress;
        Integer var92 = var2;
        Function var12 = var0 -> new DiscSummonRitual.CircleProgress();
        DiscSummonRitual.CircleProgress var23 = (DiscSummonRitual.CircleProgress)var35.computeIfAbsent(var92, var12);
        if (var23.active.isEmpty()) {
            List var42 = var23.active;
            DiscSummonRitual.Circle var122 = this.makeRandomCircle(1865770720);
            DiscSummonRitual.ActiveCircle var93 = new DiscSummonRitual.ActiveCircle(var122, 1743960705);
            var42.add(var93);
        }

        List var45 = var23.active;
        int var97 = var23.active.size() - 1;
        if (((DiscSummonRitual.ActiveCircle)var45.get(var97)).headAngle >= Math.PI * 2 && var23.active.size() < 2) {
            List var56 = var23.active;
            DiscSummonRitual.Circle var124 = this.makeRandomCircle(1865770720);
            DiscSummonRitual.ActiveCircle var99 = new DiscSummonRitual.ActiveCircle(var124, 1743960705);
            var56.add(var99);
        }

        DustOptions var25 = Cosmetics.auraDust(var2, 1.9F, 1468893591);
        DustOptions var26 = Cosmetics.auraDustEdge(var2, 0.12F, 1707342974);
        Iterator var27 = var23.active.iterator();

        while (var27.hasNext()) {
            DiscSummonRitual.ActiveCircle var28 = (DiscSummonRitual.ActiveCircle)var27.next();
            double var104 = var28.headAngle + 0.06;
            var28.headAngle = var104;
            double var29 = var28.headAngle - 1.7278759594743864;
            double var31 = Math.max(0.0, var29);
            double var116 = var28.headAngle;
            double var33 = Math.min(Math.PI * 2, var116);
            if (var31 < var33) {
                DiscSummonRitual.Circle var129 = var28.circle;
                drawArcSegment(var1, var3, var5, var7, var129, var31, var33, var25, var26, 2034492545);
            }

            if (var29 >= Math.PI * 2) {
                var27.remove();
            }
        }
    }

    private static void drawArcSegment(
        World var0,
        double var1,
        double var3,
        double var5,
        DiscSummonRitual.Circle var7,
        double var8,
        double var10,
        DustOptions var12,
        DustOptions var13,
        int var14
    ) {
        double var38 = var10 - var8;
        int var123 = (int)Math.ceil(var38 * 18.0);
        int var40 = Math.max(2, var123);

        for (int var41 = 0; var41 < var40; var41++) {
            double var117;
            if (var40 == 1) {
                var117 = 0.0;
            } else {
                double var115 = (double)var41;
                double var182 = (double)(var40 - 1);
                var117 = var115 / var182;
            }

            double var130 = var117 * var38;
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
                DustOptions var113;
                if (var50 == 0) {
                    var113 = var12;
                } else {
                    var113 = var13;
                }

                Particle var127 = Particle.DUST;
                Particles.spawn(var0, var127, var55, var57, var59, 1, 0.0, 0.0, 0.0, 0.0, var113, true, 954482185);
            }
        }
    }

    private DiscSummonRitual.Circle makeRandomCircle(int var1) {
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
        double var24 = 2.4 * var108;
        double var129 = this.random.nextDouble() * 0.5;
        double var112 = 0.75 + var129;
        double var26 = 2.4 * var112;
        return new DiscSummonRitual.Circle(var22, var23, var24, var26, 491749950);
    }

    private void groundStars(World var1, Location var2, int var3, int var4) {
        double var112 = (double)(var3 - 260) / 200.0;
        double var114 = Math.min(1.0, var112);
        double var155 = Math.pow(1.0 - var114, 0.5);
        double var118 = 0.9 * var155;
        double var28 = Math.max(0.06, var118);
        double var161 = Math.pow(0.9 / var28, 1.5);
        double var122 = 0.063 * var161;
        double var30 = Math.min(0.126, var122);
        double var101 = this.starAngle;
        double var103 = var101 + var30;
        this.starAngle = var103;
        double var32 = var2.getX();
        double var34 = var2.getZ();
        double var36 = var2.getY() + 0.6;
        double var128 = 2.8 - var28;
        double var38 = Math.max(0.0, var128) / 11.0;

        for (int var42 = 0; var42 < 4; var42++) {
            double var86 = this.starAngle;
            double var135 = (double)var42 * (Math.PI / 2);
            double var43 = var86 + var135;
            double var169 = Math.cos(var43);
            double var139 = var28 * var169;
            double var45 = var32 + var139;
            double var173 = Math.sin(var43);
            double var143 = var28 * var173;
            double var47 = var34 + var143;
            Particle var106 = Particle.END_ROD;
            double var193 = Math.cos(var43);
            double var195 = var193 * var38;
            double var207 = Math.sin(var43);
            double var209 = var207 * var38;
            Particles.spawn(var1, var106, var45, var36, var47, 0, var195, 0.031818181818181815, var209, 1.0, null, true, 954482185);
            Particle var107 = Particle.END_ROD;
            Particles.spawn(var1, var107, var45, var36, var47, 1, 0.03, 0.03, 0.03, 0.0, 1717927553);
            Particle var108 = Particle.DUST;
            DustOptions var221 = STAR_CORE;
            Particles.spawn(var1, var108, var45, var36, var47, 1, 0.0, 0.0, 0.0, 0.0, var221, true, 954482185);
        }
    }

    private void charge(World var1, Location var2, Location var3, int var4, int var5) {
        double var30 = (double)(var4 - 260) / 200.0;
        if (var4 % 2 == 0) {
            for (int var33 = 0; var33 < 32; var33++) {
                double var91 = (double)var33;
                double var54 = (Math.PI * 2) * var91 / 32.0;
                double var99 = (double)var4 * 0.05;
                double var34 = var54 + var99;
                double var103 = Math.cos(var34);
                double var36 = 4.6 * var103;
                double var107 = Math.sin(var34);
                double var38 = 4.6 * var107;
                Particle var81 = Particle.FLAME;
                double var110 = var2.getX();
                double var112 = var110 + var36;
                double var129 = var2.getY() + 0.1;
                double var143 = var2.getZ();
                double var145 = var143 + var38;
                Particles.spawn(var1, var81, var112, var129, var145, 1, 0.0, 0.01, 0.0, 0.0, 1717927553);
            }
        }

        double var116 = var30 * 0.35;
        double var151 = 0.15 + var116;
        double var83 = var2.getX();
        double var121 = var2.getY() + 0.4;
        double var136 = var2.getZ();
        DustOptions var149 = CORE;
        spawnSphereShell(var1, var83, var121, var136, var151, var149, 3, 5, 1383747451);
        if ((var4 - 260) % 26 == 0) {
            this.cosmeticLightning(var3, 1435754700);
        }

        if (var4 % 12 == 0) {
            Sound var118 = Sound.BLOCK_BEACON_AMBIENT;
            float var141 = (float)var30 * 0.9F;
            float var134 = 0.8F + var141;
            var1.playSound(var2, var118, 1.0F, var134);
        }
    }

    private void burst(World var1, Location var2, ItemStack var3, int var4) {
        Particle var6 = Particle.EXPLOSION_EMITTER;
        double var108 = var2.getX();
        double var162 = var2.getY() + 0.4;
        double var202 = var2.getZ();
        Particles.spawn(var1, var6, var108, var162, var202, 1, 749583374);
        Particle var88 = Particle.EXPLOSION;
        double var111 = var2.getX();
        double var167 = var2.getY() + 0.4;
        double var207 = var2.getZ();
        Particles.spawn(var1, var88, var111, var167, var207, 8, 0.6, 0.4, 0.6, 0.0, 1717927553);
        Particle var89 = Particle.FLAME;
        double var114 = var2.getX();
        double var172 = var2.getY() + 0.4;
        double var212 = var2.getZ();
        Particles.spawn(var1, var89, var114, var172, var212, 90, 0.3, 0.3, 0.3, 0.25, 1717927553);
        Particle var90 = Particle.LAVA;
        double var117 = var2.getX();
        double var177 = var2.getY() + 0.4;
        double var217 = var2.getZ();
        Particles.spawn(var1, var90, var117, var177, var217, 30, 0.4, 0.3, 0.4, 0.0, 1717927553);
        Particle var91 = Particle.DUST;
        double var120 = var2.getX();
        double var182 = var2.getY() + 0.4;
        double var222 = var2.getZ();
        DustOptions var27 = EMBER;
        Particles.spawn(var1, var91, var120, var182, var222, 70, 1.3, 0.6, 1.3, 0.0, var27, true, 954482185);
        double var29 = var2.getY() + 0.1;
        Color var124 = ARM_COLOR;
        DustOptions var50 = new DustOptions(var124, 0.12F);
        DustOptions var31 = var50;

        for (int var32 = 0; var32 < 4; var32++) {
            double var54 = this.starAngle;
            double var128 = (double)var32 * (Math.PI / 2);
            double var33 = var54 + var128;

            for (double var35 = 0.0; var35 < 1.4; var35 += 0.08) {
                double var134 = var35 * 6.0;
                double var37 = var33 + var134;
                double var138 = var35 * 2.2;
                double var39 = 0.3 + var138;
                Particle var93 = Particle.DUST;
                double var141 = var2.getX();
                double var226 = Math.cos(var37);
                double var192 = var39 * var226;
                double var143 = var141 + var192;
                double var229 = var2.getZ();
                double var252 = Math.sin(var37);
                double var241 = var39 * var252;
                double var231 = var229 + var241;
                Particles.spawn(var1, var93, var143, var29, var231, 1, 0.0, 0.0, 0.0, 0.0, var31, true, 954482185);
            }
        }

        var1.strikeLightningEffect(var2);
        Sound var147 = Sound.ENTITY_GENERIC_EXPLODE;
        var1.playSound(var2, var147, 1.4F, 0.8F);
        Sound var148 = Sound.BLOCK_BEACON_POWER_SELECT;
        var1.playSound(var2, var148, 1.2F, 1.2F);
        Sound var149 = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
        var1.playSound(var2, var149, 0.7F, 1.4F);
        Location var100 = var2.clone().add(0.0, 0.2, 0.0);
        Item var280 = var1.dropItem(var100, var3);
        Vector var101 = new Vector(0.0, 0.25, 0.0);
        var280.setVelocity(var101);
        var280.setPickupDelay(40);
        var280.setWillAge(false);
        TextComponent var105 = Component.text("Disc of the Wanderer");
        TextDecoration var153 = TextDecoration.ITALIC;
        Component var106 = var105.decoration(var153, false);
        var280.customName(var106);
        var280.setCustomNameVisible(true);
    }

    private void settle(World var1, Location var2, int var3, int var4) {
        Particle var6 = Particle.LARGE_SMOKE;
        double var79 = var2.getX();
        double var120 = var2.getY() + 0.3;
        double var158 = var2.getZ();
        Particles.spawn(var1, var6, var79, var120, var158, 2, 0.15, 0.3, 0.15, 0.02, 1717927553);
        if (var3 % 2 == 0) {
            Particle var70 = Particle.CAMPFIRE_COSY_SMOKE;
            double var82 = var2.getX();
            double var125 = var2.getY() + 0.2;
            double var163 = var2.getZ();
            Particles.spawn(var1, var70, var82, var125, var163, 1, 0.1, 0.2, 0.1, 0.01, 1717927553);
        }

        if (var3 % 2 == 0) {
            double[] var25 = this.randomPlatformPoint(1646672540);
            Particle var72 = Particle.LAVA;
            double var85 = var2.getX();
            double var128 = var25[0];
            double var87 = var85 + var128;
            double var133 = var2.getY() + 1.5;
            double var168 = var2.getZ();
            double var192 = var25[1];
            double var170 = var168 + var192;
            Particles.spawn(var1, var72, var87, var133, var170, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
        }

        if (var3 < 32 && var3 % 3 == 0) {
            for (int var26 = 0; var26 < 8; var26++) {
                double var100 = (double)var26;
                double var27 = (Math.PI * 2) * var100 / 8.0;
                double var107 = Math.cos(var27);
                double var29 = 2.2 * var107;
                double var111 = Math.sin(var27);
                double var31 = 2.2 * var111;
                Particle var78 = Particle.CLOUD;
                double var114 = var2.getX();
                double var116 = var114 + var29;
                double var155 = var2.getY() + 0.1;
                double var186 = var2.getZ();
                double var188 = var186 + var31;
                Particles.spawn(var1, var78, var116, var155, var188, 1, 0.05, 0.0, 0.05, 0.0, 1717927553);
            }
        }

        if (var3 % 3 == 0) {
            double var92 = (double)var3 / 6.0;
            double var246 = 1.5 + var92;
            Particle var77 = Particle.DUST;
            double var95 = var2.getX();
            double var143 = (this.random.nextDouble() - 0.5) * 0.6;
            double var97 = var95 + var143;
            double var146 = var2.getY();
            double var148 = var146 + var246;
            double var179 = var2.getZ();
            double var201 = (this.random.nextDouble() - 0.5) * 0.6;
            double var181 = var179 + var201;
            DustOptions var243;
            if (var3 % 2 == 0) {
                var243 = PINK;
            } else {
                var243 = MAGENTA;
            }

            Particles.spawn(var1, var77, var97, var148, var181, 1, 0.0, 0.0, 0.0, 0.0, var243, true, 954482185);
        }
    }

    private double[] randomPlatformPoint(int var1) {
        double var9 = this.random.nextDouble() * 2.0 * Math.PI;
        double var32 = Math.sqrt(this.random.nextDouble());
        double var11 = 2.6 * var32;
        double[] var25 = new double[2];
        double var42 = Math.cos(var9);
        double var36 = var11 * var42;
        var25[0] = var36;
        double var46 = Math.sin(var9);
        double var40 = var11 * var46;
        var25[1] = var40;
        return var25;
    }

    private void cosmeticLightning(Location var1, int var2) {
        if (var1 != null && var1.getWorld() != null) {
            for (int var9 = 0; var9 < 2; var9++) {
                int var29 = this.random.nextInt(9);
                int var10 = 7 + var29;
                int var32 = this.random.nextInt(9);
                int var11 = 7 + var32;
                Location var22 = var1.clone();
                double var40;
                if (this.random.nextBoolean()) {
                    var40 = (double)var10;
                } else {
                    var40 = (double)(-var10);
                }

                double var48;
                if (this.random.nextBoolean()) {
                    var48 = (double)var11;
                } else {
                    var48 = (double)(-var11);
                }

                Location var12 = var22.add(var40, 0.0, var48);
                World var25 = var1.getWorld();
                var25.strikeLightningEffect(var12);
            }
        }
    }

    private static void spawnSphereShell(World var0, double var1, double var3, double var5, double var7, DustOptions var9, int var10, int var11, int var12) {
        for (int var32 = 0; var32 <= var10; var32++) {
            double var80 = (double)var32;
            double var49 = Math.PI * var80;
            double var83 = (double)var10;
            double var33 = var49 / var83;
            double var87 = Math.cos(var33);
            double var35 = var7 * var87;
            double var91 = Math.sin(var33);
            double var37 = var7 * var91;

            for (int var39 = 0; var39 < var11; var39++) {
                double var94 = (double)var39;
                double var65 = (Math.PI * 2) * var94;
                double var97 = (double)var11;
                double var40 = var65 / var97;
                double var101 = Math.cos(var40);
                double var42 = var37 * var101;
                double var105 = Math.sin(var40);
                double var44 = var37 * var105;
                Particle var79 = Particle.DUST;
                double var109 = var1 + var42;
                double var113 = var3 + var35;
                double var117 = var5 + var44;
                Particles.spawn(var0, var79, var109, var113, var117, 1, 0.0, 0.0, 0.0, 0.0, var9, true, 954482185);
            }
        }
    }

    static {
        Color var14 = Color.fromRGB(255, 105, 180);
        DustOptions var6 = new DustOptions(var14, 1.0F);
        PINK = var6;
        Color var16 = Color.fromRGB(218, 112, 214);
        DustOptions var7 = new DustOptions(var16, 1.0F);
        MAGENTA = var7;
        Color var18 = Color.fromRGB(255, 205, 90);
        DustOptions var8 = new DustOptions(var18, 1.4F);
        CORE = var8;
        Color var20 = Color.fromRGB(220, 60, 30);
        DustOptions var9 = new DustOptions(var20, 1.2F);
        EMBER = var9;
        Color var22 = Color.fromRGB(255, 255, 255);
        DustOptions var10 = new DustOptions(var22, 1.1F);
        STAR_CORE = var10;
    }

    private static final class ActiveCircle {
        final DiscSummonRitual.Circle circle;
        double headAngle;

        ActiveCircle(DiscSummonRitual.Circle var1, int var2) {
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
        final List<DiscSummonRitual.ActiveCircle> active;

        private CircleProgress() {
            ArrayList var2 = new ArrayList();
            this.active = var2;
        }
    }
}
