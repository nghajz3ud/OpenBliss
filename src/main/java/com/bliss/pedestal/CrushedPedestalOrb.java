package com.bliss.pedestal;

import com.bliss.BlissPlugin;
import com.bliss.core.Particles;
import com.bliss.core.SignatureClock;
import com.bliss.goldgem.GoldGem;
import com.bliss.goldgem.RitualItems;
import com.bliss.honored.DepartureFlight;
import com.bliss.honored.PedestalFinale;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
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

public final class CrushedPedestalOrb implements Listener {
    private static final NamespacedKey KEY = NamespacedKey.fromString("bliss:crushed_pedestal");
    private static final double ORB_R = 2.704;
    private static final double ORB_GAP = 2.0;
    private static final float ORB_LOCAL_R = 0.7F;
    private static final float ORB_SCALE = 7.725714F;
    private static final double CRUSH_TILT_IN = Math.toRadians(27.0);
    private static final double CRUSH_TILT_OUT = Math.toRadians(8.0);
    private static final double CRUSH_SINK = 0.26;
    private static final double CRUSH_JITTER = Math.toRadians(6.0);
    private static final int SNAKE_COUNT = 8;
    private static final float SNAKE_DUST_SIZE = 0.546F;
    private static final double SNAKE_R = 3.46112;
    private static final double SNAKE_MIN_FACTOR = 0.82;
    private static final double SNAKE_MAX_FACTOR = 1.18;
    private static final double SNAKE_HEAD_SPEED = 0.05;
    private static final double SNAKE_TRAIL_ARC = 1.54;
    private static final double SNAKE_PTS_PER_RAD = 12.0;
    private static final double SNAKE_TAIL_FADE = 0.15;
    private static final double SNAKE_RANGE = 48.0;
    private static final double SWEEP_RADIUS = 24.0;
    private static final double LAUNCH_UP = 50.0;
    private static final double FLIGHT_THRUST = 0.012;
    private static final double FLIGHT_SPEED_CAP = 3.0;
    private static final int FLIGHT_THRUST_EVERY = 4;
    private static final double FLIGHT_MIN_SAMPLE = 0.25;
    private static final int BURST_POINTS = 96;
    private static final double BURST_SPEED = 2.6;
    private static final double BURST_LIFT = 1.0;
    private static final int BURST_2_DELAY = 7;
    private static final int BOLT_COUNT = 9;
    private static final double BOLT_SPREAD = 5.0;
    private static final double BOLT_LEN = 7.0;
    private static final int BOLT_SEGMENTS = 7;
    private static final int BOLT_PTS = 8;
    private static final double BOLT_JITTER = 0.8;
    private static final double BOLT_DROP = 3.2;
    private static final double BOLT_Y_VARY = 1.6;
    private static final long FLIGHT_DAY = 80016L;
    private static final int FLIGHT_MAX_TICKS = 3000;
    private static final int FLIGHT_GRACE_TICKS = 20;
    private static final double TOUCH_SLOP = 0.45;
    private static final double TAU = Math.PI * 2;
    private final BlissPlugin plugin;
    private final File file;
    private Location freeLoc;
    private boolean monumentUp;
    private final List<CrushedPedestalOrb.Site> sites;
    private BukkitTask task;
    private final Map<UUID, BukkitTask> flights;
    private final Set<UUID> placedInside;

    public CrushedPedestalOrb(BlissPlugin var1, int var2) {
        ArrayList var4 = new ArrayList();
        this.sites = var4;
        HashMap var13 = new HashMap();
        this.flights = var13;
        HashSet var14 = new HashSet();
        this.placedInside = var14;
        this.plugin = var1;
        File var17 = var1.getDataFolder();
        File var16 = new File(var17, "orb.yml");
        this.file = var16;
        this.load(1230815299);
    }

