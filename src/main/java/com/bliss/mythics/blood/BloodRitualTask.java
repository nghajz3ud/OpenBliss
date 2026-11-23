package com.bliss.mythics.blood;

import com.bliss.core.GemTuning;
import com.bliss.core.Particles;
import com.bliss.core.TrustStore;
import com.bliss.gems.ConsistentDamage;
import com.bliss.mythics.util.MythicCombat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class BloodRitualTask extends BukkitRunnable {
    private static final double INIT_H = 3.5;
    private static final double INIT_Y = 4.2;
    private static final double BOUNCE_H = 4.5;
    private static final double BOUNCE_Y = 1.75;
    private static final double GLIDE_SPEED = 2.7;
    private static final double CHAIN_HIT_RADIUS = 3.0;
    private static final double RITUAL_DAMAGE = 20.24;
    private static final double RITUAL_DAMAGE_CAP = 9.0;
    private static final double FALL_DMG_PER_BLOCK = 0.05;
    private static final double FALL_DMG_MAX_BONUS = 2.0;
    private static final double RING_RADIUS = 1.5;
    private static final int GRACE_TICKS = 10;
    private static final int MISS_TIMEOUT = 40;
    private static final int MAX_LAUNCHES = 6;
    private static final long IMMUNE_FOREVER = 4611686018427387903L;
    private static final long POST_IMMUNE_MS = 2000L;
    private static final Color DEEP_RED = Color.fromRGB(120, 10, 16);
    private static final Color SAW_BLOOD = Color.fromRGB(140, 13, 26);
    private static final int CMD_HERETIC_SLAM = 30015;
    private static final float HERETIC_SLAM_SCALE = 8.0F;
    private static final Key LAUNCH_SOUND = Key.key("blissmythics", "mythic_launch");
    private static final int PHASE_LAUNCH = 0;
    private static final int PHASE_GLIDE = 1;
    private static final int PHASE_END = 2;
    private final Plugin plugin;
    private final MythicBloodState state;
    private final TrustStore trust;
    private final Player caster;
    private int phase = 0;
    private int launches = 0;
    private int graceTicks = 0;
    private int ticksSinceLaunch = 0;
    private boolean apexPlayed = false;
    private double prevVelY = 0.0;
    private boolean changedAllowFlight = false;
    private Location lastGlideAt;
    private double apexY;
    private final Set<UUID> hitPlayers;
    private static final Map<UUID, BloodRitualTask> ACTIVE;
    private static final int BURST_RINGS = 3;
    private static final double BURST_FIRST = 1.6;
    private static final double BURST_GAP = 1.5;
    private static final int BURST_TICKS = 6;
    private static final int BURST_CRUMBS = 28;
    private static final double BURST_LIFT = 0.5;

    public BloodRitualTask(Plugin var1, MythicBloodState var2, TrustStore var3, Player var4, int var5) {
        HashSet var27 = new HashSet();
        this.hitPlayers = var27;
        this.plugin = var1;
        this.state = var2;
        this.trust = var3;
        this.caster = var4;
    }

    public void begin(int var1) {
        Map var2 = ACTIVE;
        UUID var25 = this.caster.getUniqueId();
        var2.put(var25, this);
        Set var10 = this.state.ritualActive;
        UUID var28 = this.caster.getUniqueId();
        var10.add(var28);
        double var32 = this.caster.getLocation().getY();
        this.apexY = var32;
        if (this.caster.getAllowFlight()) {
            this.caster.setFlying(false);
            this.caster.setAllowFlight(false);
            this.changedAllowFlight = true;
        }

        this.armImmunity(294684228);
        Plugin var38 = this.plugin;
        this.runTaskTimer(var38, 0L, 1L);
    }

    public static void handleQuit(Player var0, int var1) {
        Map var2 = ACTIVE;
        UUID var9 = var0.getUniqueId();
        BloodRitualTask var4 = (BloodRitualTask)var2.get(var9);
        if (var4 != null) {
            var4.end(698961895);
        }
    }

    public static void restoreAll(int var0) {
        Iterator var2 = ACTIVE.values().iterator();

        while (var2.hasNext()) {
            ((BloodRitualTask)var2.next()).end(698961895);
        }
    }

    public void run() {
        if (this.caster.isOnline() && !this.caster.isDead()) {
            double var15 = this.apexY;
            double var21 = this.caster.getLocation().getY();
            double var17 = Math.max(var15, var21);
            this.apexY = var17;
            switch (this.phase) {
                case 0:
                    this.doLaunch(969805156);
                    break;
                case 1:
                    this.doGlide(511983256);
            }
        } else {
            this.end(698961895);
        }
    }

    private void doLaunch(int var1) {
        boolean var50;
        if (this.launches == 0) {
            var50 = true;
        } else {
            var50 = false;
        }

        double var18;
        if (var50) {
            var18 = 3.5;
        } else {
            var18 = 4.5;
        }

        double var21;
        if (var50) {
            var21 = 4.2;
        } else {
            var21 = 1.75;
        }

        this.caster.setGliding(false);
        this.caster.setFallDistance(0.0F);
        this.caster.setGliding(true);
        Player var30 = this.caster;
        Vector var54 = this.lookDir(528012235);
        Vector var55 = var54.multiply(var18);
        Vector var56 = var55.setY(var21);
        var30.setVelocity(var56);
        Player var32 = this.caster;
        Key var57 = LAUNCH_SOUND;
        Source var76 = Source.PLAYER;
        Sound var58 = Sound.sound(var57, var76, 1.0F, 1.2F);
        var32.playSound(var58);
        World var35 = this.caster.getWorld();
        Location var61 = this.caster.getLocation();
        org.bukkit.Sound var77 = org.bukkit.Sound.ENTITY_WARDEN_SONIC_BOOM;
        var35.playSound(var61, var77, 1.0F, 1.0F);
        Player var37 = this.caster;
        PotionEffectType var82 = PotionEffectType.SPEED;
        PotionEffect var62 = new PotionEffect(var82, 40, 1, false, false);
        var37.addPotionEffect(var62);
        if (var50) {
            Plugin var41 = this.plugin;
            TrustStore var64 = this.trust;
            Player var79 = this.caster;
            groundBurst(var41, var64, var79, 1849399630);
        }

        int var66 = this.launches + 1;
        this.launches = var66;
        this.graceTicks = 10;
        this.ticksSinceLaunch = 0;
        this.lastGlideAt = null;
        this.apexPlayed = false;
        this.prevVelY = var21;
        this.armImmunity(294684228);
        this.phase = 1;
    }

    private void doGlide(int var1) {
        Location var14 = this.caster.getLocation();
        World var33 = var14.getWorld();
        Particle var3 = Particle.EXPLOSION;
        Particles.spawn(var33, var3, var14, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
        Location var136 = var14.clone().add(0.0, 1.0, 0.0);
        this.drawRing(var136, 1844487757);
        this.caster.setGliding(true);
        this.caster.setFallDistance(0.0F);
        double var15 = this.caster.getVelocity().getY();
        if (!this.apexPlayed && this.prevVelY > 0.0 && var15 <= 0.0) {
            World var54 = var14.getWorld();
            org.bukkit.Sound var191 = org.bukkit.Sound.BLOCK_AMETHYST_BLOCK_CHIME;
            var54.playSound(var14, var191, 1.0F, 0.6F);
            World var56 = var14.getWorld();
            org.bukkit.Sound var192 = org.bukkit.Sound.ENTITY_WARDEN_SONIC_CHARGE;
            var56.playSound(var14, var192, 0.5F, 1.6F);
            this.apexPlayed = true;
        }

        this.prevVelY = var15;
        if (this.graceTicks > 0) {
            int var183 = this.graceTicks - 1;
            this.graceTicks = var183;
            if (this.graceTicks == 0 && this.launches >= 6) {
                this.end(698961895);
            }
        } else {
            Player var62 = this.caster;
            Vector var146 = this.lookDir(528012235).multiply(2.7);
            var62.setVelocity(var146);
            int var148 = this.ticksSinceLaunch + 1;
            this.ticksSinceLaunch = var148;
            if (var148 >= 40) {
                this.end(698961895);
            } else {
                boolean var17;
                Location var73;
                label94: {
                    var17 = false;
                    if (this.lastGlideAt != null) {
                        World var70 = this.lastGlideAt.getWorld();
                        World var151 = var14.getWorld();
                        if (var70.equals(var151)) {
                            var73 = this.lastGlideAt;
                            break label94;
                        }
                    }

                    var73 = var14;
                }

                Location var18 = var73;
                Vector var75 = var14.toVector();
                Vector var153 = var73.toVector();
                Vector var19 = var75.subtract(var153);
                int var160 = (int)Math.ceil(var19.length() / 1.5);
                int var22 = Math.max(1, var160);
                HashSet var82 = new HashSet();
                HashSet var23 = var82;

                for (int var24 = 1; var24 <= var22; var24++) {
                    Location var86 = var18.clone();
                    Vector var163 = var19.clone();
                    double var197 = (double)var24;
                    double var215 = (double)var22;
                    double var199 = var197 / var215;
                    Vector var164 = var163.multiply(var199);
                    Location var25 = var86.add(var164);
                    World var89 = var25.getWorld();

                    for (Entity var27 : var89.getNearbyEntities(var25, 3.0, 3.0, 3.0)) {
                        if (var27 instanceof LivingEntity) {
                            LivingEntity var28 = (LivingEntity)var27;
                            if (!var28.isDead()) {
                                Player var167 = this.caster;
                                if (!var28.equals(var167) && (var28 instanceof Player || var28 instanceof Mob)) {
                                    UUID var169 = var28.getUniqueId();
                                    if (var23.add(var169)) {
                                        if (var28 instanceof Player) {
                                            Player var29 = (Player)var28;
                                            TrustStore var116 = this.trust;
                                            UUID var172 = this.caster.getUniqueId();
                                            UUID var204 = var29.getUniqueId();
                                            if (var116.isTrusted(var172, var204, 1736364606)) {
                                                continue;
                                            }

                                            GameMode var119 = var29.getGameMode();
                                            GameMode var173 = GameMode.SPECTATOR;
                                            if (var119 == var173) {
                                                continue;
                                            }

                                            GameMode var121 = var29.getGameMode();
                                            GameMode var174 = GameMode.CREATIVE;
                                            if (var121 == var174) {
                                                continue;
                                            }
                                        }

                                        this.hitVictim(var28, 1249531433);
                                        var17 = true;
                                    }
                                }
                            }
                        }
                    }
                }

                Location var177 = var14.clone();
                this.lastGlideAt = var177;
                if (var17) {
                    Location var180 = this.caster.getLocation();
                    this.flourish(var180, 575831195);
                    this.phase = 0;
                }
            }
        }
    }

    private void hitVictim(LivingEntity var1, int var2) {
        if (var1 instanceof Player var20) {
            Set var32 = this.hitPlayers;
            UUID var63 = var20.getUniqueId();
            var32.add(var63);
        }

        double var74 = this.apexY;
        double var98 = this.caster.getLocation().getY();
        double var76 = var74 - var98;
        double var125 = Math.max(0.0, var76);
        String var38 = "heretic";
        String var64 = "blood-ritual";
        double var21 = GemTuning.mythicDamage(var38, var64, 20.24, 478197159);
        String var41 = "heretic";
        String var65 = "blood-ritual-cap";
        double var23 = GemTuning.mythicDamage(var41, var65, 9.0, 478197159);
        double var111 = var125 * 0.05;
        double var102 = Math.min(2.0, var111);
        double var84 = 1.0 + var102;
        double var25 = var21 * var84;
        Player var49 = this.caster;
        ConsistentDamage.dealFullArmorAwareCapped(var49, var1, var25, var23, 1350238938);
        if (var23 > 0.0) {
            MythicCombat.damageArmor(var1, var23, 1814310217);
        }

        Location var27 = var1.getLocation().add(0.0, 1.0, 0.0);
        World var58 = var27.getWorld();
        org.bukkit.Sound var90 = org.bukkit.Sound.ITEM_MACE_SMASH_AIR;
        var58.playSound(var27, var90, 1.0F, 1.0F);
        World var60 = var27.getWorld();
        Particle var72 = Particle.BLOCK;
        BlockData var123 = Material.REDSTONE_BLOCK.createBlockData();
        Particles.spawn(var60, var72, var27, 20, 0.4, 0.4, 0.4, 0.0, var123, 1141219473);
        World var62 = var27.getWorld();
        Particle var73 = Particle.DUST;
        Color var18 = DEEP_RED;
        DustOptions var124 = new DustOptions(var18, 1.4F);
        Particles.spawn(var62, var73, var27, 15, 0.4, 0.4, 0.4, 0.0, var124, 1141219473);
    }

    public static boolean inWeb(Player var0, int var1) {
        Material var6 = var0.getLocation().getBlock().getType();
        Material var3 = Material.COBWEB;
        if (var6 != var3) {
            Material var10 = var0.getEyeLocation().getBlock().getType();
            Material var12 = Material.COBWEB;
            if (var10 != var12) {
                return false;
            }
        }

        return true;
    }

    public static void groundBurst(Plugin var0, TrustStore var1, Player var2, int var3) {
        final Location var27 = var2.getLocation().clone();
        if (var27.getWorld() != null) {
            final ThreadLocalRandom var28 = ThreadLocalRandom.current();
            Color var5 = SAW_BLOOD;
            DustOptions var56 = new DustOptions(var5, 1.1F);
            final DustOptions var29 = var56;
            Color var145 = SAW_BLOOD;
            final DustOptions var57 = new DustOptions(var145, 1.25F);
            knockAway(var1, var2, var27, 1059074000);
            World var60 = var27.getWorld();
            org.bukkit.Sound var147 = org.bukkit.Sound.ENTITY_GENERIC_EXPLODE;
            var60.playSound(var27, var147, 1.3F, 0.65F);
            World var62 = var27.getWorld();
            Particle var131 = Particle.EXPLOSION_EMITTER;
            Location var150 = var27.clone().add(0.0, 0.3, 0.0);
            Particles.spawn(var62, var131, var150, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
            spawnBloodBurst(var0, var27, 443422284);

            for (int var31 = 0; var31 < 90; var31++) {
                double var283 = var28.nextDouble(Math.PI * 2);
                double var35 = var28.nextDouble() * 2.0;
                double var39 = var28.nextDouble() * 1.0;
                double var109 = var27.getX();
                double var183 = Math.cos(var283);
                double var185 = var183 * var35;
                double var43 = var109 + var185;
                double var114 = var27.getY();
                double var47 = var114 + var39;
                double var119 = var27.getZ();
                double var191 = Math.sin(var283);
                double var193 = var191 * var35;
                double var49 = var119 + var193;
                World var124 = var27.getWorld();
                Particle var142 = Particle.DUST;
                Particles.spawn(var124, var142, var43, var47, var49, 1, 0.0, 0.0, 0.0, 0.0, var29, 2077675459);
                if (var31 % 4 == 0) {
                    World var128 = var27.getWorld();
                    Particle var144 = Particle.SOUL_FIRE_FLAME;
                    Particles.spawn(var128, var144, var43, var47, var49, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                }
            }

            BukkitRunnable var66 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    if (this.t > 5) {
                        this.cancel();
                    } else {
                        double var91 = (double)this.t * 0.7;
                        double var20 = 1.0 + var91;
                        int var81 = (int)(var20 * 11.0);
                        int var22 = Math.max(10, var81);

                        for (int var23 = 0; var23 < var22; var23++) {
                            double var95 = (double)var23;
                            double var44 = (Math.PI * 2) * var95;
                            double var98 = (double)var22;
                            double var24 = var44 / var98;
                            double var50 = var27.getX();
                            double var102 = Math.cos(var24);
                            double var104 = var102 * var20;
                            double var26 = var50 + var104;
                            double var56 = var27.getZ();
                            double var108 = Math.sin(var24);
                            double var110 = var108 * var20;
                            double var28x = var56 + var110;
                            double var64 = var27.getY() + 0.2;
                            double var118 = var28.nextDouble() * 0.3;
                            double var30 = var64 + var118;
                            World var70 = var27.getWorld();
                            Particle var85 = Particle.DUST;
                            DustOptions var145 = var29;
                            Particles.spawn(var70, var85, var26, var30, var28x, 1, 0.0, 0.0, 0.0, 0.0, var145, 2077675459);
                            if (var23 % 10 == 0) {
                                World var75 = var27.getWorld();
                                Particle var87 = Particle.SOUL_FIRE_FLAME;
                                Particles.spawn(var75, var87, var26, var30, var28x, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                            }
                        }

                        int var84 = this.t + 1;
                        this.t = var84;
                    }
                }
            };
            var66.runTaskTimer(var0, 0L, 1L);
            ArrayList var68 = new ArrayList();
            final ArrayList var282 = var68;

            for (int var32 = 0; var32 < 64; var32++) {
                double var158 = (double)var32;
                double var77 = (Math.PI * 2) * var158 / 64.0;
                double var165 = var28.nextDouble() * 0.1;
                double var33 = var77 + var165;
                double var170 = var28.nextDouble() * 0.34;
                double var37 = 0.42 + var170;
                double var175 = var28.nextDouble() * 0.34;
                double var41 = 0.55 + var175;
                double var93;
                if (var32 % 3 == 0) {
                    var93 = 1.0;
                } else {
                    var93 = 0.0;
                }

                double[] var139 = new double[7];
                double var228 = var27.getX();
                var139[0] = var228;
                double var233 = var27.getY() + 0.3;
                var139[1] = var233;
                double var236 = var27.getZ();
                var139[2] = var236;
                double var240 = Math.cos(var33);
                double var242 = var240 * var37;
                var139[3] = var242;
                var139[4] = var41;
                double var248 = Math.sin(var33);
                double var250 = var248 * var37;
                var139[5] = var250;
                var139[6] = var93;
                var282.add(var139);
            }

            BukkitRunnable var71 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    int var94 = this.t;
                    int var108 = var94 + 1;
                    this.t = var108;
                    if (var94 <= 36 && !var282.isEmpty()) {
                        Iterator var16 = var282.iterator();

                        while (var16.hasNext()) {
                            double[] var17 = (double[])var16.next();
                            double var18 = var17[0];
                            double var20 = var17[1];
                            double var22 = var17[2];
                            double var109 = var17[0];
                            double var163 = var17[3];
                            double var111 = var109 + var163;
                            var17[0] = var111;
                            double var113 = var17[1];
                            double var166 = var17[4];
                            double var115 = var113 + var166;
                            var17[1] = var115;
                            double var117 = var17[2];
                            double var169 = var17[5];
                            double var119 = var117 + var169;
                            var17[2] = var119;
                            double var123 = var17[4] - 0.05;
                            var17[4] = var123;

                            for (int var25 = 1; var25 <= 4; var25++) {
                                double var26 = (double)var25 / 4.0;
                                double var129 = var17[0];
                                double var131 = var129 - var18;
                                double var133 = var131 * var26;
                                double var28 = var18 + var133;
                                double var136 = var17[1];
                                double var138 = var136 - var20;
                                double var140 = var138 * var26;
                                double var30 = var20 + var140;
                                double var143 = var17[2];
                                double var145 = var143 - var22;
                                double var147 = var145 * var26;
                                double var32 = var22 + var147;
                                World var80 = var27.getWorld();
                                Particle var104 = Particle.DUST;
                                DustOptions var202 = var57;
                                Particles.spawn(var80, var104, var28, var30, var32, 2, 0.04, 0.04, 0.04, 0.0, var202, 2077675459);
                                if (var17[6] > 0.5) {
                                    World var87 = var27.getWorld();
                                    Particle var106 = Particle.SOUL_FIRE_FLAME;
                                    Particles.spawn(var87, var106, var28, var30, var32, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
                                }
                            }

                            double var89 = var17[1];
                            double var157 = var27.getY();
                            if (var89 <= var157) {
                                var16.remove();
                            }
                        }
                    } else {
                        this.cancel();
                    }
                }
            };
            var71.runTaskTimer(var0, 0L, 1L);
        }
    }

    private static void knockAway(TrustStore var0, Player var1, Location var2, int var3) {
        World var23 = var2.getWorld();

        for (Entity var18 : var23.getNearbyEntities(var2, 6.5, 3.5, 6.5)) {
            if (var18 instanceof LivingEntity) {
                LivingEntity var19 = (LivingEntity)var18;
                if (!var19.equals(var1) && !var19.isDead()) {
                    if (var19 instanceof Player) {
                        Player var20 = (Player)var19;
                        UUID var69 = var1.getUniqueId();
                        UUID var79 = var20.getUniqueId();
                        if (var0.isTrusted(var69, var79, 1736364606)) {
                            continue;
                        }

                        GameMode var46 = var20.getGameMode();
                        GameMode var70 = GameMode.SPECTATOR;
                        if (var46 == var70) {
                            continue;
                        }

                        GameMode var48 = var20.getGameMode();
                        GameMode var71 = GameMode.CREATIVE;
                        if (var48 == var71) {
                            continue;
                        }
                    }

                    Vector var51 = var19.getLocation().toVector();
                    Vector var73 = var2.toVector();
                    Vector var118 = var51.subtract(var73);
                    var118.setY(0);
                    if (var118.lengthSquared() < 1.0E-4) {
                        Vector var59 = new Vector(0, 0, 1);
                        var118 = var59;
                    }

                    var118.normalize();
                    Location var90 = var19.getLocation();
                    double var93 = var90.distance(var2) / 6.5;
                    double var85 = 1.0 - var93;
                    double var21 = Math.max(0.4, var85);
                    Vector var76 = var19.getVelocity();
                    double var98 = var118.getX() * 1.4;
                    double var100 = var98 * var21;
                    double var112 = var118.getZ() * 1.4;
                    double var114 = var112 * var21;
                    Vector var87 = new Vector(var100, 0.6, var114);
                    Vector var77 = var76.add(var87);
                    var19.setVelocity(var77);
                }
            }
        }
    }

    private static void spawnBloodBurst(Plugin var0, Location var1, int var2) {
        World var21 = var1.getWorld();
        if (var21 != null) {
            final Location var22 = var1.clone().add(0.0, 0.5, 0.0);
            Color var6 = Color.RED;
            final DustOptions var33 = new DustOptions(var6, 1.5F);
            BlockData var24 = Material.REDSTONE_BLOCK.createBlockData();

            for (int var25 = 0; var25 < 28; var25++) {
                double var51 = (double)var25;
                double var26 = (Math.PI * 2) * var51 / 28.0;
                Particle var48 = Particle.BLOCK;
                double var62 = Math.cos(var26);
                double var66 = Math.sin(var26);
                Particles.spawn(var21, var48, var22, 0, var62, 0.35, var66, 0.6, var24, true, 1748031581);
            }

            BukkitRunnable var45 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    if (this.t <= 6 && var22.getWorld() != null) {
                        double var23 = (double)this.t / 6.0;

                        for (int var25 = 0; var25 < 3; var25++) {
                            double var80 = (double)var25 * 1.5;
                            double var47 = 1.6 + var80;
                            double var26 = var47 * var23;
                            if (!(var26 <= 0.05)) {
                                double var88 = var26 * 12.0;
                                int var28 = (int)Math.max(20.0, var88);

                                for (int var29 = 0; var29 < var28; var29++) {
                                    double var91 = (double)var29;
                                    double var63 = (Math.PI * 2) * var91;
                                    double var94 = (double)var28;
                                    double var30 = var63 / var94;
                                    World var69 = var22.getWorld();
                                    Particle var75 = Particle.DUST;
                                    double var98 = var22.getX();
                                    double var106 = Math.cos(var30);
                                    double var108 = var106 * var26;
                                    double var100 = var98 + var108;
                                    double var112 = var22.getY();
                                    double var116 = var22.getZ();
                                    double var120 = Math.sin(var30);
                                    double var122 = var120 * var26;
                                    double var118 = var116 + var122;
                                    DustOptions var125 = var33;
                                    Particles.spawn(var69, var75, var100, var112, var118, 1, 0.0, 0.0, 0.0, 0.0, var125, true, 954482185);
                                }
                            }
                        }

                        int var73 = this.t + 1;
                        this.t = var73;
                    } else {
                        this.cancel();
                    }
                }
            };
            var45.runTaskTimer(var0, 0L, 1L);
        }
    }

    private static void spawnHereticSlam(Plugin var0, Location var1) {
        if (var1.getWorld() != null) {
            Material var3 = Material.PAPER;
            ItemStack var18 = new ItemStack(var3);
            ItemMeta var13 = var18.getItemMeta();
            if (var13 != null) {
                Integer var44 = 30015;
                var13.setCustomModelData(var44);
                var18.setItemMeta(var13);
            }

            World var51 = var1.getWorld();
            double var62 = (double)var1.getBlockX() + 0.5;
            double var76 = (double)var1.getBlockY() + 0.4;
            double var82 = (double)var1.getBlockZ() + 0.5;
            Location var25 = new Location(var51, var62, var76, var82);
            World var28 = var1.getWorld();
            float var1x = 1.6F;
            Consumer<ItemDisplay> var65 = var2 -> {
                var2.setPersistent(false);
                var2.setItemStack(var18);
                ItemDisplayTransform var16x = ItemDisplayTransform.FIXED;
                var2.setItemDisplayTransform(var16x);
                float var23 = 0.5F * var1x;
                Vector3f var5 = new Vector3f(0.0F, var23, 0.0F);
                Quaternionf var20 = new Quaternionf();
                Vector3f var22 = new Vector3f(var1x, var1x, var1x);
                Quaternionf var24 = new Quaternionf();
                Transformation var17 = new Transformation(var5, var20, var22, var24);
                var2.setTransformation(var17);
                Brightness var18x = new Brightness(15, 15);
                var2.setBrightness(var18x);
            };
            ItemDisplay var16 = (ItemDisplay)var28.spawn(var25, ItemDisplay.class, var65);
            BukkitScheduler var33 = var0.getServer().getScheduler();
            Runnable var54 = () -> {
                if (var16 != null && !var16.isDead()) {
                    var16.setInterpolationDelay(0);
                    var16.setInterpolationDuration(4);
                    Vector3f var3x = new Vector3f(0.0F, 4.0F, 0.0F);
                    Quaternionf var16x = new Quaternionf();
                    Vector3f var17 = new Vector3f(8.0F, 8.0F, 8.0F);
                    Quaternionf var18x = new Quaternionf();
                    Transformation var15 = new Transformation(var3x, var16x, var17, var18x);
                    var16.setTransformation(var15);
                }
            };
            var33.runTaskLater(var0, var54, 1L);
            BukkitScheduler var37 = var0.getServer().getScheduler();
            Runnable var56 = () -> {
                if (var16 != null && !var16.isDead()) {
                    var16.setInterpolationDelay(0);
                    var16.setInterpolationDuration(10);
                    Vector3f var3x = new Vector3f(0.0F, 0.0F, 0.0F);
                    Quaternionf var16x = new Quaternionf();
                    Vector3f var17 = new Vector3f(0.01F, 0.01F, 0.01F);
                    Quaternionf var18x = new Quaternionf();
                    Transformation var15 = new Transformation(var3x, var16x, var17, var18x);
                    var16.setTransformation(var15);
                }
            };
            var37.runTaskLater(var0, var56, 30L);
            BukkitScheduler var41 = var0.getServer().getScheduler();
            Runnable var58 = () -> {
                if (var16 != null && !var16.isDead()) {
                    var16.remove();
                }
            };
            var41.runTaskLater(var0, var58, 40L);
        }
    }

    private void drawRing(Location var1, int var2) {
        for (int var22 = 0; var22 < 15; var22++) {
            double var35 = (double)var22;
            double var23 = (Math.PI * 2) * var35 / 15.0;
            World var33 = var1.getWorld();
            Particle var34 = Particle.SOUL_FIRE_FLAME;
            double var40 = var1.getX();
            double var46 = Math.cos(var23) * 1.5;
            double var42 = var40 + var46;
            double var49 = var1.getY();
            double var52 = var1.getZ();
            double var58 = Math.sin(var23) * 1.5;
            double var54 = var52 + var58;
            Particles.spawn(var33, var34, var42, var49, var54, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
        }
    }

    private void flourish(Location var1, int var2) {
        for (int var22 = 0; var22 < 40; var22++) {
            double var35 = (double)var22;
            double var23 = (Math.PI * 2) * var35 / 40.0;
            World var33 = var1.getWorld();
            Particle var34 = Particle.SOUL_FIRE_FLAME;
            double var40 = var1.getX();
            double var46 = Math.cos(var23) * 5.0;
            double var42 = var40 + var46;
            double var51 = var1.getY() + 1.0;
            double var56 = var1.getZ();
            double var62 = Math.sin(var23) * 5.0;
            double var58 = var56 + var62;
            Particles.spawn(var33, var34, var42, var51, var58, 1, 0.0, 0.0, 0.0, 0.0, 1717927553);
        }
    }

    private void end(int var1) {
        Map var2 = ACTIVE;
        UUID var30 = this.caster.getUniqueId();
        var2.remove(var30, this);
        Set var11 = this.state.ritualActive;
        UUID var33 = this.caster.getUniqueId();
        var11.remove(var33);
        if (this.caster.isOnline()) {
            this.caster.setGliding(false);
            this.caster.setFallDistance(0.0F);
        }

        Map var22 = this.state.ritualImmuneUntil;
        UUID var38 = this.caster.getUniqueId();
        Long var44 = System.currentTimeMillis() + 2000L;
        var22.put(var38, var44);
        this.restoreFlight(117640180);
        this.forgeBloodlink(1227794384);
        this.phase = 2;

        try {
            this.cancel();
        } catch (IllegalStateException var45) {
        }
    }

    private void forgeBloodlink(int var1) {
        if (this.hitPlayers.size() >= 2) {
            if (this.plugin.isEnabled()) {
                long var10 = System.currentTimeMillis();
                Set<UUID> var12 = MythicBloodState.newMemberSet(831654120);
                Set var57 = this.hitPlayers;
                var12.addAll(var57);
                long var66 = var10 + 12000L;
                MythicBloodState.LinkGroup var27 = new MythicBloodState.LinkGroup(var12, var66, 1539956859);
                MythicBloodState.LinkGroup var13 = var27;

                for (UUID var15 : var12) {
                    Map var37 = this.state.linkOf;
                    var37.put(var15, var13);
                }

                Plugin var64 = this.plugin;
                MythicBloodState var69 = this.state;
                BloodlinkBeamTask var39 = new BloodlinkBeamTask(var64, var69, var13, 1184545919);
                var39.start(1547768783);

                for (UUID var73 : var12) {
                    Server var51 = this.plugin.getServer();
                    Player var16 = var51.getPlayer(var73);
                    if (var16 != null) {
                        World var55 = var16.getWorld();
                        Location var61 = var16.getLocation();
                        org.bukkit.Sound var65 = org.bukkit.Sound.ENTITY_WARDEN_HEARTBEAT;
                        var55.playSound(var61, var65, 1.0F, 0.7F);
                    }
                }

                this.hitPlayers.clear();
            }
        }
    }

    private void restoreFlight(int var1) {
        if (this.caster.isOnline()) {
            if (this.changedAllowFlight) {
                this.caster.setAllowFlight(true);
                this.changedAllowFlight = false;
            }
        }
    }

    private void armImmunity(int var1) {
        Map var9 = this.state.ritualImmuneUntil;
        UUID var12 = this.caster.getUniqueId();
        Long var15 = System.currentTimeMillis() + 4611686018427387903L;
        var9.put(var12, var15);
    }

    private Vector lookDir(int var1) {
        return this.caster.getLocation().getDirection().normalize();
    }

    static {
        ConcurrentHashMap var8 = new ConcurrentHashMap();
        ACTIVE = var8;
    }
}
