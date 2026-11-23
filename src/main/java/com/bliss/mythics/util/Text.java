package com.bliss.mythics.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Text {
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder().character('&').hexColors().build();
    private static final int ICON_ADV = 28;
    private static final int BOX_RIGHT = 25;
    private static final int BOX_CENTER = 23;
    private static final int DIGIT_ADV = 6;

    private Text() {
    }

    public static Component legacy(String var0, int var1) {
        if (var0 == null) {
            return Component.empty();
        } else {
            LegacyComponentSerializer var5 = LEGACY;
            String var7 = expandHexTags(var0, 1285098193);
            return var5.deserialize(var7);
        }
    }

    public static Component item(String var0, int var1) {
        Component var5 = legacy(var0, 883008065);
        TextDecoration var3 = TextDecoration.ITALIC;
        return var5.decoration(var3, false);
    }

    public static String gradient(String var0, int var1, int var2, boolean var3) {
        if (var0.isEmpty()) {
            return var0;
        } else {
            int var11 = var1 >> 16 & 0xFF;
            int var12 = var1 >> 8 & 0xFF;
            int var13 = var1 & 0xFF;
            int var14 = var2 >> 16 & 0xFF;
            int var15 = var2 >> 8 & 0xFF;
            int var16 = var2 & 0xFF;
            int var97 = var0.length() - 1;
            int var17 = Math.max(1, var97);
            StringBuilder var45 = new StringBuilder();
            StringBuilder var18 = var45;
            int var19 = 0;

            while (true) {
                int var99 = var0.length();
                if (var19 >= var99) {
                    return var18.toString();
                }

                double var49 = (double)var19;
                double var106 = (double)var17;
                double var20 = var49 / var106;
                double var54 = (double)var11;
                double var110 = (double)(var14 - var11);
                double var112 = var110 * var20;
                int var22 = (int)Math.round(var54 + var112);
                double var62 = (double)var12;
                double var116 = (double)(var15 - var12);
                double var118 = var116 * var20;
                int var23 = (int)Math.round(var62 + var118);
                double var70 = (double)var13;
                double var122 = (double)(var16 - var13);
                double var124 = var122 * var20;
                int var24 = (int)Math.round(var70 + var124);
                String var100 = "<##%02X%02X%02X>";
                Object[] var127 = new Object[3];
                Integer var138 = var22;
                var127[0] = var138;
                Integer var140 = var23;
                var127[1] = var140;
                Integer var142 = var24;
                var127[2] = var142;
                String var101 = String.format(var100, var127);
                var18.append(var101);
                if (var3) {
                    var18.append("&l");
                }

                char var104 = var0.charAt(var19);
                var18.append(var104);
                var19++;
            }
        }
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

            int var7 = matchHexPrefix(var0, var6, 654787356);
            if (var7 > 0) {
                int var16 = var6 + var7 + 7;
                int var40 = var0.length();
                if (var16 <= var40) {
                    int var42 = var6 + var7;
                    if (isHex(var0, var42, 6, 1787650763)) {
                        int var45 = var6 + var7 + 6;
                        if (var0.charAt(var45) == '>') {
                            var5.append("&x");

                            for (int var8 = 0; var8 < 6; var8++) {
                                StringBuilder var28 = var5.append('&');
                                int var63 = var6 + var7;
                                int var64 = var63 + var8;
                                char var53 = var0.charAt(var64);
                                var28.append(var53);
                            }

                            int var50 = var7 + 7;
                            var6 += var50;
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

    public static String space(int var0, int var1) {
        int var10 = Math.min(40, var0);
        int var11 = Math.max(-40, var10);
        return String.valueOf((char)(57896 + var11));
    }

    public static String slotDigits(int var0, int var1) {
        int var7 = Math.min(99, var0);
        return Integer.toString(Math.max(0, var7));
    }

    public static String iconSlot(String var0, int var1, String var2) {
        return var1 <= 0 ? var0 : iconCount(var0, var1, var2, 1112509315);
    }

    public static String iconCount(String var0, int var1, String var2, int var3) {
        int var29 = Math.min(99, var1);
        int var49 = Math.max(0, var29);
        String var9 = Integer.toString(var49);
        int var34 = (var9.length() * 6 - 1 + 1) / 2;
        int var11 = 23 - var34;
        int var12 = var11 - 28;
        int var44 = var9.length() * 6;
        int var37 = var11 + var44;
        int var13 = 28 - var37;
        String var39 = space(var12, 928271347);
        String var47 = slotDigits(var49, 937819186);
        String var48 = space(var13, 928271347);
        return var0 + var39 + var2 + var47 + var48;
    }

    private static boolean isHex(String var0, int var1, int var2, int var3) {
        for (int var7 = 0; var7 < var2; var7++) {
            int var24 = var1 + var7;
            char var8 = var0.charAt(var24);
            boolean var22;
            if ((var8 < '0' || var8 > '9') && (var8 < 'a' || var8 > 'f') && (var8 < 'A' || var8 > 'F')) {
                var22 = false;
            } else {
                var22 = true;
            }

            if (!var22) {
                return false;
            }
        }

        return true;
    }
}