    public void reconcile(int var1) {
        this.stopSnakes(1035369654);
        this.sites.clear();
        PedestalState var14 = this.plugin.pedestalState(590646643);
        Location var121;
        if (var14 == null) {
            var121 = null;
        } else {
            var121 = var14.mainLoc(15453813);
        }

        Location var15 = var121;
        if (var121 != null && var121.getWorld() == null) {
            var15 = null;
        }

        Location var118;
        if (this.freeLoc != null && this.freeLoc.getWorld() != null) {
            var118 = this.freeLoc;
        } else {
            var118 = null;
        }

        boolean var117;
        if (this.plugin.kitRules(1205957746) != null && this.plugin.kitRules(1205957746).pedestalClosed(12031987)) {
            var117 = true;
        } else {
            var117 = false;
        }

        boolean var55;
        if (this.plugin.globalState(724248306) != null && !this.plugin.globalState(724248306).crushedOrb(1885715107)) {
            var55 = false;
        } else {
            var55 = true;
        }

        boolean var19 = false;
        if (var15 != null) {
            loadArea(var15, 1801753675);
            var19 = this.sweepAt(var15, 2121861317);
        }

        if (var118 != null) {
            loadArea(var118, 1801753675);
            this.sweepAt(var118, 2121861317);
        }

        if (var15 != null) {
            boolean var120;
            if (!this.monumentUp && !var19) {
                var120 = false;
            } else {
                var120 = true;
            }

            if (var117 && (var55 || var120)) {
                var14.unbuildStructure(280713799);
                this.crush(var14, var15, 1789358777);
                double var21 = (double)var15.getBlockY() + 1.0;

                for (Location var24 : var14.structureBlocks(938188002)) {
                    double var138 = (double)var24.getBlockY() + 1.0;
                    var21 = Math.max(var21, var138);
                }

                World var133 = var15.getWorld();
                double var142 = var15.getX() + 0.5;
                double var150 = var21 + 2.0 + 2.704;
                double var157 = var15.getZ() + 0.5;
                Location var85 = new Location(var133, var142, var150, var157);
                this.spawnOrb(var85, 2036791362);
                List var88 = this.sites;
                CrushedPedestalOrb.Site var125 = new CrushedPedestalOrb.Site(var85, 1943192579);
                var88.add(var125);
                if (!this.monumentUp) {
                    this.monumentUp = true;
                    this.save(1316436539);
                }
            } else if (!var117 && var120) {
                var14.unbuildStructure(280713799);
                if (this.monumentUp) {
                    this.monumentUp = false;
                    this.save(1316436539);
                }
            }
        }

        if (var118 != null) {
            this.spawnOrb(var118, 2036791362);
            List var97 = this.sites;
            CrushedPedestalOrb.Site var128 = new CrushedPedestalOrb.Site(var118, 1943192579);
            var97.add(var128);
        }

        if (!this.sites.isEmpty()) {
            this.startSnakes(934887238);
        }
    }

    public void shutdown(int var1) {
        this.stopSnakes(1035369654);
        Set<UUID> var16 = this.flights.keySet();

        for (UUID var5 : new ArrayList<>(var16)) {
            this.stopFlight(var5, 1474519425);
        }
    }

    public Location freeOrb(int var1) {
        Location var6;
        if (this.freeLoc == null) {
            var6 = null;
        } else {
            var6 = this.freeLoc.clone();
        }

        return var6;
    }

    public void setFreeOrb(Location var1, int var2) {
        Location var7;
        if (var1 == null) {
            var7 = null;
        } else {
            var7 = var1.clone();
        }

        this.freeLoc = var7;
        this.save(1316436539);
        this.reconcile(1357222658);
    }

    public boolean clearFreeOrb(int var1) {
        if (this.freeLoc == null) {
            return false;
        } else {
            Location var4 = this.freeLoc;
            this.freeLoc = null;
            this.save(1316436539);
            if (var4.getWorld() != null) {
                loadArea(var4, 1801753675);
                this.sweepAt(var4, 2121861317);
            }

            this.reconcile(1357222658);
            return true;
        }
    }

    public static double restingLift(int var0) {
        return 2.704;
    }

