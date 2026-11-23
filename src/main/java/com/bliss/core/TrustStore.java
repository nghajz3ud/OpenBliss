package com.bliss.core;

import com.bliss.BlissPlugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public final class TrustStore {
    private final BlissPlugin plugin;
    private final File file;
    private final Map<UUID, Set<UUID>> trust;

    public TrustStore(BlissPlugin var1, int var2) {
        ConcurrentHashMap var4 = new ConcurrentHashMap();
        this.trust = var4;
        this.plugin = var1;
        File var12 = var1.getDataFolder();
        File var11 = new File(var12, "trust.yml");
        this.file = var11;
    }

    public boolean isTrusted(UUID var1, UUID var2, int var3) {
        if (var1.equals(var2)) {
            return true;
        } else {
            Map var10 = this.trust;
            Set var6 = (Set)var10.get(var1);
            boolean var16;
            if (var6 != null && var6.contains(var2)) {
                var16 = true;
            } else {
                var16 = false;
            }

            return var16;
        }
    }

    public boolean addTrust(UUID var1, UUID var2, int var3) {
        Map var7 = this.trust;
        Function var6 = var0 -> ConcurrentHashMap.newKeySet();
        Set var9 = (Set)var7.computeIfAbsent(var1, var6);
        return var9.add(var2);
    }

    public Set<UUID> trusted(UUID var1, int var2) {
        Set var12;
        if (var1 == null) {
            var12 = null;
        } else {
            Map var7 = this.trust;
            var12 = (Set)var7.get(var1);
        }

        Set var10;
        if (var12 == null) {
            var10 = Set.of();
        } else {
            var10 = Set.copyOf(var12);
        }

        return var10;
    }

    public int clearTrust(UUID var1, int var2) {
        Set var9;
        if (var1 == null) {
            var9 = null;
        } else {
            Map var7 = this.trust;
            var9 = (Set)var7.remove(var1);
        }

        int var12;
        if (var9 == null) {
            var12 = 0;
        } else {
            var12 = var9.size();
        }

        return var12;
    }

    public boolean removeTrust(UUID var1, UUID var2, int var3) {
        Map var7 = this.trust;
        Set var6 = (Set)var7.get(var1);
        boolean var13;
        if (var6 != null && var6.remove(var2)) {
            var13 = true;
        } else {
            var13 = false;
        }

        return var13;
    }

    public void load(int var1) {
        if (this.file.exists()) {
            ConfigurationSection var6 = YamlConfiguration.loadConfiguration(this.file).getConfigurationSection("trust");
            if (var6 != null) {
                for (String var8 : var6.getKeys(false)) {
                    UUID var9;
                    try {
                        var9 = UUID.fromString(var8);
                    } catch (IllegalArgumentException var58) {
                        continue;
                    }

                    Set<UUID> var55 = ConcurrentHashMap.newKeySet();
                    for (String var12 : var6.getStringList(var8)) {
                        try {
                            var55.add(UUID.fromString(var12));
                        } catch (IllegalArgumentException var59) {
                        }
                    }

                    if (!var55.isEmpty()) {
                        this.trust.put(var9, var55);
                    }
                }
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;

        for (Entry<UUID, Set<UUID>> var7 : this.trust.entrySet()) {
            HashSet<String> var20 = new HashSet<>();
            HashSet<String> var8 = var20;

            for (UUID var10 : var7.getValue()) {
                String var51 = var10.toString();
                var8.add(var51);
            }

            String var49 = "trust." + var7.getKey();
            ArrayList var3 = new ArrayList(var8);
            var5.set(var49, var3);
        }

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var53 = this.file;
            AtomicYml.save(var5, var53, 1347473142);
        } catch (IOException var57) {
            Logger var45 = this.plugin.getLogger();
            String var56 = "Failed to save trust.yml: " + var57.getMessage();
            var45.warning(var56);
        }
    }
}
