package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import com.bliss.gems.GrantedEffect;
import com.bliss.pedestal.CrushedPedestalOrb;
import io.papermc.paper.entity.TeleportFlag;
import io.papermc.paper.entity.TeleportFlag.Relative;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public final class HonoredSummonRitual {
    private static final Particle SPARK = Particle.END_ROD;
    private static final boolean FLASH_NEEDS_COLOR;
    private static final double RADIUS = 2.6;
    private static final double STEP = 0.18;
    private static final double SHAPE_THICK = 0.09;
    private static final double HG_W = 0.9880000000000001;
    private static final double HG_H = 1.612;
    private static final int SMALL_BURSTS = 5;
    private static final int SMALL_INTERVAL = 24;
    private static final int BIG_AT = 126;
    private static final int SHAPE_AT = 134;
    private static final int ARMS_AT = 214;
    private static final int FLOAT_AT = 254;
    private static final int SHAPE_TICKS = 500;
    private static final int END_AT = 634;
    private static final int SHAPE_REDRAW = 4;
    private static final double LIFT = 8.0;
    private static final double RISE_SPEED = 0.0455;
    private static final double CENTRE_RADIUS = 1.6;
    private static final int ARMS = 4;
    private static final double ARM_REACH = 4.42;
    private static final double ARM_REACH_ASC = 3.64;
    private static final double SOUL_SPIN = 0.063;
    private static final double SOUL_FLIGHT = 25.0;
    private static final double ARM_RISE = 0.35;
    private static final double ARM_BASE_Y = 0.6;
    private static final double SWIRL_SPEED;
    private static final int BOLT_SPAWN_EVERY = 14;
    private static final int BOLT_REVEAL = 5;
    private static final int BOLT_HOLD = 14;
    private static final int ASC_BURSTS = 3;
    private static final int ASC_BURST_INTERVAL = 40;
    private static final double ASC_BURST_SLOW = 15.0;
    private static final int ASC_FLOAT_START = 88;
    private static final double ASC_LIFT = 25.0;
    private static final double ASC_RISE = 0.35;
    private static final int ASC_SWARM_TICKS = 24;
    private static final int ASC_CLIMB_MAX_TICKS = 200;
    private static final int ASC_WING_GROW = 26;
    private static final int ASC_WING_HOLD = 34;
    private static final int ASC_DESIGN_ORANGE = 70;
    private static final int ASC_DESIGN_CYAN = 70;
    private static final int ASC_DESIGN_END = 140;
    private static final double ASC_HOLD_RADIUS = 2.2;
    private static final int ASC_DARK_AT = 40;
    private static final double ASC_DARK_RADIUS = 50.0;
    private static final int WING_FEATHERS = 20;
    private static final double WING_SPAN = 5.4;
    private static final double WING_LIFT = 2.8;
    private static final double WING_BACK = 1.4;
    private static final double WING_DEPTH = 2.8;
    private static final int WING_FINGERS = 6;
    private static final double WING_FINGER_LEN = 3.2;
    private final BlissPlugin plugin;
    private final CageManager cage;
    private boolean running;

    private static void flash(World var0, Location var1, int var2, double var3, double var5, double var7, int var9) {
        if (FLASH_NEEDS_COLOR) {
            Particle var26 = Particle.FLASH;
            Color var37 = Color.WHITE;
            Particles.spawn(var0, var26, var1, var2, var3, var5, var7, 0.0, var37, true, 1748031581);
        } else {
            Particle var11 = Particle.FLASH;
            Particles.spawn(var0, var11, var1, var2, var3, var5, var7, 0.0, null, true, 1748031581);
        }
    }

    public HonoredSummonRitual(BlissPlugin var1, CageManager var2, int var3) {
        this.plugin = var1;
        this.cage = var2;
    }

    public boolean isRunning(int var1) {
        return this.running;
    }

    public void begin(Location var1, Player var2, int var3) {
        if (!this.running) {
            this.running = true;
            if (this.cage != null && this.cage.isActive(128579983) && this.cage.center(859533338) != null) {
                this.cage.setPendingEngage(true, 1492496793);
            }

            final World var10 = var1.getWorld();
            final Location var11 = var1.clone();
            final UUID var30;
            if (var2 == null) {
                var30 = null;
            } else {
                var30 = var2.getUniqueId();
            }

            BukkitRunnable var31 = new BukkitRunnable() {
                int t = 0;
                boolean floating = false;
                double targetY = 0.0;
                double holdX = 0.0;
                double holdZ = 0.0;
                double spin = 0.0;
                final List<HonoredSummonRitual.Bolt> bolts;

                {
                    ArrayList var32 = new ArrayList();
                    this.bolts = var32;
                }

                public void run() {
                    Player var168;
                    if (var30 == null) {
                        var168 = null;
                    } else {
                        Server var167 = HonoredSummonRitual.this.plugin.getServer();
                        UUID var245 = var30;
                        var168 = var167.getPlayer(var245);
                    }

                    if (this.t == 0) {
                        var10.setStorm(true);
                        var10.setThundering(true);
                        var10.setWeatherDuration(12634);
                        var10.setThunderDuration(12634);
                        World var29 = var10;
                        Location var173 = var11;
                        Sound var3 = Sound.BLOCK_BEACON_ACTIVATE;
                        var29.playSound(var173, var3, 8.0F, 1.4F);
                    }

                    if (this.t < 126 && this.t % 24 == 0) {
                        HonoredSummonRitual var36 = HonoredSummonRitual.this;
                        World var177 = var10;
                        Location var247 = var11;
                        var36.smallBurst(var177, var247, 1543757482);
                    }

                    if (this.t == 126) {
                        HonoredSummonRitual var40 = HonoredSummonRitual.this;
                        World var180 = var10;
                        Location var249 = var11;
                        var40.bigBurst(var180, var249, 254665940);
                    }

                    if (this.t == 254 && var168 != null && var168.isOnline()) {
                        HonoredSummonRitual var47 = HonoredSummonRitual.this;
                        Location var251 = var11;
                        if (var47.inCentre(var168, var251, 127007166)) {
                            var168.setGlowing(true);
                            Location var14 = var168.getLocation();
                            double var187 = var14.getY() + 8.0;
                            this.targetY = var187;
                            double var190 = var14.getX();
                            this.holdX = var190;
                            double var193 = var14.getZ();
                            this.holdZ = var193;
                            this.floating = true;
                        }
                    }

                    if (this.t >= 134 && this.t < 634) {
                        if ((this.t - 134) % 4 == 0) {
                            HonoredSummonRitual var65 = HonoredSummonRitual.this;
                            World var201 = var10;
                            Location var253 = var11;
                            var65.drawShape(var201, var253, 1665102190);
                        }

                        if (this.t % 3 == 0) {
                            HonoredSummonRitual var70 = HonoredSummonRitual.this;
                            World var204 = var10;
                            Location var255 = var11;
                            var70.ambient(var204, var255, 71943518);
                        }

                        if (this.t % 14 == 0) {
                            HonoredSummonRitual var75 = HonoredSummonRitual.this;
                            List var207 = this.bolts;
                            Location var257 = var11;
                            var75.spawnBolts(var207, var257, 1043644052);
                        }

                        HonoredSummonRitual var77 = HonoredSummonRitual.this;
                        World var209 = var10;
                        List var259 = this.bolts;
                        var77.updateBolts(var209, var259, 367880801);
                        if (this.t >= 214) {
                            double var213 = this.spin + 0.063;
                            this.spin = var213;
                            HonoredSummonRitual var82 = HonoredSummonRitual.this;
                            World var216 = var10;
                            Location var261 = var11;
                            double var289 = this.spin;
                            var82.swirlArms(var216, var261, var289, 4.42, 745601489);
                        }
                    }

                    if (this.floating && var168 != null && var168.isOnline()) {
                        Location var319 = var168.getLocation();
                        double var93 = var319.getY() + 0.0455;
                        double var265 = this.targetY;
                        double var15 = Math.min(var93, var265);
                        var168.setFallDistance(0.0F);
                        World var292 = var319.getWorld();
                        double var306 = this.holdX;
                        double var315 = this.holdZ;
                        float var317 = var319.getYaw();
                        float var318 = var319.getPitch();
                        Location var218 = new Location(var292, var306, var15, var315, var317, var318);
                        var168.teleport(var218);
                        if (this.t % 10 == 0) {
                            HonoredSummonRitual var104 = HonoredSummonRitual.this;
                            World var221 = var10;
                            Location var268 = var11;
                            var104.floatStreaks(var221, var268, 1936013942);
                        }

                        double var272 = this.targetY - 0.05;
                        if (var15 < var272) {
                            HonoredSummonRitual var109 = HonoredSummonRitual.this;
                            World var223 = var10;
                            Location var275 = var168.getLocation();
                            double var297 = (double)this.t * 0.3;
                            var109.risingSwirl(var223, var275, var297, 1401879257);
                        }
                    }

                    if (this.t < 634) {
                        int var226 = this.t + 1;
                        this.t = var226;
                    } else {
                        World var114 = var10;
                        Location var228 = var11;
                        Sound var277 = Sound.BLOCK_BEACON_DEACTIVATE;
                        var114.playSound(var228, var277, 8.0F, 1.6F);
                        Location var120;
                        if (var168 != null && var168.isOnline()) {
                            var120 = var168.getLocation().clone();
                        } else {
                            var120 = var11;
                        }

                        if (var168 != null && var168.isOnline()) {
                            var168.setGlowing(false);
                            var168.setFallDistance(0.0F);
                        }

                        if (HonoredSummonRitual.this.cage != null
                            && HonoredSummonRitual.this.cage.isActive(128579983)
                            && HonoredSummonRitual.this.cage.center(859533338) != null) {
                            HonoredSummonRitual.this.cage.engage(741182186);
                            if (var168 != null && var168.isOnline()) {
                                Location var234 = HonoredSummonRitual.this.cage.center(859533338);
                                var168.teleport(var234);
                            }
                        } else if (var168 != null && var168.isOnline()) {
                            PotionEffectType var304 = PotionEffectType.SLOW_FALLING;
                            PotionEffect var243 = new PotionEffect(var304, 80, 0);
                            var168.addPotionEffect(var243);
                        }

                        HonoredSummonRitual var146 = HonoredSummonRitual.this;
                        World var236 = var10;
                        var146.fluxBurst(var236, var120, 6.5, 1860760324);
                        HonoredSummonRitual var148 = HonoredSummonRitual.this;
                        World var238 = var10;
                        Location var280 = var11;
                        var148.randomLines(var238, var280, 818185351);
                        BukkitScheduler var153 = HonoredSummonRitual.this.plugin.getServer().getScheduler();
                        BlissPlugin var241 = HonoredSummonRitual.this.plugin;
                        Runnable var283 = () -> {
                            var10.setThundering(false);
                            var10.setStorm(false);
                        };
                        var153.runTaskLater(var241, var283, 200L);
                        HonoredSummonRitual.this.running = false;
                        this.cancel();
                    }
                }
            };
            BlissPlugin var36 = this.plugin;
            var31.runTaskTimer(var36, 0L, 1L);
        }
    }

    public void beginAscension(Player var1, Location var2, int var3) {
        this.beginJourney(var1, var2, true, 2074136997);
    }

    public void beginDeparture(Player var1, Location var2, int var3) {
        this.beginJourney(var1, var2, false, 2074136997);
    }

    private void beginJourney(Player var1, Location var2, final boolean var3, int var4) {
        if (!this.running) {
            if (var1 != null && safeWorld(var2, 309480550) != null) {
                this.running = true;
                if (var3 && this.plugin.controlRoom(1275615420) != null) {
                    ControlRoomManager var26 = this.plugin.controlRoom(1275615420);
                    UUID var36 = var1.getUniqueId();
                    var26.setPending(var36, 662296968);
                }

                final World var12 = var1.getWorld();
                final UUID var13 = var1.getUniqueId();
                final Location var14 = var2.clone();
                BukkitRunnable var33 = new BukkitRunnable() {
                    int t = 0;
                    int climbTicks = 0;
                    boolean floating = false;
                    boolean reachedTop = false;
                    int swarmAge = -1;
                    int wingAge = -1;
                    double targetY = 0.0;
                    double holdX = 0.0;
                    double holdZ = 0.0;
                    double spin = 0.0;
                    Location anchor = null;
                    final List<HonoredSummonRitual.Bolt> bolts;

                    {
                        ArrayList var45 = new ArrayList();
                        this.bolts = var45;
                    }

                    public void run() {
                        Server var22 = HonoredSummonRitual.this.plugin.getServer();
                        UUID var370 = var13;
                        Player var10 = var22.getPlayer(var370);
                        if (var10 != null && var10.isOnline()) {
                            Location var11 = var10.getLocation();
                            World var30 = var11.getWorld();
                            World var372 = var12;
                            if (!var30.equals(var372)) {
                                HonoredSummonRitual var330 = HonoredSummonRitual.this;
                                World var514 = var12;
                                var330.stopStorm(var514, 491720192);
                                HonoredSummonRitual.this.running = false;
                                if (var3 && HonoredSummonRitual.this.plugin.controlRoom(1275615420) != null) {
                                    ControlRoomManager var342 = HonoredSummonRitual.this.plugin.controlRoom(1275615420);
                                    UUID var517 = var13;
                                    var342.setPending(var517, 662296968);
                                } else if (!var3 && HonoredSummonRitual.this.plugin.departurePrompt(1001037186) != null) {
                                    HonoredSummonRitual.this.plugin.departurePrompt(1001037186).rearm(91693602);
                                }

                                this.cancel();
                            } else {
                                if (this.t == 0) {
                                    Location var374 = var11.clone();
                                    this.anchor = var374;
                                    var12.setStorm(true);
                                    var12.setThundering(true);
                                    var12.setWeatherDuration(6000);
                                    var12.setThunderDuration(6000);
                                    var10.setGlowing(true);
                                    World var45 = var12;
                                    Location var381 = this.anchor;
                                    Sound var3x = Sound.BLOCK_BEACON_POWER_SELECT;
                                    var45.playSound(var381, var3x, 6.0F, 0.9F);
                                    HonoredSummonRitual var47 = HonoredSummonRitual.this;
                                    Location var383 = this.anchor;
                                    var47.burstOfLightAround(var383, 50.0, 7338311);
                                }

                                if (this.t >= 40 && this.t % 20 == 0) {
                                    for (Player var756 : var12.getPlayers()) {
                                        Location var320 = var756.getLocation();
                                        if (!(var320.distanceSquared(var11) > 2500.0)) {
                                            UUID var325 = var756.getUniqueId();
                                            UUID var511 = var13;
                                            boolean var759 = var325.equals(var511);
                                            PotionEffectType var657 = PotionEffectType.DARKNESS;
                                            byte var710;
                                            if (var759) {
                                                var710 = 1;
                                            } else {
                                                var710 = 0;
                                            }

                                            PotionEffect var512 = new PotionEffect(var657, 70, var710, false, false);
                                            var756.addPotionEffect(var512);
                                        }
                                    }

                                    PotionEffectType var606 = PotionEffectType.SLOWNESS;
                                    PotionEffect var386 = new PotionEffect(var606, 70, 9, false, false);
                                    GrantedEffect.grant(var10, var386, 491118892);
                                }

                                if (this.t < 140) {
                                    Location var65;
                                    if (this.anchor != null) {
                                        var65 = this.anchor;
                                    } else {
                                        var65 = var11;
                                    }

                                    if (this.anchor != null) {
                                        World var69 = var11.getWorld();
                                        World var390 = this.anchor.getWorld();
                                        if (var69.equals(var390)) {
                                            double var72 = var11.getX();
                                            double var528 = this.anchor.getX();
                                            double var13x = var72 - var528;
                                            double var77 = var11.getY();
                                            double var532 = this.anchor.getY();
                                            double var16 = var77 - var532;
                                            double var82 = var11.getZ();
                                            double var536 = this.anchor.getZ();
                                            double var18 = var82 - var536;
                                            double var88 = var13x * var13x;
                                            double var542 = var16 * var16;
                                            double var90 = var88 + var542;
                                            double var546 = var18 * var18;
                                            if (var90 + var546 > 4.0E-4) {
                                                Location var392 = this.anchor;
                                                HonoredSummonRitual.holdTeleport(var10, var392, 13989831);
                                            }
                                        }
                                    }

                                    if (this.t % 4 == 0) {
                                        HonoredSummonRitual var100 = HonoredSummonRitual.this;
                                        World var395 = var12;
                                        var100.drawShape(var395, var65, 1665102190);
                                        HonoredSummonRitual var102 = HonoredSummonRitual.this;
                                        World var397 = var12;
                                        double var611 = (double)this.t * 0.063;
                                        var102.swirlArms(var397, var65, var611, 3.64, 745601489);
                                    }

                                    if (this.t % 3 == 0) {
                                        HonoredSummonRitual var107 = HonoredSummonRitual.this;
                                        World var400 = var12;
                                        var107.ambient(var400, var65, 71943518);
                                    }

                                    Particle var369;
                                    if (this.t < 70) {
                                        var369 = Particle.TRIAL_SPAWNER_DETECTION;
                                    } else {
                                        var369 = Particle.TRIAL_SPAWNER_DETECTION_OMINOUS;
                                    }

                                    if (this.t % 2 == 0) {
                                        HonoredSummonRitual var114 = HonoredSummonRitual.this;
                                        World var404 = var12;
                                        int var665 = this.t / 2;
                                        var114.trialLines(var404, var65, var369, var665, 291940857);
                                    }

                                    if (this.t == 70) {
                                        World var118 = var12;
                                        Sound var554 = Sound.BLOCK_BEACON_POWER_SELECT;
                                        var118.playSound(var65, var554, 6.0F, 1.3F);
                                    }

                                    int var408 = this.t + 1;
                                    this.t = var408;
                                } else {
                                    if (!this.floating && !this.reachedTop) {
                                        Location var315;
                                        if (this.anchor != null) {
                                            var315 = this.anchor;
                                        } else {
                                            var315 = var11;
                                        }

                                        double var412 = var315.getY() + 25.0;
                                        this.targetY = var412;
                                        double var415 = var11.getX();
                                        this.holdX = var415;
                                        double var418 = var11.getZ();
                                        this.holdZ = var418;
                                        this.floating = true;
                                        World var131 = var12;
                                        Sound var556 = Sound.BLOCK_BEACON_POWER_SELECT;
                                        var131.playSound(var11, var556, 8.0F, 1.0F);
                                    }

                                    if (this.floating && !this.reachedTop) {
                                        double var137 = this.targetY;
                                        double var558 = var11.getY();
                                        double var751 = var137 - var558;
                                        int var423 = this.climbTicks + 1;
                                        this.climbTicks = var423;
                                        if (!(var751 <= 0.1) && this.climbTicks <= 200) {
                                            double var602 = var751 * 0.25 + 0.06;
                                            double var757 = Math.min(0.43, var602);
                                            var10.setFallDistance(0.0F);
                                            double var651 = this.holdX;
                                            double var703 = var11.getX();
                                            double var655 = (var651 - var703) * 0.2;
                                            double var733 = this.holdZ;
                                            double var745 = var11.getZ();
                                            double var737 = (var733 - var745) * 0.2;
                                            Vector var508 = new Vector(var655, var757, var737);
                                            var10.setVelocity(var508);
                                        } else {
                                            if (this.climbTicks > 200 && var751 > 0.1) {
                                                double var427 = var11.getY();
                                                this.targetY = var427;
                                            }

                                            this.reachedTop = true;
                                            this.swarmAge = 0;
                                            World var156 = var12;
                                            Location var432 = var10.getLocation();
                                            Sound var565 = Sound.BLOCK_BEACON_POWER_SELECT;
                                            var156.playSound(var432, var565, 8.0F, 1.4F);
                                        }

                                        if (this.t % 3 == 0) {
                                            HonoredSummonRitual var161 = HonoredSummonRitual.this;
                                            World var435 = var12;
                                            Location var567 = var10.getLocation();
                                            var161.ambient(var435, var567, 71943518);
                                        }

                                        if (this.t % 10 == 0) {
                                            HonoredSummonRitual var166 = HonoredSummonRitual.this;
                                            World var438 = var12;
                                            Location var569 = var10.getLocation();
                                            var166.floatStreaks(var438, var569, 1936013942);
                                        }

                                        if (this.t % 14 == 0) {
                                            HonoredSummonRitual var171 = HonoredSummonRitual.this;
                                            World var441 = var12;
                                            Location var571 = var10.getLocation();
                                            var171.randomLines(var441, var571, 818185351);
                                        }

                                        if (this.t % 14 == 0) {
                                            HonoredSummonRitual var176 = HonoredSummonRitual.this;
                                            List var444 = this.bolts;
                                            Location var573 = var10.getLocation();
                                            var176.spawnBolts(var444, var573, 1043644052);
                                        }

                                        HonoredSummonRitual var178 = HonoredSummonRitual.this;
                                        World var446 = var12;
                                        List var575 = this.bolts;
                                        var178.updateBolts(var446, var575, 367880801);
                                        double var447 = this.spin;
                                        double var619 = HonoredSummonRitual.SWIRL_SPEED;
                                        double var449 = var447 + var619;
                                        this.spin = var449;
                                        HonoredSummonRitual var181 = HonoredSummonRitual.this;
                                        World var452 = var12;
                                        Location var577 = var10.getLocation();
                                        double var622 = this.spin;
                                        var181.risingSwirl(var452, var577, var622, 1401879257);
                                    }

                                    if (this.reachedTop) {
                                        var10.setFallDistance(0.0F);
                                        double var625 = this.holdX;
                                        double var686 = var11.getX();
                                        double var629 = (var625 - var686) * 0.2;
                                        double var691 = this.targetY;
                                        double var716 = var11.getY();
                                        double var695 = (var691 - var716) * 0.2;
                                        double var721 = this.holdZ;
                                        double var740 = var11.getZ();
                                        double var725 = (var721 - var740) * 0.2;
                                        Vector var454 = new Vector(var629, var695, var725);
                                        var10.setVelocity(var454);
                                        HonoredSummonRitual var187 = HonoredSummonRitual.this;
                                        World var456 = var12;
                                        List var579 = this.bolts;
                                        var187.updateBolts(var456, var579, 367880801);
                                    }

                                    if (this.swarmAge >= 0 && this.wingAge < 0) {
                                        if (this.swarmAge == 0) {
                                            World var195 = var12;
                                            Location var458 = var10.getLocation();
                                            Sound var580 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
                                            var195.playSound(var458, var580, 6.0F, 1.5F);
                                        }

                                        HonoredSummonRitual var197 = HonoredSummonRitual.this;
                                        World var460 = var12;
                                        Location var582 = var10.getLocation();
                                        int var633 = this.swarmAge;
                                        var197.swarm(var460, var582, var633, 183711085);
                                        int var462 = this.swarmAge + 1;
                                        this.swarmAge = var462;
                                        if (this.swarmAge >= 24) {
                                            this.wingAge = 0;
                                        }
                                    }

                                    if (this.wingAge >= 0) {
                                        var10.setGlowing(false);
                                        var10.setFallDistance(0.0F);
                                        World var207 = var12;
                                        Location var468 = var10.getLocation();
                                        Sound var584 = Sound.ITEM_TOTEM_USE;
                                        var207.playSound(var468, var584, 8.0F, 1.0F);
                                        World var209 = var12;
                                        Location var472 = var10.getLocation().clone().add(0.0, 1.0, 0.0);
                                        HonoredSummonRitual.flash(var209, var472, 3, 0.3, 0.6, 0.3, 605603475);
                                        Location var306;
                                        if (var3) {
                                            HonoredSummonRitual var305 = HonoredSummonRitual.this;
                                            Location var504 = var14;
                                            var306 = var305.liveControlRoom(var504, 2132804542);
                                        } else if (HonoredSummonRitual.safeWorld(var14, 309480550) == null) {
                                            var306 = null;
                                        } else {
                                            var306 = var14;
                                        }

                                        if (var306 != null && var10.teleport(var306)) {
                                            PotionEffectType var474 = PotionEffectType.SLOWNESS;
                                            GrantedEffect.strip(var10, var474, 553187889);
                                            World var755 = var306.getWorld();
                                            HonoredSummonRitual var222 = HonoredSummonRitual.this;
                                            Location var590 = var306.clone().add(0.0, 1.0, 0.0);
                                            var222.fluxBurst(var755, var590, 4.0, 1860760324);
                                            Sound var591 = Sound.BLOCK_BEACON_ACTIVATE;
                                            var755.playSound(var306, var591, 6.0F, 1.2F);
                                            HonoredSummonRitual var225 = HonoredSummonRitual.this;
                                            World var478 = var12;
                                            var225.stopStorm(var478, 491720192);
                                            HonoredSummonRitual.this.running = false;
                                            if (var3) {
                                                if (HonoredSummonRitual.this.plugin.controlRoom(1275615420) != null) {
                                                    HonoredSummonRitual.this.plugin.controlRoom(1275615420).clearPending(21664137);
                                                    HonoredSummonRitual.this.plugin.controlRoom(1275615420).beginSession(1185796157);
                                                }

                                                BlissPlugin var269 = HonoredSummonRitual.this.plugin;
                                                UUID var492 = var13;
                                                var269.gatherControlRoomAudience(var492, 658540308);
                                            } else {
                                                if (HonoredSummonRitual.this.plugin.crushedPedestalOrb(952223969) != null) {
                                                    CrushedPedestalOrb var237 = HonoredSummonRitual.this.plugin.crushedPedestalOrb(952223969);
                                                    var237.ignoreUntilClear(var10, 1069596687);
                                                }

                                                BlissPlugin var240 = HonoredSummonRitual.this.plugin;
                                                UUID var482 = var13;
                                                var240.completeAssessmentAtPedestal(var482, 1401753802);
                                                BlissPlugin var243 = HonoredSummonRitual.this.plugin;
                                                UUID var484 = var13;
                                                var243.scatterAudienceAroundPedestal(var484, var306, 1666630238);
                                                if (HonoredSummonRitual.this.plugin.departureFlight(1434080140) != null) {
                                                    BukkitScheduler var248 = Bukkit.getScheduler();
                                                    BlissPlugin var487 = HonoredSummonRitual.this.plugin;
                                                    UUID var643 = var13;
                                                    Runnable var594 = () -> {
                                                        DepartureFlight var6 = HonoredSummonRitual.this.plugin.departureFlight(1434080140);
                                                        var6.begin(var643, 949513185);
                                                    };
                                                    var248.runTaskLater(var487, var594, 10L);
                                                }

                                                Location var15 = var306.clone();
                                                BukkitScheduler var252 = Bukkit.getScheduler();
                                                BlissPlugin var490 = HonoredSummonRitual.this.plugin;
                                                Runnable var596 = () -> {
                                                    if (HonoredSummonRitual.this.plugin.pedestalFinale(1936651090) != null) {
                                                        PedestalFinale var10x = HonoredSummonRitual.this.plugin.pedestalFinale(1936651090);
                                                        var10x.begin(var15, 1122725959);
                                                    }
                                                };
                                                var252.runTaskLater(var490, var596, 100L);
                                            }

                                            this.cancel();
                                        } else {
                                            PotionEffectType var493 = PotionEffectType.SLOWNESS;
                                            GrantedEffect.strip(var10, var493, 553187889);
                                            HonoredSummonRitual var272 = HonoredSummonRitual.this;
                                            World var495 = var12;
                                            var272.stopStorm(var495, 491720192);
                                            HonoredSummonRitual.this.running = false;
                                            var10.setFallDistance(0.0F);
                                            PotionEffectType var649 = PotionEffectType.SLOW_FALLING;
                                            PotionEffect var498 = new PotionEffect(var649, 400, 0, false, false);
                                            var10.addPotionEffect(var498);
                                            if (var3 && HonoredSummonRitual.this.plugin.controlRoom(1275615420) != null) {
                                                ControlRoomManager var287 = HonoredSummonRitual.this.plugin.controlRoom(1275615420);
                                                UUID var500 = var13;
                                                var287.setPending(var500, 662296968);
                                            }

                                            if (!var3 && HonoredSummonRitual.this.plugin.departurePrompt(1001037186) != null) {
                                                HonoredSummonRitual.this.plugin.departurePrompt(1001037186).rearm(91693602);
                                            }

                                            Logger var301 = HonoredSummonRitual.this.plugin.getLogger();
                                            String var523;
                                            if (var3) {
                                                var523 = "Honored One ascension could not deliver to the Control Room — parked; it retries on their next join.";
                                            } else {
                                                var523 = "Honored One departure could not deliver to the pedestal — he stays where the ritual left him.";
                                            }

                                            var301.warning(var523);
                                            this.cancel();
                                        }
                                    } else {
                                        int var506 = this.t + 1;
                                        this.t = var506;
                                    }
                                }
                            }
                        } else {
                            HonoredSummonRitual var355 = HonoredSummonRitual.this;
                            World var519 = var12;
                            var355.stopStorm(var519, 491720192);
                            HonoredSummonRitual.this.running = false;
                            if (var3 && HonoredSummonRitual.this.plugin.controlRoom(1275615420) != null) {
                                ControlRoomManager var367 = HonoredSummonRitual.this.plugin.controlRoom(1275615420);
                                UUID var522 = var13;
                                var367.setPending(var522, 662296968);
                            }

                            this.cancel();
                        }
                    }
                };
                BlissPlugin var38 = this.plugin;
                var33.runTaskTimer(var38, 0L, 1L);
            }
        }
    }

    private Location liveControlRoom(Location var1, int var2) {
        if (this.plugin.controlRoom(1275615420) != null && this.plugin.controlRoom(1275615420).isConfigured(553762660)) {
            return this.plugin.controlRoom(1275615420).location(1988022717);
        } else {
            Location var16;
            if (safeWorld(var1, 309480550) == null) {
                var16 = null;
            } else {
                var16 = var1;
            }

            return var16;
        }
    }

    private static World safeWorld(Location var0, int var1) {
        if (var0 == null) {
            return null;
        } else {
            try {
                return var0.getWorld();
            } catch (IllegalArgumentException var7) {
                return null;
            }
        }
    }

    private static void holdTeleport(Player var0, Location var1, int var2) {
        var0.setFallDistance(0.0F);
        World var16 = var1.getWorld();
        double var19 = var1.getX();
        double var22 = var1.getY();
        double var26 = var1.getZ();
        Location var14 = new Location(var16, var19, var22, var26, 0.0F, 0.0F);
        TeleportCause var15 = TeleportCause.PLUGIN;
        TeleportFlag[] var18 = new TeleportFlag[2];
        Relative var24 = Relative.YAW;
        var18[0] = var24;
        Relative var25 = Relative.PITCH;
        var18[1] = var25;
        var0.teleport(var14, var15, var18);
    }

    private void trialLines(World var1, Location var2, Particle var3, int var4, int var5) {
        double var59 = (double)(var4 % 12);
        double var26 = (Math.PI * 2) * var59 / 12.0;
        double var39 = var2.getX();
        double var67 = Math.cos(var26) * 2.6;
        double var28 = var39 + var67;
        double var44 = var2.getZ();
        double var73 = Math.sin(var26) * 2.6;
        double var30 = var44 + var73;

        for (double var32 = 0.1; var32 <= 0.6; var32 += 0.25) {
            double var86 = var2.getY();
            double var88 = var86 + var32;
            Location var77 = new Location(var1, var28, var88, var30);
            Particles.spawn(var1, var3, var77, 1, 0.02, 0.02, 0.02, 0.0, null, true, 1748031581);
        }
    }

    private void swarm(World var1, Location var2, int var3, int var4) {
        ThreadLocalRandom var22 = ThreadLocalRandom.current();
        double var12 = (double)var3 / 24.0;
        double var107 = 1.0 - var12;
        double var25 = 4.0 * var107 + 0.6;
        Location var27 = var2.clone().add(0.0, 1.1, 0.0);

        for (int var28 = 0; var28 < 14; var28++) {
            double var29 = var22.nextDouble() * Math.PI * 2.0;
            double var116 = var22.nextDouble();
            double var31 = Math.acos(2.0 * var116 - 1.0);
            double var122 = Math.sin(var31);
            double var74 = var25 * var122;
            double var126 = Math.cos(var29);
            double var33 = var74 * var126;
            double var130 = Math.cos(var31);
            double var35 = var25 * var130;
            double var134 = Math.sin(var31);
            double var84 = var25 * var134;
            double var138 = Math.sin(var29);
            double var37 = var84 * var138;
            Location var89 = var27.clone();
            Location var39 = var89.add(var33, var35, var37);
            Vector var92 = var27.toVector();
            Vector var101 = var39.toVector();
            Vector var40 = var92.subtract(var101).normalize().multiply(0.4);
            Particle var104 = SPARK;
            double var145 = var40.getX();
            double var149 = var40.getY();
            double var151 = var40.getZ();
            Particles.spawn(var1, var104, var39, 0, var145, var149, var151, 1.0, null, true, 1748031581);
        }
    }

    private void drawWings(World var1, Player var2, double var3, int var5) {
        Location var18 = var2.getLocation();
        Vector var19 = var18.getDirection();
        double var68 = var19.getX();
        double var96 = var19.getZ();
        Vector var31 = new Vector(var68, 0.0, var96);
        Vector var20 = var31;
        if (var31.lengthSquared() < 1.0E-6) {
            Vector var36 = new Vector(0, 0, 1);
            var20 = var36;
        }

        var20.normalize();
        Vector var21 = var20.clone().multiply(-1);
        Vector var42 = new Vector(0, 1, 0);
        Vector var22 = var42;
        double var77 = -var20.getZ();
        double var99 = var20.getX();
        Vector var43 = new Vector(var77, 0.0, var99);
        Vector var23 = var43;
        Location var46 = var18.clone().add(0.0, 1.2, 0.0);
        Vector var65 = var21.clone().multiply(0.3);
        Location var24 = var46.add(var65);
        double var25 = Math.sin((double)var5 * 0.3) * 0.6;

        for (byte var27 = -1; var27 <= 1; var27 += 2) {
            this.drawWing(var1, var24, var23, var22, var21, var27, var3, var25, 2023187498);
        }
    }

    private void drawWing(World var1, Location var2, Vector var3, Vector var4, Vector var5, int var6, double var7, double var9, int var11) {
        int var29 = (int)Math.ceil(20.0 * var7);
        Location var30 = null;
        Location var31 = null;

        for (int var32 = 0; var32 <= var29 && var32 <= 20; var32++) {
            double var286 = (double)var32 / 20.0;
            double var184 = Math.sin(var286 * Math.PI * 0.5);
            double var288 = 5.4 * var184;
            double var188 = Math.pow(var286, 0.7);
            double var88 = 2.8 * var188;
            double var192 = var9 * var286;
            double var38 = var88 + var192;
            double var196 = Math.pow(var286, 1.4);
            double var40 = 1.4 * var196;
            Vector var97 = var3.clone();
            double var139 = (double)var6;
            double var141 = var139 * var288;
            Vector var98 = var97.multiply(var141);
            Vector var144 = var4.clone();
            Vector var145 = var144.multiply(var38);
            Vector var99 = var98.add(var145);
            Vector var147 = var5.clone();
            Vector var148 = var147.multiply(var40);
            Vector var42 = var99.add(var148);
            Location var102 = var2.clone();
            Location var43 = var102.add(var42);
            this.spark(var1, var43, 1249444507);
            if (var30 != null) {
                this.line(var1, var30, var43, 289221028);
            }

            var30 = var43;
            var31 = var43;
            double var279 = Math.sin(var286 * Math.PI * 0.85);
            double var253 = 0.65 * var279;
            double var206 = 0.35 + var253;
            double var44 = 2.8 * var206;
            if (var44 > 0.1) {
                Vector var118 = var5.clone().multiply(0.75);
                Vector var156 = var4.clone().multiply(-0.5);
                Vector var119 = var118.add(var156);
                Vector var158 = var3.clone();
                double var215 = (double)var6 * 0.12;
                Vector var159 = var158.multiply(var215);
                Vector var46 = var119.add(var159).normalize();
                Location var227 = var43.clone();
                Vector var258 = var46.multiply(var44);
                Location var228 = var227.add(var258);
                this.line(var1, var43, var228, 289221028);
            }
        }

        if (var31 != null && var29 >= 20) {
            for (byte var285 = 0; var285 < 6; var285 += 1) {
                double var33 = (double)var285 / 5.0;
                Vector var67 = var3.clone();
                double var127 = (double)var6;
                double var259 = 1.0 * var33;
                double var218 = 0.65 - var259;
                double var129 = var127 * var218;
                Vector var68 = var67.multiply(var129);
                Vector var132 = var5.clone();
                double var229 = 0.85 * var33;
                double var165 = 0.35 + var229;
                Vector var133 = var132.multiply(var165);
                Vector var69 = var68.add(var133);
                Vector var135 = var4.clone();
                double var233 = 0.95 * var33;
                double var169 = 0.45 - var233;
                Vector var136 = var135.multiply(var169);
                Vector var35 = var69.add(var136).normalize();
                double var271 = Math.sin(var33 * Math.PI);
                double var237 = 0.35 * var271;
                double var173 = 0.65 + var237;
                double var36 = 3.2 * var173;
                Location var221 = var31.clone();
                Vector var240 = var35.multiply(var36);
                Location var222 = var221.add(var240);
                this.line(var1, var31, var222, 289221028);
            }
        }
    }

    private void stopStorm(World var1, int var2) {
        BukkitScheduler var10 = this.plugin.getServer().getScheduler();
        BlissPlugin var12 = this.plugin;
        Runnable var13 = () -> {
            var1.setThundering(false);
            var1.setStorm(false);
        };
        var10.runTaskLater(var12, var13, 60L);
    }

    private void smallBurst(World var1, Location var2, int var3) {
        Location var24 = var2.clone().add(0.0, 1.0, 0.0);
        flash(var1, var24, 1, 0.0, 0.0, 0.0, 605603475);
        Particle var31 = Particle.FIREWORK;
        Particles.spawn(var1, var31, var24, 14, 0.2, 0.3, 0.2, 0.05, null, true, 1748031581);
        Sound var34 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
        var1.playSound(var2, var34, 5.0F, 1.3F);
    }

    private void bigBurst(final World var1, final Location var2, int var3) {
        final Location var13 = var2.clone().add(0.0, 2.0, 0.0);
        Sound var7 = Sound.ITEM_TOTEM_USE;
        var1.playSound(var2, var7, 8.0F, 0.8F);
        Sound var26 = Sound.BLOCK_BEACON_POWER_SELECT;
        var1.playSound(var2, var26, 10.0F, 0.7F);
        Sound var27 = Sound.ENTITY_ILLUSIONER_CAST_SPELL;
        var1.playSound(var2, var27, 8.0F, 0.6F);
        BukkitRunnable var19 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                if (this.t >= 60) {
                    this.cancel();
                } else {
                    if (this.t % 4 == 0) {
                        World var28 = var1;
                        Location var49 = var13;
                        HonoredSummonRitual.flash(var28, var49, 2, 5.0, 5.0, 5.0, 605603475);
                    }

                    World var30 = var1;
                    Particle var50 = Particle.FIREWORK;
                    Location var59 = var13;
                    Particles.spawn(var30, var50, var59, 120, 8.0, 6.0, 8.0, 0.5, null, true, 1748031581);

                    for (double var20 = 0.0; var20 <= 10.0; var20 += 0.5) {
                        HonoredSummonRitual var37 = HonoredSummonRitual.this;
                        World var52 = var1;
                        Location var64 = var2.clone();
                        Location var65 = var64.add(0.0, var20, 0.0);
                        var37.spark(var52, var65, 1249444507);
                    }

                    if (this.t == 30) {
                        World var45 = var1;
                        Location var55 = var2;
                        Sound var68 = Sound.ENTITY_GENERIC_EXPLODE;
                        var45.playSound(var55, var68, 8.0F, 0.6F);
                    }

                    int var57 = this.t + 1;
                    this.t = var57;
                }
            }
        };
        BlissPlugin var25 = this.plugin;
        var19.runTaskTimer(var25, 0L, 1L);
    }

    private void flashBurst(final World var1, final Location var2, int var3) {
        final Location var13 = var2.clone().add(0.0, 2.0, 0.0);
        Sound var7 = Sound.ITEM_TOTEM_USE;
        var1.playSound(var2, var7, 8.0F, 0.8F);
        Sound var26 = Sound.BLOCK_BEACON_POWER_SELECT;
        var1.playSound(var2, var26, 10.0F, 0.7F);
        Sound var27 = Sound.ENTITY_ILLUSIONER_CAST_SPELL;
        var1.playSound(var2, var27, 8.0F, 0.6F);
        BukkitRunnable var19 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                if (this.t >= 60) {
                    this.cancel();
                } else {
                    if (this.t % 4 == 0) {
                        World var17 = var1;
                        Location var25 = var13;
                        HonoredSummonRitual.flash(var17, var25, 2, 5.0, 5.0, 5.0, 605603475);
                    }

                    if (this.t == 30) {
                        World var21 = var1;
                        Location var28 = var2;
                        Sound var31 = Sound.ENTITY_GENERIC_EXPLODE;
                        var21.playSound(var28, var31, 8.0F, 0.6F);
                    }

                    int var30 = this.t + 1;
                    this.t = var30;
                }
            }
        };
        BlissPlugin var25 = this.plugin;
        var19.runTaskTimer(var25, 0L, 1L);
    }

    public void burstOfLightAround(Location var1, double var2, int var4) {
        if (var1 != null && var1.getWorld() != null) {
            World var9 = var1.getWorld();
            double var10 = var2 * var2;

            for (Player var13 : var9.getPlayers()) {
                Location var31 = var13.getLocation();
                double var32 = var31.distanceSquared(var1);
                if (var32 <= var10) {
                    Location var40 = var13.getLocation();
                    this.flashBurst(var9, var40, 1714636363);
                }
            }
        }
    }

    private void fluxBurst(World var1, Location var2, double var3, int var5) {
        this.fluxBurst(var1, var2, var3, 1.0, 776379147);
    }

    private void fluxBurst(final World var1, final Location var2, final double var3, final double var5, int var7) {
        ThreadLocalRandom var32 = ThreadLocalRandom.current();
        final double var33 = var32.nextDouble(Math.PI);
        final double var35 = var32.nextDouble(Math.PI * 2);
        final double var37 = var32.nextDouble(Math.PI);
        final double var39 = var32.nextDouble(Math.PI * 2);
        final double var41 = var32.nextDouble(Math.PI);
        final double var43 = var32.nextDouble(Math.PI * 2);
        BukkitRunnable var63 = new BukkitRunnable() {
            double s = 0.4;
            double vel;

            {
                double var61 = var5;
                double var59 = 0.2 / var61;
                this.vel = var59;
            }

            public void run() {
                double var7 = this.s;
                double var32 = var3;
                if (var7 > var32) {
                    this.cancel();
                } else {
                    HonoredSummonRitual var12 = HonoredSummonRitual.this;
                    World var19 = var1;
                    Location var35x = var2;
                    double var40 = this.s;
                    double var55 = var33;
                    double var66 = var35;
                    var12.drawOrbitRing(var19, var35x, var40, var55, var66, 2010922371);
                    HonoredSummonRitual var14 = HonoredSummonRitual.this;
                    World var21 = var1;
                    Location var37x = var2;
                    double var43x = this.s;
                    double var58 = var37;
                    double var69 = var39;
                    var14.drawOrbitRing(var21, var37x, var43x, var58, var69, 2010922371);
                    HonoredSummonRitual var16 = HonoredSummonRitual.this;
                    World var23 = var1;
                    Location var39x = var2;
                    double var46 = this.s;
                    double var61 = var41;
                    double var72 = var43;
                    var16.drawOrbitRing(var23, var39x, var46, var61, var72, 2010922371);
                    double var24 = this.vel;
                    double var64 = var5;
                    double var50 = 0.12 / var64;
                    double var26 = var24 + var50;
                    this.vel = var26;
                    double var28 = this.s;
                    double var53 = this.vel;
                    double var30 = var28 + var53;
                    this.s = var30;
                }
            }
        };
        BlissPlugin var76 = this.plugin;
        var63.runTaskTimer(var76, 0L, 1L);
    }

    private void drawOrbitRing(World var1, Location var2, double var3, double var5, double var7, int var9) {
        this.drawOrbitRing(var1, var2, var3, var5, var7, null, 0.0, 174262938);
    }

    private void drawOrbitRing(World var1, Location var2, double var3, double var5, double var7, Vector var9, double var10, int var12) {
        int var153 = (int)Math.floor((Math.PI * 2) * var3 / 0.2);
        int var142 = Math.max(12, var153);
        int var32 = Math.min(220, var142);
        double var33 = Math.cos(var5);
        double var35 = Math.sin(var5);
        double var37 = Math.cos(var7);
        double var39 = Math.sin(var7);
        Color var41 = Color.fromRGB(255, 196, 64);
        Color var42 = Color.fromRGB(255, 255, 255);

        for (int var43 = 0; var43 < var32; var43++) {
            double var157 = (double)var43;
            double var85 = (Math.PI * 2) * var157;
            double var160 = (double)var32;
            double var44 = var85 / var160;
            double var91 = Math.cos(var44);
            double var46 = var91 * var3;
            double var97 = Math.sin(var44);
            double var48 = var97 * var3;
            double var103 = -var48;
            double var50 = var103 * var35;
            double var52 = var48 * var33;
            double var113 = var46 * var37;
            double var174 = var52 * var39;
            double var54 = var113 - var174;
            double var119 = var46 * var39;
            double var180 = var52 * var37;
            double var56 = var119 + var180;
            if (var9 != null) {
                double var58 = Math.hypot(var54, var56);
                if (var58 > 1.0E-6) {
                    double var187 = var9.getX();
                    double var133 = var54 * var187;
                    double var214 = var9.getZ();
                    double var191 = var56 * var214;
                    double var135 = var133 + var191;
                    double var137 = var135 / var58;
                    if (var137 >= var10) {
                        continue;
                    }
                }
            }

            Color var202;
            if (var43 % 2 == 0) {
                var202 = var41;
            } else {
                var202 = var42;
            }

            DustOptions var140 = new DustOptions(var202, 1.0F);
            Particle var146 = Particle.DUST;
            Location var200 = var2.clone();
            Location var201 = var200.add(var54, var50, var56);
            Particles.spawn(var1, var146, var201, 1, 0.0, 0.0, 0.0, 0.0, var140, true, 1748031581);
        }
    }

    private void drawShape(World var1, Location var2, int var3) {
        for (double var18 = -0.09; var18 <= 0.090000001; var18 += 0.09) {
            double var136 = 2.6 + var18;
            int var23 = (int)Math.round((Math.PI * 2) * var136 / 0.18);

            for (int var24 = 0; var24 < var23; var24++) {
                double var95 = (double)var24;
                double var63 = (Math.PI * 2) * var95;
                double var98 = (double)var23;
                double var25 = var63 / var98;
                Location var101 = var2.clone();
                double var117 = Math.cos(var25);
                double var119 = var117 * var136;
                double var131 = Math.sin(var25);
                double var133 = var131 * var136;
                Location var102 = var101.add(var119, 0.0, var133);
                this.spark(var1, var102, 1249444507);
            }
        }

        Location var135 = var2.clone().add(-0.9880000000000001, 0.0, 1.612);
        Location var20 = var2.clone().add(0.9880000000000001, 0.0, 1.612);
        Location var21 = var2.clone().add(-0.9880000000000001, 0.0, -1.612);
        Location var22 = var2.clone().add(0.9880000000000001, 0.0, -1.612);
        this.thickLine(var1, var135, var20, 1429081521);
        this.thickLine(var1, var21, var22, 1429081521);
        this.thickLine(var1, var135, var22, 1429081521);
        this.thickLine(var1, var20, var21, 1429081521);
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
            this.line(var1, var58, var69, 289221028);
        }
    }

    private void ambient(World var1, Location var2, int var3) {
        ThreadLocalRandom var21 = ThreadLocalRandom.current();

        for (int var22 = 0; var22 < 2; var22++) {
            Location var23 = this.randomAround(var2, var21, 0.2, 1.6, 1664436374);
            Location var24 = this.randomAround(var2, var21, 3.0, 6.0, 1664436374);
            Particle var37 = SPARK;
            Particles.spawn(var1, var37, var23, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
            Particle var38 = Particle.ELECTRIC_SPARK;
            Particles.spawn(var1, var38, var23, 1, 0.05, 0.05, 0.05, 0.0, null, true, 1748031581);
            Particle var39 = SPARK;
            Particles.spawn(var1, var39, var24, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
            Particle var40 = Particle.ELECTRIC_SPARK;
            Particles.spawn(var1, var40, var24, 1, 0.05, 0.05, 0.05, 0.0, null, true, 1748031581);
        }
    }

    private Location randomAround(Location var1, ThreadLocalRandom var2, double var3, double var5, int var7) {
        double var22 = var2.nextDouble() * Math.PI * 2.0;
        double var64 = var2.nextDouble() * 0.9;
        double var53 = 0.4 + var64;
        double var24 = 2.6 * var53;
        double var56 = var2.nextDouble();
        double var68 = var5 - var3;
        double var58 = var56 * var68;
        double var26 = var3 + var58;
        Location var43 = var1.clone();
        double var45 = Math.cos(var22);
        double var47 = var45 * var24;
        double var70 = Math.sin(var22);
        double var72 = var70 * var24;
        return var43.add(var47, var26, var72);
    }

    private void swirlArms(World var1, Location var2, double var3, double var5, int var7) {
        double var31 = (var5 - 2.6) / 25.0;

        for (int var35 = 0; var35 < 4; var35++) {
            double var79 = (double)var35 * (Math.PI / 2);
            double var36 = var3 + var79;
            double var38 = Math.cos(var36);
            double var40 = Math.sin(var36);
            Location var66 = var2.clone();
            double var71 = var38 * 2.6;
            double var89 = var40 * 2.6;
            Location var42 = var66.add(var71, 0.6, var89);
            Particle var73 = Particle.SCULK_SOUL;
            double var87 = var38 * var31;
            double var93 = var40 * var31;
            Particles.spawn(var1, var73, var42, 0, var87, 0.013999999999999999, var93, 1.0, null, true, 1748031581);
        }
    }

    private void floatStreaks(World var1, Location var2, int var3) {
        ThreadLocalRandom var16 = ThreadLocalRandom.current();
        Color var71 = Color.fromRGB(255, 255, 255);
        DustOptions var29 = new DustOptions(var71, 1.0F);
        DustOptions var17 = var29;
        Color var73 = Color.fromRGB(255, 196, 64);
        DustOptions var30 = new DustOptions(var73, 1.0F);
        DustOptions var18 = var30;

        for (int var19 = 0; var19 < 2; var19++) {
            double var20 = var16.nextDouble() * Math.PI * 2.0;
            double var81 = var16.nextDouble() * 4.0;
            double var22 = 1.0 + var81;
            double var59;
            if (var16.nextBoolean()) {
                var59 = var16.nextDouble() * 1.5;
            } else {
                double var94 = var16.nextDouble() * 4.0;
                var59 = 3.0 + var94;
            }

            Location var51 = var2.clone();
            double var64 = Math.cos(var20);
            double var66 = var64 * var22;
            double var121 = Math.sin(var20);
            double var123 = var121 * var22;
            Location var27 = var51.add(var66, var59, var123);
            double var88 = var16.nextDouble() - 0.5;
            double var114 = (var16.nextDouble() - 0.5) * 0.6;
            double var130 = var16.nextDouble() - 0.5;
            Vector var53 = new Vector(var88, var114, var130);
            Vector var28 = var53.normalize().multiply(0.25);
            DustOptions var120;
            if (var16.nextBoolean()) {
                var120 = var17;
            } else {
                var120 = var18;
            }

            this.animateStreak(var1, var27, var28, var120, 1081980901);
        }
    }

    private void risingSwirl(World var1, Location var2, double var3, int var5) {
        Color var25 = Color.fromRGB(255, 196, 64);
        Color var26 = Color.fromRGB(255, 255, 255);

        for (int var28 = 0; var28 < 4; var28++) {
            double var85 = (double)var28 * (Math.PI / 2);
            double var29 = var3 + var85;

            for (double var31 = 0.25; var31 <= 1.6; var31 += 0.22) {
                double var91 = var31 * 1.8;
                double var33 = var29 - var91;
                double var95 = var31 * 0.45;
                double var35 = -0.6 + var95;
                Color var70;
                if ((int)Math.round(var31 * 6.0) % 2 == 0) {
                    var70 = var25;
                } else {
                    var70 = var26;
                }

                DustOptions var71 = new DustOptions(var70, 0.8F);
                Particle var80 = Particle.DUST;
                Location var101 = var2.clone();
                double var107 = Math.cos(var33);
                double var109 = var107 * var31;
                double var126 = Math.sin(var33);
                double var128 = var126 * var31;
                Location var102 = var101.add(var109, var35, var128);
                Particles.spawn(var1, var80, var102, 1, 0.0, 0.0, 0.0, 0.0, var71, true, 1748031581);
            }
        }
    }

    private void randomLines(World var1, Location var2, int var3) {
        ThreadLocalRandom var16 = ThreadLocalRandom.current();
        Color var76 = Color.fromRGB(255, 255, 255);
        DustOptions var30 = new DustOptions(var76, 1.0F);
        DustOptions var17 = var30;
        Color var78 = Color.fromRGB(255, 196, 64);
        DustOptions var31 = new DustOptions(var78, 1.0F);
        DustOptions var18 = var31;
        int var65 = var16.nextInt(5);
        int var19 = 10 + var65;

        for (int var20 = 0; var20 < var19; var20++) {
            double var21 = var16.nextDouble() * Math.PI * 2.0;
            double var87 = var16.nextDouble() * 4.0;
            double var23 = 1.0 + var87;
            double var62;
            if (var16.nextBoolean()) {
                var62 = var16.nextDouble() * 1.5;
            } else {
                double var92 = var16.nextDouble() * 4.0;
                var62 = 3.0 + var92;
            }

            Location var53 = var2.clone();
            double var69 = Math.cos(var21);
            double var71 = var69 * var23;
            double var127 = Math.sin(var21);
            double var129 = var127 * var23;
            Location var28 = var53.add(var71, var62, var129);
            double var97 = var16.nextDouble() - 0.5;
            double var122 = (var16.nextDouble() - 0.5) * 0.6;
            double var136 = var16.nextDouble() - 0.5;
            Vector var55 = new Vector(var97, var122, var136);
            Vector var29 = var55.normalize().multiply(0.25);
            DustOptions var126;
            if (var16.nextBoolean()) {
                var126 = var17;
            } else {
                var126 = var18;
            }

            this.animateStreak(var1, var28, var29, var126, 1081980901);
        }
    }

    private void animateStreak(final World var1, final Location var2, final Vector var3, final DustOptions var4, int var5) {
        BukkitRunnable var6 = new BukkitRunnable() {
            final Location pos;
            int age;

            {
                Location var20 = var2.clone();
                this.pos = var20;
                this.age = 0;
            }

            public void run() {
                for (int var16 = 0; var16 < 2; var16++) {
                    Location var19 = this.pos;
                    Vector var28 = var3.clone().multiply(0.5);
                    var19.add(var28);
                    World var22 = var1;
                    Particle var29 = Particle.DUST;
                    Location var34 = this.pos;
                    DustOptions var36 = var4;
                    Particles.spawn(var22, var29, var34, 1, 0.0, 0.0, 0.0, 0.0, var36, true, 1748031581);
                }

                int var31 = this.age + 1;
                this.age = var31;
                if (var31 > 70) {
                    this.cancel();
                }
            }
        };
        BlissPlugin var14 = this.plugin;
        var6.runTaskTimer(var14, 0L, 1L);
    }

    private boolean inCentre(Player var1, Location var2, int var3) {
        Location var9 = var1.getLocation();
        World var16 = var9.getWorld();
        World var37 = var2.getWorld();
        if (!var16.equals(var37)) {
            return false;
        } else {
            double var20 = var9.getX();
            double var38 = var2.getX();
            double var10 = var20 - var38;
            double var25 = var9.getZ();
            double var41 = var2.getZ();
            double var12 = var25 - var41;
            double var31 = var10 * var10;
            double var47 = var12 * var12;
            boolean var36;
            if (var31 + var47 <= 2.5600000000000005) {
                var36 = true;
            } else {
                var36 = false;
            }

            return var36;
        }
    }

    private void spawnBolts(List<HonoredSummonRitual.Bolt> var1, Location var2, int var3) {
        ThreadLocalRandom var17 = ThreadLocalRandom.current();

        for (int var19 = 0; var19 < 1; var19++) {
            Location var32 = var2.clone();
            double var44 = (var17.nextDouble() - 0.5) * 7.0;
            double var63 = var17.nextDouble() * 7.0;
            double var56 = 11.0 + var63;
            double var70 = (var17.nextDouble() - 0.5) * 7.0;
            Location var20 = var32.add(var44, var56, var70);
            ArrayList var34 = new ArrayList();
            double var75 = var17.nextDouble() - 0.5;
            double var88 = var17.nextDouble() - 0.5;
            Vector var58 = new Vector(var75, -1.0, var88);
            this.buildBolt(var34, var20, var58, 5, 2, var17, 742452294);
            HonoredSummonRitual.Bolt var47 = new HonoredSummonRitual.Bolt(var34, 1332238080);
            var1.add(var47);
        }

        if (var17.nextDouble() < 0.6) {
            World var30 = var2.getWorld();
            Sound var48 = Sound.ENTITY_LIGHTNING_BOLT_THUNDER;
            var30.playSound(var2, var48, 0.3F, 1.2F);
        }
    }

    private void updateBolts(World var1, List<HonoredSummonRitual.Bolt> var2, int var3) {
        Iterator var17 = var2.iterator();

        while (var17.hasNext()) {
            HonoredSummonRitual.Bolt var18 = (HonoredSummonRitual.Bolt)var17.next();
            int var50 = var18.age + 1;
            var18.age = var50;
            int var19 = var18.pts.size();
            int var48;
            if (var18.age >= 5) {
                var48 = var19;
            } else {
                double var35 = (double)var19;
                double var60 = (double)var18.age / 5.0;
                var48 = (int)Math.round(var35 * var60);
            }

            int var20 = var48;

            for (int var21 = 0; var21 < var20 && var21 < var19; var21++) {
                Particle var54 = Particle.ELECTRIC_SPARK;
                List var63 = var18.pts;
                Location var65 = (Location)var63.get(var21);
                Particles.spawn(var1, var54, var65, 4, 0.0504, 0.0504, 0.0504, 0.0, null, true, 1748031581);
            }

            if (var18.age > 19) {
                var17.remove();
            }
        }
    }

    private void buildBolt(List<Location> var1, Location var2, Vector var3, int var4, int var5, ThreadLocalRandom var6, int var7) {
        Vector var20 = var3.clone().normalize();
        Location var21 = var2.clone();

        for (int var22 = 0; var22 < var4; var22++) {
            double var69 = var6.nextDouble() - 0.5;
            double var87 = var6.nextDouble() - 0.5;
            double var106 = var6.nextDouble() - 0.5;
            Vector var49 = new Vector(var69, var87, var106);
            Vector var50 = var49.multiply(0.6);
            var20.add(var50).normalize();
            Location var35 = var21.clone();
            Vector var52 = var20.clone();
            double var80 = var6.nextDouble() * 0.5;
            double var61 = 0.5 + var80;
            Vector var53 = var52.multiply(var61);
            Location var23 = var35.add(var53);
            this.addLinePoints(var1, var21, var23, 90175388);
            var21 = var23;
            if (var5 > 0 && var6.nextDouble() < 0.45) {
                Vector var45 = var20.clone();
                double var75 = var6.nextDouble() - 0.5;
                double var94 = var6.nextDouble() - 0.5;
                double var113 = var6.nextDouble() - 0.5;
                Vector var55 = new Vector(var75, var94, var113);
                Vector var24 = var45.add(var55).normalize();
                int var97 = var4 / 2;
                int var83 = Math.max(2, var97);
                int var99 = var5 - 1;
                this.buildBolt(var1, var23, var24, var83, var99, var6, 742452294);
            }
        }
    }

    private void addLinePoints(List<Location> var1, Location var2, Location var3, int var4) {
        Vector var16 = var3.toVector();
        Vector var33 = var2.toVector();
        Vector var9 = var16.subtract(var33);
        int var40 = (int)Math.round(var9.length() / 0.18);
        int var12 = Math.max(1, var40);
        double var50 = (double)var12;
        double var43 = 1.0 / var50;
        Vector var13 = var9.multiply(var43);
        Location var14 = var2.clone();

        for (int var15 = 0; var15 <= var12; var15++) {
            Location var47 = var14.clone();
            var1.add(var47);
            var14.add(var13);
        }
    }

    private void line(World var1, Location var2, Location var3, int var4) {
        Particle var9 = SPARK;
        this.line(var1, var2, var3, var9, 758166227);
    }

    private void line(World var1, Location var2, Location var3, Particle var4, int var5) {
        Vector var18 = var3.toVector();
        Vector var34 = var2.toVector();
        Vector var11 = var18.subtract(var34);
        int var41 = (int)Math.round(var11.length() / 0.18);
        int var14 = Math.max(1, var41);
        double var50 = (double)var14;
        double var44 = 1.0 / var50;
        Vector var15 = var11.multiply(var44);
        Location var16 = var2.clone();

        for (int var17 = 0; var17 <= var14; var17++) {
            this.spark(var1, var16, var4, 2039888462);
            var16.add(var15);
        }
    }

    private void spark(World var1, Location var2, int var3) {
        Particle var7 = SPARK;
        this.spark(var1, var2, var7, 2039888462);
    }

    private void spark(World var1, Location var2, Particle var3, int var4) {
        Particles.spawn(var1, var3, var2, 0, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
    }

    static {
        boolean var4;
        if (Particle.FLASH.getDataType() == Color.class) {
            var4 = true;
        } else {
            var4 = false;
        }

        FLASH_NEEDS_COLOR = var4;
        SWIRL_SPEED = Math.toRadians(6.0);
    }

    private static final class Bolt {
        final List<Location> pts;
        int age;

        Bolt(List<Location> var1, int var2) {
            this.pts = var1;
        }
    }
}
