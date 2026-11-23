package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import com.bliss.core.GlobalState;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

public final class EnchantLimits {
    private final BlissPlugin plugin;
    private final File file;
    private final Map<String, Integer> caps;
    private static EnchantLimits instance;

    public EnchantLimits(BlissPlugin var1, int var2) {
        HashMap var4 = new HashMap();
        this.caps = var4;
        this.plugin = var1;
        File var13 = var1.getDataFolder();
        File var12 = new File(var13, "enchantlimits.yml");
        this.file = var12;
        instance = this;
    }

    public static int allow(Enchantment var0, int var1, int var2) {
        EnchantLimits var5 = instance;
        if (var5 != null && var0 != null) {
            int var6 = var5.cap(var0, 1602405455);
            int var14;
            if (var6 < 0) {
                var14 = var1;
            } else {
                var14 = Math.min(var1, var6);
            }

            return var14;
        } else {
            return var1;
        }
    }

    public int cap(Enchantment var1, int var2) {
        if (var1 == null) {
            return -1;
        } else {
            GlobalState var22;
            if (this.plugin == null) {
                var22 = null;
            } else {
                var22 = this.plugin.globalState(724248306);
            }

            if (var22 != null && !var22.kitEnchantLimits(1706590402)) {
                return -1;
            } else {
                Map var14 = this.caps;
                String var24 = key(var1, 1365064520);
                Integer var6 = (Integer)var14.get(var24);
                int var19;
                if (var6 == null) {
                    var19 = -1;
                } else {
                    var19 = var6;
                }

                return var19;
            }
        }
    }

    public boolean isCapped(Enchantment var1) {
        boolean var5;
        if (this.cap(var1, 1602405455) >= 0) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public void setCap(Enchantment var1, int var2, int var3) {
        if (var1 != null) {
            if (var2 < 0) {
                Map var13 = this.caps;
                String var17 = key(var1, 1365064520);
                var13.remove(var17);
            } else {
                Map var9 = this.caps;
                String var15 = key(var1, 1365064520);
                Integer var18 = var2;
                var9.put(var15, var18);
            }

            this.save(701886830);
        }
    }

    public void clear(Enchantment var1) {
        this.setCap(var1, -1, 991827291);
    }

    private static String key(Enchantment var0, int var1) {
        NamespacedKey var4 = var0.getKey();
        String var7 = var4.getNamespace();
        String var9 = var4.getKey();
        return var7 + ":" + var9;
    }

    public void load(int var1) {
        this.caps.clear();
        if (this.file.isFile()) {
            YamlConfiguration var5 = YamlConfiguration.loadConfiguration(this.file);

            for (String var7 : var5.getKeys(false)) {
                String var8 = var5.getString(var7);
                if (var8 != null) {
                    try {
                        Map var29 = this.caps;
                        Integer var41 = Integer.parseInt(var8.trim());
                        var29.put(var7, var41);
                    } catch (NumberFormatException var42) {
                        Logger var34 = this.plugin.getLogger();
                        String var38 = "enchantlimits.yml: ignoring unreadable cap for " + var7;
                        var34.warning(var38);
                    }
                }
            }
        }
    }

    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;

        for (Entry var6 : this.caps.entrySet()) {
            String var24 = (String)var6.getKey();
            String var31 = String.valueOf(var6.getValue());
            var4.set(var24, var31);
        }

        try {
            File var26 = this.file;
            AtomicYml.save(var4, var26, 1347473142);
        } catch (IOException var33) {
            Logger var21 = this.plugin.getLogger();
            String var29 = "Failed to save enchantlimits.yml: " + var33.getMessage();
            var21.warning(var29);
        }
    }
}
