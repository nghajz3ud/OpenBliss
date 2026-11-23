package com.bliss.villager;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;

public final class Cosmetics {
    private Cosmetics() {
    }

    public static Component villagerName(int var0, int var1) {
        return switch (var0) {
            case 1 -> Text.legacy("&fDuke Dennis", 373192400);
            case 2 -> Text.legacy("&fKaiCenat", 373192400);
            default -> Text.legacy("&fDarren Watkins", 373192400);
        };
    }

    public static String soulColorPrefix(int var0) {
        return switch (var0) {
            case 1 -> "<##FF69B4>";
            case 2 -> "<##C0C0C0>";
            default -> "<##DA70D6>";
        };
    }

    public static DustOptions auraDust(int var0, float var1, int var2) {
        Color var6 = soulColor(var0, 511968428);
        return new DustOptions(var6, var1);
    }

    public static Color soulColor(int var0, int var1) {
        return switch (var0) {
            case 1 -> Color.fromRGB(255, 105, 180);
            case 2 -> Color.fromRGB(192, 192, 192);
            default -> Color.fromRGB(218, 112, 214);
        };
    }

    public static DustOptions auraDustEdge(int var0, float var1, int var2) {
        Color var8 = soulColor(var0, 511968428);
        int var9 = (var8.getRed() + 255) / 2;
        int var10 = (var8.getGreen() + 255) / 2;
        int var11 = (var8.getBlue() + 255) / 2;
        Color var31 = Color.fromRGB(var9, var10, var11);
        return new DustOptions(var31, var1);
    }

    public static DustOptions beamDust(int var0, int var1) {
        DustOptions var6;
        switch (var0) {
            case 1:
                Color var7 = Color.fromRGB(255, 105, 180);
                var6 = new DustOptions(var7, 1.0F);
                break;
            case 2:
                Color var11 = Color.fromRGB(180, 180, 180);
                var6 = new DustOptions(var11, 1.0F);
                break;
            default:
                Color var9 = Color.fromRGB(200, 80, 200);
                var6 = new DustOptions(var9, 1.0F);
        }

        return var6;
    }
}
