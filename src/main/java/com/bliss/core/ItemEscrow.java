package com.bliss.core;

import java.io.File;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class ItemEscrow {
    private final Plugin plugin;
    private final File file;
    private final Map<UUID, ItemStack[]> held;

    public ItemEscrow(Plugin var1, String var2, int var3) {
        ConcurrentHashMap var5 = new ConcurrentHashMap();
        this.held = var5;
        this.plugin = var1;
        File var14 = var1.getDataFolder();
        File var13 = new File(var14, var2);
        this.file = var13;
        this.load(119756577);
    }

    public boolean has(UUID var1, int var2) {
        Map var5 = this.held;
        return var5.containsKey(var1);
    }

    public Set<UUID> ids() {
        Set var4 = this.held.keySet();
        return new HashSet<>(var4);
    }

    public boolean hold(UUID var1, ItemStack[] var2, int var3) {
        Map<UUID, ItemStack[]> var8 = this.held;
        ItemStack[] var26;
        if (var2 == null) {
            var26 = new ItemStack[0];
        } else {
            var26 = var2;
        }

        ItemStack[] var7 = var8.put(var1, var26);
        if (this.save(556033496)) {
            return true;
        } else {
            if (var7 != null) {
                Map var15 = this.held;
                var15.put(var1, var7);
            } else {
                Map var19 = this.held;
                var19.remove(var1);
            }

            return false;
        }
    }

    public ItemStack[] release(UUID var1, int var2) {
        Map var6 = this.held;
        ItemStack[] var5 = (ItemStack[])var6.remove(var1);
        if (var5 != null) {
            this.save(556033496);
        }

        return var5;
    }

    private void load(int var1) {
        if (this.file.isFile()) {
            YamlConfiguration var6 = YamlConfiguration.loadConfiguration(this.file);

            for (String var8 : var6.getKeys(false)) {
                Exception var10000;
                label36: {
                    String var9;
                    try {
                        var9 = var6.getString(var8);
                        if (var9 == null || var9.isEmpty()) {
                            continue;
                        }
                    } catch (Exception var49) {
                        var10000 = var49;
                        break label36;
                    }

                    try {
                        Map var29 = this.held;
                        UUID var37 = UUID.fromString(var8);
                        Decoder var4 = Base64.getDecoder();
                        ItemStack[] var43 = ItemStack.deserializeItemsFromBytes(var4.decode(var9));
                        var29.put(var37, var43);
                        continue;
                    } catch (Exception var48) {
                        var10000 = var48;
                    }
                }

                Exception var47 = var10000;
                Logger var34 = this.plugin.getLogger();
                String var40 = this.file.getName();
                String var46 = var47.getMessage();
                String var41 = var40 + ": dropping unreadable escrow entry " + var8 + " (" + var46 + ")";
                var34.warning(var41);
            }
        }
    }

    private boolean save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var5 = var2;

        for (Entry var7 : this.held.entrySet()) {
            String var34 = ((UUID)var7.getKey()).toString();
            Encoder var36 = Base64.getEncoder();
            byte[] var40 = ItemStack.serializeItemsAsBytes((ItemStack[])var7.getValue());
            String var37 = var36.encodeToString(var40);
            var5.set(var34, var37);
        }

        try {
            File var26 = this.file;
            AtomicYml.save(var5, var26, 1347473142);
            return true;
        } catch (Exception var42) {
            Logger var19 = this.plugin.getLogger();
            String var29 = this.file.getName();
            String var35 = var42.getMessage();
            String var30 = "Could not save " + var29 + ": " + var35;
            var19.warning(var30);
            return false;
        }
    }
}
