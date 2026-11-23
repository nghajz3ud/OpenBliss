package com.bliss.mythics.blood;

import com.bliss.core.GemTuning;
import com.bliss.core.Particles;
import com.bliss.core.TrustStore;
import com.bliss.mythics.MythicItems;
import com.bliss.mythics.util.MythicGate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

public final class MythicBloodListener implements Listener {
    public static final int BLOODSAW_MAX_CHARGES = 2;
    static final long BLOODSAW_RECHARGE_MS = 5000L;
    static final long RITUAL_COOLDOWN_MS = 45000L;
    static final long COMBO_WINDOW_MS = 1200L;
    static final long FOUNTAIN_DURATION_MS = 10000L;
    static final long FOUNTAIN_COOLDOWN_MS = 40000L;
    static final double FOUNTAIN_RADIUS = 6.0;
    public static final long LINK_DURATION_MS = 12000L;
    private static final double CRIT_CHANCE = 0.75;
    private static final double CRIT_MULT = 1.9;
    private static final double VULN_CHANCE = 0.5;
    private static final double VULN_MULT = 2.0;
    private static final double BLOOD_HARDENING_CAP = 13.0;
    private final Plugin plugin;
    private final MythicBloodState state;
    private final TrustStore trust;
    private static final double FOUNTAIN_SEEK_RANGE = 22.0;

    public MythicBloodListener(Plugin var1, MythicBloodState var2, TrustStore var3, int var4) {
        this.plugin = var1;
        this.state = var2;
        this.trust = var3;
    }

