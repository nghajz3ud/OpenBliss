package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import com.bliss.core.SignatureClock;
import com.bliss.core.SonicBoomSound;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.logging.Logger;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class PedestalFinale {
    public static final long START_AFTER_LANDING = 100L;
    private static final double RADIUS = 2.6;
    private static final double STEP = 0.18;
    private static final double SHAPE_THICK = 0.09;
    private static final double HG_W = 0.9880000000000001;
    private static final double HG_H = 1.612;
    private static final int SHAPE_REDRAW = 4;
    private static final int[] BOOM_GAPS;
    private static final int BOOM_RING_TICKS = 24;
    private static final double BOOM_R0 = 1.0;
    private static final double BOOM_V_FIRST = 0.9;
    private static final double BOOM_V_LAST = 2.3;
    private static final int BOOM_COUNT = 20;
    private static final double BOOM_SPACING = 4.0;
    private static final int BOOM_COUNT_MAX = 64;
    private static final double BOOM_Y = 0.7;
    private static final int ROD_2_DELAY = 4;
    private static final int ROD_POINTS = 140;
    private static final double ROD_SPEED = 4.5;
    private static final int SIGN_FADE = 75;
    private static final double GOLD_START_R = 1.2;
    private static final double GOLD_END_R = 3.0;
    private static final double GOLD_GROW = 2.5;
    private static final float GOLD_CUBE_MIN = 0.1F;
    private static final float GOLD_CUBE_MAX = 0.18F;
    private static final int GOLD_BRIGHT_MIN = 10;
    private static final Material[] GOLD_PALETTE;
    private static final Color[] GOLD_GLOWS;
    private static final double GOLD_LIFT = 0.03;
    private static final int GOLD_GROW_TICKS = 59;
    private static final Color GOLD_GLOW = Color.fromRGB(16765530);
    private static final float BEAM_W = 0.22F;
    private static final float BEAM_SHEATH = 2.9F;
    private static final float BEAM_H = 90.0F;
    private static final int BEAM_RISE = 14;
    private static final int BEAM_PULSE = 4;
    private static final double BEAM_PULSE_A = 0.3;
    private static final double BEAM_DRESS_H = 46.0;
    private static final int BEAM_HELIX_N = 40;
    private static final double BEAM_HELIX_R = 0.8;
    private static final double BEAM_HELIX_FLARE = 2.2;
    private static final double BEAM_HELIX_TURNS = 3.0;
    private static final double BEAM_HELIX_SPIN = 0.42;
    private static final int BEAM_STREAM_N = 12;
    private static final double BEAM_STREAM_V = 1.7;
    private static final int BEAM_ARC_N = 3;
    private static final int BEAM_FLARE_EVERY = 8;
    private static final DustOptions BEAM_GOLD;
    private static final DustOptions BEAM_HOT;
    private static final double BURST_RANGE = 40.0;
    private static final double BURST_V = 1.9;
    private static final double BURST_UP = 0.9;
    private static final double BURST_2_MUL = 1.4;
    private static final int SLOWMO_TICKS = 40;
    private static final int BURST_SLOWFALL = 300;
    private static final double SLOWMO = 0.07;
    private static final int FLASH_HOLD = 4;
    private static final int FLASH_FADE = 14;
    private static final double SCREEN_RANGE = 160.0;
    private static final int RETURN_TICKS = 40;
    private static final double RETURN_R = 14.0;
    private static final int A_END = 150;
    private static final int B_AT = 225;
    private static final int B_GROW = 235;
    private static final int C_AT = 304;
    private static final int D_AT = 344;
    private static final int D_RESUME = 386;
    private static final int E_AT = 406;
    private static final int F_AT = 436;
    private static final int ORB_AT = 476;
    private static final int END_AT = 516;
    private static final long FLIGHT_DAY = 80016L;
    private static final long COVER_DAY_BASE = 80020L;
    private static final long IRIS_DAY_BASE = 80040L;
    private static final int IRIS_OPEN_TICKS = 3;
    private final BlissPlugin plugin;
    private BukkitTask task;
    private boolean running;
    private final List<PedestalFinale.GoldDot> goldSign;
    private TextDisplay goldSigil;
    private static final double SIGIL_SPAN_AT_1 = 1.0;
    private static final double SIGIL_PITCH_DEG = 90.0;
    private BlockDisplay beam;
    private BlockDisplay sheath;
    private final Map<UUID, Vector> blast;
    private final List<UUID> screened;
    private int flashStart;
    private final List<Integer> ringStarts;
    private boolean orbRaised;
    private static final boolean FLASH_NEEDS_COLOR;

    public PedestalFinale(BlissPlugin var1, int var2) {
        ArrayList var4 = new ArrayList();
        this.goldSign = var4;
        HashMap var11 = new HashMap();
        this.blast = var11;
        ArrayList var12 = new ArrayList();
        this.screened = var12;
        this.flashStart = Integer.MIN_VALUE;
        ArrayList var14 = new ArrayList();
        this.ringStarts = var14;
        this.plugin = var1;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void begin(Location var1, int var2) {
        if (!this.running && var1 != null && var1.getWorld() != null) {
            this.running = true;
            final World var9 = var1.getWorld();
            final Location var10 = var1.clone();
            if (this.plugin.controlRoomVeil(785222279) != null) {
                this.plugin.controlRoomVeil(785222279).pause(536L, 1861141377);
            }

            this.goldSign.clear();
            this.goldSigil = null;
            this.beam = null;
            this.sheath = null;
            this.blast.clear();
            this.screened.clear();
            this.ringStarts.clear();
            this.flashStart = Integer.MIN_VALUE;
            this.orbRaised = false;
            ArrayList var45 = new ArrayList();
            final ArrayList var11 = var45;
            int var12 = 0;

            for (int var16 : BOOM_GAPS) {
                Integer var69 = var12;
                var11.add(var69);
                var12 += var16;
            }

            BukkitRunnable var71 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    try {
                        PedestalFinale var7 = PedestalFinale.this;
                        World var22 = var9;
                        Location var31 = var10;
                        int var35 = this.t;
                        List var36 = var11;
                        var7.step(var22, var31, var35, var36, 520197148);
                    } catch (RuntimeException var37) {
                        Logger var12 = PedestalFinale.this.plugin.getLogger();
                        int var24 = this.t;
                        String var33 = String.valueOf(var37);
                        String var25 = "Pedestal finale aborted at tick " + var24 + ": " + var33;
                        var12.warning(var25);
                        this.cancel();
                        PedestalFinale.this.finish(true, 1208459663);
                        return;
                    }

                    if (this.t >= 516) {
                        this.cancel();
                        PedestalFinale.this.finish(true, 1208459663);
                    } else {
                        int var29 = this.t + 1;
                        this.t = var29;
                    }
                }
            };
            BlissPlugin var74 = this.plugin;
            BukkitTask var72 = var71.runTaskTimer(var74, 0L, 1L);
            this.task = var72;
        }
    }

    public void shutdown(int var1) {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }

        if (this.running) {
            this.finish(false, 1208459663);
        }
    }

    private void raiseOrb(World var1, Location var2, int var3) {
        if (!this.orbRaised) {
            this.orbRaised = true;
            if (var1 != null && var2 != null) {
                Location var33 = var2.clone().add(0.0, 1.0, 0.0);
                flash(var1, var33, 3, 0.3, 0.6, 0.3, 1626379387);
                Sound var37 = Sound.ITEM_TOTEM_USE;
                var1.playSound(var2, var37, 6.0F, 0.8F);
                Sound var38 = Sound.BLOCK_BEACON_ACTIVATE;
                var1.playSound(var2, var38, 6.0F, 0.7F);
            }

            if (this.plugin.kitRules(1205957746) != null) {
                this.plugin.kitRules(1205957746).deactivatePedestalNow(412209635);
            }
        }
    }

    private void step(World var1, Location var2, int var3, List<Integer> var4, int var5) {
        if (var3 < 150) {
            if (var3 % 4 == 0) {
                this.drawSign(var1, var2, 1339026572);
            }

            Integer var95 = var3;
            int var16 = var4.indexOf(var95);
            if (var16 >= 0) {
                List var27 = this.ringStarts;
                Integer var97 = var3;
                var27.add(var97);
                float var162 = (float)var16;
                float var99 = 0.12F * var162;
                float var17 = 0.7F + var99;
                Sound var163 = Sound.ENTITY_WARDEN_SONIC_BOOM;
                var1.playSound(var2, var163, 3.0F, var17);
                String var33 = "finale-ring|" + var16;
                Location var166 = var2.clone().add(0.0, 0.7, 0.0);
                SonicBoomSound.pulse(var33, var1, var166, 2077538397);
                Location var169 = var2.clone().add(0.0, 0.7, 0.0);
                this.rodRing(var1, var169, 1028269328);
                Sound var170 = Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST;
                var1.playSound(var2, var170, 2.0F, 1.0F);
            }

            Integer var106 = var3 - 4;
            if (var4.contains(var106)) {
                Location var174 = var2.clone().add(0.0, 0.7, 0.0);
                this.rodRing(var1, var174, 1028269328);
                Sound var175 = Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST;
                var1.playSound(var2, var175, 2.0F, 1.15F);
            }
        }

        byte var333 = 0;

        while (true) {
            int var111 = this.ringStarts.size();
            if (var333 >= var111) {
                if (var3 == 225) {
                    this.drawGoldSign(var1, var2, 445550037);
                    Sound var185 = Sound.BLOCK_BEACON_POWER_SELECT;
                    var1.playSound(var2, var185, 4.0F, 1.3F);
                }

                if (var3 == 235) {
                    this.growGoldSign(var2, 35534733);
                    Sound var186 = Sound.BLOCK_RESPAWN_ANCHOR_CHARGE;
                    var1.playSound(var2, var186, 3.0F, 0.6F);
                }

                if (var3 == 304) {
                    this.raiseBeam(var1, var2, 866414100);
                    Sound var188 = Sound.BLOCK_BEACON_ACTIVATE;
                    var1.playSound(var2, var188, 5.0F, 1.2F);
                    Sound var189 = Sound.ENTITY_WARDEN_SONIC_CHARGE;
                    var1.playSound(var2, var189, 3.0F, 1.4F);
                }

                if (var3 > 304) {
                    int var232 = var3 - 304;
                    this.beamEnergy(var1, var2, var232, 1209557470);
                }

                if (var3 == 344) {
                    this.removeDrawn(613955641);
                    this.burst(var1, var2, 1.0, true, 1993901694);
                    this.screenFlash(var1, var2, var3, 1368074405);
                    Sound var193 = Sound.ENTITY_GENERIC_EXPLODE;
                    var1.playSound(var2, var193, 6.0F, 0.5F);
                    Sound var194 = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
                    var1.playSound(var2, var194, 4.0F, 0.6F);
                    Location var138 = var2.clone().add(0.0, 1.0, 0.0);
                    flash(var1, var138, 4, 1.5, 0.8, 1.5, 1626379387);
                    Location var200 = var2.clone().add(0.0, 1.0, 0.0);
                    this.rodRing(var1, var200, 1028269328);
                }

                if (var3 > 344 && var3 < 386) {
                    this.slowMotion(2028270283);
                }

                if (var3 == 386) {
                    this.resumeBlast(var1, var2, 85013758);
                }

                if (var3 == 406) {
                    this.burst(var1, var2, 1.4, false, 1993901694);
                    this.screenFlash(var1, var2, var3, 1368074405);
                    Sound var204 = Sound.ENTITY_GENERIC_EXPLODE;
                    var1.playSound(var2, var204, 6.0F, 0.7F);
                    Location var150 = var2.clone().add(0.0, 1.0, 0.0);
                    flash(var1, var150, 6, 2.5, 1.0, 2.5, 1626379387);
                    Location var210 = var2.clone().add(0.0, 1.0, 0.0);
                    this.rodRing(var1, var210, 1028269328);
                }

                if (var3 >= 436 && var3 < 476) {
                    int var251 = var3 - 436;
                    this.returning(var1, var2, var251, 51194064);
                    if ((var3 - 436) % 10 == 0) {
                        Sound var212 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
                        float var311 = (float)(var3 - 436);
                        float var304 = 0.03F * var311;
                        float var277 = 0.8F + var304;
                        var1.playSound(var2, var212, 3.0F, var277);
                    }
                }

                if (var3 == 476) {
                    this.raiseOrb(var1, var2, 519259979);
                }

                this.sendScreens(var1, var2, var3, 1672718881);
                return;
            }

            List var113 = this.ringStarts;
            int var116 = (Integer)var113.get(var333);
            int var334 = var3 - var116;
            if (var334 >= 0 && var334 < 24) {
                double var257 = (double)var333;
                double var179 = 1.4 * var257;
                int var284 = BOOM_GAPS.length - 1;
                double var261 = (double)Math.max(1, var284);
                double var181 = var179 / var261;
                double var18 = 0.9 + var181;
                double var317 = (double)var334;
                double var287 = var18 * var317;
                double var320 = (double)var334;
                double var291 = var287 * var320 / 24.0;
                double var225 = 1.0 + var291;
                this.boomRing(var1, var2, var225, 2075434233);
            }

            var333 += 1;
        }
    }

    public void releaseScreens(Player var1, int var2) {
        List var5 = this.screened;
        UUID var7 = var1.getUniqueId();
        var5.remove(var7);
    }

    private void finish(boolean var1, int var2) {
        if (var1 && !this.orbRaised) {
            this.plugin.getLogger().warning("Pedestal finale ended before its climax — closing the pedestal anyway so the monument still exists.");

            try {
                this.raiseOrb(null, null, 519259979);
            } catch (RuntimeException var47) {
                Logger var39 = this.plugin.getLogger();
                String var45 = "Pedestal finale: closing the pedestal failed (" + var47 + ") — use /bliss orb to raise the monument by hand.";
                var39.warning(var45);
            }
        }

        this.running = false;
        this.task = null;
        this.removeDrawn(613955641);
        Iterator var6 = this.screened.iterator();

        while (var6.hasNext()) {
            Player var8 = Bukkit.getPlayer((UUID)var6.next());
            if (var8 != null) {
                var8.resetPlayerTime();
            }
        }

        this.screened.clear();
        this.blast.clear();
        this.ringStarts.clear();
    }

    private void drawSign(World var1, Location var2, int var3) {
        for (double var18 = -0.09; var18 <= 0.090000001; var18 += 0.09) {
            double var120 = 2.6 + var18;
            int var23 = (int)Math.round((Math.PI * 2) * var120 / 0.18);

            for (int var24 = 0; var24 < var23; var24++) {
                double var80 = (double)var24;
                double var55 = (Math.PI * 2) * var80;
                double var83 = (double)var23;
                double var25 = var55 / var83;
                Location var68 = var2.clone();
                double var87 = Math.cos(var25);
                double var89 = var87 * var120;
                double var108 = Math.sin(var25);
                double var110 = var108 * var120;
                Location var69 = var68.add(var89, 0.0, var110);
                spark(var1, var69, 502856997);
            }
        }

        for (double[] var22 : hourglass(1625737332)) {
            Location var71 = var2.clone();
            double var93 = var22[0];
            double var112 = var22[1];
            Location var72 = var71.add(var93, 0.0, var112);
            Location var96 = var2.clone();
            double var99 = var22[2];
            double var115 = var22[3];
            Location var97 = var96.add(var99, 0.0, var115);
            this.thickLine(var1, var72, var97, 1025846750);
        }
    }

    private static double[][] hourglass(int var0) {
        double[][] var7 = new double[4][];
        double[] var11 = new double[]{-0.9880000000000001, 1.612, 0.9880000000000001, 1.612};
        var7[0] = var11;
        double[] var13 = new double[]{-0.9880000000000001, -1.612, 0.9880000000000001, -1.612};
        var7[1] = var13;
        double[] var15 = new double[]{-0.9880000000000001, 1.612, 0.9880000000000001, -1.612};
        var7[2] = var15;
        double[] var17 = new double[]{0.9880000000000001, 1.612, -0.9880000000000001, -1.612};
        var7[3] = var17;
        return var7;
    }

    private void thickLine(World var1, Location var2, Location var3, int var4) {
        Vector var19 = var3.toVector();
        Vector var44 = var2.toVector();
        Vector var12 = var19.subtract(var44);
        var12.setY(0);
        double var13 = var12.length();
        Vector var29;
        if (var13 < 1.0E-6) {
            var29 = new Vector(0, 0, 0);
        } else {
            double var64 = -var12.getZ();
            double var75 = var12.getX();
            Vector var43 = new Vector(var64, 0.0, var75);
            double var51 = 1.0 / var13;
            var29 = var43.multiply(var51);
        }

        Vector var15 = var29;

        for (double var16 = -0.09; var16 <= 0.090000001; var16 += 0.09) {
            Vector var36 = var15.clone();
            Vector var18 = var36.multiply(var16);
            Location var57 = var2.clone();
            Location var58 = var57.add(var18);
            Location var68 = var3.clone();
            Location var69 = var68.add(var18);
            this.line(var1, var58, var69, 1818401216);
        }
    }

    private void line(World var1, Location var2, Location var3, int var4) {
        Vector var16 = var3.toVector();
        Vector var26 = var2.toVector();
        Vector var11 = var16.subtract(var26);
        int var33 = (int)Math.round(var11.length() / 0.18);
        int var14 = Math.max(1, var33);

        for (int var15 = 0; var15 <= var14; var15++) {
            Location var36 = var2.clone();
            Vector var38 = var11.clone();
            double var41 = (double)var15;
            double var45 = (double)var14;
            double var43 = var41 / var45;
            Vector var39 = var38.multiply(var43);
            Location var37 = var36.add(var39);
            spark(var1, var37, 502856997);
        }
    }

    private static void spark(World var0, Location var1, int var2) {
        Particle var4 = Particle.END_ROD;
        Particles.spawn(var0, var4, var1, 0, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
    }

    private void boomRing(World var1, Location var2, double var3, int var5) {
        int var56 = (int)Math.round((Math.PI * 2) * var3 / 4.0);
        int var51 = Math.min(64, var56);
        int var89 = Math.max(20, var51);

        for (int var30 = 0; var30 < var89; var30++) {
            double var58 = (double)var30;
            double var46 = (Math.PI * 2) * var58;
            double var61 = (double)var89;
            double var31 = var46 / var61;
            Particle var53 = Particle.SONIC_BOOM;
            double var64 = var2.getX();
            double var68 = Math.cos(var31);
            double var70 = var68 * var3;
            double var66 = var64 + var70;
            double var75 = var2.getY() + 0.7;
            double var80 = var2.getZ();
            double var84 = Math.sin(var31);
            double var86 = var84 * var3;
            double var82 = var80 + var86;
            Particles.spawn(var1, var53, var66, var75, var82, 1, 0.0, 0.0, 0.0, 0.0, null, true, 954482185);
        }
    }

    private void rodRing(World var1, Location var2, int var3) {
        for (int var21 = 0; var21 < 140; var21++) {
            double var22 = (double)var21 / 140.0 * Math.PI * 2.0;
            Particle var35 = Particle.END_ROD;
            double var41 = var2.getX();
            double var43 = var2.getY();
            double var45 = var2.getZ();
            double var47 = Math.cos(var22);
            double var49 = Math.sin(var22);
            Particles.spawn(var1, var35, var41, var43, var45, 0, var47, 0.0, var49, 4.5, null, true, 954482185);
        }
    }

    private void drawGoldSign(World var1, Location var2, int var3) {
        double var17 = var2.getX();
        double var22 = var2.getY() + 0.03;
        double var25 = var2.getZ();
        Location var4 = new Location(var1, var17, var22, var25);
        Consumer<TextDisplay> var7 = var0 -> {
            TextComponent var15x = Component.text("\ue002");
            Key var30 = Key.key("minecraft", "particles");
            Component var16 = var15x.font(var30);
            var0.text(var16);
            Billboard var17x = Billboard.FIXED;
            var0.setBillboard(var17x);
            TextAlignment var18 = TextAlignment.CENTER;
            var0.setAlignment(var18);
            Color var20 = Color.fromARGB(0, 0, 0, 0);
            var0.setBackgroundColor(var20);
            var0.setShadowed(false);
            var0.setSeeThrough(false);
            var0.setDefaultBackground(false);
            Brightness var24 = new Brightness(15, 15);
            var0.setBrightness(var24);
            var0.setPersistent(false);
            Transformation var29 = sigilShape(sigilScale(1.2, 2104849909), 635460106);
            var0.setTransformation(var29);
        };
        TextDisplay var15 = (TextDisplay)var1.spawn(var4, TextDisplay.class, var7);
        this.goldSigil = var15;
    }

    private static float sigilScale(double var0, int var2) {
        return (float)(2.0 * var0 / 1.0);
    }

    private static Transformation sigilShape(float var0, int var1) {
        Quaternionf var2 = new Quaternionf();
        float var16 = (float)Math.toRadians(90.0);
        Quaternionf var9 = var2.rotateX(var16);
        Vector3f var3 = new Vector3f(0.0F, 0.0F, 0.0F);
        Vector3f var18 = new Vector3f(var0, var0, var0);
        Quaternionf var19 = new Quaternionf();
        return new Transformation(var3, var9, var18, var19);
    }

    private PedestalFinale.GoldDot goldDot(World var1, Location var2, double var3, double var5) {
        ThreadLocalRandom var21 = ThreadLocalRandom.current();
        float var47 = var21.nextFloat() * 0.080000006F;
        float var22 = 0.1F + var47;
        Material[] var31 = GOLD_PALETTE;
        int var58 = GOLD_PALETTE.length;
        int var49 = var21.nextInt(var58);
        Material var23 = var31[var49];
        Color[] var33 = GOLD_GLOWS;
        int var60 = GOLD_GLOWS.length;
        int var51 = var21.nextInt(var60);
        Color var24 = var33[var51];
        int var53 = var21.nextInt(6);
        int var25 = 10 + var53;
        float var26 = var21.nextFloat() * (float) Math.PI * 2.0F;
        double var65 = var2.getX();
        double var67 = var65 + var3;
        double var78 = (double)var22 / 2.0;
        double var69 = var67 - var78;
        double var83 = var2.getY() + 0.03;
        double var91 = var2.getZ();
        double var93 = var91 + var5;
        double var102 = (double)var22 / 2.0;
        double var95 = var93 - var102;
        Location var41 = new Location(var1, var69, var83, var95);
        Consumer<BlockDisplay> var72 = var5x -> {
            BlockData var16 = var23.createBlockData();
            var5x.setBlock(var16);
            Transformation var18 = goldShape(var22, var26, 1336094859);
            var5x.setTransformation(var18);
            Brightness var19 = new Brightness(var25, var25);
            var5x.setBrightness(var19);
            var5x.setGlowing(true);
            var5x.setGlowColorOverride(var24);
            var5x.setPersistent(false);
        };
        BlockDisplay var28 = (BlockDisplay)var1.spawn(var41, BlockDisplay.class, var72);
        return new PedestalFinale.GoldDot(var28, var3, var5, var22, 462513282);
    }

    private static Transformation goldShape(float var0, float var1, int var2) {
        Quaternionf var3 = new Quaternionf();
        Quaternionf var11 = var3.rotateY(var1);
        float var21 = var0 / 2.0F;
        float var28 = var0 / 2.0F;
        Vector3f var15 = new Vector3f(var21, 0.0F, var28);
        Vector3f var20 = new Vector3f(var15);
        Vector3f var13 = var11.transform(var20);
        Vector3f var23 = var15.sub(var13);
        Vector3f var29 = new Vector3f(var0, var0, var0);
        Quaternionf var30 = new Quaternionf();
        return new Transformation(var23, var11, var29, var30);
    }

    private void growGoldSign(Location var1, int var2) {
        if (this.goldSigil != null && this.goldSigil.isValid()) {
            this.goldSigil.setInterpolationDelay(0);
            this.goldSigil.setInterpolationDuration(59);
            TextDisplay var14 = this.goldSigil;
            Transformation var19 = sigilShape(sigilScale(3.0, 2104849909), 635460106);
            var14.setTransformation(var19);
        }
    }

    private void raiseBeam(World var1, Location var2, int var3) {
        Material var8 = Material.GOLD_BLOCK;
        BlockDisplay var14 = this.beamShell(var1, var2, var8, 0.22F, true, 2113763831);
        this.beam = var14;
        Material var25 = Material.YELLOW_STAINED_GLASS;
        BlockDisplay var16 = this.beamShell(var1, var2, var25, 0.638F, false, 2113763831);
        this.sheath = var16;
        BukkitScheduler var12 = Bukkit.getScheduler();
        BlissPlugin var18 = this.plugin;
        Runnable var21 = () -> {
            BlockDisplay var7 = this.beam;
            this.growBeam(var7, 0.22F, 90.0F, 14, 976159322);
            BlockDisplay var9 = this.sheath;
            this.growBeam(var9, 0.638F, 86.4F, 14, 976159322);
        };
        var12.runTaskLater(var18, var21, 1L);
    }

    private BlockDisplay beamShell(World var1, Location var2, Material var3, float var4, boolean var5, int var6) {
        Consumer<BlockDisplay> var15 = var3x -> {
            BlockData var15x = var3.createBlockData();
            var3x.setBlock(var15x);
            Transformation var17 = beamShape(var4, 0.01F, 636086266);
            var3x.setTransformation(var17);
            Brightness var18 = new Brightness(15, 15);
            var3x.setBrightness(var18);
            if (var5) {
                var3x.setGlowing(true);
                Color var20 = GOLD_GLOW;
                var3x.setGlowColorOverride(var20);
            }

            var3x.setPersistent(false);
        };
        return (BlockDisplay)var1.spawn(var2, BlockDisplay.class, var15);
    }

    private static Transformation beamShape(float var0, float var1, int var2) {
        float var15 = var0 / 2.0F;
        float var13 = 0.0F - var15;
        float var23 = var0 / 2.0F;
        float var20 = 0.0F - var23;
        Vector3f var4 = new Vector3f(var13, 0.0F, var20);
        AxisAngle4f var12 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
        Vector3f var14 = new Vector3f(var0, var1, var0);
        AxisAngle4f var18 = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
        return new Transformation(var4, var12, var14, var18);
    }

    private void growBeam(BlockDisplay var1, float var2, float var3, int var4, int var5) {
        if (var1 != null && var1.isValid()) {
            var1.setInterpolationDelay(0);
            var1.setInterpolationDuration(var4);
            Transformation var16 = beamShape(var2, var3, 636086266);
            var1.setTransformation(var16);
        }
    }

    private void beamEnergy(World var1, Location var2, int var3, int var4) {
        if (var3 > 16 && var3 % 4 == 0) {
            double var28 = (double)var3 * 0.38;
            double var266 = Math.sin(var28);
            double var172 = 0.3 * var266;
            float var30 = (float)(1.0 + var172);
            double var272 = Math.sin(var28 + Math.PI);
            double var176 = 0.24 * var272;
            float var31 = (float)(1.0 + var176);
            BlockDisplay var157 = this.beam;
            float var179 = 0.22F * var30;
            this.growBeam(var157, var179, 90.0F, 4, 976159322);
            BlockDisplay var159 = this.sheath;
            float var181 = 0.638F * var31;
            this.growBeam(var159, var181, 86.4F, 4, 976159322);
        }

        double var447 = (double)var3 * 0.42;

        for (byte var449 = 0; var449 < 40; var449 += 1) {
            double var455 = (double)var449 / 40.0;
            double var459 = var455 * var455 * 46.0;
            double var318 = 1.2000000000000002 * var455;
            double var241 = 1.0 + var318;
            double var465 = 0.8 * var241;
            double var249 = var455 * 3.0 * Math.PI * 2.0;
            double var40 = var447 + var249;

            for (int var42 = 0; var42 < 2; var42++) {
                double var254 = (double)var42 * Math.PI;
                double var43 = var40 + var254;
                Particle var169 = Particle.DUST;
                double var257 = var2.getX();
                double var330 = Math.cos(var43);
                double var332 = var330 * var465;
                double var259 = var257 + var332;
                double var335 = var2.getY();
                double var337 = var335 + var459;
                double var378 = var2.getZ();
                double var405 = Math.sin(var43);
                double var407 = var405 * var465;
                double var380 = var378 + var407;
                DustOptions var443;
                if (var42 == 0) {
                    var443 = BEAM_GOLD;
                } else {
                    var443 = BEAM_HOT;
                }

                Particles.spawn(var1, var169, var259, var337, var380, 1, 0.0, 0.0, 0.0, 0.0, var443, true, 954482185);
            }
        }

        ThreadLocalRandom var450 = ThreadLocalRandom.current();

        for (byte var451 = 0; var451 < 12; var451 += 1) {
            double var457 = var450.nextDouble() * Math.PI * 2.0;
            double var463 = var450.nextDouble() * 0.8 * 0.6;
            Particle var167 = Particle.END_ROD;
            double var229 = var2.getX();
            double var307 = Math.cos(var457);
            double var309 = var307 * var463;
            double var231 = var229 + var309;
            double var312 = var2.getY();
            double var364 = var450.nextDouble() * 3.0;
            double var314 = var312 + var364;
            double var367 = var2.getZ();
            double var398 = Math.sin(var457);
            double var400 = var398 * var463;
            double var369 = var367 + var400;
            Particles.spawn(var1, var167, var231, var314, var369, 0, 0.0, 1.0, 0.0, 1.7, null, true, 954482185);
        }

        for (byte var452 = 0; var452 < 3; var452 += 1) {
            double var32 = var450.nextDouble() * Math.PI * 2.0;
            double var461 = var450.nextDouble() * 46.0 * 0.55;
            double var292 = var450.nextDouble() * 1.6;
            double var213 = 0.7 + var292;
            double var38 = 0.8 * var213;
            Particle var166 = Particle.ELECTRIC_SPARK;
            double var216 = var2.getX();
            double var296 = Math.cos(var32);
            double var298 = var296 * var38;
            double var218 = var216 + var298;
            double var301 = var2.getY();
            double var303 = var301 + var461;
            double var355 = var2.getZ();
            double var389 = Math.sin(var32);
            double var391 = var389 * var38;
            double var357 = var355 + var391;
            Particles.spawn(var1, var166, var218, var303, var357, 2, 0.12, 0.5, 0.12, 0.03, null, true, 954482185);
        }

        if (var3 % 8 == 0) {
            double var189 = (double)(var3 / 8 % 5) * 0.55;
            double var453 = 0.8 + var189;

            for (int var35 = 0; var35 < 30; var35++) {
                double var36 = (double)var35 / 30.0 * Math.PI * 2.0;
                Particle var165 = Particle.DUST;
                double var199 = var2.getX();
                double var280 = Math.cos(var36);
                double var282 = var280 * var453;
                double var201 = var199 + var282;
                double var287 = var2.getY() + 0.08;
                double var344 = var2.getZ();
                double var382 = Math.sin(var36);
                double var384 = var382 * var453;
                double var346 = var344 + var384;
                DustOptions var26 = BEAM_GOLD;
                Particles.spawn(var1, var165, var201, var287, var346, 1, 0.0, 0.0, 0.0, 0.0, var26, true, 954482185);
            }
        }
    }

    private void removeDrawn(int var1) {
        for (PedestalFinale.GoldDot var5 : this.goldSign) {
            if (var5.d.isValid()) {
                var5.d.remove();
            }
        }

        this.goldSign.clear();
        if (this.goldSigil != null && this.goldSigil.isValid()) {
            this.goldSigil.remove();
        }

        this.goldSigil = null;
        if (this.beam != null && this.beam.isValid()) {
            this.beam.remove();
        }

        this.beam = null;
        if (this.sheath != null && this.sheath.isValid()) {
            this.sheath.remove();
        }

        this.sheath = null;
    }

    private void burst(World var1, Location var2, double var3, boolean var5, int var6) {
        if (this.plugin.globalState(724248306) == null || this.plugin.globalState(724248306).finaleKnockback(1560972786)) {
            ThreadLocalRandom var19 = ThreadLocalRandom.current();

            for (Entity var21 : var1.getNearbyEntities(var2, 40.0, 40.0, 40.0)) {
                if (var21 instanceof LivingEntity) {
                    LivingEntity var22 = (LivingEntity)var21;
                    Vector var47 = var22.getLocation().toVector();
                    Vector var80 = var2.toVector();
                    Vector var23 = var47.subtract(var80);
                    var23.setY(0);
                    if (var23.lengthSquared() < 1.0E-4) {
                        double var24 = var19.nextDouble() * Math.PI * 2.0;
                        double var103 = Math.cos(var24);
                        double var115 = Math.sin(var24);
                        Vector var62 = new Vector(var103, 0.0, var115);
                        var23 = var62;
                    }

                    Vector var64 = var23.normalize();
                    double var84 = 1.9 * var3;
                    Vector var65 = var64.multiply(var84);
                    double var88 = 0.9 * var3;
                    var65.setY(var88);
                    var22.setFallDistance(0.0F);
                    var22.setVelocity(var23);
                    PotionEffectType var109 = PotionEffectType.SLOW_FALLING;
                    PotionEffect var92 = new PotionEffect(var109, 300, 0, false, false);
                    var22.addPotionEffect(var92);
                    if (var5 && var22 instanceof Player var118) {
                        Map var77 = this.blast;
                        UUID var94 = var118.getUniqueId();
                        Vector var106 = var23.clone();
                        var77.put(var94, var106);
                    }
                }
            }
        }
    }

    private void slowMotion(int var1) {
        for (Entry var7 : this.blast.entrySet()) {
            Player var8 = Bukkit.getPlayer((UUID)var7.getKey());
            if (var8 != null && var8.isOnline()) {
                var8.setFallDistance(0.0F);
                Vector var30 = ((Vector)var7.getValue()).clone().multiply(0.07);
                var8.setVelocity(var30);
            }
        }
    }

    private void resumeBlast(World var1, Location var2, int var3) {
        for (Entry var10 : this.blast.entrySet()) {
            Player var11 = Bukkit.getPlayer((UUID)var10.getKey());
            if (var11 != null && var11.isOnline()) {
                var11.setFallDistance(0.0F);
                Vector var32 = (Vector)var10.getValue();
                var11.setVelocity(var32);
            }
        }

        Sound var6 = Sound.ENTITY_GENERIC_EXPLODE;
        var1.playSound(var2, var6, 5.0F, 1.0F);
    }

    private void screenFlash(World var1, Location var2, int var3, int var4) {
        for (Player var10 : var1.getPlayers()) {
            Location var20 = var10.getLocation();
            if (!(var20.distanceSquared(var2) > 25600.0)) {
                List var25 = this.screened;
                UUID var32 = var10.getUniqueId();
                if (!var25.contains(var32)) {
                    List var28 = this.screened;
                    UUID var34 = var10.getUniqueId();
                    var28.add(var34);
                }
            }
        }

        this.flashStart = var3;
    }

    private void sendScreens(World var1, Location var2, int var3, int var4) {
        if (!this.screened.isEmpty()) {
            if (this.plugin.globalState(724248306) == null || this.plugin.globalState(724248306).shaderEffects(1378906903)) {
                int var111 = this.flashStart;
                int var17 = var3 - var111;
                int var18 = 0;
                int var19 = 0;
                byte var20 = 0;
                if (var17 >= 0 && var17 < 3) {
                    double var148 = (double)(var17 + 1);
                    var19 = (int)Math.round(15.0 * var148 / 4.0);
                    var20 = 1;
                } else if (var17 >= 3 && var17 < 7) {
                    var18 = 15;
                } else if (var17 >= 7 && var17 < 21) {
                    double var171 = (double)(var17 - 3 - 4) / 14.0;
                    double var139 = 1.0 - var171;
                    var18 = (int)Math.round(15.0 * var139);
                }

                if (var18 == 0 && var19 == 0 && var3 >= 436 && var3 < 476) {
                    double var21 = (double)(var3 - 436) / 40.0;
                    double var177 = var21 * var21;
                    double var152 = 1.0 - var177;
                    int var126 = (int)Math.ceil(15.0 * var152);
                    var19 = Math.max(1, var126);
                    var20 = 0;
                }

                boolean var70;
                if (var3 < 496) {
                    var70 = true;
                } else {
                    var70 = false;
                }

                boolean var181 = var70;
                Location var23 = var2.clone().add(0.0, 1.0, 0.0);
                Iterator var24 = this.screened.iterator();

                while (var24.hasNext()) {
                    Player var26 = Bukkit.getPlayer((UUID)var24.next());
                    if (var26 != null && var26.isOnline()) {
                        long var27 = var26.getWorld().getFullTime() % 24000L;
                        if (var18 > 0) {
                            long var157 = (long)var18;
                            long var132 = 80020L + var157;
                            SignatureClock.send(var26, var132, var27, 399305443);
                        } else if (var19 > 0) {
                            long var135 = irisDay(var26, var23, var19, var20, 321105190);
                            SignatureClock.send(var26, var135, var27, 399305443);
                        } else if (var181) {
                            SignatureClock.send(var26, 80016L, var27, 399305443);
                        } else {
                            var26.resetPlayerTime();
                        }
                    }
                }
            }
        }
    }

    private static long irisDay(Player var0, Location var1, int var2, int var3, int var4) {
        Location var12 = var0.getEyeLocation();
        double var29 = var1.getX();
        double var104 = var12.getX();
        double var13 = var29 - var104;
        double var34 = var1.getY();
        double var107 = var12.getY();
        double var15 = var34 - var107;
        double var39 = var1.getZ();
        double var110 = var12.getZ();
        double var17 = var39 - var110;
        double var45 = var13 * var13;
        double var116 = var15 * var15;
        double var47 = var45 + var116;
        double var120 = var17 * var17;
        double var19 = Math.sqrt(var47 + var120);
        if (var19 < 1.0E-6) {
            var13 = 0.0;
            var15 = 0.0;
            var17 = 1.0;
            var19 = 1.0;
        }

        double var66 = -var13;
        double var21 = Math.toDegrees(Math.atan2(var66, var17));
        double var23 = Math.toDegrees(-Math.asin(var15 / var19));
        double var130 = (double)var12.getYaw();
        int var25 = quantize(wrap180(var21 - var130, 1407259420), 437535463);
        double var134 = (double)var12.getPitch();
        int var26 = quantize(wrap180(var23 - var134, 1407259420), 437535463);
        long var137 = (long)var2;
        long var98 = 80040L + var137;
        long var150 = (long)var3;
        long var141 = 16L * var150;
        long var100 = var98 + var141;
        long var153 = (long)var25;
        long var159 = (long)var26;
        long var157 = 512L * var159;
        long var155 = var153 + var157;
        long var145 = 32L * var155;
        return var100 + var145;
    }

    private static double wrap180(double var0, int var2) {
        double var33 = var0 % 360.0;
        if (var33 >= 180.0) {
            var33 -= 360.0;
        }

        if (var33 < -180.0) {
            var33 += 360.0;
        }

        return var33;
    }

    private static int quantize(double var0, int var2) {
        int var19 = (int)Math.round((var0 + 180.0) / 360.0 * 511.0);
        int var10 = Math.min(511, var19);
        return Math.max(0, var10);
    }

    private void returning(World var1, Location var2, int var3, int var4) {
        ThreadLocalRandom var24 = ThreadLocalRandom.current();
        double var12 = (double)var3 / 40.0;
        double var91 = 1.0 - var12;
        double var27 = 14.0 * var91 + 0.6;
        Location var29 = var2.clone().add(0.0, 1.0, 0.0);

        for (int var30 = 0; var30 < 24; var30++) {
            double var31 = var24.nextDouble() * Math.PI * 2.0;
            double var100 = var24.nextDouble();
            double var33 = Math.acos(2.0 * var100 - 1.0);
            Location var69 = var29.clone();
            double var107 = Math.sin(var33);
            double var80 = var27 * var107;
            double var111 = Math.cos(var31);
            double var82 = var80 * var111;
            double var123 = Math.cos(var33);
            double var115 = var27 * var123;
            double var133 = Math.sin(var33);
            double var127 = var27 * var133;
            double var137 = Math.sin(var31);
            double var129 = var127 * var137;
            Location var35 = var69.add(var82, var115, var129);
            Vector var72 = var29.toVector();
            Vector var85 = var35.toVector();
            Vector var36 = var72.subtract(var85).normalize().multiply(0.6);
            Particle var88 = Particle.END_ROD;
            double var119 = var36.getX();
            double var131 = var36.getY();
            double var139 = var36.getZ();
            Particles.spawn(var1, var88, var35, 0, var119, var131, var139, 1.0, null, true, 1748031581);
        }
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
        int[] var3 = new int[]{40, 32, 25, 19, 14, 10};
        BOOM_GAPS = var3;
        Material[] var5 = new Material[8];
        Material var41 = Material.GOLD_BLOCK;
        var5[0] = var41;
        Material var42 = Material.GOLD_BLOCK;
        var5[1] = var42;
        Material var43 = Material.GOLD_BLOCK;
        var5[2] = var43;
        Material var44 = Material.GOLD_BLOCK;
        var5[3] = var44;
        Material var45 = Material.RAW_GOLD_BLOCK;
        var5[4] = var45;
        Material var46 = Material.RAW_GOLD_BLOCK;
        var5[5] = var46;
        Material var47 = Material.RAW_GOLD_BLOCK;
        var5[6] = var47;
        Material var48 = Material.GILDED_BLACKSTONE;
        var5[7] = var48;
        GOLD_PALETTE = var5;
        Color[] var7 = new Color[3];
        Color var50 = Color.fromRGB(16765530);
        var7[0] = var50;
        Color var52 = Color.fromRGB(16758311);
        var7[1] = var52;
        Color var54 = Color.fromRGB(16771488);
        var7[2] = var54;
        GOLD_GLOWS = var7;
        Color var33 = Color.fromRGB(16762940);
        DustOptions var10 = new DustOptions(var33, 1.2F);
        BEAM_GOLD = var10;
        Color var35 = Color.fromRGB(16774866);
        DustOptions var11 = new DustOptions(var35, 0.8F);
        BEAM_HOT = var11;
        boolean var14;
        if (Particle.FLASH.getDataType() == Color.class) {
            var14 = true;
        } else {
            var14 = false;
        }

        FLASH_NEEDS_COLOR = var14;
    }

    private static final class GoldDot {
        final BlockDisplay d;
        final double dx;
        final double dz;
        final float size;

        GoldDot(BlockDisplay var1, double var2, double var4, float var6, int var7) {
            this.d = var1;
            this.dx = var2;
            this.dz = var4;
            this.size = var6;
        }
    }
}
