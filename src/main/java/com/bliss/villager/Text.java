package com.bliss.villager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Text {
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder().character('&').hexColors().build();

    private Text() {
    }

    public static Component legacy(String var0, int var1) {
        if (var0 == null) {
            return Component.empty();
        } else {
            LegacyComponentSerializer var4 = LEGACY;
            String var7 = expandHexTags(var0, 475008331);
            return var4.deserialize(var7);
        }
    }

    public static Component gradient(String var0, int[] var1, int var2) {
        if (var0 != null && !var0.isEmpty()) {
            if (var1.length == 0) {
                return Component.text(var0);
            } else if (var1.length == 1) {
                TextColor var75 = TextColor.color(var1[0]);
                return Component.text(var0, var75);
            } else {
                Component var8 = Component.empty();
                int var9 = var0.length();

                for (int var10 = 0; var10 < var9; var10++) {
                    double var52;
                    if (var9 == 1) {
                        var52 = 0.0;
                    } else {
                        double var30 = (double)var10;
                        double var77 = (double)(var9 - 1);
                        var52 = var30 / var77;
                    }

                    double var82 = (double)(var1.length - 1);
                    double var13 = var52 * var82;
                    int var15 = (int)Math.floor(var13);
                    int var64 = var1.length - 2;
                    if (var15 > var64) {
                        var15 = var1.length - 2;
                    }

                    if (var15 < 0) {
                        var15 = 0;
                    }

                    int var48 = var1[var15];
                    int var86 = var15 + 1;
                    int var68 = var1[var86];
                    double var97 = (double)var15;
                    double var89 = var13 - var97;
                    int var16 = lerpRgb(var48, var68, var89, 845413076);
                    String var71 = String.valueOf(var0.charAt(var10));
                    TextColor var93 = TextColor.color(var16);
                    TextComponent var72 = Component.text(var71, var93);
                    var8 = var8.append(var72);
                }

                return (Component)var8;
            }
        } else {
            return Component.empty();
        }
    }

    private static int lerpRgb(int var0, int var1, double var2, int var4) {
        if (var2 < 0.0) {
            var2 = 0.0;
        } else if (var2 > 1.0) {
            var2 = 1.0;
        }

        int var13 = var0 >> 16 & 0xFF;
        int var14 = var0 >> 8 & 0xFF;
        int var15 = var0 & 0xFF;
        int var16 = var1 >> 16 & 0xFF;
        int var17 = var1 >> 8 & 0xFF;
        int var18 = var1 & 0xFF;
        double var42 = (double)var13;
        double var89 = (double)(var16 - var13);
        double var91 = var89 * var2;
        int var19 = (int)Math.round(var42 + var91);
        double var50 = (double)var14;
        double var95 = (double)(var17 - var14);
        double var97 = var95 * var2;
        int var20 = (int)Math.round(var50 + var97);
        double var58 = (double)var15;
        double var101 = (double)(var18 - var15);
        double var103 = var101 * var2;
        int var21 = (int)Math.round(var58 + var103);
        int var66 = var19 << 16;
        int var85 = var20 << 8;
        int var67 = var66 | var85;
        return var67 | var21;
    }

    public static Component flowGradient(String var0, double var1, int[] var3, int var4) {
        if (var0 != null && !var0.isEmpty()) {
            if (var3.length == 0) {
                return Component.text(var0);
            } else if (var3.length == 1) {
                TextColor var45 = TextColor.color(var3[0]);
                return Component.text(var0, var45);
            } else {
                Component var10 = Component.empty();
                int var11 = var0.length();

                for (int var12 = 0; var12 < var11; var12++) {
                    double var54;
                    if (var11 <= 1) {
                        var54 = 0.0;
                    } else {
                        double var51 = (double)var12;
                        double var58 = (double)var11;
                        var54 = var51 / var58;
                    }

                    double var13 = var1 + var54;
                    String var41 = String.valueOf(var0.charAt(var12));
                    TextColor var49 = TextColor.color(sampleCyclic(var3, var13, 944140310));
                    TextComponent var42 = Component.text(var41, var49);
                    var10 = var10.append(var42);
                }

                return (Component)var10;
            }
        } else {
            return Component.empty();
        }
    }

    private static int sampleCyclic(int[] var0, double var1, int var3) {
        int var10 = var0.length;
        double var43 = Math.floor(var1);
        double var22 = var1 - var43;
        double var46 = (double)var10;
        double var13 = var22 * var46;
        int var30 = (int)Math.floor(var13);
        int var15 = var30 % var10;
        int var33 = var15 + 1;
        int var16 = var33 % var10;
        int var36 = var0[var15];
        int var42 = var0[var16];
        double var53 = Math.floor(var13);
        double var51 = var13 - var53;
        return lerpRgb(var36, var42, var51, 845413076);
    }

    public static String smallCaps(String var0, int var1) {
        if (var0 == null) {
            return "";
        } else {
            int var19 = var0.length();
            StringBuilder var6 = new StringBuilder(var19);
            StringBuilder var4 = var6;
            int var5 = 0;

            while (true) {
                int var15 = var0.length();
                if (var5 >= var15) {
                    return var4.toString();
                }

                String var18 = smallCap(var0.charAt(var5), 1734284436);
                var4.append(var18);
                var5++;
            }
        }
    }

    private static String smallCap(char var0, int var1) {
        return switch (Character.toLowerCase(var0)) {
            case 'a' -> "ᴀ";
            case 'b' -> "ʙ";
            case 'c' -> "ᴄ";
            case 'd' -> "ᴅ";
            case 'e' -> "ᴇ";
            case 'f' -> "ꜰ";
            case 'g' -> "ɢ";
            case 'h' -> "ʜ";
            case 'i' -> "ɪ";
            case 'j' -> "ᴊ";
            case 'k' -> "ᴋ";
            case 'l' -> "ʟ";
            case 'm' -> "ᴍ";
            case 'n' -> "ɴ";
            case 'o' -> "ᴏ";
            case 'p' -> "ᴘ";
            case 'q' -> "ǫ";
            case 'r' -> "ʀ";
            case 's' -> "ꜱ";
            case 't' -> "ᴛ";
            case 'u' -> "ᴜ";
            case 'v' -> "ᴠ";
            case 'w' -> "ᴡ";
            case 'x' -> "x";
            case 'y' -> "ʏ";
            case 'z' -> "ᴢ";
            default -> String.valueOf(var0);
        };
    }

    private static String expandHexTags(String var0, int var1) {
        int var56 = var0.length();
        StringBuilder var2 = new StringBuilder(var56);
        StringBuilder var5 = var2;
        int var6 = 0;

        while (true) {
            int var35 = var0.length();
            if (var6 >= var35) {
                return var5.toString();
            }

            int var7 = matchHexPrefix(var0, var6, 495055893);
            if (var7 > 0) {
                int var18 = var6 + var7 + 7;
                int var40 = var0.length();
                if (var18 <= var40) {
                    int var42 = var6 + var7;
                    if (isHex(var0, var42, 6, 222177864)) {
                        int var45 = var6 + var7 + 6;
                        if (var0.charAt(var45) == '>') {
                            var5.append("&x");

                            for (int var8 = 0; var8 < 6; var8++) {
                                StringBuilder var28 = var5.append('&');
                                int var62 = var6 + var7;
                                int var63 = var62 + var8;
                                char var51 = var0.charAt(var63);
                                var28.append(var51);
                            }

                            int var53 = var7 + 7;
                            var6 += var53;
                            continue;
                        }
                    }
                }
            }

            char var55 = var0.charAt(var6);
            var5.append(var55);
            var6++;
        }
    }

    private static int matchHexPrefix(String var0, int var1, int var2) {
        int var6 = var1 + 1;
        int var19 = var0.length();
        if (var6 < var19 && var0.charAt(var1) == '<') {
            int var23 = var1 + 1;
            if (var0.charAt(var23) == '#') {
                int var12 = var1 + 2;
                int var27 = var0.length();
                if (var12 < var27) {
                    int var29 = var1 + 2;
                    if (var0.charAt(var29) == '#') {
                        return 3;
                    }
                }

                return 2;
            }
        }

        return 0;
    }

    private static boolean isHex(String var0, int var1, int var2, int var3) {
        for (int var7 = 0; var7 < var2; var7++) {
            int var24 = var1 + var7;
            char var8 = var0.charAt(var24);
            boolean var21;
            if ((var8 < '0' || var8 > '9') && (var8 < 'a' || var8 > 'f') && (var8 < 'A' || var8 > 'F')) {
                var21 = false;
            } else {
                var21 = true;
            }

            if (!var21) {
                return false;
            }
        }

        return true;
    }
}
