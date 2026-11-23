package com.bliss.honored;

import com.bliss.core.Particles;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class HonoredOneAuraTask extends BukkitRunnable {
    private static final double BEHIND = 0.7;
    private static final double LIFT = 1.1;
    private static final int ARM_COUNT = 2;
    private static final int ARM_JETS = 1;
    private static final double ARM_HUB_GAP = 0.0;
    private static final double ARM_LAUNCH = 0.091;
    private static final double ARM_SPIN = 0.168;
    private static final double TAU = Math.PI * 2;
    private static final double MOVE_THRESHOLD = 0.02;
    private static final DustOptions FEET_GOLD;
    private static final double DUST_RADIUS = 0.35;
    private static final double FEET_Y = 0.15;
    private static final double HEAD_Y = 1.9;
    private static final DustOptions HEAD_RING_GOLD;
    private static final double HEAD_RING_RADIUS = 0.5;
    private static final double HEAD_RING_TILT = Math.toRadians(22.0);
    private static final double HEAD_ARC_VISIBLE = Math.toRadians(60.0);
    private static final double HEAD_ARC_SPEED = 0.2;
    private static final double HEAD_ARC_SPACING = 0.08;
    private static final double HEAD_ARC_OPPOSED = Math.PI;
    private static final double HEAD_MOVE_SPACING = 0.4;
    private final Plugin plugin;
    private final HonoredOneManager manager;
    private double haloPhase = 0.0;
    private double armAng = 0.0;
    private Location lastLoc;

    public HonoredOneAuraTask(Plugin var1, HonoredOneManager var2, int var3) {
        this.plugin = var1;
        this.manager = var2;
    }

    public void start(int var1) {
        Plugin var9 = this.plugin;
        this.runTaskTimer(var9, 20L, 1L);
    }

    public void run() {
        UUID var9 = this.manager.current(1257858083);
        if (var9 != null) {
            Player var10 = Bukkit.getPlayer(var9);
            if (var10 != null && var10.isOnline()) {
                World var11 = var10.getWorld();
                Location var12 = var10.getLocation();
                Vector var13 = var12.getDirection().setY(0);
                if (var13.lengthSquared() < 1.0E-6) {
                    Vector var39 = new Vector(0, 0, 1);
                    var13 = var39;
                }

                Vector var15;
                Location var16;
                Vector var42;
                double var66;
                label36: {
                    var13.normalize();
                    var42 = new Vector(0, 1, 0);
                    Vector var44 = var13.clone();
                    var15 = var44.crossProduct(var42).normalize();
                    Location var48 = var12.clone();
                    Vector var72 = var13.clone().multiply(-0.7);
                    var16 = var48.add(var72).add(0.0, 1.1, 0.0);
                    if (this.lastLoc != null) {
                        World var55 = this.lastLoc.getWorld();
                        World var76 = var12.getWorld();
                        if (var55 == var76) {
                            Location var57 = this.lastLoc;
                            var66 = var57.distance(var12);
                            break label36;
                        }
                    }

                    var66 = 0.0;
                }

                Location var79 = var12.clone();
                this.lastLoc = var79;
                boolean var68;
                if (var66 > 0.02) {
                    var68 = true;
                } else {
                    var68 = false;
                }

                if (!var68) {
                    double var82 = this.armAng + 0.168;
                    this.armAng = var82;
                    double var104 = this.armAng;
                    this.arms(var11, var16, var15, var42, var104, 1714198514);
                }

                this.goldDust(var11, var10, var68, 848049971);
            }
        }
    }

    private void goldDust(World var1, Player var2, boolean var3, int var4) {
        Location var14 = var2.getLocation();
        ThreadLocalRandom var15 = ThreadLocalRandom.current();
        Location var22 = var14.clone().add(0.0, 0.15, 0.0);
        this.dustMote(var1, var22, var15, 1988803601);
        Location var25 = var14.clone().add(0.0, 1.9, 0.0);
        this.headRings(var1, var25, var3, 1157567442);
    }

    private void headRings(World var1, Location var2, boolean var3, int var4) {
        if (!var3) {
            double var48 = this.haloPhase + 0.2;
            this.haloPhase = var48;
        }

        Vector var21 = new Vector(0, 1, 0);
        Vector var22 = new Vector(1, 0, 0);
        double var14 = Math.cos(HEAD_RING_TILT);
        double var16 = Math.sin(HEAD_RING_TILT);
        Vector var32 = var21.clone();
        Vector var33 = var32.multiply(var14);
        Vector var53 = var22.clone();
        Vector var54 = var53.multiply(var16);
        Vector var18 = var33.add(var54).normalize();
        Vector var37 = var21.clone();
        Vector var38 = var37.multiply(var14);
        Vector var58 = var22.clone();
        Vector var59 = var58.multiply(var16);
        Vector var19 = var38.subtract(var59).normalize();
        if (var3) {
            this.drawStaticRing(var1, var2, var18, 496807417);
            this.drawStaticRing(var1, var2, var19, 496807417);
        } else {
            double var81 = this.haloPhase;
            this.drawHeadRing(var1, var2, var18, var81, 2141566869);
            double var86 = this.haloPhase + Math.PI;
            this.drawHeadRing(var1, var2, var19, var86, 2141566869);
        }
    }

    private void drawStaticRing(World var1, Location var2, Vector var3, int var4) {
        Vector var75;
        if (Math.abs(var3.getX()) < 0.9) {
            var75 = new Vector(1, 0, 0);
        } else {
            var75 = new Vector(0, 1, 0);
        }

        Vector var42 = var3.clone();
        Vector var25 = var42.crossProduct(var75).normalize();
        Vector var46 = var3.clone();
        Vector var26 = var46.crossProduct(var25).normalize();
        int var82 = (int)Math.round(Math.PI * 5.0 / 2.0);
        int var27 = Math.max(4, var82);

        for (int var28 = 0; var28 < var27; var28++) {
            double var94 = (double)var28;
            double var55 = (Math.PI * 2) * var94;
            double var97 = (double)var27;
            double var29 = var55 / var97;
            double var31 = Math.cos(var29) * 0.5;
            double var33 = Math.sin(var29) * 0.5;
            Location var72 = var2.clone();
            double var85 = var25.getX();
            double var87 = var85 * var31;
            double var108 = var26.getX();
            double var110 = var108 * var33;
            double var89 = var87 + var110;
            double var113 = var25.getY();
            double var115 = var113 * var31;
            double var127 = var26.getY();
            double var129 = var127 * var33;
            double var117 = var115 + var129;
            double var132 = var25.getZ();
            double var134 = var132 * var31;
            double var141 = var26.getZ();
            double var143 = var141 * var33;
            double var136 = var134 + var143;
            Location var35 = var72.add(var89, var117, var136);
            Particle var91 = Particle.DUST;
            DustOptions var22 = HEAD_RING_GOLD;
            Particles.spawn(var1, var91, var35, 1, 0.0, 0.0, 0.0, 0.0, var22, true, 1748031581);
        }
    }

    private void drawHeadRing(World var1, Location var2, Vector var3, double var4, int var6) {
        Vector var45;
        if (Math.abs(var3.getX()) < 0.9) {
            var45 = new Vector(1, 0, 0);
        } else {
            var45 = new Vector(0, 1, 0);
        }

        Vector var47 = var3.clone();
        Vector var27 = var47.crossProduct(var45).normalize();
        Vector var51 = var3.clone();
        Vector var28 = var51.crossProduct(var27).normalize();
        int var92 = (int)Math.round(HEAD_ARC_VISIBLE * 0.5 / 0.08);
        int var29 = Math.max(2, var92);
        double var103 = HEAD_ARC_VISIBLE;
        double var30 = var4 - var103;

        for (int var32 = 0; var32 <= var29; var32++) {
            double var105 = HEAD_ARC_VISIBLE;
            double var138 = (double)var32;
            double var107 = var105 * var138;
            double var141 = (double)var29;
            double var109 = var107 / var141;
            double var33 = var30 + var109;
            double var35 = Math.cos(var33) * 0.5;
            double var37 = Math.sin(var33) * 0.5;
            Location var79 = var2.clone();
            double var95 = var27.getX();
            double var97 = var95 * var35;
            double var124 = var28.getX();
            double var126 = var124 * var37;
            double var99 = var97 + var126;
            double var129 = var27.getY();
            double var131 = var129 * var35;
            double var149 = var28.getY();
            double var151 = var149 * var37;
            double var133 = var131 + var151;
            double var154 = var27.getZ();
            double var156 = var154 * var35;
            double var163 = var28.getZ();
            double var165 = var163 * var37;
            double var158 = var156 + var165;
            Location var39 = var79.add(var99, var133, var158);
            Particle var101 = Particle.DUST;
            DustOptions var24 = HEAD_RING_GOLD;
            Particles.spawn(var1, var101, var39, 1, 0.0, 0.0, 0.0, 0.0, var24, true, 1748031581);
        }
    }

    private void dustMote(World var1, Location var2, ThreadLocalRandom var3, int var4) {
        double var26 = var3.nextDouble() * Math.PI * 2.0;
        double var55 = Math.sqrt(var3.nextDouble());
        double var28 = 0.35 * var55;
        Location var42 = var2.clone();
        double var45 = Math.cos(var26);
        double var47 = var45 * var28;
        double var61 = Math.sin(var26);
        double var63 = var61 * var28;
        Location var30 = var42.add(var47, 0.0, var63);
        Particle var49 = Particle.DUST;
        DustOptions var24 = FEET_GOLD;
        Particles.spawn(var1, var49, var30, 1, 0.0, 0.0, 0.0, 0.0, var24, true, 1748031581);
    }

    private void arms(World var1, Location var2, Vector var3, Vector var4, double var5, int var7) {
        for (int var25 = 0; var25 < 2; var25++) {
            for (int var26 = 0; var26 < 1; var26++) {
                double var68 = (double)var25 * Math.PI;
                double var27 = var5 + var68;
                double var97 = (double)var26;
                double var87 = 0.35 * var97;
                double var72 = 1.0 - var87;
                double var29 = 0.091 * var72;
                Vector var45 = var3.clone();
                double var55 = Math.cos(var27);
                Vector var46 = var45.multiply(var55);
                Vector var58 = var4.clone();
                double var76 = Math.sin(var27);
                Vector var59 = var58.multiply(var76);
                Vector var31 = var46.add(var59);
                Location var49 = var2.clone();
                double var63 = var31.getX() * 0.0;
                double var82 = var31.getY() * 0.0;
                double var95 = var31.getZ() * 0.0;
                Location var32 = var49.add(var63, var82, var95);
                Particle var65 = Particle.END_ROD;
                double var90 = var31.getX();
                double var100 = var31.getY();
                double var102 = var31.getZ();
                Particles.spawn(var1, var65, var32, 0, var90, var100, var102, var29, null, true, 1748031581);
            }
        }
    }

    static {
        Color var13 = Color.fromRGB(255, 184, 62);
        DustOptions var0 = new DustOptions(var13, 1.0F);
        FEET_GOLD = var0;
        Color var15 = Color.fromRGB(255, 184, 62);
        DustOptions var4 = new DustOptions(var15, 0.28F);
        HEAD_RING_GOLD = var4;
    }
}
