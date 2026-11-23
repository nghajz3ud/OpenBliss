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
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityKnockbackEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class FireCrispListener implements Listener {
    public static final long CRISP_COOLDOWN_MS = 42000L;
    public static final int CRISP_DURATION_TICKS = 200;
    public static final int CRISP_BLOCK_RADIUS = 5;
    public static final int CRISP_BURN_RADIUS = 4;
    private static final int CRISP_EXPAND_PERIOD_TICKS = 2;
    private static final Set<Material> LEAVES;
    private static final Set<Material> LOGS;
    private static final Set<Material> GROUND;
    private static final Material[] STONE_REPLACEMENTS;
    private static final Material[] LOG_REPLACEMENTS;
    private static final Material[] LEAF_REPLACEMENTS;
    private static final Material[] SHORT_GRASS_REPLACEMENTS;
    private static final Material[] TALL_GRASS_REPLACEMENTS;
    private static final Map<Material, Material> AUTOSMELT;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final FireGemListener fire;
    private final Random random;
    public final Map<UUID, Location> activeCrisps;
    boolean burning;
    private final Set<UUID> doubleClickLatch;
    private final Map<UUID, Integer> lastInteractTick;
    private final List<Map<Location, FireCrispListener.SavedBlock>> activeSnapshots;
    private final TrustStore trust;

    public FireCrispListener(BlissPlugin var1, PlayerDataStore var2, FireGemListener var3, int var4) {
        Random var6 = new Random();
        this.random = var6;
        ConcurrentHashMap var16 = new ConcurrentHashMap();
        this.activeCrisps = var16;
        KeySetView var17 = ConcurrentHashMap.newKeySet();
        this.doubleClickLatch = var17;
        ConcurrentHashMap var18 = new ConcurrentHashMap();
        this.lastInteractTick = var18;
        ArrayList var19 = new ArrayList();
        this.activeSnapshots = var19;
        this.plugin = var1;
        this.players = var2;
        this.fire = var3;
        TrustStore var24 = var1.trust(310732214);
        this.trust = var24;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var18 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var18 != var3) {
            Action var20 = var1.getAction();
            Action var95 = Action.RIGHT_CLICK_BLOCK;
            if (var20 != var95) {
                return;
            }
        }

        Player var7 = var1.getPlayer();
        UUID var8 = var7.getUniqueId();
        int var9 = Bukkit.getCurrentTick();
        Map var27 = this.lastInteractTick;
        Integer var10 = (Integer)var27.get(var8);
        if (var10 != null) {
            int var32 = var10;
            if (var32 == var9) {
                return;
            }
        }

        Map var34 = this.lastInteractTick;
        Integer var118 = var9;
        var34.put(var8, var118);
        if (!BlissCoreItems.isUtilityHead(var7.getInventory().getItemInMainHand(), 1077770594)) {
            if (!BlissCoreItems.isFireGem(var7.getInventory().getItemInOffHand(), 1896087501)) {
                GemType var99 = GemType.FIRE;
                if (!GoldGem.isWieldingSoulOff(var7, var99, 1989261555)) {
                    return;
                }
            }

            PlayerDataStore var48 = this.players;
            if (var48.get(var8, 1264010596).canUseAbilities(325243089)) {
                Material var13 = var7.getInventory().getItemInMainHand().getType();
                Material var101 = Material.AIR;
                if (var13 == var101 || isSword(var13, 609128534) || isAxe(var13, 2112166919)) {
                    long var14 = System.currentTimeMillis();
                    Map var65 = this.fire.cd4;
                    Long var16 = (Long)var65.get(var8);
                    if (var16 != null) {
                        long var70 = var16;
                        if (var70 > var14) {
                            long var104 = var16;
                            Component var110 = Text.legacy(
                                "&6\ud83d\udd2e &cYour §6Crisp &cskill is on cooldown for &6" + readableRemaining(var104 - var14, 252331935), 373192400
                            );
                            var7.sendMessage(var110);
                            return;
                        }
                    }

                    if (GlobalState.forPlayer(var7, 1433087673).sneakCast(704684993)) {
                        if (!var7.isSneaking()) {
                            return;
                        }

                        Set var93 = this.doubleClickLatch;
                        var93.remove(var8);
                    } else {
                        Set var79 = this.doubleClickLatch;
                        if (!var79.contains(var8)) {
                            Set var82 = this.doubleClickLatch;
                            var82.add(var8);
                            BukkitScheduler var84 = Bukkit.getScheduler();
                            BlissPlugin var114 = this.plugin;
                            Runnable var122 = () -> {
                                Set var4 = this.doubleClickLatch;
                                var4.remove(var8);
                            };
                            var84.runTaskLater(var114, var122, 20L);
                            return;
                        }

                        Set var87 = this.doubleClickLatch;
                        var87.remove(var8);
                    }

                    this.activate(var7, 1181619581);
                }
            }
        }
    }

    private void activate(Player var1, int var2) {
        final UUID var14 = var1.getUniqueId();
        long var15 = System.currentTimeMillis();
        Location var17 = var1.getLocation().clone();
        Map var38 = this.activeCrisps;
        var38.put(var14, var17);
        String var124 = "fire";
        String var6 = "crisp-burn";
        double var127 = GemTuning.duration(var124, var6, 10.0, 812730832) * 20.0;
        int var18 = (int)Math.max(1.0, var127);
        long var102;
        if (GlobalState.isHoldingDragonEgg(var1, 2044938455)) {
            var102 = 21000L;
        } else {
            var102 = 42000L;
        }

        PlayerDataStore var48 = this.players;
        PlayerData var21 = var48.get(var14, 1264010596);
        long var22 = (long)var18 * 50L;
        Map var57 = this.fire.crispActiveUntil;
        Long var135 = var15 + var22;
        var57.put(var14, var135);
        Map var61 = this.fire.cd4;
        long var138 = var15 + var22;
        String var176 = "fire";
        String var9 = "crisp";
        long var177 = GemTuning.cd(var176, var9, 42.0, var102, 1621983041);
        int var192 = var21.energy(1160853318);
        long var179 = EnergyScale.cd(var177, var192, 907518618);
        Long var142 = var138 + var179;
        var61.put(var14, var142);
        Component var108 = Text.legacy("&6\ud83d\udd2e &cYou have activated §6Crisp", 373192400);
        var1.sendMessage(var108);
        LinkedHashMap var64 = new LinkedHashMap();
        final LinkedHashMap var24 = var64;
        List var66 = this.activeSnapshots;
        var66.add(var64);
        String var143 = "fire";
        String var160 = "crisp-blocks";
        double var144 = GemTuning.radius(var143, var160, 5.0, 1058601812);
        int var25 = (int)Math.max(0.0, var144);

        for (int var26 = 1; var26 <= var25; var26++) {
            int var27 = var26;
            BukkitScheduler var76 = Bukkit.getScheduler();
            BlissPlugin var112 = this.plugin;
            Runnable var147 = () -> this.transformShell(var17, var27, var24, 692698386);
            long var166 = (long)(var26 - 1) * 2L;
            var76.runTaskLater(var112, var147, var166);
        }

        String var78 = "fire";
        String var113 = "crisp";
        double var195 = GemTuning.radius(var78, var113, 4.0, 1058601812);
        World var82 = var17.getWorld();

        for (Entity var29 : var82.getNearbyEntities(var17, var195, var195, var195)) {
            if (var29 instanceof Player var30) {
                TrustStore var99 = this.trust;
                UUID var120 = var1.getUniqueId();
                UUID var158 = var30.getUniqueId();
                if (var99.isTrusted(var120, var158, 1736364606)) {
                    continue;
                }
            }

            int var123 = Math.max(var29.getFireTicks(), 80);
            var29.setFireTicks(var123);
        }

        this.scheduleRings(var17, 530355385);
        this.scheduleWaterClear(var14, var17, var18, 225361698);
        BukkitRunnable var89 = new BukkitRunnable() {
            public void run() {
                List var4 = FireCrispListener.this.activeSnapshots;
                Map var17 = var24;
                if (var4.remove(var17)) {
                    FireCrispListener.revertBlocks(var24, 359138727);
                }

                Map var10 = FireCrispListener.this.activeCrisps;
                UUID var19 = var14;
                var10.remove(var19);
                Map var15 = FireCrispListener.this.fire.crispActiveUntil;
                UUID var21 = var14;
                var15.remove(var21);
            }
        };
        BlissPlugin var118 = this.plugin;
        long var155 = (long)var18;
        var89.runTaskLater(var118, var155);
    }

    @EventHandler
    public void onCrispKnockback(EntityKnockbackEvent var1) {
        if (this.burning) {
            var1.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent var1) {
        Player var8 = var1.getPlayer();
        Map var15 = this.activeCrisps;
        UUID var41 = var8.getUniqueId();
        Location var9 = (Location)var15.get(var41);
        if (var9 != null) {
            World var21 = var8.getLocation().getWorld();
            World var43 = var9.getWorld();
            if (var21.equals(var43)) {
                Location var24 = var8.getLocation();
                if (!(var24.distanceSquared(var9) > 25.0)) {
                    ItemStack var10 = var1.getItemDrop().getItemStack();
                    Map var31 = AUTOSMELT;
                    Material var46 = var10.getType();
                    Material var11 = (Material)var31.get(var46);
                    if (var11 != null) {
                        int var53 = var10.getAmount();
                        ItemStack var35 = new ItemStack(var11, var53);
                        Item var37 = var1.getItemDrop();
                        var37.setItemStack(var35);
                        Material var39 = var10.getType();
                        Material var48 = Material.CHICKEN;
                        if (var39 != var48) {
                            Location var50 = var8.getLocation();
                            Sound var52 = Sound.BLOCK_FIRE_EXTINGUISH;
                            var8.playSound(var50, var52, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    private void scheduleRings(Location var1, int var2) {
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
                    drawRing(var1, var7, 1109235422);
                }
            };
            long var50 = (long)var10;
            var17.runTaskLater(var23, var32, var50);
            var10++;
        }
    }

    private static void drawRing(Location var0, double var1, int var3) {
        if (var0.getWorld() != null) {
            int var61 = (int)Math.round(var1 * 12.0);
            int var24 = Math.max(16, var61);
            Color var64 = Color.fromRGB(255, 119, 0);
            DustOptions var36 = new DustOptions(var64, 1.0F);
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

    private void transformShell(Location var1, int var2, Map<Location, FireCrispListener.SavedBlock> var3, int var4) {
        if (var1.getWorld() != null) {
            double var25 = (double)var2;
            double var79 = (double)var2;
            double var11 = var25 * var79;
            int var13 = var1.getBlockX();
            int var14 = var1.getBlockY();
            int var15 = var1.getBlockZ();

            for (int var16 = -var2; var16 <= var2; var16++) {
                for (int var17 = -var2; var17 <= var2; var17++) {
                    for (int var18 = -var2; var18 <= var2; var18++) {
                        int var45 = var16 * var16;
                        int var70 = var17 * var17;
                        int var46 = var45 + var70;
                        int var72 = var18 * var18;
                        double var48 = (double)(var46 + var72);
                        if (!(var48 > var11)) {
                            World var52 = var1.getWorld();
                            int var74 = var13 + var16;
                            int var87 = var14 + var17;
                            int var91 = var15 + var18;
                            Block var19 = var52.getBlockAt(var74, var87, var91);
                            Location var20 = var19.getLocation();
                            if (!var3.containsKey(var20)) {
                                Material var21 = var19.getType();
                                Material var22 = this.pickReplacement(var21, 495170602);
                                if (var22 != null) {
                                    BlockData var93 = var19.getBlockData();
                                    FireCrispListener.SavedBlock var88 = new FireCrispListener.SavedBlock(var93, var22);
                                    var3.put(var20, var88);
                                    var19.setType(var22, true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Material pickReplacement(Material var1, int var2) {
        Set var3 = GROUND;
        if (var3.contains(var1)) {
            Material[] var7 = STONE_REPLACEMENTS;
            Random var25 = this.random;
            int var43 = STONE_REPLACEMENTS.length;
            int var26 = var25.nextInt(var43);
            return var7[var26];
        } else {
            Material var27 = Material.SHORT_GRASS;
            if (var1 == var27) {
                Material[] var10 = SHORT_GRASS_REPLACEMENTS;
                Random var29 = this.random;
                int var45 = SHORT_GRASS_REPLACEMENTS.length;
                int var30 = var29.nextInt(var45);
                return var10[var30];
            } else {
                Material var31 = Material.TALL_GRASS;
                if (var1 == var31) {
                    Material[] var22 = TALL_GRASS_REPLACEMENTS;
                    Random var41 = this.random;
                    int var51 = TALL_GRASS_REPLACEMENTS.length;
                    int var42 = var41.nextInt(var51);
                    return var22[var42];
                } else {
                    Set var13 = LEAVES;
                    if (var13.contains(var1)) {
                        Material[] var15 = LEAF_REPLACEMENTS;
                        Random var34 = this.random;
                        int var47 = LEAF_REPLACEMENTS.length;
                        int var35 = var34.nextInt(var47);
                        return var15[var35];
                    } else {
                        Set var17 = LOGS;
                        if (var17.contains(var1)) {
                            Material[] var19 = LOG_REPLACEMENTS;
                            Random var38 = this.random;
                            int var49 = LOG_REPLACEMENTS.length;
                            int var39 = var38.nextInt(var49);
                            return var19[var39];
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
    }

    private static void revertBlocks(Map<Location, FireCrispListener.SavedBlock> var0, int var1) {
        for (Entry<Location, FireCrispListener.SavedBlock> var6 : var0.entrySet()) {
            Block var7 = var6.getKey().getBlock();
            Material var20 = var7.getType();
            Material var24 = var6.getValue().placed();
            if (var20 == var24) {
                BlockData var28 = var6.getValue().original();
                var7.setBlockData(var28, true);
            }
        }
    }

    public void revertAll(int var1) {
        List var15 = this.activeSnapshots;
        ArrayList var2 = new ArrayList(var15);
        Iterator var4 = var2.iterator();

        while (var4.hasNext()) {
            revertBlocks((Map<Location, FireCrispListener.SavedBlock>)var4.next(), 359138727);
        }

        this.activeSnapshots.clear();
    }

    private void scheduleWaterClear(UUID var1, Location var2, int var3, int var4) {
        int var20 = var3 / 10;
        int var11 = Math.max(1, var20);

        for (int var12 = 1; var12 <= var11; var12++) {
            BukkitScheduler var18 = Bukkit.getScheduler();
            BlissPlugin var23 = this.plugin;
            Runnable var25 = () -> {
                Map var5 = this.activeCrisps;
                if (var5.containsKey(var1)) {
                    this.clearWaterOnce(var2, 1152801768);
                }
            };
            long var30 = (long)var12;
            long var28 = 10L * var30;
            var18.runTaskLater(var23, var25, var28);
        }
    }

    private void clearWaterOnce(Location var1, int var2) {
        for (int var19 = -5; var19 <= 5; var19++) {
            for (int var20 = -5; var20 <= 5; var20++) {
                for (int var21 = -5; var21 <= 5; var21++) {
                    World var33 = var1.getWorld();
                    int var48 = var1.getBlockX();
                    int var49 = var48 + var19;
                    int var58 = var1.getBlockY();
                    int var59 = var58 + var20;
                    int var66 = var1.getBlockZ();
                    int var67 = var66 + var21;
                    Block var22 = var33.getBlockAt(var49, var59, var67);
                    Material var36 = var22.getType();
                    Material var50 = Material.WATER;
                    if (var36 == var50) {
                        Material var51 = Material.AIR;
                        var22.setType(var51, true);
                        if (this.random.nextInt(100) < 30) {
                            World var42 = var22.getWorld();
                            Location var55 = var22.getLocation();
                            Sound var61 = Sound.BLOCK_FIRE_EXTINGUISH;
                            var42.playSound(var55, var61, 0.2F, 1.0F);
                            World var44 = var22.getWorld();
                            Particle var56 = Particle.CLOUD;
                            Location var64 = var22.getLocation().add(0.5, 0.5, 0.5);
                            Particles.spawn(var44, var56, var64, 2, 0.1, 0.1, 0.1, 0.0, 1986132876);
                        }
                    }
                }
            }
        }
    }

    private static boolean isSword(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD-> true;
            default -> false;
        };
    }

    private static boolean isAxe(Material var0, int var1) {
        return switch (var0) {
            case WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE, NETHERITE_AXE-> true;
            default -> false;
        };
    }

    private static String readableRemaining(long var0, int var2) {
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
        Material var0 = Material.OAK_LEAVES;
        Material[] var64 = new Material[7];
        Material var3 = Material.SPRUCE_LEAVES;
        var64[0] = var3;
        Material var188 = Material.BIRCH_LEAVES;
        var64[1] = var188;
        Material var189 = Material.ACACIA_LEAVES;
        var64[2] = var189;
        Material var190 = Material.CHERRY_LEAVES;
        var64[3] = var190;
        Material var191 = Material.MANGROVE_LEAVES;
        var64[4] = var191;
        Material var192 = Material.JUNGLE_LEAVES;
        var64[5] = var192;
        Material var193 = Material.DARK_OAK_LEAVES;
        var64[6] = var193;
        LEAVES = EnumSet.of(var0, var64);
        Material var5 = Material.OAK_LOG;
        Material[] var66 = new Material[7];
        Material var194 = Material.SPRUCE_LOG;
        var66[0] = var194;
        Material var195 = Material.ACACIA_LOG;
        var66[1] = var195;
        Material var196 = Material.DARK_OAK_LOG;
        var66[2] = var196;
        Material var197 = Material.CHERRY_LOG;
        var66[3] = var197;
        Material var198 = Material.BIRCH_LOG;
        var66[4] = var198;
        Material var199 = Material.JUNGLE_LOG;
        var66[5] = var199;
        Material var200 = Material.MANGROVE_LOG;
        var66[6] = var200;
        LOGS = EnumSet.of(var5, var66);
        Material var7 = Material.GRASS_BLOCK;
        Material[] var68 = new Material[39];
        Material var201 = Material.DIRT;
        var68[0] = var201;
        Material var202 = Material.COARSE_DIRT;
        var68[1] = var202;
        Material var203 = Material.ROOTED_DIRT;
        var68[2] = var203;
        Material var204 = Material.PODZOL;
        var68[3] = var204;
        Material var205 = Material.MYCELIUM;
        var68[4] = var205;
        Material var206 = Material.DIRT_PATH;
        var68[5] = var206;
        Material var207 = Material.FARMLAND;
        var68[6] = var207;
        Material var208 = Material.MUD;
        var68[7] = var208;
        Material var209 = Material.MUDDY_MANGROVE_ROOTS;
        var68[8] = var209;
        Material var210 = Material.MOSS_BLOCK;
        var68[9] = var210;
        Material var211 = Material.STONE;
        var68[10] = var211;
        Material var212 = Material.GRANITE;
        var68[11] = var212;
        Material var213 = Material.DIORITE;
        var68[12] = var213;
        Material var214 = Material.ANDESITE;
        var68[13] = var214;
        Material var215 = Material.DEEPSLATE;
        var68[14] = var215;
        Material var216 = Material.TUFF;
        var68[15] = var216;
        Material var217 = Material.CALCITE;
        var68[16] = var217;
        Material var218 = Material.COBBLESTONE;
        var68[17] = var218;
        Material var219 = Material.GRAVEL;
        var68[18] = var219;
        Material var220 = Material.CLAY;
        var68[19] = var220;
        Material var221 = Material.SAND;
        var68[20] = var221;
        Material var222 = Material.RED_SAND;
        var68[21] = var222;
        Material var223 = Material.SANDSTONE;
        var68[22] = var223;
        Material var224 = Material.RED_SANDSTONE;
        var68[23] = var224;
        Material var225 = Material.SMOOTH_SANDSTONE;
        var68[24] = var225;
        Material var226 = Material.TERRACOTTA;
        var68[25] = var226;
        Material var227 = Material.WHITE_TERRACOTTA;
        var68[26] = var227;
        Material var228 = Material.ORANGE_TERRACOTTA;
        var68[27] = var228;
        Material var229 = Material.YELLOW_TERRACOTTA;
        var68[28] = var229;
        Material var230 = Material.BROWN_TERRACOTTA;
        var68[29] = var230;
        Material var231 = Material.RED_TERRACOTTA;
        var68[30] = var231;
        Material var232 = Material.LIGHT_GRAY_TERRACOTTA;
        var68[31] = var232;
        Material var233 = Material.SNOW_BLOCK;
        var68[32] = var233;
        Material var234 = Material.POWDER_SNOW;
        var68[33] = var234;
        Material var235 = Material.ICE;
        var68[34] = var235;
        Material var236 = Material.PACKED_ICE;
        var68[35] = var236;
        Material var237 = Material.BLUE_ICE;
        var68[36] = var237;
        Material var238 = Material.SOUL_SAND;
        var68[37] = var238;
        Material var239 = Material.SOUL_SOIL;
        var68[38] = var239;
        GROUND = EnumSet.of(var7, var68);
        Material[] var10 = new Material[3];
        Material var177 = Material.NETHERRACK;
        var10[0] = var177;
        Material var178 = Material.MAGMA_BLOCK;
        var10[1] = var178;
        Material var179 = Material.BASALT;
        var10[2] = var179;
        STONE_REPLACEMENTS = var10;
        Material[] var12 = new Material[2];
        Material var180 = Material.CRIMSON_STEM;
        var12[0] = var180;
        Material var181 = Material.WARPED_STEM;
        var12[1] = var181;
        LOG_REPLACEMENTS = var12;
        Material[] var14 = new Material[2];
        Material var182 = Material.NETHER_WART_BLOCK;
        var14[0] = var182;
        Material var183 = Material.WARPED_WART_BLOCK;
        var14[1] = var183;
        LEAF_REPLACEMENTS = var14;
        Material[] var16 = new Material[2];
        Material var184 = Material.CRIMSON_ROOTS;
        var16[0] = var184;
        Material var185 = Material.WARPED_ROOTS;
        var16[1] = var185;
        SHORT_GRASS_REPLACEMENTS = var16;
        Material[] var18 = new Material[2];
        Material var186 = Material.TWISTING_VINES;
        var18[0] = var186;
        Material var187 = Material.WEEPING_VINES;
        var18[1] = var187;
        TALL_GRASS_REPLACEMENTS = var18;
        EnumMap var19 = new EnumMap(Material.class);
        AUTOSMELT = var19;
        Map var20 = AUTOSMELT;
        Material var69 = Material.POTATO;
        Material var103 = Material.BAKED_POTATO;
        var20.put(var69, var103);
        Map var22 = AUTOSMELT;
        Material var70 = Material.RAW_IRON;
        Material var104 = Material.IRON_INGOT;
        var22.put(var70, var104);
        Map var24 = AUTOSMELT;
        Material var71 = Material.RAW_COPPER;
        Material var105 = Material.COPPER_INGOT;
        var24.put(var71, var105);
        Map var26 = AUTOSMELT;
        Material var72 = Material.RAW_GOLD;
        Material var106 = Material.GOLD_INGOT;
        var26.put(var72, var106);
        Map var28 = AUTOSMELT;
        Material var73 = Material.MUTTON;
        Material var107 = Material.COOKED_MUTTON;
        var28.put(var73, var107);
        Map var30 = AUTOSMELT;
        Material var74 = Material.BEEF;
        Material var108 = Material.COOKED_BEEF;
        var30.put(var74, var108);
        Map var32 = AUTOSMELT;
        Material var75 = Material.ANCIENT_DEBRIS;
        Material var109 = Material.NETHERITE_SCRAP;
        var32.put(var75, var109);
        Map var34 = AUTOSMELT;
        Material var76 = Material.SAND;
        Material var110 = Material.GLASS;
        var34.put(var76, var110);
        Map var36 = AUTOSMELT;
        Material var77 = Material.COBBLESTONE;
        Material var111 = Material.STONE;
        var36.put(var77, var111);
        Map var38 = AUTOSMELT;
        Material var78 = Material.STONE;
        Material var112 = Material.SMOOTH_STONE;
        var38.put(var78, var112);
        Map var40 = AUTOSMELT;
        Material var79 = Material.COBBLED_DEEPSLATE;
        Material var113 = Material.DEEPSLATE;
        var40.put(var79, var113);
        Map var42 = AUTOSMELT;
        Material var80 = Material.CHICKEN;
        Material var114 = Material.COOKED_CHICKEN;
        var42.put(var80, var114);
        Map var44 = AUTOSMELT;
        Material var81 = Material.PORKCHOP;
        Material var115 = Material.COOKED_PORKCHOP;
        var44.put(var81, var115);
        Map var46 = AUTOSMELT;
        Material var82 = Material.OAK_LOG;
        Material var116 = Material.CHARCOAL;
        var46.put(var82, var116);
        Map var48 = AUTOSMELT;
        Material var83 = Material.SPRUCE_LOG;
        Material var117 = Material.CHARCOAL;
        var48.put(var83, var117);
        Map var50 = AUTOSMELT;
        Material var84 = Material.ACACIA_LOG;
        Material var118 = Material.CHARCOAL;
        var50.put(var84, var118);
        Map var52 = AUTOSMELT;
        Material var85 = Material.DARK_OAK_LOG;
        Material var119 = Material.CHARCOAL;
        var52.put(var85, var119);
        Map var54 = AUTOSMELT;
        Material var86 = Material.CHERRY_LOG;
        Material var120 = Material.CHARCOAL;
        var54.put(var86, var120);
        Map var56 = AUTOSMELT;
        Material var87 = Material.BIRCH_LOG;
        Material var121 = Material.CHARCOAL;
        var56.put(var87, var121);
        Map var58 = AUTOSMELT;
        Material var88 = Material.JUNGLE_LOG;
        Material var122 = Material.CHARCOAL;
        var58.put(var88, var122);
        Map var60 = AUTOSMELT;
        Material var89 = Material.MANGROVE_LOG;
        Material var123 = Material.CHARCOAL;
        var60.put(var89, var123);
        Map var62 = AUTOSMELT;
        Material var90 = Material.CLAY;
        Material var124 = Material.BRICK;
        var62.put(var90, var124);
    }

    static record SavedBlock(BlockData original, Material placed) {
    }
}
