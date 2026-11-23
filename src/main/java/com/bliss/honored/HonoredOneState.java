package com.bliss.honored;

import com.bliss.BlissPlugin;
import com.bliss.core.AtomicYml;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

public final class HonoredOneState {
    private final BlissPlugin plugin;
    private final File file;
    private UUID current;
    private long trialEndsAt;

    public HonoredOneState(BlissPlugin var1, int var2) {
        this.plugin = var1;
        File var10 = var1.getDataFolder();
        File var9 = new File(var10, "honored.yml");
        this.file = var9;
    }

    public UUID current(int var1) {
        return this.current;
    }

    public boolean isHonoredOne(UUID var1, int var2) {
        if (var1 != null) {
            UUID var8 = this.current;
            if (var1.equals(var8)) {
                return true;
            }
        }

        return false;
    }

    public long trialEndsAt(int var1) {
        return this.trialEndsAt;
    }

    public void setTrialEndsAt(long var1, int var3) {
        this.trialEndsAt = var1;
        this.save(166330836);
    }

    public void set(UUID var1, int var2) {
        this.current = var1;
        this.save(166330836);
    }

    public void clear(int var1) {
        this.current = null;
        this.trialEndsAt = 0L;
        this.save(166330836);
    }

    public void load(int var1) {
        if (this.file.exists()) {
            YamlConfiguration var7 = YamlConfiguration.loadConfiguration(this.file);
            String var8 = var7.getString("current");
            if (var8 != null && !var8.isEmpty()) {
                try {
                    UUID var24 = UUID.fromString(var8);
                    this.current = var24;
                } catch (IllegalArgumentException var28) {
                }
            }

            String var4 = "trialEndsAt";
            long var26 = var7.getLong(var4, 0L);
            this.trialEndsAt = var26;
        }
    }

    public void save(int var1) {
        YamlConfiguration var2 = new YamlConfiguration();
        YamlConfiguration var4 = var2;
        if (this.current != null) {
            String var19 = "current";
            String var27 = this.current.toString();
            var2.set(var19, var27);
        }

        if (this.trialEndsAt > 0L) {
            String var20 = "trialEndsAt";
            Long var33 = this.trialEndsAt;
            var2.set(var20, var33);
        }

        try {
            File var22 = this.file;
            AtomicYml.save(var4, var22, 1347473142);
        } catch (Exception var34) {
            Logger var18 = this.plugin.getLogger();
            String var25 = "Failed to save honored.yml: " + var34.getMessage();
            var18.warning(var25);
        }
    }
}
