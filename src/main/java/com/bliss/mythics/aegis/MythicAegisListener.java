package com.bliss.mythics.aegis;

import com.bliss.core.GemTuning;
import com.bliss.core.Particles;
import com.bliss.core.TrustStore;
import com.bliss.gems.ConsistentDamage;
import com.bliss.mythics.MythicItems;
import com.bliss.mythics.util.MythicGate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class MythicAegisListener implements Listener {
    public static final int MAX_CHARGES = 2;
    static final long CHARGE_RECHARGE_MS = 10000L;
    static final long DASH_CD_MS = 15000L;
    private static final long DASH_WINDOW_MS = 2000L;
    static final long ANGELS_CD_MS = 25000L;
    private static final long SWING_GATE_MS = 350L;
    private static final double FALL_REDUCE = 0.3333333333333333;
    private static final double PARRY_HEAL = 5.0;
    private static final double PARRY_DAMAGE_SCALE = 1.56;
    private static final double PARRY_DAMAGE_MIN = 15.6;
    private static final double PARRY_DAMAGE_MAX = 31.2;
    private static final int SHIELD_LOCK = 100;
    private final Plugin plugin;
    private final MythicAegisState state;
    private final TrustStore trust;
    private final Map<UUID, Integer> lastInteractTick;
    private static final long COMBO_MS = 2000L;
    private static final double POLE_RANGE = 60.0;
    private final Set<UUID> reflecting;
    private static final int COBWEB_CLEAR_RADIUS = 4;
    private static final boolean FLASH_NEEDS_COLOR;

    public MythicAegisListener(Plugin var1, MythicAegisState var2, TrustStore var3, int var4) {
        ConcurrentHashMap var6 = new ConcurrentHashMap();
        this.lastInteractTick = var6;
        KeySetView var12 = ConcurrentHashMap.newKeySet();
        this.reflecting = var12;
        this.plugin = var1;
        this.state = var2;
        this.trust = var3;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent var1) {
        EquipmentSlot var9 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var9 == var3) {
            Player var5;
            ItemStack var6;
            boolean var32;
            label35: {
                var5 = var1.getPlayer();
                var6 = var5.getInventory().getItemInMainHand();
                Action var7 = var1.getAction();
                Action var33 = Action.RIGHT_CLICK_AIR;
                if (var7 != var33) {
                    Action var34 = Action.RIGHT_CLICK_BLOCK;
                    if (var7 != var34) {
                        var32 = false;
                        break label35;
                    }
                }

                var32 = true;
            }

            if (MythicItems.isMythicAegisGem(var6, 73386651)) {
                if (!MythicGate.disabledInSpawn(var5, 821632949)) {
                    if (var32) {
                        var1.setCancelled(true);
                        Map var26 = this.lastInteractTick;
                        UUID var37 = var5.getUniqueId();
                        Integer var40 = Bukkit.getCurrentTick();
                        var26.put(var37, var40);
                        if (var5.isSneaking()) {
                            this.tryAngelsGrasp(var5, 1402581381);
                        } else {
                            this.tryDash(var5, 1881849368);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSwing(PlayerAnimationEvent var1) {
        PlayerAnimationType var22 = var1.getAnimationType();
        PlayerAnimationType var3 = PlayerAnimationType.ARM_SWING;
        if (var22 == var3) {
            Player var14 = var1.getPlayer();
            if (MythicItems.isMythicAegisGem(var14.getInventory().getItemInMainHand(), 73386651)) {
                if (!MythicGate.disabledInSpawn(var14, 821632949)) {
                    UUID var15 = var14.getUniqueId();
                    Map var34 = this.lastInteractTick;
                    Integer var16 = (Integer)var34.get(var15);
                    if (var16 != null) {
                        int var39 = var16;
                        int var97 = Bukkit.getCurrentTick();
                        if (var39 == var97) {
                            return;
                        }
                    }

                    long var17 = System.currentTimeMillis();
                    Map var44 = this.state.lastSwingAt;
                    Long var19 = (Long)var44.get(var15);
                    if (var19 != null) {
                        long var111 = var19;
                        if (var17 - var111 < 350L) {
                            return;
                        }
                    }

                    Map var55 = this.state.lastSwingAt;
                    Long var117 = var17;
                    var55.put(var15, var117);
                    Map var59 = this.state.chainLockUntil;
                    Long var20 = (Long)var59.get(var15);
                    if (var20 != null) {
                        long var64 = var20;
                        if (var64 > var17) {
                            return;
                        }
                    }

                    MythicAegisState var68 = this.state;
                    if (var68.chainsAvailable(var15, 2, var17, 1870480623) <= 0) {
                        Location var110 = var14.getLocation();
                        Sound var132 = Sound.BLOCK_NOTE_BLOCK_BASS;
                        var14.playSound(var110, var132, 0.6F, 0.7F);
                    } else {
                        Set var72 = this.state.grappling;
                        if (!var72.contains(var15)) {
                            MythicAegisState var75 = this.state;
                            boolean var21 = var75.fullyCharged(var15, var17, 760459796);
                            Plugin var124 = this.plugin;
                            MythicAegisState var134 = this.state;
                            TrustStore var136 = this.trust;
                            PerforatorChain var77 = new PerforatorChain(var124, var134, var136, var14, 765771865);
                            var77.fire(714720128);
                            MythicAegisState var80 = this.state;
                            String var137 = "auratus";
                            String var141 = "hauling-strike";
                            long var138 = GemTuning.mythicCd(var137, var141, 10.0, 10000L, var14, 1380510852);
                            var80.useChain(var15, var17, var138, 2140983036);
                            Map var83 = this.state.lastChainThrowMs;
                            Long var129 = var17;
                            var83.put(var15, var129);
                            if (var21) {
                                Set var93 = this.state.chainThrowFull;
                                var93.add(var15);
                            } else {
                                Set var88 = this.state.chainThrowFull;
                                var88.remove(var15);
                            }

                            this.tryFlagPole(var14, var17, 500808869);
                        }
                    }
                }
            }
        }
    }

    private void tryFlagPole(Player var1, long var2, int var4) {
        if (!MythicAegisState.legacyAuratus(180703355)) {
            UUID var20 = var1.getUniqueId();
            MythicAegisState var29 = this.state;
            if (!var29.angelsActive(var20, 1811770379)) {
                Map var33 = this.state.lastParryCastMs;
                Long var21 = (Long)var33.get(var20);
                Map var38 = this.state.lastChainThrowMs;
                Long var22 = (Long)var38.get(var20);
                if (var21 != null && var22 != null) {
                    boolean var103;
                    long var44 = var21;
                    long var117 = var22;
                    label52:
                    if (var44 <= var117) {
                        Set var95 = this.state.parryCastFull;
                        if (var95.contains(var20)) {
                            long var98 = var22;
                            long var129 = var21;
                            if (var98 - var129 <= 2000L) {
                                var103 = true;
                                break label52;
                            }
                        }

                        var103 = false;
                    } else {
                        label47: {
                            Set var49 = this.state.chainThrowFull;
                            if (var49.contains(var20)) {
                                long var52 = var21;
                                long var120 = var22;
                                if (var52 - var120 <= 2000L) {
                                    var103 = true;
                                    break label47;
                                }
                            }

                            var103 = false;
                        }
                    }

                    if (var103) {
                        Map var60 = this.state.lastParryCastMs;
                        var60.remove(var20);
                        Set var64 = this.state.parryCastFull;
                        var64.remove(var20);
                        Map var68 = this.state.lastChainThrowMs;
                        var68.remove(var20);
                        Set var72 = this.state.chainThrowFull;
                        var72.remove(var20);
                        if (PerforatorChain.hookedVictim(var20, 1283753854) instanceof Player var24) {
                            Plugin var81 = this.plugin;
                            if (AngelsHang.flagPole(var81, var1, var24, 2067520302)) {
                                PerforatorChain.releaseHooked(var20, 704692586);
                                return;
                            }
                        }

                        TrustStore var133 = this.trust;
                        Player var150 = AngelsHang.pickTarget(var1, 60.0, var133, 1766358476);
                        if (var150 != null) {
                            Plugin var88 = this.plugin;
                            if (AngelsHang.flagPole(var88, var1, var150, 2067520302)) {
                                return;
                            }
                        }

                        PerforatorChain.denyRefund(var20, 185853702);
                        MythicAegisState var92 = this.state;
                        String var9 = "auratus";
                        String var10 = "hauling-strike";
                        long var134 = GemTuning.mythicCd(var9, var10, 10.0, 10000L, var1, 1380510852);
                        String var136 = "auratus";
                        String var13 = "venerated-perforators";
                        long var137 = GemTuning.mythicCd(var136, var13, 15.0, 15000L, var1, 1380510852);
                        String var141 = "auratus";
                        String var16 = "angels-grasp";
                        long var142 = GemTuning.mythicCd(var141, var16, 25.0, 25000L, var1, 1380510852);
                        var92.penalizeAll(var20, var2, var134, var137, var142, 1747609257);
                    }
                }
            }
        }
    }

    private void tryDash(Player var1, int var2) {
        UUID var15 = var1.getUniqueId();
        long var16 = System.currentTimeMillis();
        Map var24 = this.state.dashReadyAt;
        Long var18 = (Long)var24.get(var15);
        if (var18 != null) {
            long var29 = var18;
            if (var29 > var16) {
                return;
            }
        }

        Map var34 = this.state.lastParryCastMs;
        Long var71 = var16;
        var34.put(var15, var71);
        MythicAegisState var37 = this.state;
        if (var37.fullyCharged(var15, var16, 760459796)) {
            Set var59 = this.state.parryCastFull;
            var59.add(var15);
        } else {
            Set var41 = this.state.parryCastFull;
            var41.remove(var15);
        }

        this.tryFlagPole(var1, var16, 500808869);
        Map var46 = this.state.dashReadyAt;
        String var8 = "auratus";
        String var9 = "venerated-perforators";
        long var89 = GemTuning.mythicCd(var8, var9, 15.0, 15000L, var1, 1380510852);
        Long var80 = var16 + var89;
        var46.put(var15, var80);
        Map var50 = this.state.parryUntil;
        Long var85 = var16 + 2000L;
        var50.put(var15, var85);
        Set var54 = this.state.parrySucceeded;
        var54.remove(var15);
        Plugin var87 = this.plugin;
        MythicAegisState var88 = this.state;
        TrustStore var94 = this.trust;
        VeneratedDash var56 = new VeneratedDash(var87, var88, var94, var1, 8427935);
        var56.start(1949093884);
    }

    private void tryAngelsGrasp(Player var1, int var2) {
        UUID var15 = var1.getUniqueId();
        long var16 = System.currentTimeMillis();
        Map var24 = this.state.angelsReadyAt;
        Long var18 = (Long)var24.get(var15);
        if (var18 != null) {
            long var29 = var18;
            if (var29 > var16) {
                return;
            }
        }

        Map var34 = this.state.angelsReadyAt;
        String var8 = "auratus";
        String var9 = "angels-grasp";
        long var47 = GemTuning.mythicCd(var8, var9, 25.0, 25000L, var1, 1380510852);
        Long var43 = var16 + var47;
        var34.put(var15, var43);
        Plugin var45 = this.plugin;
        MythicAegisState var46 = this.state;
        TrustStore var50 = this.trust;
        AngelsGrasp var36 = new AngelsGrasp(var45, var46, var50, 327380507);
        var36.cast(var1, 316805247);
    }

    @EventHandler(
        priority = EventPriority.HIGH,
        ignoreCancelled = true
    )
    public void onDamage(EntityDamageEvent var1) {
        if (!MythicAegisState.legacyAuratus(180703355) && var1.getEntity() instanceof Player var9) {
            MythicAegisState var18 = this.state;
            UUID var48 = var9.getUniqueId();
            if (var18.parryActive(var48, 949722240) && !MythicGate.disabledInSpawn(var9, 821632949)) {
                this.echoingAegis(var9, var1, 757076741);
                return;
            }
        }

        DamageCause var24 = var1.getCause();
        DamageCause var50 = DamageCause.FALL;
        if (var24 == var50
            && var1.getEntity() instanceof Player var62
            && MythicItems.hasAegisInInventory(var62, 758703914)
            && !MythicGate.disabledInSpawn(var62, 821632949)) {
            double var52 = var1.getDamage();
            String var5 = "auratus";
            String var6 = "feathered-fall";
            double var60 = GemTuning.mythicDamage(var5, var6, 0.3333333333333333, 478197159);
            double var54 = var52 * var60;
            var1.setDamage(var54);
        } else {
            if (var1.getEntity() instanceof Player var63) {
                MythicAegisState var43 = this.state;
                UUID var57 = var63.getUniqueId();
                if (var43.parryActive(var57, 949722240) && !MythicGate.disabledInSpawn(var63, 821632949)) {
                    this.echoingAegis(var63, var1, 757076741);
                }
            }
        }
    }

    private void echoingAegis(Player var1, EntityDamageEvent var2, int var3) {
        boolean var15;
        double var18;
        Set var30 = this.state.parrySucceeded;
        UUID var120 = var1.getUniqueId();
        var15 = var30.add(var120);
        double var16 = var2.getDamage();
        var18 = var2.getFinalDamage();
        var2.setCancelled(true);
        label66:
        if (var2 instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent)var2).getDamager() instanceof LivingEntity var21 && !var21.isDead()) {
            if (var21 instanceof Player var22) {
                Set var56 = this.reflecting;
                UUID var123 = var22.getUniqueId();
                if (var56.contains(var123)) {
                    break label66;
                }
            }

            if (var21 instanceof Player var23) {
                TrustStore var63 = this.trust;
                UUID var125 = var1.getUniqueId();
                UUID var148 = var23.getUniqueId();
                if (var63.isTrusted(var125, var148, 1736364606)) {
                    break label66;
                }
            }

            if (var21 instanceof Player var211) {
                Material var126 = Material.SHIELD;
                int var25 = var211.getCooldown(var126);
                Material var127 = Material.SHIELD;
                int var150 = Math.max(var25, 100);
                var211.setCooldown(var127, var150);
            }

            String var72 = "auratus";
            String var128 = "echoing-aegis";
            double var212 = GemTuning.mythicDamage(var72, var128, 1.56, 478197159) / 1.56;
            double var79 = 31.2 * var212;
            double var159 = 15.6 * var212;
            double var187 = var16 * 1.56;
            double var189 = var187 * var212;
            double var161 = Math.max(var159, var189);
            double var26 = Math.min(var79, var161);
            Set var84 = this.reflecting;
            UUID var130 = var1.getUniqueId();
            var84.add(var130);

            try {
                ConsistentDamage.dealFullArmorAware(var1, var21, var26, 1323155734);
            } finally {
                this.reflecting.remove(var1.getUniqueId());
            }
        }

        if (!MythicAegisState.legacyAuratus(180703355)) {
            double var166 = maxHealth(var1, 1696592533);
            double var192 = var1.getHealth();
            double var168 = var166 - var192;
            double var208 = Math.min(var18, var168);
            if (var208 > 0.0) {
                var1.heal(var208);
            }
        }

        Location var210 = var1.getLocation().add(0.0, 1.0, 0.0);
        flash(var210, 1504910389);
        World var104 = var210.getWorld();
        Sound var172 = Sound.ITEM_SHIELD_BLOCK;
        var104.playSound(var210, var172, 1.0F, 0.9F);
        if (var15) {
            double var178 = maxHealth(var1, 1696592533);
            double var199 = var1.getHealth();
            double var180 = var178 - var199;
            double var141 = Math.min(5.0, var180);
            var1.heal(var141);
            PotionEffectType var182 = PotionEffectType.SPEED;
            PotionEffect var143 = new PotionEffect(var182, 100, 2, false, true);
            var1.addPotionEffect(var143);
            PotionEffectType var183 = PotionEffectType.WEAVING;
            PotionEffect var144 = new PotionEffect(var183, 100, 0, false, true);
            var1.addPotionEffect(var144);
            clearCobwebs(var1.getLocation(), 1849109663);
            World var114 = var210.getWorld();
            Sound var173 = Sound.ITEM_TOTEM_USE;
            var114.playSound(var210, var173, 0.5F, 1.6F);
        }
    }

    private static void clearCobwebs(Location var0, int var1) {
        World var7 = var0.getWorld();
        if (var7 != null) {
            int var8 = var0.getBlockX();
            int var9 = var0.getBlockY();
            int var10 = var0.getBlockZ();

            for (int var13 = -4; var13 <= 4; var13++) {
                for (int var14 = -4; var14 <= 4; var14++) {
                    for (int var15 = -4; var15 <= 4; var15++) {
                        int var38 = var13 * var13;
                        int var52 = var14 * var14;
                        int var39 = var38 + var52;
                        int var54 = var15 * var15;
                        if (var39 + var54 <= 16) {
                            int var57 = var8 + var13;
                            int var62 = var9 + var14;
                            int var64 = var10 + var15;
                            Block var16 = var7.getBlockAt(var57, var62, var64);
                            Material var44 = var16.getType();
                            Material var58 = Material.COBWEB;
                            if (var44 == var58) {
                                var16.breakNaturally();
                            }
                        }
                    }
                }
            }
        }
    }

    private static void flash(Location var0, int var1) {
        if (FLASH_NEEDS_COLOR) {
            World var18 = var0.getWorld();
            Particle var19 = Particle.FLASH;
            Color var30 = Color.WHITE;
            Particles.spawn(var18, var19, var0, 1, 0.0, 0.0, 0.0, 0.0, var30, 1141219473);
        } else {
            World var16 = var0.getWorld();
            Particle var3 = Particle.FLASH;
            Particles.spawn(var16, var3, var0, 1, 0.0, 0.0, 0.0, 0.0, null, 1141219473);
        }
    }

    private static double maxHealth(Player var0, int var1) {
        Attribute var3 = Attribute.MAX_HEALTH;
        AttributeInstance var4 = var0.getAttribute(var3);
        double var8;
        if (var4 != null) {
            var8 = var4.getValue();
        } else {
            var8 = 20.0;
        }

        return var8;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        MythicAegisAuraTask.resetReach(var1.getPlayer(), 1923315323);
        Map var6 = this.lastInteractTick;
        UUID var11 = var1.getPlayer().getUniqueId();
        var6.remove(var11);
        MythicAegisState var9 = this.state;
        UUID var14 = var1.getPlayer().getUniqueId();
        var9.clearTransient(var14, 1091539427);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent var1) {
        MythicAegisAuraTask.resetReach(var1.getPlayer(), 1923315323);
    }

    static {
        boolean var3;
        if (Particle.FLASH.getDataType() == Color.class) {
            var3 = true;
        } else {
            var3 = false;
        }

        FLASH_NEEDS_COLOR = var3;
    }
}
