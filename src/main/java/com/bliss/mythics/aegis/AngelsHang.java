package com.bliss.mythics.aegis;

import com.bliss.core.Particles;
import com.bliss.core.TrustStore;
import com.bliss.gems.ConsistentDamage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class AngelsHang {
    private static final double GRAB_RADIUS = 10.0;
    private static final int HANG_TICKS = 300;
    private static final int HANG_HEIGHT = 40;
    private static final double HANG_DROP = 3.0;
    private static final int CAP_R = 1;
    private static final double SPRING = 0.3;
    private static final double DAMP = 0.55;
    private static final double GRAV_COMP = 0.06;
    private static final double RAGDOLL = 0.3;
    private static final double LEASH_H = 3.5;
    private static final double LEASH_V = 5.0;
    private static final Random RNG;
    private static final Set<UUID> HUNG = ConcurrentHashMap.newKeySet();
    private static final DustOptions ROPE;
    private static final int POLE_HANG_TICKS = 180;
    private static final double POLE_RISE = 1.6;
    private static final double POLE_SLAM_SPEED = 3.0;
    private static final int POLE_PHASE_MAX = 80;
    private static final double ARM_R = 1.1;
    private static final double ARM_BOW = 0.45;
    private static final double ARM_WAVE = 0.48;
    private static final double ARM_STEP = 0.3;
    private static final double POLE_DROP_DAMAGE = 8.0;
    private static final int POLE_UP1 = 0;
    private static final int POLE_SLAM = 1;
    private static final int POLE_UP2 = 2;
    private static final int POLE_HANG = 3;

    private AngelsHang() {
    }

    public static void releaseAll(int var0) {
        Iterator var6 = HUNG.iterator();

        while (var6.hasNext()) {
            Player var8 = Bukkit.getPlayer((UUID)var6.next());
            if (var8 != null && var8.isOnline()) {
                var8.setFallDistance(0.0F);
                PotionEffectType var3 = PotionEffectType.SLOW_FALLING;
                PotionEffect var24 = new PotionEffect(var3, 200, 0);
                var8.addPotionEffect(var24);
            }
        }

        HUNG.clear();
    }

    public static void tryHang(Plugin var0, TrustStore var1, Player var2, int var3) {
        Location var7 = var2.getLocation();
        ArrayList var12 = new ArrayList();
        ArrayList var8 = var12;

        for (Player var10 : var2.getWorld().getPlayers()) {
            if (!var10.equals(var2) && !var10.isDead() && var10.isValid()) {
                GameMode var32 = var10.getGameMode();
                GameMode var50 = GameMode.SPECTATOR;
                if (var32 != var50) {
                    GameMode var34 = var10.getGameMode();
                    GameMode var51 = GameMode.CREATIVE;
                    if (var34 != var51) {
                        Set var35 = HUNG;
                        UUID var53 = var10.getUniqueId();
                        if (!var35.contains(var53)) {
                            UUID var55 = var2.getUniqueId();
                            UUID var60 = var10.getUniqueId();
                            if (!var1.isTrusted(var55, var60, 1736364606)) {
                                Location var40 = var10.getLocation();
                                if (var40.distanceSquared(var7) <= 100.0) {
                                    var8.add(var10);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!var8.isEmpty()) {
            Random var5 = RNG;
            int var63 = var8.size();
            int var58 = var5.nextInt(var63);
            Player var48 = (Player)var8.get(var58);
            hang(var0, var48, 574296965);
        }
    }

    private static void hang(Plugin var0, Player var1, int var2) {
        final World var17 = var1.getWorld();
        final UUID var18 = var1.getUniqueId();
        final double var19 = var1.getLocation().getX();
        final double var21 = var1.getLocation().getZ();
        final double var23 = var1.getLocation().getY() + 40.0;
        final double var25 = var23 - 3.0;
        Set var55 = HUNG;
        var55.add(var18);
        ArrayList var57 = new ArrayList();
        final ArrayList var27 = var57;
        int var28 = (int)Math.floor(var23);
        int var90 = var17.getMinHeight();
        if (var28 > var90) {
            int var92 = var17.getMaxHeight();
            if (var28 < var92) {
                int var29 = var1.getLocation().getBlockX();
                int var30 = var1.getLocation().getBlockZ();

                for (int var31 = -1; var31 <= 1; var31++) {
                    for (int var32 = -1; var32 <= 1; var32++) {
                        int var102 = var29 + var31;
                        int var119 = var30 + var32;
                        Block var33 = var17.getBlockAt(var102, var28, var119);
                        if (var33.getType().isAir()) {
                            Material var103 = Material.BARRIER;
                            var33.setType(var103, false);
                            var27.add(var33);
                        }
                    }
                }

                AngelsGrasp.registerBarriers(var27, 58884413);
            }
        }

        Location var95 = var1.getLocation();
        Sound var107 = Sound.ENTITY_WARDEN_SONIC_BOOM;
        var17.playSound(var95, var107, 0.7F, 1.5F);
        Location var97 = var1.getLocation();
        Sound var108 = Sound.BLOCK_CHAIN_PLACE;
        var17.playSound(var97, var108, 1.2F, 0.6F);
        var1.setFallDistance(0.0F);
        BukkitRunnable var77 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                Player var13 = Bukkit.getPlayer(var18);
                int var78 = this.t;
                int var100 = var78 + 1;
                this.t = var100;
                if (var78 < 300 && var13 != null && var13.isOnline() && !var13.isDead() && var13.isValid()) {
                    Location var14 = var13.getLocation();
                    if (var13.isGliding()) {
                        var13.setGliding(false);
                    }

                    label28: {
                        double var102 = var19;
                        double var146 = var14.getX();
                        double var104 = var102 - var146;
                        double var149 = var25;
                        double var175 = var14.getY();
                        double var151 = var149 - var175;
                        double var178 = var21;
                        double var196 = var14.getZ();
                        double var180 = var178 - var196;
                        Vector var32 = new Vector(var104, var151, var180);
                        Vector var35 = var13.getVelocity().multiply(0.55);
                        Vector var84 = var32.multiply(0.3);
                        Vector var36 = var35.add(var84);
                        Vector var85 = new Vector(0.0, 0.06, 0.0);
                        Vector var37 = var36.add(var85);
                        double var137 = AngelsHang.RNG.nextDouble() - 0.5;
                        double var169 = (AngelsHang.RNG.nextDouble() - 0.5) * 0.5;
                        double var192 = AngelsHang.RNG.nextDouble() - 0.5;
                        Vector var86 = new Vector(var137, var169, var192);
                        Vector var87 = var86.multiply(0.3);
                        Vector var16 = var37.add(var87);
                        var13.setVelocity(var16);
                        var13.setFallDistance(0.0F);
                        double var42 = var14.getX();
                        double var111 = var19;
                        if (!(Math.abs(var42 - var111) > 3.5)) {
                            double var50 = var14.getZ();
                            double var116 = var21;
                            if (!(Math.abs(var50 - var116) > 3.5)) {
                                double var58 = var14.getY();
                                double var123 = var23 - 0.5;
                                if (!(var58 > var123)) {
                                    double var62 = var14.getY();
                                    double var128 = var25 - 5.0;
                                    if (!(var62 < var128)) {
                                        break label28;
                                    }
                                }
                            }
                        }

                        World var140 = var17;
                        double var158 = var19;
                        double var183 = var25;
                        double var199 = var21;
                        float var201 = var14.getYaw();
                        float var202 = var14.getPitch();
                        Location var90 = new Location(var140, var158, var183, var199, var201, var202);
                        var13.teleport(var90);
                        Vector var91 = new Vector(0, 0, 0);
                        var13.setVelocity(var91);
                        var13.setFallDistance(0.0F);
                    }

                    World var70 = var17;
                    double var94 = var19;
                    double var143 = var23;
                    double var173 = var21;
                    Location var195 = var13.getLocation();
                    AngelsHang.drawRope(var70, var94, var143, var173, var195, 1803026431);
                    if (this.t % 18 == 0) {
                        World var75 = var17;
                        Location var98 = var13.getLocation();
                        Sound var130 = Sound.BLOCK_CHAIN_HIT;
                        var75.playSound(var98, var130, 0.7F, 0.8F);
                    }
                } else {
                    this.release(var13, 1787283161);
                    this.cancel();
                }
            }

            void release(Player var1, int var2) {
                Set var3 = AngelsHang.HUNG;
                UUID var30 = var18;
                var3.remove(var30);

                for (org.bukkit.block.Block var9 : (java.util.List<org.bukkit.block.Block>)var27) {
                    Material var28 = var9.getType();
                    Material var34 = Material.BARRIER;
                    if (var28 == var34) {
                        Material var35 = Material.AIR;
                        var9.setType(var35, false);
                    }
                }

                AngelsGrasp.unregisterBarriers(var27, 709912265);
                if (var1 != null && var1.isOnline()) {
                    var1.setFallDistance(0.0F);
                    World var23x = var17;
                    Location var33 = var1.getLocation();
                    Sound var5 = Sound.BLOCK_CHAIN_BREAK;
                    var23x.playSound(var33, var5, 1.0F, 0.8F);
                }
            }
        };
        var77.runTaskTimer(var0, 1L, 1L);
    }

    public static Player pickTarget(Player var0, double var1, TrustStore var3, int var4) {
        double var9 = var1 * var1;
        Location var11 = var0.getLocation();
        ArrayList var19 = new ArrayList();
        ArrayList var12 = var19;

        for (Player var14 : var0.getWorld().getPlayers()) {
            if (!var14.equals(var0) && !var14.isDead() && var14.isValid()) {
                GameMode var36 = var14.getGameMode();
                GameMode var57 = GameMode.SPECTATOR;
                if (var36 != var57) {
                    GameMode var38 = var14.getGameMode();
                    GameMode var58 = GameMode.CREATIVE;
                    if (var38 != var58) {
                        Set var39 = HUNG;
                        UUID var60 = var14.getUniqueId();
                        if (!var39.contains(var60)) {
                            UUID var62 = var0.getUniqueId();
                            UUID var68 = var14.getUniqueId();
                            if (!var3.isTrusted(var62, var68, 1736364606)) {
                                Location var44 = var14.getLocation();
                                double var45 = var44.distanceSquared(var11);
                                if (var45 <= var9) {
                                    var12.add(var14);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (var12.isEmpty()) {
            return null;
        } else {
            Random var65 = RNG;
            int var72 = var12.size();
            int var66 = var65.nextInt(var72);
            return (Player)var12.get(var66);
        }
    }

    public static boolean flagPole(Plugin var0, Player var1, Player var2, int var3) {
        if (!var2.isDead() && var2.isValid()) {
            GameMode var11 = var2.getGameMode();
            GameMode var5 = GameMode.SPECTATOR;
            if (var11 != var5) {
                GameMode var13 = var2.getGameMode();
                GameMode var21 = GameMode.CREATIVE;
                if (var13 != var21) {
                    Set var14 = HUNG;
                    UUID var23 = var2.getUniqueId();
                    if (!var14.add(var23)) {
                        return false;
                    }

                    UUID var25 = var1.getUniqueId();
                    pole(var0, var25, var2, 1912189860);
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private static void pole(Plugin var0, final UUID var1, final Player var2, int var3) {
        final World var26 = var2.getWorld();
        final UUID var27 = var2.getUniqueId();
        final double var28 = var2.getLocation().getX();
        final double var30 = var2.getLocation().getZ();
        double var32 = var2.getLocation().getY();
        final double var34 = clearCeiling(var26, var28, var32, var30, 206399672);
        double var122 = var34 - 3.0;
        final double var36 = Math.max(var32, var122);
        final double var38 = RNG.nextDouble() * Math.PI;
        ArrayList var74 = new ArrayList();
        final ArrayList var40 = var74;
        int var41 = (int)Math.floor(var34);
        int var107 = var26.getMinHeight();
        if (var41 > var107) {
            int var109 = var26.getMaxHeight();
            if (var41 < var109) {
                int var42 = var2.getLocation().getBlockX();
                int var43 = var2.getLocation().getBlockZ();

                for (int var44 = -1; var44 <= 1; var44++) {
                    for (int var45 = -1; var45 <= 1; var45++) {
                        int var113 = var42 + var44;
                        int var135 = var43 + var45;
                        Block var46 = var26.getBlockAt(var113, var41, var135);
                        if (var46.getType().isAir()) {
                            Material var114 = Material.BARRIER;
                            var46.setType(var114, false);
                            var40.add(var46);
                        }
                    }
                }

                AngelsGrasp.registerBarriers(var40, 58884413);
            }
        }

        Location var117 = var2.getLocation();
        Sound var129 = Sound.ENTITY_WARDEN_SONIC_BOOM;
        var26.playSound(var117, var129, 0.7F, 1.5F);
        Location var119 = var2.getLocation();
        Sound var130 = Sound.BLOCK_CHAIN_PLACE;
        var26.playSound(var119, var130, 1.2F, 0.6F);
        var2.setFallDistance(0.0F);
        BukkitRunnable var104 = new BukkitRunnable() {
            int phase = 0;
            int inPhase = 0;
            int hangT = 0;
            double lastY;

            {
                double var53 = var2.getLocation().getY();
                this.lastY = var53;
            }

            public void run() {
                Player var15 = Bukkit.getPlayer(var27);
                if (var15 != null && var15.isOnline() && !var15.isDead() && var15.isValid()) {
                    Location var16 = var15.getLocation();
                    if (var15.isGliding()) {
                        var15.setGliding(false);
                    }

                    int var134 = this.inPhase + 1;
                    this.inPhase = var134;
                    switch (this.phase) {
                        case 0:
                        case 2:
                            double var104 = var36;
                            double var217 = var16.getY();
                            double var403 = var104 - var217;
                            if (!(var403 <= 0.6) && this.inPhase <= 80) {
                                double var256 = var28;
                                double var313 = var16.getX();
                                double var260 = (var256 - var313) * 0.3;
                                double var359 = var403 * 0.5 + 0.4;
                                double var319 = Math.min(1.6, var359);
                                double var362 = var30;
                                double var389 = var16.getZ();
                                double var366 = (var362 - var389) * 0.3;
                                Vector var178 = new Vector(var260, var319, var366);
                                var15.setVelocity(var178);
                                World var130 = var26;
                                double var180 = var16.getX();
                                double var263 = var16.getZ();
                                double var322 = this.lastY;
                                double var369 = var16.getY();
                                double var394 = var38;
                                AngelsHang.arms(var130, var180, var263, var322, var369, var394, 536931716);
                            } else {
                                if (var403 > 0.6) {
                                    World var251 = var26;
                                    double var283 = var28;
                                    double var335 = var36;
                                    double var377 = var30;
                                    float var398 = var16.getYaw();
                                    float var401 = var16.getPitch();
                                    Location var169 = new Location(var251, var283, var335, var377, var398, var401);
                                    var15.teleport(var169);
                                }

                                Vector var170 = new Vector(0, 0, 0);
                                var15.setVelocity(var170);
                                this.inPhase = 0;
                                if (this.phase == 0) {
                                    this.phase = 1;
                                    World var127 = var26;
                                    Location var177 = var15.getLocation();
                                    Sound var224 = Sound.BLOCK_CHAIN_BREAK;
                                    var127.playSound(var177, var224, 1.0F, 0.6F);
                                } else {
                                    this.phase = 3;
                                    World var124 = var26;
                                    Location var174 = var15.getLocation();
                                    Sound var223 = Sound.BLOCK_CHAIN_PLACE;
                                    var124.playSound(var174, var223, 1.2F, 0.6F);
                                }
                            }
                            break;
                        case 1:
                            boolean var102;
                            if (!var15.isOnGround() && var16.clone().subtract(0.0, 0.15, 0.0).getBlock().isPassable()) {
                                var102 = false;
                            } else {
                                var102 = true;
                            }

                            if ((!var102 || this.inPhase <= 2) && this.inPhase <= 80) {
                                double var244 = var28;
                                double var305 = var16.getX();
                                double var248 = (var244 - var305) * 0.3;
                                double var349 = var30;
                                double var380 = var16.getZ();
                                double var353 = (var349 - var380) * 0.3;
                                Vector var167 = new Vector(var248, -3.0, var353);
                                var15.setVelocity(var167);
                            } else {
                                Player var97 = Bukkit.getPlayer(var1);
                                ConsistentDamage.deal(var97, var15, 8.0, 1092002170);
                                Vector var164 = new Vector(0, 0, 0);
                                var15.setVelocity(var164);
                                this.inPhase = 0;
                                this.phase = 2;
                            }
                            break;
                        default:
                            int var135 = this.hangT;
                            int var183 = var135 + 1;
                            this.hangT = var183;
                            if (var135 >= 180) {
                                this.release(var15, 1498026335);
                                this.cancel();
                                return;
                            }

                            label66: {
                                double var185 = var28;
                                double var265 = var16.getX();
                                double var187 = var185 - var265;
                                double var268 = var36;
                                double var324 = var16.getY();
                                double var270 = var268 - var324;
                                double var327 = var30;
                                double var371 = var16.getZ();
                                double var329 = var327 - var371;
                                Vector var39 = new Vector(var187, var270, var329);
                                Vector var42 = var15.getVelocity().multiply(0.55);
                                Vector var141 = var39.multiply(0.3);
                                Vector var43 = var42.add(var141);
                                Vector var142 = new Vector(0.0, 0.06, 0.0);
                                Vector var44 = var43.add(var142);
                                double var231 = AngelsHang.RNG.nextDouble() - 0.5;
                                double var295 = (AngelsHang.RNG.nextDouble() - 0.5) * 0.5;
                                double var344 = AngelsHang.RNG.nextDouble() - 0.5;
                                Vector var143 = new Vector(var231, var295, var344);
                                Vector var144 = var143.multiply(0.3);
                                Vector var18 = var44.add(var144);
                                var15.setVelocity(var18);
                                double var48 = var16.getX();
                                double var194 = var28;
                                if (!(Math.abs(var48 - var194) > 3.5)) {
                                    double var56 = var16.getZ();
                                    double var199 = var30;
                                    if (!(Math.abs(var56 - var199) > 3.5)) {
                                        double var64 = var16.getY();
                                        double var206 = var34 - 0.5;
                                        if (!(var64 > var206)) {
                                            double var68 = var16.getY();
                                            double var211 = var36 - 5.0;
                                            if (!(var68 < var211)) {
                                                break label66;
                                            }
                                        }
                                    }
                                }

                                World var234 = var26;
                                double var277 = var28;
                                double var332 = var36;
                                double var374 = var30;
                                float var396 = var16.getYaw();
                                float var399 = var16.getPitch();
                                Location var146 = new Location(var234, var277, var332, var374, var396, var399);
                                var15.teleport(var146);
                                Vector var147 = new Vector(0, 0, 0);
                                var15.setVelocity(var147);
                            }

                            if (this.hangT % 18 == 0) {
                                World var78 = var26;
                                Location var150 = var15.getLocation();
                                Sound var213 = Sound.BLOCK_CHAIN_HIT;
                                var78.playSound(var150, var213, 0.7F, 0.8F);
                            }
                    }

                    var15.setFallDistance(0.0F);
                    double var154 = var15.getLocation().getY();
                    this.lastY = var154;
                    World var82 = var26;
                    double var157 = var28;
                    double var238 = var34;
                    double var299 = var30;
                    Location var347 = var15.getLocation();
                    AngelsHang.drawRope(var82, var157, var238, var299, var347, 1803026431);
                } else {
                    this.release(var15, 1498026335);
                    this.cancel();
                }
            }

            void release(Player var1x, int var2x) {
                Set var3 = AngelsHang.HUNG;
                UUID var30x = var27;
                var3.remove(var30x);

                for (org.bukkit.block.Block var9 : (java.util.List<org.bukkit.block.Block>)var40) {
                    Material var20 = var9.getType();
                    Material var31 = Material.BARRIER;
                    if (var20 == var31) {
                        Material var32 = Material.AIR;
                        var9.setType(var32, false);
                    }
                }

                AngelsGrasp.unregisterBarriers(var40, 709912265);
                if (var1x != null && var1x.isOnline()) {
                    var1x.setFallDistance(0.0F);
                    World var29 = var26;
                    Location var35 = var1x.getLocation();
                    Sound var36x = Sound.BLOCK_CHAIN_BREAK;
                    var29.playSound(var35, var36x, 1.0F, 0.8F);
                }
            }
        };
        var104.runTaskTimer(var0, 1L, 1L);
    }

    private static double clearCeiling(World var0, double var1, double var3, double var5, int var7) {
        double var20 = var3 + 40.0;
        double var22 = Math.floor(var3) + 2.0;

        while (true) {
            double var50 = var3 + 40.0;
            if (!(var22 < var50)) {
                break;
            }

            Location var35 = new Location(var0, var1, var22, var5);
            if (!var35.getBlock().isPassable()) {
                var20 = var22;
                break;
            }

            var22++;
        }

        return var20;
    }

    private static void arms(World var0, double var1, double var3, double var5, double var7, double var9, int var11) {
        double var37 = Math.cos(var9);
        double var39 = Math.sin(var9);

        for (double var41 = var5; var41 < var7; var41 += 0.3) {
            double var111 = Math.cos(var41 * 0.48);
            double var100 = 0.5 * var111;
            double var89 = 0.5 + var100;
            double var79 = 0.45 * var89;
            double var71 = 1.0 - var79;
            double var43 = 1.1 * var71;

            for (byte var45 = -1; var45 <= 1; var45 += 2) {
                Particle var68 = Particle.FIREWORK;
                double var83 = var37 * var43;
                double var94 = (double)var45;
                double var85 = var83 * var94;
                double var77 = var1 + var85;
                double var104 = var39 * var43;
                double var116 = (double)var45;
                double var106 = var104 * var116;
                double var98 = var3 + var106;
                Particles.spawn(var0, var68, var77, var41, var98, 0, 0.0, 1.0, 0.0, 0.25, null, true, 954482185);
            }
        }
    }

    private static void drawRope(World var0, double var1, double var3, double var5, Location var7, int var8) {
        double var28 = var7.getX();
        double var30 = var7.getY() + 1.2;
        double var32 = var7.getZ();

        for (int var34 = 0; var34 <= 14; var34++) {
            double var35 = (double)var34 / 14.0;
            double var78 = var28 - var1;
            double var80 = var78 * var35;
            double var37 = var1 + var80;
            double var84 = var30 - var3;
            double var86 = var84 * var35;
            double var39 = var3 + var86;
            double var90 = var32 - var5;
            double var92 = var90 * var35;
            double var41 = var5 + var92;
            Particle var73 = Particle.DUST;
            Location var94 = new Location(var0, var37, var39, var41);
            DustOptions var27 = ROPE;
            Particles.spawn(var0, var73, var94, 1, 0.0, 0.0, 0.0, 0.0, var27, 1141219473);
        }
    }

    static {
        Random var0 = new Random();
        RNG = var0;
        Color var6 = Color.fromRGB(180, 180, 190);
        DustOptions var5 = new DustOptions(var6, 0.7F);
        ROPE = var5;
    }
}
