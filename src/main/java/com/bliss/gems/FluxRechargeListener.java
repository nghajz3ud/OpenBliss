package com.bliss.gems;

import com.bliss.core.GemType;
import com.bliss.core.GlobalState;
import com.bliss.goldgem.GoldGem;
import com.bliss.items.BlissCoreItems;
import com.bliss.villager.Text;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class FluxRechargeListener implements Listener {
    private final FluxGemListener flux;
    private static final Map<Material, Long> RECHARGE;
    private final BlissCoreItems items;

    public FluxRechargeListener(FluxGemListener var1, BlissCoreItems var2, int var3) {
        this.flux = var1;
        this.items = var2;
    }

    public static long wattsFor(ItemStack var0, BlissCoreItems var1, int var2) {
        if (var0 == null) {
            return 0L;
        } else {
            Map var7 = RECHARGE;
            Material var30 = var0.getType();
            Long var5 = (Long)var7.get(var30);
            if (var5 == null) {
                return 0L;
            } else if (var1 != null && var1.isRestorationCore(var0, 314565070)) {
                return 0L;
            } else {
                ItemMeta var6 = var0.getItemMeta();
                return var6 != null && var6.hasCustomModelData() ? 0L : var5;
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent var1) {
        Action var22 = var1.getAction();
        Action var3 = Action.RIGHT_CLICK_AIR;
        if (var22 != var3) {
            Action var24 = var1.getAction();
            Action var99 = Action.RIGHT_CLICK_BLOCK;
            if (var24 != var99) {
                return;
            }
        }

        if (GlobalState.forPlayer(var1.getPlayer(), 1433087673).wattDeposit(552206927)) {
            Player var30 = var1.getPlayer();
            GemType var100 = GemType.FLUX;
            if (!GoldGem.isWieldingSoulOff(var30, var100, 1989261555)) {
                return;
            }
        }

        EquipmentSlot var33 = var1.getHand();
        EquipmentSlot var101 = EquipmentSlot.HAND;
        if (var33 == var101) {
            Player var9 = var1.getPlayer();
            GemType var102 = GemType.FLUX;
            boolean var10 = GoldGem.isWieldingSoulOff(var9, var102, 1989261555);
            ItemStack var11 = var9.getInventory().getItemInOffHand();
            int var42 = BlissCoreItems.gemIdOf(var11, 382441079);
            int var104 = GemType.FLUX.id();
            if (var42 == var104 || var10) {
                if (BlissCoreItems.tierOf(var11, 1633828214) == 2 || var10) {
                    ItemStack var12 = var9.getInventory().getItemInMainHand();
                    BlissCoreItems var107 = this.items;
                    long var13 = wattsFor(var12, var107, 329075510);
                    if (var13 > 0L) {
                        UUID var15 = var9.getUniqueId();
                        Map<UUID, Long> var60 = this.flux.fluxCharge;
                        Long var127 = 0L;
                        long var16 = var60.getOrDefault(var15, var127);
                        if (var16 < 2000000L) {
                            long var132 = 2000000L - var16;
                            long var18 = Math.min(var13, var132);
                            Map var74 = this.flux.fluxCharge;
                            Long var138 = var16 + var18;
                            var74.put(var15, var138);
                            Map var78 = this.flux.fluxChargeHidden;
                            Long var143 = var16 + var18;
                            var78.put(var15, var143);
                            FluxGemListener var81 = this.flux;
                            var81.recomputeChargePercent(var15, 1834179235);
                            int var114 = var12.getAmount() - 1;
                            var12.setAmount(var114);
                            long var20 = var16 + var18;
                            FluxGemListener var147 = this.flux;
                            String var150 = formatPercent(var147.chargePercent(var15, 1875673459), 554008468);
                            Component var118 = Text.legacy("&f\ud83d\udd2e You now have " + var20 + " watts, up to " + var150 + "% charge.", 373192400);
                            var9.sendMessage(var118);
                            Material var89 = var12.getType();
                            Material var119 = Material.DIAMOND_BLOCK;
                            if (var89 != var119) {
                                Material var91 = var12.getType();
                                Material var120 = Material.NETHERITE_BLOCK;
                                if (var91 != var120) {
                                    Material var93 = var12.getType();
                                    Material var121 = Material.COPPER_BLOCK;
                                    if (var93 != var121) {
                                        Material var95 = var12.getType();
                                        Material var122 = Material.IRON_BLOCK;
                                        if (var95 != var122) {
                                            Material var97 = var12.getType();
                                            Material var123 = Material.WITHER_SKELETON_SKULL;
                                            if (var97 != var123) {
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
        }
    }

    private static String formatPercent(double var0, int var2) {
        double var17 = Math.floor(var0);
        String var15;
        if (var0 == var17) {
            String var14 = "%.1f";
            Object[] var16 = new Object[1];
            Double var19 = var0;
            var16[0] = var19;
            var15 = String.format(var14, var16);
        } else {
            var15 = String.valueOf(var0);
        }

        return var15;
    }

    static {
        EnumMap var0 = new EnumMap(Material.class);
        RECHARGE = var0;
        Map var2 = RECHARGE;
        Material var22 = Material.DIAMOND;
        Long var34 = 5202L;
        var2.put(var22, var34);
        Map var4 = RECHARGE;
        Material var23 = Material.DIAMOND_BLOCK;
        Long var37 = 46819L;
        var4.put(var23, var37);
        Map var6 = RECHARGE;
        Material var24 = Material.NETHERITE_INGOT;
        Long var40 = 46819L;
        var6.put(var24, var40);
        Map var8 = RECHARGE;
        Material var25 = Material.NETHERITE_BLOCK;
        Long var43 = 421378L;
        var8.put(var25, var43);
        Map var10 = RECHARGE;
        Material var26 = Material.COPPER_BLOCK;
        Long var46 = 17352L;
        var10.put(var26, var46);
        Map var12 = RECHARGE;
        Material var27 = Material.COPPER_INGOT;
        Long var49 = 1928L;
        var12.put(var27, var49);
        Map var14 = RECHARGE;
        Material var28 = Material.IRON_INGOT;
        Long var52 = 106L;
        var14.put(var28, var52);
        Map var16 = RECHARGE;
        Material var29 = Material.IRON_BLOCK;
        Long var55 = 1067L;
        var16.put(var29, var55);
        Map var18 = RECHARGE;
        Material var30 = Material.WITHER_SKELETON_SKULL;
        Long var58 = 112047L;
        var18.put(var30, var58);
        Map var20 = RECHARGE;
        Material var31 = Material.NETHER_STAR;
        Long var61 = 336141L;
        var20.put(var31, var61);
    }
}
