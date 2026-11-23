package com.bliss.mythics.aegis;

import com.bliss.core.GemTuning;
import com.bliss.core.Particles;
import com.bliss.core.TrustStore;
import com.bliss.gems.ConsistentDamage;
import com.bliss.mythics.util.MythicCombat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class PerforatorChain extends BukkitRunnable {
    private static final double RANGE = 300.0;
    private static final int LIFETIME = 100;
    private static final int HEAD_SPEED = 5;
    private static final double GLIDE_STEER = 0.3;
    private static final double GLIDE_CAP = 2.6;
    private static final double GLIDE_Y_MIN = -0.14;
    private static final double GLIDE_Y_MAX = 0.16;
    private static final double PULL = 2.15;
    private static final double PULL_FAST = 4.0;
    private static final double PULL_ENTITY = 2.0;
    private static final double ARRIVE_PLAYER = 1.8;
    private static final double ARRIVE_ENTITY = 2.5;
    private static final double MIN_UP = 0.12;
    private static final double LEDGE_ABOVE = 1.2;
    private static final double ENTITY_LIFT_Y = 0.45;
    private static final double ENTITY_POP_Y = 0.35;
    private static final double ENTITY_FRONT_DIST = 2.0;
    private static final double STUCK_DIST = 0.08;
    private static final int STUCK_TICKS = 20;
    private static final double OVERSHOOT = 0.75;
    private static final double SLAM_LOOK_DOWN = 0.25;
    private static final double COBWEB_RADIUS = 1.2;
    public static final double AURATUS_ARMOR_WEAR = 0.3;
    private static final double LATCH_DAMAGE = 14.08;
    private static final double CHEST_Y = 1.1;
    private static final double LINK_LEN = 0.5;
    private static final float ANCHOR_VOL = 10.0F;
    private static final float ANCHOR_VOL_SELF = 1.0F;
    private static final float ANCHOR_PITCH = 0.5F;
    private static final int ANCHOR_STACK = 2;
    private static final float RATTLE_VOL = 0.8F;
    private static final int RATTLE_GAP_TICKS = 8;
    private static final float THROW_PITCH = 1.6F;
    private static final float RATTLE_PITCH_NORMAL = 1.0F;
    private static final float RATTLE_PITCH_LOW = 0.75F;
    private static final float LINK_SCALE = 2.0F;
    private static final double LINK_OVERLAP = 0.8;
    private static final int SEG_CAP = 90;
    private static final int CMD_CHAIN = 30010;
    private static final int CMD_CHAIN_END = 30011;
    private static final int CMD_CHAIN_DOT = 30013;
    private static final float TIP_SCALE = 2.0F;
    private static final double TIP_SPAN = 0.5625;
    private static final double TIP_HALF = 0.28125;
    private static final double CHEST_FORWARD = 0.8;
    private static final int IN_FLIGHT = 0;
    private static final int PULLING_PLAYER = 1;
    private static final int PULLING_ENTITY = 2;
    private static final Map<UUID, PerforatorChain> ACTIVE;
    private final Plugin plugin;
    private final MythicAegisState state;
    private final TrustStore trust;
    private final Player caster;
    private final List<ItemDisplay> segs;
    private ItemDisplay tip;
    private Vector g;
    private Location head;
    private Location anchor;
    private Vector pullAxis;
    private LivingEntity victim;
    private double travelled;
    private Location lastPos;
    private int stuckCount;
    private int phase;
    private int rattleMuteUntil;
    private int ticks;
    private boolean done;
    private double pullSpeed;
    private boolean airborneAtFire;
    private boolean earned;
    private boolean noRefund;
    private static final long REFUND_LOCK_MS = 1000L;
    private float fallAtFire;
    private double chainPeakY;

    public PerforatorChain(Plugin var1, MythicAegisState var2, TrustStore var3, Player var4, int var5) {
        ArrayList var7 = new ArrayList();
        this.segs = var7;
        this.travelled = 0.0;
        this.stuckCount = 0;
        this.phase = 0;
        this.rattleMuteUntil = 0;
        this.ticks = 0;
        this.done = false;
        this.pullSpeed = 2.15;
        this.plugin = var1;
        this.state = var2;
        this.trust = var3;
        this.caster = var4;
    }

    public boolean fire(int var1) {
        Map var2 = ACTIVE;
        UUID var38 = this.caster.getUniqueId();
        PerforatorChain var7 = (PerforatorChain)var2.get(var38);
        if (var7 != null && var7 != this && !var7.done) {
            var7.finish(48169957);
        }

        double var80;
        label34: {
            UUID var8 = this.caster.getUniqueId();
            MythicAegisState var41 = this.state;
            if (var41.angelsActive(var8, 1811770379)) {
                MythicAegisState var44 = this.state;
                if (var44.angelsOnCeiling(var8, 82167638)) {
                    var80 = 4.0;
                    break label34;
                }
            }

            var80 = 2.15;
        }

        this.pullSpeed = var80;
        boolean var79;
        if (!this.caster.isOnGround() && !this.caster.isInWater()) {
            var79 = true;
        } else {
            var79 = false;
        }

        this.airborneAtFire = var79;
        float var54 = this.caster.getFallDistance();
        this.fallAtFire = var54;
        double var58 = this.caster.getLocation().getY();
        this.chainPeakY = var58;
        Vector var64 = this.caster.getEyeLocation().getDirection().normalize();
        this.g = var64;
        Location var67 = this.caster.getEyeLocation();
        this.head = var67;
        Map var25 = ACTIVE;
        UUID var70 = this.caster.getUniqueId();
        var25.put(var70, this);
        Set var29 = this.state.grappling;
        UUID var73 = this.caster.getUniqueId();
        var29.add(var73);
        World var33 = this.caster.getWorld();
        Location var76 = this.caster.getLocation();
        Sound var84 = Sound.ENTITY_FISHING_BOBBER_THROW;
        var33.playSound(var76, var84, 1.0F, 1.6F);
        Plugin var78 = this.plugin;
        this.runTaskTimer(var78, 0L, 1L);
        return true;
    }

    public void run() {
        if (!this.done) {
            if (this.caster.isOnline() && !this.caster.isDead()) {
                int var37 = this.ticks + 1;
                this.ticks = var37;
                if (var37 <= 100) {
                    if (this.phase == 0 && this.caster.isSneaking()) {
                        this.finish(48169957);
                        return;
                    }

                    if (this.ticks % 4 != 0) {
                        int var21 = this.ticks;
                        int var41 = this.rattleMuteUntil;
                        if (var21 >= var41) {
                            World var24 = this.caster.getWorld();
                            Location var44 = this.caster.getLocation();
                            Sound var52 = Sound.BLOCK_CHAIN_PLACE;
                            float var62;
                            if (this.phase == 0) {
                                var62 = 1.0F;
                            } else {
                                var62 = 0.75F;
                            }

                            var24.playSound(var44, var52, 0.8F, var62);
                        }
                    }

                    double var46 = this.chainPeakY;
                    double var59 = this.caster.getLocation().getY();
                    double var48 = Math.max(var46, var59);
                    this.chainPeakY = var48;
                    switch (this.phase) {
                        case 0:
                            this.flight(1478302054);
                            break;
                        case 1:
                            this.pullPlayer(341148851);
                            break;
                        case 2:
                            this.pullEntity(819090690);
                    }

                    if (!this.done) {
                        Location var51 = this.chest(1937534624);
                        Location var54 = this.head;
                        this.renderChain(var51, var54, 2113864944);
                    }

                    return;
                }
            }

            this.finish(48169957);
        }
    }

    private void flight(int var1) {
        for (int var19 = 0; var19 < 5; var19++) {
            Location var26 = this.head;
            double var122 = this.g.getX();
            double var217 = this.g.getY();
            double var240 = this.g.getZ();
            var26.add(var122, var217, var240);
            double var126 = this.travelled + 1.0;
            this.travelled = var126;
            breakCobwebs(this.head, 1109570244);
            Location var129 = this.head;
            LivingEntity var20 = this.entityAt(var129, 348046218);
            if (var20 != null) {
                label49: {
                    this.victim = var20;
                    this.phase = 2;
                    this.earned = true;
                    if (var20 instanceof Player var254) {
                        TrustStore var103 = this.trust;
                        UUID var174 = this.caster.getUniqueId();
                        UUID var205 = var254.getUniqueId();
                        if (var103.isTrusted(var174, var205, 1736364606)) {
                            break label49;
                        }
                    }

                    String var105 = "auratus";
                    String var175 = "hauling-strike-latch";
                    double var22 = GemTuning.mythicDamage(var105, var175, 14.08, 478197159);
                    Player var109 = this.caster;
                    ConsistentDamage.dealFullArmorAware(var109, var20, var22, 1323155734);
                    MythicCombat.damageArmor(var20, var22, 0.3, 256520978);
                }

                int var181 = this.ticks + 8;
                this.rattleMuteUntil = var181;
                World var114 = this.head.getWorld();
                Location var183 = this.head;
                String var211 = "minecraft:entity.leash_knot.break";
                var114.playSound(var183, var211, 1.0F, 0.9F);
                World var117 = this.head.getWorld();
                Location var185 = this.head;
                String var212 = "minecraft:item.lead.untied";
                var117.playSound(var185, var212, 1.0F, 0.9F);
                World var119 = var20.getWorld();
                Particle var186 = Particle.CRIT;
                Location var215 = var20.getLocation().add(0.0, 1.0, 0.0);
                Particles.spawn(var119, var186, var215, 10, 0.3, 0.3, 0.3, 0.1, 1986132876);
                return;
            }

            if (this.head.getBlock().getType().isSolid()) {
                Location var134 = this.head.getBlock().getLocation().add(0.5, 0.5, 0.5);
                this.anchor = var134;
                Location var137 = this.anchor.clone();
                this.head = var137;
                int var140 = this.ticks + 8;
                this.rattleMuteUntil = var140;

                for (int var21 = 0; var21 < 2; var21++) {
                    World var51 = this.head.getWorld();
                    Location var143 = this.head;
                    Sound var190 = Sound.BLOCK_CHAIN_HIT;
                    var51.playSound(var143, var190, 10.0F, 0.5F);
                    Player var53 = this.caster;
                    Location var146 = this.caster.getLocation();
                    Sound var191 = Sound.BLOCK_CHAIN_HIT;
                    var53.playSound(var146, var191, 1.0F, 0.5F);
                }

                World var56 = this.head.getWorld();
                Particle var147 = Particle.BLOCK;
                Location var193 = this.head;
                BlockData var252 = Material.IRON_BLOCK.createBlockData();
                Particles.spawn(var56, var147, var193, 12, 0.2, 0.2, 0.2, 0.0, var252, 1141219473);
                if (this.isWearingElytra(2066146292) && !this.caster.isBlocking()) {
                    MythicAegisState var63 = this.state;
                    UUID var150 = this.caster.getUniqueId();
                    if (!var63.angelsActive(var150, 1811770379)) {
                        Vector var67 = this.anchor.toVector();
                        Vector var154 = this.caster.getLocation().toVector();
                        Vector var253 = var67.subtract(var154);
                        if (var253.lengthSquared() > 1.0E-6) {
                            var253.normalize();
                        } else {
                            var253 = this.g.clone();
                        }

                        Player var76 = this.caster;
                        Vector var156 = var253.multiply(2.15);
                        var76.setVelocity(var156);
                        this.caster.setFallDistance(0.0F);
                        this.finish(48169957);
                        return;
                    }
                }

                Vector var160 = this.anchor.toVector();
                Vector var201 = this.caster.getLocation().toVector();
                Vector var161 = var160.subtract(var201);
                this.pullAxis = var161;
                if (this.pullAxis.lengthSquared() > 1.0E-6) {
                    this.pullAxis.normalize();
                } else {
                    Vector var168 = this.g.clone();
                    this.pullAxis = var168;
                }

                Location var164 = this.caster.getLocation();
                this.lastPos = var164;
                this.phase = 1;
                return;
            }

            if (this.travelled >= 300.0) {
                this.miss(304034971);
                return;
            }
        }
    }

    private void miss(int var1) {
        MythicAegisState var10 = this.state;
        UUID var22 = this.caster.getUniqueId();
        if (var10.parryActive(var22, 949722240)) {
            Location var25 = this.caster.getEyeLocation();
            Vector var45 = this.g.clone().multiply(4);
            Location var27 = var25.add(var45).add(0.0, 16.0, 0.0);
            this.anchor = var27;
            Vector var30 = this.anchor.toVector();
            Vector var51 = this.caster.getLocation().toVector();
            Vector var32 = var30.subtract(var51).normalize();
            this.pullAxis = var32;
            Location var35 = this.caster.getLocation();
            this.lastPos = var35;
            Location var38 = this.anchor.clone();
            this.head = var38;
            this.phase = 1;
        } else {
            World var19 = this.caster.getWorld();
            Location var42 = this.caster.getLocation();
            Sound var52 = Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM;
            var19.playSound(var42, var52, 0.8F, 0.7F);
            this.finish(48169957);
        }
    }

    private void pullPlayer(int var1) {
        Location var7 = this.caster.getLocation();
        Location var112 = this.anchor.clone();
        this.head = var112;
        breakCobwebs(var7, 1109570244);
        breakCobwebs(this.caster.getEyeLocation(), 1109570244);
        if (this.lastPos != null) {
            World var28 = this.lastPos.getWorld();
            World var114 = var7.getWorld();
            if (!var28.equals(var114)) {
                this.finish(48169957);
                return;
            }
        }

        label58: {
            if (this.lastPos != null) {
                Location var116 = this.lastPos;
                if (var7.distance(var116) < 0.08) {
                    int var118 = this.stuckCount + 1;
                    this.stuckCount = var118;
                    if (var118 >= 20) {
                        if (MythicAegisState.legacyAuratus(180703355)) {
                            this.arrive(1249377366);
                        } else {
                            this.caughtRelease(1302801943);
                        }

                        return;
                    }
                    break label58;
                }
            }

            this.stuckCount = 0;
        }

        Location var121 = var7.clone();
        this.lastPos = var121;
        Vector var44 = this.anchor.toVector();
        Vector var123 = var7.toVector();
        Vector var8 = var44.subtract(var123);
        double var9 = var8.length();
        Vector var50 = var7.toVector();
        Vector var126 = this.anchor.toVector();
        Vector var51 = var50.subtract(var126);
        Vector var128 = this.pullAxis;
        if (var51.dot(var128) > 0.75) {
            Vector var59 = this.caster.getVelocity();
            Vector var130 = this.pullAxis;
            if (var59.dot(var130) > 0.0) {
                this.arrive(1249377366);
                return;
            }
        }

        if (var9 <= 1.8) {
            this.arrive(1249377366);
        } else {
            double var133 = 1.0 / var9;
            Vector var13 = var8.multiply(var133);
            this.caster.setFallDistance(0.0F);
            if (this.caster.isGliding()) {
                Vector var93 = this.caster.getVelocity();
                Vector var143 = var13.multiply(0.3);
                Vector var181 = var93.add(var143);
                double var179 = var181.getY();
                double var170 = Math.min(0.16, var179);
                double var15 = Math.max(-0.14, var170);
                var181.setY(var15);
                if (var181.length() > 2.6) {
                    double var177 = var181.length();
                    double var148 = 2.6 / var177;
                    var181.multiply(var148);
                }

                Player var108 = this.caster;
                var108.setVelocity(var181);
            } else {
                double var137 = this.pullSpeed;
                Vector var14 = var13.multiply(var137);
                double var78 = this.anchor.getY();
                double var160 = var7.getY();
                if (var78 - var160 > 1.2 && var14.getY() < 0.12) {
                    var14.setY(0.12);
                }

                Player var90 = this.caster;
                var90.setVelocity(var14);
            }
        }
    }

    private void arrive(int var1) {
        double var19 = (double)this.fallAtFire;
        double var98 = this.chainPeakY;
        double var109 = this.caster.getLocation().getY();
        double var100 = var98 - var109;
        double var80 = Math.max(0.0, var100);
        double var14 = var19 + var80;
        Vector var16 = this.caster.getVelocity();
        Player var27 = this.caster;
        double var93 = var16.getX() * 0.35;
        double var111 = var16.getY();
        double var105 = Math.max(0.0, var111);
        double var116 = var16.getZ() * 0.35;
        Vector var3 = new Vector(var93, var105, var116);
        var27.setVelocity(var3);
        this.caster.setFallDistance(0.0F);
        World var32 = this.caster.getWorld();
        Location var72 = this.caster.getLocation();
        Sound var82 = Sound.BLOCK_CHAIN_PLACE;
        var32.playSound(var72, var82, 1.0F, 1.3F);
        boolean var59;
        if (MythicAegisState.legacyAuratus(180703355)) {
            if (this.airborneAtFire && this.pullAxis != null && this.pullAxis.getY() < -0.5) {
                var59 = true;
            } else {
                var59 = false;
            }
        } else if (this.pullAxis != null && this.pullAxis.getY() < 0.0 && this.g != null && this.g.getY() <= -0.25) {
            var59 = true;
        } else {
            var59 = false;
        }

        if (var59) {
            MythicAegisState var50 = this.state;
            UUID var75 = this.caster.getUniqueId();
            if (!var50.angelsActive(var75, 1811770379)) {
                this.caster.setFallDistance(0.0F);
                Plugin var55 = this.plugin;
                Player var78 = this.caster;
                TrustStore var88 = this.trust;
                if (AngelsGrasp.airSlam(var55, var78, var88, var14, 59984793)) {
                    this.earned = true;
                }
            }
        }

        this.finish(48169957);
    }

    public static void cancelFor(UUID var0, int var1) {
        cancelFor(var0, true, 260114390);
    }

    public static void cancelFor(UUID var0, boolean var1, int var2) {
        Map var3 = ACTIVE;
        PerforatorChain var5 = (PerforatorChain)var3.get(var0);
        if (var5 != null && !var5.done) {
            boolean var14;
            if (!var1) {
                var14 = true;
            } else {
                var14 = false;
            }

            var5.noRefund = var14;
            var5.finish(48169957);
        }
    }

    public static void denyRefund(UUID var0, int var1) {
        Map var2 = ACTIVE;
        PerforatorChain var4 = (PerforatorChain)var2.get(var0);
        if (var4 != null && !var4.done) {
            var4.noRefund = true;
        }
    }

    public static boolean isPullingEntity(UUID var0, int var1) {
        Map var2 = ACTIVE;
        PerforatorChain var4 = (PerforatorChain)var2.get(var0);
        boolean var12;
        if (var4 != null && !var4.done && var4.phase == 2) {
            var12 = true;
        } else {
            var12 = false;
        }

        return var12;
    }

    private void pullEntity(int var1) {
        if (this.victim != null && !this.victim.isDead() && this.victim.isValid()) {
            Location var14 = this.victim.getLocation();
            Location var15 = this.caster.getLocation();
            Location var80 = var14.clone();
            double var116 = this.victim.getHeight() / 2.0;
            Location var81 = var80.add(0.0, var116, 0.0);
            this.head = var81;
            Vector var16 = this.caster.getEyeLocation().getDirection().setY(0);
            if (var16.lengthSquared() < 1.0E-6) {
                Vector var47 = new Vector(0, 0, 1);
                var16 = var47;
            } else {
                var16.normalize();
            }

            Location var49 = var15.clone();
            Vector var84 = var16.multiply(2.0);
            Location var17 = var49.add(var84);
            Vector var52 = var17.toVector();
            Vector var86 = var14.toVector();
            Vector var18 = var52.subtract(var86);
            double var19 = var18.length();
            if (var19 <= 2.5) {
                LivingEntity var61 = this.victim;
                Vector var87 = new Vector(0.0, 0.35, 0.0);
                var61.setVelocity(var87);
                this.finish(48169957);
            } else {
                double var90 = 1.0 / var19;
                Vector var22 = var18.multiply(var90).multiply(2.0);
                if (this.victim.isOnGround()) {
                    double var71 = var17.getY();
                    double var107 = var14.getY() + 0.5;
                    if (var71 > var107) {
                        var22.setY(0.45);
                    }
                }

                LivingEntity var77 = this.victim;
                var77.setVelocity(var22);
            }
        } else {
            this.finish(48169957);
        }
    }

    private void renderChain(Location var1, Location var2, int var3) {
        Vector var40 = var2.toVector();
        Vector var122 = var1.toVector();
        Vector var16 = var40.subtract(var122);
        double var17 = var16.length();
        if (var17 < 1.0E-4) {
            this.clearDisplays(1818286661);
        } else {
            Vector var49 = var16.clone();
            double var125 = 1.0 / var17;
            Vector var19 = var49.multiply(var125);
            double var153 = var17 * 0.5;
            double var20 = Math.min(0.8, var153);
            Location var56 = var1.clone();
            Vector var128 = var19.clone();
            Vector var129 = var128.multiply(var20);
            Location var22 = var56.add(var129);
            double var23 = var17 - var20;
            boolean var65;
            if (var23 > 1.525) {
                var65 = true;
            } else {
                var65 = false;
            }

            double var67;
            if (var65) {
                var67 = var23 - 1.125 + 0.15;
            } else {
                var67 = var23;
            }

            Quaternionf var28 = this.chainBasisZ(var19, 999892523);
            int var169 = (int)Math.ceil(var67 / 0.8);
            int var132 = Math.min(90, var169);
            int var33 = Math.max(1, var132);
            double var171 = (double)var33;
            double var34 = var67 / var171;
            Vector3f var173 = new Vector3f(0.0F, 0.0F, 0.0F);
            Vector3f var189 = new Vector3f(2.0F, 2.0F, 2.0F);
            Quaternionf var195 = new Quaternionf();
            Transformation var83 = new Transformation(var173, var28, var189, var195);
            Transformation var36 = var83;

            while (true) {
                int var86 = this.segs.size();
                if (var86 >= var33) {
                    while (true) {
                        int var89 = this.segs.size();
                        if (var89 <= var33) {
                            for (byte var199 = 0; var199 < var33; var199 += 1) {
                                Location var100 = var22.clone();
                                Vector var141 = var19.clone();
                                double var193 = (double)var199 + 0.5;
                                double var177 = var34 * var193;
                                Vector var142 = var141.multiply(var177);
                                Location var38 = var100.add(var142);
                                var38.setYaw(0.0F);
                                var38.setPitch(0.0F);
                                List var105 = this.segs;
                                ItemDisplay var39 = (ItemDisplay)var105.get(var199);
                                var39.teleport(var38);
                                var39.setTransformation(var36);
                            }

                            if (var65) {
                                this.updateTip(var2, var19, 1879225077);
                            } else {
                                this.removeTip(1584324483);
                            }

                            return;
                        }

                        List var91 = this.segs;
                        int var138 = this.segs.size() - 1;
                        ItemDisplay var37 = (ItemDisplay)var91.remove(var138);
                        if (!var37.isDead()) {
                            var37.remove();
                        }
                    }
                }

                List var115 = this.segs;
                ItemDisplay var150 = this.spawnLink(var22, 1918531063);
                var115.add(var150);
            }
        }
    }

    private void updateTip(Location var1, Vector var2, int var3) {
        Vector var34 = var2.clone().multiply(-1);
        Quaternionf var12 = this.chainBasis(var34, 749037272);
        Location var17 = var1.clone();
        Vector var37 = var2.clone().multiply(0.5625);
        Location var13 = var17.subtract(var37);
        var13.setYaw(0.0F);
        var13.setPitch(0.0F);
        Vector3f var46 = new Vector3f(0.0F, 0.0F, 0.0F);
        Vector3f var49 = new Vector3f(2.0F, 2.0F, 2.0F);
        Quaternionf var50 = new Quaternionf();
        Transformation var21 = new Transformation(var46, var12, var49, var50);
        if (this.tip != null && !this.tip.isDead()) {
            ItemDisplay var28 = this.tip;
            var28.teleport(var13);
        } else {
            ItemDisplay var43 = this.spawnTip(var13, 1731111656);
            this.tip = var43;
        }

        ItemDisplay var31 = this.tip;
        var31.setTransformation(var21);
    }

    private void removeTip(int var1) {
        if (this.tip != null && !this.tip.isDead()) {
            this.tip.remove();
        }

        this.tip = null;
    }

    private Quaternionf chainBasisZ(Vector var1, int var2) {
        Quaternionf var5 = this.chainBasis(var1, 749037272);
        float var11 = (float)Math.toRadians(45.0);
        return var5.rotateZ(var11);
    }

    private Quaternionf chainBasis(Vector var1, int var2) {
        float var30 = (float)var1.getX();
        float var38 = (float)var1.getY();
        float var44 = (float)var1.getZ();
        Vector3f var3 = new Vector3f(var30, var38, var44);
        Vector3f var7 = var3.normalize();
        Vector3f var16;
        if (Math.abs(var7.y) > 0.99F) {
            var16 = new Vector3f(1.0F, 0.0F, 0.0F);
        } else {
            var16 = new Vector3f(0.0F, 1.0F, 0.0F);
        }

        Vector3f var17 = new Vector3f(var16);
        Vector3f var9 = var17.cross(var7).normalize();
        Vector3f var20 = new Vector3f(var7);
        Vector3f var10 = var20.cross(var9);
        Matrix3f var22 = new Matrix3f(var9, var10, var7);
        Quaternionf var27 = new Quaternionf();
        return var22.getNormalizedRotation(var27);
    }

    private ItemDisplay spawnTip(Location var1, int var2) {
        Material var4 = Material.PAPER;
        ItemStack var3 = new ItemStack(var4);
        ItemMeta var7 = var3.getItemMeta();
        if (var7 != null) {
            Integer var23 = 30011;
            var7.setCustomModelData(var23);
            var3.setItemMeta(var7);
        }

        Location var30 = var1.clone();
        var30.setYaw(0.0F);
        var30.setPitch(0.0F);
        World var19 = var30.getWorld();
        Consumer<ItemDisplay> var29 = var1x -> {
            var1x.setPersistent(false);
            var1x.setItemStack(var3);
            ItemDisplayTransform var11 = ItemDisplayTransform.FIXED;
            var1x.setItemDisplayTransform(var11);
            var1x.setTeleportDuration(1);
            Brightness var13 = new Brightness(15, 15);
            var1x.setBrightness(var13);
        };
        return (ItemDisplay)var19.spawn(var30, ItemDisplay.class, var29);
    }

    private ItemDisplay spawnLink(Location var1, int var2) {
        Material var4 = Material.PAPER;
        ItemStack var3 = new ItemStack(var4);
        ItemMeta var7 = var3.getItemMeta();
        short var24;
        if (MythicAegisState.legacyAuratus(180703355)) {
            var24 = 30010;
        } else {
            var24 = 30013;
        }

        if (var7 != null) {
            Integer var26 = Integer.valueOf(var24);
            var7.setCustomModelData(var26);
            var3.setItemMeta(var7);
        }

        Location var33 = var1.clone();
        var33.setYaw(0.0F);
        var33.setPitch(0.0F);
        World var21 = var33.getWorld();
        Consumer<ItemDisplay> var32 = var1x -> {
            var1x.setPersistent(false);
            var1x.setItemStack(var3);
            ItemDisplayTransform var11 = ItemDisplayTransform.FIXED;
            var1x.setItemDisplayTransform(var11);
            var1x.setTeleportDuration(1);
            Brightness var13 = new Brightness(15, 15);
            var1x.setBrightness(var13);
        };
        return (ItemDisplay)var21.spawn(var33, ItemDisplay.class, var32);
    }

    private void clearDisplays(int var1) {
        for (ItemDisplay var4 : this.segs) {
            if (!var4.isDead()) {
                var4.remove();
            }
        }

        this.segs.clear();
        this.removeTip(1584324483);
    }

    private boolean isWearingElytra(int var1) {
        ItemStack var4 = this.caster.getInventory().getChestplate();
        if (var4 != null) {
            Material var10 = var4.getType();
            Material var3 = Material.ELYTRA;
            if (var10 == var3) {
                return true;
            }
        }

        return false;
    }

    private Location chest(int var1) {
        return this.caster.getLocation().add(0.0, 1.1, 0.0);
    }

    private static void breakCobwebs(Location var0, int var1) {
        if (!MythicAegisState.legacyAuratus(180703355)) {
            int var8 = (int)Math.ceil(1.2);

            for (int var9 = -var8; var9 <= var8; var9++) {
                for (int var10 = -var8; var10 <= var8; var10++) {
                    for (int var11 = -var8; var11 <= var8; var11++) {
                        Location var28 = var0.clone();
                        double var42 = (double)var9;
                        double var48 = (double)var10;
                        double var50 = (double)var11;
                        Location var12 = var28.add(var42, var48, var50);
                        if (!(var12.distanceSquared(var0) > 1.44)) {
                            Material var36 = var12.getBlock().getType();
                            Material var45 = Material.COBWEB;
                            if (var36 == var45) {
                                Block var38 = var12.getBlock();
                                Material var46 = Material.AIR;
                                var38.setType(var46, false);
                            }
                        }
                    }
                }
            }
        }
    }

    private LivingEntity entityAt(Location var1, int var2) {
        World var15 = var1.getWorld();
        Iterator var11 = var15.getNearbyEntities(var1, 0.6, 0.6, 0.6).iterator();

        while (true) {
            if (!var11.hasNext()) {
                return null;
            }

            Entity var12 = (Entity)var11.next();
            if (var12 instanceof LivingEntity var13) {
                Player var47 = this.caster;
                if (!var13.equals(var47) && !var13.isDead() && !(var13 instanceof ArmorStand)) {
                    if (!(var13 instanceof Player var14)) {
                        return var13;
                    }

                    GameMode var38 = var14.getGameMode();
                    GameMode var48 = GameMode.CREATIVE;
                    if (var38 != var48) {
                        GameMode var40 = var14.getGameMode();
                        GameMode var49 = GameMode.SPECTATOR;
                        if (var40 != var49) {
                            TrustStore var42 = this.trust;
                            UUID var52 = this.caster.getUniqueId();
                            UUID var54 = var14.getUniqueId();
                            if (var42.isTrusted(var52, var54, 1736364606)) {
                                continue;
                            }
                            return var13;
                        }
                    }
                }
            }
        }
    }

    private void caughtRelease(int var1) {
        UUID var7 = this.caster.getUniqueId();
        Player var11 = this.caster;
        Vector var3 = new Vector(0, 0, 0);
        var11.setVelocity(var3);
        World var14 = this.caster.getWorld();
        Location var23 = this.caster.getLocation();
        Sound var26 = Sound.BLOCK_CHAIN_BREAK;
        var14.playSound(var23, var26, 1.0F, 0.9F);
        MythicAegisState var16 = this.state;
        if (var16.angelsActive(var7, 1811770379)) {
            MythicAegisState var19 = this.state;
            var19.clearAngels(var7, 950114969);
        }

        this.finish(48169957);
    }

    private void finish(int var1) {
        if (!this.done) {
            this.done = true;
            this.clearDisplays(1818286661);
            UUID var7 = this.caster.getUniqueId();
            Set var18 = this.state.grappling;
            var18.remove(var7);
            Map var20 = ACTIVE;
            var20.remove(var7, this);
            this.cancel();
            if (!MythicAegisState.legacyAuratus(180703355)) {
                MythicAegisState var25 = this.state;
                if (var25.angelsActive(var7, 1811770379)) {
                    MythicAegisState var28 = this.state;
                    if (var28.angelsOnCeiling(var7, 82167638)) {
                        this.noRefund = true;
                    }
                }

                long var8 = System.currentTimeMillis();
                if (!this.earned && !this.noRefund) {
                    MythicAegisState var38 = this.state;
                    if (var38.refundChain(var7, 1278254800)) {
                        MythicAegisState var41 = this.state;
                        if (!var41.angelsActive(var7, 1811770379) && MythicAegisState.chainRefundLock(1519417400)) {
                            Map var46 = this.state.chainLockUntil;
                            Long var60 = var8 + 1000L;
                            var46.put(var7, var60);
                        }
                    }
                }
            }
        }
    }

    public static LivingEntity hookedVictim(UUID var0, int var1) {
        Map var2 = ACTIVE;
        PerforatorChain var4 = (PerforatorChain)var2.get(var0);
        return var4 != null && !var4.done && var4.phase == 2 && var4.victim != null && !var4.victim.isDead() ? var4.victim : null;
    }

    public static void releaseHooked(UUID var0, int var1) {
        Map var2 = ACTIVE;
        PerforatorChain var4 = (PerforatorChain)var2.get(var0);
        if (var4 != null && !var4.done && var4.phase == 2) {
            var4.finish(48169957);
        }
    }

    static {
        HashMap var0 = new HashMap();
        ACTIVE = var0;
    }
}
