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

public final class ControlRoomManager {
    private final BlissPlugin plugin;
    private final File file;
    private boolean configured;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String shrineWorld;
    private int sbx;
    private int sby;
    private int sbz;
    private String shrineData;
    private UUID returnHomePending;
    private UUID pendingAscension;
    private boolean session;
    private boolean enchConfigured;
    private String enchWorld;
    private double ex;
    private double ey;
    private double ez;
    private float eyaw;
    private boolean armorConfigured;
    private String armorWorld;
    private double ax;
    private double ay;
    private double az;
    private float ayaw;
    private boolean borderConfigured;
    private String borderWorld;
    private double bx;
    private double by;
    private double bz;
    private float byaw;
    private boolean potConfigured;
    private String potWorld;
    private double px;
    private double py;
    private double pz;
    private float pyaw;

    public ControlRoomManager(BlissPlugin var1, int var2) {
        this.plugin = var1;
        File var10 = var1.getDataFolder();
        File var9 = new File(var10, "controlroom.yml");
        this.file = var9;
    }

    public boolean isSet(int var1) {
        boolean var4;
        if (this.location(1988022717) != null) {
            var4 = true;
        } else {
            var4 = false;
        }

        return var4;
    }

    public boolean isConfigured(int var1) {
        boolean var9;
        if (this.configured && this.worldName != null && !this.worldName.isEmpty()) {
            var9 = true;
        } else {
            var9 = false;
        }

        return var9;
    }

    public String worldName(int var1) {
        String var5;
        if (this.isConfigured(553762660)) {
            var5 = this.worldName;
        } else {
            var5 = null;
        }

        return var5;
    }

    public Location location(int var1) {
        if (!this.isConfigured(553762660)) {
            return null;
        } else {
            World var9 = Bukkit.getWorld(this.worldName);
            if (var9 == null) {
                return null;
            } else {
                double var18 = this.x;
                double var20 = this.y;
                double var22 = this.z;
                float var24 = this.yaw;
                float var25 = this.pitch;
                return new Location(var9, var18, var20, var22, var24, var25);
            }
        }
    }

    public UUID returnHomePending() {
        return this.returnHomePending;
    }

    public void setReturnHomePending(UUID var1) {
        this.returnHomePending = var1;
        this.save(1549144117);
    }

    public void clearReturnHomePending() {
        this.returnHomePending = null;
        this.save(1549144117);
    }

    public void setShrineBlock(Location var1, String var2, int var3) {
        String var12 = var1.getWorld().getName();
        this.shrineWorld = var12;
        int var14 = var1.getBlockX();
        this.sbx = var14;
        int var16 = var1.getBlockY();
        this.sby = var16;
        int var18 = var1.getBlockZ();
        this.sbz = var18;
        this.shrineData = var2;
        this.save(1549144117);
    }

    public void clearShrineBlock(int var1) {
        this.shrineWorld = null;
        this.shrineData = null;
        this.save(1549144117);
    }

    public Location shrineBlock(int var1) {
        if (this.shrineWorld != null && this.shrineData != null) {
            World var7 = Bukkit.getWorld(this.shrineWorld);
            Location var15;
            if (var7 == null) {
                var15 = null;
            } else {
                double var18 = (double)this.sbx;
                double var21 = (double)this.sby;
                double var24 = (double)this.sbz;
                var15 = new Location(var7, var18, var21, var24);
            }

            return var15;
        } else {
            return null;
        }
    }

    public String shrineData(int var1) {
        return this.shrineData;
    }

    public UUID pending(int var1) {
        return this.pendingAscension;
    }

    public void setPending(UUID var1, int var2) {
        this.pendingAscension = var1;
        this.save(1549144117);
    }

    public void clearPending(int var1) {
        this.pendingAscension = null;
        this.save(1549144117);
    }

    public boolean inSession(int var1) {
        return this.session;
    }

    public void beginSession(int var1) {
        if (!this.session) {
            this.session = true;
            this.save(1549144117);
            if (this.plugin.honoredOne(783425701) != null) {
                this.plugin.honoredOne(783425701).endTrial(1966060520);
            }
        }
    }

    public void endSession(int var1) {
        if (this.session) {
            this.session = false;
            this.save(1549144117);
        }
    }

    public boolean enchantConfigured(int var1) {
        boolean var9;
        if (this.enchConfigured && this.enchWorld != null && !this.enchWorld.isEmpty()) {
            var9 = true;
        } else {
            var9 = false;
        }

        return var9;
    }

    public String enchantWorldName(int var1) {
        String var5;
        if (this.enchantConfigured(2144921445)) {
            var5 = this.enchWorld;
        } else {
            var5 = null;
        }

        return var5;
    }

