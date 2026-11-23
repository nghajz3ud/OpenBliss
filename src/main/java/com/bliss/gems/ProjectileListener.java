package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemType;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TagStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class ProjectileListener implements Listener {
    private static final NamespacedKey KEY_FLUX_ARROW = NamespacedKey.fromString("bliss:flux_arrow");
    private static final NamespacedKey KEY_FIRE_ARROW = NamespacedKey.fromString("bliss:fire_arrow");
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final WealthGemListener wealth;
    private final FluxGemListener flux;
    private final Random random;

    public ProjectileListener(BlissPlugin var1, PlayerDataStore var2, WealthGemListener var3, FluxGemListener var4, int var5) {
        Random var7 = new Random();
        this.random = var7;
        this.plugin = var1;
        this.players = var2;
        this.wealth = var3;
        this.flux = var4;
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent var1) {
        if (var1.getEntity() instanceof Player var6) {
            PlayerDataStore var19 = this.players;
            UUID var67 = var6.getUniqueId();
            PlayerData var93 = var19.get(var67, 1264010596);
            ItemStack var8 = var6.getInventory().getItemInOffHand();
            Projectile var9 = (Projectile)var1.getProjectile();
            if (!(var9 instanceof Arrow)) {
            }

            int var10;
            label52: {
                var10 = BlissCoreItems.gemIdOf(var8, 382441079);
                BlissCoreItems.tierOf(var8, 1633828214);
                int var69 = GemType.FIRE.id();
                if (var10 != var69) {
                    GemType var70 = GemType.FIRE;
                    if (!GoldGem.hasAbsorbedSoul(var6, var70, 1603837876)) {
                        break label52;
                    }
                }

                if (var93.energy(1160853318) > 4 && var93.canUsePassives(2051432579) && this.random.nextInt(100) < 15) {
                    int var76 = Math.max(var9.getFireTicks(), 200);
                    var9.setFireTicks(var76);
                    PersistentDataContainer var45 = var9.getPersistentDataContainer();
                    NamespacedKey var77 = KEY_FIRE_ARROW;
                    PersistentDataType var88 = PersistentDataType.BYTE;
                    Byte var90 = (byte)1;
                    var45.set(var77, var88, var90);
                }
            }

            int var79 = GemType.FLUX.id();
            if (var10 != var79) {
                GemType var80 = GemType.FLUX;
                if (!GoldGem.hasAbsorbedSoul(var6, var80, 1603837876)) {
                    return;
                }
            }

            if (var93.energy(1160853318) > 3 && var9 instanceof Arrow var12) {
                TagStore var57 = this.plugin.tags(1027557380);
                UUID var83 = var6.getUniqueId();
                if (!var57.isTagged(var83, 995676415) && var93.canUsePassives(2051432579) && this.random.nextInt(100) < 15) {
                    PersistentDataContainer var65 = var12.getPersistentDataContainer();
                    NamespacedKey var86 = KEY_FLUX_ARROW;
                    PersistentDataType var89 = PersistentDataType.BYTE;
                    Byte var92 = (byte)1;
                    var65.set(var86, var89, var92);
                    this.scheduleFluxTrail(var12, 60532645);
                }
            }
        }
    }

    private void scheduleFluxTrail(final Arrow var1, int var2) {
        BukkitRunnable var3 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                int var31 = this.t + 1;
                this.t = var31;
                if (this.t <= 100 && var1.isValid() && !var1.isOnGround() && !var1.isInBlock()) {
                    Color var35 = Color.fromRGB(94, 215, 255);
                    DustOptions var25 = new DustOptions(var35, 1.0F);
                    World var28 = var1.getWorld();
                    Particle var33 = Particle.DUST;
                    Location var38 = var1.getLocation();
                    Particles.spawn(var28, var33, var38, 1, 0.0, 0.0, 0.0, 0.0, var25, true, 1748031581);
                } else {
                    this.cancel();
                }
            }
        };
        BlissPlugin var10 = this.plugin;
        var3.runTaskTimer(var10, 1L, 1L);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent var1) {
        if (var1.getEntity() instanceof Arrow var4) {
            if (var1.getHitEntity() == null) {
                PersistentDataContainer var14 = var4.getPersistentDataContainer();
                NamespacedKey var3 = KEY_FLUX_ARROW;
                var14.remove(var3);
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onFluxArrowHit(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Arrow var7) {
            PersistentDataContainer var17 = var7.getPersistentDataContainer();
            NamespacedKey var3 = KEY_FLUX_ARROW;
            PersistentDataType var4 = PersistentDataType.BYTE;
            if (var17.has(var3, var4)) {
                PersistentDataContainer var20 = var7.getPersistentDataContainer();
                NamespacedKey var47 = KEY_FLUX_ARROW;
                var20.remove(var47);
                if (var1.getEntity() instanceof Player var57) {
                    if (var7.getShooter() instanceof Player var58) {
                        TrustStore var35 = this.plugin.trust(310732214);
                        UUID var49 = var58.getUniqueId();
                        UUID var55 = var57.getUniqueId();
                        if (var35.isTrusted(var49, var55, 1736364606)) {
                            return;
                        }
                    }

                    Location var59 = var57.getLocation();
                    World var40 = var59.getWorld();
                    var40.strikeLightning(var59);
                    Sound var56 = Sound.BLOCK_BEACON_ACTIVATE;
                    var57.playSound(var59, var56, 10.0F, 1.0F);
                    if (this.flux != null) {
                        FluxGemListener var46 = this.flux;
                        UUID var53 = var57.getUniqueId();
                        var46.markPlayerShot(var53, 1128892998);
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onFireArrowHit(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Arrow var5) {
            PersistentDataContainer var15 = var5.getPersistentDataContainer();
            NamespacedKey var3 = KEY_FIRE_ARROW;
            PersistentDataType var4 = PersistentDataType.BYTE;
            if (var15.has(var3, var4)) {
                if (var1.getEntity() instanceof Player var49) {
                    if (var5.getShooter() instanceof Player var50) {
                        TrustStore var31 = this.plugin.trust(310732214);
                        UUID var41 = var50.getUniqueId();
                        UUID var46 = var49.getUniqueId();
                        if (var31.isTrusted(var41, var46, 1736364606)) {
                            PersistentDataContainer var34 = var5.getPersistentDataContainer();
                            NamespacedKey var42 = KEY_FIRE_ARROW;
                            var34.remove(var42);
                            BukkitScheduler var38 = this.plugin.getServer().getScheduler();
                            BlissPlugin var44 = this.plugin;
                            Runnable var48 = () -> {
                                if (var49.isValid()) {
                                    var49.setFireTicks(0);
                                }
                            };
                            var38.runTask(var44, var48);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onUnfortunateShoot(ProjectileLaunchEvent var1) {
        if (var1.getEntity().getShooter() instanceof Player var7) {
            if (this.isUnfortunate(var7, 1361531348)) {
                int var19 = this.random.nextInt(100);
                int var23 = WealthGemListener.unfortunateCancelPercent(730189779);
                if (var19 < var23) {
                    var1.setCancelled(true);
                    Location var26 = var7.getLocation();
                    Sound var4 = Sound.BLOCK_ANVIL_LAND;
                    var7.playSound(var26, var4, 0.2F, 1.0F);
                }
            }
        }
    }

    @EventHandler
    public void onUnfortunatePlace(BlockPlaceEvent var1) {
        Player var7 = var1.getPlayer();
        if (this.isUnfortunate(var7, 1361531348)) {
            int var13 = this.random.nextInt(100);
            int var17 = WealthGemListener.unfortunateCancelPercent(730189779);
            if (var13 < var17) {
                var1.setCancelled(true);
                Location var20 = var7.getLocation();
                Sound var4 = Sound.BLOCK_ANVIL_LAND;
                var7.playSound(var20, var4, 0.2F, 1.0F);
            }
        }
    }

    private boolean isUnfortunate(Player var1, int var2) {
        if (this.wealth != null) {
            WealthGemListener var7 = this.wealth;
            UUID var10 = var1.getUniqueId();
            if (var7.isUnfortunate(var10, 63502028)) {
                return true;
            }
        }

        return false;
    }
}
