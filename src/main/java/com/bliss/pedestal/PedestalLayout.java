package com.bliss.pedestal;

import com.bliss.BlissPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.bukkit.configuration.file.YamlConfiguration;

public final class PedestalLayout {
    private final List<PedestalLayout.Block> open;
    private final List<PedestalLayout.Block> closed;

    private PedestalLayout(List<PedestalLayout.Block> var1, List<PedestalLayout.Block> var2, int var3) {
        this.open = var1;
        this.closed = var2;
    }

    public List<PedestalLayout.Block> open(int var1) {
        return this.open;
    }

    public List<PedestalLayout.Block> closed(int var1) {
        return this.closed;
    }

    public boolean hasOpen(int var1) {
        boolean var5;
        if (!this.open.isEmpty()) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public boolean hasClosed(int var1) {
        boolean var5;
        if (!this.closed.isEmpty()) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static PedestalLayout load(BlissPlugin blissPlugin, int n) {
        try (InputStream inputStream = blissPlugin.getResource("pedestal_layout.yml")) {
            if (inputStream == null) {
                return null;
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(inputStreamReader);
            List<PedestalLayout.Block> list2 = PedestalLayout.parseSection(blissPlugin, yamlConfiguration.getConfigurationSection("open"), 1235169004);
            List<PedestalLayout.Block> list = PedestalLayout.parseSection(blissPlugin, yamlConfiguration.getConfigurationSection("closed"), 1235169004);
            if (list2.isEmpty() && list.isEmpty()) {
                return null;
            }
            Logger logger = blissPlugin.getLogger();
            logger.info("pedestal_layout.yml loaded (" + list2.size() + " open, " + list.size() + " closed blocks).");
            return new PedestalLayout(list2, list, 1668099881);
        } catch (Exception exception) {
            Logger logger = blissPlugin.getLogger();
            logger.warning("pedestal_layout.yml failed to load: " + exception.getMessage());
            return null;
        }
    }

    private static List<PedestalLayout.Block> parseSection(BlissPlugin var0, ConfigurationSection var1, int var2) {
        ArrayList var3 = new ArrayList();
        ArrayList var9 = var3;
        if (var1 == null) {
            return var3;
        } else {
            for (String var11 : var1.getKeys(false)) {
                String[] var12 = var11.split(",");
                if (var12.length != 3) {
                    Logger var40 = var0.getLogger();
                    String var52 = "pedestal_layout: bad offset key '" + var11 + "'";
                    var40.warning(var52);
                } else {
                    String var4 = "";
                    String var13 = var1.getString(var11, var4);
                    BlockData var14 = parseBlockData(var0, var13, var11, 577255691);
                    if (var14 != null) {
                        try {
                            int var56 = Integer.parseInt(var12[0].trim());
                            int var60 = Integer.parseInt(var12[1].trim());
                            int var64 = Integer.parseInt(var12[2].trim());
                            PedestalLayout.Block var48 = new PedestalLayout.Block(var56, var60, var64, var14);
                            var9.add(var48);
                        } catch (NumberFormatException var66) {
                            Logger var38 = var0.getLogger();
                            String var50 = "pedestal_layout: non-integer offset '" + var11 + "'";
                            var38.warning(var50);
                        }
                    }
                }
            }

            return var9;
        }
    }

    private static BlockData parseBlockData(BlissPlugin var0, String var1, String var2, int var3) {
        if (var1 != null && !var1.isEmpty()) {
            try {
                return Bukkit.createBlockData(var1);
            } catch (IllegalArgumentException var30) {
                Material var8 = Material.matchMaterial(var1);
                if (var8 != null && var8.isBlock()) {
                    return var8.createBlockData();
                } else {
                    Logger var22 = var0.getLogger();
                    String var27 = "pedestal_layout: bad block '" + var1 + "' at '" + var2 + "'";
                    var22.warning(var27);
                    return null;
                }
            }
        } else {
            Logger var25 = var0.getLogger();
            String var29 = "pedestal_layout: empty value at '" + var2 + "'";
            var25.warning(var29);
            return null;
        }
    }

    public boolean isOpenBlock(Location var1, Location var2, int var3) {
        if (var1 != null && var2 != null && var1.getWorld() != null) {
            World var15 = var1.getWorld();
            World var43 = var2.getWorld();
            if (var15.equals(var43)) {
                int var19 = var2.getBlockX();
                int var45 = var1.getBlockX();
                int var6 = var19 - var45;
                int var22 = var2.getBlockY();
                int var47 = var1.getBlockY();
                int var7 = var22 - var47;
                int var25 = var2.getBlockZ();
                int var49 = var1.getBlockZ();
                int var8 = var25 - var49;

                for (PedestalLayout.Block var10 : this.open) {
                    int var37 = var10.dx();
                    if (var37 == var6) {
                        int var39 = var10.dy();
                        if (var39 == var7) {
                            int var41 = var10.dz();
                            if (var41 == var8) {
                                return true;
                            }
                        }
                    }
                }

                return false;
            }
        }

        return false;
    }

    public static record Block(int dx, int dy, int dz, BlockData data) {
    }
}