    public Location enchantSpot(int var1) {
        if (!this.enchantConfigured(2144921445)) {
            return null;
        } else {
            World var9 = Bukkit.getWorld(this.enchWorld);
            if (var9 == null) {
                return null;
            } else {
                double var18 = this.ex;
                double var20 = this.ey;
                double var22 = this.ez;
                float var24 = this.eyaw;
                return new Location(var9, var18, var20, var22, var24, 0.0F);
            }
        }
    }

    public void setEnchantSpot(Location var1, int var2) {
        String var12 = var1.getWorld().getName();
        this.enchWorld = var12;
        double var14 = var1.getX();
        this.ex = var14;
        double var17 = var1.getY();
        this.ey = var17;
        double var20 = var1.getZ();
        this.ez = var20;
        float var23 = var1.getYaw();
        this.eyaw = var23;
        this.enchConfigured = true;
        this.save(1549144117);
    }

    public void clearEnchantSpot(int var1) {
        this.enchConfigured = false;
        this.enchWorld = null;
        this.save(1549144117);
    }

    public boolean armorConfigured(int var1) {
        boolean var9;
        if (this.armorConfigured && this.armorWorld != null && !this.armorWorld.isEmpty()) {
            var9 = true;
        } else {
            var9 = false;
        }

        return var9;
    }

    public String armorWorldName(int var1) {
        String var5;
        if (this.armorConfigured(1763373199)) {
            var5 = this.armorWorld;
        } else {
            var5 = null;
        }

        return var5;
    }

    public Location armorSpot(int var1) {
        if (!this.armorConfigured(1763373199)) {
            return null;
        } else {
            World var9 = Bukkit.getWorld(this.armorWorld);
            if (var9 == null) {
                return null;
            } else {
                double var18 = this.ax;
                double var20 = this.ay;
                double var22 = this.az;
                float var24 = this.ayaw;
                return new Location(var9, var18, var20, var22, var24, 0.0F);
            }
        }
    }

    public void setArmorSpot(Location var1, int var2) {
        String var12 = var1.getWorld().getName();
        this.armorWorld = var12;
        double var14 = var1.getX();
        this.ax = var14;
        double var17 = var1.getY();
        this.ay = var17;
        double var20 = var1.getZ();
        this.az = var20;
        float var23 = var1.getYaw();
        this.ayaw = var23;
        this.armorConfigured = true;
        this.save(1549144117);
    }

    public void clearArmorSpot(int var1) {
        this.armorConfigured = false;
        this.armorWorld = null;
        this.save(1549144117);
    }

    public boolean borderConfigured(int var1) {
        boolean var9;
        if (this.borderConfigured && this.borderWorld != null && !this.borderWorld.isEmpty()) {
            var9 = true;
        } else {
            var9 = false;
        }

        return var9;
    }

    public String borderWorldName(int var1) {
        String var4;
        if (this.borderConfigured(201221402)) {
            var4 = this.borderWorld;
        } else {
            var4 = null;
        }

        return var4;
    }

    public Location borderSpot(int var1) {
        if (!this.borderConfigured(201221402)) {
            return null;
        } else {
            World var9 = Bukkit.getWorld(this.borderWorld);
            if (var9 == null) {
                return null;
            } else {
                double var18 = this.bx;
                double var20 = this.by;
                double var22 = this.bz;
                float var24 = this.byaw;
                return new Location(var9, var18, var20, var22, var24, 0.0F);
            }
        }
    }

    public void setBorderSpot(Location var1, int var2) {
        String var12 = var1.getWorld().getName();
        this.borderWorld = var12;
        double var14 = var1.getX();
        this.bx = var14;
        double var17 = var1.getY();
        this.by = var17;
        double var20 = var1.getZ();
        this.bz = var20;
        float var23 = var1.getYaw();
        this.byaw = var23;
        this.borderConfigured = true;
        this.save(1549144117);
    }

    public void clearBorderSpot(int var1) {
        this.borderConfigured = false;
        this.borderWorld = null;
        this.save(1549144117);
    }

    public boolean potionConfigured(int var1) {
        boolean var9;
        if (this.potConfigured && this.potWorld != null && !this.potWorld.isEmpty()) {
            var9 = true;
        } else {
            var9 = false;
        }

        return var9;
    }

    public String potionWorldName(int var1) {
        String var4;
        if (this.potionConfigured(886107921)) {
            var4 = this.potWorld;
        } else {
            var4 = null;
        }

        return var4;
    }

    public Location potionSpot(int var1) {
        if (!this.potionConfigured(886107921)) {
            return null;
        } else {
            World var9 = Bukkit.getWorld(this.potWorld);
            if (var9 == null) {
                return null;
            } else {
                double var18 = this.px;
                double var20 = this.py;
                double var22 = this.pz;
                float var24 = this.pyaw;
                return new Location(var9, var18, var20, var22, var24, 0.0F);
            }
        }
    }

