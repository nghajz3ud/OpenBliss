package com.bliss.gems.legacy;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.core.TrustStore;
import com.bliss.gems.EntityClickGuard;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

public final class LegacySpeedStormListener implements Listener {
    private static final int WAVES = 3;
    private static final int STRIKES_PER_WAVE = 5;
    private static final int WAVE_TICKS = 100;
    private static final int STRIKE_GAP_TICKS = 20;
    private static final int CLOUD_HEIGHT = 8;
    private static final double STRIKE_SPREAD = 4.0;
    private static final double RADIUS_SQ = 25.0;
    private static final int LIFETIME_TICKS = 320;
    private static final long ALLY_HASTE_MS = 15000L;
    private static final long SURGE_MS = 4000L;
    private static final long FREEZE_MS = 2000L;
    private static final long CAST_FREEZE_MS = 1000L;
    private static final Color STORM_COLOR = Color.fromRGB(244, 255, 28);
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final LegacySpeedState state;
    private final GlobalState globalState;
    private final TrustStore trust;
    private final Random random;
    public final Map<UUID, Location> activeStorms;
    private final Map<UUID, Integer> lastClickTick;

    public LegacySpeedStormListener(BlissPlugin var1, PlayerDataStore var2, LegacySpeedState var3, GlobalState var4, TrustStore var5, int var6) {
        Random var8 = new Random();
        this.random = var8;
        ConcurrentHashMap var17 = new ConcurrentHashMap();
        this.activeStorms = var17;
        ConcurrentHashMap var18 = new ConcurrentHashMap();
        this.lastClickTick = var18;
        this.plugin = var1;
        this.players = var2;
        this.state = var3;
        this.globalState = var4;
        this.trust = var5;
    }

