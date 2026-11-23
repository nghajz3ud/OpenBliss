package com.bliss.pedestal;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;

public final class PedestalState {
    public static final int MAX_DURABILITY = 3;
    public static final int DEPOSIT_TARGET = 5;
    private final BlissPlugin plugin;
    private final File file;
    private final PedestalLayout layout;
    private Location mainLoc;
    private Location subLoc;
    private final Map<Integer, Location> gilded;
    private final Map<Integer, Location> gold;
    private final Map<Integer, Location> rawGold;
    private final Map<Integer, Location> cherry;
    private boolean active;
    private boolean beacon;
    private PedestalState.Ritual ritual;
    private int count;
    private int durability;
    private int depositCooldown;
    private int breakCooldown;
    private UUID revivingPlayer;
    private boolean pedestalBusy;
    private final Set<UUID> revivalLocked;

    public PedestalState(BlissPlugin var1, int var2) {
        HashMap var4 = new HashMap();
        this.gilded = var4;
        HashMap var22 = new HashMap();
        this.gold = var22;
        HashMap var23 = new HashMap();
        this.rawGold = var23;
        HashMap var24 = new HashMap();
        this.cherry = var24;
        this.active = true;
        this.beacon = false;
        PedestalState.Ritual var27 = PedestalState.Ritual.NONE;
        this.ritual = var27;
        this.count = 0;
        this.durability = 3;
        this.depositCooldown = 0;
        this.breakCooldown = 0;
        HashSet var32 = new HashSet();
        this.revivalLocked = var32;
        this.plugin = var1;
        File var37 = var1.getDataFolder();
        File var34 = new File(var37, "pedestal.yml");
        this.file = var34;
        PedestalLayout var36 = PedestalLayout.load(var1, 1177219691);
        this.layout = var36;
    }

    public boolean isCustomLayoutBlock(Location var1, int var2) {
        if (this.layout != null) {
            PedestalLayout var8 = this.layout;
            Location var11 = this.mainLoc;
            if (var8.isOpenBlock(var11, var1, 74877065)) {
                return true;
            }
        }

        return false;
    }

    public Location mainLoc(int var1) {
        return this.mainLoc;
    }

    public void setMainLoc(Location var1) {
        Location var5;
        if (var1 == null) {
            var5 = null;
        } else {
            var5 = var1.clone();
        }

        this.mainLoc = var5;
    }

    public Location subLoc(int var1) {
        return this.subLoc;
    }

    public void setSubLoc(Location var1) {
        Location var5;
        if (var1 == null) {
            var5 = null;
        } else {
            var5 = var1.clone();
        }

        this.subLoc = var5;
    }

    public Location gilded(int var1, int var2) {
        Map var5 = this.gilded;
        Integer var8 = var1;
        return (Location)var5.get(var8);
    }

    public void setGilded(int var1, Location var2) {
        Map var6 = this.gilded;
        Integer var8 = var1;
        Location var10;
        if (var2 == null) {
            var10 = null;
        } else {
            var10 = var2.clone();
        }

        var6.put(var8, var10);
    }

    public Location gold(int var1, int var2) {
        Map var5 = this.gold;
        Integer var8 = var1;
        return (Location)var5.get(var8);
    }

    public void setGold(int var1, Location var2) {
        Map var6 = this.gold;
        Integer var8 = var1;
        Location var10;
        if (var2 == null) {
            var10 = null;
        } else {
            var10 = var2.clone();
        }

        var6.put(var8, var10);
    }

    public Location rawGold(int var1, int var2) {
        Map var5 = this.rawGold;
        Integer var8 = var1;
        return (Location)var5.get(var8);
    }

    public void setRawGold(int var1, Location var2) {
        Map var6 = this.rawGold;
        Integer var8 = var1;
        Location var9;
        if (var2 == null) {
            var9 = null;
        } else {
            var9 = var2.clone();
        }

        var6.put(var8, var9);
    }

    public Location cherry(int var1, int var2) {
        Map var5 = this.cherry;
        Integer var8 = var1;
        return (Location)var5.get(var8);
    }

    public void setCherry(int var1, Location var2) {
        Map var6 = this.cherry;
        Integer var8 = var1;
        Location var10;
        if (var2 == null) {
            var10 = null;
        } else {
            var10 = var2.clone();
        }

        var6.put(var8, var10);
    }

