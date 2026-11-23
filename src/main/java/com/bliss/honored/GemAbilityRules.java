package com.bliss.honored;

import com.bliss.BlissPlugin;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

public final class GemAbilityRules {
    public static final int MIN_PCT = 25;
    public static final int MAX_PCT = 200;
    public static final int DEFAULT_PCT = 100;
    private final BlissPlugin plugin;
    private final File file;
    private final Map<GemAbilityRules.GemAbility, Boolean> enabled;
    private final Map<GemAbilityRules.GemAbility, Integer> dmgPct;
    private static GemAbilityRules instance;

    public GemAbilityRules(BlissPlugin var1, int var2) {
        EnumMap var4 = new EnumMap<>(GemAbilityRules.GemAbility.class);
        this.enabled = var4;
        EnumMap var29 = new EnumMap<>(GemAbilityRules.GemAbility.class);
        this.dmgPct = var29;
        this.plugin = var1;
        File var42 = var1.getDataFolder();
        File var31 = new File(var42, "gemabilities.yml");
        this.file = var31;

        for (GemAbilityRules.GemAbility var10 : GemAbilityRules.GemAbility.values()) {
            Map var23 = this.enabled;
            Boolean var37 = true;
            var23.put(var10, var37);
            Map var26 = this.dmgPct;
            Integer var39 = 100;
            var26.put(var10, var39);
        }

        instance = this;
    }

    public static GemAbilityRules get() {
        return instance;
    }

    public boolean enabled(GemAbilityRules.GemAbility var1, int var2) {
        Map<GemAbilityRules.GemAbility, Boolean> var6 = this.enabled;
        Boolean var10 = true;
        return var6.getOrDefault(var1, var10);
    }

    public int dmgPercent(GemAbilityRules.GemAbility var1, int var2) {
        Map<GemAbilityRules.GemAbility, Integer> var6 = this.dmgPct;
        Integer var10 = 100;
        return var6.getOrDefault(var1, var10);
    }

    public double scale(GemAbilityRules.GemAbility var1, int var2) {
        return (double)this.dmgPercent(var1, 678824930) / 100.0;
    }

    public GemAbilityRules.Action action(GemAbilityRules.GemAbility var1) {
        if (!this.enabled(var1, 1767577088)) {
            return GemAbilityRules.Action.BANNED;
        } else {
            int var4 = this.dmgPercent(var1, 678824930);
            GemAbilityRules.Action var11;
            if (var4 < 100) {
                var11 = GemAbilityRules.Action.NERF;
            } else if (var4 > 100) {
                var11 = GemAbilityRules.Action.BUFF;
            } else {
                var11 = GemAbilityRules.Action.ALLOWED;
            }

            return var11;
        }
    }

    public void allow(GemAbilityRules.GemAbility var1) {
        Map var5 = this.enabled;
        Boolean var12 = true;
        var5.put(var1, var12);
        Map var8 = this.dmgPct;
        Integer var14 = 100;
        var8.put(var1, var14);
        this.save(1340314255);
    }

    public void ban(GemAbilityRules.GemAbility var1) {
        Map var5 = this.enabled;
        Boolean var8 = false;
        var5.put(var1, var8);
        this.save(1340314255);
    }

    public void setDamagePercent(GemAbilityRules.GemAbility var1, int var2) {
        Map var8 = this.enabled;
        Boolean var15 = true;
        var8.put(var1, var15);
        Map var11 = this.dmgPct;
        int var19 = Math.min(200, var2);
        Integer var18 = Math.max(25, var19);
        var11.put(var1, var18);
        this.save(1340314255);
    }

    public static boolean on(GemAbilityRules.GemAbility var0, int var1) {
        GemAbilityRules var4 = instance;
        boolean var8;
        if (var4 != null && !var4.enabled(var0, 1767577088)) {
            var8 = false;
        } else {
            var8 = true;
        }

        return var8;
    }

    public static double scaled(GemAbilityRules.GemAbility var0, double var1, int var3) {
        GemAbilityRules var7 = instance;
        double var11;
        if (var7 == null) {
            var11 = var1;
        } else {
            double var13 = var7.scale(var0, 1974131113);
            var11 = var1 * var13;
        }

        return var11;
    }

