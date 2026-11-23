package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import com.bliss.core.SignatureClock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.SoundGroup;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Item;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

final class GoldenDreamMass {
    static final int SILENCE_TICKS = 200;
    static final int MIX_TICKS = 200;
    static final int PULL_TICKS = 400;
    static final double LIFT = 4.0;
    private static final double ORB_START_R = 0.3;
    private static final double ORB_R = 2.94;
    private static final int MOTES_MIN = 60;
    private static final int MOTES_MAX = 200;
    private static final float MASS_DUST_MIN = 1.0F;
    private static final float MASS_DUST_MAX = 1.8F;
    private static final int MOTE_GRAINS_MAX = 2;
    private static final double MOTE_SPREAD_MAX = 0.25;
    private static final int ROD_EVERY = 4;
    private static final double ROD_BEYOND = 3.0;
    private static final double PULL_REACH = 40.0;
    private static final int PLAYER_PULL_DELAY = 120;
    private static final double SHIELD_R = 2.0;
    private static final int SHIELD_DEPTH = 6;
    private static final double PULL_SPEED_FAR = 0.15;
    private static final double PULL_SPEED_NEAR = 0.55;
    private static final double CONTACT_BEYOND = 0.5;
    private static final int BLOCKS_PER_TICK = 120;
    private static final int BLOCK_TRIES = 400;
    private static final int PEEL_DEPTH_MIN = 3;
    private static final int PEEL_DEPTH_MAX = 5;
    private static final int FLY_CAP = 600;
    private static final int CRUMBLE_EVERY = 3;
    private static final double BLOCK_SPEED0 = 0.12;
    private static final double BLOCK_ACCEL = 1.06;
    private static final double BLOCK_SPEED_MAX = 3.0;
    private static final double WELL_R = 7.0;
    private static final int WELL_DEPTH = 64;
    private static final int WELL_PER_TICK = 200;
    static final long EYE_DAY = 80019L;
    static final int EYE_CLOSE_TICKS = 30;
    private static final int EYE_HOLD_TICKS = 40;
    private final BlissPlugin plugin;
    private final GoldenDreamRitual owner;
    private final Location centre;
    private final UUID trigger;
    private final Vector[] orbitU;
    private final Vector[] orbitV;
    private final double[] motePhase;
    private final double[] moteSpeed;
    private final double[] moteR;
    private final List<GoldenDreamMass.Flying> flying;
    private final Map<Long, int[]> peeled;
    private final Map<UUID, GoldenDreamMass.Prev> clocks;
    private final Set<UUID> veiled;
    private final Map<UUID, Integer> eyeK;
    private final List<Location> shield;
    private BukkitTask task;
    private final Map<Long, Integer> wellRoof;
    private int crumbled;
    private static final int BREAK_SOUNDS_PER_TICK = 12;
    private int breakSoundTick;
    private int breakSoundsThisTick;

    GoldenDreamMass(BlissPlugin var1, GoldenDreamRitual var2, Location var3, UUID var4, int var5) {
        Vector[] var31 = new Vector[200];
        this.orbitU = var31;
        Vector[] var33 = new Vector[200];
        this.orbitV = var33;
        double[] var35 = new double[200];
        this.motePhase = var35;
        double[] var37 = new double[200];
        this.moteSpeed = var37;
        double[] var39 = new double[200];
        this.moteR = var39;
        ArrayList var40 = new ArrayList();
        this.flying = var40;
        HashMap var41 = new HashMap();
        this.peeled = var41;
        HashMap var42 = new HashMap();
        this.clocks = var42;
        HashSet var43 = new HashSet();
        this.veiled = var43;
        HashMap var44 = new HashMap();
        this.eyeK = var44;
        ArrayList var45 = new ArrayList();
        this.shield = var45;
        HashMap var46 = new HashMap();
        this.wellRoof = var46;
        this.crumbled = 0;
        this.breakSoundTick = -1;
        this.breakSoundsThisTick = 0;
        this.plugin = var1;
        this.owner = var2;
        Location var54 = var3.clone().add(0.0, 4.0, 0.0);
        this.centre = var54;
        this.trigger = var4;
    }

