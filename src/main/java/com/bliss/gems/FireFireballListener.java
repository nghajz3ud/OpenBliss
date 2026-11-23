package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.BrokenStatePenaltyListener;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityKnockbackEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityKnockbackEvent.KnockbackCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class FireFireballListener implements Listener {
    private static final int MAX_POWER = 2;
    private static final double CHARGE_SECONDS = 50.0;
    private static final double CHARGE_FAST_SECONDS = 30.0;
    private static final int STAGES_PER_POWER = 20;
    private static final int MAX_HOLD_TICKS = 1000;
    private static final long FIREBALL_DAMAGE_WINDOW_MS = 90L;
    private static final int METEOR_SHOWER_COUNT = 20;
    private static final int METEOR_SHOWER_INTERVAL_TICKS = 10;
    private static final int METEOR_SHOWER_HEIGHT = 10;
    private static final float METEOR_SHOWER_PUSH = 2.0F;
    private static final int METEOR_MAX_FLIGHT_TICKS = 100;
    private static final int ZERO_DAMAGE_AFTER_LAUNCH_TICKS = 80;
    private static final double SELF_BOOST_HORIZ_RADIUS = 2.0;
    private static final double SELF_BOOST_BASE = 0.9;
    private static final double SELF_BOOST_PER_POWER = 0.05;
    private static final NamespacedKey KEY_POWER = NamespacedKey.fromString("bliss:fireball_power");
    private static final NamespacedKey KEY_SHOOTER = NamespacedKey.fromString("bliss:fireball_shooter");
    private static final NamespacedKey KEY_SHOWER = NamespacedKey.fromString("bliss:fireball_shower");
    private static final NamespacedKey KEY_SHOWER_TARGET = NamespacedKey.fromString("bliss:fireball_shower_target");
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final FireGemListener fire;
    private final GlobalState globalState;
    private final TrustStore trust;
    private final Random random;
    private final Map<UUID, FireFireballListener.ChargeState> charges;
    private final Map<UUID, Long> zeroDamageUntil;
    private final Map<UUID, Integer> lastInteractTick;
    private long lastExplosionMs;
    private UUID lastExplosionShooter;
    private boolean lastExplosionWasShower;
    private UUID lastExplosionShowerTarget;
    private boolean inShowerBlast;
    private final Map<UUID, Long> lastMeteorHitTick;
    private static final int METEOR_HIT_GAP_TICKS = 10;
    private static final DustOptions CHARGE_DUST;
    private final List<FireFireballListener.MeteorRain> rains;
    private final List<FireFireballListener.WindowRestore> showerWindowRestores;

    public FireFireballListener(BlissPlugin var1, PlayerDataStore var2, FireGemListener var3, GlobalState var4, TrustStore var5, int var6) {
        Random var8 = new Random();
        this.random = var8;
        ConcurrentHashMap var22 = new ConcurrentHashMap();
        this.charges = var22;
        ConcurrentHashMap var23 = new ConcurrentHashMap();
        this.zeroDamageUntil = var23;
        ConcurrentHashMap var24 = new ConcurrentHashMap();
        this.lastInteractTick = var24;
        this.lastExplosionMs = 0L;
        ConcurrentHashMap var27 = new ConcurrentHashMap();
        this.lastMeteorHitTick = var27;
        ArrayList var28 = new ArrayList();
        this.rains = var28;
        ArrayList var29 = new ArrayList();
        this.showerWindowRestores = var29;
        this.plugin = var1;
        this.players = var2;
        this.fire = var3;
        this.globalState = var4;
        this.trust = var5;
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent var1) {
        Action var16 = var1.getAction();
        Action var3 = Action.LEFT_CLICK_AIR;
        if (var16 != var3) {
            Action var18 = var1.getAction();
            Action var60 = Action.LEFT_CLICK_BLOCK;
            if (var18 != var60) {
                return;
            }
        }

        Player var8 = var1.getPlayer();
        UUID var9 = var8.getUniqueId();
        int var10 = Bukkit.getCurrentTick();
        Map var25 = this.lastInteractTick;
        Integer var11 = (Integer)var25.get(var9);
        if (var11 != null) {
            int var30 = var11;
            if (var30 == var10) {
                return;
            }
        }

        Map var32 = this.lastInteractTick;
        Integer var71 = var10;
        var32.put(var9, var71);
        ItemStack var12 = var8.getInventory().getItemInMainHand();
        GemType var64 = GemType.FIRE;
        boolean var13 = GoldGem.isWieldingSoulMain(var8, var64, 244096655);
        if (BlissCoreItems.isFireGem(var12, 1896087501) || var13) {
            if (var13 || BlissCoreItems.tierOf(var12, 1633828214) == 2) {
                PlayerDataStore var46 = this.players;
                if (var46.get(var9, 1264010596).canUsePowers(1616892701)) {
                    if (!GemAbilityRules.on(GemAbilityRules.GemAbility.FIRE_FIREBALL, 425337338)) {
                        Map var53 = this.charges;
                        FireFireballListener.ChargeState var15 = (FireFireballListener.ChargeState)var53.get(var9);
                        if (var15 != null) {
                            this.cancelCharge(var9, var15, 130160311);
                        }
                    } else {
                        BukkitScheduler var58 = Bukkit.getScheduler();
                        BlissPlugin var70 = this.plugin;
                        Runnable var74 = () -> {
                            if (!EntityClickGuard.meleeSince(var9, var10, 1033776613)) {
                                if (var8.isOnline()) {
                                    long var10x;
                                    Long var12x;
                                    boolean var42;
                                    label31: {
                                        var10x = System.currentTimeMillis();
                                        Map var22 = this.fire.cd3;
                                        var12x = (Long)var22.get(var9);
                                        if (var12x != null) {
                                            long var27 = var12x;
                                            if (var27 > var10x) {
                                                var42 = true;
                                                break label31;
                                            }
                                        }

                                        var42 = false;
                                    }

                                    Map var31 = this.charges;
                                    FireFireballListener.ChargeState var14 = (FireFireballListener.ChargeState)var31.get(var9);
                                    if (var14 != null) {
                                        if (var42) {
                                            long var46x = var12x;
                                            Component var52 = Text.legacy(
                                                "&6ðŸ”® &cYour Â§6ðŸ§¨Fireball &cis on cooldown for &6" + readable(var46x - var10x, 1006088703), 373192400
                                            );
                                            var8.sendMessage(var52);
                                            this.cancelCharge(var9, var14, 130160311);
                                        } else {
                                            this.launchFireball(var8, var14, 1424657481);
                                        }
                                    } else if (var42) {
                                        long var56 = var12x;
                                        Component var62 = Text.legacy(
                                            "&6ðŸ”® &cYour Â§6ðŸ§¨Fireball &cis on cooldown for &6" + readable(var56 - var10x, 1006088703), 373192400
                                        );
                                        var8.sendMessage(var62);
                                    } else {
                                        this.startCharging(var8, 215918715);
                                    }
                                }
                            }
                        };
                        var58.runTaskLater(var70, var74, 1L);
                    }
                }
            }
        }
    }

    private void startCharging(Player var1, int var2) {
        UUID var10 = var1.getUniqueId();
        FireFireballListener.ChargeState var14 = new FireFireballListener.ChargeState();
        Map var16 = this.charges;
        var16.put(var10, var14);
        Component var30 = Text.legacy("&6ðŸ”® &bCharging &6ðŸ§¨fireball", 373192400);
        var1.sendMessage(var30);
        World var20 = var1.getWorld();
        Location var32 = fireballVisualLoc(var1, 221541537);
        Consumer<ItemDisplay> var5 = var0 -> {
            var0.setPersistent(false);
            Material var3 = Material.FIRE_CHARGE;
            ItemStack var16x = new ItemStack(var3);
            var0.setItemStack(var16x);
            ItemDisplayTransform var17 = ItemDisplayTransform.GROUND;
            var0.setItemDisplayTransform(var17);
            Billboard var18 = Billboard.FIXED;
            var0.setBillboard(var18);
            Brightness var19 = new Brightness(15, 15);
            var0.setBrightness(var19);
            var0.setTeleportDuration(2);
            Vector3f var23 = new Vector3f(0.0F, 0.0F, 0.0F);
            Quaternionf var24 = new Quaternionf();
            Vector3f var25 = new Vector3f(1.5F, 1.5F, 1.5F);
            Quaternionf var26 = new Quaternionf();
            Transformation var21 = new Transformation(var23, var24, var25, var26);
            var0.setTransformation(var21);
        };
        ItemDisplay var12 = (ItemDisplay)var20.spawn(var32, ItemDisplay.class, var5);
        var14.visualStand = var12;
        Component var35 = Text.legacy("&fFireball Power", 373192400);
        Color var46 = Color.RED;
        Overlay var6 = Overlay.PROGRESS;
        BossBar var36 = BossBar.bossBar(var35, 0.0F, var46, var6);
        var14.powerBar = var36;
        BossBar var38 = var14.powerBar;
        var1.showBossBar(var38);
        var14.power = 0;
        BukkitScheduler var40 = Bukkit.getScheduler();
        BlissPlugin var45 = this.plugin;
        Runnable var48 = () -> this.tickCharge(var1, var14, 1718394286);
        BukkitTask var41 = var40.runTaskTimer(var45, var48, 1L, 1L);
        var14.tickTask = var41;
    }

    private static Location fireballVisualLoc(Player var0, int var1) {
        return var0.getLocation().add(0.0, 2.3, 0.0);
    }

    private void positionVisual(Player var1, FireFireballListener.ChargeState var2, int var3) {
        if (var2.visualStand != null && var2.visualStand.isValid()) {
            ItemDisplay var11 = var2.visualStand;
            Location var13 = fireballVisualLoc(var1, 221541537);
            var11.teleport(var13);
        }
    }

    private void tickCharge(Player var1, FireFireballListener.ChargeState var2, int var3) {
        if (var1.isOnline() && !var1.isDead()) {
            Map var26 = this.charges;
            UUID var91 = var1.getUniqueId();
            if (var26.containsKey(var91)) {
                this.positionVisual(var1, var2, 154125694);
                if (var2.power == 0) {
                    var2.power = 1;
                }

                boolean var89;
                label51: {
                    this.spawnChargeParticles(var1, var2, 1172352579);
                    Material var14 = var1.getLocation().clone().subtract(0.0, 1.0, 0.0).getBlock().getType();
                    Material var15 = var1.getLocation().getBlock().getType();
                    Material var97 = Material.MAGMA_BLOCK;
                    if (var14 != var97) {
                        Material var98 = Material.OBSIDIAN;
                        if (var14 != var98) {
                            Material var99 = Material.LAVA;
                            if (var15 != var99) {
                                var89 = false;
                                break label51;
                            }
                        }
                    }

                    var89 = true;
                }

                int var17 = maxPower(1948074776);
                double var18 = ticksPerStage(var89, var17, 606216652);
                int var102 = var2.tickAccum + 1;
                var2.tickAccum = var102;
                double var103 = var2.stageProgress;
                double var155 = 1.0 / var18;
                double var105 = var103 + var155;
                var2.stageProgress = var105;
                if (var2.stageProgress < 1.0) {
                    return;
                }

                double var109 = var2.stageProgress - 1.0;
                var2.stageProgress = var109;
                var2.tickAccum = 0;
                int var113 = var2.stage + 1;
                var2.stage = var113;
                if (var2.stage >= 20) {
                    var2.stage = 0;
                    int var63 = var2.power;
                    if (var63 < var17) {
                        int var118 = var2.power + 1;
                        var2.power = var118;
                    }

                    int var66 = var2.power;
                    if (var66 >= var17) {
                        if (var2.powerBar != null) {
                            BossBar var130 = var2.powerBar;
                            var1.hideBossBar(var130);
                            var2.powerBar = null;
                        }

                        Component var133 = Text.legacy("&4Fireball Timer", 373192400);
                        Color var159 = Color.RED;
                        Overlay var9 = Overlay.PROGRESS;
                        BossBar var134 = BossBar.bossBar(var133, 1.0F, var159, var9);
                        var2.timerBar = var134;
                        BossBar var136 = var2.timerBar;
                        var1.showBossBar(var136);
                        var2.tickTask.cancel();
                        BukkitScheduler var137 = Bukkit.getScheduler();
                        BlissPlugin var151 = this.plugin;
                        Runnable var161 = () -> this.tickTimer(var1, var2, 783881563);
                        BukkitTask var138 = var137.runTaskTimer(var151, var161, 1L, 1L);
                        var2.tickTask = var138;
                        return;
                    }
                }

                if (var2.powerBar != null) {
                    int var72 = (var2.power - 1) * 20;
                    int var123 = var2.stage;
                    float var75 = (float)(var72 + var123);
                    float var126 = (float)(var17 * 20);
                    float var21 = var75 / var126;
                    BossBar var78 = var2.powerBar;
                    float var128 = Math.min(1.0F, var21);
                    var78.progress(var128);
                }

                return;
            }
        }

        UUID var140 = var1.getUniqueId();
        this.cancelCharge(var140, var2, 130160311);
    }

    private void spawnChargeParticles(Player var1, FireFireballListener.ChargeState var2, int var3) {
        if (var2.power > 0) {
            int var24 = var2.tickAccum;
            int var33 = var2.holdTicks;
            if ((var24 + var33) % 5 == 0) {
                Location var21 = var1.getLocation().add(0.0, 1.0, 0.0);
                World var32 = var1.getWorld();
                Particle var37 = Particle.DUST;
                DustOptions var19 = CHARGE_DUST;
                Particles.spawn(var32, var37, var21, 8, 0.4, 0.4, 0.4, 0.0, var19, 1141219473);
            }
        }
    }

    private void tickTimer(Player var1, FireFireballListener.ChargeState var2, int var3) {
        if (var1.isOnline() && !var1.isDead()) {
            Map var12 = this.charges;
            UUID var29 = var1.getUniqueId();
            if (var12.containsKey(var29)) {
                this.positionVisual(var1, var2, 154125694);
                this.spawnChargeParticles(var1, var2, 1172352579);
                int var33 = var2.holdTicks + 1;
                var2.holdTicks = var33;
                float var37 = (float)var2.holdTicks / 1000.0F;
                float var7 = 1.0F - var37;
                if (var7 <= 0.0F) {
                    String var48 = "fireball";
                    this.applyCooldown(var1, var48, 1833694611);
                    UUID var42 = var1.getUniqueId();
                    this.cancelCharge(var42, var2, 130160311);
                    return;
                }

                if (var2.timerBar != null) {
                    BossBar var24 = var2.timerBar;
                    var24.progress(var7);
                }

                return;
            }
        }

        UUID var44 = var1.getUniqueId();
        this.cancelCharge(var44, var2, 130160311);
    }

    private void launchFireball(final Player var1, FireFireballListener.ChargeState var2, int var3) {
        UUID var12 = var1.getUniqueId();
        int var13 = var2.power;
        Vector var52 = var1.getEyeLocation().getDirection().multiply(3);
        final LargeFireball var14 = (LargeFireball)var1.launchProjectile(LargeFireball.class, var52);
        var14.setShooter(var1);
        var14.setPersistent(false);
        var14.setYield(0.0F);
        var14.setIsIncendiary(false);
        PersistentDataContainer var26 = var14.getPersistentDataContainer();
        NamespacedKey var41 = KEY_POWER;
        PersistentDataType var53 = PersistentDataType.INTEGER;
        Integer var66 = var13;
        var26.set(var41, var53, var66);
        PersistentDataContainer var28 = var14.getPersistentDataContainer();
        NamespacedKey var42 = KEY_SHOOTER;
        PersistentDataType var54 = PersistentDataType.STRING;
        String var68 = var12.toString();
        var28.set(var42, var54, var68);
        BukkitRunnable var29 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                if (!var14.isDead() && var14.isValid() && var1.isOnline()) {
                    int var25 = this.t + 1;
                    this.t = var25;
                    if (var25 <= 200) {
                        Vector var5 = var1.getEyeLocation().getDirection();
                        LargeFireball var20 = var14;
                        Vector var27 = new Vector(0, 0, 0);
                        var20.setAcceleration(var27);
                        LargeFireball var22 = var14;
                        Vector var29 = var5.multiply(3);
                        var22.setVelocity(var29);
                        return;
                    }
                }

                this.cancel();
            }
        };
        BlissPlugin var44 = this.plugin;
        var29.runTaskTimer(var44, 1L, 1L);
        Component var46 = Text.legacy("&6ðŸ”® &bYou have launched a &6fireball!", 373192400);
        var1.sendMessage(var46);
        Map var33 = this.zeroDamageUntil;
        long var58 = System.currentTimeMillis();
        String var72 = "fire";
        String var9 = "fireball-self-immunity";
        long var77 = (long)(GemTuning.duration(var72, var9, 4.0, 812730832) * 1000.0);
        Long var62 = var58 + var77;
        var33.put(var12, var62);
        String var63 = "fireball";
        this.applyCooldown(var1, var63, 1833694611);
        this.cancelCharge(var12, var2, 130160311);
    }

    public void stopAll(int var1) {
        Set<UUID> var22 = this.charges.keySet();

        for (UUID var5 : new ArrayList<>(var22)) {
            Map var14 = this.charges;
            FireFireballListener.ChargeState var6 = (FireFireballListener.ChargeState)var14.get(var5);
            if (var6 != null) {
                this.cancelCharge(var5, var6, 130160311);
            }
        }
    }

    private void cancelCharge(UUID var1, FireFireballListener.ChargeState var2, int var3) {
        if (var2.visualStand != null && var2.visualStand.isValid()) {
            var2.visualStand.remove();
        }

        Player var6 = Bukkit.getPlayer(var1);
        if (var6 != null) {
            if (var2.powerBar != null) {
                BossBar var29 = var2.powerBar;
                var6.hideBossBar(var29);
            }

            if (var2.timerBar != null) {
                BossBar var31 = var2.timerBar;
                var6.hideBossBar(var31);
            }
        }

        if (var2.tickTask != null) {
            var2.tickTask.cancel();
        }

        Map var27 = this.charges;
        var27.remove(var1);
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onMeteorShowerTrigger(EntityDamageByEntityEvent var1) {
        Player var8 = null;
        Entity var12 = var1.getDamager();
        if (var12 instanceof Player) {
            var8 = (Player)var12;
        } else {
            Entity var129 = var1.getDamager();
            if (var129 instanceof Projectile) {
                ProjectileSource var130 = ((Projectile)var129).getShooter();
                if (var130 instanceof Player) {
                    var8 = (Player)var130;
                }
            }
        }

        if (var8 != null) {
            DamageCause var36 = var1.getCause();
            DamageCause var3 = DamageCause.SONIC_BOOM;
            if (var36 != var3) {
                DamageCause var38 = var1.getCause();
                DamageCause var95 = DamageCause.THORNS;
                if (var38 != var95) {
                    if (var1.getDamager() instanceof Fireball) {
                        return;
                    }

                    if (var1.getEntity() instanceof LivingEntity var9) {
                        if (var9.equals(var8)) {
                            return;
                        }

                        ItemStack var127 = var8.getInventory().getItemInMainHand();
                        GemType var97 = GemType.FIRE;
                        boolean var128 = GoldGem.isWieldingSoulMain(var8, var97, 244096655);
                        if (!BlissCoreItems.isFireGem(var127, 1896087501) && !var128) {
                            return;
                        }

                        if (!var128 && BlissCoreItems.tierOf(var127, 1633828214) != 2) {
                            return;
                        }

                        PlayerDataStore var62 = this.players;
                        UUID var100 = var8.getUniqueId();
                        if (!var62.get(var100, 1264010596).canUsePowers(1616892701)) {
                            return;
                        }

                        if (!GemAbilityRules.on(GemAbilityRules.GemAbility.FIRE_METEOR, 425337338)) {
                            return;
                        }

                        long var13 = System.currentTimeMillis();
                        Map var72 = this.fire.cd3;
                        UUID var102 = var8.getUniqueId();
                        Long var15 = (Long)var72.get(var102);
                        if (var15 != null) {
                            long var77 = var15;
                            if (var77 > var13) {
                                long var114 = var15;
                                Component var120 = Text.legacy(
                                    "<##FE8120>ðŸ”® <##FF686F>Your &fðŸ§¨<##FE8120>Meteor Shower &cis on cooldown for &e"
                                        + readable(var114 - var13, 1006088703),
                                    373192400
                                );
                                var8.sendMessage(var120);
                                return;
                            }
                        }

                        if (var9 instanceof Player var16) {
                            TrustStore var85 = this.trust;
                            UUID var104 = var8.getUniqueId();
                            UUID var122 = var16.getUniqueId();
                            if (var85.isTrusted(var104, var122, 1736364606)) {
                                Component var112 = Text.legacy(
                                    "<##FE8120>ðŸ”® <##FF686F>You cannot cast a &fðŸ§¨<##FE8120>Meteor Shower <##FF686F>on an ally.", 373192400
                                );
                                var8.sendMessage(var112);
                                return;
                            }
                        }

                        Component var108 = Text.legacy(
                            "<##FE8120>ðŸ”® <##B8FFFB>You have summoned a <##FE8120>Meteor Shower <##B8FFFB>on " + var9.getName() + "!", 373192400
                        );
                        var8.sendMessage(var108);
                        String var123 = "meteor-shower";
                        this.applyCooldown(var8, var123, 1833694611);
                        this.scheduleMeteorRain(var8, var9, 1720348649);
                        return;
                    }

                    return;
                }
            }
        }
    }

    private void scheduleMeteorRain(Player var1, LivingEntity var2, int var3) {
        UUID var19 = var1.getUniqueId();
        FireFireballListener.MeteorRain var4 = new FireFireballListener.MeteorRain(var19, var2, 729865893);
        List var10 = this.rains;
        var10.add(var4);
        BlissPlugin var16 = this.plugin;
        var4.runTaskTimer(var16, 0L, 10L);
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    public void onDeathEndsShowers(EntityDeathEvent var1) {
        if (!this.rains.isEmpty()) {
            UUID var4 = var1.getEntity().getUniqueId();
            List<FireFireballListener.MeteorRain> var28 = this.rains;

            for (FireFireballListener.MeteorRain var6 : new ArrayList<>(var28)) {
                UUID var20 = var6.victimId;
                if (!var20.equals(var4)) {
                    UUID var23 = var6.shooterId;
                    if (!var23.equals(var4)) {
                        continue;
                    }
                }

                var6.end(740550479);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent var1) {
        if (var1.getEntity() instanceof Fireball var13) {
            PersistentDataContainer var33 = var13.getPersistentDataContainer();
            NamespacedKey var3 = KEY_POWER;
            PersistentDataType var4 = PersistentDataType.INTEGER;
            Integer var14 = (Integer)var33.get(var3, var4);
            PersistentDataContainer var37 = var13.getPersistentDataContainer();
            NamespacedKey var120 = KEY_SHOWER;
            PersistentDataType var144 = PersistentDataType.INTEGER;
            Integer var15 = (Integer)var37.get(var120, var144);
            PersistentDataContainer var41 = var13.getPersistentDataContainer();
            NamespacedKey var121 = KEY_SHOOTER;
            PersistentDataType var145 = PersistentDataType.STRING;
            String var16 = (String)var41.get(var121, var145);
            if (var14 != null || var15 != null) {
                UUID var119;
                if (var16 == null) {
                    var119 = null;
                } else {
                    var119 = UUID.fromString(var16);
                }

                UUID var17 = var119;
                PersistentDataContainer var49 = var13.getPersistentDataContainer();
                NamespacedKey var122 = KEY_SHOWER_TARGET;
                PersistentDataType var146 = PersistentDataType.STRING;
                String var18 = (String)var49.get(var122, var146);
                long var123 = System.currentTimeMillis();
                this.lastExplosionMs = var123;
                this.lastExplosionShooter = var119;
                boolean var142;
                if (var15 != null) {
                    var142 = true;
                } else {
                    var142 = false;
                }

                this.lastExplosionWasShower = var142;
                UUID var143;
                if (var18 == null) {
                    var143 = null;
                } else {
                    var143 = UUID.fromString(var18);
                }

                this.lastExplosionShowerTarget = var143;
                Location var19 = var13.getLocation();
                var13.remove();
                if (var15 != null) {
                    if (var1.getHitEntity() != null) {
                        var1.setCancelled(true);
                    }

                    this.inShowerBlast = true;

                    try {
                        World var111 = var19.getWorld();
                        var111.createExplosion(var19, 3.5F, false, false);
                    } finally {
                        this.inShowerBlast = false;
                        this.settleShowerWindows(1093183437);
                    }
                } else {
                    int var61;
                    if (var14 == null) {
                        var61 = 1;
                    } else {
                        var61 = var14;
                    }

                    float var20 = explosionForce(var61, 336755319);
                    World var64 = var19.getWorld();
                    var64.createExplosion(var19, var20, false, true);
                    double var21 = (double)var20 * 2.0;
                    World var72 = var19.getWorld();

                    for (Entity var24 : var72.getNearbyEntities(var19, var21, var21, var21)) {
                        if (var24 instanceof LivingEntity) {
                            LivingEntity var25 = (LivingEntity)var24;
                            if (var17 != null) {
                                UUID var86 = var24.getUniqueId();
                                if (var86.equals(var17)) {
                                    continue;
                                }
                            }

                            var25.setNoDamageTicks(0);
                        }
                    }

                    Player var104;
                    if (var17 == null) {
                        var104 = null;
                    } else {
                        var104 = Bukkit.getPlayer(var17);
                    }

                    if (var104 != null && var104.isOnline() && atOwnFeet(var104, var19, 252738020)) {
                        int var153;
                        if (var14 == null) {
                            var153 = 1;
                        } else {
                            var153 = var14;
                        }

                        int var161 = maxPower(1948074776);
                        double var157 = (double)Math.min(var153, var161) * 0.05;
                        double var175 = 0.9 + var157;
                        Vector var26 = var104.getVelocity();
                        double var163 = var26.getX();
                        double var171 = var26.getZ();
                        Vector var134 = new Vector(var163, var175, var171);
                        var104.setVelocity(var134);
                        Component var136 = Text.legacy("&6ðŸ”® &bFireball &6boost!", 373192400);
                        var104.sendMessage(var136);
                    }
                }
            }
        }
    }

    private static boolean atOwnFeet(Player var0, Location var1, int var2) {
        World var16 = var0.getWorld();
        World var52 = var1.getWorld();
        if (!var16.equals(var52)) {
            return false;
        } else {
            double var20 = var1.getX();
            double var54 = var0.getX();
            double var8 = var20 - var54;
            double var25 = var1.getZ();
            double var57 = var0.getZ();
            double var10 = var25 - var57;
            String var29 = "fire";
            String var53 = "fireball-self-boost";
            double var12 = GemTuning.radius(var29, var53, 2.0, 1058601812);
            double var34 = var8 * var8;
            double var65 = var10 * var10;
            double var36 = var34 + var65;
            double var69 = var12 * var12;
            if (var36 > var69) {
                return false;
            } else {
                double var40 = var1.getY();
                double var73 = var0.getLocation().getY();
                double var14 = var40 - var73;
                boolean var50;
                if (var14 <= 1.0 && var14 >= -3.0) {
                    var50 = true;
                } else {
                    var50 = false;
                }

                return var50;
            }
        }
    }

    private static int maxPower(int var0) {
        String var3 = "fire";
        String var4 = "fireball-max-power";
        double var10 = GemTuning.power(var3, var4, 2.0, 1503850832);
        return (int)Math.max(1.0, var10);
    }

    private static double ticksPerStage(boolean var0, int var1, int var2) {
        double var21;
        if (var0) {
            String var20 = "fire";
            String var25 = "fireball-charge-fast";
            var21 = GemTuning.duration(var20, var25, 30.0, 812730832);
        } else {
            String var13 = "fire";
            String var4 = "fireball-charge";
            var21 = GemTuning.duration(var13, var4, 50.0, 812730832);
        }

        int var24 = var1 - 1;
        int var12 = Math.max(1, var24);
        double var29 = var21 * 20.0;
        double var37 = (double)(var12 * 20);
        double var31 = var29 / var37;
        return Math.max(1.0, var31);
    }

    private static float explosionForce(int var0, int var1) {
        if (var0 <= 1) {
            return 4.5F;
        } else if (var0 >= 10) {
            return 8.5F;
        } else {
            float var14 = (float)(var0 - 1) * 0.5F;
            return 4.5F + var14;
        }
    }

    @EventHandler
    public void onPostExplosionDamage(EntityDamageEvent var1) {
        long var22 = System.currentTimeMillis();
        long var200 = this.lastExplosionMs;
        if (var22 - var200 <= 90L) {
            DamageCause var28 = var1.getCause();
            DamageCause var4 = DamageCause.ENTITY_EXPLOSION;
            if (var28 != var4) {
                DamageCause var30 = var1.getCause();
                DamageCause var149 = DamageCause.BLOCK_EXPLOSION;
                if (var30 != var149 && !ourFireballContact(var1, 281035805)) {
                    return;
                }
            }

            double var153 = var1.getDamage() * 0.3;
            var1.setDamage(var153);
            GemAbilityRules.GemAbility var36;
            if (this.lastExplosionWasShower) {
                var36 = GemAbilityRules.GemAbility.FIRE_METEOR;
            } else {
                var36 = GemAbilityRules.GemAbility.FIRE_FIREBALL;
            }

            if (!GemAbilityRules.on(var36, 425337338)) {
                var1.setCancelled(true);
            } else {
                double var205 = var1.getDamage();
                double var157 = GemAbilityRules.scaled(var36, var205, 1684677315);
                String var238 = "fire";
                String var266;
                if (this.lastExplosionWasShower) {
                    var266 = "meteor-shower";
                } else {
                    var266 = "fireball";
                }

                double var239 = GemTuning.damage(var238, var266, 1.0, 1172948035);
                double var159 = var157 * var239;
                var1.setDamage(var159);
                if (var1.getEntity() instanceof Player) {
                    String var44 = "fire";
                    String var161 = "true-damage";
                    boolean var207 = ConsistentDamage.trueDamage(1667644725);
                    if (GemTuning.flag(var44, var161, var207, 1537375024)) {
                        double var15 = var1.getDamage();

                        for (int var17 = 0; var17 < 8; var17++) {
                            double var18 = afterArmor(var1, 72972200);
                            if (var18 <= 0.0) {
                                break;
                            }

                            double var61 = Math.abs(var18 - var15);
                            double var214 = var15 * 0.005;
                            if (var61 <= var214) {
                                break;
                            }

                            double var196 = var1.getDamage();
                            double var258 = Math.pow(var15 / var18, 0.6);
                            double var198 = var196 * var258;
                            var1.setDamage(var198);
                        }
                    }
                }

                String var64 = "fire";
                String var163 = "fireball-cap";
                double var279 = GemTuning.damage(var64, var163, 0.0, 1172948035);
                if (var279 > 0.0 && !this.lastExplosionWasShower) {
                    double var73 = afterArmor(var1, 72972200);
                    if (var73 > var279) {
                        for (byte var281 = 0; var281 < 8; var281 += 1) {
                            double var285 = afterArmor(var1, 72972200);
                            double var85 = Math.abs(var285 - var279);
                            double var226 = var279 * 0.005;
                            if (var85 <= var226) {
                                break;
                            }

                            double var191 = var1.getDamage();
                            double var252 = Math.pow(var279 / var285, 0.6);
                            double var193 = var191 * var252;
                            var1.setDamage(var193);
                        }

                        double var282 = afterArmor(var1, 72972200);
                        if (var282 > var279) {
                            double var166 = var1.getDamage();
                            double var168 = var166 * var279;
                            double var170 = var168 / var282;
                            var1.setDamage(var170);
                        }
                    }
                }

                UUID var284 = var1.getEntity().getUniqueId();
                if (this.lastExplosionShooter != null) {
                    UUID var173 = this.lastExplosionShooter;
                    if (!var284.equals(var173)) {
                        GlobalState var103 = this.globalState;
                        PlayerDataStore var175 = this.players;
                        Entity var231 = var1.getEntity();
                        if (BrokenStatePenaltyListener.brokenVictim(var103, var175, var231, 1142985265)) {
                            double var179 = var1.getDamage() * 2.0;
                            var1.setDamage(var179);
                        }
                    }
                }

                label103: {
                    if (this.lastExplosionShooter != null) {
                        UUID var182 = this.lastExplosionShooter;
                        if (var284.equals(var182)) {
                            var1.setCancelled(true);
                            break label103;
                        }
                    }

                    if (this.lastExplosionShooter != null && var1.getEntity() instanceof Player) {
                        TrustStore var144 = this.trust;
                        UUID var188 = this.lastExplosionShooter;
                        if (var144.isTrusted(var188, var284, 1736364606)) {
                            var1.setCancelled(true);
                        }
                    }
                }

                if (this.inShowerBlast && !var1.isCancelled() && var1.getEntity() instanceof Player var287) {
                    long var288 = (long)Bukkit.getCurrentTick();
                    Map var125 = this.lastMeteorHitTick;
                    Long var21 = (Long)var125.get(var284);
                    if (var21 != null) {
                        long var233 = var21;
                        if (var288 - var233 < 10L) {
                            var1.setCancelled(true);
                            return;
                        }
                    }

                    List var136 = this.showerWindowRestores;
                    int var268 = var287.getNoDamageTicks();
                    double var277 = var287.getLastDamage();
                    FireFireballListener.WindowRestore var186 = new FireFireballListener.WindowRestore(var1, var287, var268, var277);
                    var136.add(var186);
                }
            }
        }
    }

    private void settleShowerWindows(int var1) {
        long var6 = (long)Bukkit.getCurrentTick();

        for (FireFireballListener.WindowRestore var9 : this.showerWindowRestores) {
            if (!var9.event().isCancelled() && var9.victim().isValid() && !var9.victim().isDead()) {
                Player var32 = var9.victim();
                int var38 = var9.iframes();
                var32.setNoDamageTicks(var38);
                Player var34 = var9.victim();
                double var40 = var9.last();
                var34.setLastDamage(var40);
                Map var36 = this.lastMeteorHitTick;
                UUID var44 = var9.victim().getUniqueId();
                Long var45 = var6;
                var36.put(var44, var45);
            }
        }

        this.showerWindowRestores.clear();
    }

    private static boolean ourFireballContact(EntityDamageEvent var0, int var1) {
        DamageCause var7 = var0.getCause();
        DamageCause var3 = DamageCause.PROJECTILE;
        if (var7 == var3 && var0 instanceof EntityDamageByEntityEvent) {
            Entity var6 = ((EntityDamageByEntityEvent)var0).getDamager();
            if (var6 instanceof LargeFireball) {
                Set var20 = ((LargeFireball)var6).getPersistentDataContainer().getKeys();
                NamespacedKey var23 = KEY_SHOOTER;
                if (var20.contains(var23)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static double afterArmor(EntityDamageEvent var0, int var1) {
        double var6 = var0.getDamage();
        DamageModifier var3 = DamageModifier.ARMOR;
        if (var0.isApplicable(var3)) {
            DamageModifier var5 = DamageModifier.ARMOR;
            double var25 = var0.getDamage(var5);
            var6 += var25;
        }

        DamageModifier var24 = DamageModifier.MAGIC;
        if (var0.isApplicable(var24)) {
            DamageModifier var30 = DamageModifier.MAGIC;
            double var28 = var0.getDamage(var30);
            var6 += var28;
        }

        return var6;
    }

    @EventHandler
    public void onPostExplosionKnockback(EntityKnockbackEvent var1) {
        KnockbackCause var5 = var1.getCause();
        KnockbackCause var3 = KnockbackCause.EXPLOSION;
        if (var5 == var3) {
            if (this.inShowerBlast) {
                label29: {
                    UUID var4 = var1.getEntity().getUniqueId();
                    if (this.lastExplosionShooter != null) {
                        UUID var21 = this.lastExplosionShooter;
                        if (var4.equals(var21)) {
                            break label29;
                        }
                    }

                    if (this.lastExplosionShowerTarget == null) {
                        return;
                    }

                    UUID var23 = this.lastExplosionShowerTarget;
                    if (!var4.equals(var23)) {
                        return;
                    }
                }

                var1.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onZeroDamageWindow(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Player var6) {
            DamageCause var32 = var1.getCause();
            DamageCause var3 = DamageCause.BLOCK_EXPLOSION;
            if (var32 != var3) {
                DamageCause var28 = DamageCause.ENTITY_EXPLOSION;
                if (var32 != var28) {
                    return;
                }
            }

            Map var19 = this.zeroDamageUntil;
            UUID var30 = var6.getUniqueId();
            Long var8 = (Long)var19.get(var30);
            if (var8 != null) {
                long var24 = var8;
                long var4 = System.currentTimeMillis();
                if (var24 >= var4) {
                    var1.setCancelled(true);
                    return;
                }
            }
        }
    }

    private void applyCooldown(Player var1, String var2, int var3) {
        PlayerDataStore var18 = this.players;
        UUID var31 = var1.getUniqueId();
        PlayerData var14 = var18.get(var31, 1264010596);
        GlobalState var21 = this.globalState;
        boolean var15 = var21.holdsDragonEgg(var1, 715974514);
        int var24 = var14.energy(1160853318);
        long var16 = cooldownMs(var24, var15, 1353330827);
        Map var29 = this.fire.cd3;
        UUID var35 = var1.getUniqueId();
        long var6 = System.currentTimeMillis();
        String var8 = "fire";
        long var39 = GemTuning.cd(var8, var2, 75.0, var16, 1621983041);
        int var44 = var14.energy(1160853318);
        long var41 = EnergyScale.cd(var39, var44, 907518618);
        Long var38 = var6 + var41;
        var29.put(var35, var38);
    }

    private static long cooldownMs(int var0, boolean var1, int var2) {
        if (var0 > 5) {
            long var21;
            if (var1) {
                var21 = 40000L;
            } else {
                var21 = 75000L;
            }

            return var21;
        } else if (var0 == 5) {
            long var18;
            if (var1) {
                var18 = 45000L;
            } else {
                var18 = 90000L;
            }

            return var18;
        } else if (var0 == 4) {
            long var15;
            if (var1) {
                var15 = 50000L;
            } else {
                var15 = 105000L;
            }

            return var15;
        } else if (var0 == 3) {
            long var12;
            if (var1) {
                var12 = 60000L;
            } else {
                var12 = 120000L;
            }

            return var12;
        } else {
            long var9;
            if (var1) {
                var9 = 75000L;
            } else {
                var9 = 150000L;
            }

            return var9;
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

    static {
        org.bukkit.Color var12 = org.bukkit.Color.fromRGB(255, 119, 0);
        DustOptions var11 = new DustOptions(var12, 1.0F);
        CHARGE_DUST = var11;
    }

    private static final class ChargeState {
        int power = 0;
        int stage = 0;
        int tickAccum = 0;
        double stageProgress = 0.0;
        int holdTicks = 0;
        BossBar powerBar;
        BossBar timerBar;
        ItemDisplay visualStand;
        BukkitTask tickTask;
    }

    private final class MeteorRain extends BukkitRunnable {
        final UUID shooterId;
        final UUID victimId;
        final LivingEntity victim;
        final int count;
        int step;

        MeteorRain(UUID nullx, LivingEntity nullxx, int var4) {
            double var27 = GemTuning.power("fire", "meteor-count", 20.0, 1503850832);
            int var21 = (int)Math.max(1.0, var27);
            this.count = var21;
            this.step = 0;
            this.shooterId = nullx;
            UUID var25 = nullxx.getUniqueId();
            this.victimId = var25;
            this.victim = nullxx;
        }

        void end(int var1) {
            List var5 = FireFireballListener.this.rains;
            var5.remove(this);
            this.cancel();
        }

        public void run() {
            Player var10 = Bukkit.getPlayer(this.shooterId);
            int var21 = this.step;
            int var72 = this.count;
            if (var21 < var72 && this.victim.isValid() && !this.victim.isDead() && var10 != null && var10.isOnline() && !var10.isDead()) {
                World var35 = this.victim.getWorld();
                World var74 = var10.getWorld();
                if (var35.equals(var74)) {
                    double var11 = FireFireballListener.this.random.nextDouble() * 4.0 - 2.0;
                    double var13 = FireFireballListener.this.random.nextDouble() * 4.0 - 2.0;
                    Location var58 = this.victim.getLocation().clone();
                    Location var15 = var58.add(var11, 10.0, var13);
                    String var60 = "fire";
                    String var77 = "meteor-push";
                    final float var16 = (float)GemTuning.power(var60, var77, 2.0, 1503850832);
                    World var65 = var10.getWorld();
                    Consumer<LargeFireball> var98 = var3 -> {
                        var3.setShooter(var10);
                        var3.setPersistent(false);
                        var3.setYield(0.0F);
                        var3.setIsIncendiary(false);
                        PersistentDataContainer var14 = var3.getPersistentDataContainer();
                        NamespacedKey var24 = FireFireballListener.KEY_SHOOTER;
                        PersistentDataType var6 = PersistentDataType.STRING;
                        String var32 = this.shooterId.toString();
                        var14.set(var24, var6, var32);
                        PersistentDataContainer var16x = var3.getPersistentDataContainer();
                        NamespacedKey var25 = FireFireballListener.KEY_SHOWER;
                        PersistentDataType var29 = PersistentDataType.INTEGER;
                        Integer var34 = 1;
                        var16x.set(var25, var29, var34);
                        PersistentDataContainer var18 = var3.getPersistentDataContainer();
                        NamespacedKey var26 = FireFireballListener.KEY_SHOWER_TARGET;
                        PersistentDataType var30 = PersistentDataType.STRING;
                        String var37 = this.victimId.toString();
                        var18.set(var26, var30, var37);
                        Vector var27 = new Vector(0, 0, 0);
                        var3.setAcceleration(var27);
                        float var41 = 0.0F - var16;
                        Vector var28 = new Vector(0.0F, var41, 0.0F);
                        var3.setVelocity(var28);
                    };
                    final LargeFireball var17 = (LargeFireball)var65.spawn(var15, LargeFireball.class, var98);
                    BukkitRunnable var68 = new BukkitRunnable() {
                        int t = 0;

                        public void run() {
                            if (var17.isValid() && !var17.isDead()) {
                                int var20 = this.t + 1;
                                this.t = var20;
                                if (var20 > 100) {
                                    var17.remove();
                                    this.cancel();
                                } else {
                                    LargeFireball var15 = var17;
                                    Vector var22 = new Vector(0, 0, 0);
                                    var15.setAcceleration(var22);
                                    LargeFireball var17x = var17;
                                    float var29 = var16;
                                    float var27 = 0.0F - var29;
                                    Vector var23 = new Vector(0.0F, var27, 0.0F);
                                    var17x.setVelocity(var23);
                                }
                            } else {
                                this.cancel();
                            }
                        }
                    };
                    BlissPlugin var81 = FireFireballListener.this.plugin;
                    var68.runTaskTimer(var81, 1L, 1L);
                    int var83 = this.step + 1;
                    this.step = var83;
                    return;
                }
            }

            this.end(740550479);
        }
    }

    private static record WindowRestore(EntityDamageEvent event, Player victim, int iframes, double last) {
    }
}
