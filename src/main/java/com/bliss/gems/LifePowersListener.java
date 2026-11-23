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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class LifePowersListener implements Listener {
    private static final Set<Material> GROWABLE;
    private static final Set<EntityType> UNDEAD;
    private static final int HEART_LOCK_TICKS = 300;
    private static final long HEART_DRAIN_GROUP_DURATION_MS = 20000L;
    private static final int CIRCLE_OF_LIFE_ITERATIONS = 30;
    private static final long CIRCLE_OF_LIFE_TICK_PERIOD = 20L;
    private static final double CIRCLE_OF_LIFE_RADIUS_SQ = 16.0;
    private static final long FLAG_REFRESH_MS = 2500L;
    private static final long COL_RING_PERIOD = 5L;
    private static final int COL_RING_STEPS = 120;
    private static final double RING_SPACING = 0.15;
    private static final double RING_SPIN = 0.618;
    private static final double GOLDEN_ANGLE = 2.3999632;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final LifeGemListener life;
    private final GlobalState globalState;
    private final TrustStore trust;
    private final Map<UUID, Integer> lastInteractTick;
    private final Map<UUID, Integer> lastLeftClickTick;

    public LifePowersListener(BlissPlugin var1, PlayerDataStore var2, LifeGemListener var3, GlobalState var4, TrustStore var5, int var6) {
        ConcurrentHashMap var8 = new ConcurrentHashMap();
        this.lastInteractTick = var8;
        ConcurrentHashMap var16 = new ConcurrentHashMap();
        this.lastLeftClickTick = var16;
        this.plugin = var1;
        this.players = var2;
        this.life = var3;
        this.globalState = var4;
        this.trust = var5;
    }

    @EventHandler
    public void onRadiantFist(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var22) {
            Set var40 = UNDEAD;
            EntityType var86 = var1.getEntity().getType();
            if (var40.contains(var86)) {
                ItemStack var136 = var22.getInventory().getItemInOffHand();
                int var46 = BlissCoreItems.gemIdOf(var136, 382441079);
                int var88 = GemType.LIFE.id();
                boolean var83;
                if (var46 == var88) {
                    var83 = true;
                } else {
                    var83 = false;
                }

                GemType var89 = GemType.LIFE;
                boolean var25 = GoldGem.hasAbsorbedSoul(var22, var89, 1603837876);
                if (var83 || var25) {
                    PlayerDataStore var52 = this.players;
                    UUID var91 = var22.getUniqueId();
                    PlayerData var26 = var52.get(var91, 1264010596);
                    if (var26.energy(1160853318) > 3) {
                        if (var26.canUsePassives(2051432579)) {
                            ItemStack var84;
                            if (var83) {
                                var84 = var136;
                            } else {
                                var84 = GoldGem.findInInventory(var22, 1361603346);
                            }

                            int var27 = BlissCoreItems.tierOf(var84, 1633828214);
                            if (GemAbilityRules.on(GemAbilityRules.GemAbility.LIFE_RADIANT_FIST, 425337338)) {
                                double var28 = GemAbilityRules.scaleOf(GemAbilityRules.GemAbility.LIFE_RADIANT_FIST, 120299689);
                                double var30 = var1.getDamage();
                                String var68 = "life";
                                String var93 = "radiant-fist";
                                double var32 = GemTuning.damage(var68, var93, 2.0, 1172948035) - 1.0;
                                if (var27 == 2) {
                                    double var109 = var30 * var32;
                                    double var111 = var109 * var28;
                                    double var97 = var30 + var111;
                                    var1.setDamage(var97);
                                } else {
                                    double var120 = var30 * var32 / 2.0;
                                    double var122 = var120 * var28;
                                    double var104 = var30 + var122;
                                    var1.setDamage(var104);
                                }

                                Location var34 = var1.getEntity().getLocation().add(0.0, 1.0, 0.0);
                                World var80 = var34.getWorld();
                                Particle var101 = Particle.DUST;
                                Color var134 = Color.fromRGB(255, 192, 220);
                                DustOptions var18 = new DustOptions(var134, 0.8F);
                                Particles.spawn(var80, var101, var34, 5, 0.25, 0.4, 0.25, 0.0, var18, 1141219473);
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
    public void onCircleLifesteal(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var14) {
            if (var1.getEntity() instanceof LivingEntity var130) {
                if (!var130.equals(var14)) {
                    Map var35 = this.life.colCasterUntil;
                    UUID var77 = var14.getUniqueId();
                    Long var131 = (Long)var35.get(var77);
                    if (var131 != null) {
                        long var40 = var131;
                        long var4 = System.currentTimeMillis();
                        if (var40 > var4) {
                            PlayerDataStore var44 = this.players;
                            UUID var79 = var14.getUniqueId();
                            if (var44.get(var79, 1264010596).isBroken(430675503)) {
                                return;
                            }

                            if (var130 instanceof Player var17) {
                                TrustStore var52 = this.trust;
                                UUID var81 = var14.getUniqueId();
                                UUID var90 = var17.getUniqueId();
                                if (var52.isTrusted(var81, var90, 1736364606)) {
                                    return;
                                }
                            }

                            String var54 = "life";
                            String var82 = "circle-of-life";
                            double var93 = Math.sqrt(16.0);
                            double var132 = GemTuning.radius(var54, var82, var93, 1058601812);
                            Location var58 = var130.getLocation();
                            Location var84 = var14.getLocation();
                            double var59 = var58.distanceSquared(var84);
                            double var97 = var132 * var132;
                            if (var59 > var97) {
                                return;
                            }

                            String var99 = "life";
                            String var6 = "circle-lifesteal-level";
                            double var100 = GemTuning.power(var99, var6, 2.0, 1503850832);
                            int var18 = (int)Math.min(32.0, var100);
                            if (var18 <= 0) {
                                return;
                            }

                            String var115 = "life";
                            String var9 = "circle-lifesteal-regen";
                            double var116 = GemTuning.duration(var115, var9, 3.0, 812730832);
                            double var104 = Math.min(3600.0, var116);
                            int var19 = (int)Math.max(1.0, var104);
                            PotionEffectType var110 = PotionEffectType.REGENERATION;
                            int var119 = var19 * 20;
                            int var125 = var18 - 1;
                            PotionEffect var85 = new PotionEffect(var110, var119, var125, true, false, false);
                            var14.addPotionEffect(var85);
                            Location var88 = var14.getLocation().add(0.0, 1.0, 0.0);
                            Color var109 = Color.fromRGB(254, 4, 180);
                            this.spawnPulse(var88, var109, 6, 807858287);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onHeartLock(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var15) {
            if (var1.getEntity() instanceof Player var188) {
                if (!var188.equals(var15)) {
                    DamageCause var49 = var1.getCause();
                    DamageCause var119 = DamageCause.SONIC_BOOM;
                    if (var49 != var119) {
                        DamageCause var51 = var1.getCause();
                        DamageCause var120 = DamageCause.THORNS;
                        if (var51 != var120) {
                            ItemStack var189 = var15.getInventory().getItemInMainHand();
                            int var56 = BlissCoreItems.gemIdOf(var189, 382441079);
                            int var122 = GemType.LIFE.id();
                            if (var56 != var122) {
                                GemType var123 = GemType.LIFE;
                                if (!GoldGem.isWieldingSoulMain(var15, var123, 244096655)) {
                                    return;
                                }
                            }

                            if (BlissCoreItems.tierOf(var189, 1633828214) != 2) {
                                GemType var125 = GemType.LIFE;
                                if (!GoldGem.isWieldingSoulMain(var15, var125, 244096655)) {
                                    return;
                                }
                            }

                            if (!GemAbilityRules.on(GemAbilityRules.GemAbility.LIFE_HEART_LOCK, 425337338)) {
                                return;
                            }

                            PlayerDataStore var66 = this.players;
                            UUID var127 = var15.getUniqueId();
                            if (!var66.get(var127, 1264010596).canUsePowers(1616892701)) {
                                return;
                            }

                            long var19 = System.currentTimeMillis();
                            Map var74 = this.life.cd3;
                            UUID var129 = var15.getUniqueId();
                            Long var21 = (Long)var74.get(var129);
                            if (var21 != null) {
                                long var79 = var21;
                                if (var79 > var19) {
                                    long var131 = var21;
                                    Component var137 = Text.legacy(
                                        "&d\ud83d\udd2e &cYour <##FE04B4>\ud83d\udc98Heart Lock &cskill is on cooldown for &d"
                                            + readable(var131 - var19, 2002426947),
                                        373192400
                                    );
                                    var15.sendMessage(var137);
                                    return;
                                }
                            }

                            TrustStore var84 = this.trust;
                            UUID var139 = var15.getUniqueId();
                            UUID var160 = var188.getUniqueId();
                            if (var84.isTrusted(var139, var160, 1736364606)) {
                                return;
                            }

                            String var86 = "life";
                            String var140 = "heart-lock";
                            long var22 = (long)(GemTuning.duration(var86, var140, 15.0, 812730832) * 1000.0);
                            Component var144 = Text.legacy(
                                "<##FE04B4>\ud83d\udd2e <##B8FFFB>You used &8\ud83d\udc98<##FE04B4>Heart Lock <##B8FFFB>on <##FE04B4>" + var188.getName(),
                                373192400
                            );
                            var15.sendMessage(var144);
                            long var147 = var22 / 1000L;
                            String var177 = var15.getName();
                            Component var150 = Text.legacy(
                                "<##FE04B4>\ud83d\udd2e <##B8FFFB>Your heart count has been locked for <##FE04B4>"
                                    + var147
                                    + " seconds <##B8FFFB>by <##FE04B4>"
                                    + var177,
                                373192400
                            );
                            var188.sendMessage(var150);
                            String var8 = "heart-lock";
                            this.applyHeartLockCooldown(var15, var19, var8, 364570523);
                            double var24 = var188.getHealth();
                            String var99 = "life";
                            String var152 = "heart-lock-min";
                            double var26 = GemTuning.health(var99, var152, 3.0, 77875367);
                            double var28 = Math.max(var24, var26);
                            Attribute var153 = Attribute.MAX_HEALTH;
                            var188.getAttribute(var153);
                            Map var112 = this.life.heartLocked;
                            UUID var155 = var188.getUniqueId();
                            long var186 = var19 + var22;
                            LifeGemListener.HeartLockState var171 = new LifeGemListener.HeartLockState(var28, var186, 1534771597);
                            var112.put(var155, var171);

                            for (int var33 = 0; var33 < 50; var33++) {
                                BukkitScheduler var117 = Bukkit.getScheduler();
                                BlissPlugin var158 = this.plugin;
                                Runnable var173 = () -> {
                                    Location var17 = var15.getLocation().clone().add(0.0, 0.5, 0.0);
                                    Location var21x = var188.getLocation().clone().add(0.0, 0.5, 0.0);
                                    drawLineTrail(var17, var21x, 679888346);
                                };
                                long var182 = (long)var33 * 2L;
                                var117.runTaskLater(var158, var173, var182);
                            }

                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHeartDrainer(PlayerInteractEvent var1) {
        Action var12 = var1.getAction();
        Action var3 = Action.LEFT_CLICK_AIR;
        if (var12 != var3) {
            Action var14 = var1.getAction();
            Action var32 = Action.LEFT_CLICK_BLOCK;
            if (var14 != var32) {
                return;
            }
        }

        Player var8 = var1.getPlayer();
        UUID var9 = var8.getUniqueId();
        int var10 = Bukkit.getCurrentTick();
        Map var21 = this.lastLeftClickTick;
        Integer var11 = (Integer)var21.get(var9);
        if (var11 != null) {
            int var26 = var11;
            if (var26 == var10) {
                return;
            }
        }

        Map var28 = this.lastLeftClickTick;
        Integer var38 = var10;
        var28.put(var9, var38);
        BukkitScheduler var30 = Bukkit.getScheduler();
        BlissPlugin var37 = this.plugin;
        Runnable var40 = () -> {
            if (!EntityClickGuard.meleeSince(var9, var10, 1033776613)) {
                if (var8.isOnline()) {
                    this.castHeartDrainer(var8, var9, 715406637);
                }
            }
        };
        var30.runTaskLater(var37, var40, 1L);
    }

    private void castHeartDrainer(Player var1, UUID var2, int var3) {
        ItemStack var11 = var1.getInventory().getItemInMainHand();
        int var23 = BlissCoreItems.gemIdOf(var11, 382441079);
        int var84 = GemType.LIFE.id();
        if (var23 != var84) {
            GemType var85 = GemType.LIFE;
            if (!GoldGem.isWieldingSoulMain(var1, var85, 244096655)) {
                return;
            }
        }

        if (BlissCoreItems.tierOf(var11, 1633828214) != 2) {
            GemType var87 = GemType.LIFE;
            if (!GoldGem.isWieldingSoulMain(var1, var87, 244096655)) {
                return;
            }
        }

        if (GemAbilityRules.on(GemAbilityRules.GemAbility.LIFE_HEART_DRAINER, 425337338)) {
            PlayerDataStore var33 = this.players;
            if (var33.get(var2, 1264010596).canUsePowers(1616892701)) {
                long var13 = System.currentTimeMillis();
                Map var41 = this.life.cd3;
                Long var15 = (Long)var41.get(var2);
                if (var15 != null) {
                    long var46 = var15;
                    if (var46 > var13) {
                        long var91 = var15;
                        Component var97 = Text.legacy(
                            "&d\ud83d\udd2e &cYour <##FE04B4>\ud83d\udc98Heart Drainer &cskill is on cooldown for &d" + readable(var91 - var13, 2002426947),
                            373192400
                        );
                        var1.sendMessage(var97);
                        return;
                    }
                }

                Component var99 = Text.legacy("&d\ud83d\udd2e &bYou have used <##FE04B4>\ud83d\udc98Heart Drainer", 373192400);
                var1.sendMessage(var99);
                String var10 = "heart-drainer";
                this.applyHeartLockCooldown(var1, var13, var10, 364570523);
                Location var102 = var1.getLocation();
                this.drawDrainerRings(var102, 1700015655);
                String var53 = "life";
                String var103 = "heart-drain";
                long var16 = (long)(GemTuning.duration(var53, var103, 20.0, 812730832) * 1000.0);

                for (Player var19 : var1.getWorld().getPlayers()) {
                    if (!var19.equals(var1)) {
                        Location var72 = var19.getLocation();
                        Location var106 = var1.getLocation();
                        if (!(var72.distanceSquared(var106) > 9.0)) {
                            TrustStore var77 = this.trust;
                            UUID var108 = var1.getUniqueId();
                            UUID var124 = var19.getUniqueId();
                            if (!var77.isTrusted(var108, var124, 1736364606)) {
                                Component var112 = Text.legacy(
                                    "&d\ud83d\udd2e &bYou have been affected with §d\ud83d\udc98Heart Drainer &bby &d" + var1.getName(), 373192400
                                );
                                var19.sendMessage(var112);
                                Map var82 = this.life.drainedGroupExpiry;
                                UUID var114 = var19.getUniqueId();
                                Long var129 = var13 + var16;
                                var82.put(var114, var129);
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyHeartLockCooldown(Player var1, long var2, String var4, int var5) {
        PlayerDataStore var19 = this.players;
        UUID var49 = var1.getUniqueId();
        PlayerData var16 = var19.get(var49, 1264010596);
        GlobalState var22 = this.globalState;
        boolean var17 = var22.holdsDragonEgg(var1, 715974514);
        int var18 = var16.energy(1160853318);
        long var66;
        if (var18 > 5) {
            long var45;
            if (var17) {
                var45 = 40000L;
            } else {
                var45 = 75000L;
            }

            var66 = var45;
        } else if (var18 == 5) {
            long var47;
            if (var17) {
                var47 = 45000L;
            } else {
                var47 = 90000L;
            }

            var66 = var47;
        } else if (var18 == 4) {
            long var41;
            if (var17) {
                var41 = 50000L;
            } else {
                var41 = 105000L;
            }

            var66 = var41;
        } else if (var18 == 3) {
            long var31;
            if (var17) {
                var31 = 60000L;
            } else {
                var31 = 120000L;
            }

            var66 = var31;
        } else {
            long var38;
            if (var17) {
                var38 = 75000L;
            } else {
                var38 = 150000L;
            }

            var66 = var38;
        }

        Map var35 = this.life.cd3;
        UUID var56 = var1.getUniqueId();
        String var10 = "life";
        long var60 = GemTuning.cd(var10, var4, 75.0, var66, 1621983041);
        int var65 = var16.energy(1160853318);
        long var62 = EnergyScale.cd(var60, var65, 907518618);
        Long var59 = var2 + var62;
        var35.put(var56, var59);
    }

    @EventHandler
    public void onCircleOfLifeAir(PlayerInteractEvent var1) {
        Action var9 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var9 != var3) {
            Action var11 = var1.getAction();
            Action var32 = Action.RIGHT_CLICK_BLOCK;
            if (var11 != var32) {
                return;
            }
        }

        Player var5 = var1.getPlayer();
        UUID var6 = var5.getUniqueId();
        int var7 = Bukkit.getCurrentTick();
        Map var18 = this.lastInteractTick;
        Integer var8 = (Integer)var18.get(var6);
        if (var8 != null) {
            int var23 = var8;
            if (var23 == var7) {
                return;
            }
        }

        Map var25 = this.lastInteractTick;
        Integer var39 = var7;
        var25.put(var6, var39);
        if (!this.tryGreenThumb(var5, var1, 2120491045)) {
            if (this.validateMainHand(var5, false, 1985503671)) {
                this.tryCircleOfLife(var5, 1636489172);
            }
        }
    }

    private boolean tryGreenThumb(Player var1, PlayerInteractEvent var2, int var3) {
        Action var29 = var2.getAction();
        Action var5 = Action.RIGHT_CLICK_BLOCK;
        if (var29 != var5) {
            return false;
        } else if (!var1.isSneaking()) {
            return false;
        } else {
            Block var25 = var2.getClickedBlock();
            if (var25 != null) {
                Set var37 = GROWABLE;
                Material var71 = var25.getType();
                if (var37.contains(var71)) {
                    Block var74 = var1.getLocation().getBlock();
                    if (!var25.equals(var74)) {
                        return false;
                    }

                    int var46 = BlissCoreItems.gemIdOf(var1.getInventory().getItemInMainHand(), 382441079);
                    int var76 = GemType.LIFE.id();
                    if (var46 != var76) {
                        GemType var77 = GemType.LIFE;
                        if (!GoldGem.isWieldingSoulMain(var1, var77, 244096655)) {
                            return false;
                        }
                    }

                    PlayerDataStore var51 = this.players;
                    UUID var79 = var1.getUniqueId();
                    if (!var51.get(var79, 1264010596).canUsePassives(2051432579)) {
                        return false;
                    }

                    var2.setCancelled(true);
                    if (var25.getBlockData() instanceof Ageable var27) {
                        int var63 = var27.getAge();
                        int var82 = var27.getMaximumAge();
                        if (var63 < var82) {
                            int var85 = var27.getAge() + 1;
                            var27.setAge(var85);
                            var25.setBlockData(var27);
                        }
                    }

                    World var67 = var25.getWorld();
                    Particle var87 = Particle.DUST;
                    Location var90 = var25.getLocation().add(0.5, 0.5, 0.5);
                    Color var93 = Color.fromRGB(255, 0, 180);
                    DustOptions var21 = new DustOptions(var93, 1.0F);
                    Particles.spawn(var67, var87, var90, 1, 0.0, 0.0, 0.0, 0.1, var21, true, 1748031581);
                    return true;
                }
            }

            return false;
        }
    }

    @EventHandler
    public void onCircleOfLifeEntity(PlayerInteractEntityEvent var1) {
        EquipmentSlot var6 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var6 == var3) {
            Player var5 = var1.getPlayer();
            if (this.validateMainHand(var5, false, 1985503671)) {
                this.tryCircleOfLife(var5, 1636489172);
            }
        }
    }

    private boolean validateMainHand(Player var1, boolean var2, int var3) {
        ItemStack var6 = var1.getInventory().getItemInMainHand();
        int var11 = BlissCoreItems.gemIdOf(var6, 382441079);
        int var27 = GemType.LIFE.id();
        if (var11 != var27) {
            GemType var28 = GemType.LIFE;
            if (!GoldGem.isWieldingSoulMain(var1, var28, 244096655)) {
                return false;
            }
        }

        if (BlissCoreItems.tierOf(var6, 1633828214) != 2) {
            GemType var30 = GemType.LIFE;
            if (!GoldGem.isWieldingSoulMain(var1, var30, 244096655)) {
                return false;
            }
        }

        if (var2) {
            return true;
        } else {
            PlayerDataStore var22 = this.players;
            UUID var32 = var1.getUniqueId();
            return var22.get(var32, 1264010596).canUsePowers(1616892701);
        }
    }

    private void tryCircleOfLife(final Player var1, int var2) {
        if (GemAbilityRules.on(GemAbilityRules.GemAbility.LIFE_CIRCLE, 425337338)) {
            final UUID var19 = var1.getUniqueId();
            long var20 = System.currentTimeMillis();
            Map var41 = this.life.cd2;
            Long var22 = (Long)var41.get(var19);
            if (var22 != null) {
                long var46 = var22;
                if (var46 > var20) {
                    long var96 = var22;
                    Component var102 = Text.legacy(
                        "&d\ud83d\udd2e &cYour §d\ud83d\udc96Circle of Life &cskill is on cooldown for &d" + readable(var96 - var20, 2002426947), 373192400
                    );
                    var1.sendMessage(var102);
                    return;
                }
            }

            Component var104 = Text.legacy("&d\ud83d\udd2e &bYou have summoned the §d\ud83d\udc96Circle of Life", 373192400);
            var1.sendMessage(var104);
            this.applyCircleOfLifeCooldown(var1, var20, 1882751829);
            String var119 = "life";
            String var158 = "circle-of-life";
            double var120 = GemTuning.duration(var119, var158, 30.0, 812730832);
            final int var23 = (int)Math.max(1.0, var120);
            String var57 = "life";
            String var106 = "circle-of-life";
            double var124 = Math.sqrt(16.0);
            final double var24 = GemTuning.radius(var57, var106, var124, 1058601812);
            final double var26 = var24 * var24;
            String var64 = "life";
            String var107 = "circle-repair-caster";
            final int var28 = (int)GemTuning.power(var64, var107, 6.0, 1503850832);
            String var68 = "life";
            String var108 = "circle-repair-ally";
            final int var29 = (int)GemTuning.power(var68, var108, 3.0, 1503850832);
            Location var110 = var1.getLocation();
            this.drawCoLRings(var110, var24, 926501202);
            long var137 = (long)var23 * 1000L;
            final long var30 = var20 + var137;
            Map var79 = this.life.colCasterUntil;
            Long var141 = var30;
            var79.put(var19, var141);
            Map var83 = this.life.colCasterExpiry;
            Long var144 = var30;
            var83.put(var19, var144);
            final HashSet var85 = new HashSet();
            BukkitRunnable var86 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    int var77 = this.t + 1;
                    this.t = var77;
                    if (var1.isOnline()) {
                        int var15 = this.t;
                        int var79 = var23;
                        if (var15 <= var79) {
                            if (var1.isDead()) {
                                Map var23x = LifePowersListener.this.life.colCasterUntil;
                                UUID var81 = var19;
                                var23x.remove(var81);
                                Map var28x = LifePowersListener.this.life.colCasterExpiry;
                                UUID var83 = var19;
                                var28x.remove(var83);
                                this.cancel();
                                return;
                            }

                            Player var32 = var1;
                            int var85x = var28;
                            LifePowersListener.repairInventory(var32, var85x, 1267412223);
                            long var7 = System.currentTimeMillis();
                            Map var38 = LifePowersListener.this.life.colCasterExpiry;
                            UUID var87 = var19;
                            long var110 = var7 + 2500L;
                            long var137 = var30;
                            Long var114 = Math.min(var110, var137);
                            var38.put(var87, var114);

                            for (Player var10 : var1.getWorld().getPlayers()) {
                                Player var89 = var1;
                                if (!var10.equals(var89)) {
                                    Location var53 = var10.getLocation();
                                    Location var92 = var1.getLocation();
                                    double var54 = var53.distanceSquared(var92);
                                    double var116 = var26;
                                    if (!(var54 > var116)) {
                                        TrustStore var59 = LifePowersListener.this.trust;
                                        UUID var94 = var19;
                                        UUID var119 = var10.getUniqueId();
                                        if (var59.isTrusted(var94, var119, 1736364606)) {
                                            Map var64 = LifePowersListener.this.life.colGroupExpiry;
                                            UUID var96 = var10.getUniqueId();
                                            long var122 = var7 + 2500L;
                                            long var142 = var30;
                                            Long var126 = Math.min(var122, var142);
                                            var64.put(var96, var126);
                                            int var98 = var29;
                                            LifePowersListener.repairArmor(var10, var98, 2070484734);
                                            Set var68 = var85;
                                            UUID var100 = var10.getUniqueId();
                                            if (var68.add(var100)) {
                                                String var103 = var1.getName();
                                                long var145 = var30;
                                                long var149 = (var145 - var7) / 1000L;
                                                long var129 = Math.max(1L, var149);
                                                Component var105 = Text.legacy(
                                                    "&d\ud83d\udd2e &bYou have received additional hearts from <##FE04B4>"
                                                        + var103
                                                        + " &bfor <##FE04B4>"
                                                        + var129
                                                        + " seconds",
                                                    373192400
                                                );
                                                var10.sendMessage(var105);
                                            }
                                        } else {
                                            Map var74 = LifePowersListener.this.life.drainedSoloExpiry;
                                            UUID var107 = var10.getUniqueId();
                                            Long var135 = var7 + 2500L;
                                            var74.put(var107, var135);
                                        }
                                    }
                                }
                            }

                            return;
                        }
                    }

                    this.cancel();
                }
            };
            BlissPlugin var114 = this.plugin;
            var86.runTaskTimer(var114, 20L, 20L);
            long var153 = (long)var23 * 20L / 5L;
            final int var33 = (int)Math.max(1L, var153);
            BukkitRunnable var93 = new BukkitRunnable() {
                int t = 0;

                public void run() {
                    if (var1.isOnline() && !var1.isDead()) {
                        int var15 = this.t + 1;
                        this.t = var15;
                        int var17 = var33;
                        if (var15 <= var17) {
                            Location var12 = LifePowersListener.groundUnder(var1.getLocation(), 1624637359);
                            int var19 = this.t;
                            double var21 = var24;
                            LifePowersListener.drawCoLRing(var12, var19, var21, 1950685109);
                            return;
                        }
                    }

                    this.cancel();
                }
            };
            BlissPlugin var116 = this.plugin;
            var93.runTaskTimer(var116, 0L, 5L);
        }
    }

    private void applyCircleOfLifeCooldown(Player var1, long var2, int var4) {
        PlayerDataStore var18 = this.players;
        UUID var48 = var1.getUniqueId();
        PlayerData var15 = var18.get(var48, 1264010596);
        GlobalState var21 = this.globalState;
        boolean var16 = var21.holdsDragonEgg(var1, 715974514);
        int var17 = var15.energy(1160853318);
        long var65;
        if (var17 > 5) {
            long var40;
            if (var16) {
                var40 = 90000L;
            } else {
                var40 = 180000L;
            }

            var65 = var40;
        } else if (var17 == 5) {
            long var44;
            if (var16) {
                var44 = 105000L;
            } else {
                var44 = 215000L;
            }

            var65 = var44;
        } else if (var17 == 4) {
            long var38;
            if (var16) {
                var38 = 126000L;
            } else {
                var38 = 252000L;
            }

            var65 = var38;
        } else if (var17 == 3) {
            long var42;
            if (var16) {
                var42 = 144000L;
            } else {
                var42 = 288000L;
            }

            var65 = var42;
        } else {
            long var46;
            if (var16) {
                var46 = 180000L;
            } else {
                var46 = 360000L;
            }

            var65 = var46;
        }

        Map var29 = this.life.cd2;
        UUID var52 = var1.getUniqueId();
        String var9 = "life";
        String var10 = "circle-of-life";
        long var59 = GemTuning.cd(var9, var10, 180.0, var65, 1621983041);
        int var64 = var15.energy(1160853318);
        long var61 = EnergyScale.cd(var59, var64, 907518618);
        Long var58 = var2 + var61;
        var29.put(var52, var58);
    }

    private static void repairInventory(Player var0, int var1, int var2) {
        if (var1 > 0) {
            PlayerInventory var7 = var0.getInventory();
            ItemStack[] var8 = var7.getContents();
            int var9 = 0;

            while (true) {
                int var37 = var8.length;
                if (var9 >= var37) {
                    return;
                }

                ItemStack var10 = var8[var9];
                if (var10 != null && var10.getType().getMaxDurability() > 0 && var10.getItemMeta() instanceof Damageable var11 && var11.getDamage() > 0) {
                    int var43 = var11.getDamage();
                    int var44 = var43 - var1;
                    int var40 = Math.max(0, var44);
                    var11.setDamage(var40);
                    var10.setItemMeta(var11);
                    var7.setItem(var9, var10);
                }

                var9++;
            }
        }
    }

    private static void repairArmor(Player var0, int var1, int var2) {
        if (var1 > 0) {
            PlayerInventory var6 = var0.getInventory();
            EquipmentSlot[] var17 = new EquipmentSlot[4];
            EquipmentSlot var5 = EquipmentSlot.HEAD;
            var17[0] = var5;
            EquipmentSlot var56 = EquipmentSlot.CHEST;
            var17[1] = var56;
            EquipmentSlot var57 = EquipmentSlot.LEGS;
            var17[2] = var57;
            EquipmentSlot var58 = EquipmentSlot.FEET;
            var17[3] = var58;

            for (EquipmentSlot var10 : var17) {
                ItemStack var11 = var6.getItem(var10);
                if (var11 != null && var11.getType().getMaxDurability() > 0) {
                    ItemMeta var13 = var11.getItemMeta();
                    if (var13 instanceof Damageable) {
                        Damageable var12 = (Damageable)var13;
                        if (var12.getDamage() > 0) {
                            int var53 = var12.getDamage();
                            int var54 = var53 - var1;
                            int var46 = Math.max(0, var54);
                            var12.setDamage(var46);
                            var11.setItemMeta(var12);
                            var6.setItem(var10, var11);
                        }
                    }
                }
            }
        }
    }

    private static void drawLineTrail(Location var0, Location var1, int var2) {
        World var33 = var0.getWorld();
        World var61 = var1.getWorld();
        if (var33.equals(var61)) {
            Color var64 = Color.fromRGB(255, 0, 180);
            DustOptions var35 = new DustOptions(var64, 1.5F);
            DustOptions var22 = var35;

            for (int var24 = 0; var24 <= 20; var24++) {
                double var25 = (double)var24 / 20.0;
                double var45 = var0.getX();
                double var69 = var1.getX();
                double var93 = var0.getX();
                double var71 = var69 - var93;
                double var73 = var71 * var25;
                double var27 = var45 + var73;
                double var50 = var0.getY();
                double var76 = var1.getY();
                double var98 = var0.getY();
                double var78 = var76 - var98;
                double var80 = var78 * var25;
                double var29 = var50 + var80;
                double var55 = var0.getZ();
                double var83 = var1.getZ();
                double var103 = var0.getZ();
                double var85 = var83 - var103;
                double var87 = var85 * var25;
                double var31 = var55 + var87;
                World var60 = var0.getWorld();
                Particle var63 = Particle.DUST;
                World var108 = var0.getWorld();
                Location var89 = new Location(var108, var27, var29, var31);
                Particles.spawn(var60, var63, var89, 1, 0.0, 0.0, 0.0, 0.0, var22, true, 1748031581);
            }
        }
    }

    private void drawDrainerRings(Location var1, int var2) {
        BlissPlugin var8 = this.plugin;
        Color var9 = Color.fromRGB(254, 4, 180);
        GemBurst.burst(var8, var1, var9, 3.0, 1155716754);
    }

    private void drawCoLRings(Location var1, double var2, int var4) {
        BlissPlugin var10 = this.plugin;
        Color var11 = Color.fromRGB(254, 4, 180);
        GemBurst.burst(var10, var1, var11, var2, 1155716754);
    }

    private static Location groundUnder(Location var0, int var1) {
        if (var0.getWorld() == null) {
            return var0;
        } else {
            int var6 = var0.getBlockX();
            int var7 = var0.getBlockZ();
            int var8 = (int)Math.floor(var0.getY());

            for (int var9 = 0; var9 <= 4; var9++) {
                World var27 = var0.getWorld();
                int var42 = var8 - var9;
                Block var10 = var27.getBlockAt(var6, var42, var7);
                if (var10.getType().isSolid()) {
                    Location var11 = var0.clone();
                    double var40 = var10.getBoundingBox().getMaxY();
                    var11.setY(var40);
                    return var11;
                }
            }

            return var0;
        }
    }

    private static void drawCoLRing(Location var0, int var1, double var2, int var4) {
        Color var19 = Color.fromRGB(255, 0, 179);
        DustOptions var5 = new DustOptions(var19, 1.5F);
        Location var14 = var0.clone().add(0.0, 0.25, 0.0);
        drawSpinningRing(var14, var2, var5, var1, 862649152);
    }

    private static void drawSpinningRing(Location var0, double var1, DustOptions var3, int var4, int var5) {
        if (var0.getWorld() != null) {
            int var70 = (int)Math.round((Math.PI * 2) * var1 / 0.15);
            int var32 = Math.max(16, var70);
            double var42 = (double)var4;
            double var99 = (double)var32;
            double var75 = (Math.PI * 2) / var99;
            double var33 = var42 * var75 * 0.618;

            for (int var35 = 0; var35 < var32; var35++) {
                double var102 = (double)var35;
                double var81 = (Math.PI * 2) * var102;
                double var105 = (double)var32;
                double var83 = var81 / var105;
                double var36 = var33 + var83;
                World var55 = var0.getWorld();
                Particle var72 = Particle.DUST;
                World var108 = var0.getWorld();
                double var122 = var0.getX();
                double var147 = Math.cos(var36);
                double var137 = var1 * var147;
                double var124 = var122 + var137;
                double var140 = var0.getY();
                double var150 = var0.getZ();
                double var164 = Math.sin(var36);
                double var160 = var1 * var164;
                double var152 = var150 + var160;
                Location var85 = new Location(var108, var124, var140, var152);
                Particles.spawn(var55, var72, var85, 0, 0.0, 0.0, 0.0, 0.0, var3, true, 1748031581);
            }

            if (var4 % 2 == 0) {
                double var89 = (double)var4 * 2.3999632;
                double var169 = var33 + var89;
                World var63 = var0.getWorld();
                Particle var74 = Particle.HAPPY_VILLAGER;
                double var92 = var0.getX();
                double var128 = Math.cos(var169);
                double var115 = var1 * var128;
                double var94 = var92 + var115;
                double var120 = var0.getY() + 0.1;
                double var133 = var0.getZ();
                double var158 = Math.sin(var169);
                double var144 = var1 * var158;
                double var135 = var133 + var144;
                Particles.spawn(var63, var74, var94, var120, var135, 1, 0.05, 0.05, 0.05, 0.0, 1717927553);
            }
        }
    }

    private static void drawRing(Location var0, double var1, DustOptions var3) {
        if (var0.getWorld() != null) {
            int var60 = (int)Math.round(var1 * 12.0);
            int var25 = Math.max(16, var60);

            for (int var26 = 0; var26 < var25; var26++) {
                double var63 = (double)var26;
                double var40 = (Math.PI * 2) * var63;
                double var66 = (double)var25;
                double var27 = var40 / var66;
                double var45 = var0.getX();
                double var78 = Math.cos(var27);
                double var70 = var1 * var78;
                double var29 = var45 + var70;
                double var50 = var0.getZ();
                double var82 = Math.sin(var27);
                double var74 = var1 * var82;
                double var31 = var50 + var74;
                World var55 = var0.getWorld();
                Particle var62 = Particle.DUST;
                World var85 = var0.getWorld();
                double var88 = var0.getY();
                Location var76 = new Location(var85, var29, var88, var31);
                Particles.spawn(var55, var62, var76, 1, 0.0, 0.0, 0.0, 0.0, var3, true, 1748031581);
            }
        }
    }

    private void spawnPulse(Location var1, Color var2, int var3, int var4) {
        BlissPlugin var10 = this.plugin;
        GemBurst.burst(var10, var1, var2, 1.5, 1155716754);
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
        Material var0 = Material.SWEET_BERRY_BUSH;
        Material[] var7 = new Material[9];
        Material var3 = Material.POTATOES;
        var7[0] = var3;
        Material var28 = Material.CARROTS;
        var7[1] = var28;
        Material var29 = Material.BEETROOTS;
        var7[2] = var29;
        Material var30 = Material.WHEAT;
        var7[3] = var30;
        Material var31 = Material.PUMPKIN_STEM;
        var7[4] = var31;
        Material var32 = Material.MELON_STEM;
        var7[5] = var32;
        Material var33 = Material.TORCHFLOWER_CROP;
        var7[6] = var33;
        Material var34 = Material.NETHER_WART;
        var7[7] = var34;
        Material var35 = Material.COCOA;
        var7[8] = var35;
        GROWABLE = EnumSet.of(var0, var7);
        EntityType var5 = EntityType.WITHER_SKELETON;
        EntityType[] var9 = new EntityType[10];
        EntityType var36 = EntityType.WITHER;
        var9[0] = var36;
        EntityType var37 = EntityType.ZOMBIE;
        var9[1] = var37;
        EntityType var38 = EntityType.STRAY;
        var9[2] = var38;
        EntityType var39 = EntityType.HUSK;
        var9[3] = var39;
        EntityType var40 = EntityType.DROWNED;
        var9[4] = var40;
        EntityType var41 = EntityType.PHANTOM;
        var9[5] = var41;
        EntityType var42 = EntityType.SKELETON;
        var9[6] = var42;
        EntityType var43 = EntityType.ZOGLIN;
        var9[7] = var43;
        EntityType var44 = EntityType.ZOMBIE_VILLAGER;
        var9[8] = var44;
        EntityType var45 = EntityType.ZOMBIFIED_PIGLIN;
        var9[9] = var45;
        UNDEAD = EnumSet.of(var5, var9);
    }
}
