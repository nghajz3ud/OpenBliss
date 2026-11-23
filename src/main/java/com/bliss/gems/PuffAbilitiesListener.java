package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameEvent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class PuffAbilitiesListener implements Listener {
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final PuffGemListener puff;
    private final GlobalState globalState;
    private final Random random;
    private final Map<UUID, UUID> dashedVictimToAttacker;
    private final Map<UUID, Integer> lastInteractTick;
    private final Map<UUID, Integer> lastLeftClickTick;
    private static final double BREEZY_FALL_CAP = 19.2;
    private static final long BREEZY_FALL_WINDOW_MS = 5000L;
    private static final double BREEZY_LAUNCH = 2.6;
    private static final double DASH_SPEED = 4.2;
    private static final int TRAIL_MAX_TICKS = 120;
    private static final int TRAIL_GROUND_GRACE = 24;
    private static final double TRAIL_STEP_SQ = 9.0;
    private static final int BREEZY_SLAM_MAX_TICKS = 80;
    private static final double BREEZY_FALL_GRACE = 3.0;
    private static final int BASH_TILT_RADIUS = 4;
    private static final double BASH_TILT_DEGREES = 16.0;
    private static final int BASH_TILT_HOLD_TICKS = 78;
    private static final int BASH_TILT_EASE_TICKS = 20;
    private static final int BASH_TILT_LIFETIME_TICKS = 100;
    private final Map<UUID, Long> breezyFallCap;
    private final Map<UUID, UUID> breezyBasher;
    private final TrustStore trust;
    private final Set<UUID> dashCancelled;

    public PuffAbilitiesListener(BlissPlugin var1, PlayerDataStore var2, PuffGemListener var3, GlobalState var4, int var5) {
        Random var7 = new Random();
        this.random = var7;
        ConcurrentHashMap var20 = new ConcurrentHashMap();
        this.dashedVictimToAttacker = var20;
        ConcurrentHashMap var21 = new ConcurrentHashMap();
        this.lastInteractTick = var21;
        ConcurrentHashMap var22 = new ConcurrentHashMap();
        this.lastLeftClickTick = var22;
        ConcurrentHashMap var23 = new ConcurrentHashMap();
        this.breezyFallCap = var23;
        ConcurrentHashMap var24 = new ConcurrentHashMap();
        this.breezyBasher = var24;
        KeySetView var25 = ConcurrentHashMap.newKeySet();
        this.dashCancelled = var25;
        this.plugin = var1;
        this.players = var2;
        this.puff = var3;
        this.globalState = var4;
        TrustStore var31 = var1.trust(310732214);
        this.trust = var31;
    }

    @EventHandler
    public void onSculkSense(BlockReceiveGameEvent var1) {
        GameEvent var8 = var1.getEvent();
        GameEvent var3 = GameEvent.SCULK_SENSOR_TENDRILS_CLICKING;
        if (var8 == var3) {
            if (var1.getEntity() instanceof Player var5) {
                PlayerDataStore var16 = this.players;
                UUID var24 = var5.getUniqueId();
                if (var16.get(var24, 1264010596).canUsePassives(2051432579)) {
                    if (this.isHoldingPuff(var5, 1499929949)) {
                        var1.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onBreezyBashDamage(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var8) {
            if (var1.getEntity() instanceof LivingEntity var130) {
                if (!var130.equals(var8)) {
                    Set var31 = this.puff.dashing;
                    UUID var89 = var8.getUniqueId();
                    if (!var31.contains(var89)) {
                        Set var35 = this.puff.stomping;
                        UUID var91 = var8.getUniqueId();
                        if (!var35.contains(var91)) {
                            DamageCause var38 = var1.getCause();
                            DamageCause var92 = DamageCause.SONIC_BOOM;
                            if (var38 != var92) {
                                DamageCause var40 = var1.getCause();
                                DamageCause var93 = DamageCause.THORNS;
                                if (var40 != var93) {
                                    ItemStack var131 = var8.getInventory().getItemInMainHand();
                                    int var45 = BlissCoreItems.gemIdOf(var131, 382441079);
                                    int var95 = GemType.PUFF.id();
                                    if (var45 != var95) {
                                        GemType var96 = GemType.PUFF;
                                        if (!GoldGem.isWieldingSoulMain(var8, var96, 244096655)) {
                                            return;
                                        }
                                    }

                                    if (BlissCoreItems.tierOf(var131, 1633828214) != 2) {
                                        GemType var98 = GemType.PUFF;
                                        if (!GoldGem.isWieldingSoulMain(var8, var98, 244096655)) {
                                            return;
                                        }
                                    }

                                    PlayerDataStore var53 = this.players;
                                    UUID var100 = var8.getUniqueId();
                                    if (!var53.get(var100, 1264010596).canUsePowers(1616892701)) {
                                        return;
                                    }

                                    if (!GemAbilityRules.on(GemAbilityRules.GemAbility.PUFF_BREEZY, 425337338)) {
                                        return;
                                    }

                                    long var12 = System.currentTimeMillis();
                                    Map var63 = this.puff.cd3;
                                    UUID var102 = var8.getUniqueId();
                                    Long var14 = (Long)var63.get(var102);
                                    if (var14 != null) {
                                        long var68 = var14;
                                        if (var68 > var12) {
                                            long var104 = var14;
                                            Component var110 = Text.legacy(
                                                "&f\ud83d\udd2e &cYour &f☁Breezy Bash &cskill is on cooldown for &f" + readable(var104 - var12, 1418271633),
                                                373192400
                                            );
                                            var8.sendMessage(var110);
                                            return;
                                        }
                                    }

                                    if (var130 instanceof Player var15) {
                                        TrustStore var77 = this.trust;
                                        UUID var112 = var8.getUniqueId();
                                        UUID var126 = var15.getUniqueId();
                                        if (var77.isTrusted(var112, var126, 1736364606)) {
                                            Component var124 = Text.legacy("&f\ud83d\udd2e &fYou cannot cast negative powers on allies!", 373192400);
                                            var8.sendMessage(var124);
                                            return;
                                        }
                                    }

                                    Component var116 = Text.legacy("&f\ud83d\udd2e &bYou used &f☁Breezy Bash &bon " + victimName(var130, 78715262), 373192400);
                                    var8.sendMessage(var116);
                                    if (var130 instanceof Player var84) {
                                        Component var120 = Text.legacy(
                                            "&f\ud83d\udd2e &bYou have been affected by &f☁Breezy Bash &bby " + var8.getName(), 373192400
                                        );
                                        var84.sendMessage(var120);
                                    }

                                    this.applyBreezyBashCooldown(var8, var12, 803402280);
                                    this.scheduleBreezyBashLaunch(var8, var130, 336536801);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void scheduleBreezyBashLaunch(Player var1, final LivingEntity var2, int var3) {
        double var15;
        final boolean var109;
        label31: {
            Map var19 = this.breezyFallCap;
            UUID var52 = var2.getUniqueId();
            Long var70 = System.currentTimeMillis() + 5000L;
            var19.put(var52, var70);
            Map var22 = this.breezyBasher;
            UUID var54 = var2.getUniqueId();
            UUID var72 = var1.getUniqueId();
            var22.put(var54, var72);
            PlayerDataStore var25 = this.players;
            UUID var56 = var1.getUniqueId();
            var15 = EnergyScale.power(var25.get(var56, 1264010596), 1425120665);
            if (var2 instanceof Player var17 && !var17.getAllowFlight()) {
                var17.setAllowFlight(true);
                var109 = true;
                break label31;
            }

            var109 = false;
        }

        BukkitScheduler var37 = Bukkit.getScheduler();
        BlissPlugin var59 = this.plugin;
        Runnable var74 = () -> {
            if (var2.isValid() && !var2.isDead()) {
                Vector var16 = var2.getVelocity();
                String var8 = "puff";
                String var9 = "breezy-bash-launch";
                double var18 = GemTuning.power(var8, var9, 2.6, 1503850832);
                double var20 = var18 * var15;
                Vector var5 = new Vector(0.0, var20, 0.0);
                Vector var17x = var16.add(var5);
                var2.setVelocity(var17x);
            }
        };
        var37.runTaskLater(var59, var74, 1L);

        for (int var110 = 0; var110 < 5; var110++) {
            BukkitScheduler var42 = Bukkit.getScheduler();
            BlissPlugin var62 = this.plugin;
            Runnable var76 = () -> drawLineTrail(var2, var1, 511835786);
            long var100 = (long)var110 * 2L;
            long var87 = 2L + var100;
            var42.runTaskLater(var62, var76, var87);
        }

        BukkitRunnable var44 = new BukkitRunnable() {
            int t = 0;
            double slammed = 0.0;
            boolean airborne = false;

            public void run() {
                if (var2.isValid() && !var2.isDead()) {
                    int var72 = this.t + 1;
                    this.t = var72;
                    if (!this.airborne) {
                        if (!var2.isOnGround()) {
                            this.airborne = true;
                        } else if (this.t < 20) {
                            return;
                        }
                    }

                    if (var2.isOnGround()) {
                        this.restore(1980816726);
                        Location var111 = var2.getLocation();
                        if (!GlobalState.forEntity(var2, 601947313).legacyPuff(253639763)) {
                            PuffAbilitiesListener var66 = PuffAbilitiesListener.this;
                            boolean var95 = var111.clone().subtract(0.0, 0.2, 0.0).getBlock().getType().isSolid();
                            var66.breezyLandingTilt(var111, var95, 303325845);
                        }

                        PuffAbilitiesListener var68 = PuffAbilitiesListener.this;
                        LivingEntity var86 = var2;
                        double var97 = this.slammed;
                        var68.applyBreezyLandingDamage(var86, var97, 1036978156);
                        this.cancel();
                    } else if (this.t > 80) {
                        this.restore(1980816726);
                        this.cancel();
                    } else {
                        Vector var7 = var2.getVelocity();
                        if (!(var7.getY() > 0.5)) {
                            this.restore(1980816726);
                            if (var2 instanceof Player var8 && var8.isFlying()) {
                                GameMode var48 = var8.getGameMode();
                                GameMode var75 = GameMode.CREATIVE;
                                if (var48 != var75) {
                                    GameMode var50 = var8.getGameMode();
                                    GameMode var76 = GameMode.SPECTATOR;
                                    if (var50 != var76) {
                                        var8.setFlying(false);
                                    }
                                }
                            }

                            LivingEntity var53 = var2;
                            PotionEffectType var78 = PotionEffectType.SLOW_FALLING;
                            var53.removePotionEffect(var78);
                            LivingEntity var55 = var2;
                            double var99 = var7.getX();
                            double var107 = var7.getZ();
                            Vector var79 = new Vector(var99, -3.2, var107);
                            var55.setVelocity(var79);
                            double var82 = this.slammed + 3.2;
                            this.slammed = var82;
                        }
                    }
                } else {
                    this.restore(1980816726);
                    this.cancel();
                }
            }

            private void restore(int var1) {
                if (var109 && var2 instanceof Player var4 && var4.isOnline()) {
                    GameMode var15 = var4.getGameMode();
                    GameMode var3 = GameMode.CREATIVE;
                    if (var15 != var3) {
                        GameMode var17 = var4.getGameMode();
                        GameMode var20 = GameMode.SPECTATOR;
                        if (var17 != var20) {
                            var4.setFlying(false);
                            var4.setAllowFlight(false);
                        }
                    }
                }
            }
        };
        BlissPlugin var64 = this.plugin;
        var44.runTaskTimer(var64, 1L, 1L);

        for (int var111 = 0; var111 < 20; var111++) {
            BukkitScheduler var49 = Bukkit.getScheduler();
            BlissPlugin var67 = this.plugin;
            Runnable var81 = () -> drawLineTrail(var2, var1, 511835786);
            long var105 = (long)var111 * 2L;
            long var93 = 14L + var105;
            var49.runTaskLater(var67, var81, var93);
        }
    }

    private void applyBreezyLandingDamage(LivingEntity var1, double var2, int var4) {
        String var5 = "puff";
        String var6 = "breezy-bash-grace";
        double var12 = GemTuning.power(var5, var6, 3.0, 1503850832);
        if (!(var2 <= var12)) {
            double var14 = var2 - var12;
            BukkitScheduler var25 = Bukkit.getScheduler();
            BlissPlugin var28 = this.plugin;
            Runnable var34 = () -> {
                UUID var9 = var1.getUniqueId();
                Map var13 = this.breezyFallCap;
                Long var10 = (Long)var13.get(var9);
                if (var10 != null) {
                    long var18 = var10;
                    long var6x = System.currentTimeMillis();
                    if (var18 >= var6x) {
                        if (var1.isValid() && !var1.isDead() && !var1.isInWater()) {
                            DamageSource var39 = DamageSource.builder(DamageType.FALL).build();
                            var1.damage(var14, var39);
                            return;
                        }

                        Map var28x = this.breezyFallCap;
                        var28x.remove(var9);
                        Map var31 = this.breezyBasher;
                        var31.remove(var9);
                        return;
                    }
                }
            };
            var25.runTaskLater(var28, var34, 2L);
        }
    }

    private void breezyLandingTilt(Location var1, boolean var2, int var3) {
        if (var2) {
            World var15 = var1.getWorld();
            if (var15 != null) {
                tiltGround(var1, 4, 1.0, 227775584);
                int var16 = var1.getBlockX();
                int var17 = var1.getBlockY();
                int var18 = var1.getBlockZ();

                for (int var19 = -4; var19 <= 4; var19++) {
                    for (int var20 = -4; var20 <= 4; var20++) {
                        int var64 = var19 * var19;
                        int var172 = var20 * var20;
                        int var21 = var64 + var172;
                        if (var21 <= 16 && (var19 != 0 || var20 != 0)) {
                            Block var22 = null;
                            int var23 = var17;

                            while (true) {
                                int var175 = var17 - 2;
                                if (var23 < var175) {
                                    break;
                                }

                                int var177 = var16 + var19;
                                int var268 = var18 + var20;
                                Block var24 = var15.getBlockAt(var177, var23, var268);
                                if (var24.getType().isSolid() && !var24.getType().isAir()) {
                                    Tag var80 = Tag.LEAVES;
                                    Material var179 = var24.getType();
                                    if (!var80.isTagged(var179)) {
                                        int var181 = var16 + var19;
                                        int var201 = var23 + 1;
                                        int var271 = var18 + var20;
                                        if (var15.getBlockAt(var181, var201, var271).getType().isAir()) {
                                            var22 = var24;
                                            break;
                                        }
                                    }
                                }

                                var23 += -1;
                            }

                            if (var22 != null) {
                                double var348 = Math.sqrt((double)var21);
                                long var206 = (long)(var16 + var19) * 912931L;
                                long var300 = (long)(var18 + var20) * 419L;
                                long var208 = var206 ^ var300;
                                Random var93 = new Random(var208);
                                double var339 = var93.nextDouble() * 0.48;
                                double var304 = 0.62 + var339;
                                double var212 = 4.0 * var304;
                                if (!(var348 > var212)) {
                                    long var218 = (long)(var16 + var19) * 341873128712L;
                                    long var312 = (long)(var18 + var20) * 132897987541L;
                                    long var220 = var218 + var312;
                                    Random var97 = new Random(var220);
                                    double var316 = var348 / 4.0;
                                    double var224 = 1.0 - var316;
                                    double var100 = 16.0 * var224;
                                    double var321 = var97.nextDouble() * 0.95;
                                    double var228 = 0.55 + var321;
                                    float var27 = (float)Math.toRadians(var100 * var228);
                                    if (!(var27 <= 0.001F)) {
                                        double var110 = (double)var19;
                                        double var28 = var110 / var348;
                                        double var115 = (double)var20;
                                        double var30 = var115 / var348;
                                        double var32 = (var97.nextDouble() - 0.5) * 1.7;
                                        double var34 = Math.cos(var32);
                                        double var36 = Math.sin(var32);
                                        double var136 = -var30;
                                        double var138 = var136 * var34;
                                        double var242 = var28 * var36;
                                        double var38 = var138 - var242;
                                        double var144 = -var30;
                                        double var146 = var144 * var36;
                                        double var248 = var28 * var34;
                                        double var40 = var146 + var248;
                                        Quaternionf var150 = new Quaternionf();
                                        float var252 = (float)var38;
                                        float var329 = (float)var40;
                                        Quaternionf var42 = var150.rotateAxis(var27, var252, 0.0F, var329);
                                        Vector3f var152 = new Vector3f(0.5F, 0.5F, 0.5F);
                                        Vector3f var153 = new Vector3f(var152);
                                        Vector3f var255 = new Vector3f(var152);
                                        Vector3f var185 = var42.transform(var255);
                                        Vector3f var154 = var153.sub(var185);
                                        float var280 = (float)var97.nextDouble() * 0.05F;
                                        float var257 = 0.03F + var280;
                                        Vector3f var44 = var154.add(0.0F, var257, 0.0F);
                                        int var188 = var22.getX();
                                        int var260 = var22.getY() + 1;
                                        int var284 = var22.getZ();
                                        Block var45 = var15.getBlockAt(var188, var260, var284);
                                        byte var262 = var45.getLightFromBlocks();
                                        byte var286 = var45.getLightFromSky();
                                        Brightness var158 = new Brightness(var262, var286);
                                        BlockData var47 = var22.getBlockData();
                                        Location var190 = var22.getLocation();
                                        Consumer<BlockDisplay> var288 = var4 -> {
                                            var4.setPersistent(false);
                                            var4.setBlock(var47);
                                            var4.setBrightness(var158);
                                            Vector3f var9 = new Vector3f(1.0F, 1.0F, 1.0F);
                                            Quaternionf var19x = new Quaternionf();
                                            Transformation var18x = new Transformation(var44, var42, var9, var19x);
                                            var4.setTransformation(var18x);
                                        };
                                        BlockDisplay var48 = (BlockDisplay)var15.spawn(var190, BlockDisplay.class, var288);
                                        BukkitScheduler var164 = Bukkit.getScheduler();
                                        BlissPlugin var192 = this.plugin;
                                        Runnable var265 = () -> {
                                            if (!var48.isDead()) {
                                                var48.setInterpolationDelay(0);
                                                var48.setInterpolationDuration(20);
                                                Vector3f var3x = new Vector3f();
                                                Quaternionf var13 = new Quaternionf();
                                                Vector3f var14 = new Vector3f(1.0F, 1.0F, 1.0F);
                                                Quaternionf var15x = new Quaternionf();
                                                Transformation var12 = new Transformation(var3x, var13, var14, var15x);
                                                var48.setTransformation(var12);
                                            }
                                        };
                                        var164.runTaskLater(var192, var265, 78L);
                                        BukkitScheduler var166 = Bukkit.getScheduler();
                                        BlissPlugin var194 = this.plugin;
                                        Runnable var267 = () -> {
                                            if (!var48.isDead()) {
                                                var48.remove();
                                            }
                                        };
                                        var166.runTaskLater(var194, var267, 100L);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onBreezyFallDamage(EntityDamageEvent var1) {
        DamageCause var13 = var1.getCause();
        DamageCause var3 = DamageCause.FALL;
        if (var13 == var3) {
            if (var1.getEntity() instanceof LivingEntity var8) {
                UUID var68 = var8.getUniqueId();
                Map var23 = this.breezyFallCap;
                Long var10 = (Long)var23.get(var68);
                if (var10 != null) {
                    long var28 = var10;
                    long var4 = System.currentTimeMillis();
                    if (var28 < var4) {
                        Map var32 = this.breezyFallCap;
                        var32.remove(var68);
                        Map var35 = this.breezyBasher;
                        var35.remove(var68);
                    } else {
                        Map var38 = this.breezyFallCap;
                        var38.remove(var68);
                        Map var41 = this.breezyBasher;
                        var41.remove(var68);
                        if (!GemAbilityRules.on(GemAbilityRules.GemAbility.PUFF_BREEZY, 425337338)) {
                            var1.setCancelled(true);
                        } else {
                            GemAbilityRules.GemAbility var45 = GemAbilityRules.GemAbility.PUFF_BREEZY;
                            String var59 = "puff";
                            String var65 = "breezy-bash";
                            double var60 = GemTuning.damage(var59, var65, 19.2, 1172948035);
                            double var11 = GemAbilityRules.scaled(var45, var60, 1684677315);
                            double var49 = var1.getDamage();
                            if (var49 > var11) {
                                var1.setDamage(var11);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void drawLineTrail(LivingEntity var0, Player var1, int var2) {
        if (var0.isValid() && var1.isOnline()) {
            Location var24 = var1.getLocation().clone().add(0.0, 0.5, 0.0);
            Location var25 = var0.getLocation().clone().add(0.0, 0.5, 0.0);
            World var49 = var24.getWorld();
            World var80 = var25.getWorld();
            if (var49.equals(var80)) {
                Color var83 = Color.fromRGB(255, 255, 255);
                DustOptions var51 = new DustOptions(var83, 1.5F);
                DustOptions var26 = var51;

                for (int var28 = 0; var28 <= 20; var28++) {
                    double var29 = (double)var28 / 20.0;
                    double var61 = var25.getX();
                    double var88 = var24.getX();
                    double var115 = var25.getX();
                    double var90 = var88 - var115;
                    double var92 = var90 * var29;
                    double var31 = var61 + var92;
                    double var66 = var25.getY();
                    double var95 = var24.getY();
                    double var120 = var25.getY();
                    double var97 = var95 - var120;
                    double var99 = var97 * var29;
                    double var33 = var66 + var99;
                    double var71 = var25.getZ();
                    double var102 = var24.getZ();
                    double var125 = var25.getZ();
                    double var104 = var102 - var125;
                    double var106 = var104 * var29;
                    double var35 = var71 + var106;
                    World var76 = var25.getWorld();
                    Particle var82 = Particle.DUST;
                    World var130 = var25.getWorld();
                    Location var108 = new Location(var130, var31, var33, var35);
                    Particles.spawn(var76, var82, var108, 1, 0.0, 0.0, 0.0, 0.0, var26, true, 1748031581);
                }
            }
        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent var1) {
        Action var34 = var1.getAction();
        Action var3 = Action.LEFT_CLICK_AIR;
        if (var34 != var3) {
            Action var36 = var1.getAction();
            Action var138 = Action.LEFT_CLICK_BLOCK;
            if (var36 != var138) {
                return;
            }
        }

        Player var11 = var1.getPlayer();
        UUID var12 = var11.getUniqueId();
        int var13 = Bukkit.getCurrentTick();
        Map var43 = this.lastLeftClickTick;
        Integer var14 = (Integer)var43.get(var12);
        if (var14 != null) {
            int var48 = var14;
            if (var48 == var13) {
                return;
            }
        }

        Map var50 = this.lastLeftClickTick;
        Integer var180 = var13;
        var50.put(var12, var180);
        ItemStack var15 = var11.getInventory().getItemInMainHand();
        int var56 = BlissCoreItems.gemIdOf(var15, 382441079);
        int var143 = GemType.PUFF.id();
        if (var56 != var143) {
            GemType var144 = GemType.PUFF;
            if (!GoldGem.isWieldingSoulMain(var11, var144, 244096655)) {
                return;
            }
        }

        if (BlissCoreItems.tierOf(var15, 1633828214) != 2) {
            GemType var146 = GemType.PUFF;
            if (!GoldGem.isWieldingSoulMain(var11, var146, 244096655)) {
                return;
            }
        }

        PlayerDataStore var64 = this.players;
        PlayerData var16 = var64.get(var12, 1264010596);
        if (var16.canUsePowers(1616892701)) {
            if (GemAbilityRules.on(GemAbilityRules.GemAbility.PUFF_BREEZY, 425337338)) {
                long var17 = System.currentTimeMillis();
                Map var74 = this.puff.cd3;
                Long var19 = (Long)var74.get(var12);
                if (var19 != null) {
                    long var79 = var19;
                    if (var79 > var17) {
                        long var173 = var19;
                        Component var179 = Text.legacy(
                            "&f\ud83d\udd2e &cYour &f☁Breezy Bash &cskill is on cooldown for &f" + readable(var173 - var17, 1418271633), 373192400
                        );
                        var11.sendMessage(var179);
                        return;
                    }
                }

                Component var150 = Text.legacy("&f\ud83d\udd2e &bYou used &f☁Breezy Bash &bGroup", 373192400);
                var11.sendMessage(var150);
                this.applyBreezyBashCooldown(var11, var17, 803402280);
                Location var20 = var11.getLocation().clone();
                BlissPlugin var88 = this.plugin;
                Color var186 = Color.fromRGB(255, 255, 255);
                GemBurst.burst(var88, var20, var186, 5.0, 1155716754);
                double var21 = EnergyScale.power(var16, 1425120665);
                String var92 = "puff";
                String var153 = "breezy-bash";
                double var23 = GemTuning.radius(var92, var153, 5.0, 1058601812);
                World var96 = var11.getWorld();

                for (Entity var26 : var96.getNearbyEntities(var20, var23, var23, var23)) {
                    if (var26 instanceof LivingEntity) {
                        LivingEntity var27 = (LivingEntity)var26;
                        if (!var27.equals(var11)) {
                            if (var27 instanceof Player) {
                                Player var28 = (Player)var27;
                                TrustStore var115 = this.trust;
                                UUID var157 = var11.getUniqueId();
                                UUID var192 = var28.getUniqueId();
                                if (var115.isTrusted(var157, var192, 1736364606)) {
                                    continue;
                                }
                            }

                            Vector var119 = var27.getLocation().toVector();
                            Vector var160 = var11.getLocation().toVector();
                            Vector var220 = var119.subtract(var160).setY(0);
                            if (var220.lengthSquared() < 1.0E-6) {
                                Vector var126 = new Vector(0, 0, 1);
                                var220 = var126;
                            }

                            String var127 = "puff";
                            String var162 = "breezy-bash-group-push";
                            double var29 = GemTuning.power(var127, var162, 2.5, 1503850832);
                            String var130 = "puff";
                            String var163 = "breezy-bash-group-up";
                            double var31 = GemTuning.power(var130, var163, 1.25, 1503850832);
                            Vector var134 = var220.normalize();
                            double var166 = var29 * var21;
                            Vector var33 = var134.multiply(var166);
                            Vector var169 = var27.getVelocity();
                            Vector var170 = var169.add(var33);
                            double var216 = var31 * var21;
                            Vector var201 = new Vector(0.0, var216, 0.0);
                            Vector var171 = var170.add(var201);
                            var27.setVelocity(var171);
                        }
                    }
                }
            }
        }
    }

    private void applyBreezyBashCooldown(Player var1, long var2, int var4) {
        PlayerDataStore var14 = this.players;
        UUID var49 = var1.getUniqueId();
        PlayerData var11 = var14.get(var49, 1264010596);
        GlobalState var17 = this.globalState;
        boolean var12 = var17.holdsDragonEgg(var1, 715974514);
        int var13 = var11.energy(1160853318);
        long var68;
        if (var13 > 5) {
            long var23;
            if (var12) {
                var23 = 40000L;
            } else {
                var23 = 75000L;
            }

            var68 = var23;
        } else if (var13 == 5) {
            long var47;
            if (var12) {
                var47 = 45000L;
            } else {
                var47 = 90000L;
            }

            var68 = var47;
        } else if (var13 == 4) {
            long var38;
            if (var12) {
                var38 = 50000L;
            } else {
                var38 = 105000L;
            }

            var68 = var38;
        } else if (var13 == 3) {
            long var45;
            if (var12) {
                var45 = 60000L;
            } else {
                var45 = 120000L;
            }

            var68 = var45;
        } else {
            long var42;
            if (var12) {
                var42 = 75000L;
            } else {
                var42 = 150000L;
            }

            var68 = var42;
        }

        String var25 = "puff";
        String var52 = "breezy-bash";
        long var26 = GemTuning.cd(var25, var52, 75.0, var68, 1621983041);
        long var66 = EnergyScale.cd(var26, var13, 907518618);
        Map var32 = this.puff.cd3;
        UUID var54 = var1.getUniqueId();
        Long var63 = var2 + var66;
        var32.put(var54, var63);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var28 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var28 != var3) {
            Action var30 = var1.getAction();
            Action var106 = Action.RIGHT_CLICK_BLOCK;
            if (var30 != var106) {
                return;
            }
        }

        Player var15 = var1.getPlayer();
        UUID var16 = var15.getUniqueId();
        int var17 = Bukkit.getCurrentTick();
        Map var37 = this.lastInteractTick;
        Integer var18 = (Integer)var37.get(var16);
        if (var18 != null) {
            int var42 = var18;
            if (var42 == var17) {
                return;
            }
        }

        Map var44 = this.lastInteractTick;
        Integer var143 = var17;
        var44.put(var16, var143);
        ItemStack var19 = var15.getInventory().getItemInMainHand();
        int var50 = BlissCoreItems.gemIdOf(var19, 382441079);
        int var111 = GemType.PUFF.id();
        if (var50 != var111) {
            GemType var112 = GemType.PUFF;
            if (!GoldGem.isWieldingSoulMain(var15, var112, 244096655)) {
                return;
            }
        }

        if (BlissCoreItems.tierOf(var19, 1633828214) != 2) {
            GemType var114 = GemType.PUFF;
            if (!GoldGem.isWieldingSoulMain(var15, var114, 244096655)) {
                return;
            }
        }

        PlayerDataStore var58 = this.players;
        PlayerData var20 = var58.get(var16, 1264010596);
        if (var20.canUsePowers(1616892701)) {
            if (GemAbilityRules.on(GemAbilityRules.GemAbility.PUFF_DASH, 425337338)) {
                long var21 = System.currentTimeMillis();
                Map var68 = this.puff.cd2;
                Long var23 = (Long)var68.get(var16);
                if (var23 != null) {
                    long var73 = var23;
                    if (var73 > var21) {
                        long var118 = var23;
                        Component var124 = Text.legacy(
                            "&f\ud83d\udd2e &cYour &f⏫Dash &cskill is on cooldown for &f" + readable(var118 - var21, 1418271633), 373192400
                        );
                        var15.sendMessage(var124);
                        return;
                    }
                }

                Component var126 = Text.legacy("&f\ud83d\udd2e &bYou have activated &f⏫Dash &bskill", 373192400);
                var15.sendMessage(var126);
                boolean var24 = GlobalState.forPlayer(var15, 1433087673).legacyPuff(253639763);
                if (!var24) {
                    World var83 = var15.getWorld();
                    Location var128 = var15.getLocation();
                    Sound var146 = Sound.ENTITY_BREEZE_JUMP;
                    SoundCategory var154 = SoundCategory.PLAYERS;
                    var83.playSound(var128, var146, var154, 1.0F, 1.0F);
                    World var85 = var15.getWorld();
                    Particle var129 = Particle.GUST_EMITTER_SMALL;
                    Location var148 = var15.getLocation();
                    Particles.spawn(var85, var129, var148, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                }

                this.applyDashCooldown(var15, var21, 1019107357);
                double var25 = EnergyScale.power(var20, 1425120665);
                Vector var93 = var15.getLocation().getDirection().normalize();
                String var131 = "puff";
                String var151 = "dash";
                double var132 = GemTuning.speed(var131, var151, 4.2, 1885669331);
                double var134 = var132 * var25;
                Vector var27 = var93.multiply(var134);
                var15.setVelocity(var27);
                Set var98 = this.puff.dashing;
                var98.add(var16);
                BukkitScheduler var100 = Bukkit.getScheduler();
                BlissPlugin var139 = this.plugin;
                Runnable var153 = () -> {
                    Set var5 = this.puff.dashing;
                    var5.remove(var16);
                };
                var100.runTaskLater(var139, var153, 26L);
                this.scheduleDashHits(var15, 297518793);
                if (!var24) {
                    this.dashTrail(var15, 1548201644);
                    this.dashLandingWatcher(var15, 924743394);
                }
            }
        }
    }

    private void dashTrail(final Player var1, int var2) {
        final Location[] var8 = new Location[1];
        Location var17 = var1.getLocation().clone();
        var8[0] = var17;
        BukkitRunnable var9 = new BukkitRunnable() {
            int t = 0;
            boolean airborne = false;

            public void run() {
                if (var1.isOnline() && !var1.isDead()) {
                    int var74 = this.t + 1;
                    this.t = var74;
                    if (var74 <= 120) {
                        Location var21 = var1.getLocation();
                        World var22 = var21.getWorld();
                        if (var22 == null) {
                            this.cancel();
                            return;
                        }

                        if (!var1.isOnGround()) {
                            this.airborne = true;
                        } else if (this.airborne || this.t >= 24) {
                            this.cancel();
                            return;
                        }

                        World var80 = var8[0].getWorld();
                        if (var22.equals(var80)) {
                            Location var83 = var8[0];
                            if (!(var21.distanceSquared(var83) < 9.0)) {
                                Location[] var50 = var8;
                                Location var96 = var21.clone();
                                var50[0] = var96;
                                Location var23 = var21.clone().add(0.0, 0.9, 0.0);
                                Particle var87 = Particle.GUST;
                                Particles.spawn(var22, var87, var23, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);

                                for (int var24 = 0; var24 < 16; var24++) {
                                    double var99 = (double)var24;
                                    double var61 = (Math.PI * 2) * var99 / 16.0;
                                    double var107 = (double)this.t * 0.35;
                                    double var25 = var61 + var107;
                                    Particle var89 = Particle.CLOUD;
                                    double var110 = var23.getX();
                                    double var118 = var23.getY();
                                    double var121 = var23.getZ();
                                    double var124 = Math.cos(var25);
                                    double var126 = Math.sin(var25);
                                    Particles.spawn(var22, var89, var110, var118, var121, 0, var124, 0.12, var126, 0.34, 1717927553);
                                }

                                return;
                            }
                        }

                        return;
                    }
                }

                this.cancel();
            }
        };
        BlissPlugin var12 = this.plugin;
        var9.runTaskTimer(var12, 1L, 1L);
    }

    private void dashLandingWatcher(final Player var1, int var2) {
        BukkitRunnable var3 = new BukkitRunnable() {
            int t = 0;
            boolean airborne = false;

            public void run() {
                if (var1.isOnline() && !var1.isDead()) {
                    int var25 = this.t + 1;
                    this.t = var25;
                    if (!var1.isOnGround()) {
                        this.airborne = true;
                    } else if (this.airborne || this.t >= 24) {
                        PuffAbilitiesListener var17 = PuffAbilitiesListener.this;
                        Player var28 = var1;
                        var17.dashLandingImpact(var28, 1293044671);
                        this.cancel();
                        return;
                    }

                    if (this.t >= 120) {
                        this.cancel();
                    }
                } else {
                    this.cancel();
                }
            }
        };
        BlissPlugin var10 = this.plugin;
        var3.runTaskTimer(var10, 2L, 1L);
    }

    private void dashLandingImpact(Player var1, int var2) {
        Location var27 = var1.getLocation();
        World var28 = var27.getWorld();
        if (var28 != null) {
            Sound var5 = Sound.ENTITY_GENERIC_EXPLODE;
            SoundCategory var6 = SoundCategory.PLAYERS;
            var28.playSound(var27, var5, var6, 0.9F, 1.25F);
            Block var29 = var27.clone().subtract(0.0, 0.2, 0.0).getBlock();
            BlockData var77;
            if (var29.getType().isSolid()) {
                var77 = var29.getBlockData();
            } else {
                var77 = null;
            }

            BlockData var30 = var77;
            if (var77 != null) {
                Particle var80 = Particle.BLOCK;
                Location var88 = var27.clone().add(0.0, 0.2, 0.0);
                Particles.spawn(var28, var80, var88, 45, 1.1, 0.3, 1.1, 0.3, var77, 1141219473);
                tiltGround(var27, 3, 2.0, 227775584);
            }

            for (int var31 = 0; var31 < 20; var31++) {
                double var92 = (double)var31;
                double var60 = (Math.PI * 2) * var92 / 20.0;
                double var100 = (double)(var31 % 2) * 0.16;
                double var32 = var60 + var100;
                double var34 = Math.cos(var32);
                double var36 = Math.sin(var32);
                if (var30 != null) {
                    Particle var83 = Particle.BLOCK;
                    double var103 = var27.getX();
                    double var121 = var27.getY() + 0.15;
                    double var135 = var27.getZ();
                    Particles.spawn(var28, var83, var103, var121, var135, 0, var34, 0.55, var36, 0.6, var30, 2077675459);
                }

                if (var31 % 2 == 0) {
                    Particle var85 = Particle.CLOUD;
                    double var106 = var27.getX();
                    double var126 = var27.getY() + 0.2;
                    double var140 = var27.getZ();
                    Particles.spawn(var28, var85, var106, var126, var140, 0, var34, 0.25, var36, 0.34, 1717927553);
                }
            }
        }
    }

    private static void tiltGround(Location var0, int var1, double var2, int var4) {
        World var23 = var0.getWorld();
        if (var23 != null) {
            int var24 = var0.getBlockX();
            int var25 = var0.getBlockY();
            int var26 = var0.getBlockZ();

            for (int var27 = -var1; var27 <= var1; var27++) {
                for (int var28 = -var1; var28 <= var1; var28++) {
                    int var64 = var27 * var27;
                    int var143 = var28 * var28;
                    int var29 = var64 + var143;
                    int var145 = var1 * var1;
                    if (var29 <= var145) {
                        Block var30 = null;
                        int var31 = var25;

                        while (true) {
                            int var147 = var25 - 2;
                            if (var31 < var147) {
                                break;
                            }

                            int var149 = var24 + var27;
                            int var200 = var26 + var28;
                            Block var32 = var23.getBlockAt(var149, var31, var200);
                            if (var32.getType().isSolid() && !var32.getType().isAir()) {
                                Tag var78 = Tag.LEAVES;
                                Material var151 = var32.getType();
                                if (!var78.isTagged(var151)) {
                                    int var153 = var24 + var27;
                                    int var163 = var31 + 1;
                                    int var203 = var26 + var28;
                                    if (var23.getBlockAt(var153, var163, var203).getType().isAir()) {
                                        var30 = var32;
                                        break;
                                    }
                                }
                            }

                            var31 += -1;
                        }

                        if (var30 != null) {
                            double var278 = Math.sqrt((double)var29);
                            long var168 = (long)(var24 + var27) * 341873128712L;
                            long var212 = (long)(var26 + var28) * 132897987541L;
                            long var170 = var168 + var212;
                            Random var91 = new Random(var170);
                            Random var33 = var91;
                            double var234 = (double)var1;
                            double var226 = var278 / var234;
                            double var216 = 1.0 - var226;
                            double var174 = 0.55 * var216;
                            double var94 = 0.18 + var174;
                            double var34 = var94 * var2;
                            double var36 = (double)var30.getX() + 0.5;
                            double var38 = (double)var30.getY() + 1.02;
                            double var40 = (double)var30.getZ() + 0.5;
                            byte var139;
                            if (var278 < 2.0) {
                                var139 = 4;
                            } else {
                                var139 = 2;
                            }

                            byte var42 = var139;
                            BlockData var43 = var30.getBlockData();

                            for (int var44 = 0; var44 < var42; var44++) {
                                double var45 = (var33.nextDouble() - 0.5) * 0.7;
                                double var47 = (var33.nextDouble() - 0.5) * 0.7;
                                Particle var155 = Particle.BLOCK;
                                double var196 = var36 + var45;
                                double var230 = var40 + var47;
                                double var240 = var45 * 0.35;
                                double var257 = var47 * 0.35;
                                Particles.spawn(var23, var155, var196, var38, var230, 0, var240, 1.0, var257, var34, var43, 2077675459);
                            }

                            Particle var156 = Particle.CLOUD;
                            double var247 = (var33.nextDouble() - 0.5) * 0.3;
                            double var264 = (var33.nextDouble() - 0.5) * 0.3;
                            double var274 = var34 * 0.7;
                            Particles.spawn(var23, var156, var36, var38, var40, 0, var247, 1.0, var264, var274, 1717927553);
                        }
                    }
                }
            }
        }
    }

    private void applyDashCooldown(Player var1, long var2, int var4) {
        GlobalState var15 = this.globalState;
        boolean var14 = var15.holdsDragonEgg(var1, 715974514);
        Map var19 = this.puff.cd2;
        UUID var22 = var1.getUniqueId();
        String var9 = "puff";
        String var10 = "dash";
        long var28;
        if (var14) {
            var28 = 10000L;
        } else {
            var28 = 20000L;
        }

        long var26 = GemTuning.cd(var9, var10, 20.0, var28, 1621983041);
        Long var25 = var2 + var26;
        var19.put(var22, var25);
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onDashCasterDeath(PlayerDeathEvent var1) {
        Set var4 = this.dashCancelled;
        UUID var7 = var1.getEntity().getUniqueId();
        var4.add(var7);
    }

    private void scheduleDashHits(Player var1, int var2) {
        UUID var12 = var1.getUniqueId();
        Set var20 = this.dashCancelled;
        var20.remove(var12);
        HashSet var22 = new HashSet();
        HashSet var13 = var22;
        String var23 = "puff";
        String var31 = "dash-hit";
        double var14 = GemTuning.radius(var23, var31, 1.8, 1058601812);

        for (int var16 = 0; var16 < 24; var16++) {
            BukkitScheduler var29 = Bukkit.getScheduler();
            BlissPlugin var34 = this.plugin;
            Runnable var36 = () -> {
                if (var1.isOnline() && !var1.isDead()) {
                    Set var31x = this.dashCancelled;
                    if (!var31x.contains(var12)) {
                        World var34x = var1.getWorld();
                        Location var76 = var1.getLocation();

                        for (Entity var22x : var34x.getNearbyEntities(var76, var14, var14, var14)) {
                            if (var22x instanceof LivingEntity) {
                                final LivingEntity var23x = (LivingEntity)var22x;
                                if (!var23x.equals(var1)) {
                                    if (var23x instanceof Player) {
                                        Player var24 = (Player)var23x;
                                        TrustStore var55 = this.trust;
                                        UUID var80 = var1.getUniqueId();
                                        UUID var95 = var24.getUniqueId();
                                        if (var55.isTrusted(var80, var95, 1736364606)) {
                                            continue;
                                        }
                                    }

                                    UUID var82 = var23x.getUniqueId();
                                    if (var13.add(var82)) {
                                        Map var60 = this.dashedVictimToAttacker;
                                        UUID var84 = var23x.getUniqueId();
                                        var60.put(var84, var12);
                                        int var112 = var23x.getNoDamageTicks();
                                        double var25 = var23x.getLastDamage();
                                        var23x.setNoDamageTicks(0);
                                        if (GemAbilityRules.on(GemAbilityRules.GemAbility.PUFF_DASH, 425337338)) {
                                            GemAbilityRules.GemAbility var97 = GemAbilityRules.GemAbility.PUFF_DASH;
                                            String var103 = "puff";
                                            String var109 = "dash";
                                            double var104 = GemTuning.damage(var103, var109, 10.8, 1172948035);
                                            double var98 = GemAbilityRules.scaled(var97, var104, 1684677315);
                                            ConsistentDamage.dealFallAware(var1, var23x, var98, 2104137282);
                                        }

                                        var23x.setNoDamageTicks(var112);
                                        var23x.setLastDamage(var25);
                                        BukkitRunnable var73 = new BukkitRunnable() {
                                            public void run() {
                                                Map var4 = PuffAbilitiesListener.this.dashedVictimToAttacker;
                                                UUID var7 = var23x.getUniqueId();
                                                var4.remove(var7);
                                            }
                                        };
                                        BlissPlugin var91 = this.plugin;
                                        var73.runTaskLater(var91, 3L);
                                    }
                                }
                            }
                        }

                        World var40 = var1.getWorld();
                        Particle var77 = Particle.CLOUD;
                        Location var93 = var1.getLocation();
                        Particles.spawn(var40, var77, var93, 3, 0.1, 0.1, 0.1, 0.0, 1986132876);
                        return;
                    }
                }
            };
            long var38 = (long)var16;
            var29.runTaskLater(var34, var36, var38);
        }
    }

    @EventHandler
    public void onDashedVictimDeath(PlayerDeathEvent var1) {
        UUID var5 = var1.getEntity().getUniqueId();
        Map var12 = this.dashedVictimToAttacker;
        UUID var6 = (UUID)var12.get(var5);
        if (var6 != null) {
            Player var7 = Bukkit.getPlayer(var6);
            String var25;
            if (var7 == null) {
                var25 = var6.toString();
            } else {
                var25 = var7.getName();
            }

            String var31 = var1.getEntity().getName();
            Component var28 = Text.legacy(var25 + " dashed through " + var31, 373192400);
            var1.deathMessage(var28);
            Map var22 = this.dashedVictimToAttacker;
            var22.remove(var5);
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onPuffOffhandProcs(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var10) {
            if (var1.getEntity() instanceof LivingEntity var67) {
                ItemStack var68 = var10.getInventory().getItemInOffHand();
                GemType var3 = GemType.PUFF;
                boolean var13 = GoldGem.hasAbsorbedSoul(var10, var3, 1603837876);
                int var34 = BlissCoreItems.gemIdOf(var68, 382441079);
                int var57 = GemType.PUFF.id();
                if (var34 == var57 || var13) {
                    PlayerDataStore var37 = this.players;
                    UUID var59 = var10.getUniqueId();
                    if (var37.get(var59, 1264010596).canUsePassives(2051432579)) {
                        int var41 = BlissCoreItems.gemIdOf(var68, 382441079);
                        int var61 = GemType.PUFF.id();
                        ItemStack var55;
                        if (var41 == var61) {
                            var55 = var68;
                        } else {
                            var55 = GoldGem.findInInventory(var10, 1361603346);
                        }

                        int var53;
                        label41: {
                            if (var13) {
                                int var45 = BlissCoreItems.gemIdOf(var68, 382441079);
                                int var63 = GemType.PUFF.id();
                                if (var45 != var63) {
                                    var53 = 2;
                                    break label41;
                                }
                            }

                            var53 = BlissCoreItems.tierOf(var55, 1633828214);
                        }

                        if (this.random.nextInt(100) == 0) {
                            byte var54;
                            if (var53 == 2) {
                                var54 = 30;
                            } else {
                                var54 = 15;
                            }

                            PotionEffectType var4 = PotionEffectType.SLOW_FALLING;
                            PotionEffect var66 = new PotionEffect(var4, var54, 0, true, false, false);
                            var67.addPotionEffect(var66);
                        }
                    }
                }
            }
        }
    }

    private boolean isHoldingPuff(Player var1, int var2) {
        ItemStack var5 = var1.getInventory().getItemInMainHand();
        ItemStack var6 = var1.getInventory().getItemInOffHand();
        int var13 = BlissCoreItems.gemIdOf(var5, 382441079);
        int var19 = GemType.PUFF.id();
        if (var13 != var19) {
            int var15 = BlissCoreItems.gemIdOf(var6, 382441079);
            int var21 = GemType.PUFF.id();
            if (var15 != var21) {
                GemType var22 = GemType.PUFF;
                if (!GoldGem.hasAbsorbedSoul(var1, var22, 1603837876)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static String victimName(LivingEntity var0, int var1) {
        if (var0 instanceof Player) {
            return ((Player)var0).getName();
        } else {
            String var5 = var0.getCustomName();
            String var10;
            if (var5 != null) {
                var10 = var5;
            } else {
                var10 = var0.getType().name().toLowerCase().replace('_', ' ');
            }

            return var10;
        }
    }

    private static String readable(long var0, int var2) {
        int var8 = (int)((var0 + 999L) / 1000L);
        int var9 = var8 / 60;
        int var10 = var8 % 60;
        if (var9 == 0) {
            return var10 + "s";
        } else {
            return var10 == 0 ? var9 + "m" : var9 + "m " + var10 + "s";
        }
    }
}
