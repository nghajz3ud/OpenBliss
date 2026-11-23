package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.Particles;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public final class FireCozyListener implements Listener {
    private static final int COZY_DURATION_TICKS = 12000;
    private static final int FINAL_RING_INTERVAL_TICKS = 5;
    private static final int FINAL_RING_PASSES = 2400;
    private static final double RING_SPACING = 0.36;
    private static final double RING_SPIN = 0.618;
    private static final double GOLDEN_ANGLE = 2.3999632;
    private static final double FINAL_RING_RADIUS = 4.0;
    private static final long COZY_COOLDOWN_NO_EGG_MS = 599000L;
    private static final long COZY_COOLDOWN_WITH_EGG_MS = 299000L;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final FireGemListener fire;
    private final GlobalState globalState;
    public final Map<UUID, Location> activeCozies;
    private final Map<UUID, TextDisplay> cozyTimerDisplays;
    private final Map<UUID, Long> cozyExpiryMs;
    private final Map<UUID, Integer> lastInteractTick;
    private final Map<UUID, Integer> cozyGen;

    public FireCozyListener(BlissPlugin var1, PlayerDataStore var2, FireGemListener var3, GlobalState var4, int var5) {
        ConcurrentHashMap var7 = new ConcurrentHashMap();
        this.activeCozies = var7;
        ConcurrentHashMap var17 = new ConcurrentHashMap();
        this.cozyTimerDisplays = var17;
        ConcurrentHashMap var18 = new ConcurrentHashMap();
        this.cozyExpiryMs = var18;
        ConcurrentHashMap var19 = new ConcurrentHashMap();
        this.lastInteractTick = var19;
        HashMap var20 = new HashMap();
        this.cozyGen = var20;
        this.plugin = var1;
        this.players = var2;
        this.fire = var3;
        this.globalState = var4;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var19 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_BLOCK;
        if (var19 == var3) {
            Player var7 = var1.getPlayer();
            UUID var8 = var7.getUniqueId();
            int var9 = Bukkit.getCurrentTick();
            Map var26 = this.lastInteractTick;
            Integer var10 = (Integer)var26.get(var8);
            if (var10 != null) {
                int var31 = var10;
                if (var31 == var9) {
                    return;
                }
            }

            Map var33 = this.lastInteractTick;
            Integer var93 = var9;
            var33.put(var8, var93);
            ItemStack var11 = var7.getInventory().getItemInMainHand();
            GemType var77 = GemType.FIRE;
            boolean var12 = GoldGem.isWieldingSoulMain(var7, var77, 244096655);
            if (BlissCoreItems.isFireGem(var11, 1896087501) || var12) {
                if (var12 || BlissCoreItems.tierOf(var11, 1633828214) == 2) {
                    PlayerDataStore var47 = this.players;
                    if (var47.get(var8, 1264010596).canUsePowers(1616892701)) {
                        long var14 = System.currentTimeMillis();
                        Map var55 = this.fire.cd2;
                        Long var16 = (Long)var55.get(var8);
                        if (var16 != null) {
                            long var60 = var16;
                            if (var60 > var14) {
                                long var82 = var16;
                                Component var88 = Text.legacy(
                                    "&6\ud83d\udd2e &cYour §6\ud83e\udd7eCozy Campfire &cis on cooldown for &6" + readable(var82 - var14, 1391486421),
                                    373192400
                                );
                                var7.sendMessage(var88);
                                return;
                            }
                        }

                        Block var17 = var1.getClickedBlock();
                        if (var17 != null) {
                            Material var68 = var17.getType();
                            Material var89 = Material.AIR;
                            if (var68 != var89) {
                                Block var18 = var17.getRelative(0, 1, 0);
                                Material var72 = var18.getType();
                                Material var91 = Material.AIR;
                                if (var72 != var91) {
                                    return;
                                }

                                this.activate(var7, var18, 1342711056);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void activate(Player var1, final Block var2, int var3) {
        final UUID var18 = var1.getUniqueId();
        Map<UUID, Integer> var31 = this.cozyGen;
        Integer var105 = 1;
        BiFunction<Integer, Integer, Integer> var7 = Integer::sum;
        final int var19 = var31.merge(var18, var105, var7);
        String var106 = "fire";
        String var140 = "cozy";
        double var109 = GemTuning.duration(var106, var140, 600.0, 812730832) * 20.0;
        int var20 = (int)Math.max(1.0, var109);
        int var80 = var20 / 5;
        final int var21 = Math.max(1, var80);
        Material var81 = Material.SOUL_CAMPFIRE;
        var2.setType(var81, true);
        Location var22 = var2.getLocation().add(0.5, 0.5, 0.5).subtract(0.0, 0.5, 0.0);
        Map var48 = this.activeCozies;
        var48.put(var18, var22);
        GlobalState var51 = this.globalState;
        long var54;
        if (var51.holdsDragonEgg(var18, 1249047238)) {
            var54 = 299000L;
        } else {
            var54 = 599000L;
        }

        PlayerDataStore var57 = this.players;
        PlayerData var26 = var57.get(var18, 1264010596);
        Map var61 = this.fire.cd2;
        long var114 = System.currentTimeMillis();
        String var159 = "fire";
        String var173 = "cozy";
        long var160 = GemTuning.cd(var159, var173, 599.0, var54, 1621983041);
        int var182 = var26.energy(1160853318);
        long var162 = EnergyScale.cd(var160, var182, 907518618);
        Long var118 = var114 + var162;
        var61.put(var18, var118);
        Component var91 = Text.legacy("&6\ud83d\udd2e &bYou have used &6\ud83e\udd7eCozy Campfire &bskill!", 373192400);
        var1.sendMessage(var91);
        BlissPlugin var65 = this.plugin;
        Color var120 = Color.fromRGB(255, 119, 0);
        GemBurst.gentleBurst(var65, var22, var120, 4.0, 1.2F, 826206677);
        Attribute var121 = Attribute.MAX_HEALTH;
        double var95 = var1.getAttribute(var121).getValue();
        double var151 = var1.getHealth() + 30.0;
        double var97 = Math.min(var95, var151);
        var1.setHealth(var97);
        BukkitRunnable var67 = new BukkitRunnable() {
            int step = 0;

            public void run() {
                int var34 = this.step + 1;
                this.step = var34;
                int var36 = var21;
                if (var34 > var36) {
                    this.cancel();
                } else {
                    Integer var9 = var19;
                    Map var39 = FireCozyListener.this.cozyGen;
                    UUID var53 = var18;
                    Object var40 = var39.get(var53);
                    if (!var9.equals(var40)) {
                        this.cancel();
                    } else {
                        Map var13 = FireCozyListener.this.activeCozies;
                        UUID var42 = var18;
                        if (!var13.containsKey(var42)) {
                            this.cancel();
                        } else {
                            Map var18x = FireCozyListener.this.activeCozies;
                            UUID var44 = var18;
                            Location var5 = (Location)var18x.get(var44);
                            if (var5 == null) {
                                this.cancel();
                            } else {
                                Material var24 = var5.getBlock().getType();
                                Material var45 = Material.SOUL_CAMPFIRE;
                                if (var24 != var45) {
                                    FireCozyListener var29 = FireCozyListener.this;
                                    UUID var51 = var18;
                                    var29.clearCozyState(var51, 1013478470);
                                    this.cancel();
                                } else {
                                    Location var27 = var5.clone().add(0.0, 0.25, 0.0);
                                    int var57 = this.step;
                                    FireCozyListener.drawRing(var27, 4.0, var57, 1154507989);
                                }
                            }
                        }
                    }
                }
            }
        };
        BlissPlugin var100 = this.plugin;
        var67.runTaskTimer(var100, 5L, 5L);
        long var69 = System.currentTimeMillis();
        long var128 = (long)var20 * 50L;
        long var27 = var69 + var128;
        Map var74 = this.cozyExpiryMs;
        Long var132 = var27;
        var74.put(var18, var132);
        Location var135 = var2.getLocation().add(0.5, 1.2, 0.5);
        this.spawnTimerDisplay(var18, var135, 1489340186);
        BukkitRunnable var77 = new BukkitRunnable() {
            public void run() {
                Material var5 = var2.getType();
                Material var2x = Material.SOUL_CAMPFIRE;
                if (var5 == var2x) {
                    Block var7 = var2;
                    Material var14 = Material.AIR;
                    var7.setType(var14, true);
                }

                Integer var10 = var19;
                Map var17 = FireCozyListener.this.cozyGen;
                UUID var22 = var18;
                Object var18x = var17.get(var22);
                if (var10.equals(var18x)) {
                    FireCozyListener var13 = FireCozyListener.this;
                    UUID var20 = var18;
                    var13.clearCozyState(var20, 1013478470);
                }
            }
        };
        BlissPlugin var104 = this.plugin;
        long var138 = (long)var20;
        var77.runTaskLater(var104, var138);
    }

    private void spawnTimerDisplay(final UUID var1, Location var2, int var3) {
        World var11 = var2.getWorld();
        Consumer<TextDisplay> var7 = var0 -> {
            var0.setPersistent(false);
            Billboard var7x = Billboard.CENTER;
            var0.setBillboard(var7x);
            var0.setShadowed(true);
            TextComponent var10x = Component.text("10m 0s");
            TextColor var12 = TextColor.color(16742144);
            Component var11x = var10x.color(var12);
            var0.text(var11x);
        };
        final TextDisplay var9 = (TextDisplay)var11.spawn(var2, TextDisplay.class, var7);
        Map<UUID, TextDisplay> var15 = this.cozyTimerDisplays;
        TextDisplay var10 = var15.put(var1, var9);
        if (var10 != null && var10.isValid()) {
            var10.remove();
        }

        BukkitRunnable var22 = new BukkitRunnable() {
            public void run() {
                Map var13 = FireCozyListener.this.cozyExpiryMs;
                UUID var47 = var1;
                Long var5 = (Long)var13.get(var47);
                if (var5 != null && var9.isValid()) {
                    long var22 = var5;
                    long var3 = System.currentTimeMillis();
                    long var6 = var22 - var3;
                    if (var6 <= 0L) {
                        this.cancel();
                    } else {
                        int var8 = (int)((var6 + 999L) / 1000L);
                        int var9x = var8 / 60;
                        int var10 = var8 % 60;
                        String var46;
                        if (var9x > 0) {
                            var46 = var9x + "m ";
                        } else {
                            var46 = "";
                        }

                        String var11 = var46 + var10 + "s";
                        TextDisplay var44 = var9;
                        TextComponent var53 = Component.text("\ud83e\udd7e " + var11);
                        TextColor var62 = TextColor.color(16742144);
                        Component var54 = var53.color(var62);
                        var44.text(var54);
                    }
                } else {
                    this.cancel();
                }
            }
        };
        BlissPlugin var26 = this.plugin;
        var22.runTaskTimer(var26, 20L, 20L);
    }

    public void shutdown(int var1) {
        for (Location var6 : this.activeCozies.values()) {
            if (var6 != null && var6.getWorld() != null) {
                Material var20 = var6.getBlock().getType();
                Material var3 = Material.SOUL_CAMPFIRE;
                if (var20 == var3) {
                    Block var22 = var6.getBlock();
                    Material var42 = Material.AIR;
                    var22.setType(var42, true);
                }
            }
        }

        for (TextDisplay var44 : this.cozyTimerDisplays.values()) {
            if (var44 != null && var44.isValid()) {
                var44.remove();
            }
        }

        this.activeCozies.clear();
        this.cozyTimerDisplays.clear();
        this.cozyExpiryMs.clear();
    }

    private void clearCozyState(UUID var1, int var2) {
        Map var6 = this.activeCozies;
        var6.remove(var1);
        Map var9 = this.cozyExpiryMs;
        var9.remove(var1);
        Map var12 = this.cozyTimerDisplays;
        TextDisplay var5 = (TextDisplay)var12.remove(var1);
        if (var5 != null && var5.isValid()) {
            var5.remove();
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onSoulCampfireBreak(BlockBreakEvent var1) {
        Material var16 = var1.getBlock().getType();
        Material var3 = Material.SOUL_CAMPFIRE;
        if (var16 == var3) {
            Location var10 = var1.getBlock().getLocation().add(0.5, 0.5, 0.5);

            for (Entry var12 : this.activeCozies.entrySet()) {
                Location var13 = (Location)var12.getValue();
                if (var13 != null) {
                    World var35 = var13.getWorld();
                    World var49 = var10.getWorld();
                    if (var35.equals(var49) && !(var13.distanceSquared(var10) > 0.64)) {
                        UUID var14 = (UUID)var12.getKey();
                        this.clearCozyState(var14, 1013478470);
                        this.applyBreakReduction(var14, 1386033407);
                        return;
                    }
                }
            }
        }
    }

    private void applyBreakReduction(UUID var1, int var2) {
        Map var22 = this.fire.cd2;
        Long var10 = (Long)var22.get(var1);
        if (var10 != null) {
            long var11 = System.currentTimeMillis();
            long var29 = var10;
            long var13 = var29 - var11;
            if (var13 > 0L) {
                int var15 = (int)(var13 / 60000L);
                if (Bukkit.getPlayer(var1) != null) {
                    PlayerDataStore var45 = this.players;
                    PlayerData var17 = var45.get(var1, 1264010596);
                    GlobalState var48 = this.globalState;
                    boolean var18 = var48.holdsDragonEgg(var1, 1249047238);
                    int var19 = var17.energy(1160853318);
                    byte var92;
                    if (var19 > 4) {
                        byte var54;
                        if (var18) {
                            var54 = 4;
                        } else {
                            var54 = 8;
                        }

                        var92 = var54;
                    } else if (var19 >= 3) {
                        byte var69;
                        if (var18) {
                            var69 = 3;
                        } else {
                            var69 = 7;
                        }

                        var92 = var69;
                    } else {
                        byte var70;
                        if (var18) {
                            var70 = 2;
                        } else {
                            var70 = 6;
                        }

                        var92 = var70;
                    }

                    if (var15 < var92) {
                        Map var64 = this.fire.cd2;
                        var64.remove(var1);
                    } else {
                        int var20 = var15 - var92;
                        Map var60 = this.fire.cd2;
                        long var90 = (long)var20 * 60000L;
                        Long var87 = var11 + var90;
                        var60.put(var1, var87);
                    }
                }
            }
        }
    }

    static void drawRing(Location var0, double var1) {
        drawRing(var0, var1, 0, 1154507989);
    }

    static void drawRing(Location var0, double var1, int var3, int var4) {
        if (var0.getWorld() != null) {
            int var83 = (int)Math.round((Math.PI * 2) * var1 / 0.36);
            int var29 = Math.max(16, var83);
            Color var88 = Color.fromRGB(255, 119, 0);
            DustOptions var43 = new DustOptions(var88, 1.8F);
            DustOptions var30 = var43;
            double var45 = (double)var3;
            double var126 = (double)var29;
            double var91 = (Math.PI * 2) / var126;
            double var31 = var45 * var91 * 0.618;

            for (int var33 = 0; var33 < var29; var33++) {
                double var129 = (double)var33;
                double var97 = (Math.PI * 2) * var129;
                double var132 = (double)var29;
                double var99 = var97 / var132;
                double var34 = var31 + var99;
                double var58 = var0.getX();
                double var136 = Math.cos(var34);
                double var103 = var1 * var136;
                double var36 = var58 + var103;
                double var63 = var0.getZ();
                double var140 = Math.sin(var34);
                double var107 = var1 * var140;
                double var38 = var63 + var107;
                World var68 = var0.getWorld();
                Particle var85 = Particle.DUST;
                World var143 = var0.getWorld();
                double var168 = var0.getY();
                Location var109 = new Location(var143, var36, var168, var38);
                Particles.spawn(var68, var85, var109, 0, 0.0, 0.0, 0.0, 0.0, var30, true, 1748031581);
            }

            if (var3 % 2 == 0) {
                double var113 = (double)var3 * 2.3999632;
                double var183 = var31 + var113;
                World var76 = var0.getWorld();
                Particle var87 = Particle.HAPPY_VILLAGER;
                double var116 = var0.getX();
                double var159 = Math.cos(var183);
                double var150 = var1 * var159;
                double var118 = var116 + var150;
                double var155 = var0.getY() + 0.1;
                double var164 = var0.getZ();
                double var179 = Math.sin(var183);
                double var172 = var1 * var179;
                double var166 = var164 + var172;
                Particles.spawn(var76, var87, var118, var155, var166, 1, 0.05, 0.05, 0.05, 0.0, 1717927553);
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
