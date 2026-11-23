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
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class SpeedStormListener implements Listener {
    private static final int TARGET_RANGE = 15;
    private static final int DURATION_TICKS = 110;
    private static final double MAX_RADIUS = 6.0;
    private static final int DAMAGE_INTERVAL = 10;
    private static final int SLOWNESS_TICKS = 60;
    private static final int SLOWNESS_AMP = 4;
    private static final int SPEED_NORMAL = 2;
    private static final int SPEED_LAST = 4;
    private static final int SELF_SPEED_TICKS = 100;
    private static final int TRUSTED_SPEED_AMP = 4;
    private static final int TRUSTED_SPEED_TICKS = 40;
    private static final int WIND_CD_TICKS = 20;
    private static final double SELF_BUFF_RANGE_SQ = 36.0;
    private static final int GUST_PERIOD = 22;
    private static final int GUST_POINTS = 20;
    private static final double CLOUD_HEIGHT = 7.0;
    private static final double CLOUD_R1 = 1.61;
    private static final double CLOUD_R2 = 3.15;
    private static final Color CLOUD_CORE = Color.fromRGB(248, 248, 252);
    private static final Color CLOUD_MID = Color.fromRGB(211, 211, 214);
    private static final Color CLOUD_RIM = Color.fromRGB(179, 179, 182);
    private static final int STRIKE_TICK = 90;
    private static final int STRIKE_BOLTS = 6;
    private static final double STRIKE_SPREAD = 4.2;
    private static final double BOLT_BASE_Y = 2.6;
    private static final double BOLT_LENGTH = 4.4;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final SpeedGemListener speed;
    private final TrustStore trust;
    private final Random random;
    private final Map<UUID, Integer> lastInteractTick;

    public SpeedStormListener(BlissPlugin var1, PlayerDataStore var2, SpeedGemListener var3, TrustStore var4, int var5) {
        Random var7 = new Random();
        this.random = var7;
        ConcurrentHashMap var14 = new ConcurrentHashMap();
        this.lastInteractTick = var14;
        this.plugin = var1;
        this.players = var2;
        this.speed = var3;
        this.trust = var4;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var24 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var24 != var3) {
            Action var26 = var1.getAction();
            Action var94 = Action.RIGHT_CLICK_BLOCK;
            if (var26 != var94) {
                return;
            }
        }

        GlobalState var29 = GlobalState.forPlayer(var1.getPlayer(), 1433087673);
        Player var96 = var1.getPlayer();
        if (!var29.legacySpeedFor(var96, 1914133154)) {
            Player var13 = var1.getPlayer();
            UUID var14 = var13.getUniqueId();
            int var15 = Bukkit.getCurrentTick();
            Map var37 = this.lastInteractTick;
            Integer var16 = (Integer)var37.get(var14);
            if (var16 != null) {
                int var42 = var16;
                if (var42 == var15) {
                    return;
                }
            }

            Map var44 = this.lastInteractTick;
            Integer var122 = var15;
            var44.put(var14, var122);
            ItemStack var17 = var13.getInventory().getItemInMainHand();
            int var50 = BlissCoreItems.gemIdOf(var17, 382441079);
            int var101 = GemType.SPEED.id();
            if (var50 != var101) {
                GemType var102 = GemType.SPEED;
                if (!GoldGem.isWieldingSoulMain(var13, var102, 244096655)) {
                    return;
                }
            }

            int var54 = BlissCoreItems.gemIdOf(var17, 382441079);
            int var104 = GemType.SPEED.id();
            if (var54 != var104) {
                var17 = GoldGem.heldGoldGem(var13, 780486261);
            }

            if (BlissCoreItems.tierOf(var17, 1633828214) == 2) {
                PlayerDataStore var60 = this.players;
                PlayerData var18 = var60.get(var14, 1264010596);
                if (var18.canUsePowers(1616892701)) {
                    if (GemAbilityRules.on(GemAbilityRules.GemAbility.SPEED_STORM, 425337338)) {
                        SpeedGemListener var67 = this.speed;
                        int var19 = var67.stormCharges(var14, 85759890);
                        if (var19 <= 0) {
                            Component var109 = Text.legacy("<##FEFD17>ðŸ”® <##82F3FF>You dont have any clouds left!", 373192400);
                            var13.sendMessage(var109);
                        } else {
                            Location var20 = this.pickTarget(var13, 1670983297);
                            if (var20 == null) {
                                Component var112 = Text.legacy("<##FEFD17>ðŸ”® <##82F3FF>No valid target location!", 373192400);
                                var13.sendMessage(var112);
                            } else {
                                boolean var91;
                                if (var19 == 1) {
                                    var91 = true;
                                } else {
                                    var91 = false;
                                }

                                long var92;
                                if (GlobalState.isHoldingDragonEgg(var13, 2044938455)) {
                                    var92 = 10000L;
                                } else {
                                    var92 = 20000L;
                                }

                                SpeedGemListener var79 = this.speed;
                                String var123 = "speed";
                                String var5 = "speed-storm";
                                long var124 = GemTuning.cd(var123, var5, 20.0, var92, 1621983041);
                                int var133 = var18.energy(1160853318);
                                long var126 = EnergyScale.cd(var124, var133, 907518618);
                                var79.spendStormCharge(var14, var126, 56212168);
                                Location var81 = var13.getLocation();
                                if (var81.distanceSquared(var20) <= 36.0) {
                                    PotionEffectType var131 = PotionEffectType.SPEED;
                                    byte var135;
                                    if (var91) {
                                        var135 = 4;
                                    } else {
                                        var135 = 2;
                                    }

                                    PotionEffect var116 = new PotionEffect(var131, 100, var135, true, false, false);
                                    var13.addPotionEffect(var116);
                                }

                                this.drawLightningColumn(var20, 252807994);
                                this.playCastSound(var20, 193881606);
                                Component var120 = Text.legacy("<##FEFD17>ðŸ”® <##82F3FF>You summoned a <##FEFD17>ðŸŒ© Speed Storm", 373192400);
                                var13.sendMessage(var120);
                                this.runStorm(var13, var20, 1114682792);
                            }
                        }
                    }
                }
            }
        }
    }

    private Location pickTarget(Player var1, int var2) {
        World var12 = var1.getWorld();
        Location var21 = var1.getEyeLocation();
        Vector var23 = var1.getEyeLocation().getDirection();
        RayTraceResult var10 = var12.rayTraceBlocks(var21, var23, 15.0);
        if (var10 != null && var10.getHitPosition() != null) {
            Vector var11 = var10.getHitPosition();
            World var25 = var1.getWorld();
            double var27 = var11.getX();
            double var29 = var11.getY();
            double var31 = var11.getZ();
            return new Location(var25, var27, var29, var31);
        } else {
            return null;
        }
    }

    private void runStorm(final Player var1, final Location var2, int var3) {
        final UUID var18 = var1.getUniqueId();
        PlayerDataStore var28 = this.players;
        final double var19 = EnergyScale.radius(var28.get(var18, 1264010596).energy(1160853318), 1465433230);
        String var33 = "speed";
        String var49 = "speed-storm";
        final int var21 = (int)Math.round(GemTuning.duration(var33, var49, 5.5, 812730832) * 20.0);
        String var41 = "speed";
        String var50 = "speed-storm";
        final double var22 = GemTuning.radius(var41, var50, 6.0, 1058601812);
        final World var24 = var2.getWorld();
        final HashSet var46 = new HashSet();
        BukkitRunnable var47 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                int var158 = this.t + 1;
                this.t = var158;
                int var160 = var21;
                if (var158 <= var160 && var1.isOnline() && !var1.isDead()) {
                    if ((this.t - 1) % 22 == 0) {
                        World var35 = var24;
                        Location var164 = var2;
                        double var360 = (double)this.t * 0.06;
                        SpeedStormListener.gustRing(var35, var164, 0.62, 0.18, var360, 1927039543);
                        World var37 = var24;
                        Location var166 = var2;
                        double var368 = (double)this.t * 0.06 + 0.4;
                        SpeedStormListener.gustRing(var37, var166, 0.31, 0.1, var368, 1927039543);
                    }

                    if (this.t % 2 == 0) {
                        double var15 = (double)this.t * 0.004;
                        World var48 = var24;
                        Location var169 = var2;
                        Color var426 = SpeedStormListener.CLOUD_CORE;
                        SpeedStormListener.cloudPuff(var48, var169, 0.0, 0.0, 0.5, var426, 116299527);

                        for (int var19x = 0; var19x < 5; var19x++) {
                            double var331 = (double)var19x;
                            double var275 = (Math.PI * 2) * var331 / 5.0;
                            double var458 = var15 + var275;
                            World var154 = var24;
                            Location var206 = var2;
                            double var337 = Math.cos(var458);
                            double var279 = 1.61 * var337;
                            double var399 = Math.sin(var458);
                            double var341 = 1.61 * var399;
                            Color var439 = SpeedStormListener.CLOUD_MID;
                            SpeedStormListener.cloudPuff(var154, var206, var279, var341, 0.25, var439, 116299527);
                        }

                        for (byte var453 = 0; var453 < 9; var453 += 1) {
                            double var318 = (double)var453;
                            double var263 = (Math.PI * 2) * var318 / 9.0;
                            double var456 = var15 + var263 + 0.35;
                            World var148 = var24;
                            Location var204 = var2;
                            double var324 = Math.cos(var456);
                            double var269 = 3.15 * var324;
                            double var393 = Math.sin(var456);
                            double var328 = 3.15 * var393;
                            Color var438 = SpeedStormListener.CLOUD_RIM;
                            SpeedStormListener.cloudPuff(var148, var204, var269, var328, 0.0, var438, 116299527);
                        }
                    }

                    for (byte var449 = 0; var449 < 15; var449 += 1) {
                        double var17 = SpeedStormListener.this.random.nextDouble() * Math.PI * 2.0;
                        String var64 = "speed";
                        String var173 = "speed-storm";
                        double var65 = GemTuning.radius(var64, var173, 6.0, 1058601812);
                        double var226 = Math.sqrt(SpeedStormListener.this.random.nextDouble());
                        double var20 = var65 * var226;
                        World var70 = var24;
                        Particle var174 = Particle.FALLING_WATER;
                        Location var230 = var2.clone();
                        double var343 = Math.cos(var17);
                        double var283 = var20 * var343;
                        double var407 = SpeedStormListener.this.random.nextDouble() * 5.0;
                        double var347 = 5.0 + var407;
                        double var442 = Math.sin(var17);
                        double var411 = var20 * var442;
                        Location var231 = var230.add(var283, var347, var411);
                        Particles.spawn(var70, var174, var231, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                    }

                    if (this.t == 90) {
                        for (byte var450 = 0; var450 < 6; var450 += 1) {
                            SpeedStormListener var76 = SpeedStormListener.this;
                            Location var179 = var2.clone();
                            double var234 = SpeedStormListener.this.rnd(4.2, 7140326);
                            double var376 = SpeedStormListener.this.rnd(4.2, 7140326);
                            Location var180 = var179.add(var234, 0.0, var376);
                            var76.drawLightningColumn(var180, 252807994);
                        }
                    }

                    if (this.t % 10 == 0) {
                        Player var81 = var1;
                        PotionEffectType var288 = PotionEffectType.SPEED;
                        PotionEffect var182 = new PotionEffect(var288, 40, 4, true, false, false);
                        var81.addPotionEffect(var182);
                        World var84 = var24;
                        Location var184 = var2;
                        double var237 = var22;
                        double var304 = var22;
                        double var380 = var22;

                        for (Entity var452 : var84.getNearbyEntities(var184, var237, var304, var380)) {
                            if (var452 instanceof LivingEntity) {
                                LivingEntity var454 = (LivingEntity)var452;
                                Player var186 = var1;
                                if (!var454.equals(var186) && !var454.isDead()) {
                                    Location var101 = var454.getLocation();
                                    Location var188 = var2;
                                    double var102 = var101.distanceSquared(var188);
                                    double var240 = var22;
                                    double var307 = var22;
                                    double var242 = var240 * var307;
                                    if (!(var102 > var242)) {
                                        boolean var156;
                                        label81: {
                                            if (var454 instanceof Player var22x) {
                                                TrustStore var111 = SpeedStormListener.this.trust;
                                                UUID var190 = var18;
                                                UUID var245 = var22x.getUniqueId();
                                                if (var111.isTrusted(var190, var245, 1736364606)) {
                                                    var156 = true;
                                                    break label81;
                                                }
                                            }

                                            var156 = false;
                                        }

                                        if (var156) {
                                            PotionEffectType var289 = PotionEffectType.SPEED;
                                            PotionEffect var191 = new PotionEffect(var289, 40, 4, true, false, false);
                                            var454.addPotionEffect(var191);
                                            if (var454 instanceof Player) {
                                                Player var460 = (Player)var454;
                                                Set var121 = var46;
                                                UUID var193 = var460.getUniqueId();
                                                if (var121.add(var193)) {
                                                    String var196 = var1.getName();
                                                    long var252 = Math.round((double)var21 / 20.0);
                                                    Component var198 = Text.legacy(
                                                        "<##FEFD17>ðŸ”® ðŸŒ© <##B8FFFB>You have received <##FEFD17>Speed <##B8FFFB>from <##FEFD17>"
                                                            + var196
                                                            + " <##B8FFFB>for <##FEFD17>"
                                                            + var252
                                                            + " seconds",
                                                        373192400
                                                    );
                                                    var460.sendMessage(var198);
                                                }
                                            }
                                        } else {
                                            if (GemAbilityRules.on(GemAbilityRules.GemAbility.SPEED_STORM, 425337338)) {
                                                PotionEffectType var254 = PotionEffectType.SLOWNESS;
                                                PotionEffect var126 = new PotionEffect(var254, 60, 4, true, false, false);
                                                if (var454 instanceof Player var140) {
                                                    GrantedEffect.grant(var140, var126, 491118892);
                                                } else {
                                                    var454.addPotionEffect(var126);
                                                }
                                            }

                                            if (var454 instanceof Player var135) {
                                                Material var200 = Material.WIND_CHARGE;
                                                var135.setCooldown(var200, 20);
                                            }

                                            World var137 = var454.getWorld();
                                            Particle var201 = Particle.ELECTRIC_SPARK;
                                            Location var257 = var454.getLocation();
                                            double var355 = var454.getHeight() / 2.0;
                                            Location var258 = var257.add(0.0, var355, 0.0);
                                            double var385 = var19;
                                            double var315 = 0.3 * var385;
                                            double var432 = var19;
                                            double var389 = 0.3 * var432;
                                            double var445 = var19;
                                            double var436 = 0.3 * var445;
                                            Particles.spawn(var137, var201, var258, 10, var315, var389, var436, 0.05, 1986132876);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    this.cancel();
                }
            }
        };
        BlissPlugin var52 = this.plugin;
        var47.runTaskTimer(var52, 0L, 1L);
    }

    private void playCastSound(Location var1, int var2) {
        World var8 = var1.getWorld();
        if (var8 != null) {
            Sound var5 = Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE;
            var8.playSound(var1, var5, 0.15F, 1.0F);
            BukkitScheduler var12 = Bukkit.getScheduler();
            BlissPlugin var15 = this.plugin;
            Runnable var17 = () -> {
                World var6 = var1.getWorld();
                if (var6 != null) {
                    Sound var3 = Sound.ITEM_TRIDENT_THUNDER;
                    var6.playSound(var1, var3, 0.12F, 1.0F);
                }
            };
            var12.runTaskLater(var15, var17, 12L);
        }
    }

    private static void gustRing(World var0, Location var1, double var2, double var4, double var6, int var8) {
        double var25 = var1.getX();
        double var27 = var1.getY();
        double var29 = var1.getZ();

        for (int var33 = 0; var33 < 20; var33++) {
            double var72 = (double)var33;
            double var62 = (Math.PI / 10) * var72;
            double var34 = var6 + var62;
            Particle var60 = Particle.CLOUD;
            double var76 = var27 + 0.2;
            double var91 = Math.cos(var34);
            double var99 = Math.sin(var34);
            Particles.spawn(var0, var60, var25, var76, var29, 0, var91, var4, var99, var2, 1717927553);
            double var36 = var34 + (Math.PI / 20);
            Particle var61 = Particle.END_ROD;
            double var82 = var27 + 0.25;
            double var95 = Math.cos(var36);
            double var103 = Math.sin(var36);
            Particles.spawn(var0, var61, var25, var82, var29, 0, var95, var4, var103, var2, 1717927553);
        }
    }

    private static void cloudPuff(World var0, Location var1, double var2, double var4, double var6, Color var8, int var9) {
        Particle var11 = Particle.DUST;
        double var30 = var1.getX();
        double var32 = var30 + var2;
        double var37 = var1.getY() + 7.0;
        double var39 = var37 + var6;
        double var44 = var1.getZ();
        double var46 = var44 + var4;
        DustOptions var27 = new DustOptions(var8, 3.4F);
        Particles.spawn(var0, var11, var32, var39, var46, 6, 0.735, 0.22, 0.735, 0.0, var27, true, 954482185);
    }

    private void drawLightningColumn(Location var1, int var2) {
        World var23 = var1.getWorld();
        Location var24 = var1.clone().add(0.0, 2.6, 0.0);
        double var25 = 0.0;
        double var27 = 0.0;

        for (int var32 = 0; var32 < 5; var32++) {
            double var33 = (this.random.nextDouble() - 0.5) * 0.8;
            double var35 = (this.random.nextDouble() - 0.5) * 0.8;

            for (int var37 = 0; var37 < 8; var37++) {
                double var38 = (double)var37 / 8.0;
                double var89 = var24.getY();
                double var128 = (double)var32 * 0.8800000000000001;
                double var91 = var89 + var128;
                double var132 = var38 * 0.8800000000000001;
                double var40 = var91 + var132;
                double var96 = var1.getX();
                double var98 = var96 + var25;
                double var138 = var33 - var25;
                double var140 = var138 * var38;
                double var42 = var98 + var140;
                double var103 = var1.getZ();
                double var105 = var103 + var27;
                double var146 = var35 - var27;
                double var148 = var146 * var38;
                double var44 = var105 + var148;
                Particle var112 = Particle.ELECTRIC_SPARK;
                Location var150 = new Location(var23, var42, var40, var44);
                Particles.spawn(var23, var112, var150, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
            }

            var25 = var33;
            var27 = var35;
        }
    }

    private double rnd(double var1, int var3) {
        double var12 = this.random.nextDouble() * 2.0 - 1.0;
        return var12 * var1;
    }
}
