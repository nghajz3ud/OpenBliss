package com.bliss.entity;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.core.Particles;
import com.bliss.gems.GrantedEffect;
import com.bliss.goldgem.GoldGem;
import com.bliss.pedestal.PedestalReviveRitual;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public final class EntityMotifs {
    private static final long PASS_TICKS = 1L;
    private static final double SCALE = 0.7;
    private static final int ARM_STAGGER = 3;
    private static final double ROD_FLIGHT = 11.0;
    private static final double SOUL_FLIGHT = 25.0;
    private static final double ROD_SWEEP_TICKS = 32.0;
    private static final double SOUL_SWEEP_TICKS = 40.0;
    private static final double BODY_JET = 0.952;
    private static final double INWARD_STOP = -0.5;
    private static final double BODY_CENTRE = 0.9;
    private static final int SNAKE_COUNT = 8;
    private static final float SNAKE_DUST_SIZE = 0.546F;
    private static final double SNAKE_R_FACTOR = 0.95;
    private static final double SNAKE_MIN_FACTOR = 0.82;
    private static final double SNAKE_MAX_FACTOR = 1.18;
    private static final double SNAKE_HEAD_SPEED = 0.05;
    private static final double SNAKE_TRAIL_ARC = 1.08;
    private static final double SNAKE_PTS_PER_RAD = 12.0;
    private static final double SNAKE_TAIL_FADE = 0.15;
    private static final double TAU = Math.PI * 2;
    private static final double MOVE_THRESHOLD = 0.02;
    private static final int WHEEL_JETS = 6;
    private static final double WHEEL_SPIN = 0.025;
    private static final double WHEEL_INNER = 0.75;
    private static final Map<String, EntityMotifs.Motif> MOTIFS;
    private final BlissPlugin plugin;
    private final File file;
    private final Map<UUID, String> assigned;
    private BukkitTask task;
    private int pass;
    private final Map<UUID, EntityMotifs.Snake[]> snakes;
    private final Map<UUID, Location> lastAt;
    private final Map<UUID, List<UUID>> viewers;
    private static final EquipmentSlot[] GEAR;

    private static EntityMotifs.Snake newSnake(Color var0, double var1, int var3) {
        ThreadLocalRandom var15 = ThreadLocalRandom.current();
        double var16 = var15.nextDouble() * (Math.PI * 2);
        double var69 = var15.nextDouble();
        double var18 = Math.acos(2.0 * var69 - 1.0);
        double var75 = Math.sin(var18);
        double var96 = Math.cos(var16);
        double var77 = var75 * var96;
        double var100 = Math.sin(var18);
        double var116 = Math.sin(var16);
        double var102 = var100 * var116;
        double var120 = Math.cos(var18);
        Vector var36 = new Vector(var77, var102, var120);
        Vector var50;
        if (Math.abs(var36.getX()) < 0.9) {
            var50 = new Vector(1, 0, 0);
        } else {
            var50 = new Vector(0, 1, 0);
        }

        EntityMotifs.Snake var43 = new EntityMotifs.Snake();
        Vector var52 = var36.clone();
        Vector var54 = var52.crossProduct(var50).normalize();
        var43.u = var54;
        Vector var56 = var36.clone();
        Vector var84 = var43.u;
        Vector var58 = var56.crossProduct(var84).normalize();
        var43.v = var58;
        double var109 = var15.nextDouble() * 0.36;
        double var88 = 0.82 + var109;
        double var61 = var1 * var88;
        var43.rU = var61;
        double var114 = var15.nextDouble() * 0.36;
        double var92 = 0.82 + var114;
        double var65 = var1 * var92;
        var43.rV = var65;
        DustOptions var67 = new DustOptions(var0, 0.546F);
        var43.dust = var67;
        return var43;
    }

    private static Color snakeColour(int var0, int var1) {
        return PedestalReviveRitual.gemColor(GoldGem.byBit(var0, 621438557), 104246386);
    }

    private static EntityMotifs.Spoke[] spokesOf(double[][][] var0, boolean var1, double var2, int var4) {
        double var18 = 0.0;
        double var20 = 0.0;
        double var22 = 0.0;
        double var24 = 0.0;
        int var26 = 0;
        int var27 = 0;

        for (double[][] var205 : var0) {
            int var140 = var205.length - 1;
            double[] var206 = var205[var140];
            double var102 = var206[0];
            double var162 = var206[1];
            double var207 = Math.hypot(var102, var162) * 0.7;
            double var209 = signedTurn(var205, 414713631);
            if (var209 < 0.0) {
                var18 -= var209;
                var22 += var207;
                var26++;
            } else {
                var20 += var209;
                var24 += var207;
                var27++;
            }
        }

        double var95;
        if (var26 > 0) {
            double var156 = (double)var26;
            double var93 = var18 / var156;
            var95 = var93 / var2;
        } else if (var27 > 0) {
            double var151 = (double)var27;
            double var89 = -(var20 / var151);
            var95 = var89 / var2;
        } else {
            var95 = 0.0;
        }

        double var202 = var95;
        if (var1) {
            var202 = -var95;
        }

        int var204 = Math.max(var26, var27);
        ArrayList<EntityMotifs.Spoke> var59 = new ArrayList<>();
        ArrayList<EntityMotifs.Spoke> var31 = var59;

        for (int var32 = 0; var32 < var204; var32++) {
            double var63 = (double)var32;
            double var181 = (double)var204;
            double var142 = (Math.PI * 2) / var181;
            double var33 = var63 * var142;
            double var70;
            if (var26 > 0) {
                double var145 = (double)var26;
                var70 = var22 / var145;
            } else {
                double var148 = (double)var27;
                var70 = var24 / var148;
            }

            EntityMotifs.Spoke var132 = new EntityMotifs.Spoke(var33, var70, var202, false);
            var31.add(var132);
            double var185 = var70 * 0.952;
            EntityMotifs.Spoke var133 = new EntityMotifs.Spoke(var33, var185, var202, false);
            var31.add(var133);
            if (var26 > 0 && var27 > 0) {
                double var196 = (double)var27;
                double var189 = var24 / var196;
                EntityMotifs.Spoke var134 = new EntityMotifs.Spoke(var33, var189, var202, true);
                var31.add(var134);
            }
        }

        EntityMotifs.Spoke[] var136 = new EntityMotifs.Spoke[0];
        return var31.toArray(var136);
    }

    private static double signedTurn(double[][] var0, int var1) {
        double var12 = 0.0;
        int var14 = 2;

        while (true) {
            int var73 = var0.length;
            if (var14 >= var73) {
                return var12;
            }

            int var75 = var14 - 1;
            double var31 = var0[var75][0];
            int var119 = var14 - 2;
            double var86 = var0[var119][0];
            double var15 = var31 - var86;
            int var78 = var14 - 1;
            double var37 = var0[var78][1];
            int var122 = var14 - 2;
            double var91 = var0[var122][1];
            double var17 = var37 - var91;
            double var43 = var0[var14][0];
            int var125 = var14 - 1;
            double var95 = var0[var125][0];
            double var19 = var43 - var95;
            double var49 = var0[var14][1];
            int var128 = var14 - 1;
            double var99 = var0[var128][1];
            double var21 = var49 - var99;
            double var23 = Math.hypot(var15, var17);
            double var25 = Math.hypot(var19, var21);
            if (!(var23 < 1.0E-9) && !(var25 < 1.0E-9)) {
                double var111 = var15 * var21;
                double var137 = var17 * var19;
                double var113 = var111 - var137;
                double var141 = var23 * var25;
                double var115 = var113 / var141;
                double var145 = var15 * var19;
                double var157 = var17 * var21;
                double var147 = var145 + var157;
                double var161 = var23 * var25;
                double var149 = var147 / var161;
                double var117 = Math.atan2(var115, var149);
                var12 += var117;
            }

            var14++;
        }
    }

    private static EntityMotifs.Spoke[] counterWheels(double[][][] var0, int var1) {
        double var13 = longReach(var0, 2088273556);
        EntityMotifs.Spoke[] var15 = new EntityMotifs.Spoke[12];

        for (int var16 = 0; var16 < 6; var16++) {
            double var17 = (double)var16 * (Math.PI / 3);
            int var34 = 2 * var16;
            double var45 = var13 * 0.75;
            EntityMotifs.Spoke var39 = new EntityMotifs.Spoke(var17, var45, 0.025, false);
            var15[var34] = var39;
            int var37 = 2 * var16 + 1;
            EntityMotifs.Spoke var42 = new EntityMotifs.Spoke(var17, var13, -0.025, false);
            var15[var37] = var42;
        }

        return var15;
    }

    private static double longReach(double[][][] var0, int var1) {
        double var10 = 0.0;
        int var12 = 0;

        for (double[][] var16 : var0) {
            if (!(signedTurn(var16, 414713631) >= 0.0)) {
                int var44 = var16.length - 1;
                double[] var17 = var16[var44];
                double var47 = var17[0];
                double var56 = var17[1];
                double var51 = Math.hypot(var47, var56) * 0.7;
                var10 += var51;
                var12++;
            }
        }

        double var39;
        if (var12 > 0) {
            double var54 = (double)var12;
            var39 = var10 / var54;
        } else {
            var39 = 0.0;
        }

        return var39;
    }

    private static double hubFor(double var0, int var2) {
        return var0 * 0.7 + 0.1;
    }

    public EntityMotifs(BlissPlugin var1, int var2) {
        LinkedHashMap var4 = new LinkedHashMap();
        Map var13 = Collections.synchronizedMap(var4);
        this.assigned = var13;
        HashMap var14 = new HashMap();
        this.snakes = var14;
        HashMap var15 = new HashMap();
        this.lastAt = var15;
        LinkedHashMap var16 = new LinkedHashMap();
        this.viewers = var16;
        this.plugin = var1;
        File var19 = var1.getDataFolder();
        File var18 = new File(var19, "entitymotifs.yml");
        this.file = var18;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::tick;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 1L, 1L);
        this.task = var10;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }

        this.snakes.clear();
        this.lastAt.clear();
        this.viewers.clear();
        Iterator var4 = this.assigned.keySet().iterator();

        while (var4.hasNext()) {
            Player var6 = Bukkit.getPlayer((UUID)var4.next());
            if (var6 != null) {
                PotionEffect var3 = motifInvisibility(238787098);
                GrantedEffect.stripLingering(var6, var3, 827997716);
                restoreGear(var6, 662574947);
            }
        }
    }

    public static List<String> motifs(int var0) {
        Set var6 = MOTIFS.keySet();
        ArrayList var1 = new ArrayList(var6);
        Collections.sort(var1);
        return var1;
    }

    public static boolean known(String var0, int var1) {
        if (var0 != null) {
            Map var4 = MOTIFS;
            String var7 = var0.toLowerCase();
            if (var4.containsKey(var7)) {
                return true;
            }
        }

        return false;
    }

    public String motifOf(Player var1, int var2) {
        Map var5 = this.assigned;
        UUID var8 = var1.getUniqueId();
        return (String)var5.get(var8);
    }

    public Map<UUID, String> all(int var1) {
        return Collections.unmodifiableMap(this.assigned);
    }

    public void set(Player var1, String var2) {
        List var6 = List.of();
        this.set(var1, var2, var6, 1623379796);
    }

    public void set(Player var1, String var2, List<UUID> var3, int var4) {
        UUID var8 = var1.getUniqueId();
        Map var11 = this.assigned;
        String var27 = var2.toLowerCase();
        var11.put(var8, var27);
        if (var3 != null && !var3.isEmpty()) {
            Map var17 = this.viewers;
            List var29 = List.copyOf(var3);
            var17.put(var8, var29);
        } else {
            Map var23 = this.viewers;
            var23.remove(var8);
        }

        hide(var1, 982250141);
        bareGear(var1, 2013177493);
        this.save(1255408554);
    }

    public List<UUID> viewersOf(UUID var1, int var2) {
        Map var6 = this.viewers;
        List var5 = (List)var6.get(var1);
        List var10;
        if (var5 == null) {
            var10 = List.of();
        } else {
            var10 = var5;
        }

        return var10;
    }

    private List<Player> audienceFor(UUID var1, int var2) {
        Map var11 = this.viewers;
        List var6 = (List)var11.get(var1);
        if (var6 != null && !var6.isEmpty()) {
            int var35 = var6.size();
            ArrayList var18 = new ArrayList(var35);
            ArrayList var7 = var18;
            Iterator var8 = var6.iterator();

            while (var8.hasNext()) {
                Player var10 = Bukkit.getPlayer((UUID)var8.next());
                if (var10 != null && var10.isOnline()) {
                    var7.add(var10);
                }
            }

            return var7;
        } else {
            return null;
        }
    }

    public boolean clear(UUID var1, int var2) {
        Map var6 = this.assigned;
        if (var6.remove(var1) == null) {
            return false;
        } else {
            Map var10 = this.viewers;
            var10.remove(var1);
            Map var13 = this.snakes;
            var13.remove(var1);
            Map var16 = this.lastAt;
            var16.remove(var1);
            Player var5 = Bukkit.getPlayer(var1);
            if (var5 != null) {
                PotionEffect var28 = motifInvisibility(238787098);
                GrantedEffect.stripLingering(var5, var28, 827997716);
                restoreGear(var5, 662574947);
            }

            this.save(1255408554);
            return true;
        }
    }

    private static PotionEffect motifInvisibility(int var0) {
        PotionEffectType var2 = PotionEffectType.INVISIBILITY;
        return new PotionEffect(var2, -1, 0, false, false, false);
    }

    private static void hide(Player var0, int var1) {
        PotionEffect var3 = motifInvisibility(238787098);
        var0.addPotionEffect(var3);
    }

    private static void bareGear(Player var0, int var1) {
        EnumMap var2 = new EnumMap(EquipmentSlot.class);
        EnumMap var5 = var2;

        for (EquipmentSlot var9 : GEAR) {
            Material var4 = Material.AIR;
            ItemStack var38 = new ItemStack(var4);
            var5.put(var9, var38);
        }

        for (Player var40 : Bukkit.getOnlinePlayers()) {
            if (!var40.equals(var0) && var40.canSee(var0)) {
                var40.sendEquipmentChange(var0, var5);
            }
        }
    }

    private static void restoreGear(Player var0, int var1) {
        EntityEquipment var5 = var0.getEquipment();
        if (var5 != null) {
            EnumMap var14 = new EnumMap(EquipmentSlot.class);
            EnumMap var6 = var14;

            for (EquipmentSlot var10 : GEAR) {
                ItemStack var11 = var5.getItem(var10);
                ItemStack var47;
                if (var11 == null) {
                    Material var4 = Material.AIR;
                    var47 = new ItemStack(var4);
                } else {
                    var47 = var11;
                }

                var6.put(var10, var47);
            }

            for (Player var49 : Bukkit.getOnlinePlayers()) {
                if (!var49.equals(var0) && var49.canSee(var0)) {
                    var49.sendEquipmentChange(var0, var6);
                }
            }
        }
    }

    private void tick() {
        if (!this.assigned.isEmpty()) {
            int var51 = this.pass;
            int var62 = var51 + 1;
            this.pass = var62;
            int var2 = var51;
            boolean var14;
            if (var51 % 20 == 0) {
                var14 = true;
            } else {
                var14 = false;
            }

            boolean var3 = var14;

            for (Entry var5 : this.assigned.entrySet()) {
                Player var6 = Bukkit.getPlayer((UUID)var5.getKey());
                if (var6 != null && var6.isOnline()) {
                    Map var31 = MOTIFS;
                    Object var54 = var5.getValue();
                    EntityMotifs.Motif var7 = (EntityMotifs.Motif)var31.get(var54);
                    if (var7 != null) {
                        if (var3) {
                            PotionEffectType var55 = PotionEffectType.INVISIBILITY;
                            if (!var6.hasPotionEffect(var55)) {
                                hide(var6, 982250141);
                            }
                        }

                        if (var2 % 5 == 0) {
                            bareGear(var6, 2013177493);
                        }

                        UUID var59 = (UUID)var5.getKey();
                        List var8 = this.audienceFor(var59, 683924392);
                        Location var45 = var6.getLocation();
                        jet(var45, var7, var2, var8, 764494123);
                        if (var7.snakeRadius() > 0.0) {
                            this.drawSnakes(var6, var7, var8, 721806578);
                        }
                    }
                }
            }
        }
    }

    private static void jet(Location var0, EntityMotifs.Motif var1, int var2, List<Player> var3, int var4) {
        World var26 = var0.getWorld();
        if (var26 != null) {
            Vector var27 = var0.getDirection().setY(0);
            if (var27.lengthSquared() < 1.0E-6) {
                Vector var67 = new Vector(0, 0, 1);
                var27 = var67;
            }

            var27.normalize();
            Vector var70 = new Vector(0, 1, 0);
            Vector var28 = var70;
            Vector var72 = var27.clone();
            Vector var29 = var72.crossProduct(var70).normalize();
            double var30 = var0.getX();
            double var79 = var0.getY();
            double var191 = var1.hubHeight();
            double var32 = var79 + var191;
            double var34 = var0.getZ();
            EntityMotifs.Spoke[] var36 = var1.spokes();
            if (var36 != null) {
                for (EntityMotifs.Spoke var40 : var36) {
                    double var97 = var40.angle();
                    double var194 = var40.spin();
                    double var237 = (double)var2;
                    double var196 = var194 * var237;
                    double var41 = var97 + var196;
                    double var44 = Math.cos(var41);
                    double var47 = Math.sin(var41);
                    double var110 = var29.getX();
                    double var112 = var110 * var44;
                    double var201 = var28.getX();
                    double var203 = var201 * var47;
                    double var50 = var112 + var203;
                    double var117 = var29.getY();
                    double var119 = var117 * var44;
                    double var208 = var28.getY();
                    double var210 = var208 * var47;
                    double var52 = var119 + var210;
                    double var124 = var29.getZ();
                    double var126 = var124 * var44;
                    double var215 = var28.getZ();
                    double var217 = var215 * var47;
                    double var54 = var126 + var217;
                    if (var40.inward()) {
                        double var56 = var40.reach();
                        Particle var222 = var1.particle();
                        double var249 = var50 * var56;
                        double var231 = var30 + var249;
                        double var263 = var52 * var56;
                        double var253 = var32 + var263;
                        double var277 = var54 * var56;
                        double var267 = var34 + var277;
                        double var286 = -var50;
                        double var293 = -var52;
                        double var298 = -var54;
                        double var306 = var56 * 1.5;
                        double var316 = var1.launch();
                        double var308 = var306 * var316;
                        emit(var26, var3, var222, var231, var253, var267, 0, var286, var293, var298, var308, null, 1015704158);
                    } else {
                        Particle var220 = var1.particle();
                        double var300 = var40.reach();
                        double var310 = var1.launch();
                        double var302 = var300 * var310;
                        emit(var26, var3, var220, var30, var32, var34, 0, var50, var52, var54, var302, null, 1015704158);
                    }
                }
            } else {
                double[][][] var319 = var1.arms();
                int var320 = var1.burstEvery();
                if (var320 > 0) {
                    if (var2 % var320 == 0) {
                        double[][][] var322 = var319;
                        int var324 = var319.length;

                        for (byte var326 = 0; var326 < var324; var326 += 1) {
                            double[][] var327 = var322[var326];
                            int var46 = var327.length;

                            for (byte var328 = 0; var328 < var46; var328 += 1) {
                                double[] var49 = var327[var328];
                                throwOne(var26, var3, var1, var29, var28, var30, var32, var34, var49, 957618683);
                            }
                        }
                    }
                } else {
                    byte var321 = 0;

                    while (true) {
                        int var175 = var319.length;
                        if (var321 >= var175) {
                            break;
                        }

                        double[][] var323 = var319[var321];
                        if (var323.length != 0) {
                            int var178 = var321 * 3;
                            int var149 = var2 + var178;
                            int var180 = var323.length;
                            int var325 = Math.floorMod(var149, var180);
                            double[] var289 = var323[var325];
                            throwOne(var26, var3, var1, var29, var28, var30, var32, var34, var289, 957618683);
                        }

                        var321 += 1;
                    }
                }
            }
        }
    }

    private static void throwOne(
        World var0, List<Player> var1, EntityMotifs.Motif var2, Vector var3, Vector var4, double var5, double var7, double var9, double[] var11, int var12
    ) {
        double var35 = var11[0] * 0.7;
        double var37 = var11[1] * 0.7;
        double var57 = var3.getX();
        double var59 = var57 * var35;
        double var98 = var4.getX();
        double var100 = var98 * var37;
        double var39 = var59 + var100;
        double var64 = var3.getY();
        double var66 = var64 * var35;
        double var105 = var4.getY();
        double var107 = var105 * var37;
        double var41 = var66 + var107;
        double var71 = var3.getZ();
        double var73 = var71 * var35;
        double var112 = var4.getZ();
        double var114 = var112 * var37;
        double var43 = var73 + var114;
        double var79 = var39 * var39;
        double var120 = var41 * var41;
        double var81 = var79 + var120;
        double var124 = var43 * var43;
        double var45 = Math.sqrt(var81 + var124);
        if (!(var45 < 1.0E-6)) {
            Particle var129 = var2.particle();
            double var138 = var39 / var45;
            double var142 = var41 / var45;
            double var146 = var43 / var45;
            double var152 = var2.launch();
            double var150 = var45 * var152;
            emit(var0, var1, var129, var5, var7, var9, 0, var138, var142, var146, var150, null, 1015704158);
        }
    }

    private static void emit(
        World var0,
        List<Player> var1,
        Particle var2,
        double var3,
        double var5,
        double var7,
        int var9,
        double var10,
        double var12,
        double var14,
        double var16,
        Object var18,
        int var19
    ) {
        if (var1 == null) {
            Particles.always(var0, var2, var3, var5, var7, var9, var10, var12, var14, var16, var18, true, 882644223);
        } else {
            for (Player var48 : var1) {
                Particles.always(var48, var2, var3, var5, var7, var9, var10, var12, var14, var16, var18, true, 470356725);
            }
        }
    }

    private void drawSnakes(Player var1, EntityMotifs.Motif var2, List<Player> var3, int var4) {
        UUID var27;
        Location var28;
        boolean var65;
        label58: {
            var27 = var1.getUniqueId();
            var28 = var1.getLocation();
            Map<UUID, Location> var55 = this.lastAt;
            Location var157 = var28.clone();
            Location var29 = var55.put(var27, var157);
            if (var29 != null) {
                World var60 = var29.getWorld();
                World var128 = var28.getWorld();
                if (var60 == var128 && var29.distance(var28) > 0.02) {
                    var65 = true;
                    break label58;
                }
            }

            var65 = false;
        }

        if (!var65) {
            Map var68 = this.snakes;
            Function var161 = var1x -> {
                EntityMotifs.Snake[] var6 = new EntityMotifs.Snake[8];
                int var7 = 0;

                while (true) {
                    int var13 = var6.length;
                    if (var7 >= var13) {
                        return var6;
                    }

                    Color var15 = snakeColour(var7, 1779947664);
                    double var17 = var2.snakeRadius();
                    EntityMotifs.Snake var16 = newSnake(var15, var17, 615193322);
                    var6[var7] = var16;
                    var7++;
                }
            };
            EntityMotifs.Snake[] var31 = (EntityMotifs.Snake[])var68.computeIfAbsent(var27, var161);
            Location var72 = var28.clone();
            double var184 = var2.hubHeight();
            Location var32 = var72.add(0.0, var184, 0.0);
            World var33 = var32.getWorld();
            if (var33 != null) {
                ThreadLocalRandom var34 = ThreadLocalRandom.current();

                for (EntityMotifs.Snake var38 : var31) {
                    double var137 = var38.head + 0.05;
                    var38.head = var137;
                    if (var38.head >= Math.PI * 2) {
                        double var141 = var38.head - (Math.PI * 2);
                        var38.head = var141;
                    }

                    if (var38.swept < 1.08) {
                        double var193 = var38.swept + 0.05;
                        double var145 = Math.min(1.08, var193);
                        var38.swept = var145;
                    }

                    double var97 = var38.head;
                    double var167 = var38.swept;
                    double var39 = var97 - var167;
                    int var154 = (int)Math.ceil(var38.swept * 12.0);
                    int var41 = Math.max(1, var154);

                    for (int var42 = 0; var42 <= var41; var42++) {
                        double var106 = (double)var42;
                        double var170 = (double)var41;
                        double var43 = var106 / var170;
                        double var111 = var34.nextDouble();
                        double var204 = 0.85 * var43;
                        double var174 = 0.15 + var204;
                        if (!(var111 > var174)) {
                            double var177 = var38.head;
                            double var179 = var177 - var39;
                            double var181 = var179 * var43;
                            double var45 = var39 + var181;
                            double var47 = Math.cos(var45);
                            double var49 = Math.sin(var45);
                            Particle var183 = Particle.DUST;
                            double var198 = var32.getX();
                            double var231 = var38.rU;
                            double var214 = var47 * var231;
                            double var235 = var38.u.getX();
                            double var216 = var214 * var235;
                            double var200 = var198 + var216;
                            double var238 = var38.rV;
                            double var220 = var49 * var238;
                            double var242 = var38.v.getX();
                            double var222 = var220 * var242;
                            double var202 = var200 + var222;
                            double var225 = var32.getY();
                            double var263 = var38.rU;
                            double var246 = var47 * var263;
                            double var267 = var38.u.getY();
                            double var248 = var246 * var267;
                            double var227 = var225 + var248;
                            double var270 = var38.rV;
                            double var252 = var49 * var270;
                            double var274 = var38.v.getY();
                            double var254 = var252 * var274;
                            double var229 = var227 + var254;
                            double var257 = var32.getZ();
                            double var289 = var38.rU;
                            double var278 = var47 * var289;
                            double var293 = var38.u.getZ();
                            double var280 = var278 * var293;
                            double var259 = var257 + var280;
                            double var296 = var38.rV;
                            double var284 = var49 * var296;
                            double var300 = var38.v.getZ();
                            double var286 = var284 * var300;
                            double var261 = var259 + var286;
                            DustOptions var302 = var38.dust;
                            emit(var33, var3, var183, var202, var229, var261, 1, 0.0, 0.0, 0.0, 0.0, var302, 1015704158);
                        }
                    }
                }
            }
        }
    }

    public void load(int var1) {
        this.assigned.clear();
        if (this.file.isFile()) {
            YamlConfiguration var5 = YamlConfiguration.loadConfiguration(this.file);

            for (String var7 : var5.getKeys(false)) {
                UUID var8;
                try {
                    var8 = UUID.fromString(var7);
                } catch (IllegalArgumentException var98) {
                    Logger var34 = this.plugin.getLogger();
                    String var77 = "entitymotifs.yml: skipping unreadable id " + var7;
                    var34.warning(var77);
                    continue;
                }

                String var62;
                if (var5.isConfigurationSection(var7)) {
                    String var80 = var7 + ".motif";
                    var62 = var5.getString(var80);
                } else {
                    var62 = var5.getString(var7);
                }

                if (var62 != null) {
                    Map var39 = MOTIFS;
                    String var82 = var62.toLowerCase();
                    if (var39.containsKey(var82)) {
                        Map var42 = this.assigned;
                        String var90 = var62.toLowerCase();
                        var42.put(var8, var90);
                        ArrayList var44 = new ArrayList();
                        ArrayList var10 = var44;
                        String var85 = var7 + ".viewers";

                        for (String var12 : var5.getStringList(var85)) {
                            try {
                                UUID var88 = UUID.fromString(var12);
                                var10.add(var88);
                            } catch (IllegalArgumentException var97) {
                            }
                        }

                        if (!var10.isEmpty()) {
                            Map var53 = this.viewers;
                            List var92 = List.copyOf(var10);
                            var53.put(var8, var92);
                        }
                    }
                }
            }

            Iterator var93 = this.assigned.keySet().iterator();

            while (var93.hasNext()) {
                Player var95 = Bukkit.getPlayer((UUID)var93.next());
                if (var95 != null) {
                    hide(var95, 982250141);
                }
            }
        }
    }

    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;

        for (Entry var6 : this.assigned.entrySet()) {
            String var7 = ((UUID)var6.getKey()).toString();
            String var55 = var7 + ".motif";
            Object var62 = var6.getValue();
            var4.set(var55, var62);
            Map var32 = this.viewers;
            Object var57 = var6.getKey();
            List<UUID> var8 = (List<UUID>)var32.get(var57);
            if (var8 != null && !var8.isEmpty()) {
                int var64 = var8.size();
                ArrayList var38 = new ArrayList(var64);
                ArrayList var9 = var38;

                for (UUID var11 : var8) {
                    String var61 = var11.toString();
                    var9.add(var61);
                }

                String var59 = var7 + ".viewers";
                var4.set(var59, var9);
            }
        }

        try {
            File var50 = this.file;
            AtomicYml.save(var4, var50, 1347473142);
        } catch (IOException var67) {
            Logger var22 = this.plugin.getLogger();
            String var53 = "Failed to save entitymotifs.yml: " + var67.getMessage();
            var22.warning(var53);
        }
    }

    static {
        String var0 = "balance";
        double[][][] var2 = BalanceMotif.STROKES;
        Particle var3 = Particle.END_ROD;
        double var35 = hubFor(2.2, 433052385);
        EntityMotifs.Spoke[] var41 = counterWheels(BalanceMotif.STROKES, 438323920);
        EntityMotifs.Motif var1 = new EntityMotifs.Motif(var2, var3, 0.09090909090909091, var35, 0, var41, 0.0);
        String var20 = "manifestation";
        double[][][] var34 = ManifestationMotif.STROKES;
        Particle var6 = Particle.SCULK_SOUL;
        EntityMotifs.Spoke[] var43 = spokesOf(ManifestationMotif.STROKES, true, 40.0, 1152279662);
        EntityMotifs.Motif var27 = new EntityMotifs.Motif(var34, var6, 0.04, 0.9, 0, var43, 2.9259999999999997);
        MOTIFS = Map.of(var0, var1, var20, var27);
        EquipmentSlot[] var19 = new EquipmentSlot[6];
        EquipmentSlot var28 = EquipmentSlot.HAND;
        var19[0] = var28;
        EquipmentSlot var29 = EquipmentSlot.OFF_HAND;
        var19[1] = var29;
        EquipmentSlot var30 = EquipmentSlot.HEAD;
        var19[2] = var30;
        EquipmentSlot var31 = EquipmentSlot.CHEST;
        var19[3] = var31;
        EquipmentSlot var32 = EquipmentSlot.LEGS;
        var19[4] = var32;
        EquipmentSlot var33 = EquipmentSlot.FEET;
        var19[5] = var33;
        GEAR = var19;
    }

    private static record Motif(
        double[][][] arms, Particle particle, double launch, double hubHeight, int burstEvery, EntityMotifs.Spoke[] spokes, double snakeRadius
    ) {
    }

    private static final class Snake {
        Vector u;
        Vector v;
        double rU;
        double rV;
        double head;
        double swept;
        DustOptions dust;
    }

    private static record Spoke(double angle, double reach, double spin, boolean inward) {
    }
}
