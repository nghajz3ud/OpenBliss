package com.bliss.honored;

import com.bliss.core.Particles;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class CageTask extends BukkitRunnable implements Listener {
    private static final Particle SPARK = Particle.END_ROD;
    private static final double WALL_HEIGHT = 8.0;
    private static final double OUTER_SCALE = 1.5;
    private static final int OUTER_THICK = 3;
    private static final double OUTER_SPREAD = 0.12;
    private static final double ENTRY_MARGIN = 0.5;
    private static final double BOUNCE_H = 1.2;
    private static final double BOUNCE_UP = 0.35;
    private static final double HITBOX = 0.3;
    private static final long BOUNCE_SOUND_GAP = 400L;
    private static final double CIRCLE_R = 3.2;
    private static final double HG_W = 1.2160000000000002;
    private static final double HG_H = 1.984;
    private static final double STEP = 0.18;
    private static final double LOWER_OFFSET = 1.0;
    private static final double STACK_GAP = 4.5;
    private static final int NUM_DECOR = 4;
    private static final double DECOR_GAP = 0.3;
    private static final double OSC_AMP = Math.toRadians(10.0);
    private static final double OSC_SPEED = 0.25;
    private static final double PRECESS_SPEED = 0.035;
    private static final int WAVE_COUNT = 5;
    private static final double WAVE_AMP = 0.5;
    private static final double VWAVE_AMP = 0.4;
    private static final double WAVE_SPEED = 0.45;
    private final Plugin plugin;
    private final CageManager cage;
    private final HonoredOneManager manager;
    private int draws;
    private UUID gravityOff;
    private final Set<UUID> pendingGravityRestore;
    private final Map<UUID, Long> lastBounce;
    private double[] decAxis;
    private double[] decHeight;
    private double[] decPhase;

    public CageTask(Plugin var1, CageManager var2, HonoredOneManager var3, int var4) {
        KeySetView var6 = ConcurrentHashMap.newKeySet();
        this.pendingGravityRestore = var6;
        ConcurrentHashMap var12 = new ConcurrentHashMap();
        this.lastBounce = var12;
        this.plugin = var1;
        this.cage = var2;
        this.manager = var3;
    }

    public void start(int var1) {
        Plugin var9 = this.plugin;
        this.runTaskTimer(var9, 20L, 2L);
    }

    public void run() {
        if (!this.cage.isActive(128579983)) {
            this.restoreGravity(1902192052);
            this.decAxis = null;
            this.lastBounce.clear();
        } else {
            UUID var4 = this.manager.current(1257858083);
            if (var4 != null) {
                UUID var47 = this.cage.caged(702898857);
                if (var4.equals(var47)) {
                    if (!this.cage.isEngaged(756907884)) {
                        this.restoreGravity(1902192052);
                        return;
                    }

                    if ((this.draws & 1) == 0) {
                        this.drawDecor(874273604);
                    }

                    if ((this.draws & 3) == 0) {
                        this.drawStatic(15790476);
                    }

                    int var51 = this.draws + 1;
                    this.draws = var51;
                    this.repelIntruders(var4, 1086916279);
                    Player var5 = Bukkit.getPlayer(var4);
                    if (var5 != null && var5.isOnline()) {
                        this.confine(var5, 738919733);
                        return;
                    }

                    return;
                }
            }

            this.restoreGravity(1902192052);
            this.cage.clear(1660358252);
            this.decAxis = null;
            Bukkit.broadcast(Text.legacy("<##ffcc00>&lThe spire crumbles &7— the title has left its first bearer.", 373192400));
        }
    }

    private void repelIntruders(UUID var1, int var2) {
        Location var7 = this.cage.center(859533338);
        if (var7 != null && var7.isWorldLoaded()) {
            for (Player var9 : var7.getWorld().getPlayers()) {
                UUID var25 = var9.getUniqueId();
                if (!var25.equals(var1)) {
                    Location var29 = var9.getLocation();
                    this.pushOut(var9, var29, null, 77512517);
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onMove(PlayerMoveEvent var1) {
        if (this.cage.isActive(128579983) && this.cage.isEngaged(756907884)) {
            Location var6 = var1.getTo();
            Location var7 = var1.getFrom();
            double var18 = var6.getX();
            double var31 = var7.getX();
            if (var18 == var31) {
                double var22 = var6.getY();
                double var34 = var7.getY();
                if (var22 == var34) {
                    double var26 = var6.getZ();
                    double var37 = var7.getZ();
                    if (var26 == var37) {
                        return;
                    }
                }
            }

            Player var30 = var1.getPlayer();
            this.pushOut(var30, var6, var1, 77512517);
        }
    }

    private void pushOut(Player var1, Location var2, PlayerMoveEvent var3, int var4) {
        Location var20 = this.cage.center(859533338);
        if (var20 != null && var20.getWorld() != null) {
            World var40 = var20.getWorld();
            World var102 = var2.getWorld();
            if (var40.equals(var102)) {
                UUID var43 = var1.getUniqueId();
                UUID var105 = this.cage.caged(702898857);
                if (!var43.equals(var105)) {
                    GameMode var21 = var1.getGameMode();
                    GameMode var106 = GameMode.CREATIVE;
                    if (var21 != var106) {
                        GameMode var107 = GameMode.SPECTATOR;
                        if (var21 != var107) {
                            double var50 = var2.getY();
                            double var118 = var20.getY() - 2.0;
                            if (var50 < var118) {
                                return;
                            }

                            double var54 = var2.getY();
                            double var125 = var20.getY() + 8.0 + 1.0;
                            if (var54 > var125) {
                                return;
                            }

                            double var22 = this.cage.radius(1148470054) * 1.5;
                            double var64 = var2.getX();
                            double var130 = var20.getX();
                            double var24 = var64 - var130;
                            double var69 = var2.getZ();
                            double var133 = var20.getZ();
                            double var26 = var69 - var133;
                            double var75 = var24 * var24;
                            double var139 = var26 * var26;
                            double var28 = Math.sqrt(var75 + var139);
                            double var143 = var22 + 0.3;
                            if (var28 > var143) {
                                return;
                            }

                            Vector var101;
                            if (var28 < 1.0E-4) {
                                var101 = new Vector(1, 0, 0);
                            } else {
                                Vector var100 = new Vector(var24, 0.0, var26);
                                double var114 = 1.0 / var28;
                                var101 = var100.multiply(var114);
                            }

                            double var31 = var22 + 0.3 + 0.5;
                            World var153 = var20.getWorld();
                            double var158 = var20.getX();
                            double var175 = var101.getX();
                            double var177 = var175 * var31;
                            double var160 = var158 + var177;
                            double var180 = var2.getY();
                            double var183 = var20.getZ();
                            double var187 = var101.getZ();
                            double var189 = var187 * var31;
                            double var185 = var183 + var189;
                            float var192 = var2.getYaw();
                            float var193 = var2.getPitch();
                            Location var93 = new Location(var153, var160, var180, var185, var192, var193);
                            if (var3 != null) {
                                var3.setTo(var93);
                            } else {
                                var1.teleport(var93);
                            }

                            var1.setFallDistance(0.0F);
                            this.bounce(var1, var101, 702960674);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void bounce(Player var1, Vector var2, int var3) {
        long var20 = System.currentTimeMillis();
        Map var26 = this.lastBounce;
        UUID var51 = var1.getUniqueId();
        Long var22 = (Long)var26.get(var51);
        if (var22 != null) {
            long var63 = var22;
            if (var20 - var63 < 400L) {
                return;
            }
        }

        Map var36 = this.lastBounce;
        UUID var53 = var1.getUniqueId();
        Long var69 = var20;
        var36.put(var53, var69);
        Vector var23 = var2.clone().multiply(1.2).setY(0.35);
        if (this.plugin.isEnabled()) {
            BukkitScheduler var45 = Bukkit.getScheduler();
            Plugin var59 = this.plugin;
            Runnable var71 = () -> {
                if (var1.isOnline()) {
                    var1.setVelocity(var23);
                    var1.setFallDistance(0.0F);
                }
            };
            var45.runTask(var59, var71);
        }

        World var24 = var1.getWorld();
        Location var61 = var1.getLocation();
        Sound var72 = Sound.ENTITY_SLIME_SQUISH;
        var24.playSound(var61, var72, 0.7F, 1.4F);
        Particle var62 = Particle.END_ROD;
        Location var75 = var1.getLocation().add(0.0, 1.0, 0.0);
        Particles.spawn(var24, var62, var75, 6, 0.2, 0.4, 0.2, 0.02, 1986132876);
    }

    private void confine(Player var1, int var2) {
        Location var17;
        Location var18;
        double var19;
        double var21;
        double var23;
        boolean var137;
        label44: {
            var17 = this.cage.center(859533338);
            var18 = var1.getLocation();
            var19 = var17.getY() + 1.0;
            var21 = var17.getY() + 4.5;
            var23 = (var19 + var21) / 2.0 - 1.0;
            if (this.gravityOff != null) {
                UUID var62 = this.gravityOff;
                UUID var138 = var1.getUniqueId();
                if (var62.equals(var138)) {
                    var137 = false;
                    break label44;
                }
            }

            var137 = true;
        }

        if (var1.hasGravity()) {
            var1.setGravity(false);
        }

        UUID var141 = var1.getUniqueId();
        this.gravityOff = var141;
        double var26 = var18.getX();
        double var28 = var18.getZ();
        double var30 = var18.getY();
        boolean var32 = false;
        if (var137) {
            var30 = var23;
            var32 = true;
        }

        World var83 = var18.getWorld();
        World var143 = var17.getWorld();
        if (!var83.equals(var143)) {
            var26 = var17.getX();
            var28 = var17.getZ();
            var30 = var23;
            var32 = true;
        } else {
            double var86 = var18.getX();
            double var156 = var17.getX();
            double var33 = var86 - var156;
            double var91 = var18.getZ();
            double var159 = var17.getZ();
            double var35 = var91 - var159;
            double var97 = var33 * var33;
            double var165 = var35 * var35;
            double var101 = Math.sqrt(var97 + var165);
            double var169 = this.cage.radius(1148470054);
            if (var101 > var169) {
                var26 = var17.getX();
                var28 = var17.getZ();
                var32 = true;
            }

            if (var30 < var19) {
                var30 = var19;
                var32 = true;
            } else if (var30 > var21) {
                var30 = var21;
                var32 = true;
            }
        }

        if (var32) {
            World var175 = var17.getWorld();
            float var180 = var18.getYaw();
            float var181 = var18.getPitch();
            Location var144 = new Location(var175, var26, var30, var28, var180, var181);
            var1.teleport(var144);
            Vector var145 = new Vector(0, 0, 0);
            var1.setVelocity(var145);
            var1.setFallDistance(0.0F);
        }
    }

    private void restoreGravity(int var1) {
        if (this.gravityOff != null) {
            Player var4 = Bukkit.getPlayer(this.gravityOff);
            if (var4 != null) {
                var4.setGravity(true);
            } else {
                Set var13 = this.pendingGravityRestore;
                UUID var17 = this.gravityOff;
                var13.add(var17);
            }

            this.gravityOff = null;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        Set var4 = this.pendingGravityRestore;
        UUID var9 = var1.getPlayer().getUniqueId();
        if (var4.remove(var9)) {
            var1.getPlayer().setGravity(true);
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onTeleport(PlayerTeleportEvent var1) {
        if (this.cage.isActive(128579983) && this.cage.isEngaged(756907884)) {
            UUID var12 = var1.getPlayer().getUniqueId();
            UUID var20 = this.cage.caged(702898857);
            if (var12.equals(var20)) {
                TeleportCause var4 = var1.getCause();
                TeleportCause var21 = TeleportCause.ENDER_PEARL;
                if (var4 != var21) {
                    TeleportCause var22 = TeleportCause.CHORUS_FRUIT;
                    if (var4 != var22) {
                        return;
                    }
                }

                var1.setCancelled(true);
            }
        }
    }

    private void drawStatic(int var1) {
        Location var19 = this.cage.center(859533338);
        if (var19 != null && var19.getWorld() != null) {
            World var20 = var19.getWorld();
            double var21 = this.cage.radius(1148470054) * 1.5;
            double var23 = this.cage.minY(1075884005);
            int var79 = (int)Math.round((Math.PI * 2) * var21 / 0.4);
            int var25 = Math.max(8, var79);
            double[] var54 = new double[]{var23, 0.0};
            double var107 = var23 + 8.0;
            var54[1] = var107;

            for (double var29 : var54) {
                for (int var31 = 0; var31 < var25; var31++) {
                    double var89 = (double)var31;
                    double var66 = (Math.PI * 2) * var89;
                    double var92 = (double)var25;
                    double var32 = var66 / var92;
                    double var95 = var19.getX();
                    double var119 = Math.cos(var32);
                    double var121 = var119 * var21;
                    double var97 = var95 + var121;
                    double var130 = var19.getZ();
                    double var134 = Math.sin(var32);
                    double var136 = var134 * var21;
                    double var132 = var130 + var136;
                    this.spawnThick(var20, var97, var29, var132, 892770026);
                }
            }

            double var112 = var19.getY() + 1.0;
            this.drawCircleHourglass(var20, var19, var112, 227530606);
            double var117 = var19.getY() + 4.5;
            this.drawCircleHourglass(var20, var19, var117, 227530606);
        }
    }

    private void drawDecor(int var1) {
        Location var18 = this.cage.center(859533338);
        if (var18 != null && var18.getWorld() != null) {
            World var19 = var18.getWorld();
            double var20 = this.cage.radius(1148470054) * 1.5 - 0.3;
            double var57 = this.cage.minY(1075884005);
            this.ensureDecor(var57, 960291697);
            int var22 = 0;

            while (true) {
                int var61 = this.decAxis.length;
                if (var22 >= var61) {
                    return;
                }

                double var45 = OSC_AMP;
                double var70 = (double)this.draws * 0.25;
                double[] var86 = this.decPhase;
                double var87 = var86[var22];
                double var74 = Math.sin(var70 + var87);
                double var23 = var45 * var74;
                double[] var50 = this.decAxis;
                double var51 = var50[var22];
                double var80 = (double)this.draws * 0.035;
                double var25 = var51 + var80;
                double var83 = var18.getX();
                double[] var92 = this.decHeight;
                double var93 = var92[var22];
                double var96 = var18.getZ();
                double[] var98 = this.decPhase;
                double var99 = var98[var22];
                this.drawTiltHoop(var19, var83, var93, var96, var25, var23, var20, var99, 626408387);
                var22++;
            }
        }
    }

    private void drawCircleHourglass(World var1, Location var2, double var3, int var5) {
        int var20 = (int)Math.round(111.70107212763709);

        for (int var21 = 0; var21 < var20; var21++) {
            double var70 = (double)var21;
            double var44 = (Math.PI * 2) * var70;
            double var73 = (double)var20;
            double var158 = var44 / var73;
            double var76 = var2.getX();
            double var126 = Math.cos(var158) * 3.2;
            double var78 = var76 + var126;
            double var149 = var2.getZ();
            double var155 = Math.sin(var158) * 3.2;
            double var151 = var149 + var155;
            this.spawn(var1, var78, var3, var151, 498673395);
        }

        double[] var31 = new double[2];
        double var82 = var2.getX() - 1.2160000000000002;
        var31[0] = var82;
        double var87 = var2.getZ() + 1.984;
        var31[1] = var87;
        double[] var33 = new double[2];
        double var92 = var2.getX() + 1.2160000000000002;
        var33[0] = var92;
        double var97 = var2.getZ() + 1.984;
        var33[1] = var97;
        double[] var35 = new double[2];
        double var102 = var2.getX() - 1.2160000000000002;
        var35[0] = var102;
        double var107 = var2.getZ() - 1.984;
        var35[1] = var107;
        double[] var37 = new double[2];
        double var112 = var2.getX() + 1.2160000000000002;
        var37[0] = var112;
        double var117 = var2.getZ() - 1.984;
        var37[1] = var117;
        this.lineXZ(var1, var3, var31, var33, 1617133504);
        this.lineXZ(var1, var3, var35, var37, 1617133504);
        this.lineXZ(var1, var3, var31, var37, 1617133504);
        this.lineXZ(var1, var3, var33, var35, 1617133504);
    }

    private void lineXZ(World var1, double var2, double[] var4, double[] var5, int var6) {
        double var30 = var5[0];
        double var67 = var4[0];
        double var20 = var30 - var67;
        double var35 = var5[1];
        double var70 = var4[1];
        double var22 = var35 - var70;
        double var41 = var20 * var20;
        double var76 = var22 * var22;
        int var64 = (int)Math.round(Math.sqrt(var41 + var76) / 0.18);
        int var26 = Math.max(1, var64);

        for (int var27 = 0; var27 <= var26; var27++) {
            double var52 = (double)var27;
            double var79 = (double)var26;
            double var28 = var52 / var79;
            double var82 = var4[0];
            double var92 = var20 * var28;
            double var84 = var82 + var92;
            double var97 = var4[1];
            double var101 = var22 * var28;
            double var99 = var97 + var101;
            this.spawn(var1, var84, var2, var99, 498673395);
        }
    }

    private void drawTiltHoop(World var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, int var16) {
        double var31 = Math.cos(var8);
        double var33 = Math.sin(var8);
        double var35 = Math.cos(var10);
        double var37 = Math.sin(var10);
        double var85 = (double)this.draws * 0.45;
        double var39 = var85 + var14;
        int var170 = (int)Math.round((Math.PI * 2) * var12 / 0.25);
        int var41 = Math.max(24, var170);

        for (int var42 = 0; var42 < var41; var42++) {
            double var176 = (double)var42;
            double var95 = (Math.PI * 2) * var176;
            double var179 = (double)var41;
            double var43 = var95 / var179;
            double var251 = 5.0 * var43;
            double var255 = Math.sin(var251 + var39);
            double var183 = 0.5 * var255;
            double var45 = var12 + var183;
            double var105 = Math.cos(var43);
            double var47 = var105 * var45;
            double var189 = 5.0 * var43;
            double var195 = Math.sin(var189 + var39 + 1.7);
            double var49 = 0.4 * var195;
            double var115 = Math.sin(var43);
            double var51 = var115 * var45;
            double var121 = var31 * var47;
            double var203 = var33 * var51;
            double var53 = var121 + var203;
            double var127 = -var33;
            double var55 = var127 * var49;
            double var133 = var33 * var47;
            double var211 = var31 * var51;
            double var57 = var133 - var211;
            double var59 = var31 * var49;
            double var143 = var47 * var35;
            double var219 = var55 * var37;
            double var145 = var143 + var219;
            double var223 = var31 * var53;
            double var273 = 1.0 - var35;
            double var225 = var223 * var273;
            double var61 = var145 + var225;
            double var151 = var49 * var35;
            double var231 = var57 * var37;
            double var63 = var151 + var231;
            double var157 = var51 * var35;
            double var237 = var59 * var37;
            double var159 = var157 + var237;
            double var241 = var33 * var53;
            double var283 = 1.0 - var35;
            double var243 = var241 * var283;
            double var65 = var159 + var243;
            double var247 = var2 + var61;
            double var289 = var4 + var63;
            double var301 = var6 + var65;
            this.spawn(var1, var247, var289, var301, 498673395);
        }
    }

    private void ensureDecor(double var1, int var3) {
        if (this.decAxis == null) {
            double[] var26 = new double[4];
            this.decAxis = var26;
            double[] var28 = new double[4];
            this.decHeight = var28;
            double[] var30 = new double[4];
            this.decPhase = var30;
            ThreadLocalRandom var11 = ThreadLocalRandom.current();

            for (int var12 = 0; var12 < 4; var12++) {
                double[] var21 = this.decAxis;
                double var39 = var11.nextDouble() * 2.0 * Math.PI;
                var21[var12] = var39;
                double[] var23 = this.decHeight;
                double var43 = var1 + 1.0;
                double var61 = var11.nextDouble() * 6.0;
                double var45 = var43 + var61;
                var23[var12] = var45;
                double[] var25 = this.decPhase;
                double var52 = var11.nextDouble() * 2.0 * Math.PI;
                var25[var12] = var52;
            }
        }
    }

    private void spawn(World var1, double var2, double var4, double var6, int var8) {
        Particle var10 = SPARK;
        Location var11 = new Location(var1, var2, var4, var6);
        Particles.spawn(var1, var10, var11, 0, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
    }

    private void spawnThick(World var1, double var2, double var4, double var6, int var8) {
        Particle var10 = SPARK;
        Location var11 = new Location(var1, var2, var4, var6);
        Particles.spawn(var1, var10, var11, 3, 0.12, 0.12, 0.12, 0.0, null, true, 1748031581);
    }
}
