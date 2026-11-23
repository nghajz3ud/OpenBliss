package com.bliss.core;

import com.bliss.BlissPlugin;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public final class PlayerDataStore {
    private final BlissPlugin plugin;
    private final File file;
    private final Map<UUID, PlayerData> data;

    public PlayerDataStore(BlissPlugin var1, int var2) {
        ConcurrentHashMap var4 = new ConcurrentHashMap();
        this.data = var4;
        this.plugin = var1;
        File var12 = var1.getDataFolder();
        File var11 = new File(var12, "players.yml");
        this.file = var11;
    }

    public PlayerData get(UUID var1, int var2) {
        return this.data.computeIfAbsent(var1, var0 -> {
            PlayerData var1x = new PlayerData();
            var1x.setOwner(var0, 597350743);
            return var1x;
        });
    }

    public Map<UUID, PlayerData> all(int var1) {
        return this.data;
    }

    public void load(int var1) {
        if (this.file.exists()) {
            ConfigurationSection var7 = YamlConfiguration.loadConfiguration(this.file).getConfigurationSection("players");
            if (var7 != null) {
                for (String var9 : var7.getKeys(false)) {
                    UUID var10;
                    try {
                        var10 = UUID.fromString(var9);
                    } catch (IllegalArgumentException var64) {
                        continue;
                    }

                    PlayerData var31 = new PlayerData();
                    var31.setOwner(var10, 597350743);
                    String var52 = var9 + ".gem";
                    GemType var44 = GemType.byId(var7.getInt(var52, 0));
                    var31.setGem(var44, 2137390487);
                    String var54 = var9 + ".tier2";
                    boolean var46 = var7.getBoolean(var54, false);
                    var31.setTier2(var46, 1757992594);
                    String var56 = var9 + ".energy";
                    int var48 = var7.getInt(var56, 1);
                    var31.setEnergy(var48, 319922676);
                    String var58 = var9 + ".joined";
                    boolean var50 = var7.getBoolean(var58, false);
                    var31.setJoined(var50, 2028976592);
                    Map var38 = this.data;
                    var38.put(var10, var31);
                }
            }
        }
    }

    // $VF: Handled exception range with multiple entry points by splitting it
    // $VF: Inserted dummy exception handlers to handle obfuscated exceptions
    public boolean save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;

        for (Entry var6 : this.data.entrySet()) {
            String var7 = "players." + ((UUID)var6.getKey()).toString();
            String var47 = var7 + ".gem";
            Integer var58 = ((PlayerData)var6.getValue()).gem(1413186337).id();
            var4.set(var47, var58);
            String var49 = var7 + ".tier2";
            Boolean var63 = ((PlayerData)var6.getValue()).tier2(629984660);
            var4.set(var49, var63);
            String var51 = var7 + ".energy";
            Integer var68 = ((PlayerData)var6.getValue()).rawEnergy(1655845820);
            var4.set(var51, var68);
            String var53 = var7 + ".joined";
            Boolean var73 = ((PlayerData)var6.getValue()).joined(225029398);
            var4.set(var53, var73);
        }

        try {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            File var42 = this.file;
            AtomicYml.save(var4, var42, 1347473142);
            return true;
        } catch (Exception var74) {
            Logger var27 = this.plugin.getLogger();
            String var45 = "Failed to save players.yml: " + var74.getMessage();
            var27.warning(var45);
            return false;
        }
    }
}
