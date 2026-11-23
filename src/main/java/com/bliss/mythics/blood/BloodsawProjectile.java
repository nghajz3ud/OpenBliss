package com.bliss.mythics.blood;

import com.bliss.core.GemTuning;
import com.bliss.core.Particles;
import com.bliss.core.TrustStore;
import com.bliss.gems.ConsistentDamage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

public final class BloodsawProjectile extends BukkitRunnable {
    private static final double SPEED = 1.0;
    private static final double HIT_RADIUS = 1.2;
    private static final float SCALE = 1.25F;
    private static final float SPIN_DEG = 40.0F;
    private static final double DAMAGE = 28.0;
    private static final double COBWEB_RADIUS = 1.2;
    private static final int PUSH_TICKS = 7;
    private static final double PUSH_SPEED = 0.7;
    private static final int MAX_TICKS = 80;
    private static final int MAX_BOUNCES = 3;
    private static final int DISPLAY_CMD = 30003;
    private static final Color BLOOD = Color.fromRGB(140, 13, 26);
    private static final double SAW_HALF_WIDTH = 0.7;
    private static final double SAW_DUST_STEP = 0.18;
    private static final float SAW_DUST_SCALE = 0.9F;
    private final Plugin plugin;
    private final TrustStore trust;
    private final MythicBloodState state;
    private final Player caster;
    private final ItemDisplay display;
    private final Set<UUID> alreadyHit;
    private Vector dir;
    private Location loc;
    private float spin;
    private int ticks;
    private int bounces;
    private static final long SAW_DR_WINDOW_MS = 2000L;
    private static final double SAW_DR_MULT = 0.7;
    private static final double RICOCHET_MULT = 0.5;
    private static final Map<String, Long> RECENT_SAW_HIT;

    public BloodsawProjectile(Plugin var1, TrustStore var2, MythicBloodState var3, Player var4, double var5, double var7, int var9) {
        HashSet var11 = new HashSet();
        this.alreadyHit = var11;
        this.spin = 0.0F;
        this.ticks = 0;
        this.bounces = 0;
        this.plugin = var1;
        this.trust = var2;
        this.state = var3;
        this.caster = var4;
        Vector var16 = var4.getLocation().getDirection().normalize();
        double var76 = Math.toRadians(var5);
        Vector var55 = rotateYaw(var16, var76, 20610653).normalize();
        this.dir = var55;
        double var58 = Math.toRadians(90.0);
        Vector var35 = rotateYaw(var16, var58, 20610653).normalize();
        Vector var17 = var35.multiply(var7);
        Location var63 = var4.getEyeLocation();
        Vector var80 = var16.clone().multiply(0.6);
        Location var64 = var63.add(var80);
        Location var65 = var64.add(var17);
        this.loc = var65;
        Material var82 = Material.NETHERITE_SWORD;
        ItemStack var38 = new ItemStack(var82);
        ItemMeta var19 = var38.getItemMeta();
        if (var19 != null) {
            Integer var67 = 30003;
            var19.setCustomModelData(var67);
            var38.setItemMeta(var19);
        }

        World var71 = this.loc.getWorld();
        Location var84 = this.loc;
        Consumer<ItemDisplay> var86 = var2x -> {
            var2x.setPersistent(false);
            var2x.setItemStack(var38);
            ItemDisplayTransform var11x = ItemDisplayTransform.GROUND;
            var2x.setItemDisplayTransform(var11x);
            Transformation var13 = this.transform(0.0F, 481745735);
            var2x.setTransformation(var13);
            var2x.setTeleportDuration(1);
        };
        ItemDisplay var73 = (ItemDisplay)var71.spawn(var84, ItemDisplay.class, var86);
        this.display = var73;
    }

    public void launch(int var1) {
        Plugin var9 = this.plugin;
        this.runTaskTimer(var9, 0L, 1L);
    }

