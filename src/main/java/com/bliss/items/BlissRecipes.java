package com.bliss.items;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.core.GlobalState;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;
import org.bukkit.inventory.meta.ItemMeta;

public final class BlissRecipes {
    private final BlissPlugin plugin;
    private final BlissCoreItems items;
    private final NamespacedKey traderKey;
    private final NamespacedKey upgraderKey;
    private final NamespacedKey fragmentKey;
    private final NamespacedKey repairKitKey;
    private final NamespacedKey restorationCoreKey;
    private final NamespacedKey restorationKey;
    private final Map<String, ItemStack[]> customGrids;
    private final File recipeFile;
    public static final String[] EDITABLE;

    public BlissRecipes(BlissPlugin var1, BlissCoreItems var2, int var3) {
        HashMap var5 = new HashMap();
        this.customGrids = var5;
        this.plugin = var1;
        this.items = var2;
        File var27 = var1.getDataFolder();
        File var20 = new File(var27, "recipes.yml");
        this.recipeFile = var20;
        NamespacedKey var21 = new NamespacedKey(var1, "trader");
        this.traderKey = var21;
        NamespacedKey var22 = new NamespacedKey(var1, "upgrader");
        this.upgraderKey = var22;
        NamespacedKey var23 = new NamespacedKey(var1, "gem_fragment");
        this.fragmentKey = var23;
        NamespacedKey var24 = new NamespacedKey(var1, "repair_kit");
        this.repairKitKey = var24;
        NamespacedKey var25 = new NamespacedKey(var1, "restoration_core");
        this.restorationCoreKey = var25;
        NamespacedKey var26 = new NamespacedKey(var1, "restoration");
        this.restorationKey = var26;
    }