    private static double orbRadius(int var0, int var1) {
        if (var0 >= 200) {
            double var60 = Math.sin((double)var0 * 0.15);
            double var53 = 0.03 * var60;
            double var45 = 1.0 + var53;
            return 2.94 * var45;
        } else {
            double var12 = (double)var0 / 200.0;
            double var21 = var12 * var12;
            double var47 = 2.0 * var12;
            double var37 = 3.0 - var47;
            double var49 = var21 * var37;
            double var41 = 2.64 * var49;
            return 0.3 + var41;
        }
    }

    void schedule(long var1, int var3) {
        BukkitScheduler var11 = this.plugin.getServer().getScheduler();
        BlissPlugin var13 = this.plugin;
        Runnable var14 = this::start;
        var11.runTaskLater(var13, var14, var1);
    }

    private void start() {
        GoldenDreamRitual var12 = this.owner;
        UUID var58 = this.trigger;
        var12.entryStarted(var58, 971795224);
        if (!this.centre.isWorldLoaded()) {
            GoldenDreamRitual var57 = this.owner;
            UUID var75 = this.trigger;
            var57.finish(var75, 1863011177);
        } else {
            World var7 = this.centre.getWorld();
            GoldenDreamRitual var20 = this.owner;
            var20.register(this, 753478446);
            if (this.plugin.controlRoomVeil(785222279) != null) {
                this.plugin.controlRoomVeil(785222279).pause(640L, 1861141377);
            }

            ThreadLocalRandom var8 = ThreadLocalRandom.current();

            for (int var9 = 0; var9 < 200; var9++) {
                Vector var10 = randomUnit(var8, 438422787);
                Vector var33 = var10.clone();
                Vector var63 = new Vector(0, 1, 0);
                Vector var11 = var33.crossProduct(var63);
                if (var11.lengthSquared() < 1.0E-6) {
                    Vector var39 = new Vector(1, 0, 0);
                    var11 = var39;
                }

                var11.normalize();
                Vector[] var43 = this.orbitU;
                var43[var9] = var11;
                Vector[] var45 = this.orbitV;
                Vector var81 = var10.clone();
                Vector var83 = var81.crossProduct(var11).normalize();
                var45[var9] = var83;
                double[] var47 = this.motePhase;
                double var85 = var8.nextDouble(0.0, Math.PI * 2);
                var47[var9] = var85;
                double[] var49 = this.moteSpeed;
                double var88 = var8.nextDouble(0.15, 0.35);
                byte var122;
                if (var8.nextBoolean()) {
                    var122 = 1;
                } else {
                    var122 = -1;
                }

                double var116 = (double)var122;
                double var90 = var88 * var116;
                var49[var9] = var90;
                double[] var51 = this.moteR;
                double var137 = Math.cbrt(var8.nextDouble());
                double var120 = 0.85 * var137;
                double var94 = 0.15 + var120;
                var51[var9] = var94;
            }

            Logger var54 = this.plugin.getLogger();
            String var70 = var7.getName();
            long var100 = Math.round(this.centre.getX());
            long var127 = Math.round(this.centre.getY());
            long var143 = Math.round(this.centre.getZ());
            String var71 = "Golden Dream: the mass forms at " + var70 + " " + var100 + "," + var127 + "," + var143;
            var54.info(var71);
            BukkitRunnable var72 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    if (!GoldenDreamMass.this.centre.isWorldLoaded()) {
                        GoldenDreamMass.this.end(false, 1264529697);
                        this.cancel();
                    } else {
                        World var8 = GoldenDreamMass.this.centre.getWorld();
                        double var9 = GoldenDreamMass.orbRadius(this.t, 1026034127);
                        GoldenDreamMass var28 = GoldenDreamMass.this;
                        int var93 = this.t;
                        var28.drawMass(var8, var93, var9, 1871763905);
                        if (this.t >= 200) {
                            GoldenDreamMass var32 = GoldenDreamMass.this;
                            int var102 = this.t - 200;
                            var32.pullEntities(var8, var9, var102, 273016466);
                            GoldenDreamMass.this.shield.clear();
                            if (this.t - 200 < 120) {
                                for (Player var12 : var8.getPlayers()) {
                                    Map var49 = GoldenDreamMass.this.eyeK;
                                    UUID var80 = var12.getUniqueId();
                                    if (!var49.containsKey(var80)) {
                                        List var53 = GoldenDreamMass.this.shield;
                                        Location var82 = var12.getLocation();
                                        var53.add(var82);
                                    }
                                }
                            }

                            GoldenDreamMass var56 = GoldenDreamMass.this;
                            var56.digWell(var8, 1572931454);
                            GoldenDreamMass var58 = GoldenDreamMass.this;
                            var58.ripBlocks(var8, var9, 27477908);
                            if (this.t % 2 == 0) {
                                GoldenDreamMass var63 = GoldenDreamMass.this;
                                var63.flyBlocks(var8, var9, 1088861030);
                            }

                            GoldenDreamMass var65 = GoldenDreamMass.this;
                            var65.tickVeils(var9, 1731296659);
                        }

                        GoldenDreamMass.this.tickEyes(442729289);
                        if (this.t >= 600) {
                            GoldenDreamMass.this.end(true, 1264529697);
                            this.cancel();
                        } else {
                            int var91 = this.t + 1;
                            this.t = var91;
                        }
                    }
                }
            };
            BlissPlugin var103 = this.plugin;
            BukkitTask var73 = var72.runTaskTimer(var103, 0L, 1L);
            this.task = var73;
        }
    }

    private void drawMass(World var1, int var2, double var3, int var5) {
        double var134 = var3 / 2.94;
        double var34 = Math.min(1.0, var134);
        double var110 = 140.0 * var34;
        int var116 = (int)Math.round(var110 * var34);
        int var36 = 60 + var116;
        float var138 = (float)var34;
        float var118 = 0.79999995F * var138;
        float var37 = 1.0F + var118;
        int var125 = (int)Math.round(1.0 * var34);
        int var38 = 1 + var125;
        double var39 = 0.25 * var34;
        Color var141 = FragmentCoreRitual.HOLDER_GOLD;
        DustOptions var65 = new DustOptions(var141, var37);
        DustOptions var41 = var65;

        for (int var42 = 0; var42 < var36; var42++) {
            double[] var69 = this.motePhase;
            double var70 = var69[var42];
            double var143 = (double)var2;
            double[] var202 = this.moteSpeed;
            double var203 = var202[var42];
            double var145 = var143 * var203;
            double var43 = var70 + var145;
            double[] var148 = this.moteR;
            double var149 = var148[var42];
            double var45 = var3 * var149;
            double var80 = this.centre.getX();
            Vector[] var152 = this.orbitU;
            double var154 = var152[var42].getX();
            double var207 = Math.cos(var43);
            double var156 = var154 * var207;
            Vector[] var210 = this.orbitV;
            double var212 = var210[var42].getX();
            double var254 = Math.sin(var43);
            double var214 = var212 * var254;
            double var158 = var156 + var214;
            double var160 = var158 * var45;
            double var47 = var80 + var160;
            double var86 = this.centre.getY();
            Vector[] var163 = this.orbitU;
            double var165 = var163[var42].getY();
            double var220 = Math.cos(var43);
            double var167 = var165 * var220;
            Vector[] var223 = this.orbitV;
            double var225 = var223[var42].getY();
            double var258 = Math.sin(var43);
            double var227 = var225 * var258;
            double var169 = var167 + var227;
            double var171 = var169 * var45;
            double var49 = var86 + var171;
            double var92 = this.centre.getZ();
            Vector[] var174 = this.orbitU;
            double var176 = var174[var42].getZ();
            double var233 = Math.cos(var43);
            double var178 = var176 * var233;
            Vector[] var236 = this.orbitV;
            double var238 = var236[var42].getZ();
            double var262 = Math.sin(var43);
            double var240 = var238 * var262;
            double var180 = var178 + var240;
            double var182 = var180 * var45;
            double var51 = var92 + var182;
            Particle var128 = Particle.DUST;
            Particles.spawn(var1, var128, var47, var49, var51, var38, var39, var39, var39, 0.0, var41, true, 954482185);
        }

        if (var2 % 4 == 0) {
            Vector var280 = randomUnit(ThreadLocalRandom.current(), 438422787);
            double var281 = var3 + 3.0;
            Location var107 = this.centre.clone();
            Vector var131 = var280.clone();
            Vector var132 = var131.multiply(var281);
            Location var283 = var107.add(var132);
            Particle var133 = Particle.END_ROD;
            double var249 = -var280.getX();
            double var269 = -var280.getY();
            double var274 = -var280.getZ();
            double var276 = var281 / 11.0;
            Particles.spawn(var1, var133, var283, 0, var249, var269, var274, var276, null, true, 1748031581);
        }
    }

    private void pullEntities(World var1, double var2, int var4, int var5) {
        double var17 = var2 + 0.5;
        Location var114 = this.centre;

        for (Entity var20 : var1.getNearbyEntities(var114, 40.0, 40.0, 40.0)) {
            if (!(var20 instanceof Display)
                && !(var20 instanceof Interaction)
                && !(var20 instanceof Marker)
                && !(var20 instanceof Hanging)
                && !(var20 instanceof ArmorStand)
                && !var20.hasMetadata("NPC")) {
                Location var21 = var20.getLocation();
                Location var117 = this.centre;
                double var22 = var21.distanceSquared(var117);
                if (var20 instanceof Player) {
                    Player var181 = (Player)var20;
                    GameMode var92 = var181.getGameMode();
                    GameMode var123 = GameMode.SPECTATOR;
                    if (var92 == var123) {
                        continue;
                    }

                    UUID var184 = var181.getUniqueId();
                    Map var96 = this.eyeK;
                    if (var96.containsKey(var184)) {
                        continue;
                    }

                    Map var99 = this.clocks;
                    long var171 = var181.getPlayerTimeOffset();
                    boolean var180 = var181.isPlayerTimeRelative();
                    GoldenDreamMass.Prev var151 = new GoldenDreamMass.Prev(var171, var180);
                    var99.putIfAbsent(var184, var151);
                    double var154 = var17 * var17;
                    if (var22 < var154) {
                        Set var105 = this.veiled;
                        var105.remove(var184);
                        Map var108 = this.eyeK;
                        Integer var157 = 0;
                        var108.put(var184, var157);
                        continue;
                    }

                    Set var111 = this.veiled;
                    var111.add(var184);
                    if (var4 < 120) {
                        continue;
                    }
                } else {
                    double var134 = var2 * var2;
                    if (var22 < var134) {
                        if (!(var20 instanceof Item) || !GoldGemItemGuardListener.isProtected(((Item)var20).getItemStack(), 1601196813)) {
                            this.eat(var1, var21, 1901983182);
                            var20.remove();
                        }
                        continue;
                    }
                }

                Vector var73 = this.centre.toVector();
                Vector var120 = var21.toVector();
                Vector var24 = var73.subtract(var120);
                double var182 = var24.length();
                if (!(var182 < 0.001) && !(var182 > 40.0)) {
                    double var162 = var182 / 40.0;
                    double var143 = Math.min(1.0, var162);
                    double var26 = 1.0 - var143;
                    double var166 = 0.4 * var26;
                    double var147 = 0.15 + var166;
                    double var149 = var147 / var182;
                    Vector var122 = var24.multiply(var149);
                    var20.setVelocity(var122);
                }
            }
        }
    }

    private void eat(World var1, Location var2, int var3) {
        Particle var5 = Particle.DUST;
        Color var17 = FragmentCoreRitual.HOLDER_GOLD;
        DustOptions var16 = new DustOptions(var17, 1.8F);
        Particles.spawn(var1, var5, var2, 8, 0.3, 0.3, 0.3, 0.0, var16, true, 1748031581);
    }

    private void ripBlocks(World var1, double var2, int var4) {
        ThreadLocalRandom var12 = ThreadLocalRandom.current();
        int var13 = this.centre.getBlockX();
        int var14 = this.centre.getBlockZ();
        int var15 = 0;

        for (int var16 = 0; var16 < 400 && var15 < 120; var16++) {
            double var17 = var12.nextDouble(0.0, Math.PI * 2);
            double var100 = Math.sqrt(var12.nextDouble());
            double var19 = 40.0 * var100;
            double var66 = Math.cos(var17);
            int var72 = (int)Math.round(var66 * var19);
            int var21 = var13 + var72;
            double var75 = Math.sin(var17);
            int var81 = (int)Math.round(var75 * var19);
            int var22 = var14 + var81;
            int var83 = var21 >> 4;
            int var104 = var22 >> 4;
            if (var1.isChunkLoaded(var83, var104)) {
                HeightMap var117 = HeightMap.WORLD_SURFACE;
                Block var23 = var1.getHighestBlockAt(var21, var22, var117);
                if (pullable(var23, 159342647) && !this.shielded(var23, 1994739544)) {
                    Map var55 = this.peeled;
                    long var89 = (long)var21 << 32;
                    long var122 = (long)var22 & 4294967295L;
                    Long var93 = var89 ^ var122;
                    Function var107 = var2x -> {
                        int[] var8 = new int[2];
                        int var10 = var23.getY();
                        var8[0] = var10;
                        int var13x = var12.nextInt(3);
                        int var12x = 3 + var13x;
                        var8[1] = var12x;
                        return var8;
                    };
                    int[] var24 = (int[])var55.computeIfAbsent(var93, var107);
                    int var59 = var23.getY();
                    int var95 = var24[0];
                    int var110 = var24[1];
                    int var96 = var95 - var110;
                    if (var59 > var96) {
                        var15++;
                        this.take(var1, var23, 359638252);
                    }
                }
            }
        }
    }

    private void digWell(World var1, int var2) {
        ThreadLocalRandom var10 = ThreadLocalRandom.current();
        int var11 = this.centre.getBlockY();
        int var33 = var1.getMinHeight();
        int var92 = var11 - 64;
        int var12 = Math.max(var33, var92);
        int var13 = this.centre.getBlockX();
        int var14 = this.centre.getBlockZ();

        for (int var15 = 0; var15 < 200; var15++) {
            double var16 = var10.nextDouble(0.0, Math.PI * 2);
            double var133 = Math.sqrt(var10.nextDouble());
            double var18 = 7.0 * var133;
            double var98 = Math.cos(var16);
            int var104 = (int)Math.round(var98 * var18);
            int var20 = var13 + var104;
            double var107 = Math.sin(var16);
            int var113 = (int)Math.round(var107 * var18);
            int var21 = var14 + var113;
            int var115 = var20 >> 4;
            int var137 = var21 >> 4;
            if (var1.isChunkLoaded(var115, var137)) {
                long var59 = (long)var20 << 32;
                long var142 = (long)var21 & 4294967295L;
                long var22 = var59 ^ var142;
                Map var64 = this.wellRoof;
                Long var118 = var22;
                Integer var24 = (Integer)var64.get(var118);
                int var91;
                if (var24 == null) {
                    var91 = var11;
                } else {
                    int var121 = var24 + 3;
                    var91 = Math.min(var11, var121);
                }

                for (int var26 = var91; var26 >= var12; var26 += -1) {
                    Block var27 = var1.getBlockAt(var20, var26, var21);
                    Material var28 = var27.getType();
                    if (!var28.isAir()) {
                        if (!var27.isLiquid() && !(var28.getHardness() < 0.0F) && !(var27.getState() instanceof TileState) && !this.shielded(var27, 1994739544)
                            )
                         {
                            Map var88 = this.wellRoof;
                            Long var128 = var22;
                            Integer var147 = var26;
                            var88.put(var128, var147);
                            this.take(var1, var27, 359638252);
                        }
                        break;
                    }
                }
            }
        }
    }

    private void take(World var1, Block var2, int var3) {
        BlockData var21 = var2.getBlockData();
        if (this.flying.size() < 600) {
            Location var67 = var2.getLocation();
            Consumer<BlockDisplay> var99 = var1x -> {
                var1x.setBlock(var21);
                var1x.setPersistent(false);
                var1x.setTeleportDuration(2);
                Brightness var13 = new Brightness(15, 15);
                var1x.setBrightness(var13);
                var1x.setGlowing(true);
                Color var15 = FragmentCoreRitual.HOLDER_GOLD;
                var1x.setGlowColorOverride(var15);
            };
            BlockDisplay var22 = (BlockDisplay)var1.spawn(var67, BlockDisplay.class, var99);

            for (UUID var24 : this.eyeK.keySet()) {
                Server var59 = this.plugin.getServer();
                Player var25 = var59.getPlayer(var24);
                if (var25 != null) {
                    BlissPlugin var83 = this.plugin;
                    var25.hideEntity(var83, var22);
                }
            }

            List var40 = this.flying;
            GoldenDreamMass.Flying var68 = new GoldenDreamMass.Flying(var22, 0.12, 1922191853);
            var40.add(var68);
        } else {
            int var85 = this.crumbled + 1;
            this.crumbled = var85;
            if (var85 % 3 == 0) {
                Particle var87 = Particle.BLOCK;
                Location var98 = var2.getLocation().add(0.5, 0.5, 0.5);
                Particles.spawn(var1, var87, var98, 6, 0.3, 0.3, 0.3, 0.0, var21, true, 1748031581);
            }
        }

        int var122 = Bukkit.getCurrentTick();
        int var70 = this.breakSoundTick;
        if (var122 != var70) {
            this.breakSoundTick = var122;
            this.breakSoundsThisTick = 0;
        }

        int var128;
        label38:
        if (this.breakSoundsThisTick < 12) {
            int var75 = this.breakSoundsThisTick + 1;
            this.breakSoundsThisTick = var75;
            var128 = 678151401;

            try {
                var128 = 678151401;
                SoundGroup var123 = var2.getBlockSoundGroup();
                var128 = 1740699974;
                Location var78 = var2.getLocation().add(0.5, 0.5, 0.5);
                Sound var92 = var123.getBreakSound();
                SoundCategory var101 = SoundCategory.BLOCKS;
                var128 = 1931310273;
                float var110 = (var123.getVolume() + 1.0F) / 2.0F;
                float var116 = var123.getPitch() * 0.8F;
                var128 = 91936690;
                var1.playSound(var78, var92, var101, var110, var116);
                var128 = 1685744625;
            } catch (Throwable var127) {
                var128 = 320171388 ^ var128;
                break label38;
            }

            var128 = 1671047746;
        } else {
            var128 = 1671047746;
        }

        BlockData var80 = Material.AIR.createBlockData();
        byte var93 = (byte)(1671047747 ^ var128);
        var2.setBlockData(var80, (var93 & 1) != 0);
    }

    private boolean shielded(Block var1, int var2) {
        for (Location var10 : this.shield) {
            double var28 = (double)var1.getX() + 0.5;
            double var58 = var10.getX();
            double var11 = var28 - var58;
            double var36 = (double)var1.getZ() + 0.5;
            double var63 = var10.getZ();
            double var13 = var36 - var63;
            double var42 = var11 * var11;
            double var69 = var13 * var13;
            if (!(var42 + var69 > 4.0)) {
                int var15 = var10.getBlockY();
                int var50 = var1.getY();
                int var54 = var15 + 1;
                if (var50 <= var54) {
                    int var52 = var1.getY();
                    int var56 = var15 - 6;
                    if (var52 >= var56) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean pullable(Block var0, int var1) {
        Material var4 = var0.getType();
        if (var4.isAir() || var0.isLiquid()) {
            return false;
        } else if (var4.getHardness() < 0.0F) {
            return false;
        } else {
            boolean var18;
            if (!(var0.getState() instanceof TileState)) {
                var18 = true;
            } else {
                var18 = false;
            }

            return var18;
        }
    }

    private void flyBlocks(World var1, double var2, int var4) {
        Vector var23 = this.centre.toVector();
        Vector var6 = new Vector(0.5, 0.5, 0.5);
        Vector var15 = var23.subtract(var6);
        Iterator var16 = this.flying.iterator();

        while (var16.hasNext()) {
            GoldenDreamMass.Flying var17 = (GoldenDreamMass.Flying)var16.next();
            if (!var17.d.isValid()) {
                var16.remove();
            } else {
                Location var18 = var17.d.getLocation();
                Vector var41 = var15.clone();
                Vector var60 = var18.toVector();
                Vector var19 = var41.subtract(var60);
                double var20 = var19.length();
                if (var20 < var2) {
                    Location var73 = var18.clone().add(0.5, 0.5, 0.5);
                    this.eat(var1, var73, 1901983182);
                    var17.d.remove();
                    var16.remove();
                } else {
                    double var77 = var17.speed * 2.0;
                    double var79 = Math.min(var77, var20);
                    double var81 = var79 / var20;
                    Vector var63 = var19.multiply(var81);
                    var18.add(var63);
                    BlockDisplay var56 = var17.d;
                    var56.teleport(var18);
                    double var90 = var17.speed * 1.06 * 1.06;
                    double var67 = Math.min(3.0, var90);
                    var17.speed = var67;
                }
            }
        }
    }

    private void tickVeils(double var1, int var3) {
        Iterator var15 = this.veiled.iterator();

        while (var15.hasNext()) {
            UUID var16 = (UUID)var15.next();
            Server var29 = this.plugin.getServer();
            Player var17 = var29.getPlayer(var16);
            if (var17 != null && var17.isOnline()) {
                World var35 = var17.getWorld();
                World var55 = this.centre.getWorld();
                if (var35 == var55) {
                    Location var37 = var17.getLocation();
                    Location var57 = this.centre;
                    if (!(var37.distanceSquared(var57) > 1600.0)) {
                        SignatureClock.send(var17, 80000L, 16001L, 399305443);
                        continue;
                    }
                }

                Map var43 = this.clocks;
                GoldenDreamMass.Prev var18 = (GoldenDreamMass.Prev)var43.remove(var16);
                if (var18 != null) {
                    long var62 = var18.offset();
                    boolean var68 = var18.relative();
                    var17.setPlayerTime(var62, var68);
                }

                var15.remove();
            } else {
                var15.remove();
                Map var51 = this.clocks;
                var51.remove(var16);
            }
        }
    }

    private void tickEyes(int var1) {
        Iterator var9 = this.eyeK.entrySet().iterator();

        while (var9.hasNext()) {
            Entry var10 = (Entry)var9.next();
            Server var25 = this.plugin.getServer();
            UUID var84 = (UUID)var10.getKey();
            Player var11 = var25.getPlayer(var84);
            if (var11 != null && var11.isOnline() && !var11.isDead()) {
                int var134 = (Integer)var10.getValue();
                if (var134 == 0) {
                    var11.setGravity(false);

                    for (GoldenDreamMass.Flying var14 : this.flying) {
                        if (var14.d.isValid()) {
                            BlissPlugin var93 = this.plugin;
                            BlockDisplay var105 = var14.d;
                            var11.hideEntity(var93, var105);
                        }
                    }
                }

                Vector var94 = new Vector();
                var11.setVelocity(var94);
                if (var134 >= 70) {
                    var11.setGravity(true);
                    Map var63 = this.clocks;
                    Object var98 = var10.getKey();
                    GoldenDreamMass.Prev var135 = (GoldenDreamMass.Prev)var63.remove(var98);
                    var9.remove();
                    GoldenDreamWorld var69 = this.owner.dream(2117018474);
                    long var115;
                    if (var135 != null) {
                        var115 = var135.offset();
                    } else {
                        var115 = 0L;
                    }

                    boolean var124;
                    if (var135 != null && !var135.relative()) {
                        var124 = false;
                    } else {
                        var124 = true;
                    }

                    UUID var126 = (UUID)var10.getKey();
                    UUID var132 = this.trigger;
                    boolean var127 = var126.equals(var132);
                    var69.enter(var11, var115, var124, var127, 1808728489);
                } else {
                    long var136 = var11.getWorld().getFullTime() % 24000L / 64L * 64L;
                    long var130 = (long)Math.min(63, var134);
                    long var120 = var136 + var130;
                    SignatureClock.send(var11, 80019L, var120, 399305443);
                    Integer var104 = var134 + 1;
                    var10.setValue(var104);
                }
            } else {
                Map var33 = this.clocks;
                Object var86 = var10.getKey();
                GoldenDreamMass.Prev var12 = (GoldenDreamMass.Prev)var33.remove(var86);
                if (var11 != null) {
                    var11.setGravity(true);
                    if (var12 != null) {
                        long var89 = var12.offset();
                        boolean var117 = var12.relative();
                        var11.setPlayerTime(var89, var117);
                    }
                }

                var9.remove();
            }
        }
    }

    private void end(boolean var1, int var2) {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }

        for (GoldenDreamMass.Flying var11 : this.flying) {
            if (var11.d.isValid()) {
                var11.d.remove();
            }
        }

        this.flying.clear();
        this.peeled.clear();
        this.wellRoof.clear();
        Set<Entry<UUID, Integer>> var113 = this.eyeK.entrySet();

        for (Entry<UUID, Integer> var132 : new ArrayList<>(var113)) {
            Server var46 = this.plugin.getServer();
            UUID var93 = (UUID)var132.getKey();
            Player var12 = var46.getPlayer(var93);
            Map var49 = this.clocks;
            Object var95 = var132.getKey();
            GoldenDreamMass.Prev var13 = (GoldenDreamMass.Prev)var49.remove(var95);
            if (var12 != null && var12.isOnline()) {
                var12.setGravity(true);
                if (var1 && !var12.isDead()) {
                    GoldenDreamWorld var61 = this.owner.dream(2117018474);
                    long var116;
                    if (var13 != null) {
                        var116 = var13.offset();
                    } else {
                        var116 = 0L;
                    }

                    boolean var125;
                    if (var13 != null && !var13.relative()) {
                        var125 = false;
                    } else {
                        var125 = true;
                    }

                    UUID var127 = (UUID)var132.getKey();
                    UUID var129 = this.trigger;
                    boolean var128 = var127.equals(var129);
                    var61.enter(var12, var116, var125, var128, 1808728489);
                } else if (var13 != null) {
                    long var99 = var13.offset();
                    boolean var118 = var13.relative();
                    var12.setPlayerTime(var99, var118);
                }
            }
        }

        this.eyeK.clear();

        for (Entry var133 : this.clocks.entrySet()) {
            Server var77 = this.plugin.getServer();
            UUID var103 = (UUID)var133.getKey();
            Player var134 = var77.getPlayer(var103);
            if (var134 != null && var134.isOnline()) {
                long var107 = ((GoldenDreamMass.Prev)var133.getValue()).offset();
                boolean var122 = ((GoldenDreamMass.Prev)var133.getValue()).relative();
                var134.setPlayerTime(var107, var122);
            }
        }

        this.clocks.clear();
        this.veiled.clear();
        GoldenDreamRitual var88 = this.owner;
        var88.unregister(this, 769799514);
        GoldenDreamRitual var90 = this.owner;
        UUID var111 = this.trigger;
        var90.finish(var111, 1863011177);
    }

    void abortNow(int var1) {
        this.end(false, 1264529697);
    }

    private static Vector randomUnit(ThreadLocalRandom var0, int var1) {
        double var15 = var0.nextDouble(-1.0, 1.0);
        double var17 = var0.nextDouble(0.0, Math.PI * 2);
        double var35 = var15 * var15;
        double var19 = Math.sqrt(1.0 - var35);
        double var45 = Math.cos(var17);
        double var39 = var19 * var45;
        double var51 = Math.sin(var17);
        double var49 = var19 * var51;
        return new Vector(var39, var15, var49);
    }

    private static final class Flying {
        final BlockDisplay d;
        double speed;

        Flying(BlockDisplay var1, double var2, int var4) {
            this.d = var1;
            this.speed = var2;
        }
    }

    private static record Prev(long offset, boolean relative) {
    }
}
