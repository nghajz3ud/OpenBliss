package com.bliss.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public final class PlayerHeads {
    private PlayerHeads() {
    }

    public static ItemStack texturedHead(String var0, int var1) {
        Material var3 = Material.PLAYER_HEAD;
        ItemStack var2 = new ItemStack(var3);
        SkullMeta var8 = (SkullMeta)var2.getItemMeta();
        UUID var13 = new UUID(0L, 0L);
        PlayerProfile var9 = Bukkit.createProfile(var13, null);
        Set var16 = var9.getProperties();
        String var4 = "textures";
        ProfileProperty var23 = new ProfileProperty(var4, var0);
        var16.add(var23);
        var8.setPlayerProfile(var9);
        var2.setItemMeta(var8);
        return var2;
    }
}