    public void run() {
        int var108 = this.ticks + 1;
        this.ticks = var108;
        if (var108 <= 80 && this.caster.isOnline()) {
            this.breakCobwebs(420572859);
            Location var36 = this.loc.clone();
            Vector var113 = this.dir.clone().multiply(1.0);
            Location var19 = var36.add(var113);
            if (var19.getBlock().getType().isSolid()) {
                int var115 = this.bounces + 1;
                this.bounces = var115;
                if (var115 > 3) {
                    this.despawn(1416531756);
                    return;
                }

                this.ricochet(2043960232);
                this.alreadyHit.clear();
                World var49 = this.loc.getWorld();
                Location var118 = this.loc;
                Sound var139 = Sound.BLOCK_ANVIL_LAND;
                var49.playSound(var118, var139, 0.4F, 1.8F);
                World var52 = this.loc.getWorld();
                Location var120 = this.loc;
                Sound var140 = Sound.BLOCK_NETHERITE_BLOCK_PLACE;
                var52.playSound(var120, var140, 0.45F, 0.5F);
                Location var55 = this.loc.clone();
                Vector var124 = this.dir.clone().multiply(1.0);
                var19 = var55.add(var124);
                if (var19.getBlock().getType().isSolid()) {
                    this.despawn(1416531756);
                    return;
                }
            }

            this.loc = var19;
            float var129 = (this.spin + 40.0F) % 360.0F;
            this.spin = var129;
            ItemDisplay var65 = this.display;
            Location var131 = this.loc;
            var65.teleport(var131);
            ItemDisplay var68 = this.display;
            float var151 = (float)Math.toRadians((double)this.spin);
            Transformation var133 = this.transform(var151, 481745735);
            var68.setTransformation(var133);
            double var20 = Math.toRadians((double)this.spin);
            double var22 = Math.cos(var20);
            double var24 = Math.sin(var20);
            BlockData var26 = Material.REDSTONE_BLOCK.createBlockData();

            for (double var27 = -0.7; var27 <= 0.7000000009999999; var27 += 0.18) {
                World var92 = this.loc.getWorld();
                Particle var134 = Particle.BLOCK;
                double var156 = this.loc.getX();
                double var167 = var22 * var27;
                double var158 = var156 + var167;
                double var171 = this.loc.getY();
                double var175 = this.loc.getZ();
                double var179 = var24 * var27;
                double var177 = var175 + var179;
                Particles.spawn(var92, var134, var158, var171, var177, 1, 0.0, 0.0, 0.0, 0.0, var26, 2077675459);
            }

            for (LivingEntity var29 : this.findTargets(1348777374)) {
                this.hit(var29, 729342118);
            }
        } else {
            this.despawn(1416531756);
        }
    }

    private void ricochet(int var1) {
        Location var11 = this.loc.clone();
        double var45 = this.dir.getX() * 1.0;
        if (var11.add(var45, 0.0, 0.0).getBlock().getType().isSolid()) {
            Vector var17 = this.dir;
            double var51 = -this.dir.getX();
            var17.setX(var51);
        }

        Location var21 = this.loc.clone();
        double var75 = this.dir.getY() * 1.0;
        if (var21.add(0.0, var75, 0.0).getBlock().getType().isSolid()) {
            Vector var27 = this.dir;
            double var59 = -this.dir.getY();
            var27.setY(var59);
        }

        Location var31 = this.loc.clone();
        double var87 = this.dir.getZ() * 1.0;
        if (var31.add(0.0, 0.0, var87).getBlock().getType().isSolid()) {
            Vector var37 = this.dir;
            double var67 = -this.dir.getZ();
            var37.setZ(var67);
        }

        this.dir.normalize();
    }

    private List<LivingEntity> findTargets(int var1) {
        ArrayList var2 = new ArrayList();
        ArrayList var9 = var2;
        World var16 = this.loc.getWorld();
        Location var58 = this.loc;

        for (Entity var11 : var16.getNearbyEntities(var58, 1.2, 1.2, 1.2)) {
            if (var11 instanceof LivingEntity) {
                LivingEntity var12 = (LivingEntity)var11;
                if (!var12.isDead()) {
                    Player var60 = this.caster;
                    if (!var12.equals(var60)) {
                        if (var12 instanceof Player) {
                            Player var13 = (Player)var12;
                            GameMode var38 = var13.getGameMode();
                            GameMode var61 = GameMode.SPECTATOR;
                            if (var38 == var61) {
                                continue;
                            }

                            GameMode var40 = var13.getGameMode();
                            GameMode var62 = GameMode.CREATIVE;
                            if (var40 == var62) {
                                continue;
                            }
                        }

                        if (var12 instanceof Player || var12 instanceof Mob) {
                            Set var46 = this.alreadyHit;
                            UUID var64 = var12.getUniqueId();
                            if (!var46.contains(var64)) {
                                if (var12 instanceof Player) {
                                    Player var71 = (Player)var12;
                                    TrustStore var53 = this.trust;
                                    UUID var67 = this.caster.getUniqueId();
                                    UUID var70 = var71.getUniqueId();
                                    if (var53.isTrusted(var67, var70, 1736364606)) {
                                        continue;
                                    }
                                }

                                var9.add(var12);
                            }
                        }
                    }
                }
            }
        }

        return var9;
    }

