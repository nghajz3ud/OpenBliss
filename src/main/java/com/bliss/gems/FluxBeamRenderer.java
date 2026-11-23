package com.bliss.gems;

import com.bliss.core.Particles;
import com.bliss.core.SonicBoomSound;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustTransition;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class FluxBeamRenderer {
    private static final Color CYAN = Color.fromRGB(94, 215, 255);
    private static final Color WHITE = Color.fromRGB(255, 255, 255);

    private FluxBeamRenderer() {
    }

    public static FluxBeamRenderer.Variant variantFor(int var0, int var1) {
        if (var0 <= 2) {
            return FluxBeamRenderer.Variant.LOW;
        } else if (var0 <= 5) {
            return FluxBeamRenderer.Variant.MID;
        } else {
            return var0 <= 8 ? FluxBeamRenderer.Variant.BEAM : FluxBeamRenderer.Variant.ULTRA;
        }
    }

    public static void render(Player var0, int var1) {
        Location var7 = var0.getEyeLocation();
        FluxBeamRenderer.Variant var8 = variantFor(var1, 391611836);
        Vector var9 = var7.getDirection().normalize();
        FluxBeamRenderer.Variant var3 = FluxBeamRenderer.Variant.LOW;
        if (var8 != var3) {
            FluxBeamRenderer.Variant var23 = FluxBeamRenderer.Variant.MID;
            if (var8 != var23) {
                drawStraight(var7, var9, var8, 2005448674);
                drawSonicTrail(var0, var7, var9, var8, 1027021141);
                World var21 = var0.getWorld();
                Sound var29 = Sound.ENTITY_WARDEN_SONIC_CHARGE;
                var21.playSound(var7, var29, 1.5F, 1.0F);
                return;
            }
        }

        drawSpiral(var7, var9, var8, 1128684241);
    }

    private static void drawSpiral(Location var0, Vector var1, FluxBeamRenderer.Variant var2, int var3) {
        double var22 = Math.random() * 60.0;
        double var24 = var2.radiusStart;
        Vector var26 = horizPerp(var1, 363265814);
        Vector var45 = var1.clone();
        Vector var27 = var45.crossProduct(var26).normalize();
        int var28 = 0;

        while (true) {
            int var90 = var2.rings;
            if (var28 >= var90) {
                return;
            }

            double var113 = (double)var28;
            double var136 = var2.forwardStep;
            double var115 = var113 * var136;
            double var29 = 1.0 + var115;
            Location var55 = var0.clone();
            Vector var92 = var1.clone();
            Vector var93 = var92.multiply(var29);
            Location var31 = var55.add(var93);
            var22 += 5.0;
            int var32 = 0;

            while (true) {
                int var95 = var2.slicesPerRing;
                if (var32 >= var95) {
                    double var122 = var2.radiusDelta;
                    var24 += var122;
                    var28++;
                    break;
                }

                int var68 = var32 - 1;
                int var98 = var2.stepDegrees;
                double var70 = (double)(var68 * var98);
                double var33 = Math.toRadians(var70 + var22);
                Vector var77 = var26.clone();
                double var101 = Math.cos(var33);
                double var103 = var101 * var24;
                Vector var78 = var77.multiply(var103);
                Vector var106 = var27.clone();
                double var128 = Math.sin(var33);
                double var130 = var128 * var24;
                Vector var107 = var106.multiply(var130);
                Vector var35 = var78.add(var107);
                Location var81 = var31.clone();
                Location var36 = var81.add(var35);
                if ((var32 & 1) == 0) {
                    World var88 = var36.getWorld();
                    Particle var111 = Particle.END_ROD;
                    Particles.spawn(var88, var111, var36, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
                } else {
                    World var86 = var36.getWorld();
                    Particle var110 = Particle.DUST_COLOR_TRANSITION;
                    Color var19 = CYAN;
                    Color var20 = WHITE;
                    DustTransition var18 = new DustTransition(var19, var20, 1.5F);
                    Particles.spawn(var86, var110, var36, 2, 0.3, 0.3, 0.3, 0.0, var18, true, 1748031581);
                }

                var32++;
            }
        }
    }

    private static void drawStraight(Location var0, Vector var1, FluxBeamRenderer.Variant var2, int var3) {
        int var20 = 0;

        while (true) {
            int var36 = var2.rings;
            if (var20 >= var36) {
                return;
            }

            double var42 = (double)var20;
            double var52 = var2.forwardStep;
            double var44 = var42 * var52;
            double var21 = 1.0 + var44;
            Location var30 = var0.clone();
            Vector var38 = var1.clone();
            Vector var39 = var38.multiply(var21);
            Location var23 = var30.add(var39);
            World var33 = var23.getWorld();
            Particle var40 = Particle.END_ROD;
            Particles.spawn(var33, var40, var23, 3, 0.0, 0.0, 0.0, 0.03, null, true, 1748031581);
            World var35 = var23.getWorld();
            Particle var41 = Particle.DUST_COLOR_TRANSITION;
            FluxBeamRenderer.Variant var56 = FluxBeamRenderer.Variant.ULTRA;
            byte var51;
            if (var2 == var56) {
                var51 = 60;
            } else {
                var51 = 40;
            }

            Color var17 = CYAN;
            Color var18 = WHITE;
            DustTransition var65 = new DustTransition(var17, var18, 2.5F);
            Particles.spawn(var35, var41, var23, var51, 0.5, 0.5, 0.5, 0.0, var65, true, 1748031581);
            var20++;
        }
    }

    private static void drawSonicTrail(Player var0, Location var1, Vector var2, FluxBeamRenderer.Variant var3, int var4) {
        FluxBeamRenderer.Variant var6 = FluxBeamRenderer.Variant.ULTRA;
        byte var28;
        if (var3 == var6) {
            var28 = 24;
        } else {
            var28 = 16;
        }

        byte var17 = var28;
        String var20 = "flux-trail";
        World var30 = var1.getWorld();
        SonicBoomSound.burst(var20, var30, var1, 1967648345);

        for (int var18 = 1; var18 <= var17; var18++) {
            Location var24 = var1.clone();
            Vector var33 = var2.clone();
            double var37 = (double)var18;
            double var43 = (double)var3.range;
            double var49 = (double)var17;
            double var45 = var43 / var49;
            double var39 = var37 * var45;
            Vector var34 = var33.multiply(var39);
            Location var19 = var24.add(var34);
            World var27 = var19.getWorld();
            Particle var35 = Particle.SONIC_BOOM;
            Particles.spawn(var27, var35, var19, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
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

    public static enum Variant {
        LOW(12, 0.4, 0.3, 0.22, 8, 45, 12),
        MID(40, 0.6, 0.5, 0.15, 12, 30, 30),
        BEAM(70, 0.5, 0.0, 0.0, 0, 0, 70),
        ULTRA(120, 0.4, 0.0, 0.0, 0, 0, 120);

        final int rings;
        final double forwardStep;
        final double radiusStart;
        final double radiusDelta;
        final int slicesPerRing;
        final int stepDegrees;
        final int range;

        private Variant(int nullxx, double nullxxx, double nullxxxx, double nullxxxxx, int nullxxxxxx, int nullxxxxxxx, int nullxxxxxxxx) {
            this.rings = nullxx;
            this.forwardStep = nullxxx;
            this.radiusStart = nullxxxx;
            this.radiusDelta = nullxxxxx;
            this.slicesPerRing = nullxxxxxx;
            this.stepDegrees = nullxxxxxxx;
            this.range = nullxxxxxxxx;
        }

        public int range() {
            return this.range;
        }

        public double hitRadius() {
            FluxBeamRenderer.Variant var2 = LOW;
            if (this != var2) {
                FluxBeamRenderer.Variant var6 = MID;
                if (this != var6) {
                    return 5.0;
                }
            }

            return 3.0;
        }
    }
}
