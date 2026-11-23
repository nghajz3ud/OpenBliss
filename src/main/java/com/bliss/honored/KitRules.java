package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.pedestal.PedestalReviveRitual;
import com.bliss.pedestal.PedestalState;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.file.YamlConfiguration;

public final class KitRules {
    public static final int WIND_CARRY_CAP = 16;
    public static final long WIND_COOLDOWN_MS = 3000L;
    private final BlissPlugin plugin;
    private final File file;
    private final Map<KitRules.ArmorTier, Boolean> armorAllowed;
    private boolean turtleMasterAllowed;
    private boolean cobwebsAllowed;
    private boolean godApplesAllowed;
    private boolean elytraAllowed;
    private boolean villagersAllowed;
    private boolean shieldAllowed;
    private double pearlCooldownMult;
    private int pearlLimit;
    private KitRules.PotionCap potions;
    private KitRules.WindMode wind;
    private boolean pedestalDeactivated;
    private int borderSize;
    public static final double PEARL_MIN = 0.0;
    public static final double PEARL_MAX = 4.0;
    public static final double PEARL_STEP = 0.25;
    public static final int PEARL_LIMIT_UNLIMITED = -1;
    public static final int PEARL_LIMIT_MIN = 4;
    public static final int PEARL_LIMIT_MAX = 32;
    private boolean pedestalClosed;

    public KitRules(BlissPlugin var1, int var2) {
        EnumMap var4 = new EnumMap<>(KitRules.ArmorTier.class);
        this.armorAllowed = var4;
        this.turtleMasterAllowed = true;
        this.cobwebsAllowed = true;
        this.godApplesAllowed = true;
        this.elytraAllowed = true;
        this.villagersAllowed = true;
        this.shieldAllowed = true;
        this.pearlCooldownMult = 1.0;
        this.pearlLimit = -1;
        KitRules.PotionCap var46 = KitRules.PotionCap.ALL;
        this.potions = var46;
        KitRules.WindMode var47 = KitRules.WindMode.ALLOWED;
        this.wind = var47;
        this.pedestalDeactivated = false;
        this.borderSize = 0;
        this.pedestalClosed = false;
        this.plugin = var1;
        File var59 = var1.getDataFolder();
        File var52 = new File(var59, "kitrules.yml");
        this.file = var52;

        for (KitRules.ArmorTier var10 : KitRules.ArmorTier.values()) {
            Map var35 = this.armorAllowed;
            Boolean var57 = true;
            var35.put(var10, var57);
        }
    }

    public boolean armorAllowed(KitRules.ArmorTier var1, int var2) {
        Map<KitRules.ArmorTier, Boolean> var6 = this.armorAllowed;
        Boolean var10 = true;
        return var6.getOrDefault(var1, var10);
    }

    public void toggleArmor(KitRules.ArmorTier var1) {
        Map var6 = this.armorAllowed;
        boolean var11;
        if (!this.armorAllowed(var1, 1135935034)) {
            var11 = true;
        } else {
            var11 = false;
        }

        Boolean var10 = var11;
        var6.put(var1, var10);
        this.save(231690688);
    }

    public boolean turtleMasterAllowed(int var1) {
        return this.turtleMasterAllowed;
    }

    public void toggleTurtleMaster() {
        boolean var5;
        if (!this.turtleMasterAllowed) {
            var5 = true;
        } else {
            var5 = false;
        }

        this.turtleMasterAllowed = var5;
        this.save(231690688);
    }

    public boolean cobwebsAllowed(int var1) {
        return this.cobwebsAllowed;
    }

    public void toggleCobwebs() {
        boolean var5;
        if (!this.cobwebsAllowed) {
            var5 = true;
        } else {
            var5 = false;
        }

        this.cobwebsAllowed = var5;
        this.save(231690688);
    }

    public void setCobwebsAllowed(boolean var1, int var2) {
        this.cobwebsAllowed = var1;
        this.save(231690688);
    }

