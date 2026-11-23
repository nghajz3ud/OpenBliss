package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public final class ArmorLimits {
    private static final ArmorLimits.Tier DEFAULT_TIER = ArmorLimits.Tier.NETHERITE;
    private final BlissPlugin plugin;
    private final File file;
    private final Map<ArmorLimits.Piece, ArmorLimits.Tier> caps;

    public ArmorLimits(BlissPlugin var1, int var2) {
        EnumMap var4 = new EnumMap<>(ArmorLimits.Piece.class);
        this.caps = var4;
        this.plugin = var1;
        File var31 = var1.getDataFolder();
        File var25 = new File(var31, "armorlimits.yml");
        this.file = var25;

        for (ArmorLimits.Piece var10 : ArmorLimits.Piece.values()) {
            Map var22 = this.caps;
            ArmorLimits.Tier var29 = DEFAULT_TIER;
            var22.put(var10, var29);
        }
    }

    public ArmorLimits.Tier tier(ArmorLimits.Piece var1, int var2) {
        Map<ArmorLimits.Piece, ArmorLimits.Tier> var6 = this.caps;
        ArmorLimits.Tier var5 = DEFAULT_TIER;
        return var6.getOrDefault(var1, var5);
    }

    public void setTier(ArmorLimits.Piece var1, ArmorLimits.Tier var2, int var3) {
        if (var1 != null && var2 != null) {
            Map var9 = this.caps;
            var9.put(var1, var2);
            this.save(510102851);
        }
    }

    public ArmorLimits.Tier step(ArmorLimits.Piece var1, int var2, int var3) {
        ArmorLimits.Tier[] var8 = ArmorLimits.Tier.values();
        int var12 = this.tier(var1, 1084990497).ordinal();
        int var9 = var12 + var2;
        if (var9 < 0) {
            var9 = 0;
        }

        int var25 = var8.length;
        if (var9 >= var25) {
            var9 = var8.length - 1;
        }

        ArmorLimits.Tier var29 = var8[var9];
        this.setTier(var1, var29, 606095390);
        return var8[var9];
    }

    public static Material material(ArmorLimits.Tier var0, ArmorLimits.Piece var1, int var2) {
        String var5 = var0.name();
        String var8 = var1.name();
        return Material.valueOf(var5 + "_" + var8);
    }

    public void load(int var1) {
        for (ArmorLimits.Piece var66 : ArmorLimits.Piece.values()) {
            Map var43 = this.caps;
            ArmorLimits.Tier var62 = DEFAULT_TIER;
            var43.put(var66, var62);
        }

        if (this.file.isFile()) {
            YamlConfiguration var63 = YamlConfiguration.loadConfiguration(this.file);

            for (ArmorLimits.Piece var10 : ArmorLimits.Piece.values()) {
                String var48 = var10.name();
                Locale var4 = Locale.ROOT;
                String var49 = var48.toLowerCase(var4);
                String var11 = var63.getString(var49);
                if (var11 != null) {
                    try {
                        Map var34 = this.caps;
                        String var57 = var11.trim();
                        Locale var5 = Locale.ROOT;
                        ArmorLimits.Tier var59 = ArmorLimits.Tier.valueOf(var57.toUpperCase(var5));
                        var34.put(var10, var59);
                    } catch (IllegalArgumentException var67) {
                        Logger var39 = this.plugin.getLogger();
                        String var52 = String.valueOf(var10);
                        String var61 = String.valueOf(DEFAULT_TIER);
                        String var53 = "armorlimits.yml: unreadable tier for " + var52 + " — keeping " + var61;
                        var39.warning(var53);
                    }
                }
            }
        }
    }

    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;

        for (ArmorLimits.Piece var9 : ArmorLimits.Piece.values()) {
            String var26 = var9.name();
            Locale var3 = Locale.ROOT;
            String var27 = var26.toLowerCase(var3);
            String var35 = this.tier(var9, 1084990497).name();
            var5.set(var27, var35);
        }

        try {
            File var29 = this.file;
            AtomicYml.save(var5, var29, 1347473142);
        } catch (IOException var37) {
            Logger var22 = this.plugin.getLogger();
            String var32 = "Failed to save armorlimits.yml: " + var37.getMessage();
            var22.warning(var32);
        }
    }

    public static enum Piece {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS;
    }

    public static enum Tier {
        LEATHER,
        GOLDEN,
        CHAINMAIL,
        IRON,
        DIAMOND,
        NETHERITE;
    }
}
