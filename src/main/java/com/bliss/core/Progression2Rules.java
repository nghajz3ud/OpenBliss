package com.bliss.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public final class Progression2Rules {
    private static final Map<Enchantment, Integer> CAPS;
    private static final List<Enchantment> BANNED;
    public static final PotionType ALLOWED_POTION = PotionType.INVISIBILITY;
    public static final Map<Material, Integer> CARRY_LIMITS;

    private Progression2Rules() {
    }

    private static boolean identityItem(Material var0, int var1) {
        Material var3 = Material.OBSIDIAN;
        if (var0 != var3) {
            Material var8 = Material.PRISMARINE_SHARD;
            if (var0 != var8) {
                Material var9 = Material.AMETHYST_SHARD;
                if (var0 != var9) {
                    Material var10 = Material.PRISMARINE_CRYSTALS;
                    if (var0 != var10) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean bannedEnchant(Enchantment var0, int var1) {
        List var2 = BANNED;
        return var2.contains(var0);
    }

    public static int cap(Enchantment var0, int var1) {
        Map var2 = CAPS;
        Integer var4 = (Integer)var2.get(var0);
        int var9;
        if (var4 == null) {
            var9 = -1;
        } else {
            var9 = var4;
        }

        return var9;
    }

    public static void applyTo(ItemStack var0) {
        applyTo(var0, null, 736519659);
    }

    public static void applyTo(ItemStack var0, UUID var1, int var2) {
        if (var0 != null && !identityItem(var0.getType(), 758910428)) {
            boolean var57;
            if (var1 != null && GemAutoEnchants.allStages(var1, 1630297028)) {
                var57 = true;
            } else {
                var57 = false;
            }

            boolean var6 = var57;

            for (Enchantment var8 : BANNED) {
                int var9 = var0.getEnchantmentLevel(var8);
                if (var9 > 0) {
                    int var35;
                    if (var6) {
                        var35 = GemAutoEnchants.granted(var1, var8, 837616120);
                    } else {
                        var35 = 0;
                    }

                    byte var10 = (byte)var35;
                    if (var10 > 0) {
                        if (var9 > var10) {
                            var0.addUnsafeEnchantment(var8, var10);
                        }
                    } else {
                        var0.removeEnchantment(var8);
                    }
                }
            }

            for (Entry var75 : CAPS.entrySet()) {
                Enchantment var64 = (Enchantment)var75.getKey();
                int var76 = var0.getEnchantmentLevel(var64);
                int var77 = (Integer)var75.getValue();
                int var56;
                if (var6) {
                    Enchantment var67 = (Enchantment)var75.getKey();
                    var56 = GemAutoEnchants.granted(var1, var67, 837616120);
                } else {
                    var56 = 0;
                }

                byte var11 = (byte)var56;
                if (var11 > var77) {
                    var77 = var11;
                }

                if (var76 > var77) {
                    Enchantment var72 = (Enchantment)var75.getKey();
                    var0.addUnsafeEnchantment(var72, var77);
                }
            }
        }
    }

    public static boolean potionBanned(ItemStack var0, int var1) {
        if (var0 == null) {
            return false;
        } else {
            Material var4 = var0.getType();
            Material var3 = Material.POTION;
            if (var4 != var3) {
                Material var42 = Material.SPLASH_POTION;
                if (var4 != var42) {
                    Material var43 = Material.LINGERING_POTION;
                    if (var4 != var43) {
                        return false;
                    }
                }
            }

            if (!(var0.getItemMeta() instanceof PotionMeta var5)) {
                return false;
            } else {
                PotionType var44 = var5.getBasePotionType();
                boolean var41;
                if ((var44 == null || var44.getPotionEffects().isEmpty()) && var5.getCustomEffects().isEmpty()) {
                    var41 = false;
                } else {
                    var41 = true;
                }

                if (!var41) {
                    return false;
                } else if (!var5.getCustomEffects().isEmpty()) {
                    return true;
                } else {
                    boolean var39;
                    if (var44 != null && isInvisibility(var44, 1518613766)) {
                        var39 = false;
                    } else {
                        var39 = true;
                    }

                    return var39;
                }
            }
        }
    }

    private static boolean isInvisibility(PotionType var0, int var1) {
        PotionType var3 = ALLOWED_POTION;
        return var0 == var3 ? true : var0.getKey().getKey().endsWith("invisibility");
    }

    public static boolean saturationStew(ItemStack var0, int var1) {
        if (var0 != null) {
            Material var8 = var0.getType();
            Material var3 = Material.SUSPICIOUS_STEW;
            if (var8 == var3) {
                if (var0.getItemMeta() instanceof SuspiciousStewMeta var4) {
                    Iterator var30 = var4.getCustomEffects().iterator();

                    while (var30.hasNext()) {
                        PotionEffectType var24 = ((PotionEffect)var30.next()).getType();
                        PotionEffectType var29 = PotionEffectType.SATURATION;
                        if (var24 == var29) {
                            return true;
                        }
                    }

                    return false;
                }

                return false;
            }
        }

        return false;
    }

    static {
        LinkedHashMap var0 = new LinkedHashMap();
        CAPS = var0;
        Map var4 = CAPS;
        Enchantment var26 = Enchantment.PROTECTION;
        Integer var35 = 3;
        var4.put(var26, var35);
        Map var6 = CAPS;
        Enchantment var27 = Enchantment.SHARPNESS;
        Integer var37 = 5;
        var6.put(var27, var37);
        Map var8 = CAPS;
        Enchantment var28 = Enchantment.POWER;
        Integer var39 = 3;
        var8.put(var28, var39);
        Map var10 = CAPS;
        Enchantment var29 = Enchantment.QUICK_CHARGE;
        Integer var41 = 2;
        var10.put(var29, var41);
        Enchantment var12 = Enchantment.INFINITY;
        Enchantment var30 = Enchantment.FIRE_ASPECT;
        Enchantment var42 = Enchantment.FLAME;
        Enchantment var2 = Enchantment.PUNCH;
        BANNED = List.of(var12, var30, var42, var2);
        LinkedHashMap var15 = new LinkedHashMap();
        Material var31 = Material.GOLDEN_APPLE;
        Integer var44 = 24;
        var15.put(var31, var44);
        Material var32 = Material.ENCHANTED_GOLDEN_APPLE;
        Integer var46 = 24;
        var15.put(var32, var46);
        Material var33 = Material.ARROW;
        Integer var48 = 32;
        var15.put(var33, var48);
        Material var34 = Material.COBWEB;
        Integer var50 = 32;
        var15.put(var34, var50);
        CARRY_LIMITS = Collections.unmodifiableMap(var15);
    }
}