    public void setPotionSpot(Location var1, int var2) {
        String var12 = var1.getWorld().getName();
        this.potWorld = var12;
        double var14 = var1.getX();
        this.px = var14;
        double var17 = var1.getY();
        this.py = var17;
        double var20 = var1.getZ();
        this.pz = var20;
        float var23 = var1.getYaw();
        this.pyaw = var23;
        this.potConfigured = true;
        this.save(1549144117);
    }

    public void clearPotionSpot(int var1) {
        this.potConfigured = false;
        this.potWorld = null;
        this.save(1549144117);
    }

    public void set(Location var1, int var2) {
        String var13 = var1.getWorld().getName();
        this.worldName = var13;
        double var15 = var1.getX();
        this.x = var15;
        double var18 = var1.getY();
        this.y = var18;
        double var21 = var1.getZ();
        this.z = var21;
        float var24 = var1.getYaw();
        this.yaw = var24;
        float var26 = var1.getPitch();
        this.pitch = var26;
        this.configured = true;
        this.save(1549144117);
    }

    public void clear(int var1) {
        this.configured = false;
        this.worldName = null;
        this.save(1549144117);
    }

    public void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var6 = YamlConfiguration.loadConfiguration(this.file);
            String var3 = "pending";
            String var4 = "";
            String var7 = var6.getString(var3, var4);
            if (var7 != null && !var7.isEmpty()) {
                try {
                    UUID var102 = UUID.fromString(var7);
                    this.pendingAscension = var102;
                } catch (IllegalArgumentException var231) {
                }
            }

            String var103 = "armor.world";
            String var202 = "";
            String var8 = var6.getString(var103, var202);
            if (var8 != null && !var8.isEmpty()) {
                this.armorWorld = var8;
                double var106 = var6.getDouble("armor.x");
                this.ax = var106;
                double var109 = var6.getDouble("armor.y");
                this.ay = var109;
                double var112 = var6.getDouble("armor.z");
                this.az = var112;
                float var117 = (float)var6.getDouble("armor.yaw");
                this.ayaw = var117;
                this.armorConfigured = true;
            }

            String var119 = "border.world";
            String var207 = "";
            String var9 = var6.getString(var119, var207);
            if (var9 != null && !var9.isEmpty()) {
                this.borderWorld = var9;
                double var122 = var6.getDouble("border.x");
                this.bx = var122;
                double var125 = var6.getDouble("border.y");
                this.by = var125;
                double var128 = var6.getDouble("border.z");
                this.bz = var128;
                float var133 = (float)var6.getDouble("border.yaw");
                this.byaw = var133;
                this.borderConfigured = true;
            }

            String var135 = "potions.world";
            String var212 = "";
            String var10 = var6.getString(var135, var212);
            if (var10 != null && !var10.isEmpty()) {
                this.potWorld = var10;
                double var138 = var6.getDouble("potions.x");
                this.px = var138;
                double var141 = var6.getDouble("potions.y");
                this.py = var141;
                double var144 = var6.getDouble("potions.z");
                this.pz = var144;
                float var149 = (float)var6.getDouble("potions.yaw");
                this.pyaw = var149;
                this.potConfigured = true;
            }

            String var217 = "session";
            boolean var152 = var6.getBoolean(var217, false);
            this.session = var152;
            String var153 = "returnHomePending";
            String var218 = "";
            String var11 = var6.getString(var153, var218);
            if (var11 != null && !var11.isEmpty()) {
                try {
                    UUID var155 = UUID.fromString(var11);
                    this.returnHomePending = var155;
                } catch (IllegalArgumentException var230) {
                }
            }

            String var156 = "shrine.world";
            String var219 = "";
            String var12 = var6.getString(var156, var219);
            if (var12 != null && !var12.isEmpty()) {
                this.shrineWorld = var12;
                int var159 = var6.getInt("shrine.x");
                this.sbx = var159;
                int var161 = var6.getInt("shrine.y");
                this.sby = var161;
                int var163 = var6.getInt("shrine.z");
                this.sbz = var163;
                String var165 = var6.getString("shrine.data");
                this.shrineData = var165;
            }

            String var166 = "enchants.world";
            String var224 = "";
            String var13 = var6.getString(var166, var224);
            if (var13 != null && !var13.isEmpty()) {
                this.enchWorld = var13;
                double var169 = var6.getDouble("enchants.x");
                this.ex = var169;
                double var172 = var6.getDouble("enchants.y");
                this.ey = var172;
                double var175 = var6.getDouble("enchants.z");
                this.ez = var175;
                float var180 = (float)var6.getDouble("enchants.yaw");
                this.eyaw = var180;
                this.enchConfigured = true;
            }

