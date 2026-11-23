package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemAutoEnchants;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TagStore;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Logger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public final class FluxGemListener implements Listener {
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final TagStore tags;
    private GlobalState globalState;
    private BukkitTask hasteTask;
    public final Map<UUID, AtomicInteger> fluxMoved;
    public final Map<UUID, Long> fluxMovedExpiry;
    public final Map<UUID, BitSet> fluxSpeedFlags;
    public final Map<UUID, AtomicInteger> fluxDamaged;
    public final Map<UUID, Long> fluxDamagedExpiry;
    public final Map<UUID, AtomicInteger> fluxBroken;
    public final Map<UUID, Long> fluxBrokenExpiry;
    public final Map<UUID, Long> fluxPlayerShotExpiry;
    public final Map<UUID, Long> freezeExpiry;
    public final Map<UUID, Long> fluxCharge;
    public final Map<UUID, Long> fluxChargeHidden;
    public static final long FLUX_CHARGE_MAX = 2000000L;
    public static final double FLUX_OVERCHARGE_MAX = 200.0;
    public final Map<UUID, Double> fluxChargePercent;
    public final Map<UUID, Double> fluxKinetic;
    public final Map<UUID, Boolean> fluxCharging;
    public final Map<UUID, Boolean> fluxOvercharging;
    public final Map<UUID, Boolean> fluxTrueOvercharging;
    public final Map<UUID, Integer> fluxOverChargeTime;
    public final Map<UUID, Long> cd2Expiry;
    public final Map<UUID, Long> cd3Expiry;
    public final Map<UUID, Long> kineticBurstExpiry;
    public final Map<UUID, Double> storedDamage;
    public final Map<UUID, Boolean> doubleDamageFlag;
    public final Map<UUID, Boolean> kinOvAlt;
    public final Map<UUID, Boolean> kinOvMain;
    public final Map<UUID, Integer> kinOvMainCounter;
    public final Map<UUID, Integer> kinOvMining;
    public final Map<UUID, Boolean> groundFlag;
    public final Map<UUID, Boolean> damagedFlag;
    public volatile String fluxKiller;
    private final TrustStore trust;
    private BukkitTask chargeTask;
    private static final double STORED_CAP = 200.0;

    public void setGlobalState(GlobalState var1, int var2) {
        this.globalState = var1;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent var1) {
        UUID var4 = var1.getPlayer().getUniqueId();
        Map var8 = this.fluxMoved;
        var8.remove(var4);
        Map var11 = this.fluxMovedExpiry;
        var11.remove(var4);
        Map var14 = this.fluxSpeedFlags;
        var14.remove(var4);
        Map var17 = this.fluxDamaged;
        var17.remove(var4);
        Map var20 = this.fluxDamagedExpiry;
        var20.remove(var4);
        Map var23 = this.fluxBroken;
        var23.remove(var4);
        Map var26 = this.fluxBrokenExpiry;
        var26.remove(var4);
        Map var29 = this.fluxCharging;
        var29.remove(var4);
        Map var32 = this.fluxOvercharging;
        var32.remove(var4);
        Map var35 = this.fluxTrueOvercharging;
        var35.remove(var4);
        Map var38 = this.fluxOverChargeTime;
        var38.remove(var4);
        Map var41 = this.storedDamage;
        var41.remove(var4);
        Map var44 = this.doubleDamageFlag;
        var44.remove(var4);
        Map var47 = this.kinOvAlt;
        var47.remove(var4);
        Map var50 = this.kinOvMain;
        var50.remove(var4);
        Map var53 = this.kinOvMainCounter;
        var53.remove(var4);
        Map var56 = this.kinOvMining;
        var56.remove(var4);
        Map var59 = this.groundFlag;
        var59.remove(var4);
        Map var62 = this.damagedFlag;
        var62.remove(var4);
        EntityClickGuard.clear(var4, 497410948);
        GrantedEffect.forget(var4, 931364342);
        GemAutoEnchants.forget(var4, 578466762);
        FluxBeamScript.clearAim(var4, 1075551428);
        this.saveWatts(1607488947);
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void saveWatts(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;

        for (Entry var97 : this.fluxCharge.entrySet()) {
            String var79 = "charge." + var97.getKey();
            Object var89 = var97.getValue();
            var5.set(var79, var89);
        }

        for (Entry var96 : this.fluxChargeHidden.entrySet()) {
            String var75 = "hidden." + var96.getKey();
            Object var87 = var96.getValue();
            var5.set(var75, var87);
        }

        for (Entry var7 : this.fluxChargePercent.entrySet()) {
            String var63 = "percent." + var7.getKey();
            Object var80 = var7.getValue();
            var5.set(var63, var80);
        }

        for (Entry var94 : this.fluxKinetic.entrySet()) {
            String var67 = "kinetic." + var94.getKey();
            Object var82 = var94.getValue();
            var5.set(var67, var82);
        }

        File var85 = this.plugin.getDataFolder();
        String var4 = "flux.yml";
        File var40 = new File(var85, var4);
        File var93 = var40;

        try {
            if (!var93.getParentFile().exists()) {
                var93.getParentFile().mkdirs();
            }
            var5.save(var93);
        } catch (IOException var95) {
            Logger var51 = this.plugin.getLogger();
            String var71 = "Failed to save flux.yml: " + var95.getMessage();
            var51.warning(var71);
        }
    }

    public void loadWatts(int var1) {
        File var92 = this.plugin.getDataFolder();
        String var4 = "flux.yml";
        File var2 = new File(var92, var4);
        if (var2.exists()) {
            YamlConfiguration var6 = YamlConfiguration.loadConfiguration(var2);
            ConfigurationSection var7 = var6.getConfigurationSection("charge");
            if (var7 != null) {
                for (String var121 : var7.getKeys(false)) {
                    try {
                        Map var72 = this.fluxCharge;
                        UUID var90 = UUID.fromString(var121);
                        Long var108 = var7.getLong(var121);
                        var72.put(var90, var108);
                    } catch (IllegalArgumentException var125) {
                    }
                }
            }

            ConfigurationSection var113 = var6.getConfigurationSection("hidden");
            if (var113 != null) {
                for (String var9 : var113.getKeys(false)) {
                    try {
                        Map var35 = this.fluxChargeHidden;
                        UUID var80 = UUID.fromString(var9);
                        Long var96 = var113.getLong(var9);
                        var35.put(var80, var96);
                    } catch (IllegalArgumentException var124) {
                    }
                }
            }

            ConfigurationSection var114 = var6.getConfigurationSection("percent");
            if (var114 != null) {
                for (String var120 : var114.getKeys(false)) {
                    try {
                        Map var65 = this.fluxChargePercent;
                        UUID var88 = UUID.fromString(var120);
                        Double var104 = var114.getDouble(var120);
                        var65.put(var88, var104);
                    } catch (IllegalArgumentException var123) {
                    }
                }
            }

            ConfigurationSection var115 = var6.getConfigurationSection("kinetic");
            if (var115 != null) {
                for (String var119 : var115.getKeys(false)) {
                    try {
                        Map var58 = this.fluxKinetic;
                        UUID var86 = UUID.fromString(var119);
                        Double var100 = var115.getDouble(var119);
                        var58.put(var86, var100);
                    } catch (IllegalArgumentException var122) {
                    }
                }
            }
        }
    }

    public void recomputeChargePercent(UUID var1, int var2) {
        Map<UUID, Long> var11 = this.fluxCharge;
        Long var20 = 0L;
        long var9 = var11.getOrDefault(var1, var20);
        Map var17 = this.fluxChargePercent;
        Double var29 = (double)var9 * 100.0 / 2000000.0;
        var17.put(var1, var29);
    }

    public double chargePercent(UUID var1, int var2) {
        Map<UUID, Double> var7 = this.fluxChargePercent;
        Double var12 = 0.0;
        return var7.getOrDefault(var1, var12);
    }

    public FluxGemListener(BlissPlugin var1, PlayerDataStore var2, TagStore var3, int var4) {
        HashMap var6 = new HashMap();
        this.fluxMoved = var6;
        HashMap var40 = new HashMap();
        this.fluxMovedExpiry = var40;
        HashMap var41 = new HashMap();
        this.fluxSpeedFlags = var41;
        HashMap var42 = new HashMap();
        this.fluxDamaged = var42;
        HashMap var43 = new HashMap();
        this.fluxDamagedExpiry = var43;
        HashMap var44 = new HashMap();
        this.fluxBroken = var44;
        HashMap var45 = new HashMap();
        this.fluxBrokenExpiry = var45;
        HashMap var46 = new HashMap();
        this.fluxPlayerShotExpiry = var46;
        HashMap var47 = new HashMap();
        this.freezeExpiry = var47;
        HashMap var48 = new HashMap();
        this.fluxCharge = var48;
        HashMap var49 = new HashMap();
        this.fluxChargeHidden = var49;
        HashMap var50 = new HashMap();
        this.fluxChargePercent = var50;
        HashMap var51 = new HashMap();
        this.fluxKinetic = var51;
        HashMap var52 = new HashMap();
        this.fluxCharging = var52;
        HashMap var53 = new HashMap();
        this.fluxOvercharging = var53;
        HashMap var54 = new HashMap();
        this.fluxTrueOvercharging = var54;
        HashMap var55 = new HashMap();
        this.fluxOverChargeTime = var55;
        HashMap var56 = new HashMap();
        this.cd2Expiry = var56;
        HashMap var57 = new HashMap();
        this.cd3Expiry = var57;
        HashMap var58 = new HashMap();
        this.kineticBurstExpiry = var58;
        HashMap var59 = new HashMap();
        this.storedDamage = var59;
        HashMap var60 = new HashMap();
        this.doubleDamageFlag = var60;
        HashMap var61 = new HashMap();
        this.kinOvAlt = var61;
        HashMap var62 = new HashMap();
        this.kinOvMain = var62;
        HashMap var63 = new HashMap();
        this.kinOvMainCounter = var63;
        HashMap var64 = new HashMap();
        this.kinOvMining = var64;
        HashMap var65 = new HashMap();
        this.groundFlag = var65;
        HashMap var66 = new HashMap();
        this.damagedFlag = var66;
        this.fluxKiller = null;
        this.plugin = var1;
        this.players = var2;
        this.tags = var3;
        TrustStore var72 = var1.trust(310732214);
        this.trust = var72;
    }

    public BlissPlugin plugin() {
        return this.plugin;
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var14 = this.plugin;
        Runnable var17 = this::applyHasteOnBreak;
        BukkitTask var11 = var3.runTaskTimer(var14, var17, 20L, 20L);
        this.hasteTask = var11;
        BukkitScheduler var12 = Bukkit.getScheduler();
        BlissPlugin var16 = this.plugin;
        Runnable var19 = this::tickChargeTransfer;
        BukkitTask var13 = var12.runTaskTimer(var16, var19, 20L, 20L);
        this.chargeTask = var13;
    }

    public void stop() {
        if (this.hasteTask != null) {
            this.hasteTask.cancel();
        }

        if (this.chargeTask != null) {
            this.chargeTask.cancel();
        }
    }

    private void tickChargeTransfer() {
        for (Player var7 : Bukkit.getOnlinePlayers()) {
            UUID var8;
            boolean var205;
            label94: {
                var8 = var7.getUniqueId();
                ItemStack var9 = var7.getInventory().getItemInOffHand();
                ItemStack var10 = var7.getInventory().getItemInMainHand();
                int var38 = BlissCoreItems.gemIdOf(var10, 382441079);
                int var206 = GemType.FLUX.id();
                if (var38 != var206 || BlissCoreItems.tierOf(var10, 1633828214) != 2) {
                    int var42 = BlissCoreItems.gemIdOf(var9, 382441079);
                    int var209 = GemType.FLUX.id();
                    if (var42 != var209 || BlissCoreItems.tierOf(var9, 1633828214) != 2) {
                        GemType var211 = GemType.FLUX;
                        if (!GoldGem.isWieldingSoul(var7, var211, 100010596)) {
                            var205 = false;
                            break label94;
                        }
                    }
                }

                var205 = true;
            }

            if (!var205) {
                Boolean var199 = Boolean.TRUE;
                Map var258 = this.fluxCharging;
                Object var259 = var258.get(var8);
                if (var199.equals(var259)) {
                    Map var202 = this.fluxCharging;
                    Boolean var365 = false;
                    var202.put(var8, var365);
                    Component var262 = Text.legacy("&cCharging paused!", 373192400);
                    var7.sendMessage(var262);
                }
            } else {
                Boolean var48 = Boolean.TRUE;
                Map var213 = this.fluxCharging;
                Object var214 = var213.get(var8);
                if (var48.equals(var214)) {
                    double var12 = this.chargePercent(var8, 1875673459);
                    Map<UUID, Double> var54 = this.fluxKinetic;
                    Double var265 = 0.0;
                    double var14 = var54.getOrDefault(var8, var265);
                    GlobalState var16 = GlobalState.forPlayer(var7, 1433087673);
                    if (var16 != null && !var16.allowOvercharge(201427360) && var14 >= 100.0) {
                        Map var68 = this.fluxKinetic;
                        Double var270 = 100.0;
                        var68.put(var8, var270);
                        Map var71 = this.fluxCharging;
                        Boolean var272 = false;
                        var71.put(var8, var272);
                        Map var74 = this.fluxOvercharging;
                        Boolean var274 = false;
                        var74.put(var8, var274);
                        Map var77 = this.fluxOverChargeTime;
                        Integer var276 = 0;
                        var77.put(var8, var276);
                        Component var222 = Text.legacy("&f\ud83d\udd2e Your gem is fully charged at 100%", 373192400);
                        var7.sendMessage(var222);
                    } else if (var12 > 0.667) {
                        if (var14 >= 200.0) {
                            Map var87 = this.fluxKinetic;
                            Double var283 = 200.0;
                            var87.put(var8, var283);
                            Map var90 = this.fluxOverChargeTime;
                            Integer var285 = 3;
                            var90.put(var8, var285);
                            Map var93 = this.fluxOvercharging;
                            Boolean var287 = true;
                            var93.put(var8, var287);
                            Map var96 = this.fluxCharging;
                            Boolean var289 = false;
                            var96.put(var8, var289);
                            Component var228 = Text.legacy("&f\ud83d\udd2e &eYour gem is fully overcharged at 200%", 373192400);
                            var7.sendMessage(var228);
                            AchievementStore var101 = this.plugin.advancements(1975441698);
                            AchievementStore.Advancement var290 = AchievementStore.Advancement.OVERCHARGED;
                            var101.unlock(var7, var290, 1132982324);
                        } else {
                            double var293 = var14 + 0.667;
                            double var17 = Math.min(200.0, var293);
                            if (var17 > 100.0) {
                                if (var14 <= 100.0) {
                                    Map var113 = this.fluxOvercharging;
                                    Boolean var300 = true;
                                    var113.put(var8, var300);
                                    Map var116 = this.fluxOverChargeTime;
                                    Integer var302 = 3;
                                    var116.put(var8, var302);
                                    Component var233 = Text.legacy("&f\ud83d\udd2e &6Overcharging! &7(past 100%)", 373192400);
                                    var7.sendMessage(var233);
                                    AchievementStore var121 = this.plugin.advancements(1975441698);
                                    AchievementStore.Advancement var303 = AchievementStore.Advancement.OVERCHARGED;
                                    var121.unlock(var7, var303, 1132982324);
                                }
                            } else {
                                Map var145 = this.fluxOverChargeTime;
                                Integer var326 = 0;
                                var145.put(var8, var326);
                            }

                            Map var123 = this.fluxKinetic;
                            Double var306 = var17;
                            var123.put(var8, var306);
                            Map var126 = this.fluxChargePercent;
                            Double var311 = var12 - 0.667;
                            var126.put(var8, var311);
                            Map<UUID, Long> var129 = this.fluxCharge;
                            Long var314 = 0L;
                            long var315 = var129.getOrDefault(var8, var314);
                            long var370 = Math.round(13340.000000000002);
                            long var317 = var315 - var370;
                            long var21 = Math.max(0L, var317);
                            Map var139 = this.fluxCharge;
                            Long var321 = var21;
                            var139.put(var8, var321);
                            Map var142 = this.fluxChargeHidden;
                            Long var324 = var21;
                            var142.put(var8, var324);
                        }
                    } else {
                        Map var148 = this.fluxKinetic;
                        Double var331 = var14 + var12;
                        var148.put(var8, var331);
                        Map var151 = this.fluxChargePercent;
                        Double var334 = 0.0;
                        var151.put(var8, var334);
                        Map var154 = this.fluxCharging;
                        Boolean var336 = false;
                        var154.put(var8, var336);
                        Map var157 = this.fluxCharge;
                        Long var339 = 0L;
                        var157.put(var8, var339);
                        Map<UUID, Double> var160 = this.fluxKinetic;
                        Double var342 = 0.0;
                        if (var160.getOrDefault(var8, var342) > 200.0) {
                            Map var167 = this.fluxKinetic;
                            Double var347 = 200.0;
                            var167.put(var8, var347);
                        }

                        Map<UUID, Double> var170 = this.fluxKinetic;
                        Double var350 = 0.0;
                        double var374 = var170.getOrDefault(var8, var350);
                        if (var16 != null && !var16.allowOvercharge(201427360) && var374 > 100.0) {
                            var374 = 100.0;
                            Map var184 = this.fluxKinetic;
                            Double var355 = 100.0;
                            var184.put(var8, var355);
                        }

                        Component var253 = Text.legacy(
                            "&f\ud83d\udd2e You ran out of watts, your gem is charged at " + formatPercent(var374, 33278980) + "%", 373192400
                        );
                        var7.sendMessage(var253);
                        if (var374 > 100.0) {
                            Map var191 = this.fluxCharging;
                            Boolean var359 = false;
                            var191.put(var8, var359);
                            Map var194 = this.fluxOvercharging;
                            Boolean var361 = true;
                            var194.put(var8, var361);
                            AchievementStore var198 = this.plugin.advancements(1975441698);
                            AchievementStore.Advancement var362 = AchievementStore.Advancement.OVERCHARGED;
                            var198.unlock(var7, var362, 1132982324);
                        }
                    }
                }
            }
        }
    }

    private static String formatPercent(double var0, int var2) {
        double var17 = Math.floor(var0);
        String var15;
        if (var0 == var17) {
            String var12 = "%.1f";
            Object[] var16 = new Object[1];
            Double var19 = var0;
            var16[0] = var19;
            var15 = String.format(var12, var16);
        } else {
            var15 = String.valueOf(var0);
        }

        return var15;
    }

    private void applyHasteOnBreak() {
        long var11 = System.currentTimeMillis();

        for (Player var14 : Bukkit.getOnlinePlayers()) {
            UUID var15 = var14.getUniqueId();
            TagStore var30 = this.tags;
            if (!var30.isTagged(var15, 995676415)) {
                PlayerDataStore var33 = this.players;
                if (var33.get(var15, 1264010596).canUsePassives(2051432579)) {
                    String var36 = "flux";
                    String var58 = "flow-state-mining";
                    if (GemTuning.passive(var36, var58, true, 14309468)) {
                        Map var39 = this.fluxBroken;
                        AtomicInteger var16 = (AtomicInteger)var39.get(var15);
                        if (var16 != null) {
                            int var56;
                            label31: {
                                Map var44 = this.fluxBrokenExpiry;
                                Long var17 = (Long)var44.get(var15);
                                if (var17 != null) {
                                    long var68 = var17;
                                    if (var11 > var68) {
                                        var56 = 0;
                                        break label31;
                                    }
                                }

                                var56 = var16.get();
                            }

                            if (var56 > 5) {
                                int var65 = (var56 - 1) / 5 - 1;
                                int var19 = Math.min(9, var65);
                                PotionEffectType var5 = PotionEffectType.HASTE;
                                PotionEffect var66 = new PotionEffect(var5, 40, var19, false, false, true);
                                GrantedEffect.grant(var14, var66, 491118892);
                            }
                        }
                    }
                }
            }
        }
    }

    public void markPlayerShot(UUID var1, int var2) {
        Map var9 = this.fluxPlayerShotExpiry;
        Long var13 = System.currentTimeMillis() + 800L;
        var9.put(var1, var13);
    }

    @EventHandler
    public void onLeftClickBeam(PlayerInteractEvent var1) {
        Action var11 = var1.getAction();
        Action var3 = Action.LEFT_CLICK_AIR;
        if (var11 != var3) {
            Action var13 = var1.getAction();
            Action var21 = Action.LEFT_CLICK_BLOCK;
            if (var13 != var21) {
                return;
            }
        }

        Player var8 = var1.getPlayer();
        UUID var9 = var8.getUniqueId();
        int var10 = Bukkit.getCurrentTick();
        BukkitScheduler var19 = Bukkit.getScheduler();
        BlissPlugin var23 = this.plugin;
        Runnable var24 = () -> {
            if (!EntityClickGuard.meleeSince(var9, var10, 1033776613)) {
                if (!WattDepositListener.justOpened(var9, 1835359645)) {
                    if (var8.isOnline()) {
                        this.fireBeam(var8, var9, 1402862128);
                    }
                }
            }
        };
        var19.runTaskLater(var23, var24, 1L);
    }

    private void fireBeam(Player var1, UUID var2, int var3) {
        TagStore var47 = this.tags;
        if (!var47.isTagged(var2, 995676415)) {
            PlayerDataStore var50 = this.players;
            PlayerData var22 = var50.get(var2, 1264010596);
            if (var22.canUsePowers(1616892701)) {
                ItemStack var23 = var1.getInventory().getItemInMainHand();
                GemType var154 = GemType.FLUX;
                boolean var24 = GoldGem.isWieldingSoulMain(var1, var154, 244096655);
                int var60 = BlissCoreItems.gemIdOf(var23, 382441079);
                int var156 = GemType.FLUX.id();
                if (var60 == var156 || var24) {
                    if (BlissCoreItems.tierOf(var23, 1633828214) == 2 || var24) {
                        if (GemAbilityRules.on(GemAbilityRules.GemAbility.FLUX_BEAM, 425337338)) {
                            long var25 = System.currentTimeMillis();
                            Map var70 = this.cd2Expiry;
                            Long var27 = (Long)var70.get(var2);
                            if (var27 != null) {
                                long var75 = var27;
                                if (var75 > var25) {
                                    return;
                                }
                            }

                            int var79 = var22.energy(1160853318);
                            boolean var160 = this.hasDragonEgg(var2, 1897338785);
                            long var28 = beamCooldownSeconds(var79, var160, 739019257);
                            Map var83 = this.cd2Expiry;
                            String var10 = "flux";
                            String var11 = "energy-beam";
                            long var260 = var28 * 1000L;
                            long var232 = GemTuning.cd(var10, var11, 30.0, var260, 1621983041);
                            int var253 = var22.energy(1160853318);
                            long var234 = EnergyScale.cd(var232, var253, 907518618);
                            Long var193 = var25 + var234;
                            var83.put(var2, var193);
                            Map<UUID, Double> var86 = this.fluxKinetic;
                            Double var196 = 0.0;
                            double var30 = var86.getOrDefault(var2, var196);
                            int var32 = tierFor(var30, 1592813063);
                            if (var32 >= 0) {
                                Map var96 = this.fluxKinetic;
                                Double var199 = 0.0;
                                var96.put(var2, var199);
                                FluxBeamRenderer.Variant var33 = FluxBeamRenderer.variantFor(var32, 391611836);
                                int var34 = var32;
                                double var35 = 0.0;
                                if (var32 <= 10) {
                                    double var202 = var30 / 100.0;
                                    double var37 = Math.min(1.0, var202);
                                    int var171 = (int)Math.round(var37 * 20.0);
                                    var34 = Math.min(10, var171);
                                    double var242 = (var37 - 0.5) / 0.5;
                                    double var206 = Math.min(1.0, var242);
                                    var35 = Math.max(0.0, var206);
                                }

                                if (var32 == 11) {
                                    int var266 = FluxBeamScript.ultraPulseCount(var30, 1347064835);
                                    BlissPlugin var148 = this.plugin;
                                    FluxBeamScript.ultraWindup(var148, var1, var266, 1032809015);
                                    BukkitScheduler var151 = Bukkit.getScheduler();
                                    BlissPlugin var187 = this.plugin;
                                    Runnable var218 = () -> {
                                        if (var1.isOnline()) {
                                            BlissPlugin var15 = this.plugin;
                                            FluxBeamScript.playTier(var15, var1, 11, 1284120202);
                                            double var41 = (var30 - 100.0) / 100.0;
                                            double var31 = Math.min(1.0, var41);
                                            double var11x = Math.max(0.0, var31);
                                            if (var11x > 0.0) {
                                                BlissPlugin var24x = this.plugin;
                                                FluxBeamScript.ultraBeam(var24x, var1, var11x, 1396458786);
                                                BlissPlugin var26 = this.plugin;
                                                FluxBeamScript.ultraBurst(var26, var1, var11x, 453258327);
                                            }
                                        }
                                    };
                                    long var230 = FluxBeamScript.ultraWindupTicks(var266, 1051196272) + 6L;
                                    var151.runTaskLater(var187, var218, var230);
                                } else {
                                    BlissPlugin var116 = this.plugin;
                                    FluxBeamScript.playTier(var116, var1, var34, 1284120202);
                                    if (var35 > 0.0) {
                                        FluxBeamScript.playDiagonalConeBeams(var1, var35, 1831491953);
                                    }

                                    if (var32 == 12) {
                                        double var180 = Math.min(var1.getHealth(), 6.0);
                                        var1.setHealth(var180);
                                    }

                                    Location var265 = var1.getEyeLocation();
                                    Vector var39 = var265.getDirection().normalize();
                                    double var40 = var33.hitRadius();
                                    int var42 = var33.range();
                                    long var45 = beamDamageDelayTicks(var34, 422462140);
                                    if (var45 <= 0L) {
                                        this.applyBeamDamage(var1, var32, var30, var265, var39, var40, var42, 1980942332);
                                    } else {
                                        BukkitScheduler var142 = Bukkit.getScheduler();
                                        BlissPlugin var184 = this.plugin;
                                        Runnable var215 = () -> this.applyBeamDamage(var1, var32, var30, var265, var39, var40, var42, 1980942332);
                                        var142.runTaskLater(var184, var215, var45);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static long beamDamageDelayTicks(int var0, int var1) {
        if (var0 <= 0) {
            return 0L;
        } else if (var0 <= 5) {
            return 12L;
        } else if (var0 <= 7) {
            return 17L;
        } else if (var0 <= 10) {
            return 34L;
        } else {
            return var0 == 11 ? 0L : 38L;
        }
    }

    private static int beamArmorWear(int var0, int var1) {
        return switch (var0) {
            case 3, 4, 5 -> 12;
            case 6 -> 45;
            case 7, 8 -> 35;
            case 9, 10 -> 75;
            default -> 0;
        };
    }

    private static void wearArmor(LivingEntity var0, int var1, int var2) {
        if (var1 > 0) {
            EntityEquipment var6 = var0.getEquipment();
            if (var6 != null) {
                EquipmentSlot[] var19 = new EquipmentSlot[4];
                EquipmentSlot var5 = EquipmentSlot.HEAD;
                var19[0] = var5;
                EquipmentSlot var70 = EquipmentSlot.CHEST;
                var19[1] = var70;
                EquipmentSlot var71 = EquipmentSlot.LEGS;
                var19[2] = var71;
                EquipmentSlot var72 = EquipmentSlot.FEET;
                var19[3] = var72;

                for (EquipmentSlot var10 : var19) {
                    ItemStack var11 = var6.getItem(var10);
                    if (var11 != null && !var11.getType().isAir()) {
                        ItemMeta var13 = var11.getItemMeta();
                        if (var13 instanceof Damageable) {
                            Damageable var12 = (Damageable)var13;
                            if (!var12.isUnbreakable()) {
                                int var55;
                                if (var12.hasMaxDamage()) {
                                    var55 = var12.getMaxDamage();
                                } else {
                                    var55 = var11.getType().getMaxDurability();
                                }

                                short var73 = (short)var55;
                                if (var73 > 0) {
                                    int var45 = var12.getDamage();
                                    int var14 = var45 + var1;
                                    if (var14 >= var73) {
                                        var6.setItem(var10, null);
                                    } else {
                                        var12.setDamage(var14);
                                        var11.setItemMeta(var12);
                                        var6.setItem(var10, var11);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void applyBeamDamage(Player var1, int var2, double var3, Location var5, Vector var6, double var7, int var9, int var10) {
        String var11 = "flux";
        String var12 = "energy-beam";
        double var23 = GemTuning.damage(var11, var12, 1.0, 1172948035);
        String var39 = "flux";
        String var103 = "energy-beam-cap";
        double var25 = GemTuning.damage(var39, var103, 0.0, 1172948035);
        HashSet var42 = new HashSet();
        HashSet var27 = var42;
        HashSet<UUID> var43 = new HashSet<>();
        HashSet<UUID> var28 = var43;

        for (int var29 = 1; var29 <= var9; var29++) {
            Location var47 = var5.clone();
            Vector var106 = var6.clone();
            Vector var107 = var106.multiply(var29);
            Location var30 = var47.add(var107);
            World var50 = var30.getWorld();

            for (Entity var32 : var50.getNearbyEntities(var30, var7, var7, var7)) {
                if (!var32.equals(var1) && var32 instanceof LivingEntity) {
                    LivingEntity var33 = (LivingEntity)var32;
                    if (var27.add(var32)) {
                        if (var32 instanceof Player) {
                            Player var34 = (Player)var32;
                            TrustStore var71 = this.trust;
                            UUID var112 = var1.getUniqueId();
                            UUID var132 = var34.getUniqueId();
                            if (var71.isTrusted(var112, var132, 1736364606)) {
                                continue;
                            }
                        }

                        if (GemAbilityRules.on(GemAbilityRules.GemAbility.FLUX_BEAM, 425337338)) {
                            if (var2 == 0) {
                                GemAbilityRules.GemAbility var133 = GemAbilityRules.GemAbility.FLUX_BEAM;
                                double var147 = 4.0 * var23;
                                double var134 = GemAbilityRules.scaled(var133, var147, 1684677315);
                                ConsistentDamage.dealVanillaOrTrue(var1, var33, var134, var25, 1236326608);
                            } else {
                                double var140 = Math.min(200.0, var3);
                                double var178 = Math.max(5.0, var140);
                                Attribute var120 = Attribute.MAX_HEALTH;
                                double var35 = var33.getAttribute(var120).getValue();
                                GemAbilityRules.GemAbility var142 = GemAbilityRules.GemAbility.FLUX_BEAM;
                                double var155 = var35 * var178 * 5.0 / 100.0;
                                double var157 = var155 * var23;
                                double var143 = GemAbilityRules.scaled(var142, var157, 1684677315);
                                ConsistentDamage.dealVanillaOrTrue(var1, var33, var143, var25, 1236326608);
                            }

                            if (!var33.isDead()) {
                                int var115 = beamArmorWear(var2, 1626198070);
                                wearArmor(var33, var115, 1842060750);
                            }

                            if (var32 instanceof Player) {
                                Map var83 = this.damagedFlag;
                                UUID var117 = var32.getUniqueId();
                                Boolean var137 = true;
                                var83.put(var117, var137);
                                UUID var119 = var32.getUniqueId();
                                var28.add(var119);
                            }
                        }
                    }
                }
            }
        }

        if (!var28.isEmpty()) {
            String var123 = var1.getName();
            this.fluxKiller = var123;
            String var177 = var1.getName();
            BukkitScheduler var101 = Bukkit.getScheduler();
            BlissPlugin var125 = this.plugin;
            Runnable var146 = () -> {
                for (UUID var7x : var28) {
                    Map var18 = this.damagedFlag;
                    Boolean var23x = false;
                    var18.put(var7x, var23x);
                }

                String var20 = this.fluxKiller;
                if (var177.equals(var20)) {
                    this.fluxKiller = null;
                }
            };
            var101.runTaskLater(var125, var146, 30L);
        }
    }

    @EventHandler(
        priority = EventPriority.HIGH
    )
    public void onFluxBeamDeath(PlayerDeathEvent var1) {
        Player var5 = var1.getEntity();
        UUID var6 = var5.getUniqueId();
        Boolean var14 = Boolean.TRUE;
        Map var33 = this.damagedFlag;
        Object var34 = var33.get(var6);
        if (var14.equals(var34)) {
            String var7 = this.fluxKiller;
            if (var7 != null) {
                boolean var32;
                label32: {
                    label31: {
                        Player var8 = Bukkit.getPlayerExact(var7);
                        if (var8 != null) {
                            if (var8.isInvisible()) {
                                break label31;
                            }

                            PotionEffectType var35 = PotionEffectType.INVISIBILITY;
                            if (var8.hasPotionEffect(var35)) {
                                break label31;
                            }
                        }

                        var32 = false;
                        break label32;
                    }

                    var32 = true;
                }

                String var27;
                if (var32) {
                    var27 = "&k???";
                } else {
                    var27 = var7;
                }

                String var37 = var5.getName();
                Component var39 = Text.legacy(var37 + " was obliterated by " + var27, 373192400);
                var1.deathMessage(var39);
                Map var30 = this.damagedFlag;
                Boolean var43 = false;
                var30.put(var6, var43);
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR
    )
    public void onFluxHolderDeath(PlayerDeathEvent var1) {
        UUID var4 = var1.getEntity().getUniqueId();
        Map var8 = this.storedDamage;
        var8.remove(var4);
        Map var11 = this.fluxKinetic;
        var11.remove(var4);
        Map var14 = this.kineticBurstExpiry;
        var14.remove(var4);
        this.saveWatts(1607488947);
    }

    static long beamCooldownSeconds(int var0, boolean var1, int var2) {
        if (var0 > 5) {
            long var23;
            if (var1) {
                var23 = 15L;
            } else {
                var23 = 30L;
            }

            return var23;
        } else if (var0 == 5) {
            long var8;
            if (var1) {
                var8 = 20L;
            } else {
                var8 = 45L;
            }

            return var8;
        } else if (var0 == 4) {
            long var21;
            if (var1) {
                var21 = 30L;
            } else {
                var21 = 60L;
            }

            return var21;
        } else if (var0 == 3) {
            long var18;
            if (var1) {
                var18 = 45L;
            } else {
                var18 = 90L;
            }

            return var18;
        } else if (var0 == 2) {
            long var14;
            if (var1) {
                var14 = 60L;
            } else {
                var14 = 120L;
            }

            return var14;
        } else {
            long var25;
            if (var1) {
                var25 = 60L;
            } else {
                var25 = 120L;
            }

            return var25;
        }
    }

    boolean hasDragonEgg(UUID var1, int var2) {
        if (this.globalState != null) {
            GlobalState var7 = this.globalState;
            if (var7.holdsDragonEgg(var1, 1249047238)) {
                return true;
            }
        }

        return false;
    }

    public String formatCd(UUID var1, Map<UUID, Long> var2, int var3) {
        long var9 = System.currentTimeMillis();
        Long var11 = (Long)var2.get(var1);
        if (var11 != null) {
            long var23 = var11;
            if (var23 > var9) {
                long var28 = var11;
                long var12 = (var28 - var9) / 1000L;
                long var14 = var12 / 60L;
                long var16 = var12 % 60L;
                if (var14 > 0L && var16 > 0L) {
                    return var14 + "m " + var16 + "s";
                }

                if (var14 > 0L) {
                    return var14 + "m";
                }

                return var16 + "s";
            }
        }

        return "&aReady!";
    }

    private static int tierFor(double var0, int var2) {
        if (var0 < 0.0) {
            return -1;
        } else if (var0 < 0.01) {
            return 0;
        } else if (var0 < 5.0) {
            return 1;
        } else if (var0 < 11.0) {
            return 3;
        } else if (var0 < 20.0) {
            return 4;
        } else if (var0 < 41.0) {
            return 5;
        } else if (var0 < 56.0) {
            return 6;
        } else if (var0 < 65.0) {
            return 7;
        } else if (var0 < 76.0) {
            return 8;
        } else if (var0 < 85.0) {
            return 9;
        } else if (var0 < 100.1) {
            return 10;
        } else {
            return var0 < 200.0 ? 11 : 12;
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent var1) {
        Player var8 = var1.getPlayer();
        UUID var9 = var8.getUniqueId();
        long var10 = System.currentTimeMillis();
        Map var31 = this.freezeExpiry;
        Long var12 = (Long)var31.get(var9);
        if (var12 != null) {
            long var36 = var12;
            if (var36 > var10) {
                Location var127 = var8.getLocation();
                var8.teleport(var127);
            }
        }

        Map var42 = this.fluxPlayerShotExpiry;
        Long var13 = (Long)var42.get(var9);
        if (var13 != null) {
            long var47 = var13;
            if (var47 > var10) {
                var1.setCancelled(true);
                return;
            }
        }

        Location var14 = var1.getFrom();
        Location var15 = var1.getTo();
        if (var15 != null) {
            double var57 = var14.getX();
            double var149 = var15.getX();
            if (var57 == var149) {
                double var61 = var14.getY();
                double var152 = var15.getY();
                if (var61 == var152) {
                    double var65 = var14.getZ();
                    double var155 = var15.getZ();
                    if (var65 == var155) {
                        return;
                    }
                }
            }

            PlayerDataStore var69 = this.players;
            PlayerData var16 = var69.get(var9, 1264010596);
            ItemStack var17 = var8.getInventory().getItemInOffHand();
            int var18 = BlissCoreItems.gemIdOf(var17, 382441079);
            BlissCoreItems.tierOf(var17, 1633828214);
            GemType var131 = GemType.FLUX;
            boolean var20 = GoldGem.hasAbsorbedSoul(var8, var131, 1603837876);
            int var133 = GemType.FLUX.id();
            if (var18 != var133 && !var20) {
                Map var116 = this.fluxMoved;
                boolean var124;
                if (var116.remove(var9) != null) {
                    var124 = true;
                } else {
                    var124 = false;
                }

                Map var119 = this.fluxSpeedFlags;
                boolean var125;
                if (var119.remove(var9) != null) {
                    var125 = true;
                } else {
                    var125 = false;
                }

                if (var124 || var125) {
                    var8.setWalkSpeed(0.2F);
                }
            } else {
                TagStore var83 = this.tags;
                if (!var83.isTagged(var9, 995676415)) {
                    if (var16.canUsePassives(2051432579)) {
                        String var87 = "flux";
                        String var135 = "flow-state-movement";
                        if (GemTuning.passive(var87, var135, true, 14309468)) {
                            Map var90 = this.fluxMoved;
                            Function var158 = var0 -> new AtomicInteger();
                            AtomicInteger var21 = (AtomicInteger)var90.computeIfAbsent(var9, var158);
                            Map var94 = this.fluxMovedExpiry;
                            Long var22 = (Long)var94.get(var9);
                            if (var22 != null) {
                                long var160 = var22;
                                if (var10 > var160) {
                                    var21.set(0);
                                    Map var103 = this.fluxSpeedFlags;
                                    var103.remove(var9);
                                }
                            }

                            Map var106 = this.fluxMovedExpiry;
                            Long var166 = var10 + 2000L;
                            var106.put(var9, var166);
                            int var23 = var21.incrementAndGet();
                            Map var111 = this.fluxSpeedFlags;
                            Function var167 = var0 -> new BitSet(20);
                            BitSet var24 = (BitSet)var111.computeIfAbsent(var9, var167);
                            applyWalkBucket(var8, var23, var24, 1686301191);
                        }
                    }
                }
            }
        }
    }

    private static void applyWalkBucket(Player var0, int var1, BitSet var2, int var3) {
        if (var1 >= 0 && var1 <= 100) {
            var0.setWalkSpeed(0.2F);
        }

        if (var1 >= 100 && var1 <= 200) {
            var0.setWalkSpeed(0.22F);
            announce(var0, var2, 0, 10, 1746655359);
        }

        if (var1 >= 200 && var1 <= 300) {
            var0.setWalkSpeed(0.24F);
            announce(var0, var2, 1, 20, 1746655359);
        }

        if (var1 >= 300 && var1 <= 400) {
            var0.setWalkSpeed(0.26F);
            announce(var0, var2, 2, 30, 1746655359);
        }

        if (var1 >= 400 && var1 <= 500) {
            var0.setWalkSpeed(0.28F);
            announce(var0, var2, 3, 40, 1746655359);
        }

        if (var1 >= 500 && var1 <= 600) {
            var0.setWalkSpeed(0.3F);
            announce(var0, var2, 4, 50, 1746655359);
        }

        if (var1 >= 600 && var1 <= 700) {
            var0.setWalkSpeed(0.32F);
            announce(var0, var2, 5, 60, 1746655359);
        }

        if (var1 >= 700 && var1 <= 800) {
            var0.setWalkSpeed(0.34F);
            announce(var0, var2, 6, 70, 1746655359);
        }

        if (var1 >= 900 && var1 <= 1000) {
            var0.setWalkSpeed(0.36F);
            announce(var0, var2, 7, 80, 1746655359);
        }

        if (var1 >= 1000 && var1 <= 1100) {
            var0.setWalkSpeed(0.38F);
            announce(var0, var2, 8, 90, 1746655359);
        }

        if (var1 >= 1100 && var1 <= 1200) {
            var0.setWalkSpeed(0.4F);
            announce(var0, var2, 9, 100, 1746655359);
        }

        if (var1 >= 1200 && var1 <= 1300) {
            var0.setWalkSpeed(0.42F);
            announce(var0, var2, 10, 110, 1746655359);
        }

        if (var1 >= 1300 && var1 <= 1400) {
            var0.setWalkSpeed(0.44F);
            announce(var0, var2, 11, 120, 1746655359);
        }

        if (var1 >= 1400 && var1 <= 1500) {
            var0.setWalkSpeed(0.46F);
            announce(var0, var2, 12, 130, 1746655359);
        }

        if (var1 >= 1500 && var1 <= 1600) {
            var0.setWalkSpeed(0.48F);
            announce(var0, var2, 13, 140, 1746655359);
        }

        if (var1 >= 1600 && var1 <= 1700) {
            var0.setWalkSpeed(0.5F);
            announce(var0, var2, 14, 150, 1746655359);
        }

        if (var1 >= 1700 && var1 <= 1800) {
            var0.setWalkSpeed(0.52F);
            announce(var0, var2, 15, 160, 1746655359);
        }

        if (var1 >= 1800 && var1 <= 1900) {
            var0.setWalkSpeed(0.54F);
            announce(var0, var2, 16, 170, 1746655359);
        }

        if (var1 >= 1900 && var1 <= 2000) {
            var0.setWalkSpeed(0.56F);
            announce(var0, var2, 17, 180, 1746655359);
        }

        if (var1 >= 2000 && var1 <= 2100) {
            var0.setWalkSpeed(0.58F);
            announce(var0, var2, 18, 190, 1746655359);
        }

        if (var1 >= 2100 && var1 <= 2200) {
            var0.setWalkSpeed(0.6F);
            announce(var0, var2, 19, 200, 1746655359);
        }
    }

    private static void announce(Player var0, BitSet var1, int var2, int var3, int var4) {
        if (!var1.get(var2)) {
            var1.set(var2);
            Component var13 = Text.legacy("&7Now moving at +" + var3 + "% speed!", 373192400);
            var0.sendMessage(var13);
        }
    }

    @EventHandler(
        priority = EventPriority.HIGHEST,
        ignoreCancelled = true
    )
    public void onAttackerDamage(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var9) {
            if (var1.getEntity() instanceof Player var177) {
                TagStore var36 = this.tags;
                UUID var127 = var9.getUniqueId();
                if (!var36.isTagged(var127, 995676415)) {
                    PlayerDataStore var39 = this.players;
                    UUID var129 = var9.getUniqueId();
                    if (var39.get(var129, 1264010596).canUsePassives(2051432579)) {
                        UUID var12 = var177.getUniqueId();
                        Map var46 = this.fluxMoved;
                        boolean var126;
                        if (var46.remove(var12) != null) {
                            var126 = true;
                        } else {
                            var126 = false;
                        }

                        Map var49 = this.fluxSpeedFlags;
                        boolean var125;
                        if (var49.remove(var12) != null) {
                            var125 = true;
                        } else {
                            var125 = false;
                        }

                        Map var52 = this.fluxDamaged;
                        var52.remove(var12);
                        if (var126 || var125) {
                            var177.setWalkSpeed(0.2F);
                        }

                        int var61 = BlissCoreItems.gemIdOf(var9.getInventory().getItemInOffHand(), 382441079);
                        int var135 = GemType.FLUX.id();
                        if (var61 != var135) {
                            GemType var136 = GemType.FLUX;
                            if (!GoldGem.hasAbsorbedSoul(var9, var136, 1603837876)) {
                                return;
                            }
                        }

                        UUID var16 = var9.getUniqueId();
                        long var17 = System.currentTimeMillis();
                        Map var69 = this.fluxDamaged;
                        Function var4 = var0 -> new AtomicInteger();
                        AtomicInteger var19 = (AtomicInteger)var69.computeIfAbsent(var16, var4);
                        Map var73 = this.fluxDamagedExpiry;
                        Long var20 = (Long)var73.get(var16);
                        if (var20 != null) {
                            long var158 = var20;
                            if (var17 > var158) {
                                var19.set(0);
                            }
                        }

                        Map var82 = this.fluxDamagedExpiry;
                        Long var164 = var17 + 3000L;
                        var82.put(var16, var164);
                        int var21 = var19.incrementAndGet();
                        double var22 = 0.0;
                        if (var21 >= 0 && var21 <= 3) {
                            var22 = 0.3;
                        }

                        if (var21 >= 3 && var21 <= 6) {
                            var22 += 0.4;
                        }

                        if (var21 >= 6 && var21 <= 9) {
                            var22 += 0.6;
                        }

                        if (var21 >= 9 && var21 <= 12) {
                            var22 += 0.8;
                        }

                        if (var21 >= 12 && var21 <= 15) {
                            var22++;
                        }

                        if (var21 >= 15 && var21 <= 99) {
                            var22++;
                        }

                        double var153 = var1.getDamage();
                        double var155 = var153 + var22;
                        var1.setDamage(var155);
                    }
                }
            }
        }
    }

    private static Player resolveAttacker(Entity var0, int var1) {
        if (var0 instanceof Player) {
            return (Player)var0;
        } else {
            if (var0 instanceof Projectile) {
                ProjectileSource var5 = ((Projectile)var0).getShooter();
                if (var5 instanceof Player) {
                    return (Player)var5;
                }
            }

            return null;
        }
    }

    @EventHandler(
        priority = EventPriority.HIGH,
        ignoreCancelled = true
    )
    public void onKinOvAltBuildsCounter(EntityDamageByEntityEvent var1) {
        Player var5 = resolveAttacker(var1.getDamager(), 1716513614);
        if (var5 != null) {
            Boolean var13 = Boolean.TRUE;
            Map var43 = this.kinOvAlt;
            UUID var62 = var5.getUniqueId();
            Object var44 = var43.get(var62);
            if (var13.equals(var44)) {
                for (Player var7 : Bukkit.getOnlinePlayers()) {
                    if (!var7.equals(var5)) {
                        UUID var8 = var7.getUniqueId();
                        Boolean var26 = Boolean.TRUE;
                        Map var47 = this.kinOvMain;
                        Object var48 = var47.get(var8);
                        if (var26.equals(var48)) {
                            Map<UUID, Integer> var29 = this.kinOvMainCounter;
                            Integer var65 = 0;
                            int var9 = var29.getOrDefault(var8, var65) + 1;
                            Map var35 = this.kinOvMainCounter;
                            Integer var67 = var9;
                            var35.put(var8, var67);
                            switch (var9) {
                                case 3:
                                    Component var55 = Text.legacy("&7You have &b3 &cATTACK &7points. &7Your next hit will do &b1.1x damage.", 373192400);
                                    var7.sendMessage(var55);
                                    break;
                                case 6:
                                    Component var59 = Text.legacy("&7You have &b6 &cATTACK &7points. &7Your next hit will do &b1.2x damage.", 373192400);
                                    var7.sendMessage(var59);
                                    break;
                                case 9:
                                    Component var57 = Text.legacy("&7You have &b9 &cATTACK &7points. &7Your next hit will do &b1.3x damage.", 373192400);
                                    var7.sendMessage(var57);
                                    break;
                                case 12:
                                    Component var61 = Text.legacy("&7You have &b12 &cATTACK &7points. &7Your next hit will do &b1.4x damage.", 373192400);
                                    var7.sendMessage(var61);
                                    break;
                                case 15:
                                    Component var53 = Text.legacy("&7You have &b15 &cATTACK &7points. &7Your next hit will do &b1.5x damage.", 373192400);
                                    var7.sendMessage(var53);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.HIGH,
        ignoreCancelled = true
    )
    public void onKinOvMainConsumesCounter(EntityDamageByEntityEvent var1) {
        Player var10 = resolveAttacker(var1.getDamager(), 1716513614);
        if (var10 != null) {
            if (var1.getEntity() instanceof LivingEntity var11) {
                UUID var134 = var10.getUniqueId();
                Boolean var28 = Boolean.TRUE;
                Map var91 = this.kinOvMain;
                Object var92 = var91.get(var134);
                if (var28.equals(var92)) {
                    PlayerDataStore var31 = this.players;
                    if (!var31.get(var134, 1264010596).isBroken(430675503)) {
                        if (var11 instanceof Player var13) {
                            TrustStore var39 = this.trust;
                            UUID var117 = var13.getUniqueId();
                            if (var39.isTrusted(var134, var117, 1736364606)) {
                                return;
                            }
                        }

                        Map<UUID, Integer> var42 = this.kinOvMainCounter;
                        Integer var119 = 0;
                        int var135 = var42.getOrDefault(var134, var119);
                        double var136;
                        if (var135 >= 3 && var135 <= 6) {
                            var136 = 1.1;
                        } else if (var135 >= 6 && var135 <= 9) {
                            var136 = 1.4;
                        } else if (var135 >= 9 && var135 <= 12) {
                            var136 = 1.5;
                        } else if (var135 >= 12 && var135 <= 15) {
                            var136 = 1.7;
                        } else {
                            if (var135 < 15 || var135 > 99) {
                                return;
                            }

                            var136 = 2.0;
                        }

                        double var51 = var1.getDamage();
                        double var53 = var51 * var136;
                        PlayerDataStore var123 = this.players;
                        double var125 = EnergyScale.power(var123.get(var134, 1264010596), 1425120665);
                        double var14 = var53 * var125;
                        var1.setCancelled(true);
                        Map var59 = this.kinOvMainCounter;
                        Integer var128 = 0;
                        var59.put(var134, var128);
                        Vector var63 = var11.getLocation().toVector();
                        Vector var102 = var10.getLocation().toVector();
                        Vector var16 = var63.subtract(var102);
                        if (var16.lengthSquared() < 1.0E-6) {
                            Vector var69 = new Vector(0.0, 0.4, 0.0);
                            var16 = var69;
                        } else {
                            var16.normalize().multiply(0.4);
                        }

                        var11.damage(var14, var10);
                        var11.setVelocity(var16);
                    }
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.NORMAL,
        ignoreCancelled = true
    )
    public void onDoubleDamage(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Player var7) {
            TagStore var15 = this.tags;
            UUID var20 = var7.getUniqueId();
            if (!var15.isTagged(var20, 995676415)) {
                Boolean var17 = Boolean.TRUE;
                Map var22 = this.doubleDamageFlag;
                UUID var29 = var7.getUniqueId();
                Object var23 = var22.get(var29);
                if (var17.equals(var23)) {
                    double var27 = var1.getDamage() * 1.5;
                    var1.setDamage(var27);
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    public void onIncomingDamageStored(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Player var10) {
            TagStore var25 = this.tags;
            UUID var66 = var10.getUniqueId();
            if (!var25.isTagged(var66, 995676415)) {
                boolean var65;
                label39: {
                    ItemStack var105 = var10.getInventory().getItemInOffHand();
                    ItemStack var12 = var10.getInventory().getItemInMainHand();
                    int var34 = BlissCoreItems.gemIdOf(var105, 382441079);
                    int var68 = GemType.FLUX.id();
                    if (var34 != var68) {
                        int var36 = BlissCoreItems.gemIdOf(var12, 382441079);
                        int var70 = GemType.FLUX.id();
                        if (var36 != var70) {
                            GemType var71 = GemType.FLUX;
                            if (!GoldGem.hasAbsorbedSoul(var10, var71, 1603837876)) {
                                var65 = false;
                                break label39;
                            }
                        }
                    }

                    var65 = true;
                }

                if (var65) {
                    PlayerDataStore var41 = this.players;
                    UUID var73 = var10.getUniqueId();
                    if (!var41.get(var73, 1264010596).isBroken(430675503)) {
                        if (!var10.isDead()) {
                            double var47 = var1.getFinalDamage();
                            double var78 = var10.getHealth();
                            if (!(var47 >= var78)) {
                                double var14 = var1.getFinalDamage() / 6.0;
                                UUID var16 = var10.getUniqueId();
                                String var57 = "flux";
                                String var74 = "static-burst-cap";
                                double var17 = GemTuning.power(var57, var74, 200.0, 1503850832);
                                Map var61 = this.storedDamage;
                                Map<UUID, Double> var93 = this.storedDamage;
                                Double var102 = 0.0;
                                double var96 = var93.getOrDefault(var16, var102);
                                double var98 = var96 + var14;
                                Double var88 = Math.min(var17, var98);
                                var61.put(var16, var88);
                                BukkitScheduler var63 = Bukkit.getScheduler();
                                BlissPlugin var77 = this.plugin;
                                Runnable var90 = () -> {
                                    Map var13 = this.storedDamage;
                                    Double var10x = (Double)var13.get(var16);
                                    if (var10x != null) {
                                        double var18 = var10x;
                                        double var11 = var18 - var14;
                                        Map var23 = this.storedDamage;
                                        Double var30 = Math.max(0.0, var11);
                                        var23.put(var16, var30);
                                    }
                                };
                                var63.runTaskLater(var77, var90, 3000L);
                                return;
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
    public void onVictimDamage(EntityDamageByEntityEvent var1) {
        if (var1.getEntity() instanceof Player var4) {
            GemType var3 = GemType.FLUX;
            boolean var39 = GoldGem.hasAbsorbedSoul(var4, var3, 1603837876);
            ItemStack var6 = var4.getInventory().getItemInOffHand();
            if (!var39) {
                int var20 = BlissCoreItems.gemIdOf(var6, 382441079);
                int var34 = GemType.FLUX.id();
                if (var20 != var34) {
                    return;
                }
            }

            if (var39 || BlissCoreItems.tierOf(var6, 1633828214) == 2) {
                UUID var7 = var4.getUniqueId();
                Map var27 = this.fluxMoved;
                var27.remove(var7);
                var4.setWalkSpeed(0.2F);
                Map var31 = this.fluxSpeedFlags;
                var31.remove(var7);
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    public void onDamageDumpsCharge(EntityDamageEvent var1) {
        if (var1.getEntity() instanceof Player var10) {
            UUID var82 = var10.getUniqueId();
            Map<UUID, Double> var26 = this.fluxKinetic;
            Double var62 = 0.0;
            double var12 = var26.getOrDefault(var82, var62);
            if (!(var12 <= 0.0)) {
                String var34 = "flux";
                String var57 = "charge-lost-on-damage";
                double var14 = GemTuning.power(var34, var57, 100.0, 1503850832);
                if (!(var14 <= 0.0)) {
                    double var55;
                    if (var14 >= 100.0) {
                        var55 = 0.0;
                    } else {
                        double var80 = var14 / 100.0;
                        double var73 = 1.0 - var80;
                        var55 = var12 * var73;
                    }

                    if (var55 <= 0.0) {
                        Map var49 = this.fluxKinetic;
                        var49.remove(var82);
                        Component var60 = Text.legacy("&b\ud83d\udd2e &fThe hit knocked your stored charge out of you.", 373192400);
                        var10.sendMessage(var60);
                    } else {
                        Map var53 = this.fluxKinetic;
                        Double var79 = var55;
                        var53.put(var82, var79);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChargedCreeperDamage(EntityDamageByEntityEvent var1) {
        if (var1.getEntity() instanceof Player var4) {
            Entity var6 = var1.getDamager();
            if (var6 instanceof Creeper) {
                if (((Creeper)var6).isPowered()) {
                    TagStore var26 = this.tags;
                    UUID var55 = var4.getUniqueId();
                    if (!var26.isTagged(var55, 995676415)) {
                        PlayerDataStore var29 = this.players;
                        UUID var57 = var4.getUniqueId();
                        if (var29.get(var57, 1264010596).energy(1160853318) > 5) {
                            ItemStack var7 = var4.getInventory().getItemInOffHand();
                            ItemStack var8 = var4.getInventory().getItemInMainHand();
                            int var40 = BlissCoreItems.gemIdOf(var7, 382441079);
                            int var60 = GemType.FLUX.id();
                            boolean var54;
                            if (var40 == var60 && BlissCoreItems.tierOf(var7, 1633828214) == 2) {
                                var54 = true;
                            } else {
                                var54 = false;
                            }

                            int var44 = BlissCoreItems.gemIdOf(var8, 382441079);
                            int var63 = GemType.FLUX.id();
                            boolean var53;
                            if (var44 == var63 && BlissCoreItems.tierOf(var8, 1633828214) == 2) {
                                var53 = true;
                            } else {
                                var53 = false;
                            }

                            GemType var65 = GemType.FLUX;
                            boolean var11 = GoldGem.hasAbsorbedSoul(var4, var65, 1603837876);
                            if (var54 || var53 || var11) {
                                var1.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
        priority = EventPriority.MONITOR,
        ignoreCancelled = true
    )
    public void onKinOvAltBreak(BlockBreakEvent var1) {
        Player var5 = var1.getPlayer();
        Boolean var11 = Boolean.TRUE;
        Map var40 = this.kinOvAlt;
        UUID var50 = var5.getUniqueId();
        Object var41 = var40.get(var50);
        if (var11.equals(var41)) {
            if (var1.getBlock().getType().isSolid()) {
                for (Player var7 : Bukkit.getOnlinePlayers()) {
                    if (!var7.equals(var5)) {
                        UUID var8 = var7.getUniqueId();
                        Boolean var28 = Boolean.TRUE;
                        Map var44 = this.kinOvMain;
                        Object var45 = var44.get(var8);
                        if (var28.equals(var45)) {
                            Map<UUID, Integer> var31 = this.kinOvMining;
                            Integer var53 = 0;
                            int var9 = var31.getOrDefault(var8, var53) + 1;
                            Map var37 = this.kinOvMining;
                            Integer var55 = var9;
                            var37.put(var8, var55);
                            applyKinOvHaste(var7, var9, 1474715120);
                        }
                    }
                }
            }
        }
    }

    private static void applyKinOvHaste(Player var0, int var1, int var2) {
        byte var19 = switch (var1) {
            case 5 -> 1;
            case 10 -> 2;
            case 15 -> 3;
            case 20 -> 4;
            case 25 -> 5;
            default -> 0;
        };
        if (var19 != 0) {
            int var13 = 20 * var19;
            String var24;
            if (var19 == 1) {
                var24 = "";
            } else {
                var24 = "s";
            }

            Component var22 = Text.legacy(
                "&7You have &b" + var19 + " &eMINING &7point" + var24 + ", giving you Haste level &b" + var19 + " &7for &b" + var13 + "s&7.", 373192400
            );
            var0.sendMessage(var22);
            PotionEffectType var26 = PotionEffectType.HASTE;
            int var28 = var13 * 20;
            int var30 = var19 - 1;
            PotionEffect var23 = new PotionEffect(var26, var28, var30, false, false, true);
            GrantedEffect.grant(var0, var23, 491118892);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent var1) {
        Player var7 = var1.getPlayer();
        TagStore var17 = this.tags;
        UUID var59 = var7.getUniqueId();
        if (!var17.isTagged(var59, 995676415)) {
            PlayerDataStore var20 = this.players;
            UUID var61 = var7.getUniqueId();
            if (var20.get(var61, 1264010596).canUsePassives(2051432579)) {
                String var24 = "flux";
                String var62 = "flow-state-mining";
                if (GemTuning.passive(var24, var62, true, 14309468)) {
                    if (var1.getBlock().getType().isSolid()) {
                        int var34 = BlissCoreItems.gemIdOf(var7.getInventory().getItemInOffHand(), 382441079);
                        int var64 = GemType.FLUX.id();
                        if (var34 != var64) {
                            GemType var65 = GemType.FLUX;
                            if (!GoldGem.hasAbsorbedSoul(var7, var65, 1603837876)) {
                                return;
                            }
                        }

                        UUID var10 = var7.getUniqueId();
                        long var11 = System.currentTimeMillis();
                        Map var42 = this.fluxBroken;
                        Function var70 = var0 -> new AtomicInteger();
                        AtomicInteger var13 = (AtomicInteger)var42.computeIfAbsent(var10, var70);
                        Map var46 = this.fluxBrokenExpiry;
                        Long var14 = (Long)var46.get(var10);
                        if (var14 != null) {
                            long var72 = var14;
                            if (var11 > var72) {
                                var13.set(0);
                            }
                        }

                        Map var55 = this.fluxBrokenExpiry;
                        Long var78 = var11 + 2000L;
                        var55.put(var10, var78);
                        var13.incrementAndGet();
                    }
                }
            }
        }
    }
}
