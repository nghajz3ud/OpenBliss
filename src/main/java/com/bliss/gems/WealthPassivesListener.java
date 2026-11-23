package com.bliss.gems;

import com.bliss.BlissPlugin;
import com.bliss.core.GemTuning;
import com.bliss.core.GemType;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.mythics.MythicItems;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class WealthPassivesListener implements Listener {
    private static final double EXP_MULTIPLIER = 1.5;
    private static final int BONUS_ORE_CHANCE_PERCENT = 50;
    private static final int BONUS_MOB_CHANCE_PERCENT = 50;
    private static final Map<Material, Material> ORE_TO_RAW;
    private final BlissPlugin plugin;
    private final PlayerDataStore players;
    private final WealthGemListener wealth;
    private final Random random;

    public WealthPassivesListener(BlissPlugin var1, PlayerDataStore var2, WealthGemListener var3, int var4) {
        Random var6 = new Random();
        this.random = var6;
        this.plugin = var1;
        this.players = var2;
        this.wealth = var3;
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onMineOre(BlockBreakEvent var1) {
        Player var10 = var1.getPlayer();
        Map var22 = ORE_TO_RAW;
        Material var83 = var1.getBlock().getType();
        Material var11 = (Material)var22.get(var83);
        if (var11 != null) {
            ItemStack var12 = var10.getInventory().getItemInMainHand();
            Enchantment var84 = Enchantment.SILK_TOUCH;
            if (!var12.containsEnchantment(var84)) {
                if (var1.isDropItems()) {
                    Block var34 = var1.getBlock();
                    if (!var34.getDrops(var12, var10).isEmpty()) {
                        if (this.holdsWealth(var10, 187201869)) {
                            PlayerDataStore var40 = this.players;
                            UUID var88 = var10.getUniqueId();
                            if (var40.get(var88, 1264010596).canUsePassives(2051432579)) {
                                String var44 = "wealth";
                                String var89 = "bonus-ore";
                                double var14 = GemTuning.chance(var44, var89, 50.0, 1091538029);
                                double var50 = (double)this.random.nextInt(100);
                                if (var50 < var14) {
                                    World var55 = var1.getBlock().getWorld();
                                    Location var94 = var1.getBlock().getLocation().add(0.5, 0.5, 0.5);
                                    ItemStack var109 = new ItemStack(var11, 1);
                                    var55.dropItemNaturally(var94, var109);
                                }

                                String var57 = "wealth";
                                String var95 = "bonus-ore-rich-rush";
                                double var16 = GemTuning.chance(var57, var95, 50.0, 1091538029);
                                long var18 = System.currentTimeMillis();
                                Map var64 = this.wealth.richRushExpiry;
                                UUID var97 = var10.getUniqueId();
                                Long var20 = (Long)var64.get(var97);
                                if (var20 != null) {
                                    long var69 = var20;
                                    if (var69 > var18) {
                                        double var75 = (double)this.random.nextInt(100);
                                        if (var75 < var16) {
                                            World var80 = var1.getBlock().getWorld();
                                            Location var102 = var1.getBlock().getLocation().add(0.5, 0.5, 0.5);
                                            ItemStack var118 = new ItemStack(var11, 1);
                                            var80.dropItemNaturally(var102, var118);
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
    public void onExtractScrap(FurnaceExtractEvent var1) {
        Material var12 = var1.getItemType();
        Material var3 = Material.NETHERITE_SCRAP;
        if (var12 == var3) {
            Player var7 = var1.getPlayer();
            if (this.holdsWealth(var7, 187201869)) {
                PlayerDataStore var18 = this.players;
                UUID var40 = var7.getUniqueId();
                if (var18.get(var40, 1264010596).canUsePassives(2051432579)) {
                    String var21 = "wealth";
                    String var41 = "double-debris";
                    if (GemTuning.passive(var21, var41, true, 14309468)) {
                        Material var46 = Material.NETHERITE_SCRAP;
                        int var48 = var1.getItemAmount();
                        ItemStack var23 = new ItemStack(var46, var48);
                        PlayerInventory var25 = var7.getInventory();
                        ItemStack[] var43 = new ItemStack[]{var23};

                        for (ItemStack var11 : var25.addItem(var43).values()) {
                            World var36 = var7.getWorld();
                            Location var45 = var7.getLocation();
                            var36.dropItemNaturally(var45, var11);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onMobDeathRichRush(EntityDeathEvent var1) {
        LivingEntity var6 = var1.getEntity();
        if (!(var6 instanceof Player)) {
            if (!var6.hasMetadata("NPC")) {
                Player var7 = var6.getKiller();
                if (var7 != null) {
                    if (this.holdsWealth(var7, 187201869)) {
                        PlayerDataStore var25 = this.players;
                        UUID var68 = var7.getUniqueId();
                        if (var25.get(var68, 1264010596).canUsePassives(2051432579)) {
                            Map var30 = this.wealth.richRushExpiry;
                            UUID var70 = var7.getUniqueId();
                            Long var8 = (Long)var30.get(var70);
                            if (var8 != null) {
                                long var35 = var8;
                                long var4 = System.currentTimeMillis();
                                if (var35 > var4) {
                                    String var38 = "wealth";
                                    String var71 = "bonus-mob-rich-rush";
                                    double var9 = GemTuning.chance(var38, var71, 50.0, 1091538029);
                                    ArrayList var41 = new ArrayList();
                                    ArrayList var11 = var41;

                                    for (ItemStack var13 : var1.getDrops()) {
                                        if (var13 != null) {
                                            Material var52 = var13.getType();
                                            Material var72 = Material.AIR;
                                            if (var52 != var72 && !neverDuplicate(var13, 1888130266)) {
                                                double var58 = (double)this.random.nextInt(100);
                                                if (var58 < var9) {
                                                    ItemStack var75 = var13.clone();
                                                    var11.add(var75);
                                                }
                                            }
                                        }
                                    }

                                    List var64 = var1.getDrops();
                                    var64.addAll(var11);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean neverDuplicate(ItemStack var0, int var1) {
        boolean var12;
        if (BlissCoreItems.gemIdOf(var0, 382441079) <= 0
            && !GoldGem.isGoldGem(var0)
            && !MythicItems.isMythicBloodGem(var0, 824514243)
            && !MythicItems.isMythicAegisGem(var0, 73386651)
            && !BlissCoreItems.isPrismaticEdge(var0, 1307109443)) {
            var12 = false;
        } else {
            var12 = true;
        }

        return var12;
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent var1) {
        Player var8 = var1.getPlayer();
        if (this.holdsWealth(var8, 187201869)) {
            PlayerDataStore var15 = this.players;
            UUID var31 = var8.getUniqueId();
            if (var15.get(var31, 1264010596).canUsePassives(2051432579)) {
                int var9 = var1.getAmount();
                if (var9 > 0) {
                    double var22 = (double)var9;
                    String var4 = "wealth";
                    String var5 = "experience-multiplier";
                    double var33 = GemTuning.power(var4, var5, 1.5, 1503850832);
                    int var10 = (int)Math.round(var22 * var33);
                    var1.setAmount(var10);
                }
            }
        }
    }

    private boolean holdsWealth(Player var1, int var2) {
        int var7 = BlissCoreItems.gemIdOf(var1.getInventory().getItemInMainHand(), 382441079);
        int var15 = GemType.WEALTH.id();
        if (var7 != var15) {
            int var11 = BlissCoreItems.gemIdOf(var1.getInventory().getItemInOffHand(), 382441079);
            int var17 = GemType.WEALTH.id();
            if (var11 != var17) {
                GemType var18 = GemType.WEALTH;
                if (!GoldGem.hasAbsorbedSoul(var1, var18, 1603837876)) {
                    return false;
                }
            }
        }

        return true;
    }

    static {
        EnumMap var0 = new EnumMap(Material.class);
        ORE_TO_RAW = var0;
        Map var2 = ORE_TO_RAW;
        Material var38 = Material.IRON_ORE;
        Material var56 = Material.RAW_IRON;
        var2.put(var38, var56);
        Map var4 = ORE_TO_RAW;
        Material var39 = Material.DEEPSLATE_IRON_ORE;
        Material var57 = Material.RAW_IRON;
        var4.put(var39, var57);
        Map var6 = ORE_TO_RAW;
        Material var40 = Material.COPPER_ORE;
        Material var58 = Material.RAW_COPPER;
        var6.put(var40, var58);
        Map var8 = ORE_TO_RAW;
        Material var41 = Material.DEEPSLATE_COPPER_ORE;
        Material var59 = Material.RAW_COPPER;
        var8.put(var41, var59);
        Map var10 = ORE_TO_RAW;
        Material var42 = Material.GOLD_ORE;
        Material var60 = Material.RAW_GOLD;
        var10.put(var42, var60);
        Map var12 = ORE_TO_RAW;
        Material var43 = Material.DEEPSLATE_GOLD_ORE;
        Material var61 = Material.RAW_GOLD;
        var12.put(var43, var61);
        Map var14 = ORE_TO_RAW;
        Material var44 = Material.NETHER_GOLD_ORE;
        Material var62 = Material.GOLD_NUGGET;
        var14.put(var44, var62);
        Map var16 = ORE_TO_RAW;
        Material var45 = Material.COAL_ORE;
        Material var63 = Material.COAL;
        var16.put(var45, var63);
        Map var18 = ORE_TO_RAW;
        Material var46 = Material.DEEPSLATE_COAL_ORE;
        Material var64 = Material.COAL;
        var18.put(var46, var64);
        Map var20 = ORE_TO_RAW;
        Material var47 = Material.LAPIS_ORE;
        Material var65 = Material.LAPIS_LAZULI;
        var20.put(var47, var65);
        Map var22 = ORE_TO_RAW;
        Material var48 = Material.DEEPSLATE_LAPIS_ORE;
        Material var66 = Material.LAPIS_LAZULI;
        var22.put(var48, var66);
        Map var24 = ORE_TO_RAW;
        Material var49 = Material.REDSTONE_ORE;
        Material var67 = Material.REDSTONE;
        var24.put(var49, var67);
        Map var26 = ORE_TO_RAW;
        Material var50 = Material.DEEPSLATE_REDSTONE_ORE;
        Material var68 = Material.REDSTONE;
        var26.put(var50, var68);
        Map var28 = ORE_TO_RAW;
        Material var51 = Material.DIAMOND_ORE;
        Material var69 = Material.DIAMOND;
        var28.put(var51, var69);
        Map var30 = ORE_TO_RAW;
        Material var52 = Material.DEEPSLATE_DIAMOND_ORE;
        Material var70 = Material.DIAMOND;
        var30.put(var52, var70);
        Map var32 = ORE_TO_RAW;
        Material var53 = Material.EMERALD_ORE;
        Material var71 = Material.EMERALD;
        var32.put(var53, var71);
        Map var34 = ORE_TO_RAW;
        Material var54 = Material.DEEPSLATE_EMERALD_ORE;
        Material var72 = Material.EMERALD;
        var34.put(var54, var72);
        Map var36 = ORE_TO_RAW;
        Material var55 = Material.NETHER_QUARTZ_ORE;
        Material var73 = Material.QUARTZ;
        var36.put(var55, var73);
    }
}