    public boolean godApplesAllowed(int var1) {
        return this.godApplesAllowed;
    }

    public void setGodApplesAllowed(boolean var1, int var2) {
        this.godApplesAllowed = var1;
        this.save(231690688);
    }

    public boolean elytraAllowed(int var1) {
        return this.elytraAllowed;
    }

    public void setElytraAllowed(boolean var1, int var2) {
        this.elytraAllowed = var1;
        this.save(231690688);
    }

    public boolean villagersAllowed(int var1) {
        return this.villagersAllowed;
    }

    public void setVillagersAllowed(boolean var1, int var2) {
        this.villagersAllowed = var1;
        this.save(231690688);
    }

    public boolean shieldAllowed(int var1) {
        return this.shieldAllowed;
    }

    public void setShieldAllowed(boolean var1, int var2) {
        this.shieldAllowed = var1;
        this.save(231690688);
    }

    public double pearlCooldownMult(int var1) {
        return this.pearlCooldownMult;
    }

    public void setPearlCooldownMult(double var1, int var3) {
        double var14 = (double)Math.round(var1 / 0.25) * 0.25;
        double var30 = Math.min(4.0, var14);
        double var26 = Math.max(0.0, var30);
        this.pearlCooldownMult = var26;
        this.save(231690688);
    }

    private void setPearlCooldownMultQuiet(double var1, int var3) {
        double var14 = (double)Math.round(var1 / 0.25) * 0.25;
        double var29 = Math.min(4.0, var14);
        double var25 = Math.max(0.0, var29);
        this.pearlCooldownMult = var25;
    }

    public int pearlLimit(int var1) {
        return this.pearlLimit;
    }

    public void stepPearlLimit(int var1, int var2) {
        int var5 = this.pearlLimit;
        int var22;
        if (var5 == -1) {
            if (var1 >= 0) {
                return;
            }

            var22 = 32;
        } else {
            var22 = var5 + var1;
            if (var22 > 32) {
                var22 = -1;
            } else if (var22 < 4) {
                var22 = 4;
            }
        }

        this.pearlLimit = var22;
        this.save(231690688);
    }

    public KitRules.PotionCap potions(int var1) {
        return this.potions;
    }

    public void cyclePotions() {
        KitRules.PotionCap var5 = this.potions.next();
        this.potions = var5;
        this.save(231690688);
    }

    public KitRules.WindMode wind(int var1) {
        return this.wind;
    }

    public void cycleWind() {
        KitRules.WindMode var5 = this.wind.next();
        this.wind = var5;
        this.save(231690688);
    }

    public boolean pedestalDeactivated(int var1) {
        return this.pedestalDeactivated;
    }

    public void togglePedestal() {
        boolean var8;
        if (!this.pedestalDeactivated) {
            var8 = true;
        } else {
            var8 = false;
        }

        this.pedestalDeactivated = var8;
        if (!this.pedestalDeactivated) {
            this.pedestalClosed = false;
        }

        this.save(231690688);
    }

    public boolean pedestalClosed(int var1) {
        return this.pedestalClosed;
    }

    public int borderSize(int var1) {
        return this.borderSize;
    }

    public void setBorderSize(int var1, int var2) {
        int var7 = Math.max(0, var1);
        this.borderSize = var7;
        this.save(231690688);
    }

    public void applyDecree() {
        this.applyDecree(true, 1681962085);
    }

    public void applyDecree(boolean var1, int var2) {
        if (var1 && this.pedestalDeactivated) {
            this.deactivatePedestalNow(412209635);
        }

        if (this.borderSize > 0 && !Bukkit.getWorlds().isEmpty()) {
            WorldBorder var10 = ((World)Bukkit.getWorlds().get(0)).getWorldBorder();
            double var11 = var10.getSize();
            double var31 = (double)this.borderSize;
            if (var31 < var11) {
                double var58 = (double)this.borderSize;
                long var53 = Math.round((var11 - var58) * 36.0);
                long var62 = Math.min(Math.max(1L, var53), 107374182L);
                double var45 = (double)this.borderSize;
                var10.setSize(var45, var62);
            }
        }
    }

