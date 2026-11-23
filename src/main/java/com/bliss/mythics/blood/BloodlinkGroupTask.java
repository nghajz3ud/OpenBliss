package com.bliss.mythics.blood;

import com.bliss.core.Particles;
import com.bliss.core.TrustStore;
import java.util.List;
import java.util.UUID;
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
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class BloodlinkGroupTask {
    private static final int CHANT_PERIOD = 20;
    private static final int CHANT_BEATS = 10;
    private static final int JET_DELAY = 20;
    private static final int JET_PERIOD = 2;
    private static final int JET_BEATS = 90;
    private static final double JET_COLUMN_STEP = 0.38;
    private static final double JET_MAX_HEIGHT = 15.0;
    private static final double JET_VASE_MID = 7.5;
    private static final double JET_BASE_RADIUS = 0.45;
    private static final double JET_BELLY_RADIUS = 1.5;
    private static final double JET_TWIST_PER_BLOCK = Math.toRadians(100.0);
    private static final int WIRE_FLAMES = 1;
    private static final int FLOW_FLAMES = 2;
    private static final double FLOW_SPREAD = 0.12;
    private static final double FLOW_SPACING = 3.4;
    private static final double FLOW_WIDTH = 1.3;
    private static final double FLOW_SPEED = 0.78;
    private static final int CORE_LIFETIME = 200;
    private static final int ORB_EVERY = 10;
    private static final double ORB_RADIUS = 0.75;
    private static final double ORB_LIFT = 0.75;
    private static final int ORB_POLAR = 15;
    private static final int ORB_AZIMUTH = 8;
    private static final double ORB_SPIN = Math.toRadians(15.0);
    private static final double TETHER_STEP = 0.3;
    private static final float TETHER_DUST = 1.8F;
    private static final int CROWN_PERIOD = 2;
    private static final int CROWN_BEATS = 90;
    private static final int CROWN_RINGS = 5;
    private static final int RING_SPACING = 18;
    private static final int RING_LIFETIME = 40;
    private static final double RING_MAX_RADIUS = 12.0;
    private static final double CROWN_HEIGHT = 15.5;
    private static final int RING_PETALS = 5;
    private static final double RING_PETAL_WOBBLE = 1.8;
    private static final double RING_WAVE_AMP = 1.5;
    private static final double RING_WAVE_PHASE = Math.toRadians(45.0);
    private static final float RING_DUST = 1.5F;
    private final Plugin plugin;
    private final MythicBloodState state;
    private final TrustStore trust;
    private final MythicBloodState.Fountain fountain;
    private final Location center;

    public BloodlinkGroupTask(Plugin var1, MythicBloodState var2, TrustStore var3, MythicBloodState.Fountain var4, int var5) {
        this.plugin = var1;
        this.state = var2;
        this.trust = var3;
        this.fountain = var4;
        Location var18 = var4.center.clone();
        this.center = var18;
    }

    public void start(int var1) {
        this.tollChant(1706083725);
        this.spillSoulJets(1358953990);
        this.spinBloodOrb(1683502079);
        this.rippleCrown(2022631149);
        long var25 = this.fountain.untilMs;
        long var7 = System.currentTimeMillis();
        long var29 = (var25 - var7) / 50L;
        long var9 = Math.max(1L, var29);
        BukkitScheduler var21 = this.plugin.getServer().getScheduler();
        Plugin var23 = this.plugin;
        Runnable var32 = this::teardown;
        var21.runTaskLater(var23, var32, var9);
    }

    private void tollChant(int var1) {
        BukkitRunnable var2 = new BukkitRunnable() {
            int beat = 0;

            public void run() {
                int var11 = this.beat;
                int var19 = var11 + 1;
                this.beat = var19;
                if (var11 >= 10) {
                    this.cancel();
                } else {
                    World var3 = BloodlinkGroupTask.this.center.getWorld();
                    Location var15 = BloodlinkGroupTask.this.center;
                    Sound var20 = Sound.ENTITY_GHAST_WARN;
                    var3.playSound(var15, var20, 1.0F, 1.1F);
                    Location var18 = BloodlinkGroupTask.this.center;
                    Sound var21 = Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE;
                    var3.playSound(var18, var21, 1.0F, 1.3F);
                }
            }
        };
        Plugin var8 = this.plugin;
        var2.runTaskTimer(var8, 0L, 20L);
    }

    private void spillSoulJets(int var1) {
        BukkitRunnable var2 = new BukkitRunnable() {
            int beat = 0;

            public void run() {
                if (this.beat >= 90) {
                    this.cancel();
                } else {
                    World var24 = BloodlinkGroupTask.this.center.getWorld();
                    double var120 = (double)this.beat;
                    double var25 = Math.min(15.0, var120);
                    double var27 = (double)this.beat * 0.78;

                    for (double var29 = 0.0; var29 <= var25; var29 += 0.38) {
                        double var31 = BloodlinkGroupTask.vaseRadius(var29, 81447850);
                        double var71 = BloodlinkGroupTask.JET_TWIST_PER_BLOCK;
                        double var33 = var71 * var29;
                        boolean var106;
                        if (((var29 - var27) % 3.4 + 3.4) % 3.4 < 1.3) {
                            var106 = true;
                        } else {
                            var106 = false;
                        }

                        byte var89;
                        if (var106) {
                            var89 = 2;
                        } else {
                            var89 = 1;
                        }

                        double var110 = (double)var89;
                        double var153 = var31 / 0.45;
                        int var116 = (int)Math.round(var110 * var153);
                        int var39 = Math.max(1, var116);
                        double var93;
                        if (var106) {
                            var93 = 0.12;
                        } else {
                            var93 = 0.0;
                        }

                        double var40 = var93;

                        for (int var42 = 0; var42 < 2; var42++) {
                            double var142 = (double)var42 * Math.PI;
                            double var43 = var33 + var142;
                            Particle var118 = Particle.SOUL_FIRE_FLAME;
                            double var147 = BloodlinkGroupTask.this.center.getX();
                            double var157 = Math.cos(var43);
                            double var159 = var157 * var31;
                            double var149 = var147 + var159;
                            double var164 = BloodlinkGroupTask.this.center.getY();
                            double var166 = var164 + var29;
                            double var173 = BloodlinkGroupTask.this.center.getZ();
                            double var177 = Math.sin(var43);
                            double var179 = var177 * var31;
                            double var175 = var173 + var179;
                            Particles.spawn(var24, var118, var149, var166, var175, var39, var40, var40, var40, 0.0, 1717927553);
                        }
                    }

                    int var108 = this.beat + 1;
                    this.beat = var108;
                }
            }
        };
        Plugin var8 = this.plugin;
        var2.runTaskTimer(var8, 20L, 2L);
    }

    private static double vaseRadius(double var0, int var2) {
        double var30 = Math.min(15.0, var0);
        double var11 = Math.max(0.0, var30);
        if (var11 <= 7.5) {
            double var50 = var11 / 7.5;
            double var40 = 1.05 * var50;
            return 0.45 + var40;
        } else {
            double var46 = (var11 - 7.5) / 7.5;
            double var36 = 1.05 * var46;
            return 1.5 - var36;
        }
    }

    private void spinBloodOrb(int var1) {
        final Location var12 = this.center.clone().add(0.0, 0.75, 0.0);
        BukkitRunnable var16 = new BukkitRunnable() {
            int tick = 0;

            public void run() {
                if (this.tick >= 200) {
                    this.cancel();
                } else {
                    BloodlinkGroupTask.this.drawTethers(296945067);
                    if (this.tick % 10 == 0) {
                        BloodlinkGroupTask var12x = BloodlinkGroupTask.this;
                        Location var17 = var12;
                        int var21 = this.tick / 10;
                        var12x.drawOrb(var17, var21, 488701287);
                    }

                    int var19 = this.tick + 1;
                    this.tick = var19;
                }
            }
        };
        Plugin var19 = this.plugin;
        var16.runTaskTimer(var19, 0L, 1L);
    }

    private void drawOrb(Location var1, int var2, int var3) {
        World var23 = var1.getWorld();
        double var44 = (double)var2;
        double var6 = ORB_SPIN;
        double var24 = var44 * var6;

        for (int var26 = 0; var26 < 15; var26++) {
            double var94 = (double)var26;
            double var27 = Math.PI * var94 / 14.0;
            double var29 = Math.sin(var27);
            double var31 = Math.cos(var27);

            for (int var33 = 0; var33 < 8; var33++) {
                double var99 = (double)var33;
                double var70 = (Math.PI * 2) * var99 / 8.0;
                double var34 = var70 + var24;
                double var76 = 0.75 * var29;
                double var109 = Math.cos(var34);
                double var36 = var76 * var109;
                double var82 = 0.75 * var29;
                double var115 = Math.sin(var34);
                double var38 = var82 * var115;
                double var40 = 0.75 * var31;
                Particle var92 = Particle.BLOCK;
                double var120 = var1.getX();
                double var122 = var120 + var36;
                double var125 = var1.getY();
                double var127 = var125 + var40;
                double var130 = var1.getZ();
                double var132 = var130 + var38;
                BlockData var135 = Material.REDSTONE_BLOCK.createBlockData();
                Particles.spawn(var23, var92, var122, var127, var132, 1, 0.0, 0.0, 0.0, 0.0, var135, 2077675459);
            }
        }
    }

    private void drawTethers(int var1) {
        World var26 = this.center.getWorld();
        if (var26 != null) {
            Location var27 = this.center.clone().add(0.0, 0.75, 0.0);

            for (Player var29 : var26.getPlayers()) {
                UUID var58 = var29.getUniqueId();
                UUID var115 = this.fountain.casterId;
                if (!var58.equals(var115) && !var29.isDead()) {
                    MythicBloodState.Fountain var63 = this.fountain;
                    Location var117 = var29.getLocation();
                    if (var63.contains(var117, 1663940311)) {
                        TrustStore var66 = this.trust;
                        UUID var120 = this.fountain.casterId;
                        UUID var131 = var29.getUniqueId();
                        if (!var66.isTrusted(var120, var131, 1736364606)) {
                            Vector var72 = var29.getLocation().add(0.0, 1.0, 0.0).toVector();
                            Vector var124 = var27.toVector();
                            Vector var31 = var72.subtract(var124);
                            double var32 = var31.length();
                            if (!(var32 < 0.001)) {
                                if (var32 > 24.0) {
                                    var32 = 24.0;
                                }

                                double var162 = var31.length();
                                double var127 = 1.0 / var162;
                                var31.multiply(var127);

                                for (double var34 = 0.0; var34 < var32; var34 += 0.3) {
                                    double var93 = var27.getX();
                                    double var139 = var31.getX();
                                    double var141 = var139 * var34;
                                    double var36 = var93 + var141;
                                    double var98 = var27.getY();
                                    double var144 = var31.getY();
                                    double var146 = var144 * var34;
                                    double var38 = var98 + var146;
                                    double var103 = var27.getZ();
                                    double var149 = var31.getZ();
                                    double var151 = var149 * var34;
                                    double var40 = var103 + var151;
                                    Particle var129 = Particle.DUST;
                                    Color var24 = Color.RED;
                                    DustOptions var23 = new DustOptions(var24, 1.8F);
                                    Particles.spawn(var26, var129, var36, var38, var40, 1, 0.0, 0.0, 0.0, 0.0, var23, 2077675459);
                                    Particle var130 = Particle.BLOCK;
                                    BlockData var186 = Material.REDSTONE_BLOCK.createBlockData();
                                    Particles.spawn(var26, var130, var36, var38, var40, 1, 0.0, 0.0, 0.0, 0.0, var186, 2077675459);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void rippleCrown(int var1) {
        BukkitRunnable var2 = new BukkitRunnable() {
            int beat = 0;

            public void run() {
                if (this.beat >= 90) {
                    this.cancel();
                } else {
                    World var6 = BloodlinkGroupTask.this.center.getWorld();

                    for (int var7 = 0; var7 < 5; var7++) {
                        int var8 = var7 * 18;
                        int var20 = this.beat;
                        int var9 = var20 - var8;
                        if (var9 >= 0 && var9 < 40) {
                            BloodlinkGroupTask var25 = BloodlinkGroupTask.this;
                            double var40 = (double)var9;
                            double var37 = 12.0 * var40 / 40.0;
                            var25.drawRing(var6, var37, 688466555);
                        }
                    }

                    int var34 = this.beat + 1;
                    this.beat = var34;
                }
            }
        };
        Plugin var8 = this.plugin;
        var2.runTaskTimer(var8, 0L, 2L);
    }

    private void drawRing(World var1, double var2, int var4) {
        double var25 = this.center.getY() + 15.5;

        for (byte var27 = 0; var27 < 360; var27 += 5) {
            double var28 = Math.toRadians((double)var27);
            double var95 = Math.sin(var28 * 5.0) * 1.8;
            double var30 = var2 + var95;
            double var58 = var28 * 5.0;
            double var99 = RING_WAVE_PHASE;
            double var32 = Math.sin(var58 + var99) * 1.5;
            double var68 = this.center.getX();
            double var105 = Math.cos(var28);
            double var107 = var105 * var30;
            double var34 = var68 + var107;
            double var74 = this.center.getZ();
            double var111 = Math.sin(var28);
            double var113 = var111 * var30;
            double var36 = var74 + var113;
            double var38 = var25 + var32;
            Particle var86 = Particle.DUST;
            Color var23 = Color.RED;
            DustOptions var22 = new DustOptions(var23, 1.5F);
            Particles.spawn(var1, var86, var34, var38, var36, 1, 0.0, 0.0, 0.0, 0.0, var22, 2077675459);
            if (var27 % 10 == 0) {
                Particle var88 = Particle.DUST;
                Color var143 = Color.BLACK;
                DustOptions var142 = new DustOptions(var143, 1.5F);
                Particles.spawn(var1, var88, var34, var38, var36, 1, 0.0, 0.0, 0.0, 0.0, var142, 2077675459);
            }
        }
    }

    private void teardown() {
        List var4 = this.state.fountains;
        MythicBloodState.Fountain var6 = this.fountain;
        var4.remove(var6);
    }
}
