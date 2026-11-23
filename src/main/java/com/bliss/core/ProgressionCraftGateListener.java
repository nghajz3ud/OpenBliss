package com.bliss.core;

import com.bliss.items.BlissCoreItems;
import com.bliss.items.BlissRecipes;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public final class ProgressionCraftGateListener implements Listener {
    private final BlissCoreItems items;
    private final GlobalState globalState;
    private final BlissRecipes recipes;

    public ProgressionCraftGateListener(BlissCoreItems var1, GlobalState var2, BlissRecipes var3, int var4) {
        this.items = var1;
        this.globalState = var2;
        this.recipes = var3;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent var1) {
        ItemStack var5 = var1.getInventory().getResult();
        if (var5 != null) {
            BlissCoreItems var17 = this.items;
            if (var17.isUpgrader(var5, 280446299)) {
                int var6 = 0;
                BlissRecipes var21 = this.recipes;
                String var101 = "upgrader";

                for (ItemStack var127 : var21.grid(var101, 1219052114)) {
                    if (var127 != null) {
                        BlissCoreItems var96 = this.items;
                        if (!var96.isBottledEnergy(var127, 1793320558)) {
                            BlissCoreItems var99 = this.items;
                            if (!var99.isUselessEnergy(var127, 1117390884)) {
                                continue;
                            }
                        }

                        var6++;
                    }
                }

                ItemStack[] var124 = var1.getInventory().getMatrix();
                int var125 = 0;
                if (var124 != null) {
                    for (ItemStack var12 : var124) {
                        if (var12 != null) {
                            BlissCoreItems var41 = this.items;
                            if (var41.isBottledEnergy(var12, 1793320558) && !this.expiredBottle(var12, 955941149)) {
                                var125++;
                            }
                        }
                    }
                }

                if (var125 < var6) {
                    CraftingInventory var47 = var1.getInventory();
                    Material var4 = Material.AIR;
                    ItemStack var108 = new ItemStack(var4);
                    var47.setResult(var108);
                    return;
                }
            }

            if (GlobalState.forWorld(var1.getView().getPlayer().getWorld(), 728926677).stage(1483650410) < 3) {
                BlissCoreItems var55 = this.items;
                if (!var55.isTrader(var5, 904484797) || !this.globalState.traderRecipe(285314044)) {
                    BlissCoreItems var61 = this.items;
                    if (!var61.isUpgrader(var5, 280446299) || !this.globalState.upgraderRecipe(459107113)) {
                        BlissCoreItems var67 = this.items;
                        if (!var67.isRestorationCore(var5, 314565070) || !this.globalState.restorationCoreRecipe(1172908205)) {
                            BlissCoreItems var73 = this.items;
                            if (!var73.isTrader(var5, 904484797)) {
                                BlissCoreItems var76 = this.items;
                                if (!var76.isUpgrader(var5, 280446299)) {
                                    BlissCoreItems var79 = this.items;
                                    if (!var79.isRestoration(var5, 300938899)) {
                                        BlissCoreItems var82 = this.items;
                                        if (!var82.isRepairKit(var5, 58420938)) {
                                            BlissCoreItems var85 = this.items;
                                            if (!var85.isFragment(var5, 1737293481)) {
                                                BlissCoreItems var88 = this.items;
                                                if (!var88.isRestorationCore(var5, 314565070)) {
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            CraftingInventory var91 = var1.getInventory();
                            Material var123 = Material.AIR;
                            ItemStack var119 = new ItemStack(var123);
                            var91.setResult(var119);
                        }
                    }
                }
            }
        }
    }

    private boolean expiredBottle(ItemStack var1, int var2) {
        if (!this.globalState.bottledEnergyExpiry(261106370)) {
            return false;
        } else {
            Long var6 = BlissCoreItems.energyExpiryOf(var1, 374126995);
            if (var6 != null) {
                long var13 = var6;
                long var4 = System.currentTimeMillis();
                if (var13 <= var4) {
                    return true;
                }
            }

            return false;
        }
    }

    @EventHandler(
        ignoreCancelled = true
    )
    public void onCrafterCraft(CrafterCraftEvent var1) {
        ItemStack var4 = var1.getResult();
        BlissCoreItems var7 = this.items;
        if (!var7.isTrader(var4, 904484797)) {
            BlissCoreItems var10 = this.items;
            if (!var10.isUpgrader(var4, 280446299)) {
                BlissCoreItems var13 = this.items;
                if (!var13.isRestoration(var4, 300938899)) {
                    BlissCoreItems var16 = this.items;
                    if (!var16.isRepairKit(var4, 58420938)) {
                        BlissCoreItems var19 = this.items;
                        if (!var19.isFragment(var4, 1737293481)) {
                            BlissCoreItems var22 = this.items;
                            if (!var22.isRestorationCore(var4, 314565070)) {
                                return;
                            }
                        }
                    }
                }
            }
        }

        var1.setCancelled(true);
    }
}
