package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TagStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public final class AstraDaggersListener implements Listener {
    private static final long SALVO_DURATION_TICKS = 490L;
    private static final double DAGGER_HIT_RADIUS_SQ = 2.25;
    private static final double DAGGER_RAY_SIZE = 0.3;
    private static final double DAGGER_DAMAGE = 8.64;
    private static final int HIT_COOLDOWN_TICKS = 5;
    private static final double DAGGER_THROW_DELAY_SECONDS = 1.0;
    private final Map<UUID, Integer> lastThrowTick;
    private static final double BLADE_OFFSET_Y = 1.0;
    private static final String THROW_SOUND = "bliss:dagger_throw";
    private static final int TAG_SECONDS_PER_HIT = 10;
    private static final double LAUNCH_SPEED = 0.825;
    private static final NamespacedKey KEY_DAGGER = NamespacedKey.fromString("bliss:dagger");
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final AstraGemListener astra;
    private final GlobalState globalState;
    private final TagStore tags;
    private final Map<UUID, AstraDaggersListener.DaggerSalvo> salvos;
    private final Map<UUID, Integer> lastInteractTick;
    private final Map<UUID, Integer> lastAttackTick;
    private final Set<UUID> hitCooldown;
    private final TrustStore trust;
    private final Map<UUID, Integer> lastRightClickTick;
    private static final int TRAIL_SPARKS = 2;
    private static final double TRAIL_SPREAD = 0.35;
    private static final double TRAIL_ROD_SPEED = 0.045;
    private static final double TRAIL_SPARK_SPEED = 0.09;

    public AstraDaggersListener(BlissPlugin var1, PlayerDataStore var2, AstraGemListener var3, GlobalState var4, TagStore var5, int var6) {
        ConcurrentHashMap var8 = new ConcurrentHashMap();
        this.lastThrowTick = var8;
        ConcurrentHashMap var21 = new ConcurrentHashMap();
        this.salvos = var21;
        ConcurrentHashMap var22 = new ConcurrentHashMap();
        this.lastInteractTick = var22;
        ConcurrentHashMap var23 = new ConcurrentHashMap();
        this.lastAttackTick = var23;
        KeySetView var24 = ConcurrentHashMap.newKeySet();
        this.hitCooldown = var24;
        ConcurrentHashMap var25 = new ConcurrentHashMap();
        this.lastRightClickTick = var25;
        this.plugin = var1;
        this.players = var2;
        this.astra = var3;
        this.globalState = var4;
        this.tags = var5;
        TrustStore var32 = var1.trust(310732214);
        this.trust = var32;
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onMeleeEntity(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var5) {
            DamageCause var23 = var1.getCause();
            DamageCause var3 = DamageCause.ENTITY_ATTACK;
            if (var23 != var3) {
                DamageCause var19 = DamageCause.ENTITY_SWEEP_ATTACK;
                if (var23 != var19) {
                    return;
                }
            }

            Map var17 = this.lastAttackTick;
            UUID var21 = var5.getUniqueId();
            Integer var22 = Bukkit.getCurrentTick();
            var17.put(var21, var22);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        UUID var4 = var1.getPlayer().getUniqueId();
        Map var8 = this.lastInteractTick;
        var8.remove(var4);
        Map var11 = this.lastAttackTick;
        var11.remove(var4);
        Map var14 = this.lastThrowTick;
        var14.remove(var4);
        Map var17 = this.lastRightClickTick;
        var17.remove(var4);
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent var1) {
        Action var4 = var1.getAction();
        Action var3 = Action.LEFT_CLICK_AIR;
        if (var4 != var3) {
            Action var6 = var1.getAction();
            Action var8 = Action.LEFT_CLICK_BLOCK;
            if (var6 != var8) {
                return;
            }
        }

        Player var10 = var1.getPlayer();
        this.swing(var10, 59121301);
    }

    @EventHandler
    public void onSwing(PlayerAnimationEvent var1) {
        PlayerAnimationType var4 = var1.getAnimationType();
        PlayerAnimationType var3 = PlayerAnimationType.ARM_SWING;
        if (var4 == var3) {
            Player var7 = var1.getPlayer();
            this.swing(var7, 59121301);
        }
    }

    @EventHandler
    public void onRightClickMark(PlayerInteractEvent var1) {
        if (var1.getAction().isRightClick()) {
            Map var8 = this.lastRightClickTick;
            UUID var11 = var1.getPlayer().getUniqueId();
            Integer var12 = Bukkit.getCurrentTick();
            var8.put(var11, var12);
        }
    }

    @EventHandler
    public void onStandManipulate(PlayerArmorStandManipulateEvent var1) {
        PersistentDataContainer var6 = var1.getRightClicked().getPersistentDataContainer();
        NamespacedKey var3 = KEY_DAGGER;
        PersistentDataType var4 = PersistentDataType.STRING;
        if (var6.has(var3, var4)) {
            var1.setCancelled(true);
        }
    }

    private void swing(Player var1, int var2) {
        UUID var9 = var1.getUniqueId();
        int var10 = Bukkit.getCurrentTick();
        Map var18 = this.lastInteractTick;
        Integer var11 = (Integer)var18.get(var9);
        if (var11 != null) {
            int var23 = var11;
            if (var23 == var10) {
                return;
            }
        }

        Map var25 = this.lastInteractTick;
        Integer var49 = var10;
        var25.put(var9, var49);
        ItemStack var12 = var1.getInventory().getItemInMainHand();
        GemType var43 = GemType.ASTRA;
        boolean var13 = GoldGem.isWieldingSoulMain(var1, var43, 244096655);
        int var33 = BlissCoreItems.gemIdOf(var12, 382441079);
        int var45 = GemType.ASTRA.id();
        if (var33 == var45 || var13) {
            if (var13 || BlissCoreItems.tierOf(var12, 1633828214) == 2) {
                BukkitScheduler var39 = Bukkit.getScheduler();
                BlissPlugin var48 = this.plugin;
                Runnable var51 = () -> {
                    Map var9x = this.lastAttackTick;
                    Integer var7 = (Integer)var9x.get(var9);
                    if (var7 != null) {
                        int var14 = var7;
                        if (var14 >= var10) {
                            return;
                        }
                    }

                    if (!EntityClickGuard.recentlyClickedEntity(var9, 1923725760)) {
                        Map var18x = this.lastRightClickTick;
                        Integer var8 = (Integer)var18x.get(var9);
                        if (var8 != null) {
                            int var23x = var8;
                            if (var23x >= var10) {
                                return;
                            }
                        }

                        this.handleDaggerClick(var1, var9, 1443567907);
                    }
                };
                var39.runTask(var48, var51);
            }
        }
    }

    private void handleDaggerClick(Player var1, UUID var2, int var3) {
        Map var14 = this.salvos;
        AstraDaggersListener.DaggerSalvo var9 = (AstraDaggersListener.DaggerSalvo)var14.get(var2);
        if (var9 != null) {
            int var10 = this.nextThrowableIndex(var9, 2125735082);
            if (var10 >= 0) {
                String var21 = "astra";
                String var80 = "dagger-throw-delay";
                int var11 = (int)Math.round(GemTuning.duration(var21, var80, 1.0, 812730832) * 20.0);
                if (var11 > 0) {
                    Map var31 = this.lastThrowTick;
                    Integer var12 = (Integer)var31.get(var2);
                    int var13 = Bukkit.getCurrentTick();
                    if (var12 != null) {
                        int var83 = var12;
                        int var37 = var13 - var83;
                        if (var37 < var11) {
                            return;
                        }
                    }

                    Map var39 = this.lastThrowTick;
                    Integer var107 = var13;
                    var39.put(var2, var107);
                }

                this.throwOne(var1, var9, var10, 1383759223);
            } else {
                if (var9.shootdagger > 0) {
                    var9.shootdagger = 0;
                    if (var9.bar != null) {
                        BossBar var89 = var9.bar;
                        var1.hideBossBar(var89);
                        var9.bar = null;
                    }
                }
            }
        } else if (var1.isOnline()) {
            Map var53 = this.astra.projections;
            if (!var53.containsKey(var2)) {
                PlayerDataStore var56 = this.players;
                if (var56.get(var2, 1264010596).canUsePowers(1616892701)) {
                    TagStore var61 = this.tags;
                    if (!var61.isTagged(var2, 995676415)) {
                        if (GemAbilityRules.on(GemAbilityRules.GemAbility.ASTRA_DAGGERS, 425337338)) {
                            long var114 = System.currentTimeMillis();
                            Map var69 = this.astra.cd2;
                            Long var116 = (Long)var69.get(var2);
                            if (var116 != null) {
                                long var74 = var116;
                                if (var74 > var114) {
                                    long var96 = var116;
                                    Component var102 = Text.legacy(
                                        "&5\ud83d\udd2e &cYour §7Daggers &cskill is on cooldown for &5" + readable(var96 - var114, 1720866896), 373192400
                                    );
                                    var1.sendMessage(var102);
                                    return;
                                }
                            }

                            this.activate(var1, 1496914380);
                        }
                    }
                }
            }
        }
    }

    private void activate(Player var1, int var2) {
        final UUID var12 = var1.getUniqueId();
        this.applyCooldown(var1, 1565333689);
        AstraDaggersListener.DaggerSalvo var21 = new AstraDaggersListener.DaggerSalvo();
        final AstraDaggersListener.DaggerSalvo var13 = var21;
        Map var23 = this.salvos;
        var23.put(var12, var21);

        for (int var14 = 0; var14 < 5; var14++) {
            World var28 = var1.getWorld();
            Location var55 = var1.getLocation();
            Consumer<ArmorStand> var83 = var1x -> {
                var1x.setPersistent(false);
                var1x.setSmall(true);
                var1x.setInvisible(true);
                var1x.setMarker(false);
                var1x.setBasePlate(false);
                var1x.setGravity(false);
                var1x.setInvulnerable(true);
                PersistentDataContainer var13x = var1x.getPersistentDataContainer();
                NamespacedKey var24 = KEY_DAGGER;
                PersistentDataType var4 = PersistentDataType.STRING;
                String var26 = var1.getName();
                var13x.set(var24, var4, var26);
                if (var1x.getEquipment() != null) {
                    EntityEquipment var17 = var1x.getEquipment();
                    ItemStack var25 = daggerHead(763374029);
                    var17.setHelmet(var25);
                }
            };
            ArmorStand var15 = (ArmorStand)var28.spawn(var55, ArmorStand.class, var83);
            UUID[] var32 = var13.daggers;
            UUID var71 = var15.getUniqueId();
            var32[var14] = var71;
            boolean[] var34 = var13.launched;
            var34[var14] = false;
        }

        var13.shootdagger = 5;
        Component var60 = Text.legacy("&fDaggers", 373192400);
        Color var84 = Color.BLUE;
        Overlay var7 = Overlay.PROGRESS;
        BossBar var61 = BossBar.bossBar(var60, 1.0F, var84, var7);
        var13.bar = var61;
        BossBar var63 = var13.bar;
        var1.showBossBar(var63);
        String var38 = "astra";
        String var64 = "daggers-salvo";
        final long var89 = (long)(GemTuning.duration(var38, var64, 24.5, 812730832) * 20.0);
        String var45 = "astra";
        String var65 = "dagger-speed";
        final double var16 = GemTuning.power(var45, var65, 0.825, 1503850832);
        BukkitRunnable var48 = new BukkitRunnable() {
            int tick = 0;

            public void run() {
                Player var7 = Bukkit.getPlayer(var12);
                if (var7 == null) {
                    Map var87 = AstraDaggersListener.this.salvos;
                    UUID var117 = var12;
                    var87.remove(var117);
                    AstraDaggersListener.removeStands(var13, 1816758797);
                    this.cancel();
                } else if (var7.isDead()) {
                    AstraDaggersListener var83 = AstraDaggersListener.this;
                    var83.endSalvo(var7, 1731684030);
                    this.cancel();
                } else {
                    int var93 = this.tick + 1;
                    this.tick = var93;
                    long var22 = (long)this.tick;
                    long var119 = var89;
                    if (var22 >= var119) {
                        AstraDaggersListener var80 = AstraDaggersListener.this;
                        var80.endSalvo(var7, 1731684030);
                        this.cancel();
                    } else {
                        if (var13.bar != null) {
                            BossBar var30 = var13.bar;
                            float var131 = (float)this.tick;
                            float var139 = (float)var89;
                            float var132 = var131 / var139;
                            float var122 = 1.0F - var132;
                            float var95 = Math.max(0.0F, var122);
                            var30.progress(var95);
                        }

                        for (int var8 = 0; var8 < 5; var8++) {
                            UUID[] var36 = var13.daggers;
                            if (var36[var8] != null) {
                                UUID[] var40 = var13.daggers;
                                Entity var9 = Bukkit.getEntity(var40[var8]);
                                if (var9 instanceof ArmorStand) {
                                    ArmorStand var10 = (ArmorStand)var9;
                                    boolean[] var49 = var13.launched;
                                    if (var49[var8]) {
                                        Location var147 = var10.getLocation();
                                        Vector var12x = var147.getDirection().clone();
                                        if (var12x.lengthSquared() > 0.0) {
                                            Vector var67 = var12x.normalize();
                                            double var106 = var16;
                                            var67.multiply(var106);
                                            AstraDaggersListener var70 = AstraDaggersListener.this;
                                            AstraDaggersListener.DaggerSalvo var143 = var13;
                                            Location var144 = var147.clone();
                                            if (var70.traceHit(var7, var10, var8, var143, var144, var12x, 979605135)) {
                                                continue;
                                            }

                                            Location var110 = var147.add(var12x);
                                            var10.teleport(var110);
                                        }

                                        Location var13x = var10.getLocation().add(0.0, 1.0, 0.0);
                                        World var78 = var10.getWorld();
                                        AstraDaggersListener.trailSparks(var78, var13x, var12x, 896202495);
                                    } else {
                                        Vector var11 = AstraDaggersListener.orbitOffsetForIndex(var7, var8, 851299155);
                                        Location var102 = var7.getLocation();
                                        Location var103 = var102.add(var11);
                                        var10.teleport(var103);
                                        AstraDaggersListener var56 = AstraDaggersListener.this;
                                        AstraDaggersListener.DaggerSalvo var141 = var13;
                                        var56.checkHits(var7, var10, var8, var141, 326610121);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
        BlissPlugin var67 = this.plugin;
        var48.runTaskTimer(var67, 1L, 1L);
        var13.task = var48;
    }

    private static void trailSparks(World var0, Location var1, Vector var2, int var3) {
        if (var0 != null && !(var2.lengthSquared() <= 0.0)) {
            Vector var25 = var2.clone().normalize().multiply(-1);
            ThreadLocalRandom var26 = ThreadLocalRandom.current();

            for (int var27 = 0; var27 < 2; var27++) {
                Particle var43 = Particle.FIREWORK;
                double var52 = var25.getX();
                double var66 = var26.nextDouble(-0.35, 0.35);
                double var54 = var52 + var66;
                double var69 = var25.getY();
                double var86 = var26.nextDouble(-0.35, 0.35);
                double var71 = var69 + var86;
                double var89 = var25.getZ();
                double var107 = var26.nextDouble(-0.35, 0.35);
                double var91 = var89 + var107;
                Particles.spawn(var0, var43, var1, 0, var54, var71, var91, 0.09, null, true, 1748031581);
            }

            Particle var42 = Particle.END_ROD;
            double var47 = var25.getX();
            double var58 = var26.nextDouble(-0.35, 0.35) * 0.5;
            double var49 = var47 + var58;
            double var61 = var25.getY();
            double var78 = var26.nextDouble(-0.35, 0.35) * 0.5;
            double var63 = var61 + var78;
            double var81 = var25.getZ();
            double var102 = var26.nextDouble(-0.35, 0.35) * 0.5;
            double var83 = var81 + var102;
            Particles.spawn(var0, var42, var1, 0, var49, var63, var83, 0.045, null, true, 1748031581);
        }
    }

    private int nextThrowableIndex(AstraDaggersListener.DaggerSalvo var1, int var2) {
        for (byte var5 = 4; var5 >= 0; var5 += -1) {
            UUID[] var9 = var1.daggers;
            if (var9[var5] != null) {
                boolean[] var12 = var1.launched;
                if (!var12[var5]) {
                    UUID[] var15 = var1.daggers;
                    if (Bukkit.getEntity(var15[var5]) instanceof ArmorStand) {
                        return var5;
                    }
                }
            }
        }

        return -1;
    }

    private void throwOne(Player var1, AstraDaggersListener.DaggerSalvo var2, int var3, int var4) {
        UUID[] var18 = var2.daggers;
        if (Bukkit.getEntity(var18[var3]) instanceof ArmorStand var15) {
            Vector var16 = var1.getEyeLocation().getDirection().clone();
            Location var30 = var1.getEyeLocation();
            Vector var53 = var16.clone().multiply(1.5);
            Location var17 = var30.add(var53);
            var17.setDirection(var16);
            var17.subtract(0.0, 1.0, 0.0);
            var15.teleport(var17);
            boolean[] var39 = var2.launched;
            var39[var3] = true;
            World var41 = var1.getWorld();
            Location var60 = var1.getLocation();
            String var68 = "bliss:dagger_throw";
            SoundCategory var70 = SoundCategory.PLAYERS;
            var41.playSound(var60, var68, var70, 1.0F, 1.0F);
        }

        if (var2.shootdagger > 0) {
            int var62 = var2.shootdagger - 1;
            var2.shootdagger = var62;
        }

        if (this.nextThrowableIndex(var2, 2125735082) < 0 && var2.bar != null) {
            BossBar var65 = var2.bar;
            var1.hideBossBar(var65);
            var2.bar = null;
        }
    }

    private boolean traceHit(Player var1, ArmorStand var2, int var3, AstraDaggersListener.DaggerSalvo var4, Location var5, Vector var6, int var7) {
        double var20 = var6.length();
        if (var20 <= 0.0) {
            return false;
        } else {
            Location var22 = var5.clone().add(0.0, 1.0, 0.0);
            World var36 = var22.getWorld();
            Vector var52 = var6.clone().normalize();
            Predicate var60 = var2x -> {
                if (var2x instanceof LivingEntity var7x && !var7x.equals(var1) && !(var2x instanceof ArmorStand)) {
                    if (var7x instanceof Player var6x && this.trust != null) {
                        TrustStore var22x = this.trust;
                        UUID var29 = var1.getUniqueId();
                        UUID var32 = var6x.getUniqueId();
                        if (var22x.isTrusted(var29, var32, 1736364606)) {
                            return false;
                        }
                    }

                    Set var25 = this.hitCooldown;
                    UUID var31 = var7x.getUniqueId();
                    if (!var25.contains(var31)) {
                        return true;
                    }
                }

                return false;
            };
            RayTraceResult var23 = var36.rayTraceEntities(var22, var52, var20, 0.3, var60);
            if (var23 != null && var23.getHitEntity() instanceof LivingEntity var24) {
                this.applyDaggerHit(var1, var24, var2, var3, var4, 1596390238);
                return true;
            } else {
                return false;
            }
        }
    }

    private void checkHits(Player var1, ArmorStand var2, int var3, AstraDaggersListener.DaggerSalvo var4, int var5) {
        Location var16 = var2.getLocation();
        String var24 = "astra";
        String var7 = "dagger-hit";
        double var17 = GemTuning.radius(var24, var7, 1.5, 1058601812);
        World var28 = var2.getWorld();

        for (Entity var20 : var28.getNearbyEntities(var16, var17, var17, var17)) {
            if (var20 instanceof LivingEntity) {
                LivingEntity var21 = (LivingEntity)var20;
                if (!var21.equals(var1) && !(var20 instanceof ArmorStand)) {
                    if (var21 instanceof Player) {
                        Player var22 = (Player)var21;
                        if (this.trust != null) {
                            TrustStore var51 = this.trust;
                            UUID var60 = var1.getUniqueId();
                            UUID var67 = var22.getUniqueId();
                            if (var51.isTrusted(var60, var67, 1736364606)) {
                                continue;
                            }
                        }
                    }

                    Set var54 = this.hitCooldown;
                    UUID var62 = var21.getUniqueId();
                    if (!var54.contains(var62)) {
                        this.applyDaggerHit(var1, var21, var2, var3, var4, 1596390238);
                        break;
                    }
                }
            }
        }
    }

    private void applyDaggerHit(Player var1, LivingEntity var2, ArmorStand var3, int var4, AstraDaggersListener.DaggerSalvo var5, int var6) {
        UUID var22 = var2.getUniqueId();
        Set var27 = this.hitCooldown;
        var27.add(var22);
        if (GemAbilityRules.on(GemAbilityRules.GemAbility.ASTRA_DAGGERS, 425337338)) {
            GemAbilityRules.GemAbility var9 = GemAbilityRules.GemAbility.ASTRA_DAGGERS;
            String var10 = "astra";
            String var11 = "dimensional-daggers";
            double var91 = GemTuning.damage(var10, var11, 8.64, 1172948035);
            double var76 = GemAbilityRules.scaled(var9, var91, 1684677315);
            ConsistentDamage.dealFullArmorAware(var1, var2, var76, 1323155734);
        }

        if (var2 instanceof Player) {
            TagStore var35 = this.tags;
            String var78 = "astra";
            String var93 = "dagger-tag";
            int var81 = (int)GemTuning.duration(var78, var93, 10.0, 812730832);
            var35.addTagSeconds(var22, var81, 1101624808);
        }

        BukkitScheduler var36 = Bukkit.getScheduler();
        BlissPlugin var64 = this.plugin;
        Runnable var83 = () -> {
            Set var4x = this.hitCooldown;
            var4x.remove(var22);
        };
        var36.runTaskLater(var64, var83, 5L);
        if (var2 instanceof Player) {
            Set var41 = var5.credited;
            if (var41.add(var22)) {
                AchievementStore var23 = this.plugin.advancements(1975441698);
                AchievementStore.Advancement var24 = AchievementStore.Advancement.PIERCING_PRECISION;
                var23.resetTargetCountersExcept(var1, var24, var22, 499157271);
                var23.targetProgress(var1, var24, var22, 1, 794164211);
            }
        }

        Location var50 = var2.getLocation();
        double var102 = var2.getHeight() * 0.5;
        Location var129 = var50.add(0.0, var102, 0.0);
        World var53 = var2.getWorld();
        Particle var70 = Particle.FIREWORK;
        Particles.spawn(var53, var70, var129, 12, 0.2, 0.2, 0.2, 0.08, null, true, 1748031581);
        World var55 = var2.getWorld();
        Particle var71 = Particle.END_ROD;
        Particles.spawn(var55, var71, var129, 8, 0.2, 0.2, 0.2, 0.05, null, true, 1748031581);
        Location var74 = var2.getLocation().add(0.0, 100.0, 0.0);
        var3.teleport(var74);
        var3.remove();
        UUID[] var60 = var5.daggers;
        var60[var4] = null;
    }

    private void endSalvo(Player var1, int var2) {
        UUID var5 = var1.getUniqueId();
        Map var9 = this.salvos;
        AstraDaggersListener.DaggerSalvo var6 = (AstraDaggersListener.DaggerSalvo)var9.remove(var5);
        if (var6 != null) {
            removeStands(var6, 1816758797);
            if (var6.bar != null && var1.isOnline()) {
                BossBar var20 = var6.bar;
                var1.hideBossBar(var20);
            }
        }
    }

    private static void removeStands(AstraDaggersListener.DaggerSalvo var0, int var1) {
        for (UUID var7 : var0.daggers) {
            if (var7 != null) {
                Entity var8 = Bukkit.getEntity(var7);
                if (var8 != null && var8.isValid()) {
                    var8.remove();
                }
            }
        }
    }

    public void stopAll(int var1) {
        for (Entry var5 : this.salvos.entrySet()) {
            AstraDaggersListener.DaggerSalvo var6 = (AstraDaggersListener.DaggerSalvo)var5.getValue();
            if (var6.task != null && !var6.task.isCancelled()) {
                var6.task.cancel();
            }

            removeStands(var6, 1816758797);
            Player var7 = Bukkit.getPlayer((UUID)var5.getKey());
            if (var7 != null && var6.bar != null) {
                BossBar var37 = var6.bar;
                var7.hideBossBar(var37);
            }
        }

        this.salvos.clear();
    }

    private void applyCooldown(Player var1, int var2) {
        PlayerDataStore var16 = this.players;
        UUID var46 = var1.getUniqueId();
        PlayerData var13 = var16.get(var46, 1264010596);
        GlobalState var19 = this.globalState;
        boolean var14 = var19.holdsDragonEgg(var1, 715974514);
        int var15 = var13.energy(1160853318);
        long var62;
        if (var15 > 5) {
            long var25;
            if (var14) {
                var25 = 45000L;
            } else {
                var25 = 90000L;
            }

            var62 = var25;
        } else if (var15 == 5) {
            long var44;
            if (var14) {
                var44 = 50000L;
            } else {
                var44 = 105000L;
            }

            var62 = var44;
        } else if (var15 == 4) {
            long var42;
            if (var14) {
                var42 = 60000L;
            } else {
                var42 = 120000L;
            }

            var62 = var42;
        } else if (var15 == 3) {
            long var40;
            if (var14) {
                var40 = 75000L;
            } else {
                var40 = 150000L;
            }

            var62 = var40;
        } else {
            long var38;
            if (var14) {
                var38 = 90000L;
            } else {
                var38 = 180000L;
            }

            var62 = var38;
        }

        Map var29 = this.astra.cd2;
        UUID var50 = var1.getUniqueId();
        long var5 = System.currentTimeMillis();
        String var7 = "astra";
        String var8 = "dimensional-daggers";
        long var57 = GemTuning.cd(var7, var8, 90.0, var62, 1621983041);
        long var59 = EnergyScale.cd(var57, var15, 907518618);
        Long var56 = var5 + var59;
        var29.put(var50, var56);
    }

    private static ItemStack daggerHead(int var0) {
        Material var2 = Material.NAUTILUS_SHELL;
        ItemStack var1 = new ItemStack(var2);
        ItemMeta var4 = var1.getItemMeta();
        if (var4 != null) {
            Integer var13 = 230;
            var4.setCustomModelData(var13);
            var1.setItemMeta(var4);
        }

        return var1;
    }

    private static Vector orbitOffsetForIndex(Player var0, int var1, int var2) {
        Vector var9 = var0.getLocation().getDirection().setY(0).normalize();
        double var48 = -var9.getZ();
        double var54 = var9.getX();
        Vector var15 = new Vector(var48, 0.0, var54);

        return switch (var1) {
            case 0 -> {
                Vector var26 = var9.clone().multiply(0.8);
                Vector var43 = var15.clone().multiply(-1.0);
                yield var26.add(var43);
            }
            case 1 -> {
                Vector var19 = var9.clone().multiply(0.8);
                Vector var34 = var15.clone().multiply(1.0);
                yield var19.add(var34);
            }
            case 2 -> var15.clone().multiply(-1.2);
            case 3 -> var15.clone().multiply(1.2);
            default -> var9.clone().multiply(1.2);
        };
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

    private static final class DaggerSalvo {
        final UUID[] daggers;
        final boolean[] launched;
        final Set<UUID> credited;
        int shootdagger;
        BossBar bar;
        BukkitRunnable task;

        private DaggerSalvo() {
            UUID[] var6 = new UUID[5];
            this.daggers = var6;
            boolean[] var8 = new boolean[5];
            this.launched = var8;
            KeySetView var9 = ConcurrentHashMap.newKeySet();
            this.credited = var9;
        }
    }
}