    public static double scaleOf(GemAbilityRules.GemAbility var0, int var1) {
        GemAbilityRules var4 = instance;
        double var7;
        if (var4 == null) {
            var7 = 1.0;
        } else {
            var7 = var4.scale(var0, 1974131113);
        }

        return var7;
    }

    public void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var9 = YamlConfiguration.loadConfiguration(this.file);

            for (GemAbilityRules.GemAbility var13 : GemAbilityRules.GemAbility.values()) {
                Map var27 = this.enabled;
                String var41 = var13.key() + ".enabled";
                Boolean var36 = var9.getBoolean(var41, true);
                var27.put(var13, var36);
                Map var30 = this.dmgPct;
                String var47 = var13.key() + ".dmgPercent";
                int var45 = var9.getInt(var47, 100);
                int var43 = Math.min(200, var45);
                Integer var39 = Math.max(25, var43);
                var30.put(var13, var39);
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;

        for (GemAbilityRules.GemAbility var9 : GemAbilityRules.GemAbility.values()) {
            String var41 = var9.key() + ".enabled";
            Map var45 = this.enabled;
            Object var46 = var45.get(var9);
            var5.set(var41, var46);
            String var44 = var9.key() + ".dmgPercent";
            Map var48 = this.dmgPct;
            Object var49 = var48.get(var9);
            var5.set(var44, var49);
        }

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var34 = this.file;
            var5.save(var34);
        } catch (IOException var51) {
            Logger var27 = this.plugin.getLogger();
            String var37 = "Failed to save gemabilities.yml: " + var51.getMessage();
            var27.warning(var37);
        }
    }

    public static enum Action {
        BANNED,
        ALLOWED,
        NERF,
        BUFF;
    }

    public static enum GemAbility {
        ASTRA_DAGGERS("Astra", "Dimensional Daggers", true),
        PUFF_DASH("Puff", "Dash", true),
        PUFF_DJ_STOMP("Puff", "Double-Jump Stomp", true),
        PUFF_BREEZY("Puff", "Breezy Bash", true),
        SPEED_BLUR("Speed", "Blur", true),
        SPEED_STORM("Speed", "Speed Storm", true),
        FLUX_KINETIC_BURST("Flux", "Kinetic Burst", true),
        FLUX_GROUND("Flux", "Flux Ground", true),
        STRENGTH_NULLIFY("Strength", "Nullify", false),
        STRENGTH_CHAD("Strength", "Chad Strength", true),
        STRENGTH_FRAILER("Strength", "Frailer", true),
        FIRE_FIREBALL("Fire", "Fireball", true),
        FIRE_METEOR("Fire", "Meteor Shower", true),
        FLUX_BEAM("Flux", "Energy Beam", true),
        LIFE_RADIANT_FIST("Life", "Radiant Fist", true),
        LIFE_HEART_LOCK("Life", "Heart Lock", false),
        LIFE_HEART_DRAINER("Life", "Heart Drainer", false),
        LIFE_CIRCLE("Life", "Circle of Life", false),
        WEALTH_ITEM_LOCK("Wealth", "Item Lock", false),
        ASTRA_PROJECTION("Astra", "Astral Projection", false),
        ASTRA_DRIFT("Astra", "Dimensional Drift", false);

        public final String gem;
        public final String display;
        public final boolean damaging;

        private GemAbility(String nullxx, String nullxxx, boolean nullxxxx) {
            this.gem = nullxx;
            this.display = nullxxx;
            this.damaging = nullxxxx;
        }

        public String key() {
            String var3 = this.name();
            Locale var2 = Locale.ROOT;
            return var3.toLowerCase(var2);
        }

        public static GemAbilityRules.GemAbility byKey(String var0) {
            for (GemAbilityRules.GemAbility var6 : values()) {
                String var15 = var6.key();
                if (var15.equals(var0)) {
                    return var6;
                }
            }

            return null;
        }
    }
}
