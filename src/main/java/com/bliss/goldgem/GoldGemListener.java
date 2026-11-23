package com.bliss.goldgem;

import com.bliss.core.GemType;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public final class GoldGemListener implements Listener {
    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent var1) {
        Player var7 = var1.getPlayer();
        if (var7.isSneaking()) {
            ItemStack var8 = GoldGem.findInInventory(var7, 1361603346);
            if (var8 != null) {
                List var9 = GoldGem.absorbedGems(var8, 874118425);
                if (var9.size() >= 2) {
                    var1.setCancelled(true);
                    GemType var28 = GoldGem.getSelected(var8, 128405186);
                    GemType var10 = nextSoul(var9, var28, 1090451905);
                    GoldGem.setSelected(var8, var10, 958539197);
                    Location var31 = var7.getLocation();
                    Sound var4 = Sound.BLOCK_AMETHYST_BLOCK_CHIME;
                    var7.playSound(var31, var4, 0.7F, 1.4F);
                }
            }
        }
    }

    static GemType nextSoul(List<GemType> var0, GemType var1, int var2) {
        int var6 = var0.indexOf(var1);
        int var12 = var6 + 1;
        int var15 = var0.size();
        int var13 = var12 % var15;
        return (GemType)var0.get(var13);
    }
}
