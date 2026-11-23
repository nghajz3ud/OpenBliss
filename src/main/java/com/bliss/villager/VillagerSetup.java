package com.bliss.villager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public final class VillagerSetup {
    public static final String EVENT_TAG = "BlissEventVillager";

    private VillagerSetup() {
    }

    public static Villager spawn(int var0, Location var1, EventState var2, int var3) {
        World var9 = var1.getWorld();
        Consumer<Villager> var26 = var1x -> {
            var1x.addScoreboardTag("BlissEventVillager");
            Component var14 = Cosmetics.villagerName(var0, 527184414);
            var1x.customName(var14);
            var1x.setCustomNameVisible(true);
            var1x.setSilent(true);
            var1x.setPersistent(true);
            var1x.setVillagerLevel(5);
            Type var19 = Type.PLAINS;
            var1x.setVillagerType(var19);
            Profession var20 = Profession.TOOLSMITH;
            var1x.setProfession(var20);
            List<MerchantRecipe> var22x = buildTrades(var0, 25042708);
            var1x.setRecipes(var22x);
        };
        Villager var8 = (Villager)var9.spawn(var1, Villager.class, var26);
        var2.setVillager(var0, var8, 607405227);
        var2.setVillagerLoc(var0, var1, 1133457113);
        var2.setVillagerAlive(var0, true, 1445271449);
        UUID var22 = var8.getUniqueId();
        var2.unlockSoulDrop(var22, 385322225);
        var2.save(1112893273);
        return var8;
    }

    public static void applyTrades(int var0, Villager var1, int var2) {
        List var5 = buildTrades(var0, 25042708);
        var1.setRecipes(var5);
    }

    private static List<MerchantRecipe> buildTrades(int var0, int var1) {
        ArrayList var2 = new ArrayList();
        Material var3 = Material.ENCHANTED_GOLDEN_APPLE;
        ItemStack var29 = new ItemStack(var3);
        Material var4 = Material.NETHERITE_INGOT;
        ItemStack var51 = new ItemStack(var4, 1);
        MerchantRecipe var30 = simple(var29, var51, 9999999, 156835881);
        var2.add(var30);
        ItemStack var32 = splashPotion(PotionType.STRONG_SWIFTNESS, 557242621);
        Material var78 = Material.EMERALD;
        ItemStack var52 = new ItemStack(var78, 8);
        MerchantRecipe var33 = simple(var32, var52, 9999999, 156835881);
        var2.add(var33);
        ItemStack var35 = splashPotion(PotionType.STRONG_STRENGTH, 557242621);
        Material var79 = Material.EMERALD;
        ItemStack var53 = new ItemStack(var79, 8);
        MerchantRecipe var36 = simple(var35, var53, 9999999, 156835881);
        var2.add(var36);
        Material var68 = Material.COBWEB;
        ItemStack var37 = new ItemStack(var68, 64);
        Material var81 = Material.EMERALD;
        ItemStack var54 = new ItemStack(var81, 4);
        MerchantRecipe var38 = simple(var37, var54, 9999999, 156835881);
        var2.add(var38);
        Material var70 = Material.BREEZE_ROD;
        ItemStack var39 = new ItemStack(var70);
        Material var82 = Material.EMERALD;
        ItemStack var55 = new ItemStack(var82, 4);
        MerchantRecipe var40 = simple(var39, var55, 9999999, 156835881);
        var2.add(var40);
        Material var72 = Material.EXPERIENCE_BOTTLE;
        ItemStack var41 = new ItemStack(var72, 64);
        Material var84 = Material.EMERALD;
        ItemStack var56 = new ItemStack(var84, 20);
        MerchantRecipe var42 = simple(var41, var56, 9999999, 156835881);
        var2.add(var42);
        Material var74 = Material.MACE;
        ItemStack var43 = new ItemStack(var74);
        ItemStack var58 = energyToken(8, 1248994343);
        MerchantRecipe var44 = simple(var43, var58, 1, 156835881);
        var2.add(var44);
        ItemStack[] var7 = exclusivesFor(var0, 110950434);
        ItemStack var46 = var7[0];
        ItemStack var61 = energyToken(4, 1248994343);
        MerchantRecipe var47 = simple(var46, var61, 1, 156835881);
        var2.add(var47);
        ItemStack var49 = var7[1];
        ItemStack var64 = energyToken(4, 1248994343);
        MerchantRecipe var50 = simple(var49, var64, 1, 156835881);
        var2.add(var50);
        return var2;
    }

    private static ItemStack energyToken(int var0, int var1) {
        Material var3 = Material.NAUTILUS_SHELL;
        ItemStack var2 = new ItemStack(var3, var0);
        ItemMeta var6 = var2.getItemMeta();
        Component var15 = Text.legacy("<#90EE90>&lᴇɴᴇʀɢʏ &r<#90EE90>ᴛᴏᴋᴇɴ", 373192400);
        TextDecoration var20 = TextDecoration.ITALIC;
        Component var16 = var15.decoration(var20, false);
        var6.displayName(var16);
        Integer var18 = 350;
        var6.setCustomModelData(var18);
        var2.setItemMeta(var6);
        return var2;
    }

    private static MerchantRecipe simple(ItemStack var0, ItemStack var1, int var2, int var3) {
        MerchantRecipe var4 = new MerchantRecipe(var0, 0, var2, false, 0, 0.0F);
        var4.addIngredient(var1);
        return var4;
    }

    private static ItemStack splashPotion(PotionType var0, int var1) {
        Material var3 = Material.SPLASH_POTION;
        ItemStack var2 = new ItemStack(var3);
        PotionMeta var5 = (PotionMeta)var2.getItemMeta();
        var5.setBasePotionType(var0);
        var2.setItemMeta(var5);
        return var2;
    }

    private static ItemStack[] exclusivesFor(int var0, int var1) {
        ItemStack[] var8;
        switch (var0) {
            case 1:
                var8 = new ItemStack[2];
                Material var4 = Material.NETHERITE_HELMET;
                String var5 = "ɴᴇᴛʜᴇʀɪᴛᴇ ʜᴇʟᴍ";
                ItemStack var15 = namedNetherite(var4, var5, 1484712041);
                var8[0] = var15;
                Material var16 = Material.NETHERITE_LEGGINGS;
                String var26 = "ɴᴇᴛʜᴇʀɪᴛᴇ ʟᴇɢɢɪɴɢꜱ";
                ItemStack var17 = namedNetherite(var16, var26, 1484712041);
                var8[1] = var17;
                break;
            case 2:
                var8 = new ItemStack[2];
                Material var22 = Material.NETHERITE_CHESTPLATE;
                String var29 = "ɴᴇᴛʜᴇʀɪᴛᴇ ᴄʜᴇꜱᴛᴘʟᴀᴛᴇ";
                ItemStack var23 = namedNetherite(var22, var29, 1484712041);
                var8[0] = var23;
                Material var24 = Material.NETHERITE_BOOTS;
                String var30 = "ɴᴇᴛʜᴇʀɪᴛᴇ ʙᴏᴏᴛꜱ";
                ItemStack var25 = namedNetherite(var24, var30, 1484712041);
                var8[1] = var25;
                break;
            default:
                var8 = new ItemStack[2];
                Material var18 = Material.NETHERITE_SWORD;
                String var27 = "ɴᴇᴛʜᴇʀɪᴛᴇ ꜱᴡᴏʀᴅ";
                ItemStack var19 = namedNetherite(var18, var27, 1484712041);
                var8[0] = var19;
                Material var20 = Material.NETHERITE_AXE;
                String var28 = "ɴᴇᴛʜᴇʀɪᴛᴇ ᴀxᴇ";
                ItemStack var21 = namedNetherite(var20, var28, 1484712041);
                var8[1] = var21;
        }

        return var8;
    }

    public static ItemStack namedNetherite(Material var0, String var1, int var2) {
        ItemStack var3 = new ItemStack(var0);
        ItemMeta var6 = var3.getItemMeta();
        Component var16 = Text.legacy("&f&l" + var1, 373192400);
        var6.displayName(var16);
        List var19 = List.of(Text.legacy("&7One of one on the server.", 373192400));
        var6.lore(var19);
        var3.setItemMeta(var6);
        return var3;
    }
}
