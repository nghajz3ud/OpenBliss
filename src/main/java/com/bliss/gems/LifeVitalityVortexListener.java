package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.EnergyScale;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public final class LifeVitalityVortexListener implements Listener {
    private static final long VORTEX_COOLDOWN_MS = 45000L;
    private static final int VORTEX_DURATION_TICKS = 180;
    private static final int MAX_RADIUS = 5;
    private static final int EXPAND_PERIOD_TICKS = 4;
    private static final Set<Material> FLOWERS;
    private static final Material[] DEAD_FANS;
    private static final Set<Material> LEAVES;
    private static final Material[] DEAD_BLOCKS;
    private static final Set<Material> LOGS;
    private static final Set<Material> GROUND;
    private static final Set<Material> GRASSY;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final LifeGemListener life;
    private final Random random;
    private final Set<UUID> doubleClickLatch;
    private final Map<UUID, Integer> lastInteractTick;
    public final Map<UUID, Location> activeVortices;
    private final Set<Location> ownedBlocks;
    private final List<Map<Location, LifeVitalityVortexListener.SavedBlock>> activeSnapshots;

    public LifeVitalityVortexListener(BlissPlugin var1, PlayerDataStore var2, LifeGemListener var3, int var4) {
        Random var6 = new Random();
        this.random = var6;
        KeySetView var16 = ConcurrentHashMap.newKeySet();
        this.doubleClickLatch = var16;
        ConcurrentHashMap var17 = new ConcurrentHashMap();
        this.lastInteractTick = var17;
        ConcurrentHashMap var18 = new ConcurrentHashMap();
        this.activeVortices = var18;
        KeySetView var19 = ConcurrentHashMap.newKeySet();
        this.ownedBlocks = var19;
        ArrayList var20 = new ArrayList();
        this.activeSnapshots = var20;
        this.plugin = var1;
        this.players = var2;
        this.life = var3;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var18 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_BLOCK;
        if (var18 != var3) {
            Action var20 = var1.getAction();
            Action var97 = Action.RIGHT_CLICK_AIR;
            if (var20 != var97) {
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
        Integer var122 = var9;
        var34.put(var8, var122);
        if (!BlissCoreItems.isUtilityHead(var7.getInventory().getItemInMainHand(), 1077770594)) {
            int var44 = BlissCoreItems.gemIdOf(var7.getInventory().getItemInOffHand(), 382441079);
            int var102 = GemType.LIFE.id();
            if (var44 != var102) {
                GemType var103 = GemType.LIFE;
                if (!GoldGem.isWieldingSoulOff(var7, var103, 1989261555)) {
                    return;
                }
            }

            PlayerDataStore var48 = this.players;
            PlayerData var12 = var48.get(var8, 1264010596);
            if (!var12.isBroken(430675503) && var12.canUseAbilities(325243089)) {
                Material var13 = var7.getInventory().getItemInMainHand().getType();
                Material var105 = Material.AIR;
                if (var13 == var105 || isSword(var13, 900497498) || isAxe(var13, 484365426)) {
                    long var14 = System.currentTimeMillis();
                    Map var67 = this.life.cd4;
                    Long var16 = (Long)var67.get(var8);
                    if (var16 != null) {
                        long var72 = var16;
                        if (var72 > var14) {
                            long var108 = var16;
                            Component var114 = Text.legacy(
                                "<##FE04B4>\ud83d\udd2e &cYour <##FE04B4>Vitality Vortex &cskill is on cooldown for &6" + readable(var108 - var14, 693692149),
                                373192400
                            );
                            var7.sendMessage(var114);
                            return;
                        }
                    }

                    if (GlobalState.forPlayer(var7, 1433087673).sneakCast(704684993)) {
                        if (!var7.isSneaking()) {
                            return;
                        }

                        Set var95 = this.doubleClickLatch;
                        var95.remove(var8);
                    } else {
                        Set var81 = this.doubleClickLatch;
                        if (!var81.contains(var8)) {
                            Set var84 = this.doubleClickLatch;
                            var84.add(var8);
                            BukkitScheduler var86 = Bukkit.getScheduler();
                            BlissPlugin var118 = this.plugin;
                            Runnable var126 = () -> {
                                Set var4 = this.doubleClickLatch;
                                var4.remove(var8);
                            };
                            var86.runTaskLater(var118, var126, 20L);
                            return;
                        }

                        Set var89 = this.doubleClickLatch;
                        var89.remove(var8);
                    }

                    this.activateVortex(var7, 1319966262);
                }
            }
        }
    }

    private void activateVortex(Player var1, int var2) {
        UUID var12 = var1.getUniqueId();
        Component var65 = Text.legacy("<##FE04B4>\ud83d\udd2e &cYou have activated <##FE04B4>Vitality Vortex", 373192400);
        var1.sendMessage(var65);
        long var27;
        if (GlobalState.isHoldingDragonEgg(var1, 2044938455)) {
            var27 = 22500L;
        } else {
            var27 = 45000L;
        }

        String var29 = "life";
        String var66 = "vitality-vortex";
        long var30 = GemTuning.cd(var29, var66, 45.0, var27, 1621983041);
        PlayerDataStore var79 = this.players;
        int var81 = var79.get(var12, 1264010596).energy(1160853318);
        long var120 = EnergyScale.cd(var30, var81, 907518618);
        long var17 = System.currentTimeMillis();
        Map var40 = this.life.vortexActiveUntil;
        Long var86 = var17 + 9000L;
        var40.put(var12, var86);
        Map var44 = this.life.cd4;
        long var89 = var17 + 9000L;
        Long var93 = var89 + var120;
        var44.put(var12, var93);
        var1.setFoodLevel(20);
        var1.setSaturation(20.0F);
        Location var19 = var1.getLocation().clone();
        HashMap var51 = new HashMap();
        HashMap var20 = var51;
        List var53 = this.activeSnapshots;
        var53.add(var51);
        Map var56 = this.activeVortices;
        var56.put(var12, var19);

        for (int var21 = 1; var21 <= 5; var21++) {
            int var22 = var21;
            BukkitScheduler var61 = Bukkit.getScheduler();
            BlissPlugin var75 = this.plugin;
            Runnable var96 = () -> this.transformRadius(var1, var19, var22, var20, 1481463005);
            long var104 = (long)(var21 - 1) * 4L;
            var61.runTaskLater(var75, var96, var104);
        }

        BukkitScheduler var63 = Bukkit.getScheduler();
        BlissPlugin var77 = this.plugin;
        Runnable var98 = () -> {
            List var5 = this.activeSnapshots;
            if (var5.remove(var20)) {
                this.revertSnapshot(var20, 2035391274);
            }

            Map var9 = this.activeVortices;
            var9.remove(var12);
            Map var13 = this.life.vortexActiveUntil;
            var13.remove(var12);
        };
        var63.runTaskLater(var77, var98, 180L);
    }

    private void revertSnapshot(Map<Location, LifeVitalityVortexListener.SavedBlock> var1, int var2) {
        for (Entry<Location, LifeVitalityVortexListener.SavedBlock> var7 : var1.entrySet()) {
            Set var17 = this.ownedBlocks;
            Location var26 = var7.getKey();
            var17.remove(var26);
            Block var8 = var7.getKey().getBlock();
            Material var24 = var8.getType();
            Material var30 = var7.getValue().placed();
            if (var24 == var30) {
                BlockData var34 = var7.getValue().original();
                var8.setBlockData(var34, true);
            }
        }
    }

    public void revertAll(int var1) {
        List<Map<Location, LifeVitalityVortexListener.SavedBlock>> var16 = this.activeSnapshots;

        for (Map<Location, LifeVitalityVortexListener.SavedBlock> var5 : new ArrayList<>(var16)) {
            this.revertSnapshot(var5, 2035391274);
        }

        this.activeSnapshots.clear();
    }

    private void transformRadius(Player var1, Location var2, int var3, Map<Location, LifeVitalityVortexListener.SavedBlock> var4, int var5) {
        if (var2.getWorld() != null) {
            double var26 = (double)var3;
            double var88 = (double)var3;
            double var12 = var26 * var88;
            int var14 = var2.getBlockX();
            int var15 = var2.getBlockY();
            int var16 = var2.getBlockZ();

            for (int var17 = -var3; var17 <= var3; var17++) {
                for (int var18 = -var3; var18 <= var3; var18++) {
                    for (int var19 = -var3; var19 <= var3; var19++) {
                        int var46 = var17 * var17;
                        int var77 = var18 * var18;
                        int var47 = var46 + var77;
                        int var79 = var19 * var19;
                        double var49 = (double)(var47 + var79);
                        if (!(var49 > var12)) {
                            World var53 = var2.getWorld();
                            int var81 = var14 + var17;
                            int var96 = var15 + var18;
                            int var101 = var16 + var19;
                            Block var20 = var53.getBlockAt(var81, var96, var101);
                            Location var21 = var20.getLocation();
                            if (!var4.containsKey(var21)) {
                                Set var60 = this.ownedBlocks;
                                if (!var60.contains(var21)) {
                                    Material var22 = var20.getType();
                                    Material var23 = this.pickReplacement(var1, var22, 1358443772);
                                    if (var23 != null) {
                                        Set var68 = this.ownedBlocks;
                                        var68.add(var21);
                                        BlockData var103 = var20.getBlockData();
                                        LifeVitalityVortexListener.SavedBlock var98 = new LifeVitalityVortexListener.SavedBlock(var103, var23);
                                        var4.put(var21, var98);
                                        var20.setType(var23, true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Material pickReplacement(Player var1, Material var2, int var3) {
        Set var4 = LEAVES;
        if (var4.contains(var2)) {
            PotionEffectType var32 = PotionEffectType.ABSORPTION;
            applyOne(var1, var32, 1, 1069738688);
            Material[] var9 = DEAD_BLOCKS;
            Random var34 = this.random;
            int var57 = DEAD_BLOCKS.length;
            int var35 = var34.nextInt(var57);
            return var9[var35];
        } else {
            Set var11 = FLOWERS;
            if (var11.contains(var2)) {
                PotionEffectType var37 = PotionEffectType.ABSORPTION;
                applyOne(var1, var37, 0, 1069738688);
                Material[] var14 = DEAD_FANS;
                Random var39 = this.random;
                int var60 = DEAD_FANS.length;
                int var40 = var39.nextInt(var60);
                return var14[var40];
            } else {
                Set var16 = GRASSY;
                if (var16.contains(var2)) {
                    PotionEffectType var42 = PotionEffectType.ABSORPTION;
                    applyOne(var1, var42, 0, 1069738688);
                    Material var43 = Material.GRASS_BLOCK;
                    Material var22;
                    if (var2 == var43) {
                        Material[] var20 = DEAD_BLOCKS;
                        Random var45 = this.random;
                        int var63 = DEAD_BLOCKS.length;
                        int var46 = var45.nextInt(var63);
                        var22 = var20[var46];
                    } else {
                        Material[] var21 = DEAD_FANS;
                        Random var48 = this.random;
                        int var65 = DEAD_FANS.length;
                        int var49 = var48.nextInt(var65);
                        var22 = var21[var49];
                    }

                    return var22;
                } else {
                    Set var23 = LOGS;
                    if (var23.contains(var2)) {
                        PotionEffectType var55 = PotionEffectType.RESISTANCE;
                        applyOne(var1, var55, 0, 1069738688);
                        return Material.ACACIA_LOG;
                    } else {
                        Set var25 = GROUND;
                        if (var25.contains(var2)) {
                            Material[] var28 = DEAD_BLOCKS;
                            Random var53 = this.random;
                            int var67 = DEAD_BLOCKS.length;
                            int var54 = var53.nextInt(var67);
                            return var28[var54];
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
    }

    private static void applyOne(Player var0, PotionEffectType var1, int var2, int var3) {
        PotionEffect var5 = new PotionEffect(var1, 20, var2, true, false, false);
        var0.addPotionEffect(var5);
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
        Material var0 = Material.CORNFLOWER;
        Material[] var17 = new Material[19];
        Material var3 = Material.POPPY;
        var17[0] = var3;
        Material var129 = Material.SUNFLOWER;
        var17[1] = var129;
        Material var130 = Material.DANDELION;
        var17[2] = var130;
        Material var131 = Material.BLUE_ORCHID;
        var17[3] = var131;
        Material var132 = Material.ALLIUM;
        var17[4] = var132;
        Material var133 = Material.AZURE_BLUET;
        var17[5] = var133;
        Material var134 = Material.RED_TULIP;
        var17[6] = var134;
        Material var135 = Material.ORANGE_TULIP;
        var17[7] = var135;
        Material var136 = Material.WHITE_TULIP;
        var17[8] = var136;
        Material var137 = Material.PINK_TULIP;
        var17[9] = var137;
        Material var138 = Material.OXEYE_DAISY;
        var17[10] = var138;
        Material var139 = Material.LILY_OF_THE_VALLEY;
        var17[11] = var139;
        Material var140 = Material.TORCHFLOWER;
        var17[12] = var140;
        Material var141 = Material.PINK_PETALS;
        var17[13] = var141;
        Material var142 = Material.LILAC;
        var17[14] = var142;
        Material var143 = Material.ROSE_BUSH;
        var17[15] = var143;
        Material var144 = Material.PEONY;
        var17[16] = var144;
        Material var145 = Material.BROWN_MUSHROOM;
        var17[17] = var145;
        Material var146 = Material.RED_MUSHROOM;
        var17[18] = var146;
        FLOWERS = EnumSet.of(var0, var17);
        Material[] var6 = new Material[10];
        Material var60 = Material.DEAD_BUBBLE_CORAL_FAN;
        var6[0] = var60;
        Material var61 = Material.DEAD_TUBE_CORAL;
        var6[1] = var61;
        Material var62 = Material.DEAD_BRAIN_CORAL;
        var6[2] = var62;
        Material var63 = Material.DEAD_BUBBLE_CORAL;
        var6[3] = var63;
        Material var64 = Material.DEAD_FIRE_CORAL;
        var6[4] = var64;
        Material var65 = Material.DEAD_HORN_CORAL;
        var6[5] = var65;
        Material var66 = Material.DEAD_TUBE_CORAL_FAN;
        var6[6] = var66;
        Material var67 = Material.DEAD_BRAIN_CORAL_FAN;
        var6[7] = var67;
        Material var68 = Material.DEAD_FIRE_CORAL_FAN;
        var6[8] = var68;
        Material var69 = Material.DEAD_HORN_CORAL_FAN;
        var6[9] = var69;
        DEAD_FANS = var6;
        Material var7 = Material.OAK_LEAVES;
        Material[] var19 = new Material[7];
        Material var147 = Material.SPRUCE_LEAVES;
        var19[0] = var147;
        Material var148 = Material.BIRCH_LEAVES;
        var19[1] = var148;
        Material var149 = Material.ACACIA_LEAVES;
        var19[2] = var149;
        Material var150 = Material.CHERRY_LEAVES;
        var19[3] = var150;
        Material var151 = Material.MANGROVE_LEAVES;
        var19[4] = var151;
        Material var152 = Material.JUNGLE_LEAVES;
        var19[5] = var152;
        Material var153 = Material.DARK_OAK_LEAVES;
        var19[6] = var153;
        LEAVES = EnumSet.of(var7, var19);
        Material[] var10 = new Material[6];
        Material var77 = Material.TUFF;
        var10[0] = var77;
        Material var78 = Material.DEAD_TUBE_CORAL_BLOCK;
        var10[1] = var78;
        Material var79 = Material.DEAD_BUBBLE_CORAL_BLOCK;
        var10[2] = var79;
        Material var80 = Material.DEAD_BRAIN_CORAL_BLOCK;
        var10[3] = var80;
        Material var81 = Material.DEAD_FIRE_CORAL_BLOCK;
        var10[4] = var81;
        Material var82 = Material.DEAD_HORN_CORAL_BLOCK;
        var10[5] = var82;
        DEAD_BLOCKS = var10;
        Material var11 = Material.OAK_LOG;
        Material[] var21 = new Material[7];
        Material var154 = Material.SPRUCE_LOG;
        var21[0] = var154;
        Material var155 = Material.ACACIA_LOG;
        var21[1] = var155;
        Material var156 = Material.DARK_OAK_LOG;
        var21[2] = var156;
        Material var157 = Material.CHERRY_LOG;
        var21[3] = var157;
        Material var158 = Material.BIRCH_LOG;
        var21[4] = var158;
        Material var159 = Material.JUNGLE_LOG;
        var21[5] = var159;
        Material var160 = Material.MANGROVE_LOG;
        var21[6] = var160;
        LOGS = EnumSet.of(var11, var21);
        Material var13 = Material.GRASS_BLOCK;
        Material[] var23 = new Material[38];
        Material var161 = Material.DIRT;
        var23[0] = var161;
        Material var162 = Material.COARSE_DIRT;
        var23[1] = var162;
        Material var163 = Material.ROOTED_DIRT;
        var23[2] = var163;
        Material var164 = Material.PODZOL;
        var23[3] = var164;
        Material var165 = Material.MYCELIUM;
        var23[4] = var165;
        Material var166 = Material.DIRT_PATH;
        var23[5] = var166;
        Material var167 = Material.FARMLAND;
        var23[6] = var167;
        Material var168 = Material.MUD;
        var23[7] = var168;
        Material var169 = Material.MUDDY_MANGROVE_ROOTS;
        var23[8] = var169;
        Material var170 = Material.MOSS_BLOCK;
        var23[9] = var170;
        Material var171 = Material.STONE;
        var23[10] = var171;
        Material var172 = Material.GRANITE;
        var23[11] = var172;
        Material var173 = Material.DIORITE;
        var23[12] = var173;
        Material var174 = Material.ANDESITE;
        var23[13] = var174;
        Material var175 = Material.DEEPSLATE;
        var23[14] = var175;
        Material var176 = Material.CALCITE;
        var23[15] = var176;
        Material var177 = Material.COBBLESTONE;
        var23[16] = var177;
        Material var178 = Material.GRAVEL;
        var23[17] = var178;
        Material var179 = Material.CLAY;
        var23[18] = var179;
        Material var180 = Material.SAND;
        var23[19] = var180;
        Material var181 = Material.RED_SAND;
        var23[20] = var181;
        Material var182 = Material.SANDSTONE;
        var23[21] = var182;
        Material var183 = Material.RED_SANDSTONE;
        var23[22] = var183;
        Material var184 = Material.SMOOTH_SANDSTONE;
        var23[23] = var184;
        Material var185 = Material.TERRACOTTA;
        var23[24] = var185;
        Material var186 = Material.WHITE_TERRACOTTA;
        var23[25] = var186;
        Material var187 = Material.ORANGE_TERRACOTTA;
        var23[26] = var187;
        Material var188 = Material.YELLOW_TERRACOTTA;
        var23[27] = var188;
        Material var189 = Material.BROWN_TERRACOTTA;
        var23[28] = var189;
        Material var190 = Material.RED_TERRACOTTA;
        var23[29] = var190;
        Material var191 = Material.LIGHT_GRAY_TERRACOTTA;
        var23[30] = var191;
        Material var192 = Material.SNOW_BLOCK;
        var23[31] = var192;
        Material var193 = Material.POWDER_SNOW;
        var23[32] = var193;
        Material var194 = Material.ICE;
        var23[33] = var194;
        Material var195 = Material.PACKED_ICE;
        var23[34] = var195;
        Material var196 = Material.BLUE_ICE;
        var23[35] = var196;
        Material var197 = Material.SOUL_SAND;
        var23[36] = var197;
        Material var198 = Material.SOUL_SOIL;
        var23[37] = var198;
        GROUND = EnumSet.of(var13, var23);
        Material var15 = Material.GRASS_BLOCK;
        Material var24 = Material.SHORT_GRASS;
        Material var41 = Material.TALL_GRASS;
        Material var128 = Material.FERN;
        Material var199 = Material.LARGE_FERN;
        GRASSY = EnumSet.of(var15, var24, var41, var128, var199);
    }

    private static record SavedBlock(BlockData original, Material placed) {
    }
}