    private void hit(LivingEntity var1, int var2) {
        String var22;
        long var23;
        double var55;
        label24: {
            Set var32 = this.alreadyHit;
            UUID var87 = var1.getUniqueId();
            var32.add(var87);
            String var37 = String.valueOf(this.caster.getUniqueId());
            String var90 = String.valueOf(var1.getUniqueId());
            var22 = var37 + "|" + var90;
            var23 = System.currentTimeMillis();
            Map var41 = RECENT_SAW_HIT;
            Long var25 = (Long)var41.get(var22);
            String var44 = "heretic";
            String var92 = "bloodsaw";
            double var26 = GemTuning.mythicDamage(var44, var92, 28.0, 478197159);
            if (var25 != null) {
                long var106 = var25;
                if (var23 - var106 <= 2000L) {
                    var55 = var26 * 0.7;
                    break label24;
                }
            }

            var55 = var26;
        }

        double var28 = var55;
        if (this.bounces > 0 && MythicBloodState.ricochetHalfDamage(858730909)) {
            var28 = var55 * 0.5;
        }

        Map var64 = RECENT_SAW_HIT;
        Long var116 = var23;
        var64.put(var22, var116);
        if (RECENT_SAW_HIT.size() > 256) {
            long var30 = var23 - 2000L;
            Collection<Long> var73 = RECENT_SAW_HIT.values();
            Predicate<Long> var97 = var2x -> {
                long var6 = var2x;
                boolean var9;
                if (var6 < var30) {
                    var9 = true;
                } else {
                    var9 = false;
                }

                return var9;
            };
            var73.removeIf(var97);
        }

        Player var76 = this.caster;
        ConsistentDamage.dealFullArmorAware(var76, var1, var28, 1323155734);
        Vector var123 = this.dir.clone();
        this.pushAlong(var1, var123, 1588019944);
        Location var141 = var1.getLocation().add(0.0, 1.0, 0.0);
        World var82 = var141.getWorld();
        Particle var102 = Particle.DUST;
        Color var20 = BLOOD;
        DustOptions var19 = new DustOptions(var20, 1.0F);
        Particles.spawn(var82, var102, var141, 15, 0.3, 0.3, 0.3, 0.0, var19, 1141219473);
        World var84 = var141.getWorld();
        Particle var103 = Particle.BLOCK;
        BlockData var140 = Material.REDSTONE_BLOCK.createBlockData();
        Particles.spawn(var84, var103, var141, 8, 0.25, 0.25, 0.25, 0.0, var140, 1141219473);
        World var86 = var141.getWorld();
        Sound var126 = Sound.ENTITY_PLAYER_HURT;
        var86.playSound(var141, var126, 1.0F, 0.7F);
    }

    private void pushAlong(final LivingEntity var1, Vector var2, int var3) {
        var2.setY(0);
        if (!(var2.lengthSquared() < 1.0E-6)) {
            final Vector var10 = var2.normalize();
            BukkitRunnable var18 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    int var34 = this.t;
                    int var37 = var34 + 1;
                    this.t = var37;
                    if (var34 < 7 && !var1.isDead() && var1.isValid()) {
                        double var58 = (double)this.t;
                        double var53 = 0.4 * var58 / 7.0;
                        double var40 = 1.0 - var53;
                        double var10x = 0.7 * var40;
                        double var12 = Math.max(var1.getVelocity().getY(), 0.06);
                        LivingEntity var32 = var1;
                        double var47 = var10.getX();
                        double var49 = var47 * var10x;
                        double var63 = var10.getZ();
                        double var65 = var63 * var10x;
                        Vector var36 = new Vector(var49, var12, var65);
                        var32.setVelocity(var36);
                    } else {
                        this.cancel();
                    }
                }
            };
            Plugin var21 = this.plugin;
            var18.runTaskTimer(var21, 0L, 1L);
        }
    }

    private void breakCobwebs(int var1) {
        int var9 = (int)Math.ceil(1.2);

        for (int var10 = -var9; var10 <= var9; var10++) {
            for (int var11 = -var9; var11 <= var9; var11++) {
                for (int var12 = -var9; var12 <= var9; var12++) {
                    Location var28 = this.loc.clone();
                    double var42 = (double)var10;
                    double var49 = (double)var11;
                    double var51 = (double)var12;
                    Location var13 = var28.add(var42, var49, var51);
                    Location var45 = this.loc;
                    if (!(var13.distanceSquared(var45) > 1.44)) {
                        Material var36 = var13.getBlock().getType();
                        Material var46 = Material.COBWEB;
                        if (var36 == var46) {
                            Block var38 = var13.getBlock();
                            Material var47 = Material.AIR;
                            var38.setType(var47, false);
                        }
                    }
                }
            }
        }
    }

    private Transformation transform(float var1, int var2) {
        Vector3f var4 = new Vector3f(0.0F, 0.0F, 0.0F);
        AxisAngle4f var12 = new AxisAngle4f(var1, 0.0F, 1.0F, 0.0F);
        Vector3f var13 = new Vector3f(1.25F, 1.25F, 1.25F);
        AxisAngle4f var15 = new AxisAngle4f(0.0F, 0.0F, 1.0F, 0.0F);
        return new Transformation(var4, var12, var13, var15);
    }

    private static Vector rotateYaw(Vector var0, double var1, int var3) {
        double var15 = Math.cos(var1);
        double var17 = Math.sin(var1);
        double var26 = var0.getX();
        double var28 = var26 * var15;
        double var33 = var0.getZ();
        double var35 = var33 * var17;
        double var30 = var28 - var35;
        double var38 = var0.getY();
        double var41 = var0.getX();
        double var43 = var41 * var17;
        double var48 = var0.getZ();
        double var50 = var48 * var15;
        double var45 = var43 + var50;
        return new Vector(var30, var38, var45);
    }

    private void despawn(int var1) {
        if (!this.display.isDead()) {
            this.display.remove();
        }

        this.cancel();
    }

    static {
        ConcurrentHashMap var4 = new ConcurrentHashMap();
        RECENT_SAW_HIT = var4;
    }
}
