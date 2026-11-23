package com.bliss.mythics.juggernaut;

import com.bliss.core.Particles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class SummoningCinematic extends BukkitRunnable {
    private static final int DURATION = 400;
    private static final int FOUNTAIN_DELAY = 140;
    private static final double BEHIND = 0.5;
    private static final double LIFT = 0.6;
    private static final double SCALE_RAD = 0.6;
    private static final double SCALE_LEN = 0.22;
    private static final double SCALE_LEN_V = 0.4;
    private static final double JET_STEP = 0.35;
    private static final double JET_MAX_H = 10.0;
    private static final double JET_MID = 5.0;
    private static final double JET_BASE_R = 0.3;
    private static final double JET_BELLY_R = 1.0;
    private static final double JET_TWIST = Math.toRadians(100.0);
    private static final double ORB_R = 0.55;
    private static final double ORB_LIFT = 0.5;
    private static final int ORB_POLAR = 16;
    private static final int ORB_AZ = 10;
    private static final int RING_SPACING = 14;
    private static final int RING_LIFE = 40;
    private static final double RING_MAX_R = 8.0;
    private static final double CROWN_H = 10.5;
    private static final double RING_PETALS = 5.0;
    private static final double RING_WOBBLE = 1.2;
    private static final double RING_WAVE = 1.0;
    private static final double RING_WAVE_PHASE = Math.toRadians(45.0);
    private static final double[][] RING_U;
    private static final double[][] RING_V;
    private static final double[] RING_RU;
    private static final double[] RING_RV;
    private static final int[] RING_DIR;
    private static final double RING_VISIBLE = Math.PI * 3.0 / 5.0;
    private static final int RING_SAMPLES = 28;
    private static final int STREAK_MAX = 12;
    private static final int STREAK_TAIL = 6;
    private static final Color GOLD = Color.fromRGB(255, 200, 60);
    private static final Sound[] WARDEN;
    private static final Sound[] DRAGON;
    private final Plugin plugin;
    private final Player player;
    private final Location anchorLoc;
    private final boolean vertical;
    private final boolean auratus;
    private final double orbSpin;
    private final double ringSpeed;
    private final double streakSpeed;
    private final double graceSwirl;
    private final double jetChance;
    private final int streakLife;
    private final int streakEvery;
    private Vector fwd;
    private int t = 0;
    private int soundIdx = 0;
    private final List<double[]> streaks;

    public SummoningCinematic(Plugin var1, Player var2, boolean var3, int var4) {
        this(var1, var2, null, false, var3, 1877222756);
    }

    public SummoningCinematic(Plugin var1, Location var2, boolean var3, boolean var4, int var5) {
        this(var1, null, var2, var3, var4, 1877222756);
    }

    private SummoningCinematic(Plugin var1, Player var2, Location var3, boolean var4, boolean var5, int var6) {
        ArrayList var25 = new ArrayList();
        this.streaks = var25;
        this.plugin = var1;
        this.player = var2;
        Location var30;
        if (var3 == null) {
            var30 = null;
        } else {
            var30 = var3.clone();
        }

        this.anchorLoc = var30;
        this.vertical = var4;
        this.auratus = var5;
        double var50;
        if (var5) {
            var50 = 8.0;
        } else {
            var50 = 15.0;
        }

        double var34 = Math.toRadians(var50);
        this.orbSpin = var34;
        double var48;
        if (var5) {
            var48 = 0.05;
        } else {
            var48 = 0.09;
        }

        this.ringSpeed = var48;
        double var38;
        if (var5) {
            var38 = 0.14;
        } else {
            var38 = 0.22;
        }

        this.streakSpeed = var38;
        double var52;
        if (var5) {
            var52 = 0.1;
        } else {
            var52 = 0.0;
        }

        this.graceSwirl = var52;
        double var46;
        if (var5) {
            var46 = 0.62;
        } else {
            var46 = 0.8;
        }

        this.jetChance = var46;
        byte var43;
        if (var5) {
            var43 = 34;
        } else {
            var43 = 22;
        }

        this.streakLife = var43;
        byte var45;
        if (var5) {
            var45 = 5;
        } else {
            var45 = 4;
        }

        this.streakEvery = var45;
    }

    public void start(int var1) {
        if (this.anchorLoc != null) {
            Vector var3 = new Vector(0, 0, 1);
            this.fwd = var3;
        } else {
            Vector var7 = this.player.getLocation().getDirection().setY(0);
            Vector var26;
            if (var7.lengthSquared() < 1.0E-6) {
                var26 = new Vector(0, 0, 1);
            } else {
                var26 = var7.normalize();
            }

            this.fwd = var26;
        }

        Plugin var19 = this.plugin;
        this.runTaskTimer(var19, 0L, 1L);
    }

    public void run() {
        if (this.t >= 400) {
            this.cancel();
        } else {
            World var130;
            Location var131;
            if (this.anchorLoc != null) {
                var130 = this.anchorLoc.getWorld();
                if (var130 == null) {
                    this.cancel();
                    return;
                }

                var131 = this.anchorLoc;
            } else {
                if (!this.player.isOnline()) {
                    this.cancel();
                    return;
                }

                var130 = this.player.getWorld();
                var131 = this.player.getLocation();
            }

            Vector var11 = this.fwd.clone().multiply(-1);
            Vector var33 = new Vector(0, 1, 0);
            Vector var36 = this.fwd.clone();
            Vector var13 = var36.crossProduct(var33).normalize();
            Location var70;
            if (this.vertical) {
                var70 = var131.clone().add(0.0, 0.6, 0.0);
            } else {
                Location var60 = var131.clone();
                Vector var89 = var11.clone().multiply(0.5);
                var70 = var60.add(var89).add(0.0, 0.6, 0.0);
            }

            this.sounds(var130, var70, 44951345);
            Location var95 = var131.clone().add(0.0, 1.0, 0.0);
            this.rings(var130, var95, 604431281);
            this.streaks(var130, var70, 274500336);
            if (this.t >= 140) {
                int var15 = this.t - 140;
                if (var15 % 2 == 0) {
                    int var122 = var15 / 2;
                    this.jets(var130, var70, var11, var13, var33, var122, 1874825748);
                    int var124 = var15 / 2;
                    this.crown(var130, var70, var11, var13, var33, var124, 2078495689);
                }

                if (var15 % 6 == 0) {
                    int var126 = var15 / 6;
                    this.orb(var130, var70, var11, var13, var33, var126, 878368187);
                }
            }

            int var86 = this.t + 1;
            this.t = var86;
        }
    }

    private void sounds(World var1, Location var2, int var3) {
        if (this.t == 0) {
            Sound var37;
            if (this.auratus) {
                var37 = Sound.ENTITY_ENDER_DRAGON_FLAP;
            } else {
                var37 = Sound.ENTITY_WARDEN_HEARTBEAT;
            }

            float var51;
            if (this.auratus) {
                var51 = 1.0F;
            } else {
                var51 = 0.9F;
            }

            var1.playSound(var2, var37, 1.0F, var51);
        }

        if (this.t == 140) {
            Sound var38;
            if (this.auratus) {
                var38 = Sound.ENTITY_ENDER_DRAGON_GROWL;
            } else {
                var38 = Sound.ENTITY_WARDEN_EMERGE;
            }

            float var50;
            if (this.auratus) {
                var50 = 1.0F;
            } else {
                var50 = 0.85F;
            }

            var1.playSound(var2, var38, 1.6F, var50);
        }

        byte var26;
        if (this.auratus) {
            var26 = 55;
        } else {
            var26 = 45;
        }

        if (this.t > 0) {
            int var21 = this.t;
            if (var21 % var26 == 0) {
                Sound[] var27;
                if (this.auratus) {
                    var27 = DRAGON;
                } else {
                    var27 = WARDEN;
                }

                int var46 = this.soundIdx;
                int var52 = var46 + 1;
                this.soundIdx = var52;
                int var48 = var27.length;
                int var41 = var46 % var48;
                Sound var36 = var27[var41];
                var1.playSound(var2, var36, 1.1F, 1.0F);
            }
        }
    }

    private Location local(Location var1, Vector var2, Vector var3, Vector var4, double var5, double var7, double var9, int var11) {
        if (this.vertical) {
            Location var25 = var1.clone();
            Vector var38 = var3.clone();
            double var58 = var5 * 0.6;
            Vector var39 = var38.multiply(var58);
            Location var26 = var25.add(var39);
            Vector var41 = var4.clone();
            double var62 = var7 * 0.4;
            Vector var42 = var41.multiply(var62);
            Location var27 = var26.add(var42);
            Vector var44 = var2.clone();
            double var66 = var9 * 0.6;
            Vector var45 = var44.multiply(var66);
            return var27.add(var45);
        } else {
            Location var20 = var1.clone();
            Vector var29 = var3.clone();
            double var46 = var5 * 0.6;
            Vector var30 = var29.multiply(var46);
            Location var21 = var20.add(var30);
            Vector var32 = var2.clone();
            double var50 = var7 * 0.22;
            Vector var33 = var32.multiply(var50);
            Location var22 = var21.add(var33);
            Vector var35 = var4.clone();
            double var54 = var9 * 0.6;
            Vector var36 = var35.multiply(var54);
            return var22.add(var36);
        }
    }

    private void jets(World var1, Location var2, Vector var3, Vector var4, Vector var5, int var6, int var7) {
        double var79 = (double)var6;
        double var29 = Math.min(10.0, var79);
        ThreadLocalRandom var31 = ThreadLocalRandom.current();

        for (double var32 = 0.0; var32 <= var29; var32 += 0.35) {
            double var34 = vase(var32, 311199837);
            double var54 = JET_TWIST;
            double var36 = var54 * var32;

            for (int var38 = 0; var38 < 2; var38++) {
                double var61 = var31.nextDouble();
                double var86 = this.jetChance;
                if (!(var61 > var86)) {
                    double var91 = (double)var38 * Math.PI;
                    double var39 = var36 + var91;
                    double var101 = Math.cos(var39);
                    double var103 = var101 * var34;
                    double var107 = Math.sin(var39);
                    double var109 = var107 * var34;
                    Location var41 = this.local(var2, var3, var4, var5, var103, var32, var109, 1623786036);
                    Particle var78;
                    if (this.auratus) {
                        var78 = Particle.END_ROD;
                    } else {
                        var78 = Particle.SOUL_FIRE_FLAME;
                    }

                    Particles.spawn(var1, var78, var41, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                }
            }
        }
    }

    private static double vase(double var0, int var2) {
        double var28 = Math.min(10.0, var0);
        double var11 = Math.max(0.0, var28);
        double var26;
        if (var11 <= 5.0) {
            double var42 = var11 / 5.0;
            double var34 = 0.7 * var42;
            var26 = 0.3 + var34;
        } else {
            double var48 = (var11 - 5.0) / 5.0;
            double var38 = 0.7 * var48;
            var26 = 1.0 - var38;
        }

        return var26;
    }

    private void orb(World var1, Location var2, Vector var3, Vector var4, Vector var5, int var6, int var7) {
        double var46 = (double)var6;
        double var98 = this.orbSpin;
        double var28 = var46 * var98;
        Material var94;
        if (this.auratus) {
            var94 = Material.GOLD_BLOCK;
        } else {
            var94 = Material.REDSTONE_BLOCK;
        }

        BlockData var30 = var94.createBlockData();

        for (int var31 = 0; var31 < 16; var31++) {
            double var101 = (double)var31;
            double var32 = Math.PI * var101 / 15.0;
            double var34 = Math.sin(var32);
            double var36 = Math.cos(var32);

            for (int var38 = 0; var38 < 10; var38++) {
                double var106 = (double)var38;
                double var75 = (Math.PI * 2) * var106 / 10.0;
                double var39 = var75 + var28;
                double var81 = 0.55 * var34;
                double var116 = Math.cos(var39);
                double var41 = var81 * var116;
                double var87 = 0.55 * var34;
                double var122 = Math.sin(var39);
                double var43 = var87 * var122;
                double var131 = 0.55 * var36;
                double var129 = 0.5 + var131;
                Location var45 = this.local(var2, var3, var4, var5, var41, var129, var43, 1623786036);
                Particle var97 = Particle.BLOCK;
                Particles.spawn(var1, var97, var45, 1, 0.0, 0.0, 0.0, 0.0, var30, 1141219473);
            }
        }
    }

    private void crown(World var1, Location var2, Vector var3, Vector var4, Vector var5, int var6, int var7) {
        int var31 = var6 / 14;
        int var99 = (var6 - 40) / 14;

        for (int var33 = Math.max(0, var99); var33 <= var31; var33++) {
            int var102 = var33 * 14;
            int var34 = var6 - var102;
            if (var34 >= 0 && var34 < 40) {
                double var113 = (double)var34;
                double var35 = 8.0 * var113 / 40.0;

                for (byte var37 = 0; var37 < 360; var37 += 5) {
                    double var38 = Math.toRadians((double)var37);
                    double var123 = Math.sin(var38 * 5.0) * 1.2;
                    double var40 = var35 + var123;
                    double var75 = var38 * 5.0;
                    double var127 = RING_WAVE_PHASE;
                    double var42 = Math.sin(var75 + var127) * 1.0;
                    double var143 = Math.cos(var38);
                    double var145 = var143 * var40;
                    double var151 = 10.5 + var42;
                    double var157 = Math.sin(var38);
                    double var159 = var157 * var40;
                    Location var44 = this.local(var2, var3, var4, var5, var145, var151, var159, 1623786036);
                    Color var96;
                    if (this.auratus) {
                        var96 = GOLD;
                    } else {
                        var96 = Color.RED;
                    }

                    Particle var106 = Particle.DUST;
                    DustOptions var28 = new DustOptions(var96, 1.4F);
                    Particles.spawn(var1, var106, var44, 1, 0.0, 0.0, 0.0, 0.0, var28, true, 1748031581);
                    if (var37 % 10 == 0) {
                        Color var95;
                        if (this.auratus) {
                            if (var37 % 20 == 0) {
                                var95 = Color.BLACK;
                            } else {
                                var95 = Color.WHITE;
                            }
                        } else {
                            var95 = Color.BLACK;
                        }

                        Particle var109 = Particle.DUST;
                        DustOptions var163 = new DustOptions(var95, 1.4F);
                        Particles.spawn(var1, var109, var44, 1, 0.0, 0.0, 0.0, 0.0, var163, true, 1748031581);
                    }
                }
            }
        }
    }

    private void rings(World var1, Location var2, int var3) {
        Color var120;
        if (this.auratus) {
            var120 = Color.WHITE;
        } else {
            var120 = Color.fromRGB(255, 40, 40);
        }

        Color var26 = var120;
        Color var54;
        if (this.auratus) {
            var54 = GOLD;
        } else {
            var54 = Color.fromRGB(70, 0, 0);
        }

        Color var27 = var54;
        int var28 = 0;

        while (true) {
            int var123 = RING_U.length;
            if (var28 >= var123) {
                return;
            }

            int[] var57 = RING_DIR;
            int var58 = var57[var28];
            int var126 = this.t;
            double var60 = (double)(var58 * var126);
            double var133 = this.ringSpeed;
            double var62 = var60 * var133;
            double var138 = (double)var28 * 2.1;
            double var29 = var62 + var138;
            double[][] var66 = RING_U;
            double[] var31 = var66[var28];
            double[][] var68 = RING_V;
            double[] var32 = var68[var28];

            for (int var33 = 0; var33 < 28; var33++) {
                double var34 = (double)var33 / 27.0;
                double var197 = 1.0 - var34;
                double var144 = (Math.PI * 3.0 / 5.0) * var197;
                double var36 = var29 - var144;
                double var83 = Math.cos(var36);
                double[] var146 = RING_RU;
                double var147 = var146[var28];
                double var38 = var83 * var147;
                double var89 = Math.sin(var36);
                double[] var149 = RING_RV;
                double var150 = var149[var28];
                double var40 = var89 * var150;
                double var94 = var2.getX();
                double var153 = var31[0];
                double var155 = var153 * var38;
                double var96 = var94 + var155;
                double var158 = var32[0];
                double var160 = var158 * var40;
                double var42 = var96 + var160;
                double var101 = var2.getY();
                double var163 = var31[1];
                double var165 = var163 * var38;
                double var103 = var101 + var165;
                double var168 = var32[1];
                double var170 = var168 * var40;
                double var44 = var103 + var170;
                double var108 = var2.getZ();
                double var173 = var31[2];
                double var175 = var173 * var38;
                double var110 = var108 + var175;
                double var178 = var32[2];
                double var180 = var178 * var40;
                double var46 = var110 + var180;
                double var184 = 0.9 * var34;
                float var48 = (float)(0.5 + var184);
                Particle var130 = Particle.DUST;
                Color var218 = blend(var27, var26, var34, 93207774);
                DustOptions var21 = new DustOptions(var218, var48);
                Particles.spawn(var1, var130, var42, var44, var46, 1, 0.0, 0.0, 0.0, 0.0, var21, true, 954482185);
            }

            var28++;
        }
    }

    private void streaks(World var1, Location var2, int var3) {
        ThreadLocalRandom var26 = ThreadLocalRandom.current();
        int var51 = this.t;
        int var175 = this.streakEvery;
        if (var51 % var175 == 0 && this.streaks.size() < 12) {
            double var27 = var26.nextDouble() * 2.0 * Math.PI;
            double var198 = var26.nextDouble();
            double var30 = Math.acos(2.0 * var198 - 1.0);
            double var73 = Math.sin(var30);
            double var204 = Math.cos(var27);
            double var32 = var73 * var204;
            double var34 = Math.cos(var30);
            double var83 = Math.sin(var30);
            double var208 = Math.sin(var27);
            double var37 = var83 * var208;
            List var88 = this.streaks;
            double[] var178 = new double[7];
            double var288 = var2.getX();
            var178[0] = var288;
            double var291 = var2.getY();
            var178[1] = var291;
            double var294 = var2.getZ();
            var178[2] = var294;
            double var340 = this.streakSpeed;
            double var298 = var32 * var340;
            var178[3] = var298;
            double var343 = this.streakSpeed;
            double var302 = var34 * var343;
            var178[4] = var302;
            double var346 = this.streakSpeed;
            double var306 = var37 * var346;
            var178[5] = var306;
            var178[6] = 0.0;
            var88.add(var178);
        }

        Color var174;
        if (this.auratus) {
            var174 = Color.WHITE;
        } else {
            var174 = Color.fromRGB(255, 40, 40);
        }

        Color var353 = var174;
        Color var95;
        if (this.auratus) {
            var95 = GOLD;
        } else {
            var95 = Color.fromRGB(90, 0, 0);
        }

        Color var29 = var95;
        double var354 = Math.cos(this.graceSwirl);
        double var356 = Math.sin(this.graceSwirl);
        Iterator var358 = this.streaks.iterator();

        while (var358.hasNext()) {
            double[] var36 = (double[])var358.next();
            if (this.graceSwirl != 0.0) {
                double var119 = var36[3];
                double var121 = var119 * var354;
                double var216 = var36[5];
                double var218 = var216 * var356;
                double var359 = var121 - var218;
                double var126 = var36[3];
                double var128 = var126 * var356;
                double var223 = var36[5];
                double var225 = var223 * var354;
                double var41 = var128 + var225;
                var36[3] = var359;
                var36[5] = var41;
            }

            double var231 = var36[0];
            double var315 = var36[3];
            double var233 = var231 + var315;
            var36[0] = var233;
            double var235 = var36[1];
            double var318 = var36[4];
            double var237 = var235 + var318;
            var36[1] = var237;
            double var239 = var36[2];
            double var321 = var36[5];
            double var241 = var239 + var321;
            var36[2] = var241;
            double var245 = var36[6] + 1.0;
            var36[6] = var245;
            double var139 = var36[6];
            double var249 = (double)this.streakLife;
            if (var139 > var249) {
                var358.remove();
            } else {
                for (byte var361 = 0; var361 < 6; var361 += 1) {
                    double var39 = (double)var361 / 6.0;
                    double var151 = var36[0];
                    double var254 = var36[3];
                    double var326 = (double)var361;
                    double var256 = var254 * var326;
                    double var43 = var151 - var256;
                    double var156 = var36[1];
                    double var259 = var36[4];
                    double var329 = (double)var361;
                    double var261 = var259 * var329;
                    double var45 = var156 - var261;
                    double var161 = var36[2];
                    double var264 = var36[5];
                    double var332 = (double)var361;
                    double var266 = var264 * var332;
                    double var47 = var161 - var266;
                    double var270 = 1.0 - var39;
                    float var49 = (float)(1.2 * var270 + 0.2);
                    Particle var193 = Particle.DUST;
                    Color var351 = blend(var353, var29, var39, 93207774);
                    DustOptions var21 = new DustOptions(var351, var49);
                    Particles.spawn(var1, var193, var43, var45, var47, 1, 0.0, 0.0, 0.0, 0.0, var21, true, 954482185);
                }
            }
        }
    }

    private static Color blend(Color var0, Color var1, double var2, int var4) {
        double var15 = (double)var0.getRed();
        int var45 = var1.getRed();
        int var66 = var0.getRed();
        double var47 = (double)(var45 - var66);
        double var49 = var47 * var2;
        int var11 = clamp((int)Math.round(var15 + var49), 258349094);
        double var25 = (double)var0.getGreen();
        int var52 = var1.getGreen();
        int var68 = var0.getGreen();
        double var54 = (double)(var52 - var68);
        double var56 = var54 * var2;
        int var12 = clamp((int)Math.round(var25 + var56), 258349094);
        double var35 = (double)var0.getBlue();
        int var59 = var1.getBlue();
        int var70 = var0.getBlue();
        double var61 = (double)(var59 - var70);
        double var63 = var61 * var2;
        int var13 = clamp((int)Math.round(var35 + var63), 258349094);
        return Color.fromRGB(var11, var12, var13);
    }

    private static int clamp(int var0, int var1) {
        int var6 = Math.min(255, var0);
        return Math.max(0, var6);
    }

    static {
        double[][] var14 = new double[3][];
        double[] var52 = new double[]{1.0, 0.0, 0.0};
        var14[0] = var52;
        double[] var54 = new double[]{0.0, 0.0, 1.0};
        var14[1] = var54;
        double[] var56 = new double[]{1.0, 0.0, 0.0};
        var14[2] = var56;
        RING_U = var14;
        double[][] var16 = new double[3][];
        double[] var58 = new double[]{0.0, 1.0, 0.0};
        var16[0] = var58;
        double[] var60 = new double[]{0.0, 1.0, 0.0};
        var16[1] = var60;
        double[] var62 = new double[]{0.0, 0.7071, 0.7071};
        var16[2] = var62;
        RING_V = var16;
        double[] var18 = new double[]{1.9, 2.4, 2.1};
        RING_RU = var18;
        double[] var20 = new double[]{2.6, 1.8, 2.8};
        RING_RV = var20;
        int[] var22 = new int[]{1, -1, 1};
        RING_DIR = var22;
        Sound[] var26 = new Sound[4];
        Sound var78 = Sound.ENTITY_WARDEN_NEARBY_CLOSEST;
        var26[0] = var78;
        Sound var79 = Sound.ENTITY_WARDEN_NEARBY_CLOSER;
        var26[1] = var79;
        Sound var80 = Sound.ENTITY_WARDEN_AMBIENT;
        var26[2] = var80;
        Sound var81 = Sound.ENTITY_WARDEN_ANGRY;
        var26[3] = var81;
        WARDEN = var26;
        Sound[] var28 = new Sound[3];
        Sound var82 = Sound.ENTITY_ENDER_DRAGON_GROWL;
        var28[0] = var82;
        Sound var83 = Sound.ENTITY_ENDER_DRAGON_AMBIENT;
        var28[1] = var83;
        Sound var84 = Sound.ENTITY_ENDER_DRAGON_FLAP;
        var28[2] = var84;
        DRAGON = var28;
    }
}
