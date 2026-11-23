package com.bliss.trader;

import com.bliss.core.GemType;
import com.bliss.items.BlissCoreItems;
import com.bliss.items.PlayerHeads;
import com.bliss.villager.Text;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class TraderMenu {
    private static final String TITLE_PLAIN = "Ultimate Trading Menu";
    public static final int CENTER_TRADE_SLOT = 13;
    public static final Map<Integer, GemType> SLOTS;

    private TraderMenu() {
    }

    public static void open(Player var0, BlissCoreItems var1, GemType var2, int var3) {
        TraderMenu.Holder var4 = new TraderMenu.Holder();
        Component var21 = Text.legacy("Ultimate Trading Menu", 373192400);
        Inventory var8 = Bukkit.createInventory(var4, 27, var21);
        var4.setInventory(var8, 100091229);
        ItemStack var22 = centerTradeHead(474718705);
        var8.setItem(13, var22);
        EnumSet var24 = EnumSet.noneOf(GemType.class);
        render(var8, var1, var2, var24, 17054106);
        var0.openInventory(var8);
    }

    public static void render(Inventory var0, BlissCoreItems var1, GemType var2, Set<GemType> var3, int var4) {
        int var13 = poolSize(var2, var3, 739645699);

        for (Entry var15 : SLOTS.entrySet()) {
            GemType var16 = (GemType)var15.getValue();
            if (var16 == var2) {
                int var39 = (Integer)var15.getKey();
                ItemStack var49 = var1.gemDisplayCurrent(var16, 1341706571);
                var0.setItem(var39, var49);
            } else if (var3.contains(var16)) {
                int var48 = (Integer)var15.getKey();
                ItemStack var53 = var1.gemDisplayExcluded(var16, 1289824476);
                var0.setItem(var48, var53);
            } else {
                int var44 = (Integer)var15.getKey();
                int var56 = chanceFor(var16, var2, var3, var13, 1194796453);
                ItemStack var51 = var1.gemDisplay(var16, var56, 2060074720);
                var0.setItem(var44, var51);
            }
        }
    }

    public static int poolSize(GemType var0, Set<GemType> var1, int var2) {
        int var5 = 0;

        for (GemType var9 : GemType.values()) {
            GemType var25 = GemType.NONE;
            if (var9 != var25 && var9 != var0 && !var1.contains(var9)) {
                var5++;
            }
        }

        return Math.max(var5, 1);
    }

    public static int chanceFor(GemType var0, GemType var1, Set<GemType> var2, int var3, int var4) {
        if (var0 != var1 && !var2.contains(var0)) {
            float var15 = (float)var3;
            return Math.round(100.0F / var15);
        } else {
            return 0;
        }
    }

    private static ItemStack centerTradeHead(int var0) {
        ItemStack var5 = PlayerHeads.texturedHead(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY2MjRhNDRlNzdiOTVkYmUxY2M3MzU1MzY5NTNlODQwYmE2YzE5YjAxNTdjNTQ5ZDliODI1MzQ2NDhjOGNlNCJ9fX0=",
            103702773
        );
        ItemMeta var6 = var5.getItemMeta();
        Component var14 = Text.legacy("<##f7b2f7>§lᴛʀᴀᴅᴇ", 373192400);
        TextDecoration var3 = TextDecoration.ITALIC;
        Component var15 = var14.decoration(var3, false);
        var6.displayName(var15);
        var5.setItemMeta(var6);
        return var5;
    }

    static {
        LinkedHashMap var0 = new LinkedHashMap();
        Integer var22 = 9;
        GemType var1 = GemType.STRENGTH;
        var0.put(var22, var1);
        Integer var24 = 10;
        GemType var37 = GemType.FIRE;
        var0.put(var24, var37);
        Integer var26 = 11;
        GemType var38 = GemType.WEALTH;
        var0.put(var26, var38);
        Integer var28 = 12;
        GemType var39 = GemType.SPEED;
        var0.put(var28, var39);
        Integer var30 = 14;
        GemType var40 = GemType.PUFF;
        var0.put(var30, var40);
        Integer var32 = 15;
        GemType var41 = GemType.ASTRA;
        var0.put(var32, var41);
        Integer var34 = 16;
        GemType var42 = GemType.LIFE;
        var0.put(var34, var42);
        Integer var36 = 17;
        GemType var43 = GemType.FLUX;
        var0.put(var36, var43);
        SLOTS = Map.copyOf(var0);
    }

    public static final class Holder implements InventoryHolder {
        private Inventory inventory;

        public Inventory getInventory() {
            return this.inventory;
        }

        public void setInventory(Inventory var1, int var2) {
            this.inventory = var1;
        }
    }
}
