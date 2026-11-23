package com.bliss.mythics.aegis;

import com.bliss.core.Particles;
import com.bliss.core.TrustStore;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class VeneratedDash extends BukkitRunnable {
    private static final int CMD_PARRY = 30012;
    private static final int LIFETIME = 22;
    private static final double DASH_POWER = 0.85;
    private static final double DASH_Y = 0.3;
    private static final double SHARD_SPEED = 1.1;
    private static final double MAX_DISTANCE = 4.0;
    private static final double START_AHEAD = 0.5;
    private static final double SHARD_SPARK_SPREAD = 0.9;
    private final Plugin plugin;
    private final MythicAegisState state;
    private final TrustStore trust;
    private final Player caster;
    private final Vector dir;
    private Location shard;
    private ItemDisplay display;
    private final Set<UUID> alreadyHit;
    private int ticks;
    private double traveled;

    public VeneratedDash(Plugin var1, MythicAegisState var2, TrustStore var3, Player var4, int var5) {
        HashSet var7 = new HashSet();
        this.alreadyHit = var7;
        this.ticks = 0;
        this.traveled = 0.0;
        this.plugin = var1;
        this.state = var2;
        this.trust = var3;
        this.caster = var4;
        Vector var26 = var4.getEyeLocation().getDirection().normalize();
        this.dir = var26;
    }

    public void start(int var1) {
        Player var10 = this.caster;
        Vector var25 = this.dir.clone().multiply(0.85);
        double var63 = this.caster.getVelocity().getY() + 0.3;
        double var44 = Math.max(0.3, var63);
        Vector var26 = var25.setY(var44);
        var10.setVelocity(var26);
        this.caster.setFallDistance(0.0F);
        World var15 = this.caster.getWorld();
        Location var30 = this.caster.getLocation();
        Sound var46 = Sound.ENTITY_WARDEN_SONIC_BOOM;
        var15.playSound(var30, var46, 0.7F, 1.4F);
        World var18 = this.caster.getWorld();
        Location var33 = this.caster.getLocation();
        Sound var47 = Sound.ITEM_TRIDENT_RIPTIDE_1;
        var18.playSound(var33, var47, 1.0F, 1.2F);
        Location var36 = this.caster.getEyeLocation();
        Vector var51 = this.dir.clone().multiply(0.5);
        Location var37 = var36.add(var51);
        this.shard = var37;
        Location var53 = this.shard;
        ItemDisplay var39 = this.spawnShard(var53, 1776218026);
        this.display = var39;
        Plugin var41 = this.plugin;
        this.runTaskTimer(var41, 0L, 1L);
    }

    public void run() {
        if (this.caster.isOnline()) {
            int var53 = this.ticks + 1;
            this.ticks = var53;
            if (var53 <= 22) {
                if (this.traveled < 4.0) {
                    double var87 = this.traveled;
                    double var72 = 4.0 - var87;
                    double var14 = Math.min(1.1, var72);
                    Location var29 = this.shard;
                    double var57 = this.dir.getX();
                    double var59 = var57 * var14;
                    double var80 = this.dir.getY();
                    double var82 = var80 * var14;
                    double var93 = this.dir.getZ();
                    double var95 = var93 * var14;
                    var29.add(var59, var82, var95);
                    double var61 = this.traveled;
                    double var63 = var61 + var14;
                    this.traveled = var63;
                    if (this.display != null && !this.display.isDead()) {
                        ItemDisplay var38 = this.display;
                        Location var66 = this.shard;
                        var38.teleport(var66);
                    }
                }

                double var41;
                if (MythicAegisState.legacyAuratus(180703355)) {
                    var41 = 0.1;
                } else {
                    var41 = 0.9;
                }

                World var45 = this.shard.getWorld();
                Particle var67 = Particle.FIREWORK;
                Location var75 = this.shard;
                Particles.spawn(var45, var67, var75, 6, var41, var41, var41, 0.02, 1986132876);
                if (this.shard.getBlock().getType().isSolid()) {
                    this.end(1010048421);
                }

                return;
            }
        }

        this.end(1010048421);
    }

    private ItemDisplay spawnShard(Location var1, int var2) {
        Material var4 = Material.PAPER;
        ItemStack var3 = new ItemStack(var4);
        ItemMeta var7 = var3.getItemMeta();
        if (var7 != null) {
            Integer var19 = 30012;
            var7.setCustomModelData(var19);
            var3.setItemMeta(var7);
        }

        World var15 = var1.getWorld();
        Consumer<ItemDisplay> var23 = var1x -> {
            var1x.setPersistent(false);
            var1x.setItemStack(var3);
            ItemDisplayTransform var16 = ItemDisplayTransform.FIXED;
            var1x.setItemDisplayTransform(var16);
            Vector3f var4x = new Vector3f(0.0F, 0.0F, 0.0F);
            Quaternionf var21 = new Quaternionf();
            float var28 = (float)Math.toRadians(90.0);
            Quaternionf var22 = var21.rotateX(var28);
            Vector3f var29 = new Vector3f(2.0F, 2.0F, 2.0F);
            Quaternionf var30 = new Quaternionf();
            Transformation var17 = new Transformation(var4x, var22, var29, var30);
            var1x.setTransformation(var17);
            var1x.setTeleportDuration(1);
            Brightness var19x = new Brightness(15, 15);
            var1x.setBrightness(var19x);
        };
        return (ItemDisplay)var15.spawn(var1, ItemDisplay.class, var23);
    }

    private void end(int var1) {
        if (this.display != null && !this.display.isDead()) {
            this.display.remove();
        }

        this.display = null;
        this.cancel();
    }
}