    private static double stormRadiusSq(int var0) {
        String var1 = "speed";
        String var2 = "legacy-speed-storm";
        double var13 = Math.sqrt(25.0);
        double var5 = GemTuning.radius(var1, var2, var13, 1058601812);
        return var5 * var5;
    }

    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent var1) {
        EquipmentSlot var20 = var1.getHand();
        EquipmentSlot var3 = EquipmentSlot.HAND;
        if (var20 == var3) {
            GlobalState var23 = GlobalState.forPlayer(var1.getPlayer(), 1433087673);
            Player var72 = var1.getPlayer();
            if (var23.legacySpeedFor(var72, 1914133154)) {
                Player var12 = var1.getPlayer();
                if (this.canCast(var12, 58304106)) {
                    EntityClickGuard.mark(var12.getUniqueId(), 1276880015);
                    Location var13 = var12.getLocation().clone();
                    this.cast(var12, var13, 1247573517);
                    Location var102 = var13.clone().add(0.0, 8.0, 0.0);
                    this.runStorm(var12, var13, var102, 681072000);
                    long var14 = System.currentTimeMillis() + 15000L;
                    Map var42 = this.state.hasteUntil;
                    UUID var77 = var12.getUniqueId();
                    Long var91 = var14;
                    var42.put(var77, var91);
                    double var16 = stormRadiusSq(1739929642);

                    for (Player var19 : var12.getWorld().getPlayers()) {
                        if (!var19.equals(var12)) {
                            Location var58 = var19.getLocation();
                            double var59 = var58.distanceSquared(var13);
                            if (!(var59 > var16)) {
                                TrustStore var63 = this.trust;
                                UUID var81 = var12.getUniqueId();
                                UUID var95 = var19.getUniqueId();
                                if (var63.isTrusted(var81, var95, 1736364606)) {
                                    Map var67 = this.state.hasteUntil;
                                    UUID var83 = var19.getUniqueId();
                                    Long var98 = var14;
                                    var67.put(var83, var98);
                                }
                            }
                        }
                    }

                    BukkitScheduler var69 = Bukkit.getScheduler();
                    BlissPlugin var85 = this.plugin;
                    Runnable var100 = () -> {
                        Map var4 = this.activeStorms;
                        UUID var6 = var12.getUniqueId();
                        var4.remove(var6);
                    };
                    var69.runTaskLater(var85, var100, 8L);
                }
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var23 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var23 != var3) {
            Action var25 = var1.getAction();
            Action var83 = Action.RIGHT_CLICK_BLOCK;
            if (var25 != var83) {
                return;
            }
        }

        Player var12 = var1.getPlayer();
        UUID var13 = var12.getUniqueId();
        int var14 = Bukkit.getCurrentTick();
        Map var32 = this.lastClickTick;
        Integer var15 = (Integer)var32.get(var13);
        if (var15 != null) {
            int var37 = var15;
            if (var37 == var14) {
                return;
            }
        }

        Map var39 = this.lastClickTick;
        Integer var97 = var14;
        var39.put(var13, var97);
        if (!EntityClickGuard.recentlyClickedEntity(var13, 1923725760)) {
            GlobalState var45 = GlobalState.forPlayer(var1.getPlayer(), 1433087673);
            Player var88 = var1.getPlayer();
            if (var45.legacySpeedFor(var88, 1914133154)) {
                if (this.canCast(var12, 58304106)) {
                    Location var16 = var12.getLocation().clone();
                    this.cast(var12, var16, 1247573517);
                    long var17 = System.currentTimeMillis() + 1000L;
                    double var19 = stormRadiusSq(1739929642);

                    for (Player var22 : var12.getWorld().getPlayers()) {
                        if (!var22.equals(var12)) {
                            Location var72 = var22.getLocation();
                            double var73 = var72.distanceSquared(var16);
                            if (!(var73 > var19)) {
                                TrustStore var77 = this.trust;
                                UUID var105 = var22.getUniqueId();
                                if (!var77.isTrusted(var13, var105, 1736364606)) {
                                    Map var81 = this.state.frozenUntil;
                                    UUID var96 = var22.getUniqueId();
                                    Long var108 = var17;
                                    var81.put(var96, var108);
                                }
                            }
                        }
                    }

                    Location var110 = var16.clone().add(0.0, 8.0, 0.0);
                    this.runStorm(var12, var16, var110, 681072000);
                }
            }
        }
    }

    private boolean canCast(Player var1, int var2) {
        ItemStack var9 = var1.getInventory().getItemInMainHand();
        int var17 = BlissCoreItems.gemIdOf(var9, 382441079);
        int var43 = GemType.SPEED.id();
        if (var17 != var43) {
            return false;
        } else if (BlissCoreItems.tierOf(var9, 1633828214) != 2) {
            return false;
        } else {
            PlayerDataStore var23 = this.players;
            UUID var46 = var1.getUniqueId();
            if (!var23.get(var46, 1264010596).canUsePowers(1616892701)) {
                return false;
            } else {
                long var11 = System.currentTimeMillis();
                Map var31 = this.state.stormCd;
                UUID var48 = var1.getUniqueId();
                Long var13 = (Long)var31.get(var48);
                if (var13 != null) {
                    long var36 = var13;
                    if (var36 > var11) {
                        long var50 = var13;
                        Component var56 = Text.legacy(
                            "&e\ud83d\udd2e &cYour &e\ud83c\udf29Speed Storm &cskill is on cooldown for &e" + readable(var50 - var11, 1240196885), 373192400
                        );
                        var1.sendMessage(var56);
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private void cast(Player var1, Location var2, int var3) {
        Map var7 = this.activeStorms;
        UUID var12 = var1.getUniqueId();
        var7.put(var12, var2);
        Component var14 = Text.legacy("&e\ud83d\udd2e &bYou summoned a &e\ud83c\udf29speed storm", 373192400);
        var1.sendMessage(var14);
        this.applyCooldown(var1, 209530596);
        this.drawGroundRings(var2, 502332589);
    }

    private void applyCooldown(Player var1, int var2) {
        PlayerDataStore var13 = this.players;
        UUID var43 = var1.getUniqueId();
        PlayerData var10 = var13.get(var43, 1264010596);
        GlobalState var16 = this.globalState;
        boolean var11 = var16.holdsDragonEgg(var1, 715974514);
        int var12 = var10.energy(1160853318);
        long var56;
        if (var12 > 5) {
            long var26;
            if (var11) {
                var26 = 75000L;
            } else {
                var26 = 150000L;
            }

            var56 = var26;
        } else if (var12 == 5) {
            long var30;
            if (var11) {
                var30 = 90000L;
            } else {
                var30 = 180000L;
            }

            var56 = var30;
        } else if (var12 == 4) {
            long var34;
            if (var11) {
                var34 = 105000L;
            } else {
                var34 = 210000L;
            }

            var56 = var34;
        } else if (var12 == 3) {
            long var41;
            if (var11) {
                var41 = 120000L;
            } else {
                var41 = 240000L;
            }

            var56 = var41;
        } else {
            long var38;
            if (var11) {
                var38 = 150000L;
            } else {
                var38 = 300000L;
            }

            var56 = var38;
        }

        Map var24 = this.state.stormCd;
        UUID var47 = var1.getUniqueId();
        long var5 = System.currentTimeMillis();
        long var54 = EnergyScale.cd(var56, var12, 907518618);
        Long var53 = var5 + var54;
        var24.put(var47, var53);
    }

    private void runStorm(Player var1, Location var2, Location var3, int var4) {
        UUID var11 = var1.getUniqueId();

        for (int var12 = 0; var12 < 3; var12++) {
            int var13 = var12 * 100;
            BukkitScheduler var22 = Bukkit.getScheduler();
            BlissPlugin var37 = this.plugin;
            Runnable var43 = () -> smokeDisc(var3, 45818294);
            long var53 = (long)var13;
            var22.runTaskLater(var37, var43, var53);

            for (int var14 = 0; var14 < 5; var14++) {
                long var27 = (long)var13;
                long var47 = (long)var14 * 20L;
                long var15 = var27 + var47;
                BukkitScheduler var31 = Bukkit.getScheduler();
                BlissPlugin var40 = this.plugin;
                Runnable var50 = () -> {
                    Map var11x = this.activeStorms;
                    if (var11x.containsKey(var11)) {
                        Location var14x = var2.clone();
                        double var29 = this.random.nextDouble() * 4.0 * 2.0 - 4.0;
                        double var45 = this.random.nextDouble() * 4.0 * 2.0 - 4.0;
                        Location var10 = var14x.add(var29, 0.0, var45);
                        if (var10.getWorld() != null) {
                            World var19 = var10.getWorld();
                            var19.strikeLightning(var10);
                        }
                    }
                };
                var31.runTaskLater(var40, var50, var15);
            }
        }

        BukkitScheduler var33 = Bukkit.getScheduler();
        BlissPlugin var42 = this.plugin;
        Runnable var52 = () -> {
            Map var4x = this.activeStorms;
            var4x.remove(var11);
        };
        var33.runTaskLater(var42, var52, 320L);
    }

    private void drawGroundRings(Location var1, int var2) {
        double[][] var12 = new double[8][];
        double[] var33 = new double[]{0.75, 1.0};
        var12[0] = var33;
        double[] var35 = new double[]{1.25, 1.5};
        var12[1] = var35;
        double[] var37 = new double[]{1.75, 2.0};
        var12[2] = var37;
        double[] var39 = new double[]{2.25, 2.5};
        var12[3] = var39;
        double[] var41 = new double[]{2.75, 3.0};
        var12[4] = var41;
        double[] var43 = new double[]{3.5, 4.0};
        var12[5] = var43;
        double[] var45 = new double[]{4.5, 5.0};
        var12[6] = var45;
        double[] var47 = new double[]{5.0};
        var12[7] = var47;
        double[][] var9 = var12;
        int var10 = 0;

        while (true) {
            int var20 = var9.length;
            if (var10 >= var20) {
                return;
            }

            double[] var11 = var9[var10];
            BukkitScheduler var17 = Bukkit.getScheduler();
            BlissPlugin var23 = this.plugin;
            Runnable var32 = () -> {
                for (double var7 : var11) {
                    ring(var1, var7, 648340617);
                }
            };
            long var50 = (long)var10;
            var17.runTaskLater(var23, var32, var50);
            var10++;
        }
    }

    private static void ring(Location var0, double var1, int var3) {
        if (var0.getWorld() != null) {
            int var50 = (int)Math.round(var1 * 12.0);
            int var27 = Math.max(16, var50);
            Color var7 = STORM_COLOR;
            DustOptions var35 = new DustOptions(var7, 1.0F);
            DustOptions var28 = var35;

            for (int var29 = 0; var29 < var27; var29++) {
                double var54 = (double)var29;
                double var40 = (Math.PI * 2) * var54;
                double var57 = (double)var27;
                double var30 = var40 / var57;
                World var45 = var0.getWorld();
                Particle var52 = Particle.DUST;
                World var62 = var0.getWorld();
                double var65 = var0.getX();
                double var74 = Math.cos(var30);
                double var69 = var1 * var74;
                double var67 = var65 + var69;
                double var72 = var0.getY();
                double var77 = var0.getZ();
                double var83 = Math.sin(var30);
                double var81 = var1 * var83;
                double var79 = var77 + var81;
                Location var59 = new Location(var62, var67, var72, var79);
                Particles.spawn(var45, var52, var59, 1, 0.0, 0.0, 0.0, 0.0, var28, true, 1748031581);
            }
        }
    }

    private static void smokeDisc(Location var0, int var1) {
        if (var0.getWorld() != null) {
            for (double var19 = -5.0; var19 < 5.0; var19 += 0.6) {
                for (double var21 = -5.0; var21 < 5.0; var21 += 0.6) {
                    World var39 = var0.getWorld();
                    Particle var3 = Particle.CAMPFIRE_COSY_SMOKE;
                    World var52 = var0.getWorld();
                    double var55 = var0.getX();
                    double var57 = var55 + var19;
                    double var60 = var0.getY();
                    double var62 = var0.getZ();
                    double var64 = var62 + var21;
                    Location var48 = new Location(var52, var57, var60, var64);
                    Particles.spawn(var39, var3, var48, 1, 0.0, 0.0, 0.0, 0.0, 1986132876);
                }
            }
        }
    }

    @EventHandler
    public void onStormIgnite(BlockIgniteEvent var1) {
        Location var6 = var1.getBlock().getLocation();
        double var7 = stormRadiusSq(1739929642);

        for (Location var10 : this.activeStorms.values()) {
            if (var10 != null && var10.getWorld() != null) {
                World var28 = var10.getWorld();
                World var35 = var6.getWorld();
                if (var28.equals(var35)) {
                    double var31 = var10.distanceSquared(var6);
                    if (!(var31 > var7)) {
                        var1.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLightningDamage(EntityDamageEvent var1) {
        DamageCause var17 = var1.getCause();
        DamageCause var3 = DamageCause.LIGHTNING;
        if (var17 == var3) {
            if (var1.getEntity() instanceof Player var9) {
                Location var10 = var9.getLocation();
                double var11 = stormRadiusSq(1739929642);

                for (Entry var14 : this.activeStorms.entrySet()) {
                    Location var15 = (Location)var14.getValue();
                    if (var15 != null && var15.getWorld() != null) {
                        World var44 = var15.getWorld();
                        World var69 = var10.getWorld();
                        if (var44.equals(var69)) {
                            double var47 = var15.distanceSquared(var10);
                            if (!(var47 > var11)) {
                                var1.setCancelled(true);
                                UUID var16 = (UUID)var14.getKey();
                                UUID var55 = var9.getUniqueId();
                                if (!var55.equals(var16)) {
                                    TrustStore var58 = this.trust;
                                    UUID var79 = var9.getUniqueId();
                                    if (!var58.isTrusted(var16, var79, 1736364606)) {
                                        Map var62 = this.state.frozenUntil;
                                        UUID var75 = var9.getUniqueId();
                                        Long var84 = System.currentTimeMillis() + 2000L;
                                        var62.put(var75, var84);
                                        return;
                                    }
                                }

                                Map var66 = this.state.surgeUntil;
                                UUID var77 = var9.getUniqueId();
                                Long var89 = System.currentTimeMillis() + 4000L;
                                var66.put(var77, var89);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFrozenMove(PlayerMoveEvent var1) {
        Map var10 = this.state.frozenUntil;
        UUID var47 = var1.getPlayer().getUniqueId();
        Long var6 = (Long)var10.get(var47);
        if (var6 != null) {
            long var15 = var6;
            long var4 = System.currentTimeMillis();
            if (var15 <= var4) {
                Map var20 = this.state.frozenUntil;
                UUID var50 = var1.getPlayer().getUniqueId();
                var20.remove(var50);
            } else {
                Location var7 = var1.getFrom();
                Location var8 = var1.getTo();
                if (var8 != null) {
                    double var28 = var7.getX();
                    double var57 = var8.getX();
                    if (var28 == var57) {
                        double var32 = var7.getY();
                        double var60 = var8.getY();
                        if (var32 == var60) {
                            double var36 = var7.getZ();
                            double var63 = var8.getZ();
                            if (var36 == var63) {
                                float var40 = var7.getYaw();
                                float var52 = var8.getYaw();
                                if (var40 == var52) {
                                    float var43 = var7.getPitch();
                                    float var54 = var8.getPitch();
                                    if (var43 == var54) {
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    var1.setCancelled(true);
                }
            }
        }
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
}
