package com.bliss.core;

import com.bliss.goldgem.GoldGem;
import com.bliss.mythics.MythicItems;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class TagStore {
    private final PlayerDataStore players;

    public TagStore(PlayerDataStore var1, int var2) {
        this.players = var1;
    }

    public boolean isTagged(UUID var1, int var2) {
        PlayerDataStore var5 = this.players;
        return var5.get(var1, 1264010596).isTagged(555875923);
    }

    public void addTagSeconds(UUID var1, int var2, int var3) {
        Player var6 = Bukkit.getPlayer(var1);
        if (var6 != null) {
            PlayerDataStore var10 = this.players;
            GemType var12 = var10.get(var1, 1264010596).gem(1413186337);
            GemType var20 = GemType.NONE;
            if (var12 == var20 && !holdsGoldGem(var6, 1274267871) && MythicItems.hasMythicGemInInventory(var6, 2063095181)) {
                return;
            }
        }

        PlayerDataStore var18 = this.players;
        PlayerData var19 = var18.get(var1, 1264010596);
        var19.addTagSeconds(var2, 1722133252);
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

    public void addTagSecondsUncapped(UUID var1, int var2, int var3) {
        PlayerDataStore var6 = this.players;
        PlayerData var7 = var6.get(var1, 1264010596);
        var7.addTagSecondsUncapped(var2, 272168802);
    }

    public void clear(UUID var1, int var2) {
        PlayerDataStore var5 = this.players;
        var5.get(var1, 1264010596).clearTag(1961285331);
    }

    public void clearAll(int var1) {
        Iterator var3 = this.players.all(1865263541).values().iterator();

        while (var3.hasNext()) {
            ((PlayerData)var3.next()).clearTag(1961285331);
        }
    }
}
