package com.bliss.mythics.aegis;

import com.bliss.core.GemTuning;
import com.bliss.core.Particles;
import com.bliss.core.SonicBoomSound;
import com.bliss.core.TrustStore;
import com.bliss.gems.ConsistentDamage;
import com.bliss.mythics.util.MythicCombat;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WaterMob;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.bliss.mythics.aegis.AngelsHang;
import com.bliss.mythics.aegis.MythicAegisState;
import com.bliss.mythics.aegis.PerforatorChain;
import org.bukkit.entity.Display;

public final class AngelsGrasp {
    private static final int CMD_PARRY = 30012;
    private static final double AOE_RADIUS = 5.0;
    private static final double AOE_DAMAGE = 16.0;
    private static final int CEILING_HEIGHT = 40;
    private static final int CEILING_R = 4;
    private static final int CEILING_TICKS = 240;
    private static final long WINDOW_MS = 12000L;
    private static final int WINDOW_TICKS = 240;
    private static final double REACH_BELOW = 3.0;
    private static final double DROP_BELOW = 20.0;
    private static final double PLATE_HALF_PER_SCALE = 0.5625;
    private static final int MAX_TARGETS = 5;
    private static final double GROUND_DAMAGE = 38.04143616;
    private static final double KB_STRENGTH = 0.95;
    private static final double KB_UP = 0.55;
    private static final double CASTER_BOUNCE = 1.15;
    private static final double CASTER_FORWARD = 1.0;
    private static final double LAUNCH_UP = 1.15;
    private static final double LAUNCH_FORWARD = 4.0;
    private static final int SMASH_SPARKS = 30;
    private static final double SMASH_SPARK_SPEED = 0.55;
    private static final double SMASH_SPARK_TILT = 0.55;
    private static final double SMASH_SPARK_SCATTER = 0.9;
    private static final int SMASH_DEBRIS_RINGS = 6;
    private static final int SMASH_DEBRIS_PER_RING = 14;
    private static final int SMASH_DEBRIS_COUNT = 4;
    private static final float GROUND_PARRY_SCALE = 16.0F;
    private static final float AIRSLAM_SCALE = 13.0F;
    private static final double GROUND_PARRY_LIFT = 0.05;
    private static final int GROUND_PARRY_TICKS = 60;
    private static final double PARRY_FORWARD = 2.0;
    private static final List<Block> CEILING_BLOCKS;
    private static File ceilingFile;
    private final Plugin plugin;
    private final MythicAegisState state;
    private final TrustStore trust;
    private static final double AIR_SLAM_DAMAGE = 25.3;
    private static final int CMD_AIRSLAM = 30014;
    private static final double FALL_DMG_PER_BLOCK = 0.05;
    private static final double FALL_DMG_MAX_BONUS = 2.0;

    public static void init(Plugin var0, int var1) {
        File var56 = var0.getDataFolder();
        String var4 = "angels_ceilings.yml";
        File var2 = new File(var56, var4);
        ceilingFile = var2;
        if (ceilingFile.isFile()) {
            YamlConfiguration var6 = YamlConfiguration.loadConfiguration(ceilingFile);
            int var7 = 0;
            Iterator var8 = var6.getStringList("blocks").iterator();

            while (var8.hasNext()) {
                String[] var10 = ((String)var8.next()).split(";");
                if (var10.length == 4) {
                    World var11 = Bukkit.getWorld(var10[0]);
                    if (var11 != null) {
                        try {
                            int var51 = Integer.parseInt(var10[1]);
                            int var60 = Integer.parseInt(var10[2]);
                            int var65 = Integer.parseInt(var10[3]);
                            Block var12 = var11.getBlockAt(var51, var60, var65);
                            Material var37 = var12.getType();
                            Material var52 = Material.BARRIER;
                            if (var37 == var52) {
                                Material var53 = Material.AIR;
                                var12.setType(var53, false);
                                var7++;
                            }
                        } catch (NumberFormatException var66) {
                        }
                    }
                }
            }

            ceilingFile.delete();
            if (var7 > 0) {
                Logger var44 = var0.getLogger();
                String var55 = "Angels Grasp: reverted " + var7 + " orphaned barrier(s) from a prior crash.";
                var44.info(var55);
            }
        }
    }

