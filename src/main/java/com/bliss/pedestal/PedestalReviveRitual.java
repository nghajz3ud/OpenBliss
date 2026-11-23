package com.bliss.pedestal;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.GemType;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TeamManager;
import com.bliss.gems.GrantedEffect;
import com.bliss.goldgem.GoldGemManager;
import com.bliss.honored.JuggernautGemGuardListener;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import io.papermc.paper.entity.LookAnchor;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public final class PedestalReviveRitual {
    private static final double BASE_RADIUS = 6.0;
    private static final double END_RADIUS = 0.5;
    private static final double ANG_VEL_MIN = 1.0;
    private static final double ANG_VEL_MAX = 12.0;
    private static final double PHASE7_Y_OFFSET = 0.3;
    private static final int SHRINK_START_TICK = 229;
    private static final int SHRINK_END_TICK = 379;
    private static final int FIRST_ROLL_TICK = 285;
    private static final int PHASE7_END = 379;
    private static final int MAX_ROLL_INTERVAL = 14;
    private static final int MIN_ROLL_INTERVAL = 2;
    private static final long CHEST_RISE_DELAY_TICKS = 50L;
    private static final int CHEST_RISE_TICKS = 60;
    private static final int HOLD_AT_TOP_TICKS = 30;
    private static final double CHEST_OFFSET = 0.3;
    private final BlissPlugin plugin;
    private final PedestalState state;
    private final PlayerDataStore players;
    private final BlissCoreItems items;
    private final Random random;
    private Map<GemType, ItemDisplay> activeGems;
    private static World weatherWorld;
    private static boolean savedStorm;
    private static boolean savedThundering;
    private static int savedWeatherDuration;
    private static int savedThunderDuration;
    private static final int STILL_TICKS = 60;
    private static final double STILL_EPSILON_SQ = 0.0025000000000000005;
    private static final double SETTLE_ABORT_RADIUS_SQ = 0.5625;
    private static final double SETTLE_ABORT_Y = 1.0;
    private static final int SETTLE_TIMEOUT_TICKS = 600;

    public PedestalReviveRitual(BlissPlugin var1, PedestalState var2, PlayerDataStore var3, BlissCoreItems var4, int var5) {
        Random var7 = new Random();
        this.random = var7;
        this.plugin = var1;
        this.state = var2;
        this.players = var3;
        this.items = var4;
    }

    public void start(Player var1, int var2) {
        GoldGemManager var6 = this.plugin.goldGemManager(256518991);
        UUID var21 = var1.getUniqueId();
        if (var6.isRepurposed(var21, 1313846040)) {
            PlayerDataStore var10 = this.plugin.players(1969902410);
            UUID var23 = var1.getUniqueId();
            GemType var12 = var10.get(var23, 1264010596).gem(1413186337);
            GemType var24 = GemType.NONE;
            if (var12 == var24) {
                Component var26 = Text.legacy("&cYour soul has been repurposed. The pedestal has nothing left to restore.", 373192400);
                var1.sendMessage(var26);
                this.state.removeBeam(1137327363);
                this.state.resetRitual(1772872156);
                restoreWeather(781471445);
                return;
            }
        }

        this.state.setPedestalBusy(true, 511184142);
        this.awaitStillness(var1, 1135886484);
    }

    private void awaitStillness(final Player var1, int var2) {
        final UUID var7 = var1.getUniqueId();
        BukkitRunnable var9 = new BukkitRunnable() {
            Location last;
            int still;
            int waited;

            {
                Location var18 = var1.getLocation().clone();
                this.last = var18;
                this.still = 0;
                this.waited = 0;
            }

            public void run() {
                if (var1.isOnline() && !var1.isDead() && PedestalReviveRitual.this.state.pedestalBusy(1861024962)) {
                    Location var11 = PedestalReviveRitual.this.state.mainLoc(15453813);
                    if (var11 != null && var11.getWorld() != null) {
                        Location var12 = var1.getLocation();
                        Location var13 = var11.clone().add(0.5, 1.0, 0.5);
                        double var41 = var12.getX();
                        double var108 = var13.getX();
                        double var14 = var41 - var108;
                        double var46 = var12.getZ();
                        double var111 = var13.getZ();
                        double var16 = var46 - var111;
                        World var51 = var12.getWorld();
                        World var94 = var11.getWorld();
                        if (var51.equals(var94)) {
                            double var55 = var14 * var14;
                            double var117 = var16 * var16;
                            if (!(var55 + var117 > 0.5625)) {
                                double var61 = var12.getY();
                                double var122 = var13.getY();
                                if (!(Math.abs(var61 - var122) > 1.0)) {
                                    int var96 = this.waited + 1;
                                    this.waited = var96;
                                    if (var96 > 600) {
                                        this.cancel();
                                        PedestalReviveRitual.this.releaseReservation(1714763430);
                                        return;
                                    }

                                    Location var99 = this.last;
                                    if (var12.distanceSquared(var99) > 0.0025000000000000005) {
                                        this.still = 0;
                                        Location var107 = var12.clone();
                                        this.last = var107;
                                    } else {
                                        int var101 = this.still + 1;
                                        this.still = var101;
                                    }

                                    if (this.still >= 60) {
                                        this.cancel();
                                        PedestalReviveRitual var81 = PedestalReviveRitual.this;
                                        Player var104 = var1;
                                        UUID var131 = var7;
                                        var81.engageLock(var104, var131, 337896695);
                                    }

                                    return;
                                }
                            }
                        }

                        this.cancel();
                        PedestalReviveRitual.this.releaseReservation(1714763430);
                    } else {
                        this.cancel();
                        PedestalReviveRitual.this.releaseReservation(1714763430);
                    }
                } else {
                    this.cancel();
                    PedestalReviveRitual.this.releaseReservation(1714763430);
                }
            }
        };
        BlissPlugin var12 = this.plugin;
        var9.runTaskTimer(var12, 1L, 1L);
    }

    private void releaseReservation(int var1) {
        this.state.setPedestalBusy(false, 511184142);
    }

    private void engageLock(Player var1, UUID var2, int var3) {
        PedestalState var12 = this.state;
        var12.setRevivalLocked(var2, true, 1934094553);
        PlayerDataStore var14 = this.players;
        var14.get(var2, 1264010596).setRestorationLock(true, 2055613441);
        var1.setInvulnerable(true);
        Location var11 = this.state.mainLoc(15453813).clone().add(0.5, -3.0, 0.5);
        this.beginAnimation(var1, var11, 1591163061);
    }

    private int smoothLevelHead(final Player var1, int var2) {
        final float var11 = var1.getLocation().getYaw();
        final float var12 = var1.getLocation().getPitch();
        float var32 = (float)(Math.ceil((double)var11 / 90.0 - 0.5) * 90.0);
        final float var14 = Location.normalizeYaw(var32 - var11);
        if (Math.abs(var14) < 1.0F && Math.abs(var12) < 1.0F) {
            return 0;
        } else {
            BukkitRunnable var42 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    if (!var1.isOnline()) {
                        this.cancel();
                    } else {
                        int var48 = this.t + 1;
                        this.t = var48;
                        double var62 = (double)this.t / 40.0;
                        double var7 = Math.min(1.0, var62);
                        double var45;
                        if (var7 < 0.5) {
                            double var43 = 2.0 * var7;
                            var45 = var43 * var7;
                        } else {
                            double var74 = Math.pow(-2.0 * var7 + 2.0, 2.0) / 2.0;
                            var45 = 1.0 - var74;
                        }

                        float var27 = var11;
                        double var51 = (double)var14;
                        float var55 = (float)(var51 * var45);
                        float var11x = var27 + var55;
                        double var31 = (double)var12;
                        double var78 = 1.0 - var45;
                        float var12x = (float)(var31 * var78);
                        Player var37 = var1;
                        PedestalReviveRitual.aimHead(var37, var11x, var12x, 2115004015);
                        if (this.t >= 40) {
                            this.cancel();
                        }
                    }
                }
            };
            BlissPlugin var48 = this.plugin;
            var42.runTaskTimer(var48, 0L, 1L);
            return 40;
        }
    }

    private static void aimHead(Player var0, float var1, float var2, int var3) {
        double var16 = Math.toRadians((double)var1);
        double var18 = Math.toRadians((double)var2);
        double var40 = -Math.cos(var18);
        double var63 = Math.sin(var16);
        double var20 = var40 * var63;
        double var22 = -Math.sin(var18);
        double var52 = Math.cos(var18);
        double var67 = Math.cos(var16);
        double var24 = var52 * var67;
        Location var26 = var0.getEyeLocation();
        double var59 = var26.getX();
        double var69 = var20 * 8.0;
        double var61 = var59 + var69;
        double var72 = var26.getY();
        double var78 = var22 * 8.0;
        double var74 = var72 + var78;
        double var81 = var26.getZ();
        double var87 = var24 * 8.0;
        double var83 = var81 + var87;
        LookAnchor var89 = LookAnchor.EYES;
        var0.lookAt(var61, var74, var83, var89);
    }

    private void beginAnimation(Player var1, Location var2, int var3) {
        if (var1.isOnline() && !var1.isDead()) {
            var1.setGravity(false);
            Map var35 = this.spawnGemRing(var2, 301184481);
            this.activeGems = var35;
            int var10 = this.smoothLevelHead(var1, 287016700);
            Map var11 = this.activeGems;
            long var27;
            if (var10 > 0) {
                var27 = (long)var10 + 8L;
            } else {
                var27 = 1L;
            }

            long var12 = var27 + 50L;
            BukkitScheduler var31 = Bukkit.getScheduler();
            BlissPlugin var38 = this.plugin;
            Runnable var46 = () -> {
                if (!var1.isOnline()) {
                    this.removeGems(var11, 1073348091);
                    UUID var14 = var1.getUniqueId();
                    this.cleanupAbort(var14, 751263136);
                } else {
                    this.raiseGemsToChest(var1, var11, var2, 192058598);
                }
            };
            var31.runTaskLater(var38, var46, var12);
        } else {
            UUID var40 = var1.getUniqueId();
            this.cleanupAbort(var40, 751263136);
        }
    }

    private Map<GemType, ItemDisplay> spawnGemRing(Location var1, int var2) {
        EnumMap var3 = new EnumMap<>(GemType.class);
        GemType var34 = GemType.PUFF;
        Location var59 = var1.clone().add(0.0, 1.5, 6.0);
        GemType var81 = GemType.PUFF;
        ItemDisplay var43 = this.spawnGemDisplay(var59, var81, 5904167);
        var3.put(var34, var43);
        GemType var35 = GemType.WEALTH;
        Location var62 = var1.clone().add(-6.0, 1.5, 0.0);
        GemType var84 = GemType.WEALTH;
        ItemDisplay var45 = this.spawnGemDisplay(var62, var84, 5904167);
        var3.put(var35, var45);
        GemType var36 = GemType.FLUX;
        Location var65 = var1.clone().add(6.0, 1.5, 0.0);
        GemType var87 = GemType.FLUX;
        ItemDisplay var47 = this.spawnGemDisplay(var65, var87, 5904167);
        var3.put(var36, var47);
        GemType var37 = GemType.ASTRA;
        Location var68 = var1.clone().add(0.0, 1.5, -6.0);
        GemType var90 = GemType.ASTRA;
        ItemDisplay var49 = this.spawnGemDisplay(var68, var90, 5904167);
        var3.put(var37, var49);
        GemType var38 = GemType.STRENGTH;
        Location var71 = var1.clone().add(-4.0, 1.5, 4.0);
        GemType var93 = GemType.STRENGTH;
        ItemDisplay var51 = this.spawnGemDisplay(var71, var93, 5904167);
        var3.put(var38, var51);
        GemType var39 = GemType.LIFE;
        Location var74 = var1.clone().add(4.0, 1.5, 4.0);
        GemType var96 = GemType.LIFE;
        ItemDisplay var53 = this.spawnGemDisplay(var74, var96, 5904167);
        var3.put(var39, var53);
        GemType var40 = GemType.SPEED;
        Location var77 = var1.clone().add(-4.0, 1.5, -4.0);
        GemType var99 = GemType.SPEED;
        ItemDisplay var55 = this.spawnGemDisplay(var77, var99, 5904167);
        var3.put(var40, var55);
        GemType var41 = GemType.FIRE;
        Location var80 = var1.clone().add(4.0, 1.5, -4.0);
        GemType var102 = GemType.FIRE;
        ItemDisplay var57 = this.spawnGemDisplay(var80, var102, 5904167);
        var3.put(var41, var57);
        return var3;
    }

    private void raiseGemsToChest(final Player var1, final Map<GemType, ItemDisplay> var2, final Location var3, int var4) {
        final double var15 = var3.getY() + 1.5;
        final double var17 = var1.getLocation().getY() + 0.3;
        BukkitRunnable var29 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                if (!var1.isOnline()) {
                    this.cancel();
                    PedestalReviveRitual var16 = PedestalReviveRitual.this;
                    Map var42 = var2;
                    var16.removeGems(var42, 1073348091);
                    PedestalReviveRitual var18 = PedestalReviveRitual.this;
                    UUID var45 = var1.getUniqueId();
                    var18.cleanupAbort(var45, 751263136);
                } else {
                    int var47 = this.t + 1;
                    this.t = var47;
                    double var61 = (double)this.t / 60.0;
                    double var8 = Math.min(1.0, var61);
                    double var40;
                    if (var8 < 0.5) {
                        double var38 = 2.0 * var8;
                        var40 = var38 * var8;
                    } else {
                        double var73 = Math.pow(-2.0 * var8 + 2.0, 2.0) / 2.0;
                        var40 = 1.0 - var73;
                    }

                    Map var30 = var2;
                    double var49 = var15;
                    double var83 = var17;
                    double var103 = var15;
                    double var85 = var83 - var103;
                    double var87 = var85 * var40;
                    double var51 = var49 + var87;
                    PedestalReviveRitual.setGemRingY(var30, var51, 2021748199);
                    if (this.t >= 60) {
                        this.cancel();
                        PedestalReviveRitual var76 = PedestalReviveRitual.this;
                        Player var90 = var1;
                        Location var100 = var3;
                        Map var108 = var2;
                        PedestalReviveRitual.RitualTask var34 = var76.new RitualTask(var90, var100, var108, 427601511);
                        BlissPlugin var56 = PedestalReviveRitual.this.plugin;
                        var34.runTaskTimer(var56, 1L, 1L);
                    }
                }
            }
        };
        BlissPlugin var32 = this.plugin;
        var29.runTaskTimer(var32, 1L, 1L);
    }

    private static void setGemRingY(Map<GemType, ItemDisplay> var0, double var1, int var3) {
        for (ItemDisplay var8 : var0.values()) {
            if (var8 != null && !var8.isDead()) {
                Location var9 = var8.getLocation();
                var9.setY(var1);
                var9.setYaw(0.0F);
                var9.setPitch(0.0F);
                var8.teleport(var9);
            }
        }
    }

    private ItemDisplay spawnGemDisplay(Location var1, GemType var2, int var3) {
        World var9 = var1.getWorld();
        Consumer<ItemDisplay> var12 = var2x -> {
            var2x.setPersistent(false);
            BlissCoreItems var21 = this.items;
            ItemStack var22 = var21.gemDisplay$1463922612(var2, 1977498201);
            var2x.setItemStack(var22);
            ItemDisplayTransform var23 = ItemDisplayTransform.HEAD;
            var2x.setItemDisplayTransform(var23);
            Vector3f var6 = new Vector3f(0.0F, 0.0F, 0.0F);
            AxisAngle4f var29 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
            Vector3f var30 = new Vector3f(0.65F, 0.65F, 0.65F);
            AxisAngle4f var32 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
            Transformation var24 = new Transformation(var6, var29, var30, var32);
            var2x.setTransformation(var24);
            var2x.setGlowing(true);
            Color var27 = gemColor(var2, 104246386);
            var2x.setGlowColorOverride(var27);
            var2x.setTeleportDuration(1);
        };
        return (ItemDisplay)var9.spawn(var1, ItemDisplay.class, var12);
    }

    private void removeGems(Map<GemType, ItemDisplay> var1, int var2) {
        if (var1 != null) {
            for (ItemDisplay var5 : var1.values()) {
                if (var5 != null && !var5.isDead()) {
                    var5.remove();
                }
            }

            var1.clear();
        }
    }

    public static void forceStormAndThunder(World var0, int var1) {
        if (var0 != null) {
            if (weatherWorld == null) {
                weatherWorld = var0;
                savedStorm = var0.hasStorm();
                savedThundering = var0.isThundering();
                savedWeatherDuration = var0.getWeatherDuration();
                savedThunderDuration = var0.getThunderDuration();
                var0.setStorm(true);
                var0.setThundering(true);
                var0.setWeatherDuration(20000);
                var0.setThunderDuration(20000);
            }
        }
    }

    public static void restoreWeather(int var0) {
        if (weatherWorld != null) {
            try {
                World var5 = weatherWorld;
                boolean var2 = savedStorm;
                var5.setStorm(var2);
                World var6 = weatherWorld;
                boolean var11 = savedThundering;
                var6.setThundering(var11);
                World var7 = weatherWorld;
                int var13 = Math.max(savedWeatherDuration, 0);
                var7.setWeatherDuration(var13);
                World var8 = weatherWorld;
                int var15 = Math.max(savedThunderDuration, 0);
                var8.setThunderDuration(var15);
            } catch (Throwable var16) {
            }

            weatherWorld = null;
        }
    }

    public void shutdownRestore(int var1) {
        UUID var5 = this.state.revivingPlayer(203133736);
        if (var5 != null && this.state.pedestalBusy(1861024962)) {
            this.cleanupAbort(var5, false, 842101988);
        }
    }

    private void cleanupAbort(UUID var1, int var2) {
        this.cleanupAbort(var1, true, 842101988);
    }

    private void cleanupAbort(UUID var1, boolean var2, int var3) {
        Map var27 = this.activeGems;
        this.removeGems(var27, 1073348091);
        this.activeGems = null;
        PlayerDataStore var10 = this.players;
        var10.get(var1, 1264010596).setRestorationLock(false, 2055613441);
        PedestalState var13 = this.state;
        var13.setRevivalLocked(var1, false, 1934094553);
        this.state.setPedestalBusy(false, 511184142);
        restoreWeather(781471445);
        Player var7 = Bukkit.getPlayer(var1);
        if (var7 != null) {
            var7.setGravity(true);
            var7.setInvulnerable(false);
            this.clearGemGlow(var7, 1603907024);
        }

        this.state.removeBeam(1137327363);
        if (var2) {
            this.state.resetRitual(1772872156);
        }
    }

    private void clearGemsInInventory(Player var1, int var2) {
        ItemStack[] var6 = var1.getInventory().getContents();
        int var7 = 0;

        while (true) {
            int var20 = var6.length;
            if (var7 >= var20) {
                return;
            }

            ItemStack var8 = var6[var7];
            if (var8 != null && BlissCoreItems.gemIdOf(var8, 382441079) > 0) {
                PlayerInventory var19 = var1.getInventory();
                var19.setItem(var7, null);
            }

            var7++;
        }
    }

    private void rollRandomGem(Player var1, int var2) {
        PlayerDataStore var10 = this.players;
        UUID var20 = var1.getUniqueId();
        PlayerData var8 = var10.get(var20, 1264010596);
        GemType var13 = var8.gem(1413186337);
        Random var22 = this.random;
        GemType var9 = GemType.randomDifferent(var13, var22);
        var8.setGem(var9, 2137390487);
        Component var27 = Text.legacy("\ud83d\udd2e &bYou have traded your current gem to " + tradeLabel(var9, 1784125127), 373192400);
        var1.sendMessage(var27);
        this.refreshGemItem(var1, var8, 1488130596);
        Location var30 = var1.getLocation();
        Sound var32 = Sound.BLOCK_AMETHYST_BLOCK_BREAK;
        var1.playSound(var30, var32, 1.0F, 1.0F);
        this.applyGemGlow(var1, var9, 747492249);
    }

    private void refreshGemItem(Player var1, PlayerData var2, int var3) {
        int var10 = BlissCoreItems.findGemSlot(var1, 1702349856);
        this.clearGemsInInventory(var1, 1855289737);
        if (!JuggernautGemGuardListener.isJuggernaut(var1, 859003337)) {
            GemType var16 = var2.gem(1413186337);
            GemType var18 = GemType.NONE;
            if (var16 != var18) {
                BlissCoreItems var20 = this.items;
                GemType var22 = var2.gem(1413186337);
                boolean var23 = var2.tier2(629984660);
                int var24 = var2.energy(1160853318);
                ItemStack var21 = var20.gemItem(var22, var23, var24, 256965471);
                BlissCoreItems.giveGemOrHold(var1, var10, var21, 2133739666);
            }
        }
    }

    private void pedestalLightning(Location var1, int var2) {
        if (var1 != null) {
            for (int var11 = 0; var11 < 3; var11++) {
                int var37 = this.random.nextInt(9);
                int var12 = 7 + var37;
                int var40 = this.random.nextInt(9);
                int var13 = 7 + var40;
                int var43 = this.random.nextInt(9);
                int var14 = 7 + var43;
                int var46 = this.random.nextInt(9);
                int var15 = 7 + var46;
                Location[] var29 = new Location[4];
                Location var59 = var1.clone();
                double var71 = (double)(-var13);
                double var89 = (double)var15;
                Location var60 = var59.add(var71, 0.0, var89);
                var29[0] = var60;
                Location var62 = var1.clone();
                double var74 = (double)var12;
                double var93 = (double)(-var14);
                Location var63 = var62.add(var74, 0.0, var93);
                var29[1] = var63;
                Location var65 = var1.clone();
                double var77 = (double)var12;
                double var96 = (double)var15;
                Location var66 = var65.add(var77, 0.0, var96);
                var29[2] = var66;
                Location var68 = var1.clone();
                double var81 = (double)(-var13);
                double var100 = (double)(-var14);
                Location var69 = var68.add(var81, 0.0, var100);
                var29[3] = var69;
                int var49 = this.random.nextInt(4);
                Location var17 = var29[var49];
                World var33 = var1.getWorld();
                var33.strikeLightning(var17);
            }
        }
    }

    private void applyGemGlow(Player var1, GemType var2, int var3) {
        Scoreboard var12 = Bukkit.getScoreboardManager().getMainScoreboard();

        for (GemType var16 : GemType.values()) {
            GemType var39 = GemType.NONE;
            if (var16 != var39) {
                String var41 = TeamManager.pedRollTeamName(var16, 965817203);
                Team var27 = var12.getTeam(var41);
                String var43 = var1.getName();
                TeamManager.safeRemoveEntry(var27, var43, 1412413626);
            }
        }

        GemType var44 = GemType.NONE;
        if (var2 != var44) {
            String var46 = TeamManager.pedRollTeamName(var2, 965817203);
            Team var54 = var12.getTeam(var46);
            if (var54 != null) {
                String var48 = var1.getName();
                var54.addEntry(var48);
                NamedTextColor var50 = gemNamedColor(var2, 1688568734);
                TeamManager.forceBroadcastTeam(var54, var50, 612238929);
            }

            PotionEffectType var6 = PotionEffectType.GLOWING;
            PotionEffect var34 = new PotionEffect(var6, 200, 0, false, false, false);
            var1.addPotionEffect(var34, true);
            GrantedEffect.grant(var1, var34, 491118892);
        }
    }

    private void clearGemGlow(Player var1, int var2) {
        Scoreboard var5 = Bukkit.getScoreboardManager().getMainScoreboard();

        for (GemType var9 : GemType.values()) {
            GemType var23 = GemType.NONE;
            if (var9 != var23) {
                String var25 = TeamManager.pedRollTeamName(var9, 965817203);
                Team var20 = var5.getTeam(var25);
                String var27 = var1.getName();
                TeamManager.safeRemoveEntry(var20, var27, 1412413626);
            }
        }

        PotionEffectType var28 = PotionEffectType.GLOWING;
        GrantedEffect.strip(var1, var28, 553187889);
    }

    private static NamedTextColor gemNamedColor(GemType var0, int var1) {
        return switch (var0) {
            case STRENGTH-> NamedTextColor.DARK_RED;
            case FIRE-> NamedTextColor.GOLD;
            case WEALTH-> NamedTextColor.DARK_GREEN;
            case SPEED-> NamedTextColor.YELLOW;
            case PUFF-> NamedTextColor.WHITE;
            case ASTRA-> NamedTextColor.DARK_PURPLE;
            case LIFE-> NamedTextColor.LIGHT_PURPLE;
            case FLUX-> NamedTextColor.AQUA;
            default -> NamedTextColor.WHITE;
        };
    }

    private static ChatColor gemChatColor(GemType var0) {
        return switch (var0) {
            case STRENGTH-> ChatColor.DARK_RED;
            case FIRE-> ChatColor.GOLD;
            case WEALTH-> ChatColor.DARK_GREEN;
            case SPEED-> ChatColor.YELLOW;
            case PUFF-> ChatColor.WHITE;
            case ASTRA-> ChatColor.DARK_PURPLE;
            case LIFE-> ChatColor.LIGHT_PURPLE;
            case FLUX-> ChatColor.AQUA;
            default -> ChatColor.WHITE;
        };
    }

    public static Color gemColor(GemType var0, int var1) {
        return switch (var0) {
            case STRENGTH-> Color.fromRGB(241, 3, 3);
            case FIRE-> Color.fromRGB(254, 129, 32);
            case WEALTH-> Color.fromRGB(0, 166, 44);
            case SPEED-> Color.fromRGB(254, 253, 23);
            case PUFF-> Color.fromRGB(239, 239, 239);
            case ASTRA-> Color.fromRGB(160, 31, 255);
            case LIFE-> Color.fromRGB(255, 20, 147);
            case FLUX-> Color.fromRGB(94, 215, 255);
            default -> Color.WHITE;
        };
    }

    private static String tradeLabel(GemType var0, int var1) {
        return switch (var0) {
            case STRENGTH-> "&4&lꜱᴛʀᴇɴɢᴛʜ";
            case FIRE-> "§6§lꜰɪʀᴇ";
            case WEALTH-> "§2§lᴡᴇᴀʟᴛʜ";
            case SPEED-> "&e&lsᴘᴇᴇᴅ";
            case PUFF-> "&f§lᴘᴜꜰꜰ";
            case ASTRA-> "&5&lᴀsᴛʀᴀ";
            case LIFE-> "§d§lʟɪꜰᴇ";
            case FLUX-> "§9§lꜰʟᴜx";
            default -> var0.coloredName();
        };
    }

    private final class RitualTask extends BukkitRunnable {
        private final Player player;
        private final Location focus;
        private final Map<GemType, ItemDisplay> gems;
        private int liftTick;
        private int holdTick;
        private int phase7Tick;
        private double phase7Angle;
        private int nextRollTick = 285;
        private int phase = 0;
        private int currentEnergy = 1;

        RitualTask(Player nullx, Location nullxx, Map<GemType, ItemDisplay> nullxxx, int var5) {
            this.player = nullx;
            Location var20 = nullxx.clone();
            this.focus = var20;
            this.gems = nullxxx;
        }

        public void run() {
            if (!this.player.isOnline()) {
                this.abort(47503386);
            } else if (this.player.isDead()) {
                this.abort(47503386);
            } else {
                PedestalState.Ritual var12 = PedestalReviveRitual.this.state.ritual(1751702735);
                PedestalState.Ritual var2 = PedestalState.Ritual.REVIVE;
                if (var12 != var2) {
                    this.abort(47503386);
                } else {
                    switch (this.phase) {
                        case 0:
                            this.lift(400290817);
                            break;
                        case 1:
                            this.spawnArmorStands(868565285);
                            break;
                        case 2:
                            this.holdAtApex(2137765700);
                            break;
                        case 3:
                            this.phase7Rotate(1508845667);
                            break;
                        case 4:
                            this.finish(565543777);
                    }
                }
            }
        }

        private void lift(int var1) {
            Location var10 = this.player.getLocation().add(0.0, 0.02, 0.0);
            Player var15 = this.player;
            var15.teleport(var10);
            Map var18 = this.gems;
            double var39 = this.player.getLocation().getY() + 0.3;
            PedestalReviveRitual.setGemRingY(var18, var39, 2021748199);
            int var42 = this.liftTick + 1;
            this.liftTick = var42;
            if (this.liftTick == 40 || this.liftTick == 80 || this.liftTick == 120 || this.liftTick == 160) {
                PedestalReviveRitual var29 = PedestalReviveRitual.this;
                Location var50 = PedestalReviveRitual.this.state.mainLoc(15453813);
                var29.pedestalLightning(var50, 1469756726);
            }

            if (this.liftTick >= 200) {
                this.phase = 1;
            }
        }

        private void spawnArmorStands(int var1) {
            this.phase = 2;
        }

        private void holdAtApex(int var1) {
            Map var7 = this.gems;
            double var14 = this.player.getLocation().getY() + 0.3;
            PedestalReviveRitual.setGemRingY(var7, var14, 2021748199);
            int var17 = this.holdTick + 1;
            this.holdTick = var17;
            if (var17 >= 30) {
                this.phase = 3;
            }
        }

        private void phase7Rotate(int var1) {
            double var200;
            double var202;
            if (this.phase7Tick < 229) {
                var200 = 6.0;
                var202 = 1.0;
            } else {
                double var16 = Math.min((double)(this.phase7Tick - 229) / 150.0, 1.0);
                double var150 = -5.5 * var16;
                var200 = 6.0 + var150;
                double var154 = 11.0 * var16;
                var202 = 1.0 + var154;
            }

            double var107 = this.phase7Angle;
            double var109 = var107 + var202;
            this.phase7Angle = var109;
            Location var204 = this.player.getLocation();
            Location var18 = PedestalReviveRitual.this.state.mainLoc(15453813);
            World var157 = var204.getWorld();
            double var179 = var18.getX() + 0.5;
            double var193 = var204.getY() + 0.3;
            double var198 = var18.getZ() + 0.5;
            Location var49 = new Location(var157, var179, var193, var198);
            double var159 = this.phase7Angle;
            this.placeGemsCircle(var49, var159, var200, 30295026);
            if (this.phase7Tick > 0 && this.phase7Tick % 50 == 0) {
                PedestalReviveRitual var57 = PedestalReviveRitual.this;
                Location var116 = PedestalReviveRitual.this.state.mainLoc(15453813);
                var57.pedestalLightning(var116, 1469756726);
            }

            int var20 = this.phase7Tick + 1;
            if (var20 == 230 || var20 == 240 || var20 == 250 || var20 == 260 || var20 == 270) {
                int var124 = this.currentEnergy + 1;
                this.currentEnergy = var124;
                PlayerDataStore var69 = PedestalReviveRitual.this.players;
                UUID var127 = this.player.getUniqueId();
                PlayerData var21 = var69.get(var127, 1264010596);
                int var129 = this.currentEnergy;
                var21.setEnergy(var129, 319922676);
                PedestalReviveRitual var73 = PedestalReviveRitual.this;
                Player var131 = this.player;
                var73.refreshGemItem(var131, var21, 1488130596);
            }

            int var75 = this.phase7Tick;
            int var133 = this.nextRollTick;
            if (var75 == var133 && this.phase7Tick < 379) {
                PedestalReviveRitual var79 = PedestalReviveRitual.this;
                Player var136 = this.player;
                var79.rollRandomGem(var136, 130091948);
                int var138 = this.phase7Tick;
                double var186 = (double)(379 - var138) / 94.0;
                double var167 = Math.min(1.0, var186);
                double var188 = Math.max(0.0, var167);
                double var171 = 12.0 * var188;
                int var22 = (int)Math.round(2.0 + var171);
                int var140 = this.phase7Tick;
                int var174 = Math.max(2, var22);
                int var141 = var140 + var174;
                this.nextRollTick = var141;
            }

            int var143 = this.phase7Tick + 1;
            this.phase7Tick = var143;
            if (this.phase7Tick >= 380) {
                this.phase = 4;
            }
        }

        private void placeGemsCircle(Location var1, double var2, double var4, int var6) {
            Map var13 = this.gems;
            GemType var8 = GemType.STRENGTH;
            ItemDisplay var15 = (ItemDisplay)var13.get(var8);
            double var59 = var2 + 0.0;
            tpGem(var15, var1, var59, var4, 231961259);
            Map var17 = this.gems;
            GemType var45 = GemType.ASTRA;
            ItemDisplay var19 = (ItemDisplay)var17.get(var45);
            double var63 = var2 + 45.0;
            tpGem(var19, var1, var63, var4, 231961259);
            Map var21 = this.gems;
            GemType var47 = GemType.FLUX;
            ItemDisplay var23 = (ItemDisplay)var21.get(var47);
            double var67 = var2 + 90.0;
            tpGem(var23, var1, var67, var4, 231961259);
            Map var25 = this.gems;
            GemType var49 = GemType.WEALTH;
            ItemDisplay var27 = (ItemDisplay)var25.get(var49);
            double var71 = var2 + 135.0;
            tpGem(var27, var1, var71, var4, 231961259);
            Map var29 = this.gems;
            GemType var51 = GemType.PUFF;
            ItemDisplay var31 = (ItemDisplay)var29.get(var51);
            double var75 = var2 + 180.0;
            tpGem(var31, var1, var75, var4, 231961259);
            Map var33 = this.gems;
            GemType var53 = GemType.FIRE;
            ItemDisplay var35 = (ItemDisplay)var33.get(var53);
            double var79 = var2 + 225.0;
            tpGem(var35, var1, var79, var4, 231961259);
            Map var37 = this.gems;
            GemType var55 = GemType.SPEED;
            ItemDisplay var39 = (ItemDisplay)var37.get(var55);
            double var83 = var2 + 270.0;
            tpGem(var39, var1, var83, var4, 231961259);
            Map var41 = this.gems;
            GemType var57 = GemType.LIFE;
            ItemDisplay var43 = (ItemDisplay)var41.get(var57);
            double var87 = var2 + 315.0;
            tpGem(var43, var1, var87, var4, 231961259);
        }

        private static void tpGem(ItemDisplay var0, Location var1, double var2, double var4, int var6) {
            if (var0 != null && !var0.isDead()) {
                double var16 = Math.toRadians(var2);
                double var31 = -var4;
                double var49 = Math.sin(var16);
                double var18 = var31 * var49;
                double var53 = Math.cos(var16);
                double var20 = var4 * var53;
                Location var40 = var1.clone();
                Location var22 = var40.add(var18, 0.0, var20);
                var22.setYaw(0.0F);
                var22.setPitch(0.0F);
                var0.teleport(var22);
            }
        }

        private void finish(int var1) {
            this.cancel();
            UUID var19 = this.player.getUniqueId();

            for (ItemDisplay var229 : this.gems.values()) {
                if (var229 != null && !var229.isDead()) {
                    var229.remove();
                }
            }

            this.gems.clear();
            PedestalReviveRitual.this.activeGems = null;
            PlayerDataStore var43 = PedestalReviveRitual.this.players;
            Color var21 = PedestalReviveRitual.gemColor(var43.get(var19, 1264010596).gem(1413186337), 104246386);
            Location var22 = this.player.getLocation().add(0.0, 1.0, 0.0);
            DustOptions var52 = new DustOptions(var21, 3.0F);
            World var54 = var22.getWorld();
            Particle var161 = Particle.DUST;
            Particles.spawn(var54, var161, var22, 350, 1.5, 1.5, 1.5, 0.0, var52, true, 1748031581);
            PedestalReviveRitual var56 = PedestalReviveRitual.this;
            Player var163 = this.player;
            var56.clearGemGlow(var163, 1603907024);
            PedestalReviveRitual.restoreWeather(781471445);
            PlayerDataStore var59 = PedestalReviveRitual.this.players;
            var59.get(var19, 1264010596).setRestorationLock(false, 2055613441);
            PedestalState var63 = PedestalReviveRitual.this.state;
            var63.setRevivalLocked(var19, false, 1934094553);
            PedestalReviveRitual.this.state.setPedestalBusy(false, 511184142);
            PedestalReviveRitual.this.state.setRevivingPlayer(null, 159973914);
            PedestalReviveRitual.this.state.resetCount(406144441);
            this.player.setGravity(true);
            this.player.setInvulnerable(false);
            Location var24 = PedestalReviveRitual.this.state.pedestalLoc1(1941968134);
            if (var24 != null) {
                World var83 = var24.getWorld();

                for (Entity var26 : var83.getNearbyEntities(var24, 10.0, 10.0, 10.0)) {
                    if (var26 instanceof Player) {
                        UUID var96 = ((Player)var26).getUniqueId();
                        if (var96.equals(var19)) {
                            continue;
                        }
                    }

                    Vector var100 = var26.getLocation().toVector();
                    Vector var174 = var24.toVector();
                    Vector var233 = var100.subtract(var174).normalize();
                    Vector var177 = var233.multiply(1.1).setY(0.75);
                    var26.setVelocity(var177);
                }
            }

            String var230 = this.player.getName();

            for (Player var114 : Bukkit.getOnlinePlayers()) {
                Component var180 = Text.legacy("&a" + var230 + " has succesfully completed their Restoration ritual!", 373192400);
                var114.sendMessage(var180);
            }

            AchievementStore var118 = PedestalReviveRitual.this.plugin.advancements(1975441698);
            Player var182 = this.player;
            AchievementStore.Advancement var200 = AchievementStore.Advancement.REAWAKENING;
            var118.unlock(var182, var200, 1132982324);
            Location var232 = this.player.getLocation();
            Player var123 = this.player;
            Sound var201 = Sound.ENTITY_WARDEN_SONIC_BOOM;
            var123.playSound(var232, var201, 2.0F, 1.0F);
            Player var125 = this.player;
            Sound var202 = Sound.ENTITY_ENDER_DRAGON_GROWL;
            var125.playSound(var232, var202, 2.0F, 1.0F);
            Player var127 = this.player;
            Sound var203 = Sound.BLOCK_BELL_RESONATE;
            var127.playSound(var232, var203, 2.0F, 1.0F);
            Player var129 = this.player;
            Sound var204 = Sound.BLOCK_BEACON_ACTIVATE;
            var129.playSound(var232, var204, 2.0F, 1.0F);
            Player var131 = this.player;
            Sound var205 = Sound.ENTITY_GENERIC_EXPLODE;
            var131.playSound(var232, var205, 2.0F, 1.0F);
            Player var133 = this.player;
            Sound var206 = Sound.ENTITY_WITHER_SPAWN;
            var133.playSound(var232, var206, 0.5F, 0.5F);
            Player var135 = this.player;
            Sound var207 = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
            var135.playSound(var232, var207, 1.0F, 0.5F);
            PedestalState var138 = PedestalReviveRitual.this.state;
            PedestalState.Ritual var190 = PedestalState.Ritual.NONE;
            var138.setRitual(var190, 779480626);
            PedestalReviveRitual.this.state.setActive(false, 1028669057);
            PedestalReviveRitual.this.state.removeBeam(1137327363);
            PedestalReviveRitual.this.state.unbuildStructure(280713799);
            PedestalReviveRitual.this.state.save(973788971);
        }

        private void abort(int var1) {
            this.cancel();

            for (ItemDisplay var5 : this.gems.values()) {
                if (var5 != null && !var5.isDead()) {
                    var5.remove();
                }
            }

            this.gems.clear();
            PedestalReviveRitual var22 = PedestalReviveRitual.this;
            UUID var24 = this.player.getUniqueId();
            var22.cleanupAbort(var24, 751263136);
        }
    }
}
