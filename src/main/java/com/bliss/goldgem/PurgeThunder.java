package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class PurgeThunder {
    private static final double[][][] MAIN;
    private static final double[][][] BRANCH;
    private static final double SCALE_Y = 2.0;
    private static final double SCALE = 2.0;
    private static final double STEP_MAIN = 0.5;
    private static final double STEP_BRANCH = 0.75;
    private static final double STEP_SPARK = 0.5;
    private static final double JOINT = 3.4;
    private static final double JIT_MAIN = 0.7;
    private static final double JIT_BRANCH = 0.44;
    private static final double JIT_DEPTH = 0.9;
    private static final double DEPTH_MAX = 3.0;
    private static final double JIT_DEPTH_BRANCH = 3.4;
    private static final double DEPTH_MAX_BRANCH = 14.0;
    private static final double ATTACH = 6.0;
    private static final double SWING_MAIN = 0.12;
    private static final double SWING_BRANCH = 0.25;
    private static final double GROUND_REACH = 12.0;
    private static final double TWIG_MAIN = 5.2;
    private static final double TWIG_BRANCH = 3.6;
    private static final double STAND_OFF = 24.0;
    private static final double LIFT = 60.0;
    private static final int REVEAL_TICKS = 14;
    private static final int STRIKE_TICK = 14;
    private static final int REFRESH = 10;
    private static final int HOLD_TICKS = 420;
    private static final int STORM_TAIL = 60;
    private static final int CLUSTER_MAIN = 1;
    private static final int CLUSTER_BRANCH = 1;
    private static final double SPREAD_MAIN = 0.0;
    private static final double SPREAD_BRANCH = 0.0;
    private static final int BURST_EVERY = 20;
    private static final int BURST_COUNT = 5;
    private static final double BURST_SPREAD = 0.0;
    private static final int BURST_STROKE = 0;
    private static final double BURST_AT = 0.5;
    private static final boolean FLASH_NEEDS_COLOR;

    private PurgeThunder() {
    }

    public static void strike(BlissPlugin var0, Location var1, int var2) {
        final World var31 = var1.getWorld();
        if (var31 != null) {
            ThreadLocalRandom var32 = ThreadLocalRandom.current();
            if (var0.goldenDream(2108340810) != null) {
                GoldenDreamWorld var106 = var0.goldenDream(2108340810).dream(2117018474);
                if (var106.isMemoryWorld(var31, 477985501)) {
                    var0.goldenDream(2108340810).dream(2117018474).storm(494, 1091629328);
                }
            }

            Player var33 = null;
            double var34 = Double.MAX_VALUE;

            for (Player var37 : var31.getPlayers()) {
                Location var529 = var37.getLocation();
                double var1078 = var529.distanceSquared(var1);
                if (var1078 < var34) {
                    var34 = var1078;
                    var33 = var37;
                }
            }

            double var1076 = 1.0;
            double var38 = 0.0;
            if (var33 != null) {
                double var125 = var1.getX();
                double var685 = var33.getLocation().getX();
                double var40 = var125 - var685;
                double var130 = var1.getZ();
                double var689 = var33.getLocation().getZ();
                double var42 = var130 - var689;
                double var44 = Math.hypot(var40, var42);
                if (var44 > 0.5) {
                    var1076 = var40 / var44;
                    var38 = var42 / var44;
                } else {
                    Vector var1086 = var33.getLocation().getDirection();
                    double var48 = var1086.getX();
                    double var52 = var1086.getZ();
                    double var55 = Math.hypot(var48, var52);
                    if (var55 > 0.05) {
                        var1076 = var48 / var55;
                        var38 = var52 / var55;
                    }
                }
            }

            double var1080 = -var38;
            double var1082 = var1076;
            double var156 = var1.getX();
            double var701 = var1076 * 24.0;
            double var1084 = var156 + var701;
            double var161 = var1.getZ();
            double var705 = var38 * 24.0;
            double var46 = var161 + var705;
            double var50 = var1.getY();
            int var546 = (int)Math.floor(var1084) >> 4;
            int var713 = (int)Math.floor(var46) >> 4;
            if (var31.isChunkLoaded(var546, var713)) {
                int var551 = (int)Math.floor(var1084);
                int var718 = (int)Math.floor(var46);
                int var54 = var31.getHighestBlockYAt(var551, var718);
                int var553 = var31.getMinHeight();
                if (var54 > var553) {
                    var50 = (double)(var54 + 1);
                }
            }

            double var1087 = var1084;
            double var57 = var50 + 60.0;
            double var59 = var46;
            ArrayList var185 = new ArrayList();
            ArrayList var61 = var185;
            ArrayList var186 = new ArrayList();
            ArrayList var62 = var186;

            for (double[][] var66 : MAIN) {
                double[][] var558 = taller(var66, 585454340);
                var61.add(var558);
                Boolean var560 = true;
                var62.add(var560);
            }

            double[][][] var1089 = BRANCH;
            int var1091 = var1089.length;

            for (byte var1094 = 0; var1094 < var1091; var1094 += 1) {
                double[][] var1097 = var1089[var1094];
                double[][] var678 = taller(var1097, 585454340);
                var61.add(var678);
                Boolean var680 = false;
                var62.add(var680);
            }

            Integer[] var1090 = new Integer[var61.size()];
            byte var1092 = 0;

            while (true) {
                int var563 = var1090.length;
                if (var1092 >= var563) {
                    Comparator<Integer> var567 = Comparator.comparingDouble(var1x -> {
                        int var10 = var1x;
                        return -topOf((double[][])var61.get(var10), 1220442145);
                    });
                    Arrays.sort(var1090, var567);
                    int var1093 = var61.size();
                    ArrayList var212 = new ArrayList(var1093);
                    ArrayList var1095 = var212;
                    int[] var1096 = new int[var1093];
                    boolean[] var67 = new boolean[var1093];
                    boolean[] var68 = new boolean[var1093];

                    for (int var69 = 0; var69 < var1093; var69++) {
                        int var70 = var1090[var69];
                        boolean var71 = (Boolean)var62.get(var70);
                        var67[var69] = var71;
                        double[][] var231 = (double[][])var61.get(var70);
                        List var72 = jointed(var231, var71, var32, 660896027);
                        int var576 = var72.size() - 1;
                        double[] var73 = (double[])var72.get(var576);
                        if (var71 && var73[1] > 0.0 && var73[1] <= 24.0) {
                            double[] var580 = new double[3];
                            double var870 = var73[0];
                            double var960 = (var32.nextDouble() - 0.5) * 1.5 * 2.0;
                            double var872 = var870 + var960;
                            var580[0] = var872;
                            var580[1] = 0.0;
                            double var877 = var73[2];
                            var580[2] = var877;
                            var72.add(var580);
                            int var583 = var72.size() - 1;
                            var73 = (double[])var72.get(var583);
                        }

                        int var74 = var72.size();
                        var1096[var69] = var74;
                        ArrayList var253 = new ArrayList();
                        var253.add(var72);
                        boolean var824;
                        if (var73[1] <= 0.5) {
                            var824 = true;
                        } else {
                            var824 = false;
                        }

                        var68[var69] = var824;
                        if (!var68[var69]) {
                            List var589 = twig(var72, var71, var32, 250909636);
                            var72.addAll(var589);
                        }

                        if (var71) {
                            int var831 = var74 - 2;
                            int var739 = Math.max(1, var831);
                            int var591 = var32.nextInt(var739);
                            int var76 = 1 + var591;
                            ArrayList var264 = new ArrayList();
                            double[] var594 = (double[])var72.get(var76);
                            var264.add(var594);
                            int var833 = var76 + 1;
                            List var596 = var72.subList(0, var833);
                            List var597 = twig(var596, false, var32, 250909636);
                            var264.addAll(var597);
                            var253.add(var264);
                        }

                        var1095.add(var253);
                    }

                    int[] var1098 = new int[var1093];
                    int[] var1099 = new int[var1093];

                    for (byte var1100 = 0; var1100 < var1093; var1100 += 1) {
                        var1098[var1100] = -1;
                        var1099[var1100] = -1;
                        double[] var1104 = (double[])((List)((List)var1095.get(var1100)).get(0)).get(0);
                        double var1106 = 6.0;

                        for (byte var1113 = 0; var1113 < var1093; var1113 += 1) {
                            if (var1113 != var1100) {
                                List var1117 = (List)((List)var1095.get(var1113)).get(0);
                                byte var1121 = 0;

                                while (true) {
                                    int var671 = var1096[var1113];
                                    if (var1121 >= var671) {
                                        break;
                                    }

                                    double var482 = ((double[])var1117.get(var1121))[0];
                                    double var801 = var1104[0];
                                    double var484 = var482 - var801;
                                    double var806 = ((double[])var1117.get(var1121))[1];
                                    double var920 = var1104[1];
                                    double var808 = var806 - var920;
                                    double var1125 = Math.hypot(var484, var808);
                                    if (var1125 < var1106) {
                                        var1106 = var1125;
                                        var1098[var1100] = var1113;
                                        var1099[var1100] = var1121;
                                    }

                                    var1121 += 1;
                                }
                            }
                        }
                    }

                    ArrayList var279 = new ArrayList(var1093);
                    ArrayList var1101 = var279;

                    for (byte var1102 = 0; var1102 < var1093; var1102 += 1) {
                        var1101.add(null);
                    }

                    boolean[] var1103 = new boolean[var1093];
                    int var1105 = var1093;

                    while (var1105 > 0) {
                        boolean var1109 = false;

                        for (byte var1111 = 0; var1111 < var1093; var1111 += 1) {
                            if (!var1103[var1111]) {
                                int var1116 = var1098[var1111];
                                if (var1116 == -1 || var1103[var1116]) {
                                    double[] var1120 = (double[])((List)((List)var1095.get(var1111)).get(0)).get(0);
                                    double[] var538;
                                    if (var1116 == -1) {
                                        var538 = new double[3];
                                        double var850 = var1120[0];
                                        var538[0] = var850;
                                        double var853 = var1120[1];
                                        var538[1] = var853;
                                        double var856 = var1120[2];
                                        var538[2] = var856;
                                    } else {
                                        List var454 = (List)((List)var1101.get(var1116)).get(0);
                                        int var659 = var1099[var1111];
                                        var538 = (double[])var454.get(var659);
                                    }

                                    double[] var1124 = var538;
                                    double var397;
                                    if (var67[var1111]) {
                                        var397 = 0.12;
                                    } else {
                                        var397 = 0.25;
                                    }

                                    double var781 = var32.nextDouble() * 2.0 - 1.0;
                                    double var1128 = var397 * var781;
                                    double var1134 = Math.cos(var1128);
                                    double var84 = Math.sin(var1128);
                                    ArrayList var409 = new ArrayList();
                                    ArrayList var1136 = var409;

                                    for (List<double[]> var89 : (List<List<double[]>>)var1095.get(var1111)) {
                                        int var786 = var89.size();
                                        ArrayList var423 = new ArrayList(var786);
                                        ArrayList var90 = var423;

                                        for (double[] var92 : var89) {
                                            double var434 = var92[0];
                                            double var788 = var1120[0];
                                            double var93 = var434 - var788;
                                            double var439 = var92[1];
                                            double var791 = var1120[1];
                                            double var95 = var439 - var791;
                                            double var444 = var92[2];
                                            double var794 = var1120[2];
                                            double var97 = var444 - var794;
                                            double[] var655 = new double[3];
                                            double var901 = var1124[0];
                                            double var1015 = var93 * var1134;
                                            double var903 = var901 + var1015;
                                            double var1019 = var97 * var84;
                                            double var905 = var903 - var1019;
                                            var655[0] = var905;
                                            double var908 = var1124[1];
                                            double var910 = var908 + var95;
                                            var655[1] = var910;
                                            double var913 = var1124[2];
                                            double var1025 = var93 * var84;
                                            double var915 = var913 + var1025;
                                            double var1029 = var97 * var1134;
                                            double var917 = var915 + var1029;
                                            var655[2] = var917;
                                            var90.add(var655);
                                        }

                                        var1136.add(var90);
                                    }

                                    var1101.set(var1111, var1136);
                                    var1103[var1111] = true;
                                    var1105 += -1;
                                    var1109 = true;
                                }
                            }
                        }

                        if (!var1109) {
                            int var1112 = 0;

                            while (var1103[var1112]) {
                                var1112 += 1;
                            }

                            for (boolean[] var1115 = new boolean[var1093]; !var1115[var1112]; var1112 = var1098[var1112]) {
                                var1115[var1112] = true;
                            }

                            int var1119 = var1112;

                            for (int var1123 = var1098[var1112]; var1123 != var1112; var1123 = var1098[var1123]) {
                                if (var1123 < var1119) {
                                    var1119 = var1123;
                                }
                            }

                            var1098[var1119] = -1;
                        }
                    }

                    ArrayList var286 = new ArrayList();
                    final ArrayList var1108 = var286;
                    ArrayList var287 = new ArrayList();
                    final ArrayList var1110 = var287;
                    ArrayList var288 = new ArrayList();
                    final ArrayList var1114 = var288;
                    ArrayList var289 = new ArrayList();
                    ArrayList var1118 = var289;

                    for (int var78 = 0; var78 < var1093; var78++) {
                        boolean var1127 = var67[var78];

                        for (List<double[]> var81 : (List<List<double[]>>)var1101.get(var78)) {
                            double var682;
                            if (var1127) {
                                var682 = 0.5;
                            } else {
                                var682 = 0.75;
                            }

                            dots(var81, var682, var1127, var1108, var1087, var57, var59, var1080, var1082, var1076, var38, 942545869);
                            if (var1127) {
                                dots(var81, 0.5, true, var1110, var1087, var57, var59, var1080, var1082, var1076, var38, 942545869);
                            }
                        }

                        if (var1090[var78] == 0) {
                            List var345 = (List)((List)var1101.get(var78)).get(0);
                            int var765 = var1096[var78];
                            List var346 = var345.subList(0, var765);
                            dots(var346, 0.5, true, var1118, var1087, var57, var59, var1080, var1082, var1076, var38, 942545869);
                        }

                        if (var68[var78]) {
                            List var1133 = (List)((List)var1101.get(var78)).get(0);
                            int var768 = var1096[var78] - 1;
                            double[] var626 = (double[])var1133.get(var768);
                            double[] var627 = place(var626, var1087, var57, var59, var1080, var1082, var1076, var38, 256292168);
                            var1114.add(var627);
                        }
                    }

                    ArrayList var539;
                    if (var1118.isEmpty()) {
                        var539 = var1108;
                    } else {
                        var539 = var1118;
                    }

                    final Location var1130;
                    if (var539.isEmpty()) {
                        Location var296 = new Location(var31, var1087, var57, var59);
                        var1130 = var296;
                    } else {
                        int var608 = var539.size() - 1;
                        int var762 = (int)((double)var539.size() * 0.5);
                        int var609 = Math.min(var608, var762);
                        PurgeThunder.Dot var1131 = (PurgeThunder.Dot)var539.get(var609);
                        double var841 = var1131.x;
                        double var942 = var1131.y;
                        double var1034 = var1131.z;
                        Location var323 = new Location(var31, var841, var942, var1034);
                        var1130 = var323;
                    }

                    double var79 = Double.MAX_VALUE;
                    double var82 = -Double.MAX_VALUE;

                    for (PurgeThunder.Dot var87 : (List<PurgeThunder.Dot>)var1108) {
                        double var746 = var87.y;
                        var79 = Math.min(var79, var746);
                        double var749 = var87.y;
                        var82 = Math.max(var82, var749);
                    }

                    Logger var317 = var0.getLogger();
                    Locale var603 = Locale.ROOT;
                    String var751 = "purge thunder: origin (%.1f, %.1f, %.1f) in %s, viewer %s at %.1f blocks, %d dots from y %.1f to %.1f, %d sparks, %d tips, burst (%.1f, %.1f, %.1f) on %d trunk dots";
                    Object[] var838 = new Object[15];
                    Double var964 = var1087;
                    var838[0] = var964;
                    Double var967 = var57;
                    var838[1] = var967;
                    Double var970 = var59;
                    var838[2] = var970;
                    String var972 = var31.getName();
                    var838[3] = var972;
                    String var1010;
                    if (var33 == null) {
                        var1010 = "none";
                    } else {
                        var1010 = var33.getName();
                    }

                    var838[4] = var1010;
                    double var1031;
                    if (var33 == null) {
                        var1031 = 0.0;
                    } else {
                        var1031 = Math.sqrt(var34);
                    }

                    Double var976 = var1031;
                    var838[5] = var976;
                    Integer var979 = var1108.size();
                    var838[6] = var979;
                    Double var982 = var79;
                    var838[7] = var982;
                    Double var985 = var82;
                    var838[8] = var985;
                    Integer var988 = var1110.size();
                    var838[9] = var988;
                    Integer var991 = var1114.size();
                    var838[10] = var991;
                    Double var995 = var1130.getX();
                    var838[11] = var995;
                    Double var999 = var1130.getY();
                    var838[12] = var999;
                    Double var1003 = var1130.getZ();
                    var838[13] = var1003;
                    Integer var1006 = var1118.size();
                    var838[14] = var1006;
                    String var604 = String.format(var603, var751, var838);
                    var317.info(var604);
                    BukkitRunnable var318 = new BukkitRunnable() {
                        int t = 0;
                        int i = 0;

                        public void run() {
                            if (this.t < 14) {
                                int var24 = var1108.size();
                                int var90 = this.t + 1;
                                int var19 = (int)Math.round((double)(var24 * var90) / 14.0);

                                while (true) {
                                    int var34 = this.i;
                                    if (var34 >= var19) {
                                        break;
                                    }

                                    int var36 = this.i;
                                    int var94 = var1108.size();
                                    if (var36 >= var94) {
                                        break;
                                    }

                                    World var38 = var31;
                                    List var96 = var1108;
                                    int var119 = this.i;
                                    PurgeThunder.Dot var98 = (PurgeThunder.Dot)var96.get(var119);
                                    PurgeThunder.glow(var38, var98, 2025921454);
                                    int var100 = this.i + 1;
                                    this.i = var100;
                                }
                            }

                            if (this.t == 14) {
                                for (double[] var20 : (List<double[]>)var1114) {
                                    World var51 = var31;
                                    World var126 = var31;
                                    double var129 = var20[0];
                                    double var136 = var20[1];
                                    double var143 = var20[2];
                                    Location var102 = new Location(var126, var129, var136, var143);
                                    PurgeThunder.flash(var51, var102, 1689585647);
                                }

                                for (PurgeThunder.Dot var151 : (List<PurgeThunder.Dot>)var1110) {
                                    World var61 = var31;
                                    Particle var103 = Particle.ELECTRIC_SPARK;
                                    double var122 = var151.x;
                                    double var132 = var151.y;
                                    double var139 = var151.z;
                                    Particles.spawn(var61, var103, var122, var132, var139, 1, 0.0, 0.0, 0.0, 0.0, null, true, 954482185);
                                }
                            }

                            if (this.t >= 14 && (this.t - 14) % 20 == 0) {
                                World var69 = var31;
                                Location var108 = var1130;
                                PurgeThunder.flash(var69, var108, 5, 0.0, 0.0, 0.0, 1249694383);
                            }

                            if (this.t >= 14 && (this.t - 14) % 10 == 0) {
                                for (PurgeThunder.Dot var152 : (List<PurgeThunder.Dot>)var1108) {
                                    World var85 = var31;
                                    PurgeThunder.glow(var85, var152, 2025921454);
                                }
                            }

                            int var114 = this.t + 1;
                            this.t = var114;
                            if (var114 > 434) {
                                this.cancel();
                            }
                        }
                    };
                    var318.runTaskTimer(var0, 0L, 1L);
                    return;
                }

                Integer var722 = Integer.valueOf(var1092);
                var1090[var1092] = var722;
                var1092 += 1;
            }
        }
    }

    private static double topOf(double[][] var0, int var1) {
        double var7 = -Double.MAX_VALUE;

        for (double[] var12 : var0) {
            double var27 = var12[1];
            var7 = Math.max(var7, var27);
        }

        return var7;
    }

    private static List<double[]> jointed(double[][] var0, boolean var1, ThreadLocalRandom var2, int var3) {
        double[][] var13 = var0;
        double var45 = var0[0][1];
        int var232 = var0.length - 1;
        double var182 = var0[var232][1];
        if (var45 < var182) {
            var13 = new double[var0.length][];
            int var14 = 0;

            while (true) {
                int var153 = var0.length;
                if (var14 >= var153) {
                    break;
                }

                int var236 = var0.length - 1;
                int var237 = var236 - var14;
                double[] var185 = var0[var237];
                var13[var14] = var185;
                var14++;
            }
        }

        double var147;
        if (var1) {
            var147 = 0.7;
        } else {
            var147 = 0.44;
        }

        double var357 = var147;
        double var56;
        if (var1) {
            var56 = 0.9;
        } else {
            var56 = 3.4;
        }

        double var15 = var56;
        double var149;
        if (var1) {
            var149 = 3.0;
        } else {
            var149 = 14.0;
        }

        double var17 = var149;
        ArrayList var59 = new ArrayList();
        ArrayList var19 = var59;
        double var145;
        if (var1) {
            var145 = 0.0;
        } else {
            double var66 = var2.nextDouble() * 2.0 - 1.0;
            var145 = var66 * var149 * 0.6;
        }

        double var20 = var145;
        double[] var156 = new double[3];
        double var253 = var13[0][0];
        var156[0] = var253;
        double var257 = var13[0][1];
        var156[1] = var257;
        var156[2] = var145;
        var59.add(var156);
        int var22 = 1;

        while (true) {
            int var158 = var13.length;
            if (var22 >= var158) {
                return var19;
            }

            int var160 = var22 - 1;
            double var23 = var13[var160][0];
            int var163 = var22 - 1;
            double var25 = var13[var163][1];
            double var27 = var13[var22][0];
            double var29 = var13[var22][1];
            double var92 = var27 - var23;
            double var200 = var29 - var25;
            double var31 = Math.hypot(var92, var200);
            int var175 = (int)Math.round(var31 / 3.4);
            int var33 = Math.max(1, var175);
            double var102 = -(var29 - var25);
            double var206 = Math.max(var31, 1.0E-6);
            double var34 = var102 / var206;
            double var108 = var27 - var23;
            double var212 = Math.max(var31, 1.0E-6);
            double var36 = var108 / var212;

            for (int var38 = 1; var38 < var33; var38++) {
                double var115 = (double)var38;
                double var215 = (double)var33;
                double var39 = var115 / var215;
                double var124 = (var2.nextDouble() - 0.5) * 2.0;
                double var41 = var124 * var357;
                double var130 = -var17;
                double var302 = (var2.nextDouble() - 0.5) * 2.0;
                double var304 = var302 * var15;
                double var269 = var20 + var304;
                double var225 = Math.min(var17, var269);
                var20 = Math.max(var130, var225);
                double[] var178 = new double[3];
                double var308 = var27 - var23;
                double var310 = var308 * var39;
                double var273 = var23 + var310;
                double var314 = var34 * var41;
                double var275 = var273 + var314;
                var178[0] = var275;
                double var318 = var29 - var25;
                double var320 = var318 * var39;
                double var279 = var25 + var320;
                double var324 = var36 * var41;
                double var281 = var279 + var324;
                var178[1] = var281;
                var178[2] = var20;
                var19.add(var178);
            }

            double var138 = -var17;
            double var331 = (var2.nextDouble() - 0.5) * 2.0;
            double var333 = var331 * var15;
            double var287 = var20 + var333;
            double var229 = Math.min(var17, var287);
            var20 = Math.max(var138, var229);
            double[] var180 = new double[]{var27, var29, var20};
            var19.add(var180);
            var22++;
        }
    }

    private static List<double[]> twig(List<double[]> var0, boolean var1, ThreadLocalRandom var2, int var3) {
        ArrayList var4 = new ArrayList();
        ArrayList var10 = var4;
        if (var0.size() < 2) {
            return var4;
        } else {
            int var161 = var0.size() - 2;
            double[] var11 = (double[])var0.get(var161);
            int var164 = var0.size() - 1;
            double[] var12 = (double[])var0.get(var164);
            double var48 = var12[0];
            double var176 = var11[0];
            double var13 = var48 - var176;
            double var53 = var12[1];
            double var179 = var11[1];
            double var15 = var53 - var179;
            double var17 = Math.hypot(var13, var15);
            if (var17 < 1.0E-6) {
                var13 = 0.0;
                var15 = -1.0;
                var17 = 1.0;
            }

            double var262 = var13 / var17;
            double var264 = var15 / var17;
            byte var157;
            if (var2.nextBoolean()) {
                var157 = 1;
            } else {
                var157 = -1;
            }

            double var80 = (double)var157;
            double var232 = var2.nextDouble() * 0.5;
            double var191 = 0.35 + var232;
            double var19 = var80 * var191;
            double var21 = var12[0];
            double var23 = var12[1];
            int var170 = var2.nextInt(2);
            int var25 = 2 + var170;
            double var93;
            if (var1) {
                var93 = 5.2;
            } else {
                var93 = 3.6;
            }

            double var26 = var93;

            for (int var28 = 0; var28 < var25; var28++) {
                double var29 = Math.cos(var19);
                double var31 = Math.sin(var19);
                double var108 = var262 * var29;
                double var198 = var264 * var31;
                double var33 = var108 - var198;
                double var114 = var262 * var31;
                double var204 = var264 * var29;
                double var268 = var114 + var204 - 0.25;
                double var37 = Math.hypot(var33, var268);
                double var266 = var33 / var37;
                double var270 = var268 / var37;
                double var216 = var266 * var26;
                var21 += var216;
                double var220 = var270 * var26;
                var23 += var220;
                double[] var173 = new double[]{var21, var23, 0.0};
                double var251 = (var2.nextDouble() - 0.5) * 2.0;
                double var260;
                if (var1) {
                    var260 = 0.9;
                } else {
                    var260 = 3.4;
                }

                double var253 = var251 * var260;
                var173[2] = var253;
                var10.add(var173);
                var262 = var266;
                var264 = var270;
                var26 *= 0.7;
                var19 *= -0.6;
            }

            return var10;
        }
    }

    private static void dots(
        List<double[]> var0,
        double var1,
        boolean var3,
        List<PurgeThunder.Dot> var4,
        double var5,
        double var7,
        double var9,
        double var11,
        double var13,
        double var15,
        double var17,
        int var19
    ) {
        double var36 = 0.0;
        int var38 = 1;

        while (true) {
            int var94 = var0.size();
            if (var38 >= var94) {
                return;
            }

            int var96 = var38 - 1;
            double[] var39 = (double[])var0.get(var96);
            double[] var40 = (double[])var0.get(var38);
            double var57 = var40[0];
            double var103 = var39[0];
            double var61 = sq(var57 - var103, 1014639907);
            double var106 = var40[1];
            double var154 = var39[1];
            double var110 = sq(var106 - var154, 1014639907);
            double var63 = var61 + var110;
            double var113 = var40[2];
            double var157 = var39[2];
            double var117 = sq(var113 - var157, 1014639907);
            double var41 = Math.sqrt(var63 + var117);
            if (!(var41 < 1.0E-6)) {
                double var43 = var36;

                while (var43 <= var41) {
                    double var45 = var43 / var41;
                    double[] var82 = new double[3];
                    double var135 = var39[0];
                    double var165 = var40[0];
                    double var193 = var39[0];
                    double var167 = var165 - var193;
                    double var169 = var167 * var45;
                    double var137 = var135 + var169;
                    var82[0] = var137;
                    double var140 = var39[1];
                    double var172 = var40[1];
                    double var198 = var39[1];
                    double var174 = var172 - var198;
                    double var176 = var174 * var45;
                    double var142 = var140 + var176;
                    var82[1] = var142;
                    double var145 = var39[2];
                    double var179 = var40[2];
                    double var203 = var39[2];
                    double var181 = var179 - var203;
                    double var183 = var181 * var45;
                    double var147 = var145 + var183;
                    var82[2] = var147;
                    double[] var47 = place(var82, var5, var7, var9, var11, var13, var15, var17, 256292168);
                    double var152 = var47[0];
                    double var188 = var47[1];
                    double var210 = var47[2];
                    PurgeThunder.Dot var101 = new PurgeThunder.Dot(var152, var188, var210, var3, 1728501403);
                    var4.add(var101);
                    var43 += var1;
                }

                var36 = var43 - var41;
            }

            var38++;
        }
    }

    private static double[] place(double[] var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, int var15) {
        double[] var25 = new double[3];
        double var59 = var0[0];
        double var42 = var7 * var59;
        double var28 = var1 + var42;
        double var62 = var0[2];
        double var46 = var11 * var62;
        double var30 = var28 + var46;
        var25[0] = var30;
        double var49 = var0[1];
        double var34 = var3 + var49;
        var25[1] = var34;
        double var65 = var0[0];
        double var53 = var9 * var65;
        double var38 = var5 + var53;
        double var68 = var0[2];
        double var57 = var13 * var68;
        double var40 = var38 + var57;
        var25[2] = var40;
        return var25;
    }

    private static double sq(double var0, int var2) {
        return var0 * var0;
    }

    private static void glow(World var0, PurgeThunder.Dot var1, int var2) {
        if (var1.main) {
        }

        double var27;
        if (var1.main) {
            var27 = 0.0;
        } else {
            var27 = 0.0;
        }

        Particle var4 = Particle.END_ROD;
        double var29 = var1.x;
        double var31 = var1.y;
        double var33 = var1.z;
        Particles.spawn(var0, var4, var29, var31, var33, 1, var27, var27, var27, 0.0, null, true, 954482185);
    }

    private static double[][] taller(double[][] var0, int var1) {
        double[][] var10 = new double[var0.length][];
        int var11 = 0;

        while (true) {
            int var18 = var0.length;
            if (var11 >= var18) {
                return var10;
            }

            double[] var20 = new double[2];
            double var25 = var0[var11][0] * 2.0;
            var20[0] = var25;
            double var33 = var0[var11][1] * 2.0 * 2.0;
            var20[1] = var33;
            var10[var11] = var20;
            var11++;
        }
    }

    private static void flash(World var0, Location var1, int var2) {
        flash(var0, var1, 1, 0.0, 0.0, 0.0, 1249694383);
    }

    private static void flash(World var0, Location var1, int var2, double var3, double var5, double var7, int var9) {
        if (FLASH_NEEDS_COLOR) {
            Particle var11 = Particle.FLASH;
            Color var22 = Color.WHITE;
            Particles.spawn(var0, var11, var1, var2, var3, var5, var7, 0.0, var22, true, 1748031581);
        } else {
            Particle var26 = Particle.FLASH;
            Particles.spawn(var0, var26, var1, var2, var3, var5, var7, 0.0, null, true, 1748031581);
        }
    }

    static {
        double[][][] var8 = new double[15][][];
        double[][] var65 = new double[3][];
        double[] var335 = new double[]{-23.6, 53.8};
        var65[0] = var335;
        double[] var337 = new double[]{-13.7, 48.0};
        var65[1] = var337;
        double[] var339 = new double[]{15.6, 29.0};
        var65[2] = var339;
        var8[0] = var65;
        double[][] var67 = new double[5][];
        double[] var341 = new double[]{14.6, 30.0};
        var67[0] = var341;
        double[] var343 = new double[]{15.9, 30.1};
        var67[1] = var343;
        double[] var345 = new double[]{20.2, 32.4};
        var67[2] = var345;
        double[] var347 = new double[]{24.8, 33.8};
        var67[3] = var347;
        double[] var349 = new double[]{36.2, 35.1};
        var67[4] = var349;
        var8[1] = var67;
        double[][] var69 = new double[3][];
        double[] var351 = new double[]{15.5, 29.6};
        var69[0] = var351;
        double[] var353 = new double[]{19.6, 22.2};
        var69[1] = var353;
        double[] var355 = new double[]{25.5, 15.2};
        var69[2] = var355;
        var8[2] = var69;
        double[][] var71 = new double[3][];
        double[] var357 = new double[]{16.5, 28.2};
        var71[0] = var357;
        double[] var359 = new double[]{21.8, 28.6};
        var71[1] = var359;
        double[] var361 = new double[]{30.5, 30.5};
        var71[2] = var361;
        var8[3] = var71;
        double[][] var73 = new double[2][];
        double[] var363 = new double[]{30.2, 30.2};
        var73[0] = var363;
        double[] var365 = new double[]{40.7, 17.1};
        var73[1] = var365;
        var8[4] = var73;
        double[][] var75 = new double[4][];
        double[] var367 = new double[]{19.3, 24.5};
        var75[0] = var367;
        double[] var369 = new double[]{12.3, 13.7};
        var75[1] = var369;
        double[] var371 = new double[]{12.4, 11.4};
        var75[2] = var371;
        double[] var373 = new double[]{14.0, 5.3};
        var75[3] = var373;
        var8[5] = var75;
        double[][] var77 = new double[5][];
        double[] var375 = new double[]{13.6, 17.6};
        var77[0] = var375;
        double[] var377 = new double[]{14.5, 15.9};
        var77[1] = var377;
        double[] var379 = new double[]{20.4, 9.6};
        var77[2] = var379;
        double[] var381 = new double[]{26.6, 4.2};
        var77[3] = var381;
        double[] var383 = new double[]{26.3, 3.9};
        var77[4] = var383;
        var8[6] = var77;
        double[][] var79 = new double[6][];
        double[] var385 = new double[]{13.9, 30.5};
        var79[0] = var385;
        double[] var387 = new double[]{0.8, 15.4};
        var79[1] = var387;
        double[] var389 = new double[]{-3.2, 11.9};
        var79[2] = var389;
        double[] var391 = new double[]{-2.4, 7.7};
        var79[3] = var391;
        double[] var393 = new double[]{-1.0, 5.6};
        var79[4] = var393;
        double[] var395 = new double[]{-0.4, 3.3};
        var79[5] = var395;
        var8[7] = var79;
        double[][] var81 = new double[2][];
        double[] var397 = new double[]{8.9, 22.6};
        var81[0] = var397;
        double[] var399 = new double[]{10.8, 3.8};
        var81[1] = var399;
        var8[8] = var81;
        double[][] var83 = new double[6][];
        double[] var401 = new double[]{-18.4, 50.1};
        var83[0] = var401;
        double[] var403 = new double[]{-18.8, 50.3};
        var83[1] = var403;
        double[] var405 = new double[]{-27.7, 44.4};
        var83[2] = var405;
        double[] var407 = new double[]{-42.7, 39.2};
        var83[3] = var407;
        double[] var409 = new double[]{-47.7, 38.0};
        var83[4] = var409;
        double[] var411 = new double[]{-56.4, 43.7};
        var83[5] = var411;
        var8[9] = var83;
        double[][] var85 = new double[3][];
        double[] var413 = new double[]{-26.2, 47.1};
        var85[0] = var413;
        double[] var415 = new double[]{-28.3, 47.8};
        var85[1] = var415;
        double[] var417 = new double[]{-45.5, 57.7};
        var85[2] = var417;
        var8[10] = var85;
        double[][] var87 = new double[4][];
        double[] var419 = new double[]{-21.5, 54.0};
        var87[0] = var419;
        double[] var421 = new double[]{-20.6, 57.8};
        var87[1] = var421;
        double[] var423 = new double[]{-20.3, 68.9};
        var87[2] = var423;
        double[] var425 = new double[]{-19.2, 73.2};
        var87[3] = var425;
        var8[11] = var87;
        double[][] var89 = new double[5][];
        double[] var427 = new double[]{-20.8, 52.9};
        var89[0] = var427;
        double[] var429 = new double[]{-29.1, 59.1};
        var89[1] = var429;
        double[] var431 = new double[]{-34.2, 64.0};
        var89[2] = var431;
        double[] var433 = new double[]{-35.8, 66.3};
        var89[3] = var433;
        double[] var435 = new double[]{-36.6, 66.5};
        var89[4] = var435;
        var8[12] = var89;
        double[][] var91 = new double[8][];
        double[] var437 = new double[]{-18.0, 50.5};
        var91[0] = var437;
        double[] var439 = new double[]{-18.0, 53.1};
        var91[1] = var439;
        double[] var441 = new double[]{-15.6, 63.4};
        var91[2] = var441;
        double[] var443 = new double[]{-10.6, 62.8};
        var91[3] = var443;
        double[] var445 = new double[]{-6.7, 60.7};
        var91[4] = var445;
        double[] var447 = new double[]{-5.1, 60.1};
        var91[5] = var447;
        double[] var449 = new double[]{-4.4, 60.4};
        var91[6] = var449;
        double[] var451 = new double[]{-1.1, 71.7};
        var91[7] = var451;
        var8[13] = var91;
        double[][] var93 = new double[2][];
        double[] var453 = new double[]{-21.0, 54.8};
        var93[0] = var453;
        double[] var455 = new double[]{-24.4, 72.2};
        var93[1] = var455;
        var8[14] = var93;
        MAIN = var8;
        double[][][] var10 = new double[36][][];
        double[][] var95 = new double[2][];
        double[] var457 = new double[]{-27.8, 58.2};
        var95[0] = var457;
        double[] var459 = new double[]{-32.4, 57.7};
        var95[1] = var459;
        var10[0] = var95;
        double[][] var97 = new double[3][];
        double[] var461 = new double[]{-30.5, 59.6};
        var97[0] = var461;
        double[] var463 = new double[]{-43.0, 59.6};
        var97[1] = var463;
        double[] var465 = new double[]{-46.6, 65.0};
        var97[2] = var465;
        var10[1] = var97;
        double[][] var99 = new double[4][];
        double[] var467 = new double[]{-32.7, 63.3};
        var99[0] = var467;
        double[] var469 = new double[]{-29.3, 68.8};
        var99[1] = var469;
        double[] var471 = new double[]{-31.9, 73.0};
        var99[2] = var471;
        double[] var473 = new double[]{-32.5, 74.9};
        var99[3] = var473;
        var10[2] = var99;
        double[][] var101 = new double[6][];
        double[] var475 = new double[]{-27.8, 55.8};
        var101[0] = var475;
        double[] var477 = new double[]{-28.7, 54.8};
        var101[1] = var477;
        double[] var479 = new double[]{-30.3, 48.9};
        var101[2] = var479;
        double[] var481 = new double[]{-31.0, 48.3};
        var101[3] = var481;
        double[] var483 = new double[]{-28.5, 47.0};
        var101[4] = var483;
        double[] var485 = new double[]{-27.2, 45.5};
        var101[5] = var485;
        var10[3] = var101;
        double[][] var103 = new double[4][];
        double[] var487 = new double[]{-24.7, 53.0};
        var103[0] = var487;
        double[] var489 = new double[]{-25.6, 46.3};
        var103[1] = var489;
        double[] var491 = new double[]{-23.4, 45.0};
        var103[2] = var491;
        double[] var493 = new double[]{-21.5, 42.8};
        var103[3] = var493;
        var10[4] = var103;
        double[][] var105 = new double[5][];
        double[] var495 = new double[]{-17.6, 49.2};
        var105[0] = var495;
        double[] var497 = new double[]{-18.0, 47.0};
        var105[1] = var497;
        double[] var499 = new double[]{-20.1, 44.5};
        var105[2] = var499;
        double[] var501 = new double[]{-23.9, 41.2};
        var105[3] = var501;
        double[] var503 = new double[]{-26.0, 40.9};
        var105[4] = var503;
        var10[5] = var105;
        double[][] var107 = new double[3][];
        double[] var505 = new double[]{-35.2, 51.8};
        var107[0] = var505;
        double[] var507 = new double[]{-37.8, 48.0};
        var107[1] = var507;
        double[] var509 = new double[]{-40.9, 45.6};
        var107[2] = var509;
        var10[6] = var107;
        double[][] var109 = new double[2][];
        double[] var511 = new double[]{-40.4, 52.7};
        var109[0] = var511;
        double[] var513 = new double[]{-46.1, 45.6};
        var109[1] = var513;
        var10[7] = var109;
        double[][] var111 = new double[4][];
        double[] var515 = new double[]{-38.2, 51.8};
        var111[0] = var515;
        double[] var517 = new double[]{-37.4, 52.0};
        var111[1] = var517;
        double[] var519 = new double[]{-33.8, 56.8};
        var111[2] = var519;
        double[] var521 = new double[]{-32.5, 57.1};
        var111[3] = var521;
        var10[8] = var111;
        double[][] var113 = new double[3][];
        double[] var523 = new double[]{-38.0, 58.4};
        var113[0] = var523;
        double[] var525 = new double[]{-46.1, 60.7};
        var113[1] = var525;
        double[] var527 = new double[]{-49.6, 61.0};
        var113[2] = var527;
        var10[9] = var113;
        double[][] var115 = new double[3][];
        double[] var529 = new double[]{20.3, 18.7};
        var115[0] = var529;
        double[] var531 = new double[]{33.1, 16.3};
        var115[1] = var531;
        double[] var533 = new double[]{35.5, 16.3};
        var115[2] = var533;
        var10[10] = var115;
        double[][] var117 = new double[3][];
        double[] var535 = new double[]{29.3, 13.4};
        var117[0] = var535;
        double[] var537 = new double[]{25.9, 8.4};
        var117[1] = var537;
        double[] var539 = new double[]{24.4, 5.1};
        var117[2] = var539;
        var10[11] = var117;
        double[][] var119 = new double[3][];
        double[] var541 = new double[]{26.0, 15.9};
        var119[0] = var541;
        double[] var543 = new double[]{30.2, 15.6};
        var119[1] = var543;
        double[] var545 = new double[]{35.4, 14.3};
        var119[2] = var545;
        var10[12] = var119;
        double[][] var121 = new double[2][];
        double[] var547 = new double[]{36.4, 21.7};
        var121[0] = var547;
        double[] var549 = new double[]{35.8, 13.9};
        var121[1] = var549;
        var10[13] = var121;
        double[][] var123 = new double[2][];
        double[] var551 = new double[]{37.4, 20.1};
        var123[0] = var551;
        double[] var553 = new double[]{49.6, 20.1};
        var123[1] = var553;
        var10[14] = var123;
        double[][] var125 = new double[2][];
        double[] var555 = new double[]{34.0, 27.2};
        var125[0] = var555;
        double[] var557 = new double[]{42.3, 33.0};
        var125[1] = var557;
        var10[15] = var125;
        double[][] var127 = new double[4][];
        double[] var559 = new double[]{37.0, 25.5};
        var127[0] = var559;
        double[] var561 = new double[]{37.6, 24.8};
        var127[1] = var561;
        double[] var563 = new double[]{44.8, 23.0};
        var127[2] = var563;
        double[] var565 = new double[]{49.7, 20.1};
        var127[3] = var565;
        var10[16] = var127;
        double[][] var129 = new double[2][];
        double[] var567 = new double[]{30.5, 30.7};
        var129[0] = var567;
        double[] var569 = new double[]{30.8, 19.5};
        var129[1] = var569;
        var10[17] = var129;
        double[][] var131 = new double[2][];
        double[] var571 = new double[]{24.5, 28.6};
        var131[0] = var571;
        double[] var573 = new double[]{22.6, 21.0};
        var131[1] = var573;
        var10[18] = var131;
        double[][] var133 = new double[3][];
        double[] var575 = new double[]{18.9, 21.7};
        var133[0] = var575;
        double[] var577 = new double[]{13.3, 23.1};
        var133[1] = var577;
        double[] var579 = new double[]{11.2, 23.1};
        var133[2] = var579;
        var10[19] = var133;
        double[][] var135 = new double[3][];
        double[] var581 = new double[]{7.1, 19.2};
        var135[0] = var581;
        double[] var583 = new double[]{10.5, 16.3};
        var135[1] = var583;
        double[] var585 = new double[]{14.2, 10.9};
        var135[2] = var585;
        var10[20] = var135;
        double[][] var137 = new double[3][];
        double[] var587 = new double[]{9.3, 8.6};
        var137[0] = var587;
        double[] var589 = new double[]{5.3, 1.5};
        var137[1] = var589;
        double[] var591 = new double[]{4.3, -1.5};
        var137[2] = var591;
        var10[21] = var137;
        double[][] var139 = new double[3][];
        double[] var593 = new double[]{-2.9, 6.2};
        var139[0] = var593;
        double[] var595 = new double[]{-0.6, 4.0};
        var139[1] = var595;
        double[] var597 = new double[]{2.8, 2.0};
        var139[2] = var597;
        var10[22] = var139;
        double[][] var141 = new double[3][];
        double[] var599 = new double[]{1.2, 15.4};
        var141[0] = var599;
        double[] var601 = new double[]{1.3, 10.6};
        var141[1] = var601;
        double[] var603 = new double[]{2.3, 6.8};
        var141[2] = var603;
        var10[23] = var141;
        double[][] var143 = new double[3][];
        double[] var605 = new double[]{-0.8, 17.3};
        var143[0] = var605;
        double[] var607 = new double[]{-0.9, 16.4};
        var143[1] = var607;
        double[] var609 = new double[]{-2.8, 14.0};
        var143[2] = var609;
        var10[24] = var143;
        double[][] var145 = new double[2][];
        double[] var611 = new double[]{10.8, 25.8};
        var145[0] = var611;
        double[] var613 = new double[]{23.6, 13.1};
        var145[1] = var613;
        var10[25] = var145;
        double[][] var147 = new double[3][];
        double[] var615 = new double[]{22.0, 8.6};
        var147[0] = var615;
        double[] var617 = new double[]{21.8, 1.6};
        var147[1] = var617;
        double[] var619 = new double[]{22.4, 0.2};
        var147[2] = var619;
        var10[26] = var147;
        double[][] var149 = new double[3][];
        double[] var621 = new double[]{20.3, 10.1};
        var149[0] = var621;
        double[] var623 = new double[]{27.1, 8.8};
        var149[1] = var623;
        double[] var625 = new double[]{45.8, 3.0};
        var149[2] = var625;
        var10[27] = var149;
        double[][] var151 = new double[2][];
        double[] var627 = new double[]{18.0, 12.2};
        var151[0] = var627;
        double[] var629 = new double[]{16.6, 5.0};
        var151[1] = var629;
        var10[28] = var151;
        double[][] var153 = new double[3][];
        double[] var631 = new double[]{35.6, 21.8};
        var153[0] = var631;
        double[] var633 = new double[]{41.2, 14.9};
        var153[1] = var633;
        double[] var635 = new double[]{50.0, 5.6};
        var153[2] = var635;
        var10[29] = var153;
        double[][] var155 = new double[3][];
        double[] var637 = new double[]{35.6, 25.0};
        var155[0] = var637;
        double[] var639 = new double[]{32.8, 16.2};
        var155[1] = var639;
        double[] var641 = new double[]{30.4, 11.2};
        var155[2] = var641;
        var10[30] = var155;
        double[][] var157 = new double[3][];
        double[] var643 = new double[]{26.3, 29.8};
        var157[0] = var643;
        double[] var645 = new double[]{30.7, 32.0};
        var157[1] = var645;
        double[] var647 = new double[]{44.2, 35.2};
        var157[2] = var647;
        var10[31] = var157;
        double[][] var159 = new double[3][];
        double[] var649 = new double[]{29.3, 34.7};
        var159[0] = var649;
        double[] var651 = new double[]{38.2, 35.9};
        var159[1] = var651;
        double[] var653 = new double[]{44.9, 37.8};
        var159[2] = var653;
        var10[32] = var159;
        double[][] var161 = new double[3][];
        double[] var655 = new double[]{25.0, 33.8};
        var161[0] = var655;
        double[] var657 = new double[]{28.6, 39.0};
        var161[1] = var657;
        double[] var659 = new double[]{31.5, 41.9};
        var161[2] = var659;
        var10[33] = var161;
        double[][] var163 = new double[4][];
        double[] var661 = new double[]{21.0, 32.6};
        var163[0] = var661;
        double[] var663 = new double[]{20.8, 30.7};
        var163[1] = var663;
        double[] var665 = new double[]{22.0, 28.1};
        var163[2] = var665;
        double[] var667 = new double[]{21.6, 26.9};
        var163[3] = var667;
        var10[34] = var163;
        double[][] var165 = new double[3][];
        double[] var669 = new double[]{18.5, 41.7};
        var165[0] = var669;
        double[] var671 = new double[]{18.2, 37.9};
        var165[1] = var671;
        double[] var673 = new double[]{16.5, 30.5};
        var165[2] = var673;
        var10[35] = var165;
        BRANCH = var10;
        boolean var13;
        if (Particle.FLASH.getDataType() == Color.class) {
            var13 = true;
        } else {
            var13 = false;
        }

        FLASH_NEEDS_COLOR = var13;
    }

    private static final class Dot {
        final double x;
        final double y;
        final double z;
        final boolean main;

        Dot(double var1, double var3, double var5, boolean var7, int var8) {
            this.x = var1;
            this.y = var3;
            this.z = var5;
            this.main = var7;
        }
    }
}
