package com.bliss.gems;

import java.util.Set;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

final class HandUse {
    private static final Set<String> USED;

    private HandUse() {
    }

    static boolean mainHandInUse(PlayerInteractEvent var0, int var1) {
        Material var5 = var0.getPlayer().getInventory().getItemInMainHand().getType();
        if (var5.isAir()) {
            return false;
        } else if (var5.isEdible()) {
            return true;
        } else {
            String var6 = var5.name();
            if (var6.equals("BUCKET") || var6.endsWith("_BUCKET")) {
                return true;
            } else if (var6.equals("MINECART") || var6.endsWith("_MINECART")) {
                return true;
            } else if (var6.endsWith("_SPAWN_EGG") || var6.endsWith("_BOAT") || var6.endsWith("_RAFT") || var6.endsWith("_BUNDLE")) {
                return true;
            } else if (!var6.endsWith("_HELMET") && !var6.endsWith("_CHESTPLATE") && !var6.endsWith("_LEGGINGS") && !var6.endsWith("_BOOTS")) {
                Set var44 = USED;
                if (var44.contains(var6)) {
                    return true;
                } else {
                    Action var48 = var0.getAction();
                    Action var68 = Action.RIGHT_CLICK_BLOCK;
                    boolean var51;
                    if (var48 == var68 && var5.isBlock()) {
                        var51 = true;
                    } else {
                        var51 = false;
                    }

                    return var51;
                }
            } else {
                return true;
            }
        }
    }

    static {
        String[] var3 = new String[37];
        String var2 = "POTION";
        var3[0] = var2;
        String var41 = "SPLASH_POTION";
        var3[1] = var41;
        String var42 = "LINGERING_POTION";
        var3[2] = var42;
        String var43 = "OMINOUS_BOTTLE";
        var3[3] = var43;
        String var44 = "HONEY_BOTTLE";
        var3[4] = var44;
        String var45 = "GLASS_BOTTLE";
        var3[5] = var45;
        String var46 = "ENDER_PEARL";
        var3[6] = var46;
        String var47 = "ENDER_EYE";
        var3[7] = var47;
        String var48 = "SNOWBALL";
        var3[8] = var48;
        String var49 = "EGG";
        var3[9] = var49;
        String var50 = "FIRE_CHARGE";
        var3[10] = var50;
        String var51 = "WIND_CHARGE";
        var3[11] = var51;
        String var52 = "FIREWORK_ROCKET";
        var3[12] = var52;
        String var53 = "BOW";
        var3[13] = var53;
        String var54 = "CROSSBOW";
        var3[14] = var54;
        String var55 = "TRIDENT";
        var3[15] = var55;
        String var56 = "SHIELD";
        var3[16] = var56;
        String var57 = "SPYGLASS";
        var3[17] = var57;
        String var58 = "GOAT_HORN";
        var3[18] = var58;
        String var59 = "FISHING_ROD";
        var3[19] = var59;
        String var60 = "FLINT_AND_STEEL";
        var3[20] = var60;
        String var61 = "BONE_MEAL";
        var3[21] = var61;
        String var62 = "LEAD";
        var3[22] = var62;
        String var63 = "NAME_TAG";
        var3[23] = var63;
        String var64 = "ARMOR_STAND";
        var3[24] = var64;
        String var65 = "END_CRYSTAL";
        var3[25] = var65;
        String var66 = "ITEM_FRAME";
        var3[26] = var66;
        String var67 = "GLOW_ITEM_FRAME";
        var3[27] = var67;
        String var68 = "PAINTING";
        var3[28] = var68;
        String var69 = "WRITABLE_BOOK";
        var3[29] = var69;
        String var70 = "WRITTEN_BOOK";
        var3[30] = var70;
        String var71 = "BUNDLE";
        var3[31] = var71;
        String var72 = "EXPERIENCE_BOTTLE";
        var3[32] = var72;
        String var73 = "ELYTRA";
        var3[33] = var73;
        String var74 = "SHEARS";
        var3[34] = var74;
        String var75 = "BRUSH";
        var3[35] = var75;
        String var76 = "KNOWLEDGE_BOOK";
        var3[36] = var76;
        USED = Set.of(var3);
    }
}