    private static String keyOf(Block var0, int var1) {
        String var7 = var0.getWorld().getName();
        int var9 = var0.getX();
        int var10 = var0.getY();
        int var11 = var0.getZ();
        return var7 + ";" + var9 + ";" + var10 + ";" + var11;
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private static synchronized void persistAdd(List<Block> var0, int var1) {
        if (ceilingFile != null && !var0.isEmpty()) {
            YamlConfiguration var27;
            label62: {
                try {
                    if (ceilingFile.isFile()) {
                        var27 = YamlConfiguration.loadConfiguration(ceilingFile);
                        break label62;
                    }
                } catch (Exception var37) {
                    boolean var10001 = false;
                    return;
                }

                try {
                    var27 = new YamlConfiguration();
                } catch (Exception var36) {
                    boolean var38 = false;
                    return;
                }
            }

            YamlConfiguration var4;
            List var5;
            Iterator var6;
            try {
                var4 = var27;
                var5 = var27.getStringList("blocks");
                var6 = var0.iterator();
            } catch (Exception var34) {
                boolean var39 = false;
                return;
            }

            while (true) {
                try {
                    if (!var6.hasNext()) {
                        break;
                    }

                    Block var7 = (Block)var6.next();
                    String var30 = keyOf(var7, 1932441320);
                    var5.add(var30);
                } catch (Exception var35) {
                    boolean var40 = false;
                    return;
                }
            }

            try {
                String var31 = "blocks";
                var4.set(var31, var5);
                File var32 = ceilingFile;
                var4.save(var32);
            } catch (Exception var33) {
                boolean var41 = false;
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private static synchronized void persistRemove(List<Block> var0, int var1) {
        if (ceilingFile != null && ceilingFile.isFile()) {
            YamlConfiguration var5;
            List var6;
            Iterator var7;
            try {
                var5 = YamlConfiguration.loadConfiguration(ceilingFile);
                var6 = var5.getStringList("blocks");
                var7 = var0.iterator();
            } catch (Exception var36) {
                boolean var10001 = false;
                return;
            }

            while (true) {
                try {
                    if (var7.hasNext()) {
                        Block var8 = (Block)var7.next();
                        String var30 = keyOf(var8, 1932441320);
                        var6.remove(var30);
                        continue;
                    }
                } catch (Exception var37) {
                    boolean var38 = false;
                    break;
                }

                YamlConfiguration var19;
                String var27;
                List var32;
                label64: {
                    try {
                        var19 = var5;
                        var27 = "blocks";
                        if (var6.isEmpty()) {
                            var32 = null;
                            break label64;
                        }
                    } catch (Exception var35) {
                        boolean var39 = false;
                        break;
                    }

                    try {
                        var32 = var6;
                    } catch (Exception var34) {
                        boolean var40 = false;
                        break;
                    }
                }

                try {
                    var19.set(var27, var32);
                    File var28 = ceilingFile;
                    var5.save(var28);
                } catch (Exception var33) {
                    boolean var41 = false;
                }
                break;
            }
        }
    }

    public static void registerBarriers(List<Block> var0, int var1) {
        if (var0 != null && !var0.isEmpty()) {
            List var6 = CEILING_BLOCKS;
            var6.addAll(var0);
            persistAdd(var0, 942572360);
        }
    }

    public static void unregisterBarriers(List<Block> var0, int var1) {
        if (var0 != null && !var0.isEmpty()) {
            List var6 = CEILING_BLOCKS;
            var6.removeAll(var0);
            persistRemove(var0, 1252856778);
        }
    }

    public AngelsGrasp(Plugin var1, MythicAegisState var2, TrustStore var3, int var4) {
        this.plugin = var1;
        this.state = var2;
        this.trust = var3;
    }

    public void cast(Player var1, int var2) {
        Location var18 = var1.getLocation();
        World var26 = var18.getWorld();
        Sound var5 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
        var26.playSound(var18, var5, 1.0F, 0.7F);
        World var28 = var18.getWorld();
        Particle var89 = Particle.SONIC_BOOM;
        Location var116 = var18.clone().add(0.0, 1.0, 0.0);
        Particles.spawn(var28, var89, var116, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
        String var32 = "angels-cast|" + var1.getUniqueId();
        World var91 = var18.getWorld();
        SonicBoomSound.burst(var32, var91, var18, 1967648345);
        World var34 = var18.getWorld();

        for (Entity var154 : var34.getNearbyEntities(var18, 5.0, 5.0, 5.0)) {
            if (var154 instanceof LivingEntity) {
                LivingEntity var22 = (LivingEntity)var154;
                if (!var22.equals(var1) && !var22.isDead()) {
                    if (var22 instanceof Player) {
                        Player var23 = (Player)var22;
                        TrustStore var69 = this.trust;
                        UUID var103 = var1.getUniqueId();
                        UUID var128 = var23.getUniqueId();
                        if (var69.isTrusted(var103, var128, 1736364606)) {
                            continue;
                        }
                    }

                    if (var22 instanceof Player) {
                        Player var155 = (Player)var22;
                        GameMode var76 = var155.getGameMode();
                        GameMode var104 = GameMode.CREATIVE;
                        if (var76 == var104) {
                            continue;
                        }

                        GameMode var78 = var155.getGameMode();
                        GameMode var105 = GameMode.SPECTATOR;
                        if (var78 == var105) {
                            continue;
                        }
                    }

                    Vector var81 = var22.getLocation().toVector();
                    Vector var107 = var18.toVector();
                    Vector var156 = var81.subtract(var107).setY(0.4);
                    if (var156.lengthSquared() > 1.0E-4) {
                        Vector var113 = var156.normalize().multiply(0.8).setY(0.45);
                        var22.setVelocity(var113);
                    }
                }
            }
        }

        Location var94 = var1.getLocation();
        double var20 = (double)this.buildCeiling(var94, 750204176) + 1.0;
        MythicAegisState var47 = this.state;
        UUID var96 = var1.getUniqueId();
        long var145 = System.currentTimeMillis() + 12000L;
        var47.armAngels(var96, var20, var145, 497992342);
        Plugin var49 = this.plugin;
        MythicAegisState var98 = this.state;
        TrustStore var125 = this.trust;
        startSequence(var49, var98, var125, var1, var20, 1649938201);
        if (MythicAegisState.legacyAuratus(180703355)) {
            Plugin var52 = this.plugin;
            TrustStore var100 = this.trust;
            AngelsHang.tryHang(var52, var100, var1, 389897471);
        }
    }

    public static void startSequence(final Plugin var0, final MythicAegisState var1, final TrustStore var2, final Player var3, final double var4, int var6) {
        final UUID var15 = var3.getUniqueId();
        BukkitRunnable var17 = new BukkitRunnable() {
            int t = 0;
            boolean reached = false;
            boolean downChained = false;
            boolean wasGrappling = false;

            public void run() {
                int var107 = this.t + 1;
                this.t = var107;
                if (var107 <= 240 && var3.isOnline() && var3.isValid()) {
                    MythicAegisState var21 = var1;
                    UUID var110 = var15;
                    if (var21.angelsActive(var110, 1811770379)) {
                        double var9 = var3.getLocation().getY();
                        if (!this.reached) {
                            double var156 = var4 - 3.0;
                            if (var9 >= var156) {
                                this.reached = true;
                                PerforatorChain.cancelFor(var15, 1586679464);
                                MythicAegisState var84 = var1;
                                UUID var130 = var15;
                                var84.markAngelsCeiling(var130, 305669104);
                                double var161 = var4 - 1.5;
                                if (var9 > var161) {
                                    Location var174 = var3.getLocation();
                                    double var134 = var4 - 2.0;
                                    var174.setY(var134);
                                    Player var93 = var3;
                                    var93.teleport(var174);
                                }

                                Player var96 = var3;
                                Vector var137 = new Vector(0.0, -0.2, 0.0);
                                var96.setVelocity(var137);
                                var3.setFallDistance(0.0F);
                                World var101 = var3.getWorld();
                                Location var141 = var3.getLocation();
                                Sound var163 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
                                var101.playSound(var141, var163, 1.0F, 1.2F);
                            }
                        } else {
                            if (PerforatorChain.isPullingEntity(var15, 1192577279)) {
                                this.downChained = false;
                                Set var126 = var1.grappling;
                                UUID var152 = var15;
                                boolean var127 = var126.contains(var152);
                                this.wasGrappling = var127;
                                return;
                            }

                            Set var35 = var1.grappling;
                            UUID var112 = var15;
                            boolean var11 = var35.contains(var112);
                            if (var11) {
                                this.downChained = true;
                            }

                            boolean var44;
                            if (this.downChained && !var11 && this.wasGrappling) {
                                var44 = true;
                            } else {
                                var44 = false;
                            }

                            this.wasGrappling = var11;
                            boolean var105;
                            if (!MythicAegisState.legacyAuratus(180703355)) {
                                if (!var44 && !var3.isOnGround()) {
                                    var105 = false;
                                } else {
                                    var105 = true;
                                }
                            } else {
                                double var147 = var4 - 20.0;
                                if (!(var9 <= var147) || !var3.isOnGround() && !var44) {
                                    var105 = false;
                                } else {
                                    var105 = true;
                                }
                            }

                            if (this.downChained && var105) {
                                var3.setFallDistance(0.0F);
                                PerforatorChain.cancelFor(var15, false, 260114390);
                                Plugin var62 = var0;
                                Player var118 = var3;
                                TrustStore var150 = var2;
                                if (!AngelsGrasp.groundImpact(var62, var118, var150, 11805084) && !MythicAegisState.legacyAuratus(180703355)) {
                                    MythicAegisState var66 = var1;
                                    UUID var120 = var15;
                                    var66.refundChain(var120, 1278254800);
                                }

                                MythicAegisState var69 = var1;
                                UUID var122 = var15;
                                var69.clearAngels(var122, 950114969);
                                this.cancel();
                            }
                        }

                        return;
                    }
                }

                MythicAegisState var103 = var1;
                UUID var143 = var15;
                var103.clearAngels(var143, 950114969);
                this.cancel();
            }
        };
        var17.runTaskTimer(var0, 1L, 1L);
    }

    private int buildCeiling(Location var1, int var2) {
        ArrayList var3 = new ArrayList();
        ArrayList var14 = var3;
        int var15 = var1.getBlockX();
        int var16 = var1.getBlockZ();
        int var17 = var1.getBlockY() + 40;
        final double var18 = (double)var15 + 0.5;
        final double var20 = (double)var16 + 0.5;

        for (int var22 = -4; var22 <= 4; var22++) {
            for (int var23 = -4; var23 <= 4; var23++) {
                int var65 = var22 * var22;
                int var87 = var23 * var23;
                if (var65 + var87 <= 16) {
                    World var68 = var1.getWorld();
                    int var90 = var15 + var22;
                    int var115 = var16 + var23;
                    Block var121 = var68.getBlockAt(var90, var17, var115);
                    if (var121.getType().isAir()) {
                        Material var91 = Material.BARRIER;
                        var121.setType(var91, false);
                        var14.add(var121);
                        List var76 = CEILING_BLOCKS;
                        var76.add(var121);
                    }
                }
            }
        }

        persistAdd(var14, 942572360);
        final double var119 = (double)var17 + 0.5;
        final World var24 = var1.getWorld();
        Color var99 = Color.fromRGB(255, 213, 79);
        final DustOptions var53 = new DustOptions(var99, 1.6F);
        BukkitRunnable var54 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                if (this.t >= 240) {
                    this.cancel();
                } else {
                    for (int var20x = 0; var20x < 40; var20x++) {
                        double var76 = (double)var20x;
                        double var21 = (Math.PI * 2) * var76 / 40.0;
                        double var38 = var18;
                        double var95 = Math.cos(var21);
                        double var83 = 4.0 * var95;
                        double var23 = var38 + var83;
                        double var43 = var20;
                        double var99 = Math.sin(var21);
                        double var87 = 4.0 * var99;
                        double var25 = var43 + var87;
                        World var48 = var24;
                        Particle var64 = Particle.END_ROD;
                        World var102 = var24;
                        double var116 = var119;
                        Location var89 = new Location(var102, var23, var116, var25);
                        Particles.spawn(var48, var64, var89, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                    }

                    for (byte var136 = -4; var136 <= 4; var136 += 1) {
                        for (byte var137 = -4; var137 <= 4; var137 += 1) {
                            int var54 = var136 * var136;
                            int var69 = var137 * var137;
                            if (var54 + var69 <= 16 && (var136 + var137 & 1) == 0) {
                                World var60 = var24;
                                Particle var73 = Particle.DUST;
                                World var106 = var24;
                                double var110 = var18;
                                double var119x = (double)var136;
                                double var112 = var110 + var119x;
                                double var122 = var119;
                                double var127 = var20;
                                double var133 = (double)var137;
                                double var129 = var127 + var133;
                                Location var91 = new Location(var106, var112, var122, var129);
                                DustOptions var135 = var53;
                                Particles.spawn(var60, var73, var91, 1, 0.0, 0.0, 0.0, 0.0, var135, 1141219473);
                            }
                        }
                    }

                    int var75 = this.t + 5;
                    this.t = var75;
                }
            }
        };
        Plugin var81 = this.plugin;
        var54.runTaskTimer(var81, 0L, 5L);
        BukkitScheduler var59 = this.plugin.getServer().getScheduler();
        Plugin var83 = this.plugin;
        Runnable var104 = () -> {
            for (Block var5 : (List<Block>)var14) {
                Material var13 = var5.getType();
                Material var2x = Material.BARRIER;
                if (var13 == var2x) {
                    Material var18x = Material.AIR;
                    var5.setType(var18x, false);
                }
            }

            List var15x = CEILING_BLOCKS;
            var15x.removeAll(var14);
            persistRemove(var14, 1252856778);
        };
        var59.runTaskLater(var83, var104, 240L);
        return var17;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void cleanupAll(int n) {
        List<Block> list;
        List<Block> list2 = list = CEILING_BLOCKS;
        synchronized (list) {
            for (Block block : CEILING_BLOCKS) {
                Material material;
                Material material2 = block.getType();
                if (material2 != (material = Material.BARRIER)) continue;
                Block block2 = block;
                Material material3 = Material.AIR;
                block2.setType(material3, false);
            }
            CEILING_BLOCKS.clear();
            // ** MonitorExit[var3_2] (shouldn't be in output)
            if (ceilingFile != null && ceilingFile.isFile()) {
                ceilingFile.delete();
            }
            return;
        }
    }

    public static boolean groundImpact(Plugin var0, Player var1, TrustStore var2, int var3) {
        String var7 = "auratus";
        String var8 = "angels-grasp";
        double var15 = GemTuning.mythicDamage(var7, var8, 38.04143616, 478197159);
        return impact(var0, var1, var2, var15, 30014, 16.0F, 0.0, 1581065999);
    }

    private static double heightMult(double var0, int var2) {
        double var17 = Math.max(0.0, var0) * 0.05;
        double var13 = Math.min(2.0, var17);
        return 1.0 + var13;
    }

    public static boolean airSlam(Plugin var0, Player var1, TrustStore var2, double var3, int var5) {
        String var9 = "auratus";
        String var10 = "air-slam";
        double var17 = GemTuning.mythicDamage(var9, var10, 25.3, 478197159);
        return impact(var0, var1, var2, var17, 30014, 13.0F, var3, 1581065999);
    }

    private static boolean impact(Plugin var0, Player var1, TrustStore var2, double var3, int var5, float var6, double var7, int var9) {
        double var208 = heightMult(var7, 1340162894);
        double var31 = var3 * var208;
        Location var33 = var1.getLocation();
        double var34 = (double)var6 * 0.5625;
        ArrayList<LivingEntity> var52 = new ArrayList<>();
        ArrayList<LivingEntity> var36 = var52;
        World var54 = var33.getWorld();

        for (Entity var38 : var54.getNearbyEntities(var33, var34, 3.0, var34)) {
            if (var38 instanceof LivingEntity) {
                LivingEntity var39 = (LivingEntity)var38;
                if (!var39.equals(var1) && !var39.isDead()) {
                    if (var39 instanceof Player) {
                        Player var40 = (Player)var39;
                        UUID var171 = var1.getUniqueId();
                        UUID var215 = var40.getUniqueId();
                        if (var2.isTrusted(var171, var215, 1736364606)) {
                            continue;
                        }
                    }

                    if (var39 instanceof Player) {
                        Player var310 = (Player)var39;
                        GameMode var81 = var310.getGameMode();
                        GameMode var172 = GameMode.CREATIVE;
                        if (var81 == var172) {
                            continue;
                        }

                        GameMode var83 = var310.getGameMode();
                        GameMode var173 = GameMode.SPECTATOR;
                        if (var83 == var173) {
                            continue;
                        }
                    }

                    var36.add(var39);
                }
            }
        }

        if (var36.size() > 5) {
            Comparator<LivingEntity> var178 = Comparator.comparingDouble(var1x -> {
                Location var4 = var1x.getLocation();
                return var4.distanceSquared(var33);
            });
            var36.sort(var178);
            List var217 = var36.subList(0, 5);
            ArrayList var89 = new ArrayList(var217);
            var36 = var89;
        }

        if (MythicAegisState.legacyAuratus(180703355)) {
            World var92 = var33.getWorld();
            Sound var218 = Sound.ENTITY_GENERIC_EXPLODE;
            var92.playSound(var33, var218, 1.3F, 0.85F);
            World var94 = var33.getWorld();
            Particle var180 = Particle.EXPLOSION_EMITTER;
            Location var221 = var33.clone().add(0.0, 0.2, 0.0);
            Particles.spawn(var94, var180, var221, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
        } else {
            maceSmashFx(var33, var34, 128604070);
        }

        Vector var307 = var33.getDirection().setY(0);
        Location var167;
        if (var307.lengthSquared() > 1.0E-6) {
            Location var165 = var33.clone();
            Vector var205 = var307.normalize().multiply(2.0);
            var167 = var165.add(var205);
        } else {
            var167 = var33;
        }

        spawnGroundParry(var0, var167, var5, var6, 1501464834);
        boolean var309 = false;

        for (LivingEntity var41 : var36) {
            if (countsAsHit(var41, 947205819)) {
                var309 = true;
            }

            ConsistentDamage.dealFullArmorAware(var1, var41, var31, 1323155734);
            MythicCombat.damageArmor(var41, var31, 0.3, 256520978);
            Vector var118 = var41.getLocation().toVector();
            Vector var187 = var33.toVector();
            Vector var42 = var118.subtract(var187);
            var42.setY(0);
            if (var42.lengthSquared() < 1.0E-4) {
                Vector var126 = new Vector(0, 0, 1);
                var42 = var126;
            }

            var42.normalize().multiply(0.95).setY(0.55);
            Vector var194 = var41.getVelocity();
            Vector var195 = var194.add(var42);
            var41.setVelocity(var195);
        }

        if (MythicAegisState.legacyAuratus(180703355)) {
            Vector var313 = var1.getVelocity();
            Vector var314 = var1.getLocation().getDirection().setY(0);
            if (var314.lengthSquared() > 1.0E-6) {
                var314.normalize();
            } else {
                Vector var160 = new Vector(0, 0, 0);
                var314 = var160;
            }

            double var258 = var313.getX() * 0.4;
            double var280 = var314.getX() * 1.0;
            double var260 = var258 + var280;
            double var296 = var313.getZ() * 0.4;
            double var305 = var314.getZ() * 1.0;
            double var298 = var296 + var305;
            Vector var202 = new Vector(var260, 1.15, var298);
            var1.setVelocity(var202);
        } else if (!var309) {
            Vector var312 = var1.getEyeLocation().getDirection().setY(0);
            if (var312.lengthSquared() > 1.0E-6) {
                var312.normalize();
            } else {
                Vector var147 = new Vector(0, 0, 0);
                var312 = var147;
            }

            double var250 = var312.getX() * 4.0;
            double var289 = var312.getZ() * 4.0;
            Vector var197 = new Vector(var250, 1.15, var289);
            var1.setVelocity(var197);
            var1.setFallDistance(0.0F);
        } else {
            Vector var199 = new Vector(0, 0, 0);
            var1.setVelocity(var199);
            var1.setFallDistance(0.0F);
        }

        return var309;
    }

    private static boolean countsAsHit(LivingEntity var0, int var1) {
        if (var0 instanceof ArmorStand) {
            return false;
        } else if (var0.isInvulnerable()) {
            return false;
        } else if (!(var0 instanceof Player)) {
            if (var0 instanceof Animals) {
                return false;
            } else {
                return var0 instanceof Ambient ? false : !(var0 instanceof WaterMob);
            }
        } else {
            GameMode var5 = ((Player)var0).getGameMode();
            GameMode var3 = GameMode.CREATIVE;
            if (var5 != var3) {
                GameMode var30 = GameMode.SPECTATOR;
                if (var5 != var30) {
                    return true;
                }
            }

            return false;
        }
    }

    private static void maceSmashFx(Location var0, double var1, int var3) {
        World var27 = var0.getWorld();
        Sound var6 = Sound.ITEM_MACE_SMASH_GROUND;
        var27.playSound(var0, var6, 1.4F, 1.0F);
        BlockData var28 = null;
        int var29 = var0.getBlockX();
        int var30 = var0.getBlockZ();
        int var31 = (int)Math.floor(var0.getY());

        for (int var32 = 0; var32 <= 4 && var28 == null; var32++) {
            int var172 = var31 - var32;
            Block var33 = var27.getBlockAt(var29, var172, var30);
            if (var33.getType().isSolid()) {
                var28 = var33.getBlockData();
            }
        }

        if (var28 == null) {
            var28 = Material.STONE.createBlockData();
        }

        double var252 = surfaceY(var0, 1100964362) + 0.1;
        ThreadLocalRandom var34 = ThreadLocalRandom.current();

        for (int var35 = 0; var35 < 30; var35++) {
            double var255 = var34.nextDouble(0.0, Math.PI * 2);
            double var155 = Math.sqrt(var34.nextDouble());
            double var257 = var1 * var155;
            double var41 = var34.nextDouble() * 0.55;
            double var160 = var34.nextDouble(-0.9, 0.9);
            double var43 = var255 + var160;
            double var207 = var34.nextDouble();
            double var191 = 0.8 * var207;
            double var164 = 0.6 + var191;
            double var45 = 0.55 * var164;
            Particle var128 = Particle.FIREWORK;
            double var167 = var0.getX();
            double var195 = Math.cos(var255);
            double var197 = var195 * var257;
            double var169 = var167 + var197;
            double var212 = var0.getZ();
            double var223 = Math.sin(var255);
            double var225 = var223 * var257;
            double var214 = var212 + var225;
            double var230 = Math.cos(var43);
            double var232 = var230 * var41;
            double var242 = Math.sin(var43);
            double var244 = var242 * var41;
            Particles.spawn(var27, var128, var169, var252, var214, 0, var232, 1.0, var244, var45, null, true, 954482185);
        }

        for (byte var254 = 1; var254 <= 6; var254 += 1) {
            double var133 = (double)var254;
            double var36 = var1 * var133 / 6.0;

            for (int var38 = 0; var38 < 14; var38++) {
                double var138 = (double)var38;
                double var88 = (Math.PI * 2) * var138 / 14.0;
                double var145 = (double)var254 * 0.37;
                double var39 = var88 + var145;
                Particle var125 = Particle.BLOCK;
                double var148 = var0.getX();
                double var183 = Math.cos(var39);
                double var185 = var183 * var36;
                double var150 = var148 + var185;
                double var202 = var0.getZ();
                double var216 = Math.sin(var39);
                double var218 = var216 * var36;
                double var204 = var202 + var218;
                Particles.spawn(var27, var125, var150, var252, var204, 4, 0.15, 0.1, 0.15, 0.0, var28, true, 954482185);
            }
        }
    }

    private static double surfaceY(Location var0, int var1) {
        World var6 = var0.getWorld();
        int var7 = var0.getBlockX();
        int var8 = var0.getBlockZ();
        int var9 = (int)Math.floor(var0.getY());

        for (int var10 = 0; var10 <= 4; var10++) {
            int var38 = var9 - var10;
            Block var11 = var6.getBlockAt(var7, var38, var8);
            if (var11.getType().isSolid()) {
                return var11.getBoundingBox().getMaxY();
            }
        }

        return var0.getY();
    }

    private static void spawnGroundParry(Plugin var0, Location var1, int var2, float var3, int var4) {
        Material var6 = Material.PAPER;
        ItemStack var5 = new ItemStack(var6);
        ItemMeta var16 = var5.getItemMeta();
        if (var16 != null) {
            Integer var42 = var2;
            var16.setCustomModelData(var42);
            var5.setItemMeta(var16);
        }

        World var49 = var1.getWorld();
        double var58 = (double)var1.getBlockX() + 0.5;
        double var70 = surfaceY(var1, 1100964362) + 0.05;
        double var76 = (double)var1.getBlockZ() + 0.5;
        Location var26 = new Location(var49, var58, var70, var76);
        float var18 = var3 * 0.2F;
        World var30 = var1.getWorld();
        Consumer<ItemDisplay> var61 = var2x -> {
            var2x.setPersistent(false);
            var2x.setItemStack(var5);
            ItemDisplayTransform var16x = ItemDisplayTransform.FIXED;
            var2x.setItemDisplayTransform(var16x);
            float var23 = 0.5F * var18;
            Vector3f var5x = new Vector3f(0.0F, var23, 0.0F);
            Quaternionf var20 = new Quaternionf();
            Vector3f var22 = new Vector3f(var18, var18, var18);
            Quaternionf var24 = new Quaternionf();
            Transformation var17 = new Transformation(var5x, var20, var22, var24);
            var2x.setTransformation(var17);
            Brightness var18x = new Brightness(15, 15);
            var2x.setBrightness(var18x);
        };
        ItemDisplay var19 = (ItemDisplay)var30.spawn(var26, ItemDisplay.class, var61);
        BukkitScheduler var35 = var0.getServer().getScheduler();
        Runnable var52 = () -> {
            if (var19 != null && !var19.isDead()) {
                var19.setInterpolationDelay(0);
                var19.setInterpolationDuration(4);
                float var19x = 0.5F * var3;
                Vector3f var4x = new Vector3f(0.0F, var19x, 0.0F);
                Quaternionf var17 = new Quaternionf();
                Vector3f var18x = new Vector3f(var3, var3, var3);
                Quaternionf var20 = new Quaternionf();
                Transformation var16x = new Transformation(var4x, var17, var18x, var20);
                var19.setTransformation(var16x);
            }
        };
        var35.runTaskLater(var0, var52, 1L);
        BukkitScheduler var39 = var0.getServer().getScheduler();
        Runnable var54 = () -> {
            if (var19 != null && !var19.isDead()) {
                var19.remove();
            }
        };
        var39.runTaskLater(var0, var54, 60L);
    }

    static {
        ArrayList var0 = new ArrayList();
        CEILING_BLOCKS = Collections.synchronizedList(var0);
    }
}