            String var182 = "world";
            String var229 = "";
            String var14 = var6.getString(var182, var229);
            if (var14 != null && !var14.isEmpty()) {
                this.worldName = var14;
                double var185 = var6.getDouble("x");
                this.x = var185;
                double var188 = var6.getDouble("y");
                this.y = var188;
                double var191 = var6.getDouble("z");
                this.z = var191;
                float var196 = (float)var6.getDouble("yaw");
                this.yaw = var196;
                float var200 = (float)var6.getDouble("pitch");
                this.pitch = var200;
                this.configured = true;
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        if (this.pendingAscension != null) {
            String var73 = "pending";
            String var113 = this.pendingAscension.toString();
            var2.set(var73, var113);
        }

        if (this.isConfigured(553762660)) {
            String var74 = "world";
            String var115 = this.worldName;
            var2.set(var74, var115);
            String var75 = "x";
            Double var119 = this.x;
            var2.set(var75, var119);
            String var76 = "y";
            Double var123 = this.y;
            var2.set(var76, var123);
            String var77 = "z";
            Double var127 = this.z;
            var2.set(var77, var127);
            String var78 = "yaw";
            Float var130 = this.yaw;
            var2.set(var78, var130);
            String var79 = "pitch";
            Float var133 = this.pitch;
            var2.set(var79, var133);
        }

        if (this.enchantConfigured(2144921445)) {
            String var80 = "enchants.world";
            String var135 = this.enchWorld;
            var2.set(var80, var135);
            String var81 = "enchants.x";
            Double var139 = this.ex;
            var2.set(var81, var139);
            String var82 = "enchants.y";
            Double var143 = this.ey;
            var2.set(var82, var143);
            String var83 = "enchants.z";
            Double var147 = this.ez;
            var2.set(var83, var147);
            String var84 = "enchants.yaw";
            Float var150 = this.eyaw;
            var2.set(var84, var150);
        }

        if (this.armorConfigured(1763373199)) {
            String var85 = "armor.world";
            String var152 = this.armorWorld;
            var2.set(var85, var152);
            String var86 = "armor.x";
            Double var156 = this.ax;
            var2.set(var86, var156);
            String var87 = "armor.y";
            Double var160 = this.ay;
            var2.set(var87, var160);
            String var88 = "armor.z";
            Double var164 = this.az;
            var2.set(var88, var164);
            String var89 = "armor.yaw";
            Float var167 = this.ayaw;
            var2.set(var89, var167);
        }

        if (this.borderConfigured(201221402)) {
            String var90 = "border.world";
            String var169 = this.borderWorld;
            var2.set(var90, var169);
            String var91 = "border.x";
            Double var173 = this.bx;
            var2.set(var91, var173);
            String var92 = "border.y";
            Double var177 = this.by;
            var2.set(var92, var177);
            String var93 = "border.z";
            Double var181 = this.bz;
            var2.set(var93, var181);
            String var94 = "border.yaw";
            Float var184 = this.byaw;
            var2.set(var94, var184);
        }

        if (this.potionConfigured(886107921)) {
            String var95 = "potions.world";
            String var186 = this.potWorld;
            var2.set(var95, var186);
            String var96 = "potions.x";
            Double var190 = this.px;
            var2.set(var96, var190);
            String var97 = "potions.y";
            Double var194 = this.py;
            var2.set(var97, var194);
            String var98 = "potions.z";
            Double var198 = this.pz;
            var2.set(var98, var198);
            String var99 = "potions.yaw";
            Float var201 = this.pyaw;
            var2.set(var99, var201);
        }

        if (this.session) {
            String var100 = "session";
            Boolean var203 = true;
            var2.set(var100, var203);
        }

        if (this.returnHomePending != null) {
            String var101 = "returnHomePending";
            String var206 = this.returnHomePending.toString();
            var2.set(var101, var206);
        }

        if (this.shrineWorld != null && this.shrineData != null) {
            String var102 = "shrine.world";
            String var208 = this.shrineWorld;
            var2.set(var102, var208);
            String var103 = "shrine.x";
            Integer var211 = this.sbx;
            var2.set(var103, var211);
            String var104 = "shrine.y";
            Integer var214 = this.sby;
            var2.set(var104, var214);
            String var105 = "shrine.z";
            Integer var217 = this.sbz;
            var2.set(var105, var217);
            String var106 = "shrine.data";
            String var219 = this.shrineData;
            var2.set(var106, var219);
        }

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var108 = this.file;
            var4.save(var108);
        } catch (IOException var5) {
            Logger var72 = this.plugin.getLogger();
            String var111 = "Failed to save controlroom.yml: " + var5.getMessage();
            var72.warning(var111);
        }
    }
}
