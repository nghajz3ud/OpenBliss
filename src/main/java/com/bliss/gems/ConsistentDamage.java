package com.bliss.gems;

import com.bliss.core.EnergyScale;
import com.bliss.core.GemType;
import com.bliss.core.PlayerData;
import com.bliss.core.PlayerDataStore;
import com.bliss.goldgem.GoldGem;
import com.bliss.mythics.MythicItems;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.damage.DamageSource.Builder;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public final class ConsistentDamage {
    private static PlayerDataStore PLAYERS;
    private static boolean TRUE_DAMAGE;
    private static UUID abilityVictim;

    private ConsistentDamage() {
    }

    public static void init(PlayerDataStore var0, int var1) {
        PLAYERS = var0;
    }

    public static void setTrueDamage(boolean var0, int var1) {
        TRUE_DAMAGE = var0;
    }

    public static boolean trueDamage(int var0) {
        return TRUE_DAMAGE;
    }

    public static boolean referenceFor(LivingEntity var0, int var1) {
        boolean var5;
        if (TRUE_DAMAGE && var0 instanceof Player) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    private static double energyMult(Player var0, int var1) {
        if (var0 != null && PLAYERS != null) {
            PlayerDataStore var6 = PLAYERS;
            UUID var21 = var0.getUniqueId();
            PlayerData var4 = var6.get(var21, 1264010596);
            GemType var9 = var4.gem(1413186337);
            GemType var22 = GemType.NONE;
            return var9 == var22 && !holdsGoldGem(var0, 764412400) && MythicItems.hasMythicGemInInventory(var0, 2063095181)
                ? 1.0
                : EnergyScale.power(var4, 1425120665);
        } else {
            return 1.0;
        }
    }

    private static boolean holdsGoldGem(Player var0, int var1) {
        ItemStack[] var4 = var0.getInventory().getContents();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; var6++) {
            if (GoldGem.isGoldGem(var4[var6])) {
                return true;
            }
        }

        return false;
    }

    public static void deal(Player var0, LivingEntity var1, double var2, int var4) {
        if (!(var2 <= 0.0) && !var1.isDead()) {
            double var19 = energyMult(var0, 1843254529);
            double var23 = var2 * var19;
            deliver(var0, var1, var23, 494825881);
        }
    }

    private static void deliver(Player var0, LivingEntity var1, double var2, int var4) {
        if (!(var2 <= 0.0) && !var1.isDead()) {
            var1.setNoDamageTicks(0);
            deliverKeepingIFrames(var0, var1, var2, 788384429);
        }
    }

    private static void deliverKeepingIFrames(Player var0, LivingEntity var1, double var2, int var4) {
        if (!(var2 <= 0.0) && !var1.isDead()) {
            Builder var11 = DamageSource.builder(DamageType.SONIC_BOOM);
            if (var0 != null) {
                Builder var19 = var11.withCausingEntity(var0);
                var19.withDirectEntity(var0);
            }

            DamageSource var25 = var11.build();
            var1.damage(var2, var25);
        }
    }

    public static void dealArmorAware(Player var0, LivingEntity var1, double var2) {
        double var11 = protectionRemaining(var1, 1085519591);
        double var9 = var2 * var11;
        deal(var0, var1, var9, 1092002170);
    }

    public static void dealFallAware(Player var0, LivingEntity var1, double var2, int var4) {
        double var12 = fallProtectionRemaining(var1, 2048322149);
        double var10 = var2 * var12;
        deal(var0, var1, var10, 1092002170);
    }

    public static double fallProtectionRemaining(LivingEntity var0, int var1) {
        if (referenceFor(var0, 1248764491)) {
            return 1.0;
        } else if (var0 instanceof Player var9) {
            int var10 = 0;

            for (ItemStack var14 : var9.getInventory().getArmorContents()) {
                if (var14 != null) {
                    Enchantment var54 = Enchantment.PROTECTION;
                    int var48 = var14.getEnchantmentLevel(var54);
                    int var41 = var10 + var48;
                    Enchantment var55 = Enchantment.FEATHER_FALLING;
                    int var51 = var14.getEnchantmentLevel(var55) * 3;
                    var10 = var41 + var51;
                }
            }

            double var59 = (double)Math.min(20, var10) * 0.04;
            double var52 = 1.0 - var59;
            return Math.max(0.2, var52);
        } else {
            return 1.0;
        }
    }

    public static double protectionRemaining(LivingEntity var0, int var1) {
        if (referenceFor(var0, 1248764491)) {
            return 1.0;
        } else if (var0 instanceof Player var9) {
            int var10 = 0;

            for (ItemStack var14 : var9.getInventory().getArmorContents()) {
                if (var14 != null) {
                    Enchantment var49 = Enchantment.PROTECTION;
                    int var46 = var14.getEnchantmentLevel(var49);
                    var10 += var46;
                }
            }

            double var52 = (double)Math.min(20, var10) * 0.04;
            double var47 = 1.0 - var52;
            return Math.max(0.2, var47);
        } else {
            return 1.0;
        }
    }

    public static void dealFullArmorAware(Player var0, LivingEntity var1, double var2, int var4) {
        dealFullArmorAwareCapped(var0, var1, var2, Double.MAX_VALUE, 1350238938);
    }

    public static void dealFullArmorAwareCapped(Player var0, LivingEntity var1, double var2, double var4, int var6) {
        if (!(var2 <= 0.0) && !var1.isDead()) {
            double var69 = energyMult(var0, 1843254529);
            double var104 = var2 * var69;
            if (!referenceFor(var1, 1248764491) && var1 instanceof Player var22) {
                Attribute var9 = Attribute.ARMOR;
                double var23 = attrValue(var22, var9, 516121975);
                Attribute var66 = Attribute.ARMOR_TOUGHNESS;
                double var25 = attrValue(var22, var66, 516121975);
                double var73 = var23 / 5.0;
                double var102 = var25 / 4.0;
                double var100 = 2.0 + var102;
                double var96 = var104 / var100;
                double var90 = var23 - var96;
                double var75 = Math.max(var73, var90);
                double var27 = Math.min(20.0, var75);
                double var94 = var27 / 25.0;
                double var79 = 1.0 - var94;
                double var56 = var104 * var79;
                double var82 = protectionRemaining(var22, 1085519591);
                var104 = var56 * var82;
            }

            if (var104 > var4) {
                var104 = var4;
            }

            deliver(var0, var1, var104, 494825881);
        }
    }

    public static double afterArmor(LivingEntity var0, double var1, int var3) {
        if (var0 instanceof Player var18) {
            Attribute var5 = Attribute.ARMOR;
            double var19 = attrValue(var18, var5, 516121975);
            Attribute var46 = Attribute.ARMOR_TOUGHNESS;
            double var21 = attrValue(var18, var46, 516121975);
            double var47 = var19 / 5.0;
            double var72 = var21 / 4.0;
            double var70 = 2.0 + var72;
            double var66 = var1 / var70;
            double var60 = var19 - var66;
            double var49 = Math.max(var47, var60);
            double var23 = Math.min(20.0, var49);
            double var64 = var23 / 25.0;
            double var53 = 1.0 - var64;
            double var42 = var1 * var53;
            double var56 = protectionRemaining(var18, 1085519591);
            return var42 * var56;
        } else {
            return var1;
        }
    }

    public static double capAfterArmor(LivingEntity var0, double var1, double var3, int var5) {
        if (!(var3 <= 0.0) && !(var1 <= 0.0)) {
            double var22 = afterArmor(var0, var1, 2026346099);
            if (!(var22 <= var3)) {
                for (int var14 = 0; var14 < 8; var14++) {
                    double var15 = afterArmor(var0, var1, 2026346099);
                    double var34 = Math.abs(var15 - var3);
                    double var68 = var3 * 0.005;
                    if (var34 <= var68) {
                        break;
                    }

                    double var80 = Math.pow(var3 / var15, 0.6);
                    var1 *= var80;
                }

                double var88 = afterArmor(var0, var1, 2026346099);
                if (var88 > var3) {
                    double var74 = var3 / var88;
                    var1 *= var74;
                }

                return var1;
            }
        }

        return var1;
    }

    public static void dealVanillaOrTrue(Player var0, LivingEntity var1, double var2, double var4, int var6) {
        if (!(var2 <= 0.0) && !var1.isDead()) {
            double var50 = energyMult(var0, 1843254529);
            double var60 = var2 * var50;
            if (!TRUE_DAMAGE) {
                UUID var15 = abilityVictim;
                abilityVictim = var1.getUniqueId();

                try {
                    double var45 = capAfterArmor(var1, var60, var4, 59041087);
                    var1.damage(var45, var0);
                } finally {
                    abilityVictim = var15;
                }
            } else if (!shieldBlocks(var0, var1, 1682749615)) {
                double var62 = var60;
                if (var4 > 0.0) {
                    var62 = Math.min(var60, var4);
                }

                deliverKeepingIFrames(var0, var1, var62, 788384429);
            }
        }
    }

    public static boolean deliveringAbilityTo(Entity var0, int var1) {
        if (abilityVictim != null && var0 != null) {
            UUID var5 = abilityVictim;
            UUID var8 = var0.getUniqueId();
            if (var5.equals(var8)) {
                return true;
            }
        }

        return false;
    }

    private static boolean shieldBlocks(Player var0, LivingEntity var1, int var2) {
        if (var0 != null && var1 instanceof Player var13 && var13.isBlocking()) {
            Vector var24 = var0.getLocation().toVector();
            Vector var52 = var13.getLocation().toVector();
            Vector var14 = var24.subtract(var52).setY(0);
            if (var14.lengthSquared() < 1.0E-12) {
                return false;
            }

            Vector var15 = var13.getEyeLocation().getDirection().setY(0);
            if (var15.lengthSquared() < 1.0E-12) {
                return false;
            }

            Vector var40 = var15.normalize();
            Vector var56 = var14.normalize();
            if (var40.dot(var56) <= 0.0) {
                return false;
            }

            World var46 = var13.getWorld();
            Location var58 = var13.getLocation();
            Sound var63 = Sound.ITEM_SHIELD_BLOCK;
            SoundCategory var7 = SoundCategory.PLAYERS;
            float var66 = (float)Math.random() * 0.4F;
            float var64 = 0.8F + var66;
            var46.playSound(var58, var63, var7, 1.0F, var64);
            return true;
        }

        return false;
    }

    private static double attrValue(LivingEntity var0, Attribute var1, int var2) {
        AttributeInstance var5 = var0.getAttribute(var1);
        double var8;
        if (var5 != null) {
            var8 = var5.getValue();
        } else {
            var8 = 0.0;
        }

        return var8;
    }
}