    public void deactivatePedestalNow(int var1) {
        if (!this.pedestalDeactivated || !this.pedestalClosed) {
            this.pedestalDeactivated = true;
            this.pedestalClosed = true;
            this.save(231690688);
        }

        PedestalState var4 = this.plugin.pedestalState(590646643);
        if (var4 != null) {
            var4.setActive(false, 1028669057);
            var4.resetRitual(1772872156);
            var4.unbuildStructure(280713799);
            PedestalReviveRitual.restoreWeather(781471445);
            var4.save(973788971);
            if (this.plugin.crushedPedestalOrb(952223969) != null) {
                this.plugin.crushedPedestalOrb(952223969).reconcile(1357222658);
            }
        }
    }

    public static KitRules.ArmorTier tierOf(Material var0, int var1) {
        return switch (var0) {
            case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS-> KitRules.ArmorTier.DIAMOND;
            case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS-> KitRules.ArmorTier.NETHERITE;
            case TURTLE_HELMET-> KitRules.ArmorTier.TURTLE;
            default -> null;
        };
    }

    public void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var7 = YamlConfiguration.loadConfiguration(this.file);

            for (KitRules.ArmorTier var11 : KitRules.ArmorTier.values()) {
                Map var44 = this.armorAllowed;
                String var114 = "armor." + var11.name().toLowerCase();
                Boolean var97 = var7.getBoolean(var114, true);
                var44.put(var11, var97);
            }

            String var4 = "turtleMaster";
            boolean var47 = var7.getBoolean(var4, true);
            this.turtleMasterAllowed = var47;
            String var81 = "cobwebs";
            boolean var49 = var7.getBoolean(var81, true);
            this.cobwebsAllowed = var49;
            String var82 = "godApples";
            boolean var51 = var7.getBoolean(var82, true);
            this.godApplesAllowed = var51;
            String var83 = "elytra";
            boolean var53 = var7.getBoolean(var83, true);
            this.elytraAllowed = var53;
            String var84 = "villagers";
            boolean var55 = var7.getBoolean(var84, true);
            this.villagersAllowed = var55;
            String var85 = "shield";
            boolean var57 = var7.getBoolean(var85, true);
            this.shieldAllowed = var57;
            String var86 = "pearlCooldownMult";
            double var59 = var7.getDouble(var86, 1.0);
            this.setPearlCooldownMultQuiet(var59, 1331018952);
            String var61 = "pearlLimit";
            int var115 = var7.getInt(var61, -1);
            int var64;
            if (var115 >= 4 && var115 <= 32) {
                var64 = var115;
            } else {
                var64 = -1;
            }

            this.pearlLimit = var64;

            try {
                String var90 = "potions";
                String var105 = "ALL";
                KitRules.PotionCap var67 = KitRules.PotionCap.valueOf(var7.getString(var90, var105));
                this.potions = var67;
            } catch (IllegalArgumentException var117) {
                KitRules.PotionCap var68 = KitRules.PotionCap.ALL;
                this.potions = var68;
            }

            try {
                String var91 = "wind";
                String var106 = "ALLOWED";
                KitRules.WindMode var71 = KitRules.WindMode.valueOf(var7.getString(var91, var106));
                this.wind = var71;
            } catch (IllegalArgumentException var116) {
                KitRules.WindMode var78 = KitRules.WindMode.ALLOWED;
                this.wind = var78;
            }

            String var92 = "pedestalDeactivated";
            boolean var73 = var7.getBoolean(var92, false);
            this.pedestalDeactivated = var73;
            String var93 = "pedestalClosed";
            boolean var109 = this.pedestalDeactivated;
            boolean var75 = var7.getBoolean(var93, var109);
            this.pedestalClosed = var75;
            String var94 = "borderSize";
            int var77 = var7.getInt(var94, 0);
            this.borderSize = var77;
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;