    public boolean isTraderRegistered(int var1) {
        boolean var5;
        if (Bukkit.getRecipe(this.traderKey) != null) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public boolean isUpgraderRegistered(int var1) {
        boolean var5;
        if (Bukkit.getRecipe(this.upgraderKey) != null) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public void registerTrader(int var1) {
        if (!this.isTraderRegistered(1163655551)) {
            String var3 = "trader";
            if (this.isEdited(var3, 2034122298)) {
                String var28 = "trader";
                this.rebuild(var28, 1609602649);
            } else {
                NamespacedKey var29 = this.traderKey;
                ItemStack var34 = this.items.trader(662298763);
                ShapedRecipe var11 = new ShapedRecipe(var29, var34);
                String[] var24 = new String[3];
                String var6 = "DBD";
                var24[0] = var6;
                String var38 = "BCB";
                var24[1] = var38;
                String var39 = "DBD";
                var24[2] = var39;
                var11.shape(var24);
                Material var30 = Material.DIAMOND_BLOCK;
                var11.setIngredient('D', var30);
                Material var31 = Material.DRAGON_BREATH;
                var11.setIngredient('B', var31);
                Material var32 = Material.SCULK_CATALYST;
                var11.setIngredient('C', var32);
                Bukkit.addRecipe(var11);
            }
        }
    }

    public void unregisterTrader(int var1) {
        Bukkit.removeRecipe(this.traderKey);
    }

    public void registerUpgrader(int var1) {
        if (!this.isUpgraderRegistered(1232619566)) {
            String var3 = "upgrader";
            if (this.isEdited(var3, 2034122298)) {
                String var29 = "upgrader";
                this.rebuild(var29, 1609602649);
            } else {
                NamespacedKey var38 = this.upgraderKey;
                ItemStack var46 = this.items.upgrader(2125662274);
                ShapedRecipe var12 = new ShapedRecipe(var38, var46);
                String[] var31 = new String[3];
                String var6 = "NFN";
                var31[0] = var6;
                String var50 = "SXS";
                var31[1] = var50;
                String var51 = "EYE";
                var31[2] = var51;
                var12.shape(var31);
                Material var39 = Material.NETHERITE_INGOT;
                var12.setIngredient('N', var39);
                Material var40 = Material.SKELETON_SKULL;
                var12.setIngredient('S', var40);
                Material var41 = Material.NETHER_STAR;
                var12.setIngredient('X', var41);
                Material var42 = Material.ENCHANTED_GOLDEN_APPLE;
                var12.setIngredient('E', var42);
                ItemStack var54 = this.items.fragment(452582255);
                ExactChoice var43 = new ExactChoice(var54);
                var12.setIngredient('F', var43);
                Material var55 = Material.NAUTILUS_SHELL;
                MaterialChoice var44 = new MaterialChoice(var55);
                var12.setIngredient('Y', var44);
                Bukkit.addRecipe(var12);
            }
        }
    }

    public void unregisterUpgrader(int var1) {
        Bukkit.removeRecipe(this.upgraderKey);
    }

    public ItemStack[] traderGrid(int var1) {
        Material var3 = Material.DIAMOND_BLOCK;
        ItemStack var2 = new ItemStack(var3);
        Material var12 = Material.DRAGON_BREATH;
        ItemStack var8 = new ItemStack(var12);
        Material var13 = Material.SCULK_CATALYST;
        ItemStack var9 = new ItemStack(var13);
        return new ItemStack[]{var2, var8, var2, var8, var9, var8, var2, var8, var2};
    }

    public ItemStack[] upgraderGrid(int var1) {
        Material var3 = Material.NETHERITE_INGOT;
        ItemStack var2 = new ItemStack(var3);
        Material var22 = Material.SKELETON_SKULL;
        ItemStack var11 = new ItemStack(var22);
        Material var23 = Material.NETHER_STAR;
        ItemStack var12 = new ItemStack(var23);
        Material var24 = Material.ENCHANTED_GOLDEN_APPLE;
        ItemStack var13 = new ItemStack(var24);
        ItemStack var9 = this.items.fragment(452582255);
        ItemStack var10 = this.items.bottledEnergy(1532783840);
        return new ItemStack[]{var2, var9, var2, var11, var12, var11, var13, var10, var13};
    }

    public ItemStack traderResult() {
        return this.items.trader(662298763);
    }

    public ItemStack upgraderResult() {
        return this.items.upgrader(2125662274);
    }

    public void registerFragment(int var1) {
        if (Bukkit.getRecipe(this.fragmentKey) == null) {
            NamespacedKey var28 = this.fragmentKey;
            ItemStack var34 = this.items.fragment(452582255);
            ShapedRecipe var9 = new ShapedRecipe(var28, var34);
            String[] var23 = new String[3];
            String var5 = "DED";
            var23[0] = var5;
            String var38 = "AIA";
            var23[1] = var38;
            String var39 = "DED";
            var23[2] = var39;
            var9.shape(var23);
            Material var29 = Material.DIAMOND;
            var9.setIngredient('D', var29);
            Material var30 = Material.EMERALD;
            var9.setIngredient('E', var30);
            Material var31 = Material.AMETHYST_CLUSTER;
            var9.setIngredient('A', var31);
            Material var32 = Material.IRON_BLOCK;
            var9.setIngredient('I', var32);
            Bukkit.addRecipe(var9);
        }
    }

    public void registerRepairKit(int var1) {
        if (Bukkit.getRecipe(this.repairKitKey) == null) {
            NamespacedKey var28 = this.repairKitKey;
            ItemStack var34 = this.items.repairKit(1231295697);
            ShapedRecipe var9 = new ShapedRecipe(var28, var34);
            String[] var23 = new String[3];
            String var5 = "FAF";
            var23[0] = var5;
            String var38 = "NCN";
            var23[1] = var38;
            String var39 = "FAF";
            var23[2] = var39;
            var9.shape(var23);
            ItemStack var42 = this.items.fragment(452582255);
            ExactChoice var29 = new ExactChoice(var42);
            var9.setIngredient('F', var29);
            Material var30 = Material.ANVIL;
            var9.setIngredient('A', var30);
            Material var31 = Material.NETHERITE_INGOT;
            var9.setIngredient('N', var31);
            Material var32 = Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
            var9.setIngredient('C', var32);
            Bukkit.addRecipe(var9);
        }
    }

    public void registerRestorationCore(int var1) {
        if (Bukkit.getRecipe(this.restorationCoreKey) == null) {
            String var3 = "restorationcore";
            if (this.isEdited(var3, 2034122298)) {
                String var26 = "restorationcore";
                this.rebuild(var26, 1609602649);
            } else {
                NamespacedKey var33 = this.restorationCoreKey;
                ItemStack var39 = this.items.restorationCore(913365449);
                ShapedRecipe var13 = new ShapedRecipe(var33, var39);
                String[] var28 = new String[3];
                String var6 = "FDF";
                var28[0] = var6;
                String var43 = "RCR";
                var28[1] = var43;
                String var44 = "FDF";
                var28[2] = var44;
                var13.shape(var28);
                ItemStack var47 = this.items.fragment(452582255);
                ExactChoice var34 = new ExactChoice(var47);
                var13.setIngredient('F', var34);
                Material var35 = Material.DRAGON_HEAD;
                var13.setIngredient('D', var35);
                ItemStack var50 = this.items.repairKit(1231295697);
                ExactChoice var36 = new ExactChoice(var50);
                var13.setIngredient('R', var36);
                Material var37 = Material.RECOVERY_COMPASS;
                var13.setIngredient('C', var37);
                Bukkit.addRecipe(var13);
            }
        }
    }

    public void unregisterRestorationCore(int var1) {
        Bukkit.removeRecipe(this.restorationCoreKey);
    }

    public void registerRestorationItem(int var1) {
        if (Bukkit.getRecipe(this.restorationKey) == null) {
            NamespacedKey var43 = this.restorationKey;
            ItemStack var54 = this.items.restoration(1610991893);
            ShapedRecipe var9 = new ShapedRecipe(var43, var54);
            String[] var33 = new String[3];
            String var5 = "RLB";
            var33[0] = var5;
            String var58 = "PCU";
            var33[1] = var58;
            String var59 = "YWO";
            var33[2] = var59;
            var9.shape(var33);
            Material var44 = Material.RED_CANDLE;
            var9.setIngredient('R', var44);
            Material var45 = Material.LIME_CANDLE;
            var9.setIngredient('L', var45);
            Material var46 = Material.LIGHT_BLUE_CANDLE;
            var9.setIngredient('B', var46);
            Material var47 = Material.PINK_CANDLE;
            var9.setIngredient('P', var47);
            ItemStack var62 = this.items.restorationCore(913365449);
            ExactChoice var48 = new ExactChoice(var62);
            var9.setIngredient('C', var48);
            Material var49 = Material.PURPLE_CANDLE;
            var9.setIngredient('U', var49);
            Material var50 = Material.YELLOW_CANDLE;
            var9.setIngredient('Y', var50);
            Material var51 = Material.WHITE_CANDLE;
            var9.setIngredient('W', var51);
            Material var52 = Material.ORANGE_CANDLE;
            var9.setIngredient('O', var52);
            Bukkit.addRecipe(var9);
        }
    }

    public ItemStack[] fragmentGrid(int var1) {
        Material var3 = Material.DIAMOND;
        ItemStack var2 = new ItemStack(var3);
        Material var14 = Material.EMERALD;
        ItemStack var9 = new ItemStack(var14);
        Material var15 = Material.AMETHYST_CLUSTER;
        ItemStack var10 = new ItemStack(var15);
        Material var16 = Material.IRON_BLOCK;
        ItemStack var11 = new ItemStack(var16);
        return new ItemStack[]{var2, var9, var2, var10, var11, var10, var2, var9, var2};
    }

    public ItemStack[] repairKitGrid(int var1) {
        ItemStack var5 = this.items.fragment(452582255);
        Material var3 = Material.ANVIL;
        ItemStack var11 = new ItemStack(var3);
        Material var16 = Material.NETHERITE_INGOT;
        ItemStack var12 = new ItemStack(var16);
        Material var17 = Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
        ItemStack var13 = new ItemStack(var17);
        return new ItemStack[]{var5, var11, var5, var12, var13, var12, var5, var11, var5};
    }

    public ItemStack[] restorationCoreGrid(int var1) {
        ItemStack var5 = this.items.fragment(452582255);
        Material var3 = Material.DRAGON_HEAD;
        ItemStack var11 = new ItemStack(var3);
        ItemStack var7 = this.items.repairKit(1231295697);
        Material var18 = Material.RECOVERY_COMPASS;
        ItemStack var15 = new ItemStack(var18);
        return new ItemStack[]{var5, var11, var5, var7, var15, var7, var5, var11, var5};
    }

    public ItemStack[] restorationGrid(int var1) {
        ItemStack[] var6 = new ItemStack[9];
        Material var5 = Material.RED_CANDLE;
        ItemStack var4 = new ItemStack(var5);
        var6[0] = var4;
        Material var25 = Material.LIME_CANDLE;
        ItemStack var15 = new ItemStack(var25);
        var6[1] = var15;
        Material var26 = Material.LIGHT_BLUE_CANDLE;
        ItemStack var16 = new ItemStack(var26);
        var6[2] = var16;
        Material var27 = Material.PINK_CANDLE;
        ItemStack var17 = new ItemStack(var27);
        var6[3] = var17;
        ItemStack var20 = this.items.restorationCore(913365449);
        var6[4] = var20;
        Material var28 = Material.PURPLE_CANDLE;
        ItemStack var21 = new ItemStack(var28);
        var6[5] = var21;
        Material var29 = Material.YELLOW_CANDLE;
        ItemStack var22 = new ItemStack(var29);
        var6[6] = var22;
        Material var30 = Material.WHITE_CANDLE;
        ItemStack var23 = new ItemStack(var30);
        var6[7] = var23;
        Material var31 = Material.ORANGE_CANDLE;
        ItemStack var24 = new ItemStack(var31);
        var6[8] = var24;
        return var6;
    }

    public ItemStack fragmentResult() {
        return this.items.fragment(452582255);
    }

    public ItemStack repairKitResult() {
        return this.items.repairKit(1231295697);
    }

    public ItemStack restorationCoreResult() {
        return this.items.restorationCore(913365449);
    }

    public ItemStack restorationResult() {
        return this.items.restoration(1610991893);
    }

    private NamespacedKey keyFor(String var1, int var2) {
        return switch (var1) {
            case "trader" -> this.traderKey;
            case "upgrader" -> this.upgraderKey;
            case "fragment" -> this.fragmentKey;
            case "repairkit" -> this.repairKitKey;
            case "restorationcore" -> this.restorationCoreKey;
            default -> this.restorationKey;
        };
    }

    public ItemStack result(String var1, int var2) {
        return switch (var1) {
            case "trader" -> this.items.trader(662298763);
            case "upgrader" -> this.items.upgrader(2125662274);
            case "fragment" -> this.items.fragment(452582255);
            case "repairkit" -> this.items.repairKit(1231295697);
            case "restorationcore" -> this.items.restorationCore(913365449);
            default -> this.items.restoration(1610991893);
        };
    }

    private ItemStack[] defaultGrid(String var1, int var2) {
        return switch (var1) {
            case "trader" -> this.traderGrid(1705431251);
            case "upgrader" -> this.upgraderGrid(32306601);
            case "fragment" -> this.fragmentGrid(1576022711);
            case "repairkit" -> this.repairKitGrid(892112776);
            case "restorationcore" -> this.restorationCoreGrid(16318132);
            default -> this.restorationGrid(1369853006);
        };
    }

    public ItemStack[] grid(String var1, int var2) {
        Map var6 = this.customGrids;
        ItemStack[] var5 = (ItemStack[])var6.get(var1);
        ItemStack[] var12;
        if (var5 != null) {
            var12 = var5;
        } else {
            var12 = this.defaultGrid(var1, 299872362);
        }

        return cloneGrid(var12, 1881703196);
    }

    public boolean isEdited(String var1, int var2) {
        Map var5 = this.customGrids;
        return var5.containsKey(var1);
    }

    public void setCustomGrid(String var1, ItemStack[] var2, int var3) {
        ItemStack[] var7 = cloneGrid(var2, 1881703196);
        int var8 = 0;

        while (true) {
            int var23 = var7.length;
            if (var8 >= var23) {
                Map var19 = this.customGrids;
                var19.put(var1, var7);
                this.saveRecipes(618673565);
                this.rebuild(var1, 1609602649);
                return;
            }

            if (var7[var8] != null) {
                BlissCoreItems var15 = this.items;
                ItemStack var26 = var7[var8];
                if (var15.isUselessEnergy(var26, 1117390884)) {
                    ItemStack var32 = this.items.bottledEnergy(1532783840);
                    var7[var8] = var32;
                }
            }

            var8++;
        }
    }

    public void resetGrid(String var1, int var2) {
        Map var5 = this.customGrids;
        var5.remove(var1);
        this.saveRecipes(618673565);
        this.rebuild(var1, 1609602649);
    }

    private void rebuild(String var1, int var2) {
        NamespacedKey var8 = this.keyFor(var1, 323539593);
        Bukkit.removeRecipe(var8);
        if (this.recipeEnabled(var1, 916289261)) {
            ItemStack var22 = this.result(var1, 1187831354);
            ItemStack[] var24 = this.grid(var1, 1219052114);
            ShapedRecipe var9 = this.buildShaped(var8, var22, var24, 2083852260);
            if (var9 != null) {
                Bukkit.addRecipe(var9);
            }
        }
    }

    private boolean recipeEnabled(String var1, int var2) {
        GlobalState var5 = this.plugin.globalState(724248306);
        if (var5 == null) {
            return true;
        } else {
            return switch (var1) {
                case "trader" -> var5.traderRecipe(285314044);
                case "upgrader" -> var5.upgraderRecipe(459107113);
                case "restorationcore" -> var5.restorationCoreRecipe(1172908205);
                default -> true;
            };
        }
    }

    private ShapedRecipe buildShaped(NamespacedKey var1, ItemStack var2, ItemStack[] var3, int var4) {
        char var10 = 'A';
        String[] var11 = new String[3];
        LinkedHashMap var21 = new LinkedHashMap();
        LinkedHashMap var12 = var21;
        boolean var13 = false;

        for (int var14 = 0; var14 < 3; var14++) {
            StringBuilder var25 = new StringBuilder();
            StringBuilder var15 = var25;

            for (int var16 = 0; var16 < 3; var16++) {
                int var29 = var14 * 3;
                int var30 = var29 + var16;
                int var66 = var3.length;
                ItemStack var32;
                if (var30 < var66) {
                    int var68 = var14 * 3;
                    int var69 = var68 + var16;
                    var32 = var3[var69];
                } else {
                    var32 = null;
                }

                if (var32 != null) {
                    Material var35 = var32.getType();
                    Material var70 = Material.AIR;
                    if (var35 != var70) {
                        char var36 = var10++;
                        var15.append((char)var36);
                        Character var76 = (char)var36;
                        RecipeChoice var88 = this.choiceFor(var32, 437414727);
                        var12.put(var76, var88);
                        var13 = true;
                        continue;
                    }
                }

                var15.append(' ');
            }

            String var90 = var15.toString();
            var11[var14] = var90;
        }

        if (!var13) {
            return null;
        } else {
            ShapedRecipe var46 = new ShapedRecipe(var1, var2);
            ShapedRecipe var106 = var46;
            String[] var80 = new String[3];
            String var99 = var11[0];
            var80[0] = var99;
            String var101 = var11[1];
            var80[1] = var101;
            String var103 = var11[2];
            var80[2] = var103;
            var46.shape(var80);

            for (Entry<Character, RecipeChoice> var108 : ((Map<Character, RecipeChoice>)var12).entrySet()) {
                char var84 = var108.getKey();
                RecipeChoice var94 = var108.getValue();
                var106.setIngredient(var84, var94);
            }

            return var106;
        }
    }

    private RecipeChoice choiceFor(ItemStack var1, int var2) {
        BlissCoreItems var8 = this.items;
        if (!var8.isBottledEnergy(var1, 1793320558)) {
            BlissCoreItems var11 = this.items;
            if (!var11.isUselessEnergy(var1, 1117390884)) {
                ItemMeta var6 = var1.getItemMeta();
                BlissCoreItems var16 = this.items;
                if (!var16.isRestorationCore(var1, 314565070) && (var6 == null || !var6.hasCustomModelData())) {
                    Material var30 = var1.getType();
                    return new MaterialChoice(var30);
                }

                ItemStack var7 = var1.clone();
                var7.setAmount(1);
                return new ExactChoice(var7);
            }
        }

        Material var32 = Material.NAUTILUS_SHELL;
        return new MaterialChoice(var32);
    }

    private static ItemStack[] cloneGrid(ItemStack[] var0, int var1) {
        ItemStack[] var6 = new ItemStack[9];

        for (int var7 = 0; var7 < 9; var7++) {
            int var15 = var0.length;
            if (var7 >= var15) {
                break;
            }

            ItemStack var20;
            if (var0[var7] == null) {
                var20 = null;
            } else {
                var20 = var0[var7].clone();
            }

            var6[var7] = var20;
        }

        return var6;
    }

    public void loadRecipes(int var1) {
        if (this.recipeFile.exists()) {
            ConfigurationSection var8 = YamlConfiguration.loadConfiguration(this.recipeFile).getConfigurationSection("recipes");
            if (var8 != null) {
                for (String var10 : var8.getKeys(false)) {
                    ItemStack[] var11 = new ItemStack[9];
                    boolean var12 = false;

                    for (int var13 = 0; var13 < 9; var13++) {
                        String var55 = var10 + "." + var13;
                        ItemStack var54 = var8.getItemStack(var55);
                        var11[var13] = var54;
                        if (var11[var13] != null) {
                            var12 = true;
                        }
                    }

                    if (!var12) {
                        Logger var41 = this.plugin.getLogger();
                        String var50 = "recipes.yml: edit for '" + var10 + "' failed to deserialize — using the default recipe.";
                        var41.warning(var50);
                    } else {
                        Map var37 = this.customGrids;
                        var37.put(var10, var11);
                    }
                }
            }
        }
    }

    public void applyCustomRecipes(int var1) {
        for (String var5 : this.customGrids.keySet()) {
            this.rebuild(var5, 1609602649);
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    private void saveRecipes(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;

        for (Entry var7 : this.customGrids.entrySet()) {
            ItemStack[] var8 = (ItemStack[])var7.getValue();

            for (int var9 = 0; var9 < 9; var9++) {
                if (var8[var9] != null) {
                    String var44 = (String)var7.getKey();
                    String var45 = "recipes." + var44 + "." + var9;
                    ItemStack var52 = var8[var9];
                    var5.set(var45, var52);
                }
            }
        }

        try {
            if (!this.recipeFile.getParentFile().exists()) {
                this.recipeFile.getParentFile().mkdirs();
            }
            File var47 = this.recipeFile;
            AtomicYml.save(var5, var47, 1347473142);
        } catch (Exception var53) {
            Logger var39 = this.plugin.getLogger();
            String var50 = "Failed to save recipes.yml: " + var53.getMessage();
            var39.warning(var50);
        }
    }

    static {
        String[] var3 = new String[6];
        String var2 = "trader";
        var3[0] = var2;
        String var9 = "upgrader";
        var3[1] = var9;
        String var10 = "fragment";
        var3[2] = var10;
        String var11 = "repairkit";
        var3[3] = var11;
        String var12 = "restorationcore";
        var3[4] = var12;
        String var13 = "restoration";
        var3[5] = var13;
        EDITABLE = var3;
    }
}
