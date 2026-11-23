package com.bliss.honored;

import com.bliss.BlissPlugin;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public final class CageManager {
    private final BlissPlugin plugin;
    private final File file;
    private boolean active;
    private boolean engaged;
    private boolean pendingEngage;
    private UUID caged;
    private Location center;
    private double radius = 5.0;
    private double minY;

    public CageManager(BlissPlugin var1, int var2) {
        this.plugin = var1;
        File var13 = var1.getDataFolder();
        File var12 = new File(var13, "cage.yml");
        this.file = var12;
    }

    public boolean isActive(int var1) {
        return this.active;
    }

    public boolean isEngaged(int var1) {
        return this.engaged;
    }

    public UUID caged(int var1) {
        return this.caged;
    }

    public Location center(int var1) {
        return this.center;
    }

    public double radius(int var1) {
        return this.radius;
    }

    public double minY(int var1) {
        return this.minY;
    }

    public void set(UUID var1, Location var2, double var3, int var5) {
        this.caged = var1;
        Location var17 = var2.clone();
        this.center = var17;
        this.radius = var3;
        double var23 = var2.getY() - 0.5;
        this.minY = var23;
        this.active = true;
        this.engaged = false;
        this.save(1684552950);
    }

    public void engage(int var1) {
        if (this.active) {
            this.engaged = true;
            this.pendingEngage = false;
            this.save(1684552950);
        }
    }

    public void setPendingEngage(boolean var1, int var2) {
        this.pendingEngage = var1;
        this.save(1684552950);
    }

    public void recoverPendingEngage(int var1) {
        if (this.pendingEngage) {
            if (this.active && !this.engaged) {
                this.engage(741182186);
                this.plugin.getLogger().info("Cage engage recovered from a summon ritual interrupted by a restart.");
            } else {
                this.pendingEngage = false;
                this.save(1684552950);
            }
        }
    }

    public void clear(int var1) {
        this.active = false;
        this.engaged = false;
        this.pendingEngage = false;
        this.caged = null;
        this.center = null;
        this.save(1684552950);
    }

    public void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var15 = YamlConfiguration.loadConfiguration(this.file);
            String var3 = "active";
            if (var15.getBoolean(var3, false)) {
                String var16 = var15.getString("caged");
                String var42 = "world";
                String var57 = "";
                World var17 = Bukkit.getWorld(var15.getString(var42, var57));
                if (var16 != null && var17 != null) {
                    try {
                        UUID var44 = UUID.fromString(var16);
                        this.caged = var44;
                    } catch (IllegalArgumentException var87) {
                        return;
                    }

                    double var72 = var15.getDouble("x");
                    double var76 = var15.getDouble("y");
                    double var78 = var15.getDouble("z");
                    float var82 = (float)var15.getDouble("yaw");
                    float var86 = (float)var15.getDouble("pitch");
                    Location var45 = new Location(var17, var72, var76, var78, var82, var86);
                    this.center = var45;
                    String var58 = "radius";
                    double var47 = var15.getDouble(var58, 5.0);
                    this.radius = var47;
                    String var59 = "minY";
                    double var68 = this.center.getY() - 0.5;
                    double var50 = var15.getDouble(var59, var68);
                    this.minY = var50;
                    this.active = true;
                    String var60 = "engaged";
                    boolean var54 = var15.getBoolean(var60, false);
                    this.engaged = var54;
                    String var61 = "pendingEngage";
                    boolean var56 = var15.getBoolean(var61, false);
                    this.pendingEngage = var56;
                }
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        String var5 = null;
        if (this.center != null) {
            try {
                World var6 = this.center.getWorld();
                if (var6 != null) {
                    var5 = var6.getName();
                }
            } catch (IllegalArgumentException var121) {
                var5 = null;
            }
        }

        if (this.active && this.center != null && this.caged != null && var5 == null) {
            this.plugin.getLogger().warning("cage.yml: the cage's world is unloaded — leaving the saved cage on disk untouched.");
        } else {
            YamlConfiguration var26 = new YamlConfiguration();
            YamlConfiguration var120 = var26;
            String var60 = "active";
            Boolean var78 = this.active;
            var26.set(var60, var78);
            String var61 = "engaged";
            Boolean var81 = this.engaged;
            var26.set(var61, var81);
            String var62 = "pendingEngage";
            Boolean var84 = this.pendingEngage;
            var26.set(var62, var84);
            if (this.active && this.center != null && this.caged != null) {
                String var63 = "caged";
                String var87 = this.caged.toString();
                var26.set(var63, var87);
                String var64 = "world";
                var26.set(var64, var5);
                String var65 = "x";
                Double var93 = this.center.getX();
                var26.set(var65, var93);
                String var66 = "y";
                Double var98 = this.center.getY();
                var26.set(var66, var98);
                String var67 = "z";
                Double var103 = this.center.getZ();
                var26.set(var67, var103);
                String var68 = "yaw";
                Float var107 = this.center.getYaw();
                var26.set(var68, var107);
                String var69 = "pitch";
                Float var111 = this.center.getPitch();
                var26.set(var69, var111);
                String var70 = "radius";
                Double var115 = this.radius;
                var26.set(var70, var115);
                String var71 = "minY";
                Double var119 = this.minY;
                var26.set(var71, var119);
            }

            try {
                if (!this.file.getParentFile().exists()) {
                    this.file.getParentFile().mkdirs();
                }
                File var73 = this.file;
                var120.save(var73);
            } catch (IOException var7) {
                Logger var57 = this.plugin.getLogger();
                String var76 = "Failed to save cage.yml: " + var7.getMessage();
                var57.warning(var76);
            }
        }
    }
}
