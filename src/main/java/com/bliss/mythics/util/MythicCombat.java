package com.bliss.mythics.util;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public final class MythicCombat {
    private static final EquipmentSlot[] ARMOR;
    private static final Random RNG;
    private static final int ARMOR_DMG_CAP = 6;
    private static final double ARMOR_DMG_FACTOR = 0.25;
    private static final int ALT_SCAN = 48;

    private MythicCombat() {
    }

    public static void damageArmor(LivingEntity var0, double var1, int var3) {
        damageArmor(var0, var1, 1.0, 256520978);
    }

    public static void damageArmor(LivingEntity var0, double var1, double var3, int var5) {
        double var55 = var1 * 0.25;
        long var59 = Math.round(var55 * var3);
        long var50 = Math.max(1L, var59);
        int var15 = (int)Math.min(6L, var50);
        EntityEquipment var16 = var0.getEquipment();
        if (var16 != null) {
            for (EquipmentSlot var20 : ARMOR) {
                ItemStack var21 = var16.getItem(var20);
                if (var21 != null && !var21.getType().isAir()) {
                    if (applyWear(var21, var15, true, 420281923)) {
                        var16.setItem(var20, null);
                    } else {
                        var16.setItem(var20, var21);
                    }
                }
            }
        }
    }

    public static boolean applyWear(ItemStack var0, int var1, boolean var2, int var3) {
        if (var0 != null && var1 > 0) {
            if (!(var0.getItemMeta() instanceof Damageable var11)) {
                return false;
            } else if (var11.isUnbreakable()) {
                return false;
            } else {
                int var64;
                if (var11.hasMaxDamage()) {
                    var64 = var11.getMaxDamage();
                } else {
                    var64 = var0.getType().getMaxDurability();
                }

                short var87 = (short)var64;
                if (var87 <= 0) {
                    return false;
                } else {
                    Enchantment var5 = Enchantment.UNBREAKING;
                    int var13 = var0.getEnchantmentLevel(var5);
                    int var14 = var1;
                    if (var13 > 0) {
                        double var40;
                        if (var2) {
                            double var83 = (double)var13 + 1.0;
                            double var72 = 0.4 / var83;
                            var40 = 0.6 + var72;
                        } else {
                            double var79 = (double)var13 + 1.0;
                            var40 = 1.0 / var79;
                        }

                        double var15 = var40;
                        var14 = 0;

                        for (int var17 = 0; var17 < var1; var17++) {
                            double var57 = RNG.nextDouble();
                            if (var57 < var15) {
                                var14++;
                            }
                        }
                    }

                    if (var14 <= 0) {
                        return false;
                    } else {
                        int var47 = var11.getDamage();
                        int var88 = var47 + var14;
                        if (var88 >= var87) {
                            return true;
                        } else {
                            var11.setDamage(var88);
                            var0.setItemMeta(var11);
                            return false;
                        }
                    }
                }
            }
        } else {
            return false;
        }
    }

    public static double heightAboveGround(LivingEntity var0) {
        Location var6 = var0.getLocation();
        if (var6.getWorld() == null) {
            return 0.0;
        } else {
            for (int var8 = 1; var8 <= 48; var8++) {
                Location var18 = var6.clone();
                double var32 = (double)var8;
                if (var18.subtract(0.0, var32, 0.0).getBlock().getType().isSolid()) {
                    return (double)(var8 - 1);
                }
            }

            return 48.0;
        }
    }

    static {
        EquipmentSlot[] var3 = new EquipmentSlot[4];
        EquipmentSlot var2 = EquipmentSlot.HEAD;
        var3[0] = var2;
        EquipmentSlot var8 = EquipmentSlot.CHEST;
        var3[1] = var8;
        EquipmentSlot var9 = EquipmentSlot.LEGS;
        var3[2] = var9;
        EquipmentSlot var10 = EquipmentSlot.FEET;
        var3[3] = var10;
        ARMOR = var3;
        Random var4 = new Random();
        RNG = var4;
    }
}