    public boolean active(int var1) {
        return this.active;
    }

    public void setActive(boolean var1, int var2) {
        this.active = var1;
        this.save(973788971);
    }

    public boolean beacon(int var1) {
        return this.beacon;
    }

    public void setBeacon(boolean var1, int var2) {
        this.beacon = var1;
    }

    public PedestalState.Ritual ritual(int var1) {
        return this.ritual;
    }

    public void setRitual(PedestalState.Ritual var1, int var2) {
        this.ritual = var1;
        this.save(973788971);
    }

    public int count(int var1) {
        return this.count;
    }

    public void incrementCount(int var1) {
        int var6 = this.count + 1;
        this.count = var6;
        this.save(973788971);
    }

    public void resetCount(int var1) {
        this.count = 0;
        this.save(973788971);
    }

    public int durability(int var1) {
        return this.durability;
    }

    public void setDurability(int var1, int var2) {
        this.durability = var1;
        this.save(973788971);
    }

    public void decrementDurability(int var1) {
        int var6 = this.durability - 1;
        this.durability = var6;
        this.save(973788971);
    }

    public int depositCooldown(int var1) {
        return this.depositCooldown;
    }

    public void setDepositCooldown(int var1, int var2) {
        this.depositCooldown = var1;
    }

    public int breakCooldown(int var1) {
        return this.breakCooldown;
    }

    public void setBreakCooldown(int var1, int var2) {
        this.breakCooldown = var1;
    }

    public UUID revivingPlayer(int var1) {
        return this.revivingPlayer;
    }

    public void setRevivingPlayer(UUID var1, int var2) {
        this.revivingPlayer = var1;
        this.save(973788971);
    }

    public boolean pedestalBusy(int var1) {
        return this.pedestalBusy;
    }

    public void setPedestalBusy(boolean var1, int var2) {
        this.pedestalBusy = var1;
    }

    public boolean revivalLocked(UUID var1, int var2) {
        Set var5 = this.revivalLocked;
        return var5.contains(var1);
    }

    public void setRevivalLocked(UUID var1, boolean var2, int var3) {
        if (var2) {
            Set var7 = this.revivalLocked;
            var7.add(var1);
        } else {
            Set var10 = this.revivalLocked;
            var10.remove(var1);
        }
    }

    public Location pedestalLoc1(int var1) {
        return this.mainLoc == null ? null : this.mainLoc.clone().add(0.0, 1.0, 0.0);
    }

    public List<Location> structureBlocks(int var1) {
        ArrayList var2 = new ArrayList();
        ArrayList var6 = var2;
        if (this.mainLoc == null) {
            return var2;
        } else if (this.layout != null && this.layout.hasClosed(958350633)) {
            for (PedestalLayout.Block var8 : this.layout.closed(2053576313)) {
                if (var8.dx() != 0 || var8.dz() != 0 || var8.dy() != 0 && var8.dy() != -1) {
                    Location var65 = this.mainLoc.clone();
                    double var98 = (double)var8.dx();
                    double var109 = (double)var8.dy();
                    double var112 = (double)var8.dz();
                    Location var66 = var65.add(var98, var109, var112);
                    var6.add(var66);
                }
            }

            return var6;
        } else {
            for (int var114 = 1; var114 <= 4; var114++) {
                Map var40 = this.gilded;
                Integer var69 = var114;
                if (var40.get(var69) != null) {
                    Map var71 = this.gilded;
                    Integer var101 = var114;
                    Location var74 = ((Location)var71.get(var101)).clone();
                    var6.add(var74);
                }

                Map var45 = this.gold;
                Integer var76 = var114;
                if (var45.get(var76) != null) {
                    Map var78 = this.gold;
                    Integer var103 = var114;
                    Location var81 = ((Location)var78.get(var103)).clone();
                    var6.add(var81);
                }

                Map var50 = this.cherry;
                Integer var83 = var114;
                if (var50.get(var83) != null) {
                    Map var85 = this.cherry;
                    Integer var105 = var114;
                    Location var88 = ((Location)var85.get(var105)).clone();
                    var6.add(var88);
                }
            }

            for (int var115 = 1; var115 <= 8; var115++) {
                Map var58 = this.rawGold;
                Integer var91 = var115;
                if (var58.get(var91) != null) {
                    Map var93 = this.rawGold;
                    Integer var107 = var115;
                    Location var96 = ((Location)var93.get(var107)).clone();
                    var6.add(var96);
                }
            }

            return var6;
        }
    }

