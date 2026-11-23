package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.AchievementStore;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.MegaMaceListener;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.Progression2Rules;
import com.bliss.core.TrustStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.honored.EnchantLimits;
import com.bliss.honored.GemAbilityRules;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public final class WealthGemListener implements Listener {
    private static final long DURABILITY_CD_MS = 1000L;
    private static final long RICH_RUSH_DURATION_MS = 180000L;
    private static final long AMPLIF_DURATION_MS = 30000L;
    private static final int AMPLIF_TRUSTED_RADIUS = 2;
    private static final int ITEM_LOCK_TICKS = 600;
    private static final int ITEM_LOCK_RADIUS = 2;
    public final Map<UUID, Long> cd1;
    public final Map<UUID, Long> cd2;
    public final Map<UUID, Long> cd3;
    public final Map<UUID, Long> richRushExpiry;
    public final Map<UUID, Long> amplifExpiry;
    public final Map<UUID, Long> unfortunateExpiry;
    private final Map<UUID, Long> durabilityCd;
    private final Map<UUID, Integer> lastInteractTick;
    private final Map<UUID, Integer> lastLeftClickTick;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final GlobalState globalState;
    private final TrustStore trust;
    private final Map<UUID, BossBar> unfortunateBars;
    private final Map<UUID, Long> itemLockExpiry;
    private static final long UNFORTUNATE_DURATION_MS = 40000L;
    private static final int UNFORTUNATE_CANCEL_DEFAULT = 25;
    private static final double UNFORTUNATE_LINK_STEP = 0.3;
    private static final float UNFORTUNATE_LINK_DUST = 1.6F;
    private static final double UNFORTUNATE_LINK_MAX_LEN = 24.0;
    private static final Map<Enchantment, Integer> AMPLIFY_MAX;
    private static final long REVERT_PERIOD_TICKS = 100L;
    private BukkitTask revertTask;

    public boolean amplified(UUID var1, int var2) {
        Map<UUID, Long> var7 = this.amplifExpiry;
        Long var14 = 0L;
        long var10 = var7.getOrDefault(var1, var14);
        long var15 = System.currentTimeMillis();
        boolean var13;
        if (var10 > var15) {
            var13 = true;
        } else {
            var13 = false;
        }

        return var13;
    }

    public WealthGemListener(BlissPlugin var1, PlayerDataStore var2, GlobalState var3, TrustStore var4, int var5) {
        ConcurrentHashMap var7 = new ConcurrentHashMap();
        this.cd1 = var7;
        ConcurrentHashMap var23 = new ConcurrentHashMap();
        this.cd2 = var23;
        ConcurrentHashMap var24 = new ConcurrentHashMap();
        this.cd3 = var24;
        ConcurrentHashMap var25 = new ConcurrentHashMap();
        this.richRushExpiry = var25;
        ConcurrentHashMap var26 = new ConcurrentHashMap();
        this.amplifExpiry = var26;
        ConcurrentHashMap var27 = new ConcurrentHashMap();
        this.unfortunateExpiry = var27;
        ConcurrentHashMap var28 = new ConcurrentHashMap();
        this.durabilityCd = var28;
        ConcurrentHashMap var29 = new ConcurrentHashMap();
        this.lastInteractTick = var29;
        ConcurrentHashMap var30 = new ConcurrentHashMap();
        this.lastLeftClickTick = var30;
        ConcurrentHashMap var31 = new ConcurrentHashMap();
        this.unfortunateBars = var31;
        HashMap var32 = new HashMap();
        this.itemLockExpiry = var32;
        this.plugin = var1;
        this.players = var2;
        this.globalState = var3;
        this.trust = var4;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var19 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var19 != var3) {
            Action var21 = var1.getAction();
            Action var71 = Action.RIGHT_CLICK_BLOCK;
            if (var21 != var71) {
                return;
            }
        }

        Player var9 = var1.getPlayer();
        UUID var10 = var9.getUniqueId();
        int var11 = Bukkit.getCurrentTick();
        Map var28 = this.lastInteractTick;
        Integer var12 = (Integer)var28.get(var10);
        if (var12 != null) {
            int var33 = var12;
            if (var33 == var11) {
                return;
            }
        }

        Map var35 = this.lastInteractTick;
        Integer var85 = var11;
        var35.put(var10, var85);
        ItemStack var13 = var9.getInventory().getItemInMainHand();
        GemType var75 = GemType.WEALTH;
        boolean var14 = GoldGem.isWieldingSoulMain(var9, var75, 244096655);
        int var43 = BlissCoreItems.gemIdOf(var13, 382441079);
        int var77 = GemType.WEALTH.id();
        if (var43 == var77 || var14) {
            if (var14 || BlissCoreItems.tierOf(var13, 1633828214) == 2) {
                if (!EntityClickGuard.recentlyClickedEntity(var10, 1923725760)) {
                    PlayerDataStore var51 = this.players;
                    if (var51.get(var10, 1264010596).canUsePowers(1616892701)) {
                        long var16 = System.currentTimeMillis();
                        Map var58 = this.cd2;
                        Long var18 = (Long)var58.get(var10);
                        if (var18 != null) {
                            long var63 = var18;
                            if (var63 > var16) {
                                return;
                            }
                        }

                        Component var82 = Text.legacy("<##0EC912>\ud83d\udd2e <##B8FFFB>You have used <##0EC912>Rich Rush", 373192400);
                        var9.sendMessage(var82);
                        Map var68 = this.richRushExpiry;
                        String var5 = "wealth";
                        String var6 = "rich-rush";
                        long var99 = Math.round(GemTuning.duration(var5, var6, 180.0, 812730832) * 1000.0);
                        Long var92 = var16 + var99;
                        var68.put(var10, var92);
                        String var101 = "rich-rush";
                        this.applyCooldown(var9, var16, var101, 2090888723);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent var1) {
        EquipmentSlot var24 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var24 == var3) {
            Player var9 = var1.getPlayer();
            ItemStack var10 = var9.getInventory().getItemInMainHand();
            GemType var109 = GemType.WEALTH;
            boolean var11 = GoldGem.isWieldingSoulMain(var9, var109, 244096655);
            int var33 = BlissCoreItems.gemIdOf(var10, 382441079);
            int var111 = GemType.WEALTH.id();
            if (var33 == var111 || var11) {
                if (var11 || BlissCoreItems.tierOf(var10, 1633828214) == 2) {
                    UUID var12 = var9.getUniqueId();
                    EntityClickGuard.mark(var12, 1276880015);
                    PlayerDataStore var42 = this.players;
                    if (var42.get(var12, 1264010596).canUsePowers(1616892701)) {
                        long var14 = System.currentTimeMillis();
                        Map var49 = this.cd2;
                        Long var16 = (Long)var49.get(var12);
                        if (var16 != null) {
                            long var54 = var16;
                            if (var54 > var14) {
                                return;
                            }
                        }

                        Entity var17 = var1.getRightClicked();
                        Location var18 = var17.getLocation();
                        String var140;
                        if (var17 instanceof Player) {
                            var140 = ((Player)var17).getName();
                        } else {
                            var140 = var17.getName();
                        }

                        Component var119 = Text.legacy(
                            "<##0EC912>\ud83d\udd2e <##B8FFFB>You have used <##0EC912>Amplification <##B8FFFB>on <##0EC912>" + var140, 373192400
                        );
                        var9.sendMessage(var119);
                        Location var121 = var9.getLocation();
                        this.drawAmplifRings(var121, 42691202);
                        String var63 = "wealth";
                        String var122 = "amplification";
                        long var19 = Math.round(GemTuning.duration(var63, var122, 30.0, 812730832) * 1000.0);
                        String var145 = "wealth";
                        String var6 = "amplification";
                        double var146 = GemTuning.radius(var145, var6, 2.0, 1058601812);
                        int var21 = (int)Math.max(1.0, var146);
                        Map var76 = this.amplifExpiry;
                        Long var152 = var14 + var19;
                        var76.put(var12, var152);
                        String var170 = "amplification";
                        this.applyCooldown(var9, var14, var170, 2090888723);

                        for (Player var23 : var17.getWorld().getPlayers()) {
                            if (!var23.equals(var9)) {
                                Location var91 = var23.getLocation();
                                double var92 = var91.distanceSquared(var18);
                                double var157 = (double)(var21 * var21);
                                if (!(var92 > var157)) {
                                    TrustStore var96 = this.trust;
                                    UUID var160 = var23.getUniqueId();
                                    if (var96.isTrusted(var12, var160, 1736364606)) {
                                        Component var131 = Text.legacy(
                                            "<##0EC912>\ud83d\udd2e <##B8FFFB>You have been affected with <##0EC912>Amplification <##B8FFFB>by <##0EC912>"
                                                + var9.getName(),
                                            373192400
                                        );
                                        var23.sendMessage(var131);
                                        this.upgradeInventory(var23, 1340562187);
                                        Map var102 = this.amplifExpiry;
                                        UUID var134 = var23.getUniqueId();
                                        Long var165 = var14 + var19;
                                        var102.put(var134, var165);
                                    }
                                }
                            }
                        }

                        if (this.upgradeInventory(var9, 1340562187) > 0) {
                            AchievementStore var108 = this.plugin.advancements(1975441698);
                            AchievementStore.Advancement var166 = AchievementStore.Advancement.BOUNDARY_BREAK;
                            var108.unlock(var9, var166, 1132982324);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent var1) {
        Action var14 = var1.getAction();
        Action var3 = Action.LEFT_CLICK_AIR;
        if (var14 != var3) {
            Action var16 = var1.getAction();
            Action var53 = Action.LEFT_CLICK_BLOCK;
            if (var16 != var53) {
                return;
            }
        }

        Player var7;
        UUID var8;
        label42: {
            var7 = var1.getPlayer();
            var8 = var7.getUniqueId();
            int var9 = Bukkit.getCurrentTick();
            Map var23 = this.lastLeftClickTick;
            Integer var10 = (Integer)var23.get(var8);
            if (var10 != null) {
                int var28 = var10;
                if (var28 == var9) {
                    break label42;
                }
            }

            Map var51 = this.lastLeftClickTick;
            Integer var68 = var9;
            var51.put(var8, var68);
        }

        ItemStack var11 = var7.getInventory().getItemInMainHand();
        GemType var56 = GemType.WEALTH;
        boolean var12 = GoldGem.isWieldingSoulMain(var7, var56, 244096655);
        int var35 = BlissCoreItems.gemIdOf(var11, 382441079);
        int var58 = GemType.WEALTH.id();
        if (var35 != var58 && !var12) {
            this.tryToolOnCooldownClick(var7, var1, 527161414);
        } else if (var12 || BlissCoreItems.tierOf(var11, 1633828214) == 2) {
            PlayerDataStore var42 = this.players;
            if (var42.get(var8, 1264010596).canUsePowers(1616892701)) {
                if (GemAbilityRules.on(GemAbilityRules.GemAbility.WEALTH_ITEM_LOCK, 425337338)) {
                    BukkitScheduler var48 = Bukkit.getScheduler();
                    BlissPlugin var63 = this.plugin;
                    Runnable var66 = () -> {
                        if (var7.isOnline()) {
                            long var7x = System.currentTimeMillis();
                            Map var14x = this.cd3;
                            Long var9x = (Long)var14x.get(var8);
                            if (var9x != null) {
                                long var19 = var9x;
                                if (var19 > var7x) {
                                    return;
                                }
                            }

                            this.applyItemLockCooldown(var7, var7x, 294200488);
                            Component var28x = Text.legacy("<##0EC912>\ud83d\udd2e <##B8FFFB>You have used &f\ud83c\udf40<##0EC912>Item Lock", 373192400);
                            var7.sendMessage(var28x);
                            Location var30 = var7.getLocation();
                            this.drawItemLockRings(var30, 353532495);
                            this.applyItemLockToNearby(var7, 219032172);
                        }
                    };
                    var48.runTaskLater(var63, var66, 1L);
                }
            }
        }
    }

    private void applyItemLockCooldown(Player var1, long var2, int var4) {
        GlobalState var18 = this.globalState;
        boolean var15 = var18.holdsDragonEgg(var1, 715974514);
        PlayerDataStore var21 = this.players;
        UUID var48 = var1.getUniqueId();
        PlayerData var16 = var21.get(var48, 1264010596);
        int var17 = var16.energy(1160853318);
        long var64;
        if (var17 > 5) {
            long var41;
            if (var15) {
                var41 = 100000L;
            } else {
                var41 = 200000L;
            }

            var64 = var41;
        } else if (var17 == 5) {
            long var28;
            if (var15) {
                var28 = 120000L;
            } else {
                var28 = 240000L;
            }

            var64 = var28;
        } else if (var17 == 4) {
            long var43;
            if (var15) {
                var43 = 145000L;
            } else {
                var43 = 289000L;
            }

            var64 = var43;
        } else if (var17 == 3) {
            long var45;
            if (var15) {
                var45 = 165000L;
            } else {
                var45 = 335000L;
            }

            var64 = var45;
        } else {
            long var37;
            if (var15) {
                var37 = 192000L;
            } else {
                var37 = 385000L;
            }

            var64 = var37;
        }

        Map var31 = this.cd3;
        UUID var52 = var1.getUniqueId();
        String var9 = "wealth";
        String var10 = "item-lock";
        long var58 = GemTuning.cd(var9, var10, 200.0, var64, 1621983041);
        int var63 = var16.energy(1160853318);
        long var60 = EnergyScale.cd(var58, var63, 907518618);
        Long var57 = var2 + var60;
        var31.put(var52, var57);
    }

    private void drawItemLockRings(Location var1, int var2) {
        drawRing(var1, 0.75, 621161786);
        drawRing(var1, 1.0, 621161786);
        BukkitScheduler var10 = Bukkit.getScheduler();
        BlissPlugin var17 = this.plugin;
        Runnable var20 = () -> {
            drawRing(var1, 1.25, 621161786);
            drawRing(var1, 1.5, 621161786);
        };
        var10.runTaskLater(var17, var20, 1L);
        BukkitScheduler var12 = Bukkit.getScheduler();
        BlissPlugin var19 = this.plugin;
        Runnable var22 = () -> {
            drawRing(var1, 1.75, 621161786);
            drawRing(var1, 2.0, 621161786);
        };
        var12.runTaskLater(var19, var22, 2L);
    }

    private void applyItemLockToNearby(Player var1, int var2) {
        String var3 = "wealth";
        String var4 = "item-lock";
        final int var12 = (int)Math.round(GemTuning.duration(var3, var4, 30.0, 812730832) * 20.0);
        String var96 = "wealth";
        String var7 = "item-lock";
        double var97 = GemTuning.radius(var96, var7, 2.0, 1058601812);
        int var13 = (int)Math.max(1.0, var97);

        for (Player var15 : var1.getWorld().getPlayers()) {
            if (!var15.equals(var1)) {
                Location var42 = var15.getLocation();
                Location var72 = var1.getLocation();
                double var43 = var42.distanceSquared(var72);
                double var101 = (double)(var13 * var13);
                if (!(var43 > var101)) {
                    TrustStore var47 = this.trust;
                    UUID var74 = var1.getUniqueId();
                    UUID var104 = var15.getUniqueId();
                    if (!var47.isTrusted(var74, var104, 1736364606)) {
                        ItemStack var16 = var15.getInventory().getItemInMainHand();
                        if (var16 != null) {
                            Material var54 = var16.getType();
                            Material var75 = Material.AIR;
                            if (var54 != var75) {
                                Material var56 = var16.getType();
                                Material var76 = Material.PRISMARINE_SHARD;
                                if (var56 != var76) {
                                    Material var58 = var16.getType();
                                    Material var77 = Material.AMETHYST_SHARD;
                                    if (var58 != var77) {
                                        Material var79 = var16.getType();
                                        var15.setCooldown(var79, var12);
                                        Map var61 = this.itemLockExpiry;
                                        UUID var81 = var15.getUniqueId();
                                        long var106 = System.currentTimeMillis();
                                        long var127 = (long)var12 * 50L;
                                        Long var110 = var106 + var127;
                                        var61.put(var81, var110);
                                        String var82 = "wealth";
                                        String var111 = "item-lock";
                                        int var85 = (int)GemTuning.duration(var82, var111, 30.0, 812730832);
                                        String var113 = var1.getName();
                                        Component var87 = Text.legacy(
                                            "<##0EC912>\ud83d\udd2e <##B8FFFB>Your held item has been locked for <##0EC912>"
                                                + var85
                                                + " seconds <##B8FFFB>by <##0EC912>"
                                                + var113,
                                            373192400
                                        );
                                        var15.sendMessage(var87);
                                        String var64 = "§fItem Lock";
                                        BarColor var88 = BarColor.GREEN;
                                        BarStyle var114 = BarStyle.SOLID;
                                        BarFlag[] var122 = new BarFlag[0];
                                        final BossBar var17 = Bukkit.createBossBar(var64, var88, var114, var122);
                                        var17.setProgress(1.0);
                                        var17.addPlayer(var15);
                                        BukkitRunnable var68 = new BukkitRunnable() {
                                            int t = 0;

                                            public void run() {
                                                int var21 = this.t + 1;
                                                this.t = var21;
                                                double var30 = (double)this.t;
                                                int var39 = var12;
                                                double var37 = (double)Math.max(1, var39);
                                                double var32 = var30 / var37;
                                                double var7 = 1.0 - var32;
                                                BossBar var14 = var17;
                                                double var34 = Math.min(1.0, var7);
                                                double var24 = Math.max(0.0, var34);
                                                var14.setProgress(var24);
                                                int var16 = this.t;
                                                int var27 = var12;
                                                if (var16 >= var27) {
                                                    var17.removeAll();
                                                    this.cancel();
                                                }
                                            }
                                        };
                                        BlissPlugin var93 = this.plugin;
                                        var68.runTaskTimer(var93, 1L, 1L);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isItemLocked(UUID var1, int var2) {
        Map var8 = this.itemLockExpiry;
        Long var7 = (Long)var8.get(var1);
        if (var7 != null) {
            long var13 = var7;
            long var5 = System.currentTimeMillis();
            if (var13 > var5) {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onLockedAttack(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var7) {
            UUID var26 = var7.getUniqueId();
            if (this.isItemLocked(var26, 1241787374)) {
                ItemStack var33 = var7.getInventory().getItemInMainHand();
                if (var33 != null) {
                    Material var21 = var33.getType();
                    Material var27 = Material.AIR;
                    if (var21 != var27) {
                        Material var29 = var33.getType();
                        if (!var7.hasCooldown(var29)) {
                            return;
                        }

                        var1.setCancelled(true);
                        Location var32 = var7.getLocation();
                        Sound var4 = Sound.BLOCK_ANVIL_LAND;
                        var7.playSound(var32, var4, 0.2F, 1.0F);
                        return;
                    }
                }
            }
        }
    }

    private void tryToolOnCooldownClick(Player var1, PlayerInteractEvent var2, int var3) {
        UUID var21 = var1.getUniqueId();
        if (this.isItemLocked(var21, 1241787374)) {
            ItemStack var9 = var1.getInventory().getItemInMainHand();
            if (var9 != null) {
                Material var16 = var9.getType();
                Material var22 = Material.AIR;
                if (var16 != var22) {
                    Material var24 = var9.getType();
                    if (!var1.hasCooldown(var24)) {
                        return;
                    }

                    var2.setCancelled(true);
                    Location var27 = var1.getLocation();
                    Sound var6 = Sound.BLOCK_ANVIL_LAND;
                    var1.playSound(var27, var6, 0.2F, 1.0F);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onLockedRightClick(PlayerInteractEvent var1) {
        Action var5 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var5 != var3) {
            Action var7 = var1.getAction();
            Action var9 = Action.RIGHT_CLICK_BLOCK;
            if (var7 != var9) {
                return;
            }
        }

        Player var11 = var1.getPlayer();
        this.tryToolOnCooldownClick(var11, var1, 527161414);
    }

    private static long unfortunateMs(int var0) {
        String var1 = "wealth";
        String var2 = "unfortunate";
        return Math.round(GemTuning.duration(var1, var2, 40.0, 812730832) * 1000.0);
    }

    public static int unfortunateCancelPercent(int var0) {
        String var1 = "wealth";
        String var2 = "unfortunate-cancel";
        return (int)GemTuning.chance(var1, var2, 25.0, 1091538029);
    }

    public static int amplificationLevels(int var0) {
        String var1 = "wealth";
        String var2 = "amplification-levels";
        return (int)GemTuning.power(var1, var2, 1.0, 1503850832);
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onUnfortunateTrigger(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var8) {
            if (var1.getEntity() instanceof Player var109) {
                if (!var109.equals(var8)) {
                    DamageCause var30 = var1.getCause();
                    DamageCause var72 = DamageCause.SONIC_BOOM;
                    if (var30 != var72) {
                        DamageCause var32 = var1.getCause();
                        DamageCause var73 = DamageCause.THORNS;
                        if (var32 != var73) {
                            ItemStack var110 = var8.getInventory().getItemInMainHand();
                            GemType var74 = GemType.WEALTH;
                            boolean var11 = GoldGem.isWieldingSoulMain(var8, var74, 244096655);
                            int var39 = BlissCoreItems.gemIdOf(var110, 382441079);
                            int var76 = GemType.WEALTH.id();
                            if (var39 != var76 && !var11) {
                                return;
                            }

                            if (!var11 && BlissCoreItems.tierOf(var110, 1633828214) != 2) {
                                return;
                            }

                            PlayerDataStore var45 = this.players;
                            UUID var79 = var8.getUniqueId();
                            if (!var45.get(var79, 1264010596).canUsePowers(1616892701)) {
                                return;
                            }

                            long var13 = System.currentTimeMillis();
                            Map var52 = this.cd3;
                            UUID var81 = var8.getUniqueId();
                            Long var15 = (Long)var52.get(var81);
                            if (var15 != null) {
                                long var57 = var15;
                                if (var57 > var13) {
                                    return;
                                }
                            }

                            TrustStore var61 = this.trust;
                            UUID var83 = var8.getUniqueId();
                            UUID var100 = var109.getUniqueId();
                            if (var61.isTrusted(var83, var100, 1736364606)) {
                                Component var98 = Text.legacy("&2\ud83d\udd2e &fYou cannot cast negative powers on allies!", 373192400);
                                var8.sendMessage(var98);
                                return;
                            }

                            Map var64 = this.unfortunateExpiry;
                            UUID var85 = var109.getUniqueId();
                            long var6 = unfortunateMs(1452044337);
                            Long var105 = var13 + var6;
                            var64.put(var85, var105);
                            Component var89 = Text.legacy("&2\ud83d\udd2e &bYou used §a\ud83c\udf40§2Unfortunate &bskill on &2" + var109.getName(), 373192400);
                            var8.sendMessage(var89);
                            Component var93 = Text.legacy(
                                "&2\ud83d\udd2e &bYou have been affected with §a\ud83c\udf40§2Unfortunate &bby &e" + var8.getName(), 373192400
                            );
                            var109.sendMessage(var93);
                            this.showUnfortunateBar(var109, 1140913778);
                            this.startUnfortunateLink(var8, var109, 97371365);
                            this.applyUnfortunateCooldown(var8, var13, 1215086490);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void startUnfortunateLink(final Player var1, final Player var2, int var3) {
        final UUID var9 = var2.getUniqueId();
        BukkitRunnable var11 = new BukkitRunnable() {
            public void run() {
                if (var1.isOnline() && var2.isOnline() && !var2.isDead()) {
                    WealthGemListener var13 = WealthGemListener.this;
                    UUID var22 = var9;
                    if (var13.isUnfortunate(var22, 63502028)) {
                        World var17 = var1.getWorld();
                        World var25 = var2.getWorld();
                        if (var17.equals(var25)) {
                            WealthGemListener var20 = WealthGemListener.this;
                            Player var27 = var1;
                            Player var28 = var2;
                            var20.drawUnfortunateBeam(var27, var28, 961607573);
                            return;
                        }
                    }
                }

                this.cancel();
            }
        };
        BlissPlugin var14 = this.plugin;
        var11.runTaskTimer(var14, 0L, 1L);
    }

    private void drawUnfortunateBeam(Player var1, Player var2, int var3) {
        World var26 = var1.getWorld();
        Location var27 = var1.getLocation().add(0.0, 1.0, 0.0);
        Location var28 = var2.getLocation().add(0.0, 1.0, 0.0);
        double var60 = var28.getX();
        double var133 = var27.getX();
        double var29 = var60 - var133;
        double var65 = var28.getY();
        double var136 = var27.getY();
        double var31 = var65 - var136;
        double var70 = var28.getZ();
        double var139 = var27.getZ();
        double var33 = var70 - var139;
        double var76 = var29 * var29;
        double var145 = var31 * var31;
        double var78 = var76 + var145;
        double var149 = var33 * var33;
        double var35 = Math.sqrt(var78 + var149);
        if (!(var35 < 0.001)) {
            if (var35 > 24.0) {
                var35 = 24.0;
            }

            double var37 = var29 / var35;
            double var39 = var31 / var35;
            double var41 = var33 / var35;
            Color var162 = Color.fromRGB(14, 201, 18);
            DustOptions var104 = new DustOptions(var162, 1.6F);
            DustOptions var43 = var104;

            for (double var44 = 0.0; var44 < var35; var44 += 0.3) {
                double var111 = var27.getX();
                double var167 = var37 * var44;
                double var46 = var111 + var167;
                double var116 = var27.getY();
                double var171 = var39 * var44;
                double var48 = var116 + var171;
                double var121 = var27.getZ();
                double var175 = var41 * var44;
                double var50 = var121 + var175;
                Particle var132 = Particle.DUST;
                Particles.spawn(var26, var132, var46, var48, var50, 1, 0.0, 0.0, 0.0, 0.0, var43, 2077675459);
            }
        }
    }

    private void showUnfortunateBar(final Player var1, int var2) {
        final UUID var11 = var1.getUniqueId();
        Map var17 = this.unfortunateBars;
        BossBar var12 = (BossBar)var17.remove(var11);
        if (var12 != null) {
            var12.removeAll();
        }

        final int var13 = (int)(unfortunateMs(1452044337) / 50L);
        String var27 = "§fUnfortunate";
        BarColor var36 = BarColor.GREEN;
        BarStyle var43 = BarStyle.SOLID;
        BarFlag[] var48 = new BarFlag[0];
        final BossBar var14 = Bukkit.createBossBar(var27, var36, var43, var48);
        var14.setProgress(1.0);
        var14.addPlayer(var1);
        Map var32 = this.unfortunateBars;
        var32.put(var11, var14);
        BukkitRunnable var34 = new BukkitRunnable() {
            int t = 0;

            public void run() {
                int var29 = this.t + 1;
                this.t = var29;
                Map var9 = WealthGemListener.this.unfortunateBars;
                UUID var31 = var11;
                Object var10 = var9.get(var31);
                BossBar var33 = var14;
                if (var10 != var33) {
                    var14.removeAll();
                    this.cancel();
                } else {
                    BossBar var15 = var14;
                    double var47 = (double)this.t;
                    double var52 = (double)var13;
                    double var49 = var47 / var52;
                    double var44 = 1.0 - var49;
                    double var36 = Math.max(0.0, var44);
                    var15.setProgress(var36);
                    int var17 = this.t;
                    int var39 = var13;
                    if (var17 >= var39 || !var1.isOnline()) {
                        var14.removeAll();
                        Map var25 = WealthGemListener.this.unfortunateBars;
                        UUID var41 = var11;
                        BossBar var43 = var14;
                        var25.remove(var41, var43);
                        this.cancel();
                    }
                }
            }
        };
        BlissPlugin var42 = this.plugin;
        var34.runTaskTimer(var42, 1L, 1L);
    }

    private void applyUnfortunateCooldown(Player var1, long var2, int var4) {
        PlayerDataStore var18 = this.players;
        UUID var47 = var1.getUniqueId();
        PlayerData var15 = var18.get(var47, 1264010596);
        GlobalState var21 = this.globalState;
        boolean var16 = var21.holdsDragonEgg(var1, 715974514);
        int var17 = var15.energy(1160853318);
        long var64;
        if (var17 > 5) {
            long var27;
            if (var16) {
                var27 = 100000L;
            } else {
                var27 = 200000L;
            }

            var64 = var27;
        } else if (var17 == 5) {
            long var34;
            if (var16) {
                var34 = 120000L;
            } else {
                var34 = 240000L;
            }

            var64 = var34;
        } else if (var17 == 4) {
            long var43;
            if (var16) {
                var43 = 145000L;
            } else {
                var43 = 289000L;
            }

            var64 = var43;
        } else if (var17 == 3) {
            long var45;
            if (var16) {
                var45 = 165000L;
            } else {
                var45 = 335000L;
            }

            var64 = var45;
        } else {
            long var41;
            if (var16) {
                var41 = 192000L;
            } else {
                var41 = 385000L;
            }

            var64 = var41;
        }

        Map var30 = this.cd3;
        UUID var51 = var1.getUniqueId();
        String var9 = "wealth";
        String var10 = "unfortunate";
        long var58 = GemTuning.cd(var9, var10, 200.0, var64, 1621983041);
        int var63 = var15.energy(1160853318);
        long var60 = EnergyScale.cd(var58, var63, 907518618);
        Long var57 = var2 + var60;
        var30.put(var51, var57);
    }

    @EventHandler
    public void onUnfortunateAttackerDamage(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var7) {
            UUID var20 = var7.getUniqueId();
            if (this.isUnfortunate(var20, 63502028)) {
                Random var16 = new Random();
                int var17 = var16.nextInt(100);
                int var22 = unfortunateCancelPercent(730189779);
                if (var17 < var22) {
                    var1.setCancelled(true);
                    Location var25 = var7.getLocation();
                    Sound var4 = Sound.BLOCK_ANVIL_LAND;
                    var7.playSound(var25, var4, 0.2F, 1.0F);
                }
            }
        }
    }

    @EventHandler
    public void onUnfortunateEat(PlayerItemConsumeEvent var1) {
        Player var7 = var1.getPlayer();
        UUID var15 = var7.getUniqueId();
        if (this.isUnfortunate(var15, 63502028)) {
            Random var11 = new Random();
            int var12 = var11.nextInt(100);
            int var17 = unfortunateCancelPercent(730189779);
            if (var12 < var17) {
                var1.setCancelled(true);
                Location var20 = var7.getLocation();
                Sound var4 = Sound.BLOCK_ANVIL_LAND;
                var7.playSound(var20, var4, 0.2F, 1.0F);
            }
        }
    }

    public boolean isUnfortunate(UUID var1, int var2) {
        Map var8 = this.unfortunateExpiry;
        Long var7 = (Long)var8.get(var1);
        if (var7 != null) {
            long var13 = var7;
            long var5 = System.currentTimeMillis();
            if (var13 > var5) {
                return true;
            }
        }

        return false;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onDamageDurability(EntityDamageByEntityEvent var1) {
        if (var1.getDamager() instanceof Player var7) {
            if (var1.getEntity() instanceof Player var74) {
                int var29 = BlissCoreItems.gemIdOf(var7.getInventory().getItemInOffHand(), 382441079);
                int var55 = GemType.WEALTH.id();
                if (var29 != var55) {
                    GemType var56 = GemType.WEALTH;
                    if (!GoldGem.hasAbsorbedSoul(var7, var56, 1603837876)) {
                        return;
                    }
                }

                PlayerDataStore var33 = this.players;
                UUID var58 = var7.getUniqueId();
                if (var33.get(var58, 1264010596).canUsePassives(2051432579)) {
                    TrustStore var38 = this.trust;
                    UUID var60 = var7.getUniqueId();
                    UUID var66 = var74.getUniqueId();
                    if (!var38.isTrusted(var60, var66, 1736364606)) {
                        long var11 = System.currentTimeMillis();
                        Map var43 = this.durabilityCd;
                        UUID var62 = var7.getUniqueId();
                        Long var13 = (Long)var43.get(var62);
                        if (var13 != null) {
                            long var48 = var13;
                            if (var48 > var11) {
                                return;
                            }
                        }

                        Map var52 = this.durabilityCd;
                        UUID var64 = var7.getUniqueId();
                        Long var73 = var11 + 1000L;
                        var52.put(var64, var73);
                        transferArmorDurability(var74, var7, 2130859709);
                    }
                }
            }
        }
    }

    private static void transferArmorDurability(Player var0, Player var1, int var2) {
        EquipmentSlot[] var12 = new EquipmentSlot[4];
        EquipmentSlot var5 = EquipmentSlot.HEAD;
        var12[0] = var5;
        EquipmentSlot var44 = EquipmentSlot.CHEST;
        var12[1] = var44;
        EquipmentSlot var45 = EquipmentSlot.LEGS;
        var12[2] = var45;
        EquipmentSlot var46 = EquipmentSlot.FEET;
        var12[3] = var46;

        for (EquipmentSlot var9 : var12) {
            PlayerInventory var20 = var0.getInventory();
            ItemStack var10 = var20.getItem(var9);
            PlayerInventory var23 = var1.getInventory();
            ItemStack var11 = var23.getItem(var9);
            damagePiece(var10, 1, 128922731);
            damagePiece(var11, -1, 128922731);
            PlayerInventory var28 = var0.getInventory();
            var28.setItem(var9, var10);
            PlayerInventory var30 = var1.getInventory();
            var30.setItem(var9, var11);
        }
    }

    private static void damagePiece(ItemStack var0, int var1, int var2) {
        if (var0 != null) {
            Material var12 = var0.getType();
            Material var4 = Material.AIR;
            if (var12 != var4) {
                if (var0.getItemMeta() instanceof Damageable var7) {
                    int var36 = var7.getDamage();
                    int var30 = var0.getType().getMaxDurability() - 1;
                    int var35 = var36 + var1;
                    int var31 = Math.min(var30, var35);
                    int var10 = Math.max(0, var31);
                    var7.setDamage(var10);
                    var0.setItemMeta(var7);
                    return;
                }

                return;
            }
        }
    }

    private void applyCooldown(Player var1, long var2, String var4, int var5) {
        PlayerDataStore var19 = this.players;
        UUID var48 = var1.getUniqueId();
        PlayerData var16 = var19.get(var48, 1264010596);
        GlobalState var22 = this.globalState;
        boolean var17 = var22.holdsDragonEgg(var1, 715974514);
        int var18 = var16.energy(1160853318);
        long var65;
        if (var18 > 5) {
            long var31;
            if (var17) {
                var31 = 210000L;
            } else {
                var31 = 420000L;
            }

            var65 = var31;
        } else if (var18 == 5) {
            long var44;
            if (var17) {
                var44 = 235000L;
            } else {
                var44 = 470000L;
            }

            var65 = var44;
        } else if (var18 == 4) {
            long var36;
            if (var17) {
                var36 = 235000L;
            } else {
                var36 = 515000L;
            }

            var65 = var36;
        } else if (var18 == 3) {
            long var46;
            if (var17) {
                var46 = 252000L;
            } else {
                var46 = 565000L;
            }

            var65 = var46;
        } else {
            long var41;
            if (var17) {
                var41 = 300000L;
            } else {
                var41 = 600000L;
            }

            var65 = var41;
        }

        Map var29 = this.cd2;
        UUID var52 = var1.getUniqueId();
        String var10 = "wealth";
        long var59 = GemTuning.cd(var10, var4, 420.0, var65, 1621983041);
        int var64 = var16.energy(1160853318);
        long var61 = EnergyScale.cd(var59, var64, 907518618);
        Long var58 = var2 + var61;
        var29.put(var52, var58);
    }

    private void drawAmplifRings(Location var1, int var2) {
        BlissPlugin var8 = this.plugin;
        Color var9 = Color.fromRGB(14, 201, 18);
        GemBurst.burst(var8, var1, var9, 2.5, 1155716754);
    }

    private static void drawRing(Location var0, double var1, int var3) {
        if (var0.getWorld() != null) {
            int var61 = (int)Math.round(var1 * 12.0);
            int var24 = Math.max(16, var61);
            Color var64 = Color.fromRGB(0, 166, 44);
            DustOptions var36 = new DustOptions(var64, 1.5F);
            DustOptions var25 = var36;

            for (int var26 = 0; var26 < var24; var26++) {
                double var66 = (double)var26;
                double var41 = (Math.PI * 2) * var66;
                double var69 = (double)var24;
                double var27 = var41 / var69;
                double var46 = var0.getX();
                double var85 = Math.cos(var27);
                double var73 = var1 * var85;
                double var29 = var46 + var73;
                double var51 = var0.getZ();
                double var89 = Math.sin(var27);
                double var77 = var1 * var89;
                double var31 = var51 + var77;
                World var56 = var0.getWorld();
                Particle var63 = Particle.DUST;
                World var92 = var0.getWorld();
                double var95 = var0.getY();
                Location var79 = new Location(var92, var29, var95, var31);
                Particles.spawn(var56, var63, var79, 1, 0.0, 0.0, 0.0, 0.0, var25, true, 1748031581);
            }
        }
    }

    private int upgradeInventory(Player var1, int var2) {
        int var9 = 0;
        GlobalState var10 = GlobalState.forPlayer(var1, 1433087673);

        for (ItemStack var14 : var1.getInventory().getContents()) {
            if (var14 != null) {
                Material var31 = var14.getType();
                Material var45 = Material.AIR;
                if (var31 != var45) {
                    for (Entry var16 : AMPLIFY_MAX.entrySet()) {
                        Enchantment var51 = (Enchantment)var16.getKey();
                        int var54 = (Integer)var16.getValue();
                        int var49 = this.upgradeOne(var10, var14, var51, var54, 236726460);
                        var9 += var49;
                    }

                    int var47 = upgradeMegaMace(var14, 1752801041);
                    var9 += var47;
                }
            }
        }

        return var9;
    }

    private static int upgradeMegaMace(ItemStack var0, int var1) {
        if (!MegaMaceListener.isBuffed(var0, 1714313194)) {
            return 0;
        } else {
            int var6 = amplificationLevels(513507964);
            if (var6 <= 0) {
                return 0;
            } else {
                int var7 = 0;
                Enchantment var3 = Enchantment.BREACH;
                if (var0.getEnchantmentLevel(var3) == 3) {
                    Enchantment var22 = Enchantment.BREACH;
                    int var26 = 3 + var6;
                    var0.addUnsafeEnchantment(var22, var26);
                    var7 = 1;
                }

                Enchantment var23 = Enchantment.WIND_BURST;
                if (var0.getEnchantmentLevel(var23) == 2) {
                    Enchantment var25 = Enchantment.WIND_BURST;
                    int var28 = 2 + var6;
                    var0.addUnsafeEnchantment(var25, var28);
                    var7++;
                }

                return var7;
            }
        }
    }

    public void start(int var1) {
        BukkitScheduler var3 = Bukkit.getScheduler();
        BlissPlugin var11 = this.plugin;
        Runnable var12 = this::revertAmplified;
        BukkitTask var10 = var3.runTaskTimer(var11, var12, 100L, 100L);
        this.revertTask = var10;
    }

    public void stop(int var1) {
        if (this.revertTask != null) {
            this.revertTask.cancel();
        }
    }

    private void revertAmplified() {
        int var4 = amplificationLevels(513507964);
        if (var4 > 0) {
            for (Player var6 : Bukkit.getOnlinePlayers()) {
                UUID var54 = var6.getUniqueId();
                if (!this.amplified(var54, 2007265206)) {
                    for (ItemStack var10 : var6.getInventory().getContents()) {
                        if (var10 != null) {
                            Material var34 = var10.getType();
                            Material var57 = Material.AIR;
                            if (var34 != var57) {
                                for (Entry var12 : AMPLIFY_MAX.entrySet()) {
                                    Enchantment var60 = (Enchantment)var12.getKey();
                                    int var44 = var10.getEnchantmentLevel(var60);
                                    int var64 = (Integer)var12.getValue();
                                    int var65 = var64 + var4;
                                    if (var44 == var65) {
                                        Enchantment var68 = (Enchantment)var12.getKey();
                                        int var80 = (Integer)var12.getValue();
                                        var10.addUnsafeEnchantment(var68, var80);
                                    }
                                }

                                if (BlissCoreItems.isMegaMace(var10, 2030001584)) {
                                    Enchantment var69 = Enchantment.BREACH;
                                    int var49 = var10.getEnchantmentLevel(var69);
                                    int var71 = 3 + var4;
                                    if (var49 == var71) {
                                        Enchantment var72 = Enchantment.BREACH;
                                        var10.addUnsafeEnchantment(var72, 3);
                                    }

                                    Enchantment var73 = Enchantment.WIND_BURST;
                                    int var52 = var10.getEnchantmentLevel(var73);
                                    int var75 = 2 + var4;
                                    if (var52 == var75) {
                                        Enchantment var76 = Enchantment.WIND_BURST;
                                        var10.addUnsafeEnchantment(var76, 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int stageCap(GlobalState var1, Enchantment var2, int var3, int var4) {
        int var7 = var3;
        int var8 = -1;
        if (var1.progEnchantLimit(1344216646)) {
            if (var1.prog2(1337683067)) {
                var8 = Progression2Rules.cap(var2, 326282801);
            } else if (var1.prog3(1589905580)) {
                var8 = prog3Cap(var2, 1723240737);
            }
        }

        if (var8 >= 0) {
            var7 = Math.min(var3, var8);
        }

        EnchantLimits var9 = this.plugin.enchantLimits(1173102795);
        int var34;
        if (var9 == null) {
            var34 = -1;
        } else {
            var34 = var9.cap(var2, 1602405455);
        }

        if (var34 >= 0) {
            var7 = Math.min(var7, var34);
        }

        return var7;
    }

    private static int prog3Cap(Enchantment var0, int var1) {
        Enchantment var3 = Enchantment.PROTECTION;
        if (var0 == var3) {
            return 3;
        } else {
            Enchantment var12 = Enchantment.FEATHER_FALLING;
            if (var0 == var12) {
                return 3;
            } else {
                Enchantment var13 = Enchantment.POWER;
                if (var0 == var13) {
                    return 3;
                } else {
                    Enchantment var14 = Enchantment.SHARPNESS;
                    return var0 == var14 ? 4 : -1;
                }
            }
        }
    }

    private int upgradeOne(GlobalState var1, ItemStack var2, Enchantment var3, int var4, int var5) {
        int var10 = this.stageCap(var1, var3, var4, 1774551610);
        if (var10 <= 0) {
            return 0;
        } else {
            int var11 = amplificationLevels(513507964);
            if (var11 <= 0) {
                return 0;
            } else {
                int var19 = var2.getEnchantmentLevel(var3);
                if (var19 == var10) {
                    int var27 = var10 + var11;
                    var2.addUnsafeEnchantment(var3, var27);
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    static {
        LinkedHashMap var0 = new LinkedHashMap();
        Enchantment var69 = Enchantment.SHARPNESS;
        Integer var101 = 5;
        var0.put(var69, var101);
        Enchantment var70 = Enchantment.UNBREAKING;
        Integer var103 = 3;
        var0.put(var70, var103);
        Enchantment var71 = Enchantment.PROTECTION;
        Integer var105 = 4;
        var0.put(var71, var105);
        Enchantment var72 = Enchantment.PROJECTILE_PROTECTION;
        Integer var107 = 4;
        var0.put(var72, var107);
        Enchantment var73 = Enchantment.FIRE_PROTECTION;
        Integer var109 = 4;
        var0.put(var73, var109);
        Enchantment var74 = Enchantment.BLAST_PROTECTION;
        Integer var111 = 4;
        var0.put(var74, var111);
        Enchantment var75 = Enchantment.LOOTING;
        Integer var113 = 3;
        var0.put(var75, var113);
        Enchantment var76 = Enchantment.SWEEPING_EDGE;
        Integer var115 = 3;
        var0.put(var76, var115);
        Enchantment var77 = Enchantment.FIRE_ASPECT;
        Integer var117 = 2;
        var0.put(var77, var117);
        Enchantment var78 = Enchantment.DEPTH_STRIDER;
        Integer var119 = 3;
        var0.put(var78, var119);
        Enchantment var79 = Enchantment.FEATHER_FALLING;
        Integer var121 = 4;
        var0.put(var79, var121);
        Enchantment var80 = Enchantment.SWIFT_SNEAK;
        Integer var123 = 3;
        var0.put(var80, var123);
        Enchantment var81 = Enchantment.SOUL_SPEED;
        Integer var125 = 3;
        var0.put(var81, var125);
        Enchantment var82 = Enchantment.RESPIRATION;
        Integer var127 = 3;
        var0.put(var82, var127);
        Enchantment var83 = Enchantment.AQUA_AFFINITY;
        Integer var129 = 1;
        var0.put(var83, var129);
        Enchantment var84 = Enchantment.THORNS;
        Integer var131 = 3;
        var0.put(var84, var131);
        Enchantment var85 = Enchantment.EFFICIENCY;
        Integer var133 = 5;
        var0.put(var85, var133);
        Enchantment var86 = Enchantment.FORTUNE;
        Integer var135 = 3;
        var0.put(var86, var135);
        Enchantment var87 = Enchantment.SILK_TOUCH;
        Integer var137 = 1;
        var0.put(var87, var137);
        Enchantment var88 = Enchantment.POWER;
        Integer var139 = 5;
        var0.put(var88, var139);
        Enchantment var89 = Enchantment.INFINITY;
        Integer var141 = 1;
        var0.put(var89, var141);
        Enchantment var90 = Enchantment.FLAME;
        Integer var143 = 1;
        var0.put(var90, var143);
        Enchantment var91 = Enchantment.MULTISHOT;
        Integer var145 = 1;
        var0.put(var91, var145);
        Enchantment var92 = Enchantment.PUNCH;
        Integer var147 = 2;
        var0.put(var92, var147);
        Enchantment var93 = Enchantment.QUICK_CHARGE;
        Integer var149 = 3;
        var0.put(var93, var149);
        Enchantment var94 = Enchantment.LURE;
        Integer var151 = 3;
        var0.put(var94, var151);
        Enchantment var95 = Enchantment.LUCK_OF_THE_SEA;
        Integer var153 = 3;
        var0.put(var95, var153);
        Enchantment var96 = Enchantment.LOYALTY;
        Integer var155 = 3;
        var0.put(var96, var155);
        Enchantment var97 = Enchantment.IMPALING;
        Integer var157 = 5;
        var0.put(var97, var157);
        Enchantment var98 = Enchantment.RIPTIDE;
        Integer var159 = 3;
        var0.put(var98, var159);
        Enchantment var99 = Enchantment.CHANNELING;
        Integer var161 = 1;
        var0.put(var99, var161);
        Enchantment var100 = Enchantment.PIERCING;
        Integer var163 = 4;
        var0.put(var100, var163);
        AMPLIFY_MAX = Collections.unmodifiableMap(var0);
    }
}
