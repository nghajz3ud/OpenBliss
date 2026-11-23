package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffectType;

public final class PotionLimits {
    private final BlissPlugin plugin;
    private final File file;
    private final Map<String, Integer> caps;

    public PotionLimits(BlissPlugin var1, int var2) {
        HashMap var4 = new HashMap();
        this.caps = var4;
        this.plugin = var1;
        File var12 = var1.getDataFolder();
        File var11 = new File(var12, "potionlimits.yml");
        this.file = var11;
    }

    public int cap(PotionEffectType var1, int var2) {
        if (var1 == null) {
            return -1;
        } else {
            Map var8 = this.caps;
            String var14 = key(var1, 2081834481);
            Integer var5 = (Integer)var8.get(var14);
            int var13;
            if (var5 == null) {
                var13 = -1;
            } else {
                var13 = var5;
            }

            return var13;
        }
    }

    public boolean isCapped(PotionEffectType var1) {
        boolean var5;
        if (this.cap(var1, 1961378735) >= 0) {
            var5 = true;
        } else {
            var5 = false;
        }

        return var5;
    }

    public void setCap(PotionEffectType var1, int var2, int var3) {
        if (var1 != null) {
            if (var2 < 0) {
                Map var13 = this.caps;
                String var17 = key(var1, 2081834481);
                var13.remove(var17);
            } else {
                Map var9 = this.caps;
                String var15 = key(var1, 2081834481);
                Integer var18 = var2;
                var9.put(var15, var18);
            }

            this.save(1802186232);
        }
    }

    public void clear(PotionEffectType var1) {
        this.setCap(var1, -1, 1301205225);
    }

    private static String key(PotionEffectType var0, int var1) {
        NamespacedKey var4 = var0.getKey();
        String var7 = var4.getNamespace();
        String var9 = var4.getKey();
        return var7 + ":" + var9;
    }

    public void load(int var1) {
        this.caps.clear();
        if (this.file.isFile()) {
            YamlConfiguration var5 = YamlConfiguration.loadConfiguration(this.file);
            Iterator<String> var6 = var5.getKeys(false).iterator();

            while (var6.hasNext()) {
                String var7 = var6.next();
                String var8 = var5.getString(var7);
                if (var8 != null) {
                    try {
                        Map<String, Integer> var29 = this.caps;
                        Integer var41 = Integer.parseInt(var8.trim());
                        var29.put(var7, var41);
                    } catch (NumberFormatException var44) {
                        Logger var34 = this.plugin.getLogger();
                        String var38 = "potionlimits.yml: ignoring unreadable cap for " + var7;
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
            String var29 = (String)var6.getKey();
            String var31 = String.valueOf(var6.getValue());
            var4.set(var29, var31);
        }

        try {
            File var23 = this.file;
            AtomicYml.save(var4, var23, 1347473142);
        } catch (IOException var33) {
            Logger var17 = this.plugin.getLogger();
            String var26 = "Failed to save potionlimits.yml: " + var33.getMessage();
            var17.warning(var26);
        }
    }
}