    public void resetRitual(int var1) {
        PedestalState.Ritual var3 = PedestalState.Ritual.NONE;
        this.ritual = var3;
        this.count = 0;
        this.depositCooldown = 0;
        this.breakCooldown = 0;
        this.durability = 3;
        this.revivingPlayer = null;
        this.pedestalBusy = false;
        this.revivalLocked.clear();
        PedestalRitualRunner.invalidateSteps(793294860);
        this.save(973788971);
    }

    public void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var6 = YamlConfiguration.loadConfiguration(this.file);
            Location var66;
            if (var6.contains("mainLoc")) {
                var66 = var6.getLocation("mainLoc");
            } else {
                var66 = null;
            }

            if (var66 != null && var66.getWorld() == null) {
                this.plugin.getLogger().warning("pedestal.yml points at a world that no longer exists — ignoring it (fixed-pedestal will rebuild if enabled).");
            } else {
                if (var66 != null) {
                    this.mainLoc = var66;
                }

                if (var6.contains("subLoc")) {
                    Location var72 = var6.getLocation("subLoc");
                    this.subLoc = var72;
                }

                for (int var8 = 1; var8 <= 4; var8++) {
                    String var75 = "gilded." + var8;
                    if (var6.contains(var75)) {
                        Map var34 = this.gilded;
                        Integer var77 = var8;
                        String var120 = "gilded." + var8;
                        Location var108 = var6.getLocation(var120);
                        var34.put(var77, var108);
                    }

                    String var79 = "gold." + var8;
                    if (var6.contains(var79)) {
                        Map var39 = this.gold;
                        Integer var81 = var8;
                        String var122 = "gold." + var8;
                        Location var110 = var6.getLocation(var122);
                        var39.put(var81, var110);
                    }

                    String var83 = "cherry." + var8;
                    if (var6.contains(var83)) {
                        Map var44 = this.cherry;
                        Integer var85 = var8;
                        String var124 = "cherry." + var8;
                        Location var112 = var6.getLocation(var124);
                        var44.put(var85, var112);
                    }
                }

                for (int var132 = 1; var132 <= 8; var132 += 1) {
                    String var104 = "rawGold." + var132;
                    if (var6.contains(var104)) {
                        Map var64 = this.rawGold;
                        Integer var106 = var132;
                        String var131 = "rawGold." + var132;
                        Location var119 = var6.getLocation(var131);
                        var64.put(var106, var119);
                    }
                }

                String var113 = "beacon";
                boolean var88 = var6.getBoolean(var113, false);
                this.beacon = var88;
                String var114 = "active";
                boolean var90 = var6.getBoolean(var114, true);
                this.active = var90;

                try {
                    String var115 = "ritual";
                    String var127 = "NONE";
                    PedestalState.Ritual var93 = PedestalState.Ritual.valueOf(var6.getString(var115, var127));
                    this.ritual = var93;
                } catch (IllegalArgumentException var135) {
                    PedestalState.Ritual var102 = PedestalState.Ritual.NONE;
                    this.ritual = var102;
                }

                String var116 = "count";
                int var95 = var6.getInt(var116, 0);
                this.count = var95;
                String var117 = "durability";
                int var97 = var6.getInt(var117, 3);
                this.durability = var97;
                String var133 = var6.getString("revivingPlayer");
                if (var133 != null) {
                    try {
                        UUID var100 = UUID.fromString(var133);
                        this.revivingPlayer = var100;
                    } catch (IllegalArgumentException var134) {
                        this.revivingPlayer = null;
                    }
                }
            }
        }
    }

    public boolean applyFixedConfig(FileConfiguration var1, int var2) {
        if (var1 != null) {
            String var4 = "fixed-pedestal.enabled";
            if (var1.getBoolean(var4, false)) {
                if (this.mainLoc != null && this.mainLoc.getWorld() != null) {
                    this.plugin.getLogger().info("fixed-pedestal: keeping the saved location from pedestal.yml (fixed coords are a fallback only).");
                    return false;
                }

                this.mainLoc = null;
                String var64 = "fixed-pedestal.world";
                String var80 = "world";
                String var10 = var1.getString(var64, var80);
                World var11 = Bukkit.getWorld(var10);
                if (var11 == null) {
                    Logger var56 = this.plugin.getLogger();
                    String var78 = "fixed-pedestal: world '" + var10 + "' is not loaded — pedestal NOT built. Check the 'world' name in config.yml.";
                    var56.warning(var78);
                    return false;
                }

                if (var1.isSet("fixed-pedestal.x") && var1.isSet("fixed-pedestal.y") && var1.isSet("fixed-pedestal.z")) {
                    String var68 = "fixed-pedestal.x";
                    int var12 = var1.getInt(var68, 0);
                    String var69 = "fixed-pedestal.y";
                    int var13 = var1.getInt(var69, 0);
                    String var70 = "fixed-pedestal.z";
                    int var14 = var1.getInt(var70, 0);
                    double var86 = (double)var12;
                    double var89 = (double)var13;
                    double var91 = (double)var14;
                    Location var71 = new Location(var11, var86, var89, var91);
                    this.configureFromBeaconCenter(var71, 528121948);
                    this.setActive(true, 1028669057);
                    this.setBeacon(false, 116258295);
                    this.resetRitual(1772872156);
                    this.buildStructure(1721962847);
                    this.save(973788971);
                    Logger var48 = this.plugin.getLogger();
                    String var75 = "fixed-pedestal: built at " + var10 + " " + var12 + "," + var13 + "," + var14 + " (fallback — no pedestal was saved).";
                    var48.info(var75);
                    return true;
                }

                this.plugin.getLogger().warning("fixed-pedestal: enabled but x/y/z are not all set — pedestal NOT built.");
                return false;
            }
        }

        return false;
    }

    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        if (this.mainLoc != null) {
            String var66 = "mainLoc";
            Location var95 = this.mainLoc;
            var2.set(var66, var95);
        }

        if (this.subLoc != null) {
            String var67 = "subLoc";
            Location var97 = this.subLoc;
            var2.set(var67, var97);
        }

        for (Entry var6 : this.gilded.entrySet()) {
            String var71 = "gilded." + var6.getKey();
            Object var99 = var6.getValue();
            var4.set(var71, var99);
        }

        for (Entry var130 : this.gold.entrySet()) {
            String var94 = "gold." + var130.getKey();
            Object var123 = var130.getValue();
            var4.set(var94, var123);
        }

        for (Entry var129 : this.rawGold.entrySet()) {
            String var90 = "rawGold." + var129.getKey();
            Object var121 = var129.getValue();
            var4.set(var90, var121);
        }

        for (Entry var128 : this.cherry.entrySet()) {
            String var86 = "cherry." + var128.getKey();
            Object var119 = var128.getValue();
            var4.set(var86, var119);
        }

        String var72 = "beacon";
        Boolean var102 = this.beacon;
        var4.set(var72, var102);
        String var73 = "active";
        Boolean var105 = this.active;
        var4.set(var73, var105);
        String var74 = "ritual";
        String var108 = this.ritual.name();
        var4.set(var74, var108);
        String var75 = "count";
        Integer var111 = this.count;
        var4.set(var75, var111);
        String var76 = "durability";
        Integer var114 = this.durability;
        var4.set(var76, var114);
        if (this.revivingPlayer != null) {
            String var77 = "revivingPlayer";
            String var117 = this.revivingPlayer.toString();
            var4.set(var77, var117);
        }

        try {
            File var79 = this.file;
            AtomicYml.save(var4, var79, 1347473142);
        } catch (Exception var131) {
            Logger var53 = this.plugin.getLogger();
            String var82 = "Failed to save pedestal.yml: " + var131.getMessage();
            var53.warning(var82);
        }
    }

    public void configureFromCenter(Location var1, int var2) {
        World var8 = var1.getWorld();
        double var15 = (double)var1.getBlockX();
        double var18 = (double)var1.getBlockY();
        double var21 = (double)var1.getBlockZ();
        Location var11 = new Location(var8, var15, var18, var21);
        this.configureFromBeaconCenter(var11, 528121948);
    }

    public void configureFromBeaconCenter(Location var1, int var2) {
        World var12 = var1.getWorld();
        double var186 = (double)var1.getBlockX();
        double var229 = (double)var1.getBlockY();
        double var272 = (double)var1.getBlockZ();
        Location var15 = new Location(var12, var186, var229, var272);
        Location var79 = var15.clone();
        this.mainLoc = var79;
        Location var82 = var15.clone().add(0.0, 1.0, 0.0);
        this.subLoc = var82;
        Map var19 = this.gilded;
        Integer var84 = 1;
        Location var127 = var15.clone().add(0.0, 0.0, -1.0);
        var19.put(var84, var127);
        Map var22 = this.gilded;
        Integer var86 = 2;
        Location var130 = var15.clone().add(0.0, 0.0, 1.0);
        var22.put(var86, var130);
        Map var25 = this.gilded;
        Integer var88 = 3;
        Location var133 = var15.clone().add(1.0, 0.0, 0.0);
        var25.put(var88, var133);
        Map var28 = this.gilded;
        Integer var90 = 4;
        Location var136 = var15.clone().add(-1.0, 0.0, 0.0);
        var28.put(var90, var136);
        Map var31 = this.gold;
        Integer var92 = 1;
        Location var139 = var15.clone().add(1.0, 0.0, -1.0);
        var31.put(var92, var139);
        Map var34 = this.gold;
        Integer var94 = 2;
        Location var142 = var15.clone().add(1.0, 0.0, 1.0);
        var34.put(var94, var142);
        Map var37 = this.gold;
        Integer var96 = 3;
        Location var145 = var15.clone().add(-1.0, 0.0, -1.0);
        var37.put(var96, var145);
        Map var40 = this.gold;
        Integer var98 = 4;
        Location var148 = var15.clone().add(-1.0, 0.0, 1.0);
        var40.put(var98, var148);
        Map var43 = this.rawGold;
        Integer var100 = 1;
        Location var151 = var15.clone().add(-1.0, 0.0, -2.0);
        var43.put(var100, var151);
        Map var46 = this.rawGold;
        Integer var102 = 2;
        Location var154 = var15.clone().add(1.0, 0.0, -2.0);
        var46.put(var102, var154);
        Map var49 = this.rawGold;
        Integer var104 = 3;
        Location var157 = var15.clone().add(-2.0, 0.0, 1.0);
        var49.put(var104, var157);
        Map var52 = this.rawGold;
        Integer var106 = 4;
        Location var160 = var15.clone().add(-2.0, 0.0, -1.0);
        var52.put(var106, var160);
        Map var55 = this.rawGold;
        Integer var108 = 5;
        Location var163 = var15.clone().add(1.0, 0.0, 2.0);
        var55.put(var108, var163);
        Map var58 = this.rawGold;
        Integer var110 = 6;
        Location var166 = var15.clone().add(-1.0, 0.0, 2.0);
        var58.put(var110, var166);
        Map var61 = this.rawGold;
        Integer var112 = 7;
        Location var169 = var15.clone().add(2.0, 0.0, -1.0);
        var61.put(var112, var169);
        Map var64 = this.rawGold;
        Integer var114 = 8;
        Location var172 = var15.clone().add(2.0, 0.0, 1.0);
        var64.put(var114, var172);
        Map var67 = this.cherry;
        Integer var116 = 1;
        Location var175 = var15.clone().add(2.0, 0.0, 0.0);
        var67.put(var116, var175);
        Map var70 = this.cherry;
        Integer var118 = 2;
        Location var178 = var15.clone().add(-2.0, 0.0, 0.0);
        var70.put(var118, var178);
        Map var73 = this.cherry;
        Integer var120 = 3;
        Location var181 = var15.clone().add(0.0, 0.0, -2.0);
        var73.put(var120, var181);
        Map var76 = this.cherry;
        Integer var122 = 4;
        Location var184 = var15.clone().add(0.0, 0.0, 2.0);
        var76.put(var122, var184);
    }

    public void buildStructure(int var1) {
        if (this.mainLoc != null) {
            if (this.layout != null && this.layout.hasOpen(937315278)) {
                for (PedestalLayout.Block var11 : this.layout.open(338737121)) {
                    Location var43 = this.mainLoc.clone();
                    double var98 = (double)var11.dx();
                    double var127 = (double)var11.dy();
                    double var135 = (double)var11.dz();
                    Block var45 = var43.add(var98, var127, var135).getBlock();
                    BlockData var101 = var11.data();
                    var45.setBlockData(var101);
                }

                Block var28 = this.mainLoc.getBlock();
                Material var3 = Material.BEACON;
                var28.setType(var3);
                Block var33 = this.mainLoc.clone().add(0.0, -1.0, 0.0).getBlock();
                Material var92 = Material.ANCIENT_DEBRIS;
                var33.setType(var92);
                this.beacon = false;
                World var37 = this.mainLoc.getWorld();
                Location var95 = this.mainLoc;
                Sound var4 = Sound.BLOCK_BEACON_AMBIENT;
                var37.playSound(var95, var4, 40.0F, 1.6F);
            } else {
                Block var48 = this.mainLoc.getBlock();
                Material var102 = Material.BEACON;
                var48.setType(var102);
                Block var53 = this.mainLoc.clone().add(0.0, -1.0, 0.0).getBlock();
                Material var105 = Material.ANCIENT_DEBRIS;
                var53.setType(var105);

                for (byte var139 = 1; var139 <= 4; var139 += 1) {
                    Map var57 = this.gilded;
                    Integer var108 = Integer.valueOf(var139);
                    Location var141 = (Location)var57.get(var108);
                    if (var141 != null) {
                        Block var62 = var141.getBlock();
                        Material var109 = Material.GILDED_BLACKSTONE;
                        var62.setType(var109);
                    }

                    Map var64 = this.gold;
                    Integer var111 = Integer.valueOf(var139);
                    Location var12 = (Location)var64.get(var111);
                    if (var12 != null) {
                        Block var69 = var12.getBlock();
                        Material var112 = Material.GOLD_BLOCK;
                        var69.setType(var112);
                    }

                    Map var71 = this.cherry;
                    Integer var114 = Integer.valueOf(var139);
                    Location var13 = (Location)var71.get(var114);
                    if (var13 != null) {
                        Block var76 = var13.getBlock();
                        Material var115 = Material.CHERRY_LOG;
                        var76.setType(var115);
                    }
                }

                for (byte var140 = 1; var140 <= 8; var140 += 1) {
                    Map var80 = this.rawGold;
                    Integer var118 = Integer.valueOf(var140);
                    Location var142 = (Location)var80.get(var118);
                    if (var142 != null) {
                        Block var85 = var142.getBlock();
                        Material var119 = Material.RAW_GOLD_BLOCK;
                        var85.setType(var119);
                    }
                }

                this.beacon = false;
                World var89 = this.mainLoc.getWorld();
                Location var122 = this.mainLoc;
                Sound var123 = Sound.BLOCK_BEACON_AMBIENT;
                var89.playSound(var122, var123, 40.0F, 1.6F);
            }
        }
    }

    public void unbuildStructure(int var1) {
        if (this.mainLoc != null) {
            if (this.layout != null && (this.layout.hasClosed(958350633) || this.layout.hasOpen(937315278))) {
                if (this.layout.hasOpen(937315278)) {
                    for (PedestalLayout.Block var181 : this.layout.open(338737121)) {
                        Location var82 = this.mainLoc.clone();
                        double var137 = (double)var181.dx();
                        double var157 = (double)var181.dy();
                        double var169 = (double)var181.dz();
                        Block var84 = var82.add(var137, var157, var169).getBlock();
                        Material var139 = Material.AIR;
                        var84.setType(var139);
                    }
                }

                if (this.layout.hasClosed(958350633)) {
                    for (PedestalLayout.Block var182 : this.layout.closed(2053576313)) {
                        Location var112 = this.mainLoc.clone();
                        double var149 = (double)var182.dx();
                        double var164 = (double)var182.dy();
                        double var175 = (double)var182.dz();
                        Block var114 = var112.add(var149, var164, var175).getBlock();
                        BlockData var152 = var182.data();
                        var114.setBlockData(var152);
                    }
                }

                Block var96 = this.mainLoc.getBlock();
                Material var140 = Material.BEDROCK;
                var96.setType(var140);
                Block var101 = this.mainLoc.clone().add(0.0, -1.0, 0.0).getBlock();
                Material var143 = Material.BEDROCK;
                var101.setType(var143);
                this.beacon = false;
                this.removeBeam(1137327363);
                World var106 = this.mainLoc.getWorld();
                Location var146 = this.mainLoc;
                Sound var153 = Sound.BLOCK_BEACON_DEACTIVATE;
                var106.playSound(var146, var153, 20.0F, 2.0F);
            } else {
                Block var25 = this.mainLoc.getBlock();
                Material var3 = Material.BEDROCK;
                var25.setType(var3);
                Block var30 = this.mainLoc.clone().add(0.0, -1.0, 0.0).getBlock();
                Material var117 = Material.BEDROCK;
                var30.setType(var117);

                for (int var10 = 1; var10 <= 4; var10++) {
                    Map var34 = this.gilded;
                    Integer var120 = var10;
                    Location var11 = (Location)var34.get(var120);
                    if (var11 != null) {
                        Block var39 = var11.getBlock();
                        Material var121 = Material.BLACKSTONE;
                        var39.setType(var121);
                    }

                    Map var41 = this.gold;
                    Integer var123 = var10;
                    Location var12 = (Location)var41.get(var123);
                    if (var12 != null) {
                        Block var46 = var12.getBlock();
                        Material var124 = Material.NETHERITE_BLOCK;
                        var46.setType(var124);
                    }

                    Map var48 = this.cherry;
                    Integer var126 = var10;
                    Location var13 = (Location)var48.get(var126);
                    if (var13 != null) {
                        Block var53 = var13.getBlock();
                        Material var127 = Material.POLISHED_BASALT;
                        var53.setType(var127);
                    }
                }

                for (byte var177 = 1; var177 <= 8; var177 += 1) {
                    Map var57 = this.rawGold;
                    Integer var130 = Integer.valueOf(var177);
                    Location var180 = (Location)var57.get(var130);
                    if (var180 != null) {
                        Block var62 = var180.getBlock();
                        Material var131 = Material.BASALT;
                        var62.setType(var131);
                    }
                }

                this.beacon = false;
                this.removeBeam(1137327363);
                World var67 = this.mainLoc.getWorld();
                Location var134 = this.mainLoc;
                Sound var4 = Sound.BLOCK_BEACON_DEACTIVATE;
                var67.playSound(var134, var4, 20.0F, 2.0F);
            }
        }
    }

    public Location ancientDebrisLoc(int var1) {
        Location var13;
        if (this.mainLoc == null) {
            var13 = null;
        } else {
            var13 = this.mainLoc.clone().add(0.0, -1.0, 0.0);
        }

        return var13;
    }

    public void removeBeam(int var1) {
        if (this.mainLoc != null) {
            World var10;
            try {
                var10 = this.mainLoc.getWorld();
            } catch (IllegalArgumentException var36) {
                return;
            }

            if (var10 != null) {
                Location var33 = this.mainLoc;

                for (Entity var12 : var10.getNearbyEntities(var33, 16.0, 512.0, 16.0)) {
                    if (var12 instanceof EnderCrystal && var12.getScoreboardTags().contains("BlissPedestalCrystal")) {
                        var12.remove();
                    }
                }
            }
        }
    }

    public void summonBeam(int var1) {
        if (this.mainLoc != null) {
            World var16 = this.mainLoc.getWorld();
            double var17 = (double)this.mainLoc.getBlockX() + 0.5;
            double var19 = (double)this.mainLoc.getBlockZ() + 0.5;
            Location var59 = this.mainLoc;

            for (Entity var78 : var16.getNearbyEntities(var59, 16.0, 512.0, 16.0)) {
                if (var78 instanceof EnderCrystal && var78.getScoreboardTags().contains("BlissPedestalCrystal")) {
                    var78.remove();
                }
            }

            Location var46 = new Location(var16, var17, 319.0, var19);
            Location var47 = new Location(var16, var17, 50.0, var19);
            Consumer<EnderCrystal> var72 = var1x -> {
                var1x.setShowingBottom(false);
                var1x.setBeamTarget(var47);
                var1x.getScoreboardTags().add("BlissPedestalCrystal");
            };
            var16.spawn(var46, EnderCrystal.class, var72);
        }
    }

    public static enum Ritual {
        NONE,
        REPAIR,
        REVIVE;
    }
}