    @EventHandler(
        ignoreCancelled = false
    )
    public void onInteract(PlayerInteractEvent var1) {
        EquipmentSlot var6 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var6 == var3) {
            Player var4 = var1.getPlayer();
            if (MythicItems.isMythicBloodGem(var4.getInventory().getItemInMainHand(), 824514243)) {
                if (!MythicGate.disabledInSpawn(var4, 821632949)) {
                    Action var5 = var1.getAction();
                    Action var26 = Action.RIGHT_CLICK_AIR;
                    if (var5 != var26) {
                        Action var27 = Action.RIGHT_CLICK_BLOCK;
                        if (var5 != var27) {
                            Action var28 = Action.LEFT_CLICK_AIR;
                            if (var5 == var28) {
                                this.tryBloodsaw(var4, 1449342487);
                            } else {
                                Action var30 = Action.LEFT_CLICK_BLOCK;
                                if (var5 == var30) {
                                    var1.setCancelled(true);
                                    this.tryBloodsaw(var4, 1449342487);
                                }
                            }

                            return;
                        }
                    }

                    var1.setCancelled(true);
                    this.tryStartRitual(var4, 204782214);
                }
            }
        }
    }

    private void tryStartRitual(Player var1, int var2) {
        UUID var15 = var1.getUniqueId();
        Set var22 = this.state.ritualActive;
        if (!var22.contains(var15)) {
            long var16 = System.currentTimeMillis();
            Map var28 = this.state.ritualReadyAt;
            Long var18 = (Long)var28.get(var15);
            if (var18 != null) {
                long var33 = var18;
                if (var33 > var16) {
                    Location var48 = var1.getLocation();
                    Sound var52 = Sound.BLOCK_NOTE_BLOCK_BASS;
                    var1.playSound(var48, var52, 0.7F, 0.6F);
                    return;
                }
            }

            Map var39 = this.state.ritualReadyAt;
            String var63 = "heretic";
            String var9 = "blood-ritual";
            long var64 = GemTuning.mythicCd(var63, var9, 45.0, 45000L, var1, 1380510852);
            Long var57 = var16 + var64;
            var39.put(var15, var57);
            if (BloodRitualTask.inWeb(var1, 544007400)) {
                Plugin var45 = this.plugin;
                TrustStore var51 = this.trust;
                BloodRitualTask.groundBurst(var45, var51, var1, 1849399630);
            } else {
                Plugin var59 = this.plugin;
                MythicBloodState var62 = this.state;
                TrustStore var67 = this.trust;
                BloodRitualTask var43 = new BloodRitualTask(var59, var62, var67, var1, 72320984);
                var43.begin(853300450);
            }
        }
    }

    private void tryBloodsaw(Player var1, int var2) {
        UUID var15 = var1.getUniqueId();
        long var16 = System.currentTimeMillis();
        MythicBloodState var24 = this.state;
        if (var24.sawsAvailable(var15, 2, var16, 1478640902) <= 0) {
            Location var66 = var1.getLocation();
            Sound var77 = Sound.BLOCK_NOTE_BLOCK_BASS;
            var1.playSound(var66, var77, 0.6F, 0.7F);
        } else {
            this.fireSaw(var1, 1753764076);
            MythicBloodState var29 = this.state;
            String var100 = "heretic";
            String var9 = "bloodsaw";
            long var101 = GemTuning.mythicCd(var100, var9, 5.0, 5000L, var1, 1380510852);
            var29.useSaw(var15, var16, var101, 2088175661);
            Map var32 = this.state.lastCastAt;
            Long var18 = (Long)var32.get(var15);
            if (var18 != null) {
                long var81 = var18;
                if (var16 - var81 <= 1200L) {
                    Map var43 = this.state.lastCastAt;
                    var43.remove(var15);
                    Map var47 = this.state.fountainReadyAt;
                    Long var19 = (Long)var47.get(var15);
                    if (var19 != null) {
                        long var52 = var19;
                        if (var52 > var16) {
                            Location var73 = var1.getLocation();
                            Sound var87 = Sound.BLOCK_NOTE_BLOCK_BASS;
                            var1.playSound(var73, var87, 0.6F, 0.5F);
                            return;
                        }
                    }

                    this.spawnFountain(var1, var16, 974116140);
                    Map var59 = this.state.fountainReadyAt;
                    String var104 = "heretic";
                    String var107 = "bloodlink";
                    long var105 = GemTuning.mythicCd(var104, var107, 40.0, 40000L, var1, 1380510852);
                    Long var94 = var16 + var105;
                    var59.put(var15, var94);
                    return;
                }
            }

            Map var63 = this.state.lastCastAt;
            Long var97 = var16;
            var63.put(var15, var97);
        }
    }

    private void fireSaw(Player var1, int var2) {
        World var13 = var1.getWorld();
        Location var15 = var1.getLocation();
        Sound var5 = Sound.ITEM_MACE_SMASH_AIR;
        var13.playSound(var15, var5, 0.8F, 0.9F);
        Plugin var17 = this.plugin;
        TrustStore var19 = this.trust;
        MythicBloodState var21 = this.state;
        BloodsawProjectile var14 = new BloodsawProjectile(var17, var19, var21, var1, 0.0, 0.0, 233916693);
        var14.launch(108813757);
    }

    private void spawnFountain(Player var1, long var2, int var4) {
        Location var16 = this.bestFountainCenter(var1, 1729321295);
        UUID var29 = var1.getUniqueId();
        long var39 = var2 + 10000L;
        MythicBloodState.Fountain var19 = new MythicBloodState.Fountain(var29, var16, 6.0, var39, 1458035542);
        List var22 = this.state.fountains;
        var22.add(var19);
        Plugin var31 = this.plugin;
        MythicBloodState var34 = this.state;
        TrustStore var37 = this.trust;
        BloodlinkGroupTask var24 = new BloodlinkGroupTask(var31, var34, var37, var19, 271014368);
        var24.start(1410081573);
        World var26 = var16.getWorld();
        Sound var32 = Sound.ENTITY_WARDEN_HEARTBEAT;
        var26.playSound(var16, var32, 1.0F, 0.7F);
    }

    private Location bestFountainCenter(Player var1, int var2) {
        ArrayList<Player> var3 = new ArrayList<>();
        ArrayList<Player> var5 = var3;

        for (Player var7 : var1.getWorld().getPlayers()) {
            if (!var7.equals(var1)) {
                GameMode var28 = var7.getGameMode();
                GameMode var85 = GameMode.SPECTATOR;
                if (var28 != var85) {
                    GameMode var30 = var7.getGameMode();
                    GameMode var86 = GameMode.CREATIVE;
                    if (var30 != var86) {
                        TrustStore var32 = this.trust;
                        UUID var88 = var1.getUniqueId();
                        UUID var98 = var7.getUniqueId();
                        if (!var32.isTrusted(var88, var98, 1736364606)) {
                            Location var35 = var7.getLocation();
                            Location var90 = var1.getLocation();
                            if (var35.distanceSquared(var90) <= 484.0) {
                                var5.add(var7);
                            }
                        }
                    }
                }
            }
        }

        if (var5.isEmpty()) {
            return var1.getLocation();
        } else {
            Location var103 = null;
            int var104 = 0;
            double var8 = Double.MAX_VALUE;
            Iterator var10 = var5.iterator();

            while (var10.hasNext()) {
                Location var12 = ((Player)var10.next()).getLocation();
                int var13 = 0;

                for (Player var15 : var5) {
                    Location var97 = var15.getLocation();
                    if (withinZone(var12, var97, 1359549759)) {
                        var13++;
                    }
                }

                Location var93 = var1.getLocation();
                double var105 = var12.distanceSquared(var93);
                if (var13 > var104 || var13 == var104 && var105 < var8) {
                    var104 = var13;
                    var8 = var105;
                    var103 = var12;
                }
            }

            Location var83;
            if (var103 != null && var104 > 0) {
                var83 = var103;
            } else {
                var83 = var1.getLocation();
            }

            return var83;
        }
    }

    private static boolean withinZone(Location var0, Location var1, int var2) {
        if (var0.getWorld() != null) {
            World var16 = var0.getWorld();
            World var48 = var1.getWorld();
            if (var16.equals(var48)) {
                double var19 = var0.getX();
                double var49 = var1.getX();
                double var8 = var19 - var49;
                double var24 = var0.getZ();
                double var52 = var1.getZ();
                double var10 = var24 - var52;
                double var29 = var1.getY();
                double var55 = var0.getY();
                double var12 = var29 - var55;
                double var35 = var8 * var8;
                double var61 = var10 * var10;
                boolean var46;
                if (var35 + var61 <= 36.0 && var12 >= -3.0 && var12 <= 18.0) {
                    var46 = true;
                } else {
                    var46 = false;
                }

                return var46;
            }
        }

        return false;
    }

    @EventHandler(
        priority = EventPriority.HIGH,
        ignoreCancelled = true
    )
    public void onDamage(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Player var17) {
            MythicBloodState var28 = this.state;
            UUID var100 = var17.getUniqueId();
            if (var28.ritualImmune(var100, 1139532672)) {
                var1.setCancelled(true);
                return;
            }
        }

        if (var1 instanceof EntityDamageByEntityEvent var159) {
            DamageCause var36 = var159.getCause();
            DamageCause var102 = DamageCause.ENTITY_ATTACK;
            if (var36 == var102 && var159.getDamager() instanceof Player var161) {
                boolean var99;
                label52: {
                    if (var159.getEntity() instanceof Player var20) {
                        TrustStore var50 = this.trust;
                        UUID var104 = var161.getUniqueId();
                        UUID var126 = var20.getUniqueId();
                        if (var50.isTrusted(var104, var126, 1736364606)) {
                            var99 = true;
                            break label52;
                        }
                    }

                    var99 = false;
                }

                if (holdsBloodGem(var161, 495418112)
                    && !MythicGate.disabledInSpawn(var161, 821632949)
                    && var159.getEntity() instanceof LivingEntity var166
                    && !var99
                    && ThreadLocalRandom.current().nextDouble() < 0.75) {
                    double var106 = var159.getDamage();
                    String var5 = "heretic";
                    String var6 = "auto-crit";
                    double var140 = GemTuning.mythicDamage(var5, var6, 1.9, 478197159);
                    double var108 = var106 * var140;
                    var159.setDamage(var108);
                    World var69 = var166.getWorld();
                    Location var111 = var166.getLocation();
                    Sound var129 = Sound.ENTITY_PLAYER_ATTACK_CRIT;
                    var69.playSound(var111, var129, 1.0F, 1.0F);
                    World var71 = var166.getWorld();
                    Particle var112 = Particle.CRIT;
                    Location var132 = var166.getLocation().add(0.0, 1.0, 0.0);
                    Particles.spawn(var71, var112, var132, 12, 0.3, 0.4, 0.3, 0.2, 1986132876);
                }
            }
        }

        if (var1 instanceof EntityDamageByEntityEvent var160) {
            DamageCause var77 = var160.getCause();
            DamageCause var113 = DamageCause.ENTITY_ATTACK;
            if (var77 == var113 && var160.getEntity() instanceof Player var162) {
                MythicBloodState var85 = this.state;
                Location var115 = var162.getLocation();
                long var133 = System.currentTimeMillis();
                MythicBloodState.Fountain var165 = var85.fountainAt(var115, var133, 675223021);
                if (var165 != null) {
                    TrustStore var89 = this.trust;
                    UUID var117 = var165.casterId;
                    UUID var136 = var162.getUniqueId();
                    if (!var89.isTrusted(var117, var136, 1736364606) && ThreadLocalRandom.current().nextDouble() < 0.5) {
                        double var119 = var160.getDamage();
                        String var146 = "heretic";
                        String var153 = "bloodlink";
                        double var147 = GemTuning.mythicDamage(var146, var153, 2.0, 478197159);
                        double var121 = var119 * var147;
                        var160.setDamage(var121);
                        World var97 = var162.getWorld();
                        Location var124 = var162.getLocation();
                        Sound var139 = Sound.ENTITY_WARDEN_ATTACK_IMPACT;
                        var97.playSound(var124, var139, 0.5F, 0.8F);
                    }
                }
            }
        }

        this.clampBloodHardening(var1, 1972437269);
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = true
    )
    public void onBloodHardeningFinal(EntityDamageEvent var1) {
        this.clampBloodHardening(var1, 1972437269);
        if (var1.getEntity() instanceof Player var5) {
            this.reflectBloodlink(var5, var1, 699821743);
        }
    }

    private void clampBloodHardening(EntityDamageEvent var1, int var2) {
        if (var1.getEntity() instanceof Player var11) {
            if (holdsBloodGem(var11, 495418112)) {
                if (!MythicGate.disabledInSpawn(var11, 821632949)) {
                    double var48 = var1.getFinalDamage();
                    if (!(var48 <= 0.0)) {
                        String var30 = "heretic";
                        String var4 = "blood-hardening";
                        double var13 = GemTuning.mythicDamage(var30, var4, 13.0, 478197159);
                        if (var48 > var13) {
                            double var38 = var1.getDamage();
                            double var46 = var13 / var48;
                            double var40 = var38 * var46;
                            var1.setDamage(var40);
                        }
                    }
                }
            }
        }
    }

    private static boolean holdsBloodGem(Player var0, int var1) {
        boolean var10;
        if (!MythicItems.isMythicBloodGem(var0.getInventory().getItemInMainHand(), 824514243)
            && !MythicItems.isMythicBloodGem(var0.getInventory().getItemInOffHand(), 824514243)) {
            var10 = false;
        } else {
            var10 = true;
        }

        return var10;
    }

    private void reflectBloodlink(Player var1, EntityDamageEvent var2, int var3) {
        UUID var11 = var1.getUniqueId();
        Set var25 = this.state.reflecting;
        if (!var25.contains(var11)) {
            long var12 = System.currentTimeMillis();
            MythicBloodState var30 = this.state;
            MythicBloodState.LinkGroup var14 = var30.activeLink(var11, var12, 44371760);
            if (var14 != null) {
                double var34 = var2.getFinalDamage();
                String var92 = "heretic";
                String var8 = "bloodlinking";
                double var93 = GemTuning.mythicDamage(var92, var8, 1.0, 478197159);
                double var15 = var34 * var93;
                if (!(var15 <= 0.0)) {
                    DamageSource var17 = DamageSource.builder(DamageType.MAGIC).build();

                    for (UUID var19 : var14.members) {
                        if (!var19.equals(var11)) {
                            TrustStore var55 = this.trust;
                            if (!var55.isTrusted(var11, var19, 1736364606)) {
                                TrustStore var58 = this.trust;
                                if (!var58.isTrusted(var19, var11, 1736364606)) {
                                    Server var62 = this.plugin.getServer();
                                    Player var20 = var62.getPlayer(var19);
                                    if (var20 != null && !var20.isDead()) {
                                        Set var69 = this.state.reflecting;
                                        var69.add(var19);

                                        try {
                                            var20.damage(var15, var17);
                                        } finally {
                                            this.state.reflecting.remove(var19);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        BloodRitualTask.handleQuit(var1.getPlayer(), 1407882446);
        MythicBloodState var6 = this.state;
        UUID var8 = var1.getPlayer().getUniqueId();
        var6.clearTransient(var8, 1875235611);
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onToggleGlide(EntityToggleGlideEvent var1) {
        if (!var1.isGliding()) {
            if (var1.getEntity() instanceof Player var4) {
                Set var15 = this.state.ritualActive;
                UUID var18 = var4.getUniqueId();
                if (var15.contains(var18)) {
                    var1.setCancelled(true);
                }
            }
        }
    }
}