    private void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var12 = YamlConfiguration.loadConfiguration(this.file);
            String var4 = "monument";
            boolean var28 = var12.getBoolean(var4, false);
            this.monumentUp = var28;
            String var13 = var12.getString("free.world");
            if (var13 != null) {
                World var14 = Bukkit.getWorld(var13);
                if (var14 != null) {
                    double var32 = var12.getDouble("free.x");
                    double var34 = var12.getDouble("free.y");
                    double var36 = var12.getDouble("free.z");
                    Location var30 = new Location(var14, var32, var34, var36);
                    this.freeLoc = var30;
                }
            }
        }
    }

    private void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        String var21 = "monument";
        Boolean var32 = this.monumentUp;
        var2.set(var21, var32);
        if (this.freeLoc != null && this.freeLoc.getWorld() != null) {
            String var22 = "free.world";
            String var36 = this.freeLoc.getWorld().getName();
            var2.set(var22, var36);
            String var23 = "free.x";
            Double var41 = this.freeLoc.getX();
            var2.set(var23, var41);
            String var24 = "free.y";
            Double var46 = this.freeLoc.getY();
            var2.set(var24, var46);
            String var25 = "free.z";
            Double var51 = this.freeLoc.getZ();
            var2.set(var25, var51);
        }

        try {
            File var27 = this.file;
            var4.save(var27);
        } catch (IOException var52) {
            Logger var20 = this.plugin.getLogger();
            String var30 = "Could not save orb.yml: " + var52.getMessage();
            var20.warning(var30);
        }
    }

    private void crush(PedestalState var1, Location var2, int var3) {
        World var17 = var2.getWorld();
        List<Location> var18 = var1.structureBlocks(938188002);
        if (!var18.isEmpty()) {
            double var19 = 0.0;

            for (Location var245 : var18) {
                double var187 = horizontal(var2, var245, 1524512382);
                var19 = Math.max(var19, var187);
            }

            if (var19 <= 1.0E-6) {
                var19 = 1.0;
            }

            ThreadLocalRandom var244 = ThreadLocalRandom.current();

            for (Location var23 : var18) {
                Block var24 = var23.getBlock();
                BlockData var25 = var24.getBlockData();
                if (!var25.getMaterial().isAir()) {
                    double var76 = var23.getX();
                    double var142 = var2.getX();
                    double var26 = var76 - var142;
                    double var81 = var23.getZ();
                    double var145 = var2.getZ();
                    double var28 = var81 - var145;
                    double var87 = var26 * var26;
                    double var151 = var28 * var28;
                    double var30 = Math.sqrt(var87 + var151);
                    if (var30 < 1.0E-6) {
                        var26 = 1.0;
                        var28 = 0.0;
                        var30 = 1.0;
                    }

                    double var157 = -var28;
                    double var159 = var157 / var30;
                    double var229 = var26 / var30;
                    Vector var102 = new Vector(var159, 0.0, var229);
                    double var204 = horizontal(var2, var23, 1524512382);
                    double var206 = var204 / var19;
                    double var163 = Math.min(1.0, var206);
                    double var33 = 1.0 - var163;
                    double var107 = CRUSH_TILT_OUT;
                    double var165 = CRUSH_TILT_IN;
                    double var208 = CRUSH_TILT_OUT;
                    double var167 = var165 - var208;
                    double var169 = var167 * var33;
                    double var109 = var107 + var169;
                    double var189 = -CRUSH_JITTER;
                    double var218 = CRUSH_JITTER;
                    double var172 = var244.nextDouble(var189, var218);
                    double var35 = var109 + var172;
                    double var37 = 0.26 * var33;
                    float var214 = (float)var35;
                    float var223 = (float)var102.getX();
                    float var236 = (float)var102.getY();
                    float var239 = (float)var102.getZ();
                    AxisAngle4f var176 = new AxisAngle4f(var214, var223, var236, var239);
                    Quaternionf var117 = new Quaternionf(var176);
                    Vector3f var118 = new Vector3f(0.5F, 0.5F, 0.5F);
                    Vector3f var119 = new Vector3f(var118);
                    Vector3f var179 = new Vector3f(var118);
                    Vector3f var136 = var117.transform(var179);
                    Vector3f var41 = var119.sub(var136);
                    float var137 = var41.y;
                    float var182 = (float)var37;
                    float var138 = var137 - var182;
                    var41.y = var138;
                    BlockData var42 = var25.clone();
                    double var194 = (double)var23.getBlockX();
                    double var226 = (double)var23.getBlockY();
                    double var242 = (double)var23.getBlockZ();
                    Location var124 = new Location(var17, var194, var226, var242);
                    Consumer<BlockDisplay> var197 = var3x -> {
                        var3x.setBlock(var42);
                        var3x.setPersistent(true);
                        Vector3f var8 = new Vector3f(1.0F, 1.0F, 1.0F);
                        Quaternionf var22 = new Quaternionf();
                        Transformation var17x = new Transformation(var41, var117, var8, var22);
                        var3x.setTransformation(var17x);
                        PersistentDataContainer var15 = var3x.getPersistentDataContainer();
                        NamespacedKey var18x = KEY;
                        PersistentDataType var19x = PersistentDataType.BYTE;
                        Byte var21 = (byte)1;
                        var15.set(var18x, var19x, var21);
                    };
                    var17.spawn(var124, BlockDisplay.class, var197);
                    Material var140 = Material.BARRIER;
                    var24.setType(var140, false);
                }
            }
        }
    }

    private static double horizontal(Location var0, Location var1, int var2) {
        double var11 = var1.getX();
        double var28 = var0.getX();
        double var7 = var11 - var28;
        double var16 = var1.getZ();
        double var31 = var0.getZ();
        double var9 = var16 - var31;
        double var22 = var7 * var7;
        double var37 = var9 * var9;
        return Math.sqrt(var22 + var37);
    }

    private void spawnOrb(Location var1, int var2) {
        Location var11 = var1.clone().subtract(0.0, 2.704, 0.0);
        World var15 = var1.getWorld();
        Consumer<Interaction> var23 = var0 -> {
            var0.setInteractionWidth(5.408F);
            var0.setInteractionHeight(5.408F);
            var0.setResponsive(true);
            var0.setPersistent(true);
            PersistentDataContainer var9 = var0.getPersistentDataContainer();
            NamespacedKey var13 = KEY;
            PersistentDataType var3 = PersistentDataType.BYTE;
            Byte var14 = (byte)1;
            var9.set(var13, var3, var14);
        };
        var15.spawn(var11, Interaction.class, var23);
        World var18 = var1.getWorld();
        Consumer<ItemDisplay> var24 = var0 -> {
            ItemStack var2x = RitualItems.goldOrb(1642447782);
            var0.setItemStack(var2x);
            Billboard var16 = Billboard.CENTER;
            var0.setBillboard(var16);
            Brightness var17 = new Brightness(15, 15);
            var0.setBrightness(var17);
            var0.setPersistent(true);
            Vector3f var24x = new Vector3f();
            Quaternionf var27 = new Quaternionf();
            Vector3f var28 = new Vector3f(7.725714F, 7.725714F, 7.725714F);
            Quaternionf var29 = new Quaternionf();
            Transformation var19 = new Transformation(var24x, var27, var28, var29);
            var0.setTransformation(var19);
            var0.setDisplayWidth(11.588572F);
            var0.setDisplayHeight(11.588572F);
            PersistentDataContainer var15x = var0.getPersistentDataContainer();
            NamespacedKey var22 = KEY;
            PersistentDataType var23x = PersistentDataType.BYTE;
            Byte var26 = (byte)1;
            var15x.set(var22, var23x, var26);
        };
        var18.spawn(var1, ItemDisplay.class, var24);
    }

    private static void loadArea(Location var0, int var1) {
        World var6 = var0.getWorld();
        int var7 = var0.getBlockX() >> 4;
        int var8 = var0.getBlockZ() >> 4;

        for (int var9 = -1; var9 <= 1; var9++) {
            for (int var10 = -1; var10 <= 1; var10++) {
                int var29 = var7 + var9;
                int var31 = var8 + var10;
                var6.getChunkAt(var29, var31).load();
            }
        }
    }

    private boolean sweepAt(Location var1, int var2) {
        boolean var11 = false;
        World var15 = var1.getWorld();

        for (Entity var13 : var15.getNearbyEntities(var1, 24.0, 24.0, 24.0)) {
            PersistentDataContainer var25 = var13.getPersistentDataContainer();
            NamespacedKey var31 = KEY;
            PersistentDataType var32 = PersistentDataType.BYTE;
            if (var25.has(var31, var32)) {
                if (var13 instanceof BlockDisplay) {
                    var11 = true;
                }

                var13.remove();
            }
        }

        return var11;
    }

    public void ignoreUntilClear(Player var1, int var2) {
        Set var5 = this.placedInside;
        UUID var7 = var1.getUniqueId();
        var5.add(var7);
    }

    private static boolean touches(Player var0, Location var1, double var2, int var4) {
        Location var9 = var0.getLocation();
        double var10 = var9.distanceSquared(var1);
        if (!(var10 <= var2)) {
            Location var14 = var0.getEyeLocation();
            double var15 = var14.distanceSquared(var1);
            if (!(var15 <= var2)) {
                return false;
            }
        }

        return true;
    }

    private boolean touchingAnyOrb(Player var1, int var2) {
        for (CrushedPedestalOrb.Site var13 : this.sites) {
            World var28 = var13.centre.getWorld();
            World var33 = var1.getWorld();
            if (var28 == var33) {
                Location var35 = var13.centre;
                if (touches(var1, var35, 9.947716000000002, 76152928)) {
                    return true;
                }
            }
        }

        return false;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onOrbUse(PlayerInteractEntityEvent var1) {
        EquipmentSlot var13 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var13 == var3) {
            Entity var11 = var1.getRightClicked();
            if (var11 instanceof Interaction) {
                PersistentDataContainer var19 = var11.getPersistentDataContainer();
                NamespacedKey var31 = KEY;
                PersistentDataType var4 = PersistentDataType.BYTE;
                if (var19.has(var31, var4)) {
                    var1.setCancelled(true);
                    Player var12 = var1.getPlayer();
                    Map var25 = this.flights;
                    UUID var34 = var12.getUniqueId();
                    if (!var25.containsKey(var34)) {
                        Set var28 = this.placedInside;
                        UUID var36 = var12.getUniqueId();
                        if (!var28.contains(var36)) {
                            Location var40 = var11.getLocation().add(0.0, 2.704, 0.0);
                            this.launch(var12, var40, 2145792032);
                        }
                    }
                }
            }
        }
    }

    private void launchTouching(Location var1, int var2) {
        for (Player var13 : var1.getWorld().getPlayers()) {
            UUID var14 = var13.getUniqueId();
            Map var32 = this.flights;
            if (!var32.containsKey(var14)) {
                boolean var15 = touches(var13, var1, 9.947716000000002, 76152928);
                Set var37 = this.placedInside;
                if (var37.contains(var14)) {
                    if (!var15 && !this.touchingAnyOrb(var13, 195502170)) {
                        Set var43 = this.placedInside;
                        var43.remove(var14);
                    }
                } else if (var15) {
                    Location var55 = var1.clone();
                    this.launch(var13, var55, 2145792032);
                }
            }
        }
    }

    private void launch(Player var1, Location var2, int var3) {
        World var12 = var2.getWorld();
        if (var12 != null) {
            if (this.plugin.departureFlight(1434080140) != null) {
                DepartureFlight var22 = this.plugin.departureFlight(1434080140);
                var22.releaseIfFlying(var1, 1565706654);
            }

            if (this.plugin.pedestalFinale(1936651090) != null) {
                PedestalFinale var28 = this.plugin.pedestalFinale(1936651090);
                var28.releaseScreens(var1, 2060286165);
            }

            Location var13 = var2.clone().add(0.0, 50.0, 0.0);
            float var52 = var1.getLocation().getYaw();
            var13.setYaw(var52);
            float var55 = var1.getLocation().getPitch();
            var13.setPitch(var55);
            var1.teleport(var13);
            var1.setFallDistance(0.0F);
            var1.setGliding(true);
            this.beginFlightVeil(var1, 887854358);
            Location var14 = var13.clone().add(0.0, 1.0, 0.0);
            this.ringBurst(var12, var14, 111606373);
            this.fireworkCrack(var12, var14, 1764896559);
            this.thunders(var12, var14, 1882571782);
            BukkitScheduler var45 = Bukkit.getScheduler();
            BlissPlugin var66 = this.plugin;
            Runnable var70 = () -> {
                this.ringBurst(var12, var14, 111606373);
                this.fireworkCrack(var12, var14, 1764896559);
            };
            var45.runTaskLater(var66, var70, 7L);
        }
    }

    private void ringBurst(World var1, Location var2, int var3) {
        for (int var21 = 0; var21 < 96; var21++) {
            double var22 = (double)var21 / 96.0 * (Math.PI * 2);
            Particle var33 = Particle.END_ROD;
            double var37 = var2.getX();
            double var39 = var2.getY();
            double var41 = var2.getZ();
            double var43 = Math.cos(var22);
            double var45 = Math.sin(var22);
            Particles.spawn(var1, var33, var37, var39, var41, 0, var43, 0.0, var45, 2.6, null, true, 954482185);
        }
    }

    private void fireworkCrack(World var1, Location var2, int var3) {
        Sound var6 = Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST;
        var1.playSound(var2, var6, 2.0F, 1.0F);
    }

    private void thunders(World var1, Location var2, int var3) {
        ThreadLocalRandom var22 = ThreadLocalRandom.current();

        for (int var25 = 0; var25 < 9; var25++) {
            double var139 = Math.sqrt(var22.nextDouble());
            double var26 = 5.0 * var139;
            double var28 = var22.nextDouble() * (Math.PI * 2);
            double var68 = var2.getX();
            double var145 = Math.cos(var28);
            double var147 = var145 * var26;
            double var30 = var68 + var147;
            double var73 = var2.getZ();
            double var151 = Math.sin(var28);
            double var153 = var151 * var26;
            double var32 = var73 + var153;
            double var80 = var2.getY() - 3.2;
            double var164 = (var22.nextDouble() * 2.0 - 1.0) * 1.6;
            double var34 = var80 + var164;
            double var36 = 0.0;
            double var38 = 0.0;

            for (int var40 = 0; var40 < 7; var40++) {
                double var41 = (var22.nextDouble() - 0.5) * 0.8;
                double var43 = (var22.nextDouble() - 0.5) * 0.8;

                for (int var45 = 0; var45 < 8; var45++) {
                    double var46 = (double)var45 / 8.0;
                    double var179 = (double)var40 * 1.0;
                    double var113 = var34 + var179;
                    double var183 = var46 * 1.0;
                    double var48 = var113 + var183;
                    double var119 = var30 + var36;
                    double var189 = var41 - var36;
                    double var191 = var189 * var46;
                    double var50 = var119 + var191;
                    double var125 = var32 + var38;
                    double var197 = var43 - var38;
                    double var199 = var197 * var46;
                    double var52 = var125 + var199;
                    Particle var136 = Particle.END_ROD;
                    Particles.spawn(var1, var136, var50, var48, var52, 0, 0.0, 0.0, 0.0, 0.0, null, true, 954482185);
                }

                var36 = var41;
                var38 = var43;
            }
        }
    }

    private void applyFlightDay(Player var1, int var2) {
        if (this.plugin.globalState(724248306) == null || this.plugin.globalState(724248306).shaderEffects(1378906903)) {
            long var10 = var1.getWorld().getFullTime() % 24000L;
            SignatureClock.send(var1, 80016L, var10, 399305443);
        }
    }

    private void beginFlightVeil(Player var1, int var2) {
        final UUID var9 = var1.getUniqueId();
        this.stopFlight(var9, 1474519425);
        this.applyFlightDay(var1, 608065286);
        BukkitRunnable var14 = new BukkitRunnable() {
            int k = 0;
            Location mark;

            public void run() {
                Player var5 = Bukkit.getPlayer(var9);
                if (var5 != null && var5.isOnline() && !var5.isDead()) {
                    int var67 = this.k + 1;
                    this.k = var67;
                    if (this.k % 20 == 0) {
                        CrushedPedestalOrb var22 = CrushedPedestalOrb.this;
                        var22.applyFlightDay(var5, 608065286);
                    }

                    var5.setGliding(true);
                    Location var6 = var5.getLocation();
                    if (this.k == 20) {
                        this.mark = var6;
                    } else if (this.mark != null && this.k > 20 && (this.k - 20) % 4 == 0) {
                        World var37 = var6.getWorld();
                        World var77 = this.mark.getWorld();
                        if (var37 == var77) {
                            Vector var39 = var6.toVector();
                            Vector var80 = this.mark.toVector();
                            Vector var7 = var39.subtract(var80).multiply(0.25);
                            double var8 = var7.length();
                            if (var8 >= 0.25 && var8 < 3.0) {
                                Vector var99 = var6.getDirection().multiply(0.048);
                                Vector var84 = var7.add(var99);
                                var5.setVelocity(var84);
                            }
                        }

                        this.mark = var6;
                    }

                    if (this.k >= 20) {
                        if (this.k >= 3000 || var5.isOnGround()) {
                            CrushedPedestalOrb var60 = CrushedPedestalOrb.this;
                            UUID var89 = var9;
                            var60.stopFlight(var89, 1474519425);
                            this.cancel();
                        }
                    }
                } else {
                    CrushedPedestalOrb var64 = CrushedPedestalOrb.this;
                    UUID var92 = var9;
                    var64.stopFlight(var92, 1474519425);
                    this.cancel();
                }
            }
        };
        BlissPlugin var21 = this.plugin;
        BukkitTask var10 = var14.runTaskTimer(var21, 1L, 1L);
        Map var17 = this.flights;
        var17.put(var9, var10);
    }

    private void stopFlight(UUID var1, int var2) {
        Map var7 = this.flights;
        BukkitTask var5 = (BukkitTask)var7.remove(var1);
        if (var5 != null) {
            var5.cancel();
        }

        Player var6 = Bukkit.getPlayer(var1);
        if (var6 != null) {
            var6.resetPlayerTime();
            if (var5 != null) {
                var6.setGliding(false);
                var6.setFallDistance(0.0F);
            }
        }
    }

    @EventHandler
    public void onToggleGlide(EntityToggleGlideEvent var1) {
        if (!var1.isGliding()) {
            if (var1.getEntity() instanceof Player) {
                Map var9 = this.flights;
                UUID var13 = var1.getEntity().getUniqueId();
                if (var9.containsKey(var13)) {
                    var1.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        UUID var8 = var1.getPlayer().getUniqueId();
        this.stopFlight(var8, 1474519425);
        Set var5 = this.placedInside;
        UUID var11 = var1.getPlayer().getUniqueId();
        var5.remove(var11);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        Player var4 = var1.getPlayer();
        if (this.touchingAnyOrb(var4, 195502170)) {
            Set var9 = this.placedInside;
            UUID var12 = var4.getUniqueId();
            var9.add(var12);
        }
    }

    private static CrushedPedestalOrb.Snake newSnake(Color var0, int var1) {
        ThreadLocalRandom var13 = ThreadLocalRandom.current();
        double var14 = var13.nextDouble() * (Math.PI * 2);
        double var67 = var13.nextDouble();
        double var16 = Math.acos(2.0 * var67 - 1.0);
        double var73 = Math.sin(var16);
        double var94 = Math.cos(var14);
        double var75 = var73 * var94;
        double var98 = Math.sin(var16);
        double var114 = Math.sin(var14);
        double var100 = var98 * var114;
        double var118 = Math.cos(var16);
        Vector var34 = new Vector(var75, var100, var118);
        Vector var41;
        if (Math.abs(var34.getX()) < 0.9) {
            var41 = new Vector(1, 0, 0);
        } else {
            var41 = new Vector(0, 1, 0);
        }

        CrushedPedestalOrb.Snake var42 = new CrushedPedestalOrb.Snake();
        Vector var50 = var34.clone();
        Vector var52 = var50.crossProduct(var41).normalize();
        var42.u = var52;
        Vector var54 = var34.clone();
        Vector var82 = var42.u;
        Vector var56 = var54.crossProduct(var82).normalize();
        var42.v = var56;
        double var107 = var13.nextDouble() * 0.36;
        double var86 = 0.82 + var107;
        double var59 = 3.46112 * var86;
        var42.rU = var59;
        double var112 = var13.nextDouble() * 0.36;
        double var90 = 0.82 + var112;
        double var63 = 3.46112 * var90;
        var42.rV = var63;
        DustOptions var65 = new DustOptions(var0, 0.546F);
        var42.dust = var65;
        return var42;
    }

    private static Color snakeColour(int var0, int var1) {
        return PedestalReviveRitual.gemColor(GoldGem.byBit(var0, 621438557), 104246386);
    }

    private void startSnakes(int var1) {
        this.stopSnakes(1035369654);
        BukkitRunnable var3 = new BukkitRunnable() {
            public void run() {
                if (CrushedPedestalOrb.this.sites.isEmpty()) {
                    this.cancel();
                    CrushedPedestalOrb.this.task = null;
                } else {
                    for (CrushedPedestalOrb.Site var4 : CrushedPedestalOrb.this.sites) {
                        if (var4.centre.getWorld() != null && CrushedPedestalOrb.anyoneNear(var4.centre, 1662988422)) {
                            CrushedPedestalOrb var24 = CrushedPedestalOrb.this;
                            Location var30 = var4.centre;
                            var24.launchTouching(var30, 1754791064);
                            CrushedPedestalOrb var26 = CrushedPedestalOrb.this;
                            var26.drawSnakes(var4, 1331978687);
                        }
                    }
                }
            }
        };
        BlissPlugin var10 = this.plugin;
        BukkitTask var8 = var3.runTaskTimer(var10, 1L, 1L);
        this.task = var8;
    }

    private void stopSnakes(int var1) {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    private static boolean anyoneNear(Location var0, int var1) {
        Iterator var6 = var0.getWorld().getPlayers().iterator();

        while (var6.hasNext()) {
            Location var17 = ((Player)var6.next()).getLocation();
            if (var17.distanceSquared(var0) <= 2304.0) {
                return true;
            }
        }

        return false;
    }

    private void drawSnakes(CrushedPedestalOrb.Site var1, int var2) {
        World var22 = var1.centre.getWorld();
        ThreadLocalRandom var23 = ThreadLocalRandom.current();
        int var24 = 0;

        while (true) {
            int var100 = var1.snakes.length;
            if (var24 >= var100) {
                return;
            }

            CrushedPedestalOrb.Snake[] var45 = var1.snakes;
            CrushedPedestalOrb.Snake var25 = var45[var24];
            if (var25 == null) {
                var25 = newSnake(snakeColour(var24, 213961500), 669577394);
                CrushedPedestalOrb.Snake[] var52 = var1.snakes;
                var52[var24] = var25;
            }

            double var105 = var25.head + 0.05;
            var25.head = var105;
            if (var25.head >= Math.PI * 2) {
                double var109 = var25.head - (Math.PI * 2);
                var25.head = var109;
            }

            if (var25.swept < 1.54) {
                double var160 = var25.swept + 0.05;
                double var113 = Math.min(1.54, var160);
                var25.swept = var113;
            }

            double var65 = var25.head;
            double var138 = var25.swept;
            double var26 = var65 - var138;
            int var122 = (int)Math.ceil(var25.swept * 12.0);
            int var28 = Math.max(1, var122);

            for (int var29 = 0; var29 <= var28; var29++) {
                double var74 = (double)var29;
                double var141 = (double)var28;
                double var30 = var74 / var141;
                double var79 = var23.nextDouble();
                double var186 = 0.85 * var30;
                double var145 = 0.15 + var186;
                if (!(var79 > var145)) {
                    double var148 = var25.head;
                    double var150 = var148 - var26;
                    double var152 = var150 * var30;
                    double var32 = var26 + var152;
                    double var34 = Math.cos(var32);
                    double var36 = Math.sin(var32);
                    Location var96 = var1.centre.clone();
                    double var165 = var25.rU;
                    double var126 = var34 * var165;
                    double var169 = var25.u.getX();
                    double var128 = var126 * var169;
                    double var195 = var25.rV;
                    double var173 = var36 * var195;
                    double var199 = var25.v.getX();
                    double var175 = var173 * var199;
                    double var130 = var128 + var175;
                    double var202 = var25.rU;
                    double var179 = var34 * var202;
                    double var206 = var25.u.getY();
                    double var181 = var179 * var206;
                    double var224 = var25.rV;
                    double var210 = var36 * var224;
                    double var228 = var25.v.getY();
                    double var212 = var210 * var228;
                    double var183 = var181 + var212;
                    double var231 = var25.rU;
                    double var216 = var34 * var231;
                    double var235 = var25.u.getZ();
                    double var218 = var216 * var235;
                    double var243 = var25.rV;
                    double var239 = var36 * var243;
                    double var247 = var25.v.getZ();
                    double var241 = var239 * var247;
                    double var220 = var218 + var241;
                    Location var38 = var96.add(var130, var183, var220);
                    Particle var132 = Particle.DUST;
                    DustOptions var249 = var25.dust;
                    Particles.spawn(var22, var132, var38, 1, 0.0, 0.0, 0.0, 0.0, var249, true, 1748031581);
                }
            }

            var24++;
        }
    }

    private static final class Site {
        final Location centre;
        final CrushedPedestalOrb.Snake[] snakes;

        Site(Location var1, int var2) {
            CrushedPedestalOrb.Snake[] var13 = new CrushedPedestalOrb.Snake[8];
            this.snakes = var13;
            Location var15 = var1.clone();
            this.centre = var15;
            int var6 = 0;

            while (true) {
                int var18 = this.snakes.length;
                if (var6 >= var18) {
                    return;
                }

                CrushedPedestalOrb.Snake[] var12 = this.snakes;
                CrushedPedestalOrb.Snake var21 = CrushedPedestalOrb.newSnake(CrushedPedestalOrb.snakeColour(var6, 213961500), 669577394);
                var12[var6] = var21;
                var6++;
            }
        }
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
}
