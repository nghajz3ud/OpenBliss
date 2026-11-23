package com.bliss.goldgem;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.core.GemType;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.SignatureClock;
import com.bliss.items.BlissCoreItems;
import com.bliss.trader.TraderListener;
import io.papermc.paper.entity.LookAnchor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.Trail;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class FragmentCoreRitual implements Listener {
    private static final long PERIOD = 2L;
    private static final int FRAGMENTS = 7;
    private static final float FRAG_SCALE = 0.35F;
    private static final float GHOST_SCALE = 0.85F;
    private static final double HALO_RADIUS = 0.72;
    private static final double HALO_HEIGHT = 0.6;
    private static final double CIRCLE_RADIUS = 0.9;
    private static final double CIRCLE_HEIGHT = 1.9;
    private static final double PUSH_BLOCKS = 2.0;
    private static final double TAU = Math.PI * 2;
    private static final NamespacedKey KEY_DISPLAY = NamespacedKey.fromString("bliss:fragcore_display");
    private static final Color GOLD = Color.fromRGB(255, 176, 42);
    private static final DustOptions GOLD_DUST;
    private static final DustOptions GOLD_DUST_SMALL;
    private static final DustOptions SHAPE_DUST;
    private static final boolean FLASH_NEEDS_COLOR;
    private static final double BUILD_BURST_BOOST = 1.3;
    private static final double CORE_BURST_REACH = 2.6;
    private static final int ERUPT_START = 19;
    private static final int ERUPT_DUR = 46;
    private static final int ARMS_START = 46;
    private static final int ARMS_DUR = 194;
    private static final int ARMS_END = 240;
    private static final int ORBIT_DUR = 180;
    private static final int[] CORE_BEATS;
    private static final int[] ABSORB_TETHERS;
    private static final int ABSORB_TAIL_HOLD = 41;
    private static final int ABSORB_YANK_TICKS = 3;
    private static final int ABSORB_PREROLL = 53;
    static final int COVER_HOLD = 20;
    static final int COVER_TICKS = 32;
    static final String[] RITUAL_SOUNDS;
    static final int SFX_CORE = 0;
    static final int SFX_BURSTS = 1;
    static final int SFX_ORBIT = 2;
    static final int SFX_HANDOFF = 3;
    static final int SFX_WIRES = 4;
    static final int SFX_CLIMAX = 5;
    static final int SFX_AFTERMATH = 6;
    static final double RITUAL_SOUND_RANGE = 80.0;
    private static final int GUST_BURST_A = 22;
    private static final int GUST_BURST_B = 27;
    private static final int GUST_POINTS = 20;
    private static final int PUFF_SUB = 2;
    private static final double PUFF_LAYER = 0.06;
    private static final Particle TETHER_PARTICLE;
    private static final int BEAT_BURSTS_MIN = 2;
    private static final int BEAT_BURSTS_MAX = 3;
    private static final int BEAT_FLASH_COUNT = 6;
    private static final int BEAT_PULSE_PEAK = 7;
    private static final int BEAT_PULSE_HOLD = 1;
    private static final int BEAT_PULSE_TICKS = 7;
    private static final int LIGHTNING_G0 = 9;
    private static final int LIGHTNING_TICKS = 28;
    private static final int LIGHTNING_EVERY = 2;
    private static final int LIGHTNING_PER_FIRE = 3;
    private static final double LIGHTNING_LEN = 6.5;
    private static final int LIGHTNING_SEGMENTS = 7;
    private static final double LIGHTNING_JITTER = 0.8;
    private static final double LIGHTNING_DIP = 0.8;
    private static final double LIGHTNING_SPREAD = 3.0;
    private static final double LIGHTNING_Y_VARY = 1.6;
    private static final int ARM_COUNT = 4;
    private static final double ARM_REACH = 4.42;
    private static final int ARM_DOTS = 5;
    private static final int ARM_DOT_TICKS = 3;
    private static final double ARM_HUB_GAP = 0.9;
    private static final double ARM_CURL = 0.85;
    private static final double ORBIT_R = 5.2;
    private static final double BURST_K = 0.11;
    private static final double BURST_CIRCLE_R = 5.0;
    private static final double BURST_STAR_R = 4.0;
    private static final int BURST_CIRCLE_PTS = 60;
    static final int BURST_PTS = 30;
    static final double BURST_SPEED = 0.55;
    private static final int CLIMAX_BURST_PTS = 48;
    private static final double CLIMAX_BURST_SPEED = 1.8;
    private static final double SOUL_SPEED_RATIO = 0.7;
    private static final double TILT_ANY = -1.0;
    private static final double TILT_FLAT = 0.0;
    static final double TILT_AFTERMATH = 45.0;
    private static final Particle[] AFTERMATH_MIX;
    private static final long AFTERMATH_SWITCH_AT = 8L;
    private static final double AFTERMATH_SLOW = 1.5;
    private static final int DRAW_START_DELAY;
    private static final int DRAW_FLIGHT_TICKS;
    private static final double DRAW_ARC_LIFT = 3.0;
    private static final int DRAW_BURST_AT;
    private static final int DRAW_COVER_AT;
    private static final int DRAW_BROKEN_AT;
    private static final int DRAW_COVER2_AT;
    private static final int DRAW_LASTBURST_AT;
    private static final int DRAW_SHARDS_AT;
    static final int SHARD_FALL_TICKS = 90;
    static final double SHARD_FALL_SPEED = 0.11;
    private static final double SHARD_SPREAD = 4.0;
    private static final int SHARD_TRAIL_REFRESH = 10;
    private static final int GEM_POINT_TICKS = 18;
    private static final int GEM_BURST_MIN_TICKS = 20;
    private static final int GEM_BURST_MAX_TICKS = 28;
    private static final double GEM_BURST_FAST = 0.26;
    private static final double GEM_BURST_SLOW = 0.13;
    private static final double GEM_BURST_LIFT = 0.3;
    private static final double GEM_BURST_DARK = 0.55;
    private static final int DRAW_COVER3_AT;
    private static final int DRAW_HANDOFF_AT;
    private static final int GOLD_DESCENT_TICKS;
    private static final int DRAW_END_AT;
    private static final int DRAW_END_FADE = 26;
    private static final double DRAW_HOVER_UP = 2.6;
    static final float DRAW_GEM_SCALE = 1.0F;
    private static final int AFTERMATH_SHOWER_TICKS;
    private static final int LIGHT_PULSE_TICKS = 40;
    private static final int PULSE_ENCORE = 2;
    private static final int[] PULSE_BEATS;
    private static final double LIGHT_PULSE_RADIUS = 12.0;
    static final double BURST_REACH_MAX = 2.2;
    static final int AFTERMATH_BEAT_GAP = 4;
    static final int[] AFTERMATH_BAR_RINGS;
    private static final float[] AFTERMATH_BAR_PITCH;
    private static final int BEAT_QUICKEN_WINDOW = 14;
    private static final double BEAT_QUICKEN = 0.65;
    private static final int AFTERMATH_BURST_AT = 0;
    private static final int AFTERMATH_DRAW_AT;
    private static final int AFTERMATH_TICKS;
    static final float GEM_ART_ROLL;
    private static final float GOLD_SPIN = 0.05F;
    static final float DRAW_SPIN = 0.07F;
    static final float DRAW_SPIN_OFFSET = 2.1F;
    private static final double[][] GEM_SHAPE;
    private static final double GEM_SHAPE_R = 0.8;
    private static final double GEM_SHAPE_DIST = 2.5;
    private static final int GEM_SHAPE_SEG = 8;
    private static final DustOptions GEM_SHAPE_DUST;
    private static final int BURST_STAR_DELAY = 12;
    private static final int FINALE_TAIL = 60;
    private static final int FLOAT_TICKS = 21;
    private static final double SPIN_BASE = 0.06;
    private static final double SPIN_MAX = 0.62;
    static final double CAM_R_MIN = 7.0;
    static final double CAM_R_MAX = 18.2;
    static final double CAM_SPEED_MIN = 0.04;
    static final double CAM_SPEED_MAX = 0.22;
    static final double CAM_BUILD_END_BOOST = 1.4;
    static final float FLASH_SCALE = 4.0F;
    private static final int FLASH_TICKS = 12;
    private static final int FLASH_HOLD = 4;
    static final double FLASH_AHEAD = 0.45;
    private static final String CORE_HOLD_SOUND = "bliss:goldgem_core_hold";
    private static final long COVER_DAY_BASE = 80020L;
    private static final int FLASH_ROWS = 24;
    static final String FLASH_FILL;
    static final float FLASH_LIFT = -12.0F;
    private static final float REVEAL_GEM_SCALE = 1.54F;
    private static final double HOLDER_GOLD_SPEED = 0.62;
    private static final double HOLDER_GOLD_REACH = 4.0;
    private static final int HOLDER_GOLD_TICKS_MIN = 20;
    private static final int HOLDER_GOLD_TICKS_MAX = 32;
    static final Color HOLDER_GOLD;
    private static final double F_REACH = 0.65;
    private static final int F_JETS = 2;
    private static final double F_HUB_GAP = 0.1;
    private static final double F_LAUNCH = 0.055;
    private static final double F_SPIN = 0.22;
    private final BlissPlugin plugin;
    private final Map<UUID, FragmentCoreRitual.State> states;
    private BukkitTask task;
    private double feetAng;
    private double rot;
    private static final int ORBIT_MOTE_EVERY = 4;
    private static final int ORB_GAP_PCT = 40;
    private static final int ORB_O_AT = 60;
    private static final int ORB_ERUPT_AT = 70;
    private static final int ORB_ERUPTIONS = 5;
    private static final int ORB_ERUPT_JETS = 3;
    private static final int ORB_RING1_AT = 146;
    private static final int ORB_RING2_AT = 166;
    private static final int ORB_COVER_AT = 192;
    private final Set<UUID> owedCore;
    private final Set<UUID> gemLifted;
    static final int EGG_COUNT = 3;
    private static final double EGG_OUT = 0.0;
    private static final double EGG_R = 5.2;
    private static final double EGG_MIN_FACTOR = 0.75;
    private static final double EGG_MAX_FACTOR = 1.25;
    private static final double EGG_HEAD_SPEED = 0.03;
    private static final double EGG_PTS_PER_RAD = 20.0;
    private static final int EGG_REST = 20;
    private static final int EGG_STAR_TICKS = 66;
    private static final double SHARD_RESYNC_SQ = 0.12249999999999998;
    private static final int PULSE_SCATTER = 120;
    private static final double PULSE_SCATTER_MIN = 0.55;
    private static final double PULSE_SCATTER_MAX = 2.4;
    private static final int ABSORB_SCATTER = 26;
    private static final double ABSORB_SCATTER_MIN = 0.55;
    private static final double ABSORB_SCATTER_MAX = 2.4;
    private static final float HURT_KICK_DEG = 2.4F;
    private static final int HURT_KICK_TICKS = 4;
    private final Map<UUID, FragmentCoreRitual.Cover> covers;
    static final long SHADER_BASE = 16000L;
    static final long SHADER_DAY = 80000L;
    static final int PHASE_BUILD = 1;
    private static final int PHASE_REVEAL = 2;
    private static final int PHASE_ABSORB = 3;
    private static final int PHASE_CLIMAX = 4;
    private static final int PHASE_AFTERMATH = 6;
    private static final int PHASE_GEM_DRAW = 5;
    private static final int PHASE_ABSORB_PRE = 7;
    static final int PHASE_HARVEST = 0;
    private static final long SHADER_FAILSAFE_TICKS = 3600L;
    private int shaderHolds;
    private World shaderWorld;
    private long shaderPrevFull;
    private boolean shaderPrevCycle;
    private BukkitTask shaderFailsafe;
    private int shaderCurPhase;
    private int shaderFlashStep;
    private int shaderGemId;
    private int shaderFlashK;
    private int shaderFlashPeak;
    private int shaderFlashHold;
    private int shaderFlashTotal;
    private BukkitTask shaderFlashTask;
    private final Set<UUID> coverDimmed;

    private static int coreBeatAt(int var0, int var1) {
        int var4 = 0;

        while (true) {
            int var10 = CORE_BEATS.length;
            if (var4 >= var10) {
                return -1;
            }

            int[] var6 = CORE_BEATS;
            int var7 = var6[var4];
            if (var7 == var0) {
                return var4;
            }

            var4++;
        }
    }

    static int slow(int var0, int var1) {
        return (int)Math.round((double)var0 * 1.5);
    }

    private static boolean isPulseBeat(int var0, int var1) {
        for (int var15 : PULSE_BEATS) {
            if (var15 == var0) {
                return true;
            }
        }

        return false;
    }

    public FragmentCoreRitual(BlissPlugin var1, int var2) {
        HashMap var4 = new HashMap();
        this.states = var4;
        HashSet var14 = new HashSet();
        this.owedCore = var14;
        HashSet var15 = new HashSet();
        this.gemLifted = var15;
        HashMap var16 = new HashMap();
        this.covers = var16;
        this.shaderFlashPeak = 15;
        this.shaderFlashHold = 4;
        this.shaderFlashTotal = 12;
        HashSet var20 = new HashSet();
        this.coverDimmed = var20;
        this.plugin = var1;
    }

    public void start(int var1) {
        this.sweepOrphans(1157704027);
        this.clockRecover(68432366);
        this.owedLoad(522454780);
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var14 = this.plugin;
        Runnable var15 = this::tick;
        BukkitTask var13 = var3.runTaskTimer(var14, var15, 2L, 2L);
        this.task = var13;
    }

    public void stop(int var1) {
        if (this.task != null) {
            this.task.cancel();
        }

        for (World var5 : Bukkit.getWorlds()) {
            this.ritualSoundStop(var5, 425244914);
        }

        for (FragmentCoreRitual.State var34 : this.states.values()) {
            this.clearDisplays(var34, 186841448);
        }

        this.states.clear();
        this.shaderRestore();
        this.coversRestore(95620666);
        this.owedSave(1489154183);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        Map var5 = this.states;
        UUID var11 = var1.getPlayer().getUniqueId();
        FragmentCoreRitual.State var4 = (FragmentCoreRitual.State)var5.remove(var11);
        if (var4 != null) {
            this.clearDisplays(var4, 186841448);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent var1) {
        EquipmentSlot var10 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var10 == var3) {
            switch (var1.getAction()) {
                case RIGHT_CLICK_AIR:
                case RIGHT_CLICK_BLOCK:
                    Player var6 = var1.getPlayer();
                    if (!RitualItems.isFragmentCore(var6.getInventory().getItemInMainHand(), 1590975450)) {
                        return;
                    } else {
                        Map var20 = this.states;
                        UUID var53 = var6.getUniqueId();
                        FragmentCoreRitual.State var7 = (FragmentCoreRitual.State)var20.get(var53);
                        if (var7 != null && var7.holding && !var7.sealed && var7.center != null) {
                            var1.setCancelled(true);
                            var7.sealed = true;
                            var7.holding = false;
                            this.consumeMainHandCore(var6, 1506399487);
                            Set var35 = this.owedCore;
                            UUID var59 = var6.getUniqueId();
                            var35.add(var59);
                            this.owedSave(1489154183);
                            Location var8 = var6.getEyeLocation();
                            ItemStack var4 = RitualItems.fragmentCore(1868423911);
                            ItemDisplay var9 = this.spawnDisplay(var8, var4, 0.85F, 1550711431);
                            var9.setGlowing(true);
                            Color var62 = GOLD;
                            var9.setGlowColorOverride(var62);
                            UUID var64 = var9.getUniqueId();
                            var7.placedCore = var64;
                            var7.floatFrom = var8;
                            var7.floatTick = 0;
                            Vector3f var68 = this.randomAxis(1280507206);
                            var7.coreAxis = var68;
                            Location var70 = var7.center;
                            this.ritualSound(var70, 0, 603439454);
                            return;
                        }

                        return;
                    }
            }
        }
    }

    private void tick() {
        double var99 = this.rot + 0.18;
        this.rot = var99;

        for (Player var11 : Bukkit.getOnlinePlayers()) {
            UUID var12 = var11.getUniqueId();
            Map var28 = this.states;
            FragmentCoreRitual.State var13 = (FragmentCoreRitual.State)var28.get(var12);
            if (var13 != null && var13.sealed) {
                if (var13.center != null) {
                    World var85 = var11.getWorld();
                    World var133 = var13.center.getWorld();
                    if (var85 == var133) {
                        Location var87 = var11.getLocation();
                        Location var135 = var13.center;
                        if (!(var87.distanceSquared(var135) > 100.0)) {
                            this.maintainSealed(var11, var13, 376885065);
                            continue;
                        }
                    }
                }

                this.clearDisplays(var13, 186841448);
                Map var94 = this.states;
                var94.remove(var12);
                this.payOwed(var11, 1245631054);
            } else if (!this.hasFragmentCore(var11, 1119873048)) {
                if (var13 != null) {
                    this.clearDisplays(var13, 186841448);
                    Map var39 = this.states;
                    var39.remove(var12);
                }
            } else {
                if (var13 == null) {
                    FragmentCoreRitual.State var42 = new FragmentCoreRitual.State();
                    var13 = var42;
                    Map var44 = this.states;
                    var44.put(var12, var42);
                    this.spawnFragments(var11, var42, 1204747096);
                }

                boolean var14 = RitualItems.isFragmentCore(var11.getInventory().getItemInMainHand(), 1590975450);
                if (var14 && !var13.holding) {
                    var13.holding = true;
                    Location var111 = var11.getLocation().clone().add(0.0, 1.9, 0.0);
                    var13.center = var111;
                    this.pushBack(var11, 62748165);
                    Location var114 = var11.getLocation();
                    List<Player> var15 = this.audience(var114, 1101174349);
                    this.pushFx(var11, var15, 1678566618);

                    for (Player var17 : var15) {
                        this.holdCover(var17, 20, 32, 59407939);
                        Location var125 = var17.getLocation();
                        String var147 = "bliss:goldgem_core_hold";
                        SoundCategory var154 = SoundCategory.MASTER;
                        var17.playSound(var125, var147, var154, 1.0F, 1.0F);
                    }

                    this.ensureGhost(var13, 214269004);
                } else if (!var14 && var13.holding) {
                    var13.holding = false;
                    UUID var128 = var13.ghost;
                    this.removeDisplay(var128, 633746888);
                    var13.ghost = null;
                    var13.center = null;
                }

                double var119 = var13.angle + 0.06;
                var13.angle = var119;
                if (var13.holding) {
                    this.updateRing(var11, var13, 321036937);
                } else {
                    this.updateHalo(var11, var13, 2128657047);
                }
            }
        }
    }

    private void maintainSealed(Player var1, FragmentCoreRitual.State var2, int var3) {
        if (!var2.finale) {
            double var88 = var2.angle + 0.04;
            var2.angle = var88;
            this.updateRing(var1, var2, 321036937);
            if (var2.floatTick >= 0 && var2.placedCore != null) {
                int var92 = var2.floatTick + 1;
                var2.floatTick = var92;
                double var120 = (double)var2.floatTick / 21.0;
                double var15 = Math.min(1.0, var120);
                double var37 = var15 * var15;
                double var149 = 2.0 * var15;
                double var126 = 3.0 - var149;
                double var17 = var37 * var126;
                UUID var94 = var2.placedCore;
                ItemDisplay var19 = this.display(var94, 1415410134);
                if (var19 != null) {
                    Location var96 = var2.floatFrom;
                    Location var129 = var2.center;
                    Location var97 = lerp(var96, var129, var17, 1628880772);
                    var19.teleport(var97);
                    this.spinCore(var19, var2, 732214092);
                }

                if (var15 >= 1.0) {
                    var2.floatTick = -1;
                    UUID var101 = var2.ghost;
                    this.removeDisplay(var101, 633746888);
                    var2.ghost = null;
                    UUID var104 = var2.placedCore;
                    ItemDisplay var20 = this.display(var104, 1415410134);
                    if (var20 != null) {
                        var20.setInterpolationDelay(0);
                        var20.setInterpolationDuration(3);
                        Vector3f var142 = new Vector3f();
                        Quaternionf var151 = new Quaternionf();
                        Vector3f var155 = new Vector3f(0.85F, 0.85F, 0.85F);
                        Quaternionf var157 = new Quaternionf();
                        Transformation var107 = new Transformation(var142, var151, var155, var157);
                        var20.setTransformation(var107);
                    }

                    if (!var2.climaxed) {
                        var2.climaxed = true;
                        Vector var64 = var1.getEyeLocation().toVector();
                        Vector var111 = var2.center.toVector();
                        Vector var21 = var64.subtract(var111);
                        var21.setY(0);
                        if (var21.lengthSquared() < 1.0E-6) {
                            Vector var72 = new Vector(0, 0, 1);
                            var21 = var72;
                        } else {
                            var21.normalize();
                        }

                        Vector var74 = var21.clone();
                        Vector var113 = new Vector(0, 1, 0);
                        Vector var22 = var74.crossProduct(var113);
                        if (var22.lengthSquared() < 1.0E-6) {
                            Vector var80 = new Vector(1, 0, 0);
                            var22 = var80;
                        } else {
                            var22.normalize();
                        }

                        this.beginFinale(var1, var2, var22, 791758146);
                    }
                }
            }
        }
    }

    private void beginFinale(Player var1, FragmentCoreRitual.State var2, final Vector var3, int var4) {
        final World var19 = var1.getWorld();
        final Location var20 = var2.center.clone();
        final Vector var31 = new Vector(0, 1, 0);
        final UUID var22 = var1.getUniqueId();
        var2.finale = true;
        this.shaderEngage(var19, 1599768622);
        this.shaderPhase(1, 707533204);
        this.ritualSound(var20, 1, 603439454);
        Vector var39 = var20.toVector();
        Vector var72 = var1.getEyeLocation().toVector();
        Vector var23 = var39.subtract(var72);
        var23.setY(0);
        if (var23.lengthSquared() < 1.0E-6) {
            Vector var47 = new Vector(0, 0, 1);
            var23 = var47;
        }

        var23.normalize();
        Location var51 = var20.clone();
        double var77 = var23.getX() * 3.0;
        double var113 = var23.getZ() * 3.0;
        final Location var24 = var51.add(var77, 8.0, var113);
        var24.setYaw(0.0F);
        var24.setPitch(0.0F);
        this.screenFlash(var1, 254213618);
        Particle var82 = Particle.END_ROD;
        Particles.spawn(var19, var82, var20, 20, 0.25, 0.25, 0.25, 0.4, 1986132876);
        Location var92 = var20.clone();
        this.climax(var19, var92, var3, var31, 1944951563);
        final float var25 = var1.getLocation().getYaw();
        final float var26 = var1.getLocation().getPitch();
        BukkitRunnable var64 = new BukkitRunnable() {
            int t = 0;
            double orbit = 0.0;
            double camTh = 0.0;
            double bYaw;
            double bPitch;
            FragmentCoreRitual.CamGroup cam;
            Float lastYaw;
            Float lastPitch;
            Float prevYaw;
            Float prevPitch;
            double armAng;
            double orbAng;
            final FragmentCoreRitual.OrbitEgg[] eggs;
            int whiteTimer;

            {
                double var51 = (double)var25;
                this.bYaw = var51;
                double var55 = (double)var26;
                this.bPitch = var55;
                this.cam = null;
                this.lastYaw = null;
                this.lastPitch = null;
                this.prevYaw = null;
                this.prevPitch = null;
                this.armAng = 0.0;
                this.orbAng = 0.0;
                FragmentCoreRitual.OrbitEgg[] var67 = new FragmentCoreRitual.OrbitEgg[3];
                this.eggs = var67;
                this.whiteTimer = 0;
            }

            public void run() {
                Player var19x = Bukkit.getPlayer(var22);
                Map var58 = FragmentCoreRitual.this.states;
                UUID var360 = var22;
                FragmentCoreRitual.State var20x = (FragmentCoreRitual.State)var58.get(var360);
                if (var19x != null && var19x.isOnline() && var20x != null && var20x.sealed) {
                    if (this.t <= 420) {
                        float var894 = var19x.getLocation().getYaw();
                        float var897 = var19x.getLocation().getPitch();
                        boolean var202;
                        if (this.t < 240) {
                            var202 = true;
                        } else {
                            var202 = false;
                        }

                        double var208;
                        if (var202) {
                            var208 = (double)this.t / 240.0;
                        } else {
                            double var642 = (double)(this.t - 240) / 180.0;
                            var208 = Math.min(1.0, var642);
                        }

                        double var341;
                        if (var202) {
                            var341 = 0.308;
                        } else {
                            var341 = 0.22;
                        }

                        double var737 = var208 * var208;
                        double var569 = 11.2 * var737;
                        double var910 = 7.0 + var569;
                        double var573 = var341 - 0.04;
                        double var575 = var573 * var208;
                        double var31x = 0.04 + var575;
                        if (this.t == 0 || this.t == 240) {
                            if (this.cam != null) {
                                this.cam.stop(373212778);
                            }

                            FragmentCoreRitual var578 = FragmentCoreRitual.this;
                            Location var686 = var20;
                            List var579 = var578.audience(var686, 1101174349);
                            FragmentCoreRitual.CamGroup var453 = FragmentCoreRitual.CamGroup.start(var19x, var579, 1794988348);
                            this.cam = var453;
                            this.camTh = 0.0;
                            this.lastYaw = null;
                            this.lastPitch = null;
                            this.prevYaw = null;
                            this.prevPitch = null;
                        }

                        if (this.cam != null) {
                            FragmentCoreRitual.CamGroup var236 = this.cam;
                            var236.publish(var910, var31x, 877774164);
                        } else {
                            if (this.lastYaw != null) {
                                float var464 = this.lastYaw;
                                double var33 = (double)Location.normalizeYaw(var894 - var464);
                                float var467 = this.lastPitch;
                                double var35 = (double)(var897 - var467);
                                double var250;
                                if (this.prevYaw != null) {
                                    float var504 = this.prevYaw;
                                    var250 = (double)Location.normalizeYaw(var894 - var504);
                                } else {
                                    var250 = var33;
                                }

                                double var357;
                                if (this.prevYaw != null) {
                                    float var470 = this.prevPitch;
                                    var357 = (double)(var897 - var470);
                                } else {
                                    var357 = var35;
                                }

                                double var258 = var250 * var250;
                                double var584 = var357 * var357;
                                double var260 = var258 + var584;
                                double var588 = var33 * var33;
                                double var749 = var35 * var35;
                                double var590 = var588 + var749;
                                boolean var337;
                                if (var260 < var590) {
                                    var337 = true;
                                } else {
                                    var337 = false;
                                }

                                double var471 = this.bYaw;
                                double var711;
                                if (var337) {
                                    var711 = var250;
                                } else {
                                    var711 = var33;
                                }

                                double var473 = var471 + var711;
                                this.bYaw = var473;
                                double var475 = this.bPitch;
                                double var691;
                                if (var337) {
                                    var691 = var357;
                                } else {
                                    var691 = var35;
                                }

                                double var477 = var475 + var691;
                                this.bPitch = var477;
                            }

                            double var596 = (double)this.t / 420.0;
                            double var912 = Math.min(1.0, var596);
                            double var755 = var912 * var912;
                            double var600 = 11.2 * var755;
                            double var914 = 7.0 + var600;
                            double var916 = 89.0 - var914;
                            double var481 = -var916;
                            double var794 = this.bPitch;
                            double var695 = Math.min(var916, var794);
                            double var483 = Math.max(var481, var695);
                            this.bPitch = var483;
                            double var485 = this.camTh;
                            double var798 = 0.18 * var912;
                            double var699 = 0.04 + var798;
                            double var487 = var485 + var699;
                            this.camTh = var487;
                            double var608 = (double)this.t / 20.0;
                            double var918 = Math.min(1.0, var608);
                            double var285 = var918 * var918;
                            double var761 = 2.0 * var918;
                            double var614 = 3.0 - var761;
                            double var920 = var285 * var614;
                            double var290 = this.bYaw;
                            double var619 = Math.cos(this.camTh);
                            double var621 = var619 * var914;
                            double var623 = var621 * var920;
                            float var922 = (float)(var290 + var623);
                            double var768 = this.bPitch;
                            double var835 = Math.sin(this.camTh);
                            double var837 = var835 * var914;
                            double var839 = var837 * var920;
                            double var770 = var768 + var839;
                            double var627 = Math.min(89.0, var770);
                            float var42 = (float)Math.max(-89.0, var627);
                            double var43 = Math.toRadians((double)var922);
                            double var45 = Math.toRadians((double)var42);
                            double var314 = -Math.cos(var45);
                            double var631 = Math.sin(var43);
                            double var47 = var314 * var631;
                            double var49 = -Math.sin(var45);
                            double var326 = Math.cos(var45);
                            double var635 = Math.cos(var43);
                            double var51 = var326 * var635;
                            Location var53 = var19x.getEyeLocation();
                            double var490 = var53.getX();
                            double var703 = var47 * 8.0;
                            double var492 = var490 + var703;
                            double var706 = var53.getY();
                            double var804 = var49 * 8.0;
                            double var708 = var706 + var804;
                            double var807 = var53.getZ();
                            double var863 = var51 * 8.0;
                            double var809 = var807 + var863;
                            LookAnchor var865 = LookAnchor.EYES;
                            var19x.lookAt(var492, var708, var809, var865);
                            Float var495 = this.lastYaw;
                            this.prevYaw = var495;
                            Float var497 = this.lastPitch;
                            this.prevPitch = var497;
                            Float var499 = var922;
                            this.lastYaw = var499;
                            Float var501 = var42;
                            this.lastPitch = var501;
                        }
                    } else if (this.cam != null) {
                        this.cam.stop(373212778);
                        this.cam = null;
                    }

                    int var21 = FragmentCoreRitual.coreBeatAt(this.t, 2031203949);
                    if (var21 >= 0) {
                        int var365 = FragmentCoreRitual.CORE_BEATS.length - 1;
                        boolean var359;
                        if (var21 == var365) {
                            var359 = true;
                        } else {
                            var359 = false;
                        }

                        if (var359) {
                            FragmentCoreRitual.this.shaderFlashPulse$80030838(20, 32, 1670127537);
                        } else {
                            FragmentCoreRitual var191 = FragmentCoreRitual.this;
                            World var448 = var19;
                            Location var563 = var20;
                            var191.flash(var448, var563, 6, 0.3, 0.3, 0.3, 2059459588);
                            FragmentCoreRitual.this.shaderFlashPulse(1, 7, 7, 1888682035);
                        }

                        FragmentCoreRitual var83 = FragmentCoreRitual.this;
                        World var368 = var19;
                        Location var511 = var20;
                        Particle var10 = Particle.END_ROD;
                        var83.angledBurst(var368, var511, true, 0.0, 78, var10, 1.8590000000000002, 1595198679);
                        ThreadLocalRandom var23 = ThreadLocalRandom.current();
                        int var370 = var23.nextInt(2);
                        int var24x = 2 + var370;

                        for (int var25x = 0; var25x < var24x; var25x++) {
                            double var26x = var23.nextDouble();
                            double var517 = var26x * var26x * 1.2000000000000002;
                            double var29 = 1.0 + var517;
                            FragmentCoreRitual var97 = FragmentCoreRitual.this;
                            World var373 = var19;
                            Location var520 = var20;
                            boolean var645 = var23.nextBoolean();
                            double var888 = 0.4 * var29;
                            double var868 = 0.6 + var888;
                            int var815 = (int)(30.0 * var868);
                            Particle var841 = Particle.SOUL;
                            double var874 = 0.55 * var29 * 1.3;
                            var97.angledBurst(var373, var520, var645, -1.0, var815, var841, var874, 1595198679);
                        }
                    }

                    double var525 = (double)this.t / 240.0;
                    double var895 = Math.min(1.0, var525);
                    double var374 = this.orbit;
                    double var844 = var895 * var895;
                    double var774 = 0.56 * var844;
                    double var648 = 0.06 + var774;
                    double var376 = var374 + var648;
                    this.orbit = var376;
                    FragmentCoreRitual var104 = FragmentCoreRitual.this;
                    double var378 = var104.rot;
                    double var850 = (double)this.t / 480.0;
                    double var778 = 1.3 * var850;
                    double var652 = 0.2 + var778;
                    double var380 = var378 + var652;
                    var104.rot = var380;
                    double var384 = this.armAng + 0.063;
                    this.armAng = var384;
                    if (this.t < 240) {
                        FragmentCoreRitual var189 = FragmentCoreRitual.this;
                        Location var561 = var20;
                        Vector var682 = var3;
                        Vector var732 = var31;
                        double var791 = this.orbit;
                        var189.placeFragments(var20x, var561, var682, var732, var791, false, 159589131);
                    } else {
                        double var389 = this.orbAng + 0.12;
                        this.orbAng = var389;
                        FragmentCoreRitual var110 = FragmentCoreRitual.this;
                        Location var528 = var24;
                        double var659 = this.orbAng;
                        boolean var789;
                        if (this.t % 2 == 0) {
                            var789 = true;
                        } else {
                            var789 = false;
                        }

                        FragmentCoreRitual var818 = FragmentCoreRitual.this;
                        int var853 = this.t;
                        DustOptions var819 = var818.goldPulse(var853, 1725442496);
                        var110.placeOrbits(var20x, var528, var659, var789, var819, 639164337);
                        if (this.t > 250) {
                            FragmentCoreRitual var114 = FragmentCoreRitual.this;
                            int var530 = this.t;
                            var114.spinGoldGem(var20x, var530, 1143770223);
                        }

                        double var536 = (double)(this.t - 240) / 180.0;
                        double var783 = Math.min(1.0, var536);
                        double var664 = 19.0 * var783;
                        int var398 = (int)(22.0 - var664);
                        int var906 = Math.max(3, var398);
                        int var400 = this.whiteTimer - 1;
                        this.whiteTimer = var400;
                        if (var400 <= 0) {
                            FragmentCoreRitual var123 = FragmentCoreRitual.this;
                            World var402 = var19;
                            Location var540 = var24;
                            var123.whiteBurst(var402, var540, 1807418117);
                            this.whiteTimer = var906;
                        }

                        FragmentCoreRitual var126 = FragmentCoreRitual.this;
                        World var405 = var19;
                        Location var542 = var24;
                        FragmentCoreRitual.OrbitEgg[] var667 = this.eggs;
                        var126.orbitEggs(var405, var542, var667, true, 1403835025);
                    }

                    if (this.t == 19) {
                        FragmentCoreRitual var130 = FragmentCoreRitual.this;
                        var130.screenFlash(var19x, 254213618);
                    }

                    if (this.t >= 31 && this.t < 240) {
                        FragmentCoreRitual var136 = FragmentCoreRitual.this;
                        World var411 = var19;
                        Location var544 = var20;
                        var136.eruption(var411, var544, 2104023806);
                    }

                    if (this.t >= 46 && this.t < 240) {
                        FragmentCoreRitual var142 = FragmentCoreRitual.this;
                        World var415 = var19;
                        Location var546 = var20;
                        Vector var669 = var3;
                        Vector var725 = var31;
                        double var786 = this.armAng;
                        var142.spinningArms(var415, var546, var669, var725, var786, 909769701);
                        if (this.t % 3 == 0) {
                            FragmentCoreRitual var147 = FragmentCoreRitual.this;
                            World var418 = var19;
                            Location var548 = var20;
                            boolean var673;
                            if (this.t % 6 == 0) {
                                var673 = true;
                            } else {
                                var673 = false;
                            }

                            Particle var854 = Particle.FIREWORK;
                            var147.angledBurst(var418, var548, var673, -1.0, 30, var854, 0.7150000000000001, 1595198679);
                        }
                    }

                    if (this.t == 240) {
                        FragmentCoreRitual var151 = FragmentCoreRitual.this;
                        Location var421 = var24;
                        var151.ritualSound(var421, 2, 603439454);
                        FragmentCoreRitual var153 = FragmentCoreRitual.this;
                        UUID var423 = var20x.placedCore;
                        var153.removeDisplay(var423, 633746888);
                        var20x.placedCore = null;
                        FragmentCoreRitual var156 = FragmentCoreRitual.this;
                        Location var426 = var24;
                        ItemStack var551 = RitualItems.goldGemVoronoi(0, 1571949517);
                        ItemDisplay var901 = var156.spawnDisplay(var426, var551, 0.01F, 1550711431);
                        Billboard var427 = Billboard.FIXED;
                        var901.setBillboard(var427);
                        var901.setGlowing(true);
                        Color var429 = FragmentCoreRitual.GOLD;
                        var901.setGlowColorOverride(var429);
                        Brightness var430 = new Brightness(15, 15);
                        var901.setBrightness(var430);
                        UUID var432 = var901.getUniqueId();
                        var20x.goldGem = var432;
                        var20x.revealed = true;
                        FragmentCoreRitual var165 = FragmentCoreRitual.this;
                        var165.popDisplay(var901, 1.54F, 8, 1058199539);
                        FragmentCoreRitual var167 = FragmentCoreRitual.this;
                        var167.orientForOrbit(var20x, 53600522);
                        FragmentCoreRitual.this.shaderPhase(2, 707533204);
                    }

                    if (this.t == 420) {
                        FragmentCoreRitual var173 = FragmentCoreRitual.this;
                        Location var439 = var24;
                        var173.ritualSound(var439, 3, 603439454);
                        FragmentCoreRitual.this.shaderFlashPulse$80030838(20, 32, 1670127537);
                        List var556 = var20x.frags;
                        ArrayList var176 = new ArrayList(var556);
                        Location var905 = var24.clone();
                        UUID var907 = var20x.goldGem;
                        FragmentCoreRitual.OrbitEgg[] var28 = this.eggs;
                        BukkitScheduler var184 = Bukkit.getScheduler();
                        BlissPlugin var443 = FragmentCoreRitual.this.plugin;
                        UUID var678 = var22;
                        Runnable var558 = () -> {
                            FragmentCoreRitual var12 = FragmentCoreRitual.this;
                            var12.beginAbsorption(var678, var905, var176, var907, var28, 407461808);
                        };
                        var184.runTaskLater(var443, var558, 20L);
                        this.cancel();
                    } else {
                        int var445 = this.t + 1;
                        this.t = var445;
                    }
                } else {
                    if (var20x != null) {
                        var20x.finale = false;
                    }

                    if (this.cam != null) {
                        this.cam.stop(373212778);
                        this.cam = null;
                    }

                    FragmentCoreRitual var353 = FragmentCoreRitual.this;
                    World var508 = var19;
                    var353.ritualSoundStop(var508, 425244914);
                    FragmentCoreRitual.this.shaderRelease(990029622);
                    this.cancel();
                }
            }
        };
        BlissPlugin var85 = this.plugin;
        var64.runTaskTimer(var85, 0L, 1L);
    }

    private void beginAbsorption(
        final UUID var1, final Location var2, final List<UUID> var3, final UUID var4, final FragmentCoreRitual.OrbitEgg[] var5, int var6
    ) {
        this.shaderPhase(7, 707533204);
        this.ritualSound(var2, 4, 603439454);
        BukkitRunnable var16 = new BukkitRunnable() {
            int idx = 0;
            int tick = 0;
            int gtick = 0;
            int tailHold = 0;
            Location startPos = null;

            public void run() {
                Player var14 = Bukkit.getPlayer(var1);
                Map var40 = FragmentCoreRitual.this.states;
                UUID var215 = var1;
                FragmentCoreRitual.State var15 = (FragmentCoreRitual.State)var40.get(var215);
                if (var14 != null && var14.isOnline() && var15 != null) {
                    World var16 = var2.getWorld();
                    int var220 = this.gtick + 1;
                    this.gtick = var220;
                    double var17 = (double)this.gtick * 0.12;
                    FragmentCoreRitual var63 = FragmentCoreRitual.this;
                    int var307 = this.gtick * 8;
                    var63.spinGoldGem(var15, var307, 1143770223);

                    while (true) {
                        int var65 = this.idx;
                        int var224 = var3.size();
                        if (var65 >= var224) {
                            break;
                        }

                        FragmentCoreRitual var67 = FragmentCoreRitual.this;
                        List var226 = var3;
                        int var309 = this.idx;
                        UUID var228 = (UUID)var226.get(var309);
                        if (var67.display(var228, 1415410134) != null) {
                            break;
                        }

                        int var230 = this.idx + 1;
                        this.idx = var230;
                        this.tick = 0;
                    }

                    int var72 = this.idx;
                    int var234 = var3.size();
                    boolean var214;
                    if (var72 >= var234) {
                        var214 = true;
                    } else {
                        var214 = false;
                    }

                    boolean var19 = var214;
                    boolean var75;
                    if (this.gtick <= 53) {
                        var75 = true;
                    } else {
                        var75 = false;
                    }

                    boolean var20 = var75;

                    for (int var21 = 0; var21 < 7; var21++) {
                        int var239 = var3.size();
                        if (var21 >= var239) {
                            break;
                        }

                        float var22 = (float)((double)var21 * (Math.PI * 2.0 / 7.0));
                        double var87 = Math.toRadians(20.0);
                        double var314 = (double)var21;
                        double var395 = Math.toRadians(19.166666666666668);
                        double var316 = var314 * var395;
                        float var23 = (float)(var87 + var316);
                        Quaternionf var92 = new Quaternionf();
                        Quaternionf var93 = var92.rotateY(var22);
                        Quaternionf var24 = var93.rotateX(var23);
                        Vector3f var242 = new Vector3f(1.0F, 0.0F, 0.0F);
                        Vector3f var25 = var24.transform(var242);
                        Vector3f var243 = new Vector3f(0.0F, 0.0F, 1.0F);
                        Vector3f var26 = var24.transform(var243);
                        float var319 = var25.x;
                        float var374 = var25.y;
                        float var400 = var25.z;
                        Vector var99 = new Vector(var319, var374, var400);
                        float var321 = var26.x;
                        float var376 = var26.y;
                        float var402 = var26.z;
                        Vector var100 = new Vector(var321, var376, var402);
                        if (this.gtick % 2 == 0) {
                            FragmentCoreRitual var105 = FragmentCoreRitual.this;
                            Location var323 = var2;
                            Particle var10 = Particle.DUST;
                            FragmentCoreRitual var446 = FragmentCoreRitual.this;
                            int var459 = this.gtick;
                            DustOptions var447 = var446.goldPulse(var459, 1725442496);
                            int var461 = Math.max(80, 291);
                            var105.drawRing(var16, var323, var99, var100, 5.2, var10, var447, var461, 1109974871);
                        }

                        if (!var19) {
                            if (!var20) {
                                int var247 = this.idx;
                                if (var21 <= var247) {
                                    continue;
                                }
                            }

                            double var327 = (double)var21 * (Math.PI * 2.0 / 7.0);
                            double var29 = var17 + var327;
                            double var31 = Math.cos(var29) * 5.2;
                            double var33 = Math.sin(var29) * 5.2;
                            FragmentCoreRitual var126 = FragmentCoreRitual.this;
                            List var249 = var3;
                            UUID var251 = (UUID)var249.get(var21);
                            ItemDisplay var35 = var126.display(var251, 1415410134);
                            if (var35 != null) {
                                Location var254 = var2.clone();
                                double var335 = var99.getX();
                                double var337 = var335 * var31;
                                double var409 = var100.getX();
                                double var411 = var409 * var33;
                                double var339 = var337 + var411;
                                double var414 = var99.getY();
                                double var416 = var414 * var31;
                                double var433 = var100.getY();
                                double var435 = var433 * var33;
                                double var418 = var416 + var435;
                                double var438 = var99.getZ();
                                double var440 = var438 * var31;
                                double var453 = var100.getZ();
                                double var455 = var453 * var33;
                                double var442 = var440 + var455;
                                Location var255 = var254.add(var339, var418, var442);
                                var35.teleport(var255);
                            }
                        }
                    }

                    FragmentCoreRitual var132 = FragmentCoreRitual.this;
                    Location var342 = var2;
                    FragmentCoreRitual.OrbitEgg[] var379 = var5;
                    var132.orbitEggs(var16, var342, var379, false, 1403835025);
                    if (this.gtick >= 9 && this.gtick < 37 && (this.gtick - 9) % 2 == 0) {
                        for (byte var464 = 0; var464 < 3; var464 += 1) {
                            FragmentCoreRitual var213 = FragmentCoreRitual.this;
                            Location var370 = var2;
                            var213.lightningBolt(var16, var370, 47730663);
                        }
                    }

                    if (var19) {
                        int var299 = this.tailHold;
                        int var367 = var299 + 1;
                        this.tailHold = var367;
                        if (var299 >= 41) {
                            FragmentCoreRitual var210 = FragmentCoreRitual.this;
                            UUID var394 = var4;
                            FragmentCoreRitual.OrbitEgg[] var424 = var5;
                            var210.finishAbsorption(var14, var15, var394, var424, 1672547075);
                            this.cancel();
                        }
                    } else if (var20) {
                        if (this.gtick == 22) {
                            FragmentCoreRitual.this.shaderPhase(3, 707533204);
                        }

                        if (this.gtick == 22 || this.gtick == 27) {
                            FragmentCoreRitual var154 = FragmentCoreRitual.this;
                            Location var344 = var2;
                            double var384 = (double)this.gtick * 0.06;
                            var154.gustBurst(var16, var344, var384, 324509252);
                        }
                    } else {
                        FragmentCoreRitual var156 = FragmentCoreRitual.this;
                        List var268 = var3;
                        int var346 = this.idx;
                        UUID var270 = (UUID)var268.get(var346);
                        ItemDisplay var465 = var156.display(var270, 1415410134);
                        if (this.tick == 0) {
                            Location var272 = var465.getLocation();
                            this.startPos = var272;
                        }

                        int[] var161 = FragmentCoreRitual.ABSORB_TETHERS;
                        int var274 = this.idx;
                        int var349 = FragmentCoreRitual.ABSORB_TETHERS.length - 1;
                        int var275 = Math.min(var274, var349);
                        int var466 = var161[var275];
                        int var164 = this.tick;
                        if (var164 < var466) {
                            FragmentCoreRitual var207 = FragmentCoreRitual.this;
                            Location var366 = this.startPos;
                            Location var391 = var2;
                            var207.tetherLine(var16, var366, var391, 319471616);
                        } else {
                            int var166 = this.tick;
                            if (var166 == var466) {
                                FragmentCoreRitual var168 = FragmentCoreRitual.this;
                                Location var351 = var2;
                                Particle var445 = Particle.END_ROD;
                                var168.angledBurst(var16, var351, true, -1.0, 30, var445, 0.55, 1595198679);
                                FragmentCoreRitual var170 = FragmentCoreRitual.this;
                                Location var353 = var2;
                                var170.absorbScatter(var16, var353, 1847470161);
                            }

                            int var172 = this.tick;
                            double var467 = (double)(var172 - var466 + 1) / 3.0;
                            if (var467 >= 1.0) {
                                var465.remove();
                                FragmentCoreRitual var196 = FragmentCoreRitual.this;
                                var196.screenFlash(var14, 254213618);
                                int var290 = this.idx + 1;
                                this.idx = var290;
                                this.tick = 0;
                                FragmentCoreRitual var200 = FragmentCoreRitual.this;
                                UUID var293 = var4;
                                ItemDisplay var471 = var200.display(var293, 1415410134);
                                if (var471 != null && this.idx <= 6) {
                                    ItemStack var297 = RitualItems.goldGemVoronoi(this.idx, 1571949517);
                                    var471.setItemStack(var297);
                                }

                                return;
                            }

                            double var469 = var467 * var467;
                            Location var187 = this.startPos;
                            Location var283 = var2;
                            Location var472 = FragmentCoreRitual.lerp(var187, var283, var469, 1628880772);
                            FragmentCoreRitual var190 = FragmentCoreRitual.this;
                            Location var389 = var2;
                            var190.tetherLine(var16, var472, var389, 319471616);
                            var465.teleport(var472);
                        }

                        int var287 = this.tick + 1;
                        this.tick = var287;
                    }
                } else {
                    FragmentCoreRitual var48 = FragmentCoreRitual.this;
                    World var218 = var2.getWorld();
                    var48.ritualSoundStop(var218, 425244914);
                    FragmentCoreRitual.this.shaderRelease(990029622);
                    this.cancel();
                }
            }
        };
        BlissPlugin var20 = this.plugin;
        var16.runTaskTimer(var20, 0L, 1L);
    }

    private void finishAbsorption(Player var1, FragmentCoreRitual.State var2, UUID var3, FragmentCoreRitual.OrbitEgg[] var4, int var5) {
        this.shaderPhase(4, 707533204);
        Location var29 = var2.center;
        this.ritualSound(var29, 5, 603439454);
        this.screenFlash(var1, 254213618);
        var1.getWorld();
        ItemDisplay var12 = this.display(var3, 1415410134);
        if (var12 != null) {
            ItemStack var32 = RitualItems.goldGemReveal(557096996);
            var12.setItemStack(var32);
        }

        Location var27;
        if (var12 != null) {
            var27 = var12.getLocation();
        } else {
            var27 = var1.getLocation();
        }

        Location var13 = var27.clone();
        UUID var34 = var1.getUniqueId();
        this.beginOrbitHold(var34, var3, var13, var4, 214366558);
    }

    private void beginOrbitHold(final UUID var1, final UUID var2, final Location var3, final FragmentCoreRitual.OrbitEgg[] var4, int var5) {
        final World var14 = var3.getWorld();
        final Vector[] var15 = this.eruptionDirs(1846592278);
        BukkitRunnable var19 = new BukkitRunnable() {
            int t = 0;
            int spin = 0;
            FragmentCoreRitual.CamGroup cam = null;

            public void run() {
                Player var13 = Bukkit.getPlayer(var1);
                FragmentCoreRitual var20 = FragmentCoreRitual.this;
                UUID var102 = var2;
                ItemDisplay var14x = var20.display(var102, 1415410134);
                if (var13 != null && var13.isOnline() && var14x != null) {
                    int var104 = this.spin + 1;
                    this.spin = var104;
                    var14x.setInterpolationDelay(0);
                    var14x.setInterpolationDuration(2);
                    Vector3f var4x = new Vector3f();
                    Quaternionf var206 = new Quaternionf();
                    float var220 = (float)this.spin * 0.05F;
                    Quaternionf var207 = var206.rotateY(var220);
                    Vector3f var221 = new Vector3f(1.54F, 1.54F, 1.54F);
                    Quaternionf var234 = new Quaternionf();
                    float var239 = FragmentCoreRitual.GEM_ART_ROLL;
                    Quaternionf var235 = var234.rotateZ(var239);
                    Transformation var107 = new Transformation(var4x, var207, var221, var235);
                    var14x.setTransformation(var107);
                    FragmentCoreRitual var31 = FragmentCoreRitual.this;
                    World var109 = var14;
                    Location var150 = var3;
                    int var175 = this.t;
                    var31.drawOrb(var109, var150, var175, 1265241801);
                    if (this.t % 4 == 0) {
                        ThreadLocalRandom var15x = ThreadLocalRandom.current();
                        FragmentCoreRitual var37 = FragmentCoreRitual.this;
                        Location var112 = var3;
                        int var152 = var15x.nextInt(7);
                        double var180 = var15x.nextDouble() * (Math.PI * 2);
                        Location var16 = var37.orbitPoint(var112, var152, var180, 1999131241);
                        World var40 = var14;
                        Particle var113 = Particle.END_ROD;
                        Particles.spawn(var40, var113, var16, 0, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
                    }

                    if (this.t == 60) {
                        FragmentCoreRitual var155 = FragmentCoreRitual.this;
                        Location var184 = var3;
                        List var156 = var155.audience(var184, 1101174349);
                        FragmentCoreRitual.CamGroup var116 = FragmentCoreRitual.CamGroup.start(var13, var156, 1794988348);
                        this.cam = var116;
                    }

                    if (this.cam != null) {
                        double var162 = (double)(this.t - 60) / 132.0;
                        double var247 = Math.min(1.0, var162);
                        FragmentCoreRitual.CamGroup var51 = this.cam;
                        double var188 = 11.2 * var247;
                        double var190 = var188 * var247;
                        double var119 = 7.0 + var190;
                        double var230 = 0.18 * var247;
                        double var194 = 0.04 + var230;
                        var51.publish(var119, var194, 877774164);
                    }

                    if (this.t < 192) {
                        FragmentCoreRitual var55 = FragmentCoreRitual.this;
                        World var123 = var14;
                        Location var165 = var3;
                        Vector[] var197 = var15;
                        var55.lineEruptionTick(var123, var165, var197, 2069481386);
                    }

                    FragmentCoreRitual var57 = FragmentCoreRitual.this;
                    World var125 = var14;
                    Location var167 = var3;
                    FragmentCoreRitual.OrbitEgg[] var199 = var4;
                    var57.orbitEggs(var125, var167, var199, true, 1403835025);
                    if (this.t == 146 || this.t == 166) {
                        FragmentCoreRitual var63 = FragmentCoreRitual.this;
                        World var129 = var14;
                        Location var169 = var3;
                        Particle var242 = Particle.SOUL;
                        var63.angledBurst(var129, var169, false, 0.0, 48, var242, 1.8, 1595198679);
                    }

                    int var130 = this.t;
                    int var170 = var130 + 1;
                    this.t = var170;
                    if (var130 >= 192) {
                        if (this.cam != null) {
                            this.cam.stop(373212778);
                            this.cam = null;
                        }

                        FragmentCoreRitual.this.shaderFlashPulse$80030838(20, 32, 1670127537);
                        BukkitScheduler var72 = Bukkit.getScheduler();
                        BlissPlugin var136 = FragmentCoreRitual.this.plugin;
                        UUID var203 = var2;
                        UUID var216 = var1;
                        Location var233 = var3;
                        Runnable var173 = () -> {
                            FragmentCoreRitual.this.shaderPhase(6, 707533204);
                            FragmentCoreRitual var11 = FragmentCoreRitual.this;
                            ItemDisplay var8 = var11.display(var203, 1415410134);
                            if (var8 != null) {
                                ItemStack var18 = RitualItems.goldGemEmpty(1009261075);
                                var8.setItemStack(var18);
                            }

                            FragmentCoreRitual var16x = FragmentCoreRitual.this;
                            var16x.beginAftermath(var216, var203, var233, 114809999);
                        };
                        var72.runTaskLater(var136, var173, 8L);
                        this.cancel();
                    }
                } else {
                    FragmentCoreRitual var76 = FragmentCoreRitual.this;
                    UUID var138 = var2;
                    var76.removeDisplay(var138, 633746888);
                    Map var79 = FragmentCoreRitual.this.states;
                    UUID var140 = var1;
                    FragmentCoreRitual.State var249 = (FragmentCoreRitual.State)var79.get(var140);
                    if (var249 != null) {
                        var249.finale = false;
                        FragmentCoreRitual var85 = FragmentCoreRitual.this;
                        var85.clearDisplays(var249, 186841448);
                    }

                    FragmentCoreRitual var87 = FragmentCoreRitual.this;
                    var87.payOwed(var13, 1245631054);
                    Map var90 = FragmentCoreRitual.this.states;
                    UUID var145 = var1;
                    var90.remove(var145);
                    if (this.cam != null) {
                        this.cam.stop(373212778);
                        this.cam = null;
                    }

                    FragmentCoreRitual var98 = FragmentCoreRitual.this;
                    World var148 = var14;
                    var98.ritualSoundStop(var148, 425244914);
                    FragmentCoreRitual.this.shaderRelease(990029622);
                    this.cancel();
                }
            }
        };
        BlissPlugin var22 = this.plugin;
        var19.runTaskTimer(var22, 0L, 1L);
    }

    private void drawOrb(World var1, Location var2, int var3, int var4) {
        if (var3 % 2 == 0) {
            ThreadLocalRandom var27 = ThreadLocalRandom.current();
            int var28 = Math.max(80, 291);
            DustOptions var29 = this.goldPulse(var3, 1725442496);

            for (int var30 = 0; var30 < 7; var30++) {
                float var31 = (float)((double)var30 * (Math.PI * 2.0 / 7.0));
                double var59 = Math.toRadians(20.0);
                double var107 = (double)var30;
                double var133 = Math.toRadians(19.166666666666668);
                double var109 = var107 * var133;
                float var32 = (float)(var59 + var109);
                Quaternionf var64 = new Quaternionf();
                Quaternionf var65 = var64.rotateY(var31);
                Quaternionf var33 = var65.rotateX(var32);
                Vector3f var100 = new Vector3f(1.0F, 0.0F, 0.0F);
                Vector3f var34 = var33.transform(var100);
                Vector3f var101 = new Vector3f(0.0F, 0.0F, 1.0F);
                Vector3f var35 = var33.transform(var101);

                for (int var36 = 0; var36 < var28; var36++) {
                    if (var27.nextInt(100) >= 40) {
                        double var76 = (double)var36;
                        double var112 = (double)var28;
                        double var37 = var76 / var112 * (Math.PI * 2);
                        double var39 = Math.cos(var37) * 5.2;
                        double var41 = Math.sin(var37) * 5.2;
                        Particle var105 = Particle.DUST;
                        Location var121 = var2.clone();
                        double var126 = (double)var34.x;
                        double var128 = var126 * var39;
                        double var144 = (double)var35.x;
                        double var146 = var144 * var41;
                        double var130 = var128 + var146;
                        double var150 = (double)var34.y;
                        double var152 = var150 * var39;
                        double var160 = (double)var35.y;
                        double var162 = var160 * var41;
                        double var154 = var152 + var162;
                        double var166 = (double)var34.z;
                        double var168 = var166 * var39;
                        double var176 = (double)var35.z;
                        double var178 = var176 * var41;
                        double var170 = var168 + var178;
                        Location var122 = var121.add(var130, var154, var170);
                        Particles.spawn(var1, var105, var122, 1, 0.0, 0.0, 0.0, 0.0, var29, true, 1748031581);
                    }
                }
            }
        }
    }

    private Vector[] eruptionDirs(int var1) {
        ThreadLocalRandom var14 = ThreadLocalRandom.current();
        Vector[] var15 = new Vector[5];
        int var16 = 0;

        while (true) {
            int var47 = var15.length;
            if (var16 >= var47) {
                return var15;
            }

            double var17 = var14.nextDouble() * (Math.PI * 2);
            double var19 = var14.nextDouble() * 2.0 - 1.0;
            double var58 = var19 * var19;
            double var55 = 1.0 - var58;
            double var21 = Math.sqrt(Math.max(0.0, var55));
            double var66 = Math.cos(var17);
            double var62 = var21 * var66;
            double var72 = Math.sin(var17);
            double var70 = var21 * var72;
            Vector var57 = new Vector(var62, var19, var70);
            var15[var16] = var57;
            var16++;
        }
    }

    private void lineEruptionTick(World var1, Location var2, Vector[] var3, int var4) {
        ThreadLocalRandom var25 = ThreadLocalRandom.current();

        for (Vector var29 : var3) {
            Location var42 = var2.clone();
            double var55 = var29.getX() * 5.2;
            double var69 = var29.getY() * 5.2;
            double var77 = var29.getZ() * 5.2;
            Location var30 = var42.add(var55, var69, var77);

            for (int var31 = 0; var31 < 3; var31++) {
                double var61 = var25.nextDouble() * 0.75;
                double var32 = 0.12 + var61;
                Particle var58 = Particle.SOUL;
                double var64 = var30.getX();
                double var72 = var30.getY();
                double var79 = var30.getZ();
                double var81 = var29.getX();
                double var83 = var29.getY();
                double var85 = var29.getZ();
                Particles.spawn(var1, var58, var64, var72, var79, 0, var81, var83, var85, var32, null, true, 954482185);
            }
        }
    }

    private void drawOrbitRings(World var1, Location var2, int var3) {
        for (int var17 = 0; var17 < 7; var17++) {
            float var18 = (float)((double)var17 * (Math.PI * 2.0 / 7.0));
            double var32 = Math.toRadians(20.0);
            double var54 = (double)var17;
            double var61 = Math.toRadians(19.166666666666668);
            double var56 = var54 * var61;
            float var19 = (float)(var32 + var56);
            Quaternionf var37 = new Quaternionf();
            Quaternionf var38 = var37.rotateY(var18);
            Quaternionf var20 = var38.rotateX(var19);
            Vector3f var49 = new Vector3f(1.0F, 0.0F, 0.0F);
            Vector3f var21 = var20.transform(var49);
            Vector3f var50 = new Vector3f(0.0F, 0.0F, 1.0F);
            Vector3f var22 = var20.transform(var50);
            if (var3 % 2 == 0) {
                float var68 = var21.x;
                float var71 = var21.y;
                float var74 = var21.z;
                Vector var60 = new Vector(var68, var71, var74);
                float var73 = var22.x;
                float var76 = var22.y;
                float var78 = var22.z;
                Vector var65 = new Vector(var73, var76, var78);
                Particle var77 = Particle.DUST;
                DustOptions var80 = this.goldPulse(var3, 1725442496);
                int var82 = Math.max(80, 291);
                this.drawRing(var1, var2, var60, var65, 5.2, var77, var80, var82, 1109974871);
            }
        }
    }

    private Location orbitPoint(Location var1, int var2, double var3, int var5) {
        float var17 = (float)((double)var2 * (Math.PI * 2.0 / 7.0));
        double var33 = Math.toRadians(20.0);
        double var73 = (double)var2;
        double var98 = Math.toRadians(19.166666666666668);
        double var75 = var73 * var98;
        float var18 = (float)(var33 + var75);
        Quaternionf var38 = new Quaternionf();
        Quaternionf var39 = var38.rotateY(var17);
        Quaternionf var19 = var39.rotateX(var18);
        Vector3f var62 = new Vector3f(1.0F, 0.0F, 0.0F);
        Vector3f var20 = var19.transform(var62);
        Vector3f var63 = new Vector3f(0.0F, 0.0F, 1.0F);
        Vector3f var21 = var19.transform(var63);
        double var22 = Math.cos(var3) * 5.2;
        double var24 = Math.sin(var3) * 5.2;
        Location var58 = var1.clone();
        double var66 = (double)var20.x;
        double var68 = var66 * var22;
        double var86 = (double)var21.x;
        double var88 = var86 * var24;
        double var70 = var68 + var88;
        double var92 = (double)var20.y;
        double var94 = var92 * var22;
        double var109 = (double)var21.y;
        double var111 = var109 * var24;
        double var96 = var94 + var111;
        double var115 = (double)var20.z;
        double var117 = var115 * var22;
        double var125 = (double)var21.z;
        double var127 = var125 * var24;
        double var119 = var117 + var127;
        return var58.add(var70, var96, var119);
    }

    private void beginAftermath(final UUID var1, final UUID var2, final Location var3, int var4) {
        final World var12 = var3.getWorld();
        this.ritualSound(var3, 6, 603439454);
        BukkitRunnable var15 = new BukkitRunnable() {
            int lastPulse = -999;
            int t = 0;
            int timer = 0;
            int spin = 0;
            int mix = 0;
            int beat = 0;
            int endAt;
            boolean released;
            float jx;
            float jy;
            float jroll;
            UUID draw;
            Location drawFrom;
            Location drawTo;
            Location goldFrom;
            int[] drawColor;
            Color drawTint;
            DustOptions drawDust;
            final List<UUID> shards;
            double shardFloorY;

            {
                int var44 = FragmentCoreRitual.AFTERMATH_TICKS;
                this.endAt = var44;
                this.released = false;
                this.jx = 0.0F;
                this.jy = 0.0F;
                this.jroll = 0.0F;
                this.draw = null;
                this.drawFrom = null;
                this.drawTo = null;
                this.goldFrom = null;
                this.drawColor = null;
                this.drawTint = null;
                this.drawDust = null;
                ArrayList var56 = new ArrayList();
                this.shards = var56;
                this.shardFloorY = Double.NaN;
            }

            public void run() {
                Player var11 = Bukkit.getPlayer(var1);
                FragmentCoreRitual var30 = FragmentCoreRitual.this;
                UUID var290 = var2;
                ItemDisplay var12x = var30.display(var290, 1415410134);
                if (var11 != null && var11.isOnline() && var12x != null) {
                    int var291 = this.t;
                    int var488 = var291 + 1;
                    this.t = var488;
                    int var293 = this.endAt;
                    if (var291 < var293) {
                        int var295 = this.spin + 1;
                        this.spin = var295;
                        var12x.setInterpolationDelay(0);
                        var12x.setInterpolationDuration(2);
                        Vector3f var573 = new Vector3f();
                        Quaternionf var642 = new Quaternionf();
                        float var711 = (float)this.spin * 0.05F;
                        Quaternionf var643 = var642.rotateY(var711);
                        Vector3f var712 = new Vector3f(1.54F, 1.54F, 1.54F);
                        Quaternionf var741 = new Quaternionf();
                        float var783 = FragmentCoreRitual.GEM_ART_ROLL;
                        Quaternionf var742 = var741.rotateZ(var783);
                        Transformation var298 = new Transformation(var573, var643, var712, var742);
                        var12x.setTransformation(var298);
                        if (!this.released && this.t % 2 == 0) {
                            FragmentCoreRitual var47 = FragmentCoreRitual.this;
                            World var301 = var12;
                            Location var491 = var3;
                            var47.drawGemShapes(var301, var491, var11, 1533848329);
                        }

                        if (!this.released && FragmentCoreRitual.isPulseBeat(this.t, 1231371211)) {
                            int var303 = this.t;
                            this.lastPulse = var303;
                            FragmentCoreRitual var55 = FragmentCoreRitual.this;
                            World var305 = var12;
                            Location var493 = var3;
                            var55.lightPulse(var305, var493, 777185327);
                        }

                        int var57 = this.t;
                        int var306 = FragmentCoreRitual.AFTERMATH_DRAW_AT;
                        if (var57 == var306) {
                            Location var309 = var11.getEyeLocation().subtract(0.0, 0.5, 0.0);
                            Vector var499 = var11.getEyeLocation().getDirection().multiply(0.35);
                            Location var310 = var309.add(var499);
                            this.drawFrom = var310;
                            this.drawFrom.setYaw(0.0F);
                            this.drawFrom.setPitch(0.0F);
                            Location var316 = var3.clone().add(0.0, 2.6, 0.0);
                            this.drawTo = var316;
                            FragmentCoreRitual var318 = FragmentCoreRitual.this;
                            Location var578 = this.drawFrom;
                            UUID var319 = var318.spawnTriggerGem(var11, var578, 1038950049);
                            this.draw = var319;
                            if (this.draw != null) {
                                FragmentCoreRitual.this.shaderPhase(5, 707533204);
                                FragmentCoreRitual var461 = FragmentCoreRitual.this;
                                int var564 = FragmentCoreRitual.this.shaderGemId;
                                int[] var462 = var461.gemFlashColor(var564, 1586303220);
                                this.drawColor = var462;
                                FragmentCoreRitual var246 = FragmentCoreRitual.this;
                                int var465 = FragmentCoreRitual.this.shaderGemId;
                                int[] var821 = var246.gemBaseColor(var465, 1417188842);
                                int var467 = var821[0];
                                int var567 = var821[1];
                                int var632 = var821[2];
                                Color var468 = Color.fromRGB(var467, var567, var632);
                                this.drawTint = var468;
                                Color var634 = this.drawTint;
                                DustOptions var469 = new DustOptions(var634, 0.8F);
                                this.drawDust = var469;
                            } else {
                                int var322 = this.t + 40;
                                this.endAt = var322;
                            }
                        }

                        int var289;
                        if (this.draw == null) {
                            var289 = Integer.MIN_VALUE;
                        } else {
                            int var71 = this.t;
                            int var323 = FragmentCoreRitual.AFTERMATH_DRAW_AT;
                            var289 = var71 - var323;
                        }

                        FragmentCoreRitual var73 = FragmentCoreRitual.this;
                        UUID var325 = this.draw;
                        ItemDisplay var14 = var73.display(var325, 1415410134);
                        if (var14 != null) {
                            int var326 = FragmentCoreRitual.DRAW_FLIGHT_TICKS;
                            if (var289 <= var326) {
                                double var78 = (double)var289;
                                double var505 = (double)FragmentCoreRitual.DRAW_FLIGHT_TICKS;
                                double var15 = var78 / var505;
                                FragmentCoreRitual var83 = FragmentCoreRitual.this;
                                Location var328 = this.drawFrom;
                                Location var508 = this.drawTo;
                                double var581 = var15 * var15;
                                double var786 = 2.0 * var15;
                                double var717 = 3.0 - var786;
                                double var583 = var581 * var717;
                                Location var18 = var83.arc(var328, var508, var583, 56258953);
                                var14.teleport(var18);
                                FragmentCoreRitual var88 = FragmentCoreRitual.this;
                                World var331 = var12;
                                Color var586 = this.drawTint;
                                var88.drawTrail(var331, var18, var586, 1290410198);
                            } else {
                                Location var454 = this.drawTo.clone();
                                double var696 = Math.sin((double)var289 * 0.08 / 1.5) * 0.08;
                                Location var455 = var454.add(0.0, var696, 0.0);
                                var14.teleport(var455);
                                if (var289 % 2 == 0) {
                                    FragmentCoreRitual var241 = FragmentCoreRitual.this;
                                    World var458 = var12;
                                    Location var561 = this.drawTo;
                                    Color var629 = this.drawTint;
                                    var241.gemHalo(var458, var561, var629, 106884068);
                                }
                            }

                            label211: {
                                int var332 = FragmentCoreRitual.DRAW_BROKEN_AT;
                                if (var289 > var332) {
                                    int var333 = FragmentCoreRitual.DRAW_COVER2_AT;
                                    if (var289 < var333) {
                                        int var335 = FragmentCoreRitual.slow(4, 1516685867);
                                        if (var289 % var335 == 0) {
                                            ThreadLocalRandom var823 = ThreadLocalRandom.current();
                                            float var343 = (float)((var823.nextDouble() - 0.5) * 0.16);
                                            this.jx = var343;
                                            float var351 = (float)((var823.nextDouble() - 0.5) * 0.16);
                                            this.jy = var351;
                                            float var359 = (float)((var823.nextDouble() - 0.5) * 0.3);
                                            this.jroll = var359;
                                        }
                                        break label211;
                                    }
                                }

                                this.jroll = 0.0F;
                                this.jy = 0.0F;
                                this.jx = 0.0F;
                            }

                            var14.setInterpolationDelay(0);
                            var14.setInterpolationDuration(2);
                            float var720 = this.jx;
                            float var748 = this.jy;
                            Vector3f var599 = new Vector3f(var720, var748, 0.0F);
                            Quaternionf var648 = new Quaternionf();
                            float var752 = (float)this.spin * 0.07F;
                            float var722 = 2.1F + var752;
                            Quaternionf var649 = var648.rotateY(var722);
                            Vector3f var723 = new Vector3f(1.0F, 1.0F, 1.0F);
                            Quaternionf var753 = new Quaternionf();
                            float var791 = FragmentCoreRitual.GEM_ART_ROLL;
                            float var802 = this.jroll;
                            float var792 = var791 + var802;
                            Quaternionf var754 = var753.rotateZ(var792);
                            Transformation var362 = new Transformation(var599, var649, var723, var754);
                            var14.setTransformation(var362);
                        }

                        if (var289 >= 0) {
                            int var363 = FragmentCoreRitual.DRAW_BURST_AT;
                            if (var289 == var363) {
                                FragmentCoreRitual var103 = FragmentCoreRitual.this;
                                World var365 = var12;
                                Location var511 = this.drawTo;
                                Color var601 = this.drawTint;
                                double var653 = (double)var289 * 0.06;
                                var103.gemGustBurst(var365, var511, var601, var653, 2145733671);
                            }

                            int var366 = FragmentCoreRitual.DRAW_COVER_AT;
                            if (var289 == var366) {
                                FragmentCoreRitual.this.shaderFlashPulse$80030838(20, 32, 1670127537);
                            }

                            int var368 = FragmentCoreRitual.DRAW_BROKEN_AT;
                            if (var289 == var368 && var14 != null) {
                                FragmentCoreRitual var110 = FragmentCoreRitual.this;
                                ItemStack var824 = var110.brokenGemItem(var11, 1605876340);
                                if (var824 != null) {
                                    var14.setItemStack(var824);
                                }
                            }

                            int var371 = FragmentCoreRitual.DRAW_COVER2_AT;
                            if (var289 == var371) {
                                FragmentCoreRitual.this.shaderFlashPulse$80030838(20, 32, 1670127537);
                            }

                            int var373 = FragmentCoreRitual.DRAW_LASTBURST_AT;
                            if (var289 == var373) {
                                FragmentCoreRitual var119 = FragmentCoreRitual.this;
                                World var375 = var12;
                                Location var515 = this.drawTo;
                                Color var603 = this.drawTint;
                                double var658 = (double)var289 * 0.06;
                                var119.gemGustBurst(var375, var515, var603, var658, 2145733671);
                            }

                            int var376 = FragmentCoreRitual.DRAW_SHARDS_AT;
                            if (var289 == var376) {
                                FragmentCoreRitual var122 = FragmentCoreRitual.this;
                                World var378 = var12;
                                Location var517 = this.drawTo;
                                List var605 = this.shards;
                                int var662 = FragmentCoreRitual.this.shaderGemId;
                                var122.spawnShards(var378, var517, var605, var662, 1225313967);
                                FragmentCoreRitual var380 = FragmentCoreRitual.this;
                                Location var519 = this.drawTo;
                                double var381 = var380.floorBelow(var519, 12.0, 2073049845);
                                this.shardFloorY = var381;
                            }

                            int var383 = FragmentCoreRitual.DRAW_SHARDS_AT;
                            if (var289 > var383) {
                                int var384 = FragmentCoreRitual.DRAW_COVER3_AT;
                                if (var289 <= var384 && this.drawDust != null) {
                                    FragmentCoreRitual var129 = FragmentCoreRitual.this;
                                    World var386 = var12;
                                    List var521 = this.shards;
                                    int var663 = FragmentCoreRitual.DRAW_SHARDS_AT;
                                    int var609 = var289 - var663;
                                    Location var665 = this.drawTo;
                                    double var725 = this.shardFloorY;
                                    DustOptions var794 = this.drawDust;
                                    var129.fallShards(var386, var521, var609, var665, var725, var794, 1, 1158341553);
                                }
                            }

                            int var387 = FragmentCoreRitual.DRAW_COVER3_AT;
                            if (var289 == var387) {
                                FragmentCoreRitual.this.shaderFlashPulse$80030838(20, 32, 1670127537);
                                FragmentCoreRitual var134 = FragmentCoreRitual.this;
                                List var390 = this.shards;
                                var134.releaseShards(var390, 2055451716);
                            }

                            int var391 = FragmentCoreRitual.DRAW_HANDOFF_AT;
                            if (var289 == var391) {
                                if (this.drawColor != null) {
                                    FragmentCoreRitual var139 = FragmentCoreRitual.this;
                                    int var525 = this.drawColor[0];
                                    int var613 = this.drawColor[1];
                                    int var669 = this.drawColor[2];
                                    var139.screenFlashEntity(var11, var525, var613, var669, 0, 26, 455878892);
                                    FragmentCoreRitual var141 = FragmentCoreRitual.this;
                                    Location var394 = var11.getLocation();
                                    int var616 = this.drawColor[0];
                                    int var673 = this.drawColor[1];
                                    int var732 = this.drawColor[2];
                                    var141.screenFlashAudience(var394, var11, var616, var673, var732, 0, 26, 1080796111);
                                }

                                FragmentCoreRitual.this.shaderRelease(990029622);
                                this.released = true;
                                FragmentCoreRitual var146 = FragmentCoreRitual.this;
                                UUID var397 = this.draw;
                                var146.removeDisplay(var397, 633746888);
                                PlayerDataStore var150 = FragmentCoreRitual.this.plugin.players(1969902410);
                                UUID var399 = var1;
                                PlayerData var825 = var150.get(var399, 1264010596);
                                if (var825 != null) {
                                    GemType var154 = var825.gem(1413186337);
                                    GemType var400 = GemType.NONE;
                                    if (var154 != var400) {
                                        ItemStack var17 = GoldGem.create(485535734);
                                        GemType var402 = var825.gem(1413186337);
                                        GoldGem.addGem(var17, var402, 876666177);
                                        var12x.setItemStack(var17);
                                    }
                                }

                                Location var406 = var12x.getLocation().clone();
                                this.goldFrom = var406;
                            }

                            if (this.goldFrom != null) {
                                int var407 = FragmentCoreRitual.DRAW_HANDOFF_AT;
                                if (var289 > var407) {
                                    int var408 = FragmentCoreRitual.DRAW_END_AT;
                                    if (var289 < var408) {
                                        int var409 = FragmentCoreRitual.DRAW_HANDOFF_AT;
                                        double var165 = (double)(var289 - var409);
                                        double var528 = (double)FragmentCoreRitual.GOLD_DESCENT_TICKS;
                                        double var826 = var165 / var528;
                                        double var171 = var826 * var826;
                                        double var676 = 2.0 * var826;
                                        double var534 = 3.0 - var676;
                                        double var831 = var171 * var534;
                                        Location var411 = this.goldFrom;
                                        Location var539 = var11.getLocation().clone().add(0.0, 1.0, 0.0);
                                        Location var412 = FragmentCoreRitual.lerp(var411, var539, var831, 1628880772);
                                        var12x.teleport(var412);
                                    }
                                }
                            }

                            int var413 = FragmentCoreRitual.DRAW_END_AT;
                            if (var289 == var413) {
                                FragmentCoreRitual var212 = FragmentCoreRitual.this;
                                var212.grantGoldGem(var11, 1740366147);
                                FragmentCoreRitual var214 = FragmentCoreRitual.this;
                                World var442 = var12;
                                Location var556 = var3;
                                var214.lightPulseEncore(var442, var556, 1929208943);
                                FragmentCoreRitual var216 = FragmentCoreRitual.this;
                                UUID var444 = var2;
                                var216.removeDisplay(var444, 633746888);
                                Map var219 = FragmentCoreRitual.this.states;
                                UUID var446 = var1;
                                FragmentCoreRitual.State var829 = (FragmentCoreRitual.State)var219.get(var446);
                                if (var829 != null) {
                                    var829.finale = false;
                                    FragmentCoreRitual var225 = FragmentCoreRitual.this;
                                    var225.clearDisplays(var829, 186841448);
                                }

                                Map var228 = FragmentCoreRitual.this.states;
                                UUID var450 = var1;
                                var228.remove(var450);
                                if (!this.released) {
                                    FragmentCoreRitual.this.shaderRelease(990029622);
                                }

                                this.cancel();
                                return;
                            }
                        }

                        if (!this.released) {
                            int var415 = this.timer - 1;
                            this.timer = var415;
                            if (var415 <= 0) {
                                int var416 = this.beat;
                                int var541 = var416 + 1;
                                this.beat = var541;
                                int var418 = FragmentCoreRitual.AFTERMATH_BAR_RINGS.length;
                                int var828 = var416 % var418;
                                int var830 = FragmentCoreRitual.slow(4, 1516685867);
                                int var186 = this.t;
                                int var420 = this.lastPulse;
                                if (var186 - var420 < 14) {
                                    int var429 = (int)Math.round((double)var830 * 0.65);
                                    var830 = Math.max(2, var429);
                                }

                                this.timer = var830;
                                FragmentCoreRitual var192 = FragmentCoreRitual.this;
                                World var432 = var12;
                                Location var543 = var3;
                                var192.flash(var432, var543, 1, 0.0, 0.0, 0.0, 2059459588);
                                int[] var193 = FragmentCoreRitual.AFTERMATH_BAR_RINGS;
                                int var833 = var193[var828];
                                ThreadLocalRandom var19 = ThreadLocalRandom.current();

                                for (int var20 = 0; var20 < var833; var20++) {
                                    Particle[] var198 = FragmentCoreRitual.AFTERMATH_MIX;
                                    int var544 = this.mix;
                                    int var625 = var544 + 1;
                                    this.mix = var625;
                                    int var546 = FragmentCoreRitual.AFTERMATH_MIX.length;
                                    int var436 = var544 % var546;
                                    Particle var21 = var198[var436];
                                    double var22 = var19.nextDouble();
                                    double var551 = var22 * var22 * 1.2000000000000002;
                                    double var24 = 1.0 + var551;
                                    Particle var437 = Particle.SCULK_CHARGE;
                                    byte var208;
                                    if (var21 == var437) {
                                        var208 = 45;
                                    } else {
                                        var208 = 30;
                                    }

                                    FragmentCoreRitual var210 = FragmentCoreRitual.this;
                                    World var439 = var12;
                                    Location var554 = var3;
                                    double var767 = (double)var208;
                                    double var817 = 0.4 * var24;
                                    double var808 = 0.6 + var817;
                                    int var771 = (int)(var767 * var808);
                                    double var812 = 0.55 * var24;
                                    var210.angledBurst(var439, var554, false, 45.0, var771, var21, var812, 1595198679);
                                }
                            }
                        }

                        return;
                    }
                }

                if (var11 != null && var11.isOnline() && this.drawColor != null && !this.released) {
                    FragmentCoreRitual var258 = FragmentCoreRitual.this;
                    int var570 = this.drawColor[0];
                    int var638 = this.drawColor[1];
                    int var703 = this.drawColor[2];
                    var258.screenFlashEntity(var11, var570, var638, var703, 0, 26, 455878892);
                    FragmentCoreRitual var260 = FragmentCoreRitual.this;
                    Location var472 = var11.getLocation();
                    int var641 = this.drawColor[0];
                    int var707 = this.drawColor[1];
                    int var740 = this.drawColor[2];
                    var260.screenFlashAudience(var472, var11, var641, var707, var740, 0, 26, 1080796111);
                }

                FragmentCoreRitual var262 = FragmentCoreRitual.this;
                UUID var474 = var2;
                var262.removeDisplay(var474, 633746888);
                FragmentCoreRitual var264 = FragmentCoreRitual.this;
                UUID var476 = this.draw;
                var264.removeDisplay(var476, 633746888);
                FragmentCoreRitual var266 = FragmentCoreRitual.this;
                List var478 = this.shards;
                var266.releaseShards(var478, 2055451716);
                FragmentCoreRitual var268 = FragmentCoreRitual.this;
                var268.payOwed(var11, 1245631054);
                Map var271 = FragmentCoreRitual.this.states;
                UUID var481 = var1;
                FragmentCoreRitual.State var822 = (FragmentCoreRitual.State)var271.get(var481);
                if (var822 != null) {
                    var822.finale = false;
                    FragmentCoreRitual var277 = FragmentCoreRitual.this;
                    var277.clearDisplays(var822, 186841448);
                }

                Map var280 = FragmentCoreRitual.this.states;
                UUID var485 = var1;
                var280.remove(var485);
                FragmentCoreRitual var283 = FragmentCoreRitual.this;
                World var487 = var12;
                var283.ritualSoundStop(var487, 425244914);
                if (!this.released) {
                    FragmentCoreRitual.this.shaderRelease(990029622);
                }

                this.cancel();
            }
        };
        BlissPlugin var18 = this.plugin;
        var15.runTaskTimer(var18, 0L, 1L);
    }

    private File owedFile(int var1) {
        File var6 = this.plugin.getDataFolder();
        String var4 = "ritual_owed.yml";
        return new File(var6, var4);
    }

    private void owedLoad(int var1) {
        File var4 = this.owedFile(36046238);
        if (var4.exists()) {
            YamlConfiguration var5 = YamlConfiguration.loadConfiguration(var4);

            for (String var7 : var5.getStringList("core")) {
                try {
                    Set var23 = this.owedCore;
                    UUID var47 = UUID.fromString(var7);
                    var23.add(var47);
                } catch (IllegalArgumentException var57) {
                }
            }

            for (String var55 : var5.getStringList("gem")) {
                try {
                    Set var35 = this.gemLifted;
                    UUID var50 = UUID.fromString(var55);
                    var35.add(var50);
                } catch (IllegalArgumentException var56) {
                }
            }
        }

        for (Player var54 : Bukkit.getOnlinePlayers()) {
            this.payOwed(var54, 1245631054);
        }
    }

    private void owedSave(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        ArrayList var9 = new ArrayList();
        ArrayList var5 = var9;

        for (UUID var7 : this.owedCore) {
            String var39 = var7.toString();
            var5.add(var39);
        }

        ArrayList var20 = new ArrayList();
        ArrayList var50 = var20;

        for (UUID var8 : this.gemLifted) {
            String var48 = var8.toString();
            var50.add(var48);
        }

        String var40 = "core";
        var2.set(var40, var5);
        String var41 = "gem";
        var2.set(var41, var50);

        try {
            File var43 = this.owedFile(36046238);
            AtomicYml.save(var4, var43, 1347473142);
        } catch (IOException var53) {
            Logger var32 = this.plugin.getLogger();
            String var46 = "Could not save ritual_owed.yml: " + var53.getMessage();
            var32.warning(var46);
        }
    }

    private void payOwed(Player var1, int var2) {
        if (var1 != null && var1.isOnline()) {
            if (!var1.isDead()) {
                Set var14 = this.owedCore;
                UUID var28 = var1.getUniqueId();
                boolean var7 = var14.remove(var28);
                Set var17 = this.gemLifted;
                UUID var30 = var1.getUniqueId();
                boolean var8 = var17.remove(var30);
                if (var7 || var8) {
                    if (var7) {
                        ItemStack var31 = RitualItems.fragmentCore(1868423911);
                        BlissCoreItems.giveOrDrop(var1, var31, 41085121);
                    }

                    if (var8) {
                        BlissCoreItems var26 = this.plugin.coreItems(401009135);
                        PlayerDataStore var34 = this.plugin.players(1969902410);
                        UUID var36 = var1.getUniqueId();
                        PlayerData var35 = var34.get(var36, 1264010596);
                        var26.refreshGemItem(var1, var35, 1088402172);
                    }

                    this.owedSave(1489154183);
                }
            }
        }
    }

    @EventHandler
    public void onJoinSettleOwed(PlayerJoinEvent var1) {
        Player var4 = var1.getPlayer();
        this.payOwed(var4, 1245631054);
    }

    @EventHandler
    public void onRespawnSettleOwed(PlayerRespawnEvent var1) {
        Player var6 = var1.getPlayer();
        BukkitScheduler var8 = Bukkit.getScheduler();
        BlissPlugin var10 = this.plugin;
        Runnable var11 = () -> this.payOwed(var6, 1245631054);
        var8.runTaskLater(var10, var11, 1L);
    }

    private FragmentCoreRitual.OrbitEgg newOrbitEgg(int var1) {
        ThreadLocalRandom var13 = ThreadLocalRandom.current();
        double var14 = var13.nextDouble() * (Math.PI * 2);
        double var65 = var13.nextDouble();
        double var16 = Math.acos(2.0 * var65 - 1.0);
        double var71 = Math.sin(var16);
        double var91 = Math.cos(var14);
        double var73 = var71 * var91;
        double var95 = Math.sin(var16);
        double var110 = Math.sin(var14);
        double var97 = var95 * var110;
        double var114 = Math.cos(var16);
        Vector var34 = new Vector(var73, var97, var114);
        Vector var47;
        if (Math.abs(var34.getX()) < 0.9) {
            var47 = new Vector(1, 0, 0);
        } else {
            var47 = new Vector(0, 1, 0);
        }

        FragmentCoreRitual.OrbitEgg var41 = new FragmentCoreRitual.OrbitEgg();
        Vector var49 = var34.clone();
        Vector var51 = var49.crossProduct(var47).normalize();
        var41.u = var51;
        Vector var53 = var34.clone();
        Vector var80 = var41.u;
        Vector var55 = var53.crossProduct(var80).normalize();
        var41.v = var55;
        double var103 = var13.nextDouble() * 0.5;
        double var84 = 0.75 + var103;
        double var58 = 5.2 * var84;
        var41.rU = var58;
        double var108 = var13.nextDouble() * 0.5;
        double var88 = 0.75 + var108;
        double var62 = 5.2 * var88;
        var41.rV = var62;
        return var41;
    }

    void orbitEggs(World var1, Location var2, FragmentCoreRitual.OrbitEgg[] var3, boolean var4, int var5) {
        int var24 = 0;

        while (true) {
            int var92 = var3.length;
            if (var24 >= var92) {
                return;
            }

            FragmentCoreRitual.OrbitEgg var25 = var3[var24];
            if (var25 == null) {
                var25 = this.newOrbitEgg(1131252892);
                var3[var24] = var25;
            }

            if (var25.rest > 0) {
                int var96 = var25.rest - 1;
                var25.rest = var96;
            } else {
                double var51 = (double)var24;
                double var179 = (double)var3.length;
                double var133 = (Math.PI * 2) / var179;
                double var26 = var51 * var133;
                Location var56 = var2.clone();
                double var101 = Math.cos(var26) * 0.0;
                double var193 = Math.sin(var26) * 0.0;
                Location var28 = var56.add(var101, 0.0, var193);
                double var29 = var25.head;
                double var105 = var25.head + 0.03;
                var25.head = var105;
                double var108 = var25.head;
                int var116 = (int)Math.ceil((var108 - var29) * 20.0);
                int var31 = Math.max(1, var116);

                for (int var32 = 1; var32 <= var31; var32++) {
                    double var139 = var25.head;
                    double var141 = var139 - var29;
                    double var184 = (double)var32;
                    double var224 = (double)var31;
                    double var186 = var184 / var224;
                    double var143 = var141 * var186;
                    double var33 = var29 + var143;
                    double var35 = Math.cos(var33);
                    double var37 = Math.sin(var33);
                    Location var87 = var28.clone();
                    double var156 = var25.rU;
                    double var122 = var35 * var156;
                    double var160 = var25.u.getX();
                    double var124 = var122 * var160;
                    double var196 = var25.rV;
                    double var164 = var37 * var196;
                    double var200 = var25.v.getX();
                    double var166 = var164 * var200;
                    double var126 = var124 + var166;
                    double var203 = var25.rU;
                    double var170 = var35 * var203;
                    double var207 = var25.u.getY();
                    double var172 = var170 * var207;
                    double var229 = var25.rV;
                    double var211 = var37 * var229;
                    double var233 = var25.v.getY();
                    double var213 = var211 * var233;
                    double var174 = var172 + var213;
                    double var236 = var25.rU;
                    double var217 = var35 * var236;
                    double var240 = var25.u.getZ();
                    double var219 = var217 * var240;
                    double var248 = var25.rV;
                    double var244 = var37 * var248;
                    double var252 = var25.v.getZ();
                    double var246 = var244 * var252;
                    double var221 = var219 + var246;
                    Location var39 = var87.add(var126, var174, var221);
                    if (var4) {
                        Particle var129 = Particle.END_ROD;
                        Particles.spawn(var1, var129, var39, 0, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
                    } else {
                        Color var188 = HOLDER_GOLD;
                        this.gemStar(var1, var39, var39, var188, 66, 49641728);
                    }
                }

                if (var25.head >= Math.PI * 2) {
                    FragmentCoreRitual.OrbitEgg var254 = this.newOrbitEgg(1131252892);
                    var254.rest = 20;
                    var3[var24] = var254;
                }
            }

            var24++;
        }
    }

    private UUID spawnTriggerGem(Player var1, Location var2, int var3) {
        PlayerDataStore var14 = this.plugin.players(1969902410);
        UUID var43 = var1.getUniqueId();
        PlayerData var9 = var14.get(var43, 1264010596);
        if (var9 != null) {
            GemType var18 = var9.gem(1413186337);
            GemType var44 = GemType.NONE;
            if (var18 != var44) {
                ItemStack var10;
                try {
                    BlissCoreItems var22 = this.plugin.coreItems(401009135);
                    GemType var46 = var9.gem(1413186337);
                    boolean var62 = var9.tier2(629984660);
                    int var67 = var9.energy(1160853318);
                    var10 = var22.gemItem(var46, var62, var67, 256965471);
                } catch (RuntimeException var73) {
                    return null;
                }

                TraderListener.clearGemItems(var1, 579055745);
                Set var26 = this.gemLifted;
                UUID var48 = var1.getUniqueId();
                var26.add(var48);
                this.owedSave(1489154183);
                ItemDisplay var11 = this.spawnDisplay(var2, var10, 1.0F, 1550711431);
                Billboard var50 = Billboard.FIXED;
                var11.setBillboard(var50);
                Brightness var51 = new Brightness(15, 15);
                var11.setBrightness(var51);
                int var54 = GoldGem.bit(var9.gem(1413186337), 880748148);
                int[] var12 = this.gemBaseColor(var54, 1417188842);
                var11.setGlowing(true);
                int var57 = var12[0];
                int var66 = var12[1];
                int var72 = var12[2];
                Color var58 = Color.fromRGB(var57, var66, var72);
                var11.setGlowColorOverride(var58);
                int var61 = GoldGem.bit(var9.gem(1413186337), 880748148);
                this.shaderGemId = var61;
                this.shaderApply(2039620520);
                return var11.getUniqueId();
            }
        }

        return null;
    }

    private ItemStack brokenGemItem(Player var1, int var2) {
        PlayerDataStore var7 = this.plugin.players(1969902410);
        UUID var17 = var1.getUniqueId();
        PlayerData var5 = var7.get(var17, 1264010596);
        if (var5 != null) {
            GemType var11 = var5.gem(1413186337);
            GemType var18 = GemType.NONE;
            if (var11 != var18) {
                return RitualItems.gemBroken(GoldGem.bit(var5.gem(1413186337), 880748148), 1044097340);
            }
        }

        return null;
    }

    void spawnShards(World var1, Location var2, List<UUID> var3, int var4, int var5) {
        for (int var12 = 0; var12 < 3; var12++) {
            Location var23 = var2.clone();
            int var30 = var12 + 1;
            ItemStack var29 = RitualItems.gemShard(var4, var30, 73604784);
            Item var13 = var1.dropItem(var23, var29);
            Vector var24 = new Vector(0, 0, 0);
            var13.setVelocity(var24);
            var13.setGravity(false);
            var13.setPickupDelay(Integer.MAX_VALUE);
            UUID var28 = var13.getUniqueId();
            var3.add(var28);
        }
    }

    void fallShards(World var1, List<UUID> var2, int var3, Location var4, double var5, DustOptions var7, int var8, int var9) {
        int var25 = var3;
        int var26 = 0;

        while (true) {
            int var59 = var2.size();
            if (var26 >= var59) {
                return;
            }

            if (Bukkit.getEntity((UUID)var2.get(var26)) instanceof Item var28) {
                Location var29 = this.shardAt(var1, var4, var26, var3, var5, 439620830);
                int var87 = Math.max(1, var8);
                int var82 = var3 + var87;
                Location var30 = this.shardAt(var1, var4, var26, var82, var5, 439620830);
                Location var47 = var28.getLocation();
                if (var47.distanceSquared(var29) > 0.12249999999999998) {
                    var28.teleport(var29);
                }

                Vector var66 = var30.toVector();
                Vector var75 = var29.toVector();
                Vector var67 = var66.subtract(var75);
                var28.setVelocity(var67);

                for (int var31 = 0; var31 <= 10 && var25 - var31 >= 0; var31++) {
                    Particle var70 = Particle.DUST;
                    int var92 = var25 - var31;
                    Location var77 = this.shardAt(var1, var4, var26, var92, var5, 439620830);
                    Particles.spawn(var1, var70, var77, 1, 0.0, 0.0, 0.0, 0.0, var7, true, 1748031581);
                }
            }

            var26++;
        }
    }

    double floorBelow(Location var1, double var2, int var4) {
        World var10 = var1.getWorld();
        int var11 = var1.getBlockX();
        int var12 = var1.getBlockZ();
        int var13 = var1.getBlockY();

        while (true) {
            int var39 = var1.getBlockY();
            int var44 = (int)var2;
            int var40 = var39 - var44;
            if (var13 < var40) {
                break;
            }

            int var42 = var10.getMinHeight();
            if (var13 < var42) {
                break;
            }

            if (var10.getBlockAt(var11, var13, var12).getType().isSolid()) {
                return (double)var13 + 1.0 + 0.05;
            }

            var13 += -1;
        }

        double var24 = var1.getY();
        return var24 - var2;
    }

    private Location shardAt(World var1, Location var2, int var3, int var4, double var5, int var7) {
        double var51 = (double)var4 / 90.0;
        double var73 = Math.min(1.0, var51);
        double var57 = Math.pow(1.0 - var73, 2.2);
        double var25 = (1.0 - var57) * 4.0;
        double var27 = (double)var3 * (Math.PI * 2.0 / 3.0);
        double var64 = var2.getY();
        double var87 = (double)var4;
        double var79 = 0.11 * var87;
        double var66 = var64 - var79;
        double var29 = Math.max(var5, var66);
        double var69 = var2.getX();
        double var81 = Math.cos(var27);
        double var83 = var81 * var25;
        double var71 = var69 + var83;
        double var90 = var2.getZ();
        double var94 = Math.sin(var27);
        double var96 = var94 * var25;
        double var92 = var90 + var96;
        return new Location(var1, var71, var29, var92);
    }

    void releaseShards(List<UUID> var1, int var2) {
        Iterator var5 = var1.iterator();

        while (var5.hasNext()) {
            if (Bukkit.getEntity((UUID)var5.next()) instanceof Item var8) {
                var8.setGravity(true);
                var8.setPickupDelay(0);
            }
        }
    }

    private void grantGoldGem(Player var1, int var2) {
        ItemStack var6 = GoldGem.create(485535734);
        GoldGemManager var9 = this.plugin.goldGemManager(256518991);
        var9.becomeHolder(var1, var6, 2022822655);
        Set var11 = this.owedCore;
        UUID var24 = var1.getUniqueId();
        var11.remove(var24);
        Set var14 = this.gemLifted;
        UUID var26 = var1.getUniqueId();
        var14.remove(var26);
        this.owedSave(1489154183);
        if (var1.isDead()) {
            GoldGemManager var22 = this.plugin.goldGemManager(256518991);
            UUID var29 = var1.getUniqueId();
            GemType var31 = GoldGem.getOwn(var6, 445527790);
            var22.setSoulboundPending(var29, var31, 1622739586);
        } else {
            BlissCoreItems.giveOrDrop(var1, var6, 41085121);
        }
    }

    private void absorbScatter(World var1, Location var2, int var3) {
        ThreadLocalRandom var27 = ThreadLocalRandom.current();

        for (int var28 = 0; var28 < 26; var28++) {
            double var29 = var27.nextDouble() * (Math.PI * 2);
            double var31 = var27.nextDouble() * 2.0 - 1.0;
            double var137 = var31 * var31;
            double var110 = 1.0 - var137;
            double var33 = Math.sqrt(Math.max(0.0, var110));
            double var114 = Math.cos(var29);
            double var35 = var33 * var114;
            double var37 = var31;
            double var118 = Math.sin(var29);
            double var39 = var33 * var118;
            double var123 = var27.nextDouble() * 1.8499999999999999;
            double var41 = 0.55 + var123;
            int var43 = var27.nextInt(3);
            if (var43 == 0) {
                Particle var97 = Particle.SOUL;
                double var126 = var2.getX();
                double var142 = var2.getY();
                double var153 = var2.getZ();
                Particles.spawn(var1, var97, var126, var142, var153, 0, var35, var31, var39, var41, null, true, 954482185);
            } else if (var43 == 1) {
                for (byte var182 = 0; var182 < 2; var182 += 1) {
                    Particle var103 = Particle.SCULK_CHARGE;
                    double var133 = var2.getX();
                    double var150 = var2.getY();
                    double var162 = var2.getZ();
                    Float var180 = 0.0F;
                    Particles.spawn(var1, var103, var133, var150, var162, 0, var35, var37, var39, var41, var180, true, 954482185);
                }
            } else {
                int var100 = var27.nextInt(9);
                int var44 = 20 + var100;
                double var45 = var41 * 11.1;
                Location var135 = var2.clone();
                double var146 = var35 * var45;
                double var159 = var31 * var45;
                double var168 = var39 * var45;
                Location var136 = var135.add(var146, var159, var168);
                Color var148 = HOLDER_GOLD;
                this.gemStar(var1, var2, var136, var148, var44, 49641728);
            }
        }
    }

    private void harmlessHit(Player var1, int var2) {
        Location var16 = var1.getLocation();
        Sound var5 = Sound.ENTITY_PLAYER_HURT;
        SoundCategory var6 = SoundCategory.PLAYERS;
        var1.playSound(var16, var5, var6, 1.0F, 1.0F);
        if (FragmentCoreRitual.CamStreamer.nudge(var1, 0.0F, 2.4F, 2070054010)) {
            final UUID var9 = var1.getUniqueId();
            BukkitRunnable var14 = new BukkitRunnable() {
                int k = 0;

                public void run() {
                    Player var4 = Bukkit.getPlayer(var9);
                    if (var4 != null && var4.isOnline() && this.k < 4) {
                        FragmentCoreRitual.CamStreamer.nudge(var4, 0.0F, -0.6F, 2070054010);
                        int var18 = this.k + 1;
                        this.k = var18;
                    } else {
                        this.cancel();
                    }
                }
            };
            BlissPlugin var19 = this.plugin;
            var14.runTaskTimer(var19, 1L, 1L);
        }
    }

    private void lightPulse(World var1, Location var2, int var3) {
        this.flash(var1, var2, 3, 0.25, 0.25, 0.25, 2059459588);
        this.lightPulseScatter(var1, var2, 1411882735);

        for (Entity var15 : var1.getNearbyEntities(var2, 12.0, 12.0, 12.0)) {
            if (var15 instanceof LivingEntity) {
                ((LivingEntity)var15).playHurtAnimation(0.0F);
            }

            if (var15 instanceof Player var47) {
                this.harmlessHit(var47, 711362634);
            }
        }
    }

    private void lightPulseEncore(World var1, Location var2, int var3) {
        for (int var9 = 1; var9 <= 2; var9++) {
            long var21 = (long)var9;
            long var10 = 40L * var21;
            BukkitScheduler var17 = Bukkit.getScheduler();
            BlissPlugin var20 = this.plugin;
            Runnable var24 = () -> {
                if (var2.getWorld() != null && var2.getChunk().isLoaded()) {
                    this.lightPulse(var1, var2, 777185327);
                }
            };
            var17.runTaskLater(var20, var24, var10);
        }
    }

    private void lightPulseScatter(World var1, Location var2, int var3) {
        ThreadLocalRandom var23 = ThreadLocalRandom.current();

        for (int var24 = 0; var24 < 120; var24++) {
            double var25 = var23.nextDouble() * (Math.PI * 2);
            double var27 = var23.nextDouble() * 2.0 - 1.0;
            double var120 = var27 * var27;
            double var96 = 1.0 - var120;
            double var29 = Math.sqrt(Math.max(0.0, var96));
            double var100 = Math.cos(var25);
            double var31 = var29 * var100;
            double var33 = var27;
            double var104 = Math.sin(var25);
            double var35 = var29 * var104;
            double var109 = var23.nextDouble() * 1.8499999999999999;
            double var37 = 0.55 + var109;
            int var39 = var23.nextInt(3);
            if (var39 == 0) {
                Particle var89 = Particle.END_ROD;
                double var118 = var2.getX();
                double var131 = var2.getY();
                double var140 = var2.getZ();
                Particles.spawn(var1, var89, var118, var131, var140, 0, var31, var27, var35, var37, null, true, 954482185);
            } else if (var39 == 1) {
                Particle var88 = Particle.SOUL;
                double var115 = var2.getX();
                double var128 = var2.getY();
                double var137 = var2.getZ();
                double var158 = var37 * 0.7;
                Particles.spawn(var1, var88, var115, var128, var137, 0, var31, var27, var35, var158, null, true, 954482185);
            } else {
                for (int var40 = 0; var40 < 2; var40++) {
                    Particle var87 = Particle.SCULK_CHARGE;
                    double var112 = var2.getX();
                    double var125 = var2.getY();
                    double var134 = var2.getZ();
                    Float var162 = 0.0F;
                    Particles.spawn(var1, var87, var112, var125, var134, 0, var31, var33, var35, var37, var162, true, 954482185);
                }
            }
        }
    }

    private Location aroundSphere(Location var1, double var2, double var4, int var6) {
        ThreadLocalRandom var22 = ThreadLocalRandom.current();
        double var23 = var22.nextDouble() * (Math.PI * 2);
        double var25 = var22.nextDouble() * 2.0 - 1.0;
        double var81 = var25 * var25;
        double var66 = 1.0 - var81;
        double var27 = Math.sqrt(Math.max(0.0, var66));
        double var69 = var22.nextDouble();
        double var85 = var4 - var2;
        double var71 = var69 * var85;
        double var29 = var2 + var71;
        Location var54 = var1.clone();
        double var73 = Math.cos(var23);
        double var56 = var27 * var73;
        double var58 = var56 * var29;
        double var79 = var25 * var29;
        double var95 = Math.sin(var23);
        double var89 = var27 * var95;
        double var91 = var89 * var29;
        return var54.add(var58, var79, var91);
    }

    private void gemStar(World var1, Location var2, Location var3, Color var4, int var5, int var6) {
        if (var4 != null) {
            Particle var8 = Particle.TRAIL;
            Trail var19 = new Trail(var3, var4, var5);
            Particles.spawn(var1, var8, var2, 1, 0.0, 0.0, 0.0, 0.0, var19, true, 1748031581);
        }
    }

    private void drawTrail(World var1, Location var2, Color var3, int var4) {
        if (var3 != null) {
            for (int var13 = 0; var13 < 3; var13++) {
                Location var14 = this.aroundSphere(var2, 0.35, 0.6, 499791509);
                Location var21 = var14.clone();
                Location var26 = var14.clone();
                Vector var30 = var26.subtract(var2).toVector().normalize().multiply(0.25);
                Location var15 = var21.add(var30);
                this.gemStar(var1, var14, var15, var3, 18, 49641728);
            }
        }
    }

    private void gemHalo(World var1, Location var2, Color var3, int var4) {
        if (var3 != null) {
            Location var13 = this.aroundSphere(var2, 0.7, 1.05, 499791509);
            Location var18 = var13.clone();
            Location var22 = var13.clone();
            Vector var26 = var22.subtract(var2).toVector().normalize().multiply(0.2);
            Location var14 = var18.add(var26);
            this.gemStar(var1, var13, var14, var3, 18, 49641728);
        }
    }

    private void gemGustBurst(World var1, Location var2, Color var3, double var4, int var6) {
        if (var3 != null) {
            Color var18 = this.darken(var3, 0.55, 994326318);
            this.gemGustRing(var1, var2, var18, 0.26, var4, 1207227329);
            double var32 = var4 + 0.4;
            this.gemGustRing(var1, var2, var18, 0.13, var32, 1207227329);
        }
    }

    private Color darken(Color var1, double var2, int var4) {
        double var14 = (double)var1.getRed();
        int var20 = (int)Math.round(var14 * var2);
        double var23 = (double)var1.getGreen();
        int var29 = (int)Math.round(var23 * var2);
        double var32 = (double)var1.getBlue();
        int var38 = (int)Math.round(var32 * var2);
        return Color.fromRGB(var20, var29, var38);
    }

    private void gemGustRing(World var1, Location var2, Color var3, double var4, double var6, int var8) {
        ThreadLocalRandom var19 = ThreadLocalRandom.current();

        for (int var22 = 0; var22 < 20; var22++) {
            double var71 = (double)var22;
            double var56 = (Math.PI / 10) * var71;
            double var23 = var6 + var56;
            int var48 = var19.nextInt(9);
            int var25 = 20 + var48;
            double var61 = (double)(var25 - 1);
            double var26 = var4 * var61;
            Location var44 = var2.clone();
            double var51 = Math.cos(var23);
            double var53 = var51 * var26;
            double var68 = 0.3 * var26;
            double var76 = Math.sin(var23);
            double var78 = var76 * var26;
            Location var28 = var44.add(var53, var68, var78);
            this.gemStar(var1, var2, var28, var3, var25, 49641728);
        }
    }

    private void lightningBolt(World var1, Location var2, int var3) {
        ThreadLocalRandom var22 = ThreadLocalRandom.current();
        double var137 = Math.sqrt(var22.nextDouble());
        double var23 = 15.600000000000001 * var137;
        double var25 = var22.nextDouble() * (Math.PI * 2);
        double var63 = var2.getX();
        double var143 = Math.cos(var25);
        double var145 = var143 * var23;
        double var27 = var63 + var145;
        double var68 = var2.getZ();
        double var149 = Math.sin(var25);
        double var151 = var149 * var23;
        double var29 = var68 + var151;
        double var77 = var2.getY() + 5.2 - 0.8;
        double var164 = (var22.nextDouble() * 2.0 - 1.0) * 1.6;
        double var31 = var77 + var164;
        double var35 = 0.0;
        double var37 = 0.0;

        for (int var39 = 0; var39 < 7; var39++) {
            double var40 = (var22.nextDouble() - 0.5) * 0.8;
            double var42 = (var22.nextDouble() - 0.5) * 0.8;

            for (int var44 = 0; var44 < 8; var44++) {
                double var45 = (double)var44 / 8.0;
                double var179 = (double)var39 * 0.9285714285714286;
                double var116 = var31 + var179;
                double var183 = var45 * 0.9285714285714286;
                double var47 = var116 + var183;
                double var122 = var27 + var35;
                double var189 = var40 - var35;
                double var191 = var189 * var45;
                double var49 = var122 + var191;
                double var128 = var29 + var37;
                double var197 = var42 - var37;
                double var199 = var197 * var45;
                double var51 = var128 + var199;
                Particle var134 = Particle.ELECTRIC_SPARK;
                Particles.spawn(var1, var134, var49, var47, var51, 1, 0.0, 0.0, 0.0, 0.0, null, true, 954482185);
            }

            var35 = var40;
            var37 = var42;
        }
    }

    int[] gemFlashColor(int var1, int var2) {
        int[] var12 = this.gemBaseColor(var1, 1417188842);

        for (int var13 = 0; var13 < 3; var13++) {
            double var22 = (double)var12[var13];
            int var34 = var12[var13];
            double var32 = (double)(255 - var34);
            double var29 = 0.25 * var32;
            int var28 = (int)Math.round(var22 + var29);
            var12[var13] = var28;
        }

        return var12;
    }

    int[] gemBaseColor(int var1, int var2) {
        switch (var1) {
            case 1:
                return new int[]{255, 64, 51};
            case 2:
                return new int[]{255, 128, 191};
            case 3:
                return new int[]{242, 247, 255};
            case 4:
                return new int[]{179, 102, 255};
            case 5:
                return new int[]{89, 230, 102};
            case 6:
                return new int[]{255, 242, 77};
            case 7:
                return new int[]{255, 140, 38};
            default:
                return new int[]{51, 230, 255};
        }
    }

    private Location arc(Location var1, Location var2, double var3, int var5) {
        Location var20 = lerp(var1, var2, 0.5, 1628880772).add(0.0, 3.0, 0.0);
        double var21 = 1.0 - var3;
        World var35 = var1.getWorld();
        double var38 = var21 * var21;
        double var49 = var1.getX();
        double var40 = var38 * var49;
        double var53 = 2.0 * var21;
        double var55 = var53 * var3;
        double var78 = var20.getX();
        double var57 = var55 * var78;
        double var42 = var40 + var57;
        double var61 = var3 * var3;
        double var83 = var2.getX();
        double var63 = var61 * var83;
        double var44 = var42 + var63;
        double var67 = var21 * var21;
        double var88 = var1.getY();
        double var69 = var67 * var88;
        double var92 = 2.0 * var21;
        double var94 = var92 * var3;
        double var117 = var20.getY();
        double var96 = var94 * var117;
        double var71 = var69 + var96;
        double var100 = var3 * var3;
        double var122 = var2.getY();
        double var102 = var100 * var122;
        double var73 = var71 + var102;
        double var106 = var21 * var21;
        double var127 = var1.getZ();
        double var108 = var106 * var127;
        double var131 = 2.0 * var21;
        double var133 = var131 * var3;
        double var146 = var20.getZ();
        double var135 = var133 * var146;
        double var110 = var108 + var135;
        double var139 = var3 * var3;
        double var151 = var2.getZ();
        double var141 = var139 * var151;
        double var112 = var110 + var141;
        return new Location(var35, var44, var73, var112);
    }

    private void placeFragments(FragmentCoreRitual.State var1, Location var2, Vector var3, Vector var4, double var5, boolean var7, int var8) {
        World var23 = var2.getWorld();

        for (int var24 = 0; var24 < 7; var24++) {
            double var71 = (double)var24 * (Math.PI * 2.0 / 7.0);
            double var25 = var5 + var71;
            Vector var38 = var3.clone();
            double var56 = Math.cos(var25) * 0.9;
            Vector var39 = var38.multiply(var56);
            Vector var59 = var4.clone();
            double var77 = Math.sin(var25) * 0.9;
            Vector var60 = var59.multiply(var77);
            Vector var27 = var39.add(var60);
            List var62 = var1.frags;
            UUID var64 = (UUID)var62.get(var24);
            ItemDisplay var28 = this.display(var64, 1415410134);
            if (var28 != null) {
                Location var45 = var2.clone();
                Location var29 = var45.add(var27);
                var28.teleport(var29);
                this.spinFragment(var28, var24, 499537219);
                if (var7) {
                    Particle var68 = Particle.DUST;
                    DustOptions var22 = GOLD_DUST_SMALL;
                    Particles.spawn(var23, var68, var29, 0, 0.0, 0.0, 0.0, 0.0, var22, 1141219473);
                }
            }
        }
    }

    private void popDisplay(ItemDisplay var1, float var2, int var3, int var4) {
        var1.setInterpolationDelay(0);
        var1.setInterpolationDuration(var3);
        Vector3f var7 = new Vector3f();
        Quaternionf var15 = new Quaternionf();
        Vector3f var16 = new Vector3f(var2, var2, var2);
        Quaternionf var17 = new Quaternionf();
        float var19 = GEM_ART_ROLL;
        Quaternionf var18 = var17.rotateZ(var19);
        Transformation var14 = new Transformation(var7, var15, var16, var18);
        var1.setTransformation(var14);
    }

    private void screenFlash(Player var1, int var2) {
        if (this.shaderHolds > 0) {
            this.shaderFlashPulse(682379073);
        } else {
            this.screenFlashEntity(var1, 255, 244, 214, 4, 12, 455878892);
        }
    }

    private void holdCover(Player var1, final int var2, final int var3, int var4) {
        final UUID var13 = var1.getUniqueId();
        Map var18 = this.covers;
        FragmentCoreRitual.Cover var14Tmp = (FragmentCoreRitual.Cover)var18.get(var13);
        if (var14Tmp != null) {
            if (var14Tmp.task != null) {
                var14Tmp.task.cancel();
            }
        } else {
            long var41 = var1.getPlayerTimeOffset();
            boolean var46 = var1.isPlayerTimeRelative();
            var14Tmp = new FragmentCoreRitual.Cover(var41, var46, 2008014521);
        }
        final FragmentCoreRitual.Cover var14 = var14Tmp;

        Map var28 = this.covers;
        var28.put(var13, var14);
        this.applyCoverDay(var1, 15, 5838947);
        BukkitRunnable var35 = new BukkitRunnable() {
            int k = 1;

            public void run() {
                Player var10 = Bukkit.getPlayer(var13);
                if (var10 != null && var10.isOnline()) {
                    int var18 = this.k;
                    int var43 = var2;
                    int var42;
                    if (var18 < var43) {
                        var42 = 15;
                    } else {
                        int var64 = this.k;
                        int var70 = var2;
                        double var66 = (double)(var64 - var70);
                        int var74 = var3;
                        int var76 = var2;
                        int var75 = var74 - var76;
                        double var72 = (double)Math.max(1, var75);
                        double var68 = var66 / var72;
                        double var60 = 1.0 - var68;
                        var42 = (int)Math.round(15.0 * var60);
                    }

                    if (var42 > 0) {
                        int var21 = this.k;
                        int var45 = var3;
                        if (var21 <= var45) {
                            FragmentCoreRitual var23 = FragmentCoreRitual.this;
                            var23.applyCoverDay(var10, var42, 5838947);
                            int var48 = this.k + 1;
                            this.k = var48;
                            return;
                        }
                    }

                    long var51 = var14.prevOffset;
                    boolean var63 = var14.prevRel;
                    var10.setPlayerTime(var51, var63);
                    Map var28 = FragmentCoreRitual.this.covers;
                    UUID var54 = var13;
                    var28.remove(var54);
                    this.cancel();
                } else {
                    Map var39 = FragmentCoreRitual.this.covers;
                    UUID var56 = var13;
                    var39.remove(var56);
                    this.cancel();
                }
            }
        };
        BlissPlugin var39 = this.plugin;
        BukkitTask var36 = var35.runTaskTimer(var39, 1L, 1L);
        var14.task = var36;
    }

    private void coversRestore(int var1) {
        Set<UUID> var35 = this.covers.keySet();

        for (UUID var6 : new ArrayList<>(var35)) {
            Map var16 = this.covers;
            FragmentCoreRitual.Cover var7 = (FragmentCoreRitual.Cover)var16.remove(var6);
            if (var7 != null) {
                if (var7.task != null) {
                    var7.task.cancel();
                }

                Player var8 = Bukkit.getPlayer(var6);
                if (var8 != null && var8.isOnline()) {
                    long var32 = var7.prevOffset;
                    boolean var36 = var7.prevRel;
                    var8.setPlayerTime(var32, var36);
                }
            }
        }
    }

    void applyCoverDay(Player var1, int var2, int var3) {
        if (this.plugin.globalState(724248306) == null || this.plugin.globalState(724248306).shaderEffects(1378906903)) {
            long var17 = (long)var2;
            long var15 = 80020L + var17;
            SignatureClock.sendKeepingHour(var1, var15, 1392304760);
        }
    }

    void screenFlashEntity(Player var1, final int var2, final int var3, final int var4, final int var5, final int var6, int var7) {
        World var19 = var1.getWorld();
        final UUID var20 = var1.getUniqueId();
        Location var21 = var1.getEyeLocation();
        Vector var49 = var21.getDirection().multiply(0.45);
        var21.add(var49);
        Consumer<TextDisplay> var61 = var3x -> {
            var3x.setPersistent(false);
            TextComponent var30 = Component.text(FLASH_FILL);
            TextColor var44 = TextColor.color(var2, var3, var4);
            Component var31 = var30.color(var44);
            var3x.text(var31);
            Billboard var32 = Billboard.CENTER;
            var3x.setBillboard(var32);
            TextAlignment var33 = TextAlignment.CENTER;
            var3x.setAlignment(var33);
            Color var35 = Color.fromARGB(255, var2, var3, var4);
            var3x.setBackgroundColor(var35);
            var3x.setSeeThrough(false);
            var3x.setShadowed(false);
            var3x.setDefaultBackground(false);
            var3x.setLineWidth(100000);
            Brightness var40 = new Brightness(15, 15);
            var3x.setBrightness(var40);
            var3x.setTeleportDuration(1);
            Vector3f var49x = new Vector3f(0.0F, -12.0F, 0.0F);
            AxisAngle4f var54 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
            Vector3f var55x = new Vector3f(4.0F, 4.0F, 4.0F);
            AxisAngle4f var57 = new AxisAngle4f(0.0F, 0.0F, 0.0F, 1.0F);
            Transformation var42 = new Transformation(var49x, var54, var55x, var57);
            var3x.setTransformation(var42);
            PersistentDataContainer var28 = var3x.getPersistentDataContainer();
            NamespacedKey var43 = KEY_DISPLAY;
            PersistentDataType var46 = PersistentDataType.BYTE;
            Byte var51 = (byte)1;
            var28.set(var43, var46, var51);
        };
        final TextDisplay var22 = (TextDisplay)var19.spawn(var21, TextDisplay.class, var61);

        for (Player var24 : Bukkit.getOnlinePlayers()) {
            UUID var45 = var24.getUniqueId();
            if (!var45.equals(var20)) {
                BlissPlugin var55 = this.plugin;
                var24.hideEntity(var55, var22);
            }
        }

        BukkitRunnable var39 = new BukkitRunnable() {
            int k = 0;

            public void run() {
                Player var11 = Bukkit.getPlayer(var20);
                if (var11 != null && var11.isOnline() && !var22.isDead()) {
                    int var23 = this.k;
                    int var45 = var6;
                    if (var23 <= var45) {
                        Location var12 = var11.getEyeLocation();
                        TextDisplay var27 = var22;
                        Vector var56 = var12.getDirection().multiply(0.45);
                        Location var47 = var12.add(var56);
                        var27.teleport(var47);
                        int var30 = this.k;
                        int var49 = var5;
                        int var44;
                        if (var30 < var49) {
                            var44 = 255;
                        } else {
                            int var69 = this.k;
                            int var75 = var5;
                            double var71 = (double)(var69 - var75);
                            int var79 = var6;
                            int var81 = var5;
                            int var80 = var79 - var81;
                            double var77 = (double)Math.max(1, var80);
                            double var73 = var71 / var77;
                            double var63 = 1.0 - var73;
                            var44 = (int)(255.0 * var63);
                        }

                        TextDisplay var32 = var22;
                        int var51 = Math.max(0, var44);
                        int var59 = var2;
                        int var66 = var3;
                        int var67 = var4;
                        Color var52 = Color.fromARGB(var51, var59, var66, var67);
                        var32.setBackgroundColor(var52);
                        int var54 = this.k + 1;
                        this.k = var54;
                        return;
                    }
                }

                if (!var22.isDead()) {
                    var22.remove();
                }

                this.cancel();
            }
        };
        BlissPlugin var52 = this.plugin;
        var39.runTaskTimer(var52, 0L, 1L);
    }

    private File clockFile(int var1) {
        File var6 = this.plugin.getDataFolder();
        String var4 = "clock.yml";
        return new File(var6, var4);
    }

    private void clockMark(World var1, int var2) {
        YamlConfiguration var3 = new YamlConfiguration();
        YamlConfiguration var5 = var3;
        String var15 = "world";
        String var23 = var1.getName();
        var3.set(var15, var23);
        String var16 = "fullTime";
        Long var27 = this.shaderPrevFull;
        var3.set(var16, var27);
        String var17 = "daylight";
        Boolean var30 = this.shaderPrevCycle;
        var3.set(var17, var30);

        try {
            File var19 = this.clockFile(1056649803);
            AtomicYml.save(var5, var19, 1347473142);
        } catch (IOException var31) {
            Logger var14 = this.plugin.getLogger();
            String var22 = "Could not save clock.yml: " + var31.getMessage();
            var14.warning(var22);
        }
    }

    private void clockUnmark(int var1) {
        File var4 = this.clockFile(1056649803);
        if (var4.exists() && !var4.delete()) {
            this.plugin.getLogger().warning("Could not delete clock.yml — the next boot will re-apply it.");
        }
    }

    private void clockRecover(int var1) {
        File var7 = this.clockFile(1056649803);
        if (var7.exists()) {
            YamlConfiguration var8 = YamlConfiguration.loadConfiguration(var7);
            String var3 = "world";
            String var4 = "";
            World var9 = Bukkit.getWorld(var8.getString(var3, var4));
            if (var9 != null) {
                GameRule var26 = GameRule.DO_DAYLIGHT_CYCLE;
                String var5 = "daylight";
                Boolean var35 = var8.getBoolean(var5, true);
                var9.setGameRule(var26, var35);
                String var36 = "fullTime";
                long var38 = var9.getFullTime();
                long var28 = var8.getLong(var36, var38);
                var9.setFullTime(var28);
                Logger var24 = this.plugin.getLogger();
                String var32 = "A Gold Gem ritual was cut short by a crash — " + var9.getName() + "'s clock and daylight cycle have been put back.";
                var24.warning(var32);
            }

            this.clockUnmark(1651664690);
        }
    }

    void shaderEngage(World var1, int var2) {
        int var17 = this.shaderHolds;
        int var28 = var17 + 1;
        this.shaderHolds = var28;
        if (var17 <= 0) {
            this.shaderWorld = var1;
            long var20 = var1.getFullTime();
            this.shaderPrevFull = var20;
            Boolean var22 = Boolean.TRUE;
            GameRule var36 = GameRule.DO_DAYLIGHT_CYCLE;
            Object var30 = var1.getGameRuleValue(var36);
            boolean var23 = var22.equals(var30);
            this.shaderPrevCycle = var23;
            this.clockMark(var1, 597871137);
            GameRule var25 = GameRule.DO_DAYLIGHT_CYCLE;
            Boolean var32 = false;
            var1.setGameRule(var25, var32);
            if (this.shaderFailsafe != null) {
                this.shaderFailsafe.cancel();
            }

            BukkitScheduler var26 = Bukkit.getScheduler();
            BlissPlugin var34 = this.plugin;
            Runnable var38 = this::shaderRestore;
            BukkitTask var27 = var26.runTaskLater(var34, var38, 3600L);
            this.shaderFailsafe = var27;
        }
    }

    void shaderPhase(int var1, int var2) {
        this.shaderCurPhase = var1;
        this.shaderApply(2039620520);
    }

    void shaderGem(int var1, int var2) {
        int var9 = Math.min(7, var1);
        int var8 = Math.max(0, var9);
        this.shaderGemId = var8;
        this.shaderApply(2039620520);
    }

    private void shaderApply(int var1) {
        if (this.shaderHolds > 0 && this.shaderWorld != null) {
            World var11 = this.shaderWorld;
            long var23 = (long)this.shaderGemId;
            long var16 = (80000L + var23) * 24000L + 16000L;
            long var31 = (long)this.shaderCurPhase;
            long var18 = var16 + var31;
            long var38 = (long)this.shaderFlashStep;
            long var35 = 8L * var38;
            long var20 = var18 + var35;
            var11.setFullTime(var20);
        }
    }

    private void shaderFlashPulse(int var1) {
        this.shaderFlashPulse$80030838(4, 12, 1670127537);
    }

    void shaderFlashPulse$80030838(int var1, int var2, int var3) {
        this.shaderFlashPulse(var1, var2, 15, 1888682035);
    }

    void shaderFlashPulse(int var1, int var2, int var3, int var4) {
        this.shaderFlashHold = var1;
        this.shaderFlashTotal = var2;
        int var30 = Math.min(15, var3);
        int var22 = Math.max(1, var30);
        this.shaderFlashPeak = var22;
        this.shaderFlashK = 0;
        int var25 = this.shaderFlashPeak;
        this.shaderFlashStep = var25;
        this.shaderApply(2039620520);
        int var27 = this.shaderFlashStep;
        this.coverGlow(var27, 1783099717);
        if (this.shaderFlashTask == null) {
            BukkitRunnable var28 = new BukkitRunnable() {
                public void run() {
                    if (FragmentCoreRitual.this.shaderHolds <= 0) {
                        FragmentCoreRitual.this.shaderFlashTask = null;
                        FragmentCoreRitual.this.coverGlowRestore(909337583);
                        this.cancel();
                    } else {
                        FragmentCoreRitual var20 = FragmentCoreRitual.this;
                        int var32 = var20.shaderFlashK + 1;
                        var20.shaderFlashK = var32;
                        FragmentCoreRitual var22 = FragmentCoreRitual.this;
                        int var47 = FragmentCoreRitual.this.shaderFlashHold;
                        int var44;
                        if (var32 < var47) {
                            var44 = FragmentCoreRitual.this.shaderFlashPeak;
                        } else {
                            double var51 = (double)FragmentCoreRitual.this.shaderFlashPeak;
                            int var66 = FragmentCoreRitual.this.shaderFlashHold;
                            double var61 = (double)(var32 - var66);
                            int var71 = FragmentCoreRitual.this.shaderFlashTotal;
                            int var74 = FragmentCoreRitual.this.shaderFlashHold;
                            int var72 = var71 - var74;
                            double var68 = (double)Math.max(1, var72);
                            double var63 = var61 / var68;
                            double var58 = 1.0 - var63;
                            int var57 = (int)Math.round(var51 * var58);
                            var44 = Math.max(0, var57);
                        }

                        var22.shaderFlashStep = var44;
                        FragmentCoreRitual.this.shaderApply(2039620520);
                        FragmentCoreRitual var26 = FragmentCoreRitual.this;
                        int var37 = FragmentCoreRitual.this.shaderFlashStep;
                        var26.coverGlow(var37, 1783099717);
                        int var40 = FragmentCoreRitual.this.shaderFlashTotal;
                        if (var32 >= var40) {
                            FragmentCoreRitual.this.shaderFlashTask = null;
                            this.cancel();
                        }
                    }
                }
            };
            BlissPlugin var32 = this.plugin;
            BukkitTask var29 = var28.runTaskTimer(var32, 1L, 1L);
            this.shaderFlashTask = var29;
        }
    }

    private void coverGlow(int var1, int var2) {
        if (var1 >= 8) {
            if (this.shaderWorld == null) {
                return;
            }

            for (ItemDisplay var7 : this.shaderWorld.getEntitiesByClass(ItemDisplay.class)) {
                PersistentDataContainer var21 = var7.getPersistentDataContainer();
                NamespacedKey var30 = KEY_DISPLAY;
                PersistentDataType var5 = PersistentDataType.BYTE;
                if (var21.has(var30, var5) && var7.isGlowing()) {
                    var7.setGlowing(false);
                    Set var27 = this.coverDimmed;
                    UUID var33 = var7.getUniqueId();
                    var27.add(var33);
                }
            }
        } else {
            this.coverGlowRestore(909337583);
        }
    }

    private void coverGlowRestore(int var1) {
        Iterator var4 = this.coverDimmed.iterator();

        while (var4.hasNext()) {
            Entity var6 = Bukkit.getEntity((UUID)var4.next());
            if (var6 instanceof ItemDisplay) {
                ((ItemDisplay)var6).setGlowing(true);
            }
        }

        this.coverDimmed.clear();
    }

    public boolean clockHeld(int var1) {
        boolean var4;
        if (this.shaderHolds > 0) {
            var4 = true;
        } else {
            var4 = false;
        }

        return var4;
    }

    public long heldPrevFull(int var1) {
        return this.shaderPrevFull;
    }

    public boolean heldPrevCycle(int var1) {
        return this.shaderPrevCycle;
    }

    public void overrideSavedClock(long var1, boolean var3, int var4) {
        this.shaderPrevFull = var1;
        this.shaderPrevCycle = var3;
        if (this.shaderWorld != null) {
            World var14 = this.shaderWorld;
            this.clockMark(var14, 597871137);
        }
    }

    void shaderRelease(int var1) {
        if (this.shaderHolds > 0) {
            int var8 = this.shaderHolds - 1;
            this.shaderHolds = var8;
            if (var8 <= 0) {
                this.shaderRestore();
            }
        }
    }

    private void shaderRestore() {
        if (this.shaderFailsafe != null) {
            this.shaderFailsafe.cancel();
            this.shaderFailsafe = null;
        }

        if (this.shaderFlashTask != null) {
            this.shaderFlashTask.cancel();
            this.shaderFlashTask = null;
        }

        this.coverGlowRestore(909337583);
        this.shaderCurPhase = 0;
        this.shaderFlashStep = 0;
        this.shaderGemId = 0;
        World var4 = this.shaderWorld;
        this.shaderWorld = null;
        this.shaderHolds = 0;
        if (var4 != null) {
            GameRule var33 = GameRule.DO_DAYLIGHT_CYCLE;
            Boolean var38 = this.shaderPrevCycle;
            var4.setGameRule(var33, var38);
            long var35 = this.shaderPrevFull;
            var4.setFullTime(var35);
        }

        this.clockUnmark(1651664690);
    }

    private void updateHalo(Player var1, FragmentCoreRitual.State var2, int var3) {
        World var15 = var1.getWorld();
        Location var68 = var1.getLocation();
        this.feetSpiral(var15, var68, 1530886565);
        Location var16 = var1.getLocation().add(0.0, 1.0, 0.0);
        this.radiateSparks(var15, var16, 2115918351);
        this.radiateWhiteGlow(var15, var16, 1996558571);
        Location var17 = var1.getEyeLocation().add(0.0, 0.6, 0.0);

        for (int var18 = 0; var18 < 7; var18++) {
            double var36 = var2.angle;
            double var74 = (double)var18 * (Math.PI * 2.0 / 7.0);
            double var19 = var36 + var74;
            Location var41 = var17.clone();
            double var60 = Math.cos(var19) * 0.72;
            double var90 = Math.sin(var19) * 0.72;
            Location var21 = var41.add(var60, 0.0, var90);
            List var63 = var2.frags;
            UUID var65 = (UUID)var63.get(var18);
            ItemDisplay var22 = this.display(var65, 1415410134);
            if (var22 != null) {
                var22.teleport(var21);
                this.spinFragment(var22, var18, 499537219);
            }
        }
    }

    private void ensureGhost(FragmentCoreRitual.State var1, int var2) {
        if (var1.center != null) {
            UUID var18 = var1.ghost;
            if (this.display(var18, 1415410134) == null) {
                Location var20 = var1.center;
                ItemStack var5 = RitualItems.fragmentCoreGhost(1056398683);
                ItemDisplay var8 = this.spawnDisplay(var20, var5, 0.85F, 1550711431);
                var8.setGlowing(true);
                Color var22 = Color.WHITE;
                var8.setGlowColorOverride(var22);
                Brightness var23 = new Brightness(15, 15);
                var8.setBrightness(var23);
                UUID var25 = var8.getUniqueId();
                var1.ghost = var25;
                return;
            }
        }
    }

    private void updateRing(Player var1, FragmentCoreRitual.State var2, int var3) {
        if (var2.center != null) {
            if (!var2.sealed) {
                if (var2.holding) {
                    this.ensureGhost(var2, 214269004);
                }

                Location var12 = var1.getLocation().add(0.0, 1.0, 0.0);
                World var78 = var1.getWorld();
                this.radiateSparks(var78, var12, 2115918351);
                World var80 = var1.getWorld();
                this.radiateWhiteGlow(var80, var12, 1996558571);
            }

            Vector var33 = var1.getEyeLocation().toVector();
            Vector var83 = var2.center.toVector();
            Vector var139 = var33.subtract(var83);
            var139.setY(0);
            if (var139.lengthSquared() < 1.0E-6) {
                Vector var41 = new Vector(1, 0, 0);
                var139 = var41;
            } else {
                var139.normalize();
            }

            Vector var43 = var139.clone();
            Vector var85 = new Vector(0, 1, 0);
            Vector var13 = var43.crossProduct(var85);
            if (var13.lengthSquared() < 1.0E-6) {
                Vector var72 = new Vector(1, 0, 0);
                var13 = var72;
            } else {
                var13.normalize();
            }

            Vector var52 = var13.clone();
            Vector var14 = var52.crossProduct(var139).normalize();

            for (int var15 = 0; var15 < 7; var15++) {
                double var58 = var2.angle;
                double var115 = (double)var15 * (Math.PI * 2.0 / 7.0);
                double var16 = var58 + var115;
                Vector var63 = var13.clone();
                double var92 = Math.cos(var16) * 0.9;
                Vector var64 = var63.multiply(var92);
                Vector var95 = var14.clone();
                double var121 = Math.sin(var16) * 0.9;
                Vector var96 = var95.multiply(var121);
                Vector var18 = var64.add(var96);
                List var98 = var2.frags;
                UUID var100 = (UUID)var98.get(var15);
                ItemDisplay var19 = this.display(var100, 1415410134);
                if (var19 != null) {
                    Location var103 = var2.center.clone();
                    Location var104 = var103.add(var18);
                    var19.teleport(var104);
                    this.spinFragment(var19, var15, 499537219);
                }
            }
        }
    }

    private void climax(World var1, Location var2, Vector var3, Vector var4, int var5) {
        for (int var19 = 0; var19 < 60; var19++) {
            double var20 = (double)var19 / 60.0 * (Math.PI * 2);
            double var47 = Math.cos(var20) * 5.0;
            double var54 = Math.sin(var20) * 5.0;
            this.launchToward(var1, var2, var3, var4, var47, var54, 2100504346);
        }

        BukkitScheduler var31 = Bukkit.getScheduler();
        BlissPlugin var35 = this.plugin;
        Runnable var40 = () -> {
            double[] var18 = new double[4];
            double[] var19x = new double[4];

            for (int var20x = 0; var20x < 4; var20x++) {
                double var262 = (double)var20x * (Math.PI / 2) + (Math.PI / 4);
                double var231 = Math.cos(var262) * 4.0;
                var18[var20x] = var231;
                double var237 = Math.sin(var262) * 4.0;
                var19x[var20x] = var237;
            }

            for (int var21 = 0; var21 < 4; var21++) {
                int var22 = (var21 + 1) % 4;
                double var23 = var18[var21] * 0.18;
                double var25 = var19x[var21] * 0.18;
                double var27 = var18[var22] * 0.18;
                double var29 = var19x[var22] * 0.18;

                for (int var31x = 0; var31x <= 44; var31x++) {
                    double var32 = (double)var31x / 44.0;
                    double var34 = 1.0 - var32;
                    double var95 = var34 * var34;
                    double var36 = var95 * var34;
                    double var101 = 3.0 * var34;
                    double var103 = var101 * var34;
                    double var38 = var103 * var32;
                    double var109 = 3.0 * var34;
                    double var111 = var109 * var32;
                    double var40x = var111 * var32;
                    double var117 = var32 * var32;
                    double var42 = var117 * var32;
                    double var193 = var18[var21];
                    double var123 = var36 * var193;
                    double var197 = var38 * var23;
                    double var125 = var123 + var197;
                    double var201 = var40x * var27;
                    double var127 = var125 + var201;
                    double var244 = var18[var22];
                    double var205 = var42 * var244;
                    double var44 = var127 + var205;
                    double var208 = var19x[var21];
                    double var133 = var36 * var208;
                    double var212 = var38 * var25;
                    double var135 = var133 + var212;
                    double var216 = var40x * var29;
                    double var137 = var135 + var216;
                    double var251 = var19x[var22];
                    double var220 = var42 * var251;
                    double var46 = var137 + var220;
                    this.launchToward(var1, var2, var3, var4, var44, var46, 2100504346);
                }
            }
        };
        var31.runTaskLater(var35, var40, 12L);
    }

    private void launchToward(World var1, Location var2, Vector var3, Vector var4, double var5, double var7, int var9) {
        double var27 = Math.hypot(var5, var7);
        if (!(var27 < 1.0E-6)) {
            Vector var36 = var3.clone();
            Vector var37 = var36.multiply(var5);
            Vector var41 = var4.clone();
            Vector var42 = var41.multiply(var7);
            Vector var29 = var37.add(var42);
            Particle var43 = Particle.END_ROD;
            double var49 = var29.getX();
            double var51 = var49 / var27;
            double var54 = var29.getY();
            double var56 = var54 / var27;
            double var59 = var29.getZ();
            double var61 = var59 / var27;
            double var65 = var27 * 0.11;
            Particles.spawn(var1, var43, var2, 0, var51, var56, var61, var65, null, true, 1748031581);
        }
    }

    private void drawCircle(World var1, Location var2, Vector var3, Vector var4, double var5) {
        int var37 = (int)(var5 * 22.0);
        int var20 = Math.max(72, var37);

        for (int var21 = 0; var21 < var20; var21++) {
            double var28 = (double)var21;
            double var40 = (double)var20;
            double var22 = var28 / var40 * (Math.PI * 2);
            double var46 = Math.cos(var22);
            double var48 = var46 * var5;
            double var52 = Math.sin(var22);
            double var54 = var52 * var5;
            this.spawnPlane(var1, var2, var3, var4, var48, var54, 1448261818);
        }
    }

    private void drawStar(World var1, Location var2, Vector var3, Vector var4, double var5) {
        double var23 = var5 * 0.42;
        double[] var25 = new double[8];
        double[] var26 = new double[8];

        for (int var27 = 0; var27 < 8; var27++) {
            double var28 = (double)var27 * (Math.PI / 4) + (Math.PI / 4);
            double var50;
            if (var27 % 2 == 1) {
                var50 = var5;
            } else {
                var50 = var23;
            }

            double var82 = Math.cos(var28);
            double var84 = var82 * var50;
            var25[var27] = var84;
            double var88 = Math.sin(var28);
            double var90 = var88 * var50;
            var26[var27] = var90;
        }

        for (byte var130 = 0; var130 < 8; var130 += 1) {
            int var131 = (var130 + 1) % 8;

            for (int var30 = 0; var30 < 22; var30++) {
                double var132 = (double)var30 / 22.0;
                double var98 = var25[var130];
                double var102 = var25[var131];
                double var114 = var25[var130];
                double var104 = var102 - var114;
                double var106 = var104 * var132;
                double var100 = var98 + var106;
                double var109 = var26[var130];
                double var119 = var26[var131];
                double var126 = var26[var130];
                double var121 = var119 - var126;
                double var123 = var121 * var132;
                double var111 = var109 + var123;
                this.spawnPlane(var1, var2, var3, var4, var100, var111, 1448261818);
            }
        }
    }

    private Location planeLoc(Location var1, Vector var2, Vector var3, double var4, double var6, int var8) {
        Location var19 = var1.clone();
        double var21 = var2.getX();
        double var23 = var21 * var4;
        double var28 = var3.getX();
        double var30 = var28 * var6;
        double var25 = var23 + var30;
        double var33 = var2.getY();
        double var35 = var33 * var4;
        double var42 = var3.getY();
        double var44 = var42 * var6;
        double var37 = var35 + var44;
        double var47 = var2.getZ();
        double var49 = var47 * var4;
        double var56 = var3.getZ();
        double var58 = var56 * var6;
        double var51 = var49 + var58;
        return var19.add(var25, var37, var51);
    }

    private void spawnPlane(World var1, Location var2, Vector var3, Vector var4, double var5, double var7, int var9) {
        Particle var11 = Particle.END_ROD;
        Location var24 = this.planeLoc(var2, var3, var4, var5, var7, 719196825);
        Particles.spawn(var1, var11, var24, 1, 0.0, 0.0, 0.0, 0.0, null, true, 1748031581);
    }

    private void eruption(World var1, Location var2, int var3) {
        Particle var5 = Particle.END_ROD;
        Particles.spawn(var1, var5, var2, 4, 0.15, 0.2, 0.15, 0.7, 1986132876);
        Particle var18 = Particle.END_ROD;
        Particles.spawn(var1, var18, var2, 2, 0.4, 0.5, 0.4, 0.25, 1986132876);
        Particle var19 = Particle.FIREWORK;
        Particles.spawn(var1, var19, var2, 2, 0.3, 0.4, 0.3, 0.3, 1986132876);
    }

    private void spinningArms(World var1, Location var2, Vector var3, Vector var4, double var5, int var7) {
        for (int var19 = 0; var19 < 4; var19++) {
            double var75 = (double)var19 * (Math.PI / 2);
            double var20 = var5 + var75;

            for (int var22 = 0; var22 < 5; var22++) {
                double var23 = (double)var22 / 4.0;
                double var81 = 3.52 * var23;
                double var25 = 0.9 + var81;
                double var85 = 0.85 * var23;
                double var27 = var20 - var85;
                Vector var52 = var3.clone();
                double var62 = Math.cos(var27);
                Vector var53 = var52.multiply(var62);
                Vector var65 = var4.clone();
                double var89 = Math.sin(var27);
                Vector var66 = var65.multiply(var89);
                Vector var29 = var53.add(var66);
                Location var56 = var2.clone();
                double var68 = var29.getX();
                double var70 = var68 * var25;
                double var93 = var29.getY();
                double var95 = var93 * var25;
                double var104 = var29.getZ();
                double var106 = var104 * var25;
                Location var30 = var56.add(var70, var95, var106);
                Color var102 = HOLDER_GOLD;
                this.gemStar(var1, var30, var30, var102, 3, 49641728);
            }
        }
    }

    void gustBurst(World var1, Location var2, double var3, int var5) {
        this.gustBurst(var1, var2, var3, 1.0, false, 1226658703);
    }

    void gustBurst(World var1, Location var2, double var3, double var5, boolean var7, int var8) {
        double var22 = 0.62 * var5;
        double var30 = 0.18 * var5;
        this.gustRing(var1, var2, var22, var30, var3, var7, 2004323435);
        double var26 = 0.31 * var5;
        double var36 = 0.1 * var5;
        double var44 = var3 + 0.4;
        this.gustRing(var1, var2, var26, var36, var44, var7, 2004323435);
    }

    private void gustRing(World var1, Location var2, double var3, double var5, double var7) {
        this.gustRing(var1, var2, var3, var5, var7, false, 2004323435);
    }

    private void gustRing(World var1, Location var2, double var3, double var5, double var7, boolean var9, int var10) {
        for (int var31 = 0; var31 < 20; var31++) {
            double var97 = (double)var31;
            double var75 = (Math.PI / 10) * var97;
            double var32 = var7 + var75;
            if (!var9) {
                Particle var70 = Particle.END_ROD;
                double var78 = var2.getX();
                double var100 = var2.getY();
                double var124 = var2.getZ();
                double var146 = Math.cos(var32);
                double var162 = Math.sin(var32);
                Particles.spawn(var1, var70, var78, var100, var124, 0, var146, var5, var162, var3, null, true, 954482185);
            } else {
                for (int var34 = 0; var34 < 2; var34++) {
                    double var105 = (double)var34 / 2.0;
                    double var82 = (Math.PI / 10) * var105;
                    double var35 = var32 + var82;
                    double var37 = Math.cos(var35);
                    double var39 = Math.sin(var35);
                    Particle var72 = Particle.CLOUD;
                    double var85 = var2.getX();
                    double var110 = var2.getY() + 0.2;
                    double var131 = var2.getZ();
                    double var174 = var3 * 0.94;
                    Particles.spawn(var1, var72, var85, var110, var131, 0, var37, var5, var39, var174, null, true, 954482185);
                    Particle var73 = Particle.CLOUD;
                    double var88 = var2.getX();
                    double var115 = var2.getY() + 0.2;
                    double var136 = var2.getZ();
                    double var178 = var3 * 1.06;
                    Particles.spawn(var1, var73, var88, var115, var136, 0, var37, var5, var39, var178, null, true, 954482185);
                }

                double var192 = var32 + (Math.PI / 20);
                Particle var74 = Particle.END_ROD;
                double var95 = var2.getX();
                double var122 = var2.getY() + 0.25;
                double var141 = var2.getZ();
                double var154 = Math.cos(var192);
                double var170 = Math.sin(var192);
                Particles.spawn(var1, var74, var95, var122, var141, 0, var154, var5, var170, var3, null, true, 954482185);
            }
        }
    }

    private void ritualSound(Location var1, int var2, int var3) {
        if (var1 != null && var1.getWorld() != null) {
            if (var2 >= 0) {
                int var36 = RITUAL_SOUNDS.length;
                if (var2 < var36) {
                    for (Player var14 : var1.getWorld().getPlayers()) {
                        Location var31 = var14.getLocation();
                        if (var31.distanceSquared(var1) <= 6400.0) {
                            String[] var39 = RITUAL_SOUNDS;
                            String var40 = var39[var2];
                            SoundCategory var41 = SoundCategory.MASTER;
                            var14.playSound(var14, var40, var41, 1.0F, 1.0F);
                        }
                    }

                    return;
                }
            }
        }
    }

    private void ritualSoundStop(World var1, int var2) {
        if (var1 != null) {
            for (Player var7 : var1.getPlayers()) {
                for (String var11 : RITUAL_SOUNDS) {
                    SoundCategory var5 = SoundCategory.MASTER;
                    var7.stopSound(var11, var5);
                }
            }
        }
    }

    void flash(World var1, Location var2, int var3, double var4, double var6, double var8, int var10) {
        if (FLASH_NEEDS_COLOR) {
            Particle var27 = Particle.FLASH;
            Color var38 = Color.WHITE;
            Particles.spawn(var1, var27, var2, var3, var4, var6, var8, 0.0, var38, true, 1748031581);
        } else {
            Particle var12 = Particle.FLASH;
            Particles.spawn(var1, var12, var2, var3, var4, var6, var8, 0.0, null, true, 1748031581);
        }
    }

    private DustOptions goldPulse(int var1, int var2) {
        double var59 = Math.sin((double)var1 * 0.05);
        double var35 = 0.5 * var59;
        double var12 = 0.5 + var35;
        double var39 = 45.0 * var12;
        int var14 = (int)(210.0 + var39);
        double var43 = 78.0 * var12;
        int var15 = (int)(150.0 + var43);
        double var47 = 85.0 * var12;
        int var16 = (int)(45.0 + var47);
        int var50 = Math.min(255, var14);
        int var53 = Math.min(255, var15);
        int var69 = Math.min(255, var16);
        Color var51 = Color.fromRGB(var50, var53, var69);
        return new DustOptions(var51, 0.8F);
    }

    private void spinGoldGem(FragmentCoreRitual.State var1, int var2, int var3) {
        UUID var21 = var1.goldGem;
        ItemDisplay var12 = this.display(var21, 1415410134);
        if (var12 != null) {
            Quaternionf var16 = new Quaternionf();
            float var24 = (float)var2 * 0.05F;
            Quaternionf var13 = var16.rotateY(var24);
            var12.setInterpolationDelay(0);
            var12.setInterpolationDuration(2);
            Vector3f var7 = new Vector3f();
            Vector3f var8 = new Vector3f(1.54F, 1.54F, 1.54F);
            Quaternionf var29 = new Quaternionf();
            float var31 = GEM_ART_ROLL;
            Quaternionf var30 = var29.rotateZ(var31);
            Transformation var27 = new Transformation(var7, var13, var8, var30);
            var12.setTransformation(var27);
        }
    }

    private void whiteBurst(World var1, Location var2, int var3) {
        this.flash(var1, var2, 1, 0.0, 0.0, 0.0, 2059459588);
    }

    private void angledBurst(World var1, Location var2, boolean var3) {
        Particle var11 = Particle.FIREWORK;
        this.angledBurst(var1, var2, var3, -1.0, 30, var11, 0.55, 1595198679);
    }

    void angledBurst(World var1, Location var2, boolean var3, double var4, int var6, Particle var7, double var8, int var10) {
        Particle[] var22 = new Particle[1];
        Particle var23;
        if (var3) {
            var23 = Particle.END_ROD;
        } else {
            var23 = var7;
        }

        var22[0] = var23;
        this.angledBurst(var1, var2, var4, var6, var8, var22, 1580517192);
    }

    private void angledBurst(World var1, Location var2, double var3, int var5, double var6, Particle[] var8, int var9) {
        ThreadLocalRandom var25 = ThreadLocalRandom.current();
        Vector var260;
        if (var3 < 0.0) {
            double var142 = var25.nextDouble() * 2.0 - 1.0;
            double var208 = var25.nextDouble() * 2.0 - 1.0;
            double var242 = var25.nextDouble() * 2.0 - 1.0;
            Vector var47 = new Vector(var142, var208, var242);
            var260 = var47;
            if (var47.lengthSquared() < 1.0E-6) {
                Vector var52 = new Vector(0, 1, 0);
                var260 = var52;
            }
        } else {
            double var115 = Math.toRadians(var3);
            double var188 = var25.nextDouble();
            double var261 = var115 * var188;
            double var263 = var25.nextDouble() * (Math.PI * 2);
            double var194 = Math.sin(var261);
            double var229 = Math.cos(var263);
            double var196 = var194 * var229;
            double var233 = Math.cos(var261);
            double var248 = Math.sin(var261);
            double var258 = Math.sin(var263);
            double var250 = var248 * var258;
            Vector var124 = new Vector(var196, var233, var250);
            var260 = var124;
        }

        var260.normalize();
        Vector var56 = var260.clone();
        Vector var127 = new Vector(0, 1, 0);
        Vector var26 = var56.crossProduct(var127);
        if (var26.lengthSquared() < 1.0E-6) {
            Vector var62 = new Vector(1, 0, 0);
            var26 = var62;
        }

        var26.normalize();
        Vector var66 = var260.clone();
        Vector var27 = var66.crossProduct(var26).normalize();
        int var28 = var5;

        for (int var29 = 0; var29 < var28; var29++) {
            int var151 = var8.length;
            int var131 = var29 % var151;
            Particle var30 = var8[var131];
            Particle var132 = Particle.SOUL;
            double var75;
            if (var30 == var132) {
                var75 = var6 * 0.7;
            } else {
                var75 = var6;
            }

            double var31 = var75;
            Particle var133 = Particle.SCULK_CHARGE;
            Float var126;
            if (var30 == var133) {
                var126 = 0.0F;
            } else {
                var126 = null;
            }

            Float var33 = var126;
            Particle var134 = Particle.SCULK_CHARGE;
            byte var125;
            if (var30 == var134) {
                var125 = 2;
            } else {
                var125 = 1;
            }

            byte var34 = var125;
            double var80 = (double)var29;
            double var153 = (double)var28;
            double var35 = var80 / var153 * (Math.PI * 2);
            double var87 = var26.getX();
            double var159 = Math.cos(var35);
            double var89 = var87 * var159;
            double var162 = var27.getX();
            double var215 = Math.sin(var35);
            double var164 = var162 * var215;
            double var37 = var89 + var164;
            double var94 = var26.getY();
            double var168 = Math.cos(var35);
            double var96 = var94 * var168;
            double var171 = var27.getY();
            double var219 = Math.sin(var35);
            double var173 = var171 * var219;
            double var39 = var96 + var173;
            double var101 = var26.getZ();
            double var177 = Math.cos(var35);
            double var103 = var101 * var177;
            double var180 = var27.getZ();
            double var223 = Math.sin(var35);
            double var182 = var180 * var223;
            double var41 = var103 + var182;

            for (int var43 = 0; var43 < var34; var43++) {
                Particles.spawn(var1, var30, var2, 0, var37, var39, var41, var31, var33, true, 1748031581);
            }
        }
    }

    private void drawGemShapes(World var1, Location var2, Player var3, int var4) {
        Vector var22 = var3.getEyeLocation().toVector();
        Vector var55 = var2.toVector();
        Vector var14 = var22.subtract(var55);
        var14.setY(0);
        if (var14.lengthSquared() < 1.0E-6) {
            Vector var30 = new Vector(1, 0, 0);
            var14 = var30;
        } else {
            var14.normalize();
        }

        Vector var32 = var14.clone();
        Vector var57 = new Vector(0, 1, 0);
        Vector var15 = var32.crossProduct(var57);
        if (var15.lengthSquared() < 1.0E-6) {
            Vector var38 = new Vector(1, 0, 0);
            var15 = var38;
        } else {
            var15.normalize();
        }

        Vector var39 = new Vector(0, 1, 0);
        Vector var16 = var39;

        for (int var17 = 0; var17 < 7; var17++) {
            double var18 = (double)var17 * (Math.PI * 2.0 / 7.0);
            Location var48 = var2.clone();
            double var63 = Math.cos(var18) * 2.5;
            double var90 = Math.sin(var18) * 2.5;
            Location var20 = var48.add(var63, 0.0, var90);
            this.drawGemShape(var1, var20, var15, var16, 124025948);
        }
    }

    private void drawGemShape(World var1, Location var2, Vector var3, Vector var4, int var5) {
        int var26 = GEM_SHAPE.length;

        for (int var27 = 0; var27 < var26; var27++) {
            double[][] var40 = GEM_SHAPE;
            double[] var28 = var40[var27];
            double[][] var42 = GEM_SHAPE;
            int var68 = var27 + 1;
            int var69 = var68 % var26;
            double[] var29 = var42[var69];

            for (int var30 = 0; var30 < 8; var30++) {
                double var31 = (double)var30 / 8.0;
                double var52 = var28[0];
                double var78 = var29[0];
                double var107 = var28[0];
                double var80 = var78 - var107;
                double var82 = var80 * var31;
                double var33 = (var52 + var82) * 0.8;
                double var59 = var28[1];
                double var87 = var29[1];
                double var112 = var28[1];
                double var89 = var87 - var112;
                double var91 = var89 * var31;
                double var35 = (var59 + var91) * 0.8;
                Particle var73 = Particle.DUST;
                Location var96 = var2.clone();
                double var100 = var3.getX();
                double var102 = var100 * var33;
                double var122 = var4.getX();
                double var124 = var122 * var35;
                double var104 = var102 + var124;
                double var127 = var3.getY();
                double var129 = var127 * var33;
                double var136 = var4.getY();
                double var138 = var136 * var35;
                double var131 = var129 + var138;
                double var141 = var3.getZ();
                double var143 = var141 * var33;
                double var150 = var4.getZ();
                double var152 = var150 * var35;
                double var145 = var143 + var152;
                Location var97 = var96.add(var104, var131, var145);
                DustOptions var24 = GEM_SHAPE_DUST;
                Particles.spawn(var1, var73, var97, 1, 0.0, 0.0, 0.0, 0.0, var24, true, 1748031581);
            }
        }
    }

    private void tetherLine(World var1, Location var2, Location var3, int var4) {
        Vector var35 = var3.toVector();
        Vector var53 = var2.toVector();
        Vector var28 = var35.subtract(var53);
        double var29 = var28.length();
        if (!(var29 < 1.0E-6)) {
            int var58 = (int)(var29 / 0.3);
            int var31 = Math.max(2, var58);

            for (int var32 = 0; var32 <= var31; var32++) {
                double var48 = (double)var32;
                double var62 = (double)var31;
                double var33 = var48 / var62;
                Particle var60 = TETHER_PARTICLE;
                double var65 = var2.getX();
                double var69 = var28.getX();
                double var71 = var69 * var33;
                double var67 = var65 + var71;
                double var74 = var2.getY();
                double var79 = var28.getY();
                double var81 = var79 * var33;
                double var76 = var74 + var81;
                double var84 = var2.getZ();
                double var89 = var28.getZ();
                double var91 = var89 * var33;
                double var86 = var84 + var91;
                Particles.spawn(var1, var60, var67, var76, var86, 1, 0.0, 0.0, 0.0, 0.0, null, true, 954482185);
            }
        }
    }

    private void drawRing(World var1, Location var2, Vector var3, Vector var4, double var5, Particle var7, Object var8, int var9, int var10) {
        for (int var32 = 0; var32 < var9; var32++) {
            double var42 = (double)var32;
            double var72 = (double)var9;
            double var33 = var42 / var72 * (Math.PI * 2);
            double var50 = Math.cos(var33);
            double var35 = var50 * var5;
            double var56 = Math.sin(var33);
            double var37 = var56 * var5;
            Location var61 = var2.clone();
            double var65 = var3.getX();
            double var67 = var65 * var35;
            double var82 = var4.getX();
            double var84 = var82 * var37;
            double var69 = var67 + var84;
            double var87 = var3.getY();
            double var89 = var87 * var35;
            double var97 = var4.getY();
            double var99 = var97 * var37;
            double var91 = var89 + var99;
            double var102 = var3.getZ();
            double var104 = var102 * var35;
            double var111 = var4.getZ();
            double var113 = var111 * var37;
            double var106 = var104 + var113;
            Location var39 = var61.add(var69, var91, var106);
            Particles.spawn(var1, var7, var39, 1, 0.0, 0.0, 0.0, 0.0, var8, true, 1748031581);
        }
    }

    private void placeOrbits(FragmentCoreRitual.State var1, Location var2, double var3, boolean var5, DustOptions var6, int var7) {
        World var20 = var2.getWorld();

        for (int var21 = 0; var21 < 7; var21++) {
            float var22 = (float)((double)var21 * (Math.PI * 2.0 / 7.0));
            double var48 = Math.toRadians(20.0);
            double var106 = (double)var21;
            double var145 = Math.toRadians(19.166666666666668);
            double var108 = var106 * var145;
            float var23 = (float)(var48 + var108);
            Quaternionf var53 = new Quaternionf();
            Quaternionf var54 = var53.rotateY(var22);
            Quaternionf var24 = var54.rotateX(var23);
            Vector3f var90 = new Vector3f(1.0F, 0.0F, 0.0F);
            Vector3f var25 = var24.transform(var90);
            Vector3f var91 = new Vector3f(0.0F, 0.0F, 1.0F);
            Vector3f var26 = var24.transform(var91);
            float var111 = var25.x;
            float var127 = var25.y;
            float var150 = var25.z;
            Vector var60 = new Vector(var111, var127, var150);
            float var113 = var26.x;
            float var129 = var26.y;
            float var152 = var26.z;
            Vector var61 = new Vector(var113, var129, var152);
            if (var5) {
                Particle var16 = Particle.DUST;
                int var184 = Math.max(80, 291);
                this.drawRing(var20, var2, var60, var61, 5.2, var16, var6, var184, 1109974871);
            }

            double var118 = (double)var21 * (Math.PI * 2.0 / 7.0);
            double var29 = var3 + var118;
            double var31 = Math.cos(var29) * 5.2;
            double var33 = Math.sin(var29) * 5.2;
            List var94 = var1.frags;
            UUID var96 = (UUID)var94.get(var21);
            ItemDisplay var35 = this.display(var96, 1415410134);
            if (var35 != null) {
                Location var84 = var2.clone();
                double var98 = var60.getX();
                double var100 = var98 * var31;
                double var134 = var61.getX();
                double var136 = var134 * var33;
                double var102 = var100 + var136;
                double var139 = var60.getY();
                double var141 = var139 * var31;
                double var164 = var61.getY();
                double var166 = var164 * var33;
                double var143 = var141 + var166;
                double var169 = var60.getZ();
                double var171 = var169 * var31;
                double var180 = var61.getZ();
                double var182 = var180 * var33;
                double var173 = var171 + var182;
                Location var36 = var84.add(var102, var143, var173);
                var35.teleport(var36);
            }
        }
    }

    private void orientForOrbit(FragmentCoreRitual.State var1, int var2) {
        for (UUID var10 : var1.frags) {
            ItemDisplay var11 = this.display(var10, 1415410134);
            if (var11 != null) {
                Billboard var27 = Billboard.CENTER;
                var11.setBillboard(var27);
                var11.setInterpolationDelay(0);
                var11.setInterpolationDuration(0);
                Vector3f var5 = new Vector3f();
                Quaternionf var32 = new Quaternionf();
                Vector3f var33 = new Vector3f(0.35F, 0.35F, 0.35F);
                Quaternionf var34 = new Quaternionf();
                Transformation var30 = new Transformation(var5, var32, var33, var34);
                var11.setTransformation(var30);
                var11.setTeleportDuration(2);
            }
        }
    }

    private void spinFragment(ItemDisplay var1, int var2, int var3) {
        float var53 = (float)Math.sin((double)var2 * 1.3 + 0.4);
        float var71 = (float)Math.cos((double)var2 * 0.7 + 0.5);
        float var85 = (float)Math.sin((double)var2 * 0.9 + 1.1);
        Vector3f var4 = new Vector3f(var53, var71, var85);
        if (var4.lengthSquared() < 1.0E-4F) {
            var4.set(0.0F, 1.0F, 0.0F);
        }

        var4.normalize();
        double var27 = this.rot;
        double var97 = (double)var2;
        double var88 = 0.25 * var97;
        double var57 = 1.0 + var88;
        double var29 = var27 * var57;
        double var60 = (double)var2;
        float var17 = (float)(var29 + var60);
        Quaternionf var34 = new Quaternionf();
        Quaternionf var18 = var34.rotateAxis(var17, var4);
        var1.setInterpolationDelay(0);
        var1.setInterpolationDuration(2);
        Vector3f var73 = new Vector3f();
        Vector3f var93 = new Vector3f(0.35F, 0.35F, 0.35F);
        Quaternionf var99 = new Quaternionf();
        Transformation var44 = new Transformation(var73, var18, var93, var99);
        var1.setTransformation(var44);
    }

    private void spinCore(ItemDisplay var1, FragmentCoreRitual.State var2, int var3) {
        if (var1 != null) {
            if (var2.coreAxis == null) {
                Vector3f var5 = new Vector3f(0.0F, 1.0F, 0.0F);
                var2.coreAxis = var5;
            }

            Quaternionf var16 = new Quaternionf();
            float var24 = (float)this.rot;
            Vector3f var29 = var2.coreAxis;
            Quaternionf var12 = var16.rotateAxis(var24, var29);
            var1.setInterpolationDelay(0);
            var1.setInterpolationDuration(2);
            Vector3f var30 = new Vector3f();
            Vector3f var32 = new Vector3f(0.85F, 0.85F, 0.85F);
            Quaternionf var33 = new Quaternionf();
            Transformation var27 = new Transformation(var30, var12, var32, var33);
            var1.setTransformation(var27);
        }
    }

    private Vector3f randomAxis(int var1) {
        ThreadLocalRandom var11 = ThreadLocalRandom.current();
        float var29 = (float)(var11.nextDouble() * 2.0 - 1.0);
        float var37 = (float)(var11.nextDouble() * 2.0 - 1.0);
        float var48 = (float)(var11.nextDouble() * 2.0 - 1.0);
        Vector3f var13 = new Vector3f(var29, var37, var48);
        if (var13.lengthSquared() < 1.0E-4F) {
            var13.set(0.0F, 1.0F, 0.0F);
        }

        return var13.normalize();
    }

    private void radiateWhiteGlow(World var1, Location var2, int var3) {
        Vector var20 = this.outwardDir(189465981);
        Location var24 = var2.clone();
        double var29 = var20.getX() * 0.25;
        double var35 = var20.getY() * 0.25;
        double var43 = var20.getZ() * 0.25;
        Location var21 = var24.add(var29, var35, var43);
        Particle var31 = Particle.SOUL;
        double var38 = var20.getX();
        double var45 = var20.getY();
        double var47 = var20.getZ();
        Particles.spawn(var1, var31, var21, 0, var38, var45, var47, 0.35, null, true, 1748031581);
    }

    private Vector outwardDir(int var1) {
        ThreadLocalRandom var9 = ThreadLocalRandom.current();
        double var10 = var9.nextDouble() * (Math.PI * 2);
        double var28 = Math.cos(var10);
        double var37 = (var9.nextDouble() - 0.4) * 0.6;
        double var44 = Math.sin(var10);
        Vector var18 = new Vector(var28, var37, var44);
        Vector var12 = var18;
        if (var18.lengthSquared() < 1.0E-6) {
            Vector var23 = new Vector(1, 0, 0);
            var12 = var23;
        }

        return var12.normalize();
    }

    private void spawnFragments(Player var1, FragmentCoreRitual.State var2, int var3) {
        for (int var8 = 1; var8 <= 7; var8++) {
            Location var21 = var1.getEyeLocation();
            ItemStack var28 = RitualItems.wireFragment(var8, 60990984);
            ItemDisplay var9 = this.spawnDisplay(var21, var28, 0.35F, 1550711431);
            var9.setGlowing(true);
            Color var23 = GOLD;
            var9.setGlowColorOverride(var23);
            Billboard var24 = Billboard.FIXED;
            var9.setBillboard(var24);
            var9.setInterpolationDuration(2);
            List var18 = var2.frags;
            UUID var27 = var9.getUniqueId();
            var18.add(var27);
        }
    }

    ItemDisplay spawnDisplay(Location var1, ItemStack var2, float var3, int var4) {
        World var13 = var1.getWorld();
        ItemDisplay var12 = (ItemDisplay)var13.spawn(var1, ItemDisplay.class);
        var12.setItemStack(var2);
        Billboard var25 = Billboard.CENTER;
        var12.setBillboard(var25);
        Vector3f var8 = new Vector3f();
        Quaternionf var33 = new Quaternionf();
        Vector3f var34 = new Vector3f(var3, var3, var3);
        Quaternionf var35 = new Quaternionf();
        Transformation var26 = new Transformation(var8, var33, var34, var35);
        var12.setTransformation(var26);
        var12.setTeleportDuration(3);
        var12.setPersistent(false);
        PersistentDataContainer var22 = var12.getPersistentDataContainer();
        NamespacedKey var29 = KEY_DISPLAY;
        PersistentDataType var30 = PersistentDataType.BYTE;
        Byte var32 = (byte)1;
        var22.set(var29, var30, var32);
        return var12;
    }

    private void pushBack(Player var1, int var2) {
        Vector var9 = var1.getLocation().getDirection();
        var9.setY(0);
        if (var9.lengthSquared() < 1.0E-6) {
            Vector var18 = new Vector(0, 0, 1);
            var9 = var18;
        }

        var9.normalize().multiply(-1.1);
        var9.setY(0.32);
        var1.setVelocity(var9);
    }

    List<Player> audience(Location var1, int var2) {
        ArrayList var3 = new ArrayList();
        ArrayList var6 = var3;
        if (var1 != null && var1.getWorld() != null) {
            for (Player var10 : var1.getWorld().getPlayers()) {
                Location var28 = var10.getLocation();
                if (var28.distanceSquared(var1) <= 6400.0) {
                    var6.add(var10);
                }
            }

            return var6;
        } else {
            return var3;
        }
    }

    void screenFlashAudience(Location var1, Player var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        for (Player var17 : this.audience(var1, 1101174349)) {
            if (var2 != null) {
                UUID var27 = var17.getUniqueId();
                UUID var31 = var2.getUniqueId();
                if (var27.equals(var31)) {
                    continue;
                }
            }

            this.screenFlashEntity(var17, var3, var4, var5, var6, var7, 455878892);
        }
    }

    private void pushFx(Player var1, List<Player> var2, int var3) {
        World var22 = var1.getWorld();
        Location var23 = var1.getLocation().add(0.0, 1.0, 0.0);
        Particle var41 = Particle.SOUL;
        Particles.spawn(var22, var41, var23, 24, 0.3, 0.4, 0.3, 0.05, null, true, 1748031581);
        this.flash(var22, var23, 1, 0.0, 0.0, 0.0, 2059459588);

        for (Player var25 : var2) {
            Location var44 = var25.getLocation();
            Sound var48 = Sound.BLOCK_BEACON_ACTIVATE;
            SoundCategory var52 = SoundCategory.MASTER;
            var25.playSound(var44, var48, var52, 1.4F, 1.3F);
            Location var46 = var25.getLocation();
            Sound var49 = Sound.BLOCK_RESPAWN_ANCHOR_CHARGE;
            SoundCategory var53 = SoundCategory.MASTER;
            var25.playSound(var46, var49, var53, 1.2F, 1.1F);
        }
    }

    private void feetSpiral(World var1, Location var2, int var3) {
        Location var25 = var2.clone().add(0.0, 0.3, 0.0);
        double var63 = this.feetAng + 0.22;
        this.feetAng = var63;

        for (int var26 = 0; var26 < 2; var26++) {
            for (int var27 = 0; var27 < 2; var27++) {
                double var44 = this.feetAng;
                double var70 = (double)var26 * Math.PI;
                double var28 = var44 + var70;
                double var96 = (double)var27;
                double var88 = 0.35 * var96;
                double var74 = 1.0 - var88;
                double var30 = 0.055 * var74;
                double var32 = Math.cos(var28);
                double var34 = Math.sin(var28);
                Particle var67 = Particle.SCULK_CHARGE;
                Location var77 = var25.clone();
                double var83 = var32 * 0.1;
                double var100 = var34 * 0.1;
                Location var78 = var77.add(var83, 0.0, var100);
                Float var102 = 0.0F;
                Particles.spawn(var1, var67, var78, 0, var32, 0.0, var34, var30, var102, true, 1748031581);
            }
        }
    }

    private void radiateSparks(World var1, Location var2, int var3) {
        Vector var14 = this.outwardDir(189465981);
        Location var20 = var2.clone();
        double var28 = var14.getX() * 0.25;
        double var37 = var14.getY() * 0.25;
        double var48 = var14.getZ() * 0.25;
        Location var15 = var20.add(var28, var37, var48);
        int var31 = ThreadLocalRandom.current().nextInt(13);
        int var17 = 20 + var31;
        Location var40 = var15.clone();
        Vector var43 = var14.clone().multiply(4.0);
        Location var41 = var40.add(var43);
        Color var44 = HOLDER_GOLD;
        this.gemStar(var1, var15, var41, var44, var17, 49641728);
    }

    private static Location lerp(Location var0, Location var1, double var2, int var4) {
        World var12 = var0.getWorld();
        double var13 = var0.getX();
        double var17 = var1.getX();
        double var28 = var0.getX();
        double var19 = var17 - var28;
        double var21 = var19 * var2;
        double var15 = var13 + var21;
        double var24 = var0.getY();
        double var33 = var1.getY();
        double var44 = var0.getY();
        double var35 = var33 - var44;
        double var37 = var35 * var2;
        double var26 = var24 + var37;
        double var40 = var0.getZ();
        double var49 = var1.getZ();
        double var55 = var0.getZ();
        double var51 = var49 - var55;
        double var53 = var51 * var2;
        double var42 = var40 + var53;
        return new Location(var12, var15, var26, var42);
    }

    private boolean hasFragmentCore(Player var1, int var2) {
        ItemStack[] var5 = var1.getInventory().getContents();
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; var7++) {
            if (RitualItems.isFragmentCore(var5[var7], 1590975450)) {
                return true;
            }
        }

        return false;
    }

    private void consumeMainHandCore(Player var1, int var2) {
        if (RitualItems.isFragmentCore(var1.getInventory().getItemInMainHand(), 1590975450)) {
            var1.getInventory().setItemInMainHand(null);
        } else {
            ItemStack[] var6 = var1.getInventory().getContents();
            int var7 = 0;

            while (true) {
                int var23 = var6.length;
                if (var7 >= var23) {
                    return;
                }

                if (RitualItems.isFragmentCore(var6[var7], 1590975450)) {
                    PlayerInventory var20 = var1.getInventory();
                    var20.setItem(var7, null);
                    return;
                }

                var7++;
            }
        }
    }

    ItemDisplay display(UUID var1, int var2) {
        if (var1 == null) {
            return null;
        } else {
            return Bukkit.getEntity(var1) instanceof ItemDisplay var13 ? var13 : null;
        }
    }

    void removeDisplay(UUID var1, int var2) {
        ItemDisplay var5 = this.display(var1, 1415410134);
        if (var5 != null) {
            var5.remove();
        }
    }

    private void clearDisplays(FragmentCoreRitual.State var1, int var2) {
        for (UUID var6 : var1.frags) {
            this.removeDisplay(var6, 633746888);
        }

        var1.frags.clear();
        UUID var23 = var1.ghost;
        this.removeDisplay(var23, 633746888);
        var1.ghost = null;
        UUID var26 = var1.placedCore;
        this.removeDisplay(var26, 633746888);
        var1.placedCore = null;
        UUID var29 = var1.goldGem;
        this.removeDisplay(var29, 633746888);
        var1.goldGem = null;
    }

    private void sweepOrphans(int var1) {
        Iterator var5 = Bukkit.getWorlds().iterator();

        while (var5.hasNext()) {
            for (ItemDisplay var8 : ((World)var5.next()).getEntitiesByClass(ItemDisplay.class)) {
                PersistentDataContainer var24 = var8.getPersistentDataContainer();
                NamespacedKey var27 = KEY_DISPLAY;
                PersistentDataType var4 = PersistentDataType.BYTE;
                if (var24.has(var27, var4)) {
                    var8.remove();
                }
            }
        }
    }

    static {
        Color var97 = GOLD;
        DustOptions var10 = new DustOptions(var97, 1.4F);
        GOLD_DUST = var10;
        Color var98 = GOLD;
        DustOptions var11 = new DustOptions(var98, 0.7F);
        GOLD_DUST_SMALL = var11;
        Color var99 = GOLD;
        DustOptions var12 = new DustOptions(var99, 0.5F);
        SHAPE_DUST = var12;
        boolean var75;
        if (Particle.FLASH.getDataType() == Color.class) {
            var75 = true;
        } else {
            var75 = false;
        }

        FLASH_NEEDS_COLOR = var75;
        int[] var16 = new int[]{6, 27, 52, 69, 91, 110, 131, 150, 171, 190, 228};
        CORE_BEATS = var16;
        int[] var18 = new int[]{26, 22, 21, 20, 19, 19, 18};
        ABSORB_TETHERS = var18;
        String[] var20 = new String[7];
        String var184 = "bliss:goldgem_p1_core";
        var20[0] = var184;
        String var185 = "bliss:goldgem_p2_bursts";
        var20[1] = var185;
        String var186 = "bliss:goldgem_p3_orbit";
        var20[2] = var186;
        String var187 = "bliss:goldgem_p4_handoff";
        var20[3] = var187;
        String var188 = "bliss:goldgem_p5_wires";
        var20[4] = var188;
        String var189 = "bliss:goldgem_p6_climax";
        var20[5] = var189;
        String var190 = "bliss:goldgem_p7_aftermath";
        var20[6] = var190;
        RITUAL_SOUNDS = var20;
        TETHER_PARTICLE = Particle.WAX_OFF;
        Particle[] var23 = new Particle[3];
        Particle var191 = Particle.SOUL;
        var23[0] = var191;
        Particle var192 = Particle.END_ROD;
        var23[1] = var192;
        Particle var193 = Particle.SCULK_CHARGE;
        var23[2] = var193;
        AFTERMATH_MIX = var23;
        DRAW_START_DELAY = slow(248, 1516685867);
        DRAW_FLIGHT_TICKS = slow(40, 1516685867);
        int var28 = DRAW_FLIGHT_TICKS;
        int var78 = slow(60, 1516685867);
        DRAW_BURST_AT = var28 + var78;
        int var30 = DRAW_BURST_AT;
        int var80 = slow(12, 1516685867);
        DRAW_COVER_AT = var30 + var80;
        DRAW_BROKEN_AT = DRAW_COVER_AT + 12;
        int var34 = DRAW_BROKEN_AT;
        int var83 = slow(70, 1516685867);
        DRAW_COVER2_AT = var34 + var83;
        DRAW_LASTBURST_AT = DRAW_COVER2_AT + 32 + 2;
        int var39 = DRAW_LASTBURST_AT;
        int var87 = slow(8, 1516685867);
        DRAW_SHARDS_AT = var39 + var87;
        DRAW_COVER3_AT = DRAW_SHARDS_AT + 90;
        DRAW_HANDOFF_AT = DRAW_COVER3_AT + 20;
        GOLD_DESCENT_TICKS = slow(44, 1516685867);
        int var47 = DRAW_HANDOFF_AT;
        int var90 = GOLD_DESCENT_TICKS;
        DRAW_END_AT = var47 + var90;
        int var49 = DRAW_START_DELAY;
        int var91 = DRAW_END_AT;
        AFTERMATH_SHOWER_TICKS = var49 + var91;
        int[] var52 = new int[]{49, 83, 122, 167, 214, 264, 308, 353, 402, 446, 494, 547, 595, 642, 691};
        PULSE_BEATS = var52;
        int[] var54 = new int[]{1, 2};
        AFTERMATH_BAR_RINGS = var54;
        float[] var56 = new float[]{1.4F, 0.95F};
        AFTERMATH_BAR_PITCH = var56;
        int var92 = DRAW_START_DELAY;
        AFTERMATH_DRAW_AT = 0 + var92;
        int var93 = AFTERMATH_SHOWER_TICKS;
        AFTERMATH_TICKS = 0 + var93;
        GEM_ART_ROLL = (float)Math.toRadians(-45.0);
        double[][] var67 = new double[14][];
        double[] var214 = new double[]{0.0, 1.0};
        var67[0] = var214;
        double[] var216 = new double[]{-0.3, 0.7};
        var67[1] = var216;
        double[] var218 = new double[]{-0.51, 0.49};
        var67[2] = var218;
        double[] var220 = new double[]{-0.51, 0.21};
        var67[3] = var220;
        double[] var222 = new double[]{-0.51, -0.21};
        var67[4] = var222;
        double[] var224 = new double[]{-0.51, -0.49};
        var67[5] = var224;
        double[] var226 = new double[]{-0.3, -0.7};
        var67[6] = var226;
        double[] var228 = new double[]{0.0, -1.0};
        var67[7] = var228;
        double[] var230 = new double[]{0.3, -0.7};
        var67[8] = var230;
        double[] var232 = new double[]{0.51, -0.49};
        var67[9] = var232;
        double[] var234 = new double[]{0.51, -0.21};
        var67[10] = var234;
        double[] var236 = new double[]{0.51, 0.21};
        var67[11] = var236;
        double[] var238 = new double[]{0.51, 0.49};
        var67[12] = var238;
        double[] var240 = new double[]{0.3, 0.7};
        var67[13] = var240;
        GEM_SHAPE = var67;
        Color var162 = Color.fromRGB(255, 228, 130);
        DustOptions var68 = new DustOptions(var162, 0.6F);
        GEM_SHAPE_DUST = var68;
        FLASH_FILL = ("█".repeat(80) + "\n").repeat(24);
        HOLDER_GOLD = Color.fromRGB(255, 199, 72);
    }

    static final class CamGroup {
        private final List<FragmentCoreRitual.CamStreamer> streams;

        CamGroup() {
            ArrayList var2 = new ArrayList();
            this.streams = var2;
        }

        static FragmentCoreRitual.CamGroup start(Player var0, List<Player> var1, int var2) {
            FragmentCoreRitual.CamStreamer var4 = FragmentCoreRitual.CamStreamer.start(var0, 1117156868);
            if (var4 == null) {
                return null;
            } else {
                FragmentCoreRitual.CamGroup var12 = new FragmentCoreRitual.CamGroup();
                FragmentCoreRitual.CamGroup var5 = var12;
                List var14 = var12.streams;
                var14.add(var4);
                HashSet var16 = new HashSet();
                HashSet var6 = var16;
                UUID var41 = var0.getUniqueId();
                var16.add(var41);

                for (Player var8 : var1) {
                    if (var8 != null && var8.isOnline()) {
                        UUID var43 = var8.getUniqueId();
                        if (var6.add(var43)) {
                            FragmentCoreRitual.CamStreamer var9 = FragmentCoreRitual.CamStreamer.start(var8, 1117156868);
                            if (var9 != null) {
                                List var35 = var5.streams;
                                var35.add(var9);
                            }
                        }
                    }
                }

                return var5;
            }
        }

        void publish(double var1, double var3, int var5) {
            for (FragmentCoreRitual.CamStreamer var20 : this.streams) {
                var20.publish(var1, var3, 725995291);
            }
        }

        void stop(int var1) {
            Iterator var3 = this.streams.iterator();

            while (var3.hasNext()) {
                ((FragmentCoreRitual.CamStreamer)var3.next()).stop(1260880365);
            }

            this.streams.clear();
        }
    }

    static final class CamStreamer {
        private static final int HZ = 60;
        private static Constructor<?> pktCtor;
        private static Constructor<?> pmrCtor;
        private static Class<?> packetCls;
        private static Object vecZero;
        private static Set<Object> relAll;
        private static boolean reflectionDead;
        private final Object conn;
        private final Method send;
        private final ScheduledExecutorService exec;
        private volatile double camR;
        private volatile double camSpeed;
        private volatile long ping;
        private final long t0;
        private double th;
        private double prevY;
        private double prevP;
        private long lastFire;
        private int seq;
        private static int nudgeSeq;

        private static void ensureReflection(int var0) throws Exception {
            if (pktCtor == null) {
                packetCls = Class.forName("net.minecraft.network.protocol.Packet");
                Class var8 = Class.forName("net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket");
                Class var9 = Class.forName("net.minecraft.world.entity.PositionMoveRotation");
                Class var10 = Class.forName("net.minecraft.world.phys.Vec3");
                Class var11 = Class.forName("net.minecraft.world.entity.Relative");
                Class[] var54 = new Class[]{var10, var10, null, null};
                Class var78 = float.class;
                var54[2] = var78;
                Class var79 = float.class;
                var54[3] = var79;
                pmrCtor = var9.getConstructor(var54);
                Class[] var56 = new Class[3];
                Class var80 = int.class;
                var56[0] = var80;
                var56[1] = var9;
                var56[2] = Set.class;
                pktCtor = var8.getConstructor(var56);
                vecZero = var10.getField("ZERO").get(null);
                String var35 = "X";
                String var59 = "Y";
                String var68 = "Z";
                String var76 = "DELTA_X";
                String var83 = "DELTA_Y";
                String var5 = "DELTA_Z";
                String var6 = "Y_ROT";
                String var7 = "X_ROT";
                Set var12 = Set.of(var35, var59, var68, var76, var83, var5, var6, var7);
                HashSet var37 = new HashSet();
                HashSet var13 = var37;

                for (Object var17 : var11.getEnumConstants()) {
                    String var64 = ((Enum)var17).name();
                    if (var12.contains(var64)) {
                        var13.add(var17);
                    }
                }

                int var51 = var13.size();
                int var67 = var12.size();
                if (var51 != var67) {
                    String var69 = "Relative flags";
                    IllegalStateException var53 = new IllegalStateException(var69);
                    throw var53;
                } else {
                    relAll = var13;
                }
            }
        }

        static FragmentCoreRitual.CamStreamer start(Player var0, int var1) {
            if (reflectionDead) {
                return null;
            } else {
                try {
                    ensureReflection(2135067391);
                    Class var11 = var0.getClass();
                    String var3 = "getHandle";
                    Class[] var30 = new Class[0];
                    Method var12 = var11.getMethod(var3, var30);
                    Object[] var32 = new Object[0];
                    Object var7 = var12.invoke(var0, var32);
                    Field var16 = var7.getClass().getField("connection");
                    Object var8 = var16.get(var7);
                    Class var19 = var8.getClass();
                    String var29 = "send";
                    Class[] var34 = new Class[1];
                    Class var6 = packetCls;
                    var34[0] = var6;
                    Method var9 = var19.getMethod(var29, var34);
                    return new FragmentCoreRitual.CamStreamer(var8, var9, 1391313718);
                } catch (Throwable var40) {
                    reflectionDead = true;
                    return null;
                }
            }
        }

        static boolean nudge(Player var0, float var1, float var2, int var3) {
            if (reflectionDead) {
                return false;
            } else {
                try {
                    ensureReflection(2135067391);
                    Class var19 = var0.getClass();
                    String var5 = "getHandle";
                    Class[] var45 = new Class[0];
                    Method var20 = var19.getMethod(var5, var45);
                    Object[] var47 = new Object[0];
                    Object var14 = var20.invoke(var0, var47);
                    Field var24 = var14.getClass().getField("connection");
                    Object var15 = var24.get(var14);
                    Class var27 = var15.getClass();
                    String var41 = "send";
                    Class[] var49 = new Class[1];
                    Class var8 = packetCls;
                    var49[0] = var8;
                    Method var16 = var27.getMethod(var41, var49);
                    Constructor var29 = pmrCtor;
                    Object[] var43 = new Object[4];
                    Object var56 = vecZero;
                    var43[0] = var56;
                    Object var57 = vecZero;
                    var43[1] = var57;
                    Float var59 = var1;
                    var43[2] = var59;
                    Float var61 = var2;
                    var43[3] = var61;
                    Object var17 = var29.newInstance(var43);
                    Object[] var51 = new Object[1];
                    Constructor var63 = pktCtor;
                    Object[] var65 = new Object[3];
                    int var12 = nudgeSeq++;
                    int var72 = var12 & 1048575;
                    int var68 = 1073741824 + var72;
                    Integer var69 = var68;
                    var65[0] = var69;
                    var65[1] = var17;
                    Set var71 = relAll;
                    var65[2] = var71;
                    Object var64 = var63.newInstance(var65);
                    var51[0] = var64;
                    var16.invoke(var15, var51);
                    return true;
                } catch (Throwable var78) {
                    reflectionDead = true;
                    return false;
                }
            }
        }

        private CamStreamer(Object var1, Method var2, int var3) {
            long var5 = System.nanoTime();
            this.ping = var5;
            long var21 = System.nanoTime();
            this.t0 = var21;
            long var23 = System.nanoTime();
            this.lastFire = var23;
            this.conn = var1;
            this.send = var2;
            ScheduledExecutorService var28 = Executors.newSingleThreadScheduledExecutor(var0 -> {
                String var3x = "bliss-cam-stream";
                Thread var1x = new Thread(var0, var3x);
                var1x.setDaemon(true);
                return var1x;
            });
            this.exec = var28;
            ScheduledExecutorService var19 = this.exec;
            Runnable var30 = this::fire;
            TimeUnit var11 = TimeUnit.NANOSECONDS;
            var19.scheduleWithFixedDelay(var30, 0L, 16666666L, var11);
        }

        private void fire() {
            try {
                long var1 = System.nanoTime();
                long var98 = this.ping;
                if (var1 - var98 > 500000000L) {
                    this.stop(1260880365);
                    return;
                }
            } catch (Throwable var189) {
                return;
            }

            try {
                long var15 = System.nanoTime();
                long var150 = this.lastFire;
                double var108 = (double)(var15 - var150) / 5.0E7;
                double var17 = Math.min(2.0, var108);
                this.lastFire = var15;
                double var86 = this.th;
                double var142 = this.camSpeed;
                double var144 = var142 * var17;
                double var88 = var86 + var144;
                this.th = var88;
                long var155 = this.t0;
                double var116 = (double)(var15 - var155) / 1.0E9;
                double var19 = Math.min(1.0, var116);
                double var45 = var19 * var19;
                double var161 = 2.0 * var19;
                double var122 = 3.0 - var161;
                double var186 = var45 * var122;
                double var52 = Math.cos(this.th);
                double var125 = this.camR;
                double var54 = var52 * var125;
                double var21 = var54 * var186;
                double var61 = Math.sin(this.th);
                double var130 = this.camR;
                double var63 = var61 * var130;
                double var23 = var63 * var186;
                double var135 = this.prevY;
                float var25 = (float)(var21 - var135);
                double var138 = this.prevP;
                float var26 = (float)(var23 - var138);
                this.prevY = var21;
                this.prevP = var23;
                Constructor var79 = pmrCtor;
                Object[] var95 = new Object[4];
                Object var163 = vecZero;
                var95[0] = var163;
                Object var164 = vecZero;
                var95[1] = var164;
                Float var166 = var25;
                var95[2] = var166;
                Float var168 = var26;
                var95[3] = var168;
                Object var27 = var79.newInstance(var95);
                Method var82 = this.send;
                Object var97 = this.conn;
                Object[] var141 = new Object[1];
                Constructor var170 = pktCtor;
                Object[] var173 = new Object[3];
                int var181 = this.seq;
                int var183 = var181 + 1;
                this.seq = var183;
                int var180 = var181 & 1048575;
                int var176 = 1073741824 + var180;
                Integer var177 = var176;
                var173[0] = var177;
                var173[1] = var27;
                Set var179 = relAll;
                var173[2] = var179;
                Object var171 = var170.newInstance(var173);
                var141[0] = var171;
                var82.invoke(var97, var141);
            } catch (Throwable var188) {
            }
        }

        void publish(double var1, double var3, int var5) {
            this.camR = var1;
            this.camSpeed = var3;
            long var13 = System.nanoTime();
            this.ping = var13;
        }

        void stop(int var1) {
            this.exec.shutdownNow();
        }
    }

    private static final class Cover {
        final long prevOffset;
        final boolean prevRel;
        BukkitTask task;

        Cover(long var1, boolean var3, int var4) {
            this.prevOffset = var1;
            this.prevRel = var3;
        }
    }

    static final class OrbitEgg {
        Vector u;
        Vector v;
        double rU;
        double rV;
        double head;
        int rest;
    }

    private static final class State {
        final List<UUID> frags;
        UUID ghost;
        UUID placedCore;
        UUID goldGem;
        boolean holding;
        boolean sealed;
        boolean climaxed;
        boolean finale;
        boolean revealed;
        Location center;
        double angle;
        Location floatFrom;
        int floatTick;
        Vector3f coreAxis;

        private State() {
            ArrayList var2 = new ArrayList();
            this.frags = var2;
            this.floatTick = -1;
        }
    }
}