        for (KitRules.ArmorTier var9 : KitRules.ArmorTier.values()) {
            String var67 = "armor." + var9.name().toLowerCase();
            Map var108 = this.armorAllowed;
            Object var109 = var108.get(var9);
            var5.set(var67, var109);
        }

        String var45 = "turtleMaster";
        Boolean var69 = this.turtleMasterAllowed;
        var5.set(var45, var69);
        String var46 = "cobwebs";
        Boolean var72 = this.cobwebsAllowed;
        var5.set(var46, var72);
        String var47 = "godApples";
        Boolean var75 = this.godApplesAllowed;
        var5.set(var47, var75);
        String var48 = "elytra";
        Boolean var78 = this.elytraAllowed;
        var5.set(var48, var78);
        String var49 = "villagers";
        Boolean var81 = this.villagersAllowed;
        var5.set(var49, var81);
        String var50 = "shield";
        Boolean var84 = this.shieldAllowed;
        var5.set(var50, var84);
        String var51 = "pearlCooldownMult";
        Double var88 = this.pearlCooldownMult;
        var5.set(var51, var88);
        String var52 = "pearlLimit";
        Integer var91 = this.pearlLimit;
        var5.set(var52, var91);
        String var53 = "potions";
        String var94 = this.potions.name();
        var5.set(var53, var94);
        String var54 = "wind";
        String var97 = this.wind.name();
        var5.set(var54, var97);
        String var55 = "pedestalDeactivated";
        Boolean var100 = this.pedestalDeactivated;
        var5.set(var55, var100);
        String var56 = "pedestalClosed";
        Boolean var103 = this.pedestalClosed;
        var5.set(var56, var103);
        String var57 = "borderSize";
        Integer var106 = this.borderSize;
        var5.set(var57, var106);

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var59 = this.file;
            var5.save(var59);
        } catch (IOException var110) {
            Logger var40 = this.plugin.getLogger();
            String var62 = "Failed to save kitrules.yml: " + var110.getMessage();
            var40.warning(var62);
        }
    }

    public static enum ArmorTier {
        DIAMOND("Diamond"),
        NETHERITE("Netherite"),
        TURTLE("Turtle Shell");

        public final String display;

        private ArmorTier(String nullxx) {
            this.display = nullxx;
        }
    }

    public static enum PotionCap {
        ALL,
        TIER1,
        NONE;

        public KitRules.PotionCap next() {
            KitRules.PotionCap[] var1 = values();
            int var6 = this.ordinal() + 1;
            int var9 = values().length;
            int var7 = var6 % var9;
            return var1[var7];
        }

        public String label() {
            return switch (this) {
                case ALL -> "All allowed (II)";
                case TIER1 -> "Tier I only";
                case NONE -> "Banned";
                default -> {
                    MatchException var5 = new MatchException(null, null);
                    throw var5;
                }
            };
        }

        public boolean permissive() {
            KitRules.PotionCap var2 = ALL;
            boolean var3;
            if (this == var2) {
                var3 = true;
            } else {
                var3 = false;
            }

            return var3;
        }
    }

    public static enum WindMode {
        ALLOWED,
        CAPPED,
        BANNED;

        public KitRules.WindMode next() {
            KitRules.WindMode[] var1 = values();
            int var6 = this.ordinal() + 1;
            int var9 = values().length;
            int var7 = var6 % var9;
            return var1[var7];
        }

        public String label() {
            return switch (this) {
                case ALLOWED -> "Allowed";
                case CAPPED -> "Capped (16 + 3s)";
                case BANNED -> "Banned";
                default -> {
                    MatchException var5 = new MatchException(null, null);
                    throw var5;
                }
            };
        }

        public boolean permissive() {
            KitRules.WindMode var2 = ALLOWED;
            boolean var3;
            if (this == var2) {
                var3 = true;
            } else {
                var3 = false;
            }

            return var3;
        }
    }
}
